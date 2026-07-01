package com.game.platformevaluate.manager;

import com.game.manager.Manager;
import com.game.platformevaluate.script.IPlatformEvaluateScript;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 * 点赞、分享和评价管理
 */
public class PlatformEvaluateManager {

    private static final Logger log = LogManager.getLogger("PlatformEvaluateManager");

       /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        PlatformEvaluateManager manager;

        Singleton() {
            this.manager = new PlatformEvaluateManager();
        }

        PlatformEvaluateManager getProcessor() {
            return manager;
        }
    }

    public static PlatformEvaluateManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
    
    
    public IPlatformEvaluateScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.PlatformEvaluateBaseScript);
        if (is instanceof IPlatformEvaluateScript) {
            return (IPlatformEvaluateScript) is;
        } else {
            log.error("没有找到具体的接口实现类！不会走到这里，请注意实现！");
            return null;
        }
    }
}
