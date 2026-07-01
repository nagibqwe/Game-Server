package com.game.boss.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.BossMessage.ReqMySelfBossUseItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //使用道具
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqMySelfBossUseItem.MsgID.eMsgID_VALUE, clazz = ReqMySelfBossUseItem.class)

public class ReqMySelfBossUseItemHandler extends Handler<ReqMySelfBossUseItem> {

    static final Logger log = LogManager.getLogger(ReqMySelfBossUseItemHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqMySelfBossUseItem message) {
        try {
            long start = TimeUtils.Time();

            Player player = session.getExecutor();
            Manager.bossManager.personalBossScript().call(player, "onReqMySelfBossUseItem", message.getItemid(), message.getNum());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqMySelfBossUseItemHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
