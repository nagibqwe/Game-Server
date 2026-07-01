/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.data.struct;

/**
 *
 * @author hewei
 */
public class ReadStringArray extends ReadArray<String> {

    public ReadStringArray() {
    }

    public ReadStringArray(String[] values) {
        super(values);
    }

    public ReadStringArray(String str, String spilt) {
        super(str, spilt);
    }

    @Override
    protected String[] initValueFromStr(String str, String spilt) {
        String[] sList = str.split(spilt);
        return sList;
    }

}
