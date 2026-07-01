/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.server.script;

import com.game.server.structs.ErrorInfo;

/**
 * 错误日志报告
 *
 * @author soko <xuchangming@haowan123.com>
 */
public interface IErrorReportScript {

    public void addlog(ErrorInfo errorInfo);

    public void removeErrorLog(long roleId);
}
