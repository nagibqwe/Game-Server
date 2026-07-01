/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.data.struct;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hewei
 */
public class ReadLongArray extends ReadArray<Long> {

    public ReadLongArray() {
    }

    public ReadLongArray(Long[] values) {
        super(values);
    }

    public ReadLongArray(String str, String spilt) {
        super(str, spilt);
    }

    @Override
    protected Long[] initValueFromStr(String str, String spilt) {
        List<Long> intList = new ArrayList<>();
        try {
            String[] sList = str.split(spilt);
            int index = 0;
            for (String s : sList) {
                try {
                    if (s.contains(".")) {
                        index = s.indexOf(".");
                        s = s.substring(0, index);
                        s = s + "L";
                    }
                    Long a = Long.parseLong(s);
                    intList.add(a);
                } catch (Exception e) {

                }
            }
        } catch (Exception e) {

        }
        Long[] intArray = new Long[intList.size()];
        return intList.toArray(intArray);
    }

}
