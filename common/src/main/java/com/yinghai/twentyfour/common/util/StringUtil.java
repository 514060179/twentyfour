package com.yinghai.twentyfour.common.util;

import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * 字符串工具类
 * 
 */
public final class StringUtil {
	private StringUtil() {}

	/**
	 * 该方法从commons-2.0's ObjectUtils中取出
	 * <p>
	 * Appends the toString that would be produced by <code>Object</code> if a class did not override toString itself. <code>null</code> will return <code>null</code>.
	 * </p>
	 * 
	 * <pre>
	 * ObjectUtils.appendIdentityToString(*, null)            = null
	 * ObjectUtils.appendIdentityToString(null, &quot;&quot;)           = &quot;java.lang.String@1e23&quot;
	 * ObjectUtils.appendIdentityToString(null, Boolean.TRUE) = &quot;java.lang.Boolean@7fa&quot;
	 * ObjectUtils.appendIdentityToString(buf, Boolean.TRUE)  = buf.append(&quot;java.lang.Boolean@7fa&quot;)
	 * </pre>
	 * 
	 * @param buffer
	 *            the buffer to append to, may be <code>null</code>
	 * @param object
	 *            the object to create a toString for, may be <code>null</code>
	 * @return the default toString text, or <code>null</code> if <code>null</code> passed in
	 * @since 2.0
	 */

	public static StringBuffer appendIdentityToString(StringBuffer buffer, Object object) {
		if (object == null) {
			return null;
		}
		if (buffer == null) {
			buffer = new StringBuffer();
		}
		return buffer.append(object.getClass().getName()).append('@')
				.append(Integer.toHexString(System.identityHashCode(object)));
	}

	/**
	 * 判断一个字符串是否为空或者是右空白符号组成
	 * 
	 * @param source
	 *            String
	 * @return boolean
	 */
	public static boolean empty(String source) {
		return (source == null || source.trim().length() == 0 || source.toLowerCase().trim().equals("null")) ? true
				: false;
	}

	/**
	 * 判断一个字符串是否不为空或者不是右空白符号组成
	 * 
	 * @param source
	 *            String
	 * @return boolean
	 */
	public static boolean notEmpty(String source) {
		return (source != null && source.trim().length() > 0 && !source.toLowerCase().equals("null")) ? true : false;
	}

	public static String[] toArray(String parseString) {
		return toArray(parseString, " \t\n\r\f", false);
	}

	public static String[] toArray(String parseString, String splitString) {
		return toArray(parseString, splitString, false);
	}

	/**
	 * 分隔一个字符串
	 * 
	 * @param parseString
	 *            String 原始字符串
	 * @param splitString
	 *            String 分隔串
	 * @param returnDelims
	 *            boolean 返回值是否包含分隔串
	 * @return String[] 分隔后的字符串
	 */
	public static String[] toArray(String parseString, String splitString, boolean returnDelims) {
		StringTokenizer tokens = new StringTokenizer(parseString, splitString, returnDelims);
		String[] values = new String[tokens.countTokens()];
		int i = 0;
		while (tokens.hasMoreTokens()) {
			values[i++] = tokens.nextToken();
		}
		return values;
	}

	public static char toUpperCase(char ch) {
		if (ch >= 'a' && ch <= 'z') {
			ch -= 32;
		}
		return ch;
	}

	/**
	 * 将给定字符串指定位置上的字母大写
	 * 
	 * @param source
	 *            String
	 * @param pos
	 *            int
	 * @return String
	 */
	public static String upperCharAt(final String source, int pos) {
		if (source == null) {
			return null;
		}
		if (pos < 0 || pos >= source.length()) {
			return "";
		}
		char[] chars = source.toCharArray();
		chars[pos] = toUpperCase(chars[pos]);
		return new String(chars);
	}

	/**
	 * 得到字符串的位长度，中文＝2,英文＝1
	 * 
	 * @param sourceString
	 *            待测的字符串
	 * @return int 字节长度
	 */
	public static int lengthInBit(String sourceString) {
		int index = 0;
		char[] sourceChrs = sourceString.toCharArray();
		int sourceLength = sourceChrs.length;
		for (int i = 0; i < sourceLength; i++) {
			if (sourceChrs[i] <= 202 && sourceChrs[i] >= 8) {
				index++;
			} else {
				index += 2;
			}
		}
		sourceChrs = null;
		return index;
	}

	/**
	 * 截取字符，主要用于显示区域长度固定的字符串的显示（中文＝2,英文＝1）
	 * 
	 * @param sourceString
	 *            待处理的字符串
	 * @param viewLength
	 *            截取的长度
	 * @return 返回截取后的字符串
	 */
	public static String subInBit(final String sourceString, int viewLength) {
		char[] sourceChrs = sourceString.toCharArray();
		int sLen = sourceChrs.length;
		int i = 0;
		for (; i < viewLength; i++) {
			if (i >= sLen) {
				return sourceString;
			}
			if (sourceChrs[i] < 8 || sourceChrs[i] > 202) { // 非英文字符
				viewLength--;
			}
		}
		sourceChrs = null;
		return sourceString.substring(0, i);
	}

	/**
	 * 截取字符串,若该字符串被截断，则添加指定字符串（append）在末尾
	 * 
	 * @param sourceString
	 *            待处理的字符串
	 * @param viewLength
	 *            截取的长度,是字节长度,一个中文两个字节
	 * @param append
	 *            需要添加的字符串
	 * @return 返回截取后的字符串
	 */
	public static String subAppend(final String sourceString, int viewLength, final String append) {
		char[] sourceChrs = sourceString.toCharArray();
		int sLen = sourceChrs.length;
		int i = 0;
		for (; i < viewLength; i++) {
			if (i >= sLen) {
				return sourceString;
			}
			if (sourceChrs[i] < 8 || sourceChrs[i] > 202) { // 非英文字符
				viewLength--;
			}
		}
		sourceChrs = null;
		return sourceString.substring(0, i) + append;
	}

	public static String wrapToXNL(String content) {
		return "<![CDATA[" + content + "]]>";
	}

	public static String getSetter(String field) {
		return "set" + upperCharAt(field, 0);
	}

	public static String getGetter(String field) {
		return "get" + upperCharAt(field, 0);
	}

	public static boolean isNumber(Object value) {
		if (value == null) {
			return false;
		}
		String sValue = value.toString();
		try {
			new java.math.BigDecimal(sValue);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public static double getDouble(Object o) {
		if (o == null) {
			return 0.0;
		}
		try {
			return Double.parseDouble(o.toString().trim());
		} catch (Exception ex) {
			return 0.0;
		}
	}

	public static int getInt(Object o) {
		if (o == null) {
			return 0;
		}
		try {
			return Integer.parseInt(o.toString().trim());
		} catch (Exception ex) {
			return 0;
		}
	}

	/**
	 * 根据给定对象（使用Object.toString()方法）返回一个非空且不包含左右空格的字符串
	 * 
	 * @param param
	 *            Object 待处理的对象
	 * @return String 处理后的字符串
	 */
	public static String getNotNullAndTrim(Object param) {
		if (param == null) {
			return "";
		}
		String rtn = param.toString();
		return rtn == null ? "" : rtn.trim();
	}

	/**
	 * 填充字符串
	 * 
	 * @param src
	 * @param length
	 * @param ch
	 * @return
	 */
	public static String fillString(String src, int length, char ch) {
		if (src == null) src = "";
		while (src.length() < length) {
			src = ch + src;
		}
		return src;
	}

	/**
	 * 
	 * <description> 方法描述：转换字符编码
	 * 
	 * </description>
	 * 
	 * @author luwc
	 * @创建日期：Feb 2, 2009 5:23:58 PM
	 * @param src
	 * @return
	 */
	public static String transferToGBK(String src) {
		if (src == null)
			return null;
		else if (empty(src)) return "";
		try {
			return new String(src.getBytes("ISO-8859-1"), "GBK");
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 全角空格为12288，半角空格为32 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
	 * 
	 * 将字符串中的全角字符转为半角
	 * 
	 * @author wangjc
	 * @date 2008-8-20 下午03:20:18
	 * @param src
	 *            要转换的包含全角的任意字符串
	 * @return 转换之后的字符串
	 */
	public static String getSemiangle(String src) {
		if (empty(src)) return null;
		char[] c = src.trim().toCharArray();
		for (int index = 0; index < c.length; index++) {
			if (c[index] == 12288) {// 全角空格
				c[index] = (char) 32;
			} else if (c[index] > 65280 && c[index] < 65375) {// 其他全角字符
				c[index] = (char) (c[index] - 65248);
			}
		}
		return String.valueOf(c);
	}

	/**
	 * 
	 * <description>获得去除特殊字符和标点符号后的字符串</description>
	 * 
	 * @author wangjc
	 * @date 2008-8-20 下午03:20:18
	 * @param src
	 * @return
	 */
	public static String getRegexString(String src) {
		if (empty(src)) return null;
		src = getSemiangle(src.trim());
		Pattern pattern = Pattern.compile("[\\.\\,\\|\\\"\\?\\!:'\\-_\\@\\#\\$\\%\\^\\&\\*\\(\\)\\{\\}\\[\\]]");// 增加对应的标点
		Matcher matcher = pattern.matcher(src);
		String first = matcher.replaceAll("");
		pattern = Pattern.compile(" {2,}");// 去除多余空格
		matcher = pattern.matcher(first);
		String second = matcher.replaceAll(" ");
		return second;
	}

	/**
	 * 第一个字符小写
	 * 
	 * @param str
	 * @return
	 */
	public static String toLowerCaseStart(String str) {
		if (StringUtils.isBlank(str)) return str;
		String substring = str.substring(0, 1);
		String lowerCase = substring.toLowerCase();
		return lowerCase + str.substring(1);
	}

	/**
	 * 第一个字符大写
	 * 
	 * @param str
	 * @return
	 */
	public static String toUpperCaseStart(String str) {
		if (StringUtils.isBlank(str)) return str;
		String substring = str.substring(0, 1);
		String upperCase = substring.toUpperCase();
		return upperCase + str.substring(1);
	}

	/**
	 * GBK转化为 UTF-8
	 * 
	 * @param chenese
	 * @return
	 */
	public static String gbk2Utf8(String chenese) {
		if (StringUtils.isBlank(chenese)) return chenese;
		char c[] = chenese.toCharArray();
		byte[] fullByte = new byte[3 * c.length];
		for (int i = 0; i < c.length; i++) {
			int m = (int) c[i];
			String word = Integer.toBinaryString(m);
			StringBuffer sb = new StringBuffer();
			int len = 16 - word.length();
			for (int j = 0; j < len; j++) {
				sb.append("0");
			}
			sb.append(word);
			sb.insert(0, "1110");
			sb.insert(8, "10");
			sb.insert(16, "10");
			String s1 = sb.substring(0, 8);
			String s2 = sb.substring(8, 16);
			String s3 = sb.substring(16);
			byte b0 = Integer.valueOf(s1, 2).byteValue();
			byte b1 = Integer.valueOf(s2, 2).byteValue();
			byte b2 = Integer.valueOf(s3, 2).byteValue();
			byte[] bf = new byte[3];
			bf[0] = b0;
			fullByte[i * 3] = bf[0];
			bf[1] = b1;
			fullByte[i * 3 + 1] = bf[1];
			bf[2] = b2;
			fullByte[i * 3 + 2] = bf[2];
		}
		try {
			return new String(fullByte, "UTF-8");
		} catch (UnsupportedEncodingException e) {}
		return chenese;
	}
	// 判断一个字符是否是中文  
    public static boolean isChinese(char c) {  
        return c >= 0x4E00 &&  c <= 0x9FA5;// 根据字节码判断  
    } 
	 // 判断一个字符串是否含有中文  
    public static boolean isChinese(String str) {  
        if (str == null) return false;  
        for (char c : str.toCharArray()) {  
            if (isChinese(c)) return true;// 有一个中文字符就返回  
        }  
        return false;  
    }  
    /**
     * 验证邮箱
     *
     * @param email
     * @return
     */

    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
}
