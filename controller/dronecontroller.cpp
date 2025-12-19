#include "dronecontroller.h"

#include <QEventLoop>
#include <QDebug>

template<typename T>
static inline const QByteArray toData(const T &s, int size = -1)
{
    if constexpr (std::is_same_v<T, string>) {
        return QByteArray::fromStdString(s);
    } else {
        return QByteArray(reinterpret_cast<const char *>(&s), size < 0 ? sizeof(s) : size);
    }
}

DroneController::DroneController(QObject *parent)
    : QObject{parent}
{
    timerHb.setInterval(HB_INTERVAL_MS);
    timerHb.setSingleShot(false);
    connect(&timerHb, &QTimer::timeout, this, &DroneController::sendHeartbeat);

    timerFly.setInterval(FLY_INTERVAL_MS);
    timerFly.setSingleShot(false);
    connect(&timerFly, &QTimer::timeout, this, &DroneController::sendFlyCmd);

    connect(&serial, &QSerialPort::readyRead, this, &DroneController::readSerial);
    connect(&serial, &QSerialPort::errorOccurred, this, [=]() {
        emit errorOccurred(serial.errorString());
    });
}

bool DroneController::setSerial(const QString &portName, int baudRate)
{
    if (serial.isOpen()) {
        serial.close();
    }
    if (baudRate <= 0 || portName.isEmpty()) {
        return true;
    }
    serial.setPortName(portName);
    serial.setBaudRate(baudRate);
    return serial.open(QIODevice::ReadWrite);
}

void DroneController::resetSerial()
{
    serial.close();
    lastHeader = BridgePacketHeader();
    bool ok = serial.open(QIODevice::ReadWrite);
    if (!ok) {
        emit errorOccurred("Error resetting serial");
    }
}

bool DroneController::sendCmd(const BridgePacketId &id, const QByteArray &data)
{
    if (!serial.isOpen()) {
        emit errorOccurred("Error sending cmd " + hex(id.val) + ": serial closed");
        return false;
    }
    const BridgePacketHeader header(id, data.size());
    const QByteArray cmdData = toData(header) + data;
    bool ok = serial.write(cmdData) == cmdData.size();
    if (ok) {
        serial.flush();
    } else {
        emit errorOccurred("Error sending cmd " + hex(id.val) + " [" + QString::number(data.size())
                           + "]: " + data.toHex());
    }
    return ok;
}

void DroneController::sendSetConnection(const ConnParams &connParams)
{
    sendCmd(BridgePacketId(PacketType_SetConnection), toData(connParams));
}

void DroneController::sendGetConnection()
{
    sendCmd(BridgePacketId(PacketType_GetConnection));
}

void DroneController::setHeartbeat()
{
    if (timerHb.isActive()) {
        timerHb.stop();
    } else {
        timerHb.start();
    }
}

void DroneController::sendHeartbeat()
{
    sendCmd(BridgePacketId(PacketType_Forward, Channel_Ctrl_UDP), toData(DroneCmd_HEARTBEAT));
}

void DroneController::setFlyCmd()
{
    if (timerFly.isActive()) {
        timerFly.stop();
    } else {
        timerFly.start();
    }
}

void DroneController::sendFlyCmd()
{
    sendCmd(BridgePacketId(PacketType_Forward, Channel_Ctrl_UDP), toData(FlyCmd(flyControls)));
}

bool DroneController::setVideo(bool enabled)
{
    static const BridgePacketId id = BridgePacketId(PacketType_Forward, Channel_RTSP_TCP);
    bool ok;
    if (enabled) {
        ok = sendCmd(id, toData(rtsp.options())) && waitRtspResponse(RTSP_RESP_TIMEOUT_MS)
             && RTSP::respOk(rtsp.readResponse()) && sendCmd(id, toData(rtsp.describe()))
             && waitRtspResponse(RTSP_RESP_TIMEOUT_MS) && RTSP::respOk(rtsp.readResponse())
             && sendCmd(id, toData(rtsp.setup())) && waitRtspResponse(RTSP_RESP_TIMEOUT_MS);
        if (ok) {
            const string resp = rtsp.readResponse();
            rtsp.sessionId = RTSP::respOk(resp) ? toData(RTSP::getField(resp, "Session")) : "";
            ok = !rtsp.sessionId.empty() && sendCmd(id, toData(rtsp.play()))
                 && waitRtspResponse(RTSP_RESP_TIMEOUT_MS) && RTSP::respOk(rtsp.readResponse());
        }
    } else {
        ok = sendCmd(id, toData(rtsp.stop())) && waitRtspResponse(RTSP_RESP_TIMEOUT_MS)
             && RTSP::respOk(rtsp.readResponse());
    }

    return ok;
}

void DroneController::sendStopControl()
{
    sendCmd(BridgePacketId(PacketType_Forward, Channel_Ctrl_UDP), toData(DroneCmd_STOP_CONTROL));
}

void DroneController::sendSwitchCamFront()
{
    sendCmd(BridgePacketId(PacketType_Forward, Channel_Ctrl_UDP), toData(DroneCmd_SWITCH_CAM_FRONT));
}

void DroneController::sendSwitchCamBack()
{
    sendCmd(BridgePacketId(PacketType_Forward, Channel_Ctrl_UDP), toData(DroneCmd_SWITCH_CAM_BACK));
}

void DroneController::sendAckPhoto()
{
    sendCmd(BridgePacketId(PacketType_Forward, Channel_Ctrl_UDP), toData(DroneCmd_ACK_PHOTO));
}

void DroneController::sendAckVideo()
{
    sendCmd(BridgePacketId(PacketType_Forward, Channel_Ctrl_UDP), toData(DroneCmd_ACK_VIDEO));
}

void DroneController::parseDroneTlm(const QByteArray &tlmData)
{
    if(tlmData.size() < sizeof(DroneTlm)){
        return;
    }
    const DroneTlm *tlm = reinterpret_cast<const DroneTlm *>(tlmData.data());
    switch (tlm->fdbkType) {
    case FdbkType_Photo:
        sendAckPhoto(); // TODO: send ack only if necessary
        break;
    case FdbkType_Video:
        sendAckVideo(); // TODO: send ack only if necessary
        break;
    default:
        break;
    }
    if(tlmData.size() > sizeof(DroneTlm)){
        //qDebug() << "TLM:" << tlmData.mid(sizeof(DroneTlm)).toHex();
    }
}

void DroneController::parseDroneVideo(const QByteArray &videoData)
{
    // --- DEFAULT TABLES ---

    static const uint8_t defaultDQT[] = {
        // Luminance
        0xFF,0xDB,0x00,0x43,0x00,
        0x10,0x0B,0x0C,0x0E,0x0C,0x0A,0x10,0x0E,
        0x0D,0x0E,0x12,0x11,0x10,0x13,0x18,0x28,
        0x1A,0x18,0x16,0x16,0x18,0x31,0x23,0x25,
        0x1D,0x28,0x3A,0x33,0x3D,0x3C,0x39,0x33,
        0x38,0x37,0x40,0x48,0x5C,0x4E,0x40,0x44,
        0x57,0x45,0x37,0x38,0x50,0x6D,0x51,0x57,
        0x5F,0x62,0x67,0x68,0x67,0x3E,0x4D,0x71,
        0x79,0x70,0x64,0x78,0x5C,0x65,0x67,0x63,

        // Chrominance
        0xFF,0xDB,0x00,0x43,0x01,
        0x11,0x12,0x12,0x18,0x15,0x18,0x2F,0x1A,
        0x1A,0x2F,0x63,0x42,0x38,0x42,0x63,0x63,
        0x63,0x63,0x63,0x63,0x63,0x63,0x63,0x63,
        0x63,0x63,0x63,0x63,0x63,0x63,0x63,0x63,
        0x63,0x63,0x63,0x63,0x63,0x63,0x63,0x63,
        0x63,0x63,0x63,0x63,0x63,0x63,0x63,0x63,
        0x63,0x63,0x63,0x63,0x63,0x63,0x63,0x63
    };

    static const uint8_t defaultDHT[] = {
        0xFF,0xC4,0x01,0xA2,
        // DC Luminance
        0x00,
        0x00,0x01,0x05,0x01,0x01,0x01,0x01,0x01,
        0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
        0x00,0x01,0x02,0x03,0x04,0x05,0x06,0x07,
        0x08,0x09,0x0A,0x0B,

        // AC Luminance
        0x10,
        0x00,0x03,0x01,0x01,0x01,0x01,0x01,0x01,
        0x01,0x01,0x01,0x00,0x00,0x00,0x00,0x00,
        0x00,0x01,0x02,0x03,0x11,0x04,0x05,0x21,
        0x31,0x06,0x12,0x41,0x51,0x07,0x61,0x71,
        0x13,0x22,0x32,0x81,0x08,0x14,0x42,0x91,
        0xA1,0xB1,0xC1,0x09,0x23,0x33,0x52,0xF0,
        0x15,0x62,0x72,0xD1,0x0A,0x16,0x24,0x34,
        0xE1,0x25,0xF1,0x17,0x18,0x19,0x1A,0x26,
        0x27,0x28,0x29,0x2A,0x35,0x36,0x37,0x38,
        0x39,0x3A,0x43,0x44,0x45,0x46,0x47,0x48,
        0x49,0x4A,0x53,0x54,0x55,0x56,0x57,0x58,
        0x59,0x5A,0x63,0x64,0x65,0x66,0x67,0x68,
        0x69,0x6A,0x73,0x74,0x75,0x76,0x77,0x78,
        0x79,0x7A,0x82,0x83,0x84,0x85,0x86,0x87,
        0x88,0x89,0x8A,0x92,0x93,0x94,0x95,0x96,
        0x97,0x98,0x99,0x9A,0xA2,0xA3,0xA4,0xA5,
        0xA6,0xA7,0xA8,0xA9,0xAA,0xB2,0xB3,0xB4,
        0xB5,0xB6,0xB7,0xB8,0xB9,0xBA,0xC2,0xC3,
        0xC4,0xC5,0xC6,0xC7,0xC8,0xC9,0xCA,0xD2,
        0xD3,0xD4,0xD5,0xD6,0xD7,0xD8,0xD9,0xDA,
        0xE2,0xE3,0xE4,0xE5,0xE6,0xE7,0xE8,0xE9,
        0xEA,0xF2,0xF3,0xF4,0xF5,0xF6,0xF7,0xF8,
        0xF9,0xFA
    };

    static const uint8_t defaultSOS[] = {
        0xFF,0xDA,0x00,0x0C,
        0x03,
        0x01,0x00,
        0x02,0x11,
        0x03,0x11,
        0x00,0x3F,0x00
    };

    // --- PARSING RTP/JPEG ---

    const uint8_t *p = reinterpret_cast<const uint8_t*>(videoData.data());
    int len = videoData.size();

    if (len < 20) return;

    uint32_t ts = (p[4] << 24) | (p[5] << 16) | (p[6] << 8) | p[7];

    const uint8_t *jpeg = p + 20;
    int jpegLen = len - 20;

    if (ts != frame.timestamp) {
        if (!frame.buffer.isEmpty()) {
            QByteArray jpegOut;
            // SOI
            jpegOut.append("\xFF\xD8", 2);

            // DQT
            jpegOut.append((const char*)defaultDQT, sizeof(defaultDQT));
            // DHT
            jpegOut.append((const char*)defaultDHT, sizeof(defaultDHT));
            // SOF0 (width/height dinamici)
            const uint16_t w = 640;
            const uint16_t h = 480;
            const uint8_t sof0[] = {
                0xFF,0xC0,0x00,0x11,
                0x08,
                uint8_t(h >> 8), uint8_t(h & 0xFF),
                uint8_t(w >> 8), uint8_t(w & 0xFF),
                0x03,
                0x01,0x11,0x00,
                0x02,0x11,0x01,
                0x03,0x11,0x01
            };
            jpegOut.append((const char*)sof0, sizeof(sof0));
            // SOS
            jpegOut.append((const char*)defaultSOS, sizeof(defaultSOS));
            // Scan data
            jpegOut.append(frame.buffer);
            // EOI
            jpegOut.append("\xFF\xD9", 2);

            emit frameReady(jpegOut);
        }

        frame.timestamp = ts;
        frame.buffer.clear();
    }

    frame.buffer.append((const char*)jpeg, jpegLen);
}

void DroneController::processData()
{
    const BridgePacketId packetId = lastHeader.id;
    const QByteArray payload = channelBuffMap.take(packetId.chan());
    lastHeader = BridgePacketHeader();

    switch (packetId.type()) {
    case PacketType_Ack: {
        if (payload.size() != sizeof(Ack)) {
            emit errorOccurred("Invalid payload of Ack received");
            break;
        }
        const Ack *ack = reinterpret_cast<const Ack *>(payload.data());
        emit ackRecv(*ack);
        break;
    }

    case PacketType_ConnectionStat: {
        if (payload.size() != sizeof(status_t)) {
            emit errorOccurred("Invalid payload of ConnectionStatus received");
            break;
        }
        const status_t *status = reinterpret_cast<const status_t *>(payload.data());
        emit connStatusRecv(*status);
        break;
    }

    case PacketType_Forward: {
        switch (packetId.chan()) {
        case Channel_Ctrl_UDP:
            parseDroneTlm(payload);
            break;
        case Channel_RTSP_TCP: {
            rtsp.buff.append(payload.toStdString());
            bool available = false;
            rtsp.firstPacketSize(&available);
            if (available) {
                emit rtspResponseRecv();
            }
            break;
        }
        case Channel_RTP_UDP:
            parseDroneVideo(payload);
            break;
        case Channel_RTCP_UDP:
            // ignore
            break;
        default:
            emit errorOccurred("Unknown channel type: " + hex(packetId.chan()));
            resetSerial();
            break;
        }
        break;
    }

    default:
        emit errorOccurred("Unknown packet type: " + hex(packetId.type()));
        resetSerial();
        break;
    }
}

void DroneController::readSerial()
{
    for (int bytesAvailable = serial.bytesAvailable(); bytesAvailable > 0;
         bytesAvailable = serial.bytesAvailable()) {
        if (lastHeader.id.type() == PacketType_Invalid) {
            if (bytesAvailable < sizeof(lastHeader)) {
                break;
            }
            int r = serial.read(reinterpret_cast<char *>(&lastHeader), sizeof(lastHeader));
            if (r < sizeof(lastHeader)) {
                emit errorOccurred("Error reading packet header from serial");
                resetSerial();
                break;
            }
            bytesAvailable -= r;
        }

        const ProtocolChannel_t chan = lastHeader.id.chan();
        const int size = MIN(bytesAvailable, lastHeader.dataSize - channelBuffMap[chan].size());
        if (size > 0) {
            const QByteArray data = serial.read(size);
            if (data.size() < size) {
                emit errorOccurred("Error reading packet payload from serial");
                resetSerial();
                break;
            }
            channelBuffMap[chan].push_back(data);
        }

        if (channelBuffMap[chan].size() >= lastHeader.dataSize) {
            processData();
        }
    }
}

bool DroneController::waitRtspResponse(int timeout_ms)
{
    bool available;
    rtsp.firstPacketSize(&available);
    if (available) {
        return true;
    }
    QEventLoop loop;
    QTimer timer;
    QObject::connect(&timer, &QTimer::timeout, &loop, &QEventLoop::quit);
    QObject::connect(this, &DroneController::rtspResponseRecv, &loop, &QEventLoop::quit);
    timer.start(timeout_ms);
    loop.exec();
    return timer.isActive();
}
