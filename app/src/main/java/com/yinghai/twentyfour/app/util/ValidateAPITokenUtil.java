package com.yinghai.twentyfour.app.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ValidateAPITokenUtil {
	public static String encryKey = "2TatUM_JrpvYj2ubrWKN!qCmqGsn_twentyfour";

	/**
	 * 判断api key是否合法
	 * 
	 * @param sendTimeString
	 * @param tokenString
	 * @return
	 */
	public static Boolean ValidatingApiToken(String sendTimeString, String tokenString) {
		String string = new ValidateAPITokenUtil().ctreatTokenStringByTimeString(sendTimeString);
		if (string.equals(tokenString)) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 
	 * @param timeString
	 * @return
	 */
	private String ctreatTokenStringByTimeString(String timeString) {
		String originString = timeString + encryKey;
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			byte[] inputByteArr = originString.getBytes();
			messageDigest.update(inputByteArr);
			byte[] resultByte = messageDigest.digest();
			String string = byteArrToHex(resultByte);
			System.out.println("================" + string + "================");
			return string;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		// return "";
	}

	private String byteArrToHex(byte[] byteArr) {

		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		char[] resultCharArray = new char[byteArr.length * 2];
		int index = 0;
		for (byte b : byteArr) {
			resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
			resultCharArray[index++] = hexDigits[b & 0xf];
		}

		return new String(resultCharArray);
	}

}
