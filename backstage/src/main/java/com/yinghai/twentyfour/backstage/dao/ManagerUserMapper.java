package com.yinghai.twentyfour.backstage.dao;

import com.yinghai.twentyfour.backstage.model.ManagerUser;
import com.yinghai.twentyfour.common.util.Page;

public interface ManagerUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ManagerUser record);

    int insertSelective(ManagerUser record);

    ManagerUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ManagerUser record);

    int updateByPrimaryKey(ManagerUser record);

    Page<ManagerUser> findByCondition(ManagerUser record);
}