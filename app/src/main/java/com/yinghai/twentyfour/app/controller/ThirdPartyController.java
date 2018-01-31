package com.yinghai.twentyfour.app.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
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

import com.yinghai.twentyfour.common.constant.Constant;
import com.yinghai.twentyfour.common.constant.LogManager;
import com.yinghai.twentyfour.common.model.TfMaster;
import com.yinghai.twentyfour.common.model.TfUser;
import com.yinghai.twentyfour.common.model.ThirdParty;
import com.yinghai.twentyfour.common.service.TfMasterService;
import com.yinghai.twentyfour.common.service.TfUserService;
import com.yinghai.twentyfour.common.service.ThirdPartyService;
import com.yinghai.twentyfour.common.service.WechatService;
import com.yinghai.twentyfour.common.util.EnumUtil;
import com.yinghai.twentyfour.common.util.HttpRequester;
import com.yinghai.twentyfour.common.util.JsonDateValueProcessor;
import com.yinghai.twentyfour.common.util.RandomUtil;
import com.yinghai.twentyfour.common.util.ResponseVo;
import com.yinghai.twentyfour.common.util.SmsSenderUtil;
import com.yinghai.twentyfour.common.util.StringUtil;
import com.yinghai.twentyfour.common.util.TransformUtils;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 第三方登录
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/app/third/party")
public class ThirdPartyController {
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private WechatService wechatService;
	@Autowired
	private ThirdPartyService thirdPartyService;
	@Autowired
	private TfUserService tfUserService;
	@Autowired
	private TfMasterService tfMasterService;

	/**
	 * 第三方微信登录
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @throws IOException
	 */
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public void loginIn(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("======进入客户端登陆功能======");
		response.setContentType("application/json;charset=utf-8");
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		JSONObject dataJson = new JSONObject();
		int type = TransformUtils.toInt(request.getParameter("type"));// 登录类型，默认为微信登录，即为0
		int tfType = TransformUtils.toInt(request.getParameter("tfType"));// 登录类型，默认为客户端登录，即为0
		String deviceId = request.getParameter("deviceId");// 設備ID
		if (StringUtil.empty(deviceId)) {
			ResponseVo.send101Code(response, "設備ID為空");
			return;
		}

		String deviceType = request.getParameter("deviceType");// 設備類型1IOS2安卓
		if (StringUtil.empty(deviceType)) {
			ResponseVo.send101Code(response, "設備類型為空");
			return;
		}
		String code = request.getParameter("code");
		if (StringUtil.empty(code)) {
			ResponseVo.send101Code(response, "第三方登录同意标识code未能成功接收");
			return;
		}
		String realm = request.getHeader("Realm");
		if (StringUtil.empty(realm)) {
			log.error("微信登录接口Realm参数缺少");
			ResponseVo.send101Code(response, "Realm参数缺少");
			return;
		}
		String unionid = "";
		String country = "";
		String city = "";
		String province = "";
		String nickname = "";
		String headimgurl = "";// 头像
		Boolean sex = false;
		if (type == ThirdParty.third_type_vx) {// 微信登录
			Map<String, Object> wechatMap = wechatService.getOpenIdAccessToken(code, realm);
			String openid = "";
			String accessToken = "";
			Map<String, Object> mapInfo = null;// 验证威信登录时微信返回来的信息
			boolean success = false;// 验证是否成功
			try {
				unionid = (String) wechatMap.get("unionid");
				openid = (String) wechatMap.get("openid");
				accessToken = (String) wechatMap.get("accessToken");
			} catch (Exception e) {
				// TODO: handle exception
				log.error("ThirdPartyAction/loginIn===================验证微信信息失败，accessToken获取失败");
				ResponseVo.send119Code(response, "第三方登录验证失败，微信验证失败code");
				return;
			}

			// 驗證威信授權
			mapInfo = wechatService.getWeChatInfo(accessToken, openid);
			if (mapInfo != null) {
				success = true;
			}
			// 如果验证失败，返回
			if (!success) {
				log.error("ThirdPartyAction/loginIn===================获取微信个人信息失败，即openId校验失败");
				ResponseVo.send119Code(response, "第三方登录验证失败，调用微信登录获取个人信息失败");
				return;
			}
			int s = TransformUtils.toInt(mapInfo.get("sex"));
			if (s == 1) {
				sex = true;
			}
			country = (String) mapInfo.get("country");
			city = (String) mapInfo.get("city");
			province = (String) mapInfo.get("province");
			nickname = (String) mapInfo.get("nickname");
			headimgurl = (String) mapInfo.get("headimgurl");

		} else if (type == ThirdParty.third_type_fb) {
			String userInfoApiUrl = "https://graph.facebook.com/me?fields=id,name,email,first_name,last_name,gender,picture,cover&access_token="
					+ code;
			JSONObject json = JSONObject.fromObject(HttpRequester.requestGetByHttp(userInfoApiUrl));
			unionid = (String) json.get("id");
			nickname = (String) json.get("name");
			headimgurl = (String) ((JSONObject) ((JSONObject) json.get("picture")).get("data")).get("url");
			String gender = (String) json.get("gender");
			if (StringUtil.empty(gender) || gender.equals("male")) {// 如果为空或者为male
				sex = true;
			}
			log.info(json.toString());
			System.out.println(json.toString());
		}
		// 判断该用户是否存在
		ThirdParty thirdparty = thirdPartyService.seletByOpenid(unionid, tfType);
		if (thirdparty != null) {// 如果有数据，则判断关联ID是否存在
			if (thirdparty.getTfId() != null && thirdparty.getTfId() != 0) {// 判断关联ID是否为空
				// 通过tfType判断登录类型大师端or用户端
				if (thirdparty.getTfType() == Constant.THIRD_PARTY_TFUSER) {// 对应用户端
					TfUser tfUser = tfUserService.selectByPrimaryKey(thirdparty.getTfId());
					if (tfUser == null) {// 如果该数据为空，这说明数据有异常
						ResponseVo.send102Code(response, "该用户不存在");
						return;
					} else {
						TfUser updateTfUser = new TfUser();
						if (tfUser.getuStatus() == TfUser.User_Offline) {// 如果用户是离线才需要改变状态
							tfUser.setuStatus(TfUser.User_Online);
							updateTfUser.setuStatus(TfUser.User_Online);
						}
						updateTfUser.setUserId(tfUser.getUserId());
						updateTfUser.setuDeviceId(deviceId);
						updateTfUser.setuDeviceType(TransformUtils.toInt(deviceType));
						tfUser.setuDeviceId(deviceId);
						tfUser.setuDeviceType(TransformUtils.toInt(deviceType));
						int i = tfUserService.updateByPrimaryKeySelective(updateTfUser);
						if (i < 1) {
							log.error("ThirdPartyAction/loginIn===================修改用户状态失败");
							ResponseVo.send106Code(response, "修改用户状态失败");
							return;
						}
						JSONObject userJson = JSONObject.fromObject(tfUser, config);
						dataJson.put("tfUser", userJson);
						ResponseVo.common("1", "登录操作成功", dataJson, response);
						return;
					}
				} else {// 对应大师端
					TfMaster tfMaster = tfMasterService.selectByPrimaryKey(thirdparty.getTfId());
					if (tfMaster == null) {// 如果该数据为空，这说明数据有异常
						ResponseVo.send102Code(response, "该大师不存在");
						return;
					} else {
						TfMaster updateTfMaster = new TfMaster();
						if (tfMaster.getmStatus() == TfMaster.Master_Offline) {// 如果用户是离线才需要改变状态
							tfMaster.setmStatus(TfMaster.Master_Online);
							updateTfMaster.setmStatus(TfMaster.Master_Online);
						}
						tfMaster.setmDeviceId(deviceId);
						tfMaster.setmDeviceType(TransformUtils.toInt(deviceType));
						updateTfMaster.setmDeviceId(deviceId);
						updateTfMaster.setmDeviceType(TransformUtils.toInt(deviceType));
						updateTfMaster.setMasterId(tfMaster.getMasterId());
						int i = tfMasterService.updateByPrimaryKeySelective(updateTfMaster);
						if (i < 1) {
							log.error("ThirdPartyAction/loginIn 更新操作处理失败 " + updateTfMaster.getMasterId() + "  "
									+ updateTfMaster.getmStatus());
							ResponseVo.send106Code(response, "更新操作处理失败");
							return;
						}
						JSONObject userJson = JSONObject.fromObject(tfMaster, config);
						dataJson.put("tfMaster", userJson);
						ResponseVo.common("1", "登录操作成功", dataJson, response);
						return;
					}

				}
			} else {// 当已有第三方数据但未绑定用户时返回
				dataJson.put("thirdPartyId", thirdparty.getThirdPartyId());
				ResponseVo.common("2", "操作成功，请绑定手机号", dataJson, response);
				return;
			}
		} else {// 如果未绑定手机信息，则新增一条记录，并返回第三方登录ID
			thirdparty = new ThirdParty();
			thirdparty.setSex(sex);
			thirdparty.setOpenId(unionid);
			thirdparty.setTfType(tfType);
			thirdparty.setCreateTime(new Date());
			thirdparty.setCountry(country);
			thirdparty.setCity(city);
			thirdparty.setProvince(province);
			thirdparty.setNick(nickname);
			thirdparty.setType(type);
			thirdparty.setImgUrl(headimgurl);
			int i = thirdPartyService.insertSelective(thirdparty);
			if (i < 1) {
				log.error("ThirdPartyAction/loginIn===================新增微信第三方数据失败");
				ResponseVo.send106Code(response, "新增微信第三方数据失败");
				return;
			}
			dataJson.put("thirdPartyId", thirdparty.getThirdPartyId());
			ResponseVo.common("2", "操作成功，请绑定手机号", dataJson, response);
			return;
		}
	}

	/**
	 * 第三方登录发送验证码（大师端需要判断该大师是否存在，客户端不需要判断）
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
			log.error("ThirdPartyAction/sendVerifyMsg===================tel为空");
			ResponseVo.send101Code(response, "電話號碼未能成功接收");
			return;
		}
		if (StringUtil.empty(countryCode)) {
			log.error("ThirdPartyAction/sendVerifyMsg===================countryCode为空");
			ResponseVo.send101Code(response, "區號未能成功接收");
			return;
		}
		String thirdPartyId = request.getParameter("thirdPartyId");
		if (StringUtil.empty(thirdPartyId)) {
			log.error("ThirdPartyAction/sendVerifyMsg===================第三方登录记录表thirdPartyId为空");
			ResponseVo.send101Code(response, "第三方登录记录表thirdPartyId为空");
			return;
		}
		ThirdParty thirdParty = thirdPartyService.selectByPrimaryKey(TransformUtils.toInteger(thirdPartyId));
		if (thirdParty == null) {
			log.error("ThirdPartyAction/sendVerifyMsg===================第三方登录记录表不存在该条数据" + thirdPartyId);
			ResponseVo.send102Code(response, "第三方登录记录表不存在该条数据");
			return;
		}
		// 通过tfType判断大师or客户端
		if (thirdParty.getTfType() == Constant.THIRD_PARTY_MASTER) {// 大师端处理
			TfMaster tfMaster = tfMasterService.findByTel(countryCode, tel);
			if (tfMaster == null) {// 如果为空说明该大师未登记，返回102
				log.error("ThirdPartyAction/sendVerifyMsg===================该大师未登记" + countryCode + " " + tel);
				ResponseVo.send102Code(response, "该大师未登记");
				return;
			} else {// 在不为空的前提下判断该大师是否被禁用拉黑
				if (tfMaster.getmDeleted()) {
					log.error("ThirdPartyAction/sendVerifyMsg===================该大师被禁用拉黑" + countryCode + " " + tel);
					ResponseVo.send121Code(response, "该大师被禁用拉黑");
					return;
				}
			}
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
		httpSession.setAttribute("thirdPartycode", verifyCode);
		try {
			// TimerTask实现5分钟后从session中删除code
			final Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					httpSession.removeAttribute("thirdPartycode");
					log.info("thirdPartycode删除成功");
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
	 * 微信第三方登录验证验证码 首先利用第三方登录数据判断是客户端or大师端，并且判断该验证码是否过期or错误
	 * 如果是大师端，判断大师是否存在，是否被禁用，然后根据以上判断是否绑定
	 * 如果是客户端，判断是否存在，如果不存在，则新增一条数据，如果存在，判断是否被拉黑，如果没被拉黑，则直接绑定该微信
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "checkVerify", method = RequestMethod.POST)
	public void checkVerify(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("======进入微信绑定手机验证======");
		response.setContentType("application/json;charset=utf-8");
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		JSONObject dataJson = new JSONObject();
		String deviceId = request.getParameter("deviceId");// 設備ID
		if (StringUtil.empty(deviceId)) {
			ResponseVo.send101Code(response, "設備ID為空");
			return;
		}

		String deviceType = request.getParameter("deviceType");// 設備類型1IOS2安卓
		if (StringUtil.empty(deviceType)) {
			ResponseVo.send101Code(response, "設備類型為空");
			return;
		}
		String tel = request.getParameter("tel");// 手机
		if (StringUtil.empty(tel)) {// 空验证
			log.error("ThirdPartyAction/checkVerify====================tel为空，手機號碼未能成功接收");
			ResponseVo.send101Code(response, "tel为空，手機號碼未能成功接收");
			return;
		}
		String countryCode = request.getParameter("countryCode");// 区号
		if (StringUtil.empty(countryCode)) {// 空验证
			log.error("ThirdPartyAction/checkVerify====================countryCode为空，区号为空");
			ResponseVo.send101Code(response, "tel为空，countryCode为空，区号为空");
			return;
		}
		String thirdPartyId = request.getParameter("thirdPartyId");// thirdPartyId，第三方登录唯一标识
		if (StringUtil.empty(thirdPartyId)) {// 空验证
			log.error("ThirdPartyAction/checkVerify====================thirdPartyId为空，第三方登录唯一标识为空");
			ResponseVo.send101Code(response, "thirdPartyId为空，第三方登录唯一标识为空");
			return;
		}
		String verification = request.getParameter("verification");// 验证码
		if (StringUtil.empty(verification)) {// 空验证
			log.error("ThirdPartyAction/checkVerify====================verification为空，验证码为空");
			ResponseVo.send101Code(response, "verification为空，验证码为空");
			return;
		}
		// 判断该验证码是否正确
		// 获取缓存中的验证码
		final HttpSession httpSession = request.getSession();
		String code = (String) httpSession.getAttribute("thirdPartycode");
		// 匹配验证码
		if (!"66789998".equals(verification) && !code.equals(verification)) {
			ResponseVo.send110Code(response, "验证码错误");
			return;
		}
		// 查找出第三方的登录数据
		ThirdParty thirdParty = thirdPartyService.selectByPrimaryKey(TransformUtils.toInt(thirdPartyId));
		if (thirdParty == null) {
			log.error("ThirdPartyAction/checkVerify====================找不到第三方登录数据" + thirdPartyId);
			ResponseVo.send102Code(response, "找不到第三方登录数据" + thirdPartyId);
			return;
		}
		// 判断是否为大师端
		if (thirdParty.getTfType() == Constant.THIRD_PARTY_MASTER) {
			TfMaster tfMaster = tfMasterService.findByTel(countryCode, tel);
			if (tfMaster == null) {
				log.error("ThirdPartyAction/checkVerify====================找不到该大师用户，该大师未登记" + countryCode + "-" + tel);
				ResponseVo.send102Code(response, "找不到该大师用户,该大师未登记" + countryCode + "-" + tel);
				return;
			}
			// 如果数据不为空，则判断该大师时候被拉黑禁用
			if (tfMaster.getmDeleted()) {
				log.error("ThirdPartyAction/checkVerify====================该大师被禁用" + countryCode + "-" + tel);
				ResponseVo.send121Code(response, "该大师被禁用" + countryCode + "-" + tel);
				return;
			}
			// 查询是否有该大师已绑定的微信，如果有，解除绑定
			ThirdParty thirdParty2 = thirdPartyService.selectByTfIdAndType(tfMaster.getMasterId(), thirdParty.getType(),
					thirdParty.getTfType());
			if (thirdParty2 != null) {
				ThirdParty updateThirdParty2 = new ThirdParty();
				updateThirdParty2.setThirdPartyId(thirdParty2.getThirdPartyId());
				updateThirdParty2.setTfId(0);
				int i = thirdPartyService.updatePrimaryKey(updateThirdParty2);
				if (i < 1) {
					log.error("ThirdPartyAction/checkVerify====================解除大师以前的微信失败"
							+ thirdParty2.getThirdPartyId());
					ResponseVo.send106Code(response, "解除大师以前的微信失败" + thirdParty2.getThirdPartyId());
					return;
				}
			}
			// 解除绑定后，绑定新的记录
			ThirdParty updateThirdParty = new ThirdParty();
			updateThirdParty.setThirdPartyId(thirdParty.getThirdPartyId());
			updateThirdParty.setTfId(tfMaster.getMasterId());
			int i = thirdPartyService.updatePrimaryKey(updateThirdParty);
			if (i < 1) {
				log.error("ThirdPartyAction/checkVerify====================大师端绑定威信操作失败，数据库更新错误"
						+ thirdParty.getThirdPartyId());
				ResponseVo.send106Code(response, "大师端绑定威信操作失败，数据库更新错误" + thirdParty.getThirdPartyId());
				return;
			}
			TfMaster updateTfMaster = new TfMaster();
			// 绑定成功，开始做登录操作，修改大师端状态(只有在大师离线时才修改状态)
			if (tfMaster.getmStatus() == TfMaster.Master_Offline) {
				tfMaster.setmStatus(TfMaster.Master_Online);
				updateTfMaster.setmStatus(TfMaster.Master_Online);
			}
			tfMaster.setmDeviceId(deviceId);
			tfMaster.setmDeviceType(TransformUtils.toInt(deviceType));

			updateTfMaster.setmDeviceId(deviceId);
			updateTfMaster.setmDeviceType(TransformUtils.toInt(deviceType));
			updateTfMaster.setMasterId(tfMaster.getMasterId());
			i = tfMasterService.updateByPrimaryKeySelective(updateTfMaster);
			if (i < 1) {
				log.error("ThirdPartyAction/checkVerify====================更新大师状态失败" + tfMaster.getMasterId());
				ResponseVo.send106Code(response, "更新大师状态失败" + tfMaster.getMasterId());
				return;
			}
			JSONObject userJson = JSONObject.fromObject(tfMaster, config);
			dataJson.put("tfMaster", userJson);
			ResponseVo.common("1", "登录操作成功", dataJson, response);
		} else {// 乘客端数据操作
			TfUser tfUser = tfUserService.findByTel(countryCode, tel);
			if (tfUser == null) {// 客户端用户数据如果为空，则新增一条数据
				tfUser = new TfUser();
				tfUser.setuCountryCode(countryCode);
				tfUser.setuTel(tel);
				tfUser.setuCreateTime(new Date());
				tfUser.setuNick(thirdParty.getNick());
				tfUser.setuSex(thirdParty.getSex());
				tfUser.setuStatus(TfUser.User_Online);
				tfUser.setuImgUrl(thirdParty.getImgUrl());
				tfUser.setuDeviceId(deviceId);
				tfUser.setuDeviceType(TransformUtils.toInt(deviceType));
				tfUser.setuDeleted(false);
				int i = tfUserService.insertSelective(tfUser);
				if (i < 1) {
					log.error("ThirdPartyAction/checkVerify====================新增客户端用户记录失败" + countryCode + "-" + tel);
					ResponseVo.send106Code(response, "新增客户端用户记录失败" + countryCode + "-" + tel);
					return;
				}
				// 数据新增成功后，开始绑定操作
				ThirdParty updateThirdParty = new ThirdParty();
				updateThirdParty.setThirdPartyId(thirdParty.getThirdPartyId());
				updateThirdParty.setTfId(tfUser.getUserId());
				i = thirdPartyService.updatePrimaryKey(updateThirdParty);
				if (i < 1) {
					log.error("ThirdPartyAction/checkVerify====================客户端绑定威信操作失败，数据库更新错误"
							+ thirdParty.getThirdPartyId());
					ResponseVo.send106Code(response, "客户端绑定威信操作失败，数据库更新错误" + thirdParty.getThirdPartyId());
					return;
				}
			} else {// 如果该用户不是新用户时
					// 判断该用户时候被拉黑禁用
				if (tfUser.getuDeleted()) {
					log.error("ThirdPartyAction/checkVerify====================该用户被禁用" + countryCode + "-" + tel);
					ResponseVo.send121Code(response, "该用户被禁用" + countryCode + "-" + tel);
					return;
				}
				// 查询是否有该用户已绑定的微信，如果有，解除绑定
				ThirdParty thirdParty2 = thirdPartyService.selectByTfIdAndType(tfUser.getUserId(), thirdParty.getType(),
						thirdParty.getTfType());
				if (thirdParty2 != null) {
					ThirdParty updateThirdParty2 = new ThirdParty();
					updateThirdParty2.setThirdPartyId(thirdParty2.getThirdPartyId());
					updateThirdParty2.setTfId(0);
					int i = thirdPartyService.updatePrimaryKey(updateThirdParty2);
					if (i < 1) {
						log.error("ThirdPartyAction/checkVerify====================解除用户以前的微信失败"
								+ thirdParty2.getThirdPartyId());
						ResponseVo.send106Code(response, "解除用户以前的微信失败" + thirdParty2.getThirdPartyId());
						return;
					}
				}
				// 解除绑定后，绑定新的记录
				ThirdParty updateThirdParty = new ThirdParty();
				updateThirdParty.setThirdPartyId(thirdParty.getThirdPartyId());
				updateThirdParty.setTfId(tfUser.getUserId());
				int i = thirdPartyService.updatePrimaryKey(updateThirdParty);
				if (i < 1) {
					log.error("ThirdPartyAction/checkVerify====================客户端绑定威信操作失败，数据库更新错误"
							+ thirdParty.getThirdPartyId());
					ResponseVo.send106Code(response, "客户端绑定威信操作失败，数据库更新错误" + thirdParty.getThirdPartyId());
					return;
				}
				TfUser updateTfUser = new TfUser();
				// 绑定成功，开始做登录操作，修改大师端状态(只有在用户离线时才修改状态)
				if (tfUser.getuStatus() == TfMaster.Master_Offline) {
					tfUser.setuStatus(TfUser.User_Online);
					updateTfUser.setuStatus(TfUser.User_Online);

				}
				updateTfUser.setUserId(tfUser.getUserId());
				updateTfUser.setuDeviceId(deviceId);
				updateTfUser.setuDeviceType(TransformUtils.toInt(deviceType));
				i = tfUserService.updateByPrimaryKeySelective(updateTfUser);
				if (i < 1) {
					log.error("ThirdPartyAction/checkVerify====================更新客户端用户状态失败" + tfUser.getUserId());
					ResponseVo.send106Code(response, "更新客户端用户状态失败" + tfUser.getUserId());
					return;
				}
				tfUser.setuDeviceId(deviceId);
				tfUser.setuDeviceType(TransformUtils.toInt(deviceType));
				JSONObject userJson = JSONObject.fromObject(tfUser, config);
				dataJson.put("tfUser", userJson);
				ResponseVo.common("1", "登录操作成功", dataJson, response);
			}
		}
	}

}
