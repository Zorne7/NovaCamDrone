package com.yls.nova.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.yls.nova.C0549R;
import com.yls.nova.libs.VideoProgressToast;
import com.yls.nova.tools.TimeFormater;
import com.yls.nova.utils.AppUtils;
import java.io.File;

/* loaded from: classes.dex */
public class VideoViewActivity extends Activity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, View.OnTouchListener, MediaPlayer.OnCompletionListener {
    private static final int MSG_HIDE_BAR_UI = 177;
    private static final int MSG_UPDATE_PROGRESS = 178;
    private static final int MSG_VIDEO_PAUSE = 179;
    private static final int MSG_VIDEO_RESUME = 180;
    private static final long TIME_UPDATE = 200;
    private LinearLayout bottomBar;
    private boolean isFastPlay;
    private boolean isPausing;
    private boolean isPreparing;
    private ImageView ivFullscreen;
    private ImageView ivPlayOrPause;
    private float mLastMotionX;
    private float mLastMotionY;
    private int pauseTime;
    private SeekBar sbProgress;
    private int screenWidth;
    private int startX;
    private int startY;
    private int threshold;
    private RelativeLayout topBar;
    private TextView tvCurrentTime;
    private TextView tvTitle;
    private TextView tvTotalTime;
    private String videoPath;
    private VideoProgressToast videoProgressToast;
    private VideoView videoView;
    private int screenMode = 0;
    private boolean isClick = true;
    private Handler mHandler = new Handler(new Handler.Callback() { // from class: com.yls.nova.activity.VideoViewActivity.2
        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            if (message != null) {
                switch (message.what) {
                    case VideoViewActivity.MSG_HIDE_BAR_UI /* 177 */:
                        if (VideoViewActivity.this.screenMode != 0) {
                            VideoViewActivity.this.showOrHideBar();
                            break;
                        }
                        break;
                    case VideoViewActivity.MSG_UPDATE_PROGRESS /* 178 */:
                        int i = message.arg1;
                        if (i == VideoViewActivity.this.videoView.getDuration()) {
                            VideoViewActivity.this.ivPlayOrPause.setImageResource(C0549R.drawable.drawable_btn_play);
                        }
                        int iRound = Math.round(i / 1000.0f);
                        VideoViewActivity.this.sbProgress.setProgress(iRound);
                        VideoViewActivity.this.tvCurrentTime.setText(TimeFormater.getTimeFormatValue(iRound));
                        if (VideoViewActivity.this.isPlaying() && VideoViewActivity.this.mHandler != null) {
                            VideoViewActivity.this.mHandler.sendMessageDelayed(VideoViewActivity.this.mHandler.obtainMessage(VideoViewActivity.MSG_UPDATE_PROGRESS, VideoViewActivity.this.videoView.getCurrentPosition(), 0), VideoViewActivity.TIME_UPDATE);
                            break;
                        }
                        break;
                    case VideoViewActivity.MSG_VIDEO_PAUSE /* 179 */:
                        VideoViewActivity.this.ivPlayOrPause.setImageResource(C0549R.drawable.drawable_btn_play);
                        VideoViewActivity.this.videoView.requestFocus();
                        break;
                    case VideoViewActivity.MSG_VIDEO_RESUME /* 180 */:
                        VideoViewActivity.this.ivPlayOrPause.setImageResource(C0549R.drawable.drawable_btn_pause);
                        VideoViewActivity.this.videoView.requestFocus();
                        VideoViewActivity.this.tvCurrentTime.setText(VideoViewActivity.this.getString(C0549R.string.default_time_format));
                        break;
                }
            }
            return false;
        }
    });

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setFlags(1024, 1024);
        setContentView(C0549R.layout.activity_video_view);
        getWindow().addFlags(128);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(C0549R.id.video_layout);
        ImageView imageView = (ImageView) findViewById(C0549R.id.video_player_top_back_btn2);
        this.videoView = (VideoView) findViewById(C0549R.id.video_view_mp4);
        this.topBar = (RelativeLayout) findViewById(C0549R.id.video_player_top_layout2);
        this.bottomBar = (LinearLayout) findViewById(C0549R.id.video_player_bottom_bar2);
        this.tvTitle = (TextView) findViewById(C0549R.id.video_player_top_tv2);
        this.tvCurrentTime = (TextView) findViewById(C0549R.id.video_player_current_time2);
        this.tvTotalTime = (TextView) findViewById(C0549R.id.video_player_total_time2);
        this.sbProgress = (SeekBar) findViewById(C0549R.id.video_player_progress2);
        this.ivPlayOrPause = (ImageView) findViewById(C0549R.id.video_player_play_btn2);
        this.ivFullscreen = (ImageView) findViewById(C0549R.id.video_player_fullscreen_btn);
        this.videoView.setOnTouchListener(this);
        this.videoView.setOnCompletionListener(this);
        imageView.setOnClickListener(this);
        this.ivPlayOrPause.setOnClickListener(this);
        this.ivFullscreen.setOnClickListener(this);
        this.sbProgress.setOnSeekBarChangeListener(this);
        ViewCompat.setOnApplyWindowInsetsListener(relativeLayout, new OnApplyWindowInsetsListener() { // from class: com.yls.nova.activity.VideoViewActivity$$ExternalSyntheticLambda0
            @Override // androidx.core.view.OnApplyWindowInsetsListener
            public final WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                return VideoViewActivity.lambda$onCreate$0(view, windowInsetsCompat);
            }
        });
        this.screenWidth = AppUtils.getScreenWidth(this);
        this.threshold = AppUtils.dip2px(this, 20.0f);
        String stringExtra = getIntent().getStringExtra("path");
        this.videoPath = stringExtra;
        playVideo(stringExtra);
        this.videoView.start();
    }

    static /* synthetic */ WindowInsetsCompat lambda$onCreate$0(View view, WindowInsetsCompat windowInsetsCompat) {
        view.setPadding(0, 0, windowInsetsCompat.getInsets(WindowInsetsCompat.Type.navigationBars()).right, 0);
        return windowInsetsCompat;
    }

    @Override // android.app.Activity
    public void onResume() {
        super.onResume();
        if (isPausing()) {
            onResumeVideo();
        }
    }

    @Override // android.app.Activity
    public void onPause() {
        super.onPause();
        pauseVideo();
    }

    private void playOrPause() {
        if (isPreparing()) {
            return;
        }
        if (isPlaying()) {
            pauseVideo();
        } else if (isPausing()) {
            onResumeVideo();
        } else {
            if (TextUtils.isEmpty(this.videoPath)) {
                return;
            }
            playVideo(this.videoPath);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isPlaying() {
        VideoView videoView = this.videoView;
        return videoView != null && videoView.isPlaying();
    }

    private boolean isPausing() {
        return this.videoView != null && this.isPausing;
    }

    private boolean isPreparing() {
        return this.videoView != null && this.isPreparing;
    }

    private void playVideo(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.videoView.setVideoPath(str);
        this.isPreparing = true;
        this.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.yls.nova.activity.VideoViewActivity.1
            @Override // android.media.MediaPlayer.OnPreparedListener
            public void onPrepared(MediaPlayer mediaPlayer) {
                VideoViewActivity.this.videoView.start();
                VideoViewActivity.this.updatePlayingUI();
                if (VideoViewActivity.this.mHandler != null) {
                    VideoViewActivity.this.mHandler.sendMessage(VideoViewActivity.this.mHandler.obtainMessage(VideoViewActivity.MSG_UPDATE_PROGRESS, VideoViewActivity.this.videoView.getCurrentPosition(), 0));
                }
                VideoViewActivity.this.isPreparing = false;
                VideoViewActivity.this.isPausing = false;
            }
        });
    }

    private void pauseVideo() {
        if (this.videoView.isPlaying() && this.videoView.canPause()) {
            this.videoView.pause();
            this.isPausing = true;
            this.pauseTime = this.videoView.getCurrentPosition();
            Handler handler = this.mHandler;
            if (handler != null) {
                handler.removeMessages(MSG_UPDATE_PROGRESS);
                this.mHandler.sendEmptyMessage(MSG_VIDEO_PAUSE);
            }
        }
    }

    private void onResumeVideo() {
        if (isPausing()) {
            this.videoView.seekTo(this.pauseTime);
            this.videoView.start();
            this.isPausing = false;
            Handler handler = this.mHandler;
            if (handler != null) {
                handler.sendEmptyMessage(MSG_VIDEO_RESUME);
                Handler handler2 = this.mHandler;
                handler2.sendMessage(handler2.obtainMessage(MSG_UPDATE_PROGRESS, this.videoView.getCurrentPosition(), 0));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePlayingUI() {
        if (TextUtils.isEmpty(this.videoPath)) {
            return;
        }
        this.ivPlayOrPause.setImageResource(C0549R.drawable.drawable_btn_pause);
        this.videoView.requestFocus();
        this.sbProgress.setMax(this.videoView.getDuration() / 1000);
        this.sbProgress.setProgress(0);
        this.tvTitle.setText(formatTitle(this.videoPath));
        this.tvCurrentTime.setText(getString(C0549R.string.default_time_format));
        this.tvTotalTime.setText(TimeFormater.getTimeFormatValue(this.videoView.getDuration() / 1000));
    }

    private String formatTitle(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (!str.contains(File.separator)) {
            return str;
        }
        return str.split(File.separator)[r2.length - 1];
    }

    private void showFastForwardToast(float f) {
        int currentPosition = this.videoView.getCurrentPosition() + ((int) ((f / (this.screenWidth / 2)) * (this.videoView.getDuration() - this.videoView.getCurrentPosition())));
        if (currentPosition >= this.videoView.getDuration()) {
            currentPosition = this.videoView.getDuration() + NotificationManagerCompat.IMPORTANCE_UNSPECIFIED;
        }
        if (this.videoView.canSeekForward()) {
            this.videoView.seekTo(currentPosition);
            if (isPausing()) {
                this.pauseTime = currentPosition;
            }
            currentPosition /= 1000;
            this.sbProgress.setProgress(currentPosition);
            this.tvCurrentTime.setText(TimeFormater.getTimeFormatValue(currentPosition));
        }
        showVideoProgressToast(VideoProgressToast.FAST_FORWARD, TimeFormater.getTimeFormatValue(currentPosition));
    }

    private void showFastBackward(float f) {
        int currentPosition = this.videoView.getCurrentPosition() - ((int) ((f / (this.screenWidth / 2)) * (this.videoView.getDuration() - this.videoView.getCurrentPosition())));
        if (currentPosition <= 0) {
            currentPosition = 1000;
        }
        if (this.videoView.canSeekBackward()) {
            this.videoView.seekTo(currentPosition);
            if (isPausing()) {
                this.pauseTime = currentPosition;
            }
            currentPosition /= 1000;
            this.sbProgress.setProgress(currentPosition);
            this.tvCurrentTime.setText(TimeFormater.getTimeFormatValue(currentPosition));
        }
        showVideoProgressToast(VideoProgressToast.FAST_BACKWARD, TimeFormater.getTimeFormatValue(currentPosition));
    }

    private void showVideoProgressToast(int i, String str) {
        if (this.videoProgressToast == null) {
            this.videoProgressToast = new VideoProgressToast(this);
        }
        this.videoProgressToast.show(i, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showOrHideBar() {
        if (this.topBar.getVisibility() == 0) {
            this.topBar.setVisibility(8);
        } else {
            this.topBar.setVisibility(0);
        }
        if (this.bottomBar.getVisibility() == 0) {
            this.bottomBar.setVisibility(8);
        } else {
            this.bottomBar.setVisibility(0);
        }
        if (this.topBar.getVisibility() == 0 && this.bottomBar.getVisibility() == 0) {
            Handler handler = this.mHandler;
            if (handler != null) {
                handler.removeMessages(MSG_HIDE_BAR_UI);
                this.mHandler.sendEmptyMessageDelayed(MSG_HIDE_BAR_UI, 5000L);
                return;
            }
            return;
        }
        this.mHandler.removeMessages(MSG_HIDE_BAR_UI);
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x0064  */
    @Override // android.view.View.OnTouchListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        int action = motionEvent.getAction();
        if (action == 0) {
            this.mLastMotionX = x;
            this.mLastMotionY = y;
            this.startX = (int) x;
            this.startY = (int) y;
            this.isFastPlay = false;
        } else if (action == 1) {
            if (Math.abs(x - this.startX) > this.threshold || Math.abs(y - this.startY) > this.threshold) {
                this.isClick = false;
            }
            this.mLastMotionX = 0.0f;
            this.mLastMotionY = 0.0f;
            this.startX = 0;
            if (this.isClick && this.screenMode == 1) {
                showOrHideBar();
            }
            this.isClick = true;
            this.isFastPlay = false;
        } else if (action == 2) {
            float f = x - this.mLastMotionX;
            float f2 = y - this.mLastMotionY;
            float fAbs = Math.abs(f);
            float fAbs2 = Math.abs(f2);
            int i = this.threshold;
            if (fAbs <= i || fAbs2 <= i) {
                if (fAbs >= i || fAbs2 <= i) {
                    if (fAbs <= i || fAbs2 >= i) {
                        return true;
                    }
                    if (f <= 0.0f && x > this.screenWidth / 2) {
                        if (!this.isFastPlay) {
                            showFastForwardToast(fAbs);
                            this.isFastPlay = true;
                        }
                    } else if (f < 0.0f && x < this.screenWidth / 2 && !this.isFastPlay) {
                        showFastBackward(fAbs);
                        this.isFastPlay = true;
                    }
                }
                this.mLastMotionX = x;
                this.mLastMotionY = y;
            } else {
                if (fAbs >= fAbs2) {
                    if (f <= 0.0f) {
                        if (f < 0.0f) {
                            showFastBackward(fAbs);
                            this.isFastPlay = true;
                        }
                    }
                }
                this.mLastMotionX = x;
                this.mLastMotionY = y;
            }
        }
        return true;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        switch (view.getId()) {
            case C0549R.id.video_player_fullscreen_btn /* 2131296594 */:
                if (this.screenMode == 0) {
                    this.screenMode = 1;
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(this.videoView.getLayoutParams());
                    layoutParams.setMargins(0, 0, 0, 0);
                    layoutParams.addRule(14);
                    this.videoView.setLayoutParams(layoutParams);
                    showOrHideBar();
                    break;
                } else {
                    this.screenMode = 0;
                    RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(this.videoView.getLayoutParams());
                    layoutParams2.setMargins(0, AppUtils.dip2px(getApplicationContext(), 48.0f), 0, AppUtils.dip2px(getApplicationContext(), 60.0f));
                    layoutParams2.addRule(14);
                    this.videoView.setLayoutParams(layoutParams2);
                    break;
                }
            case C0549R.id.video_player_play_btn2 /* 2131296595 */:
                playOrPause();
                break;
            case C0549R.id.video_player_top_back_btn2 /* 2131296597 */:
                finish();
                break;
        }
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (isPlaying() || isPausing()) {
            int progress = seekBar.getProgress();
            int i = progress * 1000;
            this.videoView.seekTo(i);
            if (isPausing()) {
                this.pauseTime = i;
            }
            this.tvCurrentTime.setText(TimeFormater.getTimeFormatValue(progress));
            return;
        }
        this.sbProgress.setProgress(0);
        this.tvCurrentTime.setText(getString(C0549R.string.default_time_format));
    }

    @Override // android.media.MediaPlayer.OnCompletionListener
    public void onCompletion(MediaPlayer mediaPlayer) {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeMessages(MSG_UPDATE_PROGRESS);
            Handler handler2 = this.mHandler;
            handler2.sendMessageDelayed(handler2.obtainMessage(MSG_UPDATE_PROGRESS, this.videoView.getDuration(), 0), TIME_UPDATE);
        }
        this.pauseTime = 0;
        this.isPausing = true;
    }
}
