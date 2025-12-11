package com.yls.nova.models;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import androidx.fragment.app.Fragment;

/* loaded from: classes.dex */
public class GravityModel implements SensorEventListener {
    private Fragment fragment;
    private float[] gravity = new float[3];
    private boolean isGravityMode = false;
    private Sensor mSensor;
    private SensorManager mSensorManager;
    private OnOperationListener oListener;

    public interface OnOperationListener {
        void onChange(int i, int i2);
    }

    public double getAxis(double d) {
        return ((d / 10.0d) * 50.0d) + 50.0d;
    }

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public GravityModel(Fragment fragment) {
        this.fragment = fragment;
        SensorManager sensorManager = (SensorManager) fragment.getActivity().getSystemService("sensor");
        this.mSensorManager = sensorManager;
        this.mSensor = sensorManager.getDefaultSensor(1);
    }

    public boolean registerSensorListener() {
        SensorManager sensorManager;
        Sensor sensor = this.mSensor;
        if (sensor == null || (sensorManager = this.mSensorManager) == null) {
            return false;
        }
        sensorManager.registerListener(this, sensor, 1);
        this.isGravityMode = true;
        return true;
    }

    public void unRegisterListener() {
        this.isGravityMode = false;
        SensorManager sensorManager = this.mSensorManager;
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    public void setOnOperationListener(OnOperationListener onOperationListener) {
        this.oListener = onOperationListener;
    }

    @Override // android.hardware.SensorEventListener
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor != null && this.isGravityMode && sensorEvent.sensor.getType() == 1) {
            for (int i = 0; i < 3; i++) {
                this.gravity[i] = (sensorEvent.values[i] * 0.8f) + (this.gravity[i] * 0.2f);
            }
            if (this.oListener != null) {
                this.oListener.onChange((int) getAxis(getRealDistance(this.gravity[1] * 0.75f)), (int) getAxis(getRealDistance(this.gravity[0] * 0.75f)));
            }
        }
    }

    private double getRealDistance(float f) {
        double dAbs = Math.abs(f);
        if (dAbs >= 3.0d) {
            dAbs = dAbs <= 5.0d ? (((dAbs - 3.0d) / 1.5d) * 2.0d) + 3.0d : dAbs <= 6.700000286102295d ? (((dAbs - 4.5d) / 1.5d) * 5.0d) + 5.0d : 10.0d;
        }
        return dAbs * (f < 0.0f ? -1 : 1);
    }
}
