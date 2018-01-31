package com.yinghai.twentyfour.common.constant;

public class ConstantCode {
	/**
	 * 更新失败
	 */
	public static final String UPDATE_FAIL ="109";
	/**
	 * 新增失败
	 */
	public static final String ADD_FAIL ="108";
	
	
	/**
	 * 操作成功
	 */
	public static final String SUCCESS ="1";
	/**
	 * 操作失败
	 */
	public static final String FAIL = "120";
	/**
	 * 被禁用拉黑
	 */
	public static final String DELETE = "121";
	
	/**
	 * 验证码不存在
	 */
	public static final String VERIFY_NOEXIST ="1052";
	
	/**
	 * 验证码已作废
	 */
	public static final String VERIFY_INVALID ="1053";
	
	/**
	 * 验证码已超时
	 */
	public static final String VERIFY_TIMEOUT ="1054";
	/**
	 * 参数为空
	 */
	public static final String DATA_IS_EMPTY ="101";
	
	/**
	 * 数据不存在
	 */
	public static final String DATA_NO_EXIST ="102";
	/**
	 * 数据已存在
	 */
	public static final String DATA_EXIST = "1020";
	/**
	 * 已收藏
	 */
	public static final String HAVA_COLLECTION = "1021";
	/**
	 * 已取消收藏
	 */
	public static final String CANCEL_COLLECTION = "1023";
	/**
	 * 已关注
	 */
	public static final String HAVA_SUBSCRIBE = "1022";
	/**
	 * 已取消关注
	 */
	public static final String CANCEL_SUBSCRIBE = "1024";
	
	/**
	 * 密码不正确
	 */
	public static final String PASSWORD_ERROR = "103";
	/**
	 * 数据格式错误
	 */
	public static final String DATA_FORMAT_ERROR ="104";
	/**
	 * 调用第三方接口失败
	 */
	public static final String NOT_ME_ERROR ="-111";
	/**
	 * 数据无效、超时
	 */
	public static final String DATA_FAILURE = "105";
	
	/**
	 * 数据库操作失败
	 */
	public static final String OPERATION_FAILURE = "106";
	/**
	 * 验证码错误
	 */
	public static final String VERIFY_ERROR = "110";
	/**
	 * 优惠券已领取过1121
	 */
	public static final String COUPON_EXIT = "1121";
	/**
	 * 优惠券不存在 1122
	 */
	public static final String COUPON_NO_EXIT = "1122";
	
	/**
	 * 优惠券已过期1123
	 */
	public static final String COUPON_OVERDUE = "1123";
	/**
	 * 幸运星座错误，格式或范围有误
	 */
	public static final String CONSTELLATION_ERROR = "113";
	/**
	 * 类型错误：收藏类型、购物车修改类型
	 */
	public static final String TYPE_ERROR = "114";
	/**
	 * 商品数量错误或格式错误
	 */
	public static final String QUANTITY_ERROR = "115";
	/**
	 * 商品已下架或已被删除
	 */
	public static final String PRODUCT_OFFLINE_OR_DELETE ="116";
	/**
	 * 统一支付时，状态不为未支付状态
	 */
	public static final String ORDER_501_CODE = "501";

	/**
	 * 订单金额被篡改
	 */
	public static final String ORDER_502_CODE = "502";

	/**
	 * 微信统一下单出错
	 */
	public static final String ORDER_503_CODE = "503";

	/**
	 * 未支付订单大师无法确定
	 */
	public static final String ORDER_504_CODE = "504";

	/**
	 * 订单已经确定过无法再次确定
	 */
	public static final String ORDER_505_CODE = "505";

	/**
	 * 支付或者取消的订单无法再次支付
	 */
	public static final String ORDER_506_CODE = "506";

	/**
	 * 支付或者取消的订单无法再次支付
	 */
	public static final String ORDER_507_CODE = "507";

	/**
	 * 不是已支付、未支付、已确定订单无法取消
	 */
	public static final String ORDER_508_CODE = "508";
	/**
	 * 不支持商品总订单退款，只能一个个退款
	 */
	public static final String ORDER_5080_CODE = "5080";
	/**
	 * 订单状态已更新
	 */
	public static final String ORDER_509_CODE = "509";
	/**
	 * 订单状态不对
	 */
	public static final String ORDER_510_CODE = "510";
	
	/**
	 * 单点登录
	 */
	public static final String LOGIN_111_CODE = "111";


	/**
	 * 提现金额不能小于等于0
	 */
	public static final String WALLET_601_CODE = "601";

	/**
	 * 提现金额不能大于账户余额！
	 */
	public static final String WALLET_602_CODE = "602";

	/**
	 * 已有提现正在进行中
	 */
	public static final String WALLET_603_CODE = "603";

	/**
	 * 新增业务价格不能低于0
	 */
	public static final String BUSINESS_701_CODE = "701";
	/**
	 * 系统异常
	 */
	public static final String SYSTEM_999_CODE = "999";
	/**
	 * 游客模式
	 */
	public static final String TOURIST_800_CODE = "800";
	/**
	 * 未购买商品，不能评论
	 */
	public static final String COMMENT_801_CODE = "801";
	/**
	 * 预约时间段不能晚于当前时间
	 */
	public static final String TIME_117_CODE = "117";
	/**
	 * 占卜订单未到预约时间
	 */
	public static final String TIME_118_CODE = "118";
	/**
	 * 占卜订单预约时间已过
	 */
	public static final String TIME_125_CODE = "125";
}
