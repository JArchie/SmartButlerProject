package com.jarchie.smartbutler.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 项目名:   SmartButler
 * 包名:     com.jarchie.smartbutler.utils
 * 文件名:   ShareUtil
 * 创建者:   Jarchie
 * 创建时间: 17/1/15 下午4:33
 * 描述:     封装SharedPreferences工具类
 */

public class ShareUtil {
    public static final String NAME = "config";

    //string类型 键 值
    public static void putString(Context mContext,String key,String value){
        SharedPreferences sp = mContext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().putString(key,value).commit();
    }

    //string类型 键 默认值
    public static String getString(Context mContext,String key,String defValue){
        SharedPreferences sp = mContext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return sp.getString(key,defValue);
    }

    //int类型 键 值
    public static void putInt(Context mContext,String key,int value){
        SharedPreferences sp = mContext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().putInt(key,value).commit();
    }

    //int类型 键 默认值
    public static int getInt(Context mContext,String key,int defValue){
        SharedPreferences sp = mContext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return sp.getInt(key,defValue);
    }

    //boolean类型 键 值
    public static void putBoolean(Context mContext,String key,boolean value){
        SharedPreferences sp = mContext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).commit();
    }

    //boolean类型 键 默认值
    public static boolean getBoolean(Context mContext,String key,boolean defValue){
        SharedPreferences sp = mContext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return sp.getBoolean(key,defValue);
    }

    //删除单个
    public static void delShare(Context mContext,String key){
        SharedPreferences sp = mContext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().remove(key).commit();
    }

    //删除全部
    public static void delAllShare(Context mContext){
        SharedPreferences sp = mContext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }

}
