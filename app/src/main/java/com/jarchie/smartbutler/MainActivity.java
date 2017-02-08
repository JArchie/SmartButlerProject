package com.jarchie.smartbutler;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.jarchie.smartbutler.fragment.ButlerFragment;
import com.jarchie.smartbutler.fragment.GirlFragment;
import com.jarchie.smartbutler.fragment.UserFragment;
import com.jarchie.smartbutler.fragment.WechatFragment;
import com.jarchie.smartbutler.ui.SettingActivity;
import java.util.ArrayList;
import java.util.List;

/**
 * @author  Jarchie
 * @version 2017/01/14
 *          定义整个App的主Activity
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //TabLayout
    private TabLayout mTabLayout;
    //ViewPager
    private ViewPager mViewPager;
    //Title
    private List<String> mTitle;
    //Fragment
    private List<Fragment> mFragment;
    //悬浮窗
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //去掉阴影
        getSupportActionBar().setElevation(0);
        initData();
        initView();
    }

    //初始化数据
    private void initData() {
        mTitle = new ArrayList<>();
        mTitle.add(this.getResources().getString(R.string.tab_first));
        mTitle.add(this.getResources().getString(R.string.tab_second));
        mTitle.add(this.getResources().getString(R.string.tab_third));
        mTitle.add(this.getResources().getString(R.string.tab_fourth));
        mFragment = new ArrayList<>();
        mFragment.add(new ButlerFragment());
        mFragment.add(new WechatFragment());
        mFragment.add(new GirlFragment());
        mFragment.add(new UserFragment());
    }

    //初始化View
    private void initView() {
        fab = (FloatingActionButton) findViewById(R.id.fab_setting);
        fab.setOnClickListener(this);
        //默认一进入app隐藏fab
        fab.setVisibility(View.GONE);
        mTabLayout = (TabLayout) findViewById(R.id.mTabLayout);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        //ViewPager的预加载
        mViewPager.setOffscreenPageLimit(mFragment.size());
        //ViewPager的滑动监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    fab.setVisibility(View.GONE);
                } else {
                    fab.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //设置适配器
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            //选中的Item
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            //返回Item的个数
            @Override
            public int getCount() {
                return mFragment.size();
            }

            //设置标题
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitle.get(position);
            }
        });
        //绑定
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
        }
    }

}
