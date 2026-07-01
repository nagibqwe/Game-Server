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
public class ReadFloatArray extends ReadArray<Float> {

    public ReadFloatArray() {
    }

    public ReadFloatArray(Float[] values) {
        super(values);
    }

    public ReadFloatArray(String str, String spilt) {
        super(str, spilt);
    }

    @Override
    protected Float[] initValueFromStr(String str, String spilt) {
        List<Float> intList = new ArrayList<>();
        try {
            String[] sList = str.split(spilt);
            for (String s : sList) {
                try {
                    Float a = Float.parseFloat(s);
                    intList.add(a);
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {

        }
        Float[] intArray = new Float[intList.size()];
        return intList.toArray(intArray);
    }

}
