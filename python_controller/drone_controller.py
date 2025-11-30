#!/usr/bin/env python3
"""
Controller Python per Drone Nova Cam tramite ESP32 Bridge

Invia comandi al drone attraverso l'ESP32 connesso via USB.
L'ESP32 inoltra i comandi UDP al drone.

Utilizzo:
    python drone_controller.py COM3  # Windows
    python drone_controller.py /dev/ttyUSB0  # Linux
"""

import serial
import time
import sys
import threading
from typing import List, Optional

class DroneController:
    """Controller per drone Nova Cam via ESP32 bridge"""
    
    # Comandi base
    CMD_HEARTBEAT = [1, 1]
    CMD_STOP_CONTROL = [8, 1]
    CMD_SWITCH_CAM_FRONT = [6, 1]
    CMD_SWITCH_CAM_BACK = [6, 2]
    CMD_PHOTO_ACK = [9, 1]
    CMD_VIDEO_ACK = [9, 2]
    
    # Valori di controllo volo
    NEUTRAL = 128
    MIN_VALUE = 1
    MAX_VALUE = 255
    
    def __init__(self, port: str, baudrate: int = 115200, debug: bool = True):
        """
        Inizializza il controller
        
        Args:
            port: Porta seriale (es. 'COM3' o '/dev/ttyUSB0')
            baudrate: Velocità seriale (default 115200)
            debug: Stampa messaggi di debug
        """
        self.port = port
        self.baudrate = baudrate
        self.debug = debug
        self.serial: Optional[serial.Serial] = None
        self.running = False
        self.read_thread: Optional[threading.Thread] = None
        
    def connect(self) -> bool:
        """Connette alla porta seriale"""
        try:
            self.serial = serial.Serial(
                port=self.port,
                baudrate=self.baudrate,
                timeout=1
            )
            time.sleep(2)  # Attendi reset ESP32
            
            if self.debug:
                print(f"✓ Connesso a {self.port}")
            
            # Avvia thread di lettura
            self.running = True
            self.read_thread = threading.Thread(target=self._read_loop, daemon=True)
            self.read_thread.start()
            
            # Richiedi info
            time.sleep(0.5)
            self.send_text_command('I')
            
            return True
            
        except Exception as e:
            print(f"✗ Errore connessione: {e}")
            return False
    
    def disconnect(self):
        """Disconnette dalla porta seriale"""
        self.running = False
        if self.read_thread:
            self.read_thread.join(timeout=2)
        if self.serial and self.serial.is_open:
            self.serial.close()
        if self.debug:
            print("Disconnesso")
    
    def _read_loop(self):
        """Thread di lettura continua dalla seriale"""
        while self.running:
            try:
                if self.serial and self.serial.in_waiting:
                    line = self.serial.readline().decode('utf-8', errors='ignore').strip()
                    if line:
                        print(f"ESP32: {line}")
            except Exception as e:
                if self.debug:
                    print(f"Errore lettura: {e}")
            time.sleep(0.01)
    
    def send_command(self, data: List[int]) -> bool:
        """
        Invia comando binario al drone
        
        Args:
            data: Lista di byte da inviare
            
        Returns:
            True se inviato con successo
        """
        if not self.serial or not self.serial.is_open:
            print("Errore: Porta seriale non aperta")
            return False
        
        try:
            # Protocollo: [LUNGHEZZA][DATI...]
            packet = bytes([len(data)] + data)
            self.serial.write(packet)
            
            if self.debug:
                hex_str = ' '.join(f'{b:02X}' for b in data)
                print(f"→ Inviato[{len(data)}]: {hex_str}")
            
            return True
            
        except Exception as e:
            print(f"Errore invio: {e}")
            return False
    
    def send_text_command(self, cmd: str) -> bool:
        """
        Invia comando testuale (per comandi rapidi)
        
        Args:
            cmd: Carattere comando ('H', 'S', 'C', 'I', 'A')
        """
        if not self.serial or not self.serial.is_open:
            return False
        
        try:
            self.serial.write(cmd.encode())
            return True
        except Exception as e:
            print(f"Errore invio testo: {e}")
            return False
    
    def heartbeat(self):
        """Invia heartbeat"""
        return self.send_command(self.CMD_HEARTBEAT)
    
    def stop_control(self):
        """Ferma la modalità controllo"""
        return self.send_command(self.CMD_STOP_CONTROL)
    
    def switch_camera(self):
        """Cambia camera (front/back)"""
        return self.send_text_command('C')
    
    def toggle_auto_heartbeat(self):
        """Attiva/disattiva heartbeat automatico dell'ESP32"""
        return self.send_text_command('A')
    
    def get_info(self):
        """Richiede informazioni di sistema"""
        return self.send_text_command('I')
    
    def create_fly_command(
        self,
        horizontal: int = NEUTRAL,  # Movimento laterale (1-255)
        vertical: int = NEUTRAL,    # Movimento avanti/indietro (1-255)
        throttle: int = NEUTRAL,    # Accelerazione/quota (1-255)
        turn: int = NEUTRAL,        # Rotazione (1-255)
        mode_flags: int = 0,        # Flag modalità
        action_flags: int = 0       # Flag azioni
    ) -> List[int]:
        """
        Crea un comando di volo a 8 byte
        
        Args:
            horizontal: Movimento orizzontale (1-255, 128=neutro)
            vertical: Movimento verticale (1-255, 128=neutro)
            throttle: Controllo quota (1-255, 128=hover, >128=su, <128=giù)
            turn: Rotazione (1-255, 128=neutro)
            mode_flags: Bit flags modalità
            action_flags: Bit flags azioni
            
        Returns:
            Lista di 8 byte
        """
        # Clamp valori
        horizontal = max(self.MIN_VALUE, min(self.MAX_VALUE, horizontal))
        vertical = max(self.MIN_VALUE, min(self.MAX_VALUE, vertical))
        throttle = max(self.MIN_VALUE, min(self.MAX_VALUE, throttle))
        turn = max(self.MIN_VALUE, min(self.MAX_VALUE, turn))
        
        return [
            102,          # Byte di inizio
            horizontal,   # Movimento laterale
            vertical,     # Movimento avanti/indietro
            throttle,     # Controllo quota
            turn,         # Rotazione
            mode_flags,   # Flag modalità
            action_flags, # Flag azioni
            153           # Byte di fine
        ]
    
    def hover(self):
        """Mantieni posizione hover"""
        cmd = self.create_fly_command()
        return self.send_command(cmd)
    
    def takeoff(self, duration: float = 2.0, power: int = 180):
        """
        Simulazione decollo
        
        Args:
            duration: Durata comando in secondi
            power: Potenza throttle (128-255)
        """
        print(f"Decollo per {duration}s...")
        cmd = self.create_fly_command(throttle=power)
        
        start = time.time()
        while time.time() - start < duration:
            self.send_command(cmd)
            time.sleep(0.05)  # 50ms = 20Hz
        
        # Torna a hover
        self.hover()
        print("Hover")
    
    def land(self, duration: float = 2.0, power: int = 80):
        """
        Simulazione atterraggio
        
        Args:
            duration: Durata comando in secondi
            power: Potenza throttle (1-128)
        """
        print(f"Atterraggio per {duration}s...")
        cmd = self.create_fly_command(throttle=power)
        
        start = time.time()
        while time.time() - start < duration:
            self.send_command(cmd)
            time.sleep(0.05)
        
        # Stop controllo
        self.stop_control()
        print("Atterrato")
    
    def move(self, direction: str, power: int = 160, duration: float = 1.0):
        """
        Movimento in una direzione
        
        Args:
            direction: 'forward', 'backward', 'left', 'right', 'up', 'down', 
                      'rotate_left', 'rotate_right'
            power: Intensità movimento (128-255 per positivo, 1-128 per negativo)
            duration: Durata in secondi
        """
        cmd_map = {
            'forward': lambda: self.create_fly_command(vertical=power),
            'backward': lambda: self.create_fly_command(vertical=256-power),
            'left': lambda: self.create_fly_command(horizontal=256-power),
            'right': lambda: self.create_fly_command(horizontal=power),
            'up': lambda: self.create_fly_command(throttle=power),
            'down': lambda: self.create_fly_command(throttle=256-power),
            'rotate_left': lambda: self.create_fly_command(turn=256-power),
            'rotate_right': lambda: self.create_fly_command(turn=power),
        }
        
        if direction not in cmd_map:
            print(f"Direzione non valida: {direction}")
            return
        
        print(f"Movimento {direction} per {duration}s...")
        cmd = cmd_map[direction]()
        
        start = time.time()
        while time.time() - start < duration:
            self.send_command(cmd)
            time.sleep(0.05)
        
        self.hover()


def interactive_mode(controller: DroneController):
    """Modalità interattiva con menu"""
    print("\n" + "="*50)
    print("DRONE CONTROLLER - Modalità Interattiva")
    print("="*50)
    print("\nComandi disponibili:")
    print("  h - Heartbeat manuale")
    print("  i - Info sistema")
    print("  a - Toggle auto-heartbeat")
    print("  c - Cambia camera")
    print("  s - Stop controllo")
    print("  t - Decollo (simulato)")
    print("  l - Atterraggio (simulato)")
    print("  v - Hover")
    print("  w/a/s/d - Avanti/Sinistra/Indietro/Destra")
    print("  q/e - Ruota sinistra/destra")
    print("  u/j - Su/Giù")
    print("  x - Esci")
    print("\nPremi un tasto seguito da INVIO:\n")
    
    try:
        while True:
            cmd = input("> ").strip().lower()
            
            if not cmd:
                continue
            
            if cmd == 'x':
                print("Uscita...")
                break
            elif cmd == 'h':
                controller.heartbeat()
            elif cmd == 'i':
                controller.get_info()
            elif cmd == 'a':
                controller.toggle_auto_heartbeat()
            elif cmd == 'c':
                controller.switch_camera()
            elif cmd == 's':
                controller.stop_control()
            elif cmd == 't':
                controller.takeoff(duration=2.0)
            elif cmd == 'l':
                controller.land(duration=2.0)
            elif cmd == 'v':
                controller.hover()
            elif cmd == 'w':
                controller.move('forward', duration=1.0)
            elif cmd == 's':
                controller.move('backward', duration=1.0)
            elif cmd == 'a':
                controller.move('left', duration=1.0)
            elif cmd == 'd':
                controller.move('right', duration=1.0)
            elif cmd == 'q':
                controller.move('rotate_left', duration=1.0)
            elif cmd == 'e':
                controller.move('rotate_right', duration=1.0)
            elif cmd == 'u':
                controller.move('up', duration=1.0)
            elif cmd == 'j':
                controller.move('down', duration=1.0)
            else:
                print(f"Comando non riconosciuto: {cmd}")
            
            time.sleep(0.1)
    
    except KeyboardInterrupt:
        print("\n\nInterrotto dall'utente")


def main():
    """Funzione principale"""
    if len(sys.argv) < 2:
        print("Utilizzo: python drone_controller.py <PORTA_SERIALE>")
        print("Esempio Windows: python drone_controller.py COM3")
        print("Esempio Linux: python drone_controller.py /dev/ttyUSB0")
        sys.exit(1)
    
    port = sys.argv[1]
    
    # Crea controller
    controller = DroneController(port, debug=True)
    
    # Connetti
    if not controller.connect():
        print("Impossibile connettersi")
        sys.exit(1)
    
    try:
        # Modalità interattiva
        interactive_mode(controller)
    
    finally:
        # Cleanup
        controller.stop_control()
        controller.disconnect()
        print("Controller terminato")


if __name__ == "__main__":
    main()
