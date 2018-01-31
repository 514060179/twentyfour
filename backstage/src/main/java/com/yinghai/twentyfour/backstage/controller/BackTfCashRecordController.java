package com.yinghai.twentyfour.backstage.controller;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yinghai.twentyfour.common.model.TfCashRecord;
import com.yinghai.twentyfour.common.model.TfCashRecordHelper;
import com.yinghai.twentyfour.common.model.TfMaster;
import com.yinghai.twentyfour.common.service.TfCashRecordService;
import com.yinghai.twentyfour.common.service.TfMasterService;
import com.yinghai.twentyfour.common.service.TfOrderService;
import com.yinghai.twentyfour.common.util.Page;
import com.yinghai.twentyfour.common.util.ResponseVo;
import com.yinghai.twentyfour.common.util.TransformUtils;

import net.sf.json.JSONObject;

/**
 * 后台提现管理
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/admin/cashRecord")
public class BackTfCashRecordController {
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private TfCashRecordService tfCashRecordService;
	@Autowired
	private TfOrderService tfOrderService;
	@Autowired
	private TfMasterService tfMasterService;
	//获取提现记录列表
	@RequestMapping("/list")
	public String getCashRecordList(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		logger.info("BackTfCashRecordController/getCashRecordList======获取提现记录列表======");
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//获取参数：页数、条数，提现记录Id、大师Id、提现状态
		Integer pageNum = TransformUtils.toInteger(request.getParameter("page"));
		Integer pageSize = TransformUtils.toInteger(request.getParameter("pageSize"));
		pageNum = (pageNum!=null&&pageNum>0)?pageNum:1;
		pageSize = (pageSize!=null&&pageSize>0)?pageSize:10;
		Integer recordId = TransformUtils.toInteger(request.getParameter("recordId"));
		Integer masterId = TransformUtils.toInteger(request.getParameter("masterId"));
		Integer status = TransformUtils.toInteger(request.getParameter("status"));
		TfCashRecord record = new TfCashRecord();
		if(recordId!=null){
			record.setRecordId(recordId);
		}
		if(masterId!=null){
			record.setrMasterId(masterId);
		}
		if(status!=null){
			record.setrStatus(status);
		}
		//根据条件查询提现记录
		Page<TfCashRecord> page = tfCashRecordService.findListByPage(pageNum, pageSize, record);
		model.addAttribute("page", page);
		model.addAttribute("record", record);
		model.addAttribute("pageNo", page.getPageNum());
		model.addAttribute("pageSize", page.getPageSize());
		model.addAttribute("recordCount", page.getTotal());
		model.addAttribute("pageCount", page.getPages());
		return "cashRecord/list";
	}
	/**
	 * 查询提现记录详情
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("detail")
	public String getDetail(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		logger.info("BackTfCashRecordController/getDetail======获取提现记录详情======");
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//获取提现记录ID
		Integer recordId = TransformUtils.toInteger(request.getParameter("recordId"));
		TfCashRecordHelper record = tfCashRecordService.findCashRecord(recordId);
		model.addAttribute("record", record);
		return "cashRecord/detail";
	}
	/**
	 * 审核提现
	 * @param request
	 * @param response
	 */ 
	@RequestMapping("audit")
	public String auditCashApplication(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		logger.info("BackTfCashRecordController/getDetail======审核提现申请======");
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Integer recordId = TransformUtils.toInteger(request.getParameter("recordId"));
		Integer masterId = TransformUtils.toInteger(request.getParameter("masterId"));
		Double amount = TransformUtils.toDouble(request.getParameter("amount"), 0.0);
		//判断记录是否存在
		//查询最近一次提现成功记录
		TfCashRecord r1 = tfCashRecordService.findCashRecordByMasterId(masterId);
		Map<String, Object> map = null;
		if(r1==null){
			//根据大师查询上月以前的完成订单金额，按月计算
			map = tfOrderService.findFinishOrderByTime(masterId,null);
		}else{//查询该记录到上个月期间的完成订单，按月计算
			Date d = r1.getrCreateTime();
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			c.set(Calendar.DATE, 1);
			map = tfOrderService.findFinishOrderByTime(masterId,c.getTime());
		}
		double sum = 0.0;
		//计算总额
		if(map!=null){
			System.out.println("======have:"+map.toString());
			Set<Entry<String, Object>> e = map.entrySet();
			Iterator<Entry<String, Object>> i = e.iterator();
			while(i.hasNext()){
				sum += (Double)i.next().getValue();
			}
		}
		//System.out.println("======null");
		model.addAttribute("map", map);
		model.addAttribute("total", sum);
		model.addAttribute("recordId", recordId);
		model.addAttribute("amount", amount);
		model.addAttribute("eq", sum==amount?1:0);
		return "cashRecord/audit";
	}
	/**
	 * 确认申请
	 * @param request
	 * @param response
	 */
	@RequestMapping("/confirm")
	public void confirmRecord(HttpServletRequest request,HttpServletResponse response){
		logger.info("BackTfCashRecordController/confirmRecord======确认审核======");
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Integer masterId = TransformUtils.toInteger(request.getParameter("masterId"));
		Integer recordId = TransformUtils.toInteger(request.getParameter("recordId"));
		if(recordId==null||recordId<0){
			ResponseVo.send101Code(response, "recordId为空");
			return;
		}
		TfCashRecordHelper r = tfCashRecordService.findCashRecord(recordId);
		if(r==null){
			ResponseVo.send102Code(response, "提现记录不存在");
			return;
		}


		if(masterId==null){//确认审核，更新记录状态
			if(r.getrStatus()!=1){
				ResponseVo.send106Code(response, "数据出错");
				return;
			}
			TfCashRecord cr = new TfCashRecord();
			cr.setRecordId(recordId);
			cr.setrStatus(2);//申请通过
			int i=0;
			try {
				i = tfCashRecordService.updateAuditRecord(cr);
			} catch (RuntimeException e) {
				e.printStackTrace();
				ResponseVo.send106Code(response, "数据出错");
				return;
			}
			if(i!=1){
				ResponseVo.send106Code(response, "数据出错");
				return;
			}
			
		}else{
			if(r.getrStatus()!=2){
				ResponseVo.send106Code(response, "数据出错");
				return;
			}
			if(masterId<0){
				ResponseVo.send101Code(response, "masterId为空");
				return;
			}
			//判断大师是否存在
			if(masterId!=r.getrMasterId()){
				ResponseVo.send101Code(response, "masterId与提现记录不符");
				return;
			}
			TfMaster m = tfMasterService.selectByPrimaryKey(masterId);
			if(m==null){
				ResponseVo.send102Code(response, "大师不存在");
				return;
			}
			//更新大师余额，更新记录状态
			int j = 0;
			try {
				j = tfCashRecordService.updateCompleteRecord(r,masterId);
			} catch (RuntimeException e) {
				e.printStackTrace();
				ResponseVo.send106Code(response, "数据出错");
				return;
			}
			if(j!=1){
				ResponseVo.send106Code(response, "数据出错");
				return;
			}
		}
		ResponseVo.send1Code(response, "success", new JSONObject());
	}
}
