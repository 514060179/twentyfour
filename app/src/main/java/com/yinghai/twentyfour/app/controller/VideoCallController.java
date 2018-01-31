package com.yinghai.twentyfour.app.controller;

import com.yinghai.twentyfour.common.model.TfOrder;
import com.yinghai.twentyfour.common.service.TfOrderService;
import com.yinghai.twentyfour.common.util.ResponseVo;
import com.yinghai.twentyfour.common.util.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2017/11/2.
 */
@RequestMapping("/app/video")
public class VideoCallController {

    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private TfOrderService tfOrderService;

    @RequestMapping(value = "play", method = RequestMethod.POST)
    public void getToken(HttpServletRequest request, HttpServletResponse response){

        String orderId = request.getParameter("orderId");
        if(StringUtil.empty(orderId)){
            ResponseVo.send101Code(response,"orderId为空！");
            return;
        }
        String accid = request.getParameter("accid");
        if(StringUtil.empty(accid)){
            ResponseVo.send101Code(response,"accid为空！");
            return;
        }
        TfOrder order = null;
        try {
            order = tfOrderService.findById(Integer.parseInt(orderId),null);
        }catch (Exception e){
            ResponseVo.send101Code(response,"orderId不为int");
            log.error(e);
            e.printStackTrace();
        }
        if (order==null){
            ResponseVo.send102Code(response,"该订单不存在#"+orderId);
        }
        String realm = request.getHeader("Realm");
        switch (realm){
            case "master":{

            }
            case "user":{

            }
        }
    }
}
