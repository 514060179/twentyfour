package com.yinghai.twentyfour.common.dao;

import org.apache.ibatis.annotations.Param;

import com.yinghai.twentyfour.common.model.TfCar;
import com.yinghai.twentyfour.common.model.TfCarHelper;
import com.yinghai.twentyfour.common.util.Page;

public interface TfCarMapper {
    int deleteByPrimaryKey(Integer carId);

    int insert(TfCar record);

    int insertSelective(TfCar record);

    TfCar selectByPrimaryKey(Integer carId);

    int updateByPrimaryKeySelective(TfCar record);

    int updateByPrimaryKey(TfCar record);
    
    TfCar selectByUserAndProduct(@Param("userId")Integer userId,@Param("productId")Integer productId);

	Page<TfCarHelper> selectCarRecordByPage(@Param("start")Integer start,@Param("pageSize")Integer pageSize,@Param("userId")Integer userId);
	
	TfCarHelper findCarInfoByCarId(Integer carId);

	//TfCar findCarInfoByCarIdAndUserId(@Param("carId")Integer carId, @Param("userId")Integer userId);

	TfCarHelper findCarInfoBySelective(TfCar c);
}