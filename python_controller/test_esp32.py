#!/usr/bin/env python3
"""
Test Script per ESP32 WiFi-Serial Bridge (Drone Nova Cam)
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
                print(f"[DEBUG] Inviato: {data}")

    def read(self, size: int = 256) -> bytes:
        if self.ser and self.ser.is_open:
            return self.ser.read(size)
        return b""

    # --- Comandi supportati dal firmware ESP32 ---
    def conn_status(self) -> bool:
        self.send(b'w')
        resp = self.read(1)
        return bool(resp and resp[0] == 1)

    def set_auto_heartbeat(self, enable: bool):
        self.send(b'a' + (b'\x01' if enable else b'\x00'))

    def heartbeat(self):
        self.send(b'h')

    def stop_control(self):
        self.send(b's')

    def switch_camera(self, front: bool = True):
        self.send(b'c' + (b'\x01' if front else b'\x00'))

    def set_fly_params(self, horizontal=128, vertical=128, throttle=128, rotation=128, flags=0):
        # Costruisce la struct FlyParams (5 byte)
        params = struct.pack("BBBBB", horizontal, vertical, throttle, rotation, flags)
        self.send(b'f' + params)


# --- Test suite ---
def run_tests(port: str):
    bridge = ESP32DroneBridge(port, debug=True)

    print("=== Apertura connessione seriale ===")
    if not bridge.connect():
        print("✗ Connessione fallita")
        return False
    print("✓ Connessione OK")

    # Test connessione WiFi
    print("\n=== Test Connessione WiFi ===")
    status = bridge.conn_status()
    print("Stato WiFi:", "Connesso" if status else "Non connesso")

    # Test heartbeat
    print("\n=== Test Heartbeat ===")
    for i in range(3):
        bridge.heartbeat()
        print(f"Heartbeat {i+1}/3 inviato")
        time.sleep(1)

    # Test cambio camera
    print("\n=== Test Cambio Camera ===")
    bridge.switch_camera(front=True)
    print("✓ Camera front")
    time.sleep(1)
    bridge.switch_camera(front=False)
    print("✓ Camera back")

    # Test fly params
    print("\n=== Test FlyParams ===")
    bridge.set_fly_params(horizontal=160)  # destra
    time.sleep(0.1)
    bridge.set_fly_params(horizontal=96)   # sinistra
    time.sleep(0.1)
    bridge.set_fly_params(vertical=160)    # avanti
    time.sleep(0.1)
    bridge.set_fly_params(vertical=96)     # indietro
    time.sleep(0.1)
    bridge.set_fly_params(throttle=160)    # su
    time.sleep(0.1)
    bridge.set_fly_params(throttle=96)     # giù
    time.sleep(0.1)
    print("✓ FlyParams inviati")

    # Test stop control
    print("\n=== Test Stop Control ===")
    bridge.stop_control()
    print("✓ Stop inviato")

    # Cleanup
    bridge.disconnect()
    print("\n=== Fine test ===")
    return True


def main():
    if len(sys.argv) < 2:
        print("Uso: python test_esp32.py <PORTA_SERIALE>")
        print("Esempio: python test_esp32.py COM3")
        sys.exit(1)

    port = sys.argv[1]
    success = run_tests(port)
    sys.exit(0 if success else 1)


if __name__ == "__main__":
    main()