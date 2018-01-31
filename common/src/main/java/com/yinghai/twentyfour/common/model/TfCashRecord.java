package com.yinghai.twentyfour.common.model;

import java.util.Date;

public class TfCashRecord {

    /**
     * 提现申请
     */
    public final static int APPLY_CASH_CODE = 1;

    /**
     *
     * 提现申请成功
     */
    public final static int APPLY_SUCCESS_CODE = 2;

    /**
     * 完成提现
     */
    public final static int APPLY_COMPLETE_CODE = 3;
    private Integer recordId;

    private Integer rMasterId;

    private Integer rAmount;

    private Date rCreateTime;

    private Integer rStatus;

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public Integer getrMasterId() {
        return rMasterId;
    }

    public void setrMasterId(Integer rMasterId) {
        this.rMasterId = rMasterId;
    }

    public Integer getrAmount() {
        return rAmount;
    }

    public void setrAmount(Integer rAmount) {
        this.rAmount = rAmount;
    }

    public Date getrCreateTime() {
        return rCreateTime;
    }

    public void setrCreateTime(Date rCreateTime) {
        this.rCreateTime = rCreateTime;
    }

    public Integer getrStatus() {
        return rStatus;
    }

    public void setrStatus(Integer rStatus) {
        this.rStatus = rStatus;
    }
}