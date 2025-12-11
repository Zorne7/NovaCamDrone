package com.yls.nova.socket;

import android.content.Intent;
import com.cooingdv.bl60xmjpeg.UAV;
import com.yls.nova.base.MainApplication;
import com.yls.nova.interfaces.OnSocketListener;
import com.yls.nova.socket.UdpComm;
import com.yls.nova.tools.IActions;
import com.yls.nova.utils.Dbug;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantLock;
import tv.danmaku.ijk.media.widget.IjkMpOptions;
import tv.danmaku.ijk.media.widget.IjkVideoView;

/* loaded from: classes.dex */
public class SocketClient {
    private static final int RECONNECT_INTERVAL = 500;
    private static String TAG = "SocketClient";
    private static final int VIDEO_VIEW_ASPECT = 3;
    private static final int VIDEO_VIEW_RENDER = 2;
    private static SocketClient mSocketClient;
    private OnSocketListener mOnSocketListener;
    private IjkVideoView mVideoView;
    private Timer netSendTimer;
    private UdpComm udpComm;
    private String mVideoPath = Config.PREVIEW_ADDRESS;
    private final ReentrantLock sendLock = new ReentrantLock();
    private int switchCameraReset = 0;
    private boolean isSwitchCamera = false;
    private boolean isSwitchingCamera = false;
    private final IjkVideoView.IVideoView.OnPreparedListener mPlayerPreparedListener = new IjkVideoView.IVideoView.OnPreparedListener() { // from class: com.yls.nova.socket.SocketClient.1
        @Override // tv.danmaku.ijk.media.widget.IjkVideoView.IVideoView.OnPreparedListener
        public void onPrepared(IjkVideoView ijkVideoView) throws IllegalStateException {
            SocketClient.this.mVideoView.setOutputOriginalVideo(true);
            if (SocketClient.this.isSwitchingCamera) {
                SocketClient.this.isSwitchingCamera = false;
            } else {
                SocketClient.this.startUdpTask();
            }
            if (SocketClient.this.mOnSocketListener != null) {
                SocketClient.this.mOnSocketListener.onConnected();
            }
        }
    };
    private final IjkVideoView.IVideoView.OnErrorListener mPlayerErrorListener = new IjkVideoView.IVideoView.OnErrorListener() { // from class: com.yls.nova.socket.SocketClient$$ExternalSyntheticLambda4
        @Override // tv.danmaku.ijk.media.widget.IjkVideoView.IVideoView.OnErrorListener
        public final boolean onError(IjkVideoView ijkVideoView, int i, int i2) {
            return this.f$0.m548lambda$new$0$comylsnovasocketSocketClient(ijkVideoView, i, i2);
        }
    };
    private final IjkVideoView.IVideoView.OnReceivedOriginalDataListener mReceivedOriginalDataListener = new IjkVideoView.IVideoView.OnReceivedOriginalDataListener() { // from class: com.yls.nova.socket.SocketClient.2
        @Override // tv.danmaku.ijk.media.widget.IjkVideoView.IVideoView.OnReceivedOriginalDataListener
        public void onReceivedOriginalData(IjkVideoView ijkVideoView, byte[] bArr, int i, int i2, int i3, int i4) {
            if (SocketClient.this.mOnSocketListener != null) {
                SocketClient.this.mOnSocketListener.onVideo(bArr);
            }
        }
    };
    private final IjkVideoView.IVideoView.OnCompletionListener mPlayerCompletionListener = new IjkVideoView.IVideoView.OnCompletionListener() { // from class: com.yls.nova.socket.SocketClient.3
        @Override // tv.danmaku.ijk.media.widget.IjkVideoView.IVideoView.OnCompletionListener
        public void onCompletion(IjkVideoView ijkVideoView) throws IllegalStateException {
            SocketClient.this.mVideoView.stopPlayback();
            SocketClient.this.mVideoView.release(true);
            SocketClient.this.mVideoView.stopBackgroundPlay();
            SocketClient.this.cancelUdpTask();
        }
    };

    public static SocketClient getInstance() {
        if (mSocketClient == null) {
            synchronized (SocketClient.class) {
                if (mSocketClient == null) {
                    mSocketClient = new SocketClient();
                }
            }
        }
        return mSocketClient;
    }

    public void setOnSocketListener(OnSocketListener onSocketListener) {
        this.mOnSocketListener = onSocketListener;
    }

    public boolean initVideoView(IjkVideoView ijkVideoView) {
        if (ijkVideoView == null) {
            return false;
        }
        this.mVideoView = ijkVideoView;
        ijkVideoView.setRender(2);
        this.mVideoView.setAspectRatio(3);
        this.mVideoView.setOnPreparedListener(this.mPlayerPreparedListener);
        this.mVideoView.setOnErrorListener(this.mPlayerErrorListener);
        this.mVideoView.setOnReceivedOriginalDataListener(this.mReceivedOriginalDataListener);
        this.mVideoView.setOnCompletionListener(this.mPlayerCompletionListener);
        applyOptionsToVideoView(this.mVideoView);
        this.mVideoView.setVideoPath(Config.PREVIEW_ADDRESS);
        return true;
    }

    private void applyOptionsToVideoView(IjkVideoView ijkVideoView) {
        IjkMpOptions ijkMpOptionsDefaultOptions = IjkMpOptions.defaultOptions();
        ijkMpOptionsDefaultOptions.setPlayerOption("mediacodec", 0L);
        ijkMpOptionsDefaultOptions.setPlayerOption("readtimeout", 5000000L);
        ijkMpOptionsDefaultOptions.setPlayerOption("preferred-image-type", 0L);
        ijkMpOptionsDefaultOptions.setPlayerOption("image-quality-min", 2L);
        ijkMpOptionsDefaultOptions.setPlayerOption("image-quality-max", 20L);
        ijkMpOptionsDefaultOptions.setPlayerOption("preferred-video-type", 2L);
        ijkMpOptionsDefaultOptions.setPlayerOption("video-need-transcoding", 1L);
        ijkMpOptionsDefaultOptions.setPlayerOption("mjpeg-pix-fmt", 1L);
        ijkMpOptionsDefaultOptions.setPlayerOption("video-quality-min", 2L);
        ijkMpOptionsDefaultOptions.setPlayerOption("video-quality-max", 20L);
        ijkMpOptionsDefaultOptions.setPlayerOption("x264-option-preset", 0L);
        ijkMpOptionsDefaultOptions.setPlayerOption("x264-option-tune", 5L);
        ijkMpOptionsDefaultOptions.setPlayerOption("x264-option-profile", 1L);
        ijkMpOptionsDefaultOptions.setPlayerOption("x264-params", "crf=23");
        ijkMpOptionsDefaultOptions.setPlayerOption("auto-drop-record-frame", 3L);
        ijkMpOptionsDefaultOptions.setCodecOption("err_detect", "explode");
        ijkVideoView.setOptions(ijkMpOptionsDefaultOptions);
    }

    /* renamed from: lambda$new$0$com-yls-nova-socket-SocketClient, reason: not valid java name */
    /* synthetic */ boolean m548lambda$new$0$comylsnovasocketSocketClient(IjkVideoView ijkVideoView, int i, int i2) {
        stopAndRestartPlayback();
        cancelUdpTask();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startUdpTask() {
        UdpComm udpComm = UdpComm.getInstance("192.168.1.1", 7099);
        this.udpComm = udpComm;
        udpComm.setCallback(new UdpComm.UdpCommCallback() { // from class: com.yls.nova.socket.SocketClient$$ExternalSyntheticLambda1
            @Override // com.yls.nova.socket.UdpComm.UdpCommCallback
            public final void onReceiveData(byte[] bArr) {
                this.f$0.m549lambda$startUdpTask$1$comylsnovasocketSocketClient(bArr);
            }
        });
        this.udpComm.initSendRecvThread();
        Timer timer = new Timer();
        this.netSendTimer = timer;
        timer.schedule(new HeartBeatTask(), 0L, 1000L);
    }

    /* renamed from: lambda$startUdpTask$1$com-yls-nova-socket-SocketClient, reason: not valid java name */
    /* synthetic */ void m549lambda$startUdpTask$1$comylsnovasocketSocketClient(byte[] bArr) {
        OnSocketListener onSocketListener = this.mOnSocketListener;
        if (onSocketListener != null) {
            onSocketListener.onReceiver(bArr);
        }
        if (bArr.length >= 1) {
            byte b = bArr[0];
            if (UAV.getInstance().getResolutionNumber() == 0) {
                UAV.getInstance().setResolutionNumber(b);
                MainApplication.getInstance().sendBroadcast(new Intent(IActions.ACTION_SET_FAKE_RESOLUTION));
            }
        }
        if (bArr.length >= 2) {
            this.switchCameraReset = bArr[1];
        }
        Dbug.m415d("Udp Recv", "STR(" + new String(bArr) + "), HEX(" + Arrays.toString(bArr) + ")");
    }

    private class HeartBeatTask extends TimerTask {
        private HeartBeatTask() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() throws InterruptedException {
            SocketClient.this.debugSend(new byte[]{1, 1});
        }
    }

    public void debugSend(byte[] bArr) throws InterruptedException {
        this.sendLock.lock();
        UdpComm udpComm = this.udpComm;
        if (udpComm != null) {
            udpComm.send(bArr);
        }
        this.sendLock.unlock();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelUdpTask() {
        if (this.isSwitchingCamera) {
            return;
        }
        this.sendLock.lock();
        Timer timer = this.netSendTimer;
        if (timer != null) {
            timer.cancel();
            this.netSendTimer = null;
        }
        UdpComm udpComm = this.udpComm;
        if (udpComm != null) {
            udpComm.setCallback(null);
            this.udpComm.closeClient();
            this.udpComm = null;
        }
        this.sendLock.unlock();
    }

    public boolean isActive() {
        IjkVideoView ijkVideoView = this.mVideoView;
        return ijkVideoView != null && ijkVideoView.isPlaying();
    }

    public void switchCamera() throws InterruptedException {
        byte[] bArr;
        if (this.isSwitchingCamera) {
            return;
        }
        if (this.switchCameraReset == 2) {
            this.mVideoView.post(new Runnable() { // from class: com.yls.nova.socket.SocketClient.4
                @Override // java.lang.Runnable
                public void run() throws IllegalStateException {
                    SocketClient.this.mVideoView.stopPlayback();
                    SocketClient.this.mVideoView.release(true);
                    SocketClient.this.mVideoView.stopBackgroundPlay();
                }
            });
        }
        boolean z = !this.isSwitchCamera;
        this.isSwitchCamera = z;
        if (z) {
            bArr = new byte[]{6, 2};
        } else {
            bArr = new byte[]{6, 1};
        }
        debugSend(bArr);
        int i = this.switchCameraReset;
        if (i == 1) {
            this.isSwitchingCamera = true;
            this.mVideoView.post(new Runnable() { // from class: com.yls.nova.socket.SocketClient$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() throws IllegalStateException {
                    this.f$0.m552lambda$switchCamera$2$comylsnovasocketSocketClient();
                }
            });
        } else if (i == 2) {
            this.mVideoView.postDelayed(new Runnable() { // from class: com.yls.nova.socket.SocketClient.5
                @Override // java.lang.Runnable
                public void run() throws IllegalStateException {
                    SocketClient.this.mVideoView.setRender(2);
                    SocketClient.this.mVideoView.setAspectRatio(3);
                    SocketClient.this.mVideoView.setVideoPath(SocketClient.this.mVideoPath);
                    SocketClient.this.mVideoView.start();
                }
            }, 600L);
            this.isSwitchingCamera = true;
        }
    }

    /* renamed from: lambda$switchCamera$2$com-yls-nova-socket-SocketClient, reason: not valid java name */
    /* synthetic */ void m552lambda$switchCamera$2$comylsnovasocketSocketClient() throws IllegalStateException {
        this.mVideoView.setRender(2);
        this.mVideoView.setAspectRatio(3);
        this.mVideoView.setVideoPath(this.mVideoPath);
        this.mVideoView.start();
    }

    public void start() throws IllegalStateException {
        IjkVideoView ijkVideoView = this.mVideoView;
        if (ijkVideoView != null) {
            ijkVideoView.setRender(2);
            this.mVideoView.setAspectRatio(3);
            this.mVideoView.setVideoPath(this.mVideoPath);
            this.mVideoView.start();
        }
    }

    public void stop() throws IllegalStateException {
        IjkVideoView ijkVideoView = this.mVideoView;
        if (ijkVideoView != null) {
            if (!ijkVideoView.isBackgroundPlayEnabled()) {
                this.mVideoView.stopPlayback();
                this.mVideoView.release(true);
                this.mVideoView.stopBackgroundPlay();
                return;
            }
            this.mVideoView.enterBackground();
        }
    }

    private void stopAndRestartPlayback() {
        this.mVideoView.post(new Runnable() { // from class: com.yls.nova.socket.SocketClient$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() throws IllegalStateException {
                this.f$0.m550lambda$stopAndRestartPlayback$3$comylsnovasocketSocketClient();
            }
        });
        this.mVideoView.postDelayed(new Runnable() { // from class: com.yls.nova.socket.SocketClient$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() throws IllegalStateException {
                this.f$0.m551lambda$stopAndRestartPlayback$4$comylsnovasocketSocketClient();
            }
        }, 500L);
    }

    /* renamed from: lambda$stopAndRestartPlayback$3$com-yls-nova-socket-SocketClient, reason: not valid java name */
    /* synthetic */ void m550lambda$stopAndRestartPlayback$3$comylsnovasocketSocketClient() throws IllegalStateException {
        this.mVideoView.stopPlayback();
        this.mVideoView.release(true);
        this.mVideoView.stopBackgroundPlay();
    }

    /* renamed from: lambda$stopAndRestartPlayback$4$com-yls-nova-socket-SocketClient, reason: not valid java name */
    /* synthetic */ void m551lambda$stopAndRestartPlayback$4$comylsnovasocketSocketClient() throws IllegalStateException {
        this.mVideoView.setRender(2);
        this.mVideoView.setAspectRatio(3);
        this.mVideoView.setVideoPath(this.mVideoPath);
        this.mVideoView.start();
    }
}
