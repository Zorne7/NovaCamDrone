#include "mainwindow.h"
#include "./ui_mainwindow.h"

#include <QLineEdit>
#include <QVBoxLayout>
#include <QWidget>
#include <QMetaEnum>

MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::MainWindow)
{
    ui->setupUi(this);

    refreshAvailablePorts();

    connect(&droneCtrl, &DroneController::errorOccurred, this, &MainWindow::onErrOccurred);
    connect(&droneCtrl, &DroneController::ackRecv, this, &MainWindow::onAckRecv);
    connect(&droneCtrl, &DroneController::connStatusRecv, this, &MainWindow::onConnStatusRecv);
    connect(&droneCtrl, &DroneController::frameReady, this, &MainWindow::onFrameReady);

    connect(ui->btnSetConn, &QPushButton::clicked, this, &MainWindow::sendSetConnection);
    connect(ui->btnGetConn, &QPushButton::clicked, &droneCtrl, &DroneController::sendGetConnection);
    connect(ui->btnVideo, &QPushButton::clicked, this, &MainWindow::setVideo);
    connect(ui->btnStop, &QPushButton::clicked, &droneCtrl, &DroneController::sendStopControl);
    connect(ui->btnCamFront, &QPushButton::clicked, &droneCtrl, &DroneController::sendSwitchCamFront);
    connect(ui->btnCamBack, &QPushButton::clicked, &droneCtrl, &DroneController::sendSwitchCamBack);
    connect(ui->resetBtn, &QPushButton::clicked, this, &MainWindow::initCurrentValues);
    connect(ui->btnClear, &QPushButton::clicked, ui->log, &QTextEdit::clear);

    connect(ui->btnRefresh, &QPushButton::clicked, this, &MainWindow::refreshAvailablePorts);

    connect(ui->leftRightSlider, &QSlider::valueChanged, this, [=](int val) {
        updateFlyCtrlPar(flyCtrls.controlByte1, val, ui->control1Line);
    });
    connect(ui->frontBackSlider, &QSlider::valueChanged, this, [=](int val) {
        updateFlyCtrlPar(flyCtrls.controlByte2, val, ui->control2Line);
    });
    connect(ui->accelSlider, &QSlider::valueChanged, this, [=](int val) {
        updateFlyCtrlPar(flyCtrls.controlAccelerator, val, ui->accellerLine);
    });
    connect(ui->turnSlider, &QSlider::valueChanged, this, [=](int val) {
        updateFlyCtrlPar(flyCtrls.controlTurn, val, ui->turnLine);
    });
    connect(ui->fastFlyCheck, &QCheckBox::stateChanged, this, [=](int en) {
        updateFlyCtrlFlag(ControlFlag_FastFly, en);
    });
    connect(ui->fastDropCheck, &QCheckBox::stateChanged, this, [=](int en) {
        updateFlyCtrlFlag(ControlFlag_FastDrop, en);
    });
    connect(ui->emergStopCheck, &QCheckBox::stateChanged, this, [=](int en) {
        updateFlyCtrlFlag(ControlFlag_EmergencyStop, en);
    });
    connect(ui->circleTurnEndCheck, &QCheckBox::stateChanged, this, [=](int en) {
        updateFlyCtrlFlag(ControlFlag_CircleTurnEnd, en);
    });
    connect(ui->noHeadCheck, &QCheckBox::stateChanged, this, [=](int en) {
        updateFlyCtrlFlag(ControlFlag_NoHeadMode, en);
    });
    connect(ui->unlockCheck, &QCheckBox::stateChanged, this, [=](int en) {
        updateFlyCtrlFlag(ControlFlag_Unlock, en);
    });
    connect(ui->unknownCheck, &QCheckBox::stateChanged, this, [=](int en) {
        updateFlyCtrlFlag(ControlFlag_Unknown, en);
    });
    connect(ui->gyroCorrCheck, &QCheckBox::stateChanged, this, [=](int en) {
        updateFlyCtrlFlag(ControlFlag_GyroCorrection, en);
    });

    initCurrentValues();
}

MainWindow::~MainWindow()
{
    if(droneCtrl.portOpened()){
        droneCtrl.sendSetConnection(ConnParams());
    }
    delete ui;
}

void MainWindow::refreshAvailablePorts()
{
    disconnect(ui->portSelector, &QComboBox::currentTextChanged, this, &MainWindow::setPort);

    ui->portSelector->clear();
    ui->portSelector->addItem("");
    for (const QSerialPortInfo &info : QSerialPortInfo::availablePorts()) {
        ui->portSelector->addItem(info.portName());
    }

    connect(ui->portSelector, &QComboBox::currentTextChanged, this, &MainWindow::setPort);
}

void MainWindow::setPort()
{
    const QString portName = ui->portSelector->currentText();

    const bool ok = droneCtrl.setPort(portName);
    if (ok && portName.isEmpty()) {
        ui->log->append("Port closed");
        ui->btnRefresh->setEnabled(true);
        ui->connStatusEdit->clear();
        return;
    }
    if (!ok) {
        ui->log->append("Error setting port " + portName);
        return;
    }

    ui->log->append(QString("Port %1 opened").arg(portName));
    ui->btnRefresh->setEnabled(false);

    droneCtrl.sendGetConnection();
    droneCtrl.sendFlyControls(flyCtrls);
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
    bool ok = droneCtrl.sendSetVideo(ui->btnVideo->isChecked()) && droneCtrl.waitAck(3000) == AckVal_OK;
    if(!ok){
        ui->btnVideo->setChecked(!ui->btnVideo->isChecked());
    }
}

void MainWindow::updateFlyCtrlPar(fly_par_t &par, int newVal, QLineEdit *line)
{
    line->setText(QString::number(newVal));
    if(par == newVal){
        return;
    }
    par = newVal;
    if(droneCtrl.portOpened()){
        droneCtrl.sendFlyControls(flyCtrls);
    }
}

void MainWindow::updateFlyCtrlFlag(FlyControlFlags flag, bool enable)
{
    FlyControlFlags_t flags = flyCtrls.flags;
    Flag_Set(flyCtrls.flags, ControlFlag_GyroCorrection, enable);
    ui->flagsLine->setText(hex(flyCtrls.flags));
    if(droneCtrl.portOpened() && flyCtrls.flags != flags) {
        droneCtrl.sendFlyControls(flyCtrls);
    }
}

void MainWindow::onErrOccurred(const QString &err)
{
    ui->log->append(err);
    if(!droneCtrl.portOpened()){
        ui->portSelector->setCurrentIndex(0);
    }
}

void MainWindow::onAckRecv(const Ack &ack)
{
    if (ack.val == 0 || ui->debugCheck->isChecked()) {
        ui->log->append(QString("Ack: Cmd = %1, Val = %2").arg(hex(ack.cmd)).arg(ack.val));
    }
}

void MainWindow::onConnStatusRecv(ConnStatus_t connStatus)
{
    const QMetaEnum e = QMetaEnum::fromType<ConnStatus>();
    QString status = e.valueToKey(connStatus);
    if(status.isEmpty()){
        status = e.valueToKey(UNKNOWN_STATUS);
    }
    ui->connStatusEdit->setText(status);
}

void MainWindow::onFrameReady(const QByteArray &frameData)
{
    QImage img;
    img.loadFromData(frameData, "JPEG");
    if (!img.isNull()) {
        ui->frame->setPixmap(QPixmap::fromImage(img));
    }
}
