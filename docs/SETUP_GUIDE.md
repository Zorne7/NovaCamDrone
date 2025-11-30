# ESP32 Drone Bridge - Guida Completa

## 📋 Requisiti Hardware

-   **ESP32 DevKit** (qualsiasi variante con WiFi)
-   **Cavo USB** (per collegamento al computer)
-   **Drone Nova Cam** con WiFi attivo

## 🔧 Installazione Firmware ESP32

### Opzione 1: Arduino IDE

1. **Installa Arduino IDE** da https://arduino.cc
2. **Aggiungi supporto ESP32:**
    - File → Preferenze
    - URL boards: `https://dl.espressif.com/dl/package_esp32_index.json`
    - Tools → Board Manager → cerca "ESP32" → Installa
3. **Configura il drone:**
    - Apri `esp32_drone_bridge.ino`
    - Modifica la riga 16:
        ```cpp
        #define DRONE_SSID "NOME_WIFI_DRONE"  // Nome della rete WiFi del drone
        ```
4. **Carica il firmware:**
    - Tools → Board → ESP32 Dev Module
    - Tools → Port → Seleziona la porta COM
    - Sketch → Upload

### Opzione 2: PlatformIO (VS Code)

1. **Installa PlatformIO** extension in VS Code
2. **Crea progetto:**
    ```
    File: esp32_drone_bridge/platformio.ini
    ```
3. **Carica:**
    - PlatformIO → Upload

## 🐍 Installazione Python Controller

### Windows

```powershell
# Crea ambiente virtuale
python -m venv venv
venv\Scripts\activate

# Installa dipendenze
pip install pyserial
```

### Linux/Mac

```bash
# Crea ambiente virtuale
python3 -m venv venv
source venv/bin/activate

# Installa dipendenze
pip install pyserial
```

## 🚀 Utilizzo

### 1. Connetti ESP32 al computer via USB

### 2. Accendi il drone e attendi rete WiFi

Il drone crea una rete WiFi tipo: `NOVA_CAM_XXXX`

### 3. Verifica connessione ESP32

Apri Serial Monitor (115200 baud):

```
Arduino IDE: Tools → Serial Monitor
```

Dovresti vedere:

```
=================================
ESP32 Drone Bridge - Starting...
=================================
Connecting to drone WiFi: NOVA_CAM_XXXX
...
✓ WiFi Connected!
IP Address: 192.168.1.X
Bridge Ready!
```

### 4. Avvia Controller Python

**Windows:**

```powershell
cd python_controller
python drone_controller.py COM3  # Sostituisci COM3 con la tua porta
```

**Linux:**

```bash
cd python_controller
python drone_controller.py /dev/ttyUSB0
```

### 5. Comandi Interattivi

Una volta avviato, puoi usare:

```
h - Heartbeat manuale
i - Info sistema
a - Toggle auto-heartbeat
c - Cambia camera
s - Stop controllo
t - Decollo (simulato)
l - Atterraggio (simulato)
v - Hover
w/a/s/d - Movimento
q/e - Rotazione
u/j - Su/Giù
x - Esci
```

## 🔍 Troubleshooting

### ESP32 non si connette al WiFi

-   Verifica il nome della rete in `DRONE_SSID`
-   Controlla che il drone sia acceso
-   Riavvia l'ESP32

### Porta seriale non trovata (Windows)

-   Installa driver CP210x o CH340 (cerca su Google per il tuo ESP32)
-   Controlla Device Manager → Porte COM

### Porta seriale non trovata (Linux)

```bash
# Verifica dispositivi USB
ls /dev/ttyUSB*
ls /dev/ttyACM*

# Aggiungi permessi
sudo usermod -a -G dialout $USER
# Poi riavvia
```

### Python non trova pyserial

```bash
pip install pyserial --upgrade
```

## 📡 Protocollo Comunicazione

### Da Computer a ESP32 (Seriale USB)

```
[LUNGHEZZA][DATI...]

Esempio: Inviare {1, 1}
→ Serial: 0x02 0x01 0x01
```

### Da ESP32 a Drone (UDP WiFi)

```
Indirizzo: 192.168.1.1:7099
Pacchetti UDP diretti
```

## 🎮 Esempi Codice Python

### Esempio Base

```python
from drone_controller import DroneController

# Connetti
drone = DroneController('COM3')
drone.connect()

# Heartbeat
drone.heartbeat()

# Hover
drone.hover()

# Muovi avanti per 2 secondi
drone.move('forward', power=160, duration=2.0)

# Disconnetti
drone.disconnect()
```

### Esempio Sequenza Volo

```python
drone = DroneController('COM3')
drone.connect()

# Sequenza
drone.takeoff(duration=2.0)
time.sleep(1)
drone.move('forward', duration=2.0)
drone.move('right', duration=1.5)
drone.move('rotate_left', duration=1.0)
drone.land(duration=2.0)

drone.disconnect()
```

### Esempio Comando Personalizzato

```python
# Movimento diagonale
cmd = drone.create_fly_command(
    horizontal=180,  # Destra
    vertical=180,    # Avanti
    throttle=150     # Leggermente su
)
drone.send_command(cmd)
```

## 📊 Struttura Progetto

```
drone-1/
├── esp32_bridge/
│   └── esp32_drone_bridge.ino    # Firmware ESP32
├── python_controller/
│   ├── drone_controller.py       # Controller Python
│   └── requirements.txt          # Dipendenze Python
└── docs/
    └── SETUP_GUIDE.md           # Questa guida
```

## ⚠️ Avvertenze di Sicurezza

1. **Testa sempre in spazio aperto** e sicuro
2. **NON volare vicino a persone** o oggetti fragili
3. **Mantieni sempre contatto visivo** con il drone
4. **Tieni pronto il comando di emergenza** (Stop Control)
5. **Rispetta le leggi locali** sui droni
6. I valori di throttle sono **simulati** - testa con cautela

## 📝 Note Tecniche

-   **Frequenza comandi volo**: 20Hz (50ms intervallo)
-   **Heartbeat**: Automatico ogni 1 secondo (disattivabile)
-   **Timeout seriale**: 100ms
-   **Latenza tipica**: 20-50ms (ESP32-Drone)
-   **Range WiFi**: ~30-50 metri (dipende da ambiente)

## 🔄 Aggiornamenti Firmware

Per aggiornare il firmware ESP32:

1. Modifica `esp32_drone_bridge.ino`
2. Ricarica via Arduino IDE
3. L'ESP32 si resetterà automaticamente

## 📞 Supporto

Per problemi o domande:

-   Controlla Serial Monitor ESP32 per diagnostica
-   Verifica connessione WiFi drone
-   Testa comandi manuali nel Serial Monitor ('H', 'I', etc.)
