package com.yinghai.twentyfour.common.service;

import com.yinghai.twentyfour.common.model.TfMasterWallet;

/**
 * Created by Administrator on 2017/10/27.
 */
public interface TfMasterWalletService {

    /**
     * 获取大师钱包
     * @param masterId
     * @return
     */
    TfMasterWallet findWalletByMasterId(Integer masterId);
}
