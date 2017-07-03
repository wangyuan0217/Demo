package com.judd.trump.widget.pickerview.helper;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.judd.trump.R;
import com.judd.trump.app.TApplication;
import com.judd.trump.widget.pickerview.lib.LoopView;
import com.judd.trump.widget.pickerview.lib.OnItemSelectedListener;

import java.util.List;

import static com.judd.trump.widget.pickerview.helper.Utils.TYPE_BIRTH;
import static com.judd.trump.widget.pickerview.helper.Utils.TYPE_NORMAL;
import static com.judd.trump.widget.pickerview.helper.Utils.TYPE_TODO;
import static com.judd.trump.widget.pickerview.helper.Utils.afterToday;
import static com.judd.trump.widget.pickerview.helper.Utils.beforeToday;
import static com.judd.trump.widget.pickerview.helper.Utils.getCurrentDay;
import static com.judd.trump.widget.pickerview.helper.Utils.getCurrentMonth;
import static com.judd.trump.widget.pickerview.helper.Utils.getCurrentYear;
import static com.judd.trump.widget.pickerview.helper.Utils.getDaysList;
import static com.judd.trump.widget.pickerview.helper.Utils.getMonthList;
import static com.judd.trump.widget.pickerview.helper.Utils.getYearList;
import static com.judd.trump.widget.pickerview.helper.Utils.isRunYear;
import static com.judd.trump.widget.pickerview.helper.Utils.setBackgroundAlpha;

public class PopDateHelper extends PopupWindow {

    private Context context;
    private View view;
    private OnClickOkListener onClickOkListener;

    private List<String> list_year, list_month, list_day;

    private int select_type;

    public PopDateHelper(Context context, OnClickOkListener onClickOkListener) {
        this(context, TYPE_NORMAL, onClickOkListener);
    }

    public PopDateHelper(Context context, int type, OnClickOkListener onClickOkListener) {
        super(context);
        this.context = context;
        this.onClickOkListener = onClickOkListener;
        this.select_type = type;
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
        list_year = getYearList(select_type);
        list_month = getMonthList();
        list_day = getDaysList();
    }

    private void initView() {
        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        Button btnOk = (Button) view.findViewById(R.id.btnOK);
        final LoopView loopView1 = (LoopView) view.findViewById(R.id.loopView1);
        final LoopView loopView2 = (LoopView) view.findViewById(R.id.loopView2);
        final LoopView loopView3 = (LoopView) view.findViewById(R.id.loopView3);
        btnOk.setTextColor(TApplication.getInstance().getThemeColor());

        loopView1.setItems(list_year);
        loopView2.setItems(list_month);
        loopView3.setItems(list_day);
        loopView1.setNotLoop();
        loopView2.setNotLoop();
        loopView3.setNotLoop();

        loopView1.setCurrentPosition(select_type == TYPE_NORMAL ?
                (getCurrentYear() - 1970) : (select_type == TYPE_BIRTH ? list_year.size() - 1 : 0));
        loopView2.setCurrentPosition(getCurrentMonth() - 1);
        loopView3.setCurrentPosition(getCurrentDay() - 1);

        initListener(btnCancel, btnOk, loopView1, loopView2, loopView3);
    }

    private void initListener(Button btnCancel, Button btnOk, final LoopView loopView1,
                              final LoopView loopView2, final LoopView loopView3) {
        loopView1.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                String year = list_year.get(index).replace("年", "");
                String month = list_month.get(loopView2.getSelectedItem()).replace("月", "");
                if (!TextUtils.isEmpty(month) && month.equals("02")) {
                    if (isRunYear(year) && list_day.size() != 29) {
                        list_day = getDaysList(29);
                    } else if (!isRunYear(year) && list_day.size() != 28) {
                        list_day = getDaysList(28);
                    }
                    loopView3.setItems(list_day);
                    loopView3.setCurrentPosition(0);
                }
            }
        });
        loopView2.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                String year = list_year.get(loopView1.getSelectedItem()).replace("年", "");
                String month = list_month.get(index).replace("月", "");
                if (month.equals("02")) {
                    if (!TextUtils.isEmpty(year) && isRunYear(year) && list_day.size() != 29) {
                        list_day = getDaysList(29);
                    } else if (!TextUtils.isEmpty(year) && !isRunYear(year) && list_day.size() != 28) {
                        list_day = getDaysList(28);
                    }
                } else if ((month.equals("01") || month.equals("03") || month.equals("05") || month.equals("07")
                        || month.equals("08") || month.equals("10") || month.equals("12")) && list_day.size() != 31) {
                    list_day = getDaysList(31);
                } else if ((month.equals("04") || month.equals("06") || month.equals("09") || month.equals("11"))
                        && list_day.size() != 30) {
                    list_day = getDaysList(30);
                }
                loopView3.setItems(list_day);
                int position = loopView3.getSelectedItem();
                loopView3.setCurrentPosition(position > list_day.size() - 1 ? list_day.size() - 1 : position);
            }
        });

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
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (null != onClickOkListener) {
                            String date = list_year.get(loopView1.getSelectedItem())
                                    + "-" + list_month.get(loopView2.getSelectedItem())
                                    + "-" + list_day.get(loopView3.getSelectedItem());
                            date = date.replace("年", "").replace("月", "").replace("日", "");

                            if (select_type == TYPE_BIRTH && afterToday(date)) {
                                Toast.makeText(context, "选择日期不能超过今天", Toast.LENGTH_SHORT).show();
                            } else if (select_type == TYPE_TODO && beforeToday(date)) {
                                Toast.makeText(context, "选择日期不能早于今天", Toast.LENGTH_SHORT).show();
                            } else {
                                onClickOkListener.onClickOk(date);
                                dismiss();
                            }
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
