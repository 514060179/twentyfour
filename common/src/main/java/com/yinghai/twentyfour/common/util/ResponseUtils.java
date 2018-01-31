package com.yinghai.twentyfour.common.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HttpServletResponse帮助类
 * 
 * @author liufang
 * 
 */
public final class ResponseUtils{
	public static final Logger log = LoggerFactory
	.getLogger(ResponseUtils.class);

	/**
	 * 发送json。使用UTF-8编码。
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param text
	 *            发送的字符串
	 */
    @SuppressWarnings("null")
	public static void renderJson(HttpServletResponse response, String text){
    	response.setContentType("application/json;charset=UTF-8");
    	response.setHeader("Pragma", "No-cache");
    	response.setHeader("Cache-Control", "no-cache");
    	response.addHeader("Access-Control-Allow-Origin", "*");
    	response.addHeader("Access-Control-Allow-Methods", "POST,GET");
    	response.setDateHeader("Expires", 0);
    	PrintWriter pw = null;
        try{
        	pw = response.getWriter();
        	pw.print(text);
        }catch(IOException e){
        	pw.print("IOException");
            log.error(e.getMessage(), e);
        }finally {
        	pw.flush();
        	pw.close();
		}
    }

	/**
	 * 後台json格式自定義
	 * @param response
	 * @param i
	 */
	public static void backRenderJson(HttpServletResponse response, Integer i){
		JSONObject responseObject = new JSONObject();
		if(i>0){
			responseObject.put("msg", "success");
			responseObject.put("code", "1");
			responseObject.put("data", new JSONObject());
		}else if(i==-1){
			responseObject.put("msg", "系统异常！");
			responseObject.put("code", "-1");
			responseObject.put("data", new JSONObject());
		}else if(i==0){
			responseObject.put("msg", "数据异常！");
			responseObject.put("code", "-1");
			responseObject.put("data", new JSONObject());
		}else if(i==-3){
			responseObject.put("msg", "操作異常！");
			responseObject.put("code", "-1");
			responseObject.put("data", new JSONObject());
		}else{
			responseObject.put("msg", "未知錯誤！");
			responseObject.put("code", "-1");
			responseObject.put("data", new JSONObject());
		}
		renderJson(response, responseObject.toString());
	}
}
