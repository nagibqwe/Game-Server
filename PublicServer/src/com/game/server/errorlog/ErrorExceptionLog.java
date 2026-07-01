/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.server.errorlog;

/**
 *errorlog的收集数据结构
 * @author soko <xuchangming@haowan123.com>
 */
public class ErrorExceptionLog extends ErrorLog{

    //错误类型
    private String errorType;
    //错误内容
    private String errorValue;

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorValue() {
        return errorValue;
    }

    public void setErrorValue(String errorValue) {
        this.errorValue = errorValue;
    }
    
    
    
    
}
