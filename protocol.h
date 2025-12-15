#ifndef PROTOCOL_H
#define PROTOCOL_H

#include <stdint.h>

#define MAX(a, b) (a > b ? a : b)
#define MIN(a, b) (a < b ? a : b)

typedef char ssid_t[8];
typedef uint8_t crc_t;
typedef uint16_t port_t;

enum FlyParamsFlags {
    FlagNone            = 0x00,
    FlagFastFly         = 1 << 0,
    FlagFastDrop        = 1 << 1,
    FlagEmergencyStop   = 1 << 2,
    FlagCircleTurnEnd   = 1 << 3,
    FlagNoHeadMode      = 1 << 4,
    FlagUnlock          = 1 << 5,
    FlagGyroCorrection  = 1 << 7
};

enum TlmNumType {
    TlmType_Photo = 77,
    TlmType_Video = 88
};

enum ClientPacketType {
    // Command
    PacketType_SetConnection = 0x01,
    PacketType_GetConnection,
    PacketType_DroneCmd,
    PacketType_FlyCmd,
    // Response
    PacketType_Ack,
    PacketType_ConnectionStat,
    PacketType_DroneTlm
};

static constexpr char *DRONE_WIFI_PREFIX    = "NOVA CAM DRONE-";
static constexpr char *DRONE_PASSW          = "";
static constexpr char *DRONE_IP             = "192.168.1.1";
static constexpr port_t DRONE_RECV_PORT     = 7099;
static constexpr port_t DRONE_SEND_PORT     = 8888;
static constexpr uint8_t FLY_PAR_NEUTRAL    = 128;
static constexpr int HB_INTERVAL_MS         = 1000;
static constexpr int FLY_INTERVAL_MS        = 50;

#pragma pack(push, 1)

struct FlyParams
{
    uint8_t controlByte1        = FLY_PAR_NEUTRAL; // Control left/right
    uint8_t controlByte2        = FLY_PAR_NEUTRAL; // Control front/back
    uint8_t controlAccelerator  = FLY_PAR_NEUTRAL; // Accelerator
    uint8_t controlTurn         = FLY_PAR_NEUTRAL; // Rotation
    uint8_t flags               = FlagNone;        // FlyParamsFlags

    void normalize(uint8_t turnDeadZone = 24)
    {
        controlByte1 = MAX(controlByte1, 1);
        controlByte2 = MAX(controlByte2, 1);
        controlAccelerator = controlAccelerator == 1 ? 0 : controlAccelerator;
        controlTurn = (controlTurn >= (FLY_PAR_NEUTRAL - turnDeadZone)
                       && controlTurn <= (FLY_PAR_NEUTRAL + turnDeadZone))
                          ? FLY_PAR_NEUTRAL
                          : MAX(controlTurn, 1);
    }
};

struct FlyCmd
{
    const uint8_t header    = 0x03;
    const uint8_t start     = 0x66;
    FlyParams flyParams     = FlyParams();
    crc_t crc               = 0x00; // use calculateCrc on flyParams
    const uint8_t end       = 0x99;
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
    uint8_t numType; // TlmNumType
    uint8_t numPhoto;
    uint8_t numVideo;
    uint8_t sporadicData[15];
};

struct ConnParams
{
    ssid_t ssid;
    uint16_t timeout;
};

struct Ack {
    uint8_t cmd; // ClientPacketType
    uint8_t res;
};

struct ClientPacket
{
    uint8_t type; // ClientPacketType
    union Data {
        ConnParams connParams;
        DroneCmd droneCmd;
        FlyCmd flyCmd;
        Ack ack;
        uint8_t connected;
        DroneTlm droneTlm;
    } data = Data{0};
};

#pragma pack(pop)

static constexpr DroneCmd DroneCmd_HEARTBEAT        = {1, 1};
static constexpr DroneCmd DroneCmd_STOP_CONTROL     = {8, 1};
static constexpr DroneCmd DroneCmd_SWITCH_CAM_FRONT = {6, 1};
static constexpr DroneCmd DroneCmd_SWITCH_CAM_BACK  = {6, 2};
static constexpr DroneCmd DroneCmd_ACK_PHOTO        = {9, 1};
static constexpr DroneCmd DroneCmd_ACK_VIDEO        = {9, 2};

static inline crc_t calculateCrc(const void *data, size_t len)
{
    crc_t crc = 0x00;
    const uint8_t *bytes = (const uint8_t *)data;
    for (int i = 0; i < len; i++) {
        crc ^= bytes[i];
    }
    return crc;
}

#endif // PROTOCOL_H
