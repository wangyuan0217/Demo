package com.judd.trump.app;

import android.app.Application;
import android.support.v4.content.ContextCompat;

/**
 * Created by Administrator on 2017/6/15.
 */

public abstract class TApplication extends Application {


    protected static TApplication mInstance;
    protected int themeColor = setThemeColor();
    protected int titleBackImg = setTitleBackImg();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static TApplication getInstance() {
        return mInstance;
    }

    public abstract int setThemeColor();

    public abstract int setTitleBackImg();

    public int getThemeColor() {
        return ContextCompat.getColor(mInstance, themeColor);
    }

    public int getTitleBackImg() {
        return titleBackImg;
    }
}
