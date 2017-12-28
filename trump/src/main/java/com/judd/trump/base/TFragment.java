package com.judd.trump.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.judd.trump.widget.commonview.LoadingDialog;
import com.judd.trump.widget.permission.PermissionReq;

/**
 * @author 王元_Trump
 * @desc Fragment的全局基类
 * @time 2016/6/2 14:15
 */
public abstract class TFragment extends Fragment implements View.OnClickListener {

    public View mBaseview;
    public Context mContext;

    protected abstract void bindView();

    protected abstract int getLayoutResId();

    protected abstract void initData(Bundle savedInstanceState);

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //防止mContext为空
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseview = inflater.inflate(getLayoutResId(), null);
        bindView();
        mContext = getActivity();
        initData(savedInstanceState);
        return mBaseview;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mContext = null;
        mBaseview = null;
    }

    @Override
    public void onClick(View v) {
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

    public void gotoActivityForResult(Class<?> toClass, int requsetCode) {
        gotoActivityForResult(toClass, requsetCode, null);
    }

    public void gotoActivityForResult(Class<?> toClass, int requsetCode, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(mContext, toClass);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requsetCode);
    }
    ///////////////////////////
    //activity 跳转 end
    ///////////////////////////

    /////////////////////////////
    // Fragment start
    /////////////////////////////
    protected void instantiateFrament(int containerId, Fragment fgm) {
        try {
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.replace(containerId, fgm);
            ft.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeFragment(Fragment fgm) {
        getChildFragmentManager().beginTransaction().remove(fgm).commit();
    }
    /////////////////////////////
    //Fragment end
    /////////////////////////////


    public void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    public void showLongToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }

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
