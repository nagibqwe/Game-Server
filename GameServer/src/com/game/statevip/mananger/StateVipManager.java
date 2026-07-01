package com.game.statevip.mananger;

import com.data.FunctionStart;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.statevip.script.IStateVip;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**

 * @author admin
 */
public class StateVipManager {

    private static final Logger logger = LogManager.getLogger(StateVipManager.class);

    private enum Singleton {
        INSTANCE;

        StateVipManager processor;

        Singleton() {
            this.processor = new StateVipManager();
        }

        StateVipManager getProcessor() {
            return processor;
        }
    }

    public static StateVipManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public IStateVip deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.StateLevelBaseScript);
        if (is instanceof IStateVip) {
            return (IStateVip) is;
        } else {
            logger.error("IStateLevel接口脚本错误");
            return null;
        }
    }

    public void playerOnLine(Player player) {
        if (Manager.controlManager.deal().isOpenFunction(player, FunctionStart.Realm)) {
            deal().reqStateVip(player);
        }
    }


}
