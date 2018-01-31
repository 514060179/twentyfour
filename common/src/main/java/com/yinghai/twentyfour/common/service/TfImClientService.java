package com.yinghai.twentyfour.common.service;

import com.yinghai.twentyfour.common.model.TfImClient;

/**
 * Created by Administrator on 2017/10/30.
 */
public interface TfImClientService {

    /**
     * 创建一个账号
     * @param tfImClient
     * @return
     */
    int createImClient(TfImClient tfImClient);
}
