package com.imooc.smartbutler.entity;

/**
 * 项目名：  SmartButler
 * 包名：    com.imooc.smartbutler.entity
 * 文件名：  CourierData
 * 创建者：  Shawn Gao
 * 创建时间：2017/1/3112:11
 * 描述：    快递查询实体
 */

public class CourierData {
    //时间
    private String datetime;
    //状态
    private String remark;
    //城市
    private String zone;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }
}
