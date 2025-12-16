#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QComboBox>
#include <QMainWindow>
#include <QPushButton>
#include <QSerialPort>
#include <QSerialPortInfo>
#include <QTextEdit>
#include <QTimer>

#include "../protocol.h"

QT_BEGIN_NAMESPACE
namespace Ui {
class MainWindow;
}
QT_END_NAMESPACE

class MainWindow : public QMainWindow
{
    Q_OBJECT
public:
    explicit MainWindow(QWidget *parent = nullptr);
    ~MainWindow();

private slots:
    void openSerial();
    void sendSetConnection();
    void sendGetConnection();
    void setHeartbeat();
    void setFlyCmd();
    void sendEnableControl();
    void sendStopControl();
    void sendSwitchCamFront();
    void sendSwitchCamBack();

private:
    void resetSerial();
    void sendCmd(const ClientPacket &cmd);
    void readSerial();
    void sendHeartbeat();
    void sendFlyCmd();
    void sendAckPhoto();
    void sendAckVideo();

    void initCurrentValues();

    Ui::MainWindow *ui;
    QSerialPort serial;
    QTimer timerHb;
    QTimer timerFly;
    FlyControls flyControls;
    ClientPacket fdbk;
    struct {
        data_size_t size = 0;
        VideoPayload payload;
    } videoData;
};

#endif // MAINWINDOW_H
