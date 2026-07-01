/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.data.struct;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author hewei
 */
public abstract class ReadArrayEs<T> {

    private final ReadArray<T>[] valuees;

    public ReadArrayEs(ReadArray<T>[] valuees) {
        this.valuees = valuees;
    }

    public ReadArrayEs(Collection<ReadArray<T>> cc) {
        this.valuees = new ReadArray[cc.size()];
        cc.toArray(this.valuees);
    }

    public ReadArrayEs(String str, String bigSpilt, String smallSpilt) {
        List<ReadArray<T>> intList = new ArrayList<>();
        try {
            String[] sList = str.split(bigSpilt);
            for (String s : sList) {
                try {
                    ReadArray<T> a = makeReadArray(s, smallSpilt);
                    if (a.size() == 0) {
                        continue;
                    }
                    intList.add(a);
                } catch (Exception e) {

                }
            }
        } catch (Exception e) {

        }
        valuees = new ReadArray[intList.size()];
        intList.toArray(valuees);
    }

    public final int size() {
        return valuees.length;
    }
    
    public final boolean isEmpty(){
        return valuees.length == 0;
    }

    public final ReadArray<T> get(int index) {
        if (index < 0) {
            return null;
        }
        if (index >= valuees.length) {
            return null;
        }
        return valuees[index];
    }

    protected abstract ReadArray<T> makeReadArray(String str, String smallSpilt);

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("[");
        for (int i = 0; i < valuees.length; ++i) {
            if (i == 0) {
                str.append(valuees[i].toString());
            } else {
                str.append(",").append(valuees[i].toString());
            }
        }
        str.append("]");
        return str.toString();
    }

    public ReadArray<T>[] getValuees()
    {
        return  valuees;
    }
}
