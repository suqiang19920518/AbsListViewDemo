package com.thinkive.bank.abslistviewdemo.activity;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.thinkive.bank.abslistviewdemo.R;
import com.thinkive.bank.abslistviewdemo.adapter.TravelAdapter;
import com.thinkive.bank.abslistviewdemo.base.BaseActivity;
import com.thinkive.bank.abslistviewdemo.bean.TravelBean;
import com.thinkive.bank.abslistviewdemo.http.BaseCallback;
import com.thinkive.bank.abslistviewdemo.http.OkHttpHelper;
import com.thinkive.bank.abslistviewdemo.util.DialogHelper;
import com.thinkive.bank.abslistviewdemo.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: sq
 * @date: 2017/8/29
 * @corporation: 深圳市思迪信息技术股份有限公司
 * @description: ListView的使用【注意：当添加头视图后，数据源的起始位置由0变为1】
 */
public class ListViewActivity extends BaseActivity {

    private String TRAVEL_URL = "http://chanyouji.com/api/articles.json?page=";

    private int pageIndex = 1;
    private boolean isLastShown;

    private View footerView;
    private ListView listView;
    private TextView emptyTextView;

    private List<TravelBean> data = new ArrayList<>();
    private TravelAdapter travelAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_list_view;
    }

    @Override
    protected void findViews() {
        listView = findView(R.id.lv_list);
        emptyTextView = findView(R.id.emptyText);
    }

    @Override
    protected void initObjects() {
        travelAdapter = new TravelAdapter(context, data, R.layout.list_item);
    }

    @Override
    protected void initViews() {

        ImageView headerView = new ImageView(this);

        //初始化LayoutParams时，需要指定宽高
        LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT, 200);

        //将LayoutParams设置到UI控件上
        headerView.setLayoutParams(param);

        //设置ImageView宽高都填充父视图
        headerView.setScaleType(ScaleType.FIT_XY);

        headerView.setImageResource(R.drawable.food);

        footerView = getLayoutInflater().inflate(R.layout.footer_inficator, null);

        listView.setEmptyView(emptyTextView);//当ListView的数据源中的个数为0时，显示EmptyView中的内容
        listView.addHeaderView(headerView);//添加一个头视图View
        listView.addFooterView(footerView);//添加一个底视图View
        listView.setAdapter(travelAdapter);

    }

    @Override
    protected void initData() {
        getNetworkData(pageIndex);//发送网络请求，获取数据
    }

    @Override
    protected void setListeners() {

        emptyTextView.setOnClickListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<TravelBean> data = travelAdapter.getData();
//                String title = data.get(position).getTitle();
                if (position >= 1) {
                    String title = data.get(position - 1).getTitle();
                    ToastUtils.showToast(context, "点击了-----》" + title);
                    startActivity(XListViewActivity.class);
                }
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 说明ListView已经滑动到底部，并且停止状态
                if (isLastShown && scrollState == SCROLL_STATE_IDLE) {
                    pageIndex++;
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

        OkHttpHelper.getInstance().get(TRAVEL_URL + pageIndex, new BaseCallback<List<TravelBean>>() {
            @Override
            public void onBeforeRequest(Request request) {
                DialogHelper.showWaitDialog(context, null, "加载中...");
                if (listView.getFooterViewsCount() == 0) {
                    listView.addFooterView(footerView);
                }
            }

            @Override
            public void onFailure(Request request, Exception e) {
                DialogHelper.hiddenWaitDialog();
                listView.removeFooterView(footerView);
                ToastUtils.showToast(context, "网络异常，请检测网络设置");
            }

            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onSuccess(Response response, List<TravelBean> travels) {
                DialogHelper.hiddenWaitDialog();
                if (travels.size() == 0) {
                    ToastUtils.showToast(context, "没有更多数据了");
                    listView.removeFooterView(footerView);
                } else {
                    data.addAll(travels);
                    travelAdapter.refresh(data);
                }

            }

            @Override
            public void onError(Response response, int code, Exception e) {
                DialogHelper.hiddenWaitDialog();
                listView.removeFooterView(footerView);
                ToastUtils.showToast(context, "获取数据异常，请检测服务器配置");
            }
        });
    }

}
