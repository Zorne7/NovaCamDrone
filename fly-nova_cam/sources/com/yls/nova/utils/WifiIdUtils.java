package com.yls.nova.utils;

import com.yls.nova.tools.IConstants;

/* loaded from: classes.dex */
public class WifiIdUtils implements IConstants {
    private static final int CHANGE_PASSWORD_8K = 80;
    private static final int CHANGE_PASSWORD_DOUBLE_8K = 81;
    private static final int CHANGE_PASSWORD_FLOW_480 = 85;
    private static final int CHANGE_PASSWORD_FLOW_8K = 82;
    private static final int DOUBLE_RESOLUTION_1080 = 43;
    private static final int DOUBLE_RESOLUTION_4K = 44;
    private static final int DOUBLE_RESOLUTION_720 = 41;
    private static final int DOUBLE_RESOLUTION_8K = 45;
    private static final int KY_DOUBLE_RESOLUTION_1080 = 23;
    private static final int KY_DOUBLE_RESOLUTION_4K = 24;
    private static final int KY_DOUBLE_RESOLUTION_720 = 21;
    private static final int KY_DOUBLE_RESOLUTION_8K = 51;
    private static final int KY_RESOLUTION_1080 = 12;
    private static final int KY_RESOLUTION_480 = 5;
    private static final int KY_RESOLUTION_4k = 19;
    private static final int KY_RESOLUTION_720 = 9;
    private static final int KY_RESOLUTION_8k = 20;
    private static final int KY_SPI_RESOLUTION_1080 = 65;
    private static final int KY_SPI_RESOLUTION_480 = 63;
    private static final int KY_SPI_RESOLUTION_4K = 66;
    private static final int KY_SPI_RESOLUTION_720 = 64;
    private static final int KY_SPI_RESOLUTION_8K = 67;
    private static final int RESOLUTION_1080 = 29;
    private static final int RESOLUTION_480 = 26;
    private static final int RESOLUTION_4k = 30;
    private static final int RESOLUTION_720 = 27;
    private static final int RESOLUTION_8k = 31;
    private static final int RESOLUTION_SETTING = 46;
    private static final int SPI_RESOLUTION_1080 = 70;
    private static final int SPI_RESOLUTION_480 = 68;
    private static final int SPI_RESOLUTION_4K = 71;
    private static final int SPI_RESOLUTION_720 = 69;
    private static final int SPI_RESOLUTION_8K = 72;

    public static boolean isDoubleCamera(int i) {
        return i == 41 || i == 43 || i == 44;
    }

    public static boolean isFakeResolution1080(int i) {
        return i == 29 || i == 43 || i == 70 || i == 12 || i == 23 || i == 65;
    }

    public static boolean isFakeResolution480(int i) {
        return i == 26 || i == 68 || i == 5 || i == 63 || i == 85;
    }

    public static boolean isFakeResolution4K(int i) {
        return i == 30 || i == 44 || i == 71;
    }

    public static boolean isFakeResolution720(int i) {
        return i == 27 || i == 41 || i == 69 || i == 9 || i == 21 || i == 64;
    }

    public static boolean isFakeResolution8K(int i) {
        return i == 31 || i == 45 || i == 72 || i == 20 || i == 51 || i == 67 || i == 19 || i == 24 || i == 66 || i == 80 || i == 81 || i == 82;
    }

    public static boolean isSettingResolution(int i) {
        return i == 46;
    }

    public static int getFakeResolution(int i) {
        if (isFakeResolution480(i)) {
            return 1;
        }
        if (isFakeResolution720(i)) {
            return 2;
        }
        if (isFakeResolution1080(i)) {
            return 3;
        }
        if (isFakeResolution4K(i)) {
            return 4;
        }
        return isFakeResolution8K(i) ? 5 : 0;
    }

    public static boolean isSupport(int i) {
        return isFakeResolution480(i) || isFakeResolution720(i) || isFakeResolution1080(i) || isFakeResolution4K(i) || isFakeResolution8K(i);
    }
}
