package com.jarchie.smartbutler.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jarchie.smartbutler.R;
import com.jarchie.smartbutler.utils.UtilTools;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 项目名:   SmartButler
 * 包名:     com.jarchie.smartbutler.ui
 * 文件名:   AboutSoftActivity
 * 创建者:   Jarchie
 * 创建时间: 17/2/5 上午10:18
 * 描述:     关于软件
 */

public class AboutSoftActivity extends BaseActivity{
    private ListView mListView;
    private List<String> mList = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;
    //圆形头像
    private CircleImageView circleImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutsoft);
        //去除阴影
        getSupportActionBar().setElevation(0);
        initView();
    }

    //初始化View
    private void initView() {
        circleImageView = (CircleImageView) findViewById(R.id.profile_image);
        //获取图片
        UtilTools.getImageFromShareUtil(this, circleImageView);
        mListView = (ListView) findViewById(R.id.mListView);
        //添加数据
        mList.add("应用名称:"+getString(R.string.app_name));
        mList.add("版本号:"+ UtilTools.getVersion(this));
        mList.add("官网:http://blog.csdn.net/jarchie520");
        mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mList);
        mListView.setAdapter(mAdapter);
    }

}
