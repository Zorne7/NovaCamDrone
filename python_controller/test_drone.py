#!/usr/bin/env python3
"""
Test Drone Flight - ESP32 Serial Bridge
"""

import sys
import time
import serial
import struct


class ESP32DroneBridge:
    def __init__(self, port: str, baudrate: int = 115200, debug: bool = True):
        self.port = port
        self.baudrate = baudrate
        self.debug = debug
        self.ser = None

    def connect(self) -> bool:
        try:
            self.ser = serial.Serial(self.port, self.baudrate, timeout=1)
            return True
        except Exception as e:
            print(f"Errore apertura seriale: {e}")
            return False

    def disconnect(self):
        if self.ser and self.ser.is_open:
            self.ser.close()

    def send(self, data: bytes):
        if self.ser and self.ser.is_open:
            self.ser.write(data)
            if self.debug:
                print(f"[DEBUG] Inviato: {data.hex()}")

    def set_fly_params(self, horizontal=128, vertical=128, throttle=128, rotation=128, flags=0):
        # Costruisce la struct FlyParams (5 byte)
        params = struct.pack("BBBBB", horizontal, vertical, throttle, rotation, flags)
        self.send(b'f' + params)


def main():
    if len(sys.argv) < 2:
        print("Uso: python fly_test.py <PORTA_SERIALE>")
        print("Esempio: python fly_test.py COM3")
        sys.exit(1)

    port = sys.argv[1]
    drone = ESP32DroneBridge(port)

    if not drone.connect():
        print("✗ Connessione fallita")
        sys.exit(1)

    print("✓ Connessione seriale aperta")

    # --- Test: far alzare il drone ---
    print("\n=== Decollo ===")
    # throttle > 128 → salita
    for i in range(20):  # invia per ~2 secondi (20 * 100ms)
        drone.set_fly_params(throttle=160)  # valore sopra neutro
        time.sleep(0.1)

    print("✓ Comando salita inviato")

    # --- Hover (mantieni posizione) ---
    print("\n=== Hover ===")
    for i in range(20):  # invia per ~2 secondi
        drone.set_fly_params(throttle=128)  # neutro
        time.sleep(0.1)

    print("✓ Hover inviato")

    # --- Stop ---
    print("\n=== Stop Control ===")
    drone.send(b's')
    print("✓ Stop inviato")

    drone.disconnect()
    print("\n=== Fine test ===")


if __name__ == "__main__":
    main()