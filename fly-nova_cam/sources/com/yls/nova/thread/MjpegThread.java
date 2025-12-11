package com.yls.nova.thread;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import com.yls.nova.interfaces.OnMjpegListener;
import com.yls.nova.models.VideoModel;
import com.yls.nova.utils.AppUtils;
import com.yls.nova.utils.BitmapUtil;
import com.yls.nova.utils.Dbug;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/* loaded from: classes.dex */
public class MjpegThread extends Thread {
    private static final String tag = "MjpegThread";
    private int[] argb;
    private int fakeHeight;
    private int fakeWidth;
    private int fps;
    private int jHeight;
    private int jWidth;
    private Context mContext;
    private OnMjpegListener mOnMjpegListener;
    private Timer timer;
    private VideoModel videoModel;
    private byte[] yuv42sp;
    private int fakeResolution = 0;
    private boolean isVideoThreadRunning = false;
    private final LinkedBlockingQueue<byte[]> mBufList = new LinkedBlockingQueue<>(5);
    private volatile boolean isWaiting = false;
    private boolean isTurnBitmap = false;
    private final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    private int encodeTime = 1;
    private int encodeEnd = 1;
    private boolean isSetResolution = false;
    private boolean isSavePicture = false;
    private boolean isSetRotate = false;
    private boolean isRotate = false;
    private int focusScale = 0;
    private float focusMoveX = 0.0f;
    private float focusMoveY = 0.0f;
    private float scaleRotate = 1.0f;
    private String photoPath = "";

    public MjpegThread(Context context) {
        this.mContext = context;
    }

    public void setOnMjpegListener(OnMjpegListener onMjpegListener) {
        this.mOnMjpegListener = onMjpegListener;
    }

    public void drawBitmap(byte[] bArr) throws InterruptedException {
        addData(bArr);
    }

    public void setFakeResolution(int i) {
        this.fakeResolution = i;
    }

    public int getContrastCompressWidth() {
        return this.jWidth;
    }

    public int getContrastCompressHeight() {
        return this.jHeight;
    }

    public void setInitTurnBitmap(boolean z) {
        this.isTurnBitmap = z;
    }

    public void setTurnBitmap(boolean z) {
        this.isTurnBitmap = z;
    }

    public void setModel(VideoModel videoModel) {
        this.videoModel = videoModel;
    }

    public void setFps(int i) {
        this.fps = i;
    }

    public void setFocusScale(int i) {
        this.focusScale = i;
        if (i <= 1) {
            this.focusMoveX = 0.0f;
            this.focusMoveY = 0.0f;
        }
    }

    public void setFocusMove(float f, float f2) {
        if (this.focusScale <= 1) {
            return;
        }
        this.focusMoveX = f;
        this.focusMoveY = f2;
    }

    public boolean getTurnBitmap() {
        return this.isTurnBitmap;
    }

    public void savePicture(String str) {
        this.photoPath = str;
        this.isSavePicture = true;
    }

    public void setRotate() {
        this.isSetRotate = true;
    }

    void addData(byte[] bArr) throws InterruptedException {
        if (this.mBufList.remainingCapacity() <= 1) {
            this.mBufList.poll();
        }
        try {
            this.mBufList.put(bArr);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (this.isWaiting) {
            synchronized (this.mBufList) {
                this.mBufList.notify();
            }
        }
    }

    void stopRunning() {
        this.isVideoThreadRunning = false;
        synchronized (this.mBufList) {
            this.mBufList.notify();
            this.mBufList.clear();
        }
    }

    void openTimer() {
        if (this.timer != null || this.fps == 0) {
            return;
        }
        Dbug.m416e("fps=", "" + this.fps);
        Timer timer = new Timer();
        this.timer = timer;
        timer.schedule(new TimerTask() { // from class: com.yls.nova.thread.MjpegThread.1
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                if (MjpegThread.this.yuv42sp == null || MjpegThread.this.yuv42sp.length == 0) {
                    return;
                }
                MjpegThread.this.videoModel.offerEncoder(MjpegThread.this.yuv42sp);
            }
        }, 100L, 1000 / this.fps);
    }

    void cancelTimer() {
        Timer timer = this.timer;
        if (timer != null) {
            timer.cancel();
            this.timer = null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:108:0x029c  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x02ec A[PHI: r14
  0x02ec: PHI (r14v8 android.graphics.Bitmap) = (r14v6 android.graphics.Bitmap), (r14v6 android.graphics.Bitmap), (r14v9 android.graphics.Bitmap) binds: [B:107:0x029a, B:109:0x029e, B:117:0x02d9] A[DONT_GENERATE, DONT_INLINE]] */
    @Override // java.lang.Thread, java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void run() {
        boolean z;
        int i;
        Bitmap bitmapCreateBitmap;
        float f;
        float f2;
        Bitmap bitmapCreateBitmap2;
        float f3;
        float f4;
        Bitmap bitmapCreateBitmap3;
        super.run();
        this.isVideoThreadRunning = true;
        synchronized (this.mBufList) {
            while (this.isVideoThreadRunning) {
                if (this.mBufList.isEmpty()) {
                    try {
                        this.isWaiting = true;
                        this.mBufList.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    this.isWaiting = false;
                    byte[] bArrRemove = this.mBufList.remove();
                    Bitmap bitmapDecodeByteArray = BitmapFactory.decodeByteArray(bArrRemove, 0, bArrRemove.length);
                    if (this.focusScale > 1) {
                        int width = bitmapDecodeByteArray.getWidth();
                        int height = bitmapDecodeByteArray.getHeight();
                        float f5 = this.focusScale / 100.0f;
                        int i2 = (int) ((width / 2) * f5);
                        int i3 = (int) ((height / 2) * f5);
                        int i4 = ((width / 2) - i2) * 2;
                        int i5 = ((height / 2) - i3) * 2;
                        int i6 = (int) (i2 - (this.focusMoveX / f5));
                        int i7 = (int) (i3 + (this.focusMoveY / f5));
                        if (i6 <= 0) {
                            i6 = 0;
                        }
                        int i8 = width - i4;
                        if (i6 >= i8) {
                            i6 = i8;
                        }
                        if (i7 <= 0) {
                            i7 = 0;
                        }
                        int i9 = height - i5;
                        if (i7 >= i9) {
                            i7 = i9;
                        }
                        bitmapDecodeByteArray = Bitmap.createBitmap(bitmapDecodeByteArray, i6, i7, i4, i5);
                        z = true;
                    } else {
                        z = false;
                    }
                    if (this.isTurnBitmap) {
                        bitmapDecodeByteArray = AppUtils.rotateBitmap(180, bitmapDecodeByteArray);
                    }
                    if (bitmapDecodeByteArray != null) {
                        this.jWidth = bitmapDecodeByteArray.getWidth();
                        int height2 = bitmapDecodeByteArray.getHeight();
                        this.jHeight = height2;
                        int i10 = this.jWidth;
                        if (i10 == 800 && height2 == 600) {
                            bitmapDecodeByteArray = Bitmap.createBitmap(bitmapDecodeByteArray, 0, 30, i10, height2 - 60);
                            this.jHeight = 540;
                        } else if ((i10 == 240 && height2 == 320) || (i10 == 160 && height2 == 272)) {
                            bitmapDecodeByteArray = AppUtils.rotateBitmap(90, bitmapDecodeByteArray);
                            int i11 = this.jWidth;
                            if (i11 == 240) {
                                this.jWidth = 320;
                                this.jHeight = 240;
                            } else if (i11 == 160) {
                                this.jWidth = 272;
                                this.jHeight = 160;
                            }
                        }
                        Bitmap bitmapCreateBitmap4 = bitmapDecodeByteArray;
                        Dbug.m417i(tag, "Bitmap size : width :" + bitmapCreateBitmap4.getWidth() + "height :" + bitmapCreateBitmap4.getHeight());
                        if (this.isSetRotate) {
                            if (this.isRotate) {
                                this.scaleRotate = (float) (this.scaleRotate + 0.025d);
                            } else {
                                this.scaleRotate = (float) (this.scaleRotate - 0.025d);
                            }
                            int width2 = bitmapCreateBitmap4.getWidth();
                            int height3 = bitmapCreateBitmap4.getHeight();
                            Matrix matrix = new Matrix();
                            float f6 = this.scaleRotate;
                            if (f6 >= 0.11d) {
                                matrix.postRotate((1.0f - f6) * 360.0f);
                            }
                            float f7 = this.scaleRotate;
                            matrix.postScale(f7, f7);
                            Bitmap bitmapCreateBitmap5 = Bitmap.createBitmap(bitmapCreateBitmap4, 0, 0, bitmapCreateBitmap4.getWidth(), bitmapCreateBitmap4.getHeight(), matrix, true);
                            bitmapCreateBitmap4 = Bitmap.createBitmap(width2, height3, Bitmap.Config.ARGB_8888);
                            new Canvas(bitmapCreateBitmap4).drawBitmap(bitmapCreateBitmap5, (width2 / 2) - (bitmapCreateBitmap5.getWidth() / 2), (height3 / 2) - (bitmapCreateBitmap5.getHeight() / 2), (Paint) null);
                            if (!this.isRotate && this.scaleRotate <= 0.1d) {
                                this.isRotate = true;
                            }
                            if (this.isRotate && this.scaleRotate >= 0.9d) {
                                this.scaleRotate = 1.0f;
                                this.isRotate = false;
                                this.isSetRotate = false;
                            }
                        }
                        Bitmap bitmapCreateBitmap6 = bitmapCreateBitmap4;
                        VideoModel videoModel = this.videoModel;
                        if (videoModel != null && videoModel.isRecording()) {
                            if (z && this.fakeResolution == 0) {
                                int width3 = bitmapCreateBitmap6.getWidth();
                                int height4 = bitmapCreateBitmap6.getHeight();
                                Matrix matrix2 = new Matrix();
                                matrix2.postScale(this.jWidth / width3, this.jHeight / height4);
                                i = 480;
                                bitmapCreateBitmap6 = Bitmap.createBitmap(bitmapCreateBitmap6, 0, 0, width3, height4, matrix2, true);
                            } else {
                                i = 480;
                            }
                            if (this.fakeResolution != 0) {
                                int width4 = bitmapCreateBitmap6.getWidth();
                                int height5 = bitmapCreateBitmap6.getHeight();
                                Matrix matrix3 = new Matrix();
                                int i12 = this.fakeResolution;
                                if (i12 == 1) {
                                    this.fakeWidth = 640;
                                    this.fakeHeight = i;
                                    if (height5 != i) {
                                        int i13 = width4 - 160;
                                        f = 640.0f / i13;
                                        f2 = 480.0f / height5;
                                        bitmapCreateBitmap2 = Bitmap.createBitmap(bitmapCreateBitmap6, 80, 0, i13, height5);
                                        f3 = f2;
                                        if (f != 0.0f) {
                                        }
                                    }
                                    f3 = 0.0f;
                                    f = 0.0f;
                                    bitmapCreateBitmap2 = null;
                                    if (f != 0.0f) {
                                    }
                                } else if (i12 == 2) {
                                    this.fakeWidth = 1280;
                                    this.fakeHeight = 720;
                                    if (width4 == 1280) {
                                        f3 = 0.0f;
                                        f = 0.0f;
                                        bitmapCreateBitmap2 = null;
                                        if (f != 0.0f) {
                                        }
                                    } else if (height5 == i) {
                                        int i14 = height5 - 120;
                                        f = 1280.0f / width4;
                                        f2 = 720.0f / i14;
                                        bitmapCreateBitmap2 = Bitmap.createBitmap(bitmapCreateBitmap6, 0, 60, width4, i14);
                                        f3 = f2;
                                        if (f != 0.0f) {
                                        }
                                    } else {
                                        f = 1280.0f / width4;
                                        f3 = 720.0f / height5;
                                        bitmapCreateBitmap2 = null;
                                        if (f != 0.0f) {
                                        }
                                    }
                                } else if (i12 == 3) {
                                    if (height5 == i) {
                                        int i15 = height5 - 120;
                                        f2 = 1080.0f / i15;
                                        bitmapCreateBitmap2 = Bitmap.createBitmap(bitmapCreateBitmap6, 0, 60, width4, i15);
                                        f = 1920.0f / width4;
                                    } else {
                                        f2 = 1080.0f / height5;
                                        f = 1920.0f / width4;
                                        bitmapCreateBitmap2 = null;
                                    }
                                    this.fakeWidth = 1920;
                                    this.fakeHeight = 1080;
                                    f3 = f2;
                                    if (f != 0.0f) {
                                    }
                                } else if (i12 == 4 || i12 == 5) {
                                    if (height5 == i) {
                                        int i16 = height5 - 120;
                                        bitmapCreateBitmap3 = Bitmap.createBitmap(bitmapCreateBitmap6, 0, 60, width4, i16);
                                        f = 2048.0f / width4;
                                        f4 = 1080.0f / i16;
                                    } else {
                                        f = 2048.0f / width4;
                                        f4 = 1080.0f / height5;
                                        bitmapCreateBitmap3 = null;
                                    }
                                    this.fakeWidth = 2048;
                                    this.fakeHeight = 1080;
                                    f3 = f4;
                                    bitmapCreateBitmap2 = bitmapCreateBitmap3;
                                    if (f != 0.0f || f3 == 0.0f) {
                                        bitmapCreateBitmap = bitmapCreateBitmap2;
                                    } else {
                                        matrix3.postScale(f, f3);
                                        if (this.fakeResolution == 1 && bitmapCreateBitmap2 != null) {
                                            bitmapCreateBitmap = Bitmap.createBitmap(bitmapCreateBitmap2, 0, 0, bitmapCreateBitmap2.getWidth(), height5, matrix3, true);
                                        } else if (height5 == i && bitmapCreateBitmap2 != null) {
                                            bitmapCreateBitmap = Bitmap.createBitmap(bitmapCreateBitmap2, 0, 0, bitmapCreateBitmap2.getWidth(), bitmapCreateBitmap2.getHeight(), matrix3, true);
                                        } else {
                                            bitmapCreateBitmap6 = Bitmap.createBitmap(bitmapCreateBitmap6, 0, 0, width4, height5, matrix3, true);
                                            bitmapCreateBitmap = bitmapCreateBitmap2;
                                        }
                                    }
                                } else {
                                    this.fakeWidth = width4;
                                    this.fakeHeight = height5;
                                    f3 = 0.0f;
                                    f = 0.0f;
                                    bitmapCreateBitmap2 = null;
                                    if (f != 0.0f) {
                                        bitmapCreateBitmap = bitmapCreateBitmap2;
                                    }
                                }
                            } else {
                                bitmapCreateBitmap = null;
                            }
                            if ((this.fakeResolution == 1 && bitmapCreateBitmap != null) || bitmapCreateBitmap != null) {
                                this.argb = this.videoModel.bitmap2argb(bitmapCreateBitmap);
                                bitmapCreateBitmap.recycle();
                            } else {
                                this.argb = this.videoModel.bitmap2argb(bitmapCreateBitmap6);
                            }
                            this.cachedThreadPool.execute(new Runnable() { // from class: com.yls.nova.thread.MjpegThread$$ExternalSyntheticLambda0
                                @Override // java.lang.Runnable
                                public final void run() {
                                    this.f$0.m553lambda$run$0$comylsnovathreadMjpegThread();
                                }
                            });
                            openTimer();
                        } else {
                            i = 480;
                            cancelTimer();
                        }
                        if (this.isSavePicture) {
                            this.isSavePicture = false;
                            int i17 = this.fakeResolution;
                            if (i17 == 1) {
                                if (bitmapCreateBitmap6.getHeight() != i) {
                                    bitmapCreateBitmap6 = Bitmap.createBitmap(bitmapCreateBitmap6, 80, 0, bitmapCreateBitmap6.getWidth() - 160, bitmapCreateBitmap6.getHeight());
                                }
                            } else if ((i17 == 2 || i17 == 3 || i17 == 4 || i17 == 5) && bitmapCreateBitmap6.getHeight() == i) {
                                bitmapCreateBitmap6 = Bitmap.createBitmap(bitmapCreateBitmap6, 0, 60, bitmapCreateBitmap6.getWidth(), bitmapCreateBitmap6.getHeight() - 120);
                            }
                            BitmapUtil.saveBitmap(this.photoPath, BitmapUtil.fakeBitmap(bitmapCreateBitmap6, this.fakeResolution, false));
                            AppUtils.mediaScanImage(this.mContext, new File(this.photoPath));
                        }
                        OnMjpegListener onMjpegListener = this.mOnMjpegListener;
                        if (onMjpegListener != null) {
                            onMjpegListener.onFrame(bitmapCreateBitmap6);
                        }
                    } else {
                        Dbug.m416e(tag, "bitmap is null. data size=" + bArrRemove.length);
                    }
                }
            }
            Dbug.m416e("stopRunning", "----->");
        }
    }

    /* renamed from: lambda$run$0$com-yls-nova-thread-MjpegThread, reason: not valid java name */
    /* synthetic */ void m553lambda$run$0$comylsnovathreadMjpegThread() {
        int i = this.encodeTime + 1;
        this.encodeTime = i;
        int i2 = this.fakeWidth;
        int i3 = this.fakeHeight;
        byte[] bArr = new byte[((i2 * i3) * 3) / 2];
        byte[] bArr2 = new byte[((i2 * i3) * 3) / 2];
        this.videoModel.encodeYUV420SP(bArr, bArr2, this.argb, i2, i3);
        int i4 = this.encodeEnd + 1;
        this.encodeEnd = i4;
        if (i4 <= i) {
            if (this.videoModel.isYUV420P()) {
                this.yuv42sp = bArr2;
                return;
            } else {
                this.yuv42sp = bArr;
                return;
            }
        }
        Dbug.m416e(tag + "Wrong", "start:" + this.encodeTime + "end:" + this.encodeEnd);
    }

    public void release() {
        stopRunning();
    }
}
