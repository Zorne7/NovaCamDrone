package com.cooingdv.bl60xmjpeg;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import com.cooingdv.bl60xmjpeg.callback.PicDataCallback;
import com.cooingdv.bl60xmjpeg.callback.ReceiveDataCallback;
import com.cooingdv.bl60xmjpeg.utils.BufChangeHex;
import com.cooingdv.bl60xmjpeg.utils.GLJni;
import com.cooingdv.bl60xmjpeg.utils.LogUtils;
import com.cooingdv.bl60xmjpeg.utils.TCJni;
import com.yls.nova.tools.IActions;

/* loaded from: classes.dex */
public class UAV {

    /* renamed from: GL */
    public static final int f50GL = 2;
    private static final String TAG = "UAV";

    /* renamed from: TC */
    public static final int f51TC = 10;
    public static final int UNKNOWN = 0;
    private boolean isActive;
    private boolean isGetNumber;
    private boolean isInit;
    private boolean isSetQuality;
    private boolean isStop;
    private Context mContext;
    private ReceiveDataCallback mDataCallback;
    private int mDeviceType;
    private GLJni mGLJni;
    private Handler mHandler;
    private PicDataCallback mPicDataCallback;
    private TCJni mTCJni;
    private String messages;
    private int resolutionNumber;

    /* synthetic */ UAV(C04911 c04911) {
        this();
    }

    public void setPicDataCallback(PicDataCallback picDataCallback) {
        this.mPicDataCallback = picDataCallback;
    }

    private UAV() {
        this.mDeviceType = 0;
        this.resolutionNumber = 0;
        this.messages = "";
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mDataCallback = new C04911();
    }

    public static UAV getInstance() {
        return UAVHolder.sUAV;
    }

    private static class UAVHolder {
        private static final UAV sUAV = new UAV(null);

        private UAVHolder() {
        }
    }

    public void init(Context context) {
        if (this.isInit) {
            return;
        }
        this.isInit = true;
        this.mContext = context.getApplicationContext();
        if (this.mGLJni == null) {
            this.mGLJni = new GLJni();
        }
        if (this.mTCJni == null) {
            this.mTCJni = new TCJni();
        }
        this.mGLJni.nativeInit();
        this.mGLJni.setDataCallback(this.mDataCallback);
        TCJni.setDataCallback(this.mDataCallback);
    }

    public boolean isGetNumber() {
        return this.isGetNumber;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public void setActive(boolean z) {
        TCJni tCJni;
        if (this.isActive) {
            if (this.mDeviceType == 10 && (tCJni = this.mTCJni) != null) {
                tCJni.unimplementedStringFromJNI();
                this.mHandler.postDelayed(new Runnable() { // from class: com.cooingdv.bl60xmjpeg.UAV$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.startServer();
                    }
                }, 1000L);
            }
            this.mDeviceType = 0;
        }
        this.isActive = z;
        this.isGetNumber = z;
        if (z) {
            return;
        }
        this.isSetQuality = false;
    }

    public boolean isSetQuality() {
        return this.isSetQuality;
    }

    public void startServer() {
        GLJni gLJni = this.mGLJni;
        if (gLJni != null) {
            gLJni.nativeStart();
        }
    }

    public void setDeviceType(int i) {
        this.mDeviceType = i;
    }

    public void setResolutionNumber(int i) {
        this.resolutionNumber = i;
    }

    public int getDeviceType() {
        return this.mDeviceType;
    }

    public int getResolutionNumber() {
        return this.resolutionNumber;
    }

    public String getMessages() {
        return this.messages;
    }

    public void sendCommand(byte[] bArr) {
        int i = this.mDeviceType;
        if (i == 10) {
            this.mTCJni.nativeSendCommand(bArr);
        } else if (i == 2) {
            this.mGLJni.nativeSendCommand(bArr);
        }
    }

    public void switchActiveCamera(boolean z) {
        if (this.mDeviceType == 2) {
            this.mGLJni.nativeSetCameraIndex(z ? 1 : 0);
        } else {
            this.mTCJni.setActiveCameraIndex(z ? 1 : 0);
        }
    }

    public void setQPara(int i, int i2, int i3, int i4) {
        if (this.mDeviceType == 10) {
            this.mTCJni.setQPara(i, i2, i3, i4);
        } else {
            this.mGLJni.nativeSetQPara(i, i2, i3, i4);
        }
        this.isSetQuality = true;
    }

    public void setModify(String str, int i, int i2, int i3, int i4, int i5) {
        if (this.mDeviceType == 2) {
            this.mGLJni.nativeSetModify(str, i, i2, i3, i4, i5);
        }
    }

    public void onStop() {
        this.isStop = true;
    }

    public void onStart() {
        this.isStop = false;
    }

    public void onDestroy() {
        TCJni tCJni;
        GLJni gLJni;
        try {
            if (this.mDeviceType == 2 && (gLJni = this.mGLJni) != null) {
                gLJni.UnregisterDeviceStatus();
                this.mGLJni.nativeStop();
                this.mGLJni.nativeUninit();
                this.mGLJni.setDataCallback(null);
            }
            if (this.mDeviceType != 10 || (tCJni = this.mTCJni) == null) {
                return;
            }
            tCJni.unimplementedStringFromJNI();
            TCJni.setDataCallback(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* renamed from: com.cooingdv.bl60xmjpeg.UAV$1 */
    class C04911 implements ReceiveDataCallback {
        C04911() {
        }

        @Override // com.cooingdv.bl60xmjpeg.callback.ReceiveDataCallback
        public void deviceStatus(byte[] bArr, long j) {
            if (UAV.this.mDeviceType == 0) {
                if (j == 10) {
                    UAV.this.mGLJni.nativeStop();
                    UAV.this.mDeviceType = 10;
                    new Thread(new Runnable() { // from class: com.cooingdv.bl60xmjpeg.UAV$1$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.m528lambda$deviceStatus$0$comcooingdvbl60xmjpegUAV$1();
                        }
                    }).start();
                    return;
                }
                UAV.this.mDeviceType = 2;
            }
        }

        /* renamed from: lambda$deviceStatus$0$com-cooingdv-bl60xmjpeg-UAV$1, reason: not valid java name */
        /* synthetic */ void m528lambda$deviceStatus$0$comcooingdvbl60xmjpegUAV$1() {
            UAV.this.mTCJni.stringFromJNI();
        }

        @Override // com.cooingdv.bl60xmjpeg.callback.ReceiveDataCallback
        public void picData(byte[] bArr, long j, byte b) {
            LogUtils.m398d(UAV.TAG, "picData：length" + (bArr.length / 1024) + "kb seq：" + j + " quality：" + ((int) b));
            if (!UAV.this.isActive) {
                UAV.this.isActive = true;
                UAV.this.sendCommand(new byte[]{100});
            }
            if (UAV.this.isStop || UAV.this.mPicDataCallback == null) {
                return;
            }
            UAV.this.mPicDataCallback.onData(bArr, j, b);
        }

        @Override // com.cooingdv.bl60xmjpeg.callback.ReceiveDataCallback
        public void picMessage(byte[] bArr) {
            if (UAV.this.mPicDataCallback != null) {
                UAV.this.mPicDataCallback.onReceiver(bArr);
            }
            if (UAV.this.mDeviceType == 10) {
                int iIntValue = Integer.valueOf(BufChangeHex.byteToHex(bArr[0]), 16).intValue();
                if (iIntValue == 77 || iIntValue == 88) {
                    return;
                }
                UAV.this.resolutionNumber = iIntValue;
                UAV.this.mContext.sendBroadcast(new Intent(IActions.ACTION_SET_FAKE_RESOLUTION));
                return;
            }
            String strBytesToHex = BufChangeHex.bytesToHex(bArr);
            UAV.this.messages = strBytesToHex.substring(2);
            int iIntValue2 = Integer.valueOf(BufChangeHex.byteToHex(bArr[1]), 16).intValue();
            if (iIntValue2 == 77 || iIntValue2 == 88) {
                return;
            }
            UAV.this.resolutionNumber = iIntValue2;
            UAV.this.mContext.sendBroadcast(new Intent(IActions.ACTION_SET_FAKE_RESOLUTION));
        }
    }
}
