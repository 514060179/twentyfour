package com.yinghai.twentyfour.common.service.impl;

import com.yinghai.twentyfour.common.dao.TfBusinessMapper;
import com.yinghai.twentyfour.common.dao.TfMasterMapper;
import com.yinghai.twentyfour.common.model.TfBusiness;
import com.yinghai.twentyfour.common.model.TfMaster;
import com.yinghai.twentyfour.common.service.TfBusinessService;
import com.yinghai.twentyfour.common.util.Page;
import com.yinghai.twentyfour.common.util.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2017/10/23.
 */
public class TfBusinessServiceImpl implements TfBusinessService {

    @Autowired
    private TfBusinessMapper tfBusinessMapper;
    @Autowired
    private TfMasterMapper tfMasterMapper;

    @Override
    public TfBusiness findById(Integer bBusinessId,Integer bMasterId) {
        TfBusiness tfBusiness = new TfBusiness();
        tfBusiness.setBusinessId(bBusinessId);
        tfBusiness.setbMasterId(bMasterId);
        List<TfBusiness> list = tfBusinessMapper.findOneByCondition(tfBusiness).getResult();
        if (list!=null&&list.size()!=0){
            return list.get(0);
        }else{
            return null;
        }
    }

    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public int saveBusiness(TfBusiness tfBusiness) {
        TfMaster tfMaster = new TfMaster();
        tfMaster.setMasterId(tfBusiness.getbMasterId());
        tfMaster.setmBargain(tfBusiness.getbPrice());
        int i = tfMasterMapper.updateBargain(tfMaster);
        i += tfBusinessMapper.insertSelective(tfBusiness);
        return i;
    }

    @Override
    public Page<TfBusiness> findByPage(Integer pageNumber, Integer pageStartSize, TfBusiness tfBusiness) {
        PageHelper.startPage(pageNumber,pageStartSize);
        return tfBusinessMapper.findOneByCondition(tfBusiness);
    }

    @Override
    public int updateBusiness(TfBusiness tfBusiness) {
        return tfBusinessMapper.updateByPrimaryKeySelective(tfBusiness);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public int deleteBusiness(Integer businessId,TfMaster tfMaster) {
        int i = tfBusinessMapper.deleteByPrimaryKey(businessId);
        i += tfMasterMapper.updateByPrimaryKeySelective(tfMaster);
        return i;
    }

    @Override
    public List<TfBusiness> findLowerBusiness(Integer businessId) {
        return tfBusinessMapper.findLowerBusiness(businessId);
    }
}
