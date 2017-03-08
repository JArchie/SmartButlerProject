package com.jarchie.smartbutler.application;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.baidu.mapapi.SDKInitializer;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.jarchie.smartbutler.utils.StaticClass;
import com.tencent.bugly.crashreport.CrashReport;

import cn.bmob.v3.Bmob;

/**
 * 项目名:   SmartButler
 * 包名:     com.jarchie.smartbutler.application
 * 文件名:   BaseApplication
 * 创建者:   Jarchie
 * 创建时间: 17/1/14 上午11:26
 * 描述:    自定义全局Application
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        //初始化Bugly
        CrashReport.initCrashReport(getApplicationContext(),
                StaticClass.BUGLY_APP_ID, true);
        //初始化Bmob后端云服务
        Bmob.initialize(this, StaticClass.BMOB_APP_ID);
        //初始化科大讯飞语音服务
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID + "=" + StaticClass.VOICE_KEY);
        //百度地图SDK
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
    }

}
