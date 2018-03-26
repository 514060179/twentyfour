package com.yinghai.twentyfour.app.controller;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yinghai.twentyfour.common.constant.PushCode;
import com.yinghai.twentyfour.common.im.method.Message;
import com.yinghai.twentyfour.common.im.model.CodeSuccessResult;
import com.yinghai.twentyfour.common.im.msg.TxtMessage;
import com.yinghai.twentyfour.common.model.TfOrder;
import com.yinghai.twentyfour.common.service.TfBusinessService;
import com.yinghai.twentyfour.common.service.TfMasterService;
import com.yinghai.twentyfour.common.service.TfOrderService;
import com.yinghai.twentyfour.common.util.JsonDateValueProcessor;
import com.yinghai.twentyfour.common.util.ResponseVo;
import com.yinghai.twentyfour.common.util.StringUtil;
import com.yinghai.twentyfour.common.util.TransformUtils;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * Created by Administrator on 2017/10/23.
 */
@Controller
@RequestMapping("app/order/test")
public class TfOrderTestController {

    Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private TfBusinessService tfBusinessService;
    @Autowired
    private TfOrderService tfOrderService;
    @Autowired
    private TfMasterService tfMasterService;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 测试支付订单时使用，直接将订单状态修改为已支付，并且推送给APP
     */
    @RequestMapping(value = "pay", method = RequestMethod.POST)
    public void payOrderTest(HttpServletRequest request, HttpServletResponse response){
    	 try {
             request.setCharacterEncoding("utf-8");
         } catch (UnsupportedEncodingException e) {
             e.printStackTrace();
         }
    	 String orderId = request.getParameter("orderId");
    	 if(StringUtil.empty(orderId)){
    		 ResponseVo.send101Code(response,"orderId为空！");
             return;
    	 }
    	 TfOrder tfOrder = tfOrderService.findAllModelById(TransformUtils.toInt(orderId));
    	 if(tfOrder==null){
    		 ResponseVo.send102Code(response,"该订单不存在！");
             return;
    	 }
    	 if(tfOrder.getoStatus()!=1){
    		 ResponseVo.send501Code(response,"订单状态不为未支付！");
             return; 
    	 }
    	 TfOrder updateTfOrder = new TfOrder();
    	 updateTfOrder.setOrderId(tfOrder.getOrderId());
    	 updateTfOrder.setoUpdateTime(new Date());
    	 updateTfOrder.setoPayTime(new Date());
    	 updateTfOrder.setoStatus(2);
    	 updateTfOrder.setoPayStatus(2);
    	 updateTfOrder.setoPayWay(1);
    	 int i = tfOrderService.updateOrder(updateTfOrder);
    	 if(i<1){
    		 ResponseVo.send106Code(response, "更新订单状态失败");
             return;
    	 }
    	 //更新成功后推送
    	 //通知用户退款成功
         Message message = new Message();
         try {
             JSONObject jsonObject = new JSONObject();
             JsonConfig config = new JsonConfig();
             config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
             JSONObject res = new JSONObject();
             res.put("orderId",tfOrder.getOrderId());
             jsonObject.put("code", PushCode.UOrderHavePaid);
             jsonObject.put("msg",PushCode.UOrderHavePaidMsg);
             jsonObject.put("data",res);
             //用户
             CodeSuccessResult r1 = message.publishPrivate("admin",new String[]{tfOrder.getTfUser().getuIm()},new TxtMessage(jsonObject.toString(),""),PushCode.UOrderHavePaidMsg,jsonObject.toString(),"1",1,1,1,1);
             if (r1.getCode()!=200){
                 log.error("订单支付通知用户失败！"+r1);
             }else{
            	 log.info("订单支付推送用户成功");
             }
             //大师
             jsonObject.put("code", PushCode.MOrderHavePaid);
             jsonObject.put("msg",PushCode.MOrderHavePaidMsg);
             CodeSuccessResult r2 = message.publishPrivate("admin",new String[]{tfOrder.getTfMaster().getmIm()},new TxtMessage(jsonObject.toString(),""),PushCode.MOrderHavePaidMsg,jsonObject.toString(),"1",1,1,1,1);
             if (r2.getCode()!=200){
                 log.error("订单支付通知大师失败！"+r2);
             }else{
            	 log.info("订单支付推送大师成功");
             }
         } catch (Exception e) {
             log.error(e);
             e.printStackTrace();
         }
         ResponseVo.common("1", "订单支付成功", new JSONObject(), response);
         return;
    }
    
    /**
     * 订单取消（测试使用）
     */
    @RequestMapping(value = "cancle", method = RequestMethod.POST)
    public void orderCencleTest(HttpServletRequest request, HttpServletResponse response){
    	 try {
             request.setCharacterEncoding("utf-8");
         } catch (UnsupportedEncodingException e) {
             e.printStackTrace();
         }
    	 String orderId = request.getParameter("orderId");
    	 String type = request.getParameter("type");
    	 if(StringUtil.empty(orderId)){
    		 ResponseVo.send101Code(response,"orderId为空！");
             return;
    	 }
    	 TfOrder tfOrder = tfOrderService.findAllModelById(TransformUtils.toInt(orderId));
    	 if(tfOrder==null){
    		 ResponseVo.send102Code(response,"该订单不存在！");
             return;
    	 }
    	 if(tfOrder.getoStatus()==999){
//    		 ResponseVo.send508Code(response,"该订单已取消，无法再次取消！");
             return; 
    	 }
    	 String userCode = "";
    	 String userMsg = "";
    	 String masterCode = "";
    	 String masterMsg = "";
    	 TfOrder updateTfOrder = new TfOrder();
    	 updateTfOrder.setOrderId(tfOrder.getOrderId());
    	 updateTfOrder.setoUpdateTime(new Date());
    	 updateTfOrder.setoCancelTime(new Date());
    	  if(TransformUtils.toInt(type)==2){//1为大师取消，2为用户取消
    		  updateTfOrder.setoCancelType(2);
    		  userCode = PushCode.UOrderCancelByUser;
    		  userMsg = PushCode.UOrderCancelByUserMsg;
    		  masterCode = PushCode.MOrderCancelByUser;
    		  masterMsg = PushCode.MOrderCancelByUserMsg;
          }else if(TransformUtils.toInt(type)==1){
        	  updateTfOrder.setoCancelType(1);
        	  userCode = PushCode.UOrderCancelByMaster;
    		  userMsg = PushCode.UOrderCancelByMasterMsg;
    		  masterCode = PushCode.MOrderCancelByMaster;
    		  masterMsg = PushCode.MOrderCancelByMasterMsg;
          }else{
        	  ResponseVo.send103Code(response, "数据不正确，type非大师也非用户");
              return;
          }
    	
    	 updateTfOrder.setoStatus(999);
    	 updateTfOrder.setoPayStatus(999);
    	 int i = tfOrderService.updateOrder(updateTfOrder);
    	 if(i<1){
    		 ResponseVo.send106Code(response, "取消订单状态失败");
             return;
    	 }
    	 //更新成功后推送
    	 //通知用户退款成功
         Message message = new Message();
         try {
             JSONObject jsonObject = new JSONObject();
             JsonConfig config = new JsonConfig();
             config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
             JSONObject res = new JSONObject();
             res.put("orderId",tfOrder.getOrderId());
             jsonObject.put("code", userCode);
             jsonObject.put("msg",userMsg);
             jsonObject.put("data",res);
             //用户
             CodeSuccessResult r1 = message.publishPrivate("admin",new String[]{tfOrder.getTfUser().getuIm()},new TxtMessage(jsonObject.toString(),""),userMsg,jsonObject.toString(),"1",1,1,1,1);
             if (r1.getCode()!=200){
                 log.error("取消订单通知用户失败！"+r1);
             }else{
            	 log.info("取消订单推送给用户成功");
             }
             //大师
             jsonObject.put("code", masterCode);
             jsonObject.put("msg",masterMsg);
             CodeSuccessResult r2 = message.publishPrivate("admin",new String[]{tfOrder.getTfMaster().getmIm()},new TxtMessage(jsonObject.toString(),""),masterMsg,jsonObject.toString(),"1",1,1,1,1);
             if (r2.getCode()!=200){
                 log.error("取消订单通知大师失败！"+r2);
             }else{
            	 log.info("取消订单推送给大师成功");
             }
         } catch (Exception e) {
             log.error(e);
             e.printStackTrace();
         }
         ResponseVo.common("1", "订单取消成功", new JSONObject(), response);
         return;
    }
}
