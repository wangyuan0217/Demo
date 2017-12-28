package com.judd.trump.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.judd.trump.R;
import com.judd.trump.R2;
import com.judd.trump.base.TActivity;

import butterknife.BindView;

public class TestActivity extends TActivity {


    @BindView(R2.id.textMoudle)
    TextView mText;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_test);
    }

    @Override
    protected void initData(Bundle savedInstance) {
        initTitle();
        mText.setText("if u see this");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_menu, menu);
        //不设置icon  就是纯文本
        //设置了icon  就显示icon
//        menu.findItem(R.id.text).setIcon(R.drawable.ic_arrow_back);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        return true;
    }
}
