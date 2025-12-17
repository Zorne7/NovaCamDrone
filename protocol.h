#ifndef PROTOCOL_H
#define PROTOCOL_H

#include <stdint.h>
#include <string>

using namespace std;

// COMMON DEFINES
#define MAX(a, b) (a > b ? a : b)
#define MIN(a, b) (a < b ? a : b)
#define STR_HELPER(x) #x
#define STR(x) STR_HELPER(x)

// DRONE CONFIG
#define DRONE_WIFI_PREFIX       "NOVA CAM DRONE-"
#define DRONE_PASSW             ""
#define DRONE_IP                "192.168.1.1"
#define DRONE_CTRL_PORT         7099
#define DRONE_VIDEO_PORT        7070
#define DRONE_CAM               "rtsp://" DRONE_IP ":" STR(DRONE_VIDEO_PORT) "/webcam"
#define DRONE_RTP_PORT          23144
#define DRONE_RTCP_PORT         23145

// PROTOCOL CONFIG
#define FLY_CONTROL_NEUTRAL     128
#define DEF_FLY_TURN_DEAD_ZONE  24
#define TLM_SPORADIC_DATA_SIZE  15
#define RTSP_VER                "1.0"
#define RTSP_USER_AGENT         "Lavf57.71.100"
#define RTSP_END_PAR            "\r\n"
#define RTSP_END_SECTION        RTSP_END_PAR RTSP_END_PAR
#define RTSP_CLIENT_PORTS       STR(DRONE_RTP_PORT) "-" STR(DRONE_RTCP_PORT)

// TIMING CONFIG
#define HB_INTERVAL_MS              1000
#define FLY_INTERVAL_MS             50
#define RTSP_KEEPALIVE_INTERVAL_MS  5000

// TYPES
typedef uint8_t fly_par_t;
typedef char ssid_t[8];
typedef uint16_t port_t;
typedef uint8_t crc_t;
static crc_t CRC_Calculate(const void *data, size_t len);

typedef uint8_t ProtocolChannel_t;
enum ProtocolChannel {
    Channel_None                = 0x00,
    Channel_Ctrl_UDP            = 0x01,
    Channel_RTSP_TCP            = 0x02,
    Channel_RTP_UDP             = 0x04,
    Channel_RTCP_UDP            = 0x08,
};

typedef uint8_t BridgePacketType_t;
enum BridgePacketType {
    PacketType_Invalid          = 0x00,
    PacketType_Forward          = 0x01,
    // Command to Bridge
    PacketType_SetConnection    = 0x02,
    PacketType_GetConnection    = 0x03,
    // Response from Bridge
    PacketType_Ack              = 0x04,
    PacketType_ConnectionStat   = 0x05,
};

// PROTOCOL STRUCTURES DEFINITION
#pragma pack(push, 1)

typedef uint8_t FlyControlFlags_t;
enum FlyControlFlags {
    ControlFlag_None            = 0x00,
    ControlFlag_FastFly         = 1 << 0,
    ControlFlag_FastDrop        = 1 << 1,
    ControlFlag_EmergencyStop   = 1 << 2,
    ControlFlag_CircleTurnEnd   = 1 << 3,
    ControlFlag_NoHeadMode      = 1 << 4,
    ControlFlag_Unlock          = 1 << 5,
    ControlFlag_Unknown         = 1 << 6,
    ControlFlag_GyroCorrection  = 1 << 7
};

struct FlyControls
{
    fly_par_t controlByte1        = FLY_CONTROL_NEUTRAL;  // Control left/right
    fly_par_t controlByte2        = FLY_CONTROL_NEUTRAL;  // Control front/back
    fly_par_t controlAccelerator  = FLY_CONTROL_NEUTRAL;  // Accelerator
    fly_par_t controlTurn         = FLY_CONTROL_NEUTRAL;  // Rotation
    FlyControlFlags_t flags       = ControlFlag_None;     // Modes/Actions

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
    explicit FlyCmd(const FlyControls &controls = FlyControls()) : flyControls(controls) {
        flyControls.normalize();
        crc = CRC_Calculate(&flyControls, sizeof(flyControls));
    }
    uint8_t header          = 0x03;
    uint8_t start           = 0x66;
    FlyControls flyControls = FlyControls();
    crc_t crc               = 0x00; // use CRC_Calculate on flyControls
    uint8_t end             = 0x99;
};

struct DroneCmd
{
    uint8_t type;
    uint8_t val;
};

typedef uint8_t TlmFdbkType_t;
enum TlmFdbkType {
    FdbkType_Photo = 77,
    FdbkType_Video = 88
};

struct DroneTlm
{
    uint8_t resolution;
    uint8_t switchCameraReset;
    TlmFdbkType_t fdbkType;
    uint8_t numPhoto;
    uint8_t numVideo;
    uint8_t sporadicData[TLM_SPORADIC_DATA_SIZE];
};

struct ConnParams
{
    ssid_t ssid;
    uint16_t timeout;
    inline bool valid() const { return ssid[0] != 0 && timeout > 0; }
};

struct BridgePacketId {
    explicit BridgePacketId(BridgePacketType_t type = PacketType_Invalid, ProtocolChannel_t chan = Channel_None)
        : val(((type & 0x0F) << 4) | (chan & 0x0F)) {}
    uint8_t val;
    inline BridgePacketType_t type() const { return (val >> 4) & 0x0F; }
    inline ProtocolChannel_t chan() const { return val & 0x0F; }
};

typedef uint8_t AckVal_t;
enum AckVal {
    AckVal_KO = 0,
    AckVal_OK = 1
};

struct Ack {
    BridgePacketId cmd;
    AckVal_t val;
};

struct BridgePacketHeader {
    explicit BridgePacketHeader(BridgePacketId packId = BridgePacketId(), uint16_t packDataSize = 0)
        : id(packId), dataSize(packDataSize) {};
    BridgePacketId id;
    uint16_t dataSize;
};

#pragma pack(pop)

// BASE DRONE COMMANDS
static constexpr DroneCmd DroneCmd_HEARTBEAT        = {1, 1};
static constexpr DroneCmd DroneCmd_STOP_CONTROL     = {8, 1};
static constexpr DroneCmd DroneCmd_SWITCH_CAM_FRONT = {6, 1};
static constexpr DroneCmd DroneCmd_SWITCH_CAM_BACK  = {6, 2};
static constexpr DroneCmd DroneCmd_ACK_PHOTO        = {9, 1};
static constexpr DroneCmd DroneCmd_ACK_VIDEO        = {9, 2};

// RTPS PACKETS
static inline string RTSP_Options(int &cseq)
{
    return "OPTIONS " DRONE_CAM " RTSP/" RTSP_VER RTSP_END_PAR
           "CSeq: " + to_string(cseq++) + RTSP_END_PAR
           "User-Agent: " RTSP_USER_AGENT RTSP_END_SECTION;
}

static inline string RTSP_Describe(int &cseq)
{
    return "DESCRIBE " DRONE_CAM " RTSP/" RTSP_VER RTSP_END_PAR
           "Accept: application/sdp" RTSP_END_PAR
           "CSeq: " + to_string(cseq++) + RTSP_END_PAR
           "User-Agent: " RTSP_USER_AGENT RTSP_END_SECTION;
}

static inline string RTSP_Setup(int &cseq)
{
    return "SETUP " DRONE_CAM "/track0 RTSP/" RTSP_VER RTSP_END_PAR
           "Transport: RTP/AVP/UDP;unicast;client_port=" RTSP_CLIENT_PORTS RTSP_END_PAR
           "CSeq: " + to_string(cseq++) + RTSP_END_PAR
           "User-Agent: " RTSP_USER_AGENT RTSP_END_SECTION;
}

static inline string RTSP_Play(int &cseq, const string &sessionId)
{
    return "PLAY " DRONE_CAM "/ RTSP/" RTSP_VER RTSP_END_PAR
           "Range: npt=0.000-" RTSP_END_PAR
           "CSeq: " + to_string(cseq++) + RTSP_END_PAR
           "User-Agent: " RTSP_USER_AGENT RTSP_END_PAR
           "Session: " + sessionId + RTSP_END_SECTION;
}

static inline string RTSP_Stop(int &cseq, const string &sessionId)
{
    return "TEARDOWN " DRONE_CAM " RTSP/" RTSP_VER RTSP_END_PAR
           "CSeq: " + to_string(cseq++) + RTSP_END_PAR
           "User-Agent: " RTSP_USER_AGENT RTSP_END_PAR
           "Session: " + sessionId + RTSP_END_SECTION;
}

// COMMON FUNCTIONS
static crc_t CRC_Calculate(const void *data, size_t len)
{
    const uint8_t *bytes = (const uint8_t *)data;
    crc_t crc = bytes[0];
    for (int i = 1; i < len; i++) {
        crc ^= bytes[i];
    }
    return crc;
}

template <typename T, typename F = T>
static inline void Flag_Set(T &flags, F flag, bool en)
{
    if (en) {
        flags |= (T)(flag);
    } else {
        flags &= (T)(~flag);
    }
}

static inline bool RTSP_RespOk(const string &resp)
{
    return resp.rfind("RTSP/" RTSP_VER " 200 OK", 0) == 0;
}

static inline string RTSP_GetField(const string &resp, const string &field)
{
    const string fieldStr = field + ":";
    const size_t idx = resp.find(fieldStr);
    if (idx == string::npos){
        return "";
    }
    const size_t start = idx + fieldStr.length();
    const size_t end = resp.find(RTSP_END_PAR, start);
    return end == string::npos ? resp.substr(start) : resp.substr(start, end - start);
}

#endif // PROTOCOL_H
