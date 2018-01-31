package com.yinghai.twentyfour.common.dao;

import java.util.List;

import com.yinghai.twentyfour.common.model.TfLuck;
import com.yinghai.twentyfour.common.model.TfLuckKey;
import com.yinghai.twentyfour.common.model.TfLuckWithBLOBs;
import com.yinghai.twentyfour.common.util.Page;

public interface TfLuckMapper {
    int deleteByPrimaryKey(TfLuckKey key);

    int insert(TfLuckWithBLOBs record);

    int insertSelective(TfLuckWithBLOBs record);

    TfLuckWithBLOBs selectByPrimaryKey(TfLuckKey key);

    int updateByPrimaryKeySelective(TfLuckWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(TfLuckWithBLOBs record);

    int updateByPrimaryKey(TfLuck record);
    
    Page<TfLuck> findByCondition(TfLuckKey key);
    
    TfLuckWithBLOBs selectById(Integer luckId);

	int deleteById(Integer luckId);
    
    
}