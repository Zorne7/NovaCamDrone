/**
 * ESP32 WiFi-Serial Bridge for Drone Nova Cam
 */

#include <WiFi.h>
#include <WiFiUdp.h>
#include <TFT_eSPI.h>

// ===== DRONE CONFIG =====
#define DRONE_SSID 			  "NOVA CAM DRONE-dc8d36"
#define DRONE_PASSWORD 		""
#define DRONE_IP 			    "192.168.1.1"
#define DRONE_PORT 			  7099
#define DRONE_RESP_PORT 	8888
#define VIDEO_PORT 			  7070
#define DRONE_BUFF_SIZE 	256

// ===== SERIAL CONFIG =====
#define SERIAL_BAUD 		  921600

// ===== TIMING =====
#define CONNECTION_TIMEOUT 	3000
#define HEARTBEAT_INTERVAL 	1000

// ===== WIFI =====
static inline bool isConnected() { return WiFi.status() == WL_CONNECTED; }
static inline bool connectToWifi(const char *ssid, const char *passw, uint32_t timeout = 3000) {
  static const int maxAttempts = 10;
  WiFi.begin(ssid, passw);  
  for (int attempts = 0; !isConnected() && attempts < maxAttempts; attempts++) {
    delay(timeout / maxAttempts);
  }
  return isConnected();
}

// ===== TYPES =====
enum FlyControllerFlags {
  FastFly 			  = 1 << 0,
  FastDrop 			  = 1 << 1,
  EmergencyStop 	= 1 << 2,
  CircleTurnEnd 	= 1 << 3,
  NoHeadMode 		  = 1 << 4,
  Unlock 			    = 1 << 5,
  GyroCorrection 	= 1 << 7
};

enum Cam {
  CamFront 	= 0x01,
  CamBack  	= 0x02
};

enum Ack {
  AckPhoto	= 0x01,
  AckVideo	= 0x02,  
};

enum ResponseType {
  AckKo		  = 0x00,
  AckOk		  = 0x01,
  Feedback	= 0x02,
  DroneData	= 0x03
};

static constexpr uint8_t NEUTRAL 	  = 128;
static constexpr uint8_t DEAD_ZONE  = 24;
struct FlyParams {
  uint8_t controlByte1 			  = NEUTRAL; 	// Control left/right
  uint8_t controlByte2 			  = NEUTRAL; 	// Control front/back
  uint8_t controlAccelerator 	= NEUTRAL; 	// Accelerator
  uint8_t controlTurn 			  = NEUTRAL; 	// Rotation
  uint8_t cmdFlags 				    = 0x00; 	// FlyControllerFlags

  void normalize() {
    controlByte1 = max(controlByte1, uint8_t(1));
    controlByte2 = max(controlByte2, uint8_t(1));
    controlAccelerator = controlAccelerator == 1 ? 0 : controlAccelerator;
    controlTurn = (controlTurn >= (NEUTRAL - DEAD_ZONE) && controlTurn <= (NEUTRAL + DEAD_ZONE)) ? NEUTRAL : max(controlTurn, uint8_t(1));
  }
};

struct FlyCmd {
  const uint8_t header 	= 0x03;
  const uint8_t start 	= 0x66;
  FlyParams flyParams;
  uint8_t crc 			    = 0x00;
  const uint8_t end 	  = 0x99;

  void calculateCrc() {    
    crc = flyParams.controlByte1 ^
          flyParams.controlByte2 ^
          flyParams.controlAccelerator ^
          flyParams.controlTurn ^
          flyParams.cmdFlags;
  }
};

enum UserCmd {
  CmdAck			    = 'a',
  CmdConnStatus 	= 'w',
  CmdSetAutoHB 		= 'b',
  CmdHeartbeat 		= 'h',
  CmdStopCtrl 		= 's',
  CmdEnableCtrl   = 'e',
  CmdSwitchCam 		= 'c',
  CmdSetFlyParams = 'f'
};

class DroneController {
public:
  void init() {
    tft.init();
    tft.setRotation(1);
    tft.frameViewport(TFT_RED, 2);
    tft.fillScreen(TFT_BLACK);
    tft.setTextColor(TFT_WHITE);
    tft.setTextSize(2);
    println(" ");
  }

  bool isConnected() const { return connected; }
  bool autoHeartbeatEnabled() const { return autoHeartbeat; }

  void setConnection(bool connected) {  
    this->connected = connected;  
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

  bool sendHeartbeat() {
    static const uint8_t CMD_HEARTBEAT[] = {1, 1};
    return sendCommand(CMD_HEARTBEAT, sizeof(CMD_HEARTBEAT));
  }

  bool sendStopControl() {
    static const uint8_t CMD_STOP_CONTROL[] = {8, 1};
    return sendCommand(CMD_STOP_CONTROL, sizeof(CMD_STOP_CONTROL));
  }

  bool sendSwitchCamera(uint8_t cam) {
    if(cam != CamFront && cam != CamBack) {
      return false;
    }
    const uint8_t CMD_SWITCH_CAM[] = {6, cam};
    return sendCommand(CMD_SWITCH_CAM, sizeof(CMD_SWITCH_CAM));
  }

  bool sendFlyCommand() {
    FlyCmd cmd = flyCmd;
    cmd.flyParams.normalize();
    cmd.calculateCrc();
    return sendCommand((const uint8_t *)&cmd, sizeof(cmd));
  }
  
  bool sendAck(uint8_t ack) {
    if(ack != AckPhoto && ack != AckVideo) {
      return false;
    }
    const uint8_t CMD_ACK[] = {9, ack};
    return sendCommand(CMD_ACK, sizeof(CMD_ACK));
  }

  void parseUserCmd() {
	  uint8_t cmd;  
    if (readCommand(&cmd, sizeof(cmd)) != sizeof(cmd)) {
	    // No cmd received
      return;
    }
    bool ok = false;
    switch (cmd){
      case CmdConnStatus:{
        const uint8_t connStatus = isConnected();
        sendResponse(Feedback, &connStatus, 1);
        return;
      }
      case CmdSetAutoHB: {
        uint8_t autoHB;
        if(readCommand(&autoHB, sizeof(autoHB)) == sizeof(autoHB)){
          autoHeartbeat = autoHB != 0;
          ok = true;
        }
        break;
      }
      case CmdHeartbeat:
        ok = sendHeartbeat();
        break;
      case CmdStopCtrl:
        ok = sendStopControl();
        break;
      case CmdEnableCtrl:
        flyCmd.flyParams = FlyParams();
        flyCmd.flyParams.controlAccelerator = 255;
        ok = sendFlyCommand();
        flyCmd.flyParams.controlAccelerator = 0;
        ok = ok && sendFlyCommand();
        flyCmd.flyParams = FlyParams();
        break;
      case CmdSwitchCam: {
        uint8_t cam;
        ok = readCommand(&cam, sizeof(cam)) == sizeof(cam) && sendSwitchCamera(cam);
        break;
      }
      case CmdSetFlyParams: {
        if (readCommand(flyData, sizeof(flyData)) == sizeof(flyData)) {
          memcpy(&flyCmd.flyParams, flyData, sizeof(flyData));
          ok = sendFlyCommand();
        }
        break;
      }
      case CmdAck: {
        uint8_t ack;
        ok = readCommand(&ack, sizeof(ack)) == sizeof(ack) && sendAck(ack);
        break;
      }
      default:
        break;
    }
	  sendResponse(ok ? AckOk : AckKo);
  }

  void forwardDroneData() {
    int packetSize = udp.parsePacket();
    while (packetSize > 0) {
      const int len = udp.read(droneData, min(packetSize, int(sizeof(droneData))));
      if(len > 0){
        sendResponse(DroneData, droneData, len);
        packetSize = udp.parsePacket();
      }
    }
  }

private:
  // Send cmd to drone
  bool sendCommand(const uint8_t *data, size_t length) {
    if(!connected){
      return false;
    }
    udp.beginPacket(DRONE_IP, DRONE_PORT);
    udp.write(data, length);
    udp.endPacket();
    return true;
  }
  
  // Send resp to user
  void sendResponse(uint8_t type, const uint8_t *data = 0, uint8_t length = 0) {
    Serial.write(&type, 1);
    if(data && length > 0){
      Serial.write(&length, 1);
      Serial.write(data, length);
      Serial.flush();
    }
  }
  
  // Read cmd from user
  int readCommand(uint8_t *data, size_t length) {
    if(Serial.available() <= 0){
      return 0;
    }
    return Serial.readBytes(data, length);
  }

  WiFiUDP udp;
  bool connected = false;
  bool autoHeartbeat = true;
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
  
  WiFi.mode(WIFI_STA);
  WiFi.setAutoReconnect(true);
  
  droneCtrl.init();
  droneCtrl.println(DRONE_SSID);
}

// ===== MAIN LOOP =====
void loop() {

  // Check connection
  if(!isConnected()){
    droneCtrl.setConnection(false);
    if(connectToWifi(DRONE_SSID, DRONE_PASSWORD, CONNECTION_TIMEOUT)){
      droneCtrl.setConnection(true);
      counter = 0;
    }
  }

  droneCtrl.parseUserCmd();
  if(!droneCtrl.isConnected()){
    return;
  }

  if(droneCtrl.autoHeartbeatEnabled() && counter % HEARTBEAT_INTERVAL == 0){
    droneCtrl.sendHeartbeat();
  }
  droneCtrl.forwardDroneData();

  delay(1);
  counter++;
}
