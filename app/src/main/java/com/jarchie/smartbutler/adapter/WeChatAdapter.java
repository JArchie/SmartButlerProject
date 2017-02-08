package com.jarchie.smartbutler.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.jarchie.smartbutler.R;
import com.jarchie.smartbutler.entity.WeChatBean;
import com.jarchie.smartbutler.utils.PicassoUtil;
import java.util.List;

/**
 * 项目名:   SmartButler
 * 包名:     com.jarchie.smartbutler.adapter
 * 文件名:   WeChatAdapter
 * 创建者:   Jarchie
 * 创建时间: 17/1/23 上午9:22
 * 描述:     微信精选的适配器
 */

public class WeChatAdapter extends BaseAdapter{
    private Context mContext;
    private List<WeChatBean.ResultBean.ListBean> list;
    private int width;
    private WindowManager wm;

    public WeChatAdapter(Context mContext,List<WeChatBean.ResultBean.ListBean> list){
        this.mContext = mContext;
        this.list = list;
        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_wechat_item,null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.iv_image);
            holder.title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.source = (TextView) convertView.findViewById(R.id.tv_source);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(list.get(position).getTitle());
        holder.source.setText(list.get(position).getSource());
        //加载图片
        if (!TextUtils.isEmpty(list.get(position).getFirstImg())){
            PicassoUtil.loadImageViewSize(mContext,list.get(position).getFirstImg(),holder.imageView,width/3,200);
        }else {
            holder.imageView.setImageResource(R.mipmap.ic_launcher);
        }
        return convertView;
    }

    class ViewHolder{
        ImageView imageView;
        TextView title,source;
    }
}
