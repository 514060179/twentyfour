package com.yinghai.twentyfour.app.controller;

import com.alipay.api.AlipayApiException;
import com.yinghai.twentyfour.app.service.TfOrderTotalService;
import com.yinghai.twentyfour.common.model.TfOrder;
import com.yinghai.twentyfour.common.model.TfOrderTotal;
import com.yinghai.twentyfour.common.service.TfOrderService;
import com.yinghai.twentyfour.common.util.AlipayUtil;
import com.yinghai.twentyfour.common.util.ResponseVo;
import com.yinghai.twentyfour.common.util.StringUtil;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2017/10/31.
 */

@Controller
@RequestMapping("/app/alipay")
public class AlipayController {

    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private TfOrderService tfOrderService;

    @Autowired
    private TfOrderTotalService tfOrderTotalService;

    @RequestMapping(value = "pay", method = RequestMethod.POST)
    public void alipay(HttpServletRequest request, HttpServletResponse response){

        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String orderNo = request.getParameter("orderNo");
        String userId = request.getParameter("userId");
        if(StringUtil.empty(orderNo)){
            ResponseVo.send101Code(response,"orderNo为空！");
            return;
        }
        String type = request.getParameter("orderType");//1在线占卜订单2到访占卜订单3商品订单.
        if(StringUtil.empty(type)){
            ResponseVo.send101Code(response,"orderType为空！");
            return;
        }
        int orderType = 0;
        try {
            orderType = Integer.parseInt(type);
        }catch (Exception e){
            log.error(e);
            ResponseVo.send101Code(response,"orderType不为int");
            e.printStackTrace();
        }

        String body = "";//描述
        String subject = "";//标题
        String outTradeNo = "";//
        Double totalAmount = 0.0;//
        switch (orderType){
            case 1:{//1在线占卜订单
                TfOrder tfOrder = tfOrderService.findByOrderNo(orderNo,null);
                if(tfOrder==null){
                    log.error("用户：#"+userId+"不存在订单：#"+orderNo);
                    ResponseVo.send102Code(response,"用户：#"+userId+"不存在订单：#"+orderNo);
                    return;
                }
                outTradeNo = tfOrder.getoOrderNo();
                if(tfOrder.getoOrderType()!=orderType){
                    ResponseVo.send507Code(response,"该订单为在线占卜订单，与传入orderType不相符");
                    return;
                }
                if(TfOrder.orderStatusNoPay!=tfOrder.getoStatus()){
                    ResponseVo.send506Code(response,"支付或者取消的订单无法再次支付!");
                    return;
                }
                body = "大师#"+tfOrder.getTfMaster().getMasterId()+tfOrder.getTfMaster().getmNick()+"业务#"+tfOrder.getTfBusiness().getBusinessId()+tfOrder.getTfBusiness().getbName();
                totalAmount = (double)tfOrder.getoAmount()/100;
                subject = "在线占卜订单";
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
                outTradeNo = tfOrderTotal.gettOrderNo();
                body = "用户#"+tfOrderTotal.gettUserId()+"购物车商品";
                totalAmount = (double)tfOrderTotal.gettAmount()/100;
                subject = "商品订单";
                break;
            }
            default:{
                ResponseVo.send101Code(response,"orderType取值:1(在线占卜订单)、2(到访订单)、3（商品订单）");
                return;
            }
        }
        try {
            String res = AlipayUtil.alipay(body,subject,outTradeNo,totalAmount,orderType+"");
            JSONObject r = new JSONObject();
            r.put("orderStr",res);
            ResponseVo.send1Code(response,"success",r);
        } catch (AlipayApiException e) {
            ResponseVo.send999Code(response,"系统异常！");
            log.error(e);
            e.printStackTrace();
        }
    }

}
