package com.yinghai.twentyfour.common.dao;

import org.apache.ibatis.annotations.Param;

import com.yinghai.twentyfour.common.model.TfCollection;
import com.yinghai.twentyfour.common.model.TfCollectionHelper;
import com.yinghai.twentyfour.common.util.Page;

public interface TfCollectionMapper {
    int deleteByPrimaryKey(Integer collectionId);

    int insert(TfCollection record);

    int insertSelective(TfCollection record);

    TfCollection selectByPrimaryKey(Integer collectionId);
    TfCollection selectCountByUserIdAndKeyIdAndType(@Param("userId")Integer userId,@Param("keyId")Integer keyId,@Param("type")Integer type);

    int updateByPrimaryKeySelective(TfCollection record);

    int updateByPrimaryKey(TfCollection record);
    
    Page<TfCollectionHelper> selectByUserId(@Param("userId")Integer userId,@Param("start")Integer start,@Param("end")Integer end);

	TfCollection selectByCollection(TfCollection c);

	//int deleteByUser(@Param("collectionId")Integer collectionId, @Param("userId")Integer userId);

	int deleteBySelective(TfCollection c);

	TfCollectionHelper selectByCollectionId(int collectionId);

	TfCollectionHelper selectDetailCollection(TfCollection c);
}