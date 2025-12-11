package com.yls.nova.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import com.yls.nova.beans.FileInfo;
import com.yls.nova.tools.BufChangeHex;
import com.yls.nova.tools.IConstants;
import com.yls.nova.tools.PreferencesHelper;
import com.yls.nova.tools.TimeFormater;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public class AppUtils implements IConstants {
    private static final String TAG = "AppUtils";

    public static int dip2px(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        return ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getWidth();
    }

    public static int judgeFileType(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        if (str.endsWith(".png") || str.endsWith(".PNG") || str.endsWith(".JPEG") || str.endsWith(".jpeg") || str.endsWith(".jpg") || str.endsWith(".JPG")) {
            return 1;
        }
        return (str.endsWith(".mov") || str.endsWith(".MOV") || str.endsWith(".mp4") || str.endsWith(".MP4") || str.endsWith(".avi") || str.endsWith(".AVI")) ? 2 : 0;
    }

    public static List<FileInfo> getAllLocalFile(String str, String str2, boolean z) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        try {
            File file = new File(str);
            if (file.exists() && file.isDirectory()) {
                for (File file2 : file.listFiles()) {
                    if (file2.isDirectory()) {
                        for (File file3 : file2.listFiles()) {
                            if (file3.getName().equals(str2)) {
                                arrayList.addAll(getLocalFileInfo(file3.getPath(), z));
                            }
                        }
                    } else {
                        FileInfo fileInfo = new FileInfo();
                        if (z) {
                            fileInfo.setFilename(getFileName(file2.getName()));
                            fileInfo.setDirectory(false);
                            fileInfo.setSize(file2.length());
                            fileInfo.setCreateDate(getFileCreateTime(file2.getName()));
                            fileInfo.setPath(file2.getAbsolutePath());
                            fileInfo.setFileType(IConstants.BROWSE_LOCAL_MODE);
                        } else {
                            String formatedDateTime = TimeFormater.getFormatedDateTime(TimeFormater.yyyyMMddHHmmss, file2.lastModified());
                            if (formatedDateTime == null) {
                                formatedDateTime = "2015-08-07 15:34:26";
                            }
                            fileInfo.setFilename(file2.getName());
                            fileInfo.setDirectory(file2.isDirectory());
                            fileInfo.setSize(file2.length());
                            fileInfo.setCreateDate(formatedDateTime);
                            fileInfo.setPath(file2.getAbsolutePath());
                            fileInfo.setFileType(IConstants.BROWSE_RECORD_MODE);
                        }
                        arrayList.add(fileInfo);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.sort(arrayList, new Comparator<FileInfo>() { // from class: com.yls.nova.utils.AppUtils.1
            @Override // java.util.Comparator
            public int compare(FileInfo fileInfo2, FileInfo fileInfo3) {
                return fileInfo3.getFilename().compareTo(fileInfo2.getFilename());
            }
        });
        return arrayList;
    }

    public static List<FileInfo> getLocalFileInfo(String str, boolean z) {
        File file;
        ArrayList arrayList = new ArrayList();
        if (str != null && !str.isEmpty()) {
            try {
                File file2 = new File(str);
                if (file2.exists() && file2.isDirectory()) {
                    File[] fileArrListFiles = file2.listFiles();
                    HashMap map = new HashMap();
                    ArrayList arrayList2 = new ArrayList();
                    if (fileArrListFiles != null && fileArrListFiles.length > 0) {
                        for (File file3 : fileArrListFiles) {
                            if (file3.isFile()) {
                                if (z) {
                                    String name = file3.getName();
                                    if (!TextUtils.isEmpty(name)) {
                                        arrayList2.add(name);
                                        map.put(name, file3);
                                    }
                                } else {
                                    String str2 = file3.lastModified() + "";
                                    arrayList2.add(str2);
                                    map.put(str2, file3);
                                }
                            }
                        }
                        for (String str3 : descSort((String[]) arrayList2.toArray(new String[arrayList2.size()]))) {
                            if (!TextUtils.isEmpty(str3) && (file = (File) map.get(str3)) != null) {
                                FileInfo fileInfo = new FileInfo();
                                if (z) {
                                    if (file.isFile() && str3.equals(file.getName())) {
                                        fileInfo.setFilename(getFileName(file.getName()));
                                        fileInfo.setDirectory(false);
                                        fileInfo.setSize(file.length());
                                        fileInfo.setCreateDate(getFileCreateTime(file.getName()));
                                        fileInfo.setPath(file.getAbsolutePath());
                                        fileInfo.setFileType(IConstants.BROWSE_LOCAL_MODE);
                                    }
                                } else if (file.isFile()) {
                                    if (str3.equals(file.lastModified() + "")) {
                                        String formatedDateTime = TimeFormater.getFormatedDateTime(TimeFormater.yyyyMMddHHmmss, file.lastModified());
                                        if (formatedDateTime == null) {
                                            formatedDateTime = "2015-08-07 15:34:26";
                                        }
                                        fileInfo.setFilename(file.getName());
                                        fileInfo.setDirectory(false);
                                        fileInfo.setSize(file.length());
                                        fileInfo.setCreateDate(formatedDateTime);
                                        fileInfo.setPath(file.getAbsolutePath());
                                        fileInfo.setFileType(IConstants.BROWSE_RECORD_MODE);
                                    }
                                }
                                arrayList.add(fileInfo);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }

    private static String[] descSort(String[] strArr) {
        if (strArr != null && strArr.length != 0) {
            for (int i = 0; i < strArr.length; i++) {
                int i2 = 0;
                while (i2 < (strArr.length - i) - 1) {
                    int i3 = i2 + 1;
                    if (strArr[i2].compareTo(strArr[i3]) < 0) {
                        String str = strArr[i2];
                        strArr[i2] = strArr[i3];
                        strArr[i3] = str;
                    }
                    i2 = i3;
                }
            }
        }
        return strArr;
    }

    private static String getFileName(String str) {
        String strSubstring;
        if (!TextUtils.isEmpty(str)) {
            if (!str.contains(".")) {
                strSubstring = "";
            } else {
                strSubstring = str.substring(str.indexOf("."));
            }
            String str2 = str.contains("_") ? str.split("_")[0] : null;
            if (!TextUtils.isEmpty(str2)) {
                return str2 + strSubstring;
            }
        }
        return null;
    }

    private static String getFileCreateTime(String str) {
        if (TextUtils.isEmpty(str) || !str.contains("_")) {
            return null;
        }
        String[] strArrSplit = str.split("_");
        if (strArrSplit[1].length() < 14) {
            return null;
        }
        String str2 = strArrSplit[1];
        return str2.substring(0, 4) + "-" + str2.substring(4, 6) + "-" + str2.substring(6, 8) + " " + str2.substring(8, 10) + ":" + str2.substring(10, 12) + ":" + str2.substring(12, 14);
    }

    /* JADX WARN: Code restructure failed: missing block: B:100:0x0212, code lost:
    
        com.yls.nova.utils.Dbug.m419w(r4, "delete file ok");
     */
    /* JADX WARN: Code restructure failed: missing block: B:102:0x0216, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x0218, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:105:0x0219, code lost:
    
        r5 = r1;
        r1 = r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:106:0x021d, code lost:
    
        r4 = r35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:107:0x021f, code lost:
    
        r5 = r1;
        r1 = r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:108:0x0223, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:109:0x0224, code lost:
    
        r4 = r35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:110:0x0226, code lost:
    
        r1 = r4;
        r3 = "delete local file!";
        r16 = r30;
     */
    /* JADX WARN: Code restructure failed: missing block: B:111:0x022d, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:112:0x022e, code lost:
    
        r1 = r35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:206:0x03f8, code lost:
    
        r16.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:208:0x03fd, code lost:
    
        r26.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:215:0x0417, code lost:
    
        com.yls.nova.utils.Dbug.m419w(r1, r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:302:?, code lost:
    
        return r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:304:?, code lost:
    
        return r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x012e, code lost:
    
        r1 = r4;
        r30 = r10;
        r5 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x0170, code lost:
    
        r0 = (int) r12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x0171, code lost:
    
        r1 = new byte[r0];
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x017c, code lost:
    
        if ((r4 + 4) > 307200) goto L80;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x0181, code lost:
    
        java.lang.System.arraycopy(r9, r11 + 4, r1, 0, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x0185, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x0186, code lost:
    
        r4 = r0;
        r3 = "delete local file!";
        r16 = r30;
        r1 = r35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x018f, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x0190, code lost:
    
        r5 = r24;
        r3 = "delete local file!";
        r16 = r30;
        r1 = r35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x019a, code lost:
    
        r0 = r39.getFilename();
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x01a2, code lost:
    
        if (android.text.TextUtils.isEmpty(r0) != false) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x01a8, code lost:
    
        if (r0.contains(".") == false) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x01aa, code lost:
    
        r0 = r0.substring(0, r0.lastIndexOf("."));
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x01b4, code lost:
    
        r0 = "";
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x01b6, code lost:
    
        r0 = r0 + "_" + r7 + ".jpg";
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x01cf, code lost:
    
        if (r40.contains("/") == false) goto L90;
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x01d1, code lost:
    
        r16 = r40.substring(0, r40.lastIndexOf("/"));
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x01da, code lost:
    
        r3 = r16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x01dc, code lost:
    
        r1 = com.yls.nova.tools.BufChangeHex.byte2File(r1, r3, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x01e0, code lost:
    
        if (r1 != false) goto L106;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x01e2, code lost:
    
        r4 = r35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x01e6, code lost:
    
        com.yls.nova.utils.Dbug.m415d(r4, "save image failed!");
        r5 = new java.io.File(r3 + "/" + r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x0204, code lost:
    
        if (r5.exists() == false) goto L107;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x020a, code lost:
    
        if (r5.isFile() == false) goto L107;
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x0210, code lost:
    
        if (r5.delete() == false) goto L107;
     */
    /* JADX WARN: Not initialized variable reg: 5, insn: 0x044a: RETURN (r5 I:boolean) A[SYNTHETIC], block:B:299:? */
    /* JADX WARN: Removed duplicated region for block: B:206:0x03f8 A[Catch: IOException -> 0x03b8, TRY_ENTER, TryCatch #1 {IOException -> 0x03b8, blocks: (B:206:0x03f8, B:208:0x03fd, B:209:0x0400, B:211:0x040b, B:213:0x0411, B:215:0x0417, B:179:0x0394, B:181:0x03a5, B:183:0x03ab, B:185:0x03b1), top: B:239:0x0037 }] */
    /* JADX WARN: Removed duplicated region for block: B:208:0x03fd A[Catch: IOException -> 0x03b8, TryCatch #1 {IOException -> 0x03b8, blocks: (B:206:0x03f8, B:208:0x03fd, B:209:0x0400, B:211:0x040b, B:213:0x0411, B:215:0x0417, B:179:0x0394, B:181:0x03a5, B:183:0x03ab, B:185:0x03b1), top: B:239:0x0037 }] */
    /* JADX WARN: Removed duplicated region for block: B:211:0x040b A[Catch: IOException -> 0x03b8, TryCatch #1 {IOException -> 0x03b8, blocks: (B:206:0x03f8, B:208:0x03fd, B:209:0x0400, B:211:0x040b, B:213:0x0411, B:215:0x0417, B:179:0x0394, B:181:0x03a5, B:183:0x03ab, B:185:0x03b1), top: B:239:0x0037 }] */
    /* JADX WARN: Removed duplicated region for block: B:224:0x0427 A[Catch: IOException -> 0x0423, TryCatch #23 {IOException -> 0x0423, blocks: (B:220:0x041f, B:224:0x0427, B:225:0x042a, B:227:0x0435, B:229:0x043b, B:231:0x0441), top: B:248:0x041f }] */
    /* JADX WARN: Removed duplicated region for block: B:227:0x0435 A[Catch: IOException -> 0x0423, TryCatch #23 {IOException -> 0x0423, blocks: (B:220:0x041f, B:224:0x0427, B:225:0x042a, B:227:0x0435, B:229:0x043b, B:231:0x0441), top: B:248:0x041f }] */
    /* JADX WARN: Removed duplicated region for block: B:248:0x041f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:271:0x0369 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:281:0x0082 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:292:? A[ADDED_TO_REGION, REMOVE, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:300:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static boolean getRecordVideoThumb(FileInfo fileInfo, String str) throws Throwable {
        String str2;
        Throwable th;
        FileOutputStream fileOutputStream;
        boolean z;
        boolean z2;
        File file;
        Throwable th2;
        FileInputStream fileInputStream;
        FileInputStream fileInputStream2;
        FileOutputStream fileOutputStream2;
        int i;
        int i2;
        long j;
        long j2;
        int i3;
        boolean zByte2File;
        int i4;
        byte[] bArr;
        byte[] bArr2;
        byte[] bArr3;
        byte[] bArr4;
        byte[] bArr5;
        int i5;
        long j3;
        long jRound;
        String strSubstring;
        int i6;
        String str3;
        byte[] bArr6;
        int i7;
        Bitmap frameAtTime;
        String strSubstring2;
        FileInfo fileInfo2 = fileInfo;
        String str4 = "delete local file!";
        String str5 = TAG;
        if (fileInfo2 == null || TextUtils.isEmpty(str)) {
            Dbug.m416e(TAG, "getRecordVideoThumb parameter is empty!");
            return false;
        }
        String path = fileInfo.getPath();
        byte[] bArr7 = new byte[1024];
        byte[] bArr8 = new byte[307200];
        byte[] bArr9 = new byte[4];
        byte[] bArr10 = new byte[4];
        byte[] bArr11 = new byte[4];
        byte[] bArr12 = new byte[4];
        byte[] bArr13 = new byte[4];
        File file2 = new File(path);
        if (!file2.exists()) {
            return false;
        }
        FileInputStream fileInputStream3 = null;
        try {
            try {
                fileInputStream = new FileInputStream(file2);
                try {
                    fileOutputStream2 = new FileOutputStream(str, true);
                    i = -1;
                    i2 = -1;
                    j = 0;
                    j2 = 0;
                    i3 = 0;
                    zByte2File = false;
                } catch (IOException e) {
                    e = e;
                    str2 = TAG;
                    fileOutputStream = null;
                    fileInputStream3 = fileInputStream;
                    z2 = false;
                    try {
                        e.printStackTrace();
                        if (fileInputStream3 != null) {
                        }
                        if (fileOutputStream != null) {
                        }
                        File file3 = new File(str);
                        if (file3.exists()) {
                        }
                    } catch (Throwable th3) {
                        th2 = th3;
                        th = th2;
                        if (fileInputStream3 != null) {
                        }
                        if (fileOutputStream != null) {
                        }
                        file = new File(str);
                        if (file.exists()) {
                            throw th;
                        }
                        throw th;
                    }
                } catch (Throwable th4) {
                    str2 = TAG;
                    fileInputStream2 = fileInputStream;
                    th = th4;
                    fileOutputStream = null;
                }
            } catch (IOException e2) {
                e = e2;
                str2 = TAG;
                fileOutputStream = null;
            } catch (Throwable th5) {
                str2 = TAG;
                th = th5;
                fileOutputStream = null;
            }
            while (true) {
                try {
                    i4 = fileInputStream.read(bArr7);
                } catch (IOException e3) {
                    e = e3;
                    str2 = str5;
                    fileOutputStream = fileOutputStream2;
                    fileInputStream2 = fileInputStream;
                    str4 = "delete local file!";
                    z2 = zByte2File;
                } catch (Throwable th6) {
                    str2 = str5;
                    fileOutputStream = fileOutputStream2;
                    fileInputStream2 = fileInputStream;
                    str4 = "delete local file!";
                    th = th6;
                }
                if (i4 == i) {
                    str2 = str5;
                    fileOutputStream = fileOutputStream2;
                    fileInputStream2 = fileInputStream;
                    break;
                }
                try {
                    fileOutputStream2.write(bArr7, 0, i4);
                    fileOutputStream2.flush();
                    int i8 = i3 + i4;
                    fileOutputStream = fileOutputStream2;
                    if (i8 <= 307200) {
                        try {
                            System.arraycopy(bArr7, 0, bArr8, i3, i4);
                            if (i8 >= 306176) {
                                if (i8 < 30720 || j != 0) {
                                    bArr = bArr7;
                                    bArr2 = bArr9;
                                    bArr3 = bArr10;
                                    bArr4 = bArr11;
                                    bArr5 = bArr12;
                                    i5 = i2;
                                    j3 = j;
                                    jRound = j2;
                                } else {
                                    System.arraycopy(bArr8, 32, bArr9, 0, 4);
                                    System.arraycopy(bArr8, 48, bArr10, 0, 4);
                                    System.arraycopy(bArr8, 64, bArr11, 0, 4);
                                    System.arraycopy(bArr8, 68, bArr12, 0, 4);
                                    long j4 = BufChangeHex.getLong(bArr10, true);
                                    long j5 = BufChangeHex.getLong(bArr9, true);
                                    bArr = bArr7;
                                    long j6 = BufChangeHex.getLong(bArr11, true);
                                    bArr2 = bArr9;
                                    bArr3 = bArr10;
                                    long j7 = BufChangeHex.getLong(bArr12, true);
                                    if (j5 > 0 && 1000000 / j5 > 0) {
                                        if (j4 % (1000000 / j5) == 0) {
                                            j2 = j4 / (1000000 / j5);
                                        } else {
                                            j2 = (j4 / (1000000 / j5)) + 1;
                                        }
                                    }
                                    bArr4 = bArr11;
                                    bArr5 = bArr12;
                                    long j8 = j2;
                                    fileInfo2.setWidth(j6);
                                    fileInfo2.setHeight(j7);
                                    fileInfo2.setTotalTime(j8);
                                    int i9 = 3;
                                    while (true) {
                                        if (i9 < 307200) {
                                            if (bArr8[i9 - 3] == 48 && bArr8[i9 - 2] == 48 && bArr8[i9 - 1] == 100 && bArr8[i9] == 99) {
                                                i2 = i9 + 1;
                                                break;
                                            }
                                            i9++;
                                        } else {
                                            break;
                                        }
                                    }
                                    int i10 = i2;
                                    if (-1 != i10) {
                                        System.arraycopy(bArr8, i10, bArr13, 0, 4);
                                        j = BufChangeHex.getLong(bArr13, true);
                                    }
                                    if (j == 0 || j8 == 0) {
                                        break;
                                    }
                                    i5 = i10;
                                    jRound = j8;
                                    j3 = j;
                                }
                                byte[] bArr14 = bArr4;
                                byte[] bArr15 = bArr13;
                                fileInputStream2 = fileInputStream;
                                strSubstring = "";
                                String str6 = path;
                                if (j3 > 0) {
                                    String str7 = str5;
                                    i6 = i8;
                                    long j9 = i5 + j3;
                                    if (i8 >= j9 + 1024) {
                                        break;
                                    }
                                    str3 = "save image failed!";
                                    str2 = str7;
                                } else {
                                    i6 = i8;
                                    str3 = "save image failed!";
                                    str2 = str5;
                                }
                                if (jRound == 0) {
                                    try {
                                        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                                        try {
                                            try {
                                                long j10 = jRound;
                                                try {
                                                    mediaMetadataRetriever.setDataSource(new File(str6).getAbsolutePath());
                                                    frameAtTime = mediaMetadataRetriever.getFrameAtTime();
                                                    str6 = str6;
                                                } catch (Exception e4) {
                                                    e = e4;
                                                    str6 = str6;
                                                }
                                                try {
                                                    jRound = Math.round(Long.parseLong(mediaMetadataRetriever.extractMetadata(9)) / 1000.0f);
                                                    bArr6 = bArr8;
                                                    try {
                                                        String filename = fileInfo.getFilename();
                                                        if (TextUtils.isEmpty(filename) || !filename.contains(".")) {
                                                            i7 = i5;
                                                            strSubstring2 = "";
                                                        } else {
                                                            i7 = i5;
                                                            try {
                                                                strSubstring2 = filename.substring(0, filename.lastIndexOf("."));
                                                            } catch (Exception e5) {
                                                                e = e5;
                                                                e.printStackTrace();
                                                                mediaMetadataRetriever.release();
                                                                str5 = str2;
                                                                j = j3;
                                                                i2 = i7;
                                                                bArr11 = bArr14;
                                                                bArr12 = bArr5;
                                                                bArr13 = bArr15;
                                                                fileInputStream = fileInputStream2;
                                                                path = str6;
                                                                bArr9 = bArr2;
                                                                bArr10 = bArr3;
                                                                bArr8 = bArr6;
                                                                i3 = i6;
                                                                i = -1;
                                                                fileInfo2 = fileInfo;
                                                                j2 = jRound;
                                                                fileOutputStream2 = fileOutputStream;
                                                                bArr7 = bArr;
                                                            }
                                                        }
                                                        String str8 = strSubstring2 + "_" + jRound + ".jpg";
                                                        String strSubstring3 = str.contains("/") ? str.substring(0, str.lastIndexOf("/")) : "";
                                                        zByte2File = BufChangeHex.byte2File(BufChangeHex.Bitmap2Bytes(frameAtTime), strSubstring3, str8);
                                                        if (!zByte2File) {
                                                            Dbug.m415d(str2, str3);
                                                            File file4 = new File(strSubstring3 + "/" + str8);
                                                            if (!file4.exists() || !file4.isFile() || !file4.delete()) {
                                                                break;
                                                            }
                                                            Dbug.m419w(str2, "delete file ok");
                                                            break;
                                                        }
                                                        break;
                                                    } catch (Exception e6) {
                                                        e = e6;
                                                        i7 = i5;
                                                        e.printStackTrace();
                                                        mediaMetadataRetriever.release();
                                                        str5 = str2;
                                                        j = j3;
                                                        i2 = i7;
                                                        bArr11 = bArr14;
                                                        bArr12 = bArr5;
                                                        bArr13 = bArr15;
                                                        fileInputStream = fileInputStream2;
                                                        path = str6;
                                                        bArr9 = bArr2;
                                                        bArr10 = bArr3;
                                                        bArr8 = bArr6;
                                                        i3 = i6;
                                                        i = -1;
                                                        fileInfo2 = fileInfo;
                                                        j2 = jRound;
                                                        fileOutputStream2 = fileOutputStream;
                                                        bArr7 = bArr;
                                                    }
                                                } catch (Exception e7) {
                                                    e = e7;
                                                    bArr6 = bArr8;
                                                    i7 = i5;
                                                    jRound = j10;
                                                    e.printStackTrace();
                                                    mediaMetadataRetriever.release();
                                                    str5 = str2;
                                                    j = j3;
                                                    i2 = i7;
                                                    bArr11 = bArr14;
                                                    bArr12 = bArr5;
                                                    bArr13 = bArr15;
                                                    fileInputStream = fileInputStream2;
                                                    path = str6;
                                                    bArr9 = bArr2;
                                                    bArr10 = bArr3;
                                                    bArr8 = bArr6;
                                                    i3 = i6;
                                                    i = -1;
                                                    fileInfo2 = fileInfo;
                                                    j2 = jRound;
                                                    fileOutputStream2 = fileOutputStream;
                                                    bArr7 = bArr;
                                                }
                                            } finally {
                                                mediaMetadataRetriever.release();
                                            }
                                        } catch (Exception e8) {
                                            e = e8;
                                            bArr6 = bArr8;
                                        }
                                    } catch (IOException e9) {
                                        e = e9;
                                        z2 = zByte2File;
                                        str4 = "delete local file!";
                                        fileInputStream3 = fileInputStream2;
                                        e.printStackTrace();
                                        if (fileInputStream3 != null) {
                                        }
                                        if (fileOutputStream != null) {
                                        }
                                        File file32 = new File(str);
                                        if (file32.exists()) {
                                        }
                                    } catch (Throwable th7) {
                                        th = th7;
                                        th = th;
                                        str4 = "delete local file!";
                                        fileInputStream3 = fileInputStream2;
                                        if (fileInputStream3 != null) {
                                        }
                                        if (fileOutputStream != null) {
                                        }
                                        file = new File(str);
                                        if (file.exists()) {
                                        }
                                    }
                                } else {
                                    long j11 = jRound;
                                    str5 = str2;
                                    i2 = i5;
                                    fileOutputStream2 = fileOutputStream;
                                    bArr12 = bArr5;
                                    bArr13 = bArr15;
                                    fileInputStream = fileInputStream2;
                                    path = str6;
                                    bArr7 = bArr;
                                    bArr9 = bArr2;
                                    i3 = i6;
                                    i = -1;
                                    fileInfo2 = fileInfo;
                                    j2 = j11;
                                    j = j3;
                                    bArr11 = bArr14;
                                    bArr10 = bArr3;
                                }
                            } else {
                                fileInfo2 = fileInfo;
                                fileOutputStream2 = fileOutputStream;
                                i3 = i8;
                                i = -1;
                            }
                        } catch (IOException e10) {
                            e = e10;
                            str2 = str5;
                            fileInputStream3 = fileInputStream;
                            z2 = zByte2File;
                            str4 = "delete local file!";
                            e.printStackTrace();
                            if (fileInputStream3 != null) {
                            }
                            if (fileOutputStream != null) {
                            }
                            File file322 = new File(str);
                            return file322.exists() ? z2 : z2;
                        } catch (Throwable th8) {
                            th2 = th8;
                            str2 = str5;
                            fileInputStream3 = fileInputStream;
                            str4 = "delete local file!";
                            th = th2;
                            if (fileInputStream3 != null) {
                            }
                            if (fileOutputStream != null) {
                            }
                            file = new File(str);
                            if (file.exists()) {
                            }
                        }
                    } else if (i8 >= 306176) {
                    }
                } catch (IOException e11) {
                    e = e11;
                    str2 = str5;
                    fileOutputStream = fileOutputStream2;
                    fileInputStream2 = fileInputStream;
                } catch (Throwable th9) {
                    th = th9;
                    str2 = str5;
                    fileOutputStream = fileOutputStream2;
                    fileInputStream2 = fileInputStream;
                }
                if (fileInputStream3 != null) {
                    try {
                        fileInputStream3.close();
                    } catch (IOException e12) {
                        e12.printStackTrace();
                        throw th;
                    }
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                file = new File(str);
                if (file.exists() && file.isFile() && file.delete()) {
                    Dbug.m419w(str2, str4);
                    throw th;
                }
                throw th;
            }
            boolean z3 = zByte2File;
            fileInputStream2.close();
            fileOutputStream.close();
            File file5 = new File(str);
            if (!file5.exists() || !file5.isFile() || !file5.delete()) {
                return z3;
            }
            Dbug.m419w(str2, "delete local file!");
            return z3;
        } catch (IOException e13) {
            e13.printStackTrace();
            return z;
        }
    }

    public static Locale getLanguage(int i) {
        switch (i) {
            case 0:
                return Locale.US;
            case 1:
                return Locale.FRENCH;
            case 2:
                return Locale.GERMAN;
            case 3:
                return new Locale("es", "");
            case 4:
                return new Locale("pt", "");
            case 5:
                return new Locale("bg", "");
            case 6:
                return new Locale("pl", "");
            case 7:
                return new Locale("nl", "");
            case 8:
                return new Locale("cs", "");
            case 9:
                return new Locale("hr", "");
            case 10:
                return new Locale("ro", "");
            case 11:
                return new Locale("sk", "");
            case 12:
                return new Locale("sl", "");
            case 13:
                return new Locale("el", "");
            case 14:
                return Locale.ITALIAN;
            default:
                Dbug.m416e(TAG, "Unknown language flag" + i);
                return null;
        }
    }

    public static void setLanguage(Context context, Locale locale) {
        Resources resources = context.getApplicationContext().getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        if (locale != null) {
            configuration.setLocale(locale);
            if (Build.VERSION.SDK_INT >= 24) {
                AppUtils$$ExternalSyntheticApiModelOutline0.m412m();
                configuration.setLocales(AppUtils$$ExternalSyntheticApiModelOutline0.m411m(new Locale[]{locale}));
                context.createConfigurationContext(configuration);
            } else {
                Locale.setDefault(locale);
                resources.updateConfiguration(configuration, displayMetrics);
            }
        }
    }

    public static Context attachBaseContext(Context context) {
        return Build.VERSION.SDK_INT >= 24 ? updateResources(context) : context;
    }

    private static Context updateResources(Context context) {
        Resources resources = context.getResources();
        Locale language = getLanguage(PreferencesHelper.getSharedPreferences(context.getApplicationContext()).getInt(IConstants.KEY_LANGUAGE_FLAG, LocalUtil.getLocaleLanguage(context)));
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(language);
        AppUtils$$ExternalSyntheticApiModelOutline0.m412m();
        configuration.setLocales(AppUtils$$ExternalSyntheticApiModelOutline0.m411m(new Locale[]{language}));
        return context.createConfigurationContext(configuration);
    }

    public static Bitmap reverseBitmap(Bitmap bitmap, int i) {
        Matrix matrix;
        if (bitmap == null) {
            return null;
        }
        Canvas canvas = new Canvas();
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmapCreateBitmap);
        if (i == 0) {
            matrix = new Matrix();
            matrix.postScale(1.0f, -1.0f);
            matrix.postTranslate(0.0f, bitmap.getHeight());
        } else if (i != 1) {
            matrix = null;
        } else {
            matrix = new Matrix();
            matrix.postScale(-1.0f, 1.0f);
            matrix.postTranslate(bitmap.getWidth(), 0.0f);
        }
        if (matrix == null) {
            return bitmap;
        }
        canvas.drawBitmap(bitmap, matrix, null);
        return bitmapCreateBitmap;
    }

    public static Bitmap rotateBitmap(int i, Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(i);
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (bitmapCreateBitmap != bitmap && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return bitmapCreateBitmap;
    }

    public static int getScreenBrightness(Activity activity) {
        if (activity != null) {
            return Settings.System.getInt(activity.getContentResolver(), "screen_brightness", 125);
        }
        return 125;
    }

    public static void setBrightness(Activity activity, int i) {
        Window window;
        WindowManager.LayoutParams attributes;
        if (activity == null || (window = activity.getWindow()) == null || (attributes = window.getAttributes()) == null) {
            return;
        }
        attributes.screenBrightness = i / 255.0f;
        window.setAttributes(attributes);
        Settings.System.putInt(activity.getContentResolver(), "screen_brightness", i);
    }

    public static int getScreenHeight(Context context) {
        return ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getHeight();
    }

    public static boolean isNotSupport4K() {
        String str = Build.MODEL;
        if (str == null || str.length() <= 0) {
            return false;
        }
        String lowerCase = str.toLowerCase();
        Dbug.m417i("MODEL", "phone_type: " + lowerCase);
        return lowerCase.contains("vivo") && lowerCase.contains("y75");
    }

    public static void mediaScanImage(Context context, File file) {
        try {
            Log.v("mediaScanImage", "file was scanned successfully: " + saveBitmap(context, BitmapFactory.decodeFile(file.getAbsolutePath()), Bitmap.CompressFormat.JPEG, "image/jpeg", file.getName()));
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("mediaScanImage", "file was scanned failed");
        }
    }

    public static void mediaScanVideo(Context context, File file) {
        try {
            Log.v("mediaScanVideo", "file was scanned successfully: " + saveVideo(context, file.getAbsolutePath(), "video/mp4", file.getName()));
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("mediaScanVideo", "file was scanned failed");
        }
    }

    public static Uri saveBitmap(Context context, Bitmap bitmap, Bitmap.CompressFormat compressFormat, String str, String str2) throws IOException {
        Uri uriInsert;
        ContentValues contentValues = new ContentValues();
        contentValues.put("_display_name", str2);
        contentValues.put("mime_type", str);
        if (Build.VERSION.SDK_INT >= 29) {
            contentValues.put("relative_path", Environment.DIRECTORY_PICTURES);
        } else {
            contentValues.put("_data", Environment.getExternalStorageDirectory().getPath() + File.separator + Environment.DIRECTORY_PICTURES + File.separator + str2);
        }
        ContentResolver contentResolver = context.getContentResolver();
        try {
            uriInsert = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        } catch (IOException e) {
            e = e;
            uriInsert = null;
        }
        try {
            if (uriInsert == null) {
                throw new IOException("Failed to create new MediaStore record.");
            }
            OutputStream outputStreamOpenOutputStream = contentResolver.openOutputStream(uriInsert);
            try {
                if (outputStreamOpenOutputStream == null) {
                    throw new IOException("Failed to open output stream.");
                }
                if (!bitmap.compress(compressFormat, 100, outputStreamOpenOutputStream)) {
                    throw new IOException("Failed to save bitmap.");
                }
                if (outputStreamOpenOutputStream != null) {
                    outputStreamOpenOutputStream.close();
                }
                return uriInsert;
            } finally {
            }
        } catch (IOException e2) {
            e = e2;
            if (uriInsert != null) {
                contentResolver.delete(uriInsert, null, null);
            }
            throw e;
        }
    }

    public static Uri saveVideo(Context context, String str, String str2, String str3) throws IOException {
        Uri uriInsert;
        ContentResolver contentResolver = context.getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", str3);
        contentValues.put("_display_name", str3);
        contentValues.put("mime_type", str2);
        contentValues.put("date_added", Long.valueOf(System.currentTimeMillis() / 1000));
        if (Build.VERSION.SDK_INT >= 29) {
            contentValues.put("relative_path", Environment.DIRECTORY_MOVIES);
            uriInsert = contentResolver.insert(MediaStore.Video.Media.getContentUri("external_primary"), contentValues);
        } else {
            contentValues.put("_data", context.getExternalMediaDirs()[0] + File.separator + str3);
            uriInsert = contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues);
        }
        if (Build.VERSION.SDK_INT >= 29) {
            contentValues.put("datetaken", Long.valueOf(System.currentTimeMillis()));
            contentValues.put("is_pending", (Integer) 1);
        }
        try {
            ParcelFileDescriptor parcelFileDescriptorOpenFileDescriptor = contentResolver.openFileDescriptor(uriInsert, "w");
            FileOutputStream fileOutputStream = new FileOutputStream(parcelFileDescriptorOpenFileDescriptor.getFileDescriptor());
            FileInputStream fileInputStream = new FileInputStream(new File(str));
            byte[] bArr = new byte[8192];
            while (true) {
                int i = fileInputStream.read(bArr);
                if (i <= 0) {
                    break;
                }
                fileOutputStream.write(bArr, 0, i);
            }
            fileOutputStream.close();
            fileInputStream.close();
            parcelFileDescriptorOpenFileDescriptor.close();
            if (Build.VERSION.SDK_INT >= 29) {
                contentValues.clear();
                contentValues.put("is_pending", (Integer) 0);
                contentResolver.update(uriInsert, contentValues, null, null);
            }
            return uriInsert;
        } catch (IOException e) {
            if (uriInsert != null) {
                contentResolver.delete(uriInsert, null, null);
            }
            throw e;
        }
    }

    public static String getVideoDirPath(Context context) {
        String videoPath = getVideoPath(context);
        if (videoPath == null) {
            return null;
        }
        File file = new File(videoPath);
        if (file.exists() || file.mkdirs()) {
            return file.getAbsolutePath();
        }
        return null;
    }

    public static String getPhotoDirPath(Context context) {
        String photoPath = getPhotoPath(context);
        if (photoPath == null) {
            return null;
        }
        File file = new File(photoPath);
        if (file.exists() || file.mkdirs()) {
            return file.getAbsolutePath();
        }
        return null;
    }

    public static String getPhotoPath(Context context) {
        return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
    }

    public static String getVideoPath(Context context) {
        return context.getExternalFilesDir(Environment.DIRECTORY_MOVIES).getAbsolutePath();
    }

    public static String getDocumentsPath(Context context) {
        return context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
    }

    public static String getThumbPath(Context context) {
        return context.getExternalFilesDir(IConstants.THUMB).getAbsolutePath();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0085 A[RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static boolean isSoftEncode(int i) {
        String str;
        if (Build.VERSION.SDK_INT >= 27 && (str = Build.MANUFACTURER) != null && str.length() > 0) {
            String lowerCase = str.toLowerCase();
            Dbug.m417i("manufacturer", "phone_type: " + lowerCase);
            lowerCase.hashCode();
            switch (lowerCase) {
                case "huawei":
                case "honor":
                case "samsung":
                    if (i == 2048) {
                        return true;
                    }
                    break;
                case "oppo":
                    if (Build.MODEL.equals("PFUM10")) {
                        return true;
                    }
                    break;
                case "vivo":
                    return true;
                case "hi nova":
                    if (Build.MODEL.equals("FIO-BD00")) {
                    }
                    break;
            }
        }
        return false;
    }
}
