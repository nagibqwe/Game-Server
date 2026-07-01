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
public class ReadStringArrayEs extends ReadArrayEs<String> {
    
    public ReadStringArrayEs(ReadArray<String>[] valuees) {
        super(valuees);
    }

    public ReadStringArrayEs(String str, String bigSpilt, String smallSpilt) {
        super(str, bigSpilt, smallSpilt);
    }

    @Override
    protected ReadArray<String> makeReadArray(String str, String smallSpilt) {
        return new ReadStringArray(str, smallSpilt);
    }

}
