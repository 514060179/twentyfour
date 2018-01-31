package com.yinghai.twentyfour.common.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yinghai.twentyfour.common.model.TfUser;
import com.yinghai.twentyfour.common.util.Page;

public interface TfUserMapper {
	
	TfUser findByTel(@Param("countryCode")String countryCode,@Param("tel")String tel);
	
    int deleteByPrimaryKey(Integer userId);

    int insert(TfUser record);

    int insertSelective(TfUser record);

    TfUser selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(TfUser record);

    int updateByPrimaryKey(TfUser record);
    
    Page<TfUser>  getTfUserRecord(TfUser map);

    int findTopId();
}