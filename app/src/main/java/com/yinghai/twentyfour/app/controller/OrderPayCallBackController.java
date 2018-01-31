package com.yinghai.twentyfour.app.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.yinghai.twentyfour.app.service.TfOrderTotalService;
import com.yinghai.twentyfour.common.constant.Alipay;
import com.yinghai.twentyfour.common.constant.PushCode;
import com.yinghai.twentyfour.common.im.method.Message;
import com.yinghai.twentyfour.common.im.model.CodeSuccessResult;
import com.yinghai.twentyfour.common.im.msg.TxtMessage;
import com.yinghai.twentyfour.common.model.TfImUser;
import com.yinghai.twentyfour.common.model.TfOrder;
import com.yinghai.twentyfour.common.model.TfOrderTotal;
import com.yinghai.twentyfour.common.service.TfOrderService;
import com.yinghai.twentyfour.common.service.TfUserService;
import com.yinghai.twentyfour.common.util.*;
import com.yinghai.twentyfour.common.vo.MasterAndUserIm;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;
import org.jdom2.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/24.
 * 回调
 */
@Controller
@RequestMapping("callback")
public class OrderPayCallBackController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private TfOrderService tfOrderService;
    @Autowired
    private TfOrderTotalService tfOrderTotalService;
    @Autowired
    private TfUserService tfUserService;

    private String parseWeixinCallback(HttpServletRequest request) throws IOException, JDOMException, ParseException {

        InputStream inStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        String result = new String(outSteam.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息
        return result;
    }

    
    /**
     * 更新的微信支付回调方法
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "wechat", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String wechatCallBack(HttpServletRequest request, HttpServletResponse response){
        String responseStr = null;
        Map<String, Object> map = null;
        String callback = "";//通知微信端后台
        try {
            responseStr = parseWeixinCallback(request);
            map = XMLUtil.doXMLParse(responseStr);
        } catch (Exception e) {
            log.debug("支付失败" + e.getMessage());
            callback = XMLUtil.setXML("FAIL", "weixin pay server exception");
            e.printStackTrace();
            ResponseUtils.renderJson(response, callback);
            return callback;
        }
        // 校验签名 防止数据泄漏导致出现“假通知”，造成资金损失
        String code = map.get("result_code").toString();
        if (!WeChatPayUtils.checkIsSignValidFromResponseString(map,(String) map.get("trade_type"))) {
            log.error("微信回调失败,签名可能被篡改");
            callback = XMLUtil.setXML("FAIL", "invalid sign");
            ResponseUtils.renderJson(response, callback);
            return callback;
        }
        if ("FAIL".equalsIgnoreCase(map.get("result_code").toString())) {
            log.error("微信回调失败");
            callback = XMLUtil.setXML("FAIL", "weixin pay fail");
            ResponseUtils.renderJson(response, callback);
            return callback;
        }
        if ("SUCCESS".equalsIgnoreCase(code)) { //支付成功！
            //更新订单状态
            //获取订单
            String orderNo = (String)map.get("out_trade_no");
            String attach = (String)map.get("attach");
            int orderType = TransformUtils.toInt(attach);
            if(orderType==0){
                if("pt".equalsIgnoreCase(orderNo.substring(0,2))){
                    orderType = 3;
                }else{
                    orderType = 1;
                }
            }
            int i = 0;
            MasterAndUserIm tfImUser = new MasterAndUserIm();
            List<MasterAndUserIm> tfImUsers = null;
            JSONObject res = new JSONObject();
            List<TfOrderTotal> totalOrders = null;
			switch (orderType){
                case 1:{//在线占卜订单
                	//查询此在线占卜订单状态是否为支付成功，如果是则结束方法
                	TfOrder order = tfOrderService.findByOrderNo(orderNo, null);
                	if(TfOrder.orderStatusPaidDone.equals(order.getoStatus())){
                		callback = XMLUtil.setXML("SUCCESS", "OK");
                		return callback;
                	}
                    i = tfOrderService.callbackUpdateStatus(orderNo,TfOrder.payWayWeChat);
                    tfImUser = tfUserService.findByOrderNo(orderNo);
                    TfOrder tfOrder = new TfOrder();
                    tfOrder.setOrderId(tfImUser.getOrderId());
                    tfOrder.setoOrderNo(tfImUser.getOrderNo());
                    res.put("order",tfOrder);
                    break;
                }
                case 3:{
                	TfOrderTotal totalOrder = tfOrderTotalService.findByOrderNo(orderNo, null);
                	if(TfOrder.orderStatusPaidDone.equals(totalOrder.gettStatus())){
                		callback = XMLUtil.setXML("SUCCESS", "OK");
                		return callback;
                	}
                	try {
						i = tfOrderService.callbackUpdateOrder(totalOrder,TfOrder.payWayWeChat);
					} catch (Exception e) {
						e.printStackTrace();
						callback = XMLUtil.setXML("FAIL", "pay fail");
		                ResponseUtils.renderJson(response, callback);
		                return callback;
					}
                	if(totalOrder.gettParentId()==null){//父总订单
                		totalOrders = tfOrderTotalService.findByPayOrderNo(totalOrder.gettOrderNo());
                		if(totalOrders==null||totalOrders.size()<1){
                			//数据有误
                			callback = XMLUtil.setXML("FAIL", "pay fail");
                            ResponseUtils.renderJson(response, callback);
                            return callback;
                		}
                		List<MasterAndUserIm> ImUsers = new ArrayList<MasterAndUserIm>();
                		for(TfOrderTotal ot:totalOrders){
                			MasterAndUserIm ImUser = tfUserService.findByOrderTotalNo(ot.gettOrderNo());
                			ImUsers.add(ImUser);
                		}
                		tfImUsers = ImUsers;
                		tfImUser = ImUsers.get(0);
                		res.put("tfOrderTotal", totalOrder);
                	}else{//子总订单
                		tfImUser = tfUserService.findByOrderTotalNo(totalOrder.gettOrderNo());
                		TfOrderTotal tfOrderTotal = new TfOrderTotal();
                		tfOrderTotal.setTotalId(tfImUser.getOrderId());
                		tfOrderTotal.settOrderNo(tfImUser.getOrderNo());
                		res.put("tfOrderTotal",tfOrderTotal);
                	}
                    break;
                }
                default:{
                    callback = XMLUtil.setXML("FAIL", "pay fail");
                    ResponseUtils.renderJson(response, callback);
                    break;
                }
            }
			JsonConfig config = new JsonConfig();
			config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
            if(i>0){
                callback = XMLUtil.setXML("SUCCESS", "OK");
                ResponseUtils.renderJson(response, callback);
                if (tfImUser!=null){
                    //推送
                    Message m = new Message();
                    JSONObject jsonObject = new JSONObject();
                    res.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    res = JSONObject.fromObject(res, config);
                    jsonObject.put("code", PushCode.UOrderHavePaid);
                    jsonObject.put("msg",PushCode.UOrderHavePaidMsg);
                    jsonObject.put("data",res);
                    try {
                    	if(tfImUsers!=null){
                    		for(int p=0;p<tfImUsers.size();p++){
                    			res.put("tfOrderTotal", totalOrders.get(p));
                    			res = JSONObject.fromObject(res, config);
                    			jsonObject.put("data", res);
                    			//用户
                            	//System.out.println("微信支付,订单类型"+orderType+",推送给用户消息："+jsonObject.toString());
                                CodeSuccessResult codeSuccessResult =  m.publishSystem("admin",new String[]{tfImUser.getTfUser().getuIm()},new TxtMessage(jsonObject.toString(),""),PushCode.UOrderHavePaidMsg,PushCode.UOrderHavePaidMsg,1,1);
                                if(codeSuccessResult.getCode()!=200){
                                    log.error("WX支付成功通知用户失败！"+codeSuccessResult);
                                }
                    		}
                    	}else{
                    		//用户
                    		//System.out.println("微信支付,订单类型"+orderType+",推送给用户消息："+jsonObject.toString());
                    		CodeSuccessResult codeSuccessResult =  m.publishSystem("admin",new String[]{tfImUser.getTfUser().getuIm()},new TxtMessage(jsonObject.toString(),""),PushCode.UOrderHavePaidMsg,PushCode.UOrderHavePaidMsg,1,1);
                    		if(codeSuccessResult.getCode()!=200){
                    			log.error("WX支付成功通知用户失败！"+codeSuccessResult);
                    		}
                    	}
                        //大师
                        jsonObject.put("code", PushCode.MOrderHavePaid);
                        jsonObject.put("msg",PushCode.MOrderHavePaidMsg);
                        if(tfImUsers!=null){
                        	for(int n=0;n<tfImUsers.size();n++){
                        		res.put("tfOrderTotal", totalOrders.get(n));
                        		res = JSONObject.fromObject(res, config);
                        		jsonObject.put("data", res);
                        		System.out.println("微信支付,订单类型"+orderType+",推送给大师"+tfImUsers.get(n).getTfMaster().getMasterId()+"消息："+jsonObject.toString());
                            	CodeSuccessResult result = m.publishSystem("admin",new String[]{tfImUsers.get(n).getTfMaster().getmIm()},new TxtMessage(jsonObject.toString(),""),PushCode.MOrderHavePaidMsg,PushCode.MOrderHavePaidMsg,1,1);
                            	if(result.getCode()!=200){
                            		log.error("WX支付成功通知大师失败！"+result);
                            	}
                        	}
                        }else{
                        	jsonObject.put("data", res);
                        	//System.out.println("微信支付,订单类型"+orderType+",推送给大师"+tfImUser.getTfMaster().getMasterId()+"消息："+jsonObject.toString());
                        	CodeSuccessResult result = m.publishSystem("admin",new String[]{tfImUser.getTfMaster().getmIm()},new TxtMessage(jsonObject.toString(),""),PushCode.MOrderHavePaidMsg,PushCode.MOrderHavePaidMsg,1,1);
                        	if(result.getCode()!=200){
                        		log.error("WX支付成功通知大师失败！"+result);
                        	}
                        }
                    } catch (Exception e) {
                        log.error("WX支付成功通知失败！",e);
                        e.printStackTrace();
                    }
                }else{
                    log.error("账户不存在！推送消息失败！");
                }
                return callback;
            }else{
                callback = XMLUtil.setXML("FAIL", "pay fail");
                ResponseUtils.renderJson(response, callback);
                return callback;
            }
        }else{//支付失败！
            callback = XMLUtil.setXML("FAIL", "pay fail");
        }
        ResponseUtils.renderJson(response, callback);
        return callback;
    }
    /**
     * 更新的支付宝支付
     * @param request
     * @param response
     * @throws AlipayApiException
     */
    @RequestMapping(value = "alipay", method = RequestMethod.POST)
    public void alipayCallBack(HttpServletRequest request, HttpServletResponse response) throws AlipayApiException {
        //获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {

            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            /*乱码解决，这段代码在出现乱码时使用。
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");*/
            params.put(name, valueStr);
        }
        //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
        boolean flag = AlipaySignature.rsaCheckV1(params, Alipay.alipayPKey, "utf-8","RSA2");
        if (flag){
            //更新
            String orderNo = params.get("out_trade_no");
            String passbackParams = params.get("passback_params");
            int orderType = 1;
            int i = 0;
            if (!StringUtil.empty(passbackParams)){
                orderType = Integer.parseInt(passbackParams);
            }else{
                if("pt".equalsIgnoreCase(orderNo.substring(0,2))){
                    orderType = 3;
                }
            }
            MasterAndUserIm tfImUser = new MasterAndUserIm();
            List<TfOrderTotal> totalOrders = null;
            List<MasterAndUserIm> tfImUsers = null;
            JSONObject res = new JSONObject();
            switch (orderType){
                case 1:{//在线占卜订单
                	//查询此在线占卜订单状态是否为支付成功，如果是则结束方法
                	TfOrder order = tfOrderService.findByOrderNo(orderNo, null);
                	if(TfOrder.orderStatusPaidDone.equals(order.getoStatus())){
                		return;
                	}
                    tfImUser = tfUserService.findByOrderNo(orderNo);
                    TfOrder tfOrder = new TfOrder();
                    tfOrder.setOrderId(tfImUser.getOrderId());
                    tfOrder.setoOrderNo(tfImUser.getOrderNo());
                    res.put("order",tfOrder);
                    i = tfOrderService.callbackUpdateStatus(orderNo,TfOrder.payWayAlipay);
                    break;
                }
                case 3:{//商品订单
                	TfOrderTotal totalOrder = tfOrderTotalService.findByOrderNo(orderNo, null);
                	if(TfOrder.orderStatusPaidDone.equals(totalOrder.gettStatus())){
                		return;
                	}
                	try {
						i = tfOrderService.callbackUpdateOrder(totalOrder,TfOrder.payWayAlipay);
					} catch (Exception e) {
						log.error("订单信息更新失败");
						return;
					}
                	if(totalOrder.gettParentId()==null){//父总订单
                		totalOrders = tfOrderTotalService.findByPayOrderNo(totalOrder.gettOrderNo());
                		List<MasterAndUserIm> ImUsers = new ArrayList<MasterAndUserIm>();
                		for(TfOrderTotal ot:totalOrders){
                			MasterAndUserIm ImUser = tfUserService.findByOrderTotalNo(ot.gettOrderNo());
                			ImUsers.add(ImUser);
                		}
                		tfImUsers = ImUsers;
                		tfImUser = ImUsers.get(0);
                		TfOrderTotal tfOrderTotal = new TfOrderTotal();
                		tfOrderTotal.setTotalId(tfImUser.getOrderId());
                		tfOrderTotal.settOrderNo(tfImUser.getOrderNo());
                		res.put("tfOrderTotal", totalOrder);
                	}else{//子总订单
                		tfImUser = tfUserService.findByOrderTotalNo(totalOrder.gettOrderNo());
                		TfOrderTotal tfOrderTotal = new TfOrderTotal();
                		tfOrderTotal.setTotalId(tfImUser.getOrderId());
                		tfOrderTotal.settOrderNo(tfImUser.getOrderNo());
                		res.put("tfOrderTotal",tfOrderTotal);
                	}
                    break;
                }
            }
			JsonConfig config = new JsonConfig();
			config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
            if (tfImUser!=null&&i>0){
                //推送
                Message message = new Message();
                JSONObject jsonObject = new JSONObject();
                res.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                res = JSONObject.fromObject(res, config);
                jsonObject.put("code", PushCode.UOrderHavePaid);
                jsonObject.put("msg",PushCode.UOrderHavePaidMsg);
                jsonObject.put("data",res);
                try {
                	if(tfImUsers!=null){
                		for(int p=0;p<tfImUsers.size();p++){
                			res.put("tfOrderTotal", totalOrders.get(p));
                			res = JSONObject.fromObject(res, config);
                			jsonObject.put("data", res);
                			//用户
                        	//System.out.println("支付宝,订单类型"+orderType+",推送给用户消息："+jsonObject.toString());
                            CodeSuccessResult codeSuccessResult =  message.publishSystem("admin",new String[]{tfImUser.getTfUser().getuIm()},new TxtMessage(jsonObject.toString(),""),PushCode.UOrderHavePaidMsg,PushCode.UOrderHavePaidMsg,1,1);
                            if(codeSuccessResult.getCode()!=200){
                                log.error("支付成功通知用户失败！"+codeSuccessResult);
                            }
                		}
                	}else{
                		//用户
                		//System.out.println("微信支付,订单类型"+orderType+",推送给用户消息："+jsonObject.toString());
                		CodeSuccessResult codeSuccessResult =  message.publishSystem("admin",new String[]{tfImUser.getTfUser().getuIm()},new TxtMessage(jsonObject.toString(),""),PushCode.UOrderHavePaidMsg,PushCode.UOrderHavePaidMsg,1,1);
                		if(codeSuccessResult.getCode()!=200){
                			log.error("支付成功通知用户失败！"+codeSuccessResult);
                		}
                	}

                    
                  //大师
                    jsonObject.put("code", PushCode.MOrderHavePaid);
                    jsonObject.put("msg",PushCode.MOrderHavePaidMsg);
                    if(tfImUsers!=null){
                    	for(int n=0;n<tfImUsers.size();n++){
                    		res.put("tfOrderTotal", totalOrders.get(n));
                    		jsonObject.put("data", res);
                    		//System.out.println("支付宝,订单类型"+orderType+",推送给大师"+tfImUsers.get(n).getTfMaster().getMasterId()+"消息："+jsonObject.toString());
                        	CodeSuccessResult result = message.publishSystem("admin",new String[]{tfImUsers.get(n).getTfMaster().getmIm()},new TxtMessage(jsonObject.toString(),""),PushCode.MOrderHavePaidMsg,PushCode.MOrderHavePaidMsg,1,1);
                        	if(result.getCode()!=200){
                        		log.error("支付成功通知大师失败！"+result);
                        	}
                    	}
                    }else{
                    	jsonObject.put("data", res);
                    	CodeSuccessResult result = message.publishSystem("admin",new String[]{tfImUser.getTfMaster().getmIm()},new TxtMessage(jsonObject.toString(),""),PushCode.MOrderHavePaidMsg,PushCode.MOrderHavePaidMsg,1,1);
                    	if(result.getCode()!=200){
                    		log.error("支付成功通知大师失败！"+result);
                    	}
                    }
                } catch (Exception e) {
                    log.error("支付成功通知失败！",e);
                    e.printStackTrace();
                }
            }else{
                log.error("账户不存在！推送消息失败！或者更新条数为0  "+i);
            }

        }else{
            log.error("签名验证失败！");
        }
    }
}
