package com.trump.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.judd.trump.base.TActivity;
import com.judd.trump.ui.TestActivity;

import butterknife.BindView;

public class MainActivity extends TActivity {

    @BindView(R.id.button)
    Button mButton;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initData(Bundle savedInstance) {

        if (null == mButton) {
            showtToastLong("button is null");
        } else {
            mButton.setText("success");
        }
    }

    public void toNext(View view) {
        gotoActivity(TestActivity.class);
    }
}
