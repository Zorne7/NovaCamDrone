package com.yls.nova.tools;

import android.os.Handler;
import android.widget.ImageView;
import com.cooingdv.bl60xmjpeg.UAV;
import com.yls.nova.C0549R;
import com.yls.nova.socket.SocketClient;
import com.yls.nova.utils.Dbug;
import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/* loaded from: classes.dex */
public class FlyController {
    private Timer mFlyControlTimer;
    private WeakReference<Handler> mHandlerRef;
    private String TAG = getClass().getSimpleName();
    private final int SEND_COMMAND_INTERVAL = 50;
    private final int RESET_COMMAND_INTERVAL = 1000;
    public final int CONTROL_VALUES_MAX = 255;
    public final int CONTROL_VALUES_DEFAULT = 128;
    public final int CONTROL_VALUES_MIN = 1;
    private final int COMMAND_LENGTH = 8;
    private final int CONTROL_VALUES_BYTE0 = 102;
    private final int CONTROL_VALUES_BYTE7 = 153;
    private int controlByte1 = 128;
    private int controlByte2 = 128;
    private int controlAccelerator = 128;
    private int controlTurn = 128;
    private boolean isControlMode = false;
    private boolean isFixedHeightMode = false;
    private boolean isGravitySensor = false;
    private boolean isGestureMode = false;
    private boolean isTrackMode = false;
    private boolean isFastFly = false;
    private boolean isFastDrop = false;
    private boolean isEmergencyStop = false;
    private boolean isCircleTurn = false;
    private boolean isCircleTurnEnd = false;
    private boolean isNoHeadMode = false;
    private boolean isGyroCorrection = false;
    private boolean isUnLock = false;
    private boolean isFastReturn = false;
    private boolean isFilterMode = false;

    public FlyController(Handler handler) {
        this.mHandlerRef = new WeakReference<>(handler);
    }

    public void setController(boolean z) {
        this.isControlMode = z;
        if (z) {
            if (this.mFlyControlTimer == null) {
                Timer timer = new Timer();
                this.mFlyControlTimer = timer;
                timer.schedule(new FlyControlTask(), 50L, 50L);
                return;
            }
            return;
        }
        Timer timer2 = this.mFlyControlTimer;
        if (timer2 != null) {
            timer2.cancel();
            this.mFlyControlTimer = null;
            if (UAV.getInstance().isActive()) {
                UAV.getInstance().sendCommand(new byte[]{101});
            } else {
                SocketClient.getInstance().debugSend(new byte[]{8, 1});
            }
        }
    }

    private class FlyControlTask extends TimerTask {
        private FlyControlTask() {
        }

        /* JADX WARN: Removed duplicated region for block: B:18:0x003e  */
        /* JADX WARN: Type inference failed for: r0v1, types: [boolean, int] */
        @Override // java.util.TimerTask, java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void run() {
            int i;
            ?? r0 = FlyController.this.isFastFly;
            int i2 = r0;
            if (FlyController.this.isFastDrop) {
                i2 = r0 + 2;
            }
            int i3 = i2;
            if (FlyController.this.isEmergencyStop) {
                i3 = i2 + 4;
            }
            int i4 = i3;
            if (FlyController.this.isCircleTurnEnd) {
                i4 = i3 + 8;
            }
            int i5 = i4;
            if (FlyController.this.isNoHeadMode) {
                i5 = i4 + 16;
            }
            if (!FlyController.this.isFastReturn) {
                i = i5;
                if (FlyController.this.isUnLock) {
                    i = i5 + 32;
                }
            }
            int i6 = i;
            if (FlyController.this.isGyroCorrection) {
                i6 = i + 128;
            }
            if (FlyController.this.controlTurn < 104 || FlyController.this.controlTurn > 152) {
                if (FlyController.this.controlTurn > 255) {
                    FlyController.this.controlTurn = 255;
                } else if (FlyController.this.controlTurn < 1) {
                    FlyController.this.controlTurn = 1;
                }
            } else {
                FlyController.this.controlTurn = 128;
            }
            if (FlyController.this.controlAccelerator == 1) {
                FlyController.this.controlAccelerator = 0;
            }
            if (FlyController.this.controlByte1 > 255) {
                FlyController.this.controlByte1 = 255;
            } else if (FlyController.this.controlByte1 < 1) {
                FlyController.this.controlByte1 = 1;
            }
            if (FlyController.this.controlByte2 > 255) {
                FlyController.this.controlByte2 = 255;
            } else if (FlyController.this.controlByte2 < 1) {
                FlyController.this.controlByte2 = 1;
            }
            int i7 = (((FlyController.this.controlByte1 ^ FlyController.this.controlByte2) ^ FlyController.this.controlAccelerator) ^ FlyController.this.controlTurn) ^ (i6 & 255);
            byte[] bArr = {102, (byte) FlyController.this.controlByte1, (byte) FlyController.this.controlByte2, (byte) FlyController.this.controlAccelerator, (byte) FlyController.this.controlTurn, (byte) i6, (byte) i7, -103};
            if (UAV.getInstance().isActive()) {
                UAV.getInstance().sendCommand(bArr);
            } else {
                byte[] bArr2 = new byte[9];
                bArr2[0] = 3;
                System.arraycopy(bArr, 0, bArr2, 1, 8);
                SocketClient.getInstance().debugSend(bArr2);
            }
            Dbug.m417i(FlyController.this.TAG, "byte0:102,byte1:" + FlyController.this.controlByte1 + ",byte2:" + FlyController.this.controlByte2 + ",byte3:" + FlyController.this.controlAccelerator + ",byte4:" + FlyController.this.controlTurn + ",byte5:" + i6 + ",byte6:" + i7 + ",byte7:153");
        }
    }

    public boolean isControlMode() {
        return this.isControlMode;
    }

    public int getControlByte1() {
        return this.controlByte1;
    }

    public boolean isFixedHeightMode() {
        return this.isFixedHeightMode;
    }

    public void setFixedHeightMode(boolean z) {
        this.isFixedHeightMode = z;
    }

    public boolean isGravitySensor() {
        return this.isGravitySensor;
    }

    public void setGravitySensor(boolean z) {
        this.isGravitySensor = z;
    }

    public boolean isGestureMode() {
        return this.isGestureMode;
    }

    public void setGestureMode(boolean z) {
        this.isGestureMode = z;
    }

    public boolean isTrackMode() {
        return this.isTrackMode;
    }

    public void setTrackMode(boolean z) {
        this.isTrackMode = z;
    }

    public void setControlByte1(int i) {
        this.controlByte1 = i;
    }

    public int getControlByte2() {
        return this.controlByte2;
    }

    public void setControlByte2(int i) {
        this.controlByte2 = i;
    }

    public int getControlAccelerator() {
        return this.controlAccelerator;
    }

    public void setControlAccelerator(int i) {
        this.controlAccelerator = i;
    }

    public int getControlTurn() {
        return this.controlTurn;
    }

    public void setControlTurn(int i) {
        this.controlTurn = i;
    }

    public boolean isFastFly() {
        return this.isFastFly;
    }

    public void setFastFly(final ImageView imageView) {
        if (this.isFastFly) {
            return;
        }
        this.isFastFly = true;
        imageView.setImageResource(C0549R.mipmap.icon_up_yellow);
        WeakReference<Handler> weakReference = this.mHandlerRef;
        if (weakReference == null || weakReference.get() == null) {
            return;
        }
        this.mHandlerRef.get().postDelayed(new Runnable() { // from class: com.yls.nova.tools.FlyController.1
            @Override // java.lang.Runnable
            public void run() {
                FlyController.this.isFastFly = false;
                imageView.setImageResource(C0549R.drawable.drawable_up);
            }
        }, 1000L);
    }

    public boolean isFastDrop() {
        return this.isFastDrop;
    }

    public void setFastDrop(final ImageView imageView) {
        if (this.isFastDrop) {
            return;
        }
        this.isFastDrop = true;
        imageView.setImageResource(C0549R.mipmap.icon_down_yellow);
        WeakReference<Handler> weakReference = this.mHandlerRef;
        if (weakReference == null || weakReference.get() == null) {
            return;
        }
        this.mHandlerRef.get().postDelayed(new Runnable() { // from class: com.yls.nova.tools.FlyController.2
            @Override // java.lang.Runnable
            public void run() {
                FlyController.this.isFastDrop = false;
                imageView.setImageResource(C0549R.drawable.drawable_down);
            }
        }, 1000L);
    }

    public boolean isEmergencyStop() {
        return this.isEmergencyStop;
    }

    public void setEmergencyStop(final ImageView imageView) {
        if (this.isEmergencyStop) {
            return;
        }
        this.isEmergencyStop = true;
        imageView.setImageResource(C0549R.mipmap.icon_stop_yellow);
        WeakReference<Handler> weakReference = this.mHandlerRef;
        if (weakReference == null || weakReference.get() == null) {
            return;
        }
        this.mHandlerRef.get().postDelayed(new Runnable() { // from class: com.yls.nova.tools.FlyController.3
            @Override // java.lang.Runnable
            public void run() {
                FlyController.this.isEmergencyStop = false;
                imageView.setImageResource(C0549R.drawable.drawable_stop);
            }
        }, 1000L);
    }

    public boolean isCircleTurn() {
        return this.isCircleTurn;
    }

    public void setCircleTurn(ImageView imageView) {
        boolean z = !this.isCircleTurn;
        this.isCircleTurn = z;
        if (z) {
            imageView.setImageResource(C0549R.drawable.drawable_top_fip_yellow);
        } else {
            imageView.setImageResource(C0549R.drawable.drawable_top_fip);
        }
    }

    public boolean isCircleTurnEnd() {
        return this.isCircleTurnEnd;
    }

    public void setCircleTurnEnd() {
        if (this.isCircleTurnEnd) {
            return;
        }
        this.isCircleTurn = false;
        this.isCircleTurnEnd = true;
        WeakReference<Handler> weakReference = this.mHandlerRef;
        if (weakReference == null || weakReference.get() == null) {
            return;
        }
        this.mHandlerRef.get().postDelayed(new Runnable() { // from class: com.yls.nova.tools.FlyController.4
            @Override // java.lang.Runnable
            public void run() {
                FlyController.this.isCircleTurnEnd = false;
                FlyController.this.controlByte1 = 128;
                FlyController.this.controlByte2 = 128;
            }
        }, 600L);
    }

    public boolean isNoHeadMode() {
        return this.isNoHeadMode;
    }

    public void setNoHeadMode(ImageView imageView) {
        boolean z = !this.isNoHeadMode;
        this.isNoHeadMode = z;
        if (z) {
            imageView.setImageResource(C0549R.drawable.drawable_head_mode_yellow);
        } else {
            imageView.setImageResource(C0549R.drawable.drawable_head_mode_icon);
        }
    }

    public boolean isGyroCorrection() {
        return this.isGyroCorrection;
    }

    public void setGyroCorrection(final ImageView imageView) {
        if (this.isGyroCorrection) {
            return;
        }
        this.isGyroCorrection = true;
        imageView.setImageResource(C0549R.mipmap.icon_gyro_ywllow);
        WeakReference<Handler> weakReference = this.mHandlerRef;
        if (weakReference == null || weakReference.get() == null) {
            return;
        }
        this.mHandlerRef.get().postDelayed(new Runnable() { // from class: com.yls.nova.tools.FlyController.5
            @Override // java.lang.Runnable
            public void run() {
                FlyController.this.isGyroCorrection = false;
                imageView.setImageResource(C0549R.drawable.drawable_gyr_icon);
            }
        }, 2000L);
    }
}
