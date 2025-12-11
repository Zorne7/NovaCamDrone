package com.yls.nova.utils;

/* loaded from: classes.dex */
public class RecognitionUtils {
    public static native int[] nativeGestureRecognition();

    static {
        System.loadLibrary("lib_gesture");
    }

    public static int[] gestureRecognition() {
        return nativeGestureRecognition();
    }
}
