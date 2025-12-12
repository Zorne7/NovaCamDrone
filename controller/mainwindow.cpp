#include "mainwindow.h"
#include "../protocol.h"
#include "./ui_mainwindow.h"

#include <QLineEdit>
#include <QVBoxLayout>
#include <QWidget>

MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::MainWindow)
{
    ui->setupUi(this);

    // Popola la combo delle porte disponibili
    for (const QSerialPortInfo &info : QSerialPortInfo::availablePorts()) {
        ui->portSelector->addItem(info.portName());
    }

    // Popola la combo dei baud rate
    QList<int> baudRates = {0, 9600, 19200, 38400, 57600, 115200, 921600};
    for (int rate : baudRates) {
        ui->baudSelector->addItem(QString::number(rate), rate);
    }
    ui->baudSelector->setCurrentText("0");

    // Collega i pulsanti agli slot
    connect(ui->btnSetConn, &QPushButton::clicked, this, &MainWindow::sendSetConnection);
    connect(ui->btnGetConn, &QPushButton::clicked, this, &MainWindow::sendGetConnection);
    connect(ui->btnHeartbeat, &QPushButton::clicked, this, &MainWindow::sendHeartbeat);
    connect(ui->btnFly, &QPushButton::clicked, this, &MainWindow::sendFlyCmd);
    connect(ui->btnStop, &QPushButton::clicked, this, &MainWindow::sendStopControl);
    connect(ui->btnCamFront, &QPushButton::clicked, this, &MainWindow::sendSwitchCamFront);
    connect(ui->btnCamBack, &QPushButton::clicked, this, &MainWindow::sendSwitchCamBack);
    connect(ui->btnAckPhoto, &QPushButton::clicked, this, &MainWindow::sendAckPhoto);
    connect(ui->btnAckVideo, &QPushButton::clicked, this, &MainWindow::sendAckVideo);

    // Collega le combo per apertura seriale
    connect(ui->portSelector, &QComboBox::currentTextChanged, this, &MainWindow::openSerial);
    connect(ui->baudSelector, &QComboBox::currentTextChanged, this, &MainWindow::openSerial);
}

MainWindow::~MainWindow()
{
    delete ui;
}

void MainWindow::openSerial()
{
    if (serial.isOpen()) {
        serial.close();
    }

    QString portName = ui->portSelector->currentText();
    int baudRate = ui->baudSelector->currentData().toInt();
    if (baudRate == 0) {
        return;
    }

    serial.setPortName(portName);
    serial.setBaudRate(baudRate);

    if (!serial.open(QIODevice::ReadWrite)) {
        ui->log->append("Error opening serial " + portName);
    } else {
        ui->log->append(QString("Serial %1 opened with %2 baud").arg(portName).arg(baudRate));
        connect(&serial, &QSerialPort::readyRead, this, &MainWindow::readSerial);
    }
}

void MainWindow::sendSetConnection()
{
    ClientCmd cmd;
    cmd.type = TypeSetConnection;

    ConnectionParams params{};
    strncpy(params.wifiSsid, ui->ssidEdit->text().toStdString().c_str(), sizeof(params.wifiSsid));
    strncpy(params.wifiPassw, ui->passEdit->text().toStdString().c_str(), sizeof(params.wifiPassw));
    strncpy(params.ip, ui->ipEdit->text().toStdString().c_str(), sizeof(params.ip));
    params.recvPort = ui->recvPortEdit->text().toUShort();
    params.sendPort = ui->sendPortEdit->text().toUShort();
    params.timeout = 1000;

    cmd.data.connParams = params;

    serial.write(reinterpret_cast<const char *>(&cmd), sizeof(cmd));
    ui->log->append("SetConnection sent");
}

void MainWindow::sendGetConnection()
{
    ClientCmd cmd;
    cmd.type = TypeGetConnection;
    serial.write(reinterpret_cast<const char *>(&cmd), sizeof(cmd));
    ui->log->append("GetConnection sent");
}

void MainWindow::sendHeartbeat()
{
    ClientCmd cmd;
    cmd.type = TypeDroneCmd;
    cmd.data.droneCmd = DroneCmd_HEARTBEAT;

    serial.write(reinterpret_cast<const char *>(&cmd), sizeof(cmd));
    ui->log->append("Heartbeat sent");
}

void MainWindow::sendFlyCmd()
{
    FlyParams fly;
    fly.controlByte1 = FLY_PAR_NEUTRAL;
    fly.controlByte2 = FLY_PAR_NEUTRAL;
    fly.controlAccelerator = FLY_PAR_NEUTRAL;
    fly.controlTurn = FLY_PAR_NEUTRAL;
    fly.flags = FastFly;
    fly.normalize();

    ClientCmd cmd;
    cmd.type = TypeFlyCmd;
    cmd.data.flyCmd.flyParams = fly;

    serial.write(reinterpret_cast<const char *>(&cmd), sizeof(cmd));
    ui->log->append("FlyCmd sent");
}

void MainWindow::sendStopControl()
{
    ClientCmd cmd;
    cmd.type = TypeDroneCmd;
    cmd.data.droneCmd = DroneCmd_STOP_CONTROL;
    serial.write(reinterpret_cast<const char *>(&cmd), sizeof(cmd));
    ui->log->append("Stop Control sent");
}

void MainWindow::sendSwitchCamFront()
{
    ClientCmd cmd;
    cmd.type = TypeDroneCmd;
    cmd.data.droneCmd = DroneCmd_SWITCH_CAM_FRONT;
    serial.write(reinterpret_cast<const char *>(&cmd), sizeof(cmd));
    ui->log->append("Switch Cam Front sent");
}

void MainWindow::sendSwitchCamBack()
{
    ClientCmd cmd;
    cmd.type = TypeDroneCmd;
    cmd.data.droneCmd = DroneCmd_SWITCH_CAM_BACK;
    serial.write(reinterpret_cast<const char *>(&cmd), sizeof(cmd));
    ui->log->append("Switch Cam Back sent");
}

void MainWindow::sendAckPhoto()
{
    ClientCmd cmd;
    cmd.type = TypeDroneCmd;
    cmd.data.droneCmd = DroneCmd_ACK_PHOTO;
    serial.write(reinterpret_cast<const char *>(&cmd), sizeof(cmd));
    ui->log->append("Ack Photo sent");
}

void MainWindow::sendAckVideo()
{
    ClientCmd cmd;
    cmd.type = TypeDroneCmd;
    cmd.data.droneCmd = DroneCmd_ACK_VIDEO;
    serial.write(reinterpret_cast<const char *>(&cmd), sizeof(cmd));
    ui->log->append("Ack Video sent");
}

void MainWindow::readSerial()
{
    while (serial.bytesAvailable() >= sizeof(ClientResp)) {
        ClientResp resp;
        serial.read(reinterpret_cast<char *>(&resp), sizeof(resp));

        switch (resp.type) {
        case TypeAck:
            ui->log->append(QString("Ack: %1").arg(resp.data.ack));
            break;
        case TypeConnectionStat:
            ui->log->append(QString("Connection: %1").arg(resp.data.connected));
            break;
        case TypeDroneTlm:
            ui->log->append(QString("TLM: photo=%1 video=%2")
                                .arg(resp.data.droneTlm.numPhoto)
                                .arg(resp.data.droneTlm.numVideo));
            break;
        default:
            ui->log->append("Unknown response type: " + QString::number(resp.type));
            break;
        }
    }
}
