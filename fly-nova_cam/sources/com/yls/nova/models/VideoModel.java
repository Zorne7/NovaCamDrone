package com.yls.nova.models;

import android.graphics.Bitmap;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.util.Log;
import android.view.Surface;
import com.yls.nova.base.BaseFragment;
import com.yls.nova.utils.AppUtils;
import com.yls.nova.utils.Dbug;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

/* loaded from: classes.dex */
public class VideoModel {
    private BaseFragment context;
    private boolean isRecording;
    private MediaMuxer mMuxer;
    private int mTrackIndex;
    private MediaCodec mediaCodec;
    private String TAG = getClass().getSimpleName();
    private int width = 640;
    private int height = 480;
    private int fps = 20;
    private boolean isYUV420P = false;
    private long frameIndex = 0;
    private int timeoutUSec = 10000;
    private byte[] spsPpsInfo = null;
    private ByteArrayOutputStream baops = new ByteArrayOutputStream();
    private String currentVideoPath = "";

    public VideoModel(BaseFragment baseFragment) {
        this.context = baseFragment;
    }

    public void init(int i, int i2, int i3) throws IOException {
        if (isRecording()) {
            stopRecorder();
        }
        this.width = i;
        this.height = i2;
        this.fps = i3;
        try {
            if (AppUtils.isSoftEncode(i)) {
                this.mediaCodec = MediaCodec.createByCodecName("OMX.google.h264.encoder");
                Dbug.m417i(this.TAG, "use soft encoder");
            } else {
                this.mediaCodec = MediaCodec.createEncoderByType("video/avc");
                Dbug.m417i(this.TAG, "use hard encoder");
            }
            MediaFormat mediaFormatCreateVideoFormat = MediaFormat.createVideoFormat("video/avc", i, i2);
            mediaFormatCreateVideoFormat.setInteger(IjkMediaMeta.IJKM_KEY_BITRATE, 2000000);
            mediaFormatCreateVideoFormat.setInteger("frame-rate", i3);
            mediaFormatCreateVideoFormat.setInteger("color-format", getSupportFormat("video/avc"));
            mediaFormatCreateVideoFormat.setInteger("i-frame-interval", 5);
            this.mediaCodec.configure(mediaFormatCreateVideoFormat, (Surface) null, (MediaCrypto) null, 1);
            this.mediaCodec.start();
            String str = "/REC_" + (System.currentTimeMillis() / 1000) + "_0.mp4";
            String videoPath = AppUtils.getVideoPath(this.context.getActivity());
            File file = new File(videoPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            String str2 = videoPath + str;
            File file2 = new File(str2);
            if (!file2.exists()) {
                file2.createNewFile();
            }
            this.currentVideoPath = str2;
            this.mMuxer = new MediaMuxer(str2, 0);
            this.mTrackIndex = -1;
            this.frameIndex = 0L;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MediaCodecInfo selectCodec(String str) {
        int codecCount = MediaCodecList.getCodecCount();
        for (int i = 0; i < codecCount; i++) {
            MediaCodecInfo codecInfoAt = MediaCodecList.getCodecInfoAt(i);
            if (codecInfoAt.isEncoder()) {
                for (String str2 : codecInfoAt.getSupportedTypes()) {
                    if (str2.equalsIgnoreCase(str)) {
                        return codecInfoAt;
                    }
                }
            }
        }
        return null;
    }

    private int getSupportFormat(String str) {
        MediaCodecInfo mediaCodecInfoSelectCodec = selectCodec(str);
        int[] iArr = mediaCodecInfoSelectCodec != null ? mediaCodecInfoSelectCodec.getCapabilitiesForType(str).colorFormats : null;
        if (iArr == null) {
            return 2135033992;
        }
        for (int i : iArr) {
            if (i == 21) {
                Log.e("CODELIST", "" + i);
                return i;
            }
        }
        for (int i2 : iArr) {
            if (i2 == 19) {
                Log.e("CODELIST", "" + i2);
                this.isYUV420P = true;
                return i2;
            }
        }
        return 2135033992;
    }

    public void startRecorder() {
        this.isRecording = true;
    }

    public int[] bitmap2argb(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] iArr = new int[width * height];
        bitmap.getPixels(iArr, 0, width, 0, 0, width, height);
        return iArr;
    }

    public boolean isRecording() {
        return this.isRecording;
    }

    public void setYUV420P(boolean z) {
        this.isYUV420P = z;
    }

    public boolean isYUV420P() {
        return this.isYUV420P;
    }

    private long computePresentationTime(long j, int i) {
        return ((j * 1000000) / i) + 132;
    }

    public byte[] offerEncoder(byte[] bArr) {
        try {
            ByteBuffer[] inputBuffers = this.mediaCodec.getInputBuffers();
            ByteBuffer[] outputBuffers = this.mediaCodec.getOutputBuffers();
            int iDequeueInputBuffer = this.mediaCodec.dequeueInputBuffer(-1L);
            if (iDequeueInputBuffer >= 0) {
                long jComputePresentationTime = computePresentationTime(this.frameIndex, this.fps);
                ByteBuffer byteBuffer = inputBuffers[iDequeueInputBuffer];
                byteBuffer.clear();
                byteBuffer.put(bArr, 0, bArr.length);
                this.mediaCodec.queueInputBuffer(iDequeueInputBuffer, 0, bArr.length, jComputePresentationTime, 0);
                this.frameIndex++;
            }
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            int iDequeueOutputBuffer = this.mediaCodec.dequeueOutputBuffer(bufferInfo, this.timeoutUSec);
            Log.e("outputBufferIndex=", iDequeueOutputBuffer + "");
            if (iDequeueOutputBuffer == -2) {
                this.mTrackIndex = this.mMuxer.addTrack(this.mediaCodec.getOutputFormat());
                this.mMuxer.start();
            }
            while (iDequeueOutputBuffer >= 0) {
                ByteBuffer byteBuffer2 = outputBuffers[iDequeueOutputBuffer];
                int i = bufferInfo.size;
                byte[] bArr2 = new byte[i];
                byteBuffer2.get(bArr2);
                if (this.spsPpsInfo == null) {
                    if (ByteBuffer.wrap(bArr2).getInt() != 1) {
                        return null;
                    }
                    byte[] bArr3 = new byte[i];
                    this.spsPpsInfo = bArr3;
                    System.arraycopy(bArr2, 0, bArr3, 0, i);
                } else {
                    this.baops.write(bArr2);
                }
                if (bufferInfo.size != 0) {
                    byteBuffer2.position(bufferInfo.offset);
                    byteBuffer2.limit(bufferInfo.offset + bufferInfo.size);
                    this.mMuxer.writeSampleData(this.mTrackIndex, byteBuffer2, bufferInfo);
                }
                this.mediaCodec.releaseOutputBuffer(iDequeueOutputBuffer, false);
                iDequeueOutputBuffer = this.mediaCodec.dequeueOutputBuffer(bufferInfo, this.timeoutUSec);
            }
            byte[] byteArray = this.baops.toByteArray();
            if (byteArray.length > 5 && byteArray[4] == 101) {
                this.baops.reset();
                this.baops.write(this.spsPpsInfo);
                this.baops.write(byteArray);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        byte[] byteArray2 = this.baops.toByteArray();
        this.baops.reset();
        return byteArray2;
    }

    private void YUV420SP2YUV420(byte[] bArr, byte[] bArr2, int i, int i2) {
        int i3;
        if (bArr == null) {
            return;
        }
        int i4 = i * i2;
        int i5 = 0;
        for (int i6 = 0; i6 < i4; i6++) {
            bArr2[i6] = bArr[i6];
        }
        int i7 = 0;
        int i8 = 0;
        while (true) {
            i3 = i4 / 2;
            if (i7 >= i3) {
                break;
            }
            bArr2[i8 + i4] = bArr[i7 + i4];
            i8++;
            i7 += 2;
        }
        for (int i9 = 1; i9 < i3; i9 += 2) {
            bArr2[((i4 * 5) / 4) + i5] = bArr[i9 + i4];
            i5++;
        }
    }

    public void encodeYUV420SP(byte[] bArr, byte[] bArr2, int[] iArr, int i, int i2) {
        int i3 = i * i2;
        int i4 = 0;
        int i5 = 0;
        for (int i6 = 0; i6 < i2; i6++) {
            int i7 = 0;
            while (i7 < i) {
                int i8 = iArr[i4];
                int i9 = (16711680 & i8) >> 16;
                int i10 = (65280 & i8) >> 8;
                int i11 = 255;
                int i12 = i8 & 255;
                int i13 = (((((i9 * 66) + (i10 * 129)) + (i12 * 25)) + 128) >> 8) + 16;
                int i14 = (((((i9 * (-38)) - (i10 * 74)) + (i12 * 112)) + 128) >> 8) + 128;
                int i15 = (((((i9 * 112) - (i10 * 94)) - (i12 * 18)) + 128) >> 8) + 128;
                int i16 = i5 + 1;
                if (i13 < 0) {
                    i13 = 0;
                } else if (i13 > 255) {
                    i13 = 255;
                }
                bArr[i5] = (byte) i13;
                if (i6 % 2 == 0 && i4 % 2 == 0) {
                    int i17 = i3 + 1;
                    if (i14 < 0) {
                        i14 = 0;
                    } else if (i14 > 255) {
                        i14 = 255;
                    }
                    bArr[i3] = (byte) i14;
                    i3 += 2;
                    if (i15 < 0) {
                        i11 = 0;
                    } else if (i15 <= 255) {
                        i11 = i15;
                    }
                    bArr[i17] = (byte) i11;
                }
                i4++;
                i7++;
                i5 = i16;
            }
        }
        if (this.isYUV420P) {
            YUV420SP2YUV420(bArr, bArr2, i, i2);
        }
    }

    public void close() {
        try {
            this.mediaCodec.stop();
            this.mediaCodec.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopRecorder() {
        this.isRecording = false;
        try {
            close();
            MediaMuxer mediaMuxer = this.mMuxer;
            if (mediaMuxer != null) {
                mediaMuxer.stop();
                this.mMuxer.release();
                this.mMuxer = null;
            }
            this.frameIndex = 0L;
            updateGallery();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateGallery() {
        if (this.currentVideoPath.isEmpty()) {
            return;
        }
        File file = new File(this.currentVideoPath);
        if (file.exists()) {
            AppUtils.mediaScanVideo(this.context.getActivity(), file);
        }
    }
}
