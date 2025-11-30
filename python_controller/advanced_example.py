#!/usr/bin/env python3
"""
Esempio Avanzato - Sequenza di Volo Automatica

ATTENZIONE: Questo script farà volare il drone!
Usare solo in spazio aperto e sicuro.
"""

import sys
import time
from drone_controller import DroneController


def flight_sequence_demo(controller: DroneController):
    """Esegue una sequenza di volo dimostrativa"""
    
    print("\n" + "="*60)
    print("SEQUENZA VOLO AUTOMATICA")
    print("="*60)
    print("\nQuesta sequenza farà:")
    print("1. Decollo (2 sec)")
    print("2. Hover (2 sec)")
    print("3. Movimento avanti (1.5 sec)")
    print("4. Hover (1 sec)")
    print("5. Rotazione 360° (8 sec)")
    print("6. Hover (1 sec)")
    print("7. Movimento indietro (1.5 sec)")
    print("8. Atterraggio (2 sec)")
    print("\nTempo totale: ~19 secondi")
    print("\n⚠️  ASSICURATI CHE:")
    print("  - Ci sia spazio libero (almeno 5x5 metri)")
    print("  - Nessuna persona nelle vicinanze")
    print("  - Sei pronto a premere CTRL+C per emergenza")
    
    confirm = input("\nDigita 'SI' per continuare: ")
    if confirm.upper() != 'SI':
        print("Sequenza annullata")
        return
    
    print("\nInizio sequenza tra...")
    for i in range(3, 0, -1):
        print(f"{i}...")
        time.sleep(1)
    print("GO!\n")
    
    try:
        # 1. Decollo
        print("1️⃣  DECOLLO")
        controller.takeoff(duration=2.0, power=180)
        
        # 2. Hover iniziale
        print("2️⃣  HOVER INIZIALE")
        start = time.time()
        while time.time() - start < 2.0:
            controller.hover()
            time.sleep(0.05)
        
        # 3. Avanti
        print("3️⃣  MOVIMENTO AVANTI")
        controller.move('forward', power=160, duration=1.5)
        
        # 4. Hover
        print("4️⃣  HOVER")
        start = time.time()
        while time.time() - start < 1.0:
            controller.hover()
            time.sleep(0.05)
        
        # 5. Rotazione 360°
        print("5️⃣  ROTAZIONE 360°")
        controller.move('rotate_right', power=160, duration=8.0)
        
        # 6. Hover
        print("6️⃣  HOVER")
        start = time.time()
        while time.time() - start < 1.0:
            controller.hover()
            time.sleep(0.05)
        
        # 7. Indietro
        print("7️⃣  MOVIMENTO INDIETRO")
        controller.move('backward', power=160, duration=1.5)
        
        # 8. Atterraggio
        print("8️⃣  ATTERRAGGIO")
        controller.land(duration=2.0, power=80)
        
        print("\n✓ Sequenza completata con successo!")
    
    except KeyboardInterrupt:
        print("\n\n⚠️  EMERGENZA - Stop immediato!")
        controller.stop_control()
        print("Comando stop inviato")
        raise


def simple_test_flight(controller: DroneController):
    """Volo di test semplice"""
    
    print("\n" + "="*60)
    print("VOLO DI TEST SEMPLICE")
    print("="*60)
    print("\nSequenza:")
    print("1. Decollo hover per 3 secondi")
    print("2. Atterraggio")
    
    confirm = input("\nDigita 'SI' per continuare: ")
    if confirm.upper() != 'SI':
        print("Test annullato")
        return
    
    try:
        print("\n⏫ Decollo...")
        controller.takeoff(duration=1.5, power=170)
        
        print("🚁 Hover per 3 secondi...")
        start = time.time()
        while time.time() - start < 3.0:
            controller.hover()
            time.sleep(0.05)
        
        print("⏬ Atterraggio...")
        controller.land(duration=1.5, power=90)
        
        print("\n✓ Test completato!")
    
    except KeyboardInterrupt:
        print("\n\n⚠️  EMERGENZA!")
        controller.stop_control()
        raise


def custom_flight(controller: DroneController):
    """Volo personalizzato guidato"""
    
    print("\n" + "="*60)
    print("MODALITÀ VOLO PERSONALIZZATO")
    print("="*60)
    print("\nControlli in tempo reale:")
    print("  w/s - Avanti/Indietro")
    print("  a/d - Sinistra/Destra")
    print("  u/j - Su/Giù")
    print("  q/e - Ruota Sinistra/Destra")
    print("  h - Hover")
    print("  l - Atterraggio")
    print("  x - Esci e atterra")
    
    confirm = input("\nDigita 'SI' per iniziare: ")
    if confirm.upper() != 'SI':
        print("Annullato")
        return
    
    print("\n⏫ Decollo automatico...")
    controller.takeoff(duration=2.0)
    
    print("\n🎮 Controlli attivi! Premi un tasto:")
    
    try:
        import msvcrt if sys.platform == 'win32' else None
        
        def get_key():
            if sys.platform == 'win32':
                if msvcrt.kbhit():
                    return msvcrt.getch().decode('utf-8').lower()
            else:
                # Linux/Mac - richiede termios
                import sys, tty, termios
                fd = sys.stdin.fileno()
                old = termios.tcgetattr(fd)
                try:
                    tty.setraw(fd)
                    return sys.stdin.read(1).lower()
                finally:
                    termios.tcsetattr(fd, termios.TCSADRAIN, old)
            return None
        
        while True:
            # Mantieni hover di default
            controller.hover()
            
            key = get_key()
            if key:
                if key == 'x':
                    break
                elif key == 'w':
                    print("↑ Avanti")
                    controller.move('forward', duration=0.5)
                elif key == 's':
                    print("↓ Indietro")
                    controller.move('backward', duration=0.5)
                elif key == 'a':
                    print("← Sinistra")
                    controller.move('left', duration=0.5)
                elif key == 'd':
                    print("→ Destra")
                    controller.move('right', duration=0.5)
                elif key == 'u':
                    print("⬆ Su")
                    controller.move('up', duration=0.5)
                elif key == 'j':
                    print("⬇ Giù")
                    controller.move('down', duration=0.5)
                elif key == 'q':
                    print("↶ Ruota sinistra")
                    controller.move('rotate_left', duration=0.5)
                elif key == 'e':
                    print("↷ Ruota destra")
                    controller.move('rotate_right', duration=0.5)
                elif key == 'h':
                    print("🚁 Hover")
                elif key == 'l':
                    print("⏬ Atterraggio")
                    controller.land()
                    break
            
            time.sleep(0.05)
    
    except:
        print("\n⚠️  Controllo non supportato, atterraggio...")
    
    finally:
        print("\n⏬ Atterraggio finale...")
        controller.land(duration=2.0)
        print("✓ Atterrato")


def main():
    if len(sys.argv) < 2:
        print("Utilizzo: python advanced_example.py <PORTA_SERIALE> [MODE]")
        print("\nMODE:")
        print("  demo   - Sequenza automatica completa (default)")
        print("  test   - Volo test semplice")
        print("  custom - Controllo manuale")
        print("\nEsempio: python advanced_example.py COM3 demo")
        sys.exit(1)
    
    port = sys.argv[1]
    mode = sys.argv[2] if len(sys.argv) > 2 else 'demo'
    
    # Connetti
    controller = DroneController(port, debug=True)
    if not controller.connect():
        print("✗ Connessione fallita")
        sys.exit(1)
    
    time.sleep(2)
    
    try:
        if mode == 'demo':
            flight_sequence_demo(controller)
        elif mode == 'test':
            simple_test_flight(controller)
        elif mode == 'custom':
            custom_flight(controller)
        else:
            print(f"Modalità non valida: {mode}")
    
    except KeyboardInterrupt:
        print("\n\nInterrotto dall'utente")
    
    finally:
        print("\nCleanup...")
        controller.stop_control()
        controller.disconnect()
        print("Fine")


if __name__ == "__main__":
    main()
