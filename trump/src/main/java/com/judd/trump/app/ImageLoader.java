package com.judd.trump.app;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.judd.trump.R;


/**
 * 全局的Image显示管理--Glide
 * Created by Trump on 2016/5/24.
 * <p>
 * 不要再非主线程里面使用Glide加载图片，如果真的使用了，请把context参数换成getApplicationContext。
 */
public class ImageLoader {

    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FOREWARD_SLASH = "/";

    public static final int IMAGE_LOADING = R.color.meeeeee;   //loading占位图
    public static final int IMAGE_ERROR = R.color.meeeeee;  //error图

    // 将资源ID转为Uri
    public static Uri resourceIdToUri(Context mContext, int resourceId) {
        return Uri.parse(ANDROID_RESOURCE + mContext.getPackageName() + FOREWARD_SLASH + resourceId);
    }

    public static void load(ImageView imageView, String url, int placeholder, int error) {
        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(placeholder)
                .error(error)
                .crossFade()   //淡入淡出
                .into(imageView);
    }

    //加载gif
    public static void loadGif(ImageView imageView, int resId) {
        Glide.with(imageView.getContext())
                .load(resId)
                .asGif()
                .into(imageView);
    }

    // 加载网络图片
    public static void loadUrl(ImageView imageView, String url) {
        loadUrlWithError(imageView, url, IMAGE_ERROR);
    }

    public static void loadUrlWithError(ImageView imageView, String url, int error) {
        load(imageView, url, IMAGE_LOADING, error);
    }

    // 加载drawable图片
    public static void loadRes(ImageView imageView, int resId) {
        Glide.with(imageView.getContext())
                .load(resourceIdToUri(imageView.getContext(), resId))
                .placeholder(IMAGE_LOADING)
                .error(IMAGE_ERROR)
                .crossFade()
                .into(imageView);
    }

    // 加载本地图片
    public static void loadLocal(ImageView imageView, String path) {
        Glide.with(imageView.getContext())
                .load("file://" + path)
                .placeholder(IMAGE_LOADING)
                .error(IMAGE_ERROR)
                .crossFade()
                .into(imageView);
    }

    // 加载圆角
    public static void loadUrlWithCorner(Context mContext, String url, ImageView imageView, int corner) {
        Glide.with(mContext)
                .load(url)
                .placeholder(IMAGE_LOADING)
                .error(IMAGE_ERROR)
                .crossFade()
                .transform(new GlideRoundTransform(mContext, corner))
                .into(imageView);
    }

    /**
     * Glide-显示圆角图片的转换类
     */
    static class GlideRoundTransform extends BitmapTransformation {

        private float radius = 0f;

        public GlideRoundTransform(Context context) {
            this(context, 4);
        }

        public GlideRoundTransform(Context context, int radiusDp) {
            super(context);
            this.radius = Resources.getSystem().getDisplayMetrics().density * radiusDp;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return roundCrop(pool, toTransform);
        }

        private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
            canvas.drawRoundRect(rectF, radius, radius, paint);
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName() + Math.round(radius);
        }
    }
}

