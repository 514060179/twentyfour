package com.yinghai.twentyfour.common.model;

import java.util.Date;

public class TfLuck extends TfLuckKey {
    private Integer luckId;

    private String lUndertaking;
    
    private String lColor;

    private String lEmotion;

    private Integer lNumber;

    private Date lCreateTime;

    private Date lUpdateTime;

    public String getlColor() {
		return lColor;
	}

	public void setlColor(String lColor) {
		this.lColor = lColor;
	}

	public Integer getLuckId() {
        return luckId;
    }

    public void setLuckId(Integer luckId) {
        this.luckId = luckId;
    }

    public String getlUndertaking() {
        return lUndertaking;
    }

    public void setlUndertaking(String lUndertaking) {
        this.lUndertaking = lUndertaking == null ? null : lUndertaking.trim();
    }

    public String getlEmotion() {
        return lEmotion;
    }

    public void setlEmotion(String lEmotion) {
        this.lEmotion = lEmotion == null ? null : lEmotion.trim();
    }

    public Integer getlNumber() {
        return lNumber;
    }

    public void setlNumber(Integer lNumber) {
        this.lNumber = lNumber;
    }

    public Date getlCreateTime() {
        return lCreateTime;
    }

    public void setlCreateTime(Date lCreateTime) {
        this.lCreateTime = lCreateTime;
    }

    public Date getlUpdateTime() {
        return lUpdateTime;
    }

    public void setlUpdateTime(Date lUpdateTime) {
        this.lUpdateTime = lUpdateTime;
    }
}