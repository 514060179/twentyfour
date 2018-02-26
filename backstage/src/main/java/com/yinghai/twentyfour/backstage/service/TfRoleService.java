package com.yinghai.twentyfour.backstage.service;

import java.util.Set;

/**
 * Created by Administrator on 2018/2/23.
 */
public interface TfRoleService {

    Set<String> findRoleByUserId(Integer userId);
}
