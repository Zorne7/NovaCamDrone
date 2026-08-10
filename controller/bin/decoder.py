#!/usr/bin/env python3
import sys
import struct

RTP_HEADER_SIZE = 12
expected_seq = None

fu_buffer = bytearray()
is_assembling_fu = False

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
    if not payload:
        return None

    if payload[:2] == b"\xFF\xD8":
        return bytes(payload)

    start = payload.find(b"\xFF\xD8")
    end = payload.rfind(b"\xFF\xD9")
    if start != -1 and end != -1 and end >= start:
        return bytes(payload[start:end + 2])

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
    global expected_seq, fu_buffer, is_assembling_fu

    seq, marker = parse_rtp_header(packet)
    if seq is None:
        log_error("Invalid RTP packet")
        return

    if expected_seq is not None and seq != expected_seq:
        log_error(f"Sequence mismatch: expected {expected_seq}, got {seq}")
        fu_buffer = bytearray()
        is_assembling_fu = False

    expected_seq = (seq + 1) & 0xFFFF
    payload = packet[RTP_HEADER_SIZE:]

    frame_data = process_rtp_mjpeg(payload)
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
