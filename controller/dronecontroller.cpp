#include "dronecontroller.h"

#include <QDebug>
#include <QEventLoop>
#include <QTimer>

DroneController::DroneController(QObject *parent)
    : QObject{parent}
{
    connect(&port, &QSerialPort::readyRead, this, &DroneController::readPort);
    connect(&port, &QSerialPort::errorOccurred, this, [=](QSerialPort::SerialPortError error) {
        if (error != QSerialPort::NoError) {
            if(port.isOpen()) {
                port.close();
            }
            emit errorOccurred("Port error: " + port.errorString());
        }
    });

    connect(&decoder, &Decoder::errorOccurred, this, &DroneController::errorOccurred);
    connect(&decoder, &Decoder::frameReady, this, &DroneController::frameReady);

    decoder.init();
}

bool DroneController::setPort(const QString &portName)
{
    if (port.isOpen()) {
        port.close();
        lastPacket = Packet();
        lastAck = Ack();
        for(auto &b : bufferMap){
            b.clear();
        }
    }
    if (portName.isEmpty()) {
        return true;
    }
    port.setPortName(portName);
    port.setBaudRate(BRIDGE_BITRATE);
    bool ok = port.open(QIODevice::ReadWrite);
    if (ok) {
        port.readAll(); // discard all pending data
    }
    return ok;
}

bool DroneController::resetPort()
{
    const QString portName = port.portName();
    bool ok = setPort(portName);
    if (!ok) {
        emit errorOccurred("Error resetting port " + portName);
    }
    return ok;
}

bool DroneController::sendCmd(const Packet &cmd)
{
    lastAck = Ack();
    if (!port.isOpen()) {
        emit errorOccurred("Error sending cmd " + hex(cmd.type) + ": port closed");
        return false;
    }
    bool ok = port.write(reinterpret_cast<const char *>(&cmd), sizeof(cmd)) == sizeof(cmd);
    if (ok) {
        port.flush();
    } else {
        emit errorOccurred("Error sending cmd " + hex(cmd.type) + ": write error");
    }
    return ok;
}

AckVal_t DroneController::waitAck(int timeout_ms)
{
    if (lastAck.cmd != PacketType_Invalid) {
        return lastAck.val;
    }
    QEventLoop loop;
    QTimer timer;
    QObject::connect(&timer, &QTimer::timeout, &loop, &QEventLoop::quit);
    QObject::connect(this, &DroneController::ackRecv, &loop, &QEventLoop::quit);
    QObject::connect(this, &DroneController::errorOccurred, &loop, &QEventLoop::quit);
    timer.start(timeout_ms);
    loop.exec();
    return lastAck.val;
}

bool DroneController::sendSetConnection(const ConnParams &connParams)
{
    return sendCmd(Packet(PacketType_SetConnection, {.connParams = connParams}));
}

bool DroneController::sendGetConnection()
{
    return sendCmd(Packet(PacketType_GetConnection));
}

bool DroneController::sendHeartbeat()
{
    return sendCmd(Packet(PacketType_DroneCmd, {.droneCmd = DroneCmd_HEARTBEAT}));
}

bool DroneController::sendFlyControls(const FlyControls &flyControls)
{
    return sendCmd(Packet(PacketType_SetControls, {.controls = flyControls}));
}

bool DroneController::sendStopControl()
{
    return sendCmd(Packet(PacketType_DroneCmd, {.droneCmd = DroneCmd_STOP_CONTROL}));
}

bool DroneController::sendSetVideo(bool enabled)
{
    return sendCmd(Packet(PacketType_SetVideo, {.videoEnabled = enabled}));
}

bool DroneController::sendSwitchCamFront()
{
    return sendCmd(Packet(PacketType_DroneCmd, {.droneCmd = DroneCmd_SWITCH_CAM_FRONT}));
}

bool DroneController::sendSwitchCamBack()
{
    return sendCmd(Packet(PacketType_DroneCmd, {.droneCmd = DroneCmd_SWITCH_CAM_BACK}));
}

bool DroneController::sendAckPhoto()
{
    return sendCmd(Packet(PacketType_DroneCmd, {.droneCmd = DroneCmd_ACK_PHOTO}));
}

bool DroneController::sendAckVideo()
{
    return sendCmd(Packet(PacketType_DroneCmd, {.droneCmd = DroneCmd_ACK_VIDEO}));
}

void DroneController::parseDroneTlm(const DroneTlm &tlm)
{
    switch (tlm.fdbkType) {
    case FdbkType_Photo:
        sendAckPhoto(); // TODO: send ack only if necessary
        break;
    case FdbkType_Video:
        sendAckVideo(); // TODO: send ack only if necessary
        break;
    default:
        break;
    }
}

void DroneController::processData()
{
    const Packet packet = lastPacket;
    lastPacket = Packet();

    switch (packet.type) {
    case PacketType_Ack:
        lastAck = packet.payload.ack;
        emit ackRecv(lastAck);
        break;

    case PacketType_ConnectionStat:
        emit connStatusRecv(packet.payload.connStatus);
        break;

    case PacketType_DroneTlm:
        parseDroneTlm(packet.payload.droneTlm);
        break;

    case PacketType_TextMsg:
    case PacketType_DroneVideo: {
        const QByteArray data(bufferMap[packet.type].data(), bufferMap[packet.type].size() - sizeof(crc_t));
        const QByteArray crc = bufferMap[packet.type].right(sizeof(crc_t));
        bufferMap[packet.type].clear();
        if(calculate_crc(data.data(), data.size()) != *(const crc_t *)crc.data()) {
            emit errorOccurred("Invalid data read from port: crc error");
            break;
        }
        if(packet.type == PacketType_DroneVideo){
            decoder.decodeVideoData(data);
        } else {
            emit textMsgRecv(data);
        }
        break;
    }
    default:
        emit errorOccurred("Unknown packet type: " + hex(packet.type));
        break;
    }
}

void DroneController::readPort()
{
    for (int bytes = port.bytesAvailable(); bytes > 0; bytes = port.bytesAvailable()) {

        if (lastPacket.type == PacketType_Invalid) {
            if (bytes < sizeof(lastPacket)) {
                break;
            }
            int r = port.read(reinterpret_cast<char *>(&lastPacket), sizeof(lastPacket));
            if (r < sizeof(lastPacket)) {
                emit errorOccurred("Error reading packet from port");
                resetPort();
                break;
            }
            if (!lastPacket.checkCrc()) {
                emit errorOccurred("Invalid packet read from port: crc error");
                resetPort();
                break;
            }
            bytes -= r;
        }

        if(bufferMap.contains(lastPacket.type)){
            const int size = MIN(bytes, lastPacket.payload.dataSize - bufferMap[lastPacket.type].size());
            if (size > 0) {
                const QByteArray data = port.read(size);
                if (data.size() != size) {
                    emit errorOccurred("Error reading data from port");
                    resetPort();
                    break;
                }
                bufferMap[lastPacket.type].push_back(data);
            }
        }

        if (!bufferMap.contains(lastPacket.type) || bufferMap[lastPacket.type].size() >= lastPacket.payload.dataSize) {
            processData();
        }
    }
}
