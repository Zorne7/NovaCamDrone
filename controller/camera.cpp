#include "camera.h"

Camera::Camera(QObject *parent)
    : QObject{parent}
{
    session.setCamera(&camera);
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
    camera.start();
}

void Camera::stop()
{
    camera.stop();
}
