package com.yls.nova.base;

import android.app.Application;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import com.yls.nova.tools.IConstants;
import com.yls.nova.tools.PreferencesHelper;
import com.yls.nova.utils.Dbug;

/* loaded from: classes.dex */
public class MainApplication extends Application implements IConstants {
    private static final String TAG = "MainApplication";
    private static MainApplication sInstance;
    private String appName = null;
    private boolean isGoogle = true;

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        this.appName = PreferencesHelper.getSharedPreferences(getApplicationContext()).getString(IConstants.KEY_ROOT_PATH_NAME, null);
        PackageManager packageManager = getPackageManager();
        if (TextUtils.isEmpty(this.appName)) {
            this.appName = getApplicationInfo().loadLabel(packageManager).toString();
        }
        Dbug.openOrCloseDebug(false);
    }

    public static MainApplication getInstance() {
        return sInstance;
    }

    @Override // android.app.Application, android.content.ComponentCallbacks
    public void onLowMemory() {
        super.onLowMemory();
    }

    public String getAppName() {
        return this.appName;
    }

    public boolean isGoogle() {
        return this.isGoogle;
    }
}
