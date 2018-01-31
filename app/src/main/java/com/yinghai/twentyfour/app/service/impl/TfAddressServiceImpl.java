package com.yinghai.twentyfour.app.service.impl;


import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.yinghai.twentyfour.app.service.TfAddressService;
import com.yinghai.twentyfour.common.dao.TfAddressMapper;
import com.yinghai.twentyfour.common.model.TfAddress;
import com.yinghai.twentyfour.common.util.Page;
import com.yinghai.twentyfour.common.util.PageHelper;

public class TfAddressServiceImpl implements TfAddressService {
	private Logger logger = Logger.getLogger(TfAddressServiceImpl.class);
	@Autowired
	private TfAddressMapper tfAddressMapper;
	@Override
	public List<TfAddress> findAddressByUserId(Integer userId) {
		return tfAddressMapper.selectByUserId(userId);
	}
	@Override
	public int createAddress(TfAddress address) {
		return tfAddressMapper.insertSelective(address);
	}
	@Override
	@Transactional
	public TfAddress deleteAddress(Integer addressId,Integer userId) {
		TfAddress a1 = tfAddressMapper.selectByAddressAndUser(addressId, userId);
		if(a1==null)return null;
		if(a1!=null&&a1.getAsDefault()){
			//更新默认地址
			List<TfAddress> list = tfAddressMapper.selectOtherByUserId(a1.getAsUserId(),addressId);
			if(list!=null&&list.size()>1){
				TfAddress a2 = new TfAddress();
				a2.setAddressId(list.get(0).getAddressId());
				a2.setAsDefault(true);
				int i = tfAddressMapper.updateByPrimaryKeySelective(a2);
				if(i!=1){
					logger.error("======TfAddressServiceImpl/deleteAddress====更新默认地址失败");
					throw new RuntimeException("======TfAddressServiceImpl/deleteAddress====更新默认地址失败");
				}
			}
		}
		int i = tfAddressMapper.deleteByPrimaryKey(addressId);
		if(i!=1){
			throw new RuntimeException("======TfAddressServiceImpl/deleteAddress====删除收货地址记录失败");
		}
		return a1;
	}
	@Override
	public TfAddress findAddressById(Integer addressId) {
		return tfAddressMapper.selectByPrimaryKey(addressId);
	}
	@Override
	public int updateAddress(TfAddress address) {
		return tfAddressMapper.updateByPrimaryKeySelective(address);
	}
	@Override
	public Page<TfAddress> findAddressByPage(Integer pageNum, Integer pageSize, Integer userId) {
		PageHelper.startPage(pageNum, pageSize);
		return tfAddressMapper.selectAddressByPage(userId);
	}
	@Override
	public TfAddress findAdressById(Integer userId, Integer addressId) {
		return tfAddressMapper.selectByAddressAndUser(addressId, userId);
	}
	@Override
	
	public int updateDefaultAddress(Integer userId, TfAddress a) {
		List<TfAddress> list = tfAddressMapper.selectByUserId(userId);
		if(list!=null){
			for(TfAddress address:list){
				if(address.getAsDefault()){
					address.setAsDefault(false);
					address.setAsUpdateTime(new Date());
					int i = tfAddressMapper.updateByPrimaryKeySelective(address);
					if(i!=1){
						throw new RuntimeException("TfAddressServiceImpl/updateDefaultAddress===修改默认地址失败");
					}
				}
			}
		}
		int j = tfAddressMapper.updateByPrimaryKeySelective(a);
		if(j!=1){
			throw new RuntimeException("TfAddressServiceImpl/updateDefaultAddress===修改默认地址失败");
		}
		return j;
	}
	@Override
	public TfAddress findAddressByUserId(Integer userId, Integer addressId) {
		return tfAddressMapper.findAddressByUserId(userId,addressId);
	}

}
