package com.yinghai.twentyfour.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yinghai.twentyfour.common.model.TfHistory;
import com.yinghai.twentyfour.common.model.TfHistoryHelper;
import com.yinghai.twentyfour.common.util.Page;

public interface TfHistoryMapper {
    int deleteByPrimaryKey(Integer historyId);

    int insert(TfHistory record);

    int insertSelective(TfHistory record);

    TfHistory selectByPrimaryKey(Integer historyId);

    int updateByPrimaryKeySelective(TfHistory record);

    int updateByPrimaryKey(TfHistory record);
    
    Page<TfHistoryHelper> selectByUserId(@Param("userId")Integer userId,@Param("start")Integer start,@Param("end")Integer end);

	List<TfHistory> selectByUserAndTypeId(@Param("userId")Integer userId, @Param("type")Integer type, @Param("id")Integer id);
}