package com.judd.trump.app;

import android.app.Application;

import com.judd.trump.util.PrefUtil;

import static com.judd.trump.app.Config.APP_NAME_EN;

/**
 * Created by Administrator on 2017/12/6.
 */

public class TApp extends Application {
    private static TApp mInstance;

    protected static PrefUtil mPrefUtil;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static TApp getInstance() {
        return mInstance;
    }

    public static PrefUtil getPref() {
        if (null == mPrefUtil) {
            mPrefUtil = new PrefUtil(mInstance, APP_NAME_EN, MODE_PRIVATE);
        }
        return mPrefUtil;
    }

}
