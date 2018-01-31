package com.yinghai.twentyfour.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yinghai.twentyfour.common.model.TfRelation;
import com.yinghai.twentyfour.common.model.TfUser;
import com.yinghai.twentyfour.common.model.TfUserFriend;

/**
 * 好友关系表
 * @author Administrator
 *
 */
public interface TfRelationMapper {
	int deleteByPrimaryKey(Integer relationId);

    int insert(TfRelation record);

    int insertSelective(TfRelation record);

    TfRelation selectByPrimaryKey(Integer relationId);

    int updateByPrimaryKeySelective(TfRelation record);

    int updateByPrimaryKey(TfRelation record);
    
    List<TfRelation> selectByUserAndFriend(@Param("userId")String userId,@Param("friendId")String friendId);

	List<TfUserFriend> findFriendList(@Param("userId")String userId,@Param("nick")String nick);

}
