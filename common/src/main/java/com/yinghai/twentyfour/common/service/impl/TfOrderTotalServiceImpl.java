package com.yinghai.twentyfour.common.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.yinghai.twentyfour.common.dao.TfOrderTotalMapper;
import com.yinghai.twentyfour.common.model.TfOrderTotalHelper;
import com.yinghai.twentyfour.common.service.TfOrderTotalService;

public class TfOrderTotalServiceImpl implements TfOrderTotalService {
	@Autowired
	private TfOrderTotalMapper tfOrderTotalMapper;

	@Override
	public TfOrderTotalHelper findOrderTotalByNo(String orderTotalNo) {
		return tfOrderTotalMapper.findOrderTotalHelperByNo(orderTotalNo);
	}

}
