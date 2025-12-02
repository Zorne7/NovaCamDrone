#!/usr/bin/env python3
"""
Monitor ESP32 - Visualizza tutti i log dell'ESP32 in tempo reale

Usa questo script per diagnosticare problemi di comunicazione
senza il rumore dei comandi del drone controller.

Utilizzo: python monitor_esp32.py COM3
"""

import serial
import sys
import time
from datetime import datetime


def main():
    if len(sys.argv) < 2:
        print("❌ Uso: python monitor_esp32.py <PORTA>")
        print("Esempio: python monitor_esp32.py COM3")
        sys.exit(1)
    
    port = sys.argv[1]
    
    print("\n" + "="*70)
    print("📡 MONITOR ESP32 - Log in Tempo Reale")
    print("="*70)
    print(f"Porta: {port}")
    print(f"Baudrate: 115200")
    print("\nPremi Ctrl+C per uscire\n")
    print("="*70 + "\n")
    
    try:
        ser = serial.Serial(port, 115200, timeout=1)
        time.sleep(2)  # Attendi reset ESP32
        
        print("✅ Connesso! Ricezione log...\n")
        
        # Statistiche
        lines_count = 0
        tx_count = 0
        rx_count = 0
        
        while True:
            if ser.in_waiting:
                line = ser.readline().decode('utf-8', errors='ignore').strip()
                if line:
                    lines_count += 1
                    timestamp = datetime.now().strftime("%H:%M:%S.%f")[:-3]
                    
                    # Colorazione e conteggi
                    if "TX[" in line:
                        tx_count += 1
                        print(f"[{timestamp}] 📤 {line}")
                    elif "RX[" in line:
                        rx_count += 1
                        print(f"[{timestamp}] 📥 {line}")
                    elif "WiFi Connected" in line or "✓" in line:
                        print(f"[{timestamp}] ✅ {line}")
                    elif "ERROR" in line or "Failed" in line or "✗" in line:
                        print(f"[{timestamp}] ❌ {line}")
                    elif "Resolution" in line:
                        # Resolutions ripetitive - comprimi
                        if lines_count % 10 == 1:  # Mostra 1 ogni 10
                            print(f"[{timestamp}] 🔁 {line} (x10)")
                    else:
                        print(f"[{timestamp}] ℹ️  {line}")
                    
                    # Statistiche ogni 50 linee
                    if lines_count % 50 == 0:
                        print(f"\n📊 Stats: {lines_count} linee | TX: {tx_count} | RX: {rx_count}\n")
            
            time.sleep(0.01)
    
    except serial.SerialException as e:
        print(f"\n❌ Errore seriale: {e}")
        print("\n💡 Verifica:")
        print("   1. ESP32 collegato via USB")
        print("   2. Driver installati")
        print("   3. Porta corretta (vedi Device Manager)")
        print("   4. Nessun altro programma usa la porta")
        sys.exit(1)
    
    except KeyboardInterrupt:
        print(f"\n\n📊 STATISTICHE FINALI")
        print("="*70)
        print(f"Linee totali: {lines_count}")
        print(f"Comandi inviati (TX): {tx_count}")
        print(f"Risposte ricevute (RX): {rx_count}")
        print("="*70)
        print("\n👋 Monitor terminato")
        ser.close()


if __name__ == "__main__":
    main()
