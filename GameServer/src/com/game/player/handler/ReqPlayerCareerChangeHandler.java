package com.game.player.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PlayerMessage.ReqPlayerCareerChange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //玩家职业转换功能
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqPlayerCareerChange.MsgID.eMsgID_VALUE, clazz = ReqPlayerCareerChange.class)

public class ReqPlayerCareerChangeHandler extends Handler<ReqPlayerCareerChange> {

    static final Logger log = LogManager.getLogger(ReqPlayerCareerChangeHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqPlayerCareerChange messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)mess.getExecutor();
            Manager.playerManager.manager().onChangeCareer(player, messInfo.getCareerNo());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqPlayerCareerChangeHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
