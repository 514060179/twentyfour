package com.yinghai.twentyfour.backstage.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yinghai.twentyfour.common.model.TfArticle;
import com.yinghai.twentyfour.common.model.TfComment;
import com.yinghai.twentyfour.common.model.TfCommentHelper;
import com.yinghai.twentyfour.common.model.TfProduct;
import com.yinghai.twentyfour.common.model.TfUser;
import com.yinghai.twentyfour.common.service.TfArticleService;
import com.yinghai.twentyfour.common.service.TfCommentService;
import com.yinghai.twentyfour.common.service.TfProductService;
import com.yinghai.twentyfour.common.service.TfUserService;
import com.yinghai.twentyfour.common.util.Page;
import com.yinghai.twentyfour.common.util.ResponseVo;
import com.yinghai.twentyfour.common.util.StringUtil;
import com.yinghai.twentyfour.common.util.TransformUtils;

import net.sf.json.JSONObject;

/**
 * 后台管理——评论管理
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/admin/comment")
public class BackTfCommentController {
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private TfCommentService tfCommentService;
	@Autowired
	private TfUserService tfUserService;
	@Autowired
	private TfProductService tfProductService;
	@Autowired
	private TfArticleService tfArticleService;
	/**
	 * 评论管理列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/list")
	public String getCommentList(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		logger.info("BackTfCommentController/getCommentList======后台管理评论列表======");
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//获取参数：查询条件(评论ID、评论人ID、商品ID、文章ID)，页数、条数
		String num = request.getParameter("page");
		String size = request.getParameter("pageSize");
		Integer pageNum = (num!=null&&TransformUtils.toInteger(num)>0)?TransformUtils.toInteger(num):1;
		Integer pageSize = (size!=null&&TransformUtils.toInteger(size)>0)?TransformUtils.toInteger(size):10;
		TfComment c = new TfComment();
		Integer commentId = TransformUtils.toInteger(request.getParameter("commentId"));
		Integer discussantId = TransformUtils.toInteger(request.getParameter("discussantId"));
		Integer articleId = TransformUtils.toInteger(request.getParameter("articleId"));
		Integer productId = TransformUtils.toInteger(request.getParameter("productId"));
		Integer invisible = TransformUtils.toInteger(request.getParameter("invisible"));
		Integer type = TransformUtils.toInteger(request.getParameter("type"));
		String content = request.getParameter("content");
		if(commentId!=null&&commentId>0){
			c.setCommentId(commentId);
		}
		if(discussantId!=null&&discussantId>0){
			c.setCtDiscussantId(discussantId);
		}
		if(articleId!=null&&articleId>0){
			c.setCtArticleId(articleId);
		}
		if(productId!=null&&productId>0){
			c.setCtProductId(productId);;
		}
		//System.out.println("****"+invisible+"****");
		if(invisible!=null&&(invisible==0||invisible==1)){
			if(invisible==0){
				c.setCtInvisible(false);
			}else{
				c.setCtInvisible(true);
			}
		}else{
			
		}
		if(type!=null&&(type==1||type==2)){
			c.setCtType(type);
		}
		if(content!=null){
			c.setCtContent(content);
		}
		Page<TfComment> page = tfCommentService.findCommentByPageBack(pageNum, pageSize, c);
		model.addAttribute("comment", c);
		model.addAttribute("page", page);
		model.addAttribute("pageNo", page.getPageNum());
		model.addAttribute("pageSize", page.getPageSize());
		model.addAttribute("pageCount", page.getPages());
		model.addAttribute("recordCount",page.getTotal());
		return "comment/list";
	}
	
	/**
	 * 删除评论
	 * @param request
	 * @param response
	 */
	@RequestMapping("/delete")
	public void deleteComment(HttpServletRequest request,HttpServletResponse response){
		logger.info("BackTfCommentController/deleteComment======后台管理删除评论======");
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//获取参数：评论id
		String cid = request.getParameter("commentId");
		if(StringUtil.empty(cid)){
			logger.error("======BackTfCommentController/deleteComment======commentId为空");
			ResponseVo.send101Code(response, "commentId为空");
			return;
		}
		Integer commentId = TransformUtils.toInteger(cid);
		if(commentId<0){
			logger.error("======BackTfCommentController/deleteComment======commentId格式错误");
			ResponseVo.send104Code(response, "commentId格式错误");
			return;
		}
		int i = tfCommentService.deleteCommentById(commentId);
		if(i!=1){
			logger.error("======BackTfCommentController/deleteComment======数据出错，commentId删除失败");
			ResponseVo.send106Code(response, "数据出错，commentId删除失败");
			return;
		}
		ResponseVo.send1Code(response, "success", new JSONObject());
	}
	/**
	 * 编辑或新增评论
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/edit")
	public String editComment(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		logger.info("BackTfCommentController/editComment======后台管理进入新增或编辑评论======");
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//获取参数
		Integer commentId= TransformUtils.toInteger(request.getParameter("commentId"));
		String look = request.getParameter("look");
		//System.out.println("*********"+commentId);
		//System.out.println("*********"+look);
		if(commentId!=null&&commentId>0){
			TfComment comment = null;
			if(StringUtil.empty(look)){
				comment  = tfCommentService.findCommentById(commentId);
				model.addAttribute("comment", comment);
			}else{
				TfCommentHelper comments = tfCommentService.findCommentDetail(commentId);
				model.addAttribute("look", look);
				model.addAttribute("comment", comments);
			}
			//System.out.println("*********"+comment);
			if(!StringUtil.empty(look)){
			}
		}
		return "comment/edit";
	}
	/**
	 * 保存或更新评论
	 * @param request
	 * @param response
	 */
	@RequestMapping("/save")
	public void updateOrCreateComment(HttpServletRequest request,HttpServletResponse response){
		logger.info("BackTfCommentController/updateOrCreateComment======后台管理新增或更新评论======");
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		JSONObject obj = new JSONObject();
		//获取参数：act(新增或更新),commentId、invisible/type、articleId、productId、discussantId、content、invisible
		String act = request.getParameter("act");
		if(act==null||(!"add".equals(act)&&!"upd".equals(act))){
			ResponseVo.send101Code(response, "请求类型出错");
			return;
		}
		if("add".equals(act)){//新增
			TfComment c = new TfComment();
			Integer type = TransformUtils.toInteger(request.getParameter("type"));
			if(type==null||(type!=1&&type!=2)){
				ResponseVo.send114Code(response, "评论类型错误");
				return;
			}
			c.setCtType(type);
			if(type==1){
				Integer articleId = TransformUtils.toInteger(request.getParameter("articleId"));
				if(articleId==null||articleId<0){
					logger.error("BackTfCommentController/updateOrCreateComment===="+articleId);
					ResponseVo.send101Code(response, "文章ID错误");
					return;
				}
				c.setCtArticleId(articleId);
			}else{
				Integer productId = TransformUtils.toInteger(request.getParameter("productId"));
				if(productId==null||productId<0){
					logger.error("BackTfCommentController/updateOrCreateComment===="+productId);
					ResponseVo.send101Code(response, "文章ID错误");
					return;
				}
				c.setCtProductId(productId);
			}
			Integer discussantId = TransformUtils.toInteger(request.getParameter("discussantId"));
			if(discussantId==null||discussantId<0){
				logger.error("BackTfCommentController/updateOrCreateComment===="+discussantId);
				ResponseVo.send101Code(response, "评论者ID错误");
				return;
			}
			c.setCtDiscussantId(discussantId);
			String content = request.getParameter("content");
			if(content==null){
				ResponseVo.send101Code(response, "评论内容为空");
				return;
			}
			c.setCtContent(content);
			Integer invisible = TransformUtils.toInteger(request.getParameter("invisible"));
			if(invisible==null||(invisible!=0&&invisible!=1)){
				logger.error("BackTfCommentController/updateOrCreateComment===="+invisible);
				ResponseVo.send101Code(response, "invisible错误");
				return;
			}
			if(invisible==1){
				c.setCtInvisible(true);
			}else{
				c.setCtInvisible(false);
			}
			c.setCtCreateTime(new Date());
			//判断对应文章和商品是否存在
			if(type==1){
				TfArticle a = tfArticleService.selectByPrimaryKey(c.getCtArticleId());
				if(a==null){
					ResponseVo.send102Code(response, "该文章不存在");
					return;
				}
			}else{
				TfProduct p = tfProductService.findProductByProuductId(c.getCtProductId());
				if(p==null){
					ResponseVo.send102Code(response, "该商品不存在");
					return;
				}
			}
			//判断评论者是否存在
			TfUser u = tfUserService.findUserById(c.getCtDiscussantId());
			if(u==null){
				ResponseVo.send102Code(response, "评论者不存在");
				return;
			}
			//新增评论
			int i = tfCommentService.createComment(c);
			if(i!=1){
				ResponseVo.send106Code(response, "数据出错，新增评论失败");
				return;
			}
			ResponseVo.send1Code(response, "新增评论成功", new JSONObject());
		}else{//更新
			TfComment comment = new TfComment();
			Integer commentId = TransformUtils.toInteger(request.getParameter("commentId"));
			if(commentId==null||commentId<0){
				ResponseVo.send101Code(response, "评论ID错误");
				return;
			}
			comment.setCommentId(commentId);
			Integer invisible = TransformUtils.toInteger(request.getParameter("invisible"));
			if(invisible==null||(invisible!=0&&invisible!=1)){
				logger.error("BackTfCommentController/updateOrCreateComment===="+invisible);
				ResponseVo.send101Code(response, "invisible错误");
				return;
			}
			if(invisible==1){
				comment.setCtInvisible(true);
			}else{
				comment.setCtInvisible(false);
			}
			//更新评论
			int j = tfCommentService.updateComment(comment);
			if(j!=1){
				ResponseVo.send106Code(response, "数据出错，更新评论失败");
				return;
			}
			ResponseVo.send1Code(response, "更新评论成功",new JSONObject());
		}
	}
	
}
