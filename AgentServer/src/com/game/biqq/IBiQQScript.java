package com.game.biqq;

import game.core.script.IScript;

public interface IBiQQScript extends IScript {

    /**
     * 日志记录
     */
    public void logRegister(long userId, String loginIp);

}
