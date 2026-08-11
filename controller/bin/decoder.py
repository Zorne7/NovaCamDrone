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


def looks_like_h264(data):
    if not data:
        return False
    if data.startswith(b"\x00\x00\x00\x01") or b"\x00\x00\x00\x01" in data:
        return True
    if len(data) >= 2 and data[:2] == b"\x00\x00":
        return True
    return any((byte & 0x1F) in (1, 5, 7, 8, 9, 24, 28) for byte in data[:64])


def normalize_h264_stream(data):
    if not data:
        return b""
    if data.startswith(b"\x00\x00\x00\x01"):
        return data

    # Some Nova Cam payloads are length-delimited NAL chunks instead of raw Annex-B.
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
            log_error(proc.stderr.decode("utf-8", "replace").strip() or "H.264 decode failed")
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


def process_rtp_h264(payload):
    global h264_buffer

    if not payload:
        return None

    # Avoid decoding every partial packet. A valid H.264 access unit must contain
    # a keyframe (IDR) plus the SPS/PPS metadata needed by ffmpeg.
    if looks_like_h264(payload):
        h264_buffer.extend(normalize_h264_stream(payload))
    else:
        nal_type = payload[0] & 0x1F
        if nal_type == 28 and len(payload) >= 2:
            fu_indicator = payload[0]
            fu_header = payload[1]
            is_start = (fu_header >> 7) & 0x1
            if is_start:
                reconstructed = bytearray()
                reconstructed.append((fu_indicator & 0xE0) | (fu_header & 0x1F))
                reconstructed.extend(payload[2:])
                h264_buffer.extend(b"\x00\x00\x00\x01")
                h264_buffer.extend(reconstructed)
            else:
                h264_buffer.extend(payload[2:])
        elif nal_type == 24 and len(payload) >= 2:
            offset = 1
            while offset + 2 <= len(payload):
                nal_size = struct.unpack("!H", payload[offset:offset + 2])[0]
                offset += 2
                if offset + nal_size > len(payload):
                    break
                h264_buffer.extend(b"\x00\x00\x00\x01")
                h264_buffer.extend(payload[offset:offset + nal_size])
                offset += nal_size
        else:
            h264_buffer.extend(b"\x00\x00\x00\x01")
            h264_buffer.extend(payload)

    if b"\x00\x00\x00\x01\x65" not in h264_buffer and b"\x00\x00\x00\x01\x67" not in h264_buffer:
        return None

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

    seq, marker = parse_rtp_header(packet)
    if seq is None:
        log_error("Invalid RTP packet")
        return

    if expected_seq is not None and seq != expected_seq:
        log_error(f"Sequence mismatch: expected {expected_seq}, got {seq}")
        fu_buffer = bytearray()
        is_assembling_fu = False
        mjpeg_buffer = bytearray()
        h264_buffer = bytearray()

    expected_seq = (seq + 1) & 0xFFFF
    payload = packet[RTP_HEADER_SIZE:]

    if not payload:
        return

    if payload.startswith(b"\x00\x00\x00\x01") or b"\x00\x00\x00\x01" in payload:
        frame_data = process_rtp_h264(payload)
    elif payload.startswith(b"\xFF\xD8") or payload.find(b"\xFF\xD8") >= 0:
        frame_data = process_rtp_mjpeg(payload)
    elif payload[0] & 0x1F in (1, 5, 7, 28, 24):
        frame_data = process_rtp_h264(payload)
    else:
        frame_data = process_rtp_mjpeg(payload)
        if frame_data is None:
            frame_data = process_rtp_h264(payload)
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
