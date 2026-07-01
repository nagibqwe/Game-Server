package com.game.player.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PlayerMessage.ReqUpgradeBlood;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //玩家血脉系统升级
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqUpgradeBlood.MsgID.eMsgID_VALUE, clazz = ReqUpgradeBlood.class)

public class ReqUpgradeBloodHandler extends Handler<ReqUpgradeBlood> {

    static final Logger log = LogManager.getLogger(ReqUpgradeBloodHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqUpgradeBlood messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.playerManager.deal( ScriptEnum.BloodBaseScript).upgradeBlood(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqUpgradeBloodHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
