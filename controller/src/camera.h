#ifndef CAMERA_H
#define CAMERA_H

#include <QCamera>
#include <QMediaCaptureSession>
#include <QObject>
#include <QVideoFrame>
#include <QVideoSink>

class Camera : public QObject
{
    Q_OBJECT
public:
    explicit Camera(uint8_t c = 0, QObject *parent = nullptr);

public slots:
    void start();
    void stop();

signals:
    void frameReady(const QImage &frame);

private:
    QCamera *cam;
    QMediaCaptureSession session;
    QVideoSink sink;
};

#endif // CAMERA_H
