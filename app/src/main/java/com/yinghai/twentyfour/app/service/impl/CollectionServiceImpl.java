package com.yinghai.twentyfour.app.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.yinghai.twentyfour.app.service.CollectionService;
import com.yinghai.twentyfour.common.dao.TfCollectionMapper;
import com.yinghai.twentyfour.common.model.TfCollection;
import com.yinghai.twentyfour.common.model.TfCollectionHelper;
import com.yinghai.twentyfour.common.util.Page;
import com.yinghai.twentyfour.common.util.PageHelper;

public class CollectionServiceImpl implements CollectionService {
	@Autowired
	private TfCollectionMapper collectionMapper;
	@Override
	public int createCollection(Integer userId, Integer type, Integer id) {
		TfCollection c  = new TfCollection();
		if(type==1){
			c.setCnMasterId(id);
			c.setCnType(type);
			c.setCnUserId(userId);
		}else if(type==2){
			c.setCnArticleId(id);
			c.setCnType(type);
			c.setCnUserId(userId);
		}else if(type==3){
			c.setCnProductId(id);
			c.setCnType(type);
			c.setCnUserId(userId);
		}else{
			return 0;
		}
		c.setCnCreateTime(new Date());
		int i = collectionMapper.insertSelective(c);
		if(i!=1){
			return 0;
		}
		return c.getCollectionId();
	}
/*	@Override
	public int deleteCollection(Integer collectionId,Integer userId) {
		return collectionMapper.deleteByUser(collectionId,userId);
	}*/
	@Override
	public Page<TfCollectionHelper> getCollectionsByPage(Integer start, Integer end, Integer userId) {
		//PageHelper.startPage(pageNum, pageSize);
		return collectionMapper.selectByUserId(userId,start,end);
	}
	@Override
	public TfCollection selectById(Integer userId, Integer type, Integer id) {
		TfCollection c = new TfCollection();
		c.setCnUserId(userId);
		if(type==1){
			c.setCnMasterId(id);
		}else if(type==2){
			c.setCnArticleId(id);
		}else if(type==3){
			c.setCnProductId(id);
		}else{
			return null;
		}
		c.setCnType(type);
		return collectionMapper.selectByCollection(c);
	}
	@Override
	public int deleteCollectionSelective(TfCollection c) {
		return collectionMapper.deleteBySelective(c);
	}
	
	@Override
	public TfCollection selectCountByUserIdAndKeyIdAndType(Integer userId, Integer keyId, Integer type) {
		// TODO Auto-generated method stub
		return collectionMapper.selectCountByUserIdAndKeyIdAndType(userId, keyId, type);
	}
	@Override
	public TfCollectionHelper selectByCollectionId(int i) {
		return collectionMapper.selectByCollectionId(i);
	}
	@Override
	public TfCollectionHelper findBySelective(TfCollection c) {
		return collectionMapper.selectDetailCollection(c);
	}

}
