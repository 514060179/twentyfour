package com.yinghai.twentyfour.common.model;

public class TfCommentWithBLOBs extends TfComment {
	private Integer userId;
	private String img;
	private String nick;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	
}
