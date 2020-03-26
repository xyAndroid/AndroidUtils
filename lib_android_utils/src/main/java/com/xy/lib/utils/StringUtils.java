package com.xy.lib.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	public static boolean isEmpty(String str){
		if (str==null || str.length()<=0) {
			return true;
		}
		return false;
	}
	
	public static boolean isNotEmpty(String str){
		return !isEmpty(str);
	}

	/**
	 * 利用正则表达式判断字符串是否是数字
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if( !isNum.matches() ){
			return false;
		}
		return true;
	}
}
