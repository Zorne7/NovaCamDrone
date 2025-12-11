package com.yls.nova.tools;

import android.graphics.Bitmap;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import kotlin.UByte;

/* loaded from: classes.dex */
public class BufChangeHex {
    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static long lastClickTime;

    public static char convertIntToAscii(int i) {
        if (i < 0 || i > 255) {
            return (char) 0;
        }
        return (char) i;
    }

    public static char[] encodeHex(byte[] bArr) {
        return encodeHex(bArr, true);
    }

    public static char[] encodeHex(byte[] bArr, boolean z) {
        return encodeHex(bArr, z ? DIGITS_LOWER : DIGITS_UPPER);
    }

    protected static char[] encodeHex(byte[] bArr, char[] cArr) {
        int length = bArr.length;
        char[] cArr2 = new char[(length << 1) + length];
        int i = 0;
        for (byte b : bArr) {
            cArr2[i] = cArr[(b & 240) >>> 4];
            int i2 = i + 2;
            cArr2[i + 1] = cArr[b & 15];
            i += 3;
            cArr2[i2] = ' ';
        }
        return cArr2;
    }

    protected static char[] encodeHex2(byte[] bArr, char[] cArr) {
        char[] cArr2 = new char[bArr.length << 1];
        int i = 0;
        for (byte b : bArr) {
            int i2 = i + 1;
            cArr2[i] = cArr[(b & 240) >>> 4];
            i += 2;
            cArr2[i2] = cArr[b & 15];
        }
        return cArr2;
    }

    public static String encodeHexStr(byte[] bArr) {
        return encodeHexStr(bArr, true);
    }

    public static String encodeHexStr(byte[] bArr, boolean z) {
        return encodeHexStr(bArr, z ? DIGITS_LOWER : DIGITS_UPPER);
    }

    protected static String encodeHexStr(byte[] bArr, char[] cArr) {
        return new String(encodeHex(bArr, cArr));
    }

    public static byte[] decodeHex(char[] cArr) {
        int length = cArr.length;
        if ((length & 1) != 0) {
            throw new RuntimeException("Odd number of characters.");
        }
        byte[] bArr = new byte[length >> 1];
        int i = 0;
        int i2 = 0;
        while (i < length) {
            int i3 = i + 1;
            int digit = (toDigit(cArr[i], i) << 4) | toDigit(cArr[i3], i3);
            i += 2;
            bArr[i2] = (byte) (digit & 255);
            i2++;
        }
        return bArr;
    }

    public static int bytesToInt(byte[] bArr) {
        int i;
        int i2;
        if (bArr.length == 2) {
            i = bArr[1] & UByte.MAX_VALUE;
            i2 = (bArr[0] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK;
        } else {
            i = (bArr[3] & UByte.MAX_VALUE) | (65280 & (bArr[2] << 8)) | ((bArr[1] << 16) & 16711680);
            i2 = (bArr[0] << 24) & ViewCompat.MEASURED_STATE_MASK;
        }
        return i2 | i;
    }

    public static final long getLong(byte[] bArr, boolean z) {
        if (bArr == null) {
            throw new IllegalArgumentException("byte array is null!");
        }
        if (bArr.length > 8) {
            throw new IllegalArgumentException("byte array size > 8 !");
        }
        long j = 0;
        if (z) {
            for (int length = bArr.length - 1; length >= 0; length--) {
                j = (j << 8) | (bArr[length] & UByte.MAX_VALUE);
            }
        } else {
            for (byte b : bArr) {
                j = (j << 8) | (b & UByte.MAX_VALUE);
            }
        }
        return j;
    }

    protected static int toDigit(char c, int i) {
        int iDigit = Character.digit(c, 16);
        if (iDigit != -1) {
            return iDigit;
        }
        throw new RuntimeException("Illegal hexadecimal character " + c + " at index " + i);
    }

    public static boolean byte2File(byte[] bArr, String str, String str2) throws Throwable {
        FileOutputStream fileOutputStream;
        File file;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            File file2 = new File(str);
            if (!file2.exists()) {
                file2.mkdirs();
            }
            if (str2 != null) {
                file = new File(str + File.separator + str2);
            } else {
                file = new File(str);
                if (file.exists()) {
                    file.delete();
                }
            }
            fileOutputStream = new FileOutputStream(file);
            try {
                try {
                    BufferedOutputStream bufferedOutputStream2 = new BufferedOutputStream(fileOutputStream);
                    try {
                        bufferedOutputStream2.write(bArr);
                        try {
                            bufferedOutputStream2.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            fileOutputStream.close();
                            return true;
                        } catch (IOException e2) {
                            e2.printStackTrace();
                            return true;
                        }
                    } catch (Exception e3) {
                        e = e3;
                        bufferedOutputStream = bufferedOutputStream2;
                        e.printStackTrace();
                        if (bufferedOutputStream != null) {
                            try {
                                bufferedOutputStream.close();
                            } catch (IOException e4) {
                                e4.printStackTrace();
                            }
                        }
                        if (fileOutputStream == null) {
                            return false;
                        }
                        try {
                            fileOutputStream.close();
                            return false;
                        } catch (IOException e5) {
                            e5.printStackTrace();
                            return false;
                        }
                    } catch (Throwable th) {
                        th = th;
                        bufferedOutputStream = bufferedOutputStream2;
                        if (bufferedOutputStream != null) {
                            try {
                                bufferedOutputStream.close();
                            } catch (IOException e6) {
                                e6.printStackTrace();
                            }
                        }
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                                throw th;
                            } catch (IOException e7) {
                                e7.printStackTrace();
                                throw th;
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Exception e8) {
                e = e8;
            }
        } catch (Exception e9) {
            e = e9;
            fileOutputStream = null;
        } catch (Throwable th3) {
            th = th3;
            fileOutputStream = null;
        }
    }

    public static byte[] Bitmap2Bytes(Bitmap bitmap) {
        if (bitmap.isRecycled()) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static String getVideoDuration(String str) {
        String str2 = null;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (str.contains("_")) {
            for (String str3 : str.split("_")) {
                if (str3.contains(".")) {
                    str2 = str3.split("\\.")[0];
                }
            }
        }
        return str2;
    }

    public static String getVideoThumb(String str, String str2) {
        File[] fileArrListFiles;
        if (str == null || str.isEmpty() || str2 == null || str2.isEmpty() || !str.contains(".")) {
            return null;
        }
        String strSubstring = str.substring(0, str.lastIndexOf("."));
        File file = new File(str2);
        if (!file.exists() || (fileArrListFiles = file.listFiles()) == null || fileArrListFiles.length <= 0) {
            return null;
        }
        for (File file2 : fileArrListFiles) {
            if (file2.getName().contains(strSubstring)) {
                return file2.getName();
            }
        }
        return null;
    }

    public static String combinDataStr(String str, String str2) {
        if (str == null || str.isEmpty()) {
            return "";
        }
        if (str2 == null || str2.isEmpty()) {
            return str;
        }
        try {
            String[] strArrSplit = str.split("\\.", 2);
            return strArrSplit[0] + "_" + str2 + "." + strArrSplit[1];
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static long readSDCard() {
        if (!"mounted".equals(Environment.getExternalStorageState())) {
            return -1L;
        }
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long blockSize = statFs.getBlockSize();
        statFs.getBlockCount();
        return statFs.getAvailableBlocks() * blockSize;
    }

    public static boolean isFastDoubleClick(int i) {
        long jCurrentTimeMillis = System.currentTimeMillis();
        long j = lastClickTime;
        if (j == 0) {
            lastClickTime = jCurrentTimeMillis;
            return false;
        }
        if (jCurrentTimeMillis - j >= i) {
            lastClickTime = jCurrentTimeMillis;
            return false;
        }
        lastClickTime = jCurrentTimeMillis;
        return true;
    }

    public static String byteToHex(byte b) {
        String hexString = Integer.toHexString(b & UByte.MAX_VALUE);
        if (hexString.length() >= 2) {
            return hexString;
        }
        return "0" + hexString;
    }
}
