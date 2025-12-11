package tv.danmaku.ijk.media.widget;

import android.content.Context;
import android.view.View;
import java.lang.ref.WeakReference;
import tv.danmaku.ijk.media.C0939R;

/* loaded from: classes.dex */
public final class MeasureHelper {
    private int mCurrentAspectRatio = 0;
    private int mMeasuredHeight;
    private int mMeasuredWidth;
    private int mVideoHeight;
    private int mVideoRotationDegree;
    private int mVideoSarDen;
    private int mVideoSarNum;
    private int mVideoWidth;
    private WeakReference<View> mWeakView;

    public MeasureHelper(View view) {
        this.mWeakView = new WeakReference<>(view);
    }

    public View getView() {
        WeakReference<View> weakReference = this.mWeakView;
        if (weakReference == null) {
            return null;
        }
        return weakReference.get();
    }

    public void setVideoSize(int i, int i2) {
        this.mVideoWidth = i;
        this.mVideoHeight = i2;
    }

    public void setVideoSampleAspectRatio(int i, int i2) {
        this.mVideoSarNum = i;
        this.mVideoSarDen = i2;
    }

    public void setVideoRotation(int i) {
        this.mVideoRotationDegree = i;
    }

    /* JADX WARN: Removed duplicated region for block: B:50:0x00a9  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x00ad  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void doMeasure(int i, int i2) {
        int i3;
        float f;
        int i4;
        int i5 = this.mVideoRotationDegree;
        if (i5 == 90 || i5 == 270) {
            i2 = i;
            i = i2;
        }
        int defaultSize = View.getDefaultSize(this.mVideoWidth, i);
        int defaultSize2 = View.getDefaultSize(this.mVideoHeight, i2);
        if (this.mCurrentAspectRatio != 3) {
            if (this.mVideoWidth <= 0 || this.mVideoHeight <= 0) {
                i = defaultSize;
                i2 = defaultSize2;
            } else {
                int mode = View.MeasureSpec.getMode(i);
                i = View.MeasureSpec.getSize(i);
                int mode2 = View.MeasureSpec.getMode(i2);
                i2 = View.MeasureSpec.getSize(i2);
                if (mode == Integer.MIN_VALUE && mode2 == Integer.MIN_VALUE) {
                    float f2 = i;
                    float f3 = i2;
                    float f4 = f2 / f3;
                    int i6 = this.mCurrentAspectRatio;
                    if (i6 == 4) {
                        int i7 = this.mVideoRotationDegree;
                        f = (i7 == 90 || i7 == 270) ? 0.5625f : 1.7777778f;
                    } else if (i6 == 5) {
                        int i8 = this.mVideoRotationDegree;
                        f = (i8 == 90 || i8 == 270) ? 0.75f : 1.3333334f;
                    } else {
                        f = this.mVideoWidth / this.mVideoHeight;
                        int i9 = this.mVideoSarNum;
                        if (i9 > 0 && (i4 = this.mVideoSarDen) > 0) {
                            f = (f * i9) / i4;
                        }
                    }
                    boolean z = f > f4;
                    if (i6 == 0) {
                        if (z) {
                            i = (int) (f3 * f);
                        } else {
                            i2 = (int) (f2 / f);
                        }
                    } else if (i6 != 1) {
                        if (i6 != 4 && i6 != 5) {
                            if (z) {
                                i = Math.min(this.mVideoWidth, i);
                                i2 = (int) (i / f);
                            } else {
                                int iMin = Math.min(this.mVideoHeight, i2);
                                i2 = iMin;
                                i = (int) (iMin * f);
                            }
                        }
                        if (z) {
                        }
                    } else if (z) {
                    }
                } else if (mode == 1073741824 && mode2 == 1073741824) {
                    int i10 = this.mVideoWidth;
                    int i11 = i10 * i2;
                    int i12 = this.mVideoHeight;
                    if (i11 < i * i12) {
                        i = (i10 * i2) / i12;
                    } else if (i10 * i2 > i * i12) {
                        i2 = (i12 * i) / i10;
                    }
                } else if (mode == 1073741824) {
                    int i13 = (this.mVideoHeight * i) / this.mVideoWidth;
                    if (mode2 != Integer.MIN_VALUE || i13 <= i2) {
                        i2 = i13;
                    }
                } else if (mode2 == 1073741824) {
                    int i14 = (this.mVideoWidth * i2) / this.mVideoHeight;
                    if (mode != Integer.MIN_VALUE || i14 <= i) {
                        i = i14;
                    }
                } else {
                    int i15 = this.mVideoWidth;
                    int i16 = this.mVideoHeight;
                    if (mode2 != Integer.MIN_VALUE || i16 <= i2) {
                        i3 = i15;
                        i2 = i16;
                    } else {
                        i3 = (i2 * i15) / i16;
                    }
                    if (mode != Integer.MIN_VALUE || i3 <= i) {
                        i = i3;
                    } else {
                        i2 = (i16 * i) / i15;
                    }
                }
            }
        }
        this.mMeasuredWidth = i;
        this.mMeasuredHeight = i2;
    }

    public int getMeasuredWidth() {
        return this.mMeasuredWidth;
    }

    public int getMeasuredHeight() {
        return this.mMeasuredHeight;
    }

    public void setAspectRatio(int i) {
        this.mCurrentAspectRatio = i;
    }

    public static String getAspectRatioText(Context context, int i) {
        if (i == 0) {
            return context.getString(C0939R.string.VideoView_ar_aspect_fit_parent);
        }
        if (i == 1) {
            return context.getString(C0939R.string.VideoView_ar_aspect_fill_parent);
        }
        if (i == 2) {
            return context.getString(C0939R.string.VideoView_ar_aspect_wrap_content);
        }
        if (i == 3) {
            return context.getString(C0939R.string.VideoView_ar_match_parent);
        }
        if (i == 4) {
            return context.getString(C0939R.string.VideoView_ar_16_9_fit_parent);
        }
        if (i == 5) {
            return context.getString(C0939R.string.VideoView_ar_4_3_fit_parent);
        }
        return context.getString(C0939R.string.N_A);
    }
}
