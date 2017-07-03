package com.trump.demo;

import android.os.Bundle;
import android.view.View;

import com.judd.trump.base.TActivity;
import com.judd.trump.ui.WebActivity;

public class MainActivity extends TActivity {

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void bindView() {

    }

    @Override
    protected void initData(Bundle savedInstance) {

    }

    public void test(View view) {
        WebActivity.open(this, "http://www.baidu.com");
    }

    public void test2(View view) {
        gotoActivity(NavigationActivity.class);
    }
}
