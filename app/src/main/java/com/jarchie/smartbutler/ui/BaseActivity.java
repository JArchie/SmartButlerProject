package com.jarchie.smartbutler.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * 项目名:   SmartButler
 * 包名:     com.jarchie.smartbutler.ui
 * 文件名:   BaseActivity
 * 创建者:   Jarchie
 * 创建时间: 17/1/14 上午11:30
 * 描述:    Activity的基类
 * 此类拥有统一的属性、接口、方法
 */

public class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //显示返回键,actionbar中自带的
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //监听菜单栏操作
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
