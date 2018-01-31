package com.yinghai.twentyfour.common.dao;

import com.yinghai.twentyfour.common.model.TfOrderTotal;
import com.yinghai.twentyfour.common.model.TfOrderTotalHelper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface TfOrderTotalMapper {
    int deleteByPrimaryKey(Integer totalId);

    int insert(TfOrderTotal record);

    int insertSelective(TfOrderTotal record);

    TfOrderTotal selectByPrimaryKey(Integer totalId);

    int updateByPrimaryKeySelective(TfOrderTotal record);

    int updateByPrimaryKey(TfOrderTotal record);

    TfOrderTotal findByOrderNo(@Param("orderNo") String orderNo, @Param("userId")Integer userId);

	TfOrderTotalHelper findOrderByUserId(@Param("totalId")Integer totalId, @Param("userId")Integer userId);

	TfOrderTotal findOrderTotalByNO(String orderNo);

	List<TfOrderTotal> findByPayOrderNo(String orderNo);

	List<TfOrderTotal> findByParentId(Integer parentId);
	/**
	 * 根据总订单编号查询总订单详细信息
	 * @param orderTotalNo
	 * @return
	 */
	TfOrderTotalHelper findOrderTotalHelperByNo(String orderTotalNo);

	
}