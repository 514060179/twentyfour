package com.yinghai.twentyfour.common.service;

import java.util.Map;

import com.yinghai.twentyfour.common.model.TfImUser;
import com.yinghai.twentyfour.common.model.TfUser;
import com.yinghai.twentyfour.common.util.Page;
import com.yinghai.twentyfour.common.vo.MasterAndUserIm;

/**
 * Created by Administrator on 2017/7/18.
 */
public interface TfUserService {
	
	TfUser findUserById(Integer userId);
	/**
	 * 通过手机号查找个人用户信息
	 * @param countryCode
	 * @param tel
	 * @return
	 */
    TfUser findByTel(String countryCode,String tel);
    /**
     * 删除个人用户信息
     * @param userId
     * @return
     */
    int deleteByPrimaryKey(Integer userId);
    /**
     * 新增个人用户信息（所有字段）
     * @param record
     * @return
     */
    int insert(TfUser record);
    /**
     * 新增个人用户信息（指定字段）
     * @param record
     * @return
     */
    int insertSelective(TfUser record);
    /**
     * 通过ID查找个人用户信息
     * @param userId
     * @return
     */
    TfUser selectByPrimaryKey(Integer userId);
    /**
     * 更新个人用户信息（指定字段）
     * @param userId
     * @return
     */
    int updateByPrimaryKeySelective(TfUser record);
    /**
     * 更新个人用户信息（全部字段）
     * @param userId
     * @return
     */
    int updateByPrimaryKey(TfUser record);
    
    Page<TfUser>  getTfUserRecord(int pageNumber,int pageStartSize,TfUser map);


    /**
     * 根据订单号获取IM账户
     * @param orderNo
     * @return
     */
    MasterAndUserIm findByOrderNo(String orderNo);

    /**
     * 根据总订单号获取IM账户
     * @param orderTotalNo
     * @return
     */
    MasterAndUserIm findByOrderTotalNo(String orderTotalNo);
}
