package com.yinghai.twentyfour.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.yinghai.twentyfour.app.service.HistoryService;
import com.yinghai.twentyfour.common.dao.TfHistoryMapper;
import com.yinghai.twentyfour.common.model.TfHistory;
import com.yinghai.twentyfour.common.model.TfHistoryHelper;
import com.yinghai.twentyfour.common.util.Page;
import com.yinghai.twentyfour.common.util.PageHelper;

public class HistoryServiceImpl implements HistoryService {
	@Autowired
	private TfHistoryMapper historyMapper;
	@Override
	public Page<TfHistoryHelper> getHistorysByPage(Integer userId,Integer start,Integer end) {
		//PageHelper.startPage(pageNum, pageSize);
		return historyMapper.selectByUserId(userId,start,end);
	}
	@Override
	public List<TfHistory> findHistoryByUserAndType(Integer userId, Integer type, Integer id) {
		return historyMapper.selectByUserAndTypeId(userId,type,id);
	}
	@Override
	public int addHistory(TfHistory h) {
		return historyMapper.insertSelective(h);
	}
	@Override
	public int deleteHistoryById(Integer historyId) {
		return historyMapper.deleteByPrimaryKey(historyId);
	}
	@Override
	public int updateHistory(TfHistory h) {
		return historyMapper.updateByPrimaryKeySelective(h);
	}

}
