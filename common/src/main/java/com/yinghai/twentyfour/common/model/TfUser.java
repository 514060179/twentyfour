package com.yinghai.twentyfour.common.model;

import java.util.Date;

public class TfUser {
	/**
	 * 用户信息提示
	 */
	public static final String TfUser_VERIFY_CODE_MSG="您的24占驗證碼是:";
	/**
	 * 用户注册信息提示
	 */
	public static final String TfUser_REGISTER_CODE_MSG="您的24占註冊驗證碼是:";
	/**
	 * 用户信息提示
	 */
	public static final String TfUser_FORGET_CODE_MSG="您的24占忘記密碼驗證碼是:";
	
	/**
	 * 用戶忘記密碼sessionkey
	 */
	public static final String TfUser_FORGET_SESSION_KEY="forgetCode";
	
	/**
	 * 用戶註冊sessionkey
	 */
	public static final String TfUser_REGISTER_SESSION_KEY="registerCode";
	
	/**
	 * 用户在线
	 */
	public static final int User_Online = 1;
	/**
	 * 用户离线
	 */
	public static final int User_Offline = 999;
    private Integer userId;

    private String uNick;

    private String uCountryCode;

    private String uTel;

    private String uPassword;

    private Integer uStatus;

    private Date uBirthday;

    private Boolean uSex;

    private Integer uConstellation;

    private Date uCreateTime;

    private Date uUpdateTime;

    private Boolean uDeleted;

    private Integer uAccid;

    private String uImgUrl;

    private String uDeviceId;

    private Integer uDeviceType;

    private String uIm;

    private String uToken;

    public String getuIm() {
        return uIm;
    }

    public void setuIm(String uIm) {
        this.uIm = uIm;
    }

    public String getuToken() {
        return uToken;
    }

    public void setuToken(String uToken) {
        this.uToken = uToken;
    }

    public String getuImgUrl() {
        return uImgUrl;
    }

    public void setuImgUrl(String uImgUrl) {
        this.uImgUrl = uImgUrl;
    }

    public String getuDeviceId() {
		return uDeviceId;
	}

	public void setuDeviceId(String uDeviceId) {
		this.uDeviceId = uDeviceId;
	}

	public Integer getuDeviceType() {
		return uDeviceType;
	}

	public void setuDeviceType(Integer uDeviceType) {
		this.uDeviceType = uDeviceType;
	}

    public Integer getuAccid() {
        return uAccid;
    }

    public void setuAccid(Integer uAccid) {
        this.uAccid = uAccid;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getuNick() {
        return uNick;
    }

    public void setuNick(String uNick) {
        this.uNick = uNick == null ? null : uNick.trim();
    }

    public String getuCountryCode() {
        return uCountryCode;
    }

    public void setuCountryCode(String uCountryCode) {
        this.uCountryCode = uCountryCode == null ? null : uCountryCode.trim();
    }

    public String getuTel() {
        return uTel;
    }

    public void setuTel(String uTel) {
        this.uTel = uTel == null ? null : uTel.trim();
    }

    public String getuPassword() {
        return uPassword;
    }

    public void setuPassword(String uPassword) {
        this.uPassword = uPassword == null ? null : uPassword.trim();
    }

    public Integer getuStatus() {
        return uStatus;
    }

    public void setuStatus(Integer uStatus) {
        this.uStatus = uStatus;
    }

    public Date getuBirthday() {
        return uBirthday;
    }

    public void setuBirthday(Date uBirthday) {
        this.uBirthday = uBirthday;
    }

    public Boolean getuSex() {
        return uSex;
    }

    public void setuSex(Boolean uSex) {
        this.uSex = uSex;
    }

    public Integer getuConstellation() {
        return uConstellation;
    }

    public void setuConstellation(Integer uConstellation) {
        this.uConstellation = uConstellation;
    }

    public Date getuCreateTime() {
        return uCreateTime;
    }

    public void setuCreateTime(Date uCreateTime) {
        this.uCreateTime = uCreateTime;
    }

    public Date getuUpdateTime() {
        return uUpdateTime;
    }

    public void setuUpdateTime(Date uUpdateTime) {
        this.uUpdateTime = uUpdateTime;
    }

    public Boolean getuDeleted() {
        return uDeleted;
    }

    public void setuDeleted(Boolean uDeleted) {
        this.uDeleted = uDeleted;
    }
}