package com.yinghai.twentyfour.common.service;

import com.yinghai.twentyfour.common.model.TfComment;
import com.yinghai.twentyfour.common.model.TfCommentHelper;
import com.yinghai.twentyfour.common.model.TfCommentWithBLOBs;
import com.yinghai.twentyfour.common.util.Page;

/**
 * 评论管理业务层接口
 * @author Administrator
 *
 */
public interface TfCommentService {
	/**
	 * 新建评论
	 * @param c
	 * @return
	 */
	public int createComment(TfComment c);
	/**
	 * 删除评论
	 * @param commentId
	 * @return
	 */
	public int deleteCommentById(Integer commentId);
	/**
	 * 根据条件删除评论
	 * @param c
	 * @return
	 */
	public int deleteComment(TfComment c);
	/**
	 * 分页查询评论列表
	 * @param pageNuM
	 * @param pageSize
	 * @param c
	 * @return
	 */
	public Page<TfCommentWithBLOBs> findCommentByPage(Integer pageNum,Integer pageSize,TfComment c);
	/**
	 * 后台查询评论列表
	 * @param pageNum
	 * @param pageSize
	 * @param c
	 * @return
	 */
	public Page<TfComment> findCommentByPageBack(Integer pageNum,Integer pageSize,TfComment c);
	/**
	 * 根据commentId查询评论
	 * @param commentId
	 * @return
	 */
	public TfComment findCommentById(Integer commentId);
	
	/**
	 * 更新评论
	 * @param c
	 * @return
	 */
	public int updateComment(TfComment c);
	/**
	 *  查询评论详情
	 * @param commentId
	 * @return
	 */
	public TfCommentHelper findCommentDetail(Integer commentId);
	/**
	 * 根据条件查询评论详情
	 * @param comment
	 * @return
	 */
	public TfCommentWithBLOBs findDetailComment(TfComment comment);

}
