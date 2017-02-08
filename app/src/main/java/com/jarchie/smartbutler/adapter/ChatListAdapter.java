package com.jarchie.smartbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.jarchie.smartbutler.R;
import com.jarchie.smartbutler.entity.ChatListBean;
import java.util.List;

/**
 * 项目名:   SmartButler
 * 包名:     com.jarchie.smartbutler.adapter
 * 文件名:   ChatListAdapter
 * 创建者:   Jarchie
 * 创建时间: 17/1/22 下午7:23
 * 描述:     对话adapter
 */

public class ChatListAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater inflater;
    private ChatListBean bean;
    private List<ChatListBean> list;
    //两种类型
    public static final int VALUE_LEFT_TEXT = 1;
    public static final int VALUE_RIGHT_TEXT = 2;

    public ChatListAdapter(Context mContext,List<ChatListBean> list){
        this.mContext = mContext;
        this.list = list;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        ViewHolderLeft viewHolderLeft = null;
        ViewHolderRight viewHolderRight = null;
        //根据当前要显示的type,根据这个type来区分数据的加载
        int type = getItemViewType(position);
        if (convertView == null){
            switch (type){
                case VALUE_LEFT_TEXT:
                    viewHolderLeft = new ViewHolderLeft();
                    convertView = inflater.inflate(R.layout.left_item,null);
                    viewHolderLeft.lefttext = (TextView) convertView.findViewById(R.id.tv_left_text);
                    convertView.setTag(viewHolderLeft);
                    break;
                case VALUE_RIGHT_TEXT:
                    viewHolderRight = new ViewHolderRight();
                    convertView = inflater.inflate(R.layout.right_item,null);
                    viewHolderRight.righttext = (TextView) convertView.findViewById(R.id.tv_right_text);
                    convertView.setTag(viewHolderRight);
                    break;
            }
        }else {
            switch (type){
                case VALUE_LEFT_TEXT:
                    viewHolderLeft = (ViewHolderLeft) convertView.getTag();
                    break;
                case VALUE_RIGHT_TEXT:
                    viewHolderRight = (ViewHolderRight) convertView.getTag();
                    break;
            }
        }
        //赋值
        ChatListBean bean = list.get(position);
        switch (type){
            case VALUE_LEFT_TEXT:
                viewHolderLeft.lefttext.setText(bean.getText());
                break;
            case VALUE_RIGHT_TEXT:
                viewHolderRight.righttext.setText(bean.getText());
                break;
        }
        return convertView;
    }

    //根据数据源的position来返回要显示的item
    @Override
    public int getItemViewType(int position) {
        ChatListBean bean = list.get(position);
        int type = bean.getType();
        return type;
    }

    //返回所有的layout数量
    @Override
    public int getViewTypeCount() {
        return 3; //list.size()+1
    }

    //左边的文本
    class ViewHolderLeft{
        private TextView lefttext;
    }

    //右边的文本
    class ViewHolderRight{
        private TextView righttext;
    }

}
