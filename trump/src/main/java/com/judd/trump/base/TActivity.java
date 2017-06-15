package com.judd.trump.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.judd.trump.R;
import com.judd.trump.app.ActivityManager;
import com.judd.trump.widget.permission.PermissionReq;


/**
 * @author 王元_Trump
 * @desc activity的全局基类
 * @time 2016/6/2 14:15
 */
public abstract class TActivity extends AppCompatActivity
        implements OnClickListener {


    public Context mContext;

    protected abstract int getTitleBarColor();

    protected abstract int getTitleBackImg();

    protected abstract void setContentView();

    protected abstract void bindView();

    protected abstract void initData(Bundle savedInstance);

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        mContext = this;
        ActivityManager.getActivityManager().pushActivity(this);

        setContentView();
        bindView();

        initData(savedInstance);
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
    //AlertDialog  start
    /////////////////////////////
    public void showAlertDialog(String message, DialogInterface.OnClickListener positiveListener) {
        showAlertDialog("提示", message, "确定", "取消", positiveListener, null);
    }

    public void showAlertDialog(String title, String message, String positiveTip, String negativeTip,
                                DialogInterface.OnClickListener positiveListener,
                                DialogInterface.OnClickListener nagetiveListener) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveTip, positiveListener)
                .setNegativeButton(negativeTip, nagetiveListener)
                .create();
        dialog.show();
    }
    /////////////////////////////
    //AlertDialog end
    /////////////////////////////


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
    //title START
    /////////////////////////////

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
        findViewById(R.id.layout_title).setBackgroundColor(
                ContextCompat.getColor(mContext, getTitleBarColor()));
        findViewById(R.id.title_back).setBackgroundResource(getTitleBackImg());
        ((TextView) findViewById(R.id.title_tv)).setText(title);
        if (!TextUtils.isEmpty(subTitle))
            ((TextView) findViewById(R.id.title_right_tv)).setText(subTitle);
        if (0 != subImgRes) {
            findViewById(R.id.title_right_img).setVisibility(View.VISIBLE);
            ((ImageView) findViewById(R.id.title_right_img)).setImageResource(subImgRes);
        }
        findViewById(R.id.title_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                title_left();
            }
        });
        findViewById(R.id.title_right_tv).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                title_right();
            }
        });
        findViewById(R.id.title_right_img).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                title_right();
            }
        });
    }

    public void title_left() {
        finish();
    }

    public void title_right() {
    }

    /////////////////////////////
    //title END
    /////////////////////////////

    /////////////////////////////
    //6.0 PERMISSIONS START
    /////////////////////////////
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionReq.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    /////////////////////////////
    //6.0 PERMISSIONS END
    /////////////////////////////
}
