/**
 * ESP32 WiFi-Serial Bridge for Drone Nova Cam
 */

#include <WiFi.h>
#include <WiFiUdp.h>
#include <TFT_eSPI.h>

// ===== DRONE CONFIG =====
#define DRONE_SSID "NOVA CAM DRONE-dc8d36"
#define DRONE_PASSWORD ""
#define DRONE_IP "192.168.1.1"
#define DRONE_PORT 7099
#define DRONE_RESP_PORT 8888
#define VIDEO_PORT 7070

// ===== SERIAL CONFIG =====
#define SERIAL_BAUD 115200
#define MAX_PACKET_SIZE 256

// ===== TIMING =====
#define HEARTBEAT_INTERVAL 1000
#define RECONNECT_INTERVAL 500
#define RECONNECT_ATTEMPTS 10

// ===== TYPES =====
typedef unsigned long Time_t;

enum FlyControllerFlags {
  FastFly = 1 << 0,
  FastDrop = 1 << 1,
  EmergencyStop = 1 << 2,
  CircleTurnEnd = 1 << 3,
  NoHeadMode = 1 << 4,
  Unlock = 1 << 5,
  GyroCorrection = 1 << 7
};

static const uint8_t NEUTRAL = 128;
struct FlyCmd {
  const uint8_t start = 102;
  uint8_t controlByte1 = NEUTRAL; // Movimento laterale
  uint8_t controlByte2 = NEUTRAL; // Movimento avanti/indietro
  uint8_t controlAccelerator = NEUTRAL; // Accelerazione/quota
  uint8_t controlTurn = NEUTRAL; // Rotazione
  uint8_t cmdFlags = 0x00; // Flag comando
  uint8_t crc = 0x00; // Checksum
  const uint8_t end = 153;

  void calculate() {
    controlByte1 = max(controlByte1, uint8_t(1));
    controlByte2 = max(controlByte2, uint8_t(1));
    controlAccelerator = controlAccelerator == 1 ? 0 : controlAccelerator;
    controlTurn = (controlTurn >= 104 && controlTurn <= 152) ? NEUTRAL : max(controlTurn, uint8_t(1));
    crc = (((controlByte1 ^ controlByte2) ^ controlAccelerator) ^ controlTurn) ^ (cmdFlags & 255);
  }
};

// ===== GLOBAL VARIABLES =====
WiFiUDP udp;
TFT_eSPI tft = TFT_eSPI();

FlyCmd flyCmd;
Time_t lastHeartbeat = 0;
bool isConnected = false;
bool autoHeartbeat = true;

// ===== STANDARD COMMANDS =====
static const uint8_t CMD_HEARTBEAT[] = {1, 1};
static const uint8_t CMD_STOP_CONTROL[] = {8, 1};
static const uint8_t CMD_SWITCH_CAM_FRONT[] = {6, 1};
static const uint8_t CMD_SWITCH_CAM_BACK[] = {6, 2};
static const uint8_t CMD_PHOTO_ACK[] = {9, 1};
static const uint8_t CMD_VIDEO_ACK[] = {9, 2};

// ===== SETUP =====
void tftPrint(const char* msg) {
  tft.print(msg);
}

void tftPrintln(const char* msg) {  
  static const int tftXstart = 10;
  static const int tftYstart = 10;
  tftPrint(msg);
  int newY = tftYstart + tft.getCursorY() + tft.fontHeight();
  tft.setCursor(tftXstart, newY);
}

void setup() {
  Serial.begin(SERIAL_BAUD);
  
  // Display init
  tft.init();
  tft.setRotation(1);
  tft.frameViewport(TFT_RED, 2);
  tft.fillScreen(TFT_BLACK);
  tft.setTextColor(TFT_WHITE);
  tft.setTextSize(2);
  tftPrintln(" ");

  tftPrintln("Drone:");
  tftPrintln(DRONE_SSID);

  // Configure WiFi
  WiFi.mode(WIFI_STA);
  WiFi.setAutoReconnect(true);
  connectToWiFi();

  // Start UDP
  udp.begin(DRONE_RESP_PORT);
}

// ===== WIFI CONNECTION =====
void onConnected() {
  isConnected = true;
  tft.fillScreen(TFT_GREEN);
}

void onDisconnected() {
  isConnected = false;
  tft.fillScreen(TFT_RED);
}

void connectToWiFi() {
  WiFi.begin(DRONE_SSID, DRONE_PASSWORD);

  int attempts = 0;
  while (WiFi.status() != WL_CONNECTED && attempts < RECONNECT_ATTEMPTS) {
    delay(RECONNECT_INTERVAL);
    attempts++;
  }

  if (WiFi.status() == WL_CONNECTED) {
    onConnected();
  } else {
    onDisconnected();
  }
}

void handleConnection() {
  if (WiFi.status() != WL_CONNECTED) {
    onDisconnected();
    connectToWiFi();
  }
}

// ===== COMMAND UDP =====
void sendCommand(const uint8_t *data, size_t length) {
  if (!isConnected) {
    return;
  }
  udp.beginPacket(DRONE_IP, DRONE_PORT);
  udp.write(data, length);
  udp.endPacket();
}

void sendCommand(FlyCmd cmd) {
  cmd.calculate();
  uint8_t data[sizeof(cmd) + 1];
  data[0] = 3;
  memcpy(data + 1, &cmd, sizeof(cmd));
  sendCommand(data, sizeof(data));
}

// ===== AUTOMATIC HEARTBEAT =====
void handleHeartbeat() {
  if (autoHeartbeat && isConnected) {
    const Time_t now = millis();
    if (now - lastHeartbeat >= HEARTBEAT_INTERVAL) {
      sendCommand(CMD_HEARTBEAT, sizeof(CMD_HEARTBEAT));
      lastHeartbeat = now;
    }
  }
}

// ===== RECV RESPONSE FROM DRONE =====
void handleUdpReceive() {
  int packetSize = udp.parsePacket();
  if (packetSize > 0) {
    uint8_t buffer[256];
    int len = udp.read(buffer, sizeof(buffer));
    /*
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
    */
  }
}

// ===== RECV SERIAL =====
void handleSerialInput() {
  while (Serial.available() > 0) {
    char c = Serial.read();
    switch (c) {
      case 'h':
        sendCommand(CMD_HEARTBEAT, sizeof(CMD_HEARTBEAT));
        break;
      case 'e':
        sendCommand(CMD_STOP_CONTROL, sizeof(CMD_STOP_CONTROL));
        break;
      case 'f':
        sendCommand(CMD_SWITCH_CAM_FRONT, sizeof(CMD_SWITCH_CAM_FRONT));
        delay(500);
        sendCommand(CMD_SWITCH_CAM_BACK, sizeof(CMD_SWITCH_CAM_BACK));
        break;
      case 'j':
        autoHeartbeat = !autoHeartbeat;
        break;
      case 'w':
        if(flyCmd.controlByte2 < 255)
          flyCmd.controlByte2++;
        break;
      case 's':
        if(flyCmd.controlByte2 > 0)
          flyCmd.controlByte2--;
        break;
      case 'd':
        if(flyCmd.controlByte1 < 255)
          flyCmd.controlByte1++;
        break;
      case 'a':
        if(flyCmd.controlByte1 > 0)
          flyCmd.controlByte1--;
        break;
      case 'o':
        if(flyCmd.controlAccelerator < 255)
          flyCmd.controlAccelerator++;
        break;
      case 'p':
        if(flyCmd.controlAccelerator > 0)
          flyCmd.controlAccelerator--;
        break;
      case 'k':
        flyCmd.cmdFlags = FastFly;
        break;
      case 'l':
        flyCmd.cmdFlags = FastDrop;
        break;
    }
  }
}

// ===== MAIN LOOP =====
void loop() {
  handleSerialInput();
  sendCommand(flyCmd);
  handleUdpReceive();
  handleHeartbeat();
  handleConnection();

  delay(1);
}
