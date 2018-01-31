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

import com.yinghai.twentyfour.app.service.HistoryService;
import com.yinghai.twentyfour.common.model.TfHistoryHelper;
import com.yinghai.twentyfour.common.util.JsonDateValueProcessor;
import com.yinghai.twentyfour.common.util.Page;
import com.yinghai.twentyfour.common.util.ResponseVo;
import com.yinghai.twentyfour.common.util.StringUtil;
import com.yinghai.twentyfour.common.util.TransformUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 历史记录
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/app/history")
public class TfHistoryController {
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private HistoryService tfHistoryService;
	/**
	 * 历史记录列表
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/list",method = RequestMethod.POST)
	public void getListByPage(HttpServletRequest request,HttpServletResponse response){
		logger.info("======获取历史记录列表======");
		try {
            request.setCharacterEncoding("utf-8");
		}catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
		//获取参数:用户id
		String id = request.getParameter("userId");
		if(StringUtil.empty(id)){
			ResponseVo.send101Code(response, "userId为空");
			return;
		}
		String num = request.getParameter("page");
		Integer pageNum = (TransformUtils.toInteger(num)==null||TransformUtils.toInteger(num)<1)?1:TransformUtils.toInteger(num);
		String size = request.getParameter("pageSize");
		Integer pageSize = (TransformUtils.toInteger(size)==null||TransformUtils.toInteger(size)<1)?10:TransformUtils.toInteger(size);
		Integer userId = null;
		try {
			userId = Integer.valueOf(id);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			ResponseVo.send104Code(response, "userId格式有误");
			return;
		}
		Integer start = (pageNum-1)*pageSize;
		Page<TfHistoryHelper> historys = tfHistoryService.getHistorysByPage(userId, start, pageSize);
		JSONArray obj = new JSONArray();
		JsonConfig config = new JsonConfig();  
        JsonDateValueProcessor jsonValueProcessor = new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss");  
        config.registerJsonValueProcessor(Date.class, jsonValueProcessor);
        obj = JSONArray.fromObject(historys, config);
        ResponseVo.send1Code(response, "success", obj);
	}
	
}
