package com.yinghai.twentyfour.common.dao;

import com.yinghai.twentyfour.common.model.TfImClient;

public interface TfImClientMapper {
    int deleteByPrimaryKey(Integer accid);

    int insert(TfImClient record);

    int insertSelective(TfImClient record);

    TfImClient selectByPrimaryKey(Integer accid);

    int updateByPrimaryKeySelective(TfImClient record);

    int updateByPrimaryKey(TfImClient record);
}