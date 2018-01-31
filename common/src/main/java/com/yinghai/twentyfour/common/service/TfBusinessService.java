package com.yinghai.twentyfour.common.service;

import com.yinghai.twentyfour.common.dao.TfBusinessMapper;
import com.yinghai.twentyfour.common.model.TfBusiness;
import com.yinghai.twentyfour.common.model.TfMaster;
import com.yinghai.twentyfour.common.model.TfOrder;
import com.yinghai.twentyfour.common.util.Page;

import java.util.List;

/**
 * Created by Administrator on 2017/10/23.
 */
public interface TfBusinessService {
    /**
     * 根据id查询
     * @param bBusinessId
     * @return
     */
    TfBusiness findById(Integer bBusinessId,Integer bMasterId);

    /**
     * 新增大师业务
     * @param tfBusiness
     * @return
     */
    int saveBusiness(TfBusiness tfBusiness);

    /**
     * 分页查询
     * @param pageNumber 页码
     * @param pageSize 每页条数
     * @param tfBusiness
     * @return
     */
    Page<TfBusiness> findByPage(Integer pageNumber, Integer pageSize,TfBusiness tfBusiness);

    /**
     * 更新
     * @param tfBusiness
     * @return
     */
    int updateBusiness(TfBusiness tfBusiness);

    /**
     * 删除业务
     * @param businessId
     * @return
     */
    int deleteBusiness(Integer businessId, TfMaster tfMaster);

    /**
     * 查询价格最低业务
     * @param businessId
     * @return
     */
    List<TfBusiness> findLowerBusiness(Integer businessId);
}
