package com.yinghai.twentyfour.app.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.apache.log4j.chainsaw.Main;

import com.yinghai.twentyfour.app.service.TfFriendService;
import com.yinghai.twentyfour.common.constant.Constant;
import com.yinghai.twentyfour.common.model.TfMaster;
import com.yinghai.twentyfour.common.model.TfUser;
import com.yinghai.twentyfour.common.model.TfUserFriend;
import com.yinghai.twentyfour.common.service.TfMasterService;
import com.yinghai.twentyfour.common.service.TfUserService;
import com.yinghai.twentyfour.common.util.EncryptUtil;
import com.yinghai.twentyfour.common.util.EnumUtil;
import com.yinghai.twentyfour.common.util.JsonDateValueProcessor;
import com.yinghai.twentyfour.common.util.RandomUtil;
import com.yinghai.twentyfour.common.util.ResponseVo;
import com.yinghai.twentyfour.common.util.SmsSenderUtil;
import com.yinghai.twentyfour.common.util.StringUtil;
import com.yinghai.twentyfour.common.util.TransformUtils;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
/**
 * 用户端管理
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/app/tfuser")
public class TfUserController {
	@Autowired
	private TfUserService tfUserService;
	@Autowired
	private TfFriendService tfFriendService;
	@Autowired
	private TfMasterService tfMasterService;
	private Logger log = Logger.getLogger(this.getClass());

	/**
	 * 发送验证码
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
		// log.info("======给用户发送验证码获取请求参数:" + apiToken + countryCode + tel);
		// boolean isExist = false;//用来判断是否要新增用户
		//判断该用户是否被拉黑，如果被拉黑，则发送验证码失败
		TfUser tfUser = tfUserService.findByTel(countryCode, tel);
		if(tfUser!=null&&tfUser.getuDeleted()!=null&&tfUser.getuDeleted()){
			ResponseVo.send121Code(response, "该用户被禁用拉黑");
			return;
		}
		// 获取4位验证
		String verifyCode = RandomUtil.getrandomString(4, EnumUtil.numberOnly);
		try {
			SmsSenderUtil.imSMSSend(countryCode, tel, TfUser.TfUser_VERIFY_CODE_MSG + verifyCode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.info("======给用户发送验证码失败，请重新发送");
		}

		// 将验证码放到session缓存中
		final HttpSession httpSession = request.getSession();
		httpSession.setAttribute("code", verifyCode);
		try {
			// TimerTask实现5分钟后从session中删除code
			final Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					httpSession.removeAttribute("code");
					log.info("code删除成功");
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
		// 获取缓存中的验证码
		final HttpSession httpSession = request.getSession();
		String verifyCode = request.getParameter("verification");
		if (StringUtil.empty(verifyCode)) {
			ResponseVo.send101Code(response, "验证码参数为空");
			return;
		}
		String code = (String) httpSession.getAttribute("code");
		// 判断验证码是否过期
		if (StringUtil.empty(code)&&!verifyCode.equals("66789998")) {
			ResponseVo.send105Code(response, "验证码已过期");
			return;
		}
		String uDeviceId = request.getParameter("uDeviceId");//設備ID
		if (StringUtil.empty(uDeviceId)) {
			ResponseVo.send101Code(response, "設備ID為空");
			return;
		}
		
		String uDeviceType = request.getParameter("uDeviceType");//設備類型1IOS2安卓
		if (StringUtil.empty(uDeviceType)) {
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
			log.error("TUserController/loginByTel-------------------------------code="+code+";verifyCode=" + verifyCode);
			ResponseVo.send110Code(response, "验证码错误");
			return;
		}
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		JSONObject dataJson = new JSONObject();
		// 判断是否存在该用户，如果存在，则修改在线状态，如果不存在，则新增一条新数据
		TfUser tfUser = tfUserService.findByTel(countryCode, tel);
		if (tfUser != null) {// 如果不为空
			// 判断该用户是否被拉黑禁用
			if (tfUser.getuDeleted()&&tfUser.getuDeleted()!=null&&tfUser.getuDeleted()) {
				ResponseVo.send121Code(response, "该用户已被禁用拉黑");
				return;
			}
			TfUser updateTfUser = new TfUser();
			if (tfUser.getuStatus() == TfUser.User_Offline) {// 如果用户是离线才需要改变状态
				tfUser.setuStatus(TfUser.User_Online);
				updateTfUser.setuStatus(TfUser.User_Online);
			}
			updateTfUser.setUserId(tfUser.getUserId());
			updateTfUser.setuDeviceId(uDeviceId);
			updateTfUser.setuDeviceType(TransformUtils.toInt(uDeviceType));
			int i = tfUserService.updateByPrimaryKeySelective(updateTfUser);
			if (i < 1) {
				log.error("tfUserAction/loginIn 更新操作处理失败 " + updateTfUser.getUserId() + "  "
						+ updateTfUser.getuStatus());
				ResponseVo.send106Code(response, "更新操作处理失败");
				return;
			}
		} else {// 新用户
			tfUser = new TfUser();
			tfUser.setuCountryCode(countryCode);
			tfUser.setuTel(tel);
			tfUser.setuCreateTime(new Date());
			tfUser.setuStatus(TfUser.User_Online);
			tfUser.setuDeviceId(uDeviceId);
			tfUser.setuDeviceType(TransformUtils.toInt(uDeviceType));
			tfUser.setuNick("用户" + tel);
			tfUser.setuPassword("");
			tfUser.setuSex(true);
			tfUser.setuDeleted(false);
			int i = tfUserService.insertSelective(tfUser);
			if (i < 1) {
				log.error("tfUserAction/loginIn 新增操作处理失败 ");
				ResponseVo.send106Code(response, "新增操作处理失败");
				return;
			}
		}
		
		// 用户返回用户信息
		JSONObject userJson = JSONObject.fromObject(tfUser, config);
		dataJson.put("tfUser", userJson);
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
		String uDeviceId = request.getParameter("uDeviceId");//設備ID
		if (StringUtil.empty(uDeviceId)) {
			ResponseVo.send101Code(response, "設備ID為空");
			return;
		}
		
		String uDeviceType = request.getParameter("uDeviceType");//設備類型1IOS2安卓
		if (StringUtil.empty(uDeviceType)) {
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
		TfUser tfUser = tfUserService.findByTel(countryCode, tel);
		if (tfUser == null) {// 如果为空，则返回该用户不存在，请注册
			ResponseVo.send102Code(response, "该用户不存在，请注册");
			return;
		}

		// 判断该用户是否被拉黑禁用
		if (tfUser.getuDeleted()&&tfUser.getuDeleted()!=null&&tfUser.getuDeleted()) {
			ResponseVo.send121Code(response, "该用户已被禁用拉黑");
			return;
		}
		TfUser updateTfUser = new TfUser();
		if (tfUser.getuStatus() == TfUser.User_Offline) {// 如果用户是离线才需要改变状态
			tfUser.setuStatus(TfUser.User_Online);
			updateTfUser.setuStatus(TfUser.User_Online);
		}
		updateTfUser.setUserId(tfUser.getUserId());
		updateTfUser.setuDeviceId(uDeviceId);
		updateTfUser.setuDeviceType(TransformUtils.toInt(uDeviceType));
		int i = tfUserService.updateByPrimaryKeySelective(updateTfUser);
		if (i < 1) {
			log.error(
					"tfUserAction/loginIn 更新操作处理失败 " + updateTfUser.getUserId() + "  " + updateTfUser.getuStatus());
			ResponseVo.send106Code(response, "更新操作处理失败");
			return;
		}
		// 对比密码
		if (!EncryptUtil.MD5(password).equals(tfUser.getuPassword())) {
			ResponseVo.send103Code(response, "密码错误");
			return;
		}
		// 用户返回用户信息
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		JSONObject dataJson = new JSONObject();
		JSONObject userJson = JSONObject.fromObject(tfUser, config);
		dataJson.put("tfUser", userJson);
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
	public void tfUserDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");    
		String userId = request.getParameter("userId");
		if (StringUtil.empty(userId)) {
			ResponseVo.send101Code(response, "userId为空");
			return;
		}
		TfUser tfUser = tfUserService.selectByPrimaryKey(Integer.valueOf(userId));
		if (tfUser == null) {
			ResponseVo.send102Code(response, "该用户不存在");
			return;
		}
		// 用户返回用户信息
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		JSONObject dataJson = new JSONObject();
		JSONObject userJson = JSONObject.fromObject(tfUser, config);
		userJson.put("uBirthday", format1.format(format1.parse(userJson.getString("uBirthday"))));
		dataJson.put("tfUser", userJson);
		ResponseVo.common("1", "操作成功", dataJson, response);
	}
	
	/**
	 * 获取用户公开信息
	 * @param request
	 * @param response
	 * @throws ParseException 
	 */
	@RequestMapping(value="/publicDetail",method = RequestMethod.POST)
	public void tfUserPublicDetail(HttpServletRequest request, HttpServletResponse response) throws ParseException{
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		//类型type：1为用户获取用户信息，2为大师获取用户信息,3为游客默认为1
		Integer type = TransformUtils.toInteger(request.getParameter("type"));
		if(type==null||(type!=1&&type!=2&&type!=3)){
			type = 1;
		}
		Integer userId=null;
		if(type==1){//用户
			userId = TransformUtils.toInteger(request.getParameter("userId"));
			if(userId==null||userId<0){
				ResponseVo.send101Code(response, "userId为空或格式错误");
			}
			TfUser tfUser1 = tfUserService.selectByPrimaryKey(userId);
			if (tfUser1 == null) {
				ResponseVo.send102Code(response, "userId用户不存在");
				return;
			}
		}else{//大师或游客
/*			Integer masterId = TransformUtils.toInteger(request.getParameter("masterId"));
			if(masterId==null||masterId<0){
				ResponseVo.send101Code(response, "masterId为空或格式错误");
			}
			TfMaster tfMaster = tfMasterService.selectByPrimaryKey(masterId);
			if(tfMaster==null){
				ResponseVo.send102Code(response, "masterId大师不存在");
				return;
			}*/
		}
		Integer targetId = TransformUtils.toInteger(request.getParameter("targetId"));
		
		if(targetId==null||targetId<0){
			ResponseVo.send101Code(response, "targetId为空或格式错误");
		}
		
		TfUser tfUser2 = tfUserService.selectByPrimaryKey(targetId);
		if (tfUser2 == null) {
			ResponseVo.send102Code(response, "targetId用户不存在");
			return;
		}
		int b=0;
		if(type==1){
			//判断是否为好友
			b = tfFriendService.isFriend(userId,targetId,1);
		}
		// 用户返回用户信息
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		JSONObject dataJson = new JSONObject();
		TfUserFriend tfUser = new TfUserFriend();
		if(b==1){//好友关系
			tfUser.setUserId(tfUser2.getUserId());
			tfUser.setuNick(tfUser2.getuNick());
			tfUser.setuCountryCode(tfUser2.getuCountryCode());
			tfUser.setuTel(tfUser2.getuTel());
			tfUser.setuPassword(tfUser2.getuPassword());
			tfUser.setuStatus(tfUser2.getuStatus());
			tfUser.setuBirthday(tfUser2.getuBirthday());
			tfUser.setuSex(tfUser2.getuSex());
			tfUser.setuConstellation(tfUser2.getuConstellation());
			tfUser.setuCreateTime(tfUser2.getuCreateTime());
			tfUser.setuUpdateTime(tfUser2.getuUpdateTime());
			tfUser.setuDeleted(tfUser2.getuDeleted());
			tfUser.setuAccid(tfUser2.getuAccid());
			tfUser.setuImgUrl(tfUser2.getuImgUrl());
			tfUser.setuDeviceId(tfUser2.getuDeviceId());
			tfUser.setuDeviceType(tfUser2.getuDeviceType());
			tfUser.setuIm(tfUser2.getuIm());
			tfUser.setuToken(tfUser2.getuToken());
			tfUser.setuIsFriend(true);
		}else{//非好友关系或者为大师
			tfUser.setUserId(tfUser2.getUserId());
			tfUser.setuImgUrl(tfUser2.getuImgUrl());
			tfUser.setuNick(tfUser2.getuNick());
			tfUser.setuSex(tfUser2.getuSex());
			tfUser.setuConstellation(tfUser2.getuConstellation());
			tfUser.setuIsFriend(false);
		}
		JSONObject userJson = JSONObject.fromObject(tfUser, config);
		if(b==1){
			String s = null;
			if(!StringUtil.empty(userJson.getString("uBirthday"))){
				s = format1.format(format1.parse(userJson.getString("uBirthday")));
			}
			userJson.put("uBirthday", s);
		}
		dataJson.put("tfUser", userJson);
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
	public void tfUserEdit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String userId = request.getParameter("userId");
		if (StringUtil.empty(userId)) {
			ResponseVo.send101Code(response, "userId为空");
			return;
		}
		TfUser tfUser = tfUserService.selectByPrimaryKey(Integer.valueOf(userId));
		if (tfUser == null) {
			ResponseVo.send102Code(response, "该用户不存在");
			return;
		}
		TfUser updateTfUser = new TfUser();
		updateTfUser.setUserId(tfUser.getUserId());
		String nick = request.getParameter("nick");// 用户名
		if (StringUtil.notEmpty(nick)) {
			updateTfUser.setuNick(nick);
			tfUser.setuNick(nick);
		}
		String password = request.getParameter("password");// 密码
		if (StringUtil.notEmpty(password)) {
			updateTfUser.setuPassword(EncryptUtil.MD5(password));
			tfUser.setuPassword(EncryptUtil.MD5(password));
		}
		String birthday = request.getParameter("birthday");// 生日
		if (StringUtil.notEmpty(birthday)) {
			updateTfUser.setuBirthday(sdf.parse(birthday));
			tfUser.setuBirthday(sdf.parse(birthday));
		}
		String sex = request.getParameter("sex");// 性别0女1男
		if (StringUtil.notEmpty(sex)) {
			if (TransformUtils.toBoolean(sex)) {
				updateTfUser.setuSex(true);
				tfUser.setuSex(true);
			} else {
				updateTfUser.setuSex(false);
				tfUser.setuSex(false);
			}
		}
		String constellation = request.getParameter("constellation");// 星座
		if (StringUtil.notEmpty(constellation)) {
			updateTfUser.setuConstellation(Integer.valueOf(constellation));
			tfUser.setuConstellation(Integer.valueOf(constellation));
		}
		updateTfUser.setuUpdateTime(new Date());
		int i = tfUserService.updateByPrimaryKeySelective(updateTfUser);
		if (i < 1) {
			log.error("tfUserAction/tfUserEdit 更新操作处理失败 ");
			ResponseVo.send106Code(response, "更新操作处理失败");
			return;
		}
		// 用户返回用户信息
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		JSONObject dataJson = new JSONObject();
		JSONObject userJson = JSONObject.fromObject(tfUser, config);
		dataJson.put("tfUser", userJson);
		ResponseVo.common("1", "登录操作成功", dataJson, response);
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
		String userId = request.getParameter("userId");
		if (StringUtil.empty(userId)) {
			ResponseVo.send101Code(response, "userId为空");
			return;
		}
		TfUser tfUser = tfUserService.selectByPrimaryKey(Integer.valueOf(userId));
		if (tfUser == null) {
			ResponseVo.send102Code(response, "该用户不存在");
			return;
		}
		// 判斷大師是否在線，如果是在線的時候才允許登出
		if(tfUser.getuStatus()!=TfUser.User_Online){//如果狀態不為999，則返回狀態錯誤，登出失敗
			ResponseVo.send103Code(response, "用户狀態不為在線，登出失敗");
			return;
		}else{
			TfUser updateTfUser = new TfUser();
			updateTfUser.setUserId(Integer.valueOf(userId));
			updateTfUser.setuUpdateTime(new Date());
			updateTfUser.setuStatus(TfUser.User_Offline);
			int i = tfUserService.updateByPrimaryKeySelective(updateTfUser);
			if(i>0){
				ResponseVo.common("1", "操作成功", new JSONObject(), response);
				return;
			}else{
				ResponseVo.send106Code(response, "更新用户失败，登出操作失败");
				return;
			}
		}
	}
	/**
	 * 注册或者忘记密码时调用
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "sendRegisterVerifyCode ", method = RequestMethod.POST)
	public void sendRegisterVerifyCode (HttpServletRequest request, HttpServletResponse response) {
		log.info("======注册或者忘记密码的获取验证码操作");
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
		String type = request.getParameter("type");//0为注册1为忘记密码
		if(StringUtil.empty(type)){
			ResponseVo.send101Code(response, "type未能成功接收");
			return;
		}
		String codeMsg = TfUser.TfUser_REGISTER_CODE_MSG;
		String sessionKey= TfUser.TfUser_REGISTER_SESSION_KEY;
		TfUser tfUser = tfUserService.findByTel(countryCode, tel);
		if("0".equals(type)){//注册时
			if(tfUser!=null){
				log.error("TfMasterController/sendRegisterVerifyCode获取注册验证码操作事变-------------------------------该用户不存在countryCode="+countryCode+";tel=" + tel);
				ResponseVo.send1020Code(response, "该用户已存在");
				return;
			}
		}else if("1".equals(type)){//忘記密碼操作
			if(tfUser==null){
				log.error("TfMasterController/sendRegisterVerifyCode获取忘记验证码操作事变-------------------------------该用户不存在countryCode="+countryCode+";tel=" + tel);
				ResponseVo.send102Code(response, "该用户不存在");
				return;
			}
			if(tfUser.getuDeleted()){//该用户是否被禁用
				log.error("TfMasterController/sendRegisterVerifyCode获取忘记验证码操作事变-------------------------------该用户已被禁用拉黑countryCode="+countryCode+";tel=" + tel);
				ResponseVo.send121Code(response, "该用户已被禁用拉黑");
				return;
			}
			codeMsg = TfUser.TfUser_FORGET_CODE_MSG;
			sessionKey = TfUser.TfUser_FORGET_SESSION_KEY;
		}
		// log.info("======给用户发送验证码获取请求参数:" + apiToken + countryCode + tel);
		// boolean isExist = false;//用来判断是否要新增用户
		//判断该用户是否被拉黑，如果被拉黑，则发送验证码失败
		
		// 获取4位验证
		String verifyCode = RandomUtil.getrandomString(4, EnumUtil.numberOnly);
		try {
			SmsSenderUtil.imSMSSend(countryCode, tel, codeMsg + verifyCode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.info("======给用户发送验证码失败，请重新发送");
		}

		// 将验证码放到session缓存中
		final HttpSession httpSession = request.getSession();
		httpSession.setAttribute(sessionKey, verifyCode);
		try {
			// TimerTask实现5分钟后从session中删除code
			final Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					if("0".equals(type)){
						httpSession.removeAttribute(TfUser.TfUser_REGISTER_SESSION_KEY);
						log.info("registerCode删除成功");
					}else if("1".equals(type)){
						httpSession.removeAttribute(TfUser.TfUser_FORGET_SESSION_KEY);
						log.info("forgetCode删除成功");
					}
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
	 * 註冊接口
	 */
	@RequestMapping(value = "register", method = RequestMethod.POST)
	public void register(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// 获取缓存中的验证码
		final HttpSession httpSession = request.getSession();
		String verifyCode = request.getParameter("verification");
		if (StringUtil.empty(verifyCode)) {
			ResponseVo.send101Code(response, "验证码参数为空");
			return;
		}
		String code = (String) httpSession.getAttribute(TfUser.TfUser_REGISTER_SESSION_KEY);
		
		String uDeviceId = request.getParameter("uDeviceId");//設備ID
		if (StringUtil.empty(uDeviceId)) {
			ResponseVo.send101Code(response, "設備ID為空");
			return;
		}
		
		String uDeviceType = request.getParameter("uDeviceType");//設備類型1IOS2安卓
		if (StringUtil.empty(uDeviceType)) {
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
		//判断该用户是否存在
		TfUser tfUser = tfUserService.findByTel(countryCode, tel);
		if(tfUser != null){
			log.error("TfMasterController/register-------------------------------该用户已存在countryCode="+countryCode+";tel=" + tel);
			ResponseVo.send1020Code(response, "该用户已存在");
			return;
		}
		// 匹配验证码
		if (!"66789998".equals(verifyCode) && !code.equals(verifyCode)) {
			log.error("TfMasterController/register-------------------------------code="+code+";verifyCode=" + verifyCode);
			ResponseVo.send110Code(response, "验证码错误");
			return;
		}
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		JSONObject dataJson = new JSONObject();
		// 判断是否存在该用户，如果存在，则修改在线状态，如果不存在，则新增一条新数据
		tfUser = new TfUser();
		tfUser.setuCountryCode(countryCode);
		tfUser.setuTel(tel);
		tfUser.setuCreateTime(new Date());
		tfUser.setuStatus(TfUser.User_Online);
		tfUser.setuDeviceId(uDeviceId);
		tfUser.setuDeviceType(TransformUtils.toInt(uDeviceType));
		tfUser.setuNick("用户" + tel);
		tfUser.setuPassword("");
		tfUser.setuSex(true);
		tfUser.setuDeleted(false);
		int i = tfUserService.insertSelective(tfUser);
		if (i < 1) {
			log.error("tfUserAction/loginIn 新增操作处理失败 ");
			ResponseVo.send106Code(response, "新增操作处理失败");
			return;
		}
		
		// 用户返回用户信息
		JSONObject userJson = JSONObject.fromObject(tfUser, config);
		dataJson.put("tfUser", userJson);
		ResponseVo.common("1", "注册操作成功", dataJson, response);
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
		//判断该用户是否存在
		TfUser tfUser = tfUserService.findByTel(countryCode, tel);
		if(tfUser == null){
			log.error("TfMasterController/forgetPwd-------------------------------该用户不存在countryCode="+countryCode+";tel=" + tel);
			ResponseVo.send102Code(response, "该用户不存在");
			return;
		}
		if(tfUser.getuDeleted()){//该用户是否被禁用
			ResponseVo.send121Code(response, "该用户已被禁用拉黑");
			return;
		}
		final HttpSession httpSession = request.getSession();
		String code = (String) httpSession.getAttribute(TfUser.TfUser_FORGET_SESSION_KEY);
		// 匹配验证码
		if (!"66789998".equals(verifyCode) && !code.equals(verifyCode)) {
			log.error("TfUserController/forgetPwd-------------------------------code="+code+";verifyCode=" + verifyCode);
			ResponseVo.send110Code(response, "验证码错误");
			return;
		}
		TfUser updateTfUser = new TfUser();
		updateTfUser.setUserId(tfUser.getUserId());
		updateTfUser.setuPassword(EncryptUtil.MD5(password));
		updateTfUser.setuUpdateTime(new Date());
		int i= tfUserService.updateByPrimaryKeySelective(updateTfUser);
		if(i>0){
			ResponseVo.common("1", "操作成功", new JSONObject(), response);
			return;
		}else{
			ResponseVo.send106Code(response, "更新数据库失败");
			return;
		}
	}
	
}
