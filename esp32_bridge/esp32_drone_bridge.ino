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
#define DRONE_BUFF_SIZE 256

// ===== SERIAL CONFIG =====
#define SERIAL_BAUD 115200

// ===== TIMING =====
#define CONNECTION_TIMEOUT 3000
#define HEARTBEAT_INTERVAL 1000

// ===== WIFI =====
static inline bool isConnected() { return WiFi.status() == WL_CONNECTED; }

// ===== TYPES =====
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
struct FlyParams {
  uint8_t controlByte1 = NEUTRAL; // Control left/right
  uint8_t controlByte2 = NEUTRAL; // Control front/back
  uint8_t controlAccelerator = NEUTRAL; // Accelerator
  uint8_t controlTurn = NEUTRAL; // Rotation
  uint8_t cmdFlags = 0x00; // FlyControllerFlags

  void normalize() {
    controlByte1 = max(controlByte1, uint8_t(1));
    controlByte2 = max(controlByte2, uint8_t(1));
    controlAccelerator = controlAccelerator == 1 ? 0 : controlAccelerator;
    controlTurn = (controlTurn >= 104 && controlTurn <= 152) ? NEUTRAL : max(controlTurn, uint8_t(1));
  }
};

struct FlyCmd {
  const uint8_t header = 3;
  const uint8_t start = 102;
  FlyParams flyParams;
  uint8_t crc = 0x00; // Checksum
  const uint8_t end = 153;

  void calculateCrc() {    
    crc = (((flyParams.controlByte1 
    ^ flyParams.controlByte2) 
    ^ flyParams.controlAccelerator) 
    ^ flyParams.controlTurn) 
    ^ flyParams.cmdFlags;
  }
};

enum UserCmd {
  CmdConnStatus = 'w',
  CmdSetAutoHB = 'a',
  CmdHeartbeat = 'h',
  CmdStopCtrl = 's',
  CmdSwitchCam = 'c',
  CmdSetFlyParams = 'f'
};

class DroneController {
public:
  bool autoHeartbeat = true;

  void init() {
    // Debug output init
    tft.init();
    tft.setRotation(1);
    tft.frameViewport(TFT_RED, 2);
    tft.fillScreen(TFT_BLACK);
    tft.setTextColor(TFT_WHITE);
    tft.setTextSize(2);
    println(" ");
  }

  void setConnection(bool connected) {    
    if(connected){
      udp.begin(DRONE_RESP_PORT);
      tft.fillScreen(TFT_GREEN);
    }else{
      udp.stop();
      tft.fillScreen(TFT_RED);
    }
  }

  void print(const char* msg) {
    tft.print(msg);
  }

  void println(const char* msg) {  
    static const int tftXstart = 10;
    static const int tftYstart = 10;
    print(msg);
    tft.setCursor(tftXstart, tftYstart + tft.getCursorY() + tft.fontHeight());
  }

  void sendHeartbeat() {
    static const uint8_t CMD_HEARTBEAT[] = {1, 1};
    sendCommand(CMD_HEARTBEAT, sizeof(CMD_HEARTBEAT));
  }

  void sendStopControl() {
    static const uint8_t CMD_STOP_CONTROL[] = {8, 1};
    sendCommand(CMD_STOP_CONTROL, sizeof(CMD_STOP_CONTROL));
  }

  void sendSwitchCamera(uint8_t cam) {
    static const uint8_t CMD_SWITCH_CAM_FRONT[] = {6, 1};
    static const uint8_t CMD_SWITCH_CAM_BACK[] = {6, 2};
    sendCommand(cam != 0 ? CMD_SWITCH_CAM_FRONT : CMD_SWITCH_CAM_BACK, sizeof(CMD_SWITCH_CAM_FRONT));
  }

  void sendFlyCommand() {
    FlyCmd cmd = flyCmd;
    cmd.flyParams.normalize();
    cmd.calculateCrc();
    sendCommand((const uint8_t *)&cmd, sizeof(cmd));
  }

  void parseUserCmd() {
    if (Serial.available() <= 0) {
      return;
    }
    char c = Serial.read();
    switch (c){
        case CmdConnStatus:{
          char status = isConnected();
          Serial.write(&status, 1);
          break;
        }
        case CmdSetAutoHB:
          if(Serial.available() > 0){
            autoHeartbeat = Serial.read() != 0;
          }
          break;
        case CmdHeartbeat:
          sendHeartbeat();
          break;
        case CmdStopCtrl:
          sendStopControl();
          break;
        case CmdSwitchCam: {
          if(Serial.available() > 0){
            sendSwitchCamera(Serial.read());
          }
          break;
        }
        case CmdSetFlyParams: {
          int r = Serial.available() >= sizeof(flyData) ? Serial.readBytes(flyData, sizeof(flyData)) : -1;
          if (r == sizeof(flyData)) {
            memcpy(&flyCmd.flyParams, flyData, sizeof(flyData));
            sendFlyCommand();
          }
          break;
        }
        default:
          break;
    }
  }

  void forwardDroneData() {
    const int packetSize = udp.parsePacket();
    if (packetSize > 0) {
      const int len = udp.read(droneData, min(packetSize, int(sizeof(droneData))));
      if(len > 0){
        Serial.write(droneData, len);
      }
    }
  }

private:
  void sendCommand(const uint8_t *data, size_t length) {
    udp.beginPacket(DRONE_IP, DRONE_PORT);
    udp.write(data, length);
    udp.endPacket();
  }
  
  // STANDARD COMMANDS
  // CMD_PHOTO_ACK[] = {9, 1};
  // CMD_VIDEO_ACK[] = {9, 2};

  WiFiUDP udp;
  TFT_eSPI tft = TFT_eSPI();
  FlyCmd flyCmd;
  uint8_t flyData[sizeof(FlyParams)];
  uint8_t droneData[DRONE_BUFF_SIZE];
};

// ===== GLOBAL VARIABLES =====
DroneController droneCtrl;
uint32_t counter = 0;

// ===== SETUP =====
void setup() {
  Serial.begin(SERIAL_BAUD);
  // Configure WiFi
  WiFi.mode(WIFI_STA);
  WiFi.setAutoReconnect(true);
  // Init DroneController
  droneCtrl.init();
  droneCtrl.println(DRONE_SSID);
}

// ===== MAIN LOOP =====
void loop() {
  if(!isConnected()){
    droneCtrl.setConnection(false);
    // Connect to WiFi
    static const int maxAttempts = 100;
    WiFi.begin(DRONE_SSID, DRONE_PASSWORD);  
    for (int attempts = 0; !isConnected() && attempts < maxAttempts; attempts++) {
      delay(CONNECTION_TIMEOUT / maxAttempts);
    }
    if(!isConnected()){
      return;
    }
    droneCtrl.setConnection(true);
    counter = 0;
  }

  if(droneCtrl.autoHeartbeat && counter % HEARTBEAT_INTERVAL == 0){
    droneCtrl.sendHeartbeat();
  }
  droneCtrl.parseUserCmd();
  droneCtrl.forwardDroneData();

  delay(1);
  counter++;
}
