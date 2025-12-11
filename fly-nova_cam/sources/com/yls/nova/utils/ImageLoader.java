package com.yls.nova.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.LruCache;
import com.yls.nova.C0549R;

/* loaded from: classes.dex */
public class ImageLoader {
    public static final String DEFAULT_PATH = "null";
    private static String TAG = "ImageLoader";
    private static ImageLoader instance;
    private LruCache<String, Bitmap> mImageCache = new LruCache<String, Bitmap>(((int) (Runtime.getRuntime().maxMemory() / 1024)) / 8) { // from class: com.yls.nova.utils.ImageLoader.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.util.LruCache
        public int sizeOf(String str, Bitmap bitmap) {
            return ImageLoader.this.sizeOfBitmap(bitmap);
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public int sizeOfBitmap(Bitmap bitmap) {
        return bitmap.getAllocationByteCount() / 1024;
    }

    public static ImageLoader getInstance() {
        if (instance == null) {
            synchronized (ImageLoader.class) {
                if (instance == null) {
                    instance = new ImageLoader();
                }
            }
        }
        return instance;
    }

    public Bitmap loadImage(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            Bitmap bitmap = this.mImageCache.get(DEFAULT_PATH);
            if (bitmap != null) {
                return bitmap;
            }
            Bitmap bitmapDecodeResource = BitmapFactory.decodeResource(context.getResources(), C0549R.mipmap.ic_default_image);
            this.mImageCache.put(DEFAULT_PATH, bitmapDecodeResource);
            return bitmapDecodeResource;
        }
        Bitmap bitmap2 = this.mImageCache.get(str);
        if (bitmap2 != null) {
            return bitmap2;
        }
        Bitmap bitmapDecodeFile = BitmapFactory.decodeFile(str);
        return bitmapDecodeFile == null ? loadImage(context, null) : bitmapDecodeFile;
    }

    public void clearCache() {
        LruCache<String, Bitmap> lruCache = this.mImageCache;
        if (lruCache != null) {
            lruCache.evictAll();
        }
        System.gc();
    }

    public void release() {
        instance = null;
        clearCache();
    }
}
