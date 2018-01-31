package com.yinghai.twentyfour.common.service;

import com.yinghai.twentyfour.common.model.ThirdParty;

/**
 * Created by Administrator on 2017/7/18.
 */
public interface ThirdPartyService {
	/**
	 * 新增数据
	 * 
	 * @param order
	 */
	int insertSelective(ThirdParty thirdParty);

	/**
	 * 根据ID查询数据
	 * 
	 * @param id
	 * @return
	 */
	ThirdParty selectByPrimaryKey(Integer id);

	/**
	 * 根据openid唯一标识查询第三方记录
	 * 
	 * @param openId微信唯一标识
	 * @param tfType用户类型0为客户端用户1为大师端用户
	 * @return
	 */
	ThirdParty seletByOpenid(String openId,Integer tfType);

	/**
	 * 更新操作
	 * 
	 * @param thirdParty
	 * @return
	 */
	int updatePrimaryKey(ThirdParty thirdParty);

	/**
	 * 根据根据骑手ID与类型查询第三方登录数据
	 * 
	 * @param type
	 *            登录类型0微信1.。。。。
	 * @param tfType
	 *            用户类型0为客户端用户1为大师端用户
	 * @param tfId
	 *            关联ID
	 * @return
	 */
	ThirdParty selectByTfIdAndType(Integer tfId, Integer type, Integer tfType);
}
