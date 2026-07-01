package com.game.ipfind.manager;

import com.game.ipfind.script.IIPFinderScript;
import com.game.login.script.ILoginScript;
import common.LoginUrl;
import game.core.script.IScript;
import game.core.script.ScriptManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author gaozhaoguang
 * @desc IPFinderManager
 * @date Created on 2021/7/29 20:23
 **/
public class IPFinderManager {

    private static final Logger logger = LogManager.getLogger(IPFinderManager.class);
    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        IPFinderManager processor;

        Singleton() {
            this.processor = new IPFinderManager();
        }

        IPFinderManager getProcessor() {
            return processor;
        }
    }

    public static IPFinderManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public IIPFinderScript deal() {
        IScript is = ScriptManager.getInstance().GetScriptClass(2);
        if (is instanceof IIPFinderScript) {
            return (IIPFinderScript) is;
        } else {
            logger.info("渠道账号:没有找到执行的脚本处理！");
            return null;
        }
    }

}
