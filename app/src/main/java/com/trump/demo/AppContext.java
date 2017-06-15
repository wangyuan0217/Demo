package com.trump.demo;

import com.judd.trump.app.TApplication;

/**
 * Created by Administrator on 2017/6/15.
 */

public class AppContext extends TApplication {

    @Override
    public void setThemeColor() {
        this.themeColor = R.color.thistle;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
