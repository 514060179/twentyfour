package com.yinghai.twentyfour.app.controller;

import com.yinghai.twentyfour.common.vo.MasterSchedule;
import com.yinghai.twentyfour.app.service.TfOrderTotalService;
import com.yinghai.twentyfour.common.im.util.ExpressCompanyUtil;
import com.yinghai.twentyfour.common.model.*;
import com.yinghai.twentyfour.common.service.TfBusinessService;
import com.yinghai.twentyfour.common.service.TfMasterService;
import com.yinghai.twentyfour.common.service.TfOrderService;
import com.yinghai.twentyfour.common.service.TfUserService;
import com.yinghai.twentyfour.common.util.JsonDateValueProcessor;
import com.yinghai.twentyfour.common.util.Page;
import com.yinghai.twentyfour.common.util.ResponseVo;
import com.yinghai.twentyfour.common.util.StringUtil;
import com.yinghai.twentyfour.common.util.TransformUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2017/10/23.
 */
@Controller
@RequestMapping("app/order")
public class TfOrderController {

    Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private TfBusinessService tfBusinessService;
    @Autowired
    private TfOrderService tfOrderService;
    @Autowired
    private TfMasterService tfMasterService;
    @Autowired
    private TfOrderTotalService tfOrderTotalService;
    @Autowired
    private TfUserService tfUserService;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //创建订单 用户端
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public void createOrder(HttpServletRequest request, HttpServletResponse response){

        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        TfOrder tfOrder = new TfOrder();
        TfOrderAttach tfOrderAttach = new TfOrderAttach();

        String appointmentTime = request.getParameter("appointmentTime");//2017-10-23 16:33
        if (StringUtil.empty(appointmentTime)){
            ResponseVo.send101Code(response,"appointmentTime为空！");
            return;
        }
        try {
            Date date = sdf.parse(appointmentTime+":00");
            Date d = new Date();
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            c1.setTime(date);
            c2.setTime(d);
            //判断预约时间是否在当天或之后
            int year1 = c1.get(Calendar.YEAR);
            int year2 = c2.get(Calendar.YEAR);
            if(year1<year2){
            	ResponseVo.send117Code(response, "预约时间段不能早于当前时间");
            	return;
            }else if(year1==year2){
            	int day1 = c1.get(Calendar.DAY_OF_YEAR);
            	int day2 = c2.get(Calendar.DAY_OF_YEAR);
            	if(day1<day2){
            		ResponseVo.send117Code(response, "预约时间段不能早于当前时间");
                	return;
            	}else if(day1==day2){
            		int hour1 = c1.get(Calendar.HOUR_OF_DAY);
            		int hour2 = c2.get(Calendar.HOUR_OF_DAY);
            		if(hour1<=13&&hour2>13){
        				ResponseVo.send117Code(response, "预约时间段不能早于当前时间");
                    	return;
            		}
            	}
            }
            tfOrder.setoAppointmentTime(date);
        } catch (ParseException e) {
            log.error(e);
            ResponseVo.send101Code(response,"appointmentTime格式有误！格式为：yyyy-MM-dd HH:mm");
            e.printStackTrace();
        }
        String businessId = request.getParameter("businessId");
        if (StringUtil.empty(businessId)){
            ResponseVo.send101Code(response,"businessId为空！");
            return;
        }
        try {
            tfOrder.setoBusinessId(Integer.parseInt(businessId));
        } catch (Exception e) {
            log.error(e);
            ResponseVo.send101Code(response,"businessId格式有误！");
            e.printStackTrace();
        }
        String userId = request.getParameter("userId");
        if (StringUtil.empty(userId)){
            ResponseVo.send101Code(response,"userId为空！");
            return;
        }
        try {
            tfOrder.setoUserId(Integer.parseInt(userId));
        } catch (Exception e) {
            log.error(e);
            ResponseVo.send101Code(response,"userId格式有误！");
            e.printStackTrace();
        }
        String masterId = request.getParameter("masterId");
        if (StringUtil.empty(masterId)){
            ResponseVo.send101Code(response,"masterId为空！");
            return;
        }
        try {
            tfOrder.setoMasterId(Integer.parseInt(masterId));
        } catch (Exception e) {
            log.error(e);
            ResponseVo.send101Code(response,"masterId格式有误！");
            e.printStackTrace();
        }
        String name = request.getParameter("name");
        if (StringUtil.empty(name)){
            ResponseVo.send101Code(response,"name为空！");
            return;
        }
        tfOrderAttach.setAhName(name);
        String sex = request.getParameter("sex");
        if (StringUtil.empty(sex)){
            ResponseVo.send101Code(response,"sex为空！");
            return;
        }
        tfOrderAttach.setAhSex(sex.equals("1")?true:false);
        String birthday = request.getParameter("birthday");
        if (StringUtil.empty(birthday)){
            ResponseVo.send101Code(response,"birthday为空！");
            return;
        }
        try {
            tfOrderAttach.setAhBirthday(sdf.parse(birthday+" 00:00:00"));
        } catch (Exception e) {
            log.error(e);
            ResponseVo.send101Code(response,"birthday格式有误！");
            e.printStackTrace();
        }
        if (StringUtil.empty(birthday)){
            ResponseVo.send101Code(response,"birthday为空！");//2017-10-23
            return;
        }
        String describe = request.getParameter("describe");
        if (!StringUtil.empty(describe)){
            tfOrderAttach.setAhDescribe(describe);
        }
        String ahCountryCode = request.getParameter("countryCode");
        if (!StringUtil.empty(ahCountryCode)){
            tfOrderAttach.setAhCountryCode(ahCountryCode);
        }
        String ahTel = request.getParameter("tel");
        if (!StringUtil.empty(ahTel)){
            tfOrderAttach.setAhTel(ahTel);
        }
        TfBusiness tfBusiness = tfBusinessService.findById(Integer.parseInt(businessId),Integer.parseInt(masterId));

        if(tfBusiness==null){
            ResponseVo.send102Code(response,"大师id："+masterId+"的业务id："+businessId+"不存在！");
            return;
        }
        tfOrder.setoOrderNo(sdf.format(new Date()).toString().replace("-","").replace(":","").replace(" ","")+userId+new Random().nextInt(999999)%900000);
        tfOrder.setoPrice(tfBusiness.getbPrice());
        tfOrder.setoAmount(tfBusiness.getbPrice());
        tfOrder.setoCreateTime(new Date());
        TfOrder order = tfOrderService.createOrder(tfOrder,tfOrderAttach);
        TfOrder send = tfOrderService.findAllModelById(order.getOrderId());
        JsonConfig config = new JsonConfig();
        config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
        JSONObject responseJson = new JSONObject();
        responseJson.put("tfOrder",JSONObject.fromObject(send,config));
        ResponseVo.send1Code(response,"success",responseJson);
    }
    /**
     * 更新获取商品订单接口
     * @param request
     * @param response
     */
    @RequestMapping(value="productList",method = RequestMethod.POST)
    public void productList(HttpServletRequest request,HttpServletResponse response){
    	log.info("productList======查询商品订单列表======");
    	TfOrder searchOrder  = new TfOrder();
    	String userId = request.getParameter("userId");
    	String masterId = request.getParameter("masterId");
    	String realm = request.getHeader("Realm");
        if(StringUtil.empty(realm)){
            ResponseVo.send101Code(response,"realm为空!");
            return;
        }
        switch (realm){
        case "user":
            if (StringUtil.empty(userId)){
                ResponseVo.send101Code(response,"userId为空！");
                return;
            }else{
            	if(TransformUtils.toInteger(userId)<-1){
            		ResponseVo.send101Code(response,"userId格式有误！");
            		return;
            	}else if(TransformUtils.toInteger(userId)==-1){
            		ResponseVo.send800Code(response, "游客模式，请登录");
            		return;
            	}
                try {
                    searchOrder.setoUserId(Integer.parseInt(userId));
                } catch (Exception e) {
                    ResponseVo.send101Code(response,"userId格式有误！");
                    e.printStackTrace();
                    return;
                }
            }
            break;
        case "master":
        	if (StringUtil.empty(masterId)){
                ResponseVo.send101Code(response,"masterId为空！");
                return;
            }else{
            	if(TransformUtils.toInteger(masterId)<0){
            		ResponseVo.send101Code(response,"masterId格式有误！");
            		return;
            	}
                try {
                    searchOrder.setoMasterId(Integer.parseInt(masterId));
                } catch (Exception e) {
                    ResponseVo.send101Code(response,"masterId格式有误！");
                    e.printStackTrace();
                    return;
                }
            }
            break;
        default:;
        }
        String statusArray = request.getParameter("statusArray");
        if (!StringUtil.empty(statusArray)){
            String[] array = statusArray.split(";");
            searchOrder.setStatusArray(array);
        }
        String status = request.getParameter("status");
        if (!StringUtil.empty(status)){
            try {
                searchOrder.setoStatus(Integer.parseInt(status));
            }catch (Exception e){
                log.error("分页查询订单时传入status不为int ："+status);
                ResponseVo.send101Code(response,"status值不为int");
                e.printStackTrace();
            }
        }
        searchOrder.setoOrderType(3);//商品订单
        String page = request.getParameter("page");
        if (StringUtil.empty(page)){
            page = "1";
        }
        String pageSize = request.getParameter("pageSize");
        if (StringUtil.empty(pageSize)){
            pageSize = "10";
        }
        //查询商品订单
        try {
			List<TfOrderTotalHelper> list = tfOrderService.selectProductOrderByPage((Integer.parseInt(page)-1)*Integer.parseInt(pageSize),Integer.parseInt(pageSize),searchOrder);
			if (list==null||list.size()<=0){
			    ResponseVo.send1Code(response,"success",new JSONObject());
			    return;
			}
			JsonConfig config = new JsonConfig();
			config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));

			JSONObject responseJson = new JSONObject();
			JSONArray jsonArray = JSONArray.fromObject(list,config);
			responseJson.put("tfOrderList",jsonArray);
			responseJson.put("total",list.size());
			responseJson.put("pages",1);
			responseJson.put("pageSize",pageSize);
			responseJson.put("pageNum",page);
			ResponseVo.send1Code(response,"success",responseJson);
		} catch (Exception e) {
			log.error("分页查询订单时传入page或pageSize不为int ："+page+"/"+pageSize+e.getMessage());
            ResponseVo.send101Code(response,"page值或pageSize值不为int");
            e.printStackTrace();
		}
    }
    
    //获取订单记录（大师端&用户端）
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public void list(HttpServletRequest request, HttpServletResponse response){
        TfOrder searchOrder = new TfOrder();
        String userId = request.getParameter("userId");
        String masterId = request.getParameter("masterId");
        String realm = request.getHeader("Realm");
        if(StringUtil.empty(realm)){
            ResponseVo.send101Code(response,"realm为空!");
            return;
        }
        switch (realm){
            case "master":
                if (StringUtil.empty(masterId)){
                    ResponseVo.send101Code(response,"masterId为空！");
                    return;
                }else{
                    try {
                        searchOrder.setoMasterId(Integer.parseInt(masterId));
                    } catch (Exception e) {
                        ResponseVo.send101Code(response,"masterId格式有误！");
                        log.error(e);
                        e.printStackTrace();
                    }
                }
                break;
            case "user":
                if (StringUtil.empty(userId)){
                    ResponseVo.send101Code(response,"userId为空！");
                    return;
                }else{
                    try {
                        searchOrder.setoUserId(Integer.parseInt(userId));
                    } catch (Exception e) {
                        ResponseVo.send101Code(response,"userId格式有误！");
                        log.error(e);
                        e.printStackTrace();
                    }
                }
            default:;
        }
        String statusArray = request.getParameter("statusArray");
        if (!StringUtil.empty(statusArray)){
            String[] array = statusArray.split(";");
            searchOrder.setStatusArray(array);
        }
        String status = request.getParameter("status");
        if (!StringUtil.empty(status)){
            try {
                searchOrder.setoStatus(Integer.parseInt(status));
            }catch (Exception e){
                log.error("分页查询订单时传入status不为int ："+status);
                ResponseVo.send101Code(response,"status值不为int");
                e.printStackTrace();
            }
        }
        String orderType = request.getParameter("orderType");
        if(!StringUtil.empty(orderType)){
            try {
                searchOrder.setoOrderType(Integer.parseInt(orderType));
            }catch (Exception e){
                log.error("orderType不为int ："+orderType);
                ResponseVo.send101Code(response,"orderType值不为int");
                e.printStackTrace();
            }
        }
        String page = request.getParameter("page");
        if (StringUtil.empty(page)){
            page = "1";
        }else{
        	if(TransformUtils.toInt(page)<1){
        		page = "1";
        	}
        }
        
        String pageSize = request.getParameter("pageSize");
        if (StringUtil.empty(pageSize)){
            pageSize = "10";
        }else{
        	if(TransformUtils.toInt(pageSize)<1){
        		page = "10";
        	}
        }
        String appointmentTime = request.getParameter("appointmentTime");//2017-10-23
        if(!StringUtil.empty(appointmentTime)){
            try {
                Date date = sdf.parse(appointmentTime+" 00:00:00");
                searchOrder.setoAppointmentTime(date);
            } catch (ParseException e) {
                log.error(e);
                ResponseVo.send101Code(response,"请求参数appointmentTime格式有误！格式为：yyyy-MM-dd");
                e.printStackTrace();
            }
        }
        try {
            List<TfOrder> list = tfOrderService.findByPage((Integer.parseInt(page)-1)*Integer.parseInt(pageSize),Integer.parseInt(pageSize),searchOrder);
            if (list==null||list.size()<=0){
                ResponseVo.send1Code(response,"success",new JSONObject());
                return;
            }

            JsonConfig config = new JsonConfig();
            config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));

            JSONObject responseJson = new JSONObject();
            JSONArray jsonArray = JSONArray.fromObject(list,config);
            responseJson.put("tfOrderList",jsonArray);
            responseJson.put("total",list.size());
            responseJson.put("pages",1);
            responseJson.put("pageSize",pageSize);
            responseJson.put("pageNum",page);
            ResponseVo.send1Code(response,"success",responseJson);
        }catch (Exception e){
            log.error("分页查询订单时传入page或pageSize不为int ："+page+"/"+pageSize);
            ResponseVo.send101Code(response,"page值或pageSize值不为int");
            e.printStackTrace();
        }
    }
    //大师端
    @RequestMapping(value = "makeSure", method = RequestMethod.POST)
    public void makeSure(HttpServletRequest request, HttpServletResponse response){

        String orderId = request.getParameter("orderId");
        if(StringUtil.empty(orderId)){
            ResponseVo.send101Code(response,"参数orderId为空！");
            return;
        }
        String masterId = request.getParameter("masterId");
        TfOrder tfOrder = null;
        try {
            tfOrder = tfOrderService.findById(Integer.parseInt(orderId),Integer.parseInt(masterId));
        }catch (Exception e){
            log.error(e);
            ResponseVo.send101Code(response,"orderId不为int");
            e.printStackTrace();
        }
        if(tfOrder==null){
            ResponseVo.send102Code(response,"该订单id不存在：orderId="+orderId);
            return;
        }
        switch (tfOrder.getoStatus()){
            case 1://未支付
            {
                ResponseVo.send504Code(response,"未支付订单无法确定！");
                break;
            }
            case 2://已支付
            {
                int i = tfOrderService.masterMakeSure(Integer.parseInt(orderId));
                if (i>0){
                    ResponseVo.send1Code(response,"success",new JSONObject());
                }else{
                    ResponseVo.send106Code(response,"确定订单操作失败!");
                }
                break;
            }
            default://
            {
                ResponseVo.send505Code(response,"订单已经确定（无法再次确定）");
                break;
            }
        }
    }
    //大师端
    @RequestMapping(value = "schedule", method = RequestMethod.POST)
    public void schedule(HttpServletRequest request, HttpServletResponse response){

        String masterId = request.getParameter("masterId");
        String statusString = request.getParameter("status");
        if (StringUtil.empty(statusString)){
            statusString = "2";
        }
        int status = 0;
        try {
            status = Integer.parseInt(statusString);
        } catch (Exception e) {
            log.error(e);
            ResponseVo.send101Code(response,"请求参数status不为int");
            e.printStackTrace();
        }
        String msDate = request.getParameter("msDate");
        if (StringUtil.empty(msDate)){
            ResponseVo.send101Code(response,"msDate为空！");
            return;
        }
        Date date = null;
        try {
            date = sdf.parse(msDate+"-01 00:00:00");
        } catch (ParseException e) {
            log.error(e);
            ResponseVo.send101Code(response,"请求参数appointmentTime格式有误！格式为：yyyy-MM");
            e.printStackTrace();
        }
        List<MasterSchedule> list = tfOrderService.findMasterSchedule(Integer.parseInt(masterId),status,date);
        if (list==null||list.size()<1){
            ResponseVo.send1Code(response,"success",new JSONObject());
            return;
        }
        JsonConfig config = new JsonConfig();
        config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd"));
        JSONArray jsonArray = JSONArray.fromObject(list,config);
        JSONObject responseObject = new JSONObject();
        responseObject.put("masterScheduleList",jsonArray);
        responseObject.put("limitNum",5);
        ResponseVo.send1Code(response,"success",responseObject);
    }
    //取消订单——更新
    @RequestMapping(value = "cancel", method = RequestMethod.POST)
    public void cancel(HttpServletRequest request, HttpServletResponse response){

    	String userId = request.getParameter("userId");
        String masterId = request.getParameter("masterId");
        String orderId = request.getParameter("orderId");
        String t = request.getParameter("type");//取消订单类型：1为子订单，2为总订单
        String realm = request.getHeader("Realm");
        if (StringUtil.empty(orderId)){
            ResponseVo.send101Code(response,"orderId为空！");
            return;
        }
        if(StringUtil.empty(t)){
        	ResponseVo.send101Code(response, "type为空！");
        	return;
        }
        Integer type = Integer.parseInt(t);
        if(type<0&&type!=1&&type!=2){
        	ResponseVo.send114Code(response, "订单类型参数错误");
        	return;
        }
        int oid = 0;
        try {
            oid = Integer.parseInt(orderId);
        }catch (Exception e){
            log.error("orderId不为int ："+orderId);
            ResponseVo.send101Code(response,"orderId值不为int"+orderId);
            e.printStackTrace();
        }
        if(StringUtil.empty(realm)){
            ResponseVo.send101Code(response,"realm为空!");
            return;
        }
        TfOrder tfOrder = null;
        TfOrderTotalHelper tfOrderTotal = null;
        int cancelType = 0;
        int i = 0;
        try {
			switch (type) {
			case 1://子订单
				if("master".equals(realm)){//大师取消
					tfOrder = tfOrderService.findById(oid,Integer.parseInt(masterId));
					cancelType = 1;
				}else if("user".equals(realm)){//用户取消
					tfOrder = tfOrderService.selectById(oid,Integer.parseInt(userId));
					cancelType = 2;
				}else{
					ResponseVo.send101Code(response, "realm错误");
					return;
				}
				if(tfOrder==null){
					ResponseVo.send102Code(response,"该订单不存在"+orderId);
			        return;
				}
				//暂时商品订单只考虑按子总订单退款
				if(tfOrder.getoOrderType()==TfOrder.orderTypeProduct){
					ResponseVo.send114Code(response, "取消订单类型错误");
					return;
				}
				tfOrder.setoCancelType(cancelType);
				int status = tfOrder.getoStatus();
			    //已取消、已完成、进行中、已退款 不能取消
			    if (TfOrder.orderStatusHaveDone==status||TfOrder.orderStatusRebackCancel==status||TfOrder.orderStatusRebackMoneyDone==status||TfOrder.orderStatusHaveInHand==status){
			        JSONObject obj = new JSONObject();
			        obj.put("oStatus", status);
			    	ResponseVo.send508Code(response,"不是已支付、未支付、已确定订单无法取消,该订单状态为",obj);
			        return;
			    }
				
				switch (status) {
				case 1://未支付
					i = tfOrderService.cancelUpdateOrder(tfOrder);
					break;
				case 2://已支付，需要退款
					i = tfOrderService.cancelOrder(tfOrder);
					break;
				case 3://已确定
					i = tfOrderService.cancelOrder(tfOrder);
					break;

				default:
					break;
				}
				break;
			case 2://子总订单——商品订单
				//总订单取消、更新状态；更新子订单状态
				
				if("master".equals(realm)){//大师取消
					cancelType = 1;
					tfOrderTotal = tfOrderTotalService.findByMasterId(oid,Integer.parseInt(masterId));
				}else if("user".equals(realm)){//用户取消
					cancelType = 2;
					tfOrderTotal = tfOrderTotalService.findByUserId(oid, Integer.parseInt(userId));//子总订单
				}else{
					ResponseVo.send101Code(response, "realm错误");
					return;
				}
					
				if(tfOrderTotal==null){
					ResponseVo.send102Code(response, "总订单不存在");
					return;
				}
				int sts = tfOrderTotal.gettStatus();//订单状态
				//已取消、已完成、进行中、已退款 不能取消
			    if (TfOrder.orderStatusHaveDone==sts||TfOrder.orderStatusRebackCancel==sts||TfOrder.orderStatusRebackMoneyDone==sts||TfOrder.orderStatusHaveInHand==sts){
			        JSONObject obj = new JSONObject();
			        obj.put("oStatus", sts);
			    	ResponseVo.send508Code(response,"不是已支付、未支付、已确定订单无法取消,该订单状态为",obj);
			        return;
			    }
				switch(sts){
				case 1://未支付
					//更新子总订单、子订单状态
					i = tfOrderService.cancelUpdateProductTotalOrder(tfOrderTotal,cancelType);
					
					break;
				case 2://已支付，需要退款
					//退款，更新子总订单、子订单
					i = tfOrderService.cancelProductOrder(tfOrderTotal,cancelType);
					break;
				case 3://已确定
					//退款，更新子总订单、子订单、父总订单
					i = tfOrderService.cancelProductOrder(tfOrderTotal,cancelType);
					break;
				}
				break;
			default:
				break;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
			ResponseVo.send106Code(response, "数据出错，取消订单失败");
			return;
		}
        if (i>0){
            ResponseVo.send1Code(response,"success",new JSONObject());
            return;
        }else if(i==-1){
        	  ResponseVo.send102Code(response, "总订单为空");
              return;
        }else if(i==-2){
        	ResponseVo.send5080Code(response, "不支持总订单的退款");
            return;
        }else if(i==-3){
        	ResponseVo.send102Code(response, "支付订单不存在");
        	return;
        }else if(i==-4){//商品订单不存在
        	ResponseVo.send102Code(response, "对应商品订单不存在");
        	return;
        }else{
        	ResponseVo.send106Code(response, "数据错误");
        	return;
        }
    }
    
    /**
     * 订单进行时
     * @param request
     * @param response
     */

    @RequestMapping(value="ongoing",method=RequestMethod.POST)
    public void orderOngoing(HttpServletRequest request,HttpServletResponse response){
    	//获取参数：订单Id，用户Id或大师Id
    	Integer orderId = TransformUtils.toInteger(request.getParameter("orderId"));
    	/*Integer userId = TransformUtils.toInteger(request.getParameter("userId"));
    	if(userId!=null&&userId==-1){
    		ResponseVo.send800Code(response, "当前为游客，请登录");
    		return;
    	}*/
    	Integer masterId = TransformUtils.toInteger(request.getParameter("masterId"));
    	if(orderId==null||orderId<1){
    		ResponseVo.send101Code(response, "orderId为空或错误");
    		return;
    	}
    	if((masterId==null||masterId<1)){
    		ResponseVo.send101Code(response, "masterId参数有误");
    		return;
    	}
    	TfOrder order = tfOrderService.findById(orderId, masterId);
    	if(order==null){
    		ResponseVo.send102Code(response, "订单不存在");
    		return;
    	}
    	//做时间限制，未到时间或时间过时都不能开启视频
    	Date now = new Date();
    	Date d = order.getoAppointmentTime();
    	Calendar c = Calendar.getInstance();
    	c.setTime(d);
    	Calendar c2 = Calendar.getInstance();
    	Calendar c3 = Calendar.getInstance();
    	c2.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
    	c3.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
    	if(c.get(Calendar.HOUR_OF_DAY)<13){//上午时间段
    		c2.set(Calendar.HOUR_OF_DAY, 9);
    		c3.set(Calendar.HOUR_OF_DAY, 13);
    		if(now.getTime()<c2.getTimeInMillis()){//时间未到
    			ResponseVo.send118Code(response, "该占卜订单预约时间未到");
    			return;
    		}else if(now.getTime()>c3.getTimeInMillis()){//时间已过
    			ResponseVo.send125Code(response, "该占卜订单预约时间已过");
    			return;
    		}
    	}else{//下午时间段
    		c2.set(Calendar.HOUR_OF_DAY, 14);
    		c3.set(Calendar.HOUR_OF_DAY, 18);
    		if(now.getTime()<c2.getTimeInMillis()){//时间未到
    			ResponseVo.send118Code(response, "该占卜订单预约时间未到");
    			return;
    		}else if(now.getTime()>c3.getTimeInMillis()){//时间已过
    			ResponseVo.send125Code(response, "该占卜订单预约时间已过");
    			return;
    		}
    	}
    	JsonConfig config = new JsonConfig();
    	config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
    	TfUser u = tfUserService.findUserById(order.getoUserId());
    	JSONObject obj = new JSONObject();
    	obj.put("user", JSONObject.fromObject(u, config));
    	//订单状态不匹配
		if(order.getoStatus()==TfOrder.orderStatusHaveInHand){
			ResponseVo.send509Code(response, "订单状态已更新", obj);;
			return;
		}
		if(order.getoStatus()!=TfOrder.orderStatusMakeSure){
			ResponseVo.send510Code(response, "订单状态不匹配");
			return;
		}
		TfOrder o = new TfOrder();
		o.setOrderId(order.getOrderId());
		o.setoStatus(TfOrder.orderStatusHaveInHand);
		//更新订单状态
		int i = tfOrderService.updateOrder(o);
		if(i!=1){
			ResponseVo.send106Code(response, "数据出错");
			return;
		}
    	order.setoStatus(TfOrder.orderStatusHaveInHand);
    	ResponseVo.send1Code(response, "success", obj);
    }
    
    /**
     * 完成订单
     * @param request
     * @param response
     */
    @RequestMapping(value="complete",method=RequestMethod.POST)
    public void completeOrder(HttpServletRequest request,HttpServletResponse response){
    	//获取订单参数：用户id，订单id
    	Integer userId = TransformUtils.toInteger(request.getParameter("userId"));
    	Integer type = TransformUtils.toInteger(request.getParameter("type"));//类型，1为商品订单，2为非商品订单
    	Integer orderId = TransformUtils.toInteger(request.getParameter("orderId"));//订单或总订单
    	if(userId!=null&&userId==-1){//游客
    		ResponseVo.send800Code(response, "当前为游客，请登录");
    		return;
    	}
    	if(userId==null||userId<1){
    		ResponseVo.send101Code(response, "userId为空或格式错误");
    		return;
    	}
    	if(orderId==null||orderId<1){
    		ResponseVo.send101Code(response, "orderId为空或格式错误");
    		return;
    	}
    	if(type==null||(type!=1&&type!=2)){
    		ResponseVo.send101Code(response, "type为空或格式错误");
    		return;
    	}
    	JSONObject obj = new JSONObject();
    	JsonConfig config = new JsonConfig();
    	config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
    	JSONObject jsonObj = new JSONObject();
    	jsonObj.put("data", "NO DATA");
    	if(type==2){
    		//查询订单
    		TfOrder order = tfOrderService.findByUserId(orderId, userId);
    		if(order==null){
    			ResponseVo.send102Code(response, "订单不存在");
    			return;
    		}
    		if(order.getoStatus()==TfOrder.orderStatusHaveDone){
    			ResponseVo.send509Code(response, "订单状态已更新", jsonObj);
    		}
    		if(order.getoStatus()!=TfOrder.orderStatusHaveInHand){
    			ResponseVo.send510Code(response, "订单状态不匹配");
    			return;
    		}
    		//更新订单状态，同时更新大师余额
    		try {
				int i = tfOrderService.completeOrder(order);
			} catch (Exception e) {
				e.printStackTrace();
				log.error("completeOrder:"+e.getMessage());
				ResponseVo.send106Code(response, "数据出错操作失败");
				return;
			}
    	}else{
    		//查询总订单
    		TfOrderTotalHelper h = tfOrderTotalService.findByUserId(orderId,userId);
    		if(h==null){
    			ResponseVo.send102Code(response, "订单不存在");
    			return;
    		}
    		if(h.gettStatus()==TfOrder.orderStatusHaveDone){
    			ResponseVo.send509Code(response, "订单状态已更新", jsonObj);
    		}
    		if(h.gettStatus()!=TfOrder.orderStatusHaveInHand){
    			ResponseVo.send510Code(response, "订单状态不匹配");
    			return;
    		}
    		int j=0;
			try {
				j = tfOrderService.completeProductOrder(h);
			} catch (Exception e) {
				e.printStackTrace();
				log.error("completeOrder:"+e.getMessage());
				ResponseVo.send106Code(response, "数据出错操作失败");
				return;
			}
    		if(j==-1){
    			ResponseVo.send510Code(response, "订单状态不匹配");
    			return;
    		}else if(j==1){
    		}else{
    			ResponseVo.send106Code(response, "数据出错操作失败");
    			return;
    		}
    	}
    	ResponseVo.send1Code(response, "success", jsonObj);
    }
    
    //商家发货
    @RequestMapping(value="/deliver",method=RequestMethod.POST)
    public void deliverGoods(HttpServletRequest request,HttpServletResponse response){
    	log.info("======商家发货======");
    	Integer masterId = TransformUtils.toInt(request.getParameter("masterId"));
    	String orderNo = request.getParameter("orderNo");
    	String realm = request.getHeader("Realm");
    	if(!"master".equals(realm)){
    		ResponseVo.send101Code(response, "Realm为空或有误");
    		return;
    	}
    	if(masterId<1){
    		ResponseVo.send101Code(response, "masterId为空或有误");
    		return;
    	}
    	if(StringUtil.empty(orderNo)){
    		ResponseVo.send101Code(response, "orderNo为空");
    		return;
    	}
    	Integer expressCompanyId = TransformUtils.toInt(request.getParameter("companyId"));
    	String expressNo = request.getParameter("expressNo");
    	if(expressCompanyId<1){
    		ResponseVo.send101Code(response, "companyId为空或有误");
    		return;
    	}
    	String company = ExpressCompanyUtil.getCompany(expressCompanyId);
    	if(StringUtil.empty(company)){
    		ResponseVo.send101Code(response, "companyId为空或有误");
    		return;
    	}
    	if(StringUtil.empty(expressNo)){
    		ResponseVo.send101Code(response, "expressNo为空");
    		return;
    	}
    	String reg = "^[A-Z0-9]+$";
    	if(!expressNo.matches(reg)){
    		ResponseVo.send104Code(response, "expressNo格式错误");
    		return;
    	}
    	//订单是否存在
    	TfOrderTotalHelper ot = tfOrderTotalService.findOrderByMaster(masterId,orderNo);
    	if(ot==null){
    		ResponseVo.send102Code(response, "订单不存在");
    		return;
    	}
    	//判断订单状态是否为进行中
    	if(!TfOrder.orderStatusPaidDone.equals(ot.gettStatus())){
    		ResponseVo.send510Code(response, "订单状态不匹配");
    		return;
    	}
    	//更新物流信息
    	TfOrderTotal orderTotal = new TfOrderTotal();
    	orderTotal.setTotalId(ot.getTotalId());
    	orderTotal.settExpressCompanyId(expressCompanyId);
    	orderTotal.settExpressCompany(company);
    	orderTotal.settExpressNo(expressNo);
    	int i = tfOrderTotalService.updateOrderTotal(orderTotal);
    	if(i!=1){
    		ResponseVo.send106Code(response, "数据出错，操作失败");
    		return;
    	}
    	ResponseVo.send1Code(response, "success", new JSONObject());
    }
}
