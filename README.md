# 🚁 Drone Nova Cam - ESP32 Bridge Controller

Sistema completo per comandare il drone Nova Cam tramite ESP32 e Python.

## 📐 Architettura

```
┌──────────────┐    USB Serial      ┌──────────┐    WiFi UDP      ┌──────────┐
│   Computer   │ ←────────────────→ │  ESP32   │ ←──────────────→ │  Drone   │
│ (C++/Python) │   921600 baud      │  Bridge  │  192.168.1.1     │ Nova Cam │
└──────────────┘                    └──────────┘                  └──────────┘
```

## 🎯 Caratteristiche

✅ **ESP32 Bridge WiFi-Serial** - Connessione trasparente al drone  
✅ **Controller C++/Python** - API completa per controllo volo  
✅ **Heartbeat Automatico** - Mantiene connessione attiva  
✅ **Comandi Volo** - Movimento completo 6DOF  
✅ **Modalità Interattiva** - Controllo in tempo reale

## 🚀 Quick Start

### 1. Hardware

-   ESP32 DevKit (qualsiasi modello con WiFi)
-   Cavo USB
-   Drone Nova Cam

### 2. Software ESP32

```bash
# Con Arduino IDE
1. Apri esp32_bridge/esp32_bridge.ino
3. Upload → ESP32
```

## 📂 Struttura Progetto

```
drone-1/
├── esp32_bridge/
│   ├── esp32_bridge.ino    # Firmware ESP32 (Arduino)
│   └── platformio.ini      # Config PlatformIO
│
├── controller/
│   └── sorgenti C++/Python
│
├── fly-nova_cam/           # Codice APK decompilato (analisi)
│
└── protocol.h           	# Header contenente il protocollo e le strutture usate
```

## 📡 Protocollo Comandi

### Comandi Base (2 byte)

| Comando   | Codice     | Descrizione         		|
| --------- | ---------- | ------------------------ |
| Heartbeat | `{1, 1}`   | Keepalive (ogni 1s) 		|
| Stop      | `{8, 1}`   | Stop controllo      		|
| Camera    | `{6, 1/2}` | Switch camera front/back |
| Ack       | `{9, 1/2}` | Ack foto/video      		|

### Comandi Volo (9 byte)

```
[3][102][H][V][T][R][MODE/ACTION][CRC][153]

H = Horizontal (1-255, 128=neutro)
V = Vertical (1-255, 128=neutro)
T = Throttle (1-255, 128=hover)
R = Rotation (1-255, 128=neutro)
MODE = Flag modalità/azioni
CRC = H ^ V ^ T ^ R ^ MODE
```
