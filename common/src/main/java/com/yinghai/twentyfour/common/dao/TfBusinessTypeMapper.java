package com.yinghai.twentyfour.common.dao;

import com.yinghai.twentyfour.common.model.TfBusinessType;
import com.yinghai.twentyfour.common.util.Page;

import java.util.List;

public interface TfBusinessTypeMapper {
    int deleteByPrimaryKey(Integer businessTypeId);

    int insert(TfBusinessType record);

    int insertSelective(TfBusinessType record);

    TfBusinessType selectByPrimaryKey(Integer businessTypeId);

    int updateByPrimaryKeySelective(TfBusinessType record);

    int updateByPrimaryKey(TfBusinessType record);

    Page<TfBusinessType> findByPage();

    List<TfBusinessType> findAll();
}