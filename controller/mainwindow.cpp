#include "mainwindow.h"
#include "../protocol.h"
#include "./ui_mainwindow.h"

#include <QLineEdit>
#include <QVBoxLayout>
#include <QWidget>

MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent)
{
    QWidget *central = new QWidget(this);
    QVBoxLayout *layout = new QVBoxLayout(central);

    // ComboBox per selezione porta
    portSelector = new QComboBox(this);
    for (const QSerialPortInfo &info : QSerialPortInfo::availablePorts()) {
        portSelector->addItem(info.portName());
    }

    // ComboBox per selezione baudrate
    baudSelector = new QComboBox(this);
    QList<int> baudRates = {0, 9600, 19200, 38400, 57600, 115200, 921600};
    for (int rate : baudRates) {
        baudSelector->addItem(QString::number(rate), rate);
    }
    baudSelector->setCurrentText("0");

    log = new QTextEdit(this);
    log->setReadOnly(true);

    ssidEdit = new QLineEdit(this);
    passEdit = new QLineEdit(this);
    ipEdit = new QLineEdit(this);
    recvPortEdit = new QLineEdit(this);
    sendPortEdit = new QLineEdit(this);

    ssidEdit->setPlaceholderText("WiFi SSID");
    passEdit->setPlaceholderText("WiFi Password");
    ipEdit->setPlaceholderText("Drone IP");
    recvPortEdit->setPlaceholderText("Recv Port");
    sendPortEdit->setPlaceholderText("Send Port");

    btnSetConn = new QPushButton("Set Connection", this);
    btnGetConn = new QPushButton("Get Connection", this);

    btnHeartbeat = new QPushButton("Send Heartbeat", this);
    btnFly = new QPushButton("Send FlyCmd", this);
    btnStop = new QPushButton("Stop Control", this);
    btnCamFront = new QPushButton("Switch Cam Front", this);
    btnCamBack = new QPushButton("Switch Cam Back", this);
    btnAckPhoto = new QPushButton("Ack Photo", this);
    btnAckVideo = new QPushButton("Ack Video", this);

    layout->addWidget(portSelector);
    layout->addWidget(baudSelector);

    layout->addWidget(ssidEdit);
    layout->addWidget(passEdit);
    layout->addWidget(ipEdit);
    layout->addWidget(recvPortEdit);
    layout->addWidget(sendPortEdit);
    layout->addWidget(btnSetConn);
    layout->addWidget(btnGetConn);

    layout->addWidget(btnHeartbeat);
    layout->addWidget(btnFly);
    layout->addWidget(btnStop);
    layout->addWidget(btnCamFront);
    layout->addWidget(btnCamBack);
    layout->addWidget(btnAckPhoto);
    layout->addWidget(btnAckVideo);
    layout->addWidget(log);

    setCentralWidget(central);

    connect(btnSetConn, &QPushButton::clicked, this, &MainWindow::sendSetConnection);
    connect(btnGetConn, &QPushButton::clicked, this, &MainWindow::sendGetConnection);
    connect(btnHeartbeat, &QPushButton::clicked, this, &MainWindow::sendHeartbeat);
    connect(btnFly, &QPushButton::clicked, this, &MainWindow::sendFlyCmd);
    connect(btnStop, &QPushButton::clicked, this, &MainWindow::sendStopControl);
    connect(btnCamFront, &QPushButton::clicked, this, &MainWindow::sendSwitchCamFront);
    connect(btnCamBack, &QPushButton::clicked, this, &MainWindow::sendSwitchCamBack);
    connect(btnAckPhoto, &QPushButton::clicked, this, &MainWindow::sendAckPhoto);
    connect(btnAckVideo, &QPushButton::clicked, this, &MainWindow::sendAckVideo);

    // Apri la seriale quando cambia selezione
    connect(portSelector, &QComboBox::currentTextChanged, this, &MainWindow::openSerial);
    connect(baudSelector, &QComboBox::currentTextChanged, this, &MainWindow::openSerial);
}

void MainWindow::openSerial()
{
    if (serial.isOpen()) {
        serial.close();
    }

    QString portName = portSelector->currentText();
    int baudRate = baudSelector->currentData().toInt();
    if (baudRate == 0) {
        return;
    }

    serial.setPortName(portName);
    serial.setBaudRate(baudRate);

    if (!serial.open(QIODevice::ReadWrite)) {
        log->append("Errore apertura seriale su " + portName);
    } else {
        log->append(QString("Seriale aperta su %1 a %2 baud").arg(portName).arg(baudRate));
        connect(&serial, &QSerialPort::readyRead, this, &MainWindow::readSerial);
    }
}

void MainWindow::sendSetConnection()
{
    ClientCmd cmd;
    cmd.type = TypeSetConnection;

    ConnectionParams params{};
    strncpy(params.wifiSsid, ssidEdit->text().toStdString().c_str(), sizeof(params.wifiSsid));
    strncpy(params.wifiPassw, passEdit->text().toStdString().c_str(), sizeof(params.wifiPassw));
    strncpy(params.ip, ipEdit->text().toStdString().c_str(), sizeof(params.ip));
    params.recvPort = recvPortEdit->text().toUShort();
    params.sendPort = sendPortEdit->text().toUShort();
    params.timeout = 1000;

    cmd.data.connParams = params;

    serial.write(reinterpret_cast<const char *>(&cmd), sizeof(cmd));
    log->append("SetConnection inviato");
}

void MainWindow::sendGetConnection()
{
    ClientCmd cmd;
    cmd.type = TypeGetConnection;
    serial.write(reinterpret_cast<const char *>(&cmd), sizeof(cmd));
    log->append("GetConnection inviato");
}

void MainWindow::sendHeartbeat()
{
    ClientCmd cmd;
    cmd.type = TypeDroneCmd;
    cmd.data.droneCmd = DroneCmd_HEARTBEAT;

    serial.write(reinterpret_cast<const char *>(&cmd), sizeof(cmd));
    log->append("Heartbeat inviato");
}

void MainWindow::sendFlyCmd()
{
    FlyParams fly;
    fly.controlByte1 = FLY_PAR_NEUTRAL;
    fly.controlByte2 = FLY_PAR_NEUTRAL;
    fly.controlAccelerator = FLY_PAR_NEUTRAL + 20; // esempio: accelera
    fly.controlTurn = FLY_PAR_NEUTRAL;
    fly.flags = FastFly;
    fly.normalize();

    ClientCmd cmd;
    cmd.type = TypeFlyCmd;
    cmd.data.flyCmd.flyParams = fly;

    serial.write(reinterpret_cast<const char *>(&cmd), sizeof(cmd));
    log->append("FlyCmd inviato");
}

void MainWindow::sendStopControl()
{
    ClientCmd cmd;
    cmd.type = TypeDroneCmd;
    cmd.data.droneCmd = DroneCmd_STOP_CONTROL;
    serial.write(reinterpret_cast<const char *>(&cmd), sizeof(cmd));
    log->append("Stop Control inviato");
}

void MainWindow::sendSwitchCamFront()
{
    ClientCmd cmd;
    cmd.type = TypeDroneCmd;
    cmd.data.droneCmd = DroneCmd_SWITCH_CAM_FRONT;
    serial.write(reinterpret_cast<const char *>(&cmd), sizeof(cmd));
    log->append("Switch Cam Front inviato");
}

void MainWindow::sendSwitchCamBack()
{
    ClientCmd cmd;
    cmd.type = TypeDroneCmd;
    cmd.data.droneCmd = DroneCmd_SWITCH_CAM_BACK;
    serial.write(reinterpret_cast<const char *>(&cmd), sizeof(cmd));
    log->append("Switch Cam Back inviato");
}

void MainWindow::sendAckPhoto()
{
    ClientCmd cmd;
    cmd.type = TypeDroneCmd;
    cmd.data.droneCmd = DroneCmd_ACK_PHOTO;
    serial.write(reinterpret_cast<const char *>(&cmd), sizeof(cmd));
    log->append("Ack Photo inviato");
}

void MainWindow::sendAckVideo()
{
    ClientCmd cmd;
    cmd.type = TypeDroneCmd;
    cmd.data.droneCmd = DroneCmd_ACK_VIDEO;
    serial.write(reinterpret_cast<const char *>(&cmd), sizeof(cmd));
    log->append("Ack Video inviato");
}

void MainWindow::readSerial()
{
    while (serial.bytesAvailable() >= sizeof(ClientResp)) {
        ClientResp resp;
        serial.read(reinterpret_cast<char *>(&resp), sizeof(resp));

        switch (resp.type) {
        case TypeAck:
            log->append(QString("Ack ricevuto: %1").arg(resp.data.ack));
            break;
        case TypeConnectionStat:
            log->append(QString("Connessione: %1").arg(resp.data.connected));
            break;
        case TypeDroneTlm:
            log->append(QString("TLM: photo=%1 video=%2")
                            .arg(resp.data.droneTlm.numPhoto)
                            .arg(resp.data.droneTlm.numVideo));
            break;
        default:
            log->append("Risposta sconosciuta");
            break;
        }
    }
}
