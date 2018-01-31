package com.yinghai.twentyfour.common.model;

import java.util.Date;

public class TfMaster {
	/**
	 * 用户信息提示
	 */
	public static final String TfMaster_VERIFY_CODE_MSG="您的24占驗證碼是:";
	/**
	 * 大师在线
	 */
	public static final int Master_Online = 1;
	/**
	 * 大师离线
	 */
	public static final int Master_Offline = 999;
	
    private Integer masterId;

    private String mNick;

    private Boolean mSex;

    private Integer mStatus;

    private String mHeadImg;

    private Integer mConstellation;

    private String mCountryCode;

    private String mTel;

    private String mPassword;

    private String mAddress;

    private String mBusinessType;

    private String mLabel;

    private Integer mBargain;

    private Long mFollows;

    private Long mDeals;

    private Double mScore;

    private Date mCreateTime;

    private Date mUpdateTime;

    private Boolean mDeleted;

    private String mIntroduction;

    private Integer mAccid;
    
    private String mDeviceId;

    private String mIm;

    private String mToken;

    private Integer mDeviceType;
    
    private Integer queryUserId;
    
    private Boolean isCollection;
    
    private Boolean isSubscribe;
    public String getmIm() {
        return mIm;
    }

    public void setmIm(String mIm) {
        this.mIm = mIm;
    }

    public String getmToken() {
        return mToken;
    }

    public void setmToken(String mToken) {
        this.mToken = mToken;
    }

    public String getmDeviceId() {
		return mDeviceId;
	}

	public void setmDeviceId(String mDeviceId) {
		this.mDeviceId = mDeviceId;
	}

	public Integer getmDeviceType() {
		return mDeviceType;
	}

	public void setmDeviceType(Integer mDeviceType) {
		this.mDeviceType = mDeviceType;
	}

	public Integer getmAccid() {
        return mAccid;
    }

    public void setmAccid(Integer mAccid) {
        this.mAccid = mAccid;
    }

    public Integer getMasterId() {
        return masterId;
    }

    public void setMasterId(Integer masterId) {
        this.masterId = masterId;
    }

    public String getmNick() {
        return mNick;
    }

    public void setmNick(String mNick) {
        this.mNick = mNick == null ? null : mNick.trim();
    }

    public Boolean getmSex() {
        return mSex;
    }

    public void setmSex(Boolean mSex) {
        this.mSex = mSex;
    }

    public Integer getmStatus() {
        return mStatus;
    }

    public void setmStatus(Integer mStatus) {
        this.mStatus = mStatus;
    }

    public String getmHeadImg() {
        return mHeadImg;
    }

    public void setmHeadImg(String mHeadImg) {
        this.mHeadImg = mHeadImg == null ? null : mHeadImg.trim();
    }

    public Integer getmConstellation() {
        return mConstellation;
    }

    public void setmConstellation(Integer mConstellation) {
        this.mConstellation = mConstellation;
    }

    public String getmCountryCode() {
        return mCountryCode;
    }

    public void setmCountryCode(String mCountryCode) {
        this.mCountryCode = mCountryCode == null ? null : mCountryCode.trim();
    }

    public String getmTel() {
        return mTel;
    }

    public void setmTel(String mTel) {
        this.mTel = mTel == null ? null : mTel.trim();
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword == null ? null : mPassword.trim();
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress == null ? null : mAddress.trim();
    }

    public String getmBusinessType() {
        return mBusinessType;
    }

    public void setmBusinessType(String mBusinessType) {
        this.mBusinessType = mBusinessType == null ? null : mBusinessType.trim();
    }

    public String getmLabel() {
        return mLabel;
    }

    public void setmLabel(String mLabel) {
        this.mLabel = mLabel == null ? null : mLabel.trim();
    }

    public Integer getmBargain() {
        return mBargain;
    }

    public void setmBargain(Integer mBargain) {
        this.mBargain = mBargain;
    }

    public Long getmFollows() {
        return mFollows;
    }

    public void setmFollows(Long mFollows) {
        this.mFollows = mFollows;
    }

    public Long getmDeals() {
        return mDeals;
    }

    public void setmDeals(Long mDeals) {
        this.mDeals = mDeals;
    }

    public Double getmScore() {
        return mScore;
    }

    public void setmScore(Double mScore) {
        this.mScore = mScore;
    }

    public Date getmCreateTime() {
        return mCreateTime;
    }

    public void setmCreateTime(Date mCreateTime) {
        this.mCreateTime = mCreateTime;
    }

    public Date getmUpdateTime() {
        return mUpdateTime;
    }

    public void setmUpdateTime(Date mUpdateTime) {
        this.mUpdateTime = mUpdateTime;
    }

    public Boolean getmDeleted() {
        return mDeleted;
    }

    public void setmDeleted(Boolean mDeleted) {
        this.mDeleted = mDeleted;
    }

    public String getmIntroduction() {
        return mIntroduction;
    }

    public void setmIntroduction(String mIntroduction) {
        this.mIntroduction = mIntroduction == null ? null : mIntroduction.trim();
    }

	public Integer getQueryUserId() {
		return queryUserId;
	}

	public void setQueryUserId(Integer queryUserId) {
		this.queryUserId = queryUserId;
	}

	public Boolean getIsCollection() {
		return isCollection;
	}

	public void setIsCollection(Boolean isCollection) {
		this.isCollection = isCollection;
	}

	public Boolean getIsSubscribe() {
		return isSubscribe;
	}

	public void setIsSubscribe(Boolean isSubscribe) {
		this.isSubscribe = isSubscribe;
	}
}