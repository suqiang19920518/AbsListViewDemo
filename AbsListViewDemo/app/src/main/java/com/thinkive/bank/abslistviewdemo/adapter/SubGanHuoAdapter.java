package com.thinkive.bank.abslistviewdemo.adapter;


import android.content.Context;

import com.thinkive.bank.abslistviewdemo.R;
import com.thinkive.bank.abslistviewdemo.base.LVBaseAdapter;
import com.thinkive.bank.abslistviewdemo.bean.GanHuoBean;

import java.util.List;

public class SubGanHuoAdapter extends LVBaseAdapter<GanHuoBean.ResultsBean> {

    public SubGanHuoAdapter(Context context, List<GanHuoBean.ResultsBean> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void initialHolder(ViewHolder holder, GanHuoBean.ResultsBean item) {
        //        TextView desc = holder.getView(R.id.tv_desc);
//        TextView published = holder.getView(R.id.tv_published);
//        desc.setText(item.getDesc());
//        published.setText(item.getPublishedAt());
        holder.setText(R.id.tv_desc, item.getDesc());
        holder.setText(R.id.tv_published, item.getPublishedAt());

    }


}
