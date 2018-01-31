package com.yinghai.twentyfour.backstage.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yinghai.twentyfour.common.model.TfOrder;
import com.yinghai.twentyfour.common.model.TfOrderTotalHelper;
import com.yinghai.twentyfour.common.service.TfOrderService;
import com.yinghai.twentyfour.common.service.TfOrderTotalService;
import com.yinghai.twentyfour.common.util.Page;
import com.yinghai.twentyfour.common.util.StringUtil;
import com.yinghai.twentyfour.common.util.TransformUtils;

import jxl.common.Logger;

/**
 * Created by Administrator on 2017/10/23.
 */
@Controller
@RequestMapping("/admin/order")
public class BackOrderController {
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private TfOrderService tfOrderService;
	@Autowired
	private TfOrderTotalService tfOrderTotalServiceBack;
	/**
	 * 订单列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/list")
	public String getOrderList(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		logger.info("BackOrderController/getOrderList======获取订单管理列表======");
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//获取参数：分页获取参数（页数、条数）；查询条件
		Integer pageNum = TransformUtils.toInteger(request.getParameter("page"));
		Integer pageSize = TransformUtils.toInteger(request.getParameter("pageSize"));
		pageNum = (pageNum!=null&&pageNum>0)?pageNum:1;
		pageSize = (pageSize!=null&&pageSize>0)?pageSize:10;
		TfOrder order = new TfOrder();
		Integer orderId = TransformUtils.toInteger(request.getParameter("orderId"));
		Integer userId = TransformUtils.toInteger(request.getParameter("userId"));
		String orderNo = request.getParameter("orderNo");
		Integer type = TransformUtils.toInteger(request.getParameter("type"));
		Integer payWay = TransformUtils.toInteger(request.getParameter("payWay"));
		Integer orderStatus = TransformUtils.toInteger(request.getParameter("orderStatus"));
		if(orderId!=null&&orderId>0){
			order.setOrderId(orderId);
		}
		if(userId!=null&&userId>0){
			order.setoUserId(userId);
		}
		if(!StringUtil.empty(orderNo)){
			order.setoOrderNo(orderNo);
		}
		if(type!=null&&type>0){
			order.setoOrderType(type);
		}
		if(payWay!=null&&payWay>0){
			order.setoPayWay(payWay);
		}
		if(orderStatus!=null&&orderStatus>0){
			order.setoStatus(orderStatus);
		}
		Page<TfOrder> page = tfOrderService.findBackByPage(pageNum, pageSize, order);
		model.addAttribute("page", page);
		model.addAttribute("pageNo", page.getPageNum());
		model.addAttribute("pageSize", page.getPageSize());
		model.addAttribute("recordCount", page.getTotal());
		model.addAttribute("pageCount", page.getPages());
		model.addAttribute("order", order);
		return "order/list";
	}
	/**
	 * 查看订单详情
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/detail")
	public String getDetail(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		logger.info("BackOrderController/getDetail======查看订单详情======");
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//获取参数：订单id
		Integer orderId = TransformUtils.toInteger(request.getParameter("orderId"));
		Integer type = TransformUtils.toInteger(request.getParameter("type"));
		TfOrder order;
		if(type!=null&&type>0){
			order = tfOrderService.findAllModelByOrderId(orderId,type);
		}else{
			order = tfOrderService.findAllModelByOrderId(orderId,null);
		}
		model.addAttribute("order", order);
		return "order/detail";
	}
	
	
	@RequestMapping("/orderTotalDetail")
	public String getOrderTotalDetail(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		logger.info("BackOrderController/getOrderTotalDetail======查看总订单详情======");
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//获取参数
		String orderTotalNo = request.getParameter("orderTotalNo");
		TfOrderTotalHelper orderTotal = tfOrderTotalServiceBack.findOrderTotalByNo(orderTotalNo);
		model.addAttribute("orderTotal", orderTotal);
		return "order/orderTotalDetail";
	}
}
