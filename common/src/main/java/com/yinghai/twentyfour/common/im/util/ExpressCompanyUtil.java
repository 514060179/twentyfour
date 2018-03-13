package com.yinghai.twentyfour.common.im.util;

import java.util.HashMap;
import java.util.Map;

public class ExpressCompanyUtil {
	public static Map<String,String> map;
	static{
		map = new HashMap<String,String>();
		map.put("shunfeng", "顺丰速运");
		map.put("ems", "邮政特快专递EMS");
		map.put("youzhengguonei", "中国邮政速递");
		map.put("huitongkuaidi", "百世快递");
		map.put("shentong", "申通快递");
		map.put("zhongtong", "中通快递");
		map.put("yuantong", "圆通快递");
		map.put("guotongkuaidi", "国通快递");
		map.put("yunda", "韵达快递");
		map.put("tiantian", "天天快递");
		map.put("youshuwuliu", "优速快递");
		map.put("kuaijiesudi", "快捷快递");
		map.put("quanfengkuaidi", "全峰快递");
		map.put("jingdong", "京东物流");
		map.put("zhaijisong", "宅急送");
		map.put("fedex", "FedEX");
		map.put("dhl", "DHL");
		map.put("ups", "UPS");
		map.put("tnt", "TNT");
	}
	public static String getCompany(String code){
		return map.get(code);
	}
}
