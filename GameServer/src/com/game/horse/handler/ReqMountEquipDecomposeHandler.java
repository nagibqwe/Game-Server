package com.game.horse.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HorseMessage.ReqMountEquipDecompose;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client->Game 装备分解 没有对应的res
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqMountEquipDecompose.MsgID.eMsgID_VALUE, clazz = ReqMountEquipDecompose.class)

public class ReqMountEquipDecomposeHandler extends Handler<ReqMountEquipDecompose> {

    static final Logger log = LogManager.getLogger(ReqMountEquipDecomposeHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqMountEquipDecompose message) {
        try {
            long start = TimeUtils.Time();

            Manager.horseManager.deal().horseEquipDecompose(session.getExecutor(), message.getEquipIdList());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
