package com.yinghai.twentyfour.common.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class StringJudge {

	public static boolean JudgeIsNotNull(String str, boolean flag) {
		boolean result = false;
		if (str != null && !"".equals(str)) {
			if (flag) {
				if (str.trim().length() > 0)
					result = true;
			} else {
				result = true;
			}
		}
		return result;
	}
	
	public static String getFilterString(String str,String prefix){
		String result="";
		if(str!=null && !"".equals(str)){
			int index1=str.lastIndexOf(prefix);
			if(index1>-1){
				result=str.substring(0, str.length()-1);
			}else{
				result=str;
			}
		}
		return result;
	}

	public static String getServerIpPrefix(HttpServletRequest request){
		return request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
	}

	public static String getServerIp() {
		String serverIp = "";
		try {
			Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ip = null;
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = netInterfaces.nextElement();
				ip = ni.getInetAddresses().nextElement();
				serverIp = ip.getHostAddress();
				if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
					serverIp = ip.getHostAddress();
					break;
				} else {
					ip = null;
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}

		return serverIp;
	}

	public static String getLocalIP() {
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		byte[] ipAddr = addr.getAddress();
		String ipAddrStr = "";
		for (int i = 0; i < ipAddr.length; i++) {
			if (i > 0) {
				ipAddrStr += ".";
			}
			ipAddrStr += ipAddr[i] & 0xFF;
		}
		return ipAddrStr;
	}


	public static String parseResult(String xml, String name) throws Exception {
		String status = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
			Document dom = builder.parse(inputStream);
			NodeList nodeList = dom.getElementsByTagName(name);
			Node rootNode = nodeList.item(0);
			Element childNode = (Element) rootNode;
			status = childNode.getFirstChild().getNodeValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}


	
	/*public static String getXMLByString(Object obj){
		String result="<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		result+="<result>";
		if(obj!=null){			
			JSONObject json=new JSONObject(obj);
			try {
				result+=XML.toString(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
		result+="</result>";		
		return result;
	}*/
	

	
//	public static void testWebServiceByRechargeInfo(){
//		// 调用WebService
//		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
////		factory.setServiceClass(ClientOrderService.class);
//		factory.setAddress("http://localhost:8080/PayGateway/ws/clientOrderService2");
//
//		QueryRechargeService service = (QueryRechargeService) factory.create();
//
//		System.out.println("#############ClientOrderServiceWs findOrderInfo##############");
//		String params="{version:v1.0,clientCode:C000001}";
//		//String result = service.findRechargeInfoByCondition(params);
//		//System.out.println("=============:" + result);
//
//		//ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "demo/order/client/client-beans.xml" });
//
//	}
	

//	public static void main(String[] args) throws Exception {
		// String url = "/recharge/callBack4Stage.do?payResultCode=S&payResultDesc=成功&payResultCode=S";
		// // String url="/recharge/callBack4Stage.do";
		// String[] keys = { "payResultCode" };
		// String[] values = { "A" };
		// String result = StringJudge.replaceSpecValue(url, keys, values);
		// System.out.println("---------url:" + url);
		// System.out.println("=============" + result);

		//StringJudge.stringToJson();
		//String f="1.0";
		//System.out.println(PropertyUtil.getMessagesProperty("err.requiredfield.required","版本1,版本2"));
		
		//StringJudge.calStrToLong("365*24 * 60 * 60 * 1000");
		
//		Timestamp startTime=null;
//		Timestamp endTime=null;
//		boolean flag=true;
//		String startTimeStr="2012-02-01";
//		if(ValidateUtil.StrNotNull(startTimeStr)){
//			try {
//				startTime=Timestamp.valueOf(startTimeStr);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		System.out.println(startTime.toString());
//		StringJudge.correctEncode();
//		String str="{PRG.ACCOUNT:******,PRG.CUST_MOBILE:****** }";
//	}
}
