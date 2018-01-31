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

import com.yinghai.twentyfour.app.service.TfFriendService;
import com.yinghai.twentyfour.common.im.util.ImIdUtil;
import com.yinghai.twentyfour.common.model.TfMaster;
import com.yinghai.twentyfour.common.model.TfUser;
import com.yinghai.twentyfour.common.model.TfUserFriend;
import com.yinghai.twentyfour.common.service.TfUserService;
import com.yinghai.twentyfour.common.util.JsonDateValueProcessor;
import com.yinghai.twentyfour.common.util.ResponseVo;
import com.yinghai.twentyfour.common.util.StringUtil;
import com.yinghai.twentyfour.common.util.TransformUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 好友关系
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/app/friend")
public class TfFriendController {
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private TfUserService tfUserService;
	@Autowired
	private TfFriendService tfFriendService;
	

	
	
	/**
	 * 添加好友
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/addFriend",method=RequestMethod.POST)
	public void addFriend(HttpServletRequest request,HttpServletResponse response){
		logger.info("TfFriendController/addFriend======添加好友======");
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//获取参数：用户ID，目标用户ID，用户类型type(现在只有用户之间)
		Integer  uId = TransformUtils.toInteger(request.getParameter("userId"));
		Integer fId = TransformUtils.toInteger(request.getParameter("friendId"));
		if(uId==null){
			ResponseVo.send101Code(response, "userId为空");
			return;
		}
		if(uId<0){
			if(uId==-1){
				ResponseVo.send800Code(response, "当前为游客，请登录");
				return;
			}
			ResponseVo.send101Code(response, "userId参数错误");
			return;
		}
		if(fId==null){
			ResponseVo.send101Code(response, "friendId为空");
			return;
		}
		if(fId<0){
			ResponseVo.send101Code(response, "friendId参数错误");
			return;
		}
		String m = request.getParameter("message");
		Integer type = TransformUtils.toInteger(request.getParameter("type"));
		//查询好友关系记录
		type = (type==null||type<1)?1:type;
		type = 1;
		Integer friendId;
		if(uId==fId){
			ResponseVo.send101Code(response, "userId与friendId不能相同");
			return;
		}
		//添加好友关系，并发送申请好友通知消息
		try {
			friendId = tfFriendService.addFriend(uId, fId, type, m);//返回目标用户的
		} catch (Exception e) {
			e.printStackTrace();
			ResponseVo.send106Code(response, e.getMessage());
			return;
		}
		if(friendId==-1){
			ResponseVo.send102Code(response, "userId对应IM不存在");
			return;
		}else if(friendId==-2){
			ResponseVo.send102Code(response, "friendId对应IM不存在");
			return;
		}else if(friendId==-3){
			ResponseVo.send1020Code(response, "已经是好友，无需重复添加");
			return;
		}else if(friendId==-4){
			ResponseVo.send121Code(response, "用户已经被拉黑");
			return;
		}else if(friendId==-5){
			logger.error("数据出错，存在多个好友关系记录:userId="+uId+",friendId="+fId);
			ResponseVo.send106Code(response, "数据出错，存在多个好友关系记录");
			return;
		}else if(friendId==-6){
			ResponseVo.send114Code(response, "类型错误");
			return;
		}else if(friendId==-7){
			ResponseVo.send101Code(response, "参数错误");
			return;
		}
		//根据目标用户Id查询目标用户数据
		TfUser friend = tfUserService.selectByPrimaryKey(friendId);
		JSONObject obj = new JSONObject();
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		JSONArray arr = JSONArray.fromObject(friend, config);
		obj.put("friend", arr);
		ResponseVo.send1Code(response, "sucess", obj);		
	}
	
	/**
	 * 同意或拒绝添加好友
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/agreeFriend",method=RequestMethod.POST)
	public void agreeFriend(HttpServletRequest request,HttpServletResponse response){
		logger.info("TfFriendController/agreeFriend======同意好友申请======");
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Integer  uId = TransformUtils.toInteger(request.getParameter("userId"));
		Integer fId = TransformUtils.toInteger(request.getParameter("friendId"));
		if(uId==null){
			ResponseVo.send101Code(response, "userId为空");
			return;
		}
		if(uId<0){
			if(uId==-1){
				ResponseVo.send800Code(response, "当前为游客，请登录");
				return;
			}
			ResponseVo.send101Code(response, "userId参数错误");
			return;
		}
		if(fId==null){
			ResponseVo.send101Code(response, "friendId为空");
			return;
		}
		if(fId<0){
			ResponseVo.send101Code(response, "friendId参数错误");
			return;
		}
		Integer type = TransformUtils.toInteger(request.getParameter("userType"));//关系类型
		type = (type==null||type<1)?1:type;
		type = 1;//用户之间
		Integer t = TransformUtils.toInteger(request.getParameter("type"));//1为同意，2为拒绝
		if(t==null||(t!=1&&t!=2)){
			ResponseVo.send114Code(response, "type错误");
			return;
		}
		if(uId==fId){
			ResponseVo.send101Code(response, "userId与friendId不能相同");
			return;
		}
		//同意申请或拒绝申请
		int i=0;
		String s = "";
		if(t==1){
			s = "同意好友申请操作失败";
		}else{
			s = "拒绝好友申请操作失败";
		}
		try {
			i = tfFriendService.dealFriend(uId,fId,type,t);
		} catch (Exception e) {
			e.printStackTrace();
			ResponseVo.send106Code(response, s);
			return;
		}
		if(i==1){
			TfUser friend = tfUserService.findUserById(fId);
			JSONObject obj = new JSONObject();
			JsonConfig config = new JsonConfig();
			config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
			JSONArray arr = JSONArray.fromObject(friend, config);
			obj.put("friend", arr);
			ResponseVo.send1Code(response, "已同意", obj);
			return;
		}else if(i==2){
			TfUser friend = tfUserService.findUserById(fId);
			JSONObject obj = new JSONObject();
			JsonConfig config = new JsonConfig();
			config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
			JSONArray arr = JSONArray.fromObject(friend, config);
			obj.put("friend", arr);
			ResponseVo.send1Code(response, "已拒绝", obj);
			return;
		}
		else if(i==-1){
			ResponseVo.send101Code(response, "参数错误");
			return;
		}else if(i==-2){//好友关系数据不存在
			ResponseVo.send102Code(response, "好友关系数据不存在");
			return;
		}else if(i==-3){//好友关系数据有多条
			logger.error("好友关系数据有多条");
			ResponseVo.send106Code(response, "好友关系数据有多条");
			return;
		}else if(i==-4){//已经为好友关系,不能执行拒绝操作
			logger.error("已经为好友关系,不能执行拒绝操作");
			ResponseVo.send106Code(response, "已经为好友关系");
			return;
		}else if(i==-5){
			logger.error("发送的请求被拒绝");//自己不能同意自己的好友请求
			ResponseVo.send106Code(response, "错误的请求");
			return;
		}
		else{//其他情况
			ResponseVo.send106Code(response, s);
			return;
		}
	}
	
	/**
	 * 删除好友或拉黑好友
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/deleteFriend",method=RequestMethod.POST)
	public void deleteFriend(HttpServletRequest request,HttpServletResponse response){
		logger.info("TfFriendController/deleteFriend======删除好友或拉黑好友======");
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//获取参数：用户ID，目标用户ID，用户类型type(现在只有用户之间)
		Integer  uId = TransformUtils.toInteger(request.getParameter("userId"));
		Integer fId = TransformUtils.toInteger(request.getParameter("friendId"));
		if(uId==null){
			ResponseVo.send101Code(response, "userId为空");
			return;
		}
		if(uId<0){
			if(uId==-1){
				ResponseVo.send800Code(response, "当前为游客，请登录");
				return;
			}
			ResponseVo.send101Code(response, "userId参数错误");
			return;
		}
		if(fId==null){
			ResponseVo.send101Code(response, "friendId为空");
			return;
		}
		if(fId<0){
			ResponseVo.send101Code(response, "friendId参数错误");
			return;
		}
		if(uId==fId){
			ResponseVo.send101Code(response, "userId与friendId不能相同");
			return;
		}
		Integer type = TransformUtils.toInteger(request.getParameter("userType"));
		type = (type==null||type<1)?1:type;
		type = 1;
		Integer t = TransformUtils.toInteger(request.getParameter("type"));
		if(t==null){
			ResponseVo.send101Code(response, "type为空");
			return;
		}
		if(t!=1&&t!=2){//1为删除好友，2为拉黑好友
			ResponseVo.send114Code(response, "type错误");
			return;
		}
		int i = tfFriendService.deleteFriend(uId, fId, type,t);
		if(i==1){
			TfUser friend = tfUserService.selectByPrimaryKey(fId);
			JSONObject obj = new JSONObject();
			JsonConfig config = new JsonConfig();
			config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
			JSONArray arr = JSONArray.fromObject(friend, config);
			obj.put("friend", arr);
			ResponseVo.send1Code(response, "success", obj);
			return;
		}else if(i==-1){
			ResponseVo.send102Code(response, "userId对应IM不存在");
			return;
		}else if(i==-2){
			ResponseVo.send102Code(response, "friendId对应IM不存在");
			return;
		}else if(i==-3){//非好友关系
			ResponseVo.send102Code(response, "非好友关系");
			return;
		}else if(i==-6){
			ResponseVo.send114Code(response, "类型错误");
			return;
		}else{
			String s;
			if(t==1){
				s = "数据出错，删除好友失败";
			}else{
				s = "数据出错，拉黑好友失败";
			}
			ResponseVo.send106Code(response, s);
			return;
		}
	}
	
	/**
	 * 查询好友列表接口
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getFriendList",method=RequestMethod.POST)
	public void getFriendList(HttpServletRequest request,HttpServletResponse response){
		logger.info("TfFriendController/getFriendList======获取好友列表======");
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Integer userId = TransformUtils.toInteger(request.getParameter("userId"));
		if(userId==null){
			ResponseVo.send101Code(response, "userId为空");
			return;
		}
		if(userId<0){
			if(userId==-1){
				ResponseVo.send800Code(response, "当前为游客，请登录");
				return;
			}
			ResponseVo.send104Code(response, "userId格式错误");
			return;
		}
		Integer type  = TransformUtils.toInteger(request.getParameter("type"));
		type = (type==null||type<0)?1:type;
		type = 1;
		Integer pageNum = TransformUtils.toInt(request.getParameter("page"));
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
		}
		Integer pageSize = TransformUtils.toInt(request.getParameter("pageSize"));
		if (pageSize == null || pageSize == 0) {
			pageSize = 10;
		}
		List<TfUserFriend> list = null;
		try {
			list = tfFriendService.getFriendList(pageNum,pageSize,userId, type);
		} catch (Exception e) {
			logger.error("获取好友列表出错"+e.getMessage());
			e.printStackTrace();
			ResponseVo.send106Code(response, "获取好友列表出错");
			return;
		}
		JSONObject obj = new JSONObject();
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		JSONArray arr = JSONArray.fromObject(list, config);
		obj.put("friend", arr);
		ResponseVo.send1Code(response, "success", obj);
	}
	/**
	 * 根据昵称查询好友
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getFriend",method=RequestMethod.POST)
	public void getFriend(HttpServletRequest request,HttpServletResponse response){
		logger.info("TfFriendController/getFriend======根据昵称查询好友======");
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Integer userId = TransformUtils.toInteger(request.getParameter("userId"));
		if(userId==null){
			ResponseVo.send101Code(response, "userId为空");
			return;
		}
		if(userId<0){
			if(userId==-1){
				ResponseVo.send800Code(response, "当前为游客，请登录");
				return;
			}
			ResponseVo.send104Code(response, "userId格式错误");
			return;
		}
		String nick = request.getParameter("nick");
		if(StringUtil.empty(nick)){
			ResponseVo.send101Code(response, "nick为空");
			return;
		}
		Integer type  = TransformUtils.toInteger(request.getParameter("type"));
		type = (type==null||type<0)?1:type;
		type = 1;
		List<TfUserFriend> list=null;
		try {
			list = tfFriendService.getFriend(userId, type, nick);
		} catch (Exception e) {
			logger.error("根据昵称查询好友出错"+e.getMessage());
			e.printStackTrace();
			ResponseVo.send106Code(response, "获取好友列表出错");
			return;
		}
		JSONObject obj = new JSONObject();
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		JSONArray arr = JSONArray.fromObject(list, config);
		obj.put("friend", arr);
		ResponseVo.send1Code(response, "success", obj);
	}
	
	/**
	 * 根据im的id/im昵称查询用户或大师
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getUserOrMaster",method=RequestMethod.POST)
	public void getUserOrMaster(HttpServletRequest request,HttpServletResponse response){
		logger.info("TfFriendController/getUserOrMaster======根据im的id/im昵称查询用户或大师======");
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Integer userId = TransformUtils.toInteger(request.getParameter("targetId"));
		Integer targetId = TransformUtils.toInteger(request.getParameter("userId"));
		String nick = request.getParameter("nick");
		if((userId==null||userId<0)&&StringUtil.empty(nick)){
			ResponseVo.send101Code(response, "targetId和nick不能同时为空");
			return;
		}
		if(targetId!=null&&targetId==-1){
			ResponseVo.send800Code(response, "当前为游客，请登录");
			return;
		}
		if(targetId==null||targetId<0){
			ResponseVo.send101Code(response, "userId不能为空");
			return;
		}
		List<TfUserFriend> l1 = tfFriendService.getUser(userId,nick,targetId);
		List<TfMaster> l2 = tfFriendService.getMaster(userId,nick);
		JSONObject obj = new JSONObject();
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		obj.put("user", JSONArray.fromObject(l1,config));
		obj.put("master", JSONArray.fromObject(l2,config));
		ResponseVo.send1Code(response, "success", obj);
	}
	/**
	 * 判断好友关系
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/isFriend",method=RequestMethod.POST)
	public void isFriend(HttpServletRequest request,HttpServletResponse response){
		logger.info("TfFriendController/isFriend======判断好友关系======");
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//获取参数：用户ID，目标用户ID，用户类型type(现在只有用户之间)
		Integer  uId = TransformUtils.toInteger(request.getParameter("userId"));
		Integer fId = TransformUtils.toInteger(request.getParameter("friendId"));
		if(uId==null){
			ResponseVo.send101Code(response, "userId为空");
			return;
		}
		if(uId<0){
			if(uId==-1){
				ResponseVo.send800Code(response, "当前为游客，请登录");
				return;
			}
			ResponseVo.send101Code(response, "userId参数错误");
			return;
		}
		if(fId==null){
			ResponseVo.send101Code(response, "friendId为空");
			return;
		}
		if(fId<0){
			ResponseVo.send101Code(response, "friendId参数错误");
			return;
		}
		if(uId==fId){
			ResponseVo.send101Code(response, "userId与friendId不能相同");
			return;
		}
		Integer type = TransformUtils.toInteger(request.getParameter("type"));
		//查询好友关系记录
		type = (type==null||type<1)?1:type;
		type = 1;
		int b = tfFriendService.isFriend(uId,fId,type);
		JSONObject obj = new JSONObject();
		if(b==1){
			obj.put("isFriend", 1);
			ResponseVo.send1Code(response, "是好友关系", obj);
		}else if(b==-1){
			ResponseVo.send101Code(response, "参数有误");
			return;
		}else if(b==-2){
			ResponseVo.send106Code(response, "好友关系数据有多条");
			return;
		}else if(b==2){
			obj.put("isFriend", 2);
			ResponseVo.send1Code(response, "不是好友关系", obj);
			return;
		}else if(b==3){
			obj.put("isFriend", 3);
			ResponseVo.send1Code(response, "已经被拉黑", obj);
			return;
		}
	}
}
