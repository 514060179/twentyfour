package com.yinghai.twentyfour.common.dao;

import com.yinghai.twentyfour.common.model.TfOrder;
import com.yinghai.twentyfour.common.model.TfOrderTotalHelper;
import com.yinghai.twentyfour.common.util.Page;
import com.yinghai.twentyfour.common.vo.MasterAndUserIm;
import com.yinghai.twentyfour.common.vo.MasterSchedule;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TfOrderMapper {
    int deleteByPrimaryKey(Integer orderId);

    int insert(TfOrder record);

    int insertSelective(TfOrder record);

    TfOrder selectByPrimaryKey(@Param("orderId")Integer orderId,@Param("masterId")Integer masterId,@Param("userId")Integer userId);

    int updateByPrimaryKeySelective(TfOrder record);

    int updateByPrimaryKey(TfOrder record);

    List<TfOrder> findPageByCondition(@Param("start")Integer start,@Param("pageSize")Integer pageSize,@Param("record")TfOrder record);

    TfOrder findByOrderNoAndUser(@Param("orderNo")String orderNo,@Param("userId")Integer userId);

    TfOrder findOrderWithAllModelById(Integer orderId);

    List<MasterSchedule> findMasterSchedule(@Param("masterId")Integer masterId, @Param("status")Integer status, @Param("msDate")Date msDate);

    int updateByConditon(TfOrder record);
    
    int updateByConditonTotal(TfOrder record);
    
    TfOrder findOrderWithAllModelByOrderId(@Param("orderId")Integer orderId,@Param("isProduct")Integer isProduct);

	Map<String, Object> findFinishOrderByTime(@Param("masterId")Integer masterId, @Param("start")Date start);

	List<TfOrder> findUnpaidTotalOrder(@Param("d1")Date d1,@Param("d2")Date d2);

	Page<TfOrderTotalHelper> findProductByCondition(@Param("tfOrder")TfOrder tfOrder,@Param("start")Integer start,@Param("end")Integer end);

    MasterAndUserIm findByOrderNo(String orderNo);

    MasterAndUserIm findByOrderTotalNo(String orderNo);

    MasterAndUserIm getByOrderNo(String orderNo);

    MasterAndUserIm getByOrderTotalNo(String orderNo);

	TfOrder findOrderByOrderNo(String orderNo);

	List<TfOrder> findOrdersByTotalNo(String orderTotalNo);

	List<TfOrderTotalHelper> selectProductOrderByPage(@Param("start")Integer start, @Param("pageSize")Integer pageSize, @Param("tfOrder")TfOrder tfOrder);

	List<TfOrder> findOrderByProductId(@Param("order")TfOrder order, @Param("productId")Integer productId);
	
	int updateUnpaidTotalOrder(String time1);

	Page<TfOrder> findBackPageByCondition(TfOrder tfOrder);
	
}