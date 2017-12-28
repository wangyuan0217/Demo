package com.judd.trump.base;

import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.judd.trump.R;
import com.judd.trump.app.Config;
import com.judd.trump.http.MyCallback;
import com.judd.trump.util.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import static com.judd.trump.app.Config.PAGE_FROM;

public abstract class BaseRefreshFragment<CELL, LIST_CELL> extends TFragment
        implements SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.RequestLoadMoreListener,
        BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener {

    private int pageSize;
    protected int currentPage = PAGE_FROM;

    protected BaseQuickAdapter mAdapter;
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    private boolean canRefresh = setCanRefresh();
    private boolean canLoadMore = setCanLoadMore();

    protected abstract void loadData();

    protected abstract int getCellLayout();

    protected abstract void setCellData(BaseViewHolder viewHolder, CELL item);

    public boolean setCanRefresh() {
        return true;
    }

    public boolean setCanLoadMore() {
        return true;
    }

    protected class QuickAdapter extends BaseQuickAdapter<CELL, BaseViewHolder> {
        public QuickAdapter() {
            super(getCellLayout(), new ArrayList<CELL>());
        }

        @Override
        protected void convert(BaseViewHolder viewHolder, CELL item) {
            setCellData(viewHolder, item);
        }
    }

    @Override
    protected void bindView() {
        if (canRefresh) {
            try {
                mSwipeRefreshLayout = (SwipeRefreshLayout) mBaseview.findViewById(R.id.swipeRefreshLayout);
                mSwipeRefreshLayout.setOnRefreshListener(this);
                mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.mThemeColor));
            } catch (Exception e) {
                showToast("no swipe layout");
            }
        }
    }

    @Override
    public void showRequestError(int responeStatus, String errmsg) {
        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing())
            mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.loadMoreComplete();
        super.showRequestError(responeStatus, errmsg);
    }

    protected void autoGetData() {
        currentPage = PAGE_FROM;
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                loadData();
            }
        });
    }

    protected void updateAfterLoad(List<CELL> model) {
        dismissLoadingDialog();
        mAdapter.loadMoreComplete();

        if (currentPage == PAGE_FROM)
            mAdapter.setNewData(model);
        else
            mAdapter.addData(model);

        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing())
            mSwipeRefreshLayout.setRefreshing(false);

        if (model.size() == 0) {
            mAdapter.loadMoreEnd(Config.HIDE_LIST_END_TIP);
            return;
        }

        if (currentPage == PAGE_FROM)
            pageSize = model.size();
        else if (model.size() < pageSize)
            mAdapter.loadMoreEnd(Config.HIDE_LIST_END_TIP);
    }

    @Override
    public void onRefresh() {
        currentPage = PAGE_FROM;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        }, Config.REFRESH_DELAY);
    }

    //加载更多回调
    @Override
    public void onLoadMoreRequested() {
        currentPage++;
        loadData();
    }


    public abstract void onSuccess(LIST_CELL model, String message, int code);

    protected MyCallback myCallback = new MyCallback<LIST_CELL>() {
        @Override
        public void onSuccess(LIST_CELL model, String message, int code) {
            BaseRefreshFragment.this.onSuccess(model, message, code);
        }

        @Override
        public void onFailure(int responseStatus, String errMsg, LIST_CELL model) {
            // TODO: 2017/12/11
//            if (responseStatus == 202) {
//                //无数据
//                if (currentPage == PAGE_FROM) {
//                    mAdapter.getData().clear();
//                    mAdapter.notifyDataSetChanged();
//                }
//            if (responseStatus == StatusCode.ERROR_NODATA)
//                mAdapter.setNewData(new ArrayList());
//            mAdapter.loadMoreEnd(SHOW_LIST_END_TIP);
//            if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing())
//                mSwipeRefreshLayout.setRefreshing(false);
////            } else {
//            showRequestError(responseStatus, errMsg);
//            }
        }
    };

    protected void initAdapter(RecyclerView mRecyclerView) {
        initAdapter(mRecyclerView, 0);
    }

    protected void initAdapter(RecyclerView mRecyclerView, int gapSize) {
        initAdapter(mRecyclerView, gapSize, 0);
    }

    protected void initAdapter(RecyclerView mRecyclerView, int gapSize, int emptyRes) {
        initAdapter(mRecyclerView, true, gapSize, emptyRes);
    }

    protected void initAdapter(RecyclerView mRecyclerView, boolean isVertical,
                               int gapSize, int emptyRes) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(isVertical ? LinearLayoutManager.VERTICAL : LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);
        if (gapSize > 0) {
            mRecyclerView.addItemDecoration(new RecycleViewDivider(mContext,
                    isVertical ? LinearLayoutManager.VERTICAL : LinearLayoutManager.HORIZONTAL,
                    gapSize, ContextCompat.getColor(mContext, R.color.recyclerDividerColor)));
        }
        mRecyclerView.setAdapter(mAdapter = new QuickAdapter());

        if (emptyRes != 0) {
        }

        //loadmore
        mAdapter.setEnableLoadMore(canLoadMore);
        if (canLoadMore) {
            mAdapter.setOnLoadMoreListener(this, mRecyclerView);
        }
        //click
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemChildClickListener(this);

        //默认第一次加载会进入回调，如果不需要可以配置：
        mAdapter.disableLoadMoreIfNotFullPage();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

}
