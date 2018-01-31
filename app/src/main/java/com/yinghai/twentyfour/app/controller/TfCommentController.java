package com.yinghai.twentyfour.app.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yinghai.twentyfour.app.service.TfOrderTotalService;
import com.yinghai.twentyfour.common.model.TfArticle;
import com.yinghai.twentyfour.common.model.TfComment;
import com.yinghai.twentyfour.common.model.TfCommentHelper;
import com.yinghai.twentyfour.common.model.TfCommentWithBLOBs;
import com.yinghai.twentyfour.common.model.TfOrder;
import com.yinghai.twentyfour.common.model.TfOrderTotal;
import com.yinghai.twentyfour.common.model.TfOrderTotalHelper;
import com.yinghai.twentyfour.common.model.TfProduct;
import com.yinghai.twentyfour.common.model.TfUser;
import com.yinghai.twentyfour.common.service.TfArticleService;
import com.yinghai.twentyfour.common.service.TfCommentService;
import com.yinghai.twentyfour.common.service.TfOrderService;
import com.yinghai.twentyfour.common.service.TfProductService;
import com.yinghai.twentyfour.common.service.TfUserService;
import com.yinghai.twentyfour.common.util.JsonDateValueProcessor;
import com.yinghai.twentyfour.common.util.Page;
import com.yinghai.twentyfour.common.util.ResponseVo;
import com.yinghai.twentyfour.common.util.StringUtil;
import com.yinghai.twentyfour.common.util.TransformUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
/**
 * 评论接口
 * @author Administrator
 * 
 */
@Controller
@RequestMapping("/app/comment")
public class TfCommentController {
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private TfUserService tfUserService;
	@Autowired
	private TfArticleService tfArticleService;
	@Autowired
	private TfProductService tfProductService;
	@Autowired
	private TfCommentService tfCommentService;
	@Autowired
	private TfOrderService tfOrderService;
	/**
	 * 创建评论
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/create",method=RequestMethod.POST)
	public void createComment(HttpServletRequest request,HttpServletResponse response){
		logger.info("======创建评论======");
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//获取参数：评论人id,评论类型，评论内容,文章或商品id
		String uid = request.getParameter("userId");//评论人id
		if(StringUtil.empty(uid)){
			logger.error("======TfCommentController/createComment======userId为空");
			ResponseVo.send101Code(response, "userId为空");
			return;
		}
		String t = request.getParameter("type");//评论类型
		if(StringUtil.empty(t)){
			logger.error("======TfCommentController/createComment======type为空");
			ResponseVo.send101Code(response, "type为空");
			return;
		}
		String id = request.getParameter("id");//商品id或文章id
		if(StringUtil.empty(id)){
			logger.error("======TfCommentController/createComment======id为空");
			ResponseVo.send101Code(response, "id为空");
			return;
		}
		String text = request.getParameter("content");//评论内容
		if(StringUtil.empty(text)){
			logger.error("======TfCommentController/createComment======text为空");
			ResponseVo.send101Code(response, "text为空");
			return;
		}
		Integer userId = TransformUtils.toInteger(uid);
		if(userId<0){
			if(userId==-1){
				ResponseVo.send800Code(response, "当前为游客，请登录");
				return;
			}
			logger.error("======TfCommentController/createComment======userId格式错误");
			ResponseVo.send104Code(response, "userId格式错误");
			return;
		}

		Integer type = TransformUtils.toInteger(t);
		if(type!=1&&type!=2){
			logger.error("======TfCommentController/createComment======type错误");
			ResponseVo.send114Code(response, "type错误");
			return;
		}
		Integer cId = TransformUtils.toInteger(id);
		if(cId<0){
			logger.error("======TfCommentController/createComment======id格式错误");
			ResponseVo.send104Code(response, "id格式错误");
			return;
		}
		//评论人是否存在，对应商品/文章是否存在
		TfUser user = tfUserService.findUserById(userId);
		if(user==null){
			logger.error("======TfCommentController/createComment======评论人不存在");
			ResponseVo.send102Code(response, "评论人不存在");
			return;
		}
		//判断该用户是否买过该商品
		if(type==2){//商品评论
			//查询完成的商品订单
			TfOrder o = new TfOrder();
			o.setoUserId(userId);
			o.setoStatus(TfOrder.orderStatusHaveDone);
			List<TfOrder> list= tfOrderService.findOrderByProductId(o,cId);
			if(list==null||list.size()<1){
				//未购买商品不能评论
				ResponseVo.send801Code(response, "未购买商品，不能发表评论");
				return;
			}
		}
		
		//创建评论
		TfComment comment = new TfComment();
		
		if(type==1){//文章评论
			TfArticle a = tfArticleService.selectByPrimaryKey(cId);
			if(a==null){
				logger.error("======TfCommentController/createComment======该文章不存在");
				ResponseVo.send102Code(response, "该文章不存在");
				return;
			}
			comment.setCtArticleId(cId);
		}else{
			TfProduct p = tfProductService.findProductByProuductId(cId);
			if(p==null||p.getpOffline()||p.getpDelete()){
				logger.error("======TfCommentController/createComment======该商品不存在或已下架");
				ResponseVo.send102Code(response, "该商品不存在或已下架");
				return;
			}
			comment.setCtProductId(cId);
		}
		comment.setCtType(type);
		comment.setCtDiscussantId(userId);
		comment.setCtContent(text);
		comment.setCtCreateTime(new Date());
		//新建评论
		int i = tfCommentService.createComment(comment);
		if(i!=1){
			logger.error("======TfCommentController/createComment======数据出错，新增评论失败");
			ResponseVo.send106Code(response, "数据出错，新增评论失败");
			return;
		}
		Integer commentId = comment.getCommentId();
		TfComment c = new TfComment();
		c.setCommentId(commentId);
		TfCommentWithBLOBs co = tfCommentService.findDetailComment(c);
		JSONObject obj = new JSONObject();
		JsonConfig config = new JsonConfig();  
        JsonDateValueProcessor jsonValueProcessor = new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss");  
        config.registerJsonValueProcessor(Date.class, jsonValueProcessor);
        JSONArray cjson = JSONArray.fromObject(co, config);
        obj.put("comments", cjson);
		ResponseVo.send1Code(response, "success", obj);
	}
	/**
	 * 删除评论，评论者自己删除评论
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public void delete(HttpServletRequest request,HttpServletResponse response){
		logger.info("======删除评论======");
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//获取参数：评论者id，文章或商品id，类型，评论id
		String uId = request.getParameter("userId");
		String cId = request.getParameter("commentId");
		if(StringUtil.empty(uId)){
			ResponseVo.send101Code(response, "userId为空");
			return;
		}
		if(StringUtil.empty(cId)){
			ResponseVo.send101Code(response, "commentId为空");
			return;
		}
		Integer userId = TransformUtils.toInteger(uId);
		Integer commentId = TransformUtils.toInteger(cId);
		if(userId<0){
			if(userId==-1){
				ResponseVo.send800Code(response, "当前为游客，请登录");
				return;
			}
			ResponseVo.send104Code(response, "userId格式错误");
			return;
		}
		if(commentId<0){
			ResponseVo.send104Code(response, "commentId格式错误");
			return;
		}
		//判断用户和评论是否存在
		TfUser user = tfUserService.findUserById(userId);
		if(user==null){
			ResponseVo.send102Code(response, "用户不存在");
			return;
		}
		TfComment c = new TfComment();
		c.setCommentId(commentId);
		c.setCtDiscussantId(userId);
		TfCommentWithBLOBs com = tfCommentService.findDetailComment(c);
		if(com==null){
			ResponseVo.send102Code(response, "该评论不存在");
			return;
		}
		int i = tfCommentService.deleteComment(c);
		if(i!=1){
			ResponseVo.send106Code(response, "数据出错，删除失败");
			return;
		}
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		JSONArray commentJson = JSONArray.fromObject(com, config);
		JSONObject obj = new JSONObject();
		obj.put("comments", commentJson);
		ResponseVo.send1Code(response, "success", obj);
	}
	/**
	 * 分页查询评论列表
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/list",method = RequestMethod.POST)
	public void getListByPage(HttpServletRequest request,HttpServletResponse response){
		logger.info("======获取评论列表======");
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//获取参数：页数、条数、类型、文章或商品id
		String num = request.getParameter("page");
		String size = request.getParameter("pageSize");
		Integer pageNum = (num!=null&&TransformUtils.toInteger(num)>0)?TransformUtils.toInteger(num):1;
		Integer pageSize = (size!=null&&TransformUtils.toInteger(size)>0)?TransformUtils.toInteger(size):10;
		String t = request.getParameter("type");
		String aId = request.getParameter("id");
		if(StringUtil.empty(t)){
			ResponseVo.send101Code(response, "type为空");
			return;
		}
		if(StringUtil.empty(aId)){
			ResponseVo.send101Code(response, "id为空");
			return;
		}
		Integer type = TransformUtils.toInteger(t);
		Integer id = TransformUtils.toInteger(aId);
		if(type!=1&&type!=2){
			ResponseVo.send114Code(response, "type错误");
			return;
		}
		if(id<0){
			ResponseVo.send104Code(response, "id格式错误");
			return;
		}
		TfComment c = new TfComment();
		c.setCtInvisible(false);
		c.setCtType(type);
		if(type==1){
			c.setCtArticleId(id);
		}else{
			c.setCtProductId(id);
		}
		Page<TfCommentWithBLOBs> page = tfCommentService.findCommentByPage(pageNum, pageSize, c);
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		JSONArray commentJson = JSONArray.fromObject(page, config);
		JSONObject obj = new JSONObject();
		obj.put("comments", commentJson);
		ResponseVo.send1Code(response, "success", obj);
	}
	
}
