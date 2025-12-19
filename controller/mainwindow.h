#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QComboBox>
#include <QMainWindow>
#include <QPushButton>
#include <QSerialPortInfo>
#include <QTextEdit>

#include "dronecontroller.h"

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
    void refreshAvailableSerialPorts();
    void setSerial();
    void initCurrentValues();
    void sendSetConnection();
    void setVideo();

    void onAckRecv(const Ack &ack);
    void onConnStatusRecv(status_t connStatus);
    void onFrameReady(const QByteArray &frameData);

private:
    Ui::MainWindow *ui;
    DroneController droneCtrl;
};

#endif // MAINWINDOW_H
