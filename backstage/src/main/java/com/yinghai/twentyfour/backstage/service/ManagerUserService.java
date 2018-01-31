package com.yinghai.twentyfour.backstage.service;

import java.util.List;

import com.yinghai.twentyfour.backstage.model.ManagerUser;
import com.yinghai.twentyfour.common.util.Page;

/**
 * Created by Administrator on 2017/7/24.
 */
public interface ManagerUserService {

	ManagerUser findOneById(Integer id);

	/**
	 * 根据查询用户名查询记录
	 * 
	 * @param username
	 * @return
	 */
	ManagerUser findByName(String username);
	
	/**
	 * 根据查询邮箱查询记录
	 * 
	 * @param username
	 * @return
	 */
	ManagerUser findByEmail(String email);
	
	/**
	 * 根据ID查询model信息
	 * 
	 * @param userId
	 *            ID
	 * @return
	 */
	ManagerUser selectByPrimaryKey(Integer userId);

	/**
	 * 保存model信息
	 * 
	 * @param user
	 *            model信息
	 * @return
	 */
	int saveManagerUser(ManagerUser user);

	/**
	 * 修改model信息
	 * 
	 * @param user
	 * @return
	 */
	int updateManagerUser(ManagerUser user);

	/**
	 * 根据ID删除model信息
	 * 
	 * @param userId
	 *            ID
	 * @return
	 */
	int deleteManagerUser(Integer userId);

	/**
	 * 查询订单列表分页
	 * 
	 * @param pageNumber
	 * @param pageStartSize
	 * @param user
	 * @return
	 */
	Page<ManagerUser> findListPage(Integer pageNumber, Integer pageStartSize, ManagerUser user);

	/**
	 * 查询订单列表分页
	 * 
	 * @param pageNumber
	 * @param pageStartSize
	 * @param user
	 * @return
	 */
	List<ManagerUser> appFindList(ManagerUser user);

	/**
	 * 获取所有专家
	 * 
	 * @return
	 */
	List<ManagerUser> findAllManagerUser();
}
