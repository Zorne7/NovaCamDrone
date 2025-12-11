package com.cooingdv.bl60xmjpeg.callback;

/* loaded from: classes.dex */
public interface ReceiveDataCallback {
    void deviceStatus(byte[] bArr, long j);

    void picData(byte[] bArr, long j, byte b);

    void picMessage(byte[] bArr);
}
