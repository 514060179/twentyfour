package com.yinghai.twentyfour.common.dao;

import com.yinghai.twentyfour.common.model.TfCashRecord;
import com.yinghai.twentyfour.common.model.TfCashRecordHelper;
import com.yinghai.twentyfour.common.util.Page;

public interface TfCashRecordMapper {
    int deleteByPrimaryKey(Integer recordId);

    int insert(TfCashRecord record);

    int insertSelective(TfCashRecord record);

    TfCashRecordHelper selectByPrimaryKey(Integer recordId);

    int updateByPrimaryKeySelective(TfCashRecord record);

    int updateByPrimaryKey(TfCashRecord record);

    Page<TfCashRecord> findByCondition(TfCashRecord tfCashRecord);

    TfCashRecord findNoCompleteCashRecord(Integer masterId);

	TfCashRecord selectLastRecord(Integer masterId);
}