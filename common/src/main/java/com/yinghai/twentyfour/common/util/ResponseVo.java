package com.yinghai.twentyfour.common.util;

import javax.servlet.http.HttpServletResponse;

import com.yinghai.twentyfour.common.constant.ConstantCode;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

public class ResponseVo {

	public ResponseVo() {}
	/**
	 * 参数为空,如手机号、密码、验证码、区号
	 * @param response
	 * @param msg
	 */
	public static void send101Code(HttpServletResponse response,String msg){
		String code = ConstantCode.DATA_IS_EMPTY;
		common(code,msg,new JSONObject(),response);
	}
	/**
	 * 失败返回
	 * @param response
	 * @param msg
	 */
	public static void send120Code(HttpServletResponse response,String msg){
		String code = ConstantCode.FAIL;
		common(code,msg,new JSONObject(),response);
	}
	/**
	 * 被禁用拉黑
	 * @param response
	 * @param msg
	 */
	public static void send121Code(HttpServletResponse response,String msg){
		String code = ConstantCode.DELETE;
		common(code,msg,new JSONObject(),response);
	}
	/**
	 * 数据不存在，如用户信息
	 * @param response
	 * @param msg
	 */
	public static void send102Code(HttpServletResponse response,String msg){
		String code = ConstantCode.DATA_NO_EXIST;
		common(code,msg,new JSONObject(),response);
	}
	/**
	 * 数据已存在，如账号
	 * @param response
	 * @param msg
	 */
	public static void send1020Code(HttpServletResponse response,String msg){
		String code = ConstantCode.DATA_EXIST;
		common(code, msg, new JSONObject(), response);
	}
	
	
	/**
	 * 数据不正确，密码错误
	 * @param response
	 * @param msg
	 */
	public static void send103Code(HttpServletResponse response,String msg){
		String code = ConstantCode.PASSWORD_ERROR;
		common(code,msg,new JSONObject(),response);
	}
	/**
	 * 验证码错误
	 * @param response
	 * @param msg
	 */
	public static void send110Code(HttpServletResponse response,String msg){
		String code = ConstantCode.VERIFY_ERROR;
		common(code,msg,new JSONObject(),response);
	}
	/**
	 * 该手机已有相关的绑定信息，无法进行该次的绑定操作
	 * @param response
	 * @param msg
	 */
	public static void send1031Code(HttpServletResponse response,String msg){
		String code = "1031";
		common(code,msg,new JSONObject(),response);
	}
	/**
	 * 数据格式错误，如手机号格式错误
	 * @param response
	 * @param msg
	 */
	public static void send104Code(HttpServletResponse response,String msg){
		String code = ConstantCode.DATA_FORMAT_ERROR;
		common(code,msg,new JSONObject(),response);
	}
	
	/**
	 * 调用第三方接口失败
	 * @param response
	 * @param msg
	 */
	public static void sendNotMeErrorCode(HttpServletResponse response,String msg){
		String code = ConstantCode.NOT_ME_ERROR;
		common(code,msg,new JSONObject(),response);
	}
	/**
	 * 成功返回
	 * @param response
	 * @param msg
	 * @param data
	 */
	public static void send1Code(HttpServletResponse response,String msg,JSON data){
		String code = ConstantCode.SUCCESS;
		common(code,msg,data,response);
	}
	/**
	 * 过期、超时，如验证码
	 * @param response
	 * @param msg
	 */
	public static void send105Code(HttpServletResponse response,String msg){
		String code = ConstantCode.DATA_FAILURE;
		common(code, msg, new JSONObject(), response);
	}
	/**
	 * 验证码不存在
	 * @param response
	 * @param msg
	 */
	public static void send1052Code(HttpServletResponse response,String msg){
		String code = "1052";
		common(code,msg,new JSONObject(),response);
	}
	/**
	 * 验证码已作废
	 * @param response
	 * @param msg
	 */
	public static void send1053Code(HttpServletResponse response,String msg){
		String code = "1053";
		common(code,msg,new JSONObject(),response);
	}
	/**
	 * 验证码已超时
	 * @param response
	 * @param msg
	 */
	public static void send1054Code(HttpServletResponse response,String msg){
		String code = "1054";
		common(code,msg,new JSONObject(),response);
	}
	/**
	 * 数据库操作失败，如插入数据、删除数据、更新数据
	 * @param response
	 * @param msg
	 */
	public static void send106Code(HttpServletResponse response,String msg){
		String code = ConstantCode.OPERATION_FAILURE;
		common(code, msg, new JSONObject(), response);
	}
	/**
	 * api 验证失败！
	 * @param response
	 * @param msg
	 */
	public static void send107Code(HttpServletResponse response,String msg){
		String code = "107";
		common(code, msg, new JSONObject(), response);
	}

	/**
	 * 数据库新增数据失败
	 * @param response
	 * @param msg
	 */
	public static void send108Code(HttpServletResponse response,String msg){
		String code = "108";
		common(code, msg, new JSONObject(), response);
	}
	/**
	 * 数据库修改数据失败
	 * @param response
	 * @param msg
	 */
	public static void send109Code(HttpServletResponse response,String msg){
		String code = "109";
		common(code, msg, new JSONObject(), response);
	}
	/**
	 * 返回错误响应
	 * @param response
	 * @param code
	 * @param msg
	 */
	public static void sendResponse(HttpServletResponse response,String code,String msg){
		common(code, msg, new JSONObject(), response);
	}
	
	/**
	 * 封装的响应方法
	 * @param code	响应码
	 * @param msg	响应信息
	 * @param data	响应数据
	 * @param response	响应对象
	 */
	public static void common(String code,String msg,JSON data,HttpServletResponse response){
		JSONObject responseObject = new JSONObject();
		responseObject.put("code", code);
		responseObject.put("msg", msg);
		responseObject.put("data", data);
		ResponseUtils.renderJson(response, responseObject.toString());
	}
	/**
	 * 第三方登录校验失败
	 * @param response
	 * @param msg
	 */
	public static void send119Code(HttpServletResponse response,String msg){
		String code = "119";
		common(code,msg,new JSONObject(),response);
	}
	
	/**
	 * 幸运星座错误
	 * @param response
	 * @param msg
	 */
	public static void send113Code(HttpServletResponse response,String msg){
		String code = ConstantCode.CONSTELLATION_ERROR;
		common(code,msg,new JSONObject(),response);
	}
	/**
	 * 类型错误，如收藏类型、评论类型
	 * @param response
	 * @param msg
	 */
	public static void send114Code(HttpServletResponse response,String msg){
		String code = ConstantCode.TYPE_ERROR;
		common(code,msg,new JSONObject(),response);
	}
	/**
	 * 商品数量错误或格式有误
	 * @param response
	 * @param msg
	 */
	public static void send115Code(HttpServletResponse response,String msg){
		String code = ConstantCode.QUANTITY_ERROR;
		common(code,msg,new JSONObject(),response);
	}
	/**
	 * 商品已被下架或删除
	 * @param response
	 * @param msg
	 */
	public static void send116Code(HttpServletResponse response,String msg){
		String code = ConstantCode.PRODUCT_OFFLINE_OR_DELETE;
		common(code,msg,new JSONObject(),response);
	}
	/**
	 * 预约时间段不能早于当前
	 * @param response
	 * @param msg
	 */
	public static void send117Code(HttpServletResponse response,String msg){
		String code = ConstantCode.TIME_117_CODE;
		common(code,msg,new JSONObject(),response);
	}
	/**
	 * 占卜订单未到预约时间
	 * @param response
	 * @param msg
	 */
	public static void send118Code(HttpServletResponse response,String msg){
		String code = ConstantCode.TIME_118_CODE;
		common(code,msg,new JSONObject(),response);
	}
	/**
	 * 占卜订单预约时间已过
	 * @param response
	 * @param msg
	 */
	public static void send125Code(HttpServletResponse response,String msg){
		String code = ConstantCode.TIME_125_CODE;
		common(code,msg,new JSONObject(),response);
	}
	/**
	 * 用户不在线
	 * @param response
	 * @param msg
	 */
	public static void send126Code(HttpServletResponse response,String msg){
		String code = ConstantCode.IM_OFFLINE;
		common(code,msg,new JSONObject(),response);
	}

	/**
	 * 资源尚未购买使用
	 * @param response
	 * @param msg
	 */
	public static void send401Code(HttpServletResponse response,String msg){
		String code = "401";
		common(code,msg,new JSONObject(),response);
	}
	/**
	 * 资源丢失
	 * @param response
	 * @param msg
	 */
	public static void send404Code(HttpServletResponse response,String msg){
		String code = "404";
		common(code,msg,new JSONObject(),response);
	}

	/**
	 * 统一支付时，状态不为未支付状态
	 * @param response
	 * @param msg
	 */
	public static void send501Code(HttpServletResponse response,String msg){
		String code = ConstantCode.ORDER_501_CODE;
		common(code,msg,new JSONObject(),response);
	}
	/**
	 * 订单金额被篡改
	 * @param response
	 * @param msg
	 */
	public static void send502Code(HttpServletResponse response,String msg){
		String code = ConstantCode.ORDER_502_CODE;
		common(code,msg,new JSONObject(),response);
	}
	/**
	 * 微信统一下单出错
	 * @param response
	 * @param msg
	 */
	public static void send503Code(HttpServletResponse response,String msg){
		String code = ConstantCode.ORDER_503_CODE;
		common(code,msg,new JSONObject(),response);
	}
	/**
	 * 大师订单确定失败（未支付订单无法确定）
	 * @param response
	 * @param msg
	 */
	public static void send504Code(HttpServletResponse response,String msg){
		String code = ConstantCode.ORDER_504_CODE;
		common(code,msg,new JSONObject(),response);
	}
	/**
	 * 确定已经确定（无法再次确定）
	 * @param response
	 * @param msg
	 */
	public static void send505Code(HttpServletResponse response,String msg){
		String code = ConstantCode.ORDER_505_CODE;
		common(code,msg,new JSONObject(),response);
	}

	/**
	 * 支付或者取消的订单无法再次支付
	 * @param response
	 * @param msg
	 */
	public static void send506Code(HttpServletResponse response,String msg){
		String code = ConstantCode.ORDER_506_CODE;
		common(code,msg,new JSONObject(),response);
	}
	/**
	 * 支付宝下单，该订单为在线占卜订单，与传入orderType不相符
	 * @param response
	 * @param msg
	 */
	public static void send507Code(HttpServletResponse response,String msg){
		String code = ConstantCode.ORDER_507_CODE;
		common(code,msg,new JSONObject(),response);
	}
	/**
	 * 不是已支付、未支付、已确定订单无法取消
	 * @param response
	 * @param msg
	 */
	public static void send508Code(HttpServletResponse response,String msg,JSON data){
		String code = ConstantCode.ORDER_508_CODE;
		common(code,msg,data,response);
	}
	/**
	 * 不支持商品的全部退款，商品只支持子订单退款
	 * @param response
	 * @param msg
	 */
	public static void send5080Code(HttpServletResponse response,String msg){
		String code = ConstantCode.ORDER_5080_CODE;
		common(code,msg,new JSONObject(),response);
	}
	/**
	 * 订单状态已更新，已更新到进行中或完成
	 * @param response
	 * @param msg
	 */
	public static void send509Code(HttpServletResponse response,String msg,JSON data){
		String code = ConstantCode.ORDER_509_CODE;
		common(code,msg,data,response);
	}
	
	/**
	 * 
	 * 订单状态不匹配
	 * @param response
	 * @param msg
	 */
	public static void send510Code(HttpServletResponse response,String msg){
		String code = ConstantCode.ORDER_510_CODE;
		common(code,msg,new JSONObject(),response);
	}

	/**
	 * 单点登录
	 * @param response
	 * @param msg
	 */
	public static void send111Code(HttpServletResponse response,String msg){
		String code = ConstantCode.LOGIN_111_CODE;
		common(code,msg,new JSONObject(),response);
	}

	/**
	 * 提现金额不能小于等于0
	 * @param response
	 * @param msg
	 */
	public static void send601Code(HttpServletResponse response,String msg){
		String code = ConstantCode.WALLET_601_CODE;
		common(code,msg,new JSONObject(),response);
	}
	/**
	 * 提现金额不能大于账户余额！
	 * @param response
	 * @param msg
	 */
	public static void send602Code(HttpServletResponse response,String msg){
		String code = ConstantCode.WALLET_602_CODE;
		common(code,msg,new JSONObject(),response);
	}
	/**
	 * 已有提现正在进行中
	 * @param response
	 * @param msg
	 */
	public static void send603Code(HttpServletResponse response,String msg){
		String code = ConstantCode.WALLET_603_CODE;
		common(code,msg,new JSONObject(),response);
	}
	/**
	 * 新增业务价格不能低于0
	 * @param response
	 * @param msg
	 */
	public static void send701Code(HttpServletResponse response,String msg){
		String code = ConstantCode.BUSINESS_701_CODE;
		common(code,msg,new JSONObject(),response);
	}

	/**
	 * 系统异常
	 * @param response
	 * @param msg
	 */
	public static void send999Code(HttpServletResponse response,String msg){
		String code = ConstantCode.SYSTEM_999_CODE;
		common(code,msg,new JSONObject(),response);
	}
	/**
	 * 已经收藏
	 * @param response
	 * @param msg
	 */
	public static void send1021Code(HttpServletResponse response,String msg){
		String code = ConstantCode.HAVA_COLLECTION;
		common(code,msg,new JSONObject(),response);
	}
	/**
	 * 已经关注
	 * @param response
	 * @param msg
	 */
	public static void send1022Code(HttpServletResponse response,String msg){
		String code = ConstantCode.HAVA_SUBSCRIBE;
		common(code,msg,new JSONObject(),response);
	}
	/**
	 * 已取消收藏
	 * @param response
	 * @param msg
	 */
	public static void send1023Code(HttpServletResponse response,String msg){
		String code = ConstantCode.CANCEL_COLLECTION;
		common(code,msg,new JSONObject(),response);
	}
	/**
	 * 已取消关注
	 * @param response
	 * @param msg
	 */
	public static void send1024Code(HttpServletResponse response,String msg){
		String code = ConstantCode.CANCEL_SUBSCRIBE;
		common(code,msg,new JSONObject(),response);
	}
	/**
	 * 游客模式
	 * @param response
	 * @param msg
	 */
	public static void send800Code(HttpServletResponse response,String msg){
		String code = ConstantCode.TOURIST_800_CODE;
		common(code,msg,new JSONObject(),response);
	}
	/**
	 * 未购买商品，无法评论
	 * @param response
	 * @param msg
	 */
	public static void send801Code(HttpServletResponse response,String msg){
		String code = ConstantCode.COMMENT_801_CODE;
		common(code,msg,new JSONObject(),response);
	}
	/**
	 * 已经填写物流信息
	 * @param response
	 * @param string
	 */
	public static void send802Code(HttpServletResponse response, String msg) {
		String code = ConstantCode.COMMENT_802_CODE;
		common(code,msg,new JSONObject(),response);
	}
}
