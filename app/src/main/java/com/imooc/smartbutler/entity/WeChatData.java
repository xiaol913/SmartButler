package com.imooc.smartbutler.entity;

/**
 * 项目名：  SmartButler
 * 包名：    com.imooc.smartbutler.entity
 * 文件名：  WeChatData
 * 创建者：  Shawn Gao
 * 创建时间：2017/1/3120:22
 * 描述：    微信精选数据类
 */

public class WeChatData {
    //标题
    private String title;
    //出处
    private String source;
    //图片地址url
    private String firstImg;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getFirstImg() {
        return firstImg;
    }

    public void setFirstImg(String firstImg) {
        this.firstImg = firstImg;
    }
}
