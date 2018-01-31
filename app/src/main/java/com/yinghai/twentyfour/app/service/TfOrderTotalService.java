package com.yinghai.twentyfour.app.service;

import java.util.List;
import java.util.Map;

import com.yinghai.twentyfour.app.model.TotalOrderParamEntity;
import com.yinghai.twentyfour.common.model.TfOrderTotal;
import com.yinghai.twentyfour.common.model.TfOrderTotalHelper;

/**
 * 生成购物车总订单——业务层接口
 * @author Administrator
 *
 */
public interface TfOrderTotalService {


	public TfOrderTotal generateOrder(Integer userId,Integer addressId,Integer sum,Integer qty,List<TotalOrderParamEntity> list);

	/**
	 * 根据订单号查询
	 * @param orderNo
	 * @return
	 */
	TfOrderTotal findByOrderNo(String orderNo,Integer userId);
	/**
	 * 根据用户Id和总订单Id查询子总订单信息
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public TfOrderTotalHelper findByUserId(Integer totalId, Integer userId);
	/**
	 * 按商家分类，分别生成总订单，最后生成支付订单
	 * @param userId		用户ID
	 * @param addressId		地址ID
	 * @param sum		按大师分类对应的总价
	 * @param qty		按大师分类对应的总数量
	 * @param sums		所有子订单的总价
	 * @param qtys		所有子订单的总数量
	 * @param map
	 * @return
	 */
	public TfOrderTotal createOrder(Integer userId, Integer addressId, Map<Integer, Integer> sum,
			Map<Integer, Integer> qty,Integer sums,Integer qtys, Map<Integer, List<TotalOrderParamEntity>> map);
	/**
	 * 根据支付订单号查询到对应的总订单记录(可能是父总订单，也有可能是子总订单)
	 * 父总订单是指包括多个大师子总订单的订单
	 * 子总订单是指一个大师对应商品(可能一个，也可能多个)的总订单
	 * @param orderNo	支付订单号
	 * @param parseInt	用户ID
	 * @return
	 */
	public List<TfOrderTotal> findByPayOrderNo(String orderNo);
	/**
	 * 大师查询总订单
	 * @param masterId
	 * @param orderNo
	 * @return
	 */
	public TfOrderTotalHelper findOrderByMaster(Integer masterId, String orderNo);
	/**
	 * 根据大师和订单Id查询总订单
	 * @param totalId
	 * @param masterId
	 * @return
	 */
	public TfOrderTotalHelper findByMasterId(Integer totalId, Integer masterId);
	/**
	 * 更新总订单
	 * @param orderTotal
	 * @return
	 */
	public int updateOrderTotal(TfOrderTotal orderTotal);
}
