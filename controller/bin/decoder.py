#!/usr/bin/env python3
import sys
import struct

RTP_HEADER_SIZE = 12
buffer = bytearray()
expected_seq = None

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

# ------------------------------------------------------------
#   CONVERSIONE HVCC → ANNEX-B (replica della funzione C)
# ------------------------------------------------------------

def convert_hvcc_to_annexb(data):
    """
    Converte un blob HVCC-like (hvcC box) in un frame HEVC Annex-B.
    Restituisce bytes pronti per il decoder.
    """

    if len(data) < 0x17:
        log_error("HVCC metadata too small")
        return None

    # NAL length size (1..4)
    length_size = (data[0x15] & 3) + 1

    num_arrays = data[0x16]
    offset = 0x17

    out = bytearray()

    for _ in range(num_arrays):
        if offset + 3 > len(data):
            log_error("Invalid HVCC array header")
            return None

        nal_type = data[offset] & 0x3F
        num_nalus = (data[offset+1] << 8) | data[offset+2]
        offset += 3

        for _ in range(num_nalus):
            if offset + length_size > len(data):
                log_error("Invalid NAL length")
                return None

            # Legge la lunghezza del NAL
            nal_len = int.from_bytes(data[offset:offset+length_size], "big")
            offset += length_size

            if offset + nal_len > len(data):
                log_error("NAL size mismatch")
                return None

            nal_payload = data[offset:offset+nal_len]
            offset += nal_len

            # Scrive start code + payload
            out.extend(b"\x00\x00\x00\x01")
            out.extend(nal_payload)

    return bytes(out)

# ------------------------------------------------------------
#   PROCESSAMENTO RTP
# ------------------------------------------------------------

def process_packet(packet):
    global buffer, expected_seq

    seq, marker = parse_rtp_header(packet)
    if seq is None:
        log_error("Invalid RTP packet")
        return

    payload = packet[RTP_HEADER_SIZE:]

    # Sequence check
    if expected_seq is not None and seq != expected_seq:
        log_error(f"Sequence mismatch: expected {expected_seq}, got {seq}")
        buffer = bytearray()

    expected_seq = (seq + 1) & 0xFFFF

    # Accumula payload
    buffer.extend(payload)

    # Marker RTP = fine batch
    if marker == 1:
        frame = convert_hvcc_to_annexb(buffer)

        if frame is None:
            log_error("Failed to convert HVCC to Annex-B")
        else:
            send_frame(frame)

        buffer = bytearray()
        expected_seq = None

# ------------------------------------------------------------
#   IO
# ------------------------------------------------------------

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