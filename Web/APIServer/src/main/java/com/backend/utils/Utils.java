package com.backend.utils;

/**
 * @Desc TODO
 * @Date 2020/9/3 20:00
 * @Auth ZUncle
 */
public class Utils {

    public static int[] splitInteger(String str, String regex) {
        String[] split = str.split(regex);
        int[] res = new int[split.length];
        for (int i=0; i< split.length; i++) {
            res[i] = Integer.parseInt(split[i]);
        }
        return res;
    }
    public static long[] splitLong(String str, String regex) {
        String[] split = str.split(regex);
        long[] res = new long[split.length];
        for (int i=0; i< split.length; i++) {
            res[i] = Long.parseLong(split[i]);
        }
        return res;
    }
}
