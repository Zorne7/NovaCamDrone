# 🚁 Drone Nova Cam - ESP32 Bridge Controller

Sistema completo per comandare il drone Nova Cam tramite ESP32 e Python.

## 📐 Architettura

```
┌──────────────┐    USB Serial      ┌──────────┐    WiFi UDP      ┌──────────┐
│   Computer   │ ←────────────────→ │  ESP32   │ ←──────────────→ │  Drone   │
│   (Python)   │   115200 baud      │  Bridge  │  192.168.1.1     │ Nova Cam │
└──────────────┘                    └──────────┘                  └──────────┘
```

## 🎯 Caratteristiche

✅ **ESP32 Bridge WiFi-Serial** - Connessione trasparente al drone  
✅ **Controller Python** - API completa per controllo volo  
✅ **Heartbeat Automatico** - Mantiene connessione attiva  
✅ **Comandi Volo** - Movimento completo 6DOF  
✅ **Modalità Interattiva** - Controllo in tempo reale  
✅ **Test Suite** - Verifica funzionamento senza volare  
✅ **Esempi Avanzati** - Sequenze automatiche pre-programmate

## 🚀 Quick Start

### 1. Hardware

-   ESP32 DevKit (qualsiasi modello con WiFi)
-   Cavo USB
-   Drone Nova Cam

### 2. Software ESP32

```bash
# Con Arduino IDE
1. Apri esp32_bridge/esp32_drone_bridge.ino
2. Modifica DRONE_SSID con il nome WiFi del tuo drone
3. Upload → ESP32
```

### 3. Software Python

```bash
# Windows
cd python_controller
pip install -r requirements.txt
python drone_controller.py COM3

# Linux
cd python_controller
pip install -r requirements.txt
python drone_controller.py /dev/ttyUSB0
```

## 📂 Struttura Progetto

```
drone-1/
├── esp32_bridge/
│   ├── esp32_drone_bridge.ino    # Firmware ESP32 (Arduino)
│   └── platformio.ini            # Config PlatformIO
│
├── python_controller/
│   ├── drone_controller.py       # Controller principale
│   ├── test_drone.py            # Test suite
│   ├── advanced_example.py      # Esempi avanzati
│   └── requirements.txt         # Dipendenze Python
│
├── docs/
│   └── SETUP_GUIDE.md           # Guida completa
│
└── sources/                      # Codice APK decompilato (analisi)
```

## 💻 Comandi Python

### Base

```python
from drone_controller import DroneController

drone = DroneController('COM3')
drone.connect()

# Comandi base
drone.heartbeat()           # Keepalive
drone.hover()              # Mantieni posizione
drone.stop_control()       # Stop emergenza
drone.switch_camera()      # Cambia camera

drone.disconnect()
```

### Movimento

```python
# Movimento direzionale
drone.move('forward', power=160, duration=1.0)
drone.move('backward', power=160, duration=1.0)
drone.move('left', power=160, duration=1.0)
drone.move('right', power=160, duration=1.0)
drone.move('up', power=160, duration=1.0)
drone.move('down', power=160, duration=1.0)
drone.move('rotate_left', power=160, duration=1.0)
drone.move('rotate_right', power=160, duration=1.0)
```

### Volo Automatico

```python
# Decollo e atterraggio
drone.takeoff(duration=2.0, power=180)
drone.land(duration=2.0, power=80)

# Comando personalizzato
cmd = drone.create_fly_command(
    horizontal=180,   # Destra
    vertical=160,     # Avanti
    throttle=150,     # Su
    turn=128          # Nessuna rotazione
)
drone.send_command(cmd)
```

## 🧪 Test

```bash
# Test connessione (senza volare)
python test_drone.py COM3

# Output:
# ✓ Connessione OK
# ✓ Info richieste
# ✓ Heartbeat OK
# ✓ Comando inviato
# ...
```

## 🎮 Esempi Avanzati

```bash
# Sequenza automatica completa
python advanced_example.py COM3 demo

# Volo test semplice
python advanced_example.py COM3 test

# Controllo manuale (keyboard)
python advanced_example.py COM3 custom
```

## 📡 Protocollo Comandi

### Comandi Base (2 byte)

| Comando   | Codice     | Descrizione         |
| --------- | ---------- | ------------------- |
| Heartbeat | `{1, 1}`   | Keepalive (ogni 1s) |
| Stop      | `{8, 1}`   | Stop controllo      |
| Camera    | `{6, 1/2}` | Switch camera       |

### Comandi Volo (8 byte)

```
[102][H][V][T][R][MODE][ACTION][153]

H = Horizontal (1-255, 128=neutro)
V = Vertical (1-255, 128=neutro)
T = Throttle (1-255, 128=hover)
R = Rotation (1-255, 128=neutro)
MODE = Flag modalità
ACTION = Flag azioni
```

## 🔧 Troubleshooting

### ESP32 non si connette

```
❌ Problema: WiFi connection failed
✅ Soluzione:
   - Verifica DRONE_SSID nel codice
   - Controlla che il drone sia acceso
   - Riavvia ESP32
```

### Python non trova porta COM

```
❌ Problema: Serial port not found
✅ Soluzione Windows:
   - Device Manager → Porte COM
   - Installa driver CP210x/CH340

✅ Soluzione Linux:
   ls /dev/ttyUSB*
   sudo usermod -a -G dialout $USER
```

### Comandi non funzionano

```
❌ Problema: Drone non risponde
✅ Soluzione:
   1. Verifica heartbeat attivo (ESP32 Serial Monitor)
   2. Controlla connessione WiFi ESP32
   3. Testa comando 'I' (info) nel controller
```

## ⚠️ Sicurezza

🔴 **IMPORTANTE:**

-   ✅ Testa sempre in **spazio aperto** (min 5x5m)
-   ✅ Mantieni **distanza di sicurezza** da persone
-   ✅ Tieni pronto **comando emergenza** (CTRL+C)
-   ✅ Verifica **livello batteria** drone
-   ✅ Rispetta **leggi locali** sui droni

## 📊 Performance

-   **Latenza**: 20-50ms (computer → drone)
-   **Frequenza comandi**: 20Hz (50ms intervallo)
-   **Range WiFi**: ~30-50m (dipende ambiente)
-   **Heartbeat**: Automatico ogni 1s

## 🔗 Link Utili

-   [Guida Setup Completa](docs/SETUP_GUIDE.md)
-   [Documentazione API Python](python_controller/drone_controller.py)
-   [Codice ESP32](esp32_bridge/esp32_drone_bridge.ino)

## 📝 Licenza

Questo progetto è fornito "as-is" per scopi educativi.  
Usare a proprio rischio. Gli autori non sono responsabili per danni.

## 🤝 Contributi

Contributi benvenuti! Per modifiche:

1. Testa sempre le modifiche in sicurezza
2. Documenta le nuove funzionalità
3. Mantieni compatibilità retroattiva

---

**Made with ❤️ for drone enthusiasts**
