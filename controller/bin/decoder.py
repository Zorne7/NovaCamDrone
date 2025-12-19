import sys
import struct
import cv2
import numpy as np

def read_exactly(n):
    """Legge esattamente n byte da stdin"""
    buf = b""
    while len(buf) < n:
        chunk = sys.stdin.buffer.read(n - len(buf))
        if not chunk:
            return None
        buf += chunk
    return buf

while True:
    # Leggi la lunghezza del pacchetto (4 byte, unsigned int)
    length_bytes = read_exactly(4)
    if not length_bytes:
        break  # fine input
    length = struct.unpack("!I", length_bytes)[0]

    # Leggi il pacchetto video
    data = read_exactly(length)
    if not data:
        break

    # Decodifica con OpenCV (assumendo H.264 NAL units)
    frame = cv2.imdecode(np.frombuffer(data, np.uint8), cv2.IMREAD_COLOR)
    if frame is None:
        continue

    # Codifica il frame in PNG
    _, buf = cv2.imencode(".png", frame)

    # Scrivi la lunghezza + dati su stdout
    sys.stdout.buffer.write(struct.pack("!I", len(buf)))
    sys.stdout.buffer.write(buf.tobytes())
    sys.stdout.flush()