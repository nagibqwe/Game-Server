package com.game.boss.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.BossMessage.ReqAddWorldBossRankCount;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //添加世界BOSS次数
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqAddWorldBossRankCount.MsgID.eMsgID_VALUE, clazz = ReqAddWorldBossRankCount.class)

public class ReqAddWorldBossRankCountHandler extends Handler<ReqAddWorldBossRankCount> {

    static final Logger log = LogManager.getLogger(ReqAddWorldBossRankCountHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqAddWorldBossRankCount message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.bossManager.manager().buyRankCount(player, message.getBossType());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqAddWorldBossRankCountHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
