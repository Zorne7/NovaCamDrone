package tv.danmaku.ijk.media.player;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.misc.IMediaDataSource;

/* loaded from: classes.dex */
public abstract class AbstractMediaPlayer implements IMediaPlayer {
    private IMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener;
    private IMediaPlayer.OnCompletionListener mOnCompletionListener;
    private IMediaPlayer.OnDeviceConnectedListener mOnDeviceConnectedListener;
    private IMediaPlayer.OnErrorListener mOnErrorListener;
    private IMediaPlayer.OnInfoListener mOnInfoListener;
    private IMediaPlayer.OnInsertVideoListener mOnInsertVideoListener;
    private IMediaPlayer.OnPreparedListener mOnPreparedListener;
    private IMediaPlayer.OnReceivedFrameListener mOnReceivedFrameListener;
    private IMediaPlayer.OnReceivedOriginalDataListener mOnReceivedOriginalDataListener;
    private IMediaPlayer.OnReceivedRtcpSrDataListener mOnReceivedRtcpSrDataListener;
    private IMediaPlayer.OnRecordVideoListener mOnRecordVideoListener;
    private IMediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener;
    private IMediaPlayer.OnTimedTextListener mOnTimedTextListener;
    private IMediaPlayer.OnTookPictureListener mOnTookPictureListener;
    private IMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener;

    @Override // tv.danmaku.ijk.media.player.IMediaPlayer
    public final void setOnPreparedListener(IMediaPlayer.OnPreparedListener onPreparedListener) {
        this.mOnPreparedListener = onPreparedListener;
    }

    @Override // tv.danmaku.ijk.media.player.IMediaPlayer
    public final void setOnCompletionListener(IMediaPlayer.OnCompletionListener onCompletionListener) {
        this.mOnCompletionListener = onCompletionListener;
    }

    @Override // tv.danmaku.ijk.media.player.IMediaPlayer
    public final void setOnBufferingUpdateListener(IMediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener) {
        this.mOnBufferingUpdateListener = onBufferingUpdateListener;
    }

    @Override // tv.danmaku.ijk.media.player.IMediaPlayer
    public final void setOnSeekCompleteListener(IMediaPlayer.OnSeekCompleteListener onSeekCompleteListener) {
        this.mOnSeekCompleteListener = onSeekCompleteListener;
    }

    @Override // tv.danmaku.ijk.media.player.IMediaPlayer
    public final void setOnVideoSizeChangedListener(IMediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedListener) {
        this.mOnVideoSizeChangedListener = onVideoSizeChangedListener;
    }

    @Override // tv.danmaku.ijk.media.player.IMediaPlayer
    public final void setOnErrorListener(IMediaPlayer.OnErrorListener onErrorListener) {
        this.mOnErrorListener = onErrorListener;
    }

    @Override // tv.danmaku.ijk.media.player.IMediaPlayer
    public final void setOnInfoListener(IMediaPlayer.OnInfoListener onInfoListener) {
        this.mOnInfoListener = onInfoListener;
    }

    @Override // tv.danmaku.ijk.media.player.IMediaPlayer
    public final void setOnTimedTextListener(IMediaPlayer.OnTimedTextListener onTimedTextListener) {
        this.mOnTimedTextListener = onTimedTextListener;
    }

    @Override // tv.danmaku.ijk.media.player.IMediaPlayer
    public final void setOnReceivedRtcpSrDataListener(IMediaPlayer.OnReceivedRtcpSrDataListener onReceivedRtcpSrDataListener) {
        this.mOnReceivedRtcpSrDataListener = onReceivedRtcpSrDataListener;
    }

    @Override // tv.danmaku.ijk.media.player.IMediaPlayer
    public final void setOnTookPictureListener(IMediaPlayer.OnTookPictureListener onTookPictureListener) {
        this.mOnTookPictureListener = onTookPictureListener;
    }

    @Override // tv.danmaku.ijk.media.player.IMediaPlayer
    public final void setOnRecordVideoListener(IMediaPlayer.OnRecordVideoListener onRecordVideoListener) {
        this.mOnRecordVideoListener = onRecordVideoListener;
    }

    @Override // tv.danmaku.ijk.media.player.IMediaPlayer
    public final void setOnInsertVideoListener(IMediaPlayer.OnInsertVideoListener onInsertVideoListener) {
        this.mOnInsertVideoListener = onInsertVideoListener;
    }

    @Override // tv.danmaku.ijk.media.player.IMediaPlayer
    public final void setOnReceivedFrameListener(IMediaPlayer.OnReceivedFrameListener onReceivedFrameListener) {
        this.mOnReceivedFrameListener = onReceivedFrameListener;
    }

    @Override // tv.danmaku.ijk.media.player.IMediaPlayer
    public final void setOnReceivedOriginalDataListener(IMediaPlayer.OnReceivedOriginalDataListener onReceivedOriginalDataListener) {
        this.mOnReceivedOriginalDataListener = onReceivedOriginalDataListener;
    }

    @Override // tv.danmaku.ijk.media.player.IMediaPlayer
    public final void setOnDeviceConnectedListener(IMediaPlayer.OnDeviceConnectedListener onDeviceConnectedListener) {
        this.mOnDeviceConnectedListener = onDeviceConnectedListener;
    }

    public void resetListeners() {
        this.mOnPreparedListener = null;
        this.mOnBufferingUpdateListener = null;
        this.mOnCompletionListener = null;
        this.mOnSeekCompleteListener = null;
        this.mOnVideoSizeChangedListener = null;
        this.mOnErrorListener = null;
        this.mOnInfoListener = null;
        this.mOnTimedTextListener = null;
        this.mOnReceivedRtcpSrDataListener = null;
        this.mOnTookPictureListener = null;
        this.mOnRecordVideoListener = null;
        this.mOnInsertVideoListener = null;
        this.mOnReceivedFrameListener = null;
        this.mOnReceivedOriginalDataListener = null;
        this.mOnDeviceConnectedListener = null;
    }

    protected final void notifyOnPrepared() {
        IMediaPlayer.OnPreparedListener onPreparedListener = this.mOnPreparedListener;
        if (onPreparedListener != null) {
            onPreparedListener.onPrepared(this);
        }
    }

    protected final void notifyOnCompletion() {
        IMediaPlayer.OnCompletionListener onCompletionListener = this.mOnCompletionListener;
        if (onCompletionListener != null) {
            onCompletionListener.onCompletion(this);
        }
    }

    protected final void notifyOnBufferingUpdate(int i) {
        IMediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener = this.mOnBufferingUpdateListener;
        if (onBufferingUpdateListener != null) {
            onBufferingUpdateListener.onBufferingUpdate(this, i);
        }
    }

    protected final void notifyOnSeekComplete() {
        IMediaPlayer.OnSeekCompleteListener onSeekCompleteListener = this.mOnSeekCompleteListener;
        if (onSeekCompleteListener != null) {
            onSeekCompleteListener.onSeekComplete(this);
        }
    }

    protected final void notifyOnVideoSizeChanged(int i, int i2, int i3, int i4) {
        IMediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedListener = this.mOnVideoSizeChangedListener;
        if (onVideoSizeChangedListener != null) {
            onVideoSizeChangedListener.onVideoSizeChanged(this, i, i2, i3, i4);
        }
    }

    protected final boolean notifyOnError(int i, int i2) {
        IMediaPlayer.OnErrorListener onErrorListener = this.mOnErrorListener;
        return onErrorListener != null && onErrorListener.onError(this, i, i2);
    }

    protected final boolean notifyOnInfo(int i, int i2) {
        IMediaPlayer.OnInfoListener onInfoListener = this.mOnInfoListener;
        return onInfoListener != null && onInfoListener.onInfo(this, i, i2);
    }

    protected final void notifyOnTimedText(IjkTimedText ijkTimedText) {
        IMediaPlayer.OnTimedTextListener onTimedTextListener = this.mOnTimedTextListener;
        if (onTimedTextListener != null) {
            onTimedTextListener.onTimedText(this, ijkTimedText);
        }
    }

    protected final void notifyOnReceivedRtcpSrData(byte[] bArr) {
        IMediaPlayer.OnReceivedRtcpSrDataListener onReceivedRtcpSrDataListener = this.mOnReceivedRtcpSrDataListener;
        if (onReceivedRtcpSrDataListener != null) {
            onReceivedRtcpSrDataListener.onReceivedRtcpSrData(this, bArr);
        }
    }

    protected final void notifyOnTookPicture(int i, String str) {
        IMediaPlayer.OnTookPictureListener onTookPictureListener = this.mOnTookPictureListener;
        if (onTookPictureListener != null) {
            onTookPictureListener.onTookPicture(this, i, str);
        }
    }

    protected final void notifyOnRecordVideo(int i, String str) {
        IMediaPlayer.OnRecordVideoListener onRecordVideoListener = this.mOnRecordVideoListener;
        if (onRecordVideoListener != null) {
            onRecordVideoListener.onRecordVideo(this, i, str);
        }
    }

    protected final void notifyOnInsertVideo(int i) {
        IMediaPlayer.OnInsertVideoListener onInsertVideoListener = this.mOnInsertVideoListener;
        if (onInsertVideoListener != null) {
            onInsertVideoListener.onInsertVideo(this, i);
        }
    }

    protected final void notifyOnReceivedFrame(byte[] bArr, int i, int i2, int i3) {
        IMediaPlayer.OnReceivedFrameListener onReceivedFrameListener = this.mOnReceivedFrameListener;
        if (onReceivedFrameListener != null) {
            onReceivedFrameListener.onReceivedFrame(this, bArr, i, i2, i3);
        }
    }

    protected final void notifyOnReceivedOriginalData(byte[] bArr, int i, int i2, int i3, int i4) {
        IMediaPlayer.OnReceivedOriginalDataListener onReceivedOriginalDataListener = this.mOnReceivedOriginalDataListener;
        if (onReceivedOriginalDataListener != null) {
            onReceivedOriginalDataListener.onReceivedOriginalData(this, bArr, i, i2, i3, i4);
        }
    }

    protected final void notifyOnDeviceConnected() {
        IMediaPlayer.OnDeviceConnectedListener onDeviceConnectedListener = this.mOnDeviceConnectedListener;
        if (onDeviceConnectedListener != null) {
            onDeviceConnectedListener.onDeviceConnected(this);
        }
    }

    @Override // tv.danmaku.ijk.media.player.IMediaPlayer
    public void setDataSource(IMediaDataSource iMediaDataSource) {
        throw new UnsupportedOperationException();
    }
}
