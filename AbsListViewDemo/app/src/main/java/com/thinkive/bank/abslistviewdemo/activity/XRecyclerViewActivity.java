package com.thinkive.bank.abslistviewdemo.activity;

import android.view.View;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.thinkive.bank.abslistviewdemo.R;
import com.thinkive.bank.abslistviewdemo.adapter.ImageLeastAdapter;
import com.thinkive.bank.abslistviewdemo.base.BaseActivity;
import com.thinkive.bank.abslistviewdemo.bean.ImageBean;
import com.thinkive.bank.abslistviewdemo.decoration.DividerGridItemDecoration;
import com.thinkive.bank.abslistviewdemo.http.BaseCallback;
import com.thinkive.bank.abslistviewdemo.http.OkHttpHelper;
import com.thinkive.bank.abslistviewdemo.util.DialogHelper;
import com.thinkive.bank.abslistviewdemo.util.ToastUtils;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: sq
 * @date: 2017/9/11
 * @corporation: 深圳市思迪信息技术股份有限公司
 * @description: 自定义上拉加载、下拉刷新的XRecyclerView控件的使用
 */
public class XRecyclerViewActivity extends BaseActivity {

    private String IMAGE_HOT_URL = "http://bz.budejie.com/?typeid=2&ver=3.4.3&" +
            "no_cry=1&client=android&c=wallPaper&a=hotRecent&index=1&size=60&bigid=0";

    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFRESH = 1;
    private static final int STATE_MORE = 2;

    private String url;
    private int pageIndex = 1;
    private int state = STATE_NORMAL;

    private TextView mEmptyTextView;
    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;

    private ImageLeastAdapter imageLeastAdapter;
    private List<ImageBean.DataBean.WallpaperListInfoBean> data = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_xrecycler_view;
    }

    @Override
    protected void findViews() {
        mEmptyTextView = findView(R.id.emptyText);
        mPullLoadMoreRecyclerView = findView(R.id.rv_xrecycler);
    }

    @Override
    protected void initObjects() {
        imageLeastAdapter = new ImageLeastAdapter(context, data, R.layout.grid_item);
    }

    @Override
    protected void initViews() {
        mPullLoadMoreRecyclerView.setGridLayout(3);
        mPullLoadMoreRecyclerView.setAdapter(imageLeastAdapter);
        mPullLoadMoreRecyclerView.getRecyclerView().addItemDecoration(new DividerGridItemDecoration(context));
    }

    @Override
    protected void initData() {
        url = IMAGE_HOT_URL;
        getNetworkData(url);//发送网络请求，获取数据
    }

    @Override
    protected void setListeners() {

        mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                state = STATE_REFRESH;
                url = IMAGE_HOT_URL.replace("index=" + pageIndex, "index=" + 1);
                pageIndex = 1;
                getNetworkData(url);
            }

            @Override
            public void onLoadMore() {
                pageIndex++;
                state = STATE_MORE;
                url = IMAGE_HOT_URL.replace("index=" + (pageIndex - 1), "index=" + pageIndex);
                getNetworkData(url);
            }
        });

    }

    @Override
    public void onClick(View v) {

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
                mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                ToastUtils.showToast(context, "网络异常，请检测网络设置");

            }

            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onSuccess(Response response, ImageBean imageBean) {

                DialogHelper.hiddenWaitDialog();
                if (mEmptyTextView.getVisibility() == View.VISIBLE) {
                    mEmptyTextView.setVisibility(View.GONE);
                }
                if (imageBean == null | imageBean.getData().getWallpaperListInfo().size() == 0) {
                    ToastUtils.showToast(context, "没有更多数据了");
                } else {
                    if (state == STATE_NORMAL) { //首次加载数据源
                        imageLeastAdapter.addData(imageBean.getData().getWallpaperListInfo());

                    } else if (state == STATE_MORE) {  //加载更多
                        if (imageBean.getData() != null && imageBean.getData().getWallpaperListInfo().size() > 0) {
                            imageLeastAdapter.loadMoreData(imageBean.getData().getWallpaperListInfo());
                        } else {
                            ToastUtils.showToast(context, "没有更多数据了");
                        }
                        mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();

                    } else if (state == STATE_REFRESH) {  //刷新数据源
                        imageLeastAdapter.clear();
                        imageLeastAdapter.addData(imageBean.getData().getWallpaperListInfo());
                        mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                    }
                }

            }

            @Override
            public void onError(Response response, int code, Exception e) {

                DialogHelper.hiddenWaitDialog();
                mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                ToastUtils.showToast(context, "获取数据异常，请检测服务器配置");

            }
        });
    }

}
