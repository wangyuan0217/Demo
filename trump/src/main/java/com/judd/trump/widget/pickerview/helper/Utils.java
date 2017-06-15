package com.judd.trump.widget.pickerview.helper;

import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/3/1.
 */

public class Utils {


    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_BIRTH = 2;  //今天之前
    public static final int TYPE_TODO = 3;   //今天之后

    /**
     * date和今天相比
     * 今天之前return false;
     * 今天之后return true;
     *
     * @param date
     */
    public static boolean afterToday(String date) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            long time = simpleDateFormat.parse(date).getTime();
            long timeToday = simpleDateFormat.parse(getCurrentTime()).getTime();
            if (time == timeToday) return false;
            return time > timeToday;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean beforeToday(String date) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            long time = simpleDateFormat.parse(date).getTime();
            long timeToday = simpleDateFormat.parse(getCurrentTime()).getTime();
            if (time == timeToday) return false;
            return time < timeToday;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static String getCurrentTime() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(System.currentTimeMillis());
    }

    public static List<String> getYearList(int type) {
        List<String> years = new ArrayList<>();
        if (type == 0) type = TYPE_NORMAL;

        Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);

        int maxYear = 0;
        if (type == TYPE_NORMAL)
            maxYear = 1970 + 100;
        else if (type == TYPE_BIRTH)
            maxYear = currentYear + 1;
        else if (type == TYPE_TODO)
            maxYear = currentYear + 100;

        for (int i = 100; i > 0; i--) {
            years.add((maxYear - i) + "年");
        }
        return years;
    }

    public static List<String> getMonthList() {
        List<String> months = new ArrayList<String>();
        for (int i = 1; i <= 12; i++) {
            months.add((i < 10 ? "0" + i : i) + "月");
        }
        return months;
    }

    public static List<String> getDaysList() {
        Calendar c = Calendar.getInstance();
        int day = c.getActualMaximum(Calendar.DATE);
        switch (day) {
            case 28:
                return getDaysList(28);
            case 29:
                return getDaysList(29);
            case 30:
                return getDaysList(30);
            default:
                return getDaysList(31);
        }
    }

    public static List<String> getDaysList(int dayMax) {
        List<String> birthDayList = new ArrayList<>();
        String str = "";
        for (int i = 1; i <= dayMax; i++) {
            str = i < 10 ? "0" + i : i + "";
            birthDayList.add(str + "日");
        }
        return birthDayList;
    }

    public static int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return calendar.get(Calendar.YEAR);
    }

    public static int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 是否是闰年
     *
     * @return
     */
    public static boolean isRunYear(String year) {
        try {
            int _year = Integer.parseInt(year);
            return _year % 4 == 0 && _year % 100 != 0 || _year % 400 == 0;
        } catch (Exception e) {

        }
        return false;
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 屏幕透明度0.0-1.0 1表示完全不透明
     */
    public static void setBackgroundAlpha(Context context, float bgAlpha) {
        if (context == null) return;
        WindowManager.LayoutParams lp = ((Activity) context).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) context).getWindow().setAttributes(lp);
    }

}
