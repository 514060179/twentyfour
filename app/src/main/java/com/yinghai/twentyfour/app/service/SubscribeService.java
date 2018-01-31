package com.yinghai.twentyfour.app.service;

import com.yinghai.twentyfour.common.model.TfMaster;
import com.yinghai.twentyfour.common.model.TfSubscribe;
import com.yinghai.twentyfour.common.model.TfSubscribeHelper;
import com.yinghai.twentyfour.common.util.Page;

/**
 * 我的关注
 * @author Administrator
 *
 */
public interface SubscribeService {
	/**
	 * 查询是否已经关注
	 * @param userId
	 * @param masterId
	 * @return
	 */
	TfSubscribe selectByUserAndMaster(Integer userId,Integer masterId);
	/**
	 * 创建关注记录
	 * @param userId
	 * @param masterId
	 * @return
	 */
	int createSubscribe(Integer userId,Integer masterId,TfMaster m);
	/**
	 * 删除关注记录
	 * @param subscribeId
	 * @return
	 */
	int deleteSubscribe(Integer userId,Integer masterId,Long follows);
	/**
	 * 根据用户id分页查询关注列表
	 * @param userId
	 * @return
	 */
	Page<TfSubscribeHelper> getSubscribesByPage(Integer pageNum,Integer pageSize,Integer userId);
	/**
	 * 根据关注记录id查询关注信息详情
	 * @param i
	 * @return
	 */
	TfSubscribeHelper selectSubscribeById(int subscribeId);
	/**
	 *  根据条件查询关注信息详情
	 * @param s
	 * @return
	 */
	TfSubscribeHelper selectDetailSubscribe(TfSubscribe s);
	
}
