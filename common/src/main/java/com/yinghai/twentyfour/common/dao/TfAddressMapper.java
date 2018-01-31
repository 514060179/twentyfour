package com.yinghai.twentyfour.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yinghai.twentyfour.common.model.TfAddress;
import com.yinghai.twentyfour.common.util.Page;

public interface TfAddressMapper {
    int deleteByPrimaryKey(Integer addressId);

    int insert(TfAddress record);

    int insertSelective(TfAddress record);

    TfAddress selectByPrimaryKey(Integer addressId);
    
    TfAddress selectByAddressAndUser(@Param("addressId")Integer addressId,@Param("userId")Integer userId);

    int updateByPrimaryKeySelective(TfAddress record);

    int updateByPrimaryKey(TfAddress record);
    
    List<TfAddress> selectByUserId(Integer userId);
    
    List<TfAddress> selectOtherByUserId(@Param("userId")Integer userId,@Param("addressId")Integer addressId);
    
    List<TfAddress> selectByUserIdAndDefault(Integer userId);

	Page<TfAddress> selectAddressByPage(Integer userId);

	TfAddress findAddressByUserId(@Param("userId")Integer userId, @Param("addressId")Integer addressId);
}