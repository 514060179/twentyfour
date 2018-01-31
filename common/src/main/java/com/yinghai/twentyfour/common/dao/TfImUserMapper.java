package com.yinghai.twentyfour.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yinghai.twentyfour.common.model.TfImUser;
import com.yinghai.twentyfour.common.model.TfMaster;
import com.yinghai.twentyfour.common.model.TfUser;
import com.yinghai.twentyfour.common.model.TfUserFriend;

public interface TfImUserMapper {
    int deleteByPrimaryKey(Integer imId);

    int insert(TfImUser record);

    int insertSelective(TfImUser record);

    TfImUser selectByPrimaryKey(Integer imId);

    int updateByPrimaryKeySelective(TfImUser record);

    int updateByPrimaryKey(TfImUser record);

	List<TfImUser> selectBySelective(TfImUser user);

	List<TfUserFriend> findUser(@Param("userId")Integer userId, @Param("nick")String nick,@Param("sourceId")Integer sourceId);

	List<TfMaster> findMaster(@Param("masterId")Integer userId, @Param("nick")String nick);
}