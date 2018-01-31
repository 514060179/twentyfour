package com.yinghai.twentyfour.app.service;

import com.yinghai.twentyfour.common.model.TfCollection;
import com.yinghai.twentyfour.common.model.TfCollectionHelper;
import com.yinghai.twentyfour.common.util.Page;

/**
 * 我的收藏
 * @author Administrator
 *
 */
public interface CollectionService {
	TfCollection selectById(Integer userId,Integer type,Integer id);
	/**
	 * 创建收藏
	 * @param userId
	 * @param type
	 * @param id
	 * @return
	 */
	int createCollection(Integer userId,Integer type,Integer id);
	/**
	 * 删除收藏
	 * @param collectionId
	 * @return
	 */
	//int deleteCollection(Integer collectionId,Integer userId);
	/**
	 * 分页查询我的收藏列表
	 * @param pageNum
	 * @param pageSize
	 * @param userId
	 * @return
	 */
	Page<TfCollectionHelper> getCollectionsByPage(Integer pageNum,Integer pageSize,Integer userId);

	/**
	 * 根据条件删除收藏
	 * @param c
	 * @return
	 */
	int deleteCollectionSelective(TfCollection c);
	
//	/**
//	 * 鏌ヨ璇ユ潯鏁版嵁鏄惁琚敤鎴锋敹钘�
//	 * @param userId
//	 * @param keyId
//	 * @param type
//	 * @return
//	 */
//	int selectCountByUserIdAndKeyIdAndType(Integer userId,Integer keyId,Integer type);
	/**
	 * 閫氳繃鐢ㄦ埛ID浠ュ強keyId鏌ヨ鏀惰棌
	 * @param userId
	 * @param keyId
	 * @param type
	 * @return
	 */
	TfCollection selectCountByUserIdAndKeyIdAndType(Integer userId,Integer keyId,Integer type);
	/**
	 * 根据collectionId查询详细收藏信息
	 * @param i
	 * @return
	 */
	TfCollectionHelper selectByCollectionId(int i);
	/**
	 *  根据条件查询详细收藏信息
	 * @param c
	 * @return
	 */
	TfCollectionHelper findBySelective(TfCollection c);

}
