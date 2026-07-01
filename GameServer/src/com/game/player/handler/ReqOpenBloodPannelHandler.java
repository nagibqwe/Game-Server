package com.game.player.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PlayerMessage.ReqOpenBloodPannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //玩家打开血脉系统
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqOpenBloodPannel.MsgID.eMsgID_VALUE, clazz = ReqOpenBloodPannel.class)

public class ReqOpenBloodPannelHandler extends Handler<ReqOpenBloodPannel> {

    static final Logger log = LogManager.getLogger(ReqOpenBloodPannelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqOpenBloodPannel messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.playerManager.deal(ScriptEnum.BloodBaseScript).openBloodPannel(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqOpenBloodPannelHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
