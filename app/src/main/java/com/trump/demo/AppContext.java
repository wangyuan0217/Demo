package com.trump.demo;

import com.judd.trump.app.TApplication;

/**
 * Created by Administrator on 2017/6/15.
 */

public class AppContext extends TApplication {

    @Override
    public int setThemeColor() {
        return R.color.colorPrimaryDark;
    }

    @Override
    public int setTitleBackImg() {
        return R.mipmap.icon_back;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }


}
