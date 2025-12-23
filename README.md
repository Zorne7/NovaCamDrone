# 🚁 Drone Nova Cam - ESP32 Bridge Controller

Complete system for controlling the Nova Cam drone using an ESP32 and C++/Python.

## 📐 Architettura

```
┌──────────────┐    USB Serial      ┌──────────┐   WiFi UDP/TCP   ┌──────────┐
│   Computer   │ ←────────────────→ │  ESP32   │ ←──────────────→ │  Drone   │
│ (C++/Python) │  configurable baud │  Bridge  │  192.168.1.1     │ Nova Cam │
└──────────────┘                    └──────────┘                  └──────────┘
```

## 🎯 Features

✅ **ESP32 WiFi USB-Serial Bridge** - Transparent connection to the drone
✅ **C++/Python Controller** - Full API for flight control and video processing
✅ **Automatic Heartbeat** - Keeps the connection alive
✅ **Flight Commands** - Full movement control
✅ **Interactive Mode** - Real‑time control

## 🚀 Quick Start

### 1. Hardware

-   ESP32 (any model with WiFi)
-   USB cable (Type-C → Type-A)
-   Drone Nova Cam

### 2. Software

-	Configure BRIDGE_BITRATE in protocol.h to select the baud rate

#### 2.1 Software ESP32
```bash
# With Arduino IDE
1. Open esp32_bridge/esp32_bridge.ino
2. Upload → ESP32
```

#### 2.2 Software PC
```bash
# With Qt (6.7.2 - MinGW 64bit)
1. Open controller/CMakeLists.txt
2. Build → Run
```

## 📂 Project Structure

```
drone/
├── esp32_bridge/
│   ├── esp32_bridge.ino    	# Firmware ESP32 (Arduino)
│   └── platformio.ini      	# Config PlatformIO
│
├── controller/
│   └── sources (C++/Python)	# PC application with GUI
│
├── fly-nova_cam/           	# APK and its decompiled code (analysis)
│
└── protocol.h           		# Header containing protocol and data structures
```

## 📡 Comand Protocol

### Basic Commands (2 bytes)

| Command   | Code	     | Description         		|
| --------- | ---------- | ------------------------ |
| Heartbeat | `{1, 1}`   | Keepalive 	 			|
| Stop      | `{8, 1}`   | Stop control      		|
| Camera    | `{6, 1/2}` | Switch camera front/back |
| Ack       | `{9, 1/2}` | Ack photo/video    		|

### Flight Command (9 bytes)

```
[0x03] [0x66] [H] [V] [T] [R] [F] [C] [0x99]
```
| 	Byte   	| Description   					|
| --------- | ---------------------------------	|
|	H 		| Horizontal (1–255, 128 = neutral)	|
|	V 		| Vertical   (1–255, 128 = neutral)	|
|	T 		| Throttle   (1–255, 128 = hover)	|
|	R 		| Rotation   (1–255, 128 = neutral)	|
|	F 		| Mode/action flags					|
|	C 		| CRC = H ^ V ^ T ^ R ^ F			|
