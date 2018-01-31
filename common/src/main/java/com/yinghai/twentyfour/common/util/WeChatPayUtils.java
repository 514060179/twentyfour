package com.yinghai.twentyfour.common.util;

import com.yinghai.twentyfour.common.constant.WeChat;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jdom2.JDOMException;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class WeChatPayUtils {

	public static String connect(String spbill_create_ip,String total_fee) {
		try {
			
		URL url = null;
		url = new URL("https://api.mch.weixin.qq.com/pay/unifiedorder");
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) url.openConnection();
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		connection.setDoInput(true);
		connection.setDoOutput(true);
		try {
			connection.setRequestMethod("POST");
		} catch (ProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String out_trade_no = sdf.format(date).toString();
		String nonce_str = random();
		String stringA = "appid="+ WeChat.weixinPayAppId+"&body=TaxiGo&mch_id="+WeChat.mchId+"&nonce_str="+nonce_str+"&notify_url=http://wxpay.wxutil.com/pub_v2/pay/notify.v2.php"
				+ "&out_trade_no="+out_trade_no+"&spbill_create_ip="+spbill_create_ip+"&total_fee="+total_fee+"&trade_type=APP";
		String stringSignTemp = stringA+"&key="+WeChat.weixinPayKey;
		String sign = MD5Encode(stringSignTemp, "utf-8").toUpperCase();
		System.out.println(sign.length());
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		sb.append("<appid>"+WeChat.weixinPayAppId+"</appid>");
		sb.append("<body>TaxiGo</body>");
		sb.append("<mch_id>"+WeChat.mchId+"</mch_id>");
		sb.append("<nonce_str>"+nonce_str+"</nonce_str>");
		sb.append("<notify_url>http://taxigomo.com:22023/TaxiGo-Server/wechat/weixinOrder</notify_url>");
		sb.append("<out_trade_no>"+out_trade_no+"</out_trade_no>");
		sb.append("<spbill_create_ip>"+spbill_create_ip+"</spbill_create_ip>");
		sb.append("<total_fee>"+total_fee+"</total_fee>");
		sb.append("<trade_type>APP</trade_type>");
		sb.append("<sign>"+sign+"</sign>");
		sb.append("</xml>");
		System.out.println(sb.toString());
		OutputStreamWriter outPut = null;
		outPut = new OutputStreamWriter(connection.getOutputStream());
		outPut.write(new String(sb.toString().getBytes("utf-8")));
		outPut.flush();
		outPut.close();
		Document doc = null;
		doc = new SAXReader().read(connection.getInputStream());
		Element element = doc.getRootElement();
		String prepay_id = element.elementText("prepay_id");
		if (prepay_id==null) {
			return "fail";
		}
		String nonce_str2=element.elementText("nonce_str");
		String timeStamp=timestamp(date);
		System.out.println(element.elementText("return_code"));
		System.out.println(element.elementText("return_msg"));
		System.out.println(prepay_id);
		String stringB="appid="+WeChat.weixinPayAppId+"&noncestr="+nonce_str2+"&package=Sign=WXPay&partnerid="+WeChat.mchId
				+ "&prepayid="+prepay_id+"&timestamp="+timeStamp;
		String stringSignTemp2 = stringB+"&key="+WeChat.weixinPayKey;
		System.out.println(stringSignTemp2);
		String sign2 = MD5Encode(stringSignTemp2, "utf-8").toUpperCase();
		return WeChat.weixinPayAppId+","+WeChat.mchId+","+prepay_id+","+nonce_str2+","+timeStamp+","+sign2+","+WeChat.weixinPayKey;
		} 
		catch (Exception e) {
			// TODO: handle exception
			return "fail";
		}
	}
	/**
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * 查询微信支付订单
	 */
	public static String queryOrder() throws Exception {
		URL url =new URL("https://api.mch.weixin.qq.com/pay/orderquery");
		HttpURLConnection connection = null;
		connection = (HttpURLConnection) url.openConnection();
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String out_trade_no = sdf.format(date).toString();
		String nonce_str = random();
		String stringA = "appid="+WeChat.weixinPayAppId+"&mch_id="+WeChat.mchId+"&nonce_str="+nonce_str+"&out_trade_no="+out_trade_no;
		String stringTemp = stringA+"&key="+WeChat.weixinPayKey;
		String sign = MD5Encode(stringTemp, "utf-8").toUpperCase();
		System.out.println(stringTemp);
		System.out.println(sign);
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		sb.append("<appid>"+WeChat.weixinPayAppId+"</appid>");
		sb.append("<mch_id>"+WeChat.mchId+"</mch_id>");
		sb.append("<nonce_str>"+nonce_str+"</nonce_str>");
		sb.append("<out_trade_no>"+out_trade_no+"</out_trade_no>");
		sb.append("<sign>"+sign+"</sign>");
		sb.append("</xml>");
		System.out.println(sb.toString());
		OutputStreamWriter output = new OutputStreamWriter(connection.getOutputStream());
		output.write(new String(sb.toString().getBytes("utf-8")));
		output.flush();
		output.close();
		Document doc = new SAXReader().read(connection.getInputStream());
		Element element = doc.getRootElement();
		System.out.println(element.elementText("return_code"));
		System.out.println(element.elementText("return_msg"));
		System.out.println(element.elementText("trade_type"));
		System.out.println(element.elementText("total_fee"));
		return element.elementText("return_code")+" "+element.elementText("return_msg");
		
	}
	/**
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * 关闭订单
	 */
	public static String closeOrder() throws Exception{
		URL url = new URL("https://api.mch.weixin.qq.com/pay/closeorder");
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String out_trade_no = sdf.format(date).toString();
		String nonce_str = random();
		String stringA = "appid="+WeChat.weixinPayAppId+"&mch_id="+WeChat.mchId+"&nonce_str="+nonce_str+"&out_trade_no="+out_trade_no;
		String stringTemp = stringA+"&key="+WeChat.weixinPayKey;
		String sign = MD5Encode(stringTemp, "utf-8").toUpperCase();
		System.out.println(stringTemp);
		System.out.println(sign);
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		sb.append("<appid>"+WeChat.weixinPayAppId+"</appid>");
		sb.append("<mch_id>"+WeChat.mchId+"</mch_id>");
		sb.append("<nonce_str>"+nonce_str+"</nonce_str>");
		sb.append("<out_trade_no>"+out_trade_no+"</out_trade_no>");
		sb.append("<sign>"+sign+"</sign>");
		sb.append("</xml>");
		System.out.println(sb.toString());
		OutputStreamWriter output = new OutputStreamWriter(connection.getOutputStream());
		output.write(new String(sb.toString().getBytes("utf-8")));
		output.flush();
		output.close();
		Document doc = new SAXReader().read(connection.getInputStream());
		Element element = doc.getRootElement();
		return element.elementText("return_code")+" "+element.elementText("return_msg");
	}
	private static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			resultSb.append(byteToHexString(b[i]));
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static String MD5Encode(String origin, String charsetname) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (charsetname == null || "".equals(charsetname))
				resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
			else
				resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
		} catch (Exception exception) {
		}
		return resultString;
	}

	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
			"e", "f" };
	
	public static String random(){
		int i = (int)(Math.random()*32)+1;
		StringBuilder sb = new StringBuilder();
		for (int j = 0; j < i; j++) {
			int temp = (int)(Math.random()*8)+1;
			sb.append(temp);
		}
		return sb.toString();
	}
	public static String timestamp(Date today) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now=sdf.format(today);
		Date orderDate = null;
		Date originDate = null;
		try {
			orderDate = sdf.parse(now);
			originDate = sdf.parse("1970-01-01 00:00:00");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long day = orderDate.getTime()-originDate.getTime();
		return String.valueOf((day/1000));
	}
	
	
	/**
     * 检验API返回的数据里面的签名是否合法，避免数据在传输的过程中被第三方篡改
     *
     * @param map API返回的XML数据字符串
     * @return API签名是否合法
     * @throws IOException
     */
    public static  boolean checkIsSignValidFromResponseString(Map<String, Object> map,String payType) {
        try {
            //Map<String, Object> map = XMLParser.getMapFromXML(responseString);
        	//log.debug(map.toString());
            String signFromAPIResponse = map.get("sign").toString();
            if ("".equals(signFromAPIResponse) || signFromAPIResponse == null) {
            //	log.debug("API返回的数据签名数据不存在，有可能被第三方篡改!!!");
                return false;
            }
          //  log.debug("服务器回包里面的签名是:" + signFromAPIResponse);
            //清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
            map.put("sign", "");
            //将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
            String signForAPIResponse = WeChatPayUtils.createSign("UTF-8", map,payType);
            if (!signForAPIResponse.equals(signFromAPIResponse)) {
                //签名验不过，表示这个API返回的数据有可能已经被篡改了
            //	log.debug("API返回的数据签名验证不通过，有可能被第三方篡改!!!");
                return false;
            }
            //log.debug("恭喜，API返回的数据签名验证通过!!!");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    
    /**
	 * 签名工具
	 * @author wangkai
	 * @date 2014-12-5下午2:29:34
	 * @Description：sign签名
	 * @param characterEncoding
	 *            编码格式 UTF-8
	 * @param parameters
	 *            请求参数
	 * @return
	 */
	public static String createSign(String characterEncoding,Map<String, Object> parameters,String payType) {
//		if(parameters.get("attach")!=null){//当该订单为去澳门微信公众号支付时，取出附加数据
//			payType = WeChat.weixinJSAPPayType2;
//		}
		parameters  = MapUtil.sortMapByKey(parameters);
		StringBuffer sb = new StringBuffer();
		Iterator<Entry<String, Object>> it = parameters.entrySet().iterator();
		while (it.hasNext()) {
			Entry <String,Object>entry = (Entry<String,Object>) it.next();
			String key = (String) entry.getKey();
			Object value = entry.getValue();//去掉带sign的项
			if (null != value && !"".equals(value) && !"sign".equals(key)
					&& !"key".equals(key)) {
				sb.append(key + "=" + value + "&");
			}
		}
		if(WeChat.weixinAPPPayType.equals(payType)){
			sb.append("key=" + WeChat.weixinPayKey);
		}else{
			sb.append("key=" + WeChat.JSAPIkey);
		}
		
		//注意sign转为大写
		System.out.println(sb.toString());
		return MD5Encode(sb.toString(), characterEncoding).toUpperCase();
	}
	
	/**
	 * @author wangkai
	 * @date
	 * @Description：将请求参数转换为xml格式的string
	 * @param parameters
	 *            请求参数
	 * @return
	 */
	public static String getRequestXml(Map<String, Object> parameters) {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		Iterator<Entry<String, Object>> iterator = parameters.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String,Object> entry = (Entry<String,Object>) iterator.next();
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			if ("attach".equalsIgnoreCase(key) || "body".equalsIgnoreCase(key)
					|| "sign".equalsIgnoreCase(key)) {
				sb.append("<" + key + ">" + "<![CDATA[" + value + "]]></" + key + ">");
			} else {
				sb.append("<" + key + ">" + value + "</" + key + ">");
			}
		}
		sb.append("</xml>");
		return sb.toString();
	}

	/**
	 * 微信AAPI統一下單
	 * @param ip
	 * @param totalFee
	 * @param orderNo
	 * @param payType
	 * @param orderType
	 * @param body
	 * @return
	 * @throws IOException
	 */
	public static String connection(String ip, String totalFee, String orderNo,String payType,String orderType,String body) throws  IOException {
		// TODO Auto-generated method stub
		String mchId = "";
		String appid = "";
		String tradeType = "";
		if(WeChat.weixinAPPPayType.equals(payType)){
			mchId = WeChat.mchId;
			appid = WeChat.weixinPayAppId;
			tradeType = WeChat.weixinAPPPayType;
		}else{
			mchId = WeChat.JSAPImchId;
			appid = WeChat.JSAPIAppId;
			tradeType = WeChat.weixinJSAPPayType;
		}
		Date date = new Date();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appid", appid);// 服务号的应用号
		map.put("body", body);// 商品描述
		map.put("mch_id", mchId);// 商户号 ？
		map.put("nonce_str",  random());// 16随机字符串(大小写字母加数字)
		map.put("out_trade_no", orderNo);// 商户订单号
		map.put("total_fee", totalFee);// 价格
		map.put("spbill_create_ip", ip);// 终端IP
		map.put("notify_url", WeChat.WEBCHAT_PAY_CALL_BACK); // 微信回调地址
		map.put("trade_type", tradeType);// 支付类型 app//JSAPI
		map.put("attach", orderType);// 订单类型
		map.put("sign", WeChatPayUtils.createSign("UTF-8", map,payType));
		map = MapUtil.sortMapByKey(map);
		JSONObject responseObject = new JSONObject();
		// 排序
		String requestXML = WeChatPayUtils.getRequestXml(map);
		/** 发送并接收返回的数据 */
		String responseStr = HttpRequester.httpsRequest(WeChat.wechat_pay_url, "POST", requestXML);// 带上post
		Map<String, Object> resutlMap = null;
		try {
			resutlMap = XMLUtil.doXMLParse(responseStr);
		} catch (JDOMException e) {
			throw new RuntimeException(e);
		}
		if (resutlMap != null && "FAIL".equals(resutlMap.get("return_code"))) {
			responseObject.put("msg", resutlMap);
			throw new RuntimeException(responseObject.toString());
		}
		//签名验证
		if (!WeChatPayUtils.checkIsSignValidFromResponseString(resutlMap, payType)) {
			throw new RuntimeException("微信统一下单失败,签名可能被篡改!");
		}
		map = new HashMap<String, Object>();
		String sign = "";
		if(WeChat.weixinJSAPPayType.equals(payType)){
			map.put("appId", appid);
			map.put("timeStamp", WeChatPayUtils.timestamp(date));
			map.put("nonceStr", (resutlMap.get("nonce_str") + "").trim());
			map.put("package", "prepay_id="+(resutlMap.get("prepay_id") + "").trim());
			map.put("signType", "MD5");
		}else{
			map.put("appid", appid);
			map.put("partnerid", mchId);
			map.put("timestamp", WeChatPayUtils.timestamp(date));
			map.put("noncestr", (resutlMap.get("nonce_str") + "").trim());
			map.put("package", "Sign=WXPay");
			map.put("prepayid", (resutlMap.get("prepay_id") + "").trim());
		}
		sign = WeChatPayUtils.createSign("UTF-8", map,payType);
		map.put("sign", sign);
		if(WeChat.weixinJSAPPayType.equals(payType)){
			map.put("packages", "prepay_id="+(resutlMap.get("prepay_id") + "").trim());
		}
		Map<String, String> data = new HashMap<String, String>();
		data.put("sign", sign);
		data.put("prepayid", resutlMap.get("prepay_id") + "");
		responseObject.put("msg", "操作成功");
		responseObject.put("code", "1");
		responseObject.put("data", map);
		//ResponseUtils.renderJson(response, responseObject.toString());
		return responseObject.toString();
	}
	/**
	 *  微信API：查詢訂單
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static Map queryWeChatOrder(String outTradeNo,String payType) throws JDOMException, IOException {
		// TODO Auto-generated method stub
		String mchId = "";
		String appid = "";
		if(WeChat.weixinAPPPayType.equals(payType)){
			mchId = WeChat.mchId;
			appid = WeChat.weixinPayAppId;
		}else{
			mchId = WeChat.JSAPImchId;
			appid = WeChat.JSAPIAppId;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appid", appid);// 服务号的应用号
		// map.put("body", body);// 商品描述
		map.put("mch_id", mchId);// 商户号 ？
		map.put("nonce_str",  random());// 16随机字符串(大小写字母加数字)
		map.put("out_trade_no", outTradeNo);// 微信订单号
		// map.put("notify_url",
		// "http://wxpay.wxutil.com/pub_v2/pay/notify.v2.php"); // 微信回调地址
		// map.put("trade_type", "APP");// 支付类型 app
		map.put("sign", WeChatPayUtils.createSign("UTF-8", map, payType));
		map = MapUtil.sortMapByKey(map);
		// 排序
		String requestXML = WeChatPayUtils.getRequestXml(map);
		/** 发送并接收返回的数据 */
		String responseStr = HttpRequester.httpsRequest(WeChat.wechat_QUERY_url, "POST", requestXML);// 带上post
		Map<String, Object> resutlMap = XMLUtil.doXMLParse(responseStr);
		return resutlMap;
	}
	/**
	 * 微信API:關閉訂單
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	public  static Map closeWeChatOrder(String outTradeNo,String payType) throws JDOMException, IOException {
		// TODO Auto-generated method stub
		String mchId = "";
		String appid = "";
		if(WeChat.weixinAPPPayType.equals(payType)){
			mchId = WeChat.mchId;
			appid = WeChat.weixinPayAppId;
		}else {
			mchId = WeChat.JSAPImchId;
			appid = WeChat.JSAPIAppId;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appid", appid);// 服务号的应用号
		map.put("mch_id", mchId);// 商户号
		map.put("out_trade_no", outTradeNo);// 商户订单号
		map.put("nonce_str",  random());// 16随机字符串(大小写字母加数字)
		map.put("sign", WeChatPayUtils.createSign("UTF-8", map, payType));
		map = MapUtil.sortMapByKey(map);
		// 排序
		String requestXML = WeChatPayUtils.getRequestXml(map);
		/** 发送并接收返回的数据 */
		String responseStr = HttpRequester.httpsRequest(WeChat.wechat_CLOSE_url, "POST", requestXML);// 带上post
		Map<String, Object> resutlMap = XMLUtil.doXMLParse(responseStr);
		return resutlMap;
	}
	/**
	 * 微信API：申請退款操作
	 * @return
	 * @throws Exception 
	 */
	public static Map orderrefund(String outTradeNo, String totalFee, String refundFee,String payType) throws Exception {
		// TODO Auto-generated method stub
		String mchId = "";
		String appid = "";
		if(WeChat.weixinAPPPayType.equals(payType)){
			mchId = WeChat.mchId;
			appid = WeChat.weixinPayAppId;
		}else{
			mchId = WeChat.JSAPImchId;
			appid = WeChat.JSAPIAppId;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String out_refund_no = sdf.format(date).toString();//退款单号
		map.put("appid", appid);// 服务号的应用号
		map.put("mch_id",mchId);// 商户号
		map.put("out_trade_no", outTradeNo);// 商户订单号
		map.put("out_refund_no", out_refund_no);// 退款订单号
		map.put("total_fee", totalFee);// 订单金额
		map.put("refund_fee", refundFee);// 退款金额
		map.put("op_user_id", WeChat.mchId);// 操作人
		map.put("nonce_str",  random());// 16随机字符串(大小写字母加数字)
		map.put("sign", WeChatPayUtils.createSign("UTF-8", map, payType));
		map = MapUtil.sortMapByKey(map);
		// 排序
		String requestXML = WeChatPayUtils.getRequestXml(map);
		//String requestXML = "<xml><appid>wxd74af2b277375f05</appid><mch_id>1380512402</mch_id><nonce_str>Qc58lKlKBZflUDXG</nonce_str><op_user_id>1380512402</op_user_id><out_refund_no>20170503153555</out_refund_no><out_trade_no>20170503101452</out_trade_no><refund_fee>1</refund_fee><total_fee>1</total_fee><transaction_id></transaction_id><sign><![CDATA[FDEB4D0FB617965647D3D3ADF05517A2]]></sign></xml>";
		/** 发送并接收返回的数据 */
		String responseStr = doRefund(requestXML,payType);
		//String responseStr = HttpRequester.httpsRequest(WeChat.wechat_refund_url, "POST", requestXML);// 带上post
		Map<String, Object> resutlMap = XMLUtil.doXMLParse(responseStr);
		return resutlMap;
	}
	/**
	 * 微信APi:退款查詢
	 * @param outTradeNo 商戶訂單號
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static Map refundquery(String outTradeNo,String payType) throws JDOMException, IOException {
		// TODO Auto-generated method stub
		String mchId = "";
		String appid = "";
		if(WeChat.weixinAPPPayType.equals(payType)){
			mchId = WeChat.mchId;
			appid = WeChat.weixinPayAppId;
		}else{
			mchId = WeChat.JSAPImchId;
			appid = WeChat.JSAPIAppId;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appid", appid);// 服务号的应用号
		map.put("mch_id", mchId);// 商户号
		map.put("nonce_str",  random());// 16随机字符串(大小写字母加数字)
		map.put("out_trade_no", outTradeNo);// 商户订单号
		map.put("sign", WeChatPayUtils.createSign("UTF-8", map, payType));
		map = MapUtil.sortMapByKey(map);
		// 排序
		String requestXML = WeChatPayUtils.getRequestXml(map);
		/** 发送并接收返回的数据 */
		String responseStr = HttpRequester.httpsRequest(WeChat.wechat_refundquery_url, "POST", requestXML);// 带上post
		Map<String, Object> resutlMap = XMLUtil.doXMLParse(responseStr);
		return resutlMap;
	}

	public static String doRefund(String data,String  payType) throws Exception {
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		String mchid = "";
		FileInputStream is = null;
		//为APP微信退款
		if(StringUtil.notEmpty(payType)&&payType.equals(WeChat.weixinAPPPayType)){
			is = new FileInputStream(new File(WeChat.apiclientCert));
			mchid = WeChat.mchId;
		}else{//微信公众号退款
			is = new FileInputStream(new File(WeChat.apiclientCertJsapi));
			mchid = WeChat.JSAPImchId;
		}
		keyStore.load(is, mchid.toCharArray());// 这里写密码..默认是你的MCHID

		// Trust own CA and all self-signed certs
		SSLContext sslcontext = SSLContexts.custom()
				.loadKeyMaterial(keyStore, mchid.toCharArray())// 这里也是写密码的
				.build();
		// Allow TLSv1 protocol only
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
				sslcontext,
				new String[] { "TLSv1" },
				null,
				SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		CloseableHttpClient httpclient = HttpClients.custom()
				.setSSLSocketFactory(sslsf)
				.build();

		//HttpGet httpget = new HttpGet("https://api.mch.weixin.qq.com/secapi/pay/refund");
		HttpPost httppost = new HttpPost(WeChat.wechat_refund_url);
		httppost.setEntity(new StringEntity(data, "UTF-8"));
		System.out.println("executing request" + httppost.getRequestLine());
		CloseableHttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();
		String jsonStr = EntityUtils.toString(response.getEntity(),
				"UTF-8");
		EntityUtils.consume(entity);
		return jsonStr;
	}
}
