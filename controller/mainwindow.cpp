#include "mainwindow.h"
#include "./ui_mainwindow.h"

#include <QEventLoop>
#include <QLineEdit>
#include <QMetaEnum>
#include <QVBoxLayout>
#include <QWidget>

static constexpr int CONN_TIMEOUT = 3000;
static constexpr int HB_INTERVAL = 1000;
static constexpr int FLY_INTERVAL = 50;

static inline void msSleep(int ms)
{
    QEventLoop loop;
    QTimer timer;
    QObject::connect(&timer, &QTimer::timeout, &loop, &QEventLoop::quit);
    timer.start(ms);
    loop.exec();
}

static inline void setFlag(uint8_t &flags, uint8_t flag, bool en)
{
    if (en) {
        flags |= flag;
    } else {
        flags &= ~flag;
    }
}

MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::MainWindow)
{
    ui->setupUi(this);

    for (const QSerialPortInfo &info : QSerialPortInfo::availablePorts()) {
        ui->portSelector->addItem(info.portName());
    }

    QList<int> baudRates = {0, 9600, 19200, 38400, 57600, 115200, 921600};
    for (int rate : baudRates) {
        ui->baudSelector->addItem(QString::number(rate), rate);
    }
    ui->baudSelector->setCurrentText("0");

    timerHb.setInterval(HB_INTERVAL);
    timerHb.setSingleShot(false);
    connect(&timerHb, &QTimer::timeout, this, &MainWindow::sendHeartbeat);

    timerFly.setInterval(FLY_INTERVAL);
    timerFly.setSingleShot(false);
    connect(&timerFly, &QTimer::timeout, this, &MainWindow::sendFlyCmd);

    connect(ui->btnSetConn, &QPushButton::clicked, this, &MainWindow::sendSetConnection);
    connect(ui->btnGetConn, &QPushButton::clicked, this, &MainWindow::sendGetConnection);
    connect(ui->btnHeartbeat, &QPushButton::clicked, this, &MainWindow::setHeartbeat);
    connect(ui->btnFly, &QPushButton::clicked, this, &MainWindow::setFlyCmd);
    connect(ui->btnEnable, &QPushButton::clicked, this, &MainWindow::sendEnableControl);
    connect(ui->btnStop, &QPushButton::clicked, this, &MainWindow::sendStopControl);
    connect(ui->btnCamFront, &QPushButton::clicked, this, &MainWindow::sendSwitchCamFront);
    connect(ui->btnCamBack, &QPushButton::clicked, this, &MainWindow::sendSwitchCamBack);
    connect(ui->resetBtn, &QPushButton::clicked, this, &MainWindow::initCurrentValues);

    connect(ui->portSelector, &QComboBox::currentTextChanged, this, &MainWindow::openSerial);
    connect(ui->baudSelector, &QComboBox::currentTextChanged, this, &MainWindow::openSerial);

    connect(ui->leftRightSlider, &QSlider::valueChanged, this, [=](int val) {
        flyParams.controlByte1 = val;
        ui->control1Line->setText(QString::number(flyParams.controlByte1));
    });
    connect(ui->frontBackSlider, &QSlider::valueChanged, this, [=](int val) {
        flyParams.controlByte2 = val;
        ui->control2Line->setText(QString::number(flyParams.controlByte2));
    });
    connect(ui->accelSlider, &QSlider::valueChanged, this, [=](int val) {
        flyParams.controlAccelerator = val;
        ui->accellerLine->setText(QString::number(flyParams.controlAccelerator));
    });
    connect(ui->turnSlider, &QSlider::valueChanged, this, [=](int val) {
        flyParams.controlTurn = val;
        ui->turnLine->setText(QString::number(flyParams.controlTurn));
    });
    connect(ui->fastFlyCheck, &QCheckBox::stateChanged, this, [=](int en) {
        setFlag(flyParams.flags, FastFly, en);
        ui->flagsLine->setText("0x" + QString::number(flyParams.flags, 16));
    });
    connect(ui->fastDropCheck, &QCheckBox::stateChanged, this, [=](int en) {
        setFlag(flyParams.flags, FastDrop, en);
        ui->flagsLine->setText("0x" + QString::number(flyParams.flags, 16));
    });
    connect(ui->emergStopCheck, &QCheckBox::stateChanged, this, [=](int en) {
        setFlag(flyParams.flags, EmergencyStop, en);
        ui->flagsLine->setText("0x" + QString::number(flyParams.flags, 16));
    });
    connect(ui->circleTurnEndCheck, &QCheckBox::stateChanged, this, [=](int en) {
        setFlag(flyParams.flags, CircleTurnEnd, en);
        ui->flagsLine->setText("0x" + QString::number(flyParams.flags, 16));
    });
    connect(ui->noHeadCheck, &QCheckBox::stateChanged, this, [=](int en) {
        setFlag(flyParams.flags, NoHeadMode, en);
        ui->flagsLine->setText("0x" + QString::number(flyParams.flags, 16));
    });
    connect(ui->unlockCheck, &QCheckBox::stateChanged, this, [=](int en) {
        setFlag(flyParams.flags, Unlock, en);
        ui->flagsLine->setText("0x" + QString::number(flyParams.flags, 16));
    });
    connect(ui->gyroCorrCheck, &QCheckBox::stateChanged, this, [=](int en) {
        setFlag(flyParams.flags, GyroCorrection, en);
        ui->flagsLine->setText("0x" + QString::number(flyParams.flags, 16));
    });

    initCurrentValues();
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
        ui->log->append("Closed serial ");
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

void MainWindow::sendCmd(const ClientCmd &cmd)
{
    if (!serial.isOpen()) {
        ui->log->append("Open serial before send command");
        return;
    }
    const QString cmdType = "0x" + QString::number(cmd.type, 16);
    const QByteArray data(reinterpret_cast<const char *>(&cmd), sizeof(cmd));
    bool ok = serial.write(data) == data.size();
    if (ok) {
        serial.flush();
        // ui->log->append("Cmd " + cmdType + " sent [" + QString::number(data.size()) + "]: " + data.toHex());
    } else {
        ui->log->append("Error sending cmd " + cmdType);
    }
}

void MainWindow::sendSetConnection()
{
    ConnectionParams params{};
    strncpy(params.wifiSsid, ui->ssidEdit->text().toStdString().c_str(), sizeof(params.wifiSsid));
    strncpy(params.wifiPassw, ui->passEdit->text().toStdString().c_str(), sizeof(params.wifiPassw));
    strncpy(params.ip, ui->ipEdit->text().toStdString().c_str(), sizeof(params.ip));
    params.recvPort = ui->recvPortEdit->text().toUShort();
    params.sendPort = ui->sendPortEdit->text().toUShort();
    params.timeout = CONN_TIMEOUT;

    ClientCmd cmd;
    cmd.type = TypeSetConnection;
    cmd.data.connParams = params;

    sendCmd(cmd);
}

void MainWindow::sendGetConnection()
{
    ClientCmd cmd;
    cmd.type = TypeGetConnection;
    sendCmd(cmd);
}

void MainWindow::setHeartbeat()
{
    if (timerHb.isActive()) {
        timerHb.stop();
        ui->log->append("HB disabled");
    } else {
        timerHb.start();
        ui->log->append("HB enabled");
    }
}

void MainWindow::sendHeartbeat()
{
    ClientCmd cmd;
    cmd.type = TypeDroneCmd;
    cmd.data.droneCmd = DroneCmd_HEARTBEAT;
    sendCmd(cmd);
}

void MainWindow::setFlyCmd()
{
    if (timerFly.isActive()) {
        timerFly.stop();
        ui->log->append("Fly disabled");
    } else {
        timerFly.start();
        ui->log->append("Fly enabled");
    }
}

void MainWindow::sendFlyCmd()
{
    ClientCmd cmd;
    cmd.type = TypeFlyCmd;
    cmd.data.flyCmd.flyParams = flyParams;
    cmd.data.flyCmd.flyParams.normalize();
    cmd.data.flyCmd.crc = calculateCrc(&cmd.data.flyCmd.flyParams, sizeof(cmd.data.flyCmd.flyParams));
    sendCmd(cmd);
}

void MainWindow::sendEnableControl()
{
    ui->log->append("FIXME: to be implemented");
}

void MainWindow::sendStopControl()
{
    ClientCmd cmd;
    cmd.type = TypeDroneCmd;
    cmd.data.droneCmd = DroneCmd_STOP_CONTROL;
    sendCmd(cmd);
}

void MainWindow::sendSwitchCamFront()
{
    ClientCmd cmd;
    cmd.type = TypeDroneCmd;
    cmd.data.droneCmd = DroneCmd_SWITCH_CAM_FRONT;
    sendCmd(cmd);
}

void MainWindow::sendSwitchCamBack()
{
    ClientCmd cmd;
    cmd.type = TypeDroneCmd;
    cmd.data.droneCmd = DroneCmd_SWITCH_CAM_BACK;
    sendCmd(cmd);
}

void MainWindow::sendAckPhoto()
{
    ClientCmd cmd;
    cmd.type = TypeDroneCmd;
    cmd.data.droneCmd = DroneCmd_ACK_PHOTO;
    sendCmd(cmd);
}

void MainWindow::sendAckVideo()
{
    ClientCmd cmd;
    cmd.type = TypeDroneCmd;
    cmd.data.droneCmd = DroneCmd_ACK_VIDEO;
    sendCmd(cmd);
}

void MainWindow::initCurrentValues()
{
    ui->leftRightSlider->setValue(FLY_PAR_NEUTRAL);
    ui->frontBackSlider->setValue(FLY_PAR_NEUTRAL);
    ui->accelSlider->setValue(FLY_PAR_NEUTRAL);
    ui->turnSlider->setValue(FLY_PAR_NEUTRAL);
    ui->fastFlyCheck->setChecked(false);
    ui->fastDropCheck->setChecked(false);
    ui->emergStopCheck->setChecked(false);
    ui->circleTurnEndCheck->setChecked(false);
    ui->noHeadCheck->setChecked(false);
    ui->unlockCheck->setChecked(false);
    ui->gyroCorrCheck->setChecked(false);
}

void MainWindow::readSerial()
{
    while (serial.bytesAvailable() >= sizeof(ClientResp)) {
        ClientResp resp;
        serial.read(reinterpret_cast<char *>(&resp), sizeof(resp));

        switch (resp.type) {
        case TypeAck:
            if (resp.data.ack == 0) {
                ui->log->append(QString("Ack: %1").arg(resp.data.ack));
            }
            break;
        case TypeConnectionStat:
            ui->log->append(QString("Connection: %1").arg(resp.data.connected));
            break;
        case TypeDroneTlm:
            switch(resp.data.droneTlm.numType){
            case TypePhoto:
                sendAckPhoto();
                break;
            case TypeVideo:
                sendAckVideo();
                break;
            }

            break;
        default:
            ui->log->append("Unknown response type: " + QString::number(resp.type));
            while (serial.bytesAvailable() > 0) {
                serial.read(serial.bytesAvailable()); // discard all
            }
            break;
        }
    }
}
