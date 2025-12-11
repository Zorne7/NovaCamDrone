package tv.danmaku.ijk.media.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.View;
import android.widget.TableLayout;
import java.util.Locale;
import tv.danmaku.ijk.media.C0939R;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.MediaPlayerProxy;

/* loaded from: classes.dex */
public class InfoHudViewHolder {
    private static final int MSG_UPDATE_HUD = 1;
    private IMediaPlayer mMediaPlayer;
    private TableLayoutBinder mTableLayoutBinder;
    private SparseArray<View> mRowMap = new SparseArray<>();
    private long mLoadCost = 0;
    private long mSeekCost = 0;
    private Handler mHandler = new Handler() { // from class: tv.danmaku.ijk.media.widget.InfoHudViewHolder.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            IjkMediaPlayer ijkMediaPlayer;
            IMediaPlayer internalMediaPlayer;
            if (message.what == 1 && InfoHudViewHolder.this.mMediaPlayer != null) {
                if (InfoHudViewHolder.this.mMediaPlayer instanceof IjkMediaPlayer) {
                    ijkMediaPlayer = (IjkMediaPlayer) InfoHudViewHolder.this.mMediaPlayer;
                } else {
                    ijkMediaPlayer = ((InfoHudViewHolder.this.mMediaPlayer instanceof MediaPlayerProxy) && (internalMediaPlayer = ((MediaPlayerProxy) InfoHudViewHolder.this.mMediaPlayer).getInternalMediaPlayer()) != null && (internalMediaPlayer instanceof IjkMediaPlayer)) ? (IjkMediaPlayer) internalMediaPlayer : null;
                }
                if (ijkMediaPlayer == null) {
                    return;
                }
                int videoDecoder = ijkMediaPlayer.getVideoDecoder();
                if (videoDecoder == 1) {
                    InfoHudViewHolder.this.setRowValue(C0939R.string.vdec, "avcodec");
                } else if (videoDecoder != 2) {
                    InfoHudViewHolder.this.setRowValue(C0939R.string.vdec, "");
                } else {
                    InfoHudViewHolder.this.setRowValue(C0939R.string.vdec, "MediaCodec");
                }
                InfoHudViewHolder.this.setRowValue(C0939R.string.fps, String.format(Locale.US, "%.2f / %.2f", Float.valueOf(ijkMediaPlayer.getVideoDecodeFramesPerSecond()), Float.valueOf(ijkMediaPlayer.getVideoOutputFramesPerSecond())));
                long videoCachedDuration = ijkMediaPlayer.getVideoCachedDuration();
                long audioCachedDuration = ijkMediaPlayer.getAudioCachedDuration();
                long videoCachedBytes = ijkMediaPlayer.getVideoCachedBytes();
                long audioCachedBytes = ijkMediaPlayer.getAudioCachedBytes();
                long tcpSpeed = ijkMediaPlayer.getTcpSpeed();
                long bitRate = ijkMediaPlayer.getBitRate();
                long seekLoadDuration = ijkMediaPlayer.getSeekLoadDuration();
                long rtpSpeed = ijkMediaPlayer.getRtpSpeed();
                long rtpTrafficStatisticByteCount = ijkMediaPlayer.getRtpTrafficStatisticByteCount();
                long connectedTime = ijkMediaPlayer.getConnectedTime();
                InfoHudViewHolder.this.setRowValue(C0939R.string.v_cache, String.format(Locale.US, "%s, %s", InfoHudViewHolder.formatedDurationMilli(videoCachedDuration), InfoHudViewHolder.formatedSize(videoCachedBytes)));
                InfoHudViewHolder.this.setRowValue(C0939R.string.a_cache, String.format(Locale.US, "%s, %s", InfoHudViewHolder.formatedDurationMilli(audioCachedDuration), InfoHudViewHolder.formatedSize(audioCachedBytes)));
                InfoHudViewHolder.this.setRowValue(C0939R.string.load_cost, String.format(Locale.US, "%d ms", Long.valueOf(InfoHudViewHolder.this.mLoadCost)));
                InfoHudViewHolder.this.setRowValue(C0939R.string.seek_cost, String.format(Locale.US, "%d ms", Long.valueOf(InfoHudViewHolder.this.mSeekCost)));
                InfoHudViewHolder.this.setRowValue(C0939R.string.seek_load_cost, String.format(Locale.US, "%d ms", Long.valueOf(seekLoadDuration)));
                InfoHudViewHolder.this.setRowValue(C0939R.string.tcp_speed, String.format(Locale.US, "%s", InfoHudViewHolder.formatedSpeed(tcpSpeed, 1000L)));
                InfoHudViewHolder.this.setRowValue(C0939R.string.bit_rate, String.format(Locale.US, "%.2f kbs", Float.valueOf(bitRate / 1000.0f)));
                InfoHudViewHolder.this.setRowValue(C0939R.string.rtp_speed, String.format(Locale.US, "%s", InfoHudViewHolder.formatedSpeed(rtpSpeed, 1000L)));
                InfoHudViewHolder.this.setRowValue(C0939R.string.rtp_byte_count, String.format(Locale.US, "%s", InfoHudViewHolder.formatedSize(rtpTrafficStatisticByteCount)));
                InfoHudViewHolder.this.setRowValue(C0939R.string.connected_time, String.format(Locale.US, "%s", InfoHudViewHolder.formattedTime(connectedTime)));
                InfoHudViewHolder.this.mHandler.removeMessages(1);
                InfoHudViewHolder.this.mHandler.sendEmptyMessageDelayed(1, 500L);
            }
        }
    };

    public InfoHudViewHolder(Context context, TableLayout tableLayout) {
        this.mTableLayoutBinder = new TableLayoutBinder(context, tableLayout);
    }

    private void appendSection(int i) {
        this.mTableLayoutBinder.appendSection(i);
    }

    private void appendRow(int i) {
        this.mRowMap.put(i, this.mTableLayoutBinder.appendRow2(i, (String) null));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setRowValue(int i, String str) {
        View view = this.mRowMap.get(i);
        if (view == null) {
            this.mRowMap.put(i, this.mTableLayoutBinder.appendRow2(i, str));
        } else {
            this.mTableLayoutBinder.setValueText(view, str);
        }
    }

    public void setMediaPlayer(IMediaPlayer iMediaPlayer) {
        this.mMediaPlayer = iMediaPlayer;
        if (iMediaPlayer != null) {
            this.mHandler.sendEmptyMessageDelayed(1, 500L);
        } else {
            this.mHandler.removeMessages(1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String formatedDurationMilli(long j) {
        return j >= 1000 ? String.format(Locale.US, "%.2f sec", Float.valueOf(j / 1000.0f)) : String.format(Locale.US, "%d msec", Long.valueOf(j));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String formatedSpeed(long j, long j2) {
        if (j2 <= 0 || j <= 0) {
            return "0 B/s";
        }
        float f = (j * 1000.0f) / j2;
        return f >= 1000000.0f ? String.format(Locale.US, "%.2f MB/s", Float.valueOf((f / 1000.0f) / 1000.0f)) : f >= 1000.0f ? String.format(Locale.US, "%.1f KB/s", Float.valueOf(f / 1000.0f)) : String.format(Locale.US, "%d B/s", Long.valueOf((long) f));
    }

    public void updateLoadCost(long j) {
        this.mLoadCost = j;
    }

    public void updateSeekCost(long j) {
        this.mSeekCost = j;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String formatedSize(long j) {
        return j >= 100000 ? String.format(Locale.US, "%.2f MB", Float.valueOf((j / 1000.0f) / 1000.0f)) : j >= 100 ? String.format(Locale.US, "%.1f KB", Float.valueOf(j / 1000.0f)) : String.format(Locale.US, "%d B", Long.valueOf(j));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String formattedTime(long j) {
        long j2 = j / 1000;
        return j2 >= 3600 ? String.format(Locale.US, "%02d:%02d:%02d", Long.valueOf(j2 / 3600), Long.valueOf((j2 % 3600) / 60), Long.valueOf(j2 % 60)) : String.format(Locale.US, "%02d:%02d", Long.valueOf(j2 / 60), Long.valueOf(j2 % 60));
    }
}
