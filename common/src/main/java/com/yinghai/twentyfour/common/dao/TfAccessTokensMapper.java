package com.yinghai.twentyfour.common.dao;

import com.yinghai.twentyfour.common.model.TfAccessTokens;

public interface TfAccessTokensMapper {
    int deleteByPrimaryKey(Integer accessId);

    int insert(TfAccessTokens record);

    int insertSelective(TfAccessTokens record);

    TfAccessTokens selectByPrimaryKey(Integer accessId);

    int updateByPrimaryKeySelective(TfAccessTokens record);

    int updateByPrimaryKey(TfAccessTokens record);
}