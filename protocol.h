#ifndef PROTOCOL_H
#define PROTOCOL_H

#include <stdint.h>

#define MAX(a, b) (a > b ? a : b)
#define MIN(a, b) (a < b ? a : b)

typedef uint8_t crc_t;

enum FlyParamsFlags {
    None = 0x00,
    FastFly = 1 << 0,
    FastDrop = 1 << 1,
    EmergencyStop = 1 << 2,
    CircleTurnEnd = 1 << 3,
    NoHeadMode = 1 << 4,
    Unlock = 1 << 5,
    GyroCorrection = 1 << 7
};

enum TlmNumType { TypePhoto = 77, TypeVideo = 88 };

enum ClientCmdType {
    TypeSetConnection = 0x01,
    TypeGetConnection = 0x02,
    TypeDroneCmd = 0x03,
    TypeFlyCmd = 0x04,
};

enum ClientRespType { TypeAck = 0x01, TypeConnectionStat = 0x02, TypeDroneTlm = 0x03 };

static constexpr uint8_t FLY_PAR_NEUTRAL = 128;
static constexpr uint8_t TURN_DEAD_ZONE = 24;

#pragma pack(push, 1)

struct FlyParams
{
    uint8_t controlByte1 = FLY_PAR_NEUTRAL;       // Control left/right
    uint8_t controlByte2 = FLY_PAR_NEUTRAL;       // Control front/back
    uint8_t controlAccelerator = FLY_PAR_NEUTRAL; // Accelerator
    uint8_t controlTurn = FLY_PAR_NEUTRAL;        // Rotation
    uint8_t flags = None;                         // FlyParamsFlags

    void normalize()
    {
        controlByte1 = MAX(controlByte1, 1);
        controlByte2 = MAX(controlByte2, 1);
        controlAccelerator = controlAccelerator == 1 ? 0 : controlAccelerator;
        controlTurn = (controlTurn >= (FLY_PAR_NEUTRAL - TURN_DEAD_ZONE)
                       && controlTurn <= (FLY_PAR_NEUTRAL + TURN_DEAD_ZONE))
                          ? FLY_PAR_NEUTRAL
                          : MAX(controlTurn, 1);
    }
};

struct FlyCmd
{
    const uint8_t header = 0x03;
    const uint8_t start = 0x66;
    FlyParams flyParams;
    uint8_t crc = 0x00;
    const uint8_t end = 0x99;
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
    uint8_t data[15];
};

static constexpr int MIN_DRONE_TLM_SIZE = sizeof(DroneTlm) - sizeof(DroneTlm::data);

struct ConnectionParams
{
    char wifiSsid[25] = "";
    char wifiPassw[25] = "";
    char ip[15] = "";
    uint16_t recvPort;
    uint16_t sendPort;
    uint16_t timeout = 1000;

    bool valid() { return wifiSsid[0] != 0 && ip[0] != 0 && recvPort > 0 && sendPort > 0; }
};

struct ClientCmd
{
    uint8_t type; // ClientCmdType
    union Data {
        ConnectionParams connParams;
        DroneCmd droneCmd;
        FlyCmd flyCmd;
    } data = Data{0};
};

struct ClientResp
{
    uint8_t type; // ClientRespType
    union Data {
        uint8_t ack;
        uint8_t connected;
        DroneTlm droneTlm;
    } data = Data{0};
};

#pragma pack(pop)

static constexpr DroneCmd DroneCmd_HEARTBEAT = {1, 1};
static constexpr DroneCmd DroneCmd_STOP_CONTROL = {8, 1};
static constexpr DroneCmd DroneCmd_SWITCH_CAM_FRONT = {6, 1};
static constexpr DroneCmd DroneCmd_SWITCH_CAM_BACK = {6, 2};
static constexpr DroneCmd DroneCmd_ACK_PHOTO = {9, 1};
static constexpr DroneCmd DroneCmd_ACK_VIDEO = {9, 2};

static inline crc_t calculateCrc(const void *data, size_t len)
{
    crc_t crc = 0x00;
    for (int i = 0; i < len; i++) {
        crc = crc ^ ((const uint8_t *)data)[i];
    }
    return crc;
}

#endif // PROTOCOL_H
