package com.thinkive.bank.abslistviewdemo.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * @author: sq
 * @date: 2017/7/31
 * @corporation: 深圳市思迪信息技术股份有限公司
 * @description: activity基类
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    protected Context context = null;

    protected FragmentManager fragmentManager = null;//Fragment管理器,不能在子类中创建

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(this.getLayoutId());
        context = this;
        fragmentManager = getSupportFragmentManager();
        findViews();
        initObjects();
        initViews();
        initData();
        setListeners();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        context = null;
        fragmentManager = null;
    }

    /**
     * 通过id查找并获取控件，使用时不需要强转
     *
     * @param id
     * @param <V>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <V extends View> V findView(int id) {
        return (V) findViewById(id);
    }

    /**
     * 通过id查找并获取控件，并setOnClickListener，使用时不需要强转
     *
     * @param id
     * @param l
     * @param <V>
     * @return
     */
    public <V extends View> V findView(int id, View.OnClickListener l) {
        V v = findView(id);
        v.setOnClickListener(l);
        return v;
    }

    /**
     * 通过tag查找并获取Fragment,使用时不需要强转
     *
     * @param tag
     * @param <F>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <F extends Fragment> F findFragmentByTag(String tag) {
        F f = (F) fragmentManager.findFragmentByTag(tag);
        return f;
    }

    /**
     * 使用class来启动一个Activity
     *
     * @param activity 需要启动的Activity的.class实例
     */
    public void startActivity(Class<?> activity) {
        final Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    /**
     * 使用class来启动一个Activity
     *
     * @param activity 需要启动的Activity的.class实例
     * @param flag     启动模式
     */
    public void startActivity(Class<?> activity, int flag) {
        final Intent intent = new Intent(this, activity);
        intent.addFlags(flag);
        startActivity(intent);
    }

    /**
     * 使用class来启动一个Activity
     *
     * @param activity 需要启动的Activity的.class实例
     * @param bundle   需要传的参数
     */
    public void startActivity(Class<?> activity, Bundle bundle) {
        final Intent intent = new Intent(this, activity);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 使用class来启动一个Activity
     *
     * @param activity 需要启动的Activity的.class实例
     * @param bundle   需要传的参数
     * @param flag     启动模式
     */
    public void startActivity(Class<?> activity, Bundle bundle, int flag) {
        final Intent intent = new Intent(this, activity);
        intent.addFlags(flag);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 获取布局文件ID
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 通过资源ID来实例化视图对象
     */
    protected abstract void findViews();


    /**
     * 初始化new出来的对象
     */
    protected abstract void initObjects();

    /**
     * 初始化视图状态
     */
    protected abstract void initViews();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 为视图设置事件监听
     */
    protected abstract void setListeners();


}
