package com.thinkive.bank.abslistviewdemo.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.thinkive.bank.abslistviewdemo.R;
import com.thinkive.bank.abslistviewdemo.adapter.GanHuoAdapter;
import com.thinkive.bank.abslistviewdemo.base.BaseActivity;
import com.thinkive.bank.abslistviewdemo.base.RVBaseAdapter;
import com.thinkive.bank.abslistviewdemo.bean.GanHuoBean;
import com.thinkive.bank.abslistviewdemo.decoration.DividerItemDecoration;
import com.thinkive.bank.abslistviewdemo.http.BaseCallback;
import com.thinkive.bank.abslistviewdemo.http.OkHttpHelper;
import com.thinkive.bank.abslistviewdemo.util.DialogHelper;
import com.thinkive.bank.abslistviewdemo.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: sq
 * @date: 2017/9/8
 * @corporation: 深圳市思迪信息技术股份有限公司
 * @description: RecyclerView的使用【包括下拉刷新、上拉加载、点击监听、滑动监听、滚动定位】
 */
public class RecyclerViewActivity extends BaseActivity implements RVBaseAdapter.InnerItemClickCallback {

    private String GANHUO_URL = "http://gank.io/api/search/query/listview/category/Android/count/10/page/";

    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFRESH = 1;
    private static final int STATE_MORE = 2;

    private int moveIndex = 0;
    private int pageIndex = 1;
    private int state = STATE_NORMAL;
    private static boolean isMove = false;

    private TextView mEmptyTextView;
    private RecyclerView recyclerView;
    private MaterialRefreshLayout mRefreshLayout;

    private GanHuoAdapter ganHuoAdapter;
    private LinearLayoutManager layoutManager;
    private List<GanHuoBean.ResultsBean> data = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recycler_view;
    }

    @Override
    protected void findViews() {
        mEmptyTextView = findView(R.id.emptyText);
        recyclerView = findView(R.id.rv_recycler);
        mRefreshLayout = findView(R.id.refresh_layout);
    }

    @Override
    protected void initObjects() {
        ganHuoAdapter = new GanHuoAdapter(context, data, R.layout.recycler_item);
        ganHuoAdapter.setItemClickCallback(this);
    }

    @Override
    protected void initViews() {
        mRefreshLayout.setLoadMore(true);
        recyclerView.setAdapter(ganHuoAdapter);
        recyclerView.setLayoutManager(getLinearLayoutManager());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.addOnScrollListener(new RecyclerViewListener());

//        recyclerView.addItemDecoration(
//                new HorizontalDividerItemDecoration.Builder(this)
//                        .color(Color.GRAY)
//                        .sizeResId(R.dimen.divider)
//                        .marginResId(R.dimen.leftmargin, R.dimen.rightmargin)
//                        .build());

    }

    @Override
    protected void initData() {
        getNetworkData(pageIndex);//发送网络请求，获取数据
    }

    @Override
    protected void setListeners() {

        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                pageIndex = 1;
                state = STATE_REFRESH;
                getNetworkData(pageIndex);

            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                pageIndex++;
                state = STATE_MORE;
                getNetworkData(pageIndex);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClicked(int position) {
        String desc = ganHuoAdapter.getItem(position).getDesc();
        ToastUtils.showToast(this, "点击了-----》" + desc);
        startActivity(XRecyclerViewActivity.class);
    }

    /**
     * 发送网络请求，获取数据
     *
     * @param pageIndex 指定页数
     */
    private void getNetworkData(int pageIndex) {

        OkHttpHelper.getInstance().get(GANHUO_URL + pageIndex, new BaseCallback<GanHuoBean>() {
            @Override
            public void onBeforeRequest(Request request) {

                if (state == STATE_NORMAL) {
                    DialogHelper.showWaitDialog(context, null, "加载中...");
                }
            }

            @Override
            public void onFailure(Request request, Exception e) {

                DialogHelper.hiddenWaitDialog();
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishRefreshLoadMore();
                ToastUtils.showToast(context, "网络异常，请检测网络设置");
            }

            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onSuccess(Response response, GanHuoBean ganHuoBeen) {

                DialogHelper.hiddenWaitDialog();
                if (mEmptyTextView.getVisibility() == View.VISIBLE) {
                    mEmptyTextView.setVisibility(View.GONE);
                }
                if (state == STATE_NORMAL) { //首次加载数据源
                    ganHuoAdapter.addData(ganHuoBeen.getResults());
                    layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                } else if (state == STATE_MORE) {  //加载更多
                    if (ganHuoBeen.getResults() != null && ganHuoBeen.getResults().size() > 0) {
                        moveIndex = ganHuoAdapter.getDatas().size();
                        ganHuoAdapter.loadMoreData(ganHuoBeen.getResults());
                        moveToPosition();//滚动数据源（定位）
                    } else {
                        ToastUtils.showToast(context, "没有更多数据了");
                    }
                    mRefreshLayout.finishRefreshLoadMore();

                } else if (state == STATE_REFRESH) {  //刷新数据源
                    ganHuoAdapter.clear();
                    ganHuoAdapter.addData(ganHuoBeen.getResults());
                    recyclerView.scrollToPosition(0);
                    mRefreshLayout.finishRefresh();
                }

            }

            @Override
            public void onError(Response response, int code, Exception e) {

                DialogHelper.hiddenWaitDialog();
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishRefreshLoadMore();
                ToastUtils.showToast(context, "获取数据异常，请检测服务器配置");
            }
        });
    }

    private RecyclerView.LayoutManager getStaggerLayoutManager() {
        return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }

    /**
     * @return 返回表格布局管理器
     */
    private RecyclerView.LayoutManager getGridLayoutManager() {
        return new GridLayoutManager(this, 2);
    }

    /**
     * @return 返回线性布局管理器
     */
    private RecyclerView.LayoutManager getLinearLayoutManager() {
        return new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    }

    /**
     * 移动recyclerView，进行定位
     */
    public void moveToPosition() {

        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        int firstItem = layoutManager.findFirstVisibleItemPosition();
        int lastItem = layoutManager.findLastVisibleItemPosition();

        //然后区分情况
        if (moveIndex <= firstItem) {
            //当要置顶的项在当前显示的第一个项的前面时
            recyclerView.scrollToPosition(moveIndex);
        } else if (moveIndex <= lastItem) {
            //当要置顶的项已经在屏幕上显示时
            int top = recyclerView.getChildAt(moveIndex - firstItem).getTop();
            recyclerView.scrollBy(0, top);
        } else {
            //当要置顶的项在当前显示的最后一项的后面时
            recyclerView.scrollToPosition(moveIndex);
            //这里这个变量是用在RecyclerView滚动监听里面的
            isMove = true;
        }

    }

    class RecyclerViewListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (isMove && newState == RecyclerView.SCROLL_STATE_IDLE) {
                isMove = false;
                int n = moveIndex - layoutManager.findFirstVisibleItemPosition();
                if (0 <= n && n < recyclerView.getChildCount()) {
                    int top = recyclerView.getChildAt(moveIndex).getTop();
                    recyclerView.smoothScrollBy(0, top);
                }

            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (isMove) {
                isMove = false;
                int n = moveIndex - layoutManager.findFirstVisibleItemPosition();
                if (0 <= n && n < recyclerView.getChildCount()) {
                    int top = recyclerView.getChildAt(n).getTop();
                    recyclerView.scrollBy(0, top);
                }
            }
        }
    }

}
