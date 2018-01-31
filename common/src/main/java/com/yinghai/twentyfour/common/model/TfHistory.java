package com.yinghai.twentyfour.common.model;

import java.util.Date;

public class TfHistory {
    private Integer historyId;

    private Integer hUserId;

    private Integer hProductId;

    private Integer hArticleId;

    private Integer hMasterId;

    private Date hCreateTime;

    private Integer hType;
    
    private Date hUpdateTime;
    
    public Date gethUpdateTime() {
		return hUpdateTime;
	}

	public void sethUpdateTime(Date hUpdateTime) {
		this.hUpdateTime = hUpdateTime;
	}

	public Integer getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Integer historyId) {
        this.historyId = historyId;
    }

    public Integer gethUserId() {
        return hUserId;
    }

    public void sethUserId(Integer hUserId) {
        this.hUserId = hUserId;
    }

    public Integer gethProductId() {
        return hProductId;
    }

    public void sethProductId(Integer hProductId) {
        this.hProductId = hProductId;
    }

    public Integer gethArticleId() {
        return hArticleId;
    }

    public void sethArticleId(Integer hArticleId) {
        this.hArticleId = hArticleId;
    }

    public Integer gethMasterId() {
        return hMasterId;
    }

    public void sethMasterId(Integer hMasterId) {
        this.hMasterId = hMasterId;
    }
    
    public Date gethCreateTime() {
		return hCreateTime;
	}

	public void sethCreateTime(Date hCreateTime) {
		this.hCreateTime = hCreateTime;
	}

	public Integer gethType() {
        return hType;
    }

    public void sethType(Integer hType) {
        this.hType = hType;
    }
}