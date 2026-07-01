package com.game.utils;

import java.util.Map;

/**
 * Created by zcd on 2018/4/9.
 */
public class IntegerMapHelp {
    public static void put(Map<Integer, Integer> map, int key, int value){
        //是0直接不用操作了
        if (value == 0) {
            return;
        }
        if (map.containsKey(key)) {
            map.put(key, map.get(key) + value);
        } else {
            map.put(key, value);
        }
    }

    /**
     * 获取所有的value值之和
     * @param map
     * @return
     */
    public static int getAll(Map<Integer, Integer> map){
        int result = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            result += entry.getValue();
        }
        return result;
    }

    public static boolean delete(Map<Integer, Integer> map, int key, int value){
        if (!map.containsKey(key)){
            return false;
        }
        if (map.get(key) <= value){
            map.remove(key);
            return true;
        }
        int newValue = value - map.get(key);
        map.put(key, newValue);
        return true;
    }

    /**
     * 求两个集合的差集，如果extra中 base中没有的数据，就返回false；
     * @param base
     * @param extra
     * @return
     */
    public static boolean DifferenceSet(Map<Integer, Integer> base, Map<Integer, Integer> extra){
        boolean result = true;
        for (Map.Entry<Integer, Integer> entry : extra.entrySet()) {
            if (!delete(base, entry.getKey(), entry.getValue())){
                result &= false;
            }
        }
        return result;
    }
}
