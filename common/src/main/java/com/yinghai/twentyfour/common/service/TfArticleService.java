package com.yinghai.twentyfour.common.service;

import java.util.List;
import java.util.Map;

import com.yinghai.twentyfour.common.model.TfArticle;
import com.yinghai.twentyfour.common.util.Page;

/**
 * Created by Administrator on 2017/7/18.
 */
public interface TfArticleService {

    /**
     * 删除文章信息
     * @param userId
     * @return
     */
    int deleteByPrimaryKey(Integer articleId);
    /**
     * 新增文章信息（所有字段）
     * @param record
     * @return
     */
    int insert(TfArticle record);
    /**
     * 新增文章信息（指定字段）
     * @param record
     * @return
     */
    int insertSelective(TfArticle record);
    /**
     * 通过ID查找文章信息
     * @param userId
     * @return
     */
    TfArticle selectByPrimaryKey(Integer articleId);
    /**
     * 更新文章信息（指定字段）
     * @param userId
     * @return
     */
    int updateByPrimaryKeySelective(TfArticle record);
    /**
     * 更新文章信息（全部字段）
     * @param userId
     * @return
     */
    int updateByPrimaryKey(TfArticle record);
    /**
     * 后台获取列表数据
     * @param pageNumber
     * @param pageStartSize
     * @param tfArticle
     * @return
     */
    Page<TfArticle>  getTfArticleRecord(int pageNumber,int pageStartSize,TfArticle tfArticle);
    /**
     * APP获取带图片列表的文章数据
     * @param startNumber
     * @param pageStartSize
     * @param tfArticle
     * @return
     */
    List<TfArticle>  getTfArticleAndImgRecord(int startNumber,int pageStartSize,TfArticle tfArticle);

}
