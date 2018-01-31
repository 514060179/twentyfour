package com.yinghai.twentyfour.common.model;

import java.util.Date;

public class TfCar {
    private Integer carId;

    private Integer cUserId;

    private Integer cProductId;

    private Integer cQty;

    private Date cCreateTime;

    private Date cUpdateTime;

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public Integer getcUserId() {
        return cUserId;
    }

    public void setcUserId(Integer cUserId) {
        this.cUserId = cUserId;
    }

    public Integer getcProductId() {
        return cProductId;
    }

    public void setcProductId(Integer cProductId) {
        this.cProductId = cProductId;
    }

    public Integer getcQty() {
        return cQty;
    }

    public void setcQty(Integer cQty) {
        this.cQty = cQty;
    }

    public Date getcCreateTime() {
        return cCreateTime;
    }

    public void setcCreateTime(Date cCreateTime) {
        this.cCreateTime = cCreateTime;
    }

    public Date getcUpdateTime() {
        return cUpdateTime;
    }

    public void setcUpdateTime(Date cUpdateTime) {
        this.cUpdateTime = cUpdateTime;
    }
}