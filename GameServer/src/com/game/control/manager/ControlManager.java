package com.game.control.manager;

import com.game.control.command.OperateCommand;
import com.game.control.script.IControlScript;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lw
 * 系统功能关闭系统
 */
public class ControlManager {

    private static final Logger logger = LogManager.getLogger("ControlManager");
    /**
     * 关闭状态
     */
    public static final int CLOSE_FUNCTION = 0;
    /**
     * 开启未领奖状态
     */
    public static final int OPEN_FUNCTION = 1;
    /**
     * 开启领奖状态
     */
    public static final int OPEN_REWARD_FUNCTION = 2;


    /**
     * 所有开关条件列表
     */
    private final ConcurrentHashMap<Integer, Set<Integer>> condFunc = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Integer, Set<Integer>> getCondFunc() {
        return condFunc;
    }

    public IControlScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.ControlBaseScript);
        if (is instanceof IControlScript) {
            return (IControlScript) is;
        } else {
            logger.error("没有实现开关控制脚本！");
            return null;
        }
    }

    public void operate(Player player, int type, int change) {

        Manager.controlManager.deal().operate(player, type, change);
//        GameServer.getInstance().getAssistThread().addCommand(new OperateCommand(player, type, changeNum));
    }

    public void operate(Player player, int type, int subType, int changeNum) {
        Manager.controlManager.deal().operate(player, changeNum, type, subType);
    }

    public void operate(Player player, int type, int subType, int ssType, int changeNum) {
        Manager.controlManager.deal().operate(player, changeNum, type, subType, ssType);
    }

    private enum Singleton {
        INSTANCE;
        ControlManager controlManager;

        Singleton() {
            this.controlManager = new ControlManager();
        }

        ControlManager getProcessor() {
            return controlManager;
        }
    }

    public static ControlManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
