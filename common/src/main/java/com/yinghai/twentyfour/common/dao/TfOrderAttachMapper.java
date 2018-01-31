package com.yinghai.twentyfour.common.dao;

import com.yinghai.twentyfour.common.model.TfOrderAttach;

public interface TfOrderAttachMapper {
    int deleteByPrimaryKey(Integer attachId);

    int insert(TfOrderAttach record);

    int insertSelective(TfOrderAttach record);

    TfOrderAttach selectByPrimaryKey(Integer attachId);

    int updateByPrimaryKeySelective(TfOrderAttach record);

    int updateByPrimaryKeyWithBLOBs(TfOrderAttach record);

    int updateByPrimaryKey(TfOrderAttach record);
}