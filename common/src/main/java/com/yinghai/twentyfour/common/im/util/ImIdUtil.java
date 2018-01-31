package com.yinghai.twentyfour.common.im.util;


/**
 * 用户id和IM id的转换
 * @author Administrator
 *
 */
public class ImIdUtil {
	public static String getImId(Integer id,Integer type){
		String s=null;
		switch (type) {
		case 1:
			s = "user"+id;
			break;
		case 2:
			s = "master"+id;
			break;
		default:
			s = id.toString();
			break;
		}
		return s;
	}
	
	
}
