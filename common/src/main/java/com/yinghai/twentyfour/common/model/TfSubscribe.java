package com.yinghai.twentyfour.common.model;

import java.util.Date;

public class TfSubscribe extends TfSubscribeKey {
    private Integer subscribeId;

    private Date sCreateTime;

    public Integer getSubscribeId() {
        return subscribeId;
    }

    public void setSubscribeId(Integer subscribeId) {
        this.subscribeId = subscribeId;
    }

    public Date getsCreateTime() {
        return sCreateTime;
    }

    public void setsCreateTime(Date sCreateTime) {
        this.sCreateTime = sCreateTime;
    }
}