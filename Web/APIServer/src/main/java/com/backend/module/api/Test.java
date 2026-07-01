package com.backend.module.api;

import com.backend.utils.DateUtil;

public class Test {

    public static void main(String[] args){
        long beging = DateUtil.getWeekStartSec(System.currentTimeMillis());
        System.out.println("xxxxxxxxxxxx"+beging);

        long end = DateUtil.getWeekEndSec(System.currentTimeMillis());
        System.out.println("xxxxxxxxxxxx"+end);
    }
}
