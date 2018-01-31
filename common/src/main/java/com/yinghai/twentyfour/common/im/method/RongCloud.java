/**
 * 融云 Server API java 客户端
 * create by kitName
 * create datetime : 2017-03-13 
 * 
 * v2.0.1
 */
package com.yinghai.twentyfour.common.im.method;

import java.util.concurrent.ConcurrentHashMap;


public class RongCloud {

	private static ConcurrentHashMap<String, RongCloud> rongCloud = new ConcurrentHashMap<String,RongCloud>();
	
	public User user;
	public Message message;
	public Group group;
	public Push push;

	private RongCloud(String appKey, String appSecret) {
		user = new User(appKey, appSecret);
		message = new Message(appKey, appSecret);
		group = new Group(appKey, appSecret);
		push = new Push(appKey, appSecret);

	}

	public static RongCloud getInstance(String appKey, String appSecret) {
		if (null == rongCloud.get(appKey)) {
			rongCloud.putIfAbsent(appKey, new RongCloud(appKey, appSecret));
		}
		return rongCloud.get(appKey);
	}
	 
}