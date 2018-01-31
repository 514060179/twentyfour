package com.yinghai.twentyfour.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yinghai.twentyfour.common.model.TfMaster;
import com.yinghai.twentyfour.common.util.Page;

public interface TfMasterMapper {
    int deleteByPrimaryKey(Integer masterId);

    int insert(TfMaster record);

    int insertSelective(TfMaster record);

    TfMaster selectByPrimaryKey(Integer masterId);

    int updateByPrimaryKeySelective(TfMaster record);

    int updateByPrimaryKeyWithBLOBs(TfMaster record);

    int updateByPrimaryKey(TfMaster record);
    
    TfMaster findByTel(@Param("countryCode")String countryCode,@Param("tel")String tel);

    Page<TfMaster>  getTfMasterRecord(TfMaster record);

    int updateBargain(TfMaster record);

    int findTopId();
    
    List<TfMaster> getTfMasterAndConSubRecord(@Param("startNumber")int startNumber,@Param("pageStartSize")int count,@Param("master")TfMaster record);
}