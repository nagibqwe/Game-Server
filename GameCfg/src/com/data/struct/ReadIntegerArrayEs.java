
package com.data.struct;

import java.util.Collection;

/**
 *
 * @author hewei
 */
public class ReadIntegerArrayEs extends ReadArrayEs<Integer> {

    public ReadIntegerArrayEs(ReadArray<Integer>[] valuees) {
        super(valuees);
    }

    public ReadIntegerArrayEs(Collection<ReadArray<Integer>> cc) {
        super(cc);
    }

    public ReadIntegerArrayEs(String str, String bigSpilt, String smallSpilt) {
        super(str, bigSpilt, smallSpilt);
    }

    @Override
    protected ReadArray<Integer> makeReadArray(String str, String smallSpilt) {
        return new ReadIntegerArray(str, smallSpilt);
    }

}
