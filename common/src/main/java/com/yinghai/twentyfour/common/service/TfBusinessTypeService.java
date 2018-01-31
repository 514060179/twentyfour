package com.yinghai.twentyfour.common.service;

import com.yinghai.twentyfour.common.model.TfBusinessType;
import com.yinghai.twentyfour.common.util.Page;

import java.util.List;

/**
 * Created by Administrator on 2017/12/20.
 */
public interface TfBusinessTypeService {

    /**
     * 分页查询
     * @return
     */
    Page<TfBusinessType> findByPage(int pageNum,int pageSize);

    /**
     * 查询所有
     * @return
     */
    List<TfBusinessType> findAll();
}
