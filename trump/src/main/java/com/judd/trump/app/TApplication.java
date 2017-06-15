package com.judd.trump.app;

import android.app.Application;

/**
 * Created by Administrator on 2017/6/15.
 */

public abstract class TApplication extends Application {

    protected int themeColor;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public abstract void setThemeColor();

    public int getThemeColor() {
        return themeColor;
    }
}
