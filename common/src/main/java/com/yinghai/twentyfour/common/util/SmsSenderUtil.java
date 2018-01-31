package com.yinghai.twentyfour.common.util;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


import com.yinghai.twentyfour.common.constant.Constant;
import net.sf.json.JSONObject;


public class SmsSenderUtil {
    /**
     *  获取随机码
     * @return
     */
	public int getRandom() {
		Random random = new Random();
    	return random.nextInt(999999)%900000+100000;
    }

    /**
     * 计算多个号码加密方式
     * @param appkey
     * @param random
     * @param curTime
     * @param phoneNumbers
     * @return
     * @throws NoSuchAlgorithmException
     */
	/*public String calculateSig(
    		String appkey,
    		long random,
    		long curTime,
    		List<String> phoneNumbers) throws NoSuchAlgorithmException {
        String phoneNumbersString = phoneNumbers.get(0);
        for (int i = 1; i < phoneNumbers.size(); i++) {
            phoneNumbersString += "," + phoneNumbers.get(i);
        }
        return strToHash(String.format(
        		"appkey=%s&random=%d&time=%d&mobile=%s",
        		appkey, random, curTime, phoneNumbersString));
    }*/

    /**
     * 使用自定义加密方式
     * @param byteArray
     * @return
     */
	public String byteArrayToHex(byte[] byteArray) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] resultCharArray = new char[byteArray.length * 2];
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }
        return new String(resultCharArray);
    }

    /**
     *  使用 SHA-256 加密方式
     * @param str
     * @return
     * @throws NoSuchAlgorithmException
     */
	protected String strToHash(String str) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] inputByteArray = str.getBytes();
        messageDigest.update(inputByteArray);
        byte[] resultByteArray = messageDigest.digest();
        return byteArrayToHex(resultByteArray);
    }

    /**
     * 默认模板发送短信
     * @param nationcode 区号
     * @param mobile 手机号
     * @param msg 消息
     * @return
     * @throws Exception
     */
    public static JSONObject imSMSSend( String nationcode,String mobile,String msg) throws Exception {
	    String[] m = {msg};
        return imSMSSend("1400029919",nationcode,mobile,"0", "珠海瀛海","25064","","",m);
    }
    /**
     * 指定签名、模板发送云短信
     * @param sign 短信签名
     * @param tplId 短信模板
     * @param nationcode 区号
     * @param mobile 号码
     * @param msg 短信内容
     * @return
     * @throws Exception
     */
/*    public static JSONObject imSMSSend(String sign,String tplId, String nationcode,String mobile,String[] msg) throws Exception {
        return imSMSSend(Constant.sdkAppId,nationcode,mobile,"0",sign,tplId,"","",msg);
    }*/


    /**
     * 指定签名、模板发送云短信
     * @param mobileArray 手机号码json数组 {"nationcode": "86", "mobile": "13788888888"  },{ "nationcode": "86",  "mobile": "13788888889" }
     * @param sign 短信签名
     * @param tplId 短信模板
     * @param msg 发送内容
     * @return
     * @throws Exception
     */
/*    public static JSONObject imSMSSend( JSONArray mobileArray,String sign,String tplId,String[] msg) throws Exception {
        return imSMSSend(Constant.sdkAppId,mobileArray,"0",sign,tplId,"","",msg);
    }*/

    /**
     * 默认模板发送群短信
     * @param mobileArray 手机号码json数组 {"nationcode": "86", "mobile": "13788888888"  },{ "nationcode": "86",  "mobile": "13788888889" }
     * @param msg 发送内容
     * @return
     * @throws Exception
     */
/*    public static JSONObject imSMSSend( JSONArray mobileArray,String msg) throws Exception {
        String[] m = {msg};
        return imSMSSend(Constant.sdkAppId,mobileArray,"0",Constant.sig,Constant.splId,"","",m);
    }*/
    /**
     * 指定应用名、签名、模板发送群云短信
     * @param sdkappid 应用id
     * @param mobileArray 手机号码组
     * @param type 短信类型
     * @param sign 短信签名
     * @param tplId 短信模板
     * @param extend 通道扩展码，可选字段，默认没有开通(需要填空)。
     * @param ext 用户的session内容，腾讯server回包中会原样返回，可选字段，不需要就填空。
     * @param msg 短信内容
     * @return
     * @throws Exception
     */
    /*public static JSONObject imSMSSend(String sdkappid, JSONArray mobileArray, String type, String sign,String tplId, String extend, String ext, String[] msg) throws Exception {
        Map<String,String> map = new HashMap<String,String>();
        SmsSenderUtil util = new SmsSenderUtil();
        int random = util.getRandom();
        long curTime =  System.currentTimeMillis()/1000;
        map.put("sdkappid",sdkappid);
        map.put("random",random+"");
        String urlStr = Constant.SEND_GROUP_SMS_MSG_URL;
        String urlString = urlStr+HttpRequester.getIMURLString(map);
        List<String> telList = new ArrayList<String>();
        for (Object a:mobileArray
                ) {
            telList.add(((JSONObject)a).getString("mobile"));
        }
        JSONObject data = new JSONObject();
        data.put("tel", mobileArray);
        data.put("type", type);
        data.put("extend", extend);
        data.put("ext", ext);
        data.put("sign", sign);
        data.put("tpl_id", tplId);
        data.put("params", msg);
        data.put("sig", util.calculateSig(Constant.appkey,random,curTime,telList));
        data.put("time", curTime+"");
        System.out.println(urlString);
        URL object = new URL(urlString);
        HttpURLConnection conn;
        conn = (HttpURLConnection) object.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestMethod("POST");
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
        wr.write(data.toString());
        wr.flush();
        // 显示 POST 请求返回的内容
        StringBuilder sb = new StringBuilder();
        int httpRspCode = conn.getResponseCode();
        if (httpRspCode == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            System.out.println(sb);
        } else {
            System.out.println("error");
        }
        return JSONObject.fromObject(sb.toString());
    }*/
    /**
     * 发送云短信
     * @param sdkappid 应用id
     * @param nationcode
     * @param mobile 手机号码
     * @param type 短信类型
     * @param sign 签名模板
     * @param extend 通道扩展码，可选字段，默认没有开通(需要填空)。
     * @param ext 用户的session内容，腾讯server回包中会原样返回，可选字段，不需要就填空。
     * @param msg 短信内容
     * @return
     * @throws Exception
     */
    public static JSONObject imSMSSend(String sdkappid, String nationcode, String mobile, String type,String sign, String tplId,String extend, String ext, String[] msg) throws Exception {
        Map<String,Object> map = new HashMap<String,Object>();
        SmsSenderUtil util = new SmsSenderUtil();
        int random = util.getRandom();
        long curTime =  System.currentTimeMillis()/1000;
        map.put("sdkappid",sdkappid);
        map.put("random",random+"");
        String urlStr = Constant.SEND_SMS_MSG_URL;
        String urlString = urlStr+HttpRequester.getIMURLString(map);
        System.out.println(urlString);
        JSONObject data = new JSONObject();
        JSONObject tel = new JSONObject();

        System.out.println(curTime+"  "+random);
        tel.put("nationcode",nationcode);
        tel.put("mobile", mobile);

        data.put("type", type);
        data.put("extend", extend);
        data.put("ext", ext);
        data.put("sign", sign);
        data.put("tpl_id", tplId);
        data.put("params", msg);
        data.put("sig", util.strToHash(String.format(
                "appkey=%s&random=%d&time=%d&mobile=%s",
                "ac26360d5b557d1008db023ac735bd74", random, curTime, mobile)));
        //ac26360d5b557d1008db023ac735bd74
        data.put("tel", tel);
        data.put("time", curTime+"");
        System.out.println(data);
        System.out.println(urlString);
        URL object = new URL(urlString);
        HttpURLConnection conn;
        conn = (HttpURLConnection) object.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestMethod("POST");

        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
        wr.write(data.toString());
        wr.flush();

        System.out.println(data.toString());

        // 显示 POST 请求返回的内容
        StringBuilder sb = new StringBuilder();
        int httpRspCode = conn.getResponseCode();
        if (httpRspCode == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            System.out.println(sb);
        } else {
            System.out.println("error");
        }
        return JSONObject.fromObject(sb.toString());
    }
	public static void main(String[] args) throws Exception {
//        System.out.println(imSMSSend("86","15919161025", "3042"));
//        JSONArray array = new JSONArray();
//        JSONObject obj = new JSONObject();
//        obj.put("nationcode","86");
//        obj.put("mobile","15773159604");
//        array.add(obj);
//        JSONObject obj1 = new JSONObject();
//        obj1.put("nationcode","86");
//        obj1.put("mobile","15919161025");
//        array.add(obj1);
//        System.out.println(imSMSSend(array,"doub"));62089488
        System.out.println(imSMSSend("86","15811674190","您的验证码：1234"));
//        System.out.println(imSMSSend("853","62268558","（测试使用请忽略）您的验证码：1234"));
//        System.out.println(imSMSSend("853","63889043","（测试使用请忽略）您的验证码：1234"));
        
    }
}
