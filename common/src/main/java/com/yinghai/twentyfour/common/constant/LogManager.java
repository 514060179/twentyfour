package com.yinghai.twentyfour.common.constant;

import java.io.Serializable;

import com.yinghai.twentyfour.common.util.TransformUtils;
import org.apache.log4j.Logger;

/**
 * 
 * <strong>discrition:</strong>日志书写类

 */
public class LogManager implements Serializable {

	private static final long serialVersionUID = 188856960929607114L;

	static Logger log = Logger.getLogger(LogManager.class);

	public static void info(Object obj) {
		log.info(TransformUtils.objToString(obj));
	}
	
	public static void error(String msg) {
		log.error(msg);
	}	
	
	
	public static void warn(String msg) {
		log.warn(msg);
	}


	public static void error(String msg, Throwable t) {
		log.error(msg, t);
	}
}
