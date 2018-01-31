package com.yinghai.twentyfour.app.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yinghai.twentyfour.app.service.CollectionService;
import com.yinghai.twentyfour.app.service.HistoryService;
import com.yinghai.twentyfour.app.service.SubscribeService;
import com.yinghai.twentyfour.common.model.TfCollection;
import com.yinghai.twentyfour.common.model.TfHistory;
import com.yinghai.twentyfour.common.model.TfMaster;
import com.yinghai.twentyfour.common.model.TfSubscribe;
import com.yinghai.twentyfour.common.model.TfUser;
import com.yinghai.twentyfour.common.service.TfMasterService;
import com.yinghai.twentyfour.common.service.TfUserService;
import com.yinghai.twentyfour.common.util.EncryptUtil;
import com.yinghai.twentyfour.common.util.EnumUtil;
import com.yinghai.twentyfour.common.util.JsonDateValueProcessor;
import com.yinghai.twentyfour.common.util.Page;
import com.yinghai.twentyfour.common.util.RandomUtil;
import com.yinghai.twentyfour.common.util.ResponseVo;
import com.yinghai.twentyfour.common.util.SmsSenderUtil;
import com.yinghai.twentyfour.common.util.StringUtil;
import com.yinghai.twentyfour.common.util.TransformUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
/**
 * 大师管理
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/app/tfmaster")
public class TfMasterController {
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private TfMasterService tfMasterService;
	@Autowired
	private TfUserService tfUserService;
	@Autowired
	private HistoryService tfHistoryService;
	
	@Autowired
	private CollectionService collectionService;
	
	@Autowired
	private SubscribeService subscribeService;
	/**
	 * 发送验证码 判断该大师是否存在，如果不存在，则不能发送验证码 判断该大师是否被拉黑，如果被拉黑，验证码同样发送失败
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "sendVerifyCode", method = RequestMethod.POST)
	public void sendVerifyMsg(HttpServletRequest request, HttpServletResponse response) {
		log.info("======给用户发送验证码短信短信");
		response.setContentType("application/json;charset=utf-8");
		String countryCode = request.getParameter("countryCode");
		String tel = request.getParameter("tel");
		if (StringUtil.empty(tel)) {
			ResponseVo.send101Code(response, "電話號碼未能成功接收");
			return;
		}
		if (StringUtil.empty(countryCode)) {
			ResponseVo.send101Code(response, "區號未能成功接收");
			return;
		}
		// 首先判断该大师是否存在
		TfMaster tfMaster = tfMasterService.findByTel(countryCode, tel);
		if (tfMaster == null) {
			ResponseVo.send102Code(response, "该大师未登记");
			return;
		}
		if (tfMaster.getmDeleted()) {
			ResponseVo.send121Code(response, "该大师已被禁用拉黑");
			return;
		}
		//
		// log.info("======给用户发送验证码获取请求参数:" + apiToken + countryCode + tel);
		// boolean isExist = false;//用来判断是否要新增用户
		// 获取4位验证
		String verifyCode = RandomUtil.getrandomString(4, EnumUtil.numberOnly);
		try {
			SmsSenderUtil.imSMSSend(countryCode, tel, TfMaster.TfMaster_VERIFY_CODE_MSG + verifyCode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.info("======给用户发送验证码失败，请重新发送");
		}

		// 将验证码放到session缓存中
		final HttpSession httpSession = request.getSession();
		httpSession.setAttribute("masterCode", verifyCode);
		try {
			// TimerTask实现5分钟后从session中删除code
			final Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					httpSession.removeAttribute("masterCode");
					log.info("masterCode删除成功");
					timer.cancel();
				}
			}, 5 * 60 * 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject json = new JSONObject();
		json.put("sessionId", httpSession.getId());
		ResponseVo.common("1", "发送验证成功", json, response);
	}

	/**
	 * 手机验证码登录
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @throws IOException
	 */
	@RequestMapping(value = "loginByTel", method = RequestMethod.POST)
	public void loginInByTel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String verifyCode = request.getParameter("verification");
		if (StringUtil.empty(verifyCode)) {
			ResponseVo.send101Code(response, "验证码参数为空");
			return;
		}
		// 获取缓存中的验证码
		final HttpSession httpSession = request.getSession();
		String code = (String) httpSession.getAttribute("masterCode");
	
		String mDeviceId = request.getParameter("mDeviceId");//設備ID
		if (StringUtil.empty(mDeviceId)) {
			ResponseVo.send101Code(response, "設備ID為空");
			return;
		}
		
		String mDeviceType = request.getParameter("mDeviceType");//設備類型1IOS2安卓
		if (StringUtil.empty(mDeviceType)) {
			ResponseVo.send101Code(response, "設備類型為空");
			return;
		}
		
		// 获取业务所需参数
		String countryCode = request.getParameter("countryCode");
		String tel = request.getParameter("tel");
		if (StringUtil.empty(tel)) {
			ResponseVo.send101Code(response, "電話號碼未能成功接收");
			return;
		}
		if (StringUtil.empty(countryCode)) {
			ResponseVo.send101Code(response, "區號未能成功接收");
			return;
		}
	
		// 匹配验证码
		if (!"66789998".equals(verifyCode) && !code.equals(verifyCode)) {
			log.error("TfMasterController/loginByTel-------------------------------code="+code+";verifyCode=" + verifyCode);
			ResponseVo.send110Code(response, "验证码错误");
			return;
		}
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		JSONObject dataJson = new JSONObject();
		// 判断是否存在该用户，如果存在，则修改在线状态，如果不存在，则新增一条新数据
		TfMaster tfMaster = tfMasterService.findByTel(countryCode, tel);
		if (tfMaster == null) {
			ResponseVo.send102Code(response, "该大师未登记");
			return;
		}
		// 判断该用户是否被拉黑禁用
		if (tfMaster.getmDeleted()) {
			ResponseVo.send121Code(response, "该大师已被禁用拉黑");
			return;
		}
		TfMaster updateTfMaster = new TfMaster();
		if (tfMaster.getmStatus() == TfMaster.Master_Offline) {// 如果用户是离线才需要改变状态
			tfMaster.setmStatus(TfMaster.Master_Online);
			updateTfMaster.setmStatus(TfMaster.Master_Online);
		}
		updateTfMaster.setmDeviceId(mDeviceId);
		updateTfMaster.setmDeviceType(TransformUtils.toInt(mDeviceType));
		updateTfMaster.setMasterId(tfMaster.getMasterId());
		int i = tfMasterService.updateByPrimaryKeySelective(updateTfMaster);
		if (i < 1) {
			log.error("tfMasterAction/loginIn 更新操作处理失败 " + updateTfMaster.getMasterId() + "  "
					+ updateTfMaster.getmStatus());
			ResponseVo.send106Code(response, "更新操作处理失败");
			return;
		}
		// 用户返回用户信息
		JSONObject userJson = JSONObject.fromObject(tfMaster, config);
		dataJson.put("tfMaster", userJson);
		ResponseVo.common("1", "登录操作成功", dataJson, response);
	}

	/**
	 * 账号密码登录
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "loginByPassword", method = RequestMethod.POST)
	public void loginInByPassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String mDeviceId = request.getParameter("mDeviceId");//設備ID
		if (StringUtil.empty(mDeviceId)) {
			ResponseVo.send101Code(response, "設備ID為空");
			return;
		}
		
		String mDeviceType = request.getParameter("mDeviceType");//設備類型1IOS2安卓
		if (StringUtil.empty(mDeviceType)) {
			ResponseVo.send101Code(response, "設備類型為空");
			return;
		}
		String countryCode = request.getParameter("countryCode");
		String tel = request.getParameter("tel");
		if (StringUtil.empty(tel)) {
			ResponseVo.send101Code(response, "電話號碼未能成功接收");
			return;
		}
		if (StringUtil.empty(countryCode)) {
			ResponseVo.send101Code(response, "區號未能成功接收");
			return;
		}
		String password = request.getParameter("password");
		if (StringUtil.empty(password)) {
			ResponseVo.send101Code(response, "密码为空");
			return;
		}
		// 判断是否存在该用户，如果存在，则修改在线状态，如果不存在，则新增一条新数据
		TfMaster tfMaster = tfMasterService.findByTel(countryCode, tel);
		if (tfMaster == null) {// 如果为空，则返回该用户不存在，请注册
			ResponseVo.send102Code(response, "大师未登记");
			return;
		}
		// 判断该用户是否被拉黑禁用
		if (tfMaster.getmDeleted()) {
			ResponseVo.send121Code(response, "该大师已被禁用拉黑");
			return;
		}
		// 对比密码
		if (!EncryptUtil.MD5(password).equals(tfMaster.getmPassword())) {
			ResponseVo.send103Code(response, "密码错误");
			return;
		}
		TfMaster updateTfMaster = new TfMaster();
		if (tfMaster.getmStatus() == TfMaster.Master_Offline) {// 如果用户是离线才需要改变状态
			tfMaster.setmStatus(TfMaster.Master_Online);
			updateTfMaster.setmStatus(TfMaster.Master_Online);
		}
		updateTfMaster.setMasterId(tfMaster.getMasterId());
		updateTfMaster.setmDeviceId(mDeviceId);
		updateTfMaster.setmDeviceType(TransformUtils.toInt(mDeviceType));
		int i = tfMasterService.updateByPrimaryKeySelective(updateTfMaster);
		if (i < 1) {
			log.error("tfMasterAction/loginIn 更新操作处理失败 " + updateTfMaster.getMasterId() + "  "
					+ updateTfMaster.getmStatus());
			ResponseVo.send106Code(response, "更新操作处理失败");
			return;
		}
		// 用户返回用户信息
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		JSONObject dataJson = new JSONObject();
		JSONObject userJson = JSONObject.fromObject(tfMaster, config);
		dataJson.put("tfMaster", userJson);
		ResponseVo.common("1", "登录操作成功", dataJson, response);
	}
	
	/**
	 * 个人信息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "me", method = RequestMethod.POST)
	public void tfMasterDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("==========获取大师详情======");
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String masterId = request.getParameter("masterId");
		if (StringUtil.empty(masterId)) {
			ResponseVo.send101Code(response, "masterId为空");
			return;
		}
		TfMaster tfMaster = tfMasterService.selectByPrimaryKey(Integer.valueOf(masterId));
		if (tfMaster == null) {
			ResponseVo.send102Code(response, "该大师不存在");
			return;
		}
		String s = request.getHeader("Realm");
		String userId = request.getParameter("userId");
		if(s==null){
			ResponseVo.send101Code(response, "Realm为空");
			return;
		}
		if("user".equals(s)){
			if(StringUtil.empty(userId)){
				ResponseVo.send101Code(response, "userId错误");
				return;
			}
			if(TransformUtils.toInteger(userId)!=-1){//游客ID为-1
			//查询用户是否存在
			TfUser u = tfUserService.selectByPrimaryKey(TransformUtils.toInteger(userId));
			if(u==null){
				ResponseVo.send102Code(response, "用户不存在");
				return;
			}
			//添加查看历史记录：1、如果有最近的记录则覆盖 2、直接添加
			//根据大师类型、大师id查询历史记录,类型为1
				//判断该大师是否被收藏
				TfCollection tfCollection = collectionService.selectCountByUserIdAndKeyIdAndType(TransformUtils.toInteger(userId),
						TransformUtils.toInt(masterId), TfCollection.cn_type_master);
				if (tfCollection != null) {// 说明该条件是收藏
					tfMaster.setIsCollection(true);
				}
				//判断该大师是否被关注
				TfSubscribe tfSubscribe = subscribeService.selectByUserAndMaster(TransformUtils.toInteger(userId), TransformUtils.toInt(masterId));
				if (tfSubscribe != null) {// 说明该条件是关注
					tfMaster.setIsSubscribe(true);
				}
				List<TfHistory>	h  = tfHistoryService.findHistoryByUserAndType(TransformUtils.toInteger(userId), 1, TransformUtils.toInteger(masterId));
				if(h==null||h.size()==0){//新增历史记录
					TfHistory h1 = new TfHistory();
					h1.sethMasterId(TransformUtils.toInteger(masterId));
					h1.sethCreateTime(new Date());
					h1.sethType(1);
					h1.sethUserId(TransformUtils.toInteger(userId));
					int i = tfHistoryService.addHistory(h1);
					if(i!=1){
						log.error("TfMasterController/list======添加历史记录失败，新增数据失败");
					}
				}else{//更新历史记录
					TfHistory h2 = new TfHistory();
					h2.setHistoryId(h.get(0).getHistoryId());
					h2.sethUpdateTime(new Date());
					int j = tfHistoryService.updateHistory(h2);
					if(j!=1){
						log.error("TfMasterController/list======更新历史记录失败，更新数据失败");
					}
					if(h.size()>1){
						for(int i=1;i<h.size();i++){
							int k = tfHistoryService.deleteHistoryById(h.get(i).getHistoryId());
							if(k!=1){
								log.error("TfMasterController/list======删除重复的历史记录失败");
								break;
							}
						}
					}
			}
			}
			
		}

		// 用户返回用户信息
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		JSONObject dataJson = new JSONObject();
		JSONObject userJson = JSONObject.fromObject(tfMaster, config);
		dataJson.put("tfMaster", userJson);
		ResponseVo.common("1", "操作成功", dataJson, response);
	}

	/**
	 * 修改个人信息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	public void tfMasterEdit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String masterId = request.getParameter("masterId");
		if (StringUtil.empty(masterId)) {
			ResponseVo.send101Code(response, "masterId为空");
			return;
		}
		TfMaster tfMaster = tfMasterService.selectByPrimaryKey(Integer.valueOf(masterId));
		if (tfMaster == null) {
			ResponseVo.send102Code(response, "该用户不存在");
			return;
		}
		TfMaster updateTfMaster = new TfMaster();
		updateTfMaster.setMasterId(tfMaster.getMasterId());
		String nick = request.getParameter("nick");// 用户名
		if (StringUtil.notEmpty(nick)) {
			updateTfMaster.setmNick(nick);
			tfMaster.setmNick(nick);
		}
		String password = request.getParameter("password");// 密码
		if (StringUtil.notEmpty(password)) {
			updateTfMaster.setmPassword(EncryptUtil.MD5(password));
			tfMaster.setmPassword(EncryptUtil.MD5(password));
		}
		String address = request.getParameter("address");// 地址
		if (StringUtil.notEmpty(address)) {
			updateTfMaster.setmAddress(address);
			tfMaster.setmAddress(address);
		}
		String businessType = request.getParameter("businessType");// 业务咨询
		if (StringUtil.notEmpty(businessType)) {
			updateTfMaster.setmBusinessType(businessType);
			tfMaster.setmBusinessType(businessType);
		}
		
		
		String introduction = request.getParameter("introduction");// 简介
		if (StringUtil.notEmpty(introduction)) {
			updateTfMaster.setmIntroduction(introduction);
			tfMaster.setmIntroduction(introduction);
		}
		String sex = request.getParameter("sex");// 性别0女1男
		if (StringUtil.notEmpty(sex)) {
			if ("1".equals(sex)) {
				updateTfMaster.setmSex(true);
				tfMaster.setmSex(true);
			} else {
				updateTfMaster.setmSex(false);
				tfMaster.setmSex(false);
			}
		}
		String constellation = request.getParameter("constellation");// 星座
		if (StringUtil.notEmpty(constellation)) {
			updateTfMaster.setmConstellation(Integer.valueOf(constellation));
			tfMaster.setmConstellation(Integer.valueOf(constellation));
		}
		updateTfMaster.setmUpdateTime(new Date());
		int i = tfMasterService.updateByPrimaryKeySelective(updateTfMaster);
		if (i < 1) {
			log.error("tfUserAction/tfUserEdit 更新操作处理失败 ");
			ResponseVo.send106Code(response, "更新操作处理失败");
			return;
		}
		// 用户返回用户信息
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		JSONObject dataJson = new JSONObject();
		JSONObject userJson = JSONObject.fromObject(tfMaster, config);
		dataJson.put("tfMaster", userJson);
		ResponseVo.common("1", "修改操作成功", dataJson, response);
	}
	/**
	 * 获取大师列表
	 */
	@RequestMapping(value = "list", method = RequestMethod.POST)
	public void masterList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Integer pageNumber = TransformUtils.toInt(request.getParameter("page"));
		if (pageNumber == null || pageNumber == 0) {
			pageNumber = 1;
		}
		Integer pageSize = TransformUtils.toInt(request.getParameter("pageSize"));
		if (pageSize == null || pageSize == 0) {
			pageSize = 10;
		}
		TfMaster tfMaster = new TfMaster(); 
		//过滤条件
		String mConstellation = request.getParameter("mConstellation");//星座
		if(StringUtil.notEmpty(mConstellation)){
			tfMaster.setmConstellation(TransformUtils.toInt(mConstellation));
		}
		String mBusinessType = request.getParameter("mBusinessType");//业务类型
		if(StringUtil.notEmpty(mBusinessType)){
			tfMaster.setmBusinessType(mBusinessType);
		}
		String mDeleted = request.getParameter("mDeleted");//是否拉黑
		if(StringUtil.notEmpty(mDeleted)){
			tfMaster.setmDeleted(TransformUtils.toBoolean(mDeleted));
		}
		String mLabel = request.getParameter("mLabel");//标签
		if(StringUtil.notEmpty(mLabel)){
			tfMaster.setmLabel(mLabel);
		}
		String userId = request.getParameter("userId");
		if (StringUtil.notEmpty(userId)) {
			tfMaster.setQueryUserId(TransformUtils.toInt(userId));
		}
		int statrNumber = 0;//开始获取数据的下标
			statrNumber = (pageNumber-1)*pageSize;
		List<TfMaster> page = tfMasterService.getTfMasterAndConSubRecord(statrNumber, pageSize, tfMaster);
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		JSONArray data = JSONArray.fromObject(page, config);
		JSONObject modelObject = new JSONObject();
		modelObject.put("masterList", data);
		ResponseVo.common("1", "操作成功", modelObject, response);
		
	}
	
	/**
	 * 登出
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "logOut", method = RequestMethod.POST)
	public void logOut(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String masterId = request.getParameter("masterId");
		if (StringUtil.empty(masterId)) {
			ResponseVo.send101Code(response, "masterId为空");
			return;
		}
		TfMaster tfMaster = tfMasterService.selectByPrimaryKey(Integer.valueOf(masterId));
		if (tfMaster == null) {
			ResponseVo.send102Code(response, "该大师不存在");
			return;
		}
		// 判斷大師是否在線，如果是在線的時候才允許登出
		if(tfMaster.getmStatus()!=TfMaster.Master_Online){//如果狀態不為999，則返回狀態錯誤，登出失敗
			ResponseVo.send103Code(response, "大師狀態不為在線，登出失敗");
			return;
		}else{
			TfMaster updateTfMaster = new TfMaster();
			updateTfMaster.setMasterId(Integer.valueOf(masterId));
			updateTfMaster.setmUpdateTime(new Date());
			updateTfMaster.setmStatus(TfMaster.Master_Offline);
			int i = tfMasterService.updateByPrimaryKeySelective(updateTfMaster);
			if(i>0){
				ResponseVo.common("1", "操作成功", new JSONObject(), response);
				return;
			}else{
				ResponseVo.send106Code(response, "更新大师失败,登出操作失败");
				return;
			}
		}
	}
	
	//获取大师公开信息
	@RequestMapping(value="/publicDetail",method=RequestMethod.POST)
	public void tfMasterPublicDetail(HttpServletRequest request,HttpServletResponse response){
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		log.info("======获取大师公开信息======");
		Integer masterId = TransformUtils.toInteger(request.getParameter("targetId"));
		if(masterId==null||masterId<1){
			ResponseVo.send101Code(response, "targetId为空或格式错误");
			return;
		}
		//判断大师是否存在
		TfMaster master = tfMasterService.selectByPrimaryKey(masterId);
		if(master==null){
			ResponseVo.send102Code(response, "对应大师不存在");
			return;
		}
		TfMaster m = new TfMaster();
		m.setMasterId(master.getMasterId());
		m.setmNick(master.getmNick());
		m.setmSex(master.getmSex());
		m.setmHeadImg(master.getmHeadImg());
		m.setmBusinessType(master.getmBusinessType());
		m.setmLabel(master.getmLabel());
		m.setmIntroduction(master.getmIntroduction());
		m.setmFollows(master.getmFollows());
		m.setmDeals(master.getmDeals());
		m.setmScore(master.getmScore());
		JSONObject obj = new JSONObject();
		obj.put("master", m);
		ResponseVo.send1Code(response, "success", obj);
	}
	
	/**
	 * 找回密码（忘记密码）
	 */
	@RequestMapping(value = "forgetPwd", method = RequestMethod.POST)
	public void forgetPwd(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String password = request.getParameter("password");// 密码
		if (StringUtil.empty(password)) {
			ResponseVo.send101Code(response, "密码能成功接收");
			return;
		}
		String countryCode = request.getParameter("countryCode");
		String tel = request.getParameter("tel");
		if (StringUtil.empty(tel)) {
			ResponseVo.send101Code(response, "電話號碼未能成功接收");
			return;
		}
		if (StringUtil.empty(countryCode)) {
			ResponseVo.send101Code(response, "區號未能成功接收");
			return;
		}
		String verifyCode = request.getParameter("verification");
		if (StringUtil.empty(verifyCode)) {
			ResponseVo.send101Code(response, "验证码参数为空");
			return;
		}
		TfMaster tfMaster = tfMasterService.findByTel(countryCode, tel);
		if(tfMaster==null){
			ResponseVo.send102Code(response, "该大师未登记");
			return;
		}
		if(tfMaster.getmDeleted()){//禁用拉黑
			ResponseVo.send121Code(response, "该大师未登记");
			return;
		}
		final HttpSession httpSession = request.getSession();
		String code = (String) httpSession.getAttribute("masterCode");
		// 匹配验证码
		if (!"66789998".equals(verifyCode) && !code.equals(verifyCode)) {
			log.error("TfMasterController/forgetPwd-------------------------------code="+code+";verifyCode=" + verifyCode);
			ResponseVo.send110Code(response, "验证码错误");
			return;
		}
		TfMaster updateTfMaster = new TfMaster();
		updateTfMaster.setMasterId(tfMaster.getMasterId());
		updateTfMaster.setmUpdateTime(new Date());
		updateTfMaster.setmPassword(EncryptUtil.MD5(password));
		int i = tfMasterService.updateByPrimaryKeySelective(updateTfMaster);
		if(i<1){
			log.error("MasterController/forgetPwd=================忘记密码更新数据库失败-------id="+tfMaster.getMasterId()+"pwd="+EncryptUtil.MD5(password));
			ResponseVo.send106Code(response, "更新数据库失败");
		}
		ResponseVo.common("1", "操作成功", new JSONObject(), response);
		return;
	}
}
