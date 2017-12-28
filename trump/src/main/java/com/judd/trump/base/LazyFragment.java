package com.judd.trump.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author 王元_Trump
 * @time 2017/7/3 14:43
 * @desc LazyFragment
 * @Override use
 * protected void initData() {
 * if (!isPrepared || !isVisible || isLoad) {
 * return;
 * }
 * <p>
 * //这里加载数据
 * progressBar();
 * <p>
 * isLoad = true;
 * }
 */
public abstract class LazyFragment extends TFragment {

    boolean isPrepared;
    boolean isLoad;
    boolean isVisible;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null == view) {
            view = inflater.inflate(getLayoutResId(), null);
            isPrepared = true;
        }
        bindView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    private void onVisible() {
        initData();
    }


    private void onInvisible() {
    }

    protected abstract int getLayoutResId();

    protected abstract void bindView();

    protected abstract void initData();

}