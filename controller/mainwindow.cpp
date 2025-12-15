#include "mainwindow.h"
#include "./ui_mainwindow.h"

#include <QEventLoop>
#include <QLineEdit>
#include <QMetaEnum>
#include <QVBoxLayout>
#include <QWidget>

static inline void ms_sleep(int ms)
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

template <typename T>
static inline const QString num2str(T value)
{
    return QString::number(value);
}

template <typename T>
static inline const QString hex(T value)
{
    return QString("0x%1").arg(value, sizeof(value) * 2, 16, QLatin1Char('0'));
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
        ui->baudSelector->addItem(num2str(rate), rate);
    }
    ui->baudSelector->setCurrentText("0");

    timerHb.setInterval(HB_INTERVAL_MS);
    timerHb.setSingleShot(false);
    connect(&timerHb, &QTimer::timeout, this, &MainWindow::sendHeartbeat);

    timerFly.setInterval(FLY_INTERVAL_MS);
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
    connect(ui->btnClear, &QPushButton::clicked, ui->log, &QTextEdit::clear);

    connect(ui->portSelector, &QComboBox::currentTextChanged, this, &MainWindow::openSerial);
    connect(ui->baudSelector, &QComboBox::currentTextChanged, this, &MainWindow::openSerial);

    connect(ui->leftRightSlider, &QSlider::valueChanged, this, [=](int val) {
        flyParams.controlByte1 = val;
        ui->control1Line->setText(num2str(flyParams.controlByte1));
    });
    connect(ui->frontBackSlider, &QSlider::valueChanged, this, [=](int val) {
        flyParams.controlByte2 = val;
        ui->control2Line->setText(num2str(flyParams.controlByte2));
    });
    connect(ui->accelSlider, &QSlider::valueChanged, this, [=](int val) {
        flyParams.controlAccelerator = val;
        ui->accellerLine->setText(num2str(flyParams.controlAccelerator));
    });
    connect(ui->turnSlider, &QSlider::valueChanged, this, [=](int val) {
        flyParams.controlTurn = val;
        ui->turnLine->setText(num2str(flyParams.controlTurn));
    });
    connect(ui->fastFlyCheck, &QCheckBox::stateChanged, this, [=](int en) {
        setFlag(flyParams.flags, FlagFastFly, en);
        ui->flagsLine->setText(hex(flyParams.flags));
    });
    connect(ui->fastDropCheck, &QCheckBox::stateChanged, this, [=](int en) {
        setFlag(flyParams.flags, FlagFastDrop, en);
        ui->flagsLine->setText(hex(flyParams.flags));
    });
    connect(ui->emergStopCheck, &QCheckBox::stateChanged, this, [=](int en) {
        setFlag(flyParams.flags, FlagEmergencyStop, en);
        ui->flagsLine->setText(hex(flyParams.flags));
    });
    connect(ui->circleTurnEndCheck, &QCheckBox::stateChanged, this, [=](int en) {
        setFlag(flyParams.flags, FlagCircleTurnEnd, en);
        ui->flagsLine->setText(hex(flyParams.flags));
    });
    connect(ui->noHeadCheck, &QCheckBox::stateChanged, this, [=](int en) {
        setFlag(flyParams.flags, FlagNoHeadMode, en);
        ui->flagsLine->setText(hex(flyParams.flags));
    });
    connect(ui->unlockCheck, &QCheckBox::stateChanged, this, [=](int en) {
        setFlag(flyParams.flags, FlagUnlock, en);
        ui->flagsLine->setText(hex(flyParams.flags));
    });
    connect(ui->gyroCorrCheck, &QCheckBox::stateChanged, this, [=](int en) {
        setFlag(flyParams.flags, FlagGyroCorrection, en);
        ui->flagsLine->setText(hex(flyParams.flags));
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

    const QString portName = ui->portSelector->currentText();
    const int baudRate = ui->baudSelector->currentData().toInt();
    if (baudRate == 0) {
        ui->log->append("Closed serial");
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

void MainWindow::sendCmd(const ClientPacket &cmd)
{
    if (!serial.isOpen()) {
        ui->log->append("Open serial before send command");
        return;
    }
    const QString cmdType = hex(cmd.type);
    const QByteArray data(reinterpret_cast<const char *>(&cmd), sizeof(cmd));
    bool ok = serial.write(data) == data.size();
    if (ok) {
        serial.flush();
        if(ui->debugCheck->isChecked()){
            ui->log->append("CMD " + cmdType + " [" + num2str(data.size()) + "]: " + data.toHex());
        }
    } else {
        ui->log->append("Error sending cmd " + cmdType);
    }
}

void MainWindow::sendSetConnection()
{
    ConnParams params{};
    strncpy(params.ssid, ui->ssidEdit->text().toStdString().c_str(), sizeof(params.ssid));
    params.timeout = ui->connTimeoutEdit->text().toUInt();

    ClientPacket cmd;
    cmd.type = PacketType_SetConnection;
    cmd.data.connParams = params;

    sendCmd(cmd);
}

void MainWindow::sendGetConnection()
{
    ClientPacket cmd;
    cmd.type = PacketType_GetConnection;
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
    ClientPacket cmd;
    cmd.type = PacketType_DroneCmd;
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
    ClientPacket cmd;
    cmd.type = PacketType_FlyCmd;
    cmd.data.flyCmd = FlyCmd(flyParams);
    sendCmd(cmd);
}

void MainWindow::sendEnableControl()
{
    ui->log->append("FIXME: to be implemented");
}

void MainWindow::sendStopControl()
{
    ClientPacket cmd;
    cmd.type = PacketType_DroneCmd;
    cmd.data.droneCmd = DroneCmd_STOP_CONTROL;
    sendCmd(cmd);
}

void MainWindow::sendSwitchCamFront()
{
    ClientPacket cmd;
    cmd.type = PacketType_DroneCmd;
    cmd.data.droneCmd = DroneCmd_SWITCH_CAM_FRONT;
    sendCmd(cmd);
}

void MainWindow::sendSwitchCamBack()
{
    ClientPacket cmd;
    cmd.type = PacketType_DroneCmd;
    cmd.data.droneCmd = DroneCmd_SWITCH_CAM_BACK;
    sendCmd(cmd);
}

void MainWindow::sendAckPhoto()
{
    ClientPacket cmd;
    cmd.type = PacketType_DroneCmd;
    cmd.data.droneCmd = DroneCmd_ACK_PHOTO;
    sendCmd(cmd);
}

void MainWindow::sendAckVideo()
{
    ClientPacket cmd;
    cmd.type = PacketType_DroneCmd;
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
    ClientPacket resp;

    while (serial.bytesAvailable() >= sizeof(resp)) {
        int r = serial.read(reinterpret_cast<char *>(&resp), sizeof(resp));
        if(r < 0){
            ui->log->append("Error reading packet from serial");
            break;
        }

        switch (resp.type) {
        case PacketType_Ack:
            if (resp.data.ack.res == 0 || ui->debugCheck->isChecked()) {
                ui->log->append(QString("Ack: Cmd = %1, Res = %2").arg(hex(resp.data.ack.cmd)).arg(resp.data.ack.res));
            }
            break;

        case PacketType_ConnectionStat:
            ui->log->append(QString("Connection: %1").arg(resp.data.connected));
            break;

        case PacketType_DroneTlm:
            if(ui->debugCheck->isChecked()){
                const QByteArray tlmData(reinterpret_cast<const char *>(&resp.data.droneTlm), sizeof(resp.data.droneTlm));
                ui->log->append("TLM [" + num2str(tlmData.size()) + "]: " + tlmData.toHex());
            }
            switch(resp.data.droneTlm.numType){
            case TlmType_Photo:
                sendAckPhoto();
                break;
            case TlmType_Video:
                sendAckVideo();
                break;
            }
            break;

        case PacketType_DroneVideo:
            r = serial.read(reinterpret_cast<char *>(&videoPayload), resp.data.videoPayloadSize);
            if(r < 0){
                ui->log->append("Error reading video payload from serial");
            } else if(ui->debugCheck->isChecked()) {
                ui->log->append("VIDEO [" + num2str(resp.data.videoPayloadSize) + "]");
            }
            break;

        default:
            ui->log->append("Unknown response type: " + hex(resp.type));
            // reset serial I/F
            serial.close();
            serial.open(QIODevice::ReadWrite);
            break;
        }
    }
}
