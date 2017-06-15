package com.judd.trump.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.WindowManager;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author 王元_Trump
 * @desc ${DESC}
 * @time 2016/10/12 15:32
 */
public class CommonUtil {

    public static String formatDoule(double money) {
        return new java.text.DecimalFormat("0.00").format(money);
    }

    public static String formatDoule(String money) {
        try {
            return new java.text.DecimalFormat("0.00").format(Double.parseDouble(money));
        } catch (Exception e) {
            return "";
        }
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().densityDpi;
        return (int) (dipValue * (scale / 160));
    }

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().densityDpi;
        return (int) ((pxValue * 160) / scale);
    }

    public static void backgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }


    /**
     * 从assets中读取txt
     */
    public static String getConfigJSON(Context context, String fileName) {
        BufferedInputStream bis = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            bis = new BufferedInputStream(context.getAssets().open(fileName));
            byte[] buffer = new byte[4096];
            int readLen = -1;
            while ((readLen = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, readLen);
            }
        } catch (IOException e) {
            Log.e("", "IOException :" + e.getMessage());
        } finally {
            try {
                if (bis != null)
                    bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bos.toString();
    }
}
