/**
 * ESP32 WiFi-Serial Bridge for Drone Nova Cam
 */

#include <WiFi.h>
#include <WiFiUdp.h>
#include <TFT_eSPI.h>

#include "../protocol.h"

// ===== SERIAL CONFIG =====
#define SERIAL_BAUD 921600

// ===== TYPES =====

class Bridge {
public:
  void init() {
    Serial.begin(SERIAL_BAUD);
    WiFi.mode(WIFI_STA);
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
    WiFi.begin(connParams.wifiSsid, connParams.wifiPassw);  
    for (int attempts = 0; !isConnected() && attempts < maxAttempts; attempts++) {
      delay(connParams.timeout / maxAttempts);
    }
    if(!isConnected()){
      return false;
    }
    udp.begin(connParams.recvPort);
  }

  void parseClientCmd() {
    if (!readFromClient(&clientCmd)) {
	    // No cmd received
      return;
    }

    bool ok = false;
    switch (clientCmd.type){

      case TypeSetConnection:
        disconnectFromDrone();
        connParams = clientCmd.data.connParams;
        ok = true;
        break;

      case TypeGetConnection:
        clientResp.type = TypeConnectionStat;
        clientResp.data.connected = isConnected();
        sendToClient(clientResp);
        return; // return to not send ack

      case TypeDroneCmd: 
        ok = sendToDrone((const uint8_t *)&clientCmd.data.droneCmd, sizeof(clientCmd.data.droneCmd));
        break;

      case TypeFlyCmd: 
        ok = sendToDrone((const uint8_t *)&clientCmd.data.flyCmd, sizeof(clientCmd.data.flyCmd));
        break;

      default:
        break;
    }

    clientResp.type = TypeAck;
    clientResp.data.ack = ok ? 1 : 0;
	  sendToClient(clientResp);
  }

  void forwardDroneTlm() {
    int packetSize = udp.parsePacket();
    while (packetSize >= MIN_DRONE_TLM_SIZE) {
      const int len = udp.read((uint8_t *)&clientResp.data.droneTlm, MIN(packetSize, sizeof(DroneTlm)));
      if(len > 0){
        clientResp.type = TypeDroneTlm;
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
    udp.beginPacket(connParams.ip, connParams.sendPort);
    udp.write(data, length);
    udp.endPacket();
    return true;
  }
  
  void sendToClient(const ClientResp &resp) {
    Serial.write((const uint8_t *)&resp, sizeof(resp));
    Serial.flush();
  }
  
  bool readFromClient(ClientCmd *cmd) {
    if(Serial.available() < sizeof(*cmd)){
      return 0;
    }
    return Serial.readBytes((uint8_t *)cmd, sizeof(*cmd)) == sizeof(*cmd);
  }

  WiFiUDP udp;
  TFT_eSPI tft = TFT_eSPI();
  ConnectionParams connParams;
  ClientCmd clientCmd;
  ClientResp clientResp;
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
    bridge.disconnectFromDrone();
    connected = bridge.connectToDrone();
  }

  bridge.parseClientCmd();
  bridge.forwardDroneTlm();

  delay(1);
}
