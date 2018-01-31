package com.yinghai.twentyfour.common.service;

import java.util.List;
import java.util.Map;

import com.yinghai.twentyfour.common.model.TfMaster;
import com.yinghai.twentyfour.common.util.Page;

/**
 * Created by Administrator on 2017/7/18.
 */
public interface TfMasterService {
	/**
	 * 通过手机号查找个人用户信息
	 * @param countryCode
	 * @param tel
	 * @return
	 */
	TfMaster findByTel(String countryCode,String tel);
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
    int insert(TfMaster record);
    /**
     * 新增个人用户信息（指定字段）
     * @param record
     * @return
     */
    int insertSelective(TfMaster record);
    /**
     * 通过ID查找个人用户信息
     * @param userId
     * @return
     */
    TfMaster selectByPrimaryKey(Integer masterId);
    /**
     * 更新个人用户信息（指定字段）
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(TfMaster record);
    /**
     * 更新个人用户信息（全部字段）
     * @param record
     * @return
     */
    int updateByPrimaryKey(TfMaster record);

    Page<TfMaster>  getTfMasterRecord(int pageNumber,int pageStartSize,TfMaster record);
    List<TfMaster> getTfMasterAndConSubRecord(int startNumber,int count,TfMaster record);
}
