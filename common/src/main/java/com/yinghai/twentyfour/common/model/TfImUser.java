package com.yinghai.twentyfour.common.model;

import java.util.Date;

public class TfImUser {
    private Integer iuId;

    private Integer iuType;

    private String iuUserId;

    private String iuName;

    private String iuImg;

    private String iuToken;

    private Date iuCreateTime;

    private Date iuUpdateTime;

    public Integer getIuType() {
        return iuType;
    }

    public void setIuType(Integer iuType) {
        this.iuType = iuType;
    }

    public String getIuToken() {
        return iuToken;
    }

    public void setIuToken(String iuToken) {
        this.iuToken = iuToken;
    }

    public Integer getIuId() {
        return iuId;
    }

    public void setIuId(Integer iuId) {
        this.iuId = iuId;
    }

    public String getIuUserId() {
        return iuUserId;
    }

    public void setIuUserId(String iuUserId) {
        this.iuUserId = iuUserId == null ? null : iuUserId.trim();
    }

    public String getIuName() {
        return iuName;
    }

    public void setIuName(String iuName) {
        this.iuName = iuName == null ? null : iuName.trim();
    }

    public String getIuImg() {
        return iuImg;
    }

    public void setIuImg(String iuImg) {
        this.iuImg = iuImg == null ? null : iuImg.trim();
    }

    public Date getIuCreateTime() {
        return iuCreateTime;
    }

    public void setIuCreateTime(Date iuCreateTime) {
        this.iuCreateTime = iuCreateTime;
    }

    public Date getIuUpdateTime() {
        return iuUpdateTime;
    }

    public void setIuUpdateTime(Date iuUpdateTime) {
        this.iuUpdateTime = iuUpdateTime;
    }
}