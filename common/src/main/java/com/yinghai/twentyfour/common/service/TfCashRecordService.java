package com.yinghai.twentyfour.common.service;

import com.yinghai.twentyfour.common.model.TfCashRecord;
import com.yinghai.twentyfour.common.model.TfCashRecordHelper;
import com.yinghai.twentyfour.common.util.Page;

/**
 * Created by Administrator on 2017/10/27.
 */
public interface TfCashRecordService {

    /**
     * 创建提现记录
     * @param tfCashRecord
     * @return
     */
    TfCashRecord createTfCashRecord(TfCashRecord tfCashRecord);

    /**
     * 分页查询
     * @param pageNumber
     * @param pageSize
     * @return
     */
    Page<TfCashRecord> findListByPage(Integer pageNumber,Integer pageSize,TfCashRecord tfCashRecord);

    /**
     * 查询未完成提款记录
     * @return
     */
    TfCashRecord findNoCompleteCashRecord(Integer masterId);
    /**
     * 根据ID查询提现记录
     * @param recordId
     * @return
     */
    TfCashRecordHelper findCashRecord(Integer recordId);
    /**
     * 查询最近一条
     * @param masterId
     * @return
     */
    TfCashRecord findCashRecordByMasterId(Integer masterId);
    /**
     * 更新提现记录信息——审核提现申请
     * @param cr
     * @return
     */
	int updateAuditRecord(TfCashRecord cr);
	/**
	 * 更新提现记录信息——完成提现
	 * @param recordId
	 * @param masterId
	 * @return
	 */
	int updateCompleteRecord(TfCashRecord record, Integer masterId);
}
