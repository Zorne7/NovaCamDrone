/**
 * ESP32 WiFi-Serial Bridge per Drone Nova Cam
 *
 * L'ESP32 si connette al WiFi del drone e inoltra comandi UDP
 * ricevuti via seriale USB dal computer.
 *
 * Protocollo Seriale:
 * - Baudrate: 115200
 * - Formato: [LUNGHEZZA][COMANDO_BYTES]
 * - Esempio: invia 2 byte {1,1} -> Serial: [0x02, 0x01, 0x01]
 */

#include <WiFi.h>
#include <WiFiUdp.h>

// ===== CONFIGURAZIONE DRONE =====
#define DRONE_SSID "NOVA_CAM_XXXX" // Modifica con il nome WiFi del drone
#define DRONE_PASSWORD ""          // Password WiFi (se presente)
#define DRONE_IP "192.168.1.1"
#define DRONE_PORT 7099
#define VIDEO_PORT 7070

// ===== CONFIGURAZIONE SERIALE =====
#define SERIAL_BAUD 115200
#define MAX_PACKET_SIZE 256

// ===== TIMING =====
#define HEARTBEAT_INTERVAL 1000 // 1 secondo
#define RECONNECT_INTERVAL 5000 // 5 secondi
#define SERIAL_TIMEOUT 100      // 100ms

// ===== VARIABILI GLOBALI =====
WiFiUDP udp;
unsigned long lastHeartbeat = 0;
unsigned long lastReconnect = 0;
bool isConnected = false;
bool autoHeartbeat = true;

// Buffer per ricezione seriale
uint8_t serialBuffer[MAX_PACKET_SIZE];
int serialBufferPos = 0;

// ===== COMANDI PREDEFINITI =====
const uint8_t CMD_HEARTBEAT[] = {1, 1};
const uint8_t CMD_STOP_CONTROL[] = {8, 1};
const uint8_t CMD_SWITCH_CAM_FRONT[] = {6, 1};
const uint8_t CMD_SWITCH_CAM_BACK[] = {6, 2};

// ===== SETUP =====
void setup() {
  Serial.begin(SERIAL_BAUD);
  delay(1000);

  Serial.println("\n\n=================================");
  Serial.println("ESP32 Drone Bridge - Starting...");
  Serial.println("=================================");

  // Configura WiFi
  WiFi.mode(WIFI_STA);
  WiFi.setAutoReconnect(true);

  connectToWiFi();

  // Avvia UDP
  udp.begin(8888); // Porta locale per ricevere risposte

  Serial.println("Bridge Ready!");
  Serial.println(
      "Commands: H=heartbeat, S=stop, C=cam, I=info, A=auto-heartbeat toggle");
  Serial.println("Binary mode: Send [LENGTH][DATA...]\n");
}

// ===== CONNESSIONE WIFI =====
void connectToWiFi() {
  Serial.print("Connecting to drone WiFi: ");
  Serial.println(DRONE_SSID);

  WiFi.begin(DRONE_SSID, DRONE_PASSWORD);

  int attempts = 0;
  while (WiFi.status() != WL_CONNECTED && attempts < 30) {
    delay(500);
    Serial.print(".");
    attempts++;
  }

  if (WiFi.status() == WL_CONNECTED) {
    isConnected = true;
    Serial.println("\n✓ WiFi Connected!");
    Serial.print("IP Address: ");
    Serial.println(WiFi.localIP());
    Serial.print("Signal Strength: ");
    Serial.print(WiFi.RSSI());
    Serial.println(" dBm");
  } else {
    isConnected = false;
    Serial.println("\n✗ WiFi Connection Failed!");
  }
}

// ===== INVIO COMANDO UDP =====
void sendCommand(const uint8_t *data, size_t length) {
  if (!isConnected) {
    Serial.println("ERROR: Not connected to WiFi");
    return;
  }

  udp.beginPacket(DRONE_IP, DRONE_PORT);
  udp.write(data, length);
  udp.endPacket();

  // Echo del comando inviato
  Serial.print("TX[");
  Serial.print(length);
  Serial.print("]: ");
  for (size_t i = 0; i < length; i++) {
    if (data[i] < 16)
      Serial.print("0");
    Serial.print(data[i], HEX);
    Serial.print(" ");
  }
  Serial.println();
}

// ===== HEARTBEAT AUTOMATICO =====
void handleHeartbeat() {
  if (autoHeartbeat && isConnected) {
    unsigned long now = millis();
    if (now - lastHeartbeat >= HEARTBEAT_INTERVAL) {
      sendCommand(CMD_HEARTBEAT, sizeof(CMD_HEARTBEAT));
      lastHeartbeat = now;
    }
  }
}

// ===== RICEZIONE RISPOSTE DRONE =====
void handleUdpReceive() {
  int packetSize = udp.parsePacket();
  if (packetSize > 0) {
    uint8_t buffer[256];
    int len = udp.read(buffer, sizeof(buffer));

    Serial.print("RX[");
    Serial.print(len);
    Serial.print("]: ");
    for (int i = 0; i < len; i++) {
      if (buffer[i] < 16)
        Serial.print("0");
      Serial.print(buffer[i], HEX);
      Serial.print(" ");
    }
    Serial.println();

    // Decodifica risposta
    if (len >= 1) {
      Serial.print("  Resolution: ");
      Serial.println(buffer[0]);
    }
    if (len >= 3) {
      if (buffer[2] == 0x4D) { // 77 = 'M' (Photo)
        Serial.println("  Type: PHOTO");
      } else if (buffer[2] == 0x58) { // 88 = 'X' (Video)
        Serial.println("  Type: VIDEO");
      }
    }
  }
}

// ===== RICEZIONE SERIALE =====
void handleSerialInput() {
  while (Serial.available() > 0) {
    char c = Serial.read();

    // Modalità testo (comandi rapidi)
    if (c == 'H' || c == 'h') {
      sendCommand(CMD_HEARTBEAT, sizeof(CMD_HEARTBEAT));
    } else if (c == 'S' || c == 's') {
      sendCommand(CMD_STOP_CONTROL, sizeof(CMD_STOP_CONTROL));
    } else if (c == 'C' || c == 'c') {
      sendCommand(CMD_SWITCH_CAM_FRONT, sizeof(CMD_SWITCH_CAM_FRONT));
      delay(500);
      sendCommand(CMD_SWITCH_CAM_BACK, sizeof(CMD_SWITCH_CAM_BACK));
    } else if (c == 'A' || c == 'a') {
      autoHeartbeat = !autoHeartbeat;
      Serial.print("Auto-heartbeat: ");
      Serial.println(autoHeartbeat ? "ON" : "OFF");
    } else if (c == 'I' || c == 'i') {
      printInfo();
    }
    // Modalità binaria: primo byte = lunghezza
    else if (c > 0 && c <= MAX_PACKET_SIZE) {
      int length = c;
      uint8_t cmd[MAX_PACKET_SIZE];

      // Attendi tutti i byte
      unsigned long timeout = millis() + SERIAL_TIMEOUT;
      int received = 0;

      while (received < length && millis() < timeout) {
        if (Serial.available()) {
          cmd[received++] = Serial.read();
        }
      }

      if (received == length) {
        sendCommand(cmd, length);
      } else {
        Serial.println("ERROR: Serial timeout");
      }
    }
  }
}

// ===== INFO SISTEMA =====
void printInfo() {
  Serial.println("\n===== SYSTEM INFO =====");
  Serial.print("WiFi Status: ");
  Serial.println(isConnected ? "CONNECTED" : "DISCONNECTED");
  if (isConnected) {
    Serial.print("SSID: ");
    Serial.println(WiFi.SSID());
    Serial.print("IP: ");
    Serial.println(WiFi.localIP());
    Serial.print("RSSI: ");
    Serial.print(WiFi.RSSI());
    Serial.println(" dBm");
  }
  Serial.print("Auto-heartbeat: ");
  Serial.println(autoHeartbeat ? "ON" : "OFF");
  Serial.print("Drone IP: ");
  Serial.println(DRONE_IP);
  Serial.print("Drone Port: ");
  Serial.println(DRONE_PORT);
  Serial.println("======================\n");
}

// ===== RECONNECT AUTOMATICO =====
void handleReconnect() {
  if (WiFi.status() != WL_CONNECTED && !isConnected) {
    unsigned long now = millis();
    if (now - lastReconnect >= RECONNECT_INTERVAL) {
      Serial.println("Reconnecting to WiFi...");
      connectToWiFi();
      lastReconnect = now;
    }
  } else if (WiFi.status() == WL_CONNECTED && !isConnected) {
    isConnected = true;
    Serial.println("WiFi reconnected!");
  }
}

// ===== LOOP PRINCIPALE =====
void loop() {
  handleSerialInput();
  handleUdpReceive();
  handleHeartbeat();
  handleReconnect();

  delay(1); // Piccolo delay per stabilità
}
