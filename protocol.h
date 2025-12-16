#ifndef PROTOCOL_H
#define PROTOCOL_H

#include <stdint.h>

// COMMON DEFINES
#define MAX(a, b) (a > b ? a : b)
#define MIN(a, b) (a < b ? a : b)
#define STR_HELPER(x) #x
#define STR(x) STR_HELPER(x)

// DRONE CONFIG
#define DRONE_WIFI_PREFIX   "NOVA CAM DRONE-"
#define DRONE_PASSW         ""
#define DRONE_IP            "192.168.1.1"
#define DRONE_RECV_PORT     7099
#define DRONE_SEND_PORT     7099
#define DRONE_VIDEO_PORT    7070
#define DRONE_CAM           "rtsp://" DRONE_IP ":" STR(DRONE_VIDEO_PORT) "/webcam"
#define DRONE_RTP_PORT      23144
#define DRONE_RTCP_PORT     23145

// PROTOCOL CONFIG
#define FLY_CONTROL_NEUTRAL     128
#define DEF_FLY_TURN_DEAD_ZONE  24
#define HB_INTERVAL_MS          1000
#define FLY_INTERVAL_MS         50
#define TLM_SPORADIC_DATA_SIZE  15
#define MAX_VIDEO_DATA_SIZE     1500

// TYPES
typedef uint8_t fly_par_t;
typedef char ssid_t[8];
typedef uint16_t port_t;
typedef uint16_t data_size_t;

typedef uint8_t crc_t;
static inline crc_t calculate_crc(const void *data, size_t len)
{
    const uint8_t *bytes = (const uint8_t *)data;
    crc_t crc = bytes[0];
    for (int i = 1; i < len; i++) {
        crc ^= bytes[i];
    }
    return crc;
}

typedef uint8_t ConnStatus_t;
enum ConnStatus {
    Disconnected        = 0x00,
    ConnectedControl    = 0x01,
    ConnectedVideo      = 0x02,
    Connected           = ConnectedControl | ConnectedVideo
};

typedef uint8_t AckVal_t;
enum AckVal {
    AckVal_KO = 0,
    AckVal_OK = 1
};

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

typedef uint8_t TlmFdbkType_t;
enum TlmFdbkType {
    FdbkType_Photo = 77,
    FdbkType_Video = 88
};

typedef uint8_t ClientPacketType_t;
enum ClientPacketType {
    PacketType_Invalid,
    // Command
    PacketType_SetConnection,
    PacketType_GetConnection,
    PacketType_SetVideo,
    PacketType_DroneCmd,
    PacketType_FlyCmd,
    // Response
    PacketType_Ack,
    PacketType_ConnectionStat,
    PacketType_DroneTlm,
    PacketType_DroneVideo
};

// PROTOCOL STRUCTURES DEFINITION
#pragma pack(push, 1)

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
        crc = calculate_crc(&flyControls, sizeof(flyControls));
    }
    uint8_t header          = 0x03;
    uint8_t start           = 0x66;
    FlyControls flyControls = FlyControls();
    crc_t crc               = 0x00; // use calculate_crc on flyControls
    uint8_t end             = 0x99;
};

struct DroneCmd
{
    uint8_t type;
    uint8_t val;
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

struct Ack {
    ClientPacketType_t cmd;
    AckVal_t val;
};

struct ClientPacket {
    ClientPacketType_t type = PacketType_Invalid;
    union Data {
        // Commands
        ConnParams connParams;
        uint8_t videoEnabled;
        DroneCmd droneCmd;
        FlyCmd flyCmd;
        // Response
        Ack ack;
        ConnStatus_t connStatus;
        DroneTlm droneTlm;
        data_size_t videoPayloadSize;
    } data = Data{0};
};

struct VideoPayload {
    uint8_t data[MAX_VIDEO_DATA_SIZE];
};

struct VideoData {
    int wIdx    = 0;
    int size    = 0;
    VideoPayload payload;
    inline int remaningSize() const { return size - wIdx; }
    inline char *writePtr() const { return (char *)(payload.data + wIdx); }
};

#pragma pack(pop)

// BASE DRONE COMMANDS
static constexpr DroneCmd DroneCmd_HEARTBEAT        = {1, 1};
static constexpr DroneCmd DroneCmd_STOP_CONTROL     = {8, 1};
static constexpr DroneCmd DroneCmd_SWITCH_CAM_FRONT = {6, 1};
static constexpr DroneCmd DroneCmd_SWITCH_CAM_BACK  = {6, 2};
static constexpr DroneCmd DroneCmd_ACK_PHOTO        = {9, 1};
static constexpr DroneCmd DroneCmd_ACK_VIDEO        = {9, 2};

// COMMON FUNCTIONS
template <typename T, typename F = T>
static inline void set_flag(T &flags, F flag, bool en)
{
    if (en) {
        flags |= (T)(flag);
    } else {
        flags &= (T)(~flag);
    }
}

#endif // PROTOCOL_H
