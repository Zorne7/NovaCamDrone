#include "decoder.h"

#include <QCoreApplication>

#define WAIT_BYTES_WRITTEN_MS 100

Decoder::Decoder(QObject *parent)
    : QObject{parent}
{
    connect(&decoderProcess, &QProcess::readyReadStandardOutput, this, &Decoder::parseVideoData);
    connect(&decoderProcess, &QProcess::readyReadStandardError, this, [=]() {
        emit errorOccurred(decoderProcess.readAllStandardError());
    });
    connect(&decoderProcess, &QProcess::errorOccurred, this, [=]() {
        emit errorOccurred(decoderProcess.errorString());
    });
}

void Decoder::init()
{
    decoderProcess.setWorkingDirectory(QCoreApplication::applicationDirPath());
    decoderProcess.start("python3", QStringList() << "decoder.py");
}

void Decoder::close()
{
    decoderProcess.close();
}

void Decoder::decodeVideoData(const QByteArray &videoData)
{
    if (decoderProcess.state() != QProcess::Running) {
        emit errorOccurred("Unable to decode video data");
        return;
    }
    QByteArray packet;
    QDataStream stream(&packet, QIODevice::WriteOnly);
    stream.setByteOrder(QDataStream::BigEndian);
    stream << static_cast<quint32>(videoData.size());
    packet.append(videoData);
    decoderProcess.write(packet);
    bool ok = decoderProcess.waitForBytesWritten(WAIT_BYTES_WRITTEN_MS);
    if (!ok) {
        emit errorOccurred("Video data not completely sent to decoder in " + QString::number(WAIT_BYTES_WRITTEN_MS) + " ms");
    }
}

void Decoder::parseVideoData()
{
    quint32 frameSize;
    buffer.append(decoderProcess.readAllStandardOutput());
    while (buffer.size() >= sizeof(frameSize)) {
        QDataStream sizeStream(buffer.left(sizeof(frameSize)));
        sizeStream.setByteOrder(QDataStream::BigEndian);
        sizeStream >> frameSize;
        if (buffer.size() < sizeof(frameSize) + frameSize) {
            break;
        }
        const QByteArray frameData = buffer.mid(sizeof(frameSize), frameSize);
        emit frameReady(frameData);
        buffer.remove(0, sizeof(frameSize) + frameSize);
    }
}
