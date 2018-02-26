package com.yinghai.twentyfour.backstage.dao;

import com.yinghai.twentyfour.backstage.model.TfPermission;

import java.util.Set;

public interface TfPermissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TfPermission record);

    int insertSelective(TfPermission record);

    TfPermission selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TfPermission record);

    int updateByPrimaryKey(TfPermission record);

    Set<String> findPermissionByUserId(Integer userId);
}