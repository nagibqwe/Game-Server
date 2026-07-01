package com.game.map.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MapMessage.ReqPetJumpBlock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //出战宠物跳出阻挡
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqPetJumpBlock.MsgID.eMsgID_VALUE, clazz = ReqPetJumpBlock.class)

public class ReqPetJumpBlockHandler extends Handler<ReqPetJumpBlock> {

    static final Logger log = LogManager.getLogger(ReqPetJumpBlockHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqPetJumpBlock message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.mapManager.deal().OnReqPetJumpBlock(player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
