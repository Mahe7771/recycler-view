package com.android.recyclerview;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by Maheswari_V on 12/31/2016.
 */

public class ImageCache {

    private LruCache<String, Bitmap> mMemoryCache;
    private static ImageCache imageCache;

    private ImageCache(float memCacheSizePercent) {
        init(memCacheSizePercent);
    }

    public static ImageCache getInstance(float memCacheSizePercent) {
            if(imageCache == null) {
                imageCache = new ImageCache(memCacheSizePercent);
            }
            return imageCache;
    }

    private void init(float memCacheSizePercent) {
        int memCacheSize = calculateMemCacheSize(memCacheSizePercent);
        mMemoryCache = new LruCache<String, Bitmap>(memCacheSize) {

            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                final int bitmapSize = getBitmapSize(bitmap) / 1024;
                return bitmapSize == 0 ? 1 : bitmapSize;
            }
        };
    }

    public void addBitmapToCache(String data, Bitmap bitmap) {
        if (data == null || bitmap == null) {
            return;
        }

        if (mMemoryCache != null && mMemoryCache.get(data) == null) {
            mMemoryCache.put(data, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String data) {
        if (mMemoryCache != null) {
            final Bitmap memBitmap = mMemoryCache.get(data);
            if (memBitmap != null) {
                return memBitmap;
            }
        }
        return null;
    }

    public static int getBitmapSize(Bitmap bitmap) {
        return bitmap.getByteCount();

    }

    public static int calculateMemCacheSize(float percent) {
        if (percent < 0.05f || percent > 0.8f) {
            throw new IllegalArgumentException("setMemCacheSizePercent - percent must be "
                    + "between 0.05 and 0.8");
        }
        return Math.round(percent * Runtime.getRuntime().maxMemory() / 1024);
    }


}