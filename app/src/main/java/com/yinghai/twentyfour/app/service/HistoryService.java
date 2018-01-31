package com.yinghai.twentyfour.app.service;

import java.util.List;

import com.yinghai.twentyfour.common.model.TfHistory;
import com.yinghai.twentyfour.common.model.TfHistoryHelper;
import com.yinghai.twentyfour.common.util.Page;

/**
 * 历史记录业务层
 * @author Administrator
 *
 */
public interface HistoryService {
	/**
	 * 根据用户id分页查询历史记录列表
	 * @param userId
	 * @return
	 */
	public Page<TfHistoryHelper> getHistorysByPage(Integer userId,Integer pageNum,Integer pageSize);
	/**
	 * 根据用户和查看类型及对应Id查询历史记录
	 * @param userId	
	 * @param type	类型：1大师2文章3商品
	 * @param id	masteId或articleId或productId
	 * @return
	 */
	public List<TfHistory> findHistoryByUserAndType(Integer userId,Integer type,Integer id);
	/**
	 *  新增历史记录
	 * @param h
	 * @return
	 */
	public int addHistory(TfHistory h);
	/**
	 * 删除历史记录
	 * @param historyId
	 * @return
	 */
	public int deleteHistoryById(Integer historyId);
	/**
	 * 更新历史记录
	 * @param h
	 * @return
	 */
	public int updateHistory(TfHistory h);
}
