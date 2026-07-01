package com.game.nature.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.NatureMessage.ReqNatureModelSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //设置显示模型
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqNatureModelSet.MsgID.eMsgID_VALUE, clazz = ReqNatureModelSet.class)

public class ReqNatureModelSetHandler extends Handler<ReqNatureModelSet> {

    static final Logger log = LogManager.getLogger(ReqNatureModelSetHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqNatureModelSet messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            if (player != null) {
                Manager.natureManager.deal().onReqNatureModelSet(player, messInfo.getNatureType(), messInfo.getModelId());
            } else {
                log.error("未获取到玩家数据！");
            }

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqNatureModelSetHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
