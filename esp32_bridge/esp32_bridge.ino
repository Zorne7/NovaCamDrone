#include <WiFi.h>
#include <WiFiUdp.h>
#include <TFT_eSPI.h>

#include "../protocol.h"

// ===== CONFIG =====
#define LOOP_DELAY  1

static inline const ByteArray read_UDP(WiFiUDP &udp)
{
  int packetSize = udp.parsePacket();
  if (packetSize <= 0) {
    return {};
  }
  ByteArray buff(packetSize);
  int r = udp.read(buff.data(), packetSize);
  if (r < 0) {
    return {};
  }
  buff.resize(r);
  return buff;
}

static inline const ByteArray read_TCP(WiFiClient &tcp)
{
  int available = tcp.available();
  if (available <= 0) {
    return {};
  }
  ByteArray buff(available);
  int r = tcp.read(buff.data(), available);
  if (r < 0) {
    return {};
  }
  buff.resize(r);
  return buff;
}

class Bridge : public BridgeInterface {
public:

  void init() override {
    Serial.begin(BRIDGE_BITRATE);
    WiFi.mode(WIFI_STA);
    WiFi.setAutoReconnect(false);
    tft.init();
    tft.setRotation(1);
    tft.frameViewport(TFT_RED, 2);
    tft.fillScreen(TFT_BLACK);
    tft.setTextColor(TFT_WHITE);
    tft.setTextSize(2);
    tft.setCursor(10, 10);
    tft.print("Drone Bridge: rate  ");
    tft.print(BRIDGE_BITRATE);
  }

  ConnStatus_t connectionStatus() const override { return WiFi.status(); }

  void disconnectFromDrone() override {
    videoRtsp.stop();
    videoRtp.stop();
    ctrl.stop();
    WiFi.disconnect(true);
  }

  void start() override {
    ctrl.begin(DRONE_CTRL_PORT);
    videoRtp.begin(DRONE_RTP_PORT);
    videoRtsp.connect(DRONE_IP, DRONE_RTSP_PORT);
  }

private:
  time_t currentTime() const override { return millis(); }

  void wait_ms(time_t ms) override { delay(ms); }

  void startConnection(const string &ssid, const string &passw) override { WiFi.begin(ssid.c_str(), passw.c_str()); }

  const ByteArray readCmdPacketData(uint16_t size) override {
    if (Serial.available() < size) {
      return {};
    }
    ByteArray buff(size);
    int r = Serial.readBytes(buff.data(), size);
    if (r != size) {
      return {};
    }
    return buff;
  }

  bool sendTlmPacketData(const ByteArray &tlmData) override {
    return Serial.write(tlmData.data(), tlmData.size()) == tlmData.size();
  }

  bool sendDroneCmdData(const ByteArray &cmdData) override {
    ctrl.beginPacket(DRONE_IP, DRONE_CTRL_PORT);
    bool ok = ctrl.write(cmdData.data(), cmdData.size()) == cmdData.size();
    ctrl.endPacket();
    return ok;
  }

  const ByteArray readDroneTlmData() override { return read_UDP(ctrl); }

  const ByteArray readDroneVideoData() override { return read_UDP(videoRtp); }

  bool sendDroneVideoCmd(const string &cmd) override {
    return videoRtsp.write((const uint8_t*)cmd.data(), cmd.size()) == cmd.size();
  }

  const string readDroneVideoResp() override {
    const ByteArray resp = read_TCP(videoRtsp);
    return string((const char *)resp.data(), resp.size());
  }

  WiFiUDP ctrl;
  WiFiClient videoRtsp;
  WiFiUDP videoRtp;
  TFT_eSPI tft = TFT_eSPI();
};

// ===== GLOBAL VARIABLES =====
Bridge bridge;
ConnStatus_t lastConnStatus = UNKNOWN_STATUS;

// ===== SETUP =====
void setup()
{  
  bridge.init();
}

// ===== MAIN LOOP =====
void loop() 
{
  const ConnStatus_t connStatus = bridge.connectionStatus();

  if (connStatus != CONNECTED) {
    bridge.connectToDrone();
  } else if (lastConnStatus != CONNECTED) {
    bridge.start();
  }

  if (connStatus != lastConnStatus) {
    bridge.forwardConnStatus();
  }
  lastConnStatus = connStatus;

  while (bridge.parseCmdPacket()) {};
  if (connStatus == CONNECTED) {
    bridge.step();
    while (bridge.forwardDroneTlm()) {};
    while (bridge.forwardDroneVideo()) {};
  }

  if (LOOP_DELAY > 0) {
    delay(LOOP_DELAY);
  }
}

