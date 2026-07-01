package com.game.copymap.sort;

import java.util.Comparator;

/**
 * Created by zcd on 2018/4/9.
 */
public class SortInteger implements Comparator<Integer>{
    @Override
    public int compare(Integer o1, Integer o2) {
        return 0 - o1.compareTo(o2);
    }
}
