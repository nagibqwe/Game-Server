/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.structs;

/**
 *
 * @author lanxiang@haowan123.com
 */
public enum LanguageEnum {
    
    CHINESE("cn", 0),             //中文 "cn"为language_cn.properties名字中的cn，也即server-config.xml中langType配置的语言类型
    
    TAIWAN("tw", 1),              //台湾文
    
    THAILAND("Thailand", 2),      //泰国文
    
    VIETNAM("yn", 3),             //越南文
    
    KOREAN("kr", 4),              //韩国文
    
    ENGLISH("ros", 5),            //新马语言，也是英语的
    
    ;
    
    private final String sType;   //语言字符串配置类型
    
    private final int iType;      //语言数字化简配类型
    
    LanguageEnum(String sType, int iType) {
        this.sType = sType;
        this.iType = iType;
    }
    
    public String getsType() {
        return sType;
    }

    public int getiType() {
        return iType;
    }
    
}
