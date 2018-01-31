package com.yinghai.twentyfour.common.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.yinghai.twentyfour.common.model.VersionControl;
import com.yinghai.twentyfour.common.service.VersionControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.alipay.api.AlipayConstants.CHARSET;

/**
 * Created by Administrator on 2017/7/18.
 */
@Controller
@RequestMapping("test")
public class TestController {
    @Autowired
    private VersionControllerService versionControllerService;

    @RequestMapping(value = "version", method = RequestMethod.GET)
    public void test(HttpServletRequest request, HttpServletResponse response){
        VersionControl versionControl = new VersionControl();
        versionControl.setDeviceType("1");
        versionControl.setForceUpdate(false);
        versionControl.setVersionId("1.0.0");
        versionControl.setRealm("driver");
        versionControl.setCreateTime(new Date());
        versionControl.setPriority(99);
        System.out.println(versionControllerService.saveVersionController(versionControl));
        VersionControl v = new VersionControl();
        v.setId(1);
        System.out.println(versionControllerService.findByCondition(v));
        System.out.println(versionControllerService.findByPage(2,5,new VersionControl()));
        v.setPriority(22);
        System.out.println(versionControllerService.updateVersionController(v));
    }

    public void test1(){
        //实例化客户端  这里会生成签名
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", "211564", "4sdada5sd5da1tggdddeyh", "json", CHARSET, "1145454545", "RSA2");
//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody("我是测试数据");//交易或商品信息描述
        model.setSubject("App支付测试Java");//订单标题
        model.setOutTradeNo("201707311645");//订单id
        model.setTimeoutExpress("30m");//订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。
        model.setTotalAmount("0.01");//支付总额
        model.setProductCode("http://api.test.alipay.net/atinterface/receive_notify.htm");//销售产品码，商家和支付宝签约的产品码
        request.setBizModel(model);
        request.setNotifyUrl("商户外网可以访问的异步地址");//响应url
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
    }

    public void test2(HttpServletRequest request){
        //获取支付宝POST过来反馈信息
        Map< String,String> params = new HashMap< String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
        //boolean AlipaySignature.rsaCheckV1(Map<String, String>params, String publicKey, String charset, String sign_type)
        try {
            boolean flag = AlipaySignature.rsaCheckV1(params, "", "", "RSA2");
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
    }
}
