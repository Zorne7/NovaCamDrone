#!/usr/bin/env python3
"""
Test Script per Drone Controller

Testa la connessione e i comandi base del drone
"""

import sys
import time
from drone_controller import DroneController


def test_connection(controller: DroneController) -> bool:
    """Test connessione base"""
    print("\n=== TEST 1: Connessione ===")
    if not controller.connect():
        print("✗ Connessione fallita")
        return False
    print("✓ Connessione OK")
    time.sleep(2)
    return True


def test_info(controller: DroneController) -> bool:
    """Test richiesta info"""
    print("\n=== TEST 2: Info Sistema ===")
    controller.get_info()
    time.sleep(1)
    print("✓ Info richieste")
    return True


def test_heartbeat(controller: DroneController) -> bool:
    """Test heartbeat"""
    print("\n=== TEST 3: Heartbeat ===")
    for i in range(3):
        print(f"Heartbeat {i+1}/3...")
        controller.heartbeat()
        time.sleep(1)
    print("✓ Heartbeat OK")
    return True


def test_camera_switch(controller: DroneController) -> bool:
    """Test cambio camera"""
    print("\n=== TEST 4: Cambio Camera ===")
    print("Cambio camera...")
    controller.switch_camera()
    time.sleep(2)
    print("✓ Comando inviato")
    return True


def test_hover_command(controller: DroneController) -> bool:
    """Test comando hover"""
    print("\n=== TEST 5: Comando Hover ===")
    print("Invio comando hover (posizione neutra)...")
    for i in range(5):
        controller.hover()
        time.sleep(0.05)  # 50ms = 20Hz
    print("✓ Comandi hover inviati (5x a 20Hz)")
    return True


def test_movement_commands(controller: DroneController) -> bool:
    """Test comandi di movimento (SENZA volare)"""
    print("\n=== TEST 6: Comandi Movimento (test sequenza) ===")
    print("NOTA: Questi sono SOLO test di invio, il drone NON volerà")
    
    tests = [
        ("Avanti", lambda: controller.create_fly_command(vertical=160)),
        ("Indietro", lambda: controller.create_fly_command(vertical=96)),
        ("Sinistra", lambda: controller.create_fly_command(horizontal=96)),
        ("Destra", lambda: controller.create_fly_command(horizontal=160)),
        ("Su", lambda: controller.create_fly_command(throttle=160)),
        ("Giù", lambda: controller.create_fly_command(throttle=96)),
    ]
    
    for name, cmd_func in tests:
        print(f"  Test {name}...", end=" ")
        cmd = cmd_func()
        controller.send_command(cmd)
        time.sleep(0.1)
        print("✓")
    
    print("✓ Tutti i comandi movimento testati")
    return True


def test_auto_heartbeat(controller: DroneController) -> bool:
    """Test toggle auto-heartbeat"""
    print("\n=== TEST 7: Auto-Heartbeat Toggle ===")
    print("Disattivo auto-heartbeat...")
    controller.toggle_auto_heartbeat()
    time.sleep(1)
    print("Riattivo auto-heartbeat...")
    controller.toggle_auto_heartbeat()
    time.sleep(1)
    print("✓ Toggle OK")
    return True


def run_all_tests(port: str):
    """Esegue tutti i test"""
    print("="*60)
    print("DRONE CONTROLLER - TEST SUITE")
    print("="*60)
    print(f"\nPorta: {port}")
    print("ATTENZIONE: Questi test NON faranno volare il drone")
    print("Sono solo test di comunicazione\n")
    
    input("Premi INVIO per iniziare i test...")
    
    controller = DroneController(port, debug=True)
    
    tests = [
        test_connection,
        test_info,
        test_heartbeat,
        test_camera_switch,
        test_hover_command,
        test_movement_commands,
        test_auto_heartbeat
    ]
    
    passed = 0
    failed = 0
    
    try:
        for test_func in tests:
            try:
                if test_func(controller):
                    passed += 1
                else:
                    failed += 1
                    print(f"✗ {test_func.__name__} fallito")
            except Exception as e:
                failed += 1
                print(f"✗ {test_func.__name__} errore: {e}")
            
            time.sleep(0.5)
    
    finally:
        # Cleanup
        print("\n=== Cleanup ===")
        controller.stop_control()
        controller.disconnect()
    
    # Risultati
    print("\n" + "="*60)
    print("RISULTATI TEST")
    print("="*60)
    print(f"Passati: {passed}/{len(tests)}")
    print(f"Falliti: {failed}/{len(tests)}")
    
    if failed == 0:
        print("\n✓ TUTTI I TEST SUPERATI!")
    else:
        print(f"\n✗ {failed} test falliti")
    
    return failed == 0


def main():
    if len(sys.argv) < 2:
        print("Utilizzo: python test_drone.py <PORTA_SERIALE>")
        print("Esempio: python test_drone.py COM3")
        sys.exit(1)
    
    port = sys.argv[1]
    success = run_all_tests(port)
    sys.exit(0 if success else 1)


if __name__ == "__main__":
    main()
