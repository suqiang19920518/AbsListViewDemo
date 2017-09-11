package com.thinkive.bank.abslistviewdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.thinkive.bank.abslistviewdemo.activity.GridViewActivity;
import com.thinkive.bank.abslistviewdemo.activity.ListViewActivity;
import com.thinkive.bank.abslistviewdemo.activity.RecyclerViewActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnListView;
    private Button mBtnRecyclerView;
    private Button mBtnGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initEvent();
    }

    private void initViews() {
        mBtnListView = ((Button) findViewById(R.id.btn_lv));
        mBtnRecyclerView = ((Button) findViewById(R.id.btn_rv));
        mBtnGridView = ((Button) findViewById(R.id.btn_gv));
    }

    private void initEvent() {
        mBtnListView.setOnClickListener(this);
        mBtnRecyclerView.setOnClickListener(this);
        mBtnGridView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_lv:
                startActivity(new Intent(this, ListViewActivity.class));
                break;
            case R.id.btn_rv:
                startActivity(new Intent(this, RecyclerViewActivity.class));
                break;
            case R.id.btn_gv:
                startActivity(new Intent(this, GridViewActivity.class));
                break;
        }
    }
}
