package com.judd.trump.widget.commonview;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;


/**
 * @author 王元_Trump
 * @time 2017/3/13 14:19
 * @desc 自定义PopWindow类，封装了PopWindow的一些常用属性，用Builder模式支持链式调用
 * github https://github.com/pinguo-zhouwei/CustomPopwindow
 * <p>
 * 使用 example
 * View contentView = LayoutInflater.from(mContext).inflate(R.layout.pop_test, null);
 * CustomPopWindow popWindow = new CustomPopWindow.PopupWindowBuilder(mContext)
 * .setView(contentView)//显示的布局
 * .create();//创建PopupWindow
 * //处理popWindow 显示内容
 * handlePopClick(popWindow, contentView);
 * popWindow.showAtLocation(activityMain, Gravity.BOTTOM, 0, 0);//显示PopupWindow
 * <p>
 * private void handlePopClick(final CustomPopWindow popWindow, View contentView) {
 * View.OnClickListener listener = new View.OnClickListener() {
 * @Override public void onClick(View v) {
 * if (popWindow != null) {
 * popWindow.dissmiss();
 * }
 * String showContent = "";
 * switch (v.getId()) {
 * case R.id.aa:
 * showContent = "点击 Item菜单1";
 * break;
 * case R.id.bb:
 * showContent = "点击 Item菜单2";
 * break;
 * }
 * Toast.makeText(MainActivity.this, showContent, Toast.LENGTH_SHORT).show();
 * }
 * };
 * contentView.findViewById(R.id.aa).setOnClickListener(listener);
 * contentView.findViewById(R.id.bb).setOnClickListener(listener);
 * }
 */
public class CustomPopWindow {
    private Context mContext;
    private int mWidth;
    private int mHeight;
    private boolean mIsFocusable = true;
    private boolean mIsOutside = true;
    private int mResLayoutId = -1;
    private View mContentView;
    private PopupWindow mPopupWindow;
    private int mAnimationStyle = -1;

    private boolean mClippEnable = true;//default is true
    private boolean mIgnoreCheekPress = false;
    private int mInputMode = -1;
    private PopupWindow.OnDismissListener mOnDismissListener;
    private int mSoftInputMode = -1;
    private boolean mTouchable = true;//default is ture
    private View.OnTouchListener mOnTouchListener;

    private CustomPopWindow(Context context) {
        mContext = context;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    /**
     * @param anchor
     * @param xOff
     * @param yOff
     * @return
     */
    public CustomPopWindow showAsDropDown(View anchor, int xOff, int yOff) {
        if (mPopupWindow != null) {
            mPopupWindow.showAsDropDown(anchor, xOff, yOff);
        }
        return this;
    }

    public CustomPopWindow showAsDropDown(View anchor) {
        if (mPopupWindow != null) {
            mPopupWindow.showAsDropDown(anchor);
        }
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public CustomPopWindow showAsDropDown(View anchor, int xOff, int yOff, int gravity) {
        if (mPopupWindow != null) {
            mPopupWindow.showAsDropDown(anchor, xOff, yOff, gravity);
        }
        return this;
    }


    /**
     * 相对于父控件的位置（通过设置Gravity.CENTER，下方Gravity.BOTTOM等 ），可以设置具体位置坐标
     *
     * @param parent
     * @param gravity
     * @param x       the popup's x location offset
     * @param y       the popup's y location offset
     * @return
     */
    public CustomPopWindow showAtLocation(View parent, int gravity, int x, int y) {
        if (mPopupWindow != null) {
            mPopupWindow.showAtLocation(parent, gravity, x, y);
        }
        return this;
    }

    /**
     * 添加一些属性设置
     *
     * @param popupWindow
     */
    private void apply(PopupWindow popupWindow) {
        popupWindow.setClippingEnabled(mClippEnable);
        if (mIgnoreCheekPress) {
            popupWindow.setIgnoreCheekPress();
        }
        if (mInputMode != -1) {
            popupWindow.setInputMethodMode(mInputMode);
        }
        if (mSoftInputMode != -1) {
            popupWindow.setSoftInputMode(mSoftInputMode);
        }
        if (mOnDismissListener != null) {
            popupWindow.setOnDismissListener(mOnDismissListener);
        } else {
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    backgroundAlpha(mContext, 1f);
                }
            });
        }
        if (mOnTouchListener != null) {
            popupWindow.setTouchInterceptor(mOnTouchListener);
        }
        popupWindow.setTouchable(mTouchable);
    }

    private PopupWindow build() {

        if (mContentView == null) {
            mContentView = LayoutInflater.from(mContext).inflate(mResLayoutId, null);
        }

        if (mWidth != 0 && mHeight != 0) {
            mPopupWindow = new PopupWindow(mContentView, mWidth, mHeight);
        } else {
            mPopupWindow = new PopupWindow(mContentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        if (mAnimationStyle != -1) {
            mPopupWindow.setAnimationStyle(mAnimationStyle);
        }

        apply(mPopupWindow);//设置一些属性

        mPopupWindow.setFocusable(mIsFocusable);
        //// 实例化一个ColorDrawable颜色为半透明
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(00000000));
        mPopupWindow.setOutsideTouchable(mIsOutside);
        backgroundAlpha(mContext, 0.4f);

        if (mWidth == 0 || mHeight == 0) {
            mPopupWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            //如果外面没有设置宽高的情况下，计算宽高并赋值
            mWidth = mPopupWindow.getContentView().getMeasuredWidth();
            mHeight = mPopupWindow.getContentView().getMeasuredHeight();
        }

        mPopupWindow.update();

        return mPopupWindow;
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(Context mContext, float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

    /**
     * 关闭popWindow
     */
    public void dissmiss() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }


    public static class PopupWindowBuilder {
        private CustomPopWindow mCustomPopWindow;

        public PopupWindowBuilder(Context context) {
            mCustomPopWindow = new CustomPopWindow(context);
        }

        public PopupWindowBuilder size(int width, int height) {
            mCustomPopWindow.mWidth = width;
            mCustomPopWindow.mHeight = height;
            return this;
        }


        public PopupWindowBuilder setFocusable(boolean focusable) {
            mCustomPopWindow.mIsFocusable = focusable;
            return this;
        }


        public PopupWindowBuilder setView(int resLayoutId) {
            mCustomPopWindow.mResLayoutId = resLayoutId;
            mCustomPopWindow.mContentView = null;
            return this;
        }

        public PopupWindowBuilder setView(View view) {
            mCustomPopWindow.mContentView = view;
            mCustomPopWindow.mResLayoutId = -1;
            return this;
        }

        public PopupWindowBuilder setOutsideTouchable(boolean outsideTouchable) {
            mCustomPopWindow.mIsOutside = outsideTouchable;
            return this;
        }

        /**
         * 设置弹窗动画
         *
         * @param animationStyle
         * @return
         */
        public PopupWindowBuilder setAnimationStyle(int animationStyle) {
            mCustomPopWindow.mAnimationStyle = animationStyle;
            return this;
        }


        public PopupWindowBuilder setClippingEnable(boolean enable) {
            mCustomPopWindow.mClippEnable = enable;
            return this;
        }


        public PopupWindowBuilder setIgnoreCheekPress(boolean ignoreCheekPress) {
            mCustomPopWindow.mIgnoreCheekPress = ignoreCheekPress;
            return this;
        }

        public PopupWindowBuilder setInputMethodMode(int mode) {
            mCustomPopWindow.mInputMode = mode;
            return this;
        }

        public PopupWindowBuilder setOnDissmissListener(PopupWindow.OnDismissListener onDissmissListener) {
            mCustomPopWindow.mOnDismissListener = onDissmissListener;
            return this;
        }


        public PopupWindowBuilder setSoftInputMode(int softInputMode) {
            mCustomPopWindow.mSoftInputMode = softInputMode;
            return this;
        }


        public PopupWindowBuilder setTouchable(boolean touchable) {
            mCustomPopWindow.mTouchable = touchable;
            return this;
        }

        public PopupWindowBuilder setTouchIntercepter(View.OnTouchListener touchIntercepter) {
            mCustomPopWindow.mOnTouchListener = touchIntercepter;
            return this;
        }


        public CustomPopWindow create() {
            //构建PopWindow
            mCustomPopWindow.build();
            return mCustomPopWindow;
        }

    }

}