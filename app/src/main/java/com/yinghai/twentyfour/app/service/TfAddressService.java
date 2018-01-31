package com.yinghai.twentyfour.app.service;

import java.util.List;

import com.yinghai.twentyfour.common.model.TfAddress;
import com.yinghai.twentyfour.common.util.Page;

/**
 * 收货地址业务层接口
 * @author Administrator
 *
 */
public interface TfAddressService {
	/**
	 * 根据用户id查询收货地址列表
	 * @param userId
	 * @return
	 */
	public List<TfAddress> findAddressByUserId(Integer userId);
	/**
	 * 新增收货地址
	 * @param address
	 * @return
	 */
	public int createAddress(TfAddress address);
	/**
	 * 根据地址ID删除地址记录
	 * @param addressId
	 * @return
	 */
	public TfAddress deleteAddress(Integer addressId,Integer userId);
	/**
	 * 根据地址ID查询地址记录
	 * @param addressId
	 * @return
	 */
	public TfAddress findAddressById(Integer addressId);
	/**
	 * 更新地址信息
	 * @param a1
	 */
	public int updateAddress(TfAddress address);
	/**
	 * 查询地址信息列表
	 * @param pageNum
	 * @param pageSize
	 * @param userId
	 * @return
	 */
	public Page<TfAddress> findAddressByPage(Integer pageNum, Integer pageSize, Integer userId);
	/**
	 * 根据用户ID和地址Id查询地址记录
	 * @param userId
	 * @param addressId
	 * @return
	 */
	public TfAddress findAdressById(Integer userId, Integer addressId);
	/**
	 * 更改默认地址
	 * @param userId
	 * @param a1
	 * @return
	 */
	public int updateDefaultAddress(Integer userId, TfAddress a1);
	/**
	 * 根据id和用户Id查询地址信息
	 * @param userId
	 * @param addressId
	 * @return
	 */
	public TfAddress findAddressByUserId(Integer userId, Integer addressId);
}
