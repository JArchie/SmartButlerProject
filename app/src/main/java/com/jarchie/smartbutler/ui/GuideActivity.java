package com.jarchie.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jarchie.smartbutler.R;
import com.jarchie.smartbutler.adapter.GuideAdapter;
import com.jarchie.smartbutler.utils.UtilTools;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名:   SmartButler
 * 包名:     com.jarchie.smartbutler.ui
 * 文件名:   GuideActivity
 * 创建者:   Jarchie
 * 创建时间: 17/1/16 下午3:07
 * 描述:     引导页
 */

public class GuideActivity extends AppCompatActivity implements View.OnClickListener{
    private ViewPager mViewPager;
    //容器
    private List<View> mList = new ArrayList<>();
    private View view1,view2,view3;
    private Button startBtn;
    //ViewPage的指示器
    private ImageView point1,point2,point3;
    //跳过
    private TextView backTv,guide1,guide2,guide3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
    }

    //初始化View
    private void initView() {
        backTv = (TextView) findViewById(R.id.tv_back);
        backTv.setOnClickListener(this);
        point1 = (ImageView) findViewById(R.id.point1);
        point2 = (ImageView) findViewById(R.id.point2);
        point3 = (ImageView) findViewById(R.id.point3);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        view1 = View.inflate(this,R.layout.pager_item_one,null);
        view2 = View.inflate(this,R.layout.pager_item_two,null);
        view3 = View.inflate(this,R.layout.pager_item_three,null);
        guide1 = (TextView) view1.findViewById(R.id.guide1_text);
        guide2 = (TextView) view2.findViewById(R.id.guide2_text);
        guide3 = (TextView) view3.findViewById(R.id.guide3_text);
        //设置字体
        UtilTools.setFont(this, guide1);
        UtilTools.setFont(this, guide2);
        UtilTools.setFont(this, guide3);
        startBtn = (Button) view3.findViewById(R.id.btn_start);
        startBtn.setOnClickListener(this);
        mList.add(view1);
        mList.add(view2);
        mList.add(view3);
        //设置适配器
        mViewPager.setAdapter(new GuideAdapter(mList,GuideActivity.this));
        //设置指示器的默认选中效果
        setPointImage(true,false,false);
        //设置ViewPager的监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            //ViewPager的切换
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        setPointImage(true,false,false);
                        backTv.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        setPointImage(false,true,false);
                        backTv.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        setPointImage(false,false,true);
                        backTv.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //设置viewpager的指示器的选中效果
    private void setPointImage(boolean isSelected1,boolean isSelected2,boolean isSelected3){
        if (isSelected1){
            point1.setBackgroundResource(R.drawable.point_on);
        }else {
            point1.setBackgroundResource(R.drawable.point_off);
        }
        if (isSelected2){
            point2.setBackgroundResource(R.drawable.point_on);
        }else {
            point2.setBackgroundResource(R.drawable.point_off);
        }
        if (isSelected3){
            point3.setBackgroundResource(R.drawable.point_on);
        }else {
            point3.setBackgroundResource(R.drawable.point_off);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_start:
            case R.id.tv_back:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
    }
}
