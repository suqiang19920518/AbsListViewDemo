package com.thinkive.bank.abslistviewdemo.adapter;


import android.content.Context;
import android.view.View;

import com.thinkive.bank.abslistviewdemo.R;
import com.thinkive.bank.abslistviewdemo.base.RVBaseAdapter;
import com.thinkive.bank.abslistviewdemo.bean.GanHuoBean;

import java.util.List;

public class GanHuoAdapter extends RVBaseAdapter<GanHuoBean.ResultsBean> {

    public GanHuoAdapter(Context context, List<GanHuoBean.ResultsBean> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void initialHolder(MyViewHolder holder, GanHuoBean.ResultsBean item, final int position) {

//        TextView desc = holder.getView(R.id.tv_desc);
//        TextView published = holder.getView(R.id.tv_published);
//        desc.setText(item.getDesc());
//        published.setText(item.getPublishedAt());
        holder.setText(R.id.tv_desc, item.getDesc());
        holder.setText(R.id.tv_published, item.getPublishedAt());
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickCallback!=null){
                    itemClickCallback.onItemClicked(position);
                }
            }
        });

    }

}
