package com.yinghai.twentyfour.common.vo;

import com.yinghai.twentyfour.common.model.TfImUser;
import com.yinghai.twentyfour.common.model.TfMaster;
import com.yinghai.twentyfour.common.model.TfUser;

/**
 * Created by Administrator on 2017/12/7.
 */
public class MasterAndUserIm {

    private Integer orderId;

    private String orderNo;

    private TfUser tfUser;

    private TfMaster tfMaster;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public TfUser getTfUser() {
        return tfUser;
    }

    public void setTfUser(TfUser tfUser) {
        this.tfUser = tfUser;
    }

    public TfMaster getTfMaster() {
        return tfMaster;
    }

    public void setTfMaster(TfMaster tfMaster) {
        this.tfMaster = tfMaster;
    }
}
