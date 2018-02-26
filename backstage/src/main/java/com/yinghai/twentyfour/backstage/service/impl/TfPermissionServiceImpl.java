package com.yinghai.twentyfour.backstage.service.impl;

import com.yinghai.twentyfour.backstage.dao.TfPermissionMapper;
import com.yinghai.twentyfour.backstage.model.TfPermission;
import com.yinghai.twentyfour.backstage.service.TfPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import java.util.Set;

/**
 * Created by Administrator on 2018/2/23.
 */
public class TfPermissionServiceImpl implements TfPermissionService {

    @Autowired
    private TfPermissionMapper tfPermissionMapper;
    @Override
    @Cacheable(value = "permissionSet" ,key = "#userId")
    public Set<String> findPermissionByUserId(Integer userId) {
        return tfPermissionMapper.findPermissionByUserId(userId);
    }

    @Cacheable(value = "permissionSet",key = "#p0")
    @Override
    public TfPermission findById(Integer id) {
        return tfPermissionMapper.selectByPrimaryKey(id);
    }
}
