package com.game.nature.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.NatureMessage.ReqNatureUpLevel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //使用物品升级 ----神兵升级也走这个
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqNatureUpLevel.MsgID.eMsgID_VALUE, clazz = ReqNatureUpLevel.class)

public class ReqNatureUpLevelHandler extends Handler<ReqNatureUpLevel> {

    static final Logger log = LogManager.getLogger(ReqNatureUpLevelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqNatureUpLevel messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            if (player != null) {
                Manager.natureManager.deal().onReqNatureUpLevel(player, messInfo.getNatureType(), messInfo.getItemid(), messInfo.getIsOneKeyUp());
            } else {
                log.error("未获取到玩家数据！");
            }

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqNatureUpLevelHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
