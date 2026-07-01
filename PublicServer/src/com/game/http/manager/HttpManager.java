package com.game.http.manager;

import com.game.http.script.IHttpScript;
import com.game.script.ScriptEnum;
import game.core.script.IScript;
import game.core.script.ScriptManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * HTTP操作管理类
 */
public class HttpManager {

    private static final Logger log = LogManager.getLogger(HttpManager.class);

    private enum Singleton {
        INSTANCE;                   //一个枚举的元素，它就代表了Singleton的一个实例
        HttpManager manager;
        Singleton() {
            this.manager = new HttpManager();
        }
        HttpManager getProcessor() {
            return manager;
        }
    }
    public static HttpManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public IHttpScript deal() {
        IScript is = ScriptManager.getInstance().GetScriptClass(ScriptEnum.HttpBackScript);
        if (is instanceof IHttpScript) {
            return (IHttpScript) is;
        } else {
            log.info("HTTP操作:没有找到执行的脚本处理！");
            return null;
        }
    }
}
