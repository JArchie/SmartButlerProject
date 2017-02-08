package com.jarchie.smartbutler.utils;

import android.util.Log;

/**
 * 项目名:   SmartButler
 * 包名:     com.jarchie.smartbutler.utils
 * 文件名:   LogUtil
 * 创建者:   Jarchie
 * 创建时间: 17/1/15 下午4:10
 * 描述:     Log封装类
 */

public class LogUtil {
    //开关
    public static final boolean DEBUG = true;
    //TAG
    public static final String TAG = "Jarchie";

    //五个等级 d、i、w、e
    public static void d(String text){
        if (DEBUG){
            Log.d(TAG,text);
        }
    }

    public static void i(String text){
        if (DEBUG){
            Log.i(TAG,text);
        }
    }

    public static void w(String text){
        if (DEBUG){
            Log.w(TAG,text);
        }
    }

    public static void e(String text){
        if (DEBUG){
            Log.e(TAG,text);
        }
    }

}
