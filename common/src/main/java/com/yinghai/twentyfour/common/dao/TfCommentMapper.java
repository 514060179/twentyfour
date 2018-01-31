package com.yinghai.twentyfour.common.dao;

import com.yinghai.twentyfour.common.model.TfComment;
import com.yinghai.twentyfour.common.model.TfCommentHelper;
import com.yinghai.twentyfour.common.model.TfCommentWithBLOBs;
import com.yinghai.twentyfour.common.util.Page;

public interface TfCommentMapper {
    int deleteByPrimaryKey(Integer commentId);

    int insert(TfComment record);

    int insertSelective(TfComment record);

    TfComment selectByPrimaryKey(Integer commentId);

    int updateByPrimaryKeySelective(TfComment record);

    int updateByPrimaryKeyWithBLOBs(TfComment record);

    int updateByPrimaryKey(TfComment record);
    
    int deleteByCondition(TfComment record);

	Page<TfCommentWithBLOBs> findCommentByPage(TfComment c);

	TfCommentHelper selectCommentDetail(Integer commentId);

	Page<TfComment> findCommentByPageBack(TfComment c);

	TfCommentWithBLOBs findCommentBySelective(TfComment comment);
}