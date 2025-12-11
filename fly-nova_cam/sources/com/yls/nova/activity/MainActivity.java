package com.yls.nova.activity;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.fragment.app.Fragment;
import com.cooingdv.bl60xmjpeg.UAV;
import com.yls.nova.C0549R;
import com.yls.nova.base.BaseActivity;
import com.yls.nova.base.BaseFragment;
import com.yls.nova.fragment.MenuFragment;
import com.yls.nova.socket.SocketClient;
import com.yls.nova.tools.IActions;
import com.yls.nova.tools.IConstants;
import com.yls.nova.tools.PreferencesHelper;
import com.yls.nova.utils.AppUtils;
import com.yls.nova.utils.LocalUtil;
import java.util.Locale;
import tv.danmaku.ijk.media.widget.IjkVideoView;

/* loaded from: classes.dex */
public class MainActivity extends BaseActivity {
    private AudioManager audioManager;
    private long mBackPressedTimes;

    @Override // com.yls.nova.base.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0549R.layout.activity_main);
        getWindow().addFlags(128);
        IjkVideoView ijkVideoView = (IjkVideoView) findViewById(C0549R.id.main_tx_video_view);
        this.audioManager = (AudioManager) getSystemService("audio");
        initUI();
        UAV.getInstance().init(this);
        UAV.getInstance().startServer();
        SocketClient socketClient = SocketClient.getInstance();
        socketClient.initVideoView(ijkVideoView);
        socketClient.start();
    }

    private void stopMusic() {
        AudioManager audioManager = this.audioManager;
        if (audioManager == null || !audioManager.isMusicActive()) {
            return;
        }
        this.audioManager.requestAudioFocus(null, 3, 2);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStart() {
        super.onStart();
        setLanguage();
        UAV.getInstance().onStart();
    }

    @Override // com.yls.nova.base.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        stopMusic();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStop() {
        super.onStop();
        UAV.getInstance().onStop();
    }

    @Override // com.yls.nova.base.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        release();
        super.onDestroy();
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    protected void onNewIntent(Intent intent) {
        Locale language = AppUtils.getLanguage(PreferencesHelper.getSharedPreferences(getApplicationContext()).getInt(IConstants.KEY_LANGUAGE_FLAG, LocalUtil.getLocaleLanguage(this)));
        if (language != null) {
            AppUtils.setLanguage(getApplicationContext(), language);
        }
        super.onNewIntent(intent);
        setIntent(intent);
        initUI();
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (!(((BaseFragment) getSupportFragmentManager().findFragmentById(C0549R.id.main_frame_layout)) instanceof MenuFragment)) {
            super.onBackPressed();
        } else if (this.mBackPressedTimes + 2000 > System.currentTimeMillis()) {
            sendBroadcast(new Intent(IActions.ACTION_QUIT_APP));
        } else {
            showToastShort(C0549R.string.double_tap_to_exit);
            this.mBackPressedTimes = System.currentTimeMillis();
        }
    }

    private void initUI() {
        String str;
        Fragment menuFragment = (BaseFragment) getSupportFragmentManager().findFragmentById(C0549R.id.main_frame_layout);
        if (menuFragment == null) {
            menuFragment = new MenuFragment();
            str = IConstants.FRAGMENT_TAG_MENU;
        } else {
            str = null;
        }
        if (!TextUtils.isEmpty(str)) {
            changeFragment(C0549R.id.main_frame_layout, menuFragment, str, true);
        } else {
            changeFragment(C0549R.id.main_frame_layout, menuFragment);
        }
    }
}
