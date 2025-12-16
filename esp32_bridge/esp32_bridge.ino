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
#define RTSP_RESPONSE_TIMEOUT_MS 1000
#define RTSP_KEEPALIVE_INTERVAL_MS 5000

// ===== TYPES =====
class RTSP {
public:

  bool init() {

    if(!rtsp.connect(DRONE_IP, DRONE_VIDEO_PORT)){
      return false;
    }

    String resp;
    sendOptions();
    resp = readResponse();
    sendDescribe();
    resp = readResponse();
    sendSetup();
    resp = readResponse();

    // extract session
    const int idx = resp.indexOf("Session:");
    const int end = resp.indexOf("\r\n", idx);
    sessionId = idx >= 0 ? resp.substring(idx + 8, end < 0 ? resp.length() : end) : "";
    sessionId.trim();

    lastKeepAlive = millis();

    rtp.begin(DRONE_RTP_PORT);

    return true;
  }

  bool isConnected() { return rtsp.connected(); }

  bool setStream(uint8_t enabled) {
    bool ok = enabled ? sendPlay() : sendStop();
    if(ok){
      lastKeepAlive = millis();
      String resp = readResponse();
    }
    return ok;
  }

  void close() {
    rtsp.stop();
    rtp.stop();
    lastKeepAlive = 0;
  }

  int pollRTP(VideoPayload *payload) {
    int packetSize = rtp.parsePacket();
    if (packetSize > 0) {
      return rtp.read((uint8_t *)payload, MIN(packetSize, sizeof(*payload)));
    }
    return 0;
  }
  
  bool sendKeepalive() {
    bool ok = false;
    if (rtsp.connected() && millis() - lastKeepAlive > RTSP_KEEPALIVE_INTERVAL_MS) {
      ok = sendOptions(); // keepalive
      lastKeepAlive = millis();
    }
    return ok;
  }

private:
  String readResponse() {
    if(!rtsp.connected()){
      return "";
    }
    unsigned long start = millis();
    while (!rtsp.available() && millis() - start < RTSP_RESPONSE_TIMEOUT_MS) {
        delay(1);
    }
    String resp;
    while (rtsp.available()) {
      char c = rtsp.read();
      resp += c;
      if (resp.endsWith("\r\n\r\n")) {
        break;
      }
    }
    return resp;
  }

  bool sendRequest(const String &req) {
    return rtsp.print(req) > 0;
  }

  bool sendOptions() {
    const String req =
        "OPTIONS " DRONE_CAM " RTSP/1.0\r\n"
        "CSeq: " + String(cseq++) + "\r\n"
        "User-Agent: Lavf57.71.100\r\n"
        "\r\n";
    return sendRequest(req);
  }

  bool sendDescribe() {
    const String req =
        "DESCRIBE " DRONE_CAM " RTSP/1.0\r\n"
        "Accept: application/sdp\r\n"
        "CSeq: " + String(cseq++) + "\r\n"
        "User-Agent: Lavf57.71.100\r\n"
        "\r\n";
    return sendRequest(req);
  }

  bool sendSetup() {
    const String req =
        "SETUP " DRONE_CAM "/track0 RTSP/1.0\r\n"
        "Transport: RTP/AVP/UDP;unicast;client_port=" STR(DRONE_RTP_PORT) "-" STR(DRONE_RTCP_PORT) "\r\n"
        "CSeq: " + String(cseq++) + "\r\n"
        "User-Agent: Lavf57.71.100\r\n"
        "\r\n";
    return sendRequest(req);
  }

  bool sendPlay() {
    const String req =
        "PLAY " DRONE_CAM "/ RTSP/1.0\r\n"
        "Range: npt=0.000-\r\n"
        "CSeq: " + String(cseq++) + "\r\n"
        "User-Agent: Lavf57.71.100\r\n"
        "Session: " + sessionId + "\r\n"
        "\r\n";
    return sendRequest(req);
  }

  bool sendStop() {
    const String req =
        "TEARDOWN " DRONE_CAM " RTSP/1.0\r\n"
        "CSeq: " + String(cseq++) + "\r\n"
        "User-Agent: Lavf57.71.100\r\n"
        "Session: " + sessionId + "\r\n"
        "\r\n";
    return sendRequest(req);
  }

  WiFiClient rtsp;
  WiFiUDP rtp;
  unsigned long lastKeepAlive = 0;
  int cseq                    = 1;
  String sessionId            = "";
};

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
    tft.print("Drone Bridge: rate  ");
    tft.print(SERIAL_BAUD);
  }

  bool isConnected() const { return WiFi.status() == WL_CONNECTED; }

  ConnStatus_t connectionStatus() { 
    return !isConnected() ? Disconnected : (!rtsp.isConnected() ? ConnectedControl : Connected);
  }

  void disconnectFromDrone() {
    rtsp.close();
    udp.stop();
    WiFi.disconnect(true);
  }

  bool connectToDrone() {
    static const int maxAttempts = 10;
    if(!connParams.valid()){
      return false;
    }
    char wifiSsid[32];
    snprintf(wifiSsid, sizeof(wifiSsid), "%s%s", DRONE_WIFI_PREFIX, connParams.ssid);
    WiFi.begin(wifiSsid, DRONE_PASSW);
    for (int attempts = 0; !isConnected() && attempts < maxAttempts; attempts++) {
      delay(connParams.timeout / maxAttempts);
    }
    if(!isConnected()){
      return false;
    }
    udp.begin(DRONE_RECV_PORT);
    rtsp.init();
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
        clientFdbk.type = PacketType_ConnectionStat;
        clientFdbk.data.connStatus = connectionStatus();
        sendToClient(clientFdbk);
        return; // return to not send ack

      case PacketType_SetVideo: 
        ok = rtsp.setStream(clientCmd.data.videoEnabled);
        break;

      case PacketType_DroneCmd: 
        ok = sendToDrone((const uint8_t *)&clientCmd.data.droneCmd, sizeof(clientCmd.data.droneCmd));
        break;

      case PacketType_FlyCmd: 
        ok = sendToDrone((const uint8_t *)&clientCmd.data.flyCmd, sizeof(clientCmd.data.flyCmd));
        break;

      default:
        break;
    }

    clientFdbk.type = PacketType_Ack;
    clientFdbk.data.ack.cmd = clientCmd.type;
    clientFdbk.data.ack.val = ok;
	  sendToClient(clientFdbk);
  }

  void forwardDroneTlm() {
    static const int MIN_DRONE_TLM_SIZE = sizeof(DroneTlm) - sizeof(DroneTlm::sporadicData);
    int packetSize = udp.parsePacket();
    while (packetSize >= MIN_DRONE_TLM_SIZE) {
      const int len = udp.read((uint8_t *)&clientFdbk.data.droneTlm, MIN(packetSize, sizeof(DroneTlm)));
      if(len > 0){
        clientFdbk.type = PacketType_DroneTlm;
        sendToClient(clientFdbk);
        packetSize = udp.parsePacket();
      }
    }
  }

  void forwardDroneVideo() {
    rtsp.sendKeepalive();
    clientFdbk.type = PacketType_DroneVideo;
    int videoPayloadSize = rtsp.pollRTP(&videoPayload);
    while(videoPayloadSize > 0) {
      clientFdbk.data.videoPayloadSize = videoPayloadSize;
      sendToClient(clientFdbk);
      sendToClient(videoPayload, videoPayloadSize);
      videoPayloadSize = rtsp.pollRTP(&videoPayload);
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
  
  template <typename T>
  void sendToClient(const T &packet, int size = -1) {
    Serial.write((const uint8_t *)&packet, size < 0 ? sizeof(packet) : size);
    Serial.flush();
  }
  
  bool readFromClient(ClientPacket *cmd) {
    if(Serial.available() < sizeof(*cmd)){
      return 0;
    }
    return Serial.readBytes((uint8_t *)cmd, sizeof(*cmd)) == sizeof(*cmd);
  }

  WiFiUDP udp;
  RTSP rtsp;
  TFT_eSPI tft = TFT_eSPI();
  ConnParams connParams;
  ClientPacket clientCmd;
  ClientPacket clientFdbk;
  VideoPayload videoPayload;
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
  bridge.forwardDroneVideo();

  if(LOOP_DELAY > 0){
    delay(LOOP_DELAY);
  }
}
