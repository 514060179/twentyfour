package com.yinghai.twentyfour.backstage.dao;

import com.yinghai.twentyfour.backstage.model.TfUserRole;

public interface TfUserRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TfUserRole record);

    int insertSelective(TfUserRole record);

    TfUserRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TfUserRole record);

    int updateByPrimaryKey(TfUserRole record);
}