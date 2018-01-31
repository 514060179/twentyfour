package com.yinghai.twentyfour.app.interceptor;


import com.yinghai.twentyfour.app.util.ValidateAPITokenUtil;
import com.yinghai.twentyfour.common.util.ResponseVo;
import net.sf.json.JSONObject;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

public class AppRequestInterceptor implements HandlerInterceptor{

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object arg2) throws Exception {
		try {
			req.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String apiSendTime = req.getParameter("apiSendTime");
		String apiToken = req.getParameter("apiToken");
		boolean success= ValidateAPITokenUtil.ValidatingApiToken(apiSendTime, apiToken);
		if(!success){
			ResponseVo.send107Code(res,"api验证失败！");
			return false;
		}
		return true;
	}


}
