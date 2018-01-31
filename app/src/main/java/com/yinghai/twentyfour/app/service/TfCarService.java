package com.yinghai.twentyfour.app.service;

import com.yinghai.twentyfour.common.model.TfCar;
import com.yinghai.twentyfour.common.model.TfCarHelper;
import com.yinghai.twentyfour.common.util.Page;

/**
 * 购物车记录业务层接口
 * @author Administrator
 *
 */
public interface TfCarService {
	/**
	 * 创建购物车记录
	 * @param c
	 * @return
	 */
	int createCarRecord(TfCar c);
	/**
	 *  根据用户和商品Id查询购物车记录
	 * @param userId
	 * @param productId
	 * @return
	 */
	TfCar findCarByUserAndProduct(Integer userId,Integer productId);
	/**
	 * 更新购物车记录
	 * @param car
	 * @return
	 */
	int updateCarRecord(TfCar car);
	/**
	 * 根据Id查询购物车记录
	 * @param carId
	 * @return
	 */
	TfCar findCarRecordByCarId(Integer carId);
	/**
	 * 根据购物车Id查询详细购物车记录
	 * @param carId
	 * @return
	 */
	TfCarHelper findCarInfoByCarId(Integer carId);
	/**
	 * 根据条件查询购物车详细记录
	 * @param c
	 * @return
	 */
	TfCarHelper findCarInfoBySelective(TfCar c);
	/**
	 * 删除购物车记录
	 * @param carId
	 * @return
	 */
	int deleteCarRecordByCarId(Integer carId);
	/**
	 * 根据用户id分页查询购物车记录
	 * @param pageNum
	 * @param pageSize
	 * @param userId
	 * @return
	 */
	Page<TfCarHelper> getCarRecordByPage(Integer pageNum, Integer pageSize, Integer userId);
	/**
	 * 根据用户Id和carId查询购物车记录
	 * @param carId
	 * @param userId
	 * @return
	 */
	//TfCar findCarRecordByCarIdAndUserId(Integer carId, Integer userId);
}
