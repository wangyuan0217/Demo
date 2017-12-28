package com.judd.trump.base;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.LubanOptions;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.judd.trump.app.Config;
import com.judd.trump.ui.view.OptionsPopupDialog;

import java.io.File;

public abstract class TakePhotoActivity extends TActivity
        implements TakePhoto.TakeResultListener, InvokeListener {


    private static final String TAG = com.jph.takephoto.app.TakePhotoActivity.class.getName();

    private TakePhoto takePhoto;
    private InvokeParam invokeParam;

    protected abstract void uploadSuccess(String url);

    public void selectPics() {
        final String aaaa[] = {"拍照", "图库选择"};
        OptionsPopupDialog.newInstance(mContext, aaaa)
                .setOptionsPopupDialogListener(new OptionsPopupDialog.OnOptionsItemClickedListener() {
                    public void onOptionsItemClicked(int i) {
                        if (i == 0) {
                            takePhoto.onEnableCompress(getCompressOptions(), true);
                            getTakePhoto().onPickFromCapture(getUri());
                        } else {
                            takePhoto.onEnableCompress(getCompressOptions(), true);
                            getTakePhoto().onPickFromGallery();
                        }
                    }
                }).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    protected CompressConfig getCompressOptions() {
        LubanOptions option = new LubanOptions.Builder()
                .setMaxHeight(1000)
                .setMaxWidth(1000)
                .setMaxSize(100 * 1024)
                .create();
        return CompressConfig.ofLuban(option);
    }

    @Override
    public void takeSuccess(TResult result) {
        Log.i(TAG, "takeSuccess：" + result.getImage().getCompressPath());
        // TODO: 2017/12/12 增加 upload
        //uploadSuccess("");
    }

    @Override
    public void takeFail(TResult result, String msg) {
        Log.i(TAG, "takeFail:" + msg);
    }

    @Override
    public void takeCancel() {
        Log.i(TAG, "takeCancel:takeCancel");
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager
                .checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    public Uri getUri() {
        File file = new File(Environment.getExternalStorageDirectory(), "/" + Config.APP_NAME_EN
                + "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        return Uri.fromFile(file);
    }
}
