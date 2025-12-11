package com.yls.nova.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import com.yls.nova.C0549R;

/* loaded from: classes.dex */
public class SoundUtils {
    private static MediaPlayer mMediaPlayer;

    public static void shootSound(Context context) throws IllegalStateException {
        AudioManager audioManager = (AudioManager) context.getSystemService("audio");
        if (audioManager.getStreamVolume(4) == 0) {
            audioManager.setStreamVolume(3, audioManager.getStreamMaxVolume(3), 4);
        }
        MediaPlayer mediaPlayerCreate = MediaPlayer.create(context, C0549R.raw.camera_click);
        mMediaPlayer = mediaPlayerCreate;
        mediaPlayerCreate.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.yls.nova.utils.SoundUtils.1
            @Override // android.media.MediaPlayer.OnCompletionListener
            public void onCompletion(MediaPlayer mediaPlayer) throws IllegalStateException {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
            }
        });
        mMediaPlayer.start();
    }

    public static void didiSound(boolean z, Context context) throws IllegalStateException {
        if (((AudioManager) context.getSystemService("audio")).getStreamVolume(4) != 0) {
            MediaPlayer mediaPlayerCreate = MediaPlayer.create(context, z ? C0549R.raw.btn_middle : C0549R.raw.btn_turn);
            mediaPlayerCreate.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.yls.nova.utils.SoundUtils.2
                @Override // android.media.MediaPlayer.OnCompletionListener
                public void onCompletion(MediaPlayer mediaPlayer) throws IllegalStateException {
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                    }
                }
            });
            mediaPlayerCreate.start();
        }
    }
}
