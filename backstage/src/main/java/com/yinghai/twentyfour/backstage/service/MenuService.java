package com.yinghai.twentyfour.backstage.service;

import com.yinghai.twentyfour.backstage.model.Menu;

import java.util.List;

/**
 * Created by Administrator on 2017/7/27.
 */
public interface MenuService {
    /**
     * 查询菜单列表
     * @return
     */
    List<Menu> findAll();
}
