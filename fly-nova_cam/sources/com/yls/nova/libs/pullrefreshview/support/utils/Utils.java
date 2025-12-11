package com.yls.nova.libs.pullrefreshview.support.utils;

/* loaded from: classes.dex */
public class Utils {
    public static final boolean isClassExists(String str) throws ClassNotFoundException {
        try {
            Class.forName(str);
            return true;
        } catch (ClassNotFoundException unused) {
            return false;
        }
    }
}
