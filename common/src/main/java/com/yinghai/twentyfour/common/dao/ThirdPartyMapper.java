package com.yinghai.twentyfour.common.dao;

import org.apache.ibatis.annotations.Param;

import com.yinghai.twentyfour.common.model.ThirdParty;

public interface ThirdPartyMapper {

	int deleteByPrimaryKey(Integer id);

	int insertSelective(ThirdParty record);

	ThirdParty selectByPrimaryKey(Integer id);

	ThirdParty selectByOpenId(@Param("openId") String openId,@Param("tfType") Integer tfType);

	ThirdParty selectByTfIdAndType(@Param("tfId") Integer tfId, @Param("type") Integer type, @Param("tfType") Integer tfType);

	int updatePrimaryKey(ThirdParty record);
}