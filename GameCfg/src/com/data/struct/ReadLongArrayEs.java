/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.data.struct;

import java.util.Collection;

/**
 *
 * @author hewei
 */
public class ReadLongArrayEs extends ReadArrayEs<Long> {
    
    public ReadLongArrayEs(ReadArray<Long>[] valuees) {
        super(valuees);
    }

    public ReadLongArrayEs(Collection<ReadArray<Long>> cc) {
        super(cc);
    }

    public ReadLongArrayEs(String str, String bigSpilt, String smallSpilt) {
        super(str, bigSpilt, smallSpilt);
    }

    @Override
    protected ReadArray<Long> makeReadArray(String str, String smallSpilt) {
        return new ReadLongArray(str, smallSpilt);
    }

}
