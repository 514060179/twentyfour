package com.yinghai.twentyfour.common.model;

import java.util.Date;

public class TfMasterWallet {
    private Integer walletId;

    private Integer wMasterId;

    private Integer wBalance;

    private Date wCreateTime;

    private Date wCashTime;

    private Date wUpdateTime;

    public Integer getWalletId() {
        return walletId;
    }

    public void setWalletId(Integer walletId) {
        this.walletId = walletId;
    }

    public Integer getwMasterId() {
        return wMasterId;
    }

    public void setwMasterId(Integer wMasterId) {
        this.wMasterId = wMasterId;
    }

    public Integer getwBalance() {
        return wBalance;
    }

    public void setwBalance(Integer wBalance) {
        this.wBalance = wBalance;
    }

    public Date getwCreateTime() {
        return wCreateTime;
    }

    public void setwCreateTime(Date wCreateTime) {
        this.wCreateTime = wCreateTime;
    }

    public Date getwCashTime() {
        return wCashTime;
    }

    public void setwCashTime(Date wCashTime) {
        this.wCashTime = wCashTime;
    }

    public Date getwUpdateTime() {
        return wUpdateTime;
    }

    public void setwUpdateTime(Date wUpdateTime) {
        this.wUpdateTime = wUpdateTime;
    }
}