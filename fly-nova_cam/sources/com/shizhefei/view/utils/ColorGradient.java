package com.shizhefei.view.utils;

import android.graphics.Color;

/* loaded from: classes.dex */
public class ColorGradient {
    private int color1;
    private int[] color1Values;
    private int color2;
    private int[] color2Values;
    private int count;

    public ColorGradient(int i, int i2, int i3) {
        this.color1 = i;
        this.color2 = i2;
        this.count = i3;
        this.color1Values = toColorValue(i);
        this.color2Values = toColorValue(i2);
    }

    public int getColor(int i) {
        int[] iArr = new int[4];
        int i2 = 0;
        while (true) {
            if (i2 < this.color2Values.length) {
                iArr[i2] = (int) (this.color1Values[i2] + ((((r3[i2] - r4) * 1.0d) / this.count) * i));
                i2++;
            } else {
                return Color.argb(iArr[0], iArr[1], iArr[2], iArr[3]);
            }
        }
    }

    private int[] toColorValue(int i) {
        return new int[]{Color.alpha(i), Color.red(i), Color.green(i), Color.blue(i)};
    }

    public int getColor1() {
        return this.color1;
    }

    public int getColor2() {
        return this.color2;
    }

    public int getCount() {
        return this.count;
    }
}
