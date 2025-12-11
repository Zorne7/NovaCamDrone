package com.yls.nova.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import com.cooingdv.bl60xmjpeg.UAV;
import com.yls.nova.C0549R;
import com.yls.nova.activity.BrowseFileActivity;
import com.yls.nova.activity.GenericActivity;
import com.yls.nova.activity.MainActivity;
import com.yls.nova.base.BaseFragment;
import com.yls.nova.socket.SocketClient;
import com.yls.nova.tools.IActions;
import com.yls.nova.tools.IConstants;

/* loaded from: classes.dex */
public class MenuFragment extends BaseFragment implements View.OnClickListener {
    private MenuBroadcastReceiver mReceiver;
    private TextView tvVersion;
    private View view;

    private class MenuBroadcastReceiver extends BroadcastReceiver {
        private MenuBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (MenuFragment.this.getActivity() == null || context == null || intent == null) {
                return;
            }
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)) {
                return;
            }
            action.hashCode();
            if (action.equals(IActions.ACTION_SET_FAKE_RESOLUTION)) {
                int resolutionNumber = UAV.getInstance().getResolutionNumber();
                MenuFragment.this.tvVersion.setText("Ver:1.0.1." + resolutionNumber);
            }
        }
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(C0549R.layout.fragment_menu, viewGroup, false);
        this.view = viewInflate;
        ImageView imageView = (ImageView) viewInflate.findViewById(C0549R.id.main_play_btn);
        ImageView imageView2 = (ImageView) this.view.findViewById(C0549R.id.main_help_btn);
        ImageView imageView3 = (ImageView) this.view.findViewById(C0549R.id.main_setting_btn);
        ImageView imageView4 = (ImageView) this.view.findViewById(C0549R.id.main_media_btn);
        this.tvVersion = (TextView) this.view.findViewById(C0549R.id.main_version_tv);
        imageView.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);
        imageView4.setOnClickListener(this);
        return this.view;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (getActivity() != null) {
            ViewCompat.setOnApplyWindowInsetsListener(this.view, new OnApplyWindowInsetsListener() { // from class: com.yls.nova.fragment.MenuFragment$$ExternalSyntheticLambda0
                @Override // androidx.core.view.OnApplyWindowInsetsListener
                public final WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                    return MenuFragment.lambda$onActivityCreated$0(view, windowInsetsCompat);
                }
            });
            if (this.mReceiver == null) {
                this.mReceiver = new MenuBroadcastReceiver();
            }
            IntentFilter intentFilter = new IntentFilter(IActions.ACTION_SET_FAKE_RESOLUTION);
            if (Build.VERSION.SDK_INT >= 26) {
                getActivity().getApplicationContext().registerReceiver(this.mReceiver, intentFilter, 2);
            } else {
                getActivity().getApplicationContext().registerReceiver(this.mReceiver, intentFilter);
            }
        }
    }

    static /* synthetic */ WindowInsetsCompat lambda$onActivityCreated$0(View view, WindowInsetsCompat windowInsetsCompat) {
        Insets insets = windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars());
        view.setPadding(insets.left, 0, insets.right, 0);
        return windowInsetsCompat;
    }

    @Override // com.yls.nova.base.BaseFragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        if (UAV.getInstance().isActive() || SocketClient.getInstance().isActive()) {
            this.tvVersion.setText("Ver:1.0.1." + UAV.getInstance().getResolutionNumber());
            return;
        }
        this.tvVersion.setText("Ver:1.0.1");
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        if (getActivity() == null || this.mReceiver == null) {
            return;
        }
        getActivity().getApplicationContext().unregisterReceiver(this.mReceiver);
        this.mReceiver = null;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        switch (view.getId()) {
            case C0549R.id.main_help_btn /* 2131296485 */:
                handlerFuncs(IConstants.FRAGMENT_TAG_INSTRUCTIONS);
                break;
            case C0549R.id.main_media_btn /* 2131296486 */:
                Intent intent = new Intent(getActivity(), (Class<?>) BrowseFileActivity.class);
                intent.putExtra(IConstants.KEY_DIR_TYPE, IConstants.VIEW_FRONT);
                startActivity(intent);
                break;
            case C0549R.id.main_play_btn /* 2131296488 */:
                changeDeviceBLFragment();
                break;
            case C0549R.id.main_setting_btn /* 2131296489 */:
                handlerFuncs(IConstants.FRAGMENT_TAG_SETTINGS);
                break;
        }
    }

    private void changeDeviceBLFragment() {
        Fragment deviceBLFragment = (BaseFragment) getActivity().getSupportFragmentManager().findFragmentByTag(IConstants.FRAGMENT_BL_TAG_DEVICE);
        if (deviceBLFragment == null) {
            deviceBLFragment = new DeviceBLFragment();
        }
        ((MainActivity) getActivity()).changeFragment(C0549R.id.main_frame_layout, deviceBLFragment, IConstants.FRAGMENT_BL_TAG_DEVICE, true);
    }

    private void handlerFuncs(String str) {
        if (getActivity() == null || TextUtils.isEmpty(str)) {
            return;
        }
        Intent intent = new Intent(getActivity(), (Class<?>) GenericActivity.class);
        intent.putExtra(IConstants.KEY_FRAGMENT_TAG, str);
        startActivity(intent);
    }
}
