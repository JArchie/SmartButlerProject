package com.jarchie.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.jarchie.smartbutler.MainActivity;
import com.jarchie.smartbutler.R;
import com.jarchie.smartbutler.utils.ShareUtil;
import com.jarchie.smartbutler.utils.StaticClass;
import com.jarchie.smartbutler.utils.UtilTools;

/**
 * 项目名:   SmartButler
 * 包名:     com.jarchie.smartbutler.ui
 * 文件名:   SplashActivity
 * 创建者:   Jarchie
 * 创建时间: 17/1/16 下午2:21
 * 描述:     闪屏页
 * 延时2秒,判断程序是否第一次运行,自定义字体,Activity全屏主题
 */

public class SplashActivity extends AppCompatActivity {
    private TextView textView;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case StaticClass.HANDLER_SPLASH:
                    //判断程序是否是第一次运行
                    if (isFirst()) {
                        startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                    finish();
                    break;
            }
        }
    };

    //判断程序是否第一次运行
    private boolean isFirst() {
        boolean isFirst = ShareUtil.getBoolean(this, StaticClass.SHARE_IS_FIRST, true);
        if (isFirst) {
            //标记我们已经启动过App了
            ShareUtil.putBoolean(this, StaticClass.SHARE_IS_FIRST, false);
            //是第一次运行
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
    }

    //初始化View
    private void initView() {
        //延时2s
        handler.sendEmptyMessageDelayed(StaticClass.HANDLER_SPLASH, 2000);
        textView = (TextView) findViewById(R.id.tv_splash);
        //设置字体
        UtilTools.setFont(this, textView);
    }

    //禁止返回键
    @Override
    public void onBackPressed() {
    }
}
