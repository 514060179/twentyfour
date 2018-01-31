package com.yinghai.twentyfour.common.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import com.yinghai.twentyfour.common.constant.LogManager;
import com.yinghai.twentyfour.common.constant.WeChat;
import com.yinghai.twentyfour.common.service.WechatService;
import com.yinghai.twentyfour.common.util.EmojiUtil;
import com.yinghai.twentyfour.common.util.TransformUtils;

import net.sf.json.JSONObject;

public class WechatServiceImpl implements WechatService{
	/**
	 * request AccessToken 参数 code
	 */
	@Override
	public Map<String, Object> getOpenIdAccessToken(String code, String deviceType) {
		String URL = "";

		if("master".equals(deviceType)){
			URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + WeChat.masterappid + "&secret="
					+ WeChat.masterappsecret + "&code=" + code + "&grant_type=authorization_code";
		}else{
			URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + WeChat.userappid + "&secret="
					+ WeChat.userappsecret + "&code=" + code + "&grant_type=authorization_code";
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			URL url = new URL(URL);
			URLConnection urlcon = url.openConnection();
			InputStream is = urlcon.getInputStream();
			BufferedReader buffer = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			StringBuffer bs = new StringBuffer();
			String l = null;
			while ((l = buffer.readLine()) != null) {
				bs.append(l);
			}
			JSONObject json = JSONObject.fromObject(bs.toString());
			map.put("accessToken", (String) json.getString("access_token"));
			map.put("openid", (String) json.getString("openid"));
			map.put("unionid", (String) json.getString("unionid"));
		} catch (IOException e) {
			LogManager.error("get wechat openid fail" + e);
		}
		return map;
	}

	/**
	 * request 微信用户个人信息 参数 acccessToken、 openid
	 */
	@Override
	public Map<String, Object> getWeChatInfo(String acccessToken, String openid) {
		try {

		} catch (Exception e) {
			// TODO: handle exception
		}
		String URL = "https://api.weixin.qq.com/sns/userinfo?access_token=" + acccessToken + "&openid=" + openid;
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			URL url = new URL(URL);
			URLConnection urlcon = url.openConnection();
			InputStream is = urlcon.getInputStream();
			BufferedReader buffer = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			StringBuffer bs = new StringBuffer();
			String l = null;
			while ((l = buffer.readLine()) != null) {
				bs.append(l);
			}
			JSONObject json = JSONObject.fromObject(bs.toString());
			map.put("nickname", EmojiUtil.filterEmoji((String) json.getString("nickname"))); // 昵称
			map.put("headimgurl", (String) json.getString("headimgurl")); // 头像
			map.put("sex", TransformUtils.toString(json.getString("sex"))); // 性别1:男，2:女
			map.put("province", TransformUtils.toString(json.getString("province")));
			map.put("city", TransformUtils.toString(json.getString("city")));
			map.put("country", TransformUtils.toString(json.getString("country")));
			map.put("unionid", TransformUtils.toString(json.getString("unionid"))); // ios与安卓都相同的唯一标识
		} catch (IOException e) {
			LogManager.error("get wechat info fail" + e);
			return null;
		}
		return map;
	}
}
