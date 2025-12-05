#!/usr/bin/env python3
"""
Test Drone Flight - ESP32 Serial Bridge con controllo ACK
"""

import sys
import time
import serial
import struct


RESP_TYPES = {
    0x00: "AckKo",
    0x01: "AckOk",
    0x02: "Feedback",
    0x03: "DroneData"
}


class ESP32DroneBridge:
    def __init__(self, port: str, baudrate: int = 921600, debug: bool = True):
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

    def read_response(self):
        if self.ser:
            resp_type = self.ser.read(1)
            if not resp_type:
                return None
            resp_type = resp_type[0]
            data = None
            if resp_type == 0x02:
                data = self.ser.read(1)
            elif resp_type == 0x03:
                data = self.ser.read(self.ser.read(1)[0])
            resp = RESP_TYPES.get(resp_type, f"Unknown({resp_type})"), data
            print(f"[RESP] {resp[0]} - Data: {resp[1].hex() if resp[1] else 'None'}")
            return resp
        return None

    def check_ack(self):
        resp = self.read_response()
        while not resp or resp[0] not in ("AckOk", "AckKo"):
            resp = self.read_response()
        return resp[0] == "AckOk"

    def set_fly_params(self, horizontal=128, vertical=128,
                       throttle=128, rotation=128, flags=0):
        params = struct.pack("BBBBB", horizontal, vertical, throttle, rotation, flags)
        self.send(b'f' + params)
        return self.check_ack()

    def switch_cam(self, cam: int):
        self.send(b'c' + bytes([cam]))
        return self.check_ack()

    def heartbeat(self):
        self.send(b'h')
        return self.check_ack()

    def stop_control(self):
        self.send(b's')
        return self.check_ack()

    def enable_control(self):
        self.send(b'e')
        return self.check_ack()

    def conn_status(self):
        self.send(b'w')
        return self.read_response()

    def set_auto_hb(self, enable=True):
        self.send(b'b' + bytes([1 if enable else 0]))
        return self.check_ack()

    def send_ack(self, ack_type=1):
        self.send(b'a' + bytes([ack_type]))
        return self.check_ack()


def main():
    if len(sys.argv) < 2:
        print("Uso: python drone_test.py <PORTA_SERIALE>")
        print("Esempio: python drone_test.py COM3")
        sys.exit(1)

    port = sys.argv[1]
    drone = ESP32DroneBridge(port)

    if not drone.connect():
        print("✗ Connessione fallita")
        sys.exit(1)

    print("✓ Connessione seriale aperta")

    # --- Test sequenza completa ---
    print("\n=== Stato Connessione ===")
    drone.conn_status()

    print("\n=== Heartbeat ===")
    drone.heartbeat()

    print("\n=== Enable Control ===")
    drone.enable_control()

    print("\n=== Decollo ===")
    for i in range(10):
        drone.set_fly_params(throttle=200)
        time.sleep(0.1)

    print("\n=== Hover ===")
    for i in range(10):
        drone.set_fly_params(throttle=128)
        time.sleep(0.1)

    print("\n=== Switch Camera ===")
    drone.switch_cam(1)  # front
    time.sleep(0.5)
    drone.switch_cam(2)  # back

    print("\n=== Ack Photo ===")
    drone.send_ack(1)

    print("\n=== Ack Video ===")
    drone.send_ack(2)

    print("\n=== Stop Control ===")
    drone.stop_control()

    print("\n=== Disabilita Auto Heartbeat ===")
    drone.set_auto_hb(False)

    drone.disconnect()
    print("\n=== Fine test ===")


if __name__ == "__main__":
    main()