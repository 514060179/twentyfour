package com.yinghai.twentyfour.common.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.yinghai.twentyfour.common.dao.TfProductMapper;
import com.yinghai.twentyfour.common.model.TfProduct;
import com.yinghai.twentyfour.common.service.TfProductService;
import com.yinghai.twentyfour.common.util.Page;
import com.yinghai.twentyfour.common.util.PageHelper;

public class TfProductServiceImpl implements TfProductService {
	@Autowired
	private TfProductMapper productMapper;
	@Override
	public TfProduct findProductByProuductId(Integer productId) {
		return productMapper.selectByPrimaryKey(productId);
	}


	@Override
	public int deleteByPrimaryKey(Integer userId) {
		// TODO Auto-generated method stub
		return productMapper.deleteByPrimaryKey(userId);
	}

	@Override
	public int insert(TfProduct record) {
		// TODO Auto-generated method stub
		return productMapper.insert(record);
	}

	@Override
	public int insertSelective(TfProduct record) {
		// TODO Auto-generated method stub
		return productMapper.insertSelective(record);
	}

	@Override
	public TfProduct selectByPrimaryKey(Integer userId) {
		// TODO Auto-generated method stub
		return productMapper.selectByPrimaryKey(userId);
	}

	@Override
	public int updateByPrimaryKeySelective(TfProduct record) {
		// TODO Auto-generated method stub
		return productMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(TfProduct record) {
		// TODO Auto-generated method stub
		return productMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<TfProduct> getTfProductAndImgRecord(int startNumber, int pageStartSize, TfProduct tfProduct) {
		// TODO Auto-generated method stub
		//PageHelper.startPage(pageNumber,pageStartSize);
		return productMapper.getTfProductAndImgRecord(tfProduct,startNumber,pageStartSize);
		//return tfProductMapper.getTfProductRecord(map);
	}
	
	@Override
	public Page<TfProduct> getTfProductRecord(int pageNumber, int pageStartSize, TfProduct tfProduct) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageNumber,pageStartSize);
		return productMapper.getTfProductRecord(tfProduct);
		//return tfProductMapper.getTfProductRecord(map);
	}

}
