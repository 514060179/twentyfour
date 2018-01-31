package com.yinghai.twentyfour.common.model;

import java.util.Date;

public class TfAccessTokens {
    private Integer accessId;

    private Integer umId;

    private String deviceId;

    private Integer deviceType;

    private Date createTime;

    public Integer getAccessId() {
        return accessId;
    }

    public void setAccessId(Integer accessId) {
        this.accessId = accessId;
    }

    public Integer getUmId() {
        return umId;
    }

    public void setUmId(Integer umId) {
        this.umId = umId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}