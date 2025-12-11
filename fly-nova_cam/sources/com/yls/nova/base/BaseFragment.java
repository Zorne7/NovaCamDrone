package com.yls.nova.base;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.yls.nova.tools.IActions;
import com.yls.nova.tools.IConstants;

/* loaded from: classes.dex */
public class BaseFragment extends Fragment implements IConstants, IActions {
    public String TAG = getClass().getSimpleName();
    private Bundle bundle;
    public MainApplication mApplication;
    private Toast mToast;

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getActivity() != null) {
            this.mApplication = (MainApplication) getActivity().getApplication();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        if (getActivity() == null || this.mApplication != null) {
            return;
        }
        this.mApplication = (MainApplication) getActivity().getApplication();
    }

    public Bundle getBundle() {
        return this.bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public void showToast(String str, int i) {
        if (getActivity() == null || TextUtils.isEmpty(str) || i < 0) {
            return;
        }
        Toast toast = this.mToast;
        if (toast == null) {
            this.mToast = Toast.makeText(getActivity().getApplicationContext(), str, i);
        } else {
            toast.setText(str);
            this.mToast.setDuration(i);
        }
        this.mToast.setGravity(17, 0, 0);
        this.mToast.show();
    }

    public void showShortToast(String str) {
        showToast(str, 0);
    }

    public void showShortToast(int i) {
        showShortToast(getString(i));
    }

    public void showLongToast(String str) {
        showToast(str, 1);
    }

    public void showLongToast(int i) {
        showLongToast(getString(i));
    }
}
