package com.yinghai.twentyfour.common.service;

import java.util.Map;

/**
 * Created by Administrator on 2017/7/18.
 */
public interface WechatService {
	/**
	 * 
	 * @param code 微信同意登录标识
	 * @param deviceType 登录种类手机APP或者微信
	 * @return
	 */
	Map<String, Object> getOpenIdAccessToken(String code,String deviceType);

	/**
	 * request 微信用户个人信息 参数 acccessToken、 openid
	 */
	Map<String, Object> getWeChatInfo(String acccessToken, String openid);
}
