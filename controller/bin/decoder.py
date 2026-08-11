#!/usr/bin/env python3
import shutil
import struct
import subprocess
import sys

RTP_HEADER_SIZE = 12
expected_seq = None

fu_buffer = bytearray()
is_assembling_fu = False
mjpeg_buffer = bytearray()
h264_buffer = bytearray()


def is_jpeg(data):
    return bool(data) and data.startswith(b"\xFF\xD8") and b"\xFF\xD9" in data


def looks_like_rtp(packet):
    if len(packet) < 12:
        return False
    version = (packet[0] >> 6) & 0x03
    payload_type = packet[1] & 0x7F
    return version == 2 and payload_type in (96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 120, 127)


def looks_like_h264(data):
    if not data:
        return False
    if is_jpeg(data):
        return False
    if data.startswith(b"\x00\x00\x00\x01") or b"\x00\x00\x00\x01" in data:
        return True
    if len(data) >= 2 and data[:2] == b"\x00\x00":
        return True
    if len(data) >= 2 and data[:2] in (b"\x00\x01", b"\x01\x00"):
        return True
    first_bytes = data[:64]
    # JPEG starts with 0xFF 0xD8 (SOI) and can falsely look like a NAL type 24
    # when scanning raw bytes, so we reject SOI before analyzing the NAL type.
    return any((b & 0x1F) in (1, 5, 6, 7, 8, 9, 24, 28) for b in first_bytes if b != 0xFF)


def normalize_h264_stream(data):
    if not data:
        return b""

    if data.startswith(b"\x00\x00\x00\x01"):
        return data

    if len(data) >= 4 and len(data) >= struct.unpack(">I", data[:4])[0] + 4:
        stream = bytearray()
        offset = 0
        while offset + 4 <= len(data):
            nal_len = struct.unpack(">I", data[offset:offset + 4])[0]
            offset += 4
            if nal_len == 0 or offset + nal_len > len(data):
                break
            stream.extend(b"\x00\x00\x00\x01")
            stream.extend(data[offset:offset + nal_len])
            offset += nal_len
        if stream:
            return bytes(stream)

    if b"\x00\x00\x00\x01" in data:
        return data

    return data


def add_h264_start_code(data):
    if not data:
        return b""
    if data.startswith(b"\x00\x00\x00\x01"):
        return data
    return b"\x00\x00\x00\x01" + data


def extract_complete_h264_frame(stream):
    if not stream:
        return None

    if b"\x00\x00\x00\x01" not in stream:
        return None

    start_positions = []
    cursor = 0
    while True:
        idx = stream.find(b"\x00\x00\x00\x01", cursor)
        if idx == -1:
            break
        start_positions.append(idx)
        cursor = idx + 1

    if not start_positions:
        return None

    # We only keep the last complete NAL from the current buffer; this avoids
    # feeding ffmpeg half-assembled access units.
    last_start = start_positions[-1]
    if last_start == 0:
        return bytes(stream)

    return bytes(stream[last_start:])


def decode_h264_to_jpeg(h264_bytes):
    if not h264_bytes:
        return None

    normalized = normalize_h264_stream(h264_bytes)
    ffmpeg = shutil.which("ffmpeg")
    if not ffmpeg:
        log_error("ffmpeg not found: cannot decode H.264 RTP payload to JPEG")
        return None

    try:
        proc = subprocess.run(
            [
                ffmpeg,
                "-hide_banner",
                "-loglevel",
                "error",
                "-f",
                "h264",
                "-i",
                "pipe:0",
                "-frames:v",
                "1",
                "-an",
                "-f",
                "mjpeg",
                "pipe:1",
            ],
            input=normalized,
            capture_output=True,
            check=False,
        )
        if proc.returncode != 0:
            stderr = proc.stderr.decode("utf-8", "replace").strip()
            if stderr:
                log_error(stderr)
            return None

        out = proc.stdout
        if is_jpeg(out):
            return out
    except Exception as exc:  # pragma: no cover - runtime failure path
        log_error(f"H.264 decode error: {exc}")
    return None


def log_error(msg):
    sys.stderr.write(msg + "\n")
    sys.stderr.flush()


def send_frame(frame_bytes):
    header = struct.pack(">I", len(frame_bytes))
    sys.stdout.buffer.write(header + frame_bytes)
    sys.stdout.buffer.flush()


def parse_rtp_header(packet):
    if len(packet) < RTP_HEADER_SIZE:
        return None, None
    seq = struct.unpack("!H", packet[2:4])[0]
    marker = (packet[1] >> 7) & 1
    return seq, marker


def process_rtp_mjpeg(payload):
    global mjpeg_buffer

    if not payload:
        return None

    start_of_image = payload.find(b"\xFF\xD8")
    if start_of_image != -1:
        payload = payload[start_of_image:]
    elif mjpeg_buffer:
        mjpeg_buffer.extend(payload)
        end_of_image = mjpeg_buffer.rfind(b"\xFF\xD9")
        if end_of_image == -1:
            return None
        frame = bytes(mjpeg_buffer[: end_of_image + 2])
        mjpeg_buffer.clear()
        return frame
    else:
        return None

    mjpeg_buffer.extend(payload)
    end_of_image = mjpeg_buffer.rfind(b"\xFF\xD9")
    if end_of_image == -1:
        return None

    frame = bytes(mjpeg_buffer[: end_of_image + 2])
    mjpeg_buffer.clear()
    return frame


def append_h264_nal(payload):
    global h264_buffer

    if not payload:
        return None

    nal_type = payload[0] & 0x1F
    if nal_type == 28 and len(payload) >= 2:
        fu_indicator = payload[0]
        fu_header = payload[1]
        is_start = (fu_header >> 7) & 0x1
        nalu_type = fu_header & 0x1F
        reconstructed = bytearray()
        reconstructed.append((fu_indicator & 0xE0) | nalu_type)
        reconstructed.extend(payload[2:])
        if is_start:
            h264_buffer.extend(b"\x00\x00\x00\x01")
            h264_buffer.extend(reconstructed)
        else:
            h264_buffer.extend(reconstructed)
        return None

    if nal_type == 24 and len(payload) >= 2:
        offset = 1
        while offset + 2 <= len(payload):
            nal_size = struct.unpack("!H", payload[offset:offset + 2])[0]
            offset += 2
            if offset + nal_size > len(payload):
                break
            if nal_size > 0:
                h264_buffer.extend(b"\x00\x00\x00\x01")
                h264_buffer.extend(payload[offset:offset + nal_size])
            offset += nal_size
        return None

    if nal_type in (1, 5, 6, 7, 8, 9):
        h264_buffer.extend(add_h264_start_code(payload))
        return None

    if looks_like_h264(payload):
        h264_buffer.extend(add_h264_start_code(payload))
        return None

    h264_buffer.extend(add_h264_start_code(payload))
    return None


def process_rtp_h264(payload, marker=False):
    global h264_buffer

    if not payload:
        return None

    nal_type = payload[0] & 0x1F

    if nal_type == 28 and len(payload) >= 2:
        fu_indicator = payload[0]
        fu_header = payload[1]
        fu_type = fu_header & 0x1F
        is_start = (fu_header >> 7) & 0x1
        is_end = (fu_header >> 6) & 0x1

        if is_start:
            h264_buffer.extend(b"\x00\x00\x00\x01")
            h264_buffer.append((fu_indicator & 0xE0) | fu_type)
            h264_buffer.extend(payload[2:])
        else:
            h264_buffer.extend(payload[2:])

        if is_end or marker:
            frame = decode_h264_to_jpeg(bytes(h264_buffer))
            if frame:
                h264_buffer.clear()
                return frame
        return None

    if nal_type == 24 and len(payload) >= 2:
        offset = 1
        while offset + 2 <= len(payload):
            nal_size = struct.unpack("!H", payload[offset:offset + 2])[0]
            offset += 2
            if offset + nal_size > len(payload):
                break
            h264_buffer.extend(b"\x00\x00\x00\x01")
            h264_buffer.extend(payload[offset:offset + nal_size])
            offset += nal_size
        if marker:
            frame = decode_h264_to_jpeg(bytes(h264_buffer))
            if frame:
                h264_buffer.clear()
                return frame
        return None

    if nal_type in (1, 5, 6, 7, 8, 9, 14, 15, 17, 18, 19, 20, 21, 22, 23):
        h264_buffer.extend(b"\x00\x00\x00\x01")
        h264_buffer.extend(payload)
        if marker:
            frame = decode_h264_to_jpeg(bytes(h264_buffer))
            if frame:
                h264_buffer.clear()
                return frame
        return None

    if looks_like_h264(payload):
        h264_buffer.extend(add_h264_start_code(payload))
        if marker:
            frame = decode_h264_to_jpeg(bytes(h264_buffer))
            if frame:
                h264_buffer.clear()
                return frame
        return None

    h264_buffer.extend(add_h264_start_code(payload))
    if marker:
        frame = decode_h264_to_jpeg(bytes(h264_buffer))
        if frame:
            h264_buffer.clear()
            return frame
    return None


def process_rtp_hevc(payload):
    global fu_buffer, is_assembling_fu

    if len(payload) < 2:
        log_error("Payload RTP troppo corto")
        return None

    nal_type = (payload[0] >> 1) & 0x3F
    out = bytearray()

    if nal_type == 49:
        if len(payload) < 3:
            return None

        fu_header = payload[2]
        s_bit = (fu_header >> 7) & 1
        e_bit = (fu_header >> 6) & 1
        fu_type = fu_header & 0x3F

        nal_header_1 = (fu_type << 1) | (payload[0] & 0x01)
        nal_header_2 = payload[1]

        if s_bit == 1:
            fu_buffer = bytearray([nal_header_1, nal_header_2])
            fu_buffer.extend(payload[3:])
            is_assembling_fu = True
        elif is_assembling_fu:
            fu_buffer.extend(payload[3:])
            if e_bit == 1:
                out.extend(b"\x00\x00\x00\x01")
                out.extend(fu_buffer)
                fu_buffer = bytearray()
                is_assembling_fu = False

    elif 0 <= nal_type <= 47:
        is_assembling_fu = False
        out.extend(b"\x00\x00\x00\x01")
        out.extend(payload)

    elif nal_type == 48:
        is_assembling_fu = False
        offset = 2
        while offset < len(payload):
            if offset + 2 > len(payload):
                break
            nal_size = struct.unpack("!H", payload[offset:offset + 2])[0]
            offset += 2
            if offset + nal_size > len(payload):
                break
            out.extend(b"\x00\x00\x00\x01")
            out.extend(payload[offset:offset + nal_size])
            offset += nal_size

    return bytes(out) if out else None


def process_packet(packet):
    global expected_seq, fu_buffer, is_assembling_fu, mjpeg_buffer, h264_buffer

    if not packet:
        return

    if is_jpeg(packet):
        send_frame(packet)
        return

    if not looks_like_rtp(packet):
        return

    seq, marker = parse_rtp_header(packet)
    if seq is None:
        log_error("Invalid RTP packet")
        return

    if expected_seq is not None:
        gap = (seq - expected_seq) & 0xFFFF
        if gap > 1 and gap < 0x8000:
            log_error(f"Sequence gap: expected {expected_seq}, got {seq}; continuing")

    expected_seq = (seq + 1) & 0xFFFF
    payload = packet[RTP_HEADER_SIZE:]

    if not payload:
        return

    if payload.startswith(b"\x00\x00\x00\x01") or b"\x00\x00\x00\x01" in payload:
        frame_data = process_rtp_h264(payload, marker=bool(marker))
    elif payload.startswith(b"\xFF\xD8") or payload.find(b"\xFF\xD8") >= 0:
        frame_data = process_rtp_mjpeg(payload)
    elif payload[0] & 0x1F in (1, 5, 6, 7, 8, 9, 24, 28):
        frame_data = process_rtp_h264(payload, marker=bool(marker))
    else:
        frame_data = process_rtp_mjpeg(payload)
        if frame_data is None:
            frame_data = process_rtp_h264(payload, marker=bool(marker))
        if frame_data is None:
            frame_data = process_rtp_hevc(payload)

    if frame_data:
        send_frame(frame_data)


def read_exact(n):
    data = bytearray()
    while len(data) < n:
        chunk = sys.stdin.buffer.read(n - len(data))
        if not chunk:
            return None
        data.extend(chunk)
    return data


def main():
    while True:
        length_bytes = read_exact(4)
        if length_bytes is None:
            break

        packet_len = struct.unpack(">I", length_bytes)[0]
        packet = read_exact(packet_len)
        if packet is None:
            log_error("Unexpected EOF")
            break

        process_packet(packet)


if __name__ == "__main__":
    main()
