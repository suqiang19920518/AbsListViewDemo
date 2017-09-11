package com.thinkive.bank.abslistviewdemo.adapter;


import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.thinkive.bank.abslistviewdemo.R;
import com.thinkive.bank.abslistviewdemo.base.LVBaseAdapter;
import com.thinkive.bank.abslistviewdemo.bean.TravelBean;

import java.util.List;

public class TravelAdapter extends LVBaseAdapter<TravelBean> {

    public TravelAdapter(Context context, List<TravelBean> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void initialHolder(ViewHolder holder, TravelBean item) {
//        TextView mTvTitle = holder.getView(R.id.titleItem);
//        TextView mTvName = holder.getView(R.id.nameItem);
//        mTvTitle.setText(item.getTitle());
//        mTvName.setText(item.getName());
        holder.setText(R.id.titleItem, item.getTitle());
        holder.setText(R.id.nameItem, item.getName());
        ImageView mIvImage = holder.getView(R.id.imageItem);
        Picasso.with(context).load(item.getImage_url()).fit()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(mIvImage);

    }
}
