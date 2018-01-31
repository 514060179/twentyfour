package com.yinghai.twentyfour.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.yinghai.twentyfour.common.dao.ThirdPartyMapper;
import com.yinghai.twentyfour.common.model.ThirdParty;
import com.yinghai.twentyfour.common.service.ThirdPartyService;

public class ThirdPartyServiceImpl implements ThirdPartyService {
	@Autowired
	private ThirdPartyMapper thirdPartyMapper;

	@Override
	public int insertSelective(ThirdParty thirdParty) {
		// TODO Auto-generated method stub
		return thirdPartyMapper.insertSelective(thirdParty);
	}

	@Override
	public ThirdParty selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return thirdPartyMapper.selectByPrimaryKey(id);
	}

	@Override
	public ThirdParty seletByOpenid(String openId,Integer tfType) {
		// TODO Auto-generated method stub
		return thirdPartyMapper.selectByOpenId(openId,tfType);
	}

	@Override
	public int updatePrimaryKey(ThirdParty thirdParty) {
		// TODO Auto-generated method stub
		return thirdPartyMapper.updatePrimaryKey(thirdParty);
	}

	@Override
	public ThirdParty selectByTfIdAndType(Integer driverId, Integer type, Integer tfType) {
		// TODO Auto-generated method stub
		return thirdPartyMapper.selectByTfIdAndType(driverId, type, tfType);
	}

}
