/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.server.script;

import com.game.server.errorlog.ErrorInfo;
import game.core.script.IScript;

/**
 *
 * @author soko <xuchangming@haowan123.com>
 */
public interface IErrorLogScript extends IScript {

    public void addlog(ErrorInfo errorInfo);
}
