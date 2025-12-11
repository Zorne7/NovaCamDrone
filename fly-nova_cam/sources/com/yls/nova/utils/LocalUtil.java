package com.yls.nova.utils;

import android.content.Context;

/* loaded from: classes.dex */
public class LocalUtil {
    public static int getLocaleLanguage(Context context) {
        String language = context.getResources().getConfiguration().locale.getLanguage();
        if (language.endsWith("fr")) {
            return 1;
        }
        if (language.endsWith("de")) {
            return 2;
        }
        if (language.endsWith("es")) {
            return 3;
        }
        if (language.endsWith("pt")) {
            return 4;
        }
        if (language.endsWith("bg")) {
            return 5;
        }
        if (language.endsWith("cs")) {
            return 8;
        }
        if (language.endsWith("el")) {
            return 13;
        }
        if (language.endsWith("hr")) {
            return 9;
        }
        if (language.endsWith("nl")) {
            return 7;
        }
        if (language.endsWith("pl")) {
            return 6;
        }
        if (language.endsWith("ro")) {
            return 10;
        }
        if (language.endsWith("sk")) {
            return 11;
        }
        if (language.endsWith("sl")) {
            return 12;
        }
        return language.endsWith("it") ? 14 : 0;
    }
}
