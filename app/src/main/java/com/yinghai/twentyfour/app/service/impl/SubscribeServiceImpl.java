package com.yinghai.twentyfour.app.service.impl;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import com.yinghai.twentyfour.app.service.SubscribeService;
import com.yinghai.twentyfour.common.dao.TfMasterMapper;
import com.yinghai.twentyfour.common.dao.TfSubscribeMapper;
import com.yinghai.twentyfour.common.model.TfMaster;
import com.yinghai.twentyfour.common.model.TfSubscribe;
import com.yinghai.twentyfour.common.model.TfSubscribeHelper;
import com.yinghai.twentyfour.common.model.TfSubscribeKey;
import com.yinghai.twentyfour.common.util.Page;
import com.yinghai.twentyfour.common.util.PageHelper;

public class SubscribeServiceImpl implements SubscribeService {
	@Autowired
	private TfSubscribeMapper subscribeMapper;
	@Autowired
	private TfMasterMapper tfMasterMapper;
	@Override
	public TfSubscribe selectByUserAndMaster(Integer userId, Integer masterId) {
		TfSubscribeKey key = new TfSubscribeKey();
		key.setsUserId(userId);
		key.setsMasterId(masterId);
		return subscribeMapper.selectByPrimaryKey(key);
	}
	@Override
	@Transactional
	public int createSubscribe(Integer userId, Integer masterId,TfMaster m) {
		TfSubscribe s = new TfSubscribe();
		s.setsUserId(userId);
		s.setsMasterId(masterId);
		s.setsCreateTime(new Date());
		int i = subscribeMapper.insertSelective(s);
		if(i!=1){
			throw new RuntimeException("新增关注记录出错");
		}
		//更新大师的关注量
		TfMaster m1 = new TfMaster();
		m1.setMasterId(m.getMasterId());
		m1.setmFollows(m.getmFollows()+1);
		int j = tfMasterMapper.updateByPrimaryKeySelective(m1);
		if(j!=1){
			throw new RuntimeException("更新关注量出错");
		}
		return s.getSubscribeId();
	}
	@Override
	@Transactional
	public int deleteSubscribe(Integer userId,Integer masterId,Long follows) {
		TfSubscribeKey key = new TfSubscribeKey();
		key.setsMasterId(masterId);
		key.setsUserId(userId);
		int i = subscribeMapper.deleteByPrimaryKey(key);
		if(i!=1){
			throw new RuntimeException("删除关注记录失败");
		}
		TfMaster m = new TfMaster();
		m.setMasterId(masterId);
		m.setmFollows(follows);
		m.setmUpdateTime(new Date());
		int j = tfMasterMapper.updateByPrimaryKeySelective(m);
		if(j!=1){
			throw new RuntimeException("更新关注量失败");
		}
		return i;
	}
	@Override
	public Page<TfSubscribeHelper> getSubscribesByPage(Integer pageNum, Integer pageSize, Integer userId) {
		PageHelper.startPage(pageNum, pageSize);
		return subscribeMapper.selectByUserId(userId);
	}
	@Override
	public TfSubscribeHelper selectSubscribeById(int subscribeId) {
		return subscribeMapper.selectSubscribeById(subscribeId);
	}
	@Override
	public TfSubscribeHelper selectDetailSubscribe(TfSubscribe s) {
		return subscribeMapper.selectDetailSubscribe(s);
	}
}
