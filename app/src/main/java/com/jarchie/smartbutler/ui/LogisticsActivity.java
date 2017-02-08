package com.jarchie.smartbutler.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.google.gson.Gson;
import com.jarchie.smartbutler.R;
import com.jarchie.smartbutler.adapter.LogisticsAdapter;
import com.jarchie.smartbutler.entity.LogisticsBean;
import com.jarchie.smartbutler.utils.StaticClass;
import com.jarchie.smartbutler.utils.UtilTools;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 项目名:   SmartButler
 * 包名:     com.jarchie.smartbutler.ui
 * 文件名:   LogisticsActivity
 * 创建者:   Jarchie
 * 创建时间: 17/1/22 上午9:51
 * 描述:     物流查询页面
 */

public class LogisticsActivity extends BaseActivity implements View.OnClickListener {
    private EditText comname, logisticsnum;
    private Button selectBtn;
    //列表
    private ListView listView;
    private List<LogisticsBean.ResultBean.ListBean> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_logistics);
        initView();
    }

    //初始化View
    private void initView() {
        comname = (EditText) findViewById(R.id.et_comname);
        logisticsnum = (EditText) findViewById(R.id.et_logisticsnum);
        selectBtn = (Button) findViewById(R.id.btn_select);
        selectBtn.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.mListView);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_select:
                String name = comname.getText().toString().trim();
                String number = logisticsnum.getText().toString().trim();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(number)) {
                    String url = "http://v.juhe.cn/exp/index?key=" + StaticClass.LOGISTICS_KEY
                            + "&com=" + name + "&no=" + number;
                    //请求网络数据,并展示到列表上
                    RxVolley.get(url, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            if (list == null) {
                                list = new ArrayList<>();
                            }
                            Gson gson = new Gson();
                            LogisticsBean bean = gson.fromJson(t, LogisticsBean.class);
                            list = bean.getResult().getList();
                            //倒序处理
                            Collections.reverse(list);
                            LogisticsAdapter mAdapter = new LogisticsAdapter(LogisticsActivity.this, list);
                            listView.setAdapter(mAdapter);
                        }

                        @Override
                        public void onFailure(int errorNo, String strMsg) {
                            UtilTools.toastShortMessage(LogisticsActivity.this, strMsg);
                        }
                    });
                } else {
                    UtilTools.toastShortMessage(this, "输入框不能为空");
                }
                break;
        }
    }
}
