package com.jarchie.smartbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.jarchie.smartbutler.R;
import com.jarchie.smartbutler.entity.LogisticsBean;
import java.util.List;

/**
 * 项目名:   SmartButler
 * 包名:     com.jarchie.smartbutler.adapter
 * 文件名:   LogisticsAdapter
 * 创建者:   Jarchie
 * 创建时间: 17/1/22 下午1:46
 * 描述:     创建物流查询时间轴的适配器
 */

public class LogisticsAdapter extends BaseAdapter {
    private Context mContext;
    private List<LogisticsBean.ResultBean.ListBean> list;

    public LogisticsAdapter(Context mContext,List<LogisticsBean.ResultBean.ListBean> list){
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_logistics_item,null);
            holder.remark = (TextView) convertView.findViewById(R.id.tv_remark);
            holder.zone = (TextView) convertView.findViewById(R.id.tv_zong);
            holder.time = (TextView) convertView.findViewById(R.id.tv_time);
            //设置缓存
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.remark.setText(list.get(position).getRemark());
        holder.zone.setText(list.get(position).getZone());
        holder.time.setText(list.get(position).getDatetime());
        return convertView;
    }

    class ViewHolder{
        TextView remark,zone,time;
    }

}
