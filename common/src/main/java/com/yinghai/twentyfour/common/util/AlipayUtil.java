package com.yinghai.twentyfour.common.util;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.yinghai.twentyfour.common.constant.Alipay;

/**
 * Created by Administrator on 2017/10/30.
 */
public class AlipayUtil {


    /**
     * 发起支付，下单
     * @param body 对一笔交易的具体描述信息
     * @param subject 商品的标题/交易标题/订单标题/订单关键字等
     * @param outTradeNo 商户网站唯一订单号
     * @param totalAmount 订单总额
     * @param orderType 订单类型 1在线占卜订单2到访占卜订单3商品订单
     * @return
     */
    public static String alipay(String body,String subject,String outTradeNo,Double totalAmount,String orderType) throws AlipayApiException {
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(Alipay.serverUrl, Alipay.appId, Alipay.privateKey, "json", "utf-8", Alipay.alipayPKey, "RSA2");
//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setSellerId(Alipay.sellerId);
        model.setBody(body);
        model.setSubject(subject);
        model.setOutTradeNo(outTradeNo);
        model.setTimeoutExpress("15d");
        model.setTotalAmount(totalAmount+"");
        model.setProductCode("QUICK_MSECURITY_PAY");
        model.setPassbackParams(orderType);
        request.setBizModel(model);
        request.setNotifyUrl(Alipay.notifyUrl);//回调
        AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
        System.out.println(response.getBody());
        return response.getBody();
    }


    public static void test() throws AlipayApiException {
//        AlipayClient alipayClient = new DefaultAlipayClient(Alipay.serverUrl,Alipay.appId,Alipay.privateKey,"json","uft-8",Alipay.alipayPKey,"RSA2");
        AlipayClient alipayClient = new DefaultAlipayClient(Alipay.serverUrl,Alipay.appId,Alipay.privateKey,"json","utf-8",Alipay.alipayPKey,"RSA2");

        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setBizContent("{" +
                "\"out_trade_no\":\"20150320010101001\"," +
//                "\"seller_id\":\"2088102171405184\"," +
                "\"total_amount\":88.88," +
//                "\"discountable_amount\":8.88," +
                "\"subject\":\"Iphone616G\"," +
                "\"goods_detail\":[{" +
                "\"goods_id\":\"apple-01\"," +
                "\"goods_name\":\"ipad\"," +
                "\"quantity\":1," +
                "\"price\":2000," +
                "\"goods_category\":\"34543238\"," +
                "\"body\":\"特价手机\"," +
                "\"show_url\":\"http://www.alipay.com/xxx.jpg\"" +
                "}]," +
                "\"body\":\"Iphone616G\"" +
//                "\"operator_id\":\"yx_001\"," +
//                "\"store_id\":\"NJ_001\"," +
//                "\"disable_pay_channels\":\"pcredit,moneyFund,debitCardExpress\"," +
//                "\"enable_pay_channels\":\"pcredit,moneyFund,debitCardExpress\"," +
//                "\"terminal_id\":\"NJ_T_001\"," +
//                "\"extend_params\":{" +
//                "\"sys_service_provider_id\":\"2088511833207846\"" +
//                "}," +
//                "\"timeout_express\":\"90m\"," +
//                "\"business_params\":\"{\\\"data\\\":\\\"123\\\"}\"" +
                "}");
        AlipayTradePrecreateResponse response = alipayClient.execute(request);
        if(response.isSuccess()){
            System.out.println("调用成功"+response.getBody());
        } else {
            System.out.println("调用失败"+response.getBody());
        }
    }
    public static void test1(){
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(Alipay.serverUrl, Alipay.appId, Alipay.privateKey, "json", "utf-8", Alipay.alipayPKey, "RSA2");
//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setSellerId("1231231");
        model.setBody("我是测试数据");
        model.setSubject("App支付测试Java");
        model.setOutTradeNo("20150320010101001");
        model.setTimeoutExpress("30m");
        model.setTotalAmount("0.01");
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl("商户外网可以访问的异步地址");
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
            System.out.println(response.getOutTradeNo());
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * 支付宝退款
     * @param outTradeNo 订单编号
     * @param outRequestNo 同一笔交易多次退款需要填写
     * @param refundAmount 退款金额
     * @param refundReason 退款原因
     * @throws AlipayApiException
     */
    public static AlipayTradeRefundResponse refund(String outTradeNo,String outRequestNo,Double refundAmount,String refundReason) throws AlipayApiException{
        AlipayClient alipayClient = new DefaultAlipayClient(Alipay.serverUrl, Alipay.appId, Alipay.privateKey, "json", "utf-8", Alipay.alipayPKey, "RSA2"); //获得初始化的AlipayClient
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();//创建API对应的request类
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        model.setOutTradeNo(outTradeNo);
        model.setTradeNo(null);
        model.setOutRequestNo(outRequestNo);//同一笔交易多次退款需要填写
        model.setRefundAmount(refundAmount+"");//退款金额
        model.setRefundReason(refundReason);//退款原因
        request.setBizModel(model);
        AlipayTradeRefundResponse response = alipayClient.execute(request);//通过alipayClient调用API，获得对应的response类
        System.out.println(response.getBody());
        return response;
    }

    public static void query(String outTradeNo,String tradeNo) throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(Alipay.serverUrl, Alipay.appId, Alipay.privateKey, "json", "utf-8", Alipay.alipayPKey, "RSA2"); //获得初始化的AlipayClient
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();//创建API对应的request类
//        request.setBizContent("{" +
//                "   \"out_trade_no\":\"20150320010101001\"" +
////                "   \"trade_no\":\"20150320010101001\"" +
//                "  }");//设置业务参数
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setOutTradeNo(outTradeNo);
        model.setTradeNo(tradeNo);
        request.setBizModel(model);
        AlipayTradeQueryResponse response = alipayClient.execute(request);//通过alipayClient调用API，获得对应的response类
        System.out.println(response.getBody());
    }

    public static void main(String[] args) throws AlipayApiException {
        alipay("大师#2邱逢生业务#36测试2017","在线占卜订单","2017121314272024251733",0.01,"1");
//        refund("ceshi001","ceshi001",0.11,"test");
//        query("ceshi001","ceshi001");
//        test();
//        test1();
    }
}
