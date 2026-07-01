package com.game.copymap.sort;


import java.util.Comparator;
import java.util.Map;

/**
 * Created by zcd on 2018/1/10.
 */
public class SortIntegerInteger implements Comparator<Map.Entry<Integer, Integer>> {
    @Override
    public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
        //只比较value
        if (o1.getKey() < o2.getKey()){
            return -1;
        }else if(o1.getKey().equals(o2.getKey())){
            return 0;
        }
        return 1;
    }
}
