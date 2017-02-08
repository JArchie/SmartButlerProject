package com.jarchie.smartbutler.entity;

import cn.bmob.v3.BmobUser;

/**
 * 项目名:   SmartButler
 * 包名:     com.jarchie.smartbutler.entity
 * 文件名:   MyUser
 * 创建者:   Jarchie
 * 创建时间: 17/1/17 上午11:34
 * 描述:     用户属性 记录用户信息的实体类
 */

public class MyUser extends BmobUser {
    private int age;
    private boolean sex;
    private String desc;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
