package com.yinghai.twentyfour.backstage.service;

import com.yinghai.twentyfour.backstage.model.TfPermission;

import java.util.Set;

/**
 * Created by Administrator on 2018/2/23.
 */
public interface TfPermissionService {

    Set<String> findPermissionByUserId(Integer userId);

    TfPermission findById(Integer id);
}
