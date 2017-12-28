package com.judd.trump.ui.common_activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.judd.trump.R;
import com.judd.trump.base.TActivity;
import com.judd.trump.ui.view.ViewPagerFixed;
import com.judd.trump.widget.permission.PermissionReq;
import com.judd.trump.widget.permission.PermissionResult;
import com.judd.trump.widget.permission.Permissions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

public class PhotoBrowserActivity extends TActivity {

    ViewPagerFixed mViewPagerFixed;
    PhotoViewPageAdapter mPhotoViewPageAdapter;
    TextView mHint, mSave;

    ArrayList<String> images;
    int currentPosition;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_photo_browser);
    }

    @Override
    protected void initData(Bundle savedInstance) {
        mViewPagerFixed = (ViewPagerFixed) findViewById(R.id.viewPager);
        mHint = (TextView) findViewById(R.id.hint);
        mSave = (TextView) findViewById(R.id.save);

        //获取data
        Intent intent = getIntent();
        images = intent.getStringArrayListExtra("images");
        currentPosition = intent.getIntExtra("position", 0);
        mHint.setText(currentPosition + 1 + "/" + images.size());

        //设置ViewPager
        mPhotoViewPageAdapter = new PhotoViewPageAdapter(this, images);
        mViewPagerFixed.setAdapter(mPhotoViewPageAdapter);
        mViewPagerFixed.setCurrentItem(currentPosition);
        mViewPagerFixed.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                mHint.setText(position + 1 + "/" + images.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void saveImage(final View view) {
        PermissionReq.with(this)
                .permissions(Permissions.STORAGE)
                .result(new PermissionResult() {
                    @Override
                    public void onGranted() {
                        Glide.with(view.getContext()).load(images.get(currentPosition)).asBitmap().into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                                // 创建目录
                                File appDir = new File(Environment.getExternalStorageDirectory(), "JellyImage");
                                if (!appDir.exists()) {
                                    appDir.mkdir();
                                }

                                String imageType = getImageType(images.get(currentPosition)); //获取图片类型
                                String fileName = System.currentTimeMillis() + "." + imageType;
                                File file = new File(appDir, fileName);
                                //保存图片
                                try {
                                    FileOutputStream fos = new FileOutputStream(file);
                                    if (TextUtils.equals(imageType, "jpg"))
                                        imageType = "jpeg";
                                    imageType = imageType.toUpperCase();
                                    bitmap.compress(Bitmap.CompressFormat.valueOf(imageType), 100, fos);
                                    fos.flush();
                                    fos.close();
                                    Toast.makeText(view.getContext(), "保存成功", Toast.LENGTH_SHORT).show(); //Toast
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                // 其次把文件插入到系统图库
                                try {
                                    MediaStore.Images.Media.insertImage(view.getContext().getContentResolver(), file.getAbsolutePath(), fileName, null);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                // 最后通知图库更新
                                view.getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getPath())));
                            }
                        }); //方法中设置asBitmap可以设置回调类型
                    }

                    @Override
                    public void onDenied() {
                        showToast("缺少读写权限，无法更新");
                    }
                })
                .request();
    }

    public String getImageType(String imageUrl) {
        String[] imageTypes = new String[]{".jpg", ".png", ".jpeg", "webp"};
        String imageType = "";
        if (imageUrl.endsWith(imageTypes[0])) {
            imageType = "jpg";
        } else if (imageUrl.endsWith(imageTypes[1])) {
            imageType = "png";
        } else {
            imageType = "jpeg";
        }
        return imageType;
    }

    public static void startActivity(Context context, ArrayList<String> images, int position) {
        Intent intent = new Intent(context, PhotoBrowserActivity.class);
        intent.putStringArrayListExtra("images", images);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

    class PhotoViewPageAdapter extends PagerAdapter {
        private Context context;
        private List<String> images;
        private SparseArray<View> cacheView;
        private ViewGroup containerTemp;

        public PhotoViewPageAdapter(Context context, List<String> images) {
            this.context = context;
            this.images = images;
            cacheView = new SparseArray<>(images.size());
        }

        @Override
        public Object instantiateItem(final ViewGroup container, int position) {
            if (containerTemp == null) containerTemp = container;

            View view = cacheView.get(position);

            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.item_photobrowse, container, false);
                view.setTag(position);
                final ImageView image = (ImageView) view.findViewById(R.id.imageView);
                final PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(image);

                Glide.with(context)
                        .load(images.get(position))
                        .into(new GlideDrawableImageViewTarget(image) {
                            @Override
                            public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                                super.onResourceReady(drawable, anim);
                                //在这里添加一些图片加载完成的操作
                                photoViewAttacher.update();
                            }

                        });


                photoViewAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                    @Override
                    public void onPhotoTap(View view, float x, float y) {
                        Activity activity = (Activity) context;
                        activity.finish();
                    }
                });
                cacheView.put(position, view);
            }
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }

    }
}
