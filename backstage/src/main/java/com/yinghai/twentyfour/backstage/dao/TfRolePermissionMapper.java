package com.yinghai.twentyfour.backstage.dao;

import com.yinghai.twentyfour.backstage.model.TfRolePermission;

public interface TfRolePermissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TfRolePermission record);

    int insertSelective(TfRolePermission record);

    TfRolePermission selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TfRolePermission record);

    int updateByPrimaryKey(TfRolePermission record);
}