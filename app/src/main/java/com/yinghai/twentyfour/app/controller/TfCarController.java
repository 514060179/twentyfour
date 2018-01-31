package com.yinghai.twentyfour.app.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yinghai.twentyfour.app.service.TfCarService;
import com.yinghai.twentyfour.common.model.TfCar;
import com.yinghai.twentyfour.common.model.TfCarHelper;
import com.yinghai.twentyfour.common.model.TfProduct;
import com.yinghai.twentyfour.common.service.TfProductService;
import com.yinghai.twentyfour.common.util.JsonDateValueProcessor;
import com.yinghai.twentyfour.common.util.Page;
import com.yinghai.twentyfour.common.util.ResponseVo;
import com.yinghai.twentyfour.common.util.StringUtil;
import com.yinghai.twentyfour.common.util.TransformUtils;

import jxl.common.Logger;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 购物车
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/app/car")
public class TfCarController {
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private TfCarService tfCarService;
	@Autowired 
	private TfProductService tfProductService;
	/**
	 * 创建购物车记录
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/create",method= RequestMethod.POST)
	public void createCarRecord(HttpServletRequest request,HttpServletResponse response){
		logger.info("======创建购物车记录======");
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//获取参数：用户id，商品id，商品数量
		String uid = request.getParameter("userId");
		String pid = request.getParameter("productId");
		String q = request.getParameter("quantity");
		if(StringUtil.empty(uid)){
			ResponseVo.send101Code(response, "userId为空");
			return;
		}
		if(StringUtil.empty(pid)){
			ResponseVo.send101Code(response, "productId为空");
			return;
		}
		if(StringUtil.empty(q)){
			ResponseVo.send101Code(response, "quantity为空");
			return;
		}
		Integer userId = TransformUtils.toInteger(uid);
		Integer productId = TransformUtils.toInteger(pid);
		Integer quantity = TransformUtils.toInteger(q);
		if(userId<0){
			ResponseVo.send104Code(response, "userId格式错误");
			return;
		}
		if(productId<0){
			ResponseVo.send104Code(response, "productId格式错误");
			return;
		}
		if(quantity<1){
			ResponseVo.send101Code(response, "quantity错误");
			return;
		}
		//判断该商品是否下架或删除
		TfProduct p = tfProductService.findProductByProuductId(productId);
		if(p==null){
			ResponseVo.send102Code(response, "该商品不存在");
			return;
		}
		if(p.getpDelete()||p.getpOffline()){
			ResponseVo.send116Code(response, "该商品已下架或已删除");
			return;
		}
		//判断数量是否大于剩余库存数
		if(quantity>p.getpTotal()){
			ResponseVo.send115Code(response, "数量不能多于库存数量");
			return;
		}
		//当该商品在购物车时，只增加购物车记录中的数量，否则新增购物车记录
		TfCar c = tfCarService.findCarByUserAndProduct(userId, productId);
		if(c!=null){
			//判断总数量是否大于剩余库存数
			if(c.getcQty()>p.getpTotal()||(c.getcQty()+quantity)>p.getpTotal()){
				ResponseVo.send115Code(response, "总数量不能多于库存数量");
				return;
			}
			TfCar car1 = new TfCar();
			car1.setCarId(c.getCarId());
			car1.setcQty(c.getcQty()+quantity);
			car1.setcUpdateTime(new Date());
			int j = tfCarService.updateCarRecord(car1);
			if(j!=1){
				ResponseVo.send106Code(response, "数据出错，加入购物车失败");
				return;
			}
			TfCarHelper car3 = tfCarService.findCarInfoByCarId(c.getCarId());
			JSONObject obj = new JSONObject();
			JsonConfig config = new JsonConfig();
			JsonDateValueProcessor jsonValueProcessor = new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss");  
	        config.registerJsonValueProcessor(Date.class, jsonValueProcessor);
	        JSONObject json = JSONObject.fromObject(car3, config);
	        obj.put("car", json);
			ResponseVo.send1Code(response, "success", obj);
			return;
		}
		TfCar car2 = new TfCar();
		car2.setcUserId(userId);
		car2.setcProductId(productId);
		car2.setcQty(quantity);
		Date d = new Date();
		car2.setcCreateTime(d);
		car2.setcUpdateTime(d);
		int i = tfCarService.createCarRecord(car2);
		if(i!=1){
			ResponseVo.send106Code(response, "数据出错，加入购物车失败");
			return;
		}
		TfCarHelper car4 = tfCarService.findCarInfoByCarId(car2.getCarId());
		JSONObject obj = new JSONObject();
		JsonConfig config = new JsonConfig();
		JsonDateValueProcessor jsonValueProcessor = new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss");  
        config.registerJsonValueProcessor(Date.class, jsonValueProcessor);
        JSONObject json = JSONObject.fromObject(car4, config);
        obj.put("car", json);
		ResponseVo.send1Code(response, "success", obj);
	}
	
	/**
	 * 删除购物车记录
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/delete",method = RequestMethod.POST)
	public void deleteCarRecord(HttpServletRequest request,HttpServletResponse response){
		logger.info("======删除购物车记录======");
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//获取参数：购物车记录id
		String uid = request.getParameter("userId");
		if(StringUtil.empty(uid)){
			ResponseVo.send101Code(response, "userId为空");
			return;
		}
		String cid = request.getParameter("carId");
		if(StringUtil.empty(cid)){
			ResponseVo.send101Code(response, "carId为空");
			return;
		}
		Integer userId = TransformUtils.toInteger(uid);
		if(userId<0){
			ResponseVo.send104Code(response, "userId格式错误");
			return;
		}
		Integer carId = TransformUtils.toInteger(cid);
		if(carId<0){
			ResponseVo.send104Code(response, "carId格式错误");
			return;
		}
		//查询购物车记录
		//TfCar car = tfCarService.findCarRecordByCarId(carId);
		//TfCar car = tfCarService.findCarRecordByCarIdAndUserId(carId,userId);
		TfCar c = new TfCar();
		c.setCarId(carId);
		c.setcUserId(userId);
		TfCarHelper car = tfCarService.findCarInfoBySelective(c);
		if(car==null){
			ResponseVo.send102Code(response, "该购物车记录不存在");
			return;
		}
		int i = tfCarService.deleteCarRecordByCarId(carId);
		if(i!=1){
			ResponseVo.send106Code(response, "数据出错，删除购物车记录失败");
			return;
		}
		JSONObject obj = new JSONObject();
		JsonConfig config = new JsonConfig();
		JsonDateValueProcessor jsonValueProcessor = new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss");  
        config.registerJsonValueProcessor(Date.class, jsonValueProcessor);
        JSONObject json = JSONObject.fromObject(car, config);
        obj.put("car", json);
		ResponseVo.send1Code(response, "success", obj);
	}
	/**
	 * 更新购物车记录
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/update",method = RequestMethod.POST)
	public void updateCarRecord(HttpServletRequest request,HttpServletResponse response){
		logger.info("======修改购物车记录======");
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//获取参数：购物车记录Id，类型(1为增加，0为减少)，更改数量
		String cid = request.getParameter("carId");
		String q = request.getParameter("quantity");
		if(StringUtil.empty(cid)){
			ResponseVo.send101Code(response, "carId为空");
			return;
		}
		if(StringUtil.empty(q)){
			ResponseVo.send101Code(response, "quantity为空");
			return;
		}
		Integer carId = TransformUtils.toInteger(cid);
		Integer quantity = TransformUtils.toInteger(q);
		if(carId<1){
			ResponseVo.send104Code(response, "carId格式错误");
			return;
		}
		if(quantity<1){
			ResponseVo.send101Code(response, "quantity错误");
			return;
		}
		//判断是否有该购物车记录
		TfCar c = tfCarService.findCarRecordByCarId(carId);
		if(c==null){
			ResponseVo.send102Code(response, "该购物车记录不存在");
			return;
		}
		//判断该商品是否下架或删除
		TfProduct p = tfProductService.findProductByProuductId(c.getcProductId());
		if(p==null){
			ResponseVo.send102Code(response, "该商品不存在");
			return;
		}
		if(p.getpDelete()||p.getpOffline()){
			ResponseVo.send116Code(response, "该商品已下架或已删除");
			return;
		}
		TfCar car = new TfCar();
		car.setCarId(c.getCarId());
		if(c.getcQty()>p.getpTotal()||quantity>p.getpTotal()){
			ResponseVo.send115Code(response, "库存不足");
			return;
		}
		car.setcQty(quantity);
		car.setcUpdateTime(new Date());
		int i = tfCarService.updateCarRecord(car);
		if(i!=1){
			ResponseVo.send106Code(response, "数据出错，修改购物车记录失败");
			return;
		}
		ResponseVo.send1Code(response, "success", new JSONObject());
	}
	/**
	 * 分页获取购物车记录列表
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/list",method = RequestMethod.POST)
	public void getListByPage(HttpServletRequest request,HttpServletResponse response){
		logger.info("======获取购物车记录列表======");
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//获取参数：用户id，页数，条数
		String uid = request.getParameter("userId");
		if(StringUtil.empty(uid)){
			ResponseVo.send101Code(response, "userId为空");
			return;
		}
		Integer userId = TransformUtils.toInteger(uid);
		if(userId<1){
			ResponseVo.send104Code(response, "userId格式错误");
			return;
		}
		Integer num = TransformUtils.toInteger(request.getParameter("page"));
		Integer size = TransformUtils.toInteger(request.getParameter("pageSize"));
		Integer pageNum = (num!=null&&num>0)?num:1;
		Integer pageSize = (size!=null&&size>0)?size:10;
		Page<TfCarHelper> page = tfCarService.getCarRecordByPage(pageNum,pageSize,userId);
		JSONArray cars = new JSONArray();
		JsonConfig config = new JsonConfig();
		JsonDateValueProcessor jsonValueProcessor = new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss");  
        config.registerJsonValueProcessor(Date.class, jsonValueProcessor);
        cars = JSONArray.fromObject(page, config);
        ResponseVo.send1Code(response, "success", cars);
	}
	
	
}
