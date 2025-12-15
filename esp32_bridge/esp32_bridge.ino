/**
 * ESP32 WiFi-Serial Bridge for Drone Nova Cam
 */

#include <WiFi.h>
#include <WiFiUdp.h>
#include <TFT_eSPI.h>

#include "../protocol.h"

// ===== CONFIG =====
#define LOOP_DELAY  1
#define SERIAL_BAUD 921600

static inline char* concat(const char* a, const char* b) {
  size_t lenA = strlen(a);
  size_t lenB = strlen(b);
  char* result = (char*)malloc(lenA + lenB + 1); // +1 for '\0'
  if (!result) return nullptr;
  memcpy(result, a, lenA);
  memcpy(result + lenA, b, lenB);
  result[lenA + lenB] = '\0';
  return result;
}

class Bridge {
public:
  void init() {
    Serial.begin(SERIAL_BAUD);
    WiFi.mode(WIFI_STA);
    WiFi.setAutoReconnect(false);
    tft.init();
    tft.setRotation(1);
    tft.frameViewport(TFT_RED, 2);
    tft.fillScreen(TFT_BLACK);
    tft.setTextColor(TFT_WHITE);
    tft.setTextSize(2);
    tft.setCursor(10, 10);
    tft.print("Drone Bridge: serial ");
    tft.print(SERIAL_BAUD);
  }

  bool isConnected() const { return WiFi.status() == WL_CONNECTED; }
  void disconnectFromDrone() {
    udp.stop();
    WiFi.disconnect(true);
  }
  bool connectToDrone() {
    static const int maxAttempts = 10;
    if(!connParams.valid()){
      return false;
    }
    char *wifiSsid = concat(DRONE_WIFI_PREFIX, connParams.ssid);
    WiFi.begin(wifiSsid, DRONE_PASSW);  
    for (int attempts = 0; !isConnected() && attempts < maxAttempts; attempts++) {
      delay(connParams.timeout / maxAttempts);
    }
    free(wifiSsid);
    if(!isConnected()){
      return false;
    }
    udp.begin(DRONE_RECV_PORT);
    return true;
  }

  void parseClientCmd() {
    if (!readFromClient(&clientCmd)) {
	    // No cmd received
      return;
    }

    bool ok = false;
    switch (clientCmd.type){

      case PacketType_SetConnection:
        disconnectFromDrone();
        connParams = clientCmd.data.connParams;
        ok = true;
        break;

      case PacketType_GetConnection:
        clientResp.type = PacketType_ConnectionStat;
        clientResp.data.connected = isConnected();
        sendToClient(clientResp);
        return; // return to not send ack

      case PacketType_DroneCmd: 
        ok = sendToDrone((const uint8_t *)&clientCmd.data.droneCmd, sizeof(clientCmd.data.droneCmd));
        break;

      case PacketType_FlyCmd: 
        ok = sendToDrone((const uint8_t *)&clientCmd.data.flyCmd, sizeof(clientCmd.data.flyCmd));
        break;

      default:
        break;
    }

    clientResp.type = PacketType_Ack;
    clientResp.data.ack.cmd = clientCmd.type;
    clientResp.data.ack.res = ok;
	  sendToClient(clientResp);
  }

  void forwardDroneTlm() {
    static const int MIN_DRONE_TLM_SIZE = sizeof(DroneTlm) - sizeof(DroneTlm::sporadicData);
    int packetSize = udp.parsePacket();
    while (packetSize >= MIN_DRONE_TLM_SIZE) {
      const int len = udp.read((uint8_t *)&clientResp.data.droneTlm, MIN(packetSize, sizeof(DroneTlm)));
      if(len > 0){
        clientResp.type = PacketType_DroneTlm;
        sendToClient(clientResp);
        packetSize = udp.parsePacket();
      }
    }
  }

private:
  bool sendToDrone(const uint8_t *data, size_t length) {
    if(!isConnected()){
      return false;
    }
    udp.beginPacket(DRONE_IP, DRONE_SEND_PORT);
    udp.write(data, length);
    udp.endPacket();
    return true;
  }
  
  void sendToClient(const ClientPacket &resp) {
    Serial.write((const uint8_t *)&resp, sizeof(resp));
    Serial.flush();
  }
  
  bool readFromClient(ClientPacket *cmd) {
    if(Serial.available() < sizeof(*cmd)){
      return 0;
    }
    return Serial.readBytes((uint8_t *)cmd, sizeof(*cmd)) == sizeof(*cmd);
  }

  WiFiUDP udp;
  TFT_eSPI tft = TFT_eSPI();
  ConnParams connParams;
  ClientPacket clientCmd;
  ClientPacket clientResp;
};

// ===== GLOBAL VARIABLES =====
Bridge bridge;

// ===== SETUP =====
void setup() {  
  bridge.init();
}

// ===== MAIN LOOP =====
void loop() {

  bool connected = bridge.isConnected();
  if(!connected){
    connected = bridge.connectToDrone();
  }

  bridge.parseClientCmd();
  bridge.forwardDroneTlm();

  if(LOOP_DELAY > 0){
    delay(LOOP_DELAY);
  }
}
