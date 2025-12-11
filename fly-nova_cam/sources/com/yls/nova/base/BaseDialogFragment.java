package com.yls.nova.base;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import com.yls.nova.tools.IActions;
import com.yls.nova.tools.IConstants;

/* loaded from: classes.dex */
public class BaseDialogFragment extends DialogFragment implements IConstants, IActions {
    private Bundle bundle;
    private Toast mToast;
    public String TAG = getClass().getSimpleName();
    private boolean isShown = false;

    @Override // android.app.DialogFragment
    public void show(FragmentManager fragmentManager, String str) {
        if (this.isShown || fragmentManager == null) {
            return;
        }
        FragmentTransaction fragmentTransactionBeginTransaction = fragmentManager.beginTransaction();
        fragmentTransactionBeginTransaction.add(this, str);
        fragmentTransactionBeginTransaction.commitAllowingStateLoss();
        this.isShown = true;
    }

    @Override // android.app.DialogFragment
    public void dismiss() {
        super.dismissAllowingStateLoss();
        this.isShown = false;
    }

    @Override // android.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        this.isShown = false;
    }

    @Override // android.app.DialogFragment, android.app.Fragment
    public void onDetach() {
        super.onDetach();
        this.isShown = false;
    }

    public boolean isShowing() {
        return this.isShown;
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

    public void changeFragment(int i, Fragment fragment, String str) {
        FragmentTransaction fragmentTransactionBeginTransaction;
        if (fragment == null || getActivity() == null) {
            return;
        }
        try {
            FragmentManager childFragmentManager = getChildFragmentManager();
            if (childFragmentManager == null || (fragmentTransactionBeginTransaction = childFragmentManager.beginTransaction()) == null) {
                return;
            }
            if (!TextUtils.isEmpty(str)) {
                fragmentTransactionBeginTransaction.replace(i, fragment, str);
            } else {
                fragmentTransactionBeginTransaction.replace(i, fragment);
            }
            fragmentTransactionBeginTransaction.addToBackStack(null);
            fragmentTransactionBeginTransaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
