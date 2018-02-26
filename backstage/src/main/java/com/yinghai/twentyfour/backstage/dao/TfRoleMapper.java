package com.yinghai.twentyfour.backstage.dao;

import com.yinghai.twentyfour.backstage.model.TfRole;

import java.util.Set;

public interface TfRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TfRole record);

    int insertSelective(TfRole record);

    TfRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TfRole record);

    int updateByPrimaryKey(TfRole record);

    Set<String> findRoleByUserId(Integer userId);
}