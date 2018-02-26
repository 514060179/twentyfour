package com.yinghai.twentyfour.backstage.service.impl;

import com.yinghai.twentyfour.backstage.dao.TfRoleMapper;
import com.yinghai.twentyfour.backstage.service.TfRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import java.util.Set;

/**
 * Created by Administrator on 2018/2/23.
 */
public class TfRoleServiceImpl implements TfRoleService {

    @Autowired
    private TfRoleMapper tfRoleMapper;

    @Override
    @Cacheable(value = "roleSet" ,key = "#p0")
    public Set<String> findRoleByUserId(Integer userId) {
        return tfRoleMapper.findRoleByUserId(userId);
    }
}
