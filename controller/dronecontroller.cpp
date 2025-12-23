#include "dronecontroller.h"

#include <QDebug>
#include <QEventLoop>
#include <QTimer>

DroneController::DroneController(QObject *parent)
    : QObject{parent}
{
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

bool DroneController::setSerial(const QString &portName)
{
    if (serial.isOpen()) {
        serial.close();
        lastPacket = Packet();
        lastAck = Ack();
        droneVideoData.clear();
    }
    if (portName.isEmpty()) {
        return true;
    }
    serial.setPortName(portName);
    serial.setBaudRate(BRIDGE_BITRATE);
    return serial.open(QIODevice::ReadWrite);
}

bool DroneController::resetSerial()
{
    const QString portName = serial.portName();
    bool ok = setSerial(portName);
    if (!ok) {
        emit errorOccurred("Error resetting serial " + portName);
    }
    return ok;
}

bool DroneController::sendCmd(const Packet &cmd)
{
    lastAck = Ack();
    if (!serial.isOpen()) {
        emit errorOccurred("Error sending cmd " + hex(cmd.type) + ": serial closed");
        return false;
    }
    bool ok = serial.write(reinterpret_cast<const char *>(&cmd), sizeof(cmd)) == sizeof(cmd);
    if (ok) {
        serial.flush();
    } else {
        emit errorOccurred("Error sending cmd " + hex(cmd.type) + ": write error");
    }
    return ok;
}

AckVal_t DroneController::waitAck(int timeout_ms)
{
    if (lastAck.cmd != PacketType_Invalid) {
        return true;
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
        lastAck = packet.data.ack;
        emit ackRecv(lastAck);
        break;

    case PacketType_ConnectionStat:
        emit connStatusRecv(packet.data.connStatus);
        break;

    case PacketType_DroneTlm:
        parseDroneTlm(packet.data.droneTlm);
        break;

    case PacketType_DroneVideo: {
        const QByteArray crcData = droneVideoData.right(sizeof(crc_t));
        const crc_t crc = *reinterpret_cast<const crc_t *>(crcData.data());
        if(calculate_crc(droneVideoData.data(), droneVideoData.size() - sizeof(crc_t)) == crc) {
            decoder.decodeVideoData(droneVideoData);
        }else{
            emit errorOccurred("Invalid video data read from serial: crc error");
        }
        droneVideoData.clear();
        break;
    }
    default:
        emit errorOccurred("Unknown packet type: " + hex(packet.type));
        resetSerial();
        break;
    }
}

void DroneController::readSerial()
{
    for (int bytes = serial.bytesAvailable(); bytes > 0; bytes = serial.bytesAvailable()) {

        if (lastPacket.type == PacketType_Invalid) {
            if (bytes < sizeof(lastPacket)) {
                break;
            }
            int r = serial.read(reinterpret_cast<char *>(&lastPacket), sizeof(lastPacket));
            if (r < sizeof(lastPacket)) {
                emit errorOccurred("Error reading packet from serial");
                resetSerial();
                break;
            }
            if (!lastPacket.checkCrc()) {
                emit errorOccurred("Invalid packet read from serial: crc error");
                resetSerial();
                break;
            }
            bytes -= r;
        }

        if(lastPacket.type == PacketType_DroneVideo){
            const int size = MIN(bytes, lastPacket.data.droneVideo.dataSize - droneVideoData.size());
            if (size > 0) {
                const QByteArray data = serial.read(size);
                if (data.size() != size) {
                    emit errorOccurred("Error reading video data from serial");
                    resetSerial();
                    break;
                }
                droneVideoData.push_back(data);
            }
        }

        if (lastPacket.type != PacketType_DroneVideo || droneVideoData.size() >= lastPacket.data.droneVideo.dataSize) {
            processData();
        }
    }
}
