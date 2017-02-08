package com.jarchie.smartbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.jarchie.smartbutler.R;
import com.jarchie.smartbutler.entity.GirlBean;
import com.jarchie.smartbutler.utils.PicassoUtil;
import java.util.List;

/**
 * 项目名:   SmartButler
 * 包名:     com.jarchie.smartbutler.adapter
 * 文件名:   GirlAdapter
 * 创建者:   Jarchie
 * 创建时间: 17/1/26 下午6:48
 * 描述:     美女社区的适配器
 */

public class GirlAdapter extends BaseAdapter {
    private Context context;
    private List<GirlBean.ResultsBean> list;
    private int width;
    private WindowManager wm;

    public GirlAdapter(Context context,List<GirlBean.ResultsBean> list){
        this.context = context;
        this.list = list;
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.girl_item,null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageview);
            holder.textView = (TextView) convertView.findViewById(R.id.textview);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        //解析图片,设置数据
        PicassoUtil.loadImageViewSize(context,list.get(position).getUrl(),holder.imageView,width/2,500);
        holder.textView.setText(list.get(position).getPublishedAt().substring(0,list.get(position).getPublishedAt().length() - 7));
        return convertView;
    }

    class ViewHolder{
        ImageView imageView;
        TextView textView;
    }

}
