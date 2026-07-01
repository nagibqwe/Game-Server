package com.game.utils;

import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArrayEs;
import com.data.struct.ReadLongArrayEs;

/**
 * BI数据统计工具
 */
public class BIUtils {

    public static String makeBIRewardStr(ReadLongArrayEs values){
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < values.size(); ++i) {
            if(i > 0){
                str.append(",");
            }
            ReadArray<Long> ra = values.get(i);
            for (int j = 0; j < ra.size(); j++) {
                if (j > 0) {
                    str.append(":");
                }
                str.append(ra.get(j));
            }
        }
        return str.toString();
    }

    public static String makeBIRewardStr(ReadIntegerArrayEs values){
        if(values.isEmpty()){
            return "";
        }
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < values.size(); ++i) {
            if(i > 0){
                str.append(",");
            }
            ReadArray<Integer> ra = values.get(i);
            for (int j = 0; j < ra.size(); j++) {
                if (j > 0) {
                    str.append(":");
                }
                str.append(ra.get(j));
            }
        }
        return str.toString();
    }
}
