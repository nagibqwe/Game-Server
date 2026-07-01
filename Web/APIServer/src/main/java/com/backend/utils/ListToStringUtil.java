package com.backend.utils;

import java.util.List;

public class ListToStringUtil {
	public static String listToString(List<Object> stringList){
        if (stringList==null) {
            return null;
        }
        StringBuilder result=new StringBuilder();
        boolean flag=false;
        for (Object string : stringList) {
            if (flag) {
                result.append(",");
            }else {
                flag=true;
            }
            result.append(string.toString());
        }
        return result.toString();
    }
}
