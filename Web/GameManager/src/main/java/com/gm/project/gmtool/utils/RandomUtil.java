package com.gm.project.gmtool.utils;

import java.util.Random;

public class RandomUtil {

    static Random r = new Random();

    public static int random(int min, int max) {
        return r.nextInt((max - min) + 1) + min;
    }

    public static StringGenerator sg(int min, int max) {
        return new StringGenerator(min, max);
    }

    public static StringGenerator sg(int len) {
        return new StringGenerator(len, len);
    }

    public static void main(String[] args) {
        String key;
        for (int i = 0; i < 100; i++) {
            key = RandomUtil.sg(6).next();
            System.out.println(key);
        }
    }
}
