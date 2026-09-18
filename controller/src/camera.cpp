#include "camera.h"

#include <QMediaDevices>

Camera::Camera(uint8_t c, QObject *parent)
    : QObject{parent}, cam(nullptr)
{
    const QList<QCameraDevice> cams = QMediaDevices::videoInputs();
    if(c < cams.size()) {
        cam = new QCamera(cams[c], this);
    }

    session.setCamera(cam);
    session.setVideoSink(&sink);
    connect(&sink, &QVideoSink::videoFrameChanged, [this](const QVideoFrame &frame) {
        if (frame.isValid()) {
            QImage img = frame.toImage();
            if (!img.isNull()) {
                emit frameReady(img);
            }
        }
    });
}

void Camera::start()
{
    if(cam) cam->start();
}

void Camera::stop()
{
    if(cam) cam->stop();
}
