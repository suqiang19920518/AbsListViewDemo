package com.thinkive.bank.abslistviewdemo.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.thinkive.bank.abslistviewdemo.R;
import com.thinkive.bank.abslistviewdemo.adapter.ImageHotAdapter;
import com.thinkive.bank.abslistviewdemo.base.BaseActivity;
import com.thinkive.bank.abslistviewdemo.bean.ImageBean;
import com.thinkive.bank.abslistviewdemo.http.BaseCallback;
import com.thinkive.bank.abslistviewdemo.http.OkHttpHelper;
import com.thinkive.bank.abslistviewdemo.util.DialogHelper;
import com.thinkive.bank.abslistviewdemo.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: sq
 * @date: 2017/9/10
 * @corporation: 深圳市思迪信息技术股份有限公司
 * @description: GridView的使用【包括下拉刷新、上拉加载更多、点击监听】
 */
public class GridViewActivity extends BaseActivity {

    private String IMAGE_HOT_URL = "http://bz.budejie.com/?typeid=2&ver=3.4.3&" +
            "no_cry=1&client=android&c=wallPaper&a=hotRecent&index=1&size=60&bigid=0";

    private String url;
    private int pageIndex = 1;
    private boolean isPullUp;//判断是否上拉

    private TextView emptyTextView;
    private PullToRefreshGridView mPullRefreshGridView;

    private ImageHotAdapter imageHotAdapter;
    private List<ImageBean.DataBean.WallpaperListInfoBean> data = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_grid_view;
    }

    @Override
    protected void findViews() {
        emptyTextView = findView(R.id.emptyText);
        mPullRefreshGridView = findView(R.id.gv_grid);
    }

    @Override
    protected void initObjects() {
        imageHotAdapter = new ImageHotAdapter(context, data, R.layout.grid_item);
    }

    @Override
    protected void initViews() {
        mPullRefreshGridView.setEmptyView(emptyTextView);
        mPullRefreshGridView.setAdapter(imageHotAdapter);

    }

    @Override
    protected void initData() {
        initIndicator();
        url = IMAGE_HOT_URL;
        getNetworkData(url);//发送网络请求，获取数据
    }

    @Override
    protected void setListeners() {

        emptyTextView.setOnClickListener(this);

        mPullRefreshGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tag = data.get(position).getTags();
                ToastUtils.showToast(context, tag);
            }
        });

        mPullRefreshGridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                isPullUp = false;
                url = IMAGE_HOT_URL.replace("index=" + pageIndex, "index=" + 1);
                pageIndex=1;
                getNetworkData(url);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                isPullUp = true;
                pageIndex++;
                url = IMAGE_HOT_URL.replace("index=" + (pageIndex-1), "index=" + pageIndex);
                getNetworkData(url);
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.emptyText) {
            getNetworkData(url);
        }
    }

    private void getNetworkData(String url) {
        OkHttpHelper.getInstance().get(url, new BaseCallback<ImageBean>() {
            @Override
            public void onBeforeRequest(Request request) {
                DialogHelper.showWaitDialog(context, null, "加载中...");
            }

            @Override
            public void onFailure(Request request, Exception e) {
                DialogHelper.hiddenWaitDialog();
                if (mPullRefreshGridView.isRefreshing()) {
                    mPullRefreshGridView.onRefreshComplete();
                }

                ToastUtils.showToast(context, "网络异常，请检测网络设置");
            }

            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onSuccess(Response response, ImageBean imageBean) {
                DialogHelper.hiddenWaitDialog();
                if (imageBean == null | imageBean.getData().getWallpaperListInfo().size() == 0) {
                    ToastUtils.showToast(context, "没有更多数据了");
                } else {
                    if (isPullUp) {  //上拉加载更多
                        data.addAll(imageBean.getData().getWallpaperListInfo());
                    } else {  //下拉刷新
                        data.clear();
                        data.addAll(imageBean.getData().getWallpaperListInfo());
                    }
                    imageHotAdapter.refresh(data);
                }
                if (mPullRefreshGridView.isRefreshing()) {
                    mPullRefreshGridView.onRefreshComplete();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                DialogHelper.hiddenWaitDialog();
                if (mPullRefreshGridView.isRefreshing()) {
                    mPullRefreshGridView.onRefreshComplete();
                }
                ToastUtils.showToast(context, "获取数据异常，请检测服务器配置");
            }
        });
    }

    /**
     * 自定义下拉指示器文本内容
     * 默认上拉和下拉的字同时改变的，此处让其单独改变
     */
    private void initIndicator() {

        ILoadingLayout startLabels = mPullRefreshGridView.getLoadingLayoutProxy(true, false);

        startLabels.setPullLabel(getResources().getString(R.string.pulldown));
        startLabels.setRefreshingLabel(getResources().getString(R.string.loading));
        startLabels.setReleaseLabel(getResources().getString(R.string.release));

        ILoadingLayout endLabels = mPullRefreshGridView.getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel(getResources().getString(R.string.pullup));
        endLabels.setRefreshingLabel(getResources().getString(R.string.loading));
        endLabels.setReleaseLabel(getResources().getString(R.string.release));
    }
}
