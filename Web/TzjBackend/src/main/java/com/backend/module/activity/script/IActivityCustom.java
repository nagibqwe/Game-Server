package com.backend.module.activity.script;

import com.backend.bean.Activity;

import java.text.ParseException;
import java.util.Map;

/**
 * @Desc TODO
 * @Date 2020/9/3 16:10
 * @Auth ZUncle
 */
public interface IActivityCustom {

    /**
     * 解析自定义参数
     * @param paramMap
     * @return
     */
    Activity parseCustom( Map<String, String[]> paramMap) throws ParseException;

}
