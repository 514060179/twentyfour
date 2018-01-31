package com.yinghai.twentyfour.app.controller;

import com.yinghai.twentyfour.app.service.TfOrderTotalService;
import com.yinghai.twentyfour.common.constant.WeChat;
import com.yinghai.twentyfour.common.model.TfOrder;
import com.yinghai.twentyfour.common.model.TfOrderTotal;
import com.yinghai.twentyfour.common.service.TfOrderService;
import com.yinghai.twentyfour.common.util.ResponseVo;
import com.yinghai.twentyfour.common.util.StringUtil;
import com.yinghai.twentyfour.common.util.WeChatPayUtils;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Administrator on 2017/10/23.
 */
@Controller
@RequestMapping("app/wechat")
public class WeChatPayController {

    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private TfOrderService tfOrderService;

    @Autowired
    private TfOrderTotalService tfOrderTotalService;
    /**
     * 微信支付，然后将需要给APP的数据传过去
     */
    @RequestMapping(value = "pay", method = RequestMethod.POST)
    public void connection(HttpServletRequest request, HttpServletResponse response) {
        String payType = request.getParameter("payType");
        String openId = request.getParameter("openId");
        String userId = request.getParameter("userId");
        if (StringUtil.empty(payType)) {
            payType = WeChat.weixinAPPPayType;
        }
        String spbill_create_ip = request.getParameter("spbillCreateIp"); // 用户端ip
        if (StringUtil.empty(spbill_create_ip)){
            ResponseVo.send101Code(response,"spbillCreateIp为空");
            return;
        }
        String type = request.getParameter("orderType");// 订单类型1在线占卜订单2到访占卜订单3商品订单.
        if (StringUtil.empty(type)){
            ResponseVo.send101Code(response,"orderType为空");
            return;
        }
        int orderType = 0;
        try {
            orderType = Integer.parseInt(type);
        }catch (Exception e){
            ResponseVo.send101Code(response,"orderType不为int");
            log.error(e);
            e.printStackTrace();
        }
        String orderNo = request.getParameter("orderNo");// 订单号
        if (StringUtil.empty(orderNo)){
            ResponseVo.send101Code(response,"orderNo为空");
            return;
        }
//        if(tfOrder.getoPrice()!=total_fee){
//            log.error("订单价格可能被篡改.订单价格："+tfOrder.getoPrice()+"传过来价格："+total_fee);
//            ResponseVo.send502Code(response,"订单价格可能被篡改.订单价格："+tfOrder.getoPrice()+"传过来价格："+total_fee);
//        }
        String body = "";//描述
        String totalAmount = "";//价格
        switch (orderType){
            case 1:{//1在线占卜订单
                TfOrder tfOrder = tfOrderService.findByOrderNo(orderNo,Integer.parseInt(userId));
                if(tfOrder==null){
                    log.error("用户：#"+userId+"不存在订单：#"+orderNo);
                    ResponseVo.send102Code(response,"该订单不存在，订单号为："+orderNo);
                    return;
                }
                if(tfOrder.getoOrderType()!=orderType){
                    ResponseVo.send507Code(response,"该订单为在线占卜订单，与传入orderType不相符");
                    return;
                }
                if(TfOrder.orderStatusNoPay!=tfOrder.getoStatus()){
                    ResponseVo.send506Code(response,"支付或者取消的订单无法再次支付!");
                    return;
                }
                body = "twentyfour"+tfOrder.getTfMaster().getMasterId()+tfOrder.getTfMaster().getmNick()+"业务#"+tfOrder.getTfBusiness().getBusinessId()+tfOrder.getTfBusiness().getbName();
                totalAmount = tfOrder.getoAmount()+"";
                break;
            }
//            case 2:{}
            case 3:{//3商品订单
                TfOrderTotal tfOrderTotal = tfOrderTotalService.findByOrderNo(orderNo,Integer.parseInt(userId));
                if(tfOrderTotal==null){
                    log.error("用户：#"+userId+"不存在订单：#"+orderNo);
                    ResponseVo.send102Code(response,"用户：#"+userId+"不存在订单：#"+orderNo);
                    return;
                }

                body = "用户#"+tfOrderTotal.gettUserId()+"购物车商品";
                totalAmount = tfOrderTotal.gettAmount()+"";
                break;
            }
            default:{
                ResponseVo.send101Code(response,"orderType取值:1(在线占卜订单)、2(到访订单)、3（商品订单）");
                return;
            }
        }
        String json = null;
        try {
            json = WeChatPayUtils.connection(spbill_create_ip, totalAmount, orderNo,payType,orderType+"",body);
        } catch (IOException e) {
            log.error("======确定支付操作失败！======"+e.getMessage()+"订单号："+orderNo);
            ResponseVo.send111Code(response,e.getMessage());
            e.printStackTrace();
        }
        JSONObject res = JSONObject.fromObject(json);
        String code = res.getString("code");
        if("1".equals(code)){
            ResponseVo.send1Code(response,"success",res);
        }else{
            log.error("微信支付统一下单失败："+res);
            ResponseVo.send111Code(response,res.getString("msg"));
        }
    }
}
