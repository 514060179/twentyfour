package com.yinghai.twentyfour.common.service.impl;

import com.yinghai.twentyfour.common.dao.TfBusinessTypeMapper;
import com.yinghai.twentyfour.common.model.TfBusinessType;
import com.yinghai.twentyfour.common.service.TfBusinessTypeService;
import com.yinghai.twentyfour.common.util.Page;
import com.yinghai.twentyfour.common.util.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Administrator on 2017/12/20.
 */
public class TfBusinessTypeServiceImpl implements TfBusinessTypeService{

    @Autowired
    private TfBusinessTypeMapper tfBusinessTypeMapper;

    @Override
    public Page<TfBusinessType> findByPage(int pageNum,int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return tfBusinessTypeMapper.findByPage();
    }

    @Override
    public List<TfBusinessType> findAll() {
        return tfBusinessTypeMapper.findAll();
    }
}
