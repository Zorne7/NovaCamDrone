#ifndef DECODER_H
#define DECODER_H

#include <QObject>
#include <QProcess>

class Decoder : public QObject
{
    Q_OBJECT
public:
    explicit Decoder(QObject *parent = nullptr);
    ~Decoder() { close(); };

public slots:
    void init();
    void close();
    void decodeVideoData(const QByteArray &videoData);

signals:
    void errorOccurred(const QString &err);
    void frameReady(const QByteArray &frameData);

private slots:
    void parseVideoData();

private:
    QProcess decoderProcess;
    QByteArray buffer;
};

#endif // DECODER_H
