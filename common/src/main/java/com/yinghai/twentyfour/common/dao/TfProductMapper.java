package com.yinghai.twentyfour.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yinghai.twentyfour.common.model.TfProduct;
import com.yinghai.twentyfour.common.util.Page;

public interface TfProductMapper {
    int deleteByPrimaryKey(Integer productId);

    int insert(TfProduct record);

    int insertSelective(TfProduct record);

    TfProduct selectByPrimaryKey(Integer productId);

    int updateByPrimaryKeySelective(TfProduct record);

    int updateByPrimaryKeyWithBLOBs(TfProduct record);

    int updateByPrimaryKey(TfProduct record);
    List<TfProduct> getTfProductAndImgRecord(@Param("tfProduct")TfProduct record,@Param("startNumber")Integer startNumber, @Param("pageStartSize")Integer pageStartSize);
    Page<TfProduct> getTfProductRecord(TfProduct record);
}