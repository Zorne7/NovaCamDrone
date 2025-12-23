#ifndef DRONECONTROLLER_H
#define DRONECONTROLLER_H

#include <QMap>
#include <QObject>
#include <QSerialPort>

#include "../protocol.h"
#include "decoder.h"

class DroneController : public QObject
{
    Q_OBJECT
public:
    explicit DroneController(QObject *parent = nullptr);
    bool setSerial(const QString &portName);
    AckVal_t waitAck(int timeout_ms);

public slots:
    bool resetSerial();

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
    void readSerial();
    bool sendAckPhoto();
    bool sendAckVideo();

private:
    bool sendCmd(const Packet &cmd);
    void parseDroneTlm(const DroneTlm &tlm);
    void processData();

    QSerialPort serial;
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
