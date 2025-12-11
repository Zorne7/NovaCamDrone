package com.cooingdv.bl60xmjpeg.utils;

import com.cooingdv.bl60xmjpeg.callback.ReceiveDataCallback;

/* loaded from: classes.dex */
public class GLJni {
    private static final String TAG = "GLJni";
    private ReceiveDataCallback mDataCallback;

    public native void UnregisterDeviceStatus();

    public native void nativeInit();

    public native int nativeSendCommand(byte[] bArr);

    public native void nativeSetCameraIndex(int i);

    public native int nativeSetModify(String str, int i, int i2, int i3, int i4, int i5);

    public native void nativeSetQPara(int i, int i2, int i3, int i4);

    public native void nativeStart();

    public native void nativeStop();

    public native void nativeUninit();

    static {
        System.loadLibrary("uav_gl");
    }

    public void setDataCallback(ReceiveDataCallback receiveDataCallback) {
        this.mDataCallback = receiveDataCallback;
    }

    private void cbJpegFromNative(byte[] bArr, long j, byte b) {
        ReceiveDataCallback receiveDataCallback = this.mDataCallback;
        if (receiveDataCallback != null) {
            receiveDataCallback.picData(bArr, j, b);
        }
    }

    private void cbCtlMsgFromNative(byte[] bArr, long j) {
        ReceiveDataCallback receiveDataCallback = this.mDataCallback;
        if (receiveDataCallback != null) {
            receiveDataCallback.picMessage(bArr);
        }
    }

    private void cbDeviceStatusFromNative(byte[] bArr, long j) {
        ReceiveDataCallback receiveDataCallback = this.mDataCallback;
        if (receiveDataCallback != null) {
            receiveDataCallback.deviceStatus(bArr, j);
        }
    }
}
