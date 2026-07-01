/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.data.struct;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

/**
 *
 * @author hewei
 */
public abstract class ReadArray<T> {

    private  T[] value;

    public ReadArray() {
    }

    public ReadArray(T[] values) {
        this.value = values;
    }

    public ReadArray(String str, String spilt) {
        value = initValueFromStr(str, spilt);
    }

    public final int size() {
        return value.length;
    }

    public final boolean isEmpty() {
        return value.length == 0;
    }

    public final int indexOf(T v) {
        for (int i = 0; i < value.length; ++i) {
            if (Objects.equals(value[i], v)) {
                return i;
            }
        }
        return -1;
    }

    /**
     *
     * 判断集合是否包含某个值
     * @param beginIndex 开始索引下标
     * @param endIndex 结束索引下标
     * @param v
     * @return
     */
    public final boolean contains(int beginIndex ,int endIndex ,T v) {
        if(beginIndex > endIndex){
            return false;
        }
        if(endIndex>value.length){
            endIndex = value.length;
        }
        for(int i = beginIndex;i<endIndex;i++){
            T one = value[i];
            if (Objects.equals(one, v)) {
                return true;
            }
        }
        return false;
    }
    public final boolean contains(T v) {
        for (T one : value) {
            if (Objects.equals(one, v)) {
                return true;
            }
        }
        return false;
    }
    public final T get(int index, T defValue) {
        if (index < 0) {
            return defValue;
        }
        if (index >= value.length) {
            return defValue;
        }
        return value[index];
    }
    public final T get(int index) {
        return get(index, null);
    }

    protected abstract T[] initValueFromStr(String str, String spilt);

    @Override
    public String toString() {
        return Arrays.toString(value);
    }

    public  T[] getValue()
    {
        return  value;
    }

    public String getValueString(){
        StringBuilder b = new StringBuilder();
        for (int i = 0 ;i< value.length;i++){
            b.append(String.valueOf(value[i]));
            if (i < value.length -1) {
                b.append(",");
            }
        }
        return b.toString();
    }
}
