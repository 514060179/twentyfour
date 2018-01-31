package com.yinghai.twentyfour.common.service;

import java.util.List;

import com.yinghai.twentyfour.common.model.TfOrderTotalHelper;

/**
 * 总订单数据操作
 * @author Administrator
 *
 */
public interface TfOrderTotalService {

	/**
	 *  根据总订单号查询总订单详情
	 * @param orderTotalNo
	 * @return
	 */
	TfOrderTotalHelper findOrderTotalByNo(String orderTotalNo);
}
