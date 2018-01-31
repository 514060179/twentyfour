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

import com.yinghai.twentyfour.app.service.SubscribeService;
import com.yinghai.twentyfour.common.model.TfMaster;
import com.yinghai.twentyfour.common.model.TfSubscribe;
import com.yinghai.twentyfour.common.model.TfSubscribeHelper;
import com.yinghai.twentyfour.common.model.TfUser;
import com.yinghai.twentyfour.common.service.TfMasterService;
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
 * 我的关注
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/app/subscribe")
public class TfSubscribeController {
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private SubscribeService tfSubscribeService;
	@Autowired
	private TfMasterService tfMasterService;
	@Autowired
	private TfUserService tfUserService;
	/**
	 * 创建关注记录
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/create",method= RequestMethod.POST)
	public void createSubscribe(HttpServletRequest request,HttpServletResponse response){
		logger.info("TfSubscribeController/createSubscribe======创建关注记录======");
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//获取参数
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
		String mid = request.getParameter("masterId");
		if(StringUtil.empty(mid)){
			ResponseVo.send101Code(response, "masterId为空");
			return;
		}
		Integer masterId = TransformUtils.toInteger(mid);
		if(masterId<0){
			ResponseVo.send104Code(response, "masterId格式错误");
			return;
		}
		//判断用户、大师是否存在
		TfUser u = tfUserService.findUserById(userId);
		if(u==null){
			ResponseVo.send102Code(response, "该用户不存在");
			return;
		}
		TfMaster m = tfMasterService.selectByPrimaryKey(masterId);
		if(m==null){
			ResponseVo.send102Code(response, "对应大师不存在");
			return;
		}
		//判断是否关注
		TfSubscribe s = tfSubscribeService.selectByUserAndMaster(userId, masterId);
		if(s!=null){
			ResponseVo.send1022Code(response, "已经关注了,不能重复关注");
			return;
		}
		//创建关注记录
		int i=0;
		try {
			i = tfSubscribeService.createSubscribe(userId, masterId,m);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("TfSubscribeController/createSubscribe====="+e.getMessage());
			ResponseVo.send106Code(response, "数据出错，创建关注记录失败");
			return;
		}
		if(i<1){
			ResponseVo.send106Code(response, "数据出错，创建关注记录失败");
			return;
		}
		//根据关注记录id查询关注的详细信息
		TfSubscribeHelper t =  tfSubscribeService.selectSubscribeById(i);
		JSONObject obj = new JSONObject();
		JsonConfig config = new JsonConfig();  
        JsonDateValueProcessor jsonValueProcessor = new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss");  
        config.registerJsonValueProcessor(Date.class, jsonValueProcessor);
        JSONArray sub = JSONArray.fromObject(t, config);
        obj.put("subscribe", sub);
		ResponseVo.send1Code(response, "success", obj);
	}
	/**
	 * 删除关注记录
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/delete",method= RequestMethod.POST)
	public void deleteSubscribe(HttpServletRequest request,HttpServletResponse response){
		logger.info("TfSubscribeController/deleteSubscribe======删除关注记录======");
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//获取参数
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
		String mid = request.getParameter("masterId");
		if(StringUtil.empty(mid)){
			ResponseVo.send101Code(response, "masterId为空");
			return;
		}
		Integer masterId = TransformUtils.toInteger(mid);
		if(masterId<0){
			ResponseVo.send104Code(response, "masterId格式错误");
			return;
		}
		TfSubscribe s = new TfSubscribe();
		s.setsUserId(userId);
		s.setsMasterId(masterId);;
		TfSubscribeHelper sub = tfSubscribeService.selectDetailSubscribe(s);
		if(sub==null){
			ResponseVo.send1024Code(response, "已经取消关注，不要重复取消");
			return;
		}
		long f = sub.getFollows();
		if(f>0){
			f = f -1;
		}else{
			f = 0;
		}
		int i = tfSubscribeService.deleteSubscribe(userId, masterId,f);
		if(i!=1){
			ResponseVo.send106Code(response, "数据出错，关注记录删除失败");
			return;
		}
		JSONObject obj = new JSONObject();
		JsonConfig config = new JsonConfig();
		JsonDateValueProcessor jsonValueProcessor = new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss");  
        config.registerJsonValueProcessor(Date.class, jsonValueProcessor);
        JSONArray ss = JSONArray.fromObject(sub, config);
		obj.put("subscribe", ss);
		ResponseVo.send1Code(response, "success", obj);
	}
	/**
	 * 分页查询关注记录列表
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/list",method= RequestMethod.POST)
	public void getSubscirbesByPage(HttpServletRequest request,HttpServletResponse response){
		logger.info("======查询关注记录列表======");
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//获取参数：用户id，页数，条数
		String num = request.getParameter("page");
		String size = request.getParameter("pageSize");
		Integer n = TransformUtils.toInteger(num);
		Integer s = TransformUtils.toInteger(size);
		Integer pageNum = (n==null||n<1)?1:n;
		Integer pageSize = (s==null||s<1)?10:s;
		String uId = request.getParameter("userId");
		if(StringUtil.empty(uId)){
			ResponseVo.send101Code(response, "userId为空");
			return;
		}
		Integer userId = TransformUtils.toInteger(uId);
		if(userId<0){
			if(userId==-1){
				ResponseVo.send800Code(response, "当前为游客，请登录");
				return;
			}
			ResponseVo.send104Code(response, "userId格式错误");
			return;
		}
		//根据userId查询关注信息：大师头像和昵称
		Page<TfSubscribeHelper> pages = tfSubscribeService.getSubscribesByPage(pageNum, pageSize, userId);
		JSONObject obj = new JSONObject();
		JsonConfig config = new JsonConfig();
		JsonDateValueProcessor jsonValueProcessor = new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss");  
        config.registerJsonValueProcessor(Date.class, jsonValueProcessor);
		JSONArray ss = JSONArray.fromObject(pages, config);
		obj.put("subscribes", ss);
		ResponseVo.send1Code(response, "success", obj);
	}
	
}
