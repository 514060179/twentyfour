package com.yinghai.twentyfour.common.model;

import java.util.Date;

public class ThirdParty {
	/**
	 * 微信登录类型
	 */
	public static final int third_type_vx=0;
	/**
	 * QQ登录类型
	 */
	public static final int third_type_qq=1;
	/**
	 * 邮箱登录类型
	 */
	public static final int third_type_email=2;
	/**
	 * facebook登录类型
	 */
	public static final int third_type_fb=3;
	
	/**
	 * id
	 */
	private Integer thirdPartyId;
	/**
	 * 第三方登录唯一标识
	 */
	private String openId;
	/**
	 * 用户ID
	 */
	private Integer tfId;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 性别
	 */
	private Boolean sex;
	/**
	 * 登录第三方类型0微信 1QQ 2邮箱3faceBook
	 */
	private Integer type;
	/**
	 * 省份
	 */
	private String province;

	/**
	 * 国家
	 */
	private String country;

	/**
	 * 城市
	 */
	private String city;
	/**
	 * 名字
	 */
	private String nick;
	
	/**
	 * 名字
	 */
	private String imgUrl;

	/**
	 * 用户类型0为客户端用户1为大师端用户
	 */
	private Integer tfType;


	public Integer getThirdPartyId() {
		return thirdPartyId;
	}

	public void setThirdPartyId(Integer thirdPartyId) {
		this.thirdPartyId = thirdPartyId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public Integer getTfId() {
		return tfId;
	}

	public void setTfId(Integer tfId) {
		this.tfId = tfId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Boolean getSex() {
		return sex;
	}

	public void setSex(Boolean sex) {
		this.sex = sex;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public Integer getTfType() {
		return tfType;
	}

	public void setTfType(Integer tfType) {
		this.tfType = tfType;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

}
