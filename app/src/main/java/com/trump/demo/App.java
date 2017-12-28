package com.trump.demo;

import android.app.Application;

/**
 * Created by Administrator on 2017/6/15.
 */

public class App extends Application {

    private App mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

}
