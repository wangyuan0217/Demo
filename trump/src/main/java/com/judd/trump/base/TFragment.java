package com.judd.trump.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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


    /////////////////////////////
    //AlertDialog  start
    /////////////////////////////
    public void showAlertDialog(String message, DialogInterface.OnClickListener positiveListener) {
        showAlertDialog("提示", message, "确定", "取消", positiveListener, null);
    }

    public void showAlertDialog(String title, String message, String positiveTip, String negativeTip,
                                DialogInterface.OnClickListener positiveListener,
                                DialogInterface.OnClickListener nagetiveListener) {
        AlertDialog dialog = new AlertDialog.Builder(mContext)
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

    public void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    public void showLongToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }
}
