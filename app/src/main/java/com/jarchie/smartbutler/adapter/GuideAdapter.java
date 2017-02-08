package com.jarchie.smartbutler.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

/**
 * 项目名:   SmartButler
 * 包名:     com.jarchie.smartbutler.adapter
 * 文件名:   GuideAdapter
 * 创建者: Jarchie
 * 创建时间: 17/1/16 下午4:03
 * 描述:     引导页的View的适配器
 */

public class GuideAdapter extends PagerAdapter{
    private List<View> list;
    private Context mContext;

    public GuideAdapter(List<View> list,Context mContext){
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager) container).addView(list.get(position));
        return list.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView(list.get(position));
    }
}
