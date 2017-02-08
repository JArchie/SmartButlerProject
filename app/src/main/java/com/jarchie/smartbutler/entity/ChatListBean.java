package com.jarchie.smartbutler.entity;

/**
 * 项目名:   SmartButler
 * 包名:     com.jarchie.smartbutler.entity
 * 文件名:   ChatListBean
 * 创建者:   Jarchie
 * 创建时间: 17/1/22 下午7:25
 * 描述:     对话列表的实体类
 */

public class ChatListBean {
    private String text;
    private int type;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
