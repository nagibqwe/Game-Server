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
public class ReadFloatArrayEs extends ReadArrayEs<Float> {
    
    public ReadFloatArrayEs(ReadArray<Float>[] valuees) {
        super(valuees);
    }

    public ReadFloatArrayEs(String str, String bigSpilt, String smallSpilt) {
        super(str, bigSpilt, smallSpilt);
    }

    @Override
    protected ReadArray<Float> makeReadArray(String str, String smallSpilt) {
        return new ReadFloatArray(str, smallSpilt);
    }

}
