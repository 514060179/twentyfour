package com.yinghai.twentyfour.app.service;

import java.util.List;

import com.yinghai.twentyfour.common.model.TfMaster;
import com.yinghai.twentyfour.common.model.TfUser;
import com.yinghai.twentyfour.common.model.TfUserFriend;

/**
 * 好友关系业务层
 * @author Administrator
 *
 */
public interface TfFriendService {
	/**
	 * 添加好友
	 * @param userId	用户Id
	 * @param friendId	目标用户Id
	 * @param type		好友关系类型，现在只能为1(用户之间添加好友)
	 * @param m			添加好友的理由
	 * @return	返回添加好友的userId
	 */
	int addFriend(Integer userId,Integer friendId,Integer type,String m);
	/**
	 * 删除好友或拉黑好友
	 * @param userId	用户Id
	 * @param friendId	目标用户Id
	 * @param type		好友关系类型，现在只能为1(用户之间添加好友)
	 * @param t			1为删除好友，2为拉黑好友
	 * @return	返回删除数据的条数
	 */
	int deleteFriend(Integer userId,Integer friendId,Integer type,Integer t);
	/**
	 * 查询好友列表
	 * @param userId
	 * @param type	好友关系类型，现在只能为1(用户之间添加好友)
	 * @return
	 */
	List<TfUserFriend> getFriendList(Integer pageNum,Integer pageSize,Integer userId,Integer type);
	/**
	 * 根据昵称查询好友
	 * @param userId
	 * @param type
	 * @param nick
	 * @return
	 */
	List<TfUserFriend> getFriend(Integer userId,Integer type,String nick);
	/**
	 * 根据昵称或IM的id查询用户
	 * @param userId
	 * @param nick
	 * @return
	 */
	List<TfUserFriend> getUser(Integer userId, String nick,Integer sourceId);
	/**
	 * 根据昵称或IM的id查询大师
	 * @param userId
	 * @param nick
	 * @return
	 */
	List<TfMaster> getMaster(Integer userId, String nick);
	/**
	 * 同意或拒绝好友申请
	 * @param uId	用户id(当前用户)
	 * @param fId	目标用户id(发起申请的用户)
	 * @param type	用户类型，现在只适合用户之间的好友关系
	 * @param t		1为同意，2为拒绝
	 * @return
	 */
	int dealFriend(Integer uId, Integer fId, Integer type, Integer t);
	/**
	 * 判断是否是好友关系
	 * @param uId
	 * @param fId
	 * @param type
	 * @return
	 */
	int isFriend(Integer uId, Integer fId, Integer type);
	
}
