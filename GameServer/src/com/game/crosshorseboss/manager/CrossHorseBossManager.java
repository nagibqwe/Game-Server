package com.game.crosshorseboss.manager;

import com.game.crosshorseboss.script.ICrosshorseBoss;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;

/**
 * Created by cxl on 2021/4/16.
 */
public class CrossHorseBossManager  {

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        CrossHorseBossManager processor;

        Singleton() {
            this.processor = new CrossHorseBossManager();
        }

        CrossHorseBossManager getProcessor() {
            return processor;
        }
    }

    public static CrossHorseBossManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public ICrosshorseBoss deal() throws Exception {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.CrossHorseBossScript);
        if (is instanceof ICrosshorseBoss) {
            return (ICrosshorseBoss) is;
        }
        throw new Exception("没有实现跨服战场的副本！ICrosshorseBoss没有找到具体的实例！");
    }

}
