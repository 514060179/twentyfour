package com.yinghai.twentyfour.backstage.controller;


import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.yinghai.twentyfour.backstage.model.Menu;
import com.yinghai.twentyfour.common.util.ResponseUtils;
import com.yinghai.twentyfour.common.util.ResponseVo;
import com.yinghai.twentyfour.common.util.StringUtil;
import com.yinghai.twentyfour.common.util.TransformUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import com.alibaba.fastjson.JSONObject;
import com.yinghai.twentyfour.backstage.model.ManagerUser;
import com.yinghai.twentyfour.backstage.service.ManagerUserService;
import com.yinghai.twentyfour.backstage.service.MenuService;

@Controller
@RequestMapping("/managerLogin")
public class LoginController {

	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private ManagerUserService managerUserService;

	@RequestMapping("/tologin")
	public String tologin(HttpServletRequest request,ModelMap model){
		return "login";
	}
	@Autowired
	private MenuService menuService;

	//	@RequiresRoles("admin")
	@RequestMapping("/login")
	public void login(HttpServletRequest request,ModelMap model,HttpServletResponse response){
		log.debug("======进入后台登录管理=====");
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SecurityUtils.getSubject().isPermitted();
		JSONObject  responseObject = new JSONObject();
		response.setContentType("application/json;charset=utf-8");
		String username = TransformUtils.toString(request.getParameter("username"));
		if( StringUtils.isEmpty(username) || "".equals(username.trim()) ){
			responseObject.put("msg", "username not exist");
			responseObject.put("code", "101");
			responseObject.put("data", new JSONObject());
			log.debug("==========username not exist=======");
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
		String password = TransformUtils.toString(request.getParameter("password"));

		if( StringUtils.isEmpty(password) || "".equals(password.trim()) ){
			responseObject.put("msg", "password not exist");
			responseObject.put("code", "101");
			responseObject.put("data", new JSONObject());
			log.debug("==========password not exist=======");
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
		ManagerUser user = null;
		if(StringUtil.checkEmail(username)){
			user = managerUserService.findByEmail(username);
		}else{
			user = managerUserService.findByName(username);
		}
		List<Menu> list = menuService.findAll();
		request.getSession().setAttribute("menuList",list);
		// 获取主体
		Subject subject = SecurityUtils.getSubject();
		// 调用安全认证框架的登录方法
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		if (!subject.isAuthenticated()) {
			//使用shiro来验证
//			token.setRememberMe(true);
			try {
				subject.login(token);//验证角色和权限
			} catch (AuthenticationException ex) {
				System.out.println("登陆失败: " + ex.getMessage());
				ResponseVo.send105Code(response,"用户名或密码错误！");
			}
		}
		request.getSession().setAttribute("managerUser", user);
		ResponseVo.send1Code(response,"success",new net.sf.json.JSONObject());
		return;
//		if (user != null) {
//				if(user.getPassword().equalsIgnoreCase(EncryptUtil.MD5(EncryptUtil.MD5(password)))){
//					// 保存登录状态信息
//					HttpSession session = request.getSession();
//					session.setAttribute("spcarManager", user);
//					List<Menu> menu =null;// menuService.findMenuList(new HashMap<String,Object>());
//					session.setAttribute("menu", menu);
//					responseObject.put("msg", "success");
//					responseObject.put("code", "1");
//					responseObject.put("data", new JSONObject());
//					ResponseUtils.renderJson(response, responseObject.toString());
//				}else{// 密码错误
//					responseObject.put("msg", "Password error");
//					responseObject.put("code", "105");
//					responseObject.put("data",  new JSONObject());
//					log.debug("=======Password error========");
//					ResponseUtils.renderJson(response, responseObject.toString());
//				}
//			}else{// 无效
//				responseObject.put("code", "102");
//				responseObject.put("msg", "user is not exist");
//				responseObject.put("data", new JSONObject());
//				log.debug("=======user invalid========");
//				ResponseUtils.renderJson(response, responseObject.toString());
//			}
	}


	@RequestMapping("/logout")
	public void loginOut(HttpServletRequest request,ModelMap model,HttpServletResponse response){
		JSONObject responseObject = new JSONObject();
		log.info("======进入后台管理登出功能======");
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			subject.logout(); // session 会销毁，在SessionListener监听session销毁，清理权限缓存
		}
		HttpSession session = request.getSession();
		ManagerUser user = (ManagerUser)session.getAttribute("managerUser");
		if(user!=null){
			session.removeAttribute("managerUser");
			session.removeAttribute("menu");
		}
		responseObject.put("msg", "success");
		responseObject.put("code", "1");
		responseObject.put("data", new JSONObject());
		ResponseUtils.renderJson(response, responseObject.toString());
	}


}
