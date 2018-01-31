package com.yinghai.twentyfour.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yinghai.twentyfour.common.model.TfArticle;
import com.yinghai.twentyfour.common.util.Page;

public interface TfArticleMapper {
    int deleteByPrimaryKey(Integer articleId);

    int insert(TfArticle record);

    int insertSelective(TfArticle record);

    TfArticle selectByPrimaryKey(Integer articleId);

    int updateByPrimaryKeySelective(TfArticle record);
    int updateByPrimaryKeyWithBLOBs(TfArticle record);

    int updateByPrimaryKey(TfArticle record);
    
    Page<TfArticle>  getTfArticleRecord(TfArticle tfArticle);
    
    List<TfArticle> getTfArticleAndImgRecord(@Param("tfArticle")TfArticle record,@Param("startNumber")Integer startNumber, @Param("pageStartSize")Integer pageStartSize);
}