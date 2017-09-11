package com.thinkive.bank.abslistviewdemo.activity;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.thinkive.bank.abslistviewdemo.R;
import com.thinkive.bank.abslistviewdemo.adapter.SubGanHuoAdapter;
import com.thinkive.bank.abslistviewdemo.base.BaseActivity;
import com.thinkive.bank.abslistviewdemo.bean.GanHuoBean;
import com.thinkive.bank.abslistviewdemo.http.BaseCallback;
import com.thinkive.bank.abslistviewdemo.http.OkHttpHelper;
import com.thinkive.bank.abslistviewdemo.util.DialogHelper;
import com.thinkive.bank.abslistviewdemo.util.ToastUtils;
import com.thinkive.bank.abslistviewdemo.xlistview.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: sq
 * @date: 2017/9/11
 * @corporation: 深圳市思迪信息技术股份有限公司
 * @description: 自定义上拉加载、下拉刷新的XListView控件的使用、cardView的使用
 * 【注意：当添加头视图后，数据源的起始位置由0变为1】
 */
public class XListViewActivity extends BaseActivity implements XListView.IXListViewListener {

    private String GANHUO_URL = "http://gank.io/api/search/query/listview/category/Android/count/10/page/";

    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFRESH = 1;
    private static final int STATE_MORE = 2;

    private int pageIndex = 1;
    private boolean isLastShown;
    private boolean isFirstShown;
    private int state = STATE_NORMAL;

    private XListView xListView;
    private TextView emptyTextView;

    private SubGanHuoAdapter ganHuoAdapter;
    private List<GanHuoBean.ResultsBean> data = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_xlist_view;
    }

    @Override
    protected void findViews() {
        xListView = findView(R.id.xlv_xlist);
        emptyTextView = findView(R.id.emptyText);
    }

    @Override
    protected void initObjects() {
        ganHuoAdapter = new SubGanHuoAdapter(context, data, R.layout.card_layout);
    }

    @Override
    protected void initViews() {
        xListView.setAdapter(ganHuoAdapter);
    }

    @Override
    protected void initData() {
        getNetworkData(pageIndex);//发送网络请求，获取数据
    }

    @Override
    protected void setListeners() {

        emptyTextView.setOnClickListener(this);

        xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<GanHuoBean.ResultsBean> data = ganHuoAdapter.getData();
//                String title = data.get(position).getTitle();
                if (position >= 1) {
                    String title = data.get(position - 1).getDesc();
                    ToastUtils.showToast(context, "点击了-----》" + title);
                }
            }
        });

        xListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 说明XListView已经滑动到底部，并且停止状态
                if (isLastShown && scrollState == SCROLL_STATE_IDLE) {
                    pageIndex++;
                    state = STATE_MORE;
                    getNetworkData(pageIndex);
                }
                // 说明XListView已经滑动到顶部，并且停止状态
                if (isFirstShown && scrollState == SCROLL_STATE_IDLE) {
                    pageIndex = 1;
                    state = STATE_REFRESH;
                    getNetworkData(pageIndex);
                }
            }

            /**
             * 只要ListView处于滑动状态，此方法会被一直调用
             * @param view    指ListView本身
             * @param firstVisibleItem    屏幕当前第一个可见Item的位置,从0开始
             * @param visibleItemCount    屏幕当前可见item的个数
             * @param totalItemCount        ListView中总的item的个数
             */
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // 通过方法参数可以判断，最后一条是否已经显示到屏幕上
                isLastShown = firstVisibleItem + visibleItemCount == totalItemCount;
                // 通过方法参数可以判断，第一条是否已经显示到屏幕顶部
                isFirstShown = firstVisibleItem == 0;
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.emptyText) {
            getNetworkData(pageIndex);
        }
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

                DialogHelper.showWaitDialog(context, null, "加载中...");
                if (state == STATE_REFRESH) {
                    xListView.onRefresh();
                } else if (state == STATE_MORE) {
                    xListView.showFooter(true);
                    xListView.onLoadMore();
                }
            }

            @Override
            public void onFailure(Request request, Exception e) {

                DialogHelper.hiddenWaitDialog();
                xListView.stopRefresh();
                xListView.showFooter(false);
                ToastUtils.showToast(context, "网络异常，请检测网络设置");
            }

            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onSuccess(Response response, GanHuoBean ganHuoBean) {

                DialogHelper.hiddenWaitDialog();
                if (emptyTextView.getVisibility() == View.VISIBLE) { //隐藏emptyTextView
                    emptyTextView.setVisibility(View.GONE);
                }

                if (state == STATE_NORMAL) { //首次加载数据源
                    data.addAll(ganHuoBean.getResults());
                    ganHuoAdapter.refresh(data);

                } else if (state == STATE_MORE) {  //加载更多
                    if (ganHuoBean != null && ganHuoBean.getResults().size() > 0) {
                        data.addAll(ganHuoBean.getResults());
                        onLoadMore();
                    } else {
                        ToastUtils.showToast(context, "没有更多数据了");
                    }

                } else if (state == STATE_REFRESH) {  //刷新数据源
                    data.clear();
                    data.addAll(ganHuoBean.getResults());
                    onRefresh();
                }

            }

            @Override
            public void onError(Response response, int code, Exception e) {

                DialogHelper.hiddenWaitDialog();
                xListView.stopRefresh();
                xListView.showFooter(false);
                ToastUtils.showToast(context, "获取数据异常，请检测服务器配置");
            }
        });
    }

    @Override
    public void onRefresh() {
        ganHuoAdapter.refresh(data);
        xListView.stopRefresh();
    }

    @Override
    public void onLoadMore() {
        ganHuoAdapter.refresh(data);
        xListView.stopLoadMore();
    }
}
