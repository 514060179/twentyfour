package com.yinghai.twentyfour.common.service;

import com.yinghai.twentyfour.common.model.TfOrder;
import com.yinghai.twentyfour.common.model.TfOrderAttach;
import com.yinghai.twentyfour.common.model.TfOrderTotal;
import com.yinghai.twentyfour.common.model.TfOrderTotalHelper;
import com.yinghai.twentyfour.common.util.Page;
import com.yinghai.twentyfour.common.vo.MasterSchedule;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/24.
 */
public interface TfOrderService {

    /**
     *  用户下单（预约占卜订单）
     * @param tfOrder
     * @param tfOrderAttach
     * @return
     */
    TfOrder createOrder(TfOrder tfOrder, TfOrderAttach tfOrderAttach);

    /**
     * 分页查询
     * @param pageNumber
     * @param pageSize
     * @param tfOrder
     * @return
     */
    List<TfOrder> findByPage(Integer pageNumber, Integer pageSize,TfOrder tfOrder);
    /**
     * 
     * @param pageNumber
     * @param pageSize
     * @param tfOrder
     * @return
     */
    Page<TfOrder> findBackByPage(Integer pageNumber, Integer pageSize,TfOrder tfOrder);
    /**
     * 根据订单号查询
     * @param orderNo
     * @return
     */
    TfOrder findByOrderNo(@Param("orderNo") String orderNo, @Param("userId")Integer userId);

    /**
     * 回调更新订单状态 (在线占卜订单)
     * @param orderNo
     * @param payWay
     * @return
     */
    int callbackUpdateStatus(String orderNo,Integer payWay);

    /**
     *  回调更新订单状态 (商品订单)
     * @param orderNo
     * @return
     */
    int callbackUpdate(String orderNo,Integer payWay);
    /**
     * 根据大师id以及订单id查询
     * @param orderId
     * @param masterId
     * @return
     */
    TfOrder findById(Integer orderId,Integer masterId);

    /**
     * 根据用户id以及订单id查询
     * @param orderId
     * @param userId
     * @return
     */
    TfOrder selectById(Integer orderId,Integer userId);
    /**
     * 大师确定订单
     * @param orderId
     * @return
     */
    int masterMakeSure(Integer orderId);

    /**
     * 查询含有大师、大师业务、订单附加信息的订单model
     * @param orderId
     * @return
     */
    TfOrder findAllModelById(Integer orderId);
    /**
     * 根据订单类型和订单ID查询订单详细信息model
     */
    TfOrder findAllModelByOrderId(Integer orderId,Integer isProduct);

    /**
     * 查询大师每天订单数量
     * @param masterId 大师id
     * @param status 订单状态
     * @param msDate 日期
     * @return
     */
    List<MasterSchedule> findMasterSchedule(Integer masterId, Integer status, Date msDate);

    /**
     * 取消订单，需要退款
     * @param order
     * @return
     */
    int cancelOrder(TfOrder order);

    /**
     * 取消订单,不需要退款
     * @param order 订单
     * @return
     */
    int cancelUpdateOrder(TfOrder order);
    /**
     * 根据大师和起始日期查询从起始日期到上月期间每个月的完成订单总额
     * @param masterId
     * @param object
     * @return
     */
	Map<String,Object> findFinishOrderByTime(Integer masterId, Date object);
	
	
	/**
	 * 根据订单Id和用户Id查询订单
	 * @param orderId
	 * @param userId
	 * @return
	 */
	TfOrder findByUserId(Integer orderId, Integer userId);
	/**
	 * 完成非商品订单
	 * @param order
	 * @return
	 */
	int completeOrder(TfOrder order);
	/**
	 * 完成商品订单
	 * @param h
	 * @return
	 */
	int completeProductOrder(TfOrderTotalHelper h);
	/**
	 * 分页查询商品订单
	 * @param parseInt
	 * @param parseInt2
	 * @param searchOrder
	 * @return
	 */
	Page<TfOrderTotalHelper> findProductOrderByPage(Integer pageNumber, Integer pageSize,TfOrder tfOrder);
	
	
	
	int updateOrder(TfOrder tfOrder);
	/**
	 * 回调更新订单状态(商品，更新后——父、子总订单的情况)
	 * @param orderNo
	 * @param paywaywechat
	 * @return
	 */
	int callbackUpdateOrder(TfOrderTotal totalOrder, Integer payWay);
	/**
	 * 更新分页查询商品订单
	 * @param i
	 * @param parseInt
	 * @param searchOrder
	 * @return
	 */
	List<TfOrderTotalHelper> selectProductOrderByPage(Integer  start, Integer pageSize, TfOrder tfOrder);
	/**
	 * 取消商品子总订单，不需要退款
	 * @param tfOrderTotal
	 * @return
	 */
	int cancelUpdateProductTotalOrder(TfOrderTotal tfOrderTotal);
	/**
	 * 取消商品子总订单，需要退款
	 * @param tfOrderTotal
	 * @return
	 */
	int cancelProductOrder(TfOrderTotalHelper tfOrderTotal);
	/**
	 * 根据产品Id和其他条件查询订单
	 * @param o
	 * @param cId
	 * @return
	 */
	List<TfOrder> findOrderByProductId(TfOrder o, Integer cId);

	//TfOrder findOrderByOrderNo(String orderNo);
	/**
	 * 查询应该需要自动取消未支付的订单
	 * 
	 */
	List<TfOrder> autoCancleOrder(Date time1, Date time2);
	
	int updateUnpaidTotalOrder(String time1);
}
