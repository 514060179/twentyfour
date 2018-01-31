package com.yinghai.twentyfour.common.model;

import java.util.Date;

/**
 * 好友关系类
 * @author Administrator
 *
 */
public class TfRelation {
	Integer relationId;
	String rUserId;//用户im的ID
	String rFriendId;//目标用户im的ID
	Boolean rIsValidate;//好友关系是否同意
	Integer rGroupId;//好友分组ID
	Integer rVisible;//朋友圈可见权限
	Integer rSee;//是否看对方朋友圈
	Date rCreateTime;
	Date rUpdateTime;
	Integer rDelete;//是否拉黑
	Integer rAdd;//申请好友的情况，1为A申请B，2为相互申请
	
	public Integer getrAdd() {
		return rAdd;
	}
	public void setrAdd(Integer rAdd) {
		this.rAdd = rAdd;
	}
	public Integer getRelationId() {
		return relationId;
	}
	public void setRelationId(Integer relationId) {
		this.relationId = relationId;
	}
	public String getrUserId() {
		return rUserId;
	}
	public void setrUserId(String rUserId) {
		this.rUserId = rUserId;
	}
	public String getrFriendId() {
		return rFriendId;
	}
	public void setrFriendId(String rFriendId) {
		this.rFriendId = rFriendId;
	}
	public Boolean getrIsValidate() {
		return rIsValidate;
	}
	public void setrIsValidate(Boolean rIsValidate) {
		this.rIsValidate = rIsValidate;
	}
	public Integer getrGroupId() {
		return rGroupId;
	}
	public void setrGroupId(Integer rGroupId) {
		this.rGroupId = rGroupId;
	}
	public Integer getrVisible() {
		return rVisible;
	}
	public void setrVisible(Integer rVisible) {
		this.rVisible = rVisible;
	}
	public Integer getrSee() {
		return rSee;
	}
	public void setrSee(Integer rSee) {
		this.rSee = rSee;
	}
	public Date getrCreateTime() {
		return rCreateTime;
	}
	public void setrCreateTime(Date rCreateTime) {
		this.rCreateTime = rCreateTime;
	}
	public Date getrUpdateTime() {
		return rUpdateTime;
	}
	public void setrUpdateTime(Date rUpdateTime) {
		this.rUpdateTime = rUpdateTime;
	}
	public Integer getrDelete() {
		return rDelete;
	}
	public void setrDelete(Integer rDelete) {
		this.rDelete = rDelete;
	}

	
}
