package com.game.player.manager;


import com.game.manager.Manager;
import com.game.player.script.IPlayerAttribute;
import com.game.player.structs.PlayerAttributeType;
import com.game.attribute.script.IAttributeScript;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author lw
 */
public class PlayerAttributeManager {

    private static final Logger logger = LogManager.getLogger(PlayerAttributeManager.class);


    private enum Singleton {
        INSTANCE;
        PlayerAttributeManager manager;

        Singleton() {
            this.manager = new PlayerAttributeManager();
        }

        PlayerAttributeManager getProcessor() {
            return manager;
        }
    }

    public static PlayerAttributeManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public IAttributeScript deal(PlayerAttributeType type) {
        int scriptId = ScriptEnum.AttributeScript * ScriptEnum.NodeLimit + type.getValue();
        IScript is = Manager.scriptManager.GetScriptClass(scriptId);
        if (is == null) {
            logger.error("IPlayerAttributeScript接口脚本错误 type={}", type);
            return null;
        }
        return (IAttributeScript) is;
    }
    public IPlayerAttribute deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.AttributeScript);
        if (is == null) {
            logger.error("IPlayerAttribute 错误");
            return null;
        }
        return (IPlayerAttribute) is;
    }

}
