package com.yinghai.twentyfour.common.dao;

import org.apache.ibatis.annotations.Param;

import com.yinghai.twentyfour.common.model.TfSubscribe;
import com.yinghai.twentyfour.common.model.TfSubscribeHelper;
import com.yinghai.twentyfour.common.model.TfSubscribeKey;
import com.yinghai.twentyfour.common.util.Page;

public interface TfSubscribeMapper {
    int deleteByPrimaryKey(TfSubscribeKey key);

    int insert(TfSubscribe record);

    int insertSelective(TfSubscribe record);

    TfSubscribe selectByPrimaryKey(TfSubscribeKey key);

    int updateByPrimaryKeySelective(TfSubscribe record);

    int updateByPrimaryKey(TfSubscribe record);

	Page<TfSubscribeHelper> selectByUserId(Integer userId);

	TfSubscribeHelper selectSubscribeById(int subscribeId);

	TfSubscribeHelper selectDetailSubscribe(TfSubscribe s);
	
}