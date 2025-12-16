#include "mainwindow.h"
#include "./ui_mainwindow.h"

#include <QEventLoop>
#include <QLineEdit>
#include <QMetaEnum>
#include <QVBoxLayout>
#include <QWidget>

static const QList<int> BAUDRATES = {0, 9600, 19200, 38400, 57600, 115200, 921600};

static inline void ms_sleep(int ms)
{
    QEventLoop loop;
    QTimer timer;
    QObject::connect(&timer, &QTimer::timeout, &loop, &QEventLoop::quit);
    timer.start(ms);
    loop.exec();
}

template <typename T>
static inline const QByteArray toData(const T &s)
{
    return QByteArray(reinterpret_cast<const char *>(&s), sizeof(s));
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
    for (int rate : BAUDRATES) {
        ui->baudSelector->addItem(num2str(rate), rate);
    }
    ui->baudSelector->setCurrentIndex(0);

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
    connect(ui->btnVideo, &QPushButton::clicked, this, &MainWindow::setVideo);
    connect(ui->btnStop, &QPushButton::clicked, this, &MainWindow::sendStopControl);
    connect(ui->btnCamFront, &QPushButton::clicked, this, &MainWindow::sendSwitchCamFront);
    connect(ui->btnCamBack, &QPushButton::clicked, this, &MainWindow::sendSwitchCamBack);
    connect(ui->resetBtn, &QPushButton::clicked, this, &MainWindow::initCurrentValues);
    connect(ui->btnClear, &QPushButton::clicked, ui->log, &QTextEdit::clear);

    connect(ui->portSelector, &QComboBox::currentTextChanged, this, &MainWindow::openSerial);
    connect(ui->baudSelector, &QComboBox::currentTextChanged, this, &MainWindow::openSerial);

    connect(ui->leftRightSlider, &QSlider::valueChanged, this, [=](int val) {
        flyControls.controlByte1 = val;
        ui->control1Line->setText(num2str(flyControls.controlByte1));
    });
    connect(ui->frontBackSlider, &QSlider::valueChanged, this, [=](int val) {
        flyControls.controlByte2 = val;
        ui->control2Line->setText(num2str(flyControls.controlByte2));
    });
    connect(ui->accelSlider, &QSlider::valueChanged, this, [=](int val) {
        flyControls.controlAccelerator = val;
        ui->accellerLine->setText(num2str(flyControls.controlAccelerator));
    });
    connect(ui->turnSlider, &QSlider::valueChanged, this, [=](int val) {
        flyControls.controlTurn = val;
        ui->turnLine->setText(num2str(flyControls.controlTurn));
    });
    connect(ui->fastFlyCheck, &QCheckBox::stateChanged, this, [=](int en) {
        set_flag(flyControls.flags, ControlFlag_FastFly, en);
        ui->flagsLine->setText(hex(flyControls.flags));
    });
    connect(ui->fastDropCheck, &QCheckBox::stateChanged, this, [=](int en) {
        set_flag(flyControls.flags, ControlFlag_FastDrop, en);
        ui->flagsLine->setText(hex(flyControls.flags));
    });
    connect(ui->emergStopCheck, &QCheckBox::stateChanged, this, [=](int en) {
        set_flag(flyControls.flags, ControlFlag_EmergencyStop, en);
        ui->flagsLine->setText(hex(flyControls.flags));
    });
    connect(ui->circleTurnEndCheck, &QCheckBox::stateChanged, this, [=](int en) {
        set_flag(flyControls.flags, ControlFlag_CircleTurnEnd, en);
        ui->flagsLine->setText(hex(flyControls.flags));
    });
    connect(ui->noHeadCheck, &QCheckBox::stateChanged, this, [=](int en) {
        set_flag(flyControls.flags, ControlFlag_NoHeadMode, en);
        ui->flagsLine->setText(hex(flyControls.flags));
    });
    connect(ui->unlockCheck, &QCheckBox::stateChanged, this, [=](int en) {
        set_flag(flyControls.flags, ControlFlag_Unlock, en);
        ui->flagsLine->setText(hex(flyControls.flags));
    });
    connect(ui->unknownCheck, &QCheckBox::stateChanged, this, [=](int en) {
        set_flag(flyControls.flags, ControlFlag_Unknown, en);
        ui->flagsLine->setText(hex(flyControls.flags));
    });
    connect(ui->gyroCorrCheck, &QCheckBox::stateChanged, this, [=](int en) {
        set_flag(flyControls.flags, ControlFlag_GyroCorrection, en);
        ui->flagsLine->setText(hex(flyControls.flags));
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
        ui->log->append(QString("Serial %1 opened with baudrate %2").arg(portName).arg(baudRate));
        connect(&serial, &QSerialPort::readyRead, this, &MainWindow::readSerial);
    }
}

void MainWindow::resetSerial()
{
    serial.close();
    serial.open(QIODevice::ReadWrite);
}

bool MainWindow::sendCmd(const ClientPacket &cmd)
{
    if (!serial.isOpen()) {
        ui->log->append("Open serial before send command");
        return false;
    }
    const QByteArray data = toData(cmd);
    bool ok = serial.write(data) == data.size();
    if (ok) {
        serial.flush();
        if(ui->debugCheck->isChecked()){
            ui->log->append("CMD " + hex(cmd.type) + " [" + num2str(data.size()) + "]: " + data.toHex());
        }
    } else {
        ui->log->append("Error sending cmd " + hex(cmd.type));
    }
    return ok;
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
    cmd.data.flyCmd = FlyCmd(flyControls);
    sendCmd(cmd);
}

void MainWindow::setVideo()
{
    ClientPacket cmd;
    cmd.type = PacketType_SetStream;
    cmd.data.streamStatusReq = ui->btnVideo->isChecked() ? StreamStatus_Enabled : StreamStatus_Disabled;
    bool ok = sendCmd(cmd);
    if(!ok){
        ui->btnVideo->setChecked(!ui->btnVideo->isChecked());
        return;
    }
    if(cmd.data.streamStatusReq == StreamStatus_Enabled){
        ui->log->append("Video enabled");
    }else{
        ui->log->append("Video disabled");
    }
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
    ui->leftRightSlider->setValue(FLY_CONTROL_NEUTRAL);
    ui->frontBackSlider->setValue(FLY_CONTROL_NEUTRAL);
    ui->accelSlider->setValue(FLY_CONTROL_NEUTRAL);
    ui->turnSlider->setValue(FLY_CONTROL_NEUTRAL);
    ui->fastFlyCheck->setChecked(false);
    ui->fastDropCheck->setChecked(false);
    ui->emergStopCheck->setChecked(false);
    ui->circleTurnEndCheck->setChecked(false);
    ui->noHeadCheck->setChecked(false);
    ui->unlockCheck->setChecked(false);
    ui->gyroCorrCheck->setChecked(false);
}

void MainWindow::parseFeedback()
{
    switch (fdbk.type) {
    case PacketType_Ack:
        if (fdbk.data.ack.val == AckVal_KO || ui->debugCheck->isChecked()) {
            ui->log->append(QString("Ack: Cmd = %1, Val = %2")
                                .arg(hex(fdbk.data.ack.cmd))
                                .arg(hex(fdbk.data.ack.val)));
        }
        if (fdbk.data.ack.val == AckVal_KO && fdbk.data.ack.cmd == PacketType_SetStream) {
            ui->btnVideo->setChecked(!ui->btnVideo->isChecked());
        }
        break;

    case PacketType_ConnectionStat:
        ui->log->append("Connection status: " + hex(fdbk.data.connStatus));
        break;

    case PacketType_DroneTlm:
        if(ui->debugCheck->isChecked()){
            const QByteArray tlmData = toData(fdbk.data.droneTlm);
            ui->log->append("TLM [" + num2str(tlmData.size()) + "]: " + tlmData.toHex());
        }
        switch(fdbk.data.droneTlm.fdbkType){
        case FdbkType_Photo:
            sendAckPhoto(); // TODO: send ack only if necessary
            break;
        case FdbkType_Video:
            sendAckVideo(); // TODO: send ack only if necessary
            break;
        }
        break;

    case PacketType_DroneStream:
        videoData = VideoData();
        videoData.size = fdbk.data.videoPayloadSize;
        break;

    default:
        ui->log->append("Unknown feedback type: " + hex(fdbk.type));
        resetSerial();
        break;
    }
}

void MainWindow::readSerial()
{
    while (serial.bytesAvailable() > 0) {

        const int bytesAvailable = serial.bytesAvailable();
        const int remaningVideoData = videoData.remaningSize();

        char *ptr = remaningVideoData > 0 ? videoData.writePtr() : reinterpret_cast<char *>(&fdbk);
        const int size = remaningVideoData > 0 ? MIN(bytesAvailable, remaningVideoData) : sizeof(fdbk);
        if(bytesAvailable < size){
            break;
        }

        int r = serial.read(ptr, size);
        if(r < size){
            ui->log->append("Error reading packet from serial");
            videoData = VideoData();
            resetSerial();
            break;
        }

        if(remaningVideoData > 0){
            videoData.wIdx += r;
            if(videoData.remaningSize() <= 0){
                ui->log->append("VIDEO RECEIVED " + num2str(videoData.size));
            }
        }else{
            parseFeedback();
        }
    }
}
