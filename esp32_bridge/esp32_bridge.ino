#include <WiFi.h>
#include <WiFiUdp.h>
#include <TFT_eSPI.h>

#include "../protocol.h"

// ===== CONFIG =====
#define LOOP_DELAY  1

// ===== TYPES =====
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
    videoRtsp.connect(DRONE_IP, DRONE_VIDEO_PORT);
    videoRtp.begin(DRONE_RTP_PORT);
  }

private:
  template <typename T>
  static inline int data_available(T &port) {
    if constexpr (std::is_same_v<std::decay_t<T>, WiFiUDP>) {
      return port.parsePacket();
    } else {
      return port.available();
    }
  }

  template <typename T>
  static inline const ByteArray read_from(T &port, int size = -1, bool *err = nullptr) {
    const int dataSize = MAX(data_available(port), size);
    ByteArray bytes(dataSize, 0);
    if(dataSize > 0){
      const int r = port.read(bytes.data(), dataSize);
      if (err) *err = r != dataSize;
      if(r != dataSize){
        bytes.resize(MAX(r, 0));
      }
    }
    return bytes;
  }

  template <typename T>
  static inline bool send_to(T &port, const ByteArray &buff) { return port.write(buff.data(), buff.size()) == buff.size(); }

  time_t currentTime() const override { return millis(); };

  void wait_ms(time_t ms) override { delay(ms); }

  void startConnection(const string &ssid, const string &passw) override { WiFi.begin(ssid.c_str(), passw.c_str()); };

  const ByteArray readCmdPacketData() override {
    ByteArray pktData;
    if(data_available(Serial) >= sizeof(Packet)){
      bool err;
      pktData = read_from(Serial, sizeof(Packet), &err);
      if(err){
        pktData.clear(); 
      }
    }
    return pktData;
  }

  bool sendTlmPacketData(const ByteArray &tlmData) override { return send_to(Serial, tlmData); };

  bool sendDroneCmdData(const ByteArray &cmdData) override {
    ctrl.beginPacket(DRONE_IP, DRONE_CTRL_PORT);
    bool ok = send_to(ctrl, cmdData);
    ctrl.endPacket();
    return ok;
  };

  const ByteArray readDroneTlmData() override { return read_from(ctrl); };

  const ByteArray readDroneVideoData() override { return read_from(videoRtp); };

  bool sendDroneVideoCmd(const string &cmd) override {
    const ByteArray cmdData((const uint8_t *)cmd.data(), (const uint8_t *)cmd.data() + cmd.size()); 
    return send_to(videoRtsp, cmdData); 
  }

  const string readDroneVideoResp() override {
    const ByteArray resp = read_from(videoRtsp);
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

  while(bridge.parseCmdPacket()){};
  if(connStatus == CONNECTED){
    bridge.step();
    while(bridge.forwardDroneTlm()){};
    while(bridge.forwardDroneVideo()){};
  }

  if (LOOP_DELAY > 0) {
    delay(LOOP_DELAY);
  }
}

