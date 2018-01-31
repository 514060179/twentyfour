package com.yinghai.twentyfour.common.service.impl;

import com.yinghai.twentyfour.common.dao.TfMasterWalletMapper;
import com.yinghai.twentyfour.common.model.TfMasterWallet;
import com.yinghai.twentyfour.common.service.TfMasterWalletService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2017/10/27.
 */
public class TfMasterWalletServiceImpl implements TfMasterWalletService {

    @Autowired
    private TfMasterWalletMapper tfMasterWalletMapper;
    @Override
    public TfMasterWallet findWalletByMasterId(Integer masterId) {
        return tfMasterWalletMapper.findWalletByMasterId(masterId);
    }
}
