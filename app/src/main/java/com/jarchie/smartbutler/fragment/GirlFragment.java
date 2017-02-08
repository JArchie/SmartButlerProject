package com.jarchie.smartbutler.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.google.gson.Gson;
import com.jarchie.smartbutler.R;
import com.jarchie.smartbutler.adapter.GirlAdapter;
import com.jarchie.smartbutler.entity.GirlBean;
import com.jarchie.smartbutler.utils.PicassoUtil;
import com.jarchie.smartbutler.utils.StaticClass;
import com.jarchie.smartbutler.view.CustomDialog;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import java.util.ArrayList;
import java.util.List;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 项目名:   SmartButler
 * 包名:     com.jarchie.smartbutler.fragment
 * 文件名:   ButlerFragment
 * 创建者:   Jarchie
 * 创建时间: 17/1/15 下午2:12
 * 描述:     美女如云,这里接的是gank的接口,搜索api
 */

public class GirlFragment extends Fragment {
    private GridView gridView;
    private List<GirlBean.ResultsBean> list;
    private GirlAdapter mAdapter;
    //提示框
    private CustomDialog dialog;
    //预览图片
    private ImageView imageView;
    //PhotoView
    private PhotoViewAttacher mAttacher;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_girl,null);
        initView(view);
        return view;
    }

    //初始化View
    private void initView(View view){
        gridView = (GridView) view.findViewById(R.id.mGridView);
        //初始化提示框
        dialog = new CustomDialog(getActivity(), LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,
                R.layout.dialog_girl,R.style.Theme_dialog, Gravity.CENTER,R.style.pop_anim_style);
        imageView = (ImageView) dialog.findViewById(R.id.iv_img);
        //解析数据
        RxVolley.get(StaticClass.GIRL_URL, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                Gson gson = new Gson();
                GirlBean bean = gson.fromJson(t,GirlBean.class);
                if (list == null){
                    list = new ArrayList<>();
                }
                list = bean.getResults();
                mAdapter = new GirlAdapter(getActivity(),list);
                gridView.setAdapter(mAdapter);
            }
        });
        //点击事件
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                PicassoUtil.loadImageView(getActivity(),list.get(position).getUrl(),imageView);
                //缩放
                mAttacher = new PhotoViewAttacher(imageView);
                //刷新
                mAttacher.update();
                dialog.show();
                dialog.setCanceledOnTouchOutside(true);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog != null){
            dialog.dismiss();
        }
    }
}
