package com.yinghai.twentyfour.common.model;

public class TfLuckWithBLOBs extends TfLuck {
    private String lSuit;

    private String lUnsuitable;

    private String lMore;

    public String getlSuit() {
        return lSuit;
    }

    public void setlSuit(String lSuit) {
        this.lSuit = lSuit == null ? null : lSuit.trim();
    }

    public String getlUnsuitable() {
        return lUnsuitable;
    }

    public void setlUnsuitable(String lUnsuitable) {
        this.lUnsuitable = lUnsuitable == null ? null : lUnsuitable.trim();
    }

    public String getlMore() {
        return lMore;
    }

    public void setlMore(String lMore) {
        this.lMore = lMore == null ? null : lMore.trim();
    }
}