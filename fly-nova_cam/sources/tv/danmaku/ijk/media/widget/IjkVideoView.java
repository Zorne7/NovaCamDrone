package tv.danmaku.ijk.media.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.TableLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import kotlin.UByte;
import tv.danmaku.ijk.media.C0939R;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.IjkTimedText;
import tv.danmaku.ijk.media.player.misc.IMediaFormat;
import tv.danmaku.ijk.media.player.misc.ITrackInfo;
import tv.danmaku.ijk.media.player.misc.IjkMediaFormat;
import tv.danmaku.ijk.media.services.MediaPlayerService;
import tv.danmaku.ijk.media.widget.IRenderView;

/* loaded from: classes.dex */
public class IjkVideoView extends FrameLayout implements MediaController.MediaPlayerControl {
    private static final byte DATA_SIGNATURE_BYTE_1 = 105;
    private static final byte DATA_SIGNATURE_BYTE_2 = 30;
    private static final byte DATA_SIGNATURE_BYTE_3 = 90;
    private static final byte DATA_SIGNATURE_BYTE_4 = 15;
    private static final int IJK_LOG_LEVEL = 4;
    public static final int RENDER_NONE = 0;
    public static final int RENDER_SURFACE_VIEW = 1;
    public static final int RENDER_TEXTURE_VIEW = 2;
    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PAUSED = 4;
    private static final int STATE_PLAYBACK_COMPLETED = 5;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_PREPARED = 2;
    private static final int STATE_PREPARING = 1;
    private static final int[] s_allAspectRatio = {0, 1, 2, 4, 5};
    private String TAG;
    private byte[] _data;
    private Context mAppContext;
    private IMediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener;
    private boolean mCanPause;
    private boolean mCanSeekBack;
    private boolean mCanSeekForward;
    private IMediaPlayer.OnCompletionListener mCompletionListener;
    private int mCurrentAspectRatio;
    private int mCurrentAspectRatioIndex;
    private int mCurrentBufferPercentage;
    private int mCurrentRender;
    private int mCurrentState;
    private IMediaPlayer.OnDeviceConnectedListener mDeviceConnectedListener;
    private boolean mEnableBackgroundPlay;
    private IMediaPlayer.OnErrorListener mErrorListener;
    private Map<String, String> mHeaders;
    private InfoHudViewHolder mHudViewHolder;
    private IMediaPlayer.OnInfoListener mInfoListener;
    private IMediaPlayer.OnInsertVideoListener mInsertVideoListener;
    private IMediaController mMediaController;
    private IMediaPlayer mMediaPlayer;
    private IVideoView.OnCompletionListener mOnCompletionListener;
    private IVideoView.OnDeviceConnectedListener mOnDeviceConnectedListener;
    private IVideoView.OnErrorListener mOnErrorListener;
    private IVideoView.OnInfoListener mOnInfoListener;
    private IVideoView.OnInsertVideoListener mOnInsertVideoListener;
    private IVideoView.OnPreparedListener mOnPreparedListener;
    private IVideoView.OnReceivedDataListener mOnReceivedDataListener;
    private IVideoView.OnReceivedFrameListener mOnReceivedFrameListener;
    private IVideoView.OnReceivedOriginalDataListener mOnReceivedOriginalDataListener;
    private IVideoView.OnReceivedRtcpSrDataListener mOnReceivedRtcpSrDataListener;
    private IVideoView.OnRecordVideoListener mOnRecordVideoListener;
    private IMediaPlayer.OnTimedTextListener mOnTimedTextListener;
    private IVideoView.OnTookPictureListener mOnTookPictureListener;
    private IjkMpOptions mOptions;
    private long mPrepareEndTime;
    private long mPrepareStartTime;
    IMediaPlayer.OnPreparedListener mPreparedListener;
    private IMediaPlayer.OnReceivedFrameListener mReceivedFrameListener;
    private IMediaPlayer.OnReceivedOriginalDataListener mReceivedOriginalDataListener;
    private IMediaPlayer.OnReceivedRtcpSrDataListener mReceivedRtcpSrDataListener;
    private IMediaPlayer.OnRecordVideoListener mRecordVideoListener;
    private IRenderView mRenderView;
    IRenderView.IRenderCallback mSHCallback;
    private IMediaPlayer.OnSeekCompleteListener mSeekCompleteListener;
    private long mSeekEndTime;
    private long mSeekStartTime;
    private int mSeekWhenPrepared;
    IMediaPlayer.OnVideoSizeChangedListener mSizeChangedListener;
    private int mSurfaceHeight;
    private IRenderView.ISurfaceHolder mSurfaceHolder;
    private int mSurfaceWidth;
    private int mTargetState;
    private IMediaPlayer.OnTookPictureListener mTookPictureListener;
    private Uri mUri;
    private int mVideoHeight;
    private int mVideoRotationDegree;
    private int mVideoSarDen;
    private int mVideoSarNum;
    private int mVideoWidth;
    private boolean rotation180;
    private TextView subtitleDisplay;
    private boolean vrMode;
    private boolean vrStretched;

    public interface IVideoView {

        public interface OnCompletionListener {
            void onCompletion(IjkVideoView ijkVideoView);
        }

        public interface OnDeviceConnectedListener {
            void onDeviceConnected(IjkVideoView ijkVideoView);
        }

        public interface OnErrorListener {
            boolean onError(IjkVideoView ijkVideoView, int i, int i2);
        }

        public interface OnInfoListener {
            boolean onInfo(IjkVideoView ijkVideoView, int i, int i2);
        }

        public interface OnInsertVideoListener {
            void onInsertVideo(IjkVideoView ijkVideoView, int i);
        }

        public interface OnPreparedListener {
            void onPrepared(IjkVideoView ijkVideoView);
        }

        public interface OnReceivedDataListener {
            void onReceivedData(IjkVideoView ijkVideoView, byte[] bArr);
        }

        public interface OnReceivedFrameListener {
            void onReceivedFrame(IjkVideoView ijkVideoView, byte[] bArr, int i, int i2, int i3);
        }

        public interface OnReceivedOriginalDataListener {
            void onReceivedOriginalData(IjkVideoView ijkVideoView, byte[] bArr, int i, int i2, int i3, int i4);
        }

        public interface OnReceivedRtcpSrDataListener {
            void onReceivedRtcpSrData(IjkVideoView ijkVideoView, byte[] bArr);
        }

        public interface OnRecordVideoListener {
            void onRecordVideo(IjkVideoView ijkVideoView, int i, String str);
        }

        public interface OnTookPictureListener {
            void onTookPicture(IjkVideoView ijkVideoView, int i, String str);
        }

        void setOnCompletionListener(OnCompletionListener onCompletionListener);

        void setOnDeviceConnectedListener(OnDeviceConnectedListener onDeviceConnectedListener);

        void setOnErrorListener(OnErrorListener onErrorListener);

        void setOnInfoListener(OnInfoListener onInfoListener);

        void setOnInsertVideoListener(OnInsertVideoListener onInsertVideoListener);

        void setOnPreparedListener(OnPreparedListener onPreparedListener);

        void setOnReceivedDataListener(OnReceivedDataListener onReceivedDataListener);

        void setOnReceivedFrameListener(OnReceivedFrameListener onReceivedFrameListener);

        void setOnReceivedOriginalDataListener(OnReceivedOriginalDataListener onReceivedOriginalDataListener);

        void setOnReceivedRtcpSrDataListener(OnReceivedRtcpSrDataListener onReceivedRtcpSrDataListener);

        void setOnRecordVideoListener(OnRecordVideoListener onRecordVideoListener);

        void setOnTookPictureListener(OnTookPictureListener onTookPictureListener);
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public int getAudioSessionId() {
        return 0;
    }

    public IjkVideoView(Context context) {
        super(context);
        this.TAG = "IjkVideoView";
        this.mCurrentState = 0;
        this.mTargetState = 0;
        this.mSurfaceHolder = null;
        this.mMediaPlayer = null;
        this.mCanPause = true;
        this.mCanSeekBack = true;
        this.mCanSeekForward = true;
        this.mPrepareStartTime = 0L;
        this.mPrepareEndTime = 0L;
        this.mSeekStartTime = 0L;
        this.mSeekEndTime = 0L;
        this.mSizeChangedListener = new IMediaPlayer.OnVideoSizeChangedListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.1
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnVideoSizeChangedListener
            public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i2, int i3, int i4) {
                IjkVideoView.this.mVideoWidth = iMediaPlayer.getVideoWidth();
                IjkVideoView.this.mVideoHeight = iMediaPlayer.getVideoHeight();
                IjkVideoView.this.mVideoSarNum = iMediaPlayer.getVideoSarNum();
                IjkVideoView.this.mVideoSarDen = iMediaPlayer.getVideoSarDen();
                if (IjkVideoView.this.mVideoWidth == 0 || IjkVideoView.this.mVideoHeight == 0) {
                    return;
                }
                if (IjkVideoView.this.mRenderView != null) {
                    IjkVideoView.this.mRenderView.setVideoSize(IjkVideoView.this.mVideoWidth, IjkVideoView.this.mVideoHeight);
                    IjkVideoView.this.mRenderView.setVideoSampleAspectRatio(IjkVideoView.this.mVideoSarNum, IjkVideoView.this.mVideoSarDen);
                }
                IjkVideoView.this.requestLayout();
            }
        };
        this.mPreparedListener = new IMediaPlayer.OnPreparedListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.2
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnPreparedListener
            public void onPrepared(IMediaPlayer iMediaPlayer) throws IllegalStateException {
                IjkVideoView.this.mPrepareEndTime = System.currentTimeMillis();
                if (IjkVideoView.this.mHudViewHolder != null) {
                    IjkVideoView.this.mHudViewHolder.updateLoadCost(IjkVideoView.this.mPrepareEndTime - IjkVideoView.this.mPrepareStartTime);
                }
                IjkVideoView.this.mCurrentState = 2;
                IjkVideoView.this.rotation180 = false;
                if (IjkVideoView.this.mOnPreparedListener != null) {
                    IjkVideoView.this.mOnPreparedListener.onPrepared(IjkVideoView.this);
                }
                if (IjkVideoView.this.mMediaController != null) {
                    IjkVideoView.this.mMediaController.setEnabled(true);
                }
                IjkVideoView.this.mVideoWidth = iMediaPlayer.getVideoWidth();
                IjkVideoView.this.mVideoHeight = iMediaPlayer.getVideoHeight();
                int i = IjkVideoView.this.mSeekWhenPrepared;
                if (i != 0) {
                    IjkVideoView.this.seekTo(i);
                }
                if (IjkVideoView.this.mVideoWidth == 0 || IjkVideoView.this.mVideoHeight == 0) {
                    if (IjkVideoView.this.mTargetState == 3) {
                        IjkVideoView.this.start();
                    }
                } else if (IjkVideoView.this.mRenderView != null) {
                    IjkVideoView.this.mRenderView.setVideoSize(IjkVideoView.this.mVideoWidth, IjkVideoView.this.mVideoHeight);
                    IjkVideoView.this.mRenderView.setVideoSampleAspectRatio(IjkVideoView.this.mVideoSarNum, IjkVideoView.this.mVideoSarDen);
                    if (!IjkVideoView.this.mRenderView.shouldWaitForResize() || (IjkVideoView.this.mSurfaceWidth == IjkVideoView.this.mVideoWidth && IjkVideoView.this.mSurfaceHeight == IjkVideoView.this.mVideoHeight)) {
                        if (IjkVideoView.this.mTargetState == 3) {
                            IjkVideoView.this.start();
                            if (IjkVideoView.this.mMediaController != null) {
                                IjkVideoView.this.mMediaController.show();
                            }
                        } else if (!IjkVideoView.this.isPlaying() && ((i != 0 || IjkVideoView.this.getCurrentPosition() > 0) && IjkVideoView.this.mMediaController != null)) {
                            IjkVideoView.this.mMediaController.show(0);
                        }
                    }
                }
                IjkVideoView ijkVideoView = IjkVideoView.this;
                ijkVideoView.setStretchVrMode(ijkVideoView.vrMode, IjkVideoView.this.vrStretched);
            }
        };
        this.mCompletionListener = new IMediaPlayer.OnCompletionListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.3
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnCompletionListener
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                IjkVideoView.this.mCurrentState = 5;
                IjkVideoView.this.mTargetState = 5;
                if (IjkVideoView.this.mMediaController != null) {
                    IjkVideoView.this.mMediaController.hide();
                }
                if (IjkVideoView.this.mOnCompletionListener != null) {
                    IjkVideoView.this.mOnCompletionListener.onCompletion(IjkVideoView.this);
                }
            }
        };
        this.mInfoListener = new IMediaPlayer.OnInfoListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.4
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnInfoListener
            public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i2) {
                if (IjkVideoView.this.mOnInfoListener != null) {
                    IjkVideoView.this.mOnInfoListener.onInfo(IjkVideoView.this, i, i2);
                }
                if (i == 3) {
                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_VIDEO_RENDERING_START:");
                    return true;
                }
                if (i == 901) {
                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_UNSUPPORTED_SUBTITLE:");
                    return true;
                }
                if (i == 902) {
                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_SUBTITLE_TIMED_OUT:");
                    return true;
                }
                if (i == 10001) {
                    IjkVideoView.this.mVideoRotationDegree = i2;
                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_VIDEO_ROTATION_CHANGED: " + i2);
                    if (IjkVideoView.this.mRenderView == null) {
                        return true;
                    }
                    IjkVideoView.this.mRenderView.setVideoRotation(i2);
                    return true;
                }
                if (i != 10002) {
                    switch (i) {
                        case 700:
                            Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_VIDEO_TRACK_LAGGING:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_BUFFERING_START /* 701 */:
                            Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_BUFFERING_START:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_BUFFERING_END /* 702 */:
                            Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_BUFFERING_END:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH /* 703 */:
                            Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_NETWORK_BANDWIDTH: " + i2);
                            break;
                        default:
                            switch (i) {
                                case 800:
                                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_BAD_INTERLEAVING:");
                                    break;
                                case IMediaPlayer.MEDIA_INFO_NOT_SEEKABLE /* 801 */:
                                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_NOT_SEEKABLE:");
                                    break;
                                case IMediaPlayer.MEDIA_INFO_METADATA_UPDATE /* 802 */:
                                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_METADATA_UPDATE:");
                                    break;
                            }
                    }
                    return true;
                }
                Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_AUDIO_RENDERING_START:");
                return true;
            }
        };
        this.mErrorListener = new IMediaPlayer.OnErrorListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.5
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnErrorListener
            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i2) {
                int i3;
                Log.d(IjkVideoView.this.TAG, "Error: " + i + "," + i2);
                IjkVideoView.this.mCurrentState = -1;
                IjkVideoView.this.mTargetState = -1;
                if (IjkVideoView.this.mMediaController != null) {
                    IjkVideoView.this.mMediaController.hide();
                }
                if ((IjkVideoView.this.mOnErrorListener == null || !IjkVideoView.this.mOnErrorListener.onError(IjkVideoView.this, i, i2)) && IjkVideoView.this.getWindowToken() != null) {
                    IjkVideoView.this.mAppContext.getResources();
                    if (i == 200) {
                        i3 = C0939R.string.VideoView_error_text_invalid_progressive_playback;
                    } else {
                        i3 = C0939R.string.VideoView_error_text_unknown;
                    }
                    new AlertDialog.Builder(IjkVideoView.this.getContext()).setMessage(i3).setPositiveButton(C0939R.string.VideoView_error_button, new DialogInterface.OnClickListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.5.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i4) {
                            if (IjkVideoView.this.mOnCompletionListener != null) {
                                IjkVideoView.this.mOnCompletionListener.onCompletion(IjkVideoView.this);
                            }
                        }
                    }).setCancelable(false).show();
                }
                return true;
            }
        };
        this.mBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.6
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnBufferingUpdateListener
            public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
                IjkVideoView.this.mCurrentBufferPercentage = i;
            }
        };
        this.mSeekCompleteListener = new IMediaPlayer.OnSeekCompleteListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.7
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnSeekCompleteListener
            public void onSeekComplete(IMediaPlayer iMediaPlayer) {
                IjkVideoView.this.mSeekEndTime = System.currentTimeMillis();
                if (IjkVideoView.this.mHudViewHolder != null) {
                    IjkVideoView.this.mHudViewHolder.updateSeekCost(IjkVideoView.this.mSeekEndTime - IjkVideoView.this.mSeekStartTime);
                }
            }
        };
        this.mOnTimedTextListener = new IMediaPlayer.OnTimedTextListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.8
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnTimedTextListener
            public void onTimedText(IMediaPlayer iMediaPlayer, IjkTimedText ijkTimedText) {
                if (ijkTimedText != null) {
                    IjkVideoView.this.subtitleDisplay.setText(ijkTimedText.getText());
                }
            }
        };
        this.mReceivedRtcpSrDataListener = new IMediaPlayer.OnReceivedRtcpSrDataListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.9
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnReceivedRtcpSrDataListener
            public void onReceivedRtcpSrData(IMediaPlayer iMediaPlayer, byte[] bArr) {
                if (IjkVideoView.this.mOnReceivedRtcpSrDataListener != null) {
                    IjkVideoView.this.mOnReceivedRtcpSrDataListener.onReceivedRtcpSrData(IjkVideoView.this, bArr);
                }
                if (IjkVideoView.this.mOnReceivedDataListener != null && bArr.length > 4 && bArr[0] == 105 && bArr[1] == 30 && bArr[2] == 90 && bArr[3] == 15) {
                    int i = (bArr[4] + UByte.MIN_VALUE) & 255;
                    if (i > 0 && bArr.length - 5 >= i) {
                        IjkVideoView.this.mOnReceivedDataListener.onReceivedData(IjkVideoView.this, Arrays.copyOfRange(bArr, 5, i + 5));
                    } else if (i == 0) {
                        Log.d(IjkVideoView.this.TAG, "onReceivedData: empty message\n");
                    }
                }
            }
        };
        this.mTookPictureListener = new IMediaPlayer.OnTookPictureListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.10
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnTookPictureListener
            public void onTookPicture(IMediaPlayer iMediaPlayer, int i, String str) {
                if (IjkVideoView.this.mOnTookPictureListener != null) {
                    IjkVideoView.this.mOnTookPictureListener.onTookPicture(IjkVideoView.this, i, str);
                }
            }
        };
        this.mRecordVideoListener = new IMediaPlayer.OnRecordVideoListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.11
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnRecordVideoListener
            public void onRecordVideo(IMediaPlayer iMediaPlayer, int i, String str) {
                if (IjkVideoView.this.mOnRecordVideoListener != null) {
                    IjkVideoView.this.mOnRecordVideoListener.onRecordVideo(IjkVideoView.this, i, str);
                }
            }
        };
        this.mInsertVideoListener = new IMediaPlayer.OnInsertVideoListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.12
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnInsertVideoListener
            public void onInsertVideo(IMediaPlayer iMediaPlayer, int i) {
                if (IjkVideoView.this.mOnInsertVideoListener != null) {
                    IjkVideoView.this.mOnInsertVideoListener.onInsertVideo(IjkVideoView.this, i);
                }
            }
        };
        this.mReceivedFrameListener = new IMediaPlayer.OnReceivedFrameListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.13
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnReceivedFrameListener
            public void onReceivedFrame(IMediaPlayer iMediaPlayer, byte[] bArr, int i, int i2, int i3) {
                if (IjkVideoView.this.mOnReceivedFrameListener != null) {
                    IjkVideoView.this.mOnReceivedFrameListener.onReceivedFrame(IjkVideoView.this, bArr, i, i2, i3);
                }
            }
        };
        this.mReceivedOriginalDataListener = new IMediaPlayer.OnReceivedOriginalDataListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.14
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnReceivedOriginalDataListener
            public void onReceivedOriginalData(IMediaPlayer iMediaPlayer, byte[] bArr, int i, int i2, int i3, int i4) {
                if (IjkVideoView.this.mOnReceivedOriginalDataListener != null) {
                    IjkVideoView.this.mOnReceivedOriginalDataListener.onReceivedOriginalData(IjkVideoView.this, bArr, i, i2, i3, i4);
                }
            }
        };
        this.mDeviceConnectedListener = new IMediaPlayer.OnDeviceConnectedListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.15
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnDeviceConnectedListener
            public void onDeviceConnected(IMediaPlayer iMediaPlayer) {
                if (IjkVideoView.this.mOnDeviceConnectedListener != null) {
                    IjkVideoView.this.mOnDeviceConnectedListener.onDeviceConnected(IjkVideoView.this);
                }
            }
        };
        this.mSHCallback = new IRenderView.IRenderCallback() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.16
            @Override // tv.danmaku.ijk.media.widget.IRenderView.IRenderCallback
            public void onSurfaceChanged(IRenderView.ISurfaceHolder iSurfaceHolder, int i, int i2, int i3) throws IllegalStateException {
                if (iSurfaceHolder.getRenderView() != IjkVideoView.this.mRenderView) {
                    Log.e(IjkVideoView.this.TAG, "onSurfaceChanged: unmatched render callback\n");
                    return;
                }
                IjkVideoView.this.mSurfaceWidth = i2;
                IjkVideoView.this.mSurfaceHeight = i3;
                boolean z = true;
                boolean z2 = IjkVideoView.this.mTargetState == 3;
                if (IjkVideoView.this.mRenderView.shouldWaitForResize() && (IjkVideoView.this.mVideoWidth != i2 || IjkVideoView.this.mVideoHeight != i3)) {
                    z = false;
                }
                if (IjkVideoView.this.mMediaPlayer != null && z2 && z) {
                    if (IjkVideoView.this.mSeekWhenPrepared != 0) {
                        IjkVideoView ijkVideoView = IjkVideoView.this;
                        ijkVideoView.seekTo(ijkVideoView.mSeekWhenPrepared);
                    }
                    IjkVideoView.this.start();
                }
            }

            @Override // tv.danmaku.ijk.media.widget.IRenderView.IRenderCallback
            public void onSurfaceCreated(IRenderView.ISurfaceHolder iSurfaceHolder, int i, int i2) {
                if (iSurfaceHolder.getRenderView() != IjkVideoView.this.mRenderView) {
                    Log.e(IjkVideoView.this.TAG, "onSurfaceCreated: unmatched render callback\n");
                    return;
                }
                IjkVideoView.this.mSurfaceHolder = iSurfaceHolder;
                if (IjkVideoView.this.mMediaPlayer == null) {
                    IjkVideoView.this.openVideo();
                } else {
                    IjkVideoView ijkVideoView = IjkVideoView.this;
                    ijkVideoView.bindSurfaceHolder(ijkVideoView.mMediaPlayer, iSurfaceHolder);
                }
            }

            @Override // tv.danmaku.ijk.media.widget.IRenderView.IRenderCallback
            public void onSurfaceDestroyed(IRenderView.ISurfaceHolder iSurfaceHolder) {
                if (iSurfaceHolder.getRenderView() != IjkVideoView.this.mRenderView) {
                    Log.e(IjkVideoView.this.TAG, "onSurfaceDestroyed: unmatched render callback\n");
                } else {
                    IjkVideoView.this.mSurfaceHolder = null;
                    IjkVideoView.this.releaseWithoutStop();
                }
            }
        };
        this._data = null;
        this.mCurrentAspectRatioIndex = 0;
        this.mCurrentAspectRatio = s_allAspectRatio[0];
        this.mCurrentRender = 0;
        this.mEnableBackgroundPlay = false;
        initVideoView(context);
    }

    public IjkVideoView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.TAG = "IjkVideoView";
        this.mCurrentState = 0;
        this.mTargetState = 0;
        this.mSurfaceHolder = null;
        this.mMediaPlayer = null;
        this.mCanPause = true;
        this.mCanSeekBack = true;
        this.mCanSeekForward = true;
        this.mPrepareStartTime = 0L;
        this.mPrepareEndTime = 0L;
        this.mSeekStartTime = 0L;
        this.mSeekEndTime = 0L;
        this.mSizeChangedListener = new IMediaPlayer.OnVideoSizeChangedListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.1
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnVideoSizeChangedListener
            public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i2, int i3, int i4) {
                IjkVideoView.this.mVideoWidth = iMediaPlayer.getVideoWidth();
                IjkVideoView.this.mVideoHeight = iMediaPlayer.getVideoHeight();
                IjkVideoView.this.mVideoSarNum = iMediaPlayer.getVideoSarNum();
                IjkVideoView.this.mVideoSarDen = iMediaPlayer.getVideoSarDen();
                if (IjkVideoView.this.mVideoWidth == 0 || IjkVideoView.this.mVideoHeight == 0) {
                    return;
                }
                if (IjkVideoView.this.mRenderView != null) {
                    IjkVideoView.this.mRenderView.setVideoSize(IjkVideoView.this.mVideoWidth, IjkVideoView.this.mVideoHeight);
                    IjkVideoView.this.mRenderView.setVideoSampleAspectRatio(IjkVideoView.this.mVideoSarNum, IjkVideoView.this.mVideoSarDen);
                }
                IjkVideoView.this.requestLayout();
            }
        };
        this.mPreparedListener = new IMediaPlayer.OnPreparedListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.2
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnPreparedListener
            public void onPrepared(IMediaPlayer iMediaPlayer) throws IllegalStateException {
                IjkVideoView.this.mPrepareEndTime = System.currentTimeMillis();
                if (IjkVideoView.this.mHudViewHolder != null) {
                    IjkVideoView.this.mHudViewHolder.updateLoadCost(IjkVideoView.this.mPrepareEndTime - IjkVideoView.this.mPrepareStartTime);
                }
                IjkVideoView.this.mCurrentState = 2;
                IjkVideoView.this.rotation180 = false;
                if (IjkVideoView.this.mOnPreparedListener != null) {
                    IjkVideoView.this.mOnPreparedListener.onPrepared(IjkVideoView.this);
                }
                if (IjkVideoView.this.mMediaController != null) {
                    IjkVideoView.this.mMediaController.setEnabled(true);
                }
                IjkVideoView.this.mVideoWidth = iMediaPlayer.getVideoWidth();
                IjkVideoView.this.mVideoHeight = iMediaPlayer.getVideoHeight();
                int i = IjkVideoView.this.mSeekWhenPrepared;
                if (i != 0) {
                    IjkVideoView.this.seekTo(i);
                }
                if (IjkVideoView.this.mVideoWidth == 0 || IjkVideoView.this.mVideoHeight == 0) {
                    if (IjkVideoView.this.mTargetState == 3) {
                        IjkVideoView.this.start();
                    }
                } else if (IjkVideoView.this.mRenderView != null) {
                    IjkVideoView.this.mRenderView.setVideoSize(IjkVideoView.this.mVideoWidth, IjkVideoView.this.mVideoHeight);
                    IjkVideoView.this.mRenderView.setVideoSampleAspectRatio(IjkVideoView.this.mVideoSarNum, IjkVideoView.this.mVideoSarDen);
                    if (!IjkVideoView.this.mRenderView.shouldWaitForResize() || (IjkVideoView.this.mSurfaceWidth == IjkVideoView.this.mVideoWidth && IjkVideoView.this.mSurfaceHeight == IjkVideoView.this.mVideoHeight)) {
                        if (IjkVideoView.this.mTargetState == 3) {
                            IjkVideoView.this.start();
                            if (IjkVideoView.this.mMediaController != null) {
                                IjkVideoView.this.mMediaController.show();
                            }
                        } else if (!IjkVideoView.this.isPlaying() && ((i != 0 || IjkVideoView.this.getCurrentPosition() > 0) && IjkVideoView.this.mMediaController != null)) {
                            IjkVideoView.this.mMediaController.show(0);
                        }
                    }
                }
                IjkVideoView ijkVideoView = IjkVideoView.this;
                ijkVideoView.setStretchVrMode(ijkVideoView.vrMode, IjkVideoView.this.vrStretched);
            }
        };
        this.mCompletionListener = new IMediaPlayer.OnCompletionListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.3
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnCompletionListener
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                IjkVideoView.this.mCurrentState = 5;
                IjkVideoView.this.mTargetState = 5;
                if (IjkVideoView.this.mMediaController != null) {
                    IjkVideoView.this.mMediaController.hide();
                }
                if (IjkVideoView.this.mOnCompletionListener != null) {
                    IjkVideoView.this.mOnCompletionListener.onCompletion(IjkVideoView.this);
                }
            }
        };
        this.mInfoListener = new IMediaPlayer.OnInfoListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.4
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnInfoListener
            public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i2) {
                if (IjkVideoView.this.mOnInfoListener != null) {
                    IjkVideoView.this.mOnInfoListener.onInfo(IjkVideoView.this, i, i2);
                }
                if (i == 3) {
                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_VIDEO_RENDERING_START:");
                    return true;
                }
                if (i == 901) {
                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_UNSUPPORTED_SUBTITLE:");
                    return true;
                }
                if (i == 902) {
                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_SUBTITLE_TIMED_OUT:");
                    return true;
                }
                if (i == 10001) {
                    IjkVideoView.this.mVideoRotationDegree = i2;
                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_VIDEO_ROTATION_CHANGED: " + i2);
                    if (IjkVideoView.this.mRenderView == null) {
                        return true;
                    }
                    IjkVideoView.this.mRenderView.setVideoRotation(i2);
                    return true;
                }
                if (i != 10002) {
                    switch (i) {
                        case 700:
                            Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_VIDEO_TRACK_LAGGING:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_BUFFERING_START /* 701 */:
                            Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_BUFFERING_START:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_BUFFERING_END /* 702 */:
                            Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_BUFFERING_END:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH /* 703 */:
                            Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_NETWORK_BANDWIDTH: " + i2);
                            break;
                        default:
                            switch (i) {
                                case 800:
                                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_BAD_INTERLEAVING:");
                                    break;
                                case IMediaPlayer.MEDIA_INFO_NOT_SEEKABLE /* 801 */:
                                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_NOT_SEEKABLE:");
                                    break;
                                case IMediaPlayer.MEDIA_INFO_METADATA_UPDATE /* 802 */:
                                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_METADATA_UPDATE:");
                                    break;
                            }
                    }
                    return true;
                }
                Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_AUDIO_RENDERING_START:");
                return true;
            }
        };
        this.mErrorListener = new IMediaPlayer.OnErrorListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.5
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnErrorListener
            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i2) {
                int i3;
                Log.d(IjkVideoView.this.TAG, "Error: " + i + "," + i2);
                IjkVideoView.this.mCurrentState = -1;
                IjkVideoView.this.mTargetState = -1;
                if (IjkVideoView.this.mMediaController != null) {
                    IjkVideoView.this.mMediaController.hide();
                }
                if ((IjkVideoView.this.mOnErrorListener == null || !IjkVideoView.this.mOnErrorListener.onError(IjkVideoView.this, i, i2)) && IjkVideoView.this.getWindowToken() != null) {
                    IjkVideoView.this.mAppContext.getResources();
                    if (i == 200) {
                        i3 = C0939R.string.VideoView_error_text_invalid_progressive_playback;
                    } else {
                        i3 = C0939R.string.VideoView_error_text_unknown;
                    }
                    new AlertDialog.Builder(IjkVideoView.this.getContext()).setMessage(i3).setPositiveButton(C0939R.string.VideoView_error_button, new DialogInterface.OnClickListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.5.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i4) {
                            if (IjkVideoView.this.mOnCompletionListener != null) {
                                IjkVideoView.this.mOnCompletionListener.onCompletion(IjkVideoView.this);
                            }
                        }
                    }).setCancelable(false).show();
                }
                return true;
            }
        };
        this.mBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.6
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnBufferingUpdateListener
            public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
                IjkVideoView.this.mCurrentBufferPercentage = i;
            }
        };
        this.mSeekCompleteListener = new IMediaPlayer.OnSeekCompleteListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.7
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnSeekCompleteListener
            public void onSeekComplete(IMediaPlayer iMediaPlayer) {
                IjkVideoView.this.mSeekEndTime = System.currentTimeMillis();
                if (IjkVideoView.this.mHudViewHolder != null) {
                    IjkVideoView.this.mHudViewHolder.updateSeekCost(IjkVideoView.this.mSeekEndTime - IjkVideoView.this.mSeekStartTime);
                }
            }
        };
        this.mOnTimedTextListener = new IMediaPlayer.OnTimedTextListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.8
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnTimedTextListener
            public void onTimedText(IMediaPlayer iMediaPlayer, IjkTimedText ijkTimedText) {
                if (ijkTimedText != null) {
                    IjkVideoView.this.subtitleDisplay.setText(ijkTimedText.getText());
                }
            }
        };
        this.mReceivedRtcpSrDataListener = new IMediaPlayer.OnReceivedRtcpSrDataListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.9
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnReceivedRtcpSrDataListener
            public void onReceivedRtcpSrData(IMediaPlayer iMediaPlayer, byte[] bArr) {
                if (IjkVideoView.this.mOnReceivedRtcpSrDataListener != null) {
                    IjkVideoView.this.mOnReceivedRtcpSrDataListener.onReceivedRtcpSrData(IjkVideoView.this, bArr);
                }
                if (IjkVideoView.this.mOnReceivedDataListener != null && bArr.length > 4 && bArr[0] == 105 && bArr[1] == 30 && bArr[2] == 90 && bArr[3] == 15) {
                    int i = (bArr[4] + UByte.MIN_VALUE) & 255;
                    if (i > 0 && bArr.length - 5 >= i) {
                        IjkVideoView.this.mOnReceivedDataListener.onReceivedData(IjkVideoView.this, Arrays.copyOfRange(bArr, 5, i + 5));
                    } else if (i == 0) {
                        Log.d(IjkVideoView.this.TAG, "onReceivedData: empty message\n");
                    }
                }
            }
        };
        this.mTookPictureListener = new IMediaPlayer.OnTookPictureListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.10
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnTookPictureListener
            public void onTookPicture(IMediaPlayer iMediaPlayer, int i, String str) {
                if (IjkVideoView.this.mOnTookPictureListener != null) {
                    IjkVideoView.this.mOnTookPictureListener.onTookPicture(IjkVideoView.this, i, str);
                }
            }
        };
        this.mRecordVideoListener = new IMediaPlayer.OnRecordVideoListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.11
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnRecordVideoListener
            public void onRecordVideo(IMediaPlayer iMediaPlayer, int i, String str) {
                if (IjkVideoView.this.mOnRecordVideoListener != null) {
                    IjkVideoView.this.mOnRecordVideoListener.onRecordVideo(IjkVideoView.this, i, str);
                }
            }
        };
        this.mInsertVideoListener = new IMediaPlayer.OnInsertVideoListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.12
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnInsertVideoListener
            public void onInsertVideo(IMediaPlayer iMediaPlayer, int i) {
                if (IjkVideoView.this.mOnInsertVideoListener != null) {
                    IjkVideoView.this.mOnInsertVideoListener.onInsertVideo(IjkVideoView.this, i);
                }
            }
        };
        this.mReceivedFrameListener = new IMediaPlayer.OnReceivedFrameListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.13
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnReceivedFrameListener
            public void onReceivedFrame(IMediaPlayer iMediaPlayer, byte[] bArr, int i, int i2, int i3) {
                if (IjkVideoView.this.mOnReceivedFrameListener != null) {
                    IjkVideoView.this.mOnReceivedFrameListener.onReceivedFrame(IjkVideoView.this, bArr, i, i2, i3);
                }
            }
        };
        this.mReceivedOriginalDataListener = new IMediaPlayer.OnReceivedOriginalDataListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.14
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnReceivedOriginalDataListener
            public void onReceivedOriginalData(IMediaPlayer iMediaPlayer, byte[] bArr, int i, int i2, int i3, int i4) {
                if (IjkVideoView.this.mOnReceivedOriginalDataListener != null) {
                    IjkVideoView.this.mOnReceivedOriginalDataListener.onReceivedOriginalData(IjkVideoView.this, bArr, i, i2, i3, i4);
                }
            }
        };
        this.mDeviceConnectedListener = new IMediaPlayer.OnDeviceConnectedListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.15
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnDeviceConnectedListener
            public void onDeviceConnected(IMediaPlayer iMediaPlayer) {
                if (IjkVideoView.this.mOnDeviceConnectedListener != null) {
                    IjkVideoView.this.mOnDeviceConnectedListener.onDeviceConnected(IjkVideoView.this);
                }
            }
        };
        this.mSHCallback = new IRenderView.IRenderCallback() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.16
            @Override // tv.danmaku.ijk.media.widget.IRenderView.IRenderCallback
            public void onSurfaceChanged(IRenderView.ISurfaceHolder iSurfaceHolder, int i, int i2, int i3) throws IllegalStateException {
                if (iSurfaceHolder.getRenderView() != IjkVideoView.this.mRenderView) {
                    Log.e(IjkVideoView.this.TAG, "onSurfaceChanged: unmatched render callback\n");
                    return;
                }
                IjkVideoView.this.mSurfaceWidth = i2;
                IjkVideoView.this.mSurfaceHeight = i3;
                boolean z = true;
                boolean z2 = IjkVideoView.this.mTargetState == 3;
                if (IjkVideoView.this.mRenderView.shouldWaitForResize() && (IjkVideoView.this.mVideoWidth != i2 || IjkVideoView.this.mVideoHeight != i3)) {
                    z = false;
                }
                if (IjkVideoView.this.mMediaPlayer != null && z2 && z) {
                    if (IjkVideoView.this.mSeekWhenPrepared != 0) {
                        IjkVideoView ijkVideoView = IjkVideoView.this;
                        ijkVideoView.seekTo(ijkVideoView.mSeekWhenPrepared);
                    }
                    IjkVideoView.this.start();
                }
            }

            @Override // tv.danmaku.ijk.media.widget.IRenderView.IRenderCallback
            public void onSurfaceCreated(IRenderView.ISurfaceHolder iSurfaceHolder, int i, int i2) {
                if (iSurfaceHolder.getRenderView() != IjkVideoView.this.mRenderView) {
                    Log.e(IjkVideoView.this.TAG, "onSurfaceCreated: unmatched render callback\n");
                    return;
                }
                IjkVideoView.this.mSurfaceHolder = iSurfaceHolder;
                if (IjkVideoView.this.mMediaPlayer == null) {
                    IjkVideoView.this.openVideo();
                } else {
                    IjkVideoView ijkVideoView = IjkVideoView.this;
                    ijkVideoView.bindSurfaceHolder(ijkVideoView.mMediaPlayer, iSurfaceHolder);
                }
            }

            @Override // tv.danmaku.ijk.media.widget.IRenderView.IRenderCallback
            public void onSurfaceDestroyed(IRenderView.ISurfaceHolder iSurfaceHolder) {
                if (iSurfaceHolder.getRenderView() != IjkVideoView.this.mRenderView) {
                    Log.e(IjkVideoView.this.TAG, "onSurfaceDestroyed: unmatched render callback\n");
                } else {
                    IjkVideoView.this.mSurfaceHolder = null;
                    IjkVideoView.this.releaseWithoutStop();
                }
            }
        };
        this._data = null;
        this.mCurrentAspectRatioIndex = 0;
        this.mCurrentAspectRatio = s_allAspectRatio[0];
        this.mCurrentRender = 0;
        this.mEnableBackgroundPlay = false;
        initVideoView(context);
    }

    public IjkVideoView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.TAG = "IjkVideoView";
        this.mCurrentState = 0;
        this.mTargetState = 0;
        this.mSurfaceHolder = null;
        this.mMediaPlayer = null;
        this.mCanPause = true;
        this.mCanSeekBack = true;
        this.mCanSeekForward = true;
        this.mPrepareStartTime = 0L;
        this.mPrepareEndTime = 0L;
        this.mSeekStartTime = 0L;
        this.mSeekEndTime = 0L;
        this.mSizeChangedListener = new IMediaPlayer.OnVideoSizeChangedListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.1
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnVideoSizeChangedListener
            public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i2, int i22, int i3, int i4) {
                IjkVideoView.this.mVideoWidth = iMediaPlayer.getVideoWidth();
                IjkVideoView.this.mVideoHeight = iMediaPlayer.getVideoHeight();
                IjkVideoView.this.mVideoSarNum = iMediaPlayer.getVideoSarNum();
                IjkVideoView.this.mVideoSarDen = iMediaPlayer.getVideoSarDen();
                if (IjkVideoView.this.mVideoWidth == 0 || IjkVideoView.this.mVideoHeight == 0) {
                    return;
                }
                if (IjkVideoView.this.mRenderView != null) {
                    IjkVideoView.this.mRenderView.setVideoSize(IjkVideoView.this.mVideoWidth, IjkVideoView.this.mVideoHeight);
                    IjkVideoView.this.mRenderView.setVideoSampleAspectRatio(IjkVideoView.this.mVideoSarNum, IjkVideoView.this.mVideoSarDen);
                }
                IjkVideoView.this.requestLayout();
            }
        };
        this.mPreparedListener = new IMediaPlayer.OnPreparedListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.2
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnPreparedListener
            public void onPrepared(IMediaPlayer iMediaPlayer) throws IllegalStateException {
                IjkVideoView.this.mPrepareEndTime = System.currentTimeMillis();
                if (IjkVideoView.this.mHudViewHolder != null) {
                    IjkVideoView.this.mHudViewHolder.updateLoadCost(IjkVideoView.this.mPrepareEndTime - IjkVideoView.this.mPrepareStartTime);
                }
                IjkVideoView.this.mCurrentState = 2;
                IjkVideoView.this.rotation180 = false;
                if (IjkVideoView.this.mOnPreparedListener != null) {
                    IjkVideoView.this.mOnPreparedListener.onPrepared(IjkVideoView.this);
                }
                if (IjkVideoView.this.mMediaController != null) {
                    IjkVideoView.this.mMediaController.setEnabled(true);
                }
                IjkVideoView.this.mVideoWidth = iMediaPlayer.getVideoWidth();
                IjkVideoView.this.mVideoHeight = iMediaPlayer.getVideoHeight();
                int i2 = IjkVideoView.this.mSeekWhenPrepared;
                if (i2 != 0) {
                    IjkVideoView.this.seekTo(i2);
                }
                if (IjkVideoView.this.mVideoWidth == 0 || IjkVideoView.this.mVideoHeight == 0) {
                    if (IjkVideoView.this.mTargetState == 3) {
                        IjkVideoView.this.start();
                    }
                } else if (IjkVideoView.this.mRenderView != null) {
                    IjkVideoView.this.mRenderView.setVideoSize(IjkVideoView.this.mVideoWidth, IjkVideoView.this.mVideoHeight);
                    IjkVideoView.this.mRenderView.setVideoSampleAspectRatio(IjkVideoView.this.mVideoSarNum, IjkVideoView.this.mVideoSarDen);
                    if (!IjkVideoView.this.mRenderView.shouldWaitForResize() || (IjkVideoView.this.mSurfaceWidth == IjkVideoView.this.mVideoWidth && IjkVideoView.this.mSurfaceHeight == IjkVideoView.this.mVideoHeight)) {
                        if (IjkVideoView.this.mTargetState == 3) {
                            IjkVideoView.this.start();
                            if (IjkVideoView.this.mMediaController != null) {
                                IjkVideoView.this.mMediaController.show();
                            }
                        } else if (!IjkVideoView.this.isPlaying() && ((i2 != 0 || IjkVideoView.this.getCurrentPosition() > 0) && IjkVideoView.this.mMediaController != null)) {
                            IjkVideoView.this.mMediaController.show(0);
                        }
                    }
                }
                IjkVideoView ijkVideoView = IjkVideoView.this;
                ijkVideoView.setStretchVrMode(ijkVideoView.vrMode, IjkVideoView.this.vrStretched);
            }
        };
        this.mCompletionListener = new IMediaPlayer.OnCompletionListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.3
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnCompletionListener
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                IjkVideoView.this.mCurrentState = 5;
                IjkVideoView.this.mTargetState = 5;
                if (IjkVideoView.this.mMediaController != null) {
                    IjkVideoView.this.mMediaController.hide();
                }
                if (IjkVideoView.this.mOnCompletionListener != null) {
                    IjkVideoView.this.mOnCompletionListener.onCompletion(IjkVideoView.this);
                }
            }
        };
        this.mInfoListener = new IMediaPlayer.OnInfoListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.4
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnInfoListener
            public boolean onInfo(IMediaPlayer iMediaPlayer, int i2, int i22) {
                if (IjkVideoView.this.mOnInfoListener != null) {
                    IjkVideoView.this.mOnInfoListener.onInfo(IjkVideoView.this, i2, i22);
                }
                if (i2 == 3) {
                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_VIDEO_RENDERING_START:");
                    return true;
                }
                if (i2 == 901) {
                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_UNSUPPORTED_SUBTITLE:");
                    return true;
                }
                if (i2 == 902) {
                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_SUBTITLE_TIMED_OUT:");
                    return true;
                }
                if (i2 == 10001) {
                    IjkVideoView.this.mVideoRotationDegree = i22;
                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_VIDEO_ROTATION_CHANGED: " + i22);
                    if (IjkVideoView.this.mRenderView == null) {
                        return true;
                    }
                    IjkVideoView.this.mRenderView.setVideoRotation(i22);
                    return true;
                }
                if (i2 != 10002) {
                    switch (i2) {
                        case 700:
                            Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_VIDEO_TRACK_LAGGING:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_BUFFERING_START /* 701 */:
                            Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_BUFFERING_START:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_BUFFERING_END /* 702 */:
                            Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_BUFFERING_END:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH /* 703 */:
                            Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_NETWORK_BANDWIDTH: " + i22);
                            break;
                        default:
                            switch (i2) {
                                case 800:
                                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_BAD_INTERLEAVING:");
                                    break;
                                case IMediaPlayer.MEDIA_INFO_NOT_SEEKABLE /* 801 */:
                                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_NOT_SEEKABLE:");
                                    break;
                                case IMediaPlayer.MEDIA_INFO_METADATA_UPDATE /* 802 */:
                                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_METADATA_UPDATE:");
                                    break;
                            }
                    }
                    return true;
                }
                Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_AUDIO_RENDERING_START:");
                return true;
            }
        };
        this.mErrorListener = new IMediaPlayer.OnErrorListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.5
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnErrorListener
            public boolean onError(IMediaPlayer iMediaPlayer, int i2, int i22) {
                int i3;
                Log.d(IjkVideoView.this.TAG, "Error: " + i2 + "," + i22);
                IjkVideoView.this.mCurrentState = -1;
                IjkVideoView.this.mTargetState = -1;
                if (IjkVideoView.this.mMediaController != null) {
                    IjkVideoView.this.mMediaController.hide();
                }
                if ((IjkVideoView.this.mOnErrorListener == null || !IjkVideoView.this.mOnErrorListener.onError(IjkVideoView.this, i2, i22)) && IjkVideoView.this.getWindowToken() != null) {
                    IjkVideoView.this.mAppContext.getResources();
                    if (i2 == 200) {
                        i3 = C0939R.string.VideoView_error_text_invalid_progressive_playback;
                    } else {
                        i3 = C0939R.string.VideoView_error_text_unknown;
                    }
                    new AlertDialog.Builder(IjkVideoView.this.getContext()).setMessage(i3).setPositiveButton(C0939R.string.VideoView_error_button, new DialogInterface.OnClickListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.5.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i4) {
                            if (IjkVideoView.this.mOnCompletionListener != null) {
                                IjkVideoView.this.mOnCompletionListener.onCompletion(IjkVideoView.this);
                            }
                        }
                    }).setCancelable(false).show();
                }
                return true;
            }
        };
        this.mBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.6
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnBufferingUpdateListener
            public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i2) {
                IjkVideoView.this.mCurrentBufferPercentage = i2;
            }
        };
        this.mSeekCompleteListener = new IMediaPlayer.OnSeekCompleteListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.7
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnSeekCompleteListener
            public void onSeekComplete(IMediaPlayer iMediaPlayer) {
                IjkVideoView.this.mSeekEndTime = System.currentTimeMillis();
                if (IjkVideoView.this.mHudViewHolder != null) {
                    IjkVideoView.this.mHudViewHolder.updateSeekCost(IjkVideoView.this.mSeekEndTime - IjkVideoView.this.mSeekStartTime);
                }
            }
        };
        this.mOnTimedTextListener = new IMediaPlayer.OnTimedTextListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.8
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnTimedTextListener
            public void onTimedText(IMediaPlayer iMediaPlayer, IjkTimedText ijkTimedText) {
                if (ijkTimedText != null) {
                    IjkVideoView.this.subtitleDisplay.setText(ijkTimedText.getText());
                }
            }
        };
        this.mReceivedRtcpSrDataListener = new IMediaPlayer.OnReceivedRtcpSrDataListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.9
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnReceivedRtcpSrDataListener
            public void onReceivedRtcpSrData(IMediaPlayer iMediaPlayer, byte[] bArr) {
                if (IjkVideoView.this.mOnReceivedRtcpSrDataListener != null) {
                    IjkVideoView.this.mOnReceivedRtcpSrDataListener.onReceivedRtcpSrData(IjkVideoView.this, bArr);
                }
                if (IjkVideoView.this.mOnReceivedDataListener != null && bArr.length > 4 && bArr[0] == 105 && bArr[1] == 30 && bArr[2] == 90 && bArr[3] == 15) {
                    int i2 = (bArr[4] + UByte.MIN_VALUE) & 255;
                    if (i2 > 0 && bArr.length - 5 >= i2) {
                        IjkVideoView.this.mOnReceivedDataListener.onReceivedData(IjkVideoView.this, Arrays.copyOfRange(bArr, 5, i2 + 5));
                    } else if (i2 == 0) {
                        Log.d(IjkVideoView.this.TAG, "onReceivedData: empty message\n");
                    }
                }
            }
        };
        this.mTookPictureListener = new IMediaPlayer.OnTookPictureListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.10
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnTookPictureListener
            public void onTookPicture(IMediaPlayer iMediaPlayer, int i2, String str) {
                if (IjkVideoView.this.mOnTookPictureListener != null) {
                    IjkVideoView.this.mOnTookPictureListener.onTookPicture(IjkVideoView.this, i2, str);
                }
            }
        };
        this.mRecordVideoListener = new IMediaPlayer.OnRecordVideoListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.11
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnRecordVideoListener
            public void onRecordVideo(IMediaPlayer iMediaPlayer, int i2, String str) {
                if (IjkVideoView.this.mOnRecordVideoListener != null) {
                    IjkVideoView.this.mOnRecordVideoListener.onRecordVideo(IjkVideoView.this, i2, str);
                }
            }
        };
        this.mInsertVideoListener = new IMediaPlayer.OnInsertVideoListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.12
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnInsertVideoListener
            public void onInsertVideo(IMediaPlayer iMediaPlayer, int i2) {
                if (IjkVideoView.this.mOnInsertVideoListener != null) {
                    IjkVideoView.this.mOnInsertVideoListener.onInsertVideo(IjkVideoView.this, i2);
                }
            }
        };
        this.mReceivedFrameListener = new IMediaPlayer.OnReceivedFrameListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.13
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnReceivedFrameListener
            public void onReceivedFrame(IMediaPlayer iMediaPlayer, byte[] bArr, int i2, int i22, int i3) {
                if (IjkVideoView.this.mOnReceivedFrameListener != null) {
                    IjkVideoView.this.mOnReceivedFrameListener.onReceivedFrame(IjkVideoView.this, bArr, i2, i22, i3);
                }
            }
        };
        this.mReceivedOriginalDataListener = new IMediaPlayer.OnReceivedOriginalDataListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.14
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnReceivedOriginalDataListener
            public void onReceivedOriginalData(IMediaPlayer iMediaPlayer, byte[] bArr, int i2, int i22, int i3, int i4) {
                if (IjkVideoView.this.mOnReceivedOriginalDataListener != null) {
                    IjkVideoView.this.mOnReceivedOriginalDataListener.onReceivedOriginalData(IjkVideoView.this, bArr, i2, i22, i3, i4);
                }
            }
        };
        this.mDeviceConnectedListener = new IMediaPlayer.OnDeviceConnectedListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.15
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnDeviceConnectedListener
            public void onDeviceConnected(IMediaPlayer iMediaPlayer) {
                if (IjkVideoView.this.mOnDeviceConnectedListener != null) {
                    IjkVideoView.this.mOnDeviceConnectedListener.onDeviceConnected(IjkVideoView.this);
                }
            }
        };
        this.mSHCallback = new IRenderView.IRenderCallback() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.16
            @Override // tv.danmaku.ijk.media.widget.IRenderView.IRenderCallback
            public void onSurfaceChanged(IRenderView.ISurfaceHolder iSurfaceHolder, int i2, int i22, int i3) throws IllegalStateException {
                if (iSurfaceHolder.getRenderView() != IjkVideoView.this.mRenderView) {
                    Log.e(IjkVideoView.this.TAG, "onSurfaceChanged: unmatched render callback\n");
                    return;
                }
                IjkVideoView.this.mSurfaceWidth = i22;
                IjkVideoView.this.mSurfaceHeight = i3;
                boolean z = true;
                boolean z2 = IjkVideoView.this.mTargetState == 3;
                if (IjkVideoView.this.mRenderView.shouldWaitForResize() && (IjkVideoView.this.mVideoWidth != i22 || IjkVideoView.this.mVideoHeight != i3)) {
                    z = false;
                }
                if (IjkVideoView.this.mMediaPlayer != null && z2 && z) {
                    if (IjkVideoView.this.mSeekWhenPrepared != 0) {
                        IjkVideoView ijkVideoView = IjkVideoView.this;
                        ijkVideoView.seekTo(ijkVideoView.mSeekWhenPrepared);
                    }
                    IjkVideoView.this.start();
                }
            }

            @Override // tv.danmaku.ijk.media.widget.IRenderView.IRenderCallback
            public void onSurfaceCreated(IRenderView.ISurfaceHolder iSurfaceHolder, int i2, int i22) {
                if (iSurfaceHolder.getRenderView() != IjkVideoView.this.mRenderView) {
                    Log.e(IjkVideoView.this.TAG, "onSurfaceCreated: unmatched render callback\n");
                    return;
                }
                IjkVideoView.this.mSurfaceHolder = iSurfaceHolder;
                if (IjkVideoView.this.mMediaPlayer == null) {
                    IjkVideoView.this.openVideo();
                } else {
                    IjkVideoView ijkVideoView = IjkVideoView.this;
                    ijkVideoView.bindSurfaceHolder(ijkVideoView.mMediaPlayer, iSurfaceHolder);
                }
            }

            @Override // tv.danmaku.ijk.media.widget.IRenderView.IRenderCallback
            public void onSurfaceDestroyed(IRenderView.ISurfaceHolder iSurfaceHolder) {
                if (iSurfaceHolder.getRenderView() != IjkVideoView.this.mRenderView) {
                    Log.e(IjkVideoView.this.TAG, "onSurfaceDestroyed: unmatched render callback\n");
                } else {
                    IjkVideoView.this.mSurfaceHolder = null;
                    IjkVideoView.this.releaseWithoutStop();
                }
            }
        };
        this._data = null;
        this.mCurrentAspectRatioIndex = 0;
        this.mCurrentAspectRatio = s_allAspectRatio[0];
        this.mCurrentRender = 0;
        this.mEnableBackgroundPlay = false;
        initVideoView(context);
    }

    public IjkVideoView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.TAG = "IjkVideoView";
        this.mCurrentState = 0;
        this.mTargetState = 0;
        this.mSurfaceHolder = null;
        this.mMediaPlayer = null;
        this.mCanPause = true;
        this.mCanSeekBack = true;
        this.mCanSeekForward = true;
        this.mPrepareStartTime = 0L;
        this.mPrepareEndTime = 0L;
        this.mSeekStartTime = 0L;
        this.mSeekEndTime = 0L;
        this.mSizeChangedListener = new IMediaPlayer.OnVideoSizeChangedListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.1
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnVideoSizeChangedListener
            public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i22, int i222, int i3, int i4) {
                IjkVideoView.this.mVideoWidth = iMediaPlayer.getVideoWidth();
                IjkVideoView.this.mVideoHeight = iMediaPlayer.getVideoHeight();
                IjkVideoView.this.mVideoSarNum = iMediaPlayer.getVideoSarNum();
                IjkVideoView.this.mVideoSarDen = iMediaPlayer.getVideoSarDen();
                if (IjkVideoView.this.mVideoWidth == 0 || IjkVideoView.this.mVideoHeight == 0) {
                    return;
                }
                if (IjkVideoView.this.mRenderView != null) {
                    IjkVideoView.this.mRenderView.setVideoSize(IjkVideoView.this.mVideoWidth, IjkVideoView.this.mVideoHeight);
                    IjkVideoView.this.mRenderView.setVideoSampleAspectRatio(IjkVideoView.this.mVideoSarNum, IjkVideoView.this.mVideoSarDen);
                }
                IjkVideoView.this.requestLayout();
            }
        };
        this.mPreparedListener = new IMediaPlayer.OnPreparedListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.2
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnPreparedListener
            public void onPrepared(IMediaPlayer iMediaPlayer) throws IllegalStateException {
                IjkVideoView.this.mPrepareEndTime = System.currentTimeMillis();
                if (IjkVideoView.this.mHudViewHolder != null) {
                    IjkVideoView.this.mHudViewHolder.updateLoadCost(IjkVideoView.this.mPrepareEndTime - IjkVideoView.this.mPrepareStartTime);
                }
                IjkVideoView.this.mCurrentState = 2;
                IjkVideoView.this.rotation180 = false;
                if (IjkVideoView.this.mOnPreparedListener != null) {
                    IjkVideoView.this.mOnPreparedListener.onPrepared(IjkVideoView.this);
                }
                if (IjkVideoView.this.mMediaController != null) {
                    IjkVideoView.this.mMediaController.setEnabled(true);
                }
                IjkVideoView.this.mVideoWidth = iMediaPlayer.getVideoWidth();
                IjkVideoView.this.mVideoHeight = iMediaPlayer.getVideoHeight();
                int i22 = IjkVideoView.this.mSeekWhenPrepared;
                if (i22 != 0) {
                    IjkVideoView.this.seekTo(i22);
                }
                if (IjkVideoView.this.mVideoWidth == 0 || IjkVideoView.this.mVideoHeight == 0) {
                    if (IjkVideoView.this.mTargetState == 3) {
                        IjkVideoView.this.start();
                    }
                } else if (IjkVideoView.this.mRenderView != null) {
                    IjkVideoView.this.mRenderView.setVideoSize(IjkVideoView.this.mVideoWidth, IjkVideoView.this.mVideoHeight);
                    IjkVideoView.this.mRenderView.setVideoSampleAspectRatio(IjkVideoView.this.mVideoSarNum, IjkVideoView.this.mVideoSarDen);
                    if (!IjkVideoView.this.mRenderView.shouldWaitForResize() || (IjkVideoView.this.mSurfaceWidth == IjkVideoView.this.mVideoWidth && IjkVideoView.this.mSurfaceHeight == IjkVideoView.this.mVideoHeight)) {
                        if (IjkVideoView.this.mTargetState == 3) {
                            IjkVideoView.this.start();
                            if (IjkVideoView.this.mMediaController != null) {
                                IjkVideoView.this.mMediaController.show();
                            }
                        } else if (!IjkVideoView.this.isPlaying() && ((i22 != 0 || IjkVideoView.this.getCurrentPosition() > 0) && IjkVideoView.this.mMediaController != null)) {
                            IjkVideoView.this.mMediaController.show(0);
                        }
                    }
                }
                IjkVideoView ijkVideoView = IjkVideoView.this;
                ijkVideoView.setStretchVrMode(ijkVideoView.vrMode, IjkVideoView.this.vrStretched);
            }
        };
        this.mCompletionListener = new IMediaPlayer.OnCompletionListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.3
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnCompletionListener
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                IjkVideoView.this.mCurrentState = 5;
                IjkVideoView.this.mTargetState = 5;
                if (IjkVideoView.this.mMediaController != null) {
                    IjkVideoView.this.mMediaController.hide();
                }
                if (IjkVideoView.this.mOnCompletionListener != null) {
                    IjkVideoView.this.mOnCompletionListener.onCompletion(IjkVideoView.this);
                }
            }
        };
        this.mInfoListener = new IMediaPlayer.OnInfoListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.4
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnInfoListener
            public boolean onInfo(IMediaPlayer iMediaPlayer, int i22, int i222) {
                if (IjkVideoView.this.mOnInfoListener != null) {
                    IjkVideoView.this.mOnInfoListener.onInfo(IjkVideoView.this, i22, i222);
                }
                if (i22 == 3) {
                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_VIDEO_RENDERING_START:");
                    return true;
                }
                if (i22 == 901) {
                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_UNSUPPORTED_SUBTITLE:");
                    return true;
                }
                if (i22 == 902) {
                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_SUBTITLE_TIMED_OUT:");
                    return true;
                }
                if (i22 == 10001) {
                    IjkVideoView.this.mVideoRotationDegree = i222;
                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_VIDEO_ROTATION_CHANGED: " + i222);
                    if (IjkVideoView.this.mRenderView == null) {
                        return true;
                    }
                    IjkVideoView.this.mRenderView.setVideoRotation(i222);
                    return true;
                }
                if (i22 != 10002) {
                    switch (i22) {
                        case 700:
                            Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_VIDEO_TRACK_LAGGING:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_BUFFERING_START /* 701 */:
                            Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_BUFFERING_START:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_BUFFERING_END /* 702 */:
                            Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_BUFFERING_END:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH /* 703 */:
                            Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_NETWORK_BANDWIDTH: " + i222);
                            break;
                        default:
                            switch (i22) {
                                case 800:
                                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_BAD_INTERLEAVING:");
                                    break;
                                case IMediaPlayer.MEDIA_INFO_NOT_SEEKABLE /* 801 */:
                                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_NOT_SEEKABLE:");
                                    break;
                                case IMediaPlayer.MEDIA_INFO_METADATA_UPDATE /* 802 */:
                                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_METADATA_UPDATE:");
                                    break;
                            }
                    }
                    return true;
                }
                Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_AUDIO_RENDERING_START:");
                return true;
            }
        };
        this.mErrorListener = new IMediaPlayer.OnErrorListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.5
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnErrorListener
            public boolean onError(IMediaPlayer iMediaPlayer, int i22, int i222) {
                int i3;
                Log.d(IjkVideoView.this.TAG, "Error: " + i22 + "," + i222);
                IjkVideoView.this.mCurrentState = -1;
                IjkVideoView.this.mTargetState = -1;
                if (IjkVideoView.this.mMediaController != null) {
                    IjkVideoView.this.mMediaController.hide();
                }
                if ((IjkVideoView.this.mOnErrorListener == null || !IjkVideoView.this.mOnErrorListener.onError(IjkVideoView.this, i22, i222)) && IjkVideoView.this.getWindowToken() != null) {
                    IjkVideoView.this.mAppContext.getResources();
                    if (i22 == 200) {
                        i3 = C0939R.string.VideoView_error_text_invalid_progressive_playback;
                    } else {
                        i3 = C0939R.string.VideoView_error_text_unknown;
                    }
                    new AlertDialog.Builder(IjkVideoView.this.getContext()).setMessage(i3).setPositiveButton(C0939R.string.VideoView_error_button, new DialogInterface.OnClickListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.5.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i4) {
                            if (IjkVideoView.this.mOnCompletionListener != null) {
                                IjkVideoView.this.mOnCompletionListener.onCompletion(IjkVideoView.this);
                            }
                        }
                    }).setCancelable(false).show();
                }
                return true;
            }
        };
        this.mBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.6
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnBufferingUpdateListener
            public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i22) {
                IjkVideoView.this.mCurrentBufferPercentage = i22;
            }
        };
        this.mSeekCompleteListener = new IMediaPlayer.OnSeekCompleteListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.7
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnSeekCompleteListener
            public void onSeekComplete(IMediaPlayer iMediaPlayer) {
                IjkVideoView.this.mSeekEndTime = System.currentTimeMillis();
                if (IjkVideoView.this.mHudViewHolder != null) {
                    IjkVideoView.this.mHudViewHolder.updateSeekCost(IjkVideoView.this.mSeekEndTime - IjkVideoView.this.mSeekStartTime);
                }
            }
        };
        this.mOnTimedTextListener = new IMediaPlayer.OnTimedTextListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.8
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnTimedTextListener
            public void onTimedText(IMediaPlayer iMediaPlayer, IjkTimedText ijkTimedText) {
                if (ijkTimedText != null) {
                    IjkVideoView.this.subtitleDisplay.setText(ijkTimedText.getText());
                }
            }
        };
        this.mReceivedRtcpSrDataListener = new IMediaPlayer.OnReceivedRtcpSrDataListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.9
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnReceivedRtcpSrDataListener
            public void onReceivedRtcpSrData(IMediaPlayer iMediaPlayer, byte[] bArr) {
                if (IjkVideoView.this.mOnReceivedRtcpSrDataListener != null) {
                    IjkVideoView.this.mOnReceivedRtcpSrDataListener.onReceivedRtcpSrData(IjkVideoView.this, bArr);
                }
                if (IjkVideoView.this.mOnReceivedDataListener != null && bArr.length > 4 && bArr[0] == 105 && bArr[1] == 30 && bArr[2] == 90 && bArr[3] == 15) {
                    int i22 = (bArr[4] + UByte.MIN_VALUE) & 255;
                    if (i22 > 0 && bArr.length - 5 >= i22) {
                        IjkVideoView.this.mOnReceivedDataListener.onReceivedData(IjkVideoView.this, Arrays.copyOfRange(bArr, 5, i22 + 5));
                    } else if (i22 == 0) {
                        Log.d(IjkVideoView.this.TAG, "onReceivedData: empty message\n");
                    }
                }
            }
        };
        this.mTookPictureListener = new IMediaPlayer.OnTookPictureListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.10
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnTookPictureListener
            public void onTookPicture(IMediaPlayer iMediaPlayer, int i22, String str) {
                if (IjkVideoView.this.mOnTookPictureListener != null) {
                    IjkVideoView.this.mOnTookPictureListener.onTookPicture(IjkVideoView.this, i22, str);
                }
            }
        };
        this.mRecordVideoListener = new IMediaPlayer.OnRecordVideoListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.11
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnRecordVideoListener
            public void onRecordVideo(IMediaPlayer iMediaPlayer, int i22, String str) {
                if (IjkVideoView.this.mOnRecordVideoListener != null) {
                    IjkVideoView.this.mOnRecordVideoListener.onRecordVideo(IjkVideoView.this, i22, str);
                }
            }
        };
        this.mInsertVideoListener = new IMediaPlayer.OnInsertVideoListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.12
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnInsertVideoListener
            public void onInsertVideo(IMediaPlayer iMediaPlayer, int i22) {
                if (IjkVideoView.this.mOnInsertVideoListener != null) {
                    IjkVideoView.this.mOnInsertVideoListener.onInsertVideo(IjkVideoView.this, i22);
                }
            }
        };
        this.mReceivedFrameListener = new IMediaPlayer.OnReceivedFrameListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.13
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnReceivedFrameListener
            public void onReceivedFrame(IMediaPlayer iMediaPlayer, byte[] bArr, int i22, int i222, int i3) {
                if (IjkVideoView.this.mOnReceivedFrameListener != null) {
                    IjkVideoView.this.mOnReceivedFrameListener.onReceivedFrame(IjkVideoView.this, bArr, i22, i222, i3);
                }
            }
        };
        this.mReceivedOriginalDataListener = new IMediaPlayer.OnReceivedOriginalDataListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.14
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnReceivedOriginalDataListener
            public void onReceivedOriginalData(IMediaPlayer iMediaPlayer, byte[] bArr, int i22, int i222, int i3, int i4) {
                if (IjkVideoView.this.mOnReceivedOriginalDataListener != null) {
                    IjkVideoView.this.mOnReceivedOriginalDataListener.onReceivedOriginalData(IjkVideoView.this, bArr, i22, i222, i3, i4);
                }
            }
        };
        this.mDeviceConnectedListener = new IMediaPlayer.OnDeviceConnectedListener() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.15
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnDeviceConnectedListener
            public void onDeviceConnected(IMediaPlayer iMediaPlayer) {
                if (IjkVideoView.this.mOnDeviceConnectedListener != null) {
                    IjkVideoView.this.mOnDeviceConnectedListener.onDeviceConnected(IjkVideoView.this);
                }
            }
        };
        this.mSHCallback = new IRenderView.IRenderCallback() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.16
            @Override // tv.danmaku.ijk.media.widget.IRenderView.IRenderCallback
            public void onSurfaceChanged(IRenderView.ISurfaceHolder iSurfaceHolder, int i22, int i222, int i3) throws IllegalStateException {
                if (iSurfaceHolder.getRenderView() != IjkVideoView.this.mRenderView) {
                    Log.e(IjkVideoView.this.TAG, "onSurfaceChanged: unmatched render callback\n");
                    return;
                }
                IjkVideoView.this.mSurfaceWidth = i222;
                IjkVideoView.this.mSurfaceHeight = i3;
                boolean z = true;
                boolean z2 = IjkVideoView.this.mTargetState == 3;
                if (IjkVideoView.this.mRenderView.shouldWaitForResize() && (IjkVideoView.this.mVideoWidth != i222 || IjkVideoView.this.mVideoHeight != i3)) {
                    z = false;
                }
                if (IjkVideoView.this.mMediaPlayer != null && z2 && z) {
                    if (IjkVideoView.this.mSeekWhenPrepared != 0) {
                        IjkVideoView ijkVideoView = IjkVideoView.this;
                        ijkVideoView.seekTo(ijkVideoView.mSeekWhenPrepared);
                    }
                    IjkVideoView.this.start();
                }
            }

            @Override // tv.danmaku.ijk.media.widget.IRenderView.IRenderCallback
            public void onSurfaceCreated(IRenderView.ISurfaceHolder iSurfaceHolder, int i22, int i222) {
                if (iSurfaceHolder.getRenderView() != IjkVideoView.this.mRenderView) {
                    Log.e(IjkVideoView.this.TAG, "onSurfaceCreated: unmatched render callback\n");
                    return;
                }
                IjkVideoView.this.mSurfaceHolder = iSurfaceHolder;
                if (IjkVideoView.this.mMediaPlayer == null) {
                    IjkVideoView.this.openVideo();
                } else {
                    IjkVideoView ijkVideoView = IjkVideoView.this;
                    ijkVideoView.bindSurfaceHolder(ijkVideoView.mMediaPlayer, iSurfaceHolder);
                }
            }

            @Override // tv.danmaku.ijk.media.widget.IRenderView.IRenderCallback
            public void onSurfaceDestroyed(IRenderView.ISurfaceHolder iSurfaceHolder) {
                if (iSurfaceHolder.getRenderView() != IjkVideoView.this.mRenderView) {
                    Log.e(IjkVideoView.this.TAG, "onSurfaceDestroyed: unmatched render callback\n");
                } else {
                    IjkVideoView.this.mSurfaceHolder = null;
                    IjkVideoView.this.releaseWithoutStop();
                }
            }
        };
        this._data = null;
        this.mCurrentAspectRatioIndex = 0;
        this.mCurrentAspectRatio = s_allAspectRatio[0];
        this.mCurrentRender = 0;
        this.mEnableBackgroundPlay = false;
        initVideoView(context);
    }

    public void setOptions(IjkMpOptions ijkMpOptions) {
        this.mOptions = ijkMpOptions;
    }

    private void initVideoView(Context context) {
        this.mAppContext = context.getApplicationContext();
        initBackground();
        initRenders();
        this.mVideoWidth = 0;
        this.mVideoHeight = 0;
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        this.mCurrentState = 0;
        this.mTargetState = 0;
        TextView textView = new TextView(context);
        this.subtitleDisplay = textView;
        textView.setTextSize(24.0f);
        this.subtitleDisplay.setGravity(17);
        addView(this.subtitleDisplay, new FrameLayout.LayoutParams(-1, -2, 80));
    }

    public void setRenderView(IRenderView iRenderView) {
        int i;
        int i2;
        if (this.mRenderView != null) {
            IMediaPlayer iMediaPlayer = this.mMediaPlayer;
            if (iMediaPlayer != null) {
                iMediaPlayer.setDisplay(null);
            }
            View view = this.mRenderView.getView();
            this.mRenderView.removeRenderCallback(this.mSHCallback);
            this.mRenderView = null;
            removeView(view);
        }
        if (iRenderView == null) {
            return;
        }
        this.mRenderView = iRenderView;
        iRenderView.setAspectRatio(this.mCurrentAspectRatio);
        int i3 = this.mVideoWidth;
        if (i3 > 0 && (i2 = this.mVideoHeight) > 0) {
            iRenderView.setVideoSize(i3, i2);
        }
        int i4 = this.mVideoSarNum;
        if (i4 > 0 && (i = this.mVideoSarDen) > 0) {
            iRenderView.setVideoSampleAspectRatio(i4, i);
        }
        View view2 = this.mRenderView.getView();
        view2.setLayoutParams(new FrameLayout.LayoutParams(-2, -2, 17));
        addView(view2);
        this.mRenderView.addRenderCallback(this.mSHCallback);
        this.mRenderView.setVideoRotation(this.mVideoRotationDegree);
    }

    public void setRender(int i) {
        if (i == 0) {
            setRenderView(null);
            return;
        }
        if (i == 1) {
            setRenderView(new SurfaceRenderView(getContext()));
            return;
        }
        if (i == 2) {
            TextureRenderView textureRenderView = new TextureRenderView(getContext());
            if (this.mMediaPlayer != null) {
                textureRenderView.getSurfaceHolder().bindToMediaPlayer(this.mMediaPlayer);
                textureRenderView.setVideoSize(this.mMediaPlayer.getVideoWidth(), this.mMediaPlayer.getVideoHeight());
                textureRenderView.setVideoSampleAspectRatio(this.mMediaPlayer.getVideoSarNum(), this.mMediaPlayer.getVideoSarDen());
                textureRenderView.setAspectRatio(this.mCurrentAspectRatio);
            }
            setRenderView(textureRenderView);
            return;
        }
        Log.e(this.TAG, String.format(Locale.getDefault(), "invalid render %d\n", Integer.valueOf(i)));
    }

    public void setHudView(TableLayout tableLayout) {
        this.mHudViewHolder = new InfoHudViewHolder(getContext(), tableLayout);
    }

    public void setVideoPath(String str) {
        setVideoURI(Uri.parse(str));
    }

    public void setVideoURI(Uri uri) {
        setVideoURI(uri, null);
    }

    private void setVideoURI(Uri uri, Map<String, String> map) {
        this.mUri = uri;
        this.mHeaders = map;
        this.mSeekWhenPrepared = 0;
        openVideo();
        requestLayout();
        invalidate();
    }

    public void stopPlayback() throws IllegalStateException {
        IMediaPlayer iMediaPlayer = this.mMediaPlayer;
        if (iMediaPlayer != null) {
            iMediaPlayer.stop();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
            InfoHudViewHolder infoHudViewHolder = this.mHudViewHolder;
            if (infoHudViewHolder != null) {
                infoHudViewHolder.setMediaPlayer(null);
            }
            this.mCurrentState = 0;
            this.mTargetState = 0;
            ((AudioManager) this.mAppContext.getSystemService("audio")).abandonAudioFocus(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openVideo() {
        if (this.mUri == null || this.mSurfaceHolder == null) {
            return;
        }
        release(false);
        ((AudioManager) this.mAppContext.getSystemService("audio")).requestAudioFocus(null, 3, 1);
        try {
            this.mMediaPlayer = createPlayer();
            getContext();
            this.mMediaPlayer.setOnPreparedListener(this.mPreparedListener);
            this.mMediaPlayer.setOnVideoSizeChangedListener(this.mSizeChangedListener);
            this.mMediaPlayer.setOnCompletionListener(this.mCompletionListener);
            this.mMediaPlayer.setOnErrorListener(this.mErrorListener);
            this.mMediaPlayer.setOnInfoListener(this.mInfoListener);
            this.mMediaPlayer.setOnBufferingUpdateListener(this.mBufferingUpdateListener);
            this.mMediaPlayer.setOnSeekCompleteListener(this.mSeekCompleteListener);
            this.mMediaPlayer.setOnTimedTextListener(this.mOnTimedTextListener);
            this.mMediaPlayer.setOnReceivedRtcpSrDataListener(this.mReceivedRtcpSrDataListener);
            this.mMediaPlayer.setOnTookPictureListener(this.mTookPictureListener);
            this.mMediaPlayer.setOnRecordVideoListener(this.mRecordVideoListener);
            this.mMediaPlayer.setOnInsertVideoListener(this.mInsertVideoListener);
            this.mMediaPlayer.setOnReceivedFrameListener(this.mReceivedFrameListener);
            this.mMediaPlayer.setOnReceivedOriginalDataListener(this.mReceivedOriginalDataListener);
            this.mMediaPlayer.setOnDeviceConnectedListener(this.mDeviceConnectedListener);
            this.mCurrentBufferPercentage = 0;
            this.mUri.getScheme();
            this.mMediaPlayer.setDataSource(this.mAppContext, this.mUri, this.mHeaders);
            bindSurfaceHolder(this.mMediaPlayer, this.mSurfaceHolder);
            this.mMediaPlayer.setAudioStreamType(3);
            this.mMediaPlayer.setScreenOnWhilePlaying(true);
            this.mPrepareStartTime = System.currentTimeMillis();
            this.mMediaPlayer.prepareAsync();
            InfoHudViewHolder infoHudViewHolder = this.mHudViewHolder;
            if (infoHudViewHolder != null) {
                infoHudViewHolder.setMediaPlayer(this.mMediaPlayer);
            }
            this.mCurrentState = 1;
            attachMediaController();
        } catch (IOException e) {
            Log.w(this.TAG, "Unable to open content: " + this.mUri, e);
            this.mCurrentState = -1;
            this.mTargetState = -1;
            this.mErrorListener.onError(this.mMediaPlayer, 1, 0);
        } catch (IllegalArgumentException e2) {
            Log.w(this.TAG, "Unable to open content: " + this.mUri, e2);
            this.mCurrentState = -1;
            this.mTargetState = -1;
            this.mErrorListener.onError(this.mMediaPlayer, 1, 0);
        }
    }

    public void setMediaController(IMediaController iMediaController) {
        IMediaController iMediaController2 = this.mMediaController;
        if (iMediaController2 != null) {
            iMediaController2.hide();
        }
        this.mMediaController = iMediaController;
        attachMediaController();
    }

    private void attachMediaController() {
        IMediaController iMediaController;
        if (this.mMediaPlayer == null || (iMediaController = this.mMediaController) == null) {
            return;
        }
        iMediaController.setMediaPlayer(this);
        this.mMediaController.setAnchorView(getParent() instanceof View ? (View) getParent() : this);
        this.mMediaController.setEnabled(isInPlaybackState());
    }

    public void setOnPreparedListener(IVideoView.OnPreparedListener onPreparedListener) {
        this.mOnPreparedListener = onPreparedListener;
    }

    public void setOnCompletionListener(IVideoView.OnCompletionListener onCompletionListener) {
        this.mOnCompletionListener = onCompletionListener;
    }

    public void setOnErrorListener(IVideoView.OnErrorListener onErrorListener) {
        this.mOnErrorListener = onErrorListener;
    }

    public void setOnInfoListener(IVideoView.OnInfoListener onInfoListener) {
        this.mOnInfoListener = onInfoListener;
    }

    public void setOnReceivedRtcpSrDataListener(IVideoView.OnReceivedRtcpSrDataListener onReceivedRtcpSrDataListener) {
        this.mOnReceivedRtcpSrDataListener = onReceivedRtcpSrDataListener;
    }

    public void setOnReceivedDataListener(IVideoView.OnReceivedDataListener onReceivedDataListener) {
        this.mOnReceivedDataListener = onReceivedDataListener;
    }

    public void setOnTookPictureListener(IVideoView.OnTookPictureListener onTookPictureListener) {
        this.mOnTookPictureListener = onTookPictureListener;
    }

    public void setOnRecordVideoListener(IVideoView.OnRecordVideoListener onRecordVideoListener) {
        this.mOnRecordVideoListener = onRecordVideoListener;
    }

    public void setOnInsertVideoListener(IVideoView.OnInsertVideoListener onInsertVideoListener) {
        this.mOnInsertVideoListener = onInsertVideoListener;
    }

    public void setOnReceivedFrameListener(IVideoView.OnReceivedFrameListener onReceivedFrameListener) {
        this.mOnReceivedFrameListener = onReceivedFrameListener;
    }

    public void setOnReceivedOriginalDataListener(IVideoView.OnReceivedOriginalDataListener onReceivedOriginalDataListener) {
        this.mOnReceivedOriginalDataListener = onReceivedOriginalDataListener;
    }

    public void setOnDeviceConnectedListener(IVideoView.OnDeviceConnectedListener onDeviceConnectedListener) {
        this.mOnDeviceConnectedListener = onDeviceConnectedListener;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void bindSurfaceHolder(IMediaPlayer iMediaPlayer, IRenderView.ISurfaceHolder iSurfaceHolder) {
        if (iMediaPlayer == null) {
            return;
        }
        if (iSurfaceHolder == null) {
            iMediaPlayer.setDisplay(null);
        } else {
            iSurfaceHolder.bindToMediaPlayer(iMediaPlayer);
        }
    }

    public void releaseWithoutStop() {
        IMediaPlayer iMediaPlayer = this.mMediaPlayer;
        if (iMediaPlayer != null) {
            iMediaPlayer.setDisplay(null);
        }
    }

    public void release(boolean z) {
        IMediaPlayer iMediaPlayer = this.mMediaPlayer;
        if (iMediaPlayer != null) {
            iMediaPlayer.reset();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
            this.mCurrentState = 0;
            if (z) {
                this.mTargetState = 0;
            }
            ((AudioManager) this.mAppContext.getSystemService("audio")).abandonAudioFocus(null);
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!isInPlaybackState() || this.mMediaController == null) {
            return false;
        }
        toggleMediaControlsVisiblity();
        return false;
    }

    @Override // android.view.View
    public boolean onTrackballEvent(MotionEvent motionEvent) {
        if (!isInPlaybackState() || this.mMediaController == null) {
            return false;
        }
        toggleMediaControlsVisiblity();
        return false;
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) throws IllegalStateException {
        boolean z = (i == 4 || i == 24 || i == 25 || i == 164 || i == 82 || i == 5 || i == 6) ? false : true;
        if (isInPlaybackState() && z && this.mMediaController != null) {
            if (i == 79 || i == 85) {
                if (this.mMediaPlayer.isPlaying()) {
                    pause();
                    this.mMediaController.show();
                } else {
                    start();
                    this.mMediaController.hide();
                }
                return true;
            }
            if (i == 126) {
                if (!this.mMediaPlayer.isPlaying()) {
                    start();
                    this.mMediaController.hide();
                }
                return true;
            }
            if (i == 86 || i == 127) {
                if (this.mMediaPlayer.isPlaying()) {
                    pause();
                    this.mMediaController.show();
                }
                return true;
            }
            toggleMediaControlsVisiblity();
        }
        return super.onKeyDown(i, keyEvent);
    }

    private void toggleMediaControlsVisiblity() {
        if (this.mMediaController.isShowing()) {
            this.mMediaController.hide();
        } else {
            this.mMediaController.show();
        }
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public void start() throws IllegalStateException {
        if (isInPlaybackState()) {
            this.mMediaPlayer.start();
            this.mCurrentState = 3;
        }
        this.mTargetState = 3;
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public void pause() throws IllegalStateException {
        if (isInPlaybackState() && this.mMediaPlayer.isPlaying()) {
            this.mMediaPlayer.pause();
            this.mCurrentState = 4;
        }
        this.mTargetState = 4;
    }

    public void suspend() {
        release(false);
    }

    public void resume() {
        openVideo();
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public int getDuration() {
        if (isInPlaybackState()) {
            return (int) this.mMediaPlayer.getDuration();
        }
        return -1;
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public int getCurrentPosition() {
        if (isInPlaybackState()) {
            return (int) this.mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public void seekTo(int i) throws IllegalStateException {
        if (isInPlaybackState()) {
            this.mSeekStartTime = System.currentTimeMillis();
            this.mMediaPlayer.seekTo(i);
            this.mSeekWhenPrepared = 0;
            return;
        }
        this.mSeekWhenPrepared = i;
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public boolean isPlaying() {
        return isInPlaybackState() && this.mMediaPlayer.isPlaying();
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public int getBufferPercentage() {
        if (this.mMediaPlayer != null) {
            return this.mCurrentBufferPercentage;
        }
        return 0;
    }

    private boolean isInPlaybackState() {
        int i;
        return (this.mMediaPlayer == null || (i = this.mCurrentState) == -1 || i == 0 || i == 1) ? false : true;
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public boolean canPause() {
        return this.mCanPause;
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public boolean canSeekBackward() {
        return this.mCanSeekBack;
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public boolean canSeekForward() {
        return this.mCanSeekForward;
    }

    IjkMediaPlayer ijkplayerInstance(IMediaPlayer iMediaPlayer) {
        if (iMediaPlayer instanceof IjkMediaPlayer) {
            return (IjkMediaPlayer) iMediaPlayer;
        }
        return null;
    }

    private void doBackgroundTask(String str, Runnable runnable) {
        HandlerThread handlerThread = new HandlerThread(str);
        handlerThread.start();
        new Handler(handlerThread.getLooper()).post(runnable);
    }

    public void sendRtcpRrData(byte[] bArr) throws IllegalStateException {
        if (this.mMediaPlayer == null || !isPlaying()) {
            return;
        }
        this.mMediaPlayer.sendRtcpRrData(bArr);
    }

    public void sendData(byte[] bArr) throws IllegalStateException {
        if (this.mMediaPlayer == null || !isPlaying()) {
            return;
        }
        byte[] bArr2 = this._data;
        if (bArr2 == null || bArr2.length != bArr.length + 3) {
            this._data = new byte[bArr.length + 3];
        }
        byte b = 0;
        for (byte b2 : bArr) {
            b = (byte) (b ^ b2);
        }
        byte b3 = (byte) (~b);
        byte[] bArr3 = this._data;
        bArr3[0] = 102;
        System.arraycopy(bArr, 0, bArr3, 1, bArr.length);
        byte[] bArr4 = this._data;
        bArr4[bArr.length + 1] = b3;
        bArr4[bArr.length + 2] = -103;
        this.mMediaPlayer.sendRtcpRrData(bArr4);
    }

    public void takePicture(final String str, final String str2, final int i, final int i2, final int i3) throws IllegalStateException, IOException, SecurityException, IllegalArgumentException {
        if (this.mMediaPlayer == null || !isPlaying()) {
            return;
        }
        doBackgroundTask("takePicture", new Runnable() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.17
            @Override // java.lang.Runnable
            public void run() {
                try {
                    IjkVideoView.this.mMediaPlayer.takePicture(str, str2, i, i2, i3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void startRecordVideo(final String str, final String str2, final int i, final int i2) throws IllegalStateException, IOException, SecurityException, IllegalArgumentException {
        if (this.mMediaPlayer == null || !isPlaying()) {
            return;
        }
        doBackgroundTask("startRecordVideo", new Runnable() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.18
            @Override // java.lang.Runnable
            public void run() {
                try {
                    IjkVideoView.this.mMediaPlayer.startRecordVideo(str, str2, i, i2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void stopRecordVideo() throws IllegalStateException {
        if (this.mMediaPlayer != null) {
            doBackgroundTask("startRecordVideo", new Runnable() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.19
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        IjkVideoView.this.mMediaPlayer.stopRecordVideo();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void prestartInsertVideo(String str, String str2, int i, int i2) throws IllegalStateException, IOException, SecurityException, IllegalArgumentException {
        IjkMediaPlayer ijkMediaPlayerIjkplayerInstance = ijkplayerInstance(this.mMediaPlayer);
        if (ijkMediaPlayerIjkplayerInstance != null) {
            ijkMediaPlayerIjkplayerInstance.prestartInsertVideo(str, str2, i, i2);
        }
    }

    public void startInsertVideo(final int i, final int i2, final int i3) throws IllegalStateException {
        if (this.mMediaPlayer == null || !isPlaying()) {
            return;
        }
        doBackgroundTask("startInsertVideo", new Runnable() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.20
            @Override // java.lang.Runnable
            public void run() {
                try {
                    IjkVideoView.this.mMediaPlayer.startInsertVideo(i, i2, i3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void insertVideoData(final byte[] bArr, final int i, final boolean z) throws IllegalStateException {
        if (this.mMediaPlayer == null || !isPlaying()) {
            return;
        }
        doBackgroundTask("insertVideoData", new Runnable() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.21
            @Override // java.lang.Runnable
            public void run() {
                try {
                    IjkVideoView.this.mMediaPlayer.insertVideoData(bArr, i, z);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void stopInsertVideo() throws IllegalStateException {
        if (this.mMediaPlayer == null || !isPlaying()) {
            return;
        }
        doBackgroundTask("stopInsertVideo", new Runnable() { // from class: tv.danmaku.ijk.media.widget.IjkVideoView.22
            @Override // java.lang.Runnable
            public void run() {
                try {
                    IjkVideoView.this.mMediaPlayer.stopInsertVideo();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setOutputVideo(boolean z) throws IllegalStateException {
        if (ijkplayerInstance(this.mMediaPlayer) != null) {
            this.mMediaPlayer.setOutputVideo(z);
        }
    }

    public void setOutputOriginalVideo(boolean z) throws IllegalStateException {
        IjkMediaPlayer ijkMediaPlayerIjkplayerInstance = ijkplayerInstance(this.mMediaPlayer);
        if (ijkMediaPlayerIjkplayerInstance != null) {
            ijkMediaPlayerIjkplayerInstance.setOutputOriginalVideo(z);
        }
    }

    public boolean isVrMode() {
        return this.vrMode;
    }

    public void setVrMode(boolean z) throws IllegalStateException {
        if (this.mMediaPlayer != null && isPlaying() && this.mMediaPlayer.setVrMode(z)) {
            this.vrMode = z;
            this.vrStretched = false;
        }
    }

    public void setStretchVrMode(boolean z, boolean z2) throws IllegalStateException {
        IjkMediaPlayer ijkMediaPlayerIjkplayerInstance = ijkplayerInstance(this.mMediaPlayer);
        if (ijkMediaPlayerIjkplayerInstance != null && isPlaying() && ijkMediaPlayerIjkplayerInstance.setStretchedVrMode(z, z2)) {
            this.vrMode = z;
            this.vrStretched = z2;
        }
    }

    public void setVideoRotation(int i) {
        IRenderView iRenderView = this.mRenderView;
        if (iRenderView != null) {
            iRenderView.setVideoRotation(i);
        }
    }

    public boolean isRotation180() {
        return this.rotation180;
    }

    public void setRotation180(boolean z) throws IllegalStateException {
        IjkMediaPlayer ijkMediaPlayerIjkplayerInstance = ijkplayerInstance(this.mMediaPlayer);
        if (ijkMediaPlayerIjkplayerInstance != null) {
            ijkMediaPlayerIjkplayerInstance.setRotation180(z);
            this.rotation180 = z;
        }
    }

    public void setScreenCoordRect(float f, float f2, float f3, float f4) throws IllegalStateException {
        IMediaPlayer iMediaPlayer = this.mMediaPlayer;
        if (iMediaPlayer != null) {
            iMediaPlayer.setTexcoordRect(f, f4, f3, f2);
        }
    }

    public void resetScreenCoordRect() throws IllegalStateException {
        setScreenCoordRect(0.0f, 0.0f, 1.0f, 1.0f);
    }

    public void setVideoFilter(String str, String str2, String str3, boolean z) throws IllegalStateException {
        IjkMediaPlayer ijkMediaPlayerIjkplayerInstance = ijkplayerInstance(this.mMediaPlayer);
        if (ijkMediaPlayerIjkplayerInstance != null) {
            ijkMediaPlayerIjkplayerInstance.setVideoFilter(str, str2, str3, z);
        }
    }

    public void setScale(float f) throws IllegalStateException {
        IjkMediaPlayer ijkMediaPlayerIjkplayerInstance = ijkplayerInstance(this.mMediaPlayer);
        if (ijkMediaPlayerIjkplayerInstance != null) {
            ijkMediaPlayerIjkplayerInstance.setScale(f);
        }
    }

    public float getScale() {
        IjkMediaPlayer ijkMediaPlayerIjkplayerInstance = ijkplayerInstance(this.mMediaPlayer);
        if (ijkMediaPlayerIjkplayerInstance != null) {
            return ijkMediaPlayerIjkplayerInstance.getScale();
        }
        return 1.0f;
    }

    public void skipFrame(int i) throws IllegalStateException {
        IjkMediaPlayer ijkMediaPlayerIjkplayerInstance = ijkplayerInstance(this.mMediaPlayer);
        if (ijkMediaPlayerIjkplayerInstance != null) {
            ijkMediaPlayerIjkplayerInstance.skipFrame(i);
        }
    }

    public void skipFrameInMS(long j) throws IllegalStateException {
        IjkMediaPlayer ijkMediaPlayerIjkplayerInstance = ijkplayerInstance(this.mMediaPlayer);
        if (ijkMediaPlayerIjkplayerInstance != null) {
            ijkMediaPlayerIjkplayerInstance.skipFrameInMS(j);
        }
    }

    public void stopSkipFrame() throws IllegalStateException {
        IjkMediaPlayer ijkMediaPlayerIjkplayerInstance = ijkplayerInstance(this.mMediaPlayer);
        if (ijkMediaPlayerIjkplayerInstance != null) {
            ijkMediaPlayerIjkplayerInstance.stopSkipFrame();
        }
    }

    public void dropRecordFrame(int i) throws IllegalStateException {
        IjkMediaPlayer ijkMediaPlayerIjkplayerInstance = ijkplayerInstance(this.mMediaPlayer);
        if (ijkMediaPlayerIjkplayerInstance != null) {
            ijkMediaPlayerIjkplayerInstance.dropRecordFrame(i);
        }
    }

    public float getDefineFramerate() {
        IjkMediaPlayer ijkMediaPlayerIjkplayerInstance = ijkplayerInstance(this.mMediaPlayer);
        if (ijkMediaPlayerIjkplayerInstance != null) {
            return ijkMediaPlayerIjkplayerInstance.getVideoDefineFramesPerSecond();
        }
        return 0.0f;
    }

    public float getOutputFramerate() {
        IjkMediaPlayer ijkMediaPlayerIjkplayerInstance = ijkplayerInstance(this.mMediaPlayer);
        if (ijkMediaPlayerIjkplayerInstance != null) {
            return ijkMediaPlayerIjkplayerInstance.getVideoOutputFramesPerSecond();
        }
        return 0.0f;
    }

    public float getDecodeFramerate() {
        IjkMediaPlayer ijkMediaPlayerIjkplayerInstance = ijkplayerInstance(this.mMediaPlayer);
        if (ijkMediaPlayerIjkplayerInstance != null) {
            return ijkMediaPlayerIjkplayerInstance.getVideoDecodeFramesPerSecond();
        }
        return 0.0f;
    }

    public void setVideoForceFramerate(float f) {
        IjkMediaPlayer ijkMediaPlayerIjkplayerInstance = ijkplayerInstance(this.mMediaPlayer);
        if (ijkMediaPlayerIjkplayerInstance != null) {
            ijkMediaPlayerIjkplayerInstance.setVideoForceFramerate(f);
        }
    }

    public int toggleAspectRatio() {
        int i = this.mCurrentAspectRatioIndex + 1;
        this.mCurrentAspectRatioIndex = i;
        int[] iArr = s_allAspectRatio;
        int length = i % iArr.length;
        this.mCurrentAspectRatioIndex = length;
        int i2 = iArr[length];
        this.mCurrentAspectRatio = i2;
        IRenderView iRenderView = this.mRenderView;
        if (iRenderView != null) {
            iRenderView.setAspectRatio(i2);
        }
        return this.mCurrentAspectRatio;
    }

    public void setAspectRatio(int i) {
        int i2 = 0;
        while (true) {
            int[] iArr = s_allAspectRatio;
            if (i2 >= iArr.length) {
                break;
            }
            if (i == iArr[i2]) {
                this.mCurrentAspectRatioIndex = i2;
                break;
            }
            i2++;
        }
        this.mCurrentAspectRatio = i;
        IRenderView iRenderView = this.mRenderView;
        if (iRenderView != null) {
            iRenderView.setAspectRatio(i);
        }
    }

    private void initRenders() {
        this.mCurrentRender = 1;
        setRender(1);
    }

    public IMediaPlayer createPlayer() {
        if (this.mUri == null) {
            return null;
        }
        IjkMediaPlayer ijkMediaPlayer = new IjkMediaPlayer();
        IjkMediaPlayer.native_setLogLevel(4);
        IjkMpOptions ijkMpOptions = this.mOptions;
        if (ijkMpOptions == null) {
            return ijkMediaPlayer;
        }
        ijkMpOptions.applyToMediaPlayer(ijkMediaPlayer);
        return ijkMediaPlayer;
    }

    private void initBackground() {
        this.mEnableBackgroundPlay = false;
    }

    public boolean isBackgroundPlayEnabled() {
        return this.mEnableBackgroundPlay;
    }

    public void enterBackground() throws IllegalStateException {
        MediaPlayerService.setMediaPlayer(this.mMediaPlayer);
    }

    public void stopBackgroundPlay() throws IllegalStateException {
        MediaPlayerService.setMediaPlayer(null);
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x015c  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0192  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x01d0  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void showMediaInfo() {
        int i;
        int i2;
        IMediaFormat format;
        char c;
        IMediaPlayer iMediaPlayer = this.mMediaPlayer;
        if (iMediaPlayer == null) {
            return;
        }
        int selectedTrack = MediaPlayerCompat.getSelectedTrack(iMediaPlayer, 1);
        int selectedTrack2 = MediaPlayerCompat.getSelectedTrack(this.mMediaPlayer, 2);
        int selectedTrack3 = MediaPlayerCompat.getSelectedTrack(this.mMediaPlayer, 3);
        TableLayoutBinder tableLayoutBinder = new TableLayoutBinder(getContext());
        tableLayoutBinder.appendSection(C0939R.string.mi_player);
        tableLayoutBinder.appendRow2(C0939R.string.mi_player, MediaPlayerCompat.getName(this.mMediaPlayer));
        tableLayoutBinder.appendSection(C0939R.string.mi_media);
        tableLayoutBinder.appendRow2(C0939R.string.mi_resolution, buildResolution(this.mVideoWidth, this.mVideoHeight, this.mVideoSarNum, this.mVideoSarDen));
        tableLayoutBinder.appendRow2(C0939R.string.mi_length, buildTimeMilli(this.mMediaPlayer.getDuration()));
        ITrackInfo[] trackInfo = this.mMediaPlayer.getTrackInfo();
        if (trackInfo != null) {
            int length = trackInfo.length;
            int i3 = -1;
            int i4 = 0;
            while (i4 < length) {
                ITrackInfo iTrackInfo = trackInfo[i4];
                i3++;
                int trackType = iTrackInfo.getTrackType();
                if (i3 == selectedTrack) {
                    StringBuilder sb = new StringBuilder();
                    i = selectedTrack;
                    sb.append(getContext().getString(C0939R.string.mi_stream_fmt1, Integer.valueOf(i3)));
                    sb.append(" ");
                    sb.append(getContext().getString(C0939R.string.mi__selected_video_track));
                    tableLayoutBinder.appendSection(sb.toString());
                    i2 = selectedTrack2;
                } else {
                    i = selectedTrack;
                    if (i3 == selectedTrack2) {
                        StringBuilder sb2 = new StringBuilder();
                        i2 = selectedTrack2;
                        sb2.append(getContext().getString(C0939R.string.mi_stream_fmt1, Integer.valueOf(i3)));
                        sb2.append(" ");
                        sb2.append(getContext().getString(C0939R.string.mi__selected_audio_track));
                        tableLayoutBinder.appendSection(sb2.toString());
                    } else {
                        i2 = selectedTrack2;
                        if (i3 == selectedTrack3) {
                            tableLayoutBinder.appendSection(getContext().getString(C0939R.string.mi_stream_fmt1, Integer.valueOf(i3)) + " " + getContext().getString(C0939R.string.mi__selected_subtitle_track));
                        } else {
                            tableLayoutBinder.appendSection(getContext().getString(C0939R.string.mi_stream_fmt1, Integer.valueOf(i3)));
                            tableLayoutBinder.appendRow2(C0939R.string.mi_type, buildTrackType(trackType));
                            tableLayoutBinder.appendRow2(C0939R.string.mi_language, buildLanguage(iTrackInfo.getLanguage()));
                            format = iTrackInfo.getFormat();
                            if (format == null || !(format instanceof IjkMediaFormat)) {
                                c = 2;
                            } else if (trackType == 1) {
                                c = 2;
                                if (trackType == 2) {
                                    tableLayoutBinder.appendRow2(C0939R.string.mi_codec, format.getString(IjkMediaFormat.KEY_IJK_CODEC_LONG_NAME_UI));
                                    tableLayoutBinder.appendRow2(C0939R.string.mi_profile_level, format.getString(IjkMediaFormat.KEY_IJK_CODEC_PROFILE_LEVEL_UI));
                                    tableLayoutBinder.appendRow2(C0939R.string.mi_sample_rate, format.getString(IjkMediaFormat.KEY_IJK_SAMPLE_RATE_UI));
                                    tableLayoutBinder.appendRow2(C0939R.string.mi_channels, format.getString(IjkMediaFormat.KEY_IJK_CHANNEL_UI));
                                    tableLayoutBinder.appendRow2(C0939R.string.mi_bit_rate, format.getString(IjkMediaFormat.KEY_IJK_BIT_RATE_UI));
                                }
                            } else {
                                c = 2;
                                tableLayoutBinder.appendRow2(C0939R.string.mi_codec, format.getString(IjkMediaFormat.KEY_IJK_CODEC_LONG_NAME_UI));
                                tableLayoutBinder.appendRow2(C0939R.string.mi_profile_level, format.getString(IjkMediaFormat.KEY_IJK_CODEC_PROFILE_LEVEL_UI));
                                tableLayoutBinder.appendRow2(C0939R.string.mi_pixel_format, format.getString(IjkMediaFormat.KEY_IJK_CODEC_PIXEL_FORMAT_UI));
                                tableLayoutBinder.appendRow2(C0939R.string.mi_resolution, format.getString(IjkMediaFormat.KEY_IJK_RESOLUTION_UI));
                                tableLayoutBinder.appendRow2(C0939R.string.mi_frame_rate, format.getString(IjkMediaFormat.KEY_IJK_FRAME_RATE_UI));
                                tableLayoutBinder.appendRow2(C0939R.string.mi_bit_rate, format.getString(IjkMediaFormat.KEY_IJK_BIT_RATE_UI));
                            }
                            i4++;
                            selectedTrack2 = i2;
                            selectedTrack = i;
                        }
                    }
                }
                tableLayoutBinder.appendRow2(C0939R.string.mi_type, buildTrackType(trackType));
                tableLayoutBinder.appendRow2(C0939R.string.mi_language, buildLanguage(iTrackInfo.getLanguage()));
                format = iTrackInfo.getFormat();
                if (format == null) {
                    if (trackType == 1) {
                    }
                }
                i4++;
                selectedTrack2 = i2;
                selectedTrack = i;
            }
        }
        AlertDialog.Builder builderBuildAlertDialogBuilder = tableLayoutBinder.buildAlertDialogBuilder();
        builderBuildAlertDialogBuilder.setTitle(C0939R.string.media_information);
        builderBuildAlertDialogBuilder.setNegativeButton(C0939R.string.close, (DialogInterface.OnClickListener) null);
        builderBuildAlertDialogBuilder.show();
    }

    private String buildResolution(int i, int i2, int i3, int i4) {
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append(" x ");
        sb.append(i2);
        if (i3 > 1 || i4 > 1) {
            sb.append("[");
            sb.append(i3);
            sb.append(":");
            sb.append(i4);
            sb.append("]");
        }
        return sb.toString();
    }

    private String buildTimeMilli(long j) {
        long j2 = j / 1000;
        long j3 = j2 / 3600;
        long j4 = (j2 % 3600) / 60;
        long j5 = j2 % 60;
        if (j <= 0) {
            return "--:--";
        }
        return j3 >= 100 ? String.format(Locale.US, "%d:%02d:%02d", Long.valueOf(j3), Long.valueOf(j4), Long.valueOf(j5)) : j3 > 0 ? String.format(Locale.US, "%02d:%02d:%02d", Long.valueOf(j3), Long.valueOf(j4), Long.valueOf(j5)) : String.format(Locale.US, "%02d:%02d", Long.valueOf(j4), Long.valueOf(j5));
    }

    private String buildTrackType(int i) {
        Context context = getContext();
        if (i == 1) {
            return context.getString(C0939R.string.TrackType_video);
        }
        if (i == 2) {
            return context.getString(C0939R.string.TrackType_audio);
        }
        if (i == 3) {
            return context.getString(C0939R.string.TrackType_timedtext);
        }
        if (i == 4) {
            return context.getString(C0939R.string.TrackType_subtitle);
        }
        if (i == 5) {
            return context.getString(C0939R.string.TrackType_metadata);
        }
        return context.getString(C0939R.string.TrackType_unknown);
    }

    private String buildLanguage(String str) {
        return TextUtils.isEmpty(str) ? "und" : str;
    }

    public ITrackInfo[] getTrackInfo() {
        IMediaPlayer iMediaPlayer = this.mMediaPlayer;
        if (iMediaPlayer == null) {
            return null;
        }
        return iMediaPlayer.getTrackInfo();
    }

    public void selectTrack(int i) {
        MediaPlayerCompat.selectTrack(this.mMediaPlayer, i);
    }

    public void deselectTrack(int i) {
        MediaPlayerCompat.deselectTrack(this.mMediaPlayer, i);
    }

    public int getSelectedTrack(int i) {
        return MediaPlayerCompat.getSelectedTrack(this.mMediaPlayer, i);
    }

    public int getVideoWidth() {
        return this.mVideoWidth;
    }

    public int getVideoHeight() {
        return this.mVideoHeight;
    }
}
