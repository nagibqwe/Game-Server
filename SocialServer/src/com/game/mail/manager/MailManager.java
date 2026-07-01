package com.game.mail.manager;

import com.game.mail.script.IMailScript;
import com.game.manager.Manager;
import com.game.script.struct.ScriptEnum;
import game.core.script.IScript;

/**
 * @Desc TODO
 * @Date 2021/8/2 15:11
 * @Auth ZUncle
 */
public class MailManager {

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        INSTANCE;                   //一个枚举的元素，它就代表了Singleton的一个实例
        MailManager manager;

        Singleton() {
            this.manager = new MailManager();
        }

        MailManager getProcessor() {
            return manager;
        }
    }

    public static MailManager getInstance() {
        return MailManager.Singleton.INSTANCE.getProcessor();
    }

    /**
     * 获取逻辑脚本九 零一 起玩 www.90 175.com
     * @return
     */
    public IMailScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.MailScript);
        return (IMailScript) is;
    }

}
