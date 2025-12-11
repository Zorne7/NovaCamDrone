package com.cooingdv.bl60xmjpeg.utils;

import android.util.Log;

/* loaded from: classes.dex */
public class LogUtils {
    private static final String TAG = "uav-sdk";
    private static boolean isDebug = false;

    public static void isDebug(boolean z) {
    }

    private LogUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /* renamed from: i */
    public static void m401i(String str) {
        if (isDebug) {
            Log.i(TAG, str);
        }
    }

    /* renamed from: d */
    public static void m397d(String str) {
        if (isDebug) {
            Log.d(TAG, str);
        }
    }

    /* renamed from: e */
    public static void m399e(String str) {
        if (isDebug) {
            Log.e(TAG, str);
        }
    }

    /* renamed from: v */
    public static void m403v(String str) {
        if (isDebug) {
            Log.v(TAG, str);
        }
    }

    /* renamed from: i */
    public static void m402i(String str, String str2) {
        if (isDebug) {
            Log.i(str, str2);
        }
    }

    /* renamed from: d */
    public static void m398d(String str, String str2) {
        if (isDebug) {
            Log.d(str, str2);
        }
    }

    /* renamed from: e */
    public static void m400e(String str, String str2) {
        if (isDebug) {
            Log.e(str, str2);
        }
    }

    /* renamed from: v */
    public static void m404v(String str, String str2) {
        if (isDebug) {
            Log.v(str, str2);
        }
    }
}
