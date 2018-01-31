package com.yinghai.twentyfour.common.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.yinghai.twentyfour.common.dao.TfArticleMapper;
import com.yinghai.twentyfour.common.model.TfArticle;
import com.yinghai.twentyfour.common.service.TfArticleService;
import com.yinghai.twentyfour.common.util.Page;
import com.yinghai.twentyfour.common.util.PageHelper;

public class TfArticleServiceImpl implements TfArticleService {
	@Autowired
	private TfArticleMapper tfArticleMapper;

	@Override
	public int deleteByPrimaryKey(Integer userId) {
		// TODO Auto-generated method stub
		return tfArticleMapper.deleteByPrimaryKey(userId);
	}

	@Override
	public int insert(TfArticle record) {
		// TODO Auto-generated method stub
		return tfArticleMapper.insert(record);
	}

	@Override
	public int insertSelective(TfArticle record) {
		// TODO Auto-generated method stub
		return tfArticleMapper.insertSelective(record);
	}

	@Override
	public TfArticle selectByPrimaryKey(Integer userId) {
		// TODO Auto-generated method stub
		return tfArticleMapper.selectByPrimaryKey(userId);
	}

	@Override
	public int updateByPrimaryKeySelective(TfArticle record) {
		// TODO Auto-generated method stub
		return tfArticleMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(TfArticle record) {
		// TODO Auto-generated method stub
		return tfArticleMapper.updateByPrimaryKey(record);
	}

	@Override
	public Page<TfArticle> getTfArticleRecord(int pageNumber, int pageStartSize, TfArticle tfArticle) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageNumber,pageStartSize);
		return tfArticleMapper.getTfArticleRecord(tfArticle);
	}

	@Override
	public List<TfArticle> getTfArticleAndImgRecord(int startNumber, int pageStartSize, TfArticle tfArticle) {
		return tfArticleMapper.getTfArticleAndImgRecord(tfArticle, startNumber, pageStartSize);
	}

}
