#ifndef DRONECONTROLLER_H
#define DRONECONTROLLER_H

#include <QMap>
#include <QObject>
#include <QSerialPort>

namespace Protocol {
Q_NAMESPACE
#include "../protocol.h"
Q_ENUM_NS(ConnStatus);
Q_ENUM_NS(PacketType);
Q_ENUM_NS(AckVal);
};
#include "decoder.h"

using namespace Protocol;

class DroneController : public QObject
{
    Q_OBJECT
public:
    explicit DroneController(QObject *parent = nullptr);
    bool setPort(const QString &portName);
    bool portOpened() const { return port.isOpen(); }
    AckVal_t waitAck(int timeout_ms);

public slots:
    bool resetPort();

    bool sendSetConnection(const ConnParams &connParams);
    bool sendGetConnection();
    bool sendHeartbeat();
    bool sendFlyControls(const FlyControls &flyControls);
    bool sendStopControl();
    bool sendSetVideo(bool enabled);
    bool sendSwitchCamFront();
    bool sendSwitchCamBack();

signals:
    void ackRecv(const Ack &ack);
    void connStatusRecv(ConnStatus_t connStatus);
    void errorOccurred(const QString &err);
    void frameReady(const QByteArray &frameData);

private slots:
    void readPort();
    bool sendAckPhoto();
    bool sendAckVideo();

private:
    bool sendCmd(const Packet &cmd);
    void parseDroneTlm(const DroneTlm &tlm);
    void processData();

    QSerialPort port;
    Packet lastPacket;
    Ack lastAck;
    QByteArray droneVideoData;
    Decoder decoder;
};

template <typename T>
static inline const QString hex(T value)
{
    return QString("0x%1").arg(value, sizeof(value) * 2, 16, QLatin1Char('0'));
}

#endif // DRONECONTROLLER_H
