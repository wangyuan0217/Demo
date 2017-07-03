package com.judd.trump.app;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by Administrator on 2017/4/17.
 * <meta-data
 * android:name="com.judd.trump.app.TGlideModule"
 * android:value="GlideModule" />
 */

public class TGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //Android里有两个方法去解析图片：ARGB8888和RGB565。
        // 第一个为每个像素采用4 byte表示，后面一个则用2 byte表示。
        // ARG8888有更高的图片质量，并且能够存储一个alpha通道。
        // 当Picasso使用ARGB888时，Glide默认使用低质量的RGB565
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);

//        .setMemoryCache(MemoryCache memoryCache)
//                .setBitmapPool(BitmapPool bitmapPool)
//                .setDiskCache(DiskCache.Factory diskCacheFactory)
//                .setDiskCacheService(ExecutorService service)
//                .setResizeService(ExecutorService service)
//                .setDecodeFormat(DecodeFormat decodeFormat)
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        // nothing to do here
    }
}