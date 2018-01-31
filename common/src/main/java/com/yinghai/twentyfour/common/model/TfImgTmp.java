package com.yinghai.twentyfour.common.model;

import java.util.Date;

/**
 * 附件
 * 
 * @author Administrator
 *
 */
public class TfImgTmp {
	public static final int itType_protect=1;
	public static final int itType_article=2;
	/**
	 * 唯一标识ID
	 */
	private Integer imgTmpId;
	/**
	 * 大师ID
	 */
	private Integer itMasterId;
	/**
	 * 关联ID
	 */
	private Integer itKeyId;
	
	/**
	 * 图片类型1商品图片2文章图片
	 */
	private Integer itType;
	/**
	 * 图片访问地址
	 */
	private String itUrl;
	/**
	 * 创建时间
	 */
	private Date itCreateTime;
	/**
	 * 更新时间
	 */
	private Date itUpdateTime;
	/**
	 * 服务器路径
	 */
	private String itAbsolute;
	
	/**
	 * APP前端访问路径
	 */
	private String itAppPath;
	/**
	 * 是否有使用
	 */
	private Boolean itIsUser;
	
	/**
	 * 图片尺寸
	 */
	private String itSize;

	
	public Integer getImgTmpId() {
		return imgTmpId;
	}

	public void setImgTmpId(Integer imgTmpId) {
		this.imgTmpId = imgTmpId;
	}

	public Integer getItMasterId() {
		return itMasterId;
	}

	public void setItMasterId(Integer itMasterId) {
		this.itMasterId = itMasterId;
	}

	public Integer getItKeyId() {
		return itKeyId;
	}

	public void setItKeyId(Integer itKeyId) {
		this.itKeyId = itKeyId;
	}

	public String getItUrl() {
		return itUrl;
	}

	public void setItUrl(String itUrl) {
		this.itUrl = itUrl;
	}

	public Date getItCreateTime() {
		return itCreateTime;
	}

	public void setItCreateTime(Date itCreateTime) {
		this.itCreateTime = itCreateTime;
	}

	public Date getItUpdateTime() {
		return itUpdateTime;
	}

	public void setItUpdateTime(Date itUpdateTime) {
		this.itUpdateTime = itUpdateTime;
	}

	public String getItAbsolute() {
		return itAbsolute;
	}

	public void setItAbsolute(String itAbsolute) {
		this.itAbsolute = itAbsolute;
	}

	public Boolean getItIsUser() {
		return itIsUser;
	}

	public void setItIsUser(Boolean itIsUser) {
		this.itIsUser = itIsUser;
	}

	public Integer getItType() {
		return itType;
	}

	public void setItType(Integer itType) {
		this.itType = itType;
	}

	public String getItAppPath() {
		return itAppPath;
	}

	public void setItAppPath(String itAppPath) {
		this.itAppPath = itAppPath;
	}

	public String getItSize() {
		return itSize;
	}

	public void setItSize(String itSize) {
		this.itSize = itSize;
	}

}
