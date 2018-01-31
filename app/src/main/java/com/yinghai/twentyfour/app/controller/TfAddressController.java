package com.yinghai.twentyfour.app.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yinghai.twentyfour.app.service.TfAddressService;
import com.yinghai.twentyfour.common.model.TfAddress;
import com.yinghai.twentyfour.common.model.TfUser;
import com.yinghai.twentyfour.common.service.TfUserService;
import com.yinghai.twentyfour.common.util.JsonDateValueProcessor;
import com.yinghai.twentyfour.common.util.Page;
import com.yinghai.twentyfour.common.util.ResponseVo;
import com.yinghai.twentyfour.common.util.StringUtil;
import com.yinghai.twentyfour.common.util.TransformUtils;

import jxl.common.Logger;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Controller
@RequestMapping("/app/address")
public class TfAddressController {
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private TfUserService tfUserService;
	@Autowired
	private TfAddressService tfAddressService;
	
	//新增或修改地址记录
	@RequestMapping(value="/createOrUpdate",method=RequestMethod.POST)
	public void updateOrAddAddress(HttpServletRequest request,HttpServletResponse response){
		logger.info("TfAddressController/updateOrAddAddress======新增或修改地址记录======");
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//接收参数：addressId,userId,type(类型1只修改默认地址2只修改地址信息),name,tel,countryCode,details,street
		Integer userId = TransformUtils.toInteger(request.getParameter("userId"));
		if(userId==null){
			ResponseVo.send101Code(response, "userId为空");
			return;
		}
		if(userId<0){
			if(userId==-1){
				ResponseVo.send800Code(response, "当前为游客，请登录");
				return;
			}
			ResponseVo.send104Code(response, "userId格式错误");
			return;
		}
		Integer addressId = TransformUtils.toInteger(request.getParameter("addressId"));
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		if(addressId==null){//新增地址
			TfAddress a = new TfAddress();
			TfUser u = tfUserService.findUserById(userId);
			if(u==null){
				ResponseVo.send102Code(response, "用户不存在");
				return;
			}
			String name = request.getParameter("name");
			String tel = request.getParameter("tel");
			String countryCode = request.getParameter("countryCode");
			String street = request.getParameter("street");
			String detail = request.getParameter("details");
			Integer sex = TransformUtils.toInteger(request.getParameter("sex"));
			if(sex==null||sex!=0){
				sex = 1;
			}
			if(StringUtil.empty(name)){
				ResponseVo.send101Code(response, "name为空");
				return;
			}
			if(StringUtil.empty(tel)){
				ResponseVo.send101Code(response, "tel为空");
				return;
			}
			if(StringUtil.empty(countryCode)){
				ResponseVo.send101Code(response, "countryCode为空");
				return;
			}
			if(StringUtil.empty(street)){
				ResponseVo.send101Code(response, "street为空");
				return;
			}
			if(StringUtil.empty(detail)){
				ResponseVo.send101Code(response, "detail为空");
				return;
			}
			a.setAsUserId(userId);
			a.setAsConsigneeName(name);
			a.setAsTel(tel);
			a.setAsCountryCode(countryCode);
			a.setAsDetails(detail);
			a.setAsStreet(street);
			a.setAsCreateTime(new Date());
			a.setAsSex(sex==1?true:false);
			List<TfAddress> l = tfAddressService.findAddressByUserId(userId);
			if(l==null||l.size()==0){
				a.setAsDefault(true);
			}
			int i = tfAddressService.createAddress(a);
			if(i!=1){
				ResponseVo.send106Code(response, "数据出错，新增地址失败");
				return;
			}
			TfAddress address = tfAddressService.findAddressById(a.getAddressId());
			JSONArray jsonData = JSONArray.fromObject(address, config);
			JSONObject obj = new JSONObject();
			obj.put("address", jsonData);
			ResponseVo.send1Code(response, "success", obj);
		}else{
			if(addressId<0){
				ResponseVo.send104Code(response, "addressId格式错误");
			}
			//判断该地址是否存在
			TfAddress a1 = tfAddressService.findAdressById(userId,addressId);
			if(a1==null){
				ResponseVo.send102Code(response, "该收货地址不存在");
				return;
			}
			//修改地址
			Integer type = TransformUtils.toInteger(request.getParameter("type"));
			if(type==null){
				ResponseVo.send101Code(response, "type为空");
				return;
			}
			if(type==1){//修改默认地址
				if(a1.getAsDefault()){
					JSONArray jsonData = JSONArray.fromObject(a1, config);
					JSONObject obj = new JSONObject();
					obj.put("address", jsonData);
					ResponseVo.send1Code(response, "success", obj);
					return;
				}else{//取消原有的默认地址，并将此地址更新为默认地址
					TfAddress a3 = new TfAddress();
					a3.setAddressId(a1.getAddressId());
					a3.setAsDefault(true);
					a3.setAsUpdateTime(new Date());
					int j=0;
					try {
						j = tfAddressService.updateDefaultAddress(userId,a3);
					} catch (Exception e) {
						e.printStackTrace();
						ResponseVo.send106Code(response, "修改默认地址失败");
						return;
					}
					if(j!=1){
						ResponseVo.send106Code(response, "修改默认地址失败");
						return;
					}
					a1.setAsDefault(true);
				}
			}else if(type==2){//修改地址信息
				TfAddress a4 = new TfAddress();
				a4.setAddressId(a1.getAddressId());
				String name = request.getParameter("name");
				String tel = request.getParameter("tel");
				String countryCode = request.getParameter("countryCode");
				String street = request.getParameter("street");
				String detail = request.getParameter("details");
				Integer sex = TransformUtils.toInteger(request.getParameter("sex"));
				if(sex==null||sex!=0){
					sex=1;
				}
				a4.setAsSex(sex==1?true:false);
				a1.setAsSex(sex==1?true:false);
				if(!StringUtil.empty(name)){
					a4.setAsConsigneeName(name);
					a1.setAsConsigneeName(name);
				}
				if(!StringUtil.empty(tel)){
					a4.setAsTel(tel);
					a1.setAsTel(tel);
				}
				if(!StringUtil.empty(countryCode)){
					a4.setAsCountryCode(countryCode);;
					a1.setAsCountryCode(countryCode);;
				}
				if(!StringUtil.empty(street)){
					a4.setAsStreet(street);;
					a1.setAsStreet(street);;
				}
				if(!StringUtil.empty(detail)){
					a4.setAsDetails(detail);;
					a1.setAsDetails(detail);;
				}
				Date d1 = new Date();
				a4.setAsUpdateTime(d1);
				int k = tfAddressService.updateAddress(a4);
				if(k!=1){
					ResponseVo.send106Code(response, "修改地址信息失败");
					return;
				}
				a1.setAsUpdateTime(d1);
			}else{
				ResponseVo.send114Code(response, "修改类型错误");
				return;
			}
			JSONArray jsonData = JSONArray.fromObject(a1, config);
			JSONObject obj = new JSONObject();
			obj.put("address", jsonData);
			ResponseVo.send1Code(response, "success", obj);
		}
	}
	/**
	 * 删除地址记录
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public void deleteAddress(HttpServletRequest request,HttpServletResponse response){
		logger.info("TfAddressController/deleteAddress======删除地址记录======");
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//获取参数：地址id
		String uId = request.getParameter("userId");
		String aId = request.getParameter("addressId");
		if(StringUtil.empty(aId)){
			ResponseVo.send101Code(response, "地址ID为空");
			return;
		}
		if(StringUtil.empty(uId)){
			ResponseVo.send101Code(response, "userId为空");
			return;
		}
		Integer addressId = TransformUtils.toInteger(aId);
		if(addressId<0){
			ResponseVo.send104Code(response, "地址ID格式错误");
			return;
		}
		Integer userId = TransformUtils.toInteger(uId);
		if(userId<0){
			if(userId==-1){
				ResponseVo.send800Code(response, "当前为游客，请登录");
				return;
			}
			ResponseVo.send104Code(response, "userId格式错误");
			return;
		}
		//删除地址
		TfAddress address=null;
		try {
			address = tfAddressService.deleteAddress(addressId,userId);
		} catch (Exception e) {
			e.printStackTrace();
			ResponseVo.send106Code(response, "数据出错，删除收货地址失败");
			return;
		}
		if(address==null){
			ResponseVo.send102Code(response, "收货地址不存在");;
			return;
		}
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		JSONArray jsonData = JSONArray.fromObject(address, config);
		JSONObject obj = new JSONObject();
		obj.put("address", jsonData);
		ResponseVo.send1Code(response, "success", obj);
	}
	
	/**
	 * 获取地址列表(分页获取)
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/list",method=RequestMethod.POST)
	public void getAddressList(HttpServletRequest request,HttpServletResponse response){
		logger.info("TfAddressController/getAddressList======获取地址列表======");
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String num = request.getParameter("page");
		String size = request.getParameter("pageSize");
		Integer pageNum = (num!=null&&TransformUtils.toInteger(num)>0)?TransformUtils.toInteger(num):1;
		Integer pageSize = (size!=null&&TransformUtils.toInteger(size)>0)?TransformUtils.toInteger(size):10;
		String uId = request.getParameter("userId");
		if(StringUtil.empty(uId)){
			ResponseVo.send101Code(response, "用户ID为空");
			return;
		}
		Integer userId = TransformUtils.toInteger(uId);
		if(userId<0){
			if(userId==-1){
				ResponseVo.send800Code(response, "当前为游客，请登录");
				return;
			}
			ResponseVo.send104Code(response, "用户ID格式错误");
			return;
		}
		TfUser user = tfUserService.selectByPrimaryKey(userId);
		if(user==null){
			ResponseVo.send102Code(response, "用户不存在");
			return;
		}
		Page<TfAddress> page = tfAddressService.findAddressByPage(pageNum,pageSize,userId);
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		JSONArray jsonData = JSONArray.fromObject(page, config);
		JSONObject obj = new JSONObject();
		obj.put("address", jsonData);
		ResponseVo.send1Code(response, "success", obj);
	}
	
}
