package com.yinghai.twentyfour.common.service;

import java.util.List;
import java.util.Map;

import com.yinghai.twentyfour.common.model.TfProduct;
import com.yinghai.twentyfour.common.util.Page;

/**
 * 商品业务层接口
 * @author Administrator
 *
 */
public interface TfProductService {
	/**
	 * 根据商品id查询商品信息
	 * @param productId
	 * @return
	 */
	TfProduct findProductByProuductId(Integer productId);
	int deleteByPrimaryKey(Integer userId);
	int insert(TfProduct record);
	int insertSelective(TfProduct record);
	TfProduct selectByPrimaryKey(Integer userId);
	int updateByPrimaryKeySelective(TfProduct record);
	int updateByPrimaryKey(TfProduct record);
	List<TfProduct> getTfProductAndImgRecord(int startNumber, int pageStartSize, TfProduct tfProduct);
	Page<TfProduct> getTfProductRecord(int pageNumber, int pageStartSize, TfProduct tfProduct);
}
