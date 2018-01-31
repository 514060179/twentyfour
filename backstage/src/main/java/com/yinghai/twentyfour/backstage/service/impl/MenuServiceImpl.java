package com.yinghai.twentyfour.backstage.service.impl;

import com.yinghai.twentyfour.backstage.dao.MenuMapper;
import com.yinghai.twentyfour.backstage.model.Menu;
import com.yinghai.twentyfour.backstage.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Administrator on 2017/7/27.
 */
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;
    @Override
    public List<Menu> findAll() {
        return menuMapper.findMenus();
    }
}
