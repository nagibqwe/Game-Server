package com.game.guildcrossfud.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GuildCrossFudMessage.ReqDevilBossList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求魔王缝隙boss列表
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqDevilBossList.MsgID.eMsgID_VALUE, clazz = ReqDevilBossList.class)

public class ReqDevilBossListHandler extends Handler<ReqDevilBossList> {

    static final Logger log = LogManager.getLogger(ReqDevilBossListHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqDevilBossList messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.crossFudManager.deal().ReqDevilBossListHandler(mess.getExecutor(), messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqDevilBossListHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
