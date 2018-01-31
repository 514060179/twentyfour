package com.yinghai.twentyfour.common.service;

import java.util.List;
import java.util.Map;

import com.yinghai.twentyfour.common.model.TfImgTmp;
import com.yinghai.twentyfour.common.util.Page;

/**
 * Created by Administrator on 2017/7/18.
 */
public interface TfImgTmpService {

    /**
     * 删除图片信息
     * @param userId
     * @return
     */
    int deleteByPrimaryKey(Integer imgtmpId);
    /**
     * 新增文章信息（所有字段）
     * @param record
     * @return
     */
    int insert(TfImgTmp record);
    /**
     * 新增图片信息（指定字段）
     * @param record
     * @return
     */
    int insertSelective(TfImgTmp record);
    /**
     * 通过ID查找图片信息
     * @param userId
     * @return
     */
    TfImgTmp selectByPrimaryKey(Integer imgtmpId);
    /**
     * 更新图片信息（指定字段）
     * @param userId
     * @return
     */
    int updateByPrimaryKeySelective(TfImgTmp record);
    /**
     * 更新图片信息（全部字段）
     * @param userId
     * @return
     */
    int updateByPrimaryKey(TfImgTmp record);
    Page<TfImgTmp>  getTfImgTmpRecord(int pageNumber,int pageStartSize,Map map);
    
    /**
     * 根據ID綁定keyId和used的變更
     */
    int updateKeyIdById(String[] ids,Integer keyId);
    
    List<TfImgTmp> getListByArticle(int articleId,int type);
}
