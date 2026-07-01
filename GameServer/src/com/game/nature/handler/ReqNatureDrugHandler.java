package com.game.nature.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.NatureMessage.ReqNatureDrug;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //吃药使用物品 ----神兵没有吃药功能
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqNatureDrug.MsgID.eMsgID_VALUE, clazz = ReqNatureDrug.class)

public class ReqNatureDrugHandler extends Handler<ReqNatureDrug> {

    static final Logger log = LogManager.getLogger(ReqNatureDrugHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqNatureDrug messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            if (player != null) {
                Manager.natureManager.deal().onReqNatureDrug(player, messInfo.getNatureType(), messInfo.getItemid());
            } else {
                log.error("未获取到玩家数据！");
            }

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqNatureDrugHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
