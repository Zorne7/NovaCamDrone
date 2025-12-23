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
    void refreshAvailablePorts();
    void setPort();
    void initCurrentValues();
    void sendSetConnection();
    void setVideo();

    void updateFlyCtrlPar(fly_par_t &par, int newVal, QLineEdit *line);
    void updateFlyCtrlFlag(FlyControlFlags flag, bool enable);

    void onErrOccurred(const QString &err);
    void onAckRecv(const Ack &ack);
    void onConnStatusRecv(ConnStatus_t connStatus);
    void onFrameReady(const QByteArray &frameData);

private:
    Ui::MainWindow *ui;
    DroneController droneCtrl;
    FlyControls flyCtrls;
};

#endif // MAINWINDOW_H
