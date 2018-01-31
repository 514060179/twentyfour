package com.yinghai.twentyfour.common.service.impl;

import com.yinghai.twentyfour.common.dao.TfCashRecordMapper;
import com.yinghai.twentyfour.common.dao.TfMasterWalletMapper;
import com.yinghai.twentyfour.common.model.TfCashRecord;
import com.yinghai.twentyfour.common.model.TfCashRecordHelper;
import com.yinghai.twentyfour.common.model.TfMasterWallet;
import com.yinghai.twentyfour.common.service.TfCashRecordService;
import com.yinghai.twentyfour.common.util.Page;
import com.yinghai.twentyfour.common.util.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by Administrator on 2017/10/27.
 */
public class TfCashRecordServiceImpl implements TfCashRecordService {

    @Autowired
    private TfCashRecordMapper tfCashRecordMapper;

    @Autowired
    private TfMasterWalletMapper tfMasterWalletMapper;

    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public TfCashRecord createTfCashRecord(TfCashRecord tfCashRecord) {

        int i = tfCashRecordMapper.insertSelective(tfCashRecord);
        TfMasterWallet tfMasterWallet = new TfMasterWallet();
        tfMasterWallet.setwMasterId(tfCashRecord.getrMasterId());
        tfMasterWallet.setwBalance(-tfCashRecord.getrAmount());
        tfMasterWallet.setwUpdateTime(new Date());
        tfMasterWalletMapper.updateMasterBalance(tfMasterWallet);
        return tfCashRecord;
    }

    @Override
    public Page<TfCashRecord> findListByPage(Integer pageNumber, Integer pageSize, TfCashRecord tfCashRecord) {
        PageHelper.startPage(pageNumber,pageSize);
        return tfCashRecordMapper.findByCondition(tfCashRecord);
    }

    @Override
    public TfCashRecord findNoCompleteCashRecord(Integer masterId) {
        return tfCashRecordMapper.findNoCompleteCashRecord(masterId);
    }

	@Override
	public TfCashRecordHelper findCashRecord(Integer recordId) {
		return tfCashRecordMapper.selectByPrimaryKey(recordId);
	}

	@Override
	public TfCashRecord findCashRecordByMasterId(Integer masterId) {
		//查询上一次成功提现记录
		return tfCashRecordMapper.selectLastRecord(masterId);
	}

	@Override
	@Transactional
	public int updateAuditRecord(TfCashRecord cr) {
		
		//更新提现记录状态
		int i = tfCashRecordMapper.updateByPrimaryKeySelective(cr);
		//TODO推送消息给APP端
		return i;
	}

	@Override
	@Transactional
	public int updateCompleteRecord(TfCashRecord record, Integer masterId) {
		//更新大师余额,插入一条钱包余额记录
		/*TfMasterWallet mw = tfMasterWalletMapper.findWalletByMasterId(masterId);
		if(mw==null){
			throw new RuntimeException("钱包余额不存在");
		}
		Integer amount = mw.getwBalance()-record.getrAmount();
		if(amount<0){
			throw new RuntimeException("提现金额超过余额");
		}
		TfMasterWallet w = new TfMasterWallet();
		w.setwMasterId(masterId);
		w.setwCreateTime(new Date());
		w.setwBalance(amount);
		w.setwCashTime(record.getrCreateTime());
		int j = tfMasterWalletMapper.insertSelective(w);
		if(j!=1){
			throw new RuntimeException("数据出错");
		}*/
		//更新提现记录
		TfCashRecord r = new TfCashRecord();
		r.setrMasterId(masterId);
		r.setRecordId(record.getRecordId());
		r.setrStatus(3);
		int i = tfCashRecordMapper.updateByPrimaryKeySelective(r);
		//TODO发送消息给APP端
		return i;
	}


}
