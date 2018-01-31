package com.yinghai.twentyfour.app.controller;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yinghai.twentyfour.common.model.TfLuckKey;
import com.yinghai.twentyfour.common.model.TfLuckWithBLOBs;
import com.yinghai.twentyfour.common.model.TfUser;
import com.yinghai.twentyfour.common.service.LuckService;
import com.yinghai.twentyfour.common.service.TfUserService;
import com.yinghai.twentyfour.common.util.JsonDateValueProcessor;
import com.yinghai.twentyfour.common.util.ResponseVo;
import com.yinghai.twentyfour.common.util.StringUtil;
import com.yinghai.twentyfour.common.util.TransformUtils;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 开启运势
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/app/luck")
public class TfLuckController {
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private LuckService tfLuckService;
	@Autowired
	private TfUserService tfUserService;
	/**
	 * 更换运势
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value="/changeLuck",method=RequestMethod.POST)
	public void changeLuck(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		logger.info("======更换运势======");
		try {
            request.setCharacterEncoding("utf-8");
		}catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
		String constellation = request.getParameter("constellation");
		if(StringUtil.empty(constellation)){
			ResponseVo.send101Code(response, "幸运星座为空");
			return;
		}
		Integer lConstellation = TransformUtils.toInteger(constellation);
		if(lConstellation<1){
			ResponseVo.send113Code(response, "幸运星座数据错误");
			return;
		}
		TfLuckKey key = new TfLuckKey();
		key.setlConstellation(lConstellation);
		key.setlDate(new Date());
		TfLuckWithBLOBs luck = tfLuckService.findLuck(key);
		JsonConfig config = new JsonConfig();  
        JsonDateValueProcessor jsonValueProcessor = new JsonDateValueProcessor();  
        config.registerJsonValueProcessor(Date.class, jsonValueProcessor);
        JSONObject obj = new JSONObject();
        obj = JSONObject.fromObject(luck, config);
        ResponseVo.send1Code(response, "success", obj);
	}
	/**
	 * 开启运势 
	 * @param request
	 * @param response
	 */

	@RequestMapping(value="openLuck",method=RequestMethod.POST)
	public void openLuck(HttpServletRequest request,HttpServletResponse response){
		logger.info("======开启运势======");
		try {
            request.setCharacterEncoding("utf-8");
		}catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
		//日期为当天
		String id = request.getParameter("userId");
		Date d = new Date();
		Integer lConstellation = null;
		Integer userId = null;
		if(StringUtil.empty(id)){
			//默认为当天日期对应的星座
			lConstellation = getConstellation(d);
		}else{
			userId = TransformUtils.toInteger(id);
			if(userId==-1){//游客模式
				lConstellation = getConstellation(d);
			}else{
				TfUser user = tfUserService.findUserById(userId);
				if(user==null){
					ResponseVo.send102Code(response, "该用户不存在");
					return;
				}
				lConstellation = user.getuConstellation();
			}
		}
		TfLuckKey key = new TfLuckKey();
		key.setlDate(d);//当天
		key.setlConstellation(lConstellation);
		TfLuckWithBLOBs luck = tfLuckService.findLuck(key);
		if(luck==null){
			ResponseVo.send102Code(response, "该运势信息不存在");
			return;
		}else{
			JsonConfig config = new JsonConfig();  
	        JsonDateValueProcessor jsonValueProcessor = new JsonDateValueProcessor();  
	        config.registerJsonValueProcessor(Date.class, jsonValueProcessor);
	        JSONObject obj = new JSONObject();
	        obj = JSONObject.fromObject(luck, config);
	        ResponseVo.send1Code(response, "success", obj);
		}
		
	}
	
	public Integer getConstellation(Date d){
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		int month = c.get(Calendar.MONTH)+1;
		int day = c.get(Calendar.DATE);
		final int[] constellationArr = {12,1,2,3,4,5,6,7,8,9,10,11};
			//{"水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座","天蝎座", "射手座", "魔羯座" };   
		final int[] constellationEdgeDay = {20, 19, 20, 20, 21, 21, 22, 23, 23, 23, 22,21};  
		 if (day <= constellationEdgeDay[month-1]) {     
	          month = month - 1;     
	       }     
	       if (month > 0) {     
	           return constellationArr[month-1];     
	       }else if(month == 0){
	    	   return constellationArr[constellationArr.length-1];
	       }
	           //default to return 白羊座  
	    return constellationArr[3];     
	}
	
	
	public static void main(String[] args){
//		Calendar c = Calendar.getInstance();
//		c.set(Calendar.YEAR, 2017);
//		c.set(Calendar.MONTH,1);
//		c.set(Calendar.DATE, 30);
//		Date d = c.getTime();
//		System.out.println(getConstellationD(d));
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		System.out.println(sdf.format(d));
	}
	
	
}
