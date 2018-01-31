package com.yinghai.twentyfour.common.service.impl;

import com.yinghai.twentyfour.common.dao.TfImClientMapper;
import com.yinghai.twentyfour.common.model.TfImClient;
import com.yinghai.twentyfour.common.service.TfImClientService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2017/10/30.
 */
public class TfImClientServiceImpl implements TfImClientService {

    @Autowired
    private TfImClientMapper tfImClientMapper;

    @Override
    public int createImClient(TfImClient tfImClient) {
        return tfImClientMapper.insertSelective(tfImClient);
    }
}
