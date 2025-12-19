#include "dronecontroller.h"

#include <QDebug>
#include <QEventLoop>

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
    connect(&serial, &QSerialPort::errorOccurred, this, [=](QSerialPort::SerialPortError error) {
        if (error != QSerialPort::NoError) {
            emit errorOccurred(serial.errorString());
        }
    });

    connect(&decoder, &Decoder::errorOccurred, this, &DroneController::errorOccurred);
    connect(&decoder, &Decoder::frameReady, this, &DroneController::frameReady);

    decoder.init();
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
    if (tlmData.size() < sizeof(DroneTlm)) {
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
    if (tlmData.size() > sizeof(DroneTlm)) {
        //qDebug() << "TLM:" << tlmData.mid(sizeof(DroneTlm)).toHex();
    }
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
            decoder.decodeVideoData(payload);
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
