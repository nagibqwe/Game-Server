package com.backend.utils;

public class StringGenerator {

    private static final char[] src = "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    private int maxLen;
    private int minLen;

    public StringGenerator(int max) {
        maxLen = max;
        minLen = 1;
    }

    public StringGenerator(int min, int max) {
        maxLen = max;
        minLen = min;
    }

    public String next() {
        if (maxLen <= 0 || minLen <= 0 || minLen > maxLen)
            return null;
        char[] buf = new char[RandomUtil.random(minLen, maxLen)];
        for (int i = 0; i < buf.length; i++) {
            buf[i] = src[Math.abs(RandomUtil.r.nextInt(src.length))];
        }
        return new String(buf);
    }

}
