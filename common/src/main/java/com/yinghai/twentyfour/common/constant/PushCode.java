package com.yinghai.twentyfour.common.constant;

import com.yinghai.twentyfour.common.util.JsonDateValueProcessor;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import java.util.Date;

/**
 * Created by Administrator on 2017/11/13.
 */
public class PushCode {

    //用户支付成功推送code
    public static final String UOrderHavePaid = "2001";
    public static final String UOrderHavePaidMsg = "您有张订单已支付成功！";
    //用户取消订单code
    public static final String UOrderCancelByUser = "2002";
    public static final String UOrderCancelByUserMsg = "您有张订单已取消成功！";
    //大师取消订单code
    public static final String UOrderCancelByMaster = "2003";
    public static final String UOrderCancelByMasterMsg = "您有张订单已被大师取消！";
    //订单即将开始
    public static final String UOrderBegin = "2004";
    public static final String UOrderBeginMsg = "您有张订单积即将开始！";
    //用户收到好友验证
    public static final String UFriendApply = "2005";
    public static final String UFriendApplyMsg = "您收到一个添加好友申请！";
    //用户收到同意好友信息
    public static final String UFriendAgree = "2006";
    public static final String UFriendAgreeMsg = "您收到一条好友同意信息！";
    //用户收到拒绝好友信息
    public static final String UFriendRefuse = "2007";
    public static final String UFriendRefuseMsg = "您收到一条好友拒绝信息！";
    //用户完成订单
    public static final String UOrderComplete = "2008";
    public static final String UOrderCompleteMsg = "您有张订单成功完成";
    //大师为确定退款信息
    public static final String UOrderNoConfirm = "2009";
    public static final String UOrderNoConfirmMsg = "您有张订单大师未确定，已成功退款";
    


    //大师收到订单推送code
    public static final String MOrderHavePaid = "3001";
    public static final String MOrderHavePaidMsg = "您收到一张预约订单，请查看！";
    //大师收到用户取消订单推送code
    public static final String MOrderCancelByUser = "3002";
    public static final String MOrderCancelByUserMsg = "您的一张预约订单已被用户取消！";
    //大师收到大师取消订单推送code
    public static final String MOrderCancelByMaster = "3003";
    public static final String MOrderCancelByMasterMsg = "您的一张订单已被您取消！";
    //大师收到订单完成推送code
    public static final String MOrderCompleteByUser = "3004";
    public static final String MOrderCompleteByUserMsg = "您的一张订单成功完成！";
    //大师收到订单完成推送code
    public static final String MOrderNoConfirm = "3005";
    public static final String MOrderNoConfirmMsg = "您的一张订单待确定时间超时，系统已自动取消！";
    
    //商品订单已发货
    public static final String UOrderDeliver = "4001";
    public static final String UOrderDeliverMsg = "您的一张订单商家已发货！";






}
