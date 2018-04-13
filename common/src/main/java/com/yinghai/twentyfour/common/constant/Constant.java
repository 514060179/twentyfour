package com.yinghai.twentyfour.common.constant;

import com.yinghai.twentyfour.common.util.PropertyUtil;

/**
 * Created by Administrator on 2017/7/24.
 */
public class Constant {
	/**
	 * 大师头像类型图片
	 */
	public static final int IMAGE_TYPE_MASTER=1;
	/**
	 * 用户头像类型图片
	 */
	public static final int IMAGE_TYPE_USER=2;
	/**
	 * 文章类型图片
	 */
	public static final int IMAGE_TYPE_ARTICLE=3;
	/**
	 * 产品类型图片
	 */
	public static final int IMAGE_TYPE_PRODUCT=4;
	
	/**
	 * 对应用户端登录类型
	 */
	public static final Integer THIRD_PARTY_TFUSER=0;
	/**
	 * 对应大师端登录类型
	 */
	public static final Integer THIRD_PARTY_MASTER=1;
	/**
	 * 微信登录时type的值0
	 */
	public static final String WEIXIN_LOGIN_TYPE="0";
	
	/**
	 * QQ登录时type的值1
	 */
	public static final String QQ_LOGIN_TYPE="1";
	
	/**
	 * Email登录时type的值3
	 */
	public static final String EMAIL_LOGIN_TYPE="3";
	
		/**
	 *云通信 发送云短信请求url
	 */
	public static final String SEND_SMS_MSG_URL = "https://yun.tim.qq.com/v5/tlssmssvr/sendsms";
	/**
	 * 短信验证码有效时间
	 */
	public static final int VERIFY_DURATION_TIME = 10*60*1000;
	
	/**
	 * 所有文件的提交路径,文件绝对路径（存放路径）
	 */
	public static final String filepath = PropertyUtil.getAppProperty("filepath");
	
	/**
	 * 文章的存放图片
	 */
	public static final String articleimage = PropertyUtil.getAppProperty("articleimage");
	
	/**
	 * 大师头像
	 */
	public static final String masterimage = PropertyUtil.getAppProperty("masterimage");
	
	/**
	 * 用户头像
	 */
	public static final String userimage = PropertyUtil.getAppProperty("userimage");
	
	/**
	 * 产品图片
	 */
	public static final String productimage = PropertyUtil.getAppProperty("productimage");
	
	/**
	 * 专辑的资源文件路径
	 */
	public static final String resourceUrl = PropertyUtil.getAppProperty("resourceUrl");
	
	public static final String imageUrl = PropertyUtil.getAppProperty("imageUrl");
	
	public static final String http = PropertyUtil.getAppProperty("http");
	
	/**
	 * 省份地区文件地址
	 */
	public static final String provincePath = PropertyUtil.getAppProperty("provincePath");
	/**
	 * 运势信息xcel模板 
	 */
	public static final String lucktemplate = PropertyUtil.getAppProperty("lucktemplate");
	/**
	 * 用户端apk
	 */
	public static final String PREFIX_USER = PropertyUtil.getAppProperty("userapk");
	/**
	 * 大师端apk
	 */
	public static final String PREFIX_MASTER = PropertyUtil.getAppProperty("masterapk");
	public static String apk = PropertyUtil.getAppProperty("apk");
	/**
	 * 一个时间段允许的预约人数  
	 */
	public static final String ORDERERS = PropertyUtil.getAppProperty("orderers");
}
