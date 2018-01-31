package com.yinghai.twentyfour.common.dao;

import com.yinghai.twentyfour.common.model.TfMasterWallet;
import org.apache.ibatis.annotations.Param;

public interface TfMasterWalletMapper {
    int deleteByPrimaryKey(Integer walletId);

    int insert(TfMasterWallet record);

    int insertSelective(TfMasterWallet record);

    TfMasterWallet selectByPrimaryKey(Integer walletId);

    int updateByPrimaryKeySelective(TfMasterWallet record);

    int updateByPrimaryKey(TfMasterWallet record);

    TfMasterWallet findWalletByMasterId(Integer masterId);

    int updateMasterBalance(TfMasterWallet record);
}