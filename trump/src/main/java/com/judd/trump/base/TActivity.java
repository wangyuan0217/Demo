package com.judd.trump.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.judd.trump.R;
import com.judd.trump.util.ActivityManager;
import com.judd.trump.widget.commonview.LoadingDialog;
import com.judd.trump.widget.permission.PermissionReq;

import butterknife.ButterKnife;


/**
 * @author 王元_Trump
 * @desc activity的全局基类
 * @time 2016/6/2 14:15
 */
public abstract class TActivity extends AppCompatActivity
        implements OnClickListener, Toolbar.OnMenuItemClickListener {

    public Context mContext;

    protected abstract void setContentView();

    protected abstract void initData(Bundle savedInstance);

    @Override
    protected void onCreate(Bundle savedInstance) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstance);
        mContext = this;
        ActivityManager.getActivityManager().pushActivity(this);
        beforeSetContentView();
        setContentView();
        ButterKnife.bind(this);
        bindView();

        initData(savedInstance);
    }

    protected void hideActionBar() {
        if (null != getSupportActionBar())
            getSupportActionBar().hide();
    }

    protected void bindView() {
    }

    protected void beforeSetContentView() {
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getActivityManager().popActivity(this);
    }

    public void showRequestError(int code, String errmsg) {
        showToast(errmsg);
        dismissLoadingDialog();
    }

    protected void showLoadingDialog(String message) {
        LoadingDialog.show(mContext, message);
    }

    protected void showLoadingDialog() {
        showLoadingDialog("");
    }

    protected void dismissLoadingDialog() {
        LoadingDialog.dismiss(mContext);
    }

    ///////////////////////////
    //activity 跳转 start
    ///////////////////////////

    public void gotoActivity(Class<?> toClass) {
        gotoActivity(toClass, null);
    }

    public void gotoActivity(Class<?> toClass, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(mContext, toClass);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        mContext.startActivity(intent);
    }

    public void gotoActivityForResult(Class<?> toClass, int requestCode) {
        gotoActivityForResult(toClass, requestCode, null);
    }

    public void gotoActivityForResult(Class<?> toClass, int requestCode, Bundle data) {
        Intent intent = new Intent();
        intent.setClass(mContext, toClass);
        if (null != data) {
            intent.putExtras(data);
        }
        startActivityForResult(intent, requestCode);
    }

    ///////////////////////////
    //activity 跳转 end
    ///////////////////////////


    /////////////////////////////
    //title START
    /////////////////////////////

    public void initTitle() {
        String title = getTitle().toString();
        if (TextUtils.isEmpty(title)) {
            showtToastLong("title text is null");
        }
        initTitle(title, null, 0);
    }

    public void initTitle(String title) {
        initTitle(title, null, 0);
    }

    public void initTitle(String title, String subTitle) {
        initTitle(title, subTitle, 0);
    }

    public void initTitle(String title, int subImgRes) {
        initTitle(title, null, subImgRes);
    }

    public void initTitle(String title, String subTitle, int subImgRes) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar == null) return;
        ((TextView) findViewById(R.id.title)).setText(title);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (null == actionBar) return;
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        toolbar.inflateMenu(R.menu.single_menu);
        toolbar.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        return false;
    }

    public void title_left() {
        finish();
    }

    public void title_right() {
    }

    /////////////////////////////
    //title END
    /////////////////////////////


    ///////////////////////////
    //Toast start
    ///////////////////////////
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showtToastLong(int resId) {
        showtToastLong(getResources().getString(resId));
    }

    public void showtToastLong(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
    ///////////////////////////
    //Toast end
    ///////////////////////////

    @Override
    public void onClick(View view) {

    }

    /////////////////////////////
    // Fragment start
    /////////////////////////////

    protected void instantiateFrament(int containerId, Fragment fgm) {
        try {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(containerId, fgm);
            ft.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addFragment(Fragment fragment, int layoutId, String tag) {
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.add(layoutId, fragment, tag);
//        ft.commit();
        ft.commitAllowingStateLoss();
    }

    /////////////////////////////
    //Fragment end
    /////////////////////////////

    /////////////////////////////
    //6.0 PERMISSIONS START
    /////////////////////////////
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionReq.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    /////////////////////////////
    //6.0 PERMISSIONS END
    /////////////////////////////
}
