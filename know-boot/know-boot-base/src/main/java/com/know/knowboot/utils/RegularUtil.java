package com.know.knowboot.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularUtil {
	
	public static String matcher(String owntext,String regular){
		  String result = "";
		  if(!StringUtils.isEmpty(owntext)){
			  Pattern p = Pattern.compile(regular);  
			  Matcher m = p.matcher(owntext);      
			  if(m.find()){
			     result = m.group();   
			  }
		  }
		  return result;
	}

}
