package com.yls.nova.interfaces;

/* loaded from: classes.dex */
public interface OnSocketListener {
    void onConnected();

    void onReceiver(byte[] bArr);

    void onVideo(byte[] bArr);
}
