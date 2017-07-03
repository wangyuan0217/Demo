package com.judd.trump.widget.pickerview.helper;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.judd.trump.R;
import com.judd.trump.app.TApplication;
import com.judd.trump.widget.pickerview.lib.LoopView;

import java.util.List;

import static com.judd.trump.widget.pickerview.helper.Utils.getCurrentMonth;
import static com.judd.trump.widget.pickerview.helper.Utils.getCurrentYear;
import static com.judd.trump.widget.pickerview.helper.Utils.getMonthList;
import static com.judd.trump.widget.pickerview.helper.Utils.getYearList;
import static com.judd.trump.widget.pickerview.helper.Utils.setBackgroundAlpha;

/**
 * @author 王元_Trump
 * @time 2017/4/11 14:14
 * @desc 年月的日期控件
 */
public class PopDateHelper_YM extends PopupWindow {

    private Context context;
    private View view;
    private OnClickOkListener onClickOkListener;

    private List<String> list_year, list_month;

    public PopDateHelper_YM(Context context, OnClickOkListener onClickOkListener) {
        super(context);
        this.context = context;
        this.onClickOkListener = onClickOkListener;
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.pop_picker_date, null);
        this.setContentView(view);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setTouchable(true);
        this.setOutsideTouchable(true);

        this.setAnimationStyle(android.R.style.Theme_Material_InputMethod);

        initData();
        initView();
    }

    public void show(View view) {
        setBackgroundAlpha(context, 0.4f);
        showAtLocation(view.getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        list_year = getYearList(Utils.TYPE_NORMAL);
        list_month = getMonthList();
    }

    private void initView() {
        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        Button btnOk = (Button) view.findViewById(R.id.btnOK);
        final LoopView loopView1 = (LoopView) view.findViewById(R.id.loopView1);
        final LoopView loopView2 = (LoopView) view.findViewById(R.id.loopView2);
        view.findViewById(R.id.loopView3).setVisibility(View.GONE);
        btnOk.setTextColor(TApplication.getInstance().getThemeColor());

        loopView1.setItems(list_year);
        loopView2.setItems(list_month);
        loopView1.setNotLoop();
        loopView2.setNotLoop();

        loopView1.setCurrentPosition(getCurrentYear() - 1970);
        loopView2.setCurrentPosition(getCurrentMonth() - 1);

        initListener(btnCancel, btnOk, loopView1, loopView2);
    }

    private void initListener(Button btnCancel, Button btnOk, final LoopView loopView1,
                              final LoopView loopView2) {

        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(context, 1f);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (null != onClickOkListener) {
                            String date = list_year.get(loopView1.getSelectedItem())
                                    + "-" + list_month.get(loopView2.getSelectedItem());

                            date = date.replace("年", "").replace("月", "").replace("日", "");

                            onClickOkListener.onClickOk(date);
                        }
                    }
                }, 300);
            }
        });
    }

    public interface OnClickOkListener {
        void onClickOk(String date);
    }
}
