package com.yinghai.twentyfour.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.yinghai.twentyfour.app.service.TfCarService;
import com.yinghai.twentyfour.common.dao.TfCarMapper;
import com.yinghai.twentyfour.common.model.TfCar;
import com.yinghai.twentyfour.common.model.TfCarHelper;
import com.yinghai.twentyfour.common.util.Page;
import com.yinghai.twentyfour.common.util.PageHelper;

public class TfCarServiceImpl implements TfCarService {
	@Autowired
	private TfCarMapper carMapper;
	@Override
	public int createCarRecord(TfCar c) {
		return carMapper.insertSelective(c);
	}
	@Override
	public TfCar findCarByUserAndProduct(Integer userId, Integer productId) {
		return carMapper.selectByUserAndProduct(userId, productId);
	}
	@Override
	public int updateCarRecord(TfCar car) {
		return carMapper.updateByPrimaryKeySelective(car);
	}
	@Override
	public TfCar findCarRecordByCarId(Integer carId) {
		return carMapper.selectByPrimaryKey(carId);
	}
	@Override
	public int deleteCarRecordByCarId(Integer carId) {
		return carMapper.deleteByPrimaryKey(carId);
	}
	@Override
	public Page<TfCarHelper> getCarRecordByPage(Integer pageNum, Integer pageSize, Integer userId) {
		Integer start = (pageNum-1)*pageSize;
		return carMapper.selectCarRecordByPage(start,pageSize,userId);
	}
	@Override
	public TfCarHelper findCarInfoByCarId(Integer carId) {
		return carMapper.findCarInfoByCarId(carId);
	}
	/*@Override
	public TfCar findCarRecordByCarIdAndUserId(Integer carId, Integer userId) {
		return carMapper.findCarInfoByCarIdAndUserId(carId,userId);
	}*/
	@Override
	public TfCarHelper findCarInfoBySelective(TfCar c) {
		return carMapper.findCarInfoBySelective(c);
	}

}
