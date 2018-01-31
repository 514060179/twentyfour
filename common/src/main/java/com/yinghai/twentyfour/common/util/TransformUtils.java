package com.yinghai.twentyfour.common.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.yinghai.twentyfour.common.constant.ChineseUtill;
import com.yinghai.twentyfour.common.constant.LogManager;


/**
 * 
 * <strong>discrition:</strong>类型转换
 *
 */
@SuppressWarnings({"rawtypes" , "unchecked"})
public class TransformUtils {

	public static boolean toBoolean(Object obj) {

		return toBoolean(obj, false);
	}

	public static boolean toBoolean(Object obj, boolean defaultValue) {
		if (obj == null) {
			return defaultValue;
		}
		try {
			return Boolean.parseBoolean(toString(obj));
		} catch (Exception e) {
		}
		return defaultValue ;
	}

	public static byte toByte(Object obj) {
		return toByte(obj, (byte) 0);
	}

	public static byte toByte(Object obj, byte defaultValue) {
		if (obj == null) {
			return defaultValue;
		}

		if (obj instanceof Number) {
			Number number = (Number) obj;
			return number.byteValue();
		}
		String value = toString(obj) ;
		try {
			return Byte.parseByte( value ) ;
		} catch (Exception e) {
		}
		return defaultValue ;
	}

	public static char toCharacter(Object obj) {
		return toCharacter(obj, (char) ' ');
	}

	public static char toCharacter(Object obj, char defaultValue) {
		if (obj == null) {
			return defaultValue;
		}
		String str = obj.toString().trim();
		if (str.length() == 0) {
			return defaultValue;
		}
		return (char) str.toCharArray()[0];
	}

	public static double toDouble(Object obj) {
		return toDouble(obj, 0d);
	}

	public static double toDouble(Object obj, double defaultValue) {
		if (obj == null) {
			return defaultValue;
		}

		if (obj instanceof Number) {
			Number number = (Number) obj;
			return number.doubleValue() ;
		}
		String value = toString(obj) ;
		try {
			return Double.parseDouble(value) ;
		} catch (Exception e) {
		}
		return defaultValue ;
	}

	public static float toFloat(Object obj) {
		return toFloat(obj, 0);
	}

	public static float toFloat(Object obj, float defaultValue) {
		if (obj == null) {
			return defaultValue;
		}

		if (obj instanceof Number) {
			Number number = (Number) obj;
			return number.floatValue();
		}
		String value = toString(obj) ;
		try {
			return Float.parseFloat(value) ;
		} catch (Exception e) {
		}
		return defaultValue ;
	}

	public static int toInt(Object obj) {
		return toInt(obj, 0);
	}

	public static int toInt(Object obj, int defaultValue) {
		if (obj == null) {
			return defaultValue;
		}

		if (obj instanceof Number) {
			Number number = (Number) obj;
			return number.intValue();
		}
		String value = toString(obj) ;
		try {
			return Integer.parseInt(value) ;
		} catch (Exception e) {
		}
		return defaultValue ;
	}
	
	public static Integer toInteger(String s){
		return toInteger(s,null);
	}
	
	public static Integer toInteger(String s,Integer defaultValue){
		if(StringUtil.empty(s)){
			return defaultValue;
		}
		try {
			return Integer.parseInt(s) ;
		} catch (Exception e) {
			return -2;
		}
	}
	
	public static long toLong(Object obj) {
		return toLong(obj, 0L);
	}

	public static long toLong(Object obj, long defaultValue) {
		if (obj == null) {
			return defaultValue;
		}

		if (obj instanceof Number) {
			Number number = (Number) obj;
			return number.longValue();
		}
		String value = toString(obj) ;
		try {
			return Long.parseLong(value) ;
		} catch (Exception e) {
		}
		return defaultValue ;
	}

	public static short toShort(Object obj) {
		return toShort(obj, (byte) 0);
	}
	
	
	public static short toShort(Object obj, short defaultValue) {
		if (obj == null) {
			return defaultValue;
		}

		if (obj instanceof Number) {
			Number number = (Number) obj;
			return number.shortValue();
		}
		String value = toString(obj) ;
		try {
			return Short.parseShort(value) ;
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static String toString(Object value) {
		if (value == null) {
			return "";
		}
		if(value instanceof BigDecimal){
			BigDecimal bigDecimal = (BigDecimal) value ;
			DecimalFormat df = new DecimalFormat("##########################.##############");
			return df.format(bigDecimal) ;
		}
		
		if(value instanceof Number){
			Number number = (Number) value ;
			return String.valueOf( number ) ;
		}
		return value.toString().trim();
	}
	
	
	public static BigDecimal toBigDecimal(Object value){
		return toBigDecimal(value , new BigDecimal(0)) ;
	}
	
	public static BigDecimal toBigDecimal(Object value, BigDecimal defaultValue) {
		if(value == null){
			return defaultValue ;
		}
		if(value instanceof BigDecimal){
			BigDecimal decimal = (BigDecimal) value ;
			return decimal; 
		}
		return new BigDecimal( toDouble( value ) );
	}

	public static String dateToString(Object value, String pattern){
		java.util.Date date = (java.util.Date) value;  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime( date ) ; 
        SimpleDateFormat sdf = new SimpleDateFormat( pattern );  
        return sdf.format( date ) ; 
	}
	
	public static Object transform(Object obj,Class<?> clazz){
		if(clazz == null){
			return obj ;  
		}
		
		if(clazz.isEnum()){
			Field[]fields=clazz.getFields() ;
			int tempInt = toInt(obj) ;
			if(fields.length > tempInt){
				try {
					return Enum.valueOf((Class)clazz , fields[tempInt].getName());
				} catch (Exception e) {
				}
			}
		}
		
		if(obj.getClass().equals(clazz)){
			return obj ; 
		}
		if(int.class.equals(clazz) || Integer.class.equals(clazz)){
			return toInt(obj) ; 
		}else if( String.class.equals(clazz) ){
			return toString( obj ) ; 
		}else if(float.class.equals(clazz) || Float.class.equals(clazz)){
			return toFloat(obj) ; 
		}else if(double.class.equals(clazz) || Double.class.equals(clazz)){
			return toDouble(obj) ; 
		}else if(byte.class.equals(clazz) || Byte.class.equals(clazz)){
			return toByte(obj) ; 
		}else if(char.class.equals(clazz) || Character.class.equals(clazz)){
			return toCharacter(obj) ; 
		}else if(short.class.equals(clazz) || Short.class.equals(clazz)){
			return toShort(obj) ; 
		}else if(long.class.equals(clazz) || Long.class.equals(clazz)){
			return toLong(obj) ; 
		}else if(boolean.class.equals(clazz) || Boolean.class.equals(clazz)){
			return toBoolean(obj) ; 
		}else if(BigDecimal.class.equals(clazz)){
			return toBigDecimal( obj ) ; 
		}else if(java.util.Date.class.equals(clazz) || 
				java.sql.Date.class.equals(clazz) 
				|| java.sql.Timestamp.class.equals(clazz)){
			try {
				java.util.Date date = DateUtils.parseDate(obj.toString() 
						, "yyyyMMdd" , "yyyy-MM-dd" , 
						"yyyy年MM月dd日" , 
						 "yyyyMMddHHmmss" , 
						 "yyyy-MM-dd HH:mm:ss" , 
						 "yyyy年MM月dd日HH时mm分ss秒" ) ;  
				if(java.sql.Date.class.equals(clazz)){
					return new java.sql.Date(date.getTime()) ;  
				}else if(java.sql.Timestamp.class.equals(clazz)){
					return new java.sql.Timestamp(date.getTime()) ;
				}
				return date ; 
			} catch (Exception e) {
				return null;
			}
		}
		return obj ; 
	}
	
	public static String objToString(Object value) {
		if (null == value) {
			return "";
		}
		if (value instanceof BigDecimal || value instanceof Double
				|| value instanceof Float) {
			BigDecimal decimal = toBigDecimal(value);
			DecimalFormat df = new DecimalFormat("0.00");
			return df.format(decimal);
		} else if (value instanceof Date) {
			Date date = (Date) value;
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			String pattern = "yyyy-MM-dd HH:mm:ss";
			if (calendar.get(Calendar.HOUR_OF_DAY) == 0
					&& calendar.get(Calendar.MINUTE) == 0
					&& calendar.get(Calendar.SECOND) == 0) {
				pattern = "yyyy-MM-dd";
			}
			return DateFormatUtils.format(calendar, pattern);
		}

		return value.toString();
	}
	
	public static boolean isNull(Object value){
		if(value == null){
			return true ; 
		}
		if(value.getClass().isArray()){
			if(Array.getLength(value) == 0){
				return true ; 
			}
		}
		if(value instanceof Collection<?>){
			Collection<?> collection = (Collection<?>) value ;
			if(collection.isEmpty()){
				return true ;
			}
		}else if(value instanceof Map<?, ?>){
			Map<?, ?> map = (Map<?, ?>) value ;
			if(map.isEmpty()){
				return true ;
			}
		}else if(value instanceof String){
			String string = (String) value ;
			return isNull(string) ;
		}
		return false; 
	}
	
	public static boolean isNull(String value){
		if(value == null){
			return true ;
		}
		if("".equals(value.trim())){
			return true ;
		}
		return false ;
	}
	
	public static String getCurrentUrl(HttpServletRequest request){
		return getCurrentUrl(request , "page" ) ;
	}
	
	public static boolean exists(String[]array , String item){
		if(TransformUtils.isNull(array)){
			return false ;
		}
		item = TransformUtils.toString( item ) ;
		for(String value : array){
			if(item.equals(value)){
				return true ; 
			}
		}
		return false ;
	}
	
	public static String getCurrentUrl(HttpServletRequest request,String...noNames){
		
		String currentPage = getBasepath(request)  ; 
		String servletPath = request.getServletPath() ;
		currentPage = currentPage + (servletPath.startsWith("/") ? servletPath.substring(1) : servletPath);
		Enumeration<?> enumeration = request.getParameterNames();
		if(null != enumeration){
			while(enumeration.hasMoreElements()){
				String name = TransformUtils.toString(enumeration.nextElement());
				if(!exists(noNames, name)){
					String[] values = request.getParameterValues( name );
					if(!TransformUtils.isNull(values)){
						for(String value : values){
							if(currentPage.indexOf("?") == -1){
								currentPage = currentPage + "?" ;
							}else{
								currentPage = currentPage + "&" ;
							}
							currentPage = currentPage + name + "=" + ChineseUtill.toChinese(value) ;  
						}
						
					}
				}
			}
		}
		return currentPage ;
	}
	
	public static String getBasepath(HttpServletRequest request)
	  {
	    String path = request.getContextPath();
	    String basePath = request.getScheme() + 
	      "://" + 
	      request.getServerName() + (
	      request.getServerPort() == 80 ? "" : new StringBuilder(":").append(
	      request.getServerPort()).toString()) + 
	      path + "/";

	    return basePath;
	  }
	
	public static List<Map<String,Object>> toMaps(List<?> data){
		List<Map<String,Object>> results = new Vector<Map<String,Object>>();
		if(isNull(data)){
			return results ;
		}
		for(Object item : data){
			Map<String,Object> map = toMap(item);
			results.add( map ) ;
		}
		
		return results ;
	}
	
	public static Map<String,Object> toMap(Object item){
		Map<String,Object> map = new HashMap<String, Object>();
		try{
			BeanInfo beanInfo = Introspector.getBeanInfo(item.getClass());
			PropertyDescriptor[] pds=beanInfo.getPropertyDescriptors();
			for(PropertyDescriptor pd : pds){
				if(null != pd.getReadMethod() && null != pd.getWriteMethod()){
					Object obj = pd.getReadMethod().invoke(item) ; 
					map.put(pd.getDisplayName() , obj ) ;  
				}
			}
		}catch(Exception e){
			LogManager.error("toMap make mistake ", e);
		}
		return map;
	}
	
	public static void writeText(Object ob , HttpServletResponse response){
		try {
			String jsonStr = JSONObject.fromObject( ob ).toString();
			response.setContentType("text/html");
			response.getWriter().write(jsonStr);
		} catch (Exception e) {
			LogManager.error( "writeText" , e );
		}
	} 
	
	public static String randStr(int len){
		StringBuffer buffer =  new StringBuffer();
		for(int x=65;x<=90;x++){
			buffer.append( (char)x );
		}
		int strLenth = buffer.length() ;
		StringBuffer randStr = new StringBuffer();
		for(int x=0;x<len;x++){
			randStr.append( buffer.charAt(new Random().nextInt( strLenth ))) ;
		}
		return randStr.toString() ;
	}
	
	public static String replaceToBr(String str){
		if(str==null){
			return "";
		}
		//str = str.replaceAll("\n", "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");	407bug
		str = str.replaceAll("\n", "<br/>");
		//str = str.replaceAll("\t", "<br/>");
        //str = str.replaceAll("\r", "<br/>");
        return str;
	}
}
