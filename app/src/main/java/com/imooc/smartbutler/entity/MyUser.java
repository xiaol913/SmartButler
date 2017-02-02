package com.imooc.smartbutler.entity;

import cn.bmob.v3.BmobUser;

/**
 * 项目名：  SmartButler
 * 包名：    com.imooc.smartbutler.entity
 * 文件名：  MyUser
 * 创建者：  Shawn Gao
 * 创建时间：2017/1/2920:27
 * 描述：    用戶屬性
 */

public class MyUser extends BmobUser {
    private int age;
    private boolean sex;//true男false女
    private String desc;
    private String userFaceUrl;//头像

    public String getUserFaceUrl() {
        return userFaceUrl;
    }

    public void setUserFaceUrl(String userFaceUrl) {
        this.userFaceUrl = userFaceUrl;
    }

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
