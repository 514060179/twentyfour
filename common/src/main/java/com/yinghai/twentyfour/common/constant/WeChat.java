package com.yinghai.twentyfour.common.constant;

import com.yinghai.twentyfour.common.util.PropertyUtil;

/**
 * Created by Administrator on 2017/10/24.
 */
public class WeChat {

    /**
     * 微信支付的url
     */
    public static final String wechat_pay_url="https://api.mch.weixin.qq.com/pay/unifiedorder";

    /**
     * 微信訂單查詢的URL
     */
    public static final String wechat_QUERY_url = "https://api.mch.weixin.qq.com/pay/orderquery";

    /**
     * 查询退款的URL
     */
    public static final String wechat_refundquery_url = "https://api.mch.weixin.qq.com/pay/refundquery";

    /**
     * 关闭订单的URL
     */
    public static final String wechat_CLOSE_url = "https://api.mch.weixin.qq.com/pay/closeorder";

    /**
     * 申请退款的URL
     */
    public static final String wechat_refund_url = "https://api.mch.weixin.qq.com/secapi/pay/refund";




    /**
     * 微信公众号APPID
     */
    public static final String JSAPIAppId = PropertyUtil.getAppProperty("JSAPIAppId");
    /**
     * 公众号的APPSECRET
     */
    public static final String JSAPIAPPSECRET = PropertyUtil.getAppProperty("JSAPIAPPSECRET");
    /**
     * 微信公众号商店key
     */
    public static final String JSAPIkey = PropertyUtil.getAppProperty("JSAPIkey");
    /**
     * 微信支付商户号，微信支付分配的商户号（公众号）
     */
    public static final String JSAPImchId = PropertyUtil.getAppProperty("JSAPImch_id");
    /**
     * 微信支付类型JSAP
     */
    public static final String weixinJSAPPayType = "JSAPI";




    /**
     * 微信公眾號退款证书
     */
    public static final String apiclientCertJsapi = PropertyUtil.getAppProperty("apiclient_certjsapi");
    /**
     * 微信支付APPId(应用ID)APP端
     */
    public static final String weixinPayAppId = PropertyUtil.getAppProperty("weixinPayAppId");
    /**
     * 微信支付商户号，微信支付分配的商户号APP
     */
    public static final String mchId = PropertyUtil.getAppProperty("mch_id");
    /**
     * 微信APP商店key
     */
    public static final String weixinPayKey = PropertyUtil.getAppProperty("weixinPayKey");
    /**
     * 微信支付类型APP
     */
    public static final String weixinAPPPayType = "APP";
    /**
     * 微信回调url
     */
    public static final String WEBCHAT_PAY_CALL_BACK = PropertyUtil.getAppProperty("weixinCallBackUrl");
    /**
     * 微信APP退款
     */
    public static final String apiclientCert = PropertyUtil.getAppProperty("apiclient_cert");

    /**
     * 大师微信登录
     */
    public static final String masterappid = PropertyUtil.getAppProperty("masterappid");
    /**
     * 大师微信登录
     */
    public static final String masterappsecret = PropertyUtil.getAppProperty("masterappsecret");
    
    /**
     * 用户微信登录
     */
    public static final String userappid = PropertyUtil.getAppProperty("userappid");
    /**
     * 用户微信登录
     */
    public static final String userappsecret = PropertyUtil.getAppProperty("userappsecret");
}
