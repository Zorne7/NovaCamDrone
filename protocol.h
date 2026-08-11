#ifndef PROTOCOL_H
#define PROTOCOL_H

#include <memory.h>
#include <stdint.h>
#include <string>
#include <vector>

using namespace std;

// COMMON DEFINES
#define MAX(a, b) (a > b ? a : b)
#define MIN(a, b) (a < b ? a : b)
#define STR_HELPER(x) #x
#define STR(x) STR_HELPER(x)

// CONNECTION CONFIG
#define DRONE_WIFI_PREFIX "NOVA CAM DRONE-"
#define DRONE_PASSW ""
#define DRONE_IP "192.168.1.1"
#define DRONE_CTRL_PORT 7099
#define DRONE_RTSP_PORT 7070
#define DRONE_RTP_PORT 23144
#define DRONE_RTCP_PORT 23145
#define DRONE_CAM "rtsp://" DRONE_IP ":" STR(DRONE_RTSP_PORT) "/webcam"

// PROTOCOL CONFIG
#define FLY_CONTROL_NEUTRAL 128
#define DEF_FLY_TURN_DEAD_ZONE 24
#define RTSP_VER "1.0"
#define RTSP_USER_AGENT "Lavf57.71.100"
#define RTSP_END_PAR "\r\n"
#define RTSP_END_SECTION RTSP_END_PAR RTSP_END_PAR
#define RTSP_CLIENT_PORTS STR(DRONE_RTP_PORT) "-" STR(DRONE_RTCP_PORT)

// TIMING CONFIG
#define BRIDGE_BITRATE 2000000
#define HB_INTERVAL_MS 1000
#define FLY_INTERVAL_MS 50
#define RTSP_RESP_TIMEOUT_MS 3000

// TYPES
typedef uint8_t fly_par_t;
typedef char ssid_t[8];
typedef uint16_t port_t;
typedef vector<uint8_t> ByteArray;
typedef uint8_t crc_t;

// COMMON FUNCTIONS
static inline crc_t calculate_crc(const void *data, size_t len, crc_t init_val = 0x00)
{
    const uint8_t *bytes = (const uint8_t *) data;
    crc_t crc = init_val;
    for (int i = 0; i < len; i++) {
        crc ^= bytes[i];
    }
    return crc;
}

template<typename T>
static inline const ByteArray toBytes(const T &data)
{
    if constexpr (is_same_v<T, string>){
        return ByteArray((const uint8_t *)data.c_str(), (const uint8_t *)data.c_str() + data.size());
    }
    else {
        return ByteArray((const uint8_t *)&data, (const uint8_t *)&data + sizeof(T));
    }
}

template<typename T, typename F = T>
static inline void Flag_Set(T &flags, F flag, bool en)
{
    if (en) {
        flags |= (T) (flag);
    } else {
        flags &= (T) (~flag);
    }
}

// PROTOCOL STRUCTURES DEFINITION
#pragma pack(push, 1)

typedef uint8_t FlyControlFlags_t;
enum FlyControlFlags {
    ControlFlag_None = 0x00,
    ControlFlag_FastFly = 1 << 0,
    ControlFlag_FastDrop = 1 << 1,
    ControlFlag_EmergencyStop = 1 << 2,
    ControlFlag_CircleTurnEnd = 1 << 3,
    ControlFlag_NoHeadMode = 1 << 4,
    ControlFlag_Unlock = 1 << 5,
    ControlFlag_Unknown = 1 << 6,
    ControlFlag_GyroCorrection = 1 << 7
};

struct FlyControls
{
    fly_par_t controlByte1 = FLY_CONTROL_NEUTRAL;       // Control left/right
    fly_par_t controlByte2 = FLY_CONTROL_NEUTRAL;       // Control front/back
    fly_par_t controlAccelerator = FLY_CONTROL_NEUTRAL; // Accelerator
    fly_par_t controlTurn = FLY_CONTROL_NEUTRAL;        // Rotation
    FlyControlFlags_t flags = ControlFlag_None;         // Modes/Actions

    void normalize(fly_par_t turnDeadZone = DEF_FLY_TURN_DEAD_ZONE)
    {
        static_assert(DEF_FLY_TURN_DEAD_ZONE < FLY_CONTROL_NEUTRAL);
        controlByte1 = MAX(controlByte1, 1);
        controlByte2 = MAX(controlByte2, 1);
        controlAccelerator = controlAccelerator == 1 ? 0 : controlAccelerator;
        controlTurn = (controlTurn >= (FLY_CONTROL_NEUTRAL - turnDeadZone)
                       && controlTurn <= (FLY_CONTROL_NEUTRAL + turnDeadZone))
                          ? FLY_CONTROL_NEUTRAL
                          : MAX(controlTurn, 1);
    }
};

struct FlyCmd
{
    const uint8_t header = 0x03;
    const uint8_t start = 0x66;
    FlyControls flyControls = FlyControls();
    crc_t crc = calculate_crc(&flyControls, sizeof(flyControls));
    const uint8_t end = 0x99;

    inline void setControls(const FlyControls &controls, fly_par_t turnDeadZone = DEF_FLY_TURN_DEAD_ZONE)
    {
        flyControls = controls;
        flyControls.normalize(turnDeadZone);
        crc = calculate_crc(&flyControls, sizeof(flyControls));
    }
};

struct DroneCmd
{
    uint8_t type;
    uint8_t val;
};

typedef uint8_t TlmFdbkType_t;
enum TlmFdbkType { FdbkType_Photo = 77, FdbkType_Video = 88 };
struct DroneTlm
{
    uint8_t resolution;
    uint8_t switchCameraReset;
    TlmFdbkType_t fdbkType;
    uint8_t numPhoto;
    uint8_t numVideo;
    uint8_t dataSize;
    uint8_t data[15];

    inline bool fromBytes(const ByteArray &bytes)
    {
        static const int MIN_SIZE = sizeof(DroneTlm) - sizeof(dataSize) - sizeof(data);
        if(bytes.size() < MIN_SIZE) {
            return false;
        }
        memcpy(this, bytes.data(), MIN_SIZE);
        dataSize = bytes.size() - MIN_SIZE;
        if(dataSize > 0){
            memcpy(data, bytes.data() + MIN_SIZE, dataSize);
        }
        return true;
    }
};

struct ConnParams
{
    ssid_t ssid = "";
    uint16_t timeout = 0;
    inline bool valid() const { return ssid[0] != 0 && timeout > 0; }
};

typedef uint8_t ConnStatus_t; // Imported from arduino library
enum ConnStatus {
    NO_SHIELD = 255,
    STOPPED = 254,
    IDLE_STATUS = 0,
    NO_SSID_AVAIL = 1,
    SCAN_COMPLETED = 2,
    CONNECTED = 3,
    CONNECT_FAILED = 4,
    CONNECTION_LOST = 5,
    DISCONNECTED = 6,
    UNKNOWN_STATUS = 200, // Internal use only
};

typedef uint8_t PacketType_t;
enum PacketType {
    PacketType_Invalid = 0x00,
    // Command
    PacketType_SetConnection,
    PacketType_GetConnection,
    PacketType_DroneCmd,
    PacketType_SetControls,
    PacketType_SetVideo,
    // Telemetry
    PacketType_TextMsg,
    PacketType_Ack,
    PacketType_ConnectionStat,
    PacketType_DroneTlm,
    PacketType_DroneVideo,
};

typedef uint8_t AckVal_t;
enum AckVal { AckVal_Tiemout = 0, AckVal_OK, AckVal_KO, AckVal_CrcErr };
struct Ack
{
    PacketType_t cmd = PacketType_Invalid;
    AckVal_t val = AckVal_Tiemout;
};

union PacketPayload {
    // Command
    ConnParams connParams;
    DroneCmd droneCmd;
    FlyControls controls;
    bool videoEnabled;
    // Telemetry
    uint16_t dataSize;
    Ack ack;
    ConnStatus_t connStatus;
    DroneTlm droneTlm;
};

struct Packet {
    explicit Packet(PacketType packType = PacketType_Invalid, const PacketPayload &packPayload = {0})
        : type(packType), payload(packPayload){}

    PacketType_t type;
    PacketPayload payload;
};

#pragma pack(pop)

// BASE DRONE COMMANDS
static constexpr DroneCmd DroneCmd_HEARTBEAT = {1, 1};
static constexpr DroneCmd DroneCmd_STOP_CONTROL = {8, 1};
static constexpr DroneCmd DroneCmd_SWITCH_CAM_FRONT = {6, 1};
static constexpr DroneCmd DroneCmd_SWITCH_CAM_BACK = {6, 2};
static constexpr DroneCmd DroneCmd_ACK_PHOTO = {9, 1};
static constexpr DroneCmd DroneCmd_ACK_VIDEO = {9, 2};

// COMMON INTERFACES
struct RTSP
{
    int cseq = 1;
    string sessionId = "";
    string buff;

    static inline bool respOk(const string &resp)
    {
        return resp.rfind("RTSP/" RTSP_VER " 200 OK", 0) == 0;
    }

    static inline const string getField(const string &resp, const string &field, bool trim = true)
    {
        const string fieldStr = field + ":";
        const size_t idx = resp.find(fieldStr);
        if (idx == string::npos) {
            return "";
        }
        const size_t start = idx + fieldStr.length();
        const size_t end = resp.find(RTSP_END_PAR, start);
        string value = (end == string::npos) ? resp.substr(start) : resp.substr(start, end - start);
        if (trim) {
            const size_t first = value.find_first_not_of(" \t");
            const size_t last = value.find_last_not_of(" \t");
            value = (first == string::npos) ? "" : value.substr(first, last - first + 1);
        }
        return value;
    }

    inline size_t firstPacketSize(bool *available = nullptr) const
    {
        const size_t headerEnd = buff.find(RTSP_END_SECTION);
        if (headerEnd == string::npos) {
            return 0; // incomplete header
        }

        const string header = buff.substr(0, headerEnd);
        size_t contentLen = 0;
        try {
            const string lenStr = getField(header, "Content-Length");
            if (!lenStr.empty()) {
                contentLen = stoul(lenStr);
            }
        } catch (...) {
            contentLen = 0; // fallback
        }

        const size_t totSize = headerEnd + string(RTSP_END_SECTION).size()
                               + (contentLen > 0 ? contentLen : 0);
        if (available) {
            *available = buff.size() >= totSize;
        }
        return totSize;
    }

    inline const string readResponse()
    {
        bool available = false;
        const size_t firstPackSize = firstPacketSize(&available);
        if (!available) {
            return string(); // incomplete response
        }
        const string resp = buff.substr(0, firstPackSize);
        buff = buff.substr(firstPackSize, buff.size() - firstPackSize); // remove response read
        return resp;
    }

    // packets
    inline const string options()
    {
        return "OPTIONS " DRONE_CAM " RTSP/" RTSP_VER RTSP_END_PAR "CSeq: " + to_string(cseq++)
        + RTSP_END_PAR "User-Agent: " RTSP_USER_AGENT RTSP_END_SECTION;
    }

    inline const string describe()
    {
        return "DESCRIBE " DRONE_CAM " RTSP/" RTSP_VER RTSP_END_PAR
               "Accept: application/sdp" RTSP_END_PAR "CSeq: "
               + to_string(cseq++) + RTSP_END_PAR "User-Agent: " RTSP_USER_AGENT RTSP_END_SECTION;
    }

    inline const string setup()
    {
        return "SETUP " DRONE_CAM "/track0 RTSP/" RTSP_VER RTSP_END_PAR
               "Transport: RTP/AVP/UDP;unicast;client_port=" RTSP_CLIENT_PORTS RTSP_END_PAR "CSeq: "
               + to_string(cseq++) + RTSP_END_PAR "User-Agent: " RTSP_USER_AGENT RTSP_END_SECTION;
    }

    inline const string play()
    {
        return "PLAY " DRONE_CAM "/ RTSP/" RTSP_VER RTSP_END_PAR "Range: npt=0.000-" RTSP_END_PAR
               "CSeq: "
               + to_string(cseq++)
               + RTSP_END_PAR "User-Agent: " RTSP_USER_AGENT RTSP_END_PAR "Session: " + sessionId
               + RTSP_END_SECTION;
    }

    inline const string stop()
    {
        return "TEARDOWN " DRONE_CAM " RTSP/" RTSP_VER RTSP_END_PAR "CSeq: " + to_string(cseq++)
        + RTSP_END_PAR "User-Agent: " RTSP_USER_AGENT RTSP_END_PAR "Session: " + sessionId
            + RTSP_END_SECTION;
    }
};

class BridgeInterface {
public:

    virtual void init() = 0;
    virtual ConnStatus_t connectionStatus() const = 0;
    virtual void disconnectFromDrone() = 0;
    virtual void start() = 0;

    bool connectToDrone() {
        if(!connParams.valid()){
            return false;
        }
        const time_t now = currentTime();
        if (now - lastConnectionStart < connParams.timeout) {
            return true;
        }
        const string ssid = DRONE_WIFI_PREFIX + string(connParams.ssid);
        startConnection(ssid, DRONE_PASSW);
        lastConnectionStart = now;
        return true;
    }

    void step() {
        const time_t now = currentTime();
        if(now - lastHeartbeatSent >= HB_INTERVAL_MS){
            if(sendDroneCmd(DroneCmd_HEARTBEAT)){
                lastHeartbeatSent = now;
            }
        }
        if(now - lastFlyCmdSent >= FLY_INTERVAL_MS){
            if(sendDroneCmdData(toBytes(flyCmd))){
                lastFlyCmdSent = now;
            }
        }
    }

    bool parseCmdPacket()
    {
        Packet cmdPkt;
        crc_t cmdCrc;
        if (!readCmdPacket(&cmdPkt, &cmdCrc)) {
            // No cmd received
            return false;
        }

        if(calculate_crc(&cmdPkt, sizeof(cmdPkt)) != cmdCrc) {
            // Crc error
            sendTlmPacket(Packet(PacketType_Ack, {.ack = {.cmd = cmdPkt.type, .val = AckVal_CrcErr}}));
            return true;
        }

        bool ok = false;

        switch (cmdPkt.type){

        case PacketType_SetConnection:
            disconnectFromDrone();
            connParams = cmdPkt.payload.connParams;
            ok = true;
            break;

        case PacketType_GetConnection:
            forwardConnStatus();
            return true; // Return to not send ack

        case PacketType_DroneCmd:
            ok = sendDroneCmd(cmdPkt.payload.droneCmd);
            break;

        case PacketType_SetControls:
            flyCmd.setControls(cmdPkt.payload.controls);
            ok = true;
            break;

        case PacketType_SetVideo:
            if (cmdPkt.payload.videoEnabled) {
                ok = RTSP::respOk(sendDroneVideoCmd(rtsp.options(), RTSP_RESP_TIMEOUT_MS))
                     && RTSP::respOk(sendDroneVideoCmd(rtsp.describe(), RTSP_RESP_TIMEOUT_MS));
                if (ok) {
                    const string resp = sendDroneVideoCmd(rtsp.setup(), RTSP_RESP_TIMEOUT_MS);
                    rtsp.sessionId = RTSP::respOk(resp) ? RTSP::getField(resp, "Session") : "";
                    ok = !rtsp.sessionId.empty() && RTSP::respOk(sendDroneVideoCmd(rtsp.play(), RTSP_RESP_TIMEOUT_MS));
                }
            } else {
                ok = RTSP::respOk(sendDroneVideoCmd(rtsp.stop(), RTSP_RESP_TIMEOUT_MS));
            }
            break;

        default:
            break;
        }

        const AckVal_t ackVal = ok ? AckVal_OK : AckVal_KO;
        sendTlmPacket(Packet(PacketType_Ack, {.ack = {.cmd = cmdPkt.type, .val = ackVal}}));

        return true;
    }

    bool log(const string &msg) {
        if(msg.empty()){
            return false;
        }
        const ByteArray txt = toBytes(msg);
        const Packet tlmPkt = Packet(PacketType_TextMsg, {.dataSize = (uint16_t)txt.size()});
        return sendTlmPacket(tlmPkt, txt);
    }

    bool forwardConnStatus() {
        return sendTlmPacket(Packet(PacketType_ConnectionStat, {.connStatus = connectionStatus()}));
    }

    bool forwardDroneTlm() {
        DroneTlm droneTlm;
        if(!readDroneTlm(&droneTlm)){
            return false;
        }
        return sendTlmPacket(Packet(PacketType_DroneTlm, {.droneTlm = droneTlm}));
    }

    bool forwardDroneVideo() {
        const ByteArray videoData = readDroneVideoData();
        if(videoData.empty()){
            return false;
        }
        const Packet tlmPkt = Packet(PacketType_DroneVideo, {.dataSize = (uint16_t)videoData.size()});
        return sendTlmPacket(tlmPkt, videoData);
    }

protected:
    bool readCmdPacket(Packet *pkt, crc_t *crc) {
        static constexpr uint16_t pktSize = sizeof(*pkt) + sizeof(*crc);
        const ByteArray pktContent = readCmdPacketData(pktSize);
        if(pktContent.size() < pktSize) {
            return false;
        }
        *pkt = *(typeof(pkt))pktContent.data();
        *crc = *(typeof(crc))(pktContent.data() + sizeof(*pkt));
        return true;
    }

    bool sendTlmPacket(const Packet &pkt, const ByteArray &pktData = {}) {
        ByteArray data = toBytes(pkt);
        if(!pktData.empty()){
            data.insert(data.end(), pktData.begin(), pktData.end());
        }
        const ByteArray crc = toBytes(calculate_crc(data.data(), data.size()));
        data.insert(data.end(), crc.begin(), crc.end());
        return sendTlmPacketData(data);
    }

    bool sendDroneCmd(const DroneCmd &droneCmd) { return sendDroneCmdData(toBytes(droneCmd)); };

    bool readDroneTlm(DroneTlm *droneTlm) { return droneTlm->fromBytes(readDroneTlmData()); }

    const string sendDroneVideoCmd(const string &cmd, int timeout_ms) {
        if(!sendDroneVideoCmd(cmd)){
            return string();
        }
        bool available = false;
        const time_t now = currentTime();
        do {
            wait_ms(1);
            rtsp.buff += readDroneVideoResp();
            rtsp.firstPacketSize(&available);
        } while(!available && currentTime() - now < timeout_ms);
        return rtsp.readResponse();
    }

    virtual time_t currentTime() const = 0;
    virtual void wait_ms(time_t ms) = 0;
    virtual void startConnection(const string &ssid, const string &passw) = 0;
    virtual const ByteArray readCmdPacketData(uint16_t size) = 0;
    virtual bool sendTlmPacketData(const ByteArray &tlmData) = 0;
    virtual bool sendDroneCmdData(const ByteArray &cmdData) = 0;
    virtual const ByteArray readDroneTlmData() = 0;
    virtual const ByteArray readDroneVideoData() = 0;
    virtual bool sendDroneVideoCmd(const string &cmd) = 0;
    virtual const string readDroneVideoResp() = 0;

    ConnParams connParams = {0};
    time_t lastConnectionStart = 0;
    time_t lastHeartbeatSent = 0;
    time_t lastFlyCmdSent = 0;
    FlyCmd flyCmd;
    RTSP rtsp;
};

#endif // PROTOCOL_H
