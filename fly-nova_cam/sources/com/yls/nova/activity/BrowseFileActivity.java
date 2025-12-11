package com.yls.nova.activity;

import android.content.Intent;
import android.os.Bundle;
import com.yls.nova.C0549R;
import com.yls.nova.base.BaseActivity;
import com.yls.nova.base.BaseFragment;
import com.yls.nova.fragment.BrowseSelectFragment;
import com.yls.nova.tools.IConstants;

/* loaded from: classes.dex */
public class BrowseFileActivity extends BaseActivity {
    @Override // com.yls.nova.base.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        String stringExtra;
        super.onCreate(bundle);
        setContentView(C0549R.layout.activity_browse_file);
        getWindow().addFlags(128);
        Intent intent = getIntent();
        if (intent == null) {
            stringExtra = IConstants.VIEW_FRONT;
        } else {
            stringExtra = intent.getStringExtra(IConstants.KEY_DIR_TYPE);
        }
        BaseFragment browseSelectFragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(IConstants.FRAGMENT_TAG_BROWSE_SELECT);
        if (browseSelectFragment == null) {
            browseSelectFragment = new BrowseSelectFragment();
        }
        Bundle bundle2 = new Bundle();
        bundle2.putString(IConstants.KEY_DIR_TYPE, stringExtra);
        browseSelectFragment.setBundle(bundle2);
        changeFragment(C0549R.id.browse_file_frame_layout, browseSelectFragment, IConstants.FRAGMENT_TAG_BROWSE_SELECT, true);
    }

    @Override // com.yls.nova.base.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        setLanguage();
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (((BaseFragment) getSupportFragmentManager().findFragmentById(C0549R.id.browse_file_frame_layout)) instanceof BrowseSelectFragment) {
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
