package com.yinghai.twentyfour.common.im.method;

import com.yinghai.twentyfour.common.im.constant.App;
import com.yinghai.twentyfour.common.im.model.*;
import com.yinghai.twentyfour.common.im.msg.BaseMessage;
import com.yinghai.twentyfour.common.im.util.GsonUtil;
import com.yinghai.twentyfour.common.im.util.HostType;
import com.yinghai.twentyfour.common.im.util.HttpUtil;

import java.net.HttpURLConnection;
import java.net.URLEncoder;

public class User {

	private static final String UTF8 = "UTF-8";
	private String appKey;
	private String appSecret;

	public User(){
		appKey = App.appKey;
		appSecret = App.appSecret;
	}
	public User(String appKey, String appSecret) {
		this.appKey = appKey;
		this.appSecret = appSecret;

	}
	
	
	/**
	 * 获取 Token 方法 
	 * 
	 * @param  userId:用户 Id，最大长度 64 字节.是用户在 App 中的唯一标识码，必须保证在同一个 App 内不重复，重复的用户 Id 将被当作是同一用户。（必传）
	 * @param  name:用户名称，最大长度 128 字节.用来在 Push 推送时显示用户的名称.用户名称，最大长度 128 字节.用来在 Push 推送时显示用户的名称。（必传）
	 * @param  portraitUri:用户头像 URI，最大长度 1024 字节.用来在 Push 推送时显示用户的头像。（必传）
	 *
	 * @return TokenResult
	 **/
	public TokenResult getToken(String userId, String name, String portraitUri) throws Exception {
		if (userId == null) {
			throw new IllegalArgumentException("Paramer 'userId' is required");
		}
		
		if (name == null) {
			throw new IllegalArgumentException("Paramer 'name' is required");
		}
		
		if (portraitUri == null) {
			throw new IllegalArgumentException("Paramer 'portraitUri' is required");
		}
		
	    StringBuilder sb = new StringBuilder();
	    sb.append("&userId=").append(URLEncoder.encode(userId.toString(), UTF8));
	    sb.append("&name=").append(URLEncoder.encode(name.toString(), UTF8));
	    sb.append("&portraitUri=").append(URLEncoder.encode(portraitUri.toString(), UTF8));
		String body = sb.toString();
	   	if (body.indexOf("&") == 0) {
	   		body = body.substring(1, body.length());
	   	}
	   	
		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(HostType.API, appKey, appSecret, "/user/getToken.json", "application/x-www-form-urlencoded");
		HttpUtil.setBodyParameter(body, conn);
	    
	    return (TokenResult) GsonUtil.fromJson(HttpUtil.returnResult(conn), TokenResult.class);
	}
	
	/**
	 * 刷新用户信息方法 
	 * 
	 * @param  userId:用户 Id，最大长度 64 字节.是用户在 App 中的唯一标识码，必须保证在同一个 App 内不重复，重复的用户 Id 将被当作是同一用户。（必传）
	 * @param  name:用户名称，最大长度 128 字节。用来在 Push 推送时，显示用户的名称，刷新用户名称后 5 分钟内生效。（可选，提供即刷新，不提供忽略）
	 * @param  portraitUri:用户头像 URI，最大长度 1024 字节。用来在 Push 推送时显示。（可选，提供即刷新，不提供忽略）
	 *
	 * @return CodeSuccessResult
	 **/
	public CodeSuccessResult refresh(String userId, String name, String portraitUri) throws Exception {
		if (userId == null) {
			throw new IllegalArgumentException("Paramer 'userId' is required");
		}
		
	    StringBuilder sb = new StringBuilder();
	    sb.append("&userId=").append(URLEncoder.encode(userId.toString(), UTF8));
	    
	    if (name != null) {
	    	sb.append("&name=").append(URLEncoder.encode(name.toString(), UTF8));
	    }
	    
	    if (portraitUri != null) {
	    	sb.append("&portraitUri=").append(URLEncoder.encode(portraitUri.toString(), UTF8));
	    }
		String body = sb.toString();
	   	if (body.indexOf("&") == 0) {
	   		body = body.substring(1, body.length());
	   	}
	   	
		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(HostType.API, appKey, appSecret, "/user/refresh.json", "application/x-www-form-urlencoded");
		HttpUtil.setBodyParameter(body, conn);
	    
	    return (CodeSuccessResult) GsonUtil.fromJson(HttpUtil.returnResult(conn), CodeSuccessResult.class);
	}
	
	/**
	 * 检查用户在线状态 方法 
	 * 
	 * @param  userId:用户 Id，最大长度 64 字节。是用户在 App 中的唯一标识码，必须保证在同一个 App 内不重复，重复的用户 Id 将被当作是同一用户。（必传）
	 *
	 * @return CheckOnlineResult
	 **/
	public CheckOnlineResult checkOnline(String userId) throws Exception {
		if (userId == null) {
			throw new IllegalArgumentException("Paramer 'userId' is required");
		}
		
	    StringBuilder sb = new StringBuilder();
	    sb.append("&userId=").append(URLEncoder.encode(userId.toString(), UTF8));
		String body = sb.toString();
	   	if (body.indexOf("&") == 0) {
	   		body = body.substring(1, body.length());
	   	}
	   	
		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(HostType.API, appKey, appSecret, "/user/checkOnline.json", "application/x-www-form-urlencoded");
		HttpUtil.setBodyParameter(body, conn);
	    
	    return (CheckOnlineResult) GsonUtil.fromJson(HttpUtil.returnResult(conn), CheckOnlineResult.class);
	}
	
	/**
	 * 封禁用户方法（每秒钟限 100 次） 
	 * 
	 * @param  userId:用户 Id。（必传）
	 * @param  minute:封禁时长,单位为分钟，最大值为43200分钟。（必传）
	 *
	 * @return CodeSuccessResult
	 **/
	public CodeSuccessResult block(String userId, Integer minute) throws Exception {
		if (userId == null) {
			throw new IllegalArgumentException("Paramer 'userId' is required");
		}
		
		if (minute == null) {
			throw new IllegalArgumentException("Paramer 'minute' is required");
		}
		
	    StringBuilder sb = new StringBuilder();
	    sb.append("&userId=").append(URLEncoder.encode(userId.toString(), UTF8));
	    sb.append("&minute=").append(URLEncoder.encode(minute.toString(), UTF8));
		String body = sb.toString();
	   	if (body.indexOf("&") == 0) {
	   		body = body.substring(1, body.length());
	   	}
	   	
		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(HostType.API, appKey, appSecret, "/user/block.json", "application/x-www-form-urlencoded");
		HttpUtil.setBodyParameter(body, conn);
	    
	    return (CodeSuccessResult) GsonUtil.fromJson(HttpUtil.returnResult(conn), CodeSuccessResult.class);
	}
	
	/**
	 * 解除用户封禁方法（每秒钟限 100 次） 
	 * 
	 * @param  userId:用户 Id。（必传）
	 *
	 * @return CodeSuccessResult
	 **/
	public CodeSuccessResult unBlock(String userId) throws Exception {
		if (userId == null) {
			throw new IllegalArgumentException("Paramer 'userId' is required");
		}
		
	    StringBuilder sb = new StringBuilder();
	    sb.append("&userId=").append(URLEncoder.encode(userId.toString(), UTF8));
		String body = sb.toString();
	   	if (body.indexOf("&") == 0) {
	   		body = body.substring(1, body.length());
	   	}
	   	
		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(HostType.API, appKey, appSecret, "/user/unblock.json", "application/x-www-form-urlencoded");
		HttpUtil.setBodyParameter(body, conn);
	    
	    return (CodeSuccessResult) GsonUtil.fromJson(HttpUtil.returnResult(conn), CodeSuccessResult.class);
	}
	
	/**
	 * 获取被封禁用户方法（每秒钟限 100 次） 
	 * 
	 *
	 * @return QueryBlockUserResult
	 **/
	public QueryBlockUserResult queryBlock() throws Exception {
	    StringBuilder sb = new StringBuilder();
		String body = sb.toString();
	   	if (body.indexOf("&") == 0) {
	   		body = body.substring(1, body.length());
	   	}
	   	
		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(HostType.API, appKey, appSecret, "/user/block/query.json", "application/x-www-form-urlencoded");
		HttpUtil.setBodyParameter(body, conn);
	    
	    return (QueryBlockUserResult) GsonUtil.fromJson(HttpUtil.returnResult(conn), QueryBlockUserResult.class);
	}
	
	/**
	 * 添加用户到黑名单方法（每秒钟限 100 次） 
	 * 
	 * @param  userId:用户 Id。（必传）
	 * @param  blackUserId:被加到黑名单的用户Id。（必传）
	 *
	 * @return CodeSuccessResult
	 **/
	public CodeSuccessResult addBlacklist(String userId, String blackUserId) throws Exception {
		if (userId == null) {
			throw new IllegalArgumentException("Paramer 'userId' is required");
		}
		
		if (blackUserId == null) {
			throw new IllegalArgumentException("Paramer 'blackUserId' is required");
		}
		
	    StringBuilder sb = new StringBuilder();
	    sb.append("&userId=").append(URLEncoder.encode(userId.toString(), UTF8));
	    sb.append("&blackUserId=").append(URLEncoder.encode(blackUserId.toString(), UTF8));
		String body = sb.toString();
	   	if (body.indexOf("&") == 0) {
	   		body = body.substring(1, body.length());
	   	}
	   	
		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(HostType.API, appKey, appSecret, "/user/blacklist/add.json", "application/x-www-form-urlencoded");
		HttpUtil.setBodyParameter(body, conn);
	    
	    return (CodeSuccessResult) GsonUtil.fromJson(HttpUtil.returnResult(conn), CodeSuccessResult.class);
	}
	
	/**
	 * 获取某用户的黑名单列表方法（每秒钟限 100 次） 
	 * 
	 * @param  userId:用户 Id。（必传）
	 *
	 * @return QueryBlacklistUserResult
	 **/
	public QueryBlacklistUserResult queryBlacklist(String userId) throws Exception {
		if (userId == null) {
			throw new IllegalArgumentException("Paramer 'userId' is required");
		}
		
	    StringBuilder sb = new StringBuilder();
	    sb.append("&userId=").append(URLEncoder.encode(userId.toString(), UTF8));
		String body = sb.toString();
	   	if (body.indexOf("&") == 0) {
	   		body = body.substring(1, body.length());
	   	}
	   	
		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(HostType.API, appKey, appSecret, "/user/blacklist/query.json", "application/x-www-form-urlencoded");
		HttpUtil.setBodyParameter(body, conn);
	    
	    return (QueryBlacklistUserResult) GsonUtil.fromJson(HttpUtil.returnResult(conn), QueryBlacklistUserResult.class);
	}
	
	/**
	 * 从黑名单中移除用户方法（每秒钟限 100 次） 
	 * 
	 * @param  userId:用户 Id。（必传）
	 * @param  blackUserId:被移除的用户Id。（必传）
	 *
	 * @return CodeSuccessResult
	 **/
	public CodeSuccessResult removeBlacklist(String userId, String blackUserId) throws Exception {
		if (userId == null) {
			throw new IllegalArgumentException("Paramer 'userId' is required");
		}
		
		if (blackUserId == null) {
			throw new IllegalArgumentException("Paramer 'blackUserId' is required");
		}
		
	    StringBuilder sb = new StringBuilder();
	    sb.append("&userId=").append(URLEncoder.encode(userId.toString(), UTF8));
	    sb.append("&blackUserId=").append(URLEncoder.encode(blackUserId.toString(), UTF8));
		String body = sb.toString();
	   	if (body.indexOf("&") == 0) {
	   		body = body.substring(1, body.length());
	   	}
	   	
		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(HostType.API, appKey, appSecret, "/user/blacklist/remove.json", "application/x-www-form-urlencoded");
		HttpUtil.setBodyParameter(body, conn);
	    
	    return (CodeSuccessResult) GsonUtil.fromJson(HttpUtil.returnResult(conn), CodeSuccessResult.class);
	}

	public static void main(String[] args) {
		User u = new User();
		try {
			System.out.println(u.getToken("user6","测试","http://www.rongcloud.cn/images/logo.png"));//dq0CQPDt0V2rlpLoT9PK4xZh+ts6C20ikNG5iOFyyK40nMQM2IlCaDcypsRtHpEzlj5zoG+sN+mjcO+nIP9ySw==
//			System.out.println(u.getToken("test02","测试用户2","http://www.rongcloud.cn/images/logo.png"));//nZc58/8IHdaqj0oVpJ97ft7+WtsNIwZY4vddmjXVTejzhy76w0fpIDOGu15XY/Pv8SogsHfH8WSmt9AAQZ4L9w==
//			System.out.println(u.getToken("admin","管理员","http://www.rongcloud.cn/images/logo.png"));//Z4FQT4R52uUi+2+RhBKX69OtkZXW2UD8UGPslFcbY2CMA8+z5+ILAnm/DguLGKyAwmuRnUDSbD2ol68cp7Xa1Q==
//			System.out.println(u.refresh("test01","修改测试名字",""));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 发送系统消息方法（一个用户向一个或多个用户发送系统消息，单条消息最大 128k，会话类型为 SYSTEM。每秒钟最多发送 100 条消息，每次最多同时向 100 人发送，如：一次发送 100 人时，示为 100 条消息。） 
	 * 
	 * @param  fromUserId:发送人用户 Id。（必传）
	 * @param  toUserId:接收用户 Id，提供多个本参数可以实现向多人发送消息，上限为 1000 人。（必传）
	 * @param  txtMessage:发送消息内容（必传）
	 * @param  pushContent:如果为自定义消息，定义显示的 Push 内容，内容中定义标识通过 values 中设置的标识位内容进行替换.如消息类型为自定义不需要 Push 通知，则对应数组传空值即可。（可选）
	 * @param  pushData:针对 iOS 平台为 Push 通知时附加到 payload 中，Android 客户端收到推送消息时对应字段名为 pushData。如不需要 Push 功能对应数组传空值即可。（可选）
	 * @param  isPersisted:当前版本有新的自定义消息，而老版本没有该自定义消息时，老版本客户端收到消息后是否进行存储，0 表示为不存储、 1 表示为存储，默认为 1 存储消息。（可选）
	 * @param  isCounted:当前版本有新的自定义消息，而老版本没有该自定义消息时，老版本客户端收到消息后是否进行未读消息计数，0 表示为不计数、 1 表示为计数，默认为 1 计数，未读消息数增加 1。（可选）
	 *
	 * @return CodeSuccessResult
	 **/
	public CodeSuccessResult PublishSystem(String fromUserId, String[] toUserId, BaseMessage message, String pushContent, String pushData, Integer isPersisted, Integer isCounted) throws Exception {
		if (fromUserId == null) {
			throw new IllegalArgumentException("Paramer 'fromUserId' is required");
		}
		
		if (toUserId == null) {
			throw new IllegalArgumentException("Paramer 'toUserId' is required");
		}
		
		if (message == null) {
			throw new IllegalArgumentException("Paramer 'message' is required");
		}
		
	    StringBuilder sb = new StringBuilder();
	    sb.append("&fromUserId=").append(URLEncoder.encode(fromUserId.toString(), UTF8));
	    
	    for (int i = 0 ; i< toUserId.length; i++) {
			String child  = toUserId[i];
			sb.append("&toUserId=").append(URLEncoder.encode(child, UTF8));
		}
		
	    sb.append("&objectName=").append(URLEncoder.encode(message.getType(), UTF8));
   	    sb.append("&content=").append(URLEncoder.encode(message.toString(), UTF8));
	    
	    if (pushContent != null) {
	    	sb.append("&pushContent=").append(URLEncoder.encode(pushContent.toString(), UTF8));
	    }
	    
	    if (pushData != null) {
	    	sb.append("&pushData=").append(URLEncoder.encode(pushData.toString(), UTF8));
	    }
	    
	    if (isPersisted != null) {
	    	sb.append("&isPersisted=").append(URLEncoder.encode(isPersisted.toString(), UTF8));
	    }
	    
	    if (isCounted != null) {
	    	sb.append("&isCounted=").append(URLEncoder.encode(isCounted.toString(), UTF8));
	    }
		String body = sb.toString();
	   	if (body.indexOf("&") == 0) {
	   		body = body.substring(1, body.length());
	   	}
	   	
		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(HostType.API, appKey, appSecret, "/message/system/publish.json", "application/x-www-form-urlencoded");
		HttpUtil.setBodyParameter(body, conn);
	    
	    return (CodeSuccessResult) GsonUtil.fromJson(HttpUtil.returnResult(conn), CodeSuccessResult.class);
	}
	
}