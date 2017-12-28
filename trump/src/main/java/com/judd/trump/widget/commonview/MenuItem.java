package com.judd.trump.widget.commonview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.judd.trump.R;

/**
 * @author 王元_Trump
 * @time 2017/3/10 14:35
 * @desc 类似设置界面的cell
 */
public class MenuItem extends RelativeLayout {

    RelativeLayout layout;
    ImageView iconLeft;
    TextView menuText;
    TextView subText;
    SwitchButton switchButton;

    MenuClick mMenuClick;
    SwichButtonClick swichButtonClick;

    public static final int DEFAULT_TEXTCOLOR = Color.BLACK;

    public MenuItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MenuItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_menuitem, this);

        initView();

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MenuItemView);
        String text = ta.getString(R.styleable.MenuItemView_text);
        float cellHeight = ta.getDimension(R.styleable.MenuItemView_cellHeight,
                getResources().getDimension(R.dimen.default_menuitem_height));
        float textSize = ta.getDimension(R.styleable.MenuItemView_textSize,
                getResources().getDimension(R.dimen.default_menuitem_textSize));
        int textColor = ta.getColor(R.styleable.MenuItemView_textColor, DEFAULT_TEXTCOLOR);
        float textSize2 = ta.getDimension(R.styleable.MenuItemView_textSize_sub,
                getResources().getDimension(R.dimen.default_menuitem_textSize));
        int textColor2 = ta.getColor(R.styleable.MenuItemView_textColor_sub, DEFAULT_TEXTCOLOR);
        int leftIconId = ta.getResourceId(R.styleable.MenuItemView_leftIcon, -1);
        int rightIconId = ta.getResourceId(R.styleable.MenuItemView_rightIcon, -1);
        String textRight = ta.getString(R.styleable.MenuItemView_rightText);
        boolean showSwitchButton = ta.getBoolean(R.styleable.MenuItemView_showSwitchButton, false);
        ta.recycle();

        //cell高度
        LayoutParams layoutParams = (LayoutParams) layout.getLayoutParams();
        layoutParams.height = (int) cellHeight;
        layout.setLayoutParams(layoutParams);

        //字体大小
        menuText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

        //字体颜色
        menuText.setTextColor(textColor);

        //文字
        menuText.setText(text);

        //左边图标
        if (leftIconId != -1) {
            iconLeft.setVisibility(VISIBLE);
            iconLeft.setBackgroundResource(leftIconId);
        }

        //右侧文字
        if (!TextUtils.isEmpty(textRight)) {
            subText.setText(textRight);
            subText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize2);
            subText.setTextColor(textColor2);
        }

        //右边图标
        if (rightIconId != -1) {
            Drawable nav_up = getResources().getDrawable(rightIconId);
            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
            subText.setCompoundDrawables(null, null, nav_up, null);
        } else {
            subText.setCompoundDrawables(null, null, null, null);
        }

        //swicthButton
        if (showSwitchButton) {
            subText.setVisibility(View.GONE);
            switchButton.setVisibility(View.VISIBLE);
            //点击事件
            switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                    if (swichButtonClick != null)
                        swichButtonClick.toggle(view, isChecked);
                }
            });
        }

        //cell的点击事件
        layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mMenuClick)
                    mMenuClick.click(MenuItem.this);
            }
        });
    }

    public void initView() {
        layout = (RelativeLayout) findViewById(R.id.layout);
        iconLeft = (ImageView) findViewById(R.id.icon_left);
        menuText = (TextView) findViewById(R.id.menu_text);
        subText = (TextView) findViewById(R.id.txt_right);
        switchButton = (SwitchButton) findViewById(R.id.switchButton);
    }

    public void setText(String text) {
        if (menuText != null)
            menuText.setText(text);
    }

    public void setSubText(String text) {
        if (subText != null)
            subText.setText(text);
    }

    public String getSubText() {
        return subText.getText().toString().trim();
    }

    public void setSwitchButton(boolean checked) {
        if (switchButton != null)
            switchButton.setChecked(checked);
    }


    public void setMenuClick(MenuClick mMenuClick) {
        this.mMenuClick = mMenuClick;
    }

    public void setSwitchCheckedChangeListener(SwichButtonClick mSwichButtonClick) {
        this.swichButtonClick = mSwichButtonClick;
    }

    public interface MenuClick {
        void click(MenuItem view);
    }

    public interface SwichButtonClick {
        void toggle(SwitchButton view, boolean isChecked);
    }
}