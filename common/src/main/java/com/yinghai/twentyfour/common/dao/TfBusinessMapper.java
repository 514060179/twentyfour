package com.yinghai.twentyfour.common.dao;

import com.yinghai.twentyfour.common.model.TfBusiness;
import com.yinghai.twentyfour.common.util.Page;

import java.util.List;

public interface TfBusinessMapper {
    int deleteByPrimaryKey(Integer businessId);

    int insert(TfBusiness record);

    int insertSelective(TfBusiness record);

    TfBusiness selectByPrimaryKey(Integer businessId);

    int updateByPrimaryKeySelective(TfBusiness record);

    int updateByPrimaryKeyWithBLOBs(TfBusiness record);

    int updateByPrimaryKey(TfBusiness record);

    Page<TfBusiness> findOneByCondition(TfBusiness record);

    List<TfBusiness> findLowerBusiness(Integer businessId);
}