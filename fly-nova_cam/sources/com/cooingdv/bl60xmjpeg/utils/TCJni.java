package com.cooingdv.bl60xmjpeg.utils;

import com.cooingdv.bl60xmjpeg.callback.ReceiveDataCallback;

/* loaded from: classes.dex */
public class TCJni {
    private static final String TAG = "TCJni";
    private static ReceiveDataCallback mDataCallback;

    private native int nativeSetModify(String str, int i, int i2, int i3, int i4);

    public native int nativeSendCommand(byte[] bArr);

    public native int setActiveCameraIndex(int i);

    public native int setQPara(int i, int i2, int i3, int i4);

    public native String stringFromJNI();

    public native int unimplementedStringFromJNI();

    static {
        System.loadLibrary("uav_tc");
    }

    public static void setDataCallback(ReceiveDataCallback receiveDataCallback) {
        mDataCallback = receiveDataCallback;
    }

    public void setModify(String str, int i, int i2, int i3, int i4) {
        nativeSetModify(str, i, i2, i3, i4);
    }

    public static int function_for_pic(byte[] bArr, long j, byte b) {
        ReceiveDataCallback receiveDataCallback = mDataCallback;
        if (receiveDataCallback == null) {
            return 1;
        }
        receiveDataCallback.picData(bArr, j, b);
        return 1;
    }

    public static int java_function_for_mcuctl(byte[] bArr, int i, int i2) {
        ReceiveDataCallback receiveDataCallback = mDataCallback;
        if (receiveDataCallback == null) {
            return 0;
        }
        receiveDataCallback.picMessage(bArr);
        return 0;
    }
}
