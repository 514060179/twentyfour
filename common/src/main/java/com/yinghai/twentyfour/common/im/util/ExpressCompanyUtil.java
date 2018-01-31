package com.yinghai.twentyfour.common.im.util;

import java.util.HashMap;
import java.util.Map;

public class ExpressCompanyUtil {
	public static Map<Integer,String> map;
	static{
		map = new HashMap<Integer,String>();
		map.put(1, "顺丰速运");
		map.put(2, "邮政特快专递EMS");
		map.put(3, "中国邮政速递");
		map.put(4, "百世快递");
		map.put(5, "申通快递");
		map.put(6, "中通快递");
		map.put(7, "圆通快递");
		map.put(8, "国通快递");
		map.put(9, "韵达快递");
		map.put(10, "天天快递");
		map.put(11, "优速快递");
		map.put(12, "快捷快递");
		map.put(13, "全峰快递");
		map.put(14, "京东物流");
		map.put(15, "宅急送");
		map.put(31, "FedEX");
		map.put(32, "DHL");
		map.put(33, "UPS");
		map.put(34, "TNT");
	}
	public static String getCompany(Integer id){
		return map.get(id);
	}
}
