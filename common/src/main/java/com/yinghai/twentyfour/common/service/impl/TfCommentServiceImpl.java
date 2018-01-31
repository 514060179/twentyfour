package com.yinghai.twentyfour.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.yinghai.twentyfour.common.dao.TfCommentMapper;
import com.yinghai.twentyfour.common.model.TfComment;
import com.yinghai.twentyfour.common.model.TfCommentHelper;
import com.yinghai.twentyfour.common.model.TfCommentWithBLOBs;
import com.yinghai.twentyfour.common.service.TfCommentService;
import com.yinghai.twentyfour.common.util.Page;
import com.yinghai.twentyfour.common.util.PageHelper;

public class TfCommentServiceImpl implements TfCommentService {
	@Autowired
	private TfCommentMapper tfCommentMapper;
	@Override
	public int createComment(TfComment c) {
		return tfCommentMapper.insertSelective(c);
	}
	@Override
	public int deleteCommentById(Integer commentId) {
		return tfCommentMapper.deleteByPrimaryKey(commentId);
	}
	@Override
	public int deleteComment(TfComment c) {
		return tfCommentMapper.deleteByCondition(c);
	}
	@Override
	public Page<TfCommentWithBLOBs> findCommentByPage(Integer pageNum, Integer pageSize, TfComment c) {
		PageHelper.startPage(pageNum, pageSize);
		return tfCommentMapper.findCommentByPage(c);
	}
	@Override
	public TfComment findCommentById(Integer commentId) {
		return tfCommentMapper.selectByPrimaryKey(commentId);
	}
	@Override
	public int updateComment(TfComment c) {
		return tfCommentMapper.updateByPrimaryKeySelective(c);
	}
	@Override
	public TfCommentHelper findCommentDetail(Integer commentId) {
		return tfCommentMapper.selectCommentDetail(commentId);
	}
	@Override
	public Page<TfComment> findCommentByPageBack(Integer pageNum, Integer pageSize, TfComment c) {
		PageHelper.startPage(pageNum, pageSize);
		return tfCommentMapper.findCommentByPageBack(c);
	}
	@Override
	public TfCommentWithBLOBs findDetailComment(TfComment comment) {
		return tfCommentMapper.findCommentBySelective(comment);
	}

}
