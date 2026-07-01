package com.game.thread;

import com.game.server.script.IhandlerScript;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <b>命令分发线程</b>
 *
 * @author ChenLong
 */
public class DispatchProcessor {

    private static final Logger log = LogManager.getLogger(DispatchProcessor.class);

    private DispatchProcessor() {
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        DispatchProcessor processor;

        Singleton() {
            this.processor = new DispatchProcessor();
        }

        DispatchProcessor getProcessor() {
            return processor;
        }
    }

    /**
     * 获取DispatchProcessor的实例对象.
     *
     * @return
     */
    public static DispatchProcessor getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public IhandlerScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.HandlerScript);
        if (is == null) {
            log.error("没有找到过渡实例 IhandlerScript！=============");
            return null;
        }
        return ((IhandlerScript) is);
    }
}
