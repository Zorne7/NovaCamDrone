package com.cooingdv.bl60xmjpeg.utils;

import kotlin.UByte;

/* loaded from: classes.dex */
public class BufChangeHex {
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static String byteToHex(byte b) {
        String hexString = Integer.toHexString(b & UByte.MAX_VALUE);
        if (hexString.length() >= 2) {
            return hexString;
        }
        return "0" + hexString;
    }

    public static String bytesToHex(byte[] bArr) {
        char[] cArr = new char[bArr.length * 2];
        for (int i = 0; i < bArr.length; i++) {
            byte b = bArr[i];
            int i2 = b & UByte.MAX_VALUE;
            int i3 = i * 2;
            char[] cArr2 = HEX_ARRAY;
            cArr[i3] = cArr2[i2 >>> 4];
            cArr[i3 + 1] = cArr2[b & 15];
        }
        return new String(cArr);
    }
}
