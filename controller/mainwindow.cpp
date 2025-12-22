#include "mainwindow.h"
#include "./ui_mainwindow.h"

#include <QLineEdit>
#include <QVBoxLayout>
#include <QWidget>

static const QList<int> BAUDRATES = {0, 115200, 921600, 1000000, 2000000};

MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::MainWindow)
{
    ui->setupUi(this);

    for (int rate : BAUDRATES) {
        ui->baudSelector->addItem(QString::number(rate), rate);
    }
    ui->baudSelector->setCurrentIndex(0);
    refreshAvailableSerialPorts();

    connect(&droneCtrl, &DroneController::errorOccurred, ui->log, &QTextEdit::append);
    connect(&droneCtrl, &DroneController::ackRecv, this, &MainWindow::onAckRecv);
    connect(&droneCtrl, &DroneController::connStatusRecv, this, &MainWindow::onConnStatusRecv);
    connect(&droneCtrl, &DroneController::frameReady, this, &MainWindow::onFrameReady);

    connect(ui->btnSetConn, &QPushButton::clicked, this, &MainWindow::sendSetConnection);
    connect(ui->btnGetConn, &QPushButton::clicked, &droneCtrl, &DroneController::sendGetConnection);
    connect(ui->btnHeartbeat, &QPushButton::clicked, &droneCtrl, &DroneController::setHeartbeat);
    connect(ui->btnFly, &QPushButton::clicked, &droneCtrl, &DroneController::setFlyCmd);
    connect(ui->btnVideo, &QPushButton::clicked, this, &MainWindow::setVideo);
    connect(ui->btnStop, &QPushButton::clicked, &droneCtrl, &DroneController::sendStopControl);
    connect(ui->btnCamFront, &QPushButton::clicked, &droneCtrl, &DroneController::sendSwitchCamFront);
    connect(ui->btnCamBack, &QPushButton::clicked, &droneCtrl, &DroneController::sendSwitchCamBack);
    connect(ui->resetBtn, &QPushButton::clicked, this, &MainWindow::initCurrentValues);
    connect(ui->btnClear, &QPushButton::clicked, ui->log, &QTextEdit::clear);

    connect(ui->btnRefresh, &QPushButton::clicked, this, &MainWindow::refreshAvailableSerialPorts);
    connect(ui->baudSelector, &QComboBox::currentTextChanged, this, &MainWindow::setSerial);

    connect(ui->leftRightSlider, &QSlider::valueChanged, this, [=](int val) {
        droneCtrl.getFlyControls()->controlByte1 = val;
        ui->control1Line->setText(QString::number(droneCtrl.getFlyControls()->controlByte1));
    });
    connect(ui->frontBackSlider, &QSlider::valueChanged, this, [=](int val) {
        droneCtrl.getFlyControls()->controlByte2 = val;
        ui->control2Line->setText(QString::number(droneCtrl.getFlyControls()->controlByte2));
    });
    connect(ui->accelSlider, &QSlider::valueChanged, this, [=](int val) {
        droneCtrl.getFlyControls()->controlAccelerator = val;
        ui->accellerLine->setText(QString::number(droneCtrl.getFlyControls()->controlAccelerator));
    });
    connect(ui->turnSlider, &QSlider::valueChanged, this, [=](int val) {
        droneCtrl.getFlyControls()->controlTurn = val;
        ui->turnLine->setText(QString::number(droneCtrl.getFlyControls()->controlTurn));
    });
    connect(ui->fastFlyCheck, &QCheckBox::stateChanged, this, [=](int en) {
        Flag_Set(droneCtrl.getFlyControls()->flags, ControlFlag_FastFly, en);
        ui->flagsLine->setText(hex(droneCtrl.getFlyControls()->flags));
    });
    connect(ui->fastDropCheck, &QCheckBox::stateChanged, this, [=](int en) {
        Flag_Set(droneCtrl.getFlyControls()->flags, ControlFlag_FastDrop, en);
        ui->flagsLine->setText(hex(droneCtrl.getFlyControls()->flags));
    });
    connect(ui->emergStopCheck, &QCheckBox::stateChanged, this, [=](int en) {
        Flag_Set(droneCtrl.getFlyControls()->flags, ControlFlag_EmergencyStop, en);
        ui->flagsLine->setText(hex(droneCtrl.getFlyControls()->flags));
    });
    connect(ui->circleTurnEndCheck, &QCheckBox::stateChanged, this, [=](int en) {
        Flag_Set(droneCtrl.getFlyControls()->flags, ControlFlag_CircleTurnEnd, en);
        ui->flagsLine->setText(hex(droneCtrl.getFlyControls()->flags));
    });
    connect(ui->noHeadCheck, &QCheckBox::stateChanged, this, [=](int en) {
        Flag_Set(droneCtrl.getFlyControls()->flags, ControlFlag_NoHeadMode, en);
        ui->flagsLine->setText(hex(droneCtrl.getFlyControls()->flags));
    });
    connect(ui->unlockCheck, &QCheckBox::stateChanged, this, [=](int en) {
        Flag_Set(droneCtrl.getFlyControls()->flags, ControlFlag_Unlock, en);
        ui->flagsLine->setText(hex(droneCtrl.getFlyControls()->flags));
    });
    connect(ui->unknownCheck, &QCheckBox::stateChanged, this, [=](int en) {
        Flag_Set(droneCtrl.getFlyControls()->flags, ControlFlag_Unknown, en);
        ui->flagsLine->setText(hex(droneCtrl.getFlyControls()->flags));
    });
    connect(ui->gyroCorrCheck, &QCheckBox::stateChanged, this, [=](int en) {
        Flag_Set(droneCtrl.getFlyControls()->flags, ControlFlag_GyroCorrection, en);
        ui->flagsLine->setText(hex(droneCtrl.getFlyControls()->flags));
    });

    initCurrentValues();
}

MainWindow::~MainWindow()
{
    delete ui;
}

void MainWindow::refreshAvailableSerialPorts()
{
    disconnect(ui->portSelector, &QComboBox::currentTextChanged, this, &MainWindow::setSerial);

    ui->portSelector->clear();
    for (const QSerialPortInfo &info : QSerialPortInfo::availablePorts()) {
        ui->portSelector->addItem(info.portName());
    }

    connect(ui->portSelector, &QComboBox::currentTextChanged, this, &MainWindow::setSerial);
}

void MainWindow::setSerial()
{
    const QString portName = ui->portSelector->currentText();
    const int baudRate = ui->baudSelector->currentData().toInt();

    const bool ok = droneCtrl.setSerial(portName, baudRate);
    if (ok && baudRate == 0) {
        ui->log->append("Serial closed");
        ui->btnRefresh->setEnabled(true);
        return;
    }
    if (!ok) {
        ui->log->append("Error setting serial " + portName);
    } else {
        ui->log->append(QString("Serial %1 opened with baudrate %2").arg(portName).arg(baudRate));
        ui->btnRefresh->setEnabled(false);
    }
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
    ui->unknownCheck->setChecked(false);
    ui->gyroCorrCheck->setChecked(false);
}

void MainWindow::sendSetConnection()
{
    ConnParams connParams{};
    strncpy(connParams.ssid, ui->ssidEdit->text().toUtf8().data(), sizeof(connParams.ssid));
    connParams.timeout = ui->connTimeoutEdit->text().toUInt();
    droneCtrl.sendSetConnection(connParams);
}

void MainWindow::setVideo()
{
    bool ok = droneCtrl.setVideo(ui->btnVideo->isChecked());
    if(!ok){
        ui->btnVideo->setChecked(!ui->btnVideo->isChecked());
    }
}

void MainWindow::onAckRecv(const Ack &ack)
{
    if (ack.val == 0 || ui->debugCheck->isChecked()) {
        ui->log->append(QString("Ack: Cmd = %1, Val = %2").arg(ack.cmd.val).arg(ack.val));
    }
}

void MainWindow::onConnStatusRecv(status_t connStatus)
{
    ui->connStatusEdit->setText(QString::number(connStatus));
}

void MainWindow::onFrameReady(const QByteArray &frameData)
{
    QImage img;
    img.loadFromData(frameData, "JPEG");
    if (!img.isNull()) {
        ui->frame->setPixmap(QPixmap::fromImage(img));
    }
}
