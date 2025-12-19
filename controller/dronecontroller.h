#ifndef DRONECONTROLLER_H
#define DRONECONTROLLER_H

#include <QMap>
#include <QObject>
#include <QSerialPort>
#include <QTimer>

#include "../protocol.h"
#include "decoder.h"

class DroneController : public QObject
{
    Q_OBJECT
public:
    explicit DroneController(QObject *parent = nullptr);

    FlyControls* getFlyControls() { return &flyControls; }
    bool setSerial(const QString &portName, int baudRate);
    bool setVideo(bool enabled);

public slots:
    void resetSerial();

    void sendSetConnection(const ConnParams &connParams);
    void sendGetConnection();
    void setHeartbeat();
    void setFlyCmd();
    void sendStopControl();
    void sendSwitchCamFront();
    void sendSwitchCamBack();
    void sendHeartbeat();
    void sendFlyCmd();
    void sendAckPhoto();
    void sendAckVideo();

signals:
    void ackRecv(const Ack &ack);
    void connStatusRecv(status_t status);
    void errorOccurred(const QString &err);
    void frameReady(const QByteArray &frameData);
    void rtspResponseRecv();

private:
    bool sendCmd(const BridgePacketId &id, const QByteArray &data = QByteArray());
    void readSerial();
    bool waitRtspResponse(int timeout_ms);
    void parseDroneTlm(const QByteArray &tlmData);
    void processData();

    QSerialPort serial;
    QTimer timerHb;
    QTimer timerFly;
    FlyControls flyControls;
    BridgePacketHeader lastHeader;
    QMap<ProtocolChannel_t, QByteArray> channelBuffMap;
    RTSP rtsp;
    Decoder decoder;
};

template <typename T>
static inline const QString hex(T value)
{
    return QString("0x%1").arg(value, sizeof(value) * 2, 16, QLatin1Char('0'));
}

#endif // DRONECONTROLLER_H
