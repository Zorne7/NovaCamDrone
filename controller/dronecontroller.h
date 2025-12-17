#ifndef DRONECONTROLLER_H
#define DRONECONTROLLER_H

#include <QObject>
#include <QSerialPort>
#include <QTimer>
#include <QMap>

#include "../protocol.h"

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
    void ackRecv(Ack ack);
    void connStatusRecv(ProtocolChannel_t status);
    void errorOccurred(const QString &err);
    void rtspResponseRecv();

private:
    bool sendCmd(const BridgePacketId &id, const QByteArray &data = QByteArray());
    void readSerial();
    bool waitRtspResponse(int timeout_ms);
    const QByteArray readRtspResponse();
    void parseDroneTlm(const DroneTlm *tlm);
    void processData();

    QSerialPort serial;
    QTimer timerHb;
    QTimer timerFly;
    FlyControls flyControls;
    BridgePacketHeader lastHeader;
    QMap<ProtocolChannel_t, QByteArray> channelBuffMap;
    int cseq = 1;
    QByteArray sessionId;
    QByteArray rtspResponse;
    QByteArray currentFrame;
};

template <typename T>
static inline const QString hex(T value)
{
    return QString("0x%1").arg(value, sizeof(value) * 2, 16, QLatin1Char('0'));
}

#endif // DRONECONTROLLER_H
