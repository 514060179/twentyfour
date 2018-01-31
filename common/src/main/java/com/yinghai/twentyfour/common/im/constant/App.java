package com.yinghai.twentyfour.common.im.constant;

import com.yinghai.twentyfour.common.util.PropertyUtil;

/**
 * Created by Administrator on 2017/11/6.
 */
public class App {

    public static final String appKey = PropertyUtil.getAppProperty("appKey");
    public static final String appSecret = PropertyUtil.getAppProperty("appSecret");

    //客户端包括大师端与用户端
    public static final String tag = "app";
    //客户端 用户端标签
    public static final String userTag = "user";
    //大师端标签
    public static final String masterTag = "master";
    
    //系统im账号
    public static final String admin = "admin";

}
