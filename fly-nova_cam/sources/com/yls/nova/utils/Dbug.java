package com.yls.nova.utils;

import android.util.Log;

/* loaded from: classes.dex */
public class Dbug {

    /* renamed from: a */
    private static boolean f71a = true;

    public static void openOrCloseDebug(boolean z) {
        f71a = z;
    }

    /* renamed from: v */
    public static void m418v(String str, String str2) {
        if (f71a) {
            Log.v(str, str2);
        }
    }

    /* renamed from: d */
    public static void m415d(String str, String str2) {
        if (f71a) {
            Log.d(str, str2);
        }
    }

    /* renamed from: i */
    public static void m417i(String str, String str2) {
        if (f71a) {
            Log.i(str, str2);
        }
    }

    /* renamed from: w */
    public static void m419w(String str, String str2) {
        if (f71a) {
            Log.w(str, str2);
        }
    }

    /* renamed from: e */
    public static void m416e(String str, String str2) {
        if (f71a) {
            Log.e(str, str2);
        }
    }
}
