package com.game.spirit.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SpiritMessage.ReqUpStar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求点亮灵星消息
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqUpStar.MsgID.eMsgID_VALUE, clazz = ReqUpStar.class)

public class ReqUpStarHandler extends Handler<ReqUpStar> {

    static final Logger log = LogManager.getLogger(ReqUpStarHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqUpStar messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.equipManager.deal().upStar(player, messInfo.getStarNum());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqUpStarHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
