package com.jarchie.smartbutler.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.google.gson.Gson;
import com.jarchie.smartbutler.R;
import com.jarchie.smartbutler.adapter.WeChatAdapter;
import com.jarchie.smartbutler.entity.WeChatBean;
import com.jarchie.smartbutler.ui.WeChatDetailActivity;
import com.jarchie.smartbutler.utils.StaticClass;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目名:   SmartButler
 * 包名:     com.jarchie.smartbutler.fragment
 * 文件名:   ButlerFragment
 * 创建者:   Jarchie
 * 创建时间: 17/1/15 下午2:12
 * 描述:     微信精选
 */

public class WechatFragment extends Fragment {
    private ListView mListView;
    //数据集合
    private List<WeChatBean.ResultBean.ListBean> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wechat,null);
        initView(view);
        return view;
    }

    //初始化View
    private void initView(View view){
        mListView = (ListView) view.findViewById(R.id.mListView);
        requestDataToListview();
    }

    //解析数据并且展示到列表中
    private void requestDataToListview(){
        String url = "http://v.juhe.cn/weixin/query?key=" + StaticClass.WECHAT_KEY + "&ps=100";
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                Gson gson = new Gson();
                WeChatBean bean = gson.fromJson(t,WeChatBean.class);
                if (list == null){
                    list = new ArrayList<>();
                }
                list = bean.getResult().getList();
                WeChatAdapter mAdapter = new WeChatAdapter(getActivity(),list);
                mListView.setAdapter(mAdapter);
            }
        });
        //mListView的点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getActivity(), WeChatDetailActivity.class);
                intent.putExtra("title",list.get(position).getTitle());
                intent.putExtra("url",list.get(position).getUrl());
                startActivity(intent);
            }
        });
    }

}
