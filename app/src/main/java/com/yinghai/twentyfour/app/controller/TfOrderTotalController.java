package com.yinghai.twentyfour.app.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yinghai.twentyfour.app.model.TotalOrderParamEntity;
import com.yinghai.twentyfour.app.service.TfAddressService;
import com.yinghai.twentyfour.app.service.TfCarService;
import com.yinghai.twentyfour.app.service.TfOrderTotalService;
import com.yinghai.twentyfour.common.model.TfAddress;
import com.yinghai.twentyfour.common.model.TfCar;
import com.yinghai.twentyfour.common.model.TfCarHelper;
import com.yinghai.twentyfour.common.model.TfOrderTotal;
import com.yinghai.twentyfour.common.model.TfProduct;
import com.yinghai.twentyfour.common.model.TfUser;
import com.yinghai.twentyfour.common.service.TfUserService;
import com.yinghai.twentyfour.common.util.JsonDateValueProcessor;
import com.yinghai.twentyfour.common.util.ResponseVo;
import com.yinghai.twentyfour.common.util.StringUtil;
import com.yinghai.twentyfour.common.util.TransformUtils;

import jxl.common.Logger;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 购物车总订单
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/app/orderTotal")
public class TfOrderTotalController {
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private TfCarService tfCarService;
	@Autowired
	private TfOrderTotalService tfOrderTotalService;
	@Autowired
	private TfUserService tfUserService;
	@Autowired
	private TfAddressService tfAddressService;
	/**
	 * 商品订单生成总订单
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/generateTotalOrder",method=RequestMethod.POST)
	public void generateTotalOrder(HttpServletRequest request,HttpServletResponse response){
		logger.info("======生成购物车总订单======");
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//获取参数：用户ID，购物车记录ID组(JSON字符串形式)
		String uid = request.getParameter("userId");
		if(StringUtil.empty(uid)){
			logger.error("TfOrderTotalController/generateTotalOrder============userId为空");
			ResponseVo.send101Code(response, "userId为空");
			return;
		}
		String carIds = request.getParameter("carIds");//整形数组
		if(StringUtil.empty(carIds)){
			logger.error("TfOrderTotalController/generateTotalOrder==========carIds为空");
			ResponseVo.send101Code(response, "carIds为空");
			return; 
		}
		String regex = "^[1-9][0-9]*;([1-9][0-9]*;)*$";
		if(!carIds.matches(regex)){
			logger.error("TfOrderTotalController/generateTotalOrder==========carIds格式错误");
			ResponseVo.send104Code(response, "carIds格式错误");
			return;
		}
		String qs = request.getParameter("quantities");
		if(StringUtil.empty(qs)){
			logger.error("TfOrderTotalController/generateTotalOrder==========quantitys为空");
			ResponseVo.send101Code(response, "quantitys为空");
			return; 
		}
		if(!qs.matches(regex)){
			logger.error("TfOrderTotalController/generateTotalOrder==========quantitys格式错误");
			ResponseVo.send104Code(response, "quantitys格式错误");
			return;
		}
		Integer userId = TransformUtils.toInteger(uid);
		if(userId<0){
			logger.error("TfOrderTotalController/generateTotalOrder==========userId格式错误");
			ResponseVo.send104Code(response, "userId格式错误");
			return;
		}
		Integer addressId = TransformUtils.toInteger(request.getParameter("addressId"));
		if(addressId==null||addressId<0){
			ResponseVo.send101Code(response, "addressId");
			return;
		}
		TfAddress address = tfAddressService.findAddressByUserId(userId,addressId);
		if(address==null){
			ResponseVo.send102Code(response, "对应地址不存在");
			return;
		}
		//判断该用户是否存在
		TfUser u = tfUserService.findUserById(userId);
		if(u==null){
			logger.error("TfOrderTotalController/generateTotalOrder==========该用户不存在");
			ResponseVo.send102Code(response, "该用户不存在");
			return;
		}
		String[] cars = carIds.split(";");
		String[] quantities = qs.split(";");
		if(cars.length!=quantities.length){
			logger.error("TfOrderTotalController/generateTotalOrder==========carIds和quantitys格式错误");
			ResponseVo.send104Code(response, "carIds和quantitys数据不匹配");
			return;
		}
		Map<Integer,Integer> sum = new HashMap<Integer,Integer>();//总价
		Map<Integer,Integer> qty = new HashMap<Integer,Integer>();//总数量
		Integer ss = 0;
		Integer qq = 0;
		//遍历判断购物车记录是否存在、商品是否下架、数量是否超过库存
		Map<Integer,List<TotalOrderParamEntity>> map = new HashMap<Integer,List<TotalOrderParamEntity>>();//按大师分类，key为大师id，value为对应的子订单
		List<TotalOrderParamEntity> lists = new ArrayList<TotalOrderParamEntity>();
		for(int i=0;i<cars.length;i++){
			Integer carId = TransformUtils.toInteger(cars[i]);
			Integer quantity = TransformUtils.toInteger(quantities[i]);
			//根据购物车记录ID查询购物车记录及详细信息
			TfCarHelper carInfo = tfCarService.findCarInfoByCarId(carId);
			if(carInfo==null){
				logger.error("TfOrderTotalController/generateTotalOrder==========购物车记录不存在"+carId);
				ResponseVo.send102Code(response, "购物车记录不存在");
				return;
			}
			if(carInfo.getProduct()==null){
				logger.error("TfOrderTotalController/generateTotalOrder==========对应商品不存在");
				ResponseVo.send102Code(response, "对应商品不存在");
				return;
			}else{
				TfProduct p = carInfo.getProduct();
				if(p.getpDelete()||p.getpOffline()){
					logger.error("TfOrderTotalController/generateTotalOrder==========对应商品已下架或已删除");
					ResponseVo.send116Code(response, "对应商品已下架或已删除");
					return;
				}
				if(p.getpTotal()<quantity){
					logger.error("TfOrderTotalController/generateTotalOrder==========库存不足");
					ResponseVo.send115Code(response, "对应商品库存不足");
					return;
				}
				TotalOrderParamEntity t = new TotalOrderParamEntity();
				t.setAmount(quantity);
				t.setPid(carInfo.getcProductId());
				t.setPrice(p.getpPrice());
				t.setMid(p.getpMasterId());
				t.setStock(p.getpTotal());
				t.setCarId(carInfo.getCarId());
				lists.add(t);
				ss = ss + quantity*p.getpPrice();
				qq = qq + quantity;
				if(map.containsKey(p.getpMasterId())){//已经包含该大师的数据，只要在value中添加一个值即可
					map.get(p.getpMasterId()).add(t);
					if(!sum.containsKey(p.getpMasterId())||!qty.containsKey(p.getpMasterId())){
						logger.error("订单按大师分类数据出错");
						ResponseVo.send106Code(response, "数据出错");
						return;
					}
					sum.put(p.getpMasterId(), sum.get(p.getpMasterId())+quantity*p.getpPrice());
					qty.put(p.getpMasterId(), qty.get(p.getpMasterId())+quantity);
				}else{
					List<TotalOrderParamEntity> list = new ArrayList<TotalOrderParamEntity>();//重新创建一个保存所有子订单的集合
					list.add(t);
					map.put(p.getpMasterId(), list);
					int s = 0;
					sum.put(p.getpMasterId(), s+quantity*p.getpPrice());
					int q = 0;
					qty.put(p.getpMasterId(), q+quantity);
				}

			}
		}
		JSONObject obj = null;
		//生成总订单、生成条目订单、修正库存数
		try {
			TfOrderTotal orderTotal = tfOrderTotalService.createOrder(userId,addressId, sum,qty,ss,qq, map);
			if(orderTotal!=null){
				obj  = new JSONObject();
				JsonConfig config = new JsonConfig();
				JsonDateValueProcessor jsonValueProcessor = new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss");
				config.registerJsonValueProcessor(Date.class, jsonValueProcessor);
				obj = JSONObject.fromObject(orderTotal, config);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("TfOrderTotalController/generateTotalOrder==========数据出错，订单生成失败");
			//更新购物车数量
			boolean success = false;
			for(TotalOrderParamEntity t:lists){
				TfCar car = new TfCar();
				car.setCarId(t.getCarId());
				car.setcQty(t.getAmount());
				int i = 0;
				try {
					i = tfCarService.updateCarRecord(car);
				} catch (Exception e1) {
					success = true;
					break;  
				}
				if(i!=1){
					success = true;
					break;
				}
			}
			if(success){
				logger.error("购物车记录数量更新失败");
			}
			ResponseVo.send106Code(response, "数据出错，订单生成失败!");
			return;
		}
		ResponseVo.send1Code(response, "success", obj);
		return;
	}
}
