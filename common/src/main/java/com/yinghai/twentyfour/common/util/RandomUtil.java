package com.yinghai.twentyfour.common.util;

import java.util.Random;


/**
 * 
 * 
 * @author TaxiGo02
 *
 */
public class RandomUtil {
	/**
	 * 获取随机码
	 * @return
	 */
	public static int getRandomInt() {
		Random random = new Random();
		return random.nextInt(999999)%900000+100000;
	}
	public static int getRandomInt(Integer weishu) {
		Random random = new Random();
		return random.nextInt(999999)%900000+100000;
	}
	public static String random(){
		int i = (int)(Math.random()*32)+1;
		StringBuilder sb = new StringBuilder();
		for (int j = 0; j < i; j++) {
			int temp = (int)(Math.random()*8)+1;
			sb.append(temp);
		}
		return sb.toString();
	}
	
	/**
	 * 获取4位随机码
	 * 
	 * @return
	 */
	public static String getrandomString(int length,EnumUtil type) {
		String code="";
	//	RandomUtil randomUtil = new RandomUtil();
		switch (type) {
		case numberOnly:
		{
			for (code.length(); code.length() < length;) {
				Integer temp = new Random().nextInt(10);
				code += temp.toString();
			}
			break;}
case lowerStringOnly:{
	String chars = "abcdefghijklmnopqrstuvwxyz";
	for ( code.length() ; code.length() < length;) {
		code+= chars.charAt((int)(Math.random() * 26));
	}
	break;}
case capitalStringOnly:{
	String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	for ( code.length() ; code.length() < length;) {
		code+= chars.charAt((int)(Math.random() * 26));
	}
	break;}
case both:{
	String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	for ( code.length() ; code.length() < length;) {
		code+= chars.charAt((int)(Math.random() * 62));
	}
	break;}
		default:
			break;
		}
		
		return code;
	}
	public static void main(String[] args) {
		System.out.println(getRandomInt());
	}
}
