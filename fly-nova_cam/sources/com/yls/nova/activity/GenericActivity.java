package com.yls.nova.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.yls.nova.C0549R;
import com.yls.nova.base.BaseActivity;
import com.yls.nova.base.BaseFragment;
import com.yls.nova.fragment.InstructionsFragment;
import com.yls.nova.fragment.SettingsFragment;
import com.yls.nova.tools.IConstants;

/* loaded from: classes.dex */
public class GenericActivity extends BaseActivity implements View.OnClickListener {
    private BaseFragment fragment = null;
    private FrameLayout frameLayout;
    private View layoutTopBar;
    private TextView tvTopTitle;

    @Override // com.yls.nova.base.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0549R.layout.activity_generic);
        getWindow().addFlags(128);
        this.frameLayout = (FrameLayout) findViewById(C0549R.id.generic_frame_layout);
        ImageView imageView = (ImageView) findViewById(C0549R.id.bar_generic_left_btn);
        TextView textView = (TextView) findViewById(C0549R.id.bar_generic_center_tv);
        this.tvTopTitle = textView;
        textView.setKeepScreenOn(true);
        imageView.setOnClickListener(this);
        this.layoutTopBar = findViewById(C0549R.id.generic_top_bar);
        Intent intent = getIntent();
        if (intent != null) {
            switchSubFragment(intent.getStringExtra(IConstants.KEY_FRAGMENT_TAG));
        }
        setInserts();
    }

    private void setInserts() {
        ViewCompat.setOnApplyWindowInsetsListener(this.frameLayout, new OnApplyWindowInsetsListener() { // from class: com.yls.nova.activity.GenericActivity$$ExternalSyntheticLambda0
            @Override // androidx.core.view.OnApplyWindowInsetsListener
            public final WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                return GenericActivity.lambda$setInserts$0(view, windowInsetsCompat);
            }
        });
    }

    static /* synthetic */ WindowInsetsCompat lambda$setInserts$0(View view, WindowInsetsCompat windowInsetsCompat) {
        view.setPadding(0, 0, windowInsetsCompat.getInsets(WindowInsetsCompat.Type.navigationBars()).right, 0);
        return windowInsetsCompat;
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        finish();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view != null && view.getId() == C0549R.id.bar_generic_left_btn) {
            finish();
        }
    }

    private void switchSubFragment(String str) {
        String string;
        str.hashCode();
        if (str.equals(IConstants.FRAGMENT_TAG_SETTINGS)) {
            BaseFragment baseFragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(IConstants.FRAGMENT_TAG_SETTINGS);
            this.fragment = baseFragment;
            if (baseFragment == null) {
                this.fragment = new SettingsFragment();
            }
            string = getString(C0549R.string.item_system_settings);
        } else if (str.equals(IConstants.FRAGMENT_TAG_INSTRUCTIONS)) {
            BaseFragment baseFragment2 = (BaseFragment) getSupportFragmentManager().findFragmentByTag(IConstants.FRAGMENT_TAG_INSTRUCTIONS);
            this.fragment = baseFragment2;
            if (baseFragment2 == null) {
                this.fragment = new InstructionsFragment();
            }
            this.layoutTopBar.setVisibility(8);
            string = getString(C0549R.string.item_instructions);
        } else {
            string = null;
        }
        if (this.fragment == null || TextUtils.isEmpty(str)) {
            return;
        }
        changeFragment(C0549R.id.generic_frame_layout, this.fragment, str, true);
        if (TextUtils.isEmpty(string)) {
            return;
        }
        this.tvTopTitle.setText(string);
    }
}
