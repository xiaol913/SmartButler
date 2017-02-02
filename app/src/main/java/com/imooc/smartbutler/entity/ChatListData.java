package com.imooc.smartbutler.entity;

/**
 * 项目名：  SmartButler
 * 包名：    com.imooc.smartbutler.entity
 * 文件名：  ChatListData
 * 创建者：  Shawn Gao
 * 创建时间：2017/1/3114:56
 * 描述：    对话列表实体
 */

public class ChatListData {
    //用于区分左边右边

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    private int type;
    //文本
    private String text;
}
