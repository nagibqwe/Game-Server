/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.server.errorlog;

/**
 *错误信息结构
 * @author soko <xuchangming@haowan123.com>
 */
public class ErrorInfo {

    private final String key;

    private final ErrorLog log;

    public ErrorInfo(String key, ErrorLog log) {
        this.key = key;
        this.log = log;
    }

    public String getKey() {
        return key;
    }

    public ErrorLog getLog() {
        return log;
    }

}
