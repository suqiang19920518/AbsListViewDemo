package com.thinkive.bank.abslistviewdemo.adapter;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.thinkive.bank.abslistviewdemo.R;
import com.thinkive.bank.abslistviewdemo.base.LVBaseAdapter;
import com.thinkive.bank.abslistviewdemo.bean.ImageBean;
import com.thinkive.bank.abslistviewdemo.util.AppInfoUtils;

import java.util.List;

public class ImageHotAdapter extends LVBaseAdapter<ImageBean.DataBean.WallpaperListInfoBean> {

    public ImageHotAdapter(Context context, List<ImageBean.DataBean.WallpaperListInfoBean> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void initialHolder(ViewHolder holder, ImageBean.DataBean.WallpaperListInfoBean item) {
        ImageView imageView = holder.getView(R.id.iv_grid);
        imageView.setLayoutParams(new FrameLayout.LayoutParams(
                AppInfoUtils.getApp(context).getScreenWidth() / 3,
                AppInfoUtils.getApp(context).getScreenHeight() / 3
        ));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        String url = item.getWallPaperMiddle();
        Glide.with(context).load(url)
                .placeholder(R.color.colorBlack)  //默认空白的图片
                .skipMemoryCache(false)  //控制内存缓存的使用
                .diskCacheStrategy(DiskCacheStrategy.ALL)   //控制磁盘缓存的使用
                .crossFade()  //淡入淡出的动画效果
                .into(imageView);
    }


}
