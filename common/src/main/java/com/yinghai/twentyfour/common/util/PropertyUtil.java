package com.yinghai.twentyfour.common.util;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * @author simon.feng
 * 2016-11-23
 */
public class PropertyUtil {
	
	/**
	 * 获取属性文件/com/config/website.properties  的配置
	 * @param key 
	 * @return
	 */
	public static String getProperty(String key) {
		InputStream in = PropertyUtil.class.getResourceAsStream("/config/website.properties");
		Properties p = new Properties();
		try {
			p.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return p.getProperty(key);
	}
	/**
	 * 根据属性文件路径查找键值
	 * @return
	 */
	public static String getPropertyByPath(String key,String path) {
		InputStream in = PropertyUtil.class.getResourceAsStream(path);
		Properties p = new Properties();
		try {
			p.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return p.getProperty(key);
	}
	public static void main(String[] args) {
		System.out.println(getAppProperty("sdkAppId"));
	}
	/**
	 * 獲取app.properties key對應的vaule值
	 * @param key
	 * @return
	 */
	public static String getAppProperty(String key){
		return getPropertes().getProperty(key);
	}
	/**
	 * 获取配置文件中部署环境信息，true为生产环境，false为测试环境
	 * 
	 * @return
	 */
	public static boolean isProductDeploy() {
		return (new Boolean(getProperty("isProductDeploy"))).booleanValue();
	}

	/**
	 * 获取对应文件夹下的配置文件
	 * 
	 * @return
	 */
	public static Properties getPropertes() {

		String path = "/config"+getConfigDir() + "/app.properties";
		InputStream in = PropertyUtil.class.getResourceAsStream(path);

		Properties p = new Properties();
		try {
			p.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return p;
	}

	public static String getConfigDir() {
		String dir = "/test";
		if (isProductDeploy()) {
			dir = "/product";
		}
		return dir;
	}
}
