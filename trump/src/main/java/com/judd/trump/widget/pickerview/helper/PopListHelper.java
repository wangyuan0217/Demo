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
import com.judd.trump.widget.pickerview.lib.LoopView;

import java.util.List;

import static com.judd.trump.widget.pickerview.helper.Utils.setBackgroundAlpha;

public class PopListHelper extends PopupWindow {

    private Context context;
    private View view;
    private LoopView loopView;
    private OnClickOkListener onClickOkListener;

    private List<String> list_data;

    public PopListHelper(Context context, List<String> list_data, OnClickOkListener onClickOkListener) {
        super(context);
        this.context = context;
        this.list_data = list_data;
        this.onClickOkListener = onClickOkListener;
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.pop_picker_list, null);
        this.setContentView(view);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setTouchable(true);
        this.setOutsideTouchable(true);

        this.setAnimationStyle(android.R.style.Theme_Material_InputMethod);

        initView();
    }

    public void show(View view) {
        setBackgroundAlpha(context, 0.4f);
        showAtLocation(view.getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void initView() {
        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        Button btnOk = (Button) view.findViewById(R.id.btnOK);
        loopView = (LoopView) view.findViewById(R.id.loopView);

        loopView.setItems(list_data);
        loopView.setNotLoop();

        loopView.setCurrentPosition(0);


        initListener(btnCancel, btnOk);
    }

    private void initListener(Button btnCancel, Button btnOk) {

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
                            String date = list_data.get(loopView.getSelectedItem());
                            onClickOkListener.onClickOk(loopView.getSelectedItem(), date);
                        }
                    }
                }, 300);
            }
        });
    }

    public interface OnClickOkListener {
        void onClickOk(int position, String date);
    }
}
