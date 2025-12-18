#include <WiFi.h>
#include <WiFiUdp.h>
#include <TFT_eSPI.h>

#include "../protocol.h"

// ===== CONFIG =====
#define LOOP_DELAY  1
#define SERIAL_BAUD 921600

// ===== TYPES =====
typedef std::vector<uint8_t> ByteArray;

struct BridgePacket {
  BridgePacketHeader header;
  ByteArray payload;

  template <typename T>
  void setData(const T &val)
  {
    header.dataSize = sizeof(T);
    payload.resize(sizeof(T));
    T *data = (T *)payload.data();
    *data = val;
  }
};

static inline ByteArray UDP_read(WiFiUDP &udp)
{
  const int packetSize = udp.parsePacket();
  ByteArray bytes(packetSize, 0);
  const int r = udp.read(bytes.data(), packetSize);
  if(r != packetSize){
    bytes.resize(MAX(r, 0));
  }
  return bytes;
}

static inline ByteArray TCP_read(WiFiClient &tcp)
{
  const int dataSize = tcp.available();
  ByteArray bytes(dataSize, 0);
  const int r = tcp.read(bytes.data(), dataSize);
  if(r != dataSize){
    bytes.resize(MAX(r, 0));
  }
  return bytes;
}

class Bridge {
public:

  void init()
  {
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
    tft.print("Drone Bridge: rate  ");
    tft.print(SERIAL_BAUD);
  }

  uint16_t connectionTimeout() const { return connParams.timeout; }
  bool isConnected() const { return WiFi.status() == WL_CONNECTED; }

  void disconnectFromDrone()
  {    
    rtsp.stop();
    rtp.stop();
    ctrl.stop();
    WiFi.disconnect(true);
  }

  bool connectToDrone()
  {
    if(!connParams.valid()){
      return false;
    }
    char wifiSsid[sizeof(DRONE_WIFI_PREFIX) + sizeof(connParams.ssid) + 1];
    snprintf(wifiSsid, sizeof(wifiSsid), "%s%s", DRONE_WIFI_PREFIX, connParams.ssid);
    WiFi.begin(wifiSsid, DRONE_PASSW);
    return true;
  }

  void start()
  {    
    ctrl.begin(DRONE_CTRL_PORT);
    rtsp.connect(DRONE_IP, DRONE_VIDEO_PORT);
    rtp.begin(DRONE_RTP_PORT);
  }

  void parseClientCmd()
  {
    if (!readFromClient(&clientPkt)) {
	    // No cmd received
      return;
    }

    bool ok = false;

    if(clientPkt.header.dataSize == bridgePkt.payload.size()) {

      switch (clientPkt.header.id.type()){

        case PacketType_SetConnection:
          if(clientPkt.header.dataSize != sizeof(ConnParams)){
            break;
          }
          disconnectFromDrone();
          connParams = *(ConnParams *)bridgePkt.payload.data();
          ok = true;
          break;

        case PacketType_GetConnection:
          bridgePkt.header.id = BridgePacketId(PacketType_ConnectionStat);
          bridgePkt.setData((status_t)isConnected());
          sendToClient(bridgePkt);
          return; // return to not send ack

        case PacketType_Forward:
          switch (clientPkt.header.id.chan()) {
            case Channel_Ctrl_UDP:              
              ctrl.beginPacket(DRONE_IP, DRONE_CTRL_PORT);
              ok = ctrl.write(bridgePkt.payload.data(), bridgePkt.payload.size()) == bridgePkt.payload.size();
              ctrl.endPacket();
              break;
            case Channel_RTSP_TCP:
              ok = rtsp.write(bridgePkt.payload.data(), bridgePkt.payload.size()) == bridgePkt.payload.size();
              break;
            default:
              break;
          }
          break;

        default:
          break;
      }
    }

    bridgePkt.header.id = BridgePacketId(PacketType_Ack);
    bridgePkt.setData(Ack{.cmd = clientPkt.header.id, .val = ok});
    sendToClient(bridgePkt);
  }

  void forwardDroneTlm() { forwardToClient(Channel_Ctrl_UDP, UDP_read(ctrl)); }
  void forwardDroneResp() { forwardToClient(Channel_RTSP_TCP, TCP_read(rtsp)); }
  void forwardDroneVideo() { forwardToClient(Channel_RTP_UDP, UDP_read(rtp)); }

private:  
  void sendToClient(const BridgePacket &pkt)
  {
    Serial.write((const uint8_t *)&pkt.header, sizeof(pkt.header));
    Serial.write(pkt.payload.data(), pkt.payload.size());
    Serial.flush();
  }

  void forwardToClient(ProtocolChannel chan, const ByteArray &packet) {
    if(packet.empty()){
      return;
    }
    bridgePkt.header = BridgePacketHeader(BridgePacketId(PacketType_Forward, chan), packet.size());
    bridgePkt.payload = packet;
    sendToClient(bridgePkt);
  }
  
  bool readFromClient(BridgePacket *pkt)
  {
    if(Serial.available() < sizeof(pkt->header)){
      return false;
    }
    bool ok = Serial.readBytes((uint8_t *)&pkt->header, sizeof(pkt->header)) == sizeof(pkt->header);
    if(ok){
      pkt->payload.resize(pkt->header.dataSize);
      ok = Serial.readBytes(pkt->payload.data(), pkt->header.dataSize) == pkt->header.dataSize;
    }
    return ok;
  }

  WiFiUDP ctrl;
  WiFiClient rtsp;
  WiFiUDP rtp;

  TFT_eSPI tft = TFT_eSPI();
  ConnParams connParams;
  BridgePacket clientPkt;
  BridgePacket bridgePkt;
};

// ===== GLOBAL VARIABLES =====
Bridge bridge;
bool connecting;
unsigned long lastTryConnection;

// ===== SETUP =====
void setup()
{  
  bridge.init();
  connecting = false;
  lastTryConnection = 0;
}

// ===== MAIN LOOP =====
void loop() 
{
  if (!bridge.isConnected()) {
    const unsigned long now = millis();
    if (now - lastTryConnection >= bridge.connectionTimeout()) {
      connecting = bridge.connectToDrone();
      lastTryConnection = now;
    }
  } else {
    if (connecting) {
      bridge.start();
      connecting = false;
      lastTryConnection = 0;
    }
  }

  bridge.parseClientCmd();
  bridge.forwardDroneTlm();
  bridge.forwardDroneResp();
  bridge.forwardDroneVideo();

  if (LOOP_DELAY > 0) {
    delay(LOOP_DELAY);
  }
}

