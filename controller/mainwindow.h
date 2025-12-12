#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QComboBox>
#include <QMainWindow>
#include <QPushButton>
#include <QSerialPort>
#include <QSerialPortInfo>
#include <QTextEdit>

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

private slots:
    void openSerial();
    void sendSetConnection();
    void sendGetConnection();
    void sendHeartbeat();
    void sendFlyCmd();
    void sendStopControl();
    void sendSwitchCamFront();
    void sendSwitchCamBack();
    void sendAckPhoto();
    void sendAckVideo();
    void readSerial();

private:
    QSerialPort serial;
    QTextEdit *log;
    QPushButton *btnSetConn;
    QPushButton *btnGetConn;
    QLineEdit *ssidEdit;
    QLineEdit *passEdit;
    QLineEdit *ipEdit;
    QLineEdit *recvPortEdit;
    QLineEdit *sendPortEdit;
    QPushButton *btnHeartbeat;
    QPushButton *btnFly;
    QPushButton *btnStop;
    QPushButton *btnCamFront;
    QPushButton *btnCamBack;
    QPushButton *btnAckPhoto;
    QPushButton *btnAckVideo;
    QComboBox *portSelector;
    QComboBox *baudSelector;
    Ui::MainWindow *ui;
};

#endif // MAINWINDOW_H
