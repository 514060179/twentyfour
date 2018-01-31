package com.yinghai.twentyfour.app.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yinghai.twentyfour.app.service.CollectionService;
import com.yinghai.twentyfour.common.model.TfCollection;
import com.yinghai.twentyfour.common.model.TfCollectionHelper;
import com.yinghai.twentyfour.common.util.JsonDateValueProcessor;
import com.yinghai.twentyfour.common.util.Page;
import com.yinghai.twentyfour.common.util.ResponseVo;
import com.yinghai.twentyfour.common.util.StringUtil;
import com.yinghai.twentyfour.common.util.TransformUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 我的收藏
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/app/collection")
public class TfCollectionController {
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private CollectionService tfCollectionService;
	/**
	 * 创建收藏记录
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/create",method = RequestMethod.POST)
	public void createCollection(HttpServletRequest request,HttpServletResponse response){
		logger.info("======创建收藏记录======");
		try {
            request.setCharacterEncoding("utf-8");
		}catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
		//获取参数：用户id，收藏类型，对应类型id
		String uid = request.getParameter("userId");
		if(StringUtil.empty(uid)){
			ResponseVo.send101Code(response, "userId为空");
			return;
		}
		Integer userId = TransformUtils.toInteger(uid);
		if(userId<0){
			if(userId==-1){
				ResponseVo.send800Code(response, "当前为游客，请登录");
				return;
			}
			ResponseVo.send104Code(response, "userId格式错误");
			return;
		}
		String t = request.getParameter("type");
		if(StringUtil.empty(t)){
			ResponseVo.send101Code(response, "type为空");
			return;
		}
		Integer type = TransformUtils.toInteger(t);
		if(type!=1&&type!=2&&type!=3){
			ResponseVo.send114Code(response, "type错误");
			return;
		}
		String aid = request.getParameter("id");
		if(StringUtil.empty(aid)){
			ResponseVo.send101Code(response, "id为空");
			return;
		}
		Integer id = TransformUtils.toInteger(aid);
		if(id<0){
			ResponseVo.send104Code(response, "id格式错误");
			return;
		}
		//判断是否收藏
		TfCollection c = tfCollectionService.selectById(userId, type, id);
		if(c!=null){
			ResponseVo.send1021Code(response, "已收藏，不能重复收藏");
			return;
		}
		//创建收藏记录
		int	i = tfCollectionService.createCollection(userId, type, id);//返回的id为collectionId
		if(i<1){
			ResponseVo.send106Code(response, "数据出错，收藏失败");
			return;
		}
		//根据collectionId查询详细收藏信息
		TfCollectionHelper c1 = tfCollectionService.selectByCollectionId(i);
		JSONObject obj = new JSONObject();
		JsonConfig config = new JsonConfig();  
        JsonDateValueProcessor jsonValueProcessor = new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss");  
        config.registerJsonValueProcessor(Date.class, jsonValueProcessor);
        JSONArray col = JSONArray.fromObject(c1, config);
        obj.put("collection", col);
		ResponseVo.send1Code(response, "success", obj);
	}
	/**
	 * 删除收藏记录
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public void deleteCollection(HttpServletRequest request,HttpServletResponse response){
		logger.info("======删除收藏记录======");
		try {
            request.setCharacterEncoding("utf-8");
		}catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
		//获取收藏记录
		Integer userId = TransformUtils.toInteger(request.getParameter("userId"));
		Integer collectionId = TransformUtils.toInteger(request.getParameter("collectionId"));
		Integer type = TransformUtils.toInteger(request.getParameter("type"));
		Integer keyId = TransformUtils.toInteger(request.getParameter("keyId"));
		if(userId==null){
			ResponseVo.send101Code(response, "userId为空");
			return;
		}else{
			if(userId<0){
				if(userId==-1){
					ResponseVo.send800Code(response, "当前为游客，请登录");
					return;
				}
				ResponseVo.send104Code(response, "userId格式错误");
				return;
			}
		}
		TfCollection c = new TfCollection();
		c.setCnUserId(userId);
		TfCollectionHelper col = null;
		if(collectionId!=null){
			if(collectionId<0){
				ResponseVo.send104Code(response, "collectionId格式错误");
				return;
			}
			c.setCollectionId(collectionId);
			col = tfCollectionService.findBySelective(c);

		}else{
			if(type==null){
				ResponseVo.send101Code(response, "type为空");
				return;
			}else{
				if(type!=1&&type!=2&&type!=3){
					ResponseVo.send104Code(response, "type格式错误");
					return;
				}
			}
			if(keyId==null){
				ResponseVo.send101Code(response, "keyId为空");
				return;
			}else{
				if(keyId<0){
					ResponseVo.send104Code(response, "keyId格式错误");
					return;
				}
			}
			c.setCnType(type);
			if(type==1){
				c.setCnMasterId(keyId);
			}else if(type==2){
				c.setCnArticleId(keyId);
			}else{
				c.setCnProductId(keyId);
			}
			col =  tfCollectionService.findBySelective(c);
		}
		if(col==null){
			ResponseVo.send1023Code(response, "已经取消收藏，不要重复取消");
			return;
		}
		int j = tfCollectionService.deleteCollectionSelective(c);
		if(j!=1){
			ResponseVo.send106Code(response, "数据出错，删除失败");
			return;
		}
		JSONObject obj = new JSONObject();
		JsonConfig config = new JsonConfig();  
        JsonDateValueProcessor jsonValueProcessor = new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss");  
        config.registerJsonValueProcessor(Date.class, jsonValueProcessor);
        JSONArray json = JSONArray.fromObject(col, config);
        obj.put("collection", json);
		ResponseVo.send1Code(response, "success", obj);
	}
	
	/**
	 * 分页查询收藏列表
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/list",method=RequestMethod.POST)
	public void getCollectionList(HttpServletRequest request,HttpServletResponse response){
		logger.info("======分页查询收藏记录======");
		try {
            request.setCharacterEncoding("utf-8");
		}catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
		String num = request.getParameter("page");
		Integer pageNum = (TransformUtils.toInteger(num)==null||TransformUtils.toInteger(num)<1)?1:TransformUtils.toInteger(num);
		String size = request.getParameter("pageSize");
		Integer pageSize = (TransformUtils.toInteger(size)==null||TransformUtils.toInteger(size)<1)?10:TransformUtils.toInteger(size);
		String uid = request.getParameter("userId");
		if(StringUtil.empty(uid)){
			ResponseVo.send101Code(response, "userId为空");
			return;
		}
		if(TransformUtils.toInteger(uid)<0){
			if(TransformUtils.toInteger(uid)==-1){
				ResponseVo.send800Code(response, "当前为游客，请登录");
				return;
			}
			ResponseVo.send104Code(response, "userId格式错误");
			return;
		}
		Integer start = (pageNum-1)*pageSize;
		Page<TfCollectionHelper> collections = tfCollectionService.getCollectionsByPage(start, pageSize, TransformUtils.toInteger(uid));
		JSONObject obj = new JSONObject();
		JsonConfig config = new JsonConfig();  
        JsonDateValueProcessor jsonValueProcessor = new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss");  
        config.registerJsonValueProcessor(Date.class, jsonValueProcessor);
        JSONArray arr = JSONArray.fromObject(collections, config);
        obj.put("collection", arr);
        ResponseVo.send1Code(response, "success", obj);
	}
	
	
}
