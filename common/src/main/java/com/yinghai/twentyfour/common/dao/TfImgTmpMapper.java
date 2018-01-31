package com.yinghai.twentyfour.common.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yinghai.twentyfour.common.model.TfImgTmp;
import com.yinghai.twentyfour.common.util.Page;

public interface TfImgTmpMapper {
    int deleteByPrimaryKey(Integer tfImgTmpId);

    int insert(TfImgTmp record);

    int insertSelective(TfImgTmp record);

    TfImgTmp selectByPrimaryKey(Integer tfImgTmpId);

    int updateByPrimaryKeySelective(TfImgTmp record);

    int updateByPrimaryKey(TfImgTmp record);
    Page<TfImgTmp>  getTfImgTmpRecord(Map map);
    /**
     * 根據ID綁定keyId和used的變更
     */
    int updateKeyIdById(@Param("ids")String[] ids,@Param("keyId")Integer keyId);
    
    List<TfImgTmp> getListByArticle(@Param("keyId")int articleId,@Param("type")int type);
}