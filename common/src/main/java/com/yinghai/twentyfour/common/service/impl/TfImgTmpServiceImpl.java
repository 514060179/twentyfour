package com.yinghai.twentyfour.common.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.yinghai.twentyfour.common.dao.TfImgTmpMapper;
import com.yinghai.twentyfour.common.model.TfImgTmp;
import com.yinghai.twentyfour.common.service.TfImgTmpService;
import com.yinghai.twentyfour.common.util.Page;
import com.yinghai.twentyfour.common.util.PageHelper;

public class TfImgTmpServiceImpl implements TfImgTmpService {
	@Autowired
	private TfImgTmpMapper tfImgTmpMapper;

	@Override
	public int deleteByPrimaryKey(Integer userId) {
		// TODO Auto-generated method stub
		return tfImgTmpMapper.deleteByPrimaryKey(userId);
	}

	@Override
	public int insert(TfImgTmp record) {
		// TODO Auto-generated method stub
		return tfImgTmpMapper.insert(record);
	}

	@Override
	public int insertSelective(TfImgTmp record) {
		// TODO Auto-generated method stub
		return tfImgTmpMapper.insertSelective(record);
	}

	@Override
	public TfImgTmp selectByPrimaryKey(Integer userId) {
		// TODO Auto-generated method stub
		return tfImgTmpMapper.selectByPrimaryKey(userId);
	}

	@Override
	public int updateByPrimaryKeySelective(TfImgTmp record) {
		// TODO Auto-generated method stub
		return tfImgTmpMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(TfImgTmp record) {
		// TODO Auto-generated method stub
		return tfImgTmpMapper.updateByPrimaryKey(record);
	}

	@Override
	public Page<TfImgTmp> getTfImgTmpRecord(int pageNumber, int pageStartSize, Map map) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageNumber,pageStartSize);
		return tfImgTmpMapper.getTfImgTmpRecord(map);
	}

	@Override
	public int updateKeyIdById(String[] ids,Integer keyId) {
		// TODO Auto-generated method stub
		return tfImgTmpMapper.updateKeyIdById(ids,keyId);
	}

	@Override
	public List<TfImgTmp> getListByArticle(int articleId,int type) {
		// TODO Auto-generated method stub
		return tfImgTmpMapper.getListByArticle(articleId,type);
	}

}
