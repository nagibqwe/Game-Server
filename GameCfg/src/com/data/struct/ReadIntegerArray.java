/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.data.struct;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hewei
 */
public class ReadIntegerArray extends ReadArray<Integer> {

    public ReadIntegerArray() {
    }

    public ReadIntegerArray(Integer[] values) {
        super(values);
    }

    public ReadIntegerArray(String str, String spilt) {
        super(str, spilt);
    }

    @Override
    protected Integer[] initValueFromStr(String str, String spilt) {
        List<Integer> intList = new ArrayList<>();
        int index =0;
        try {
            String[] sList = str.split(spilt);
            for (String s : sList) {
                try {
                    if (s.length()>=8)
                    {
                        s = new BigDecimal(s).toPlainString();
                    }
                    if (s.contains(".")) {
                        index = s.indexOf(".");
                        s = s.substring(0,index);
                    }
                    Integer a = Integer.parseInt(s);
                    intList.add(a);
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {

        }
        Integer[] intArray = new Integer[intList.size()];
        return intList.toArray(intArray);
    }

}
