package com.yinghai.twentyfour.common.model;
/**
 * 关注类的相关信息实体类
 * @author Administrator
 *
 */
public class TfSubscribeHelper extends TfSubscribe {
	private String img;
	private String nick;
	private Long follows;
	
	public Long getFollows() {
		return follows;
	}
	public void setFollows(Long follows) {
		this.follows = follows;
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
