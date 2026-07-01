package com.game.nature.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.NatureMessage.ReqNatureFashionUpLevel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求化形升级，激活也是他
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqNatureFashionUpLevel.MsgID.eMsgID_VALUE, clazz = ReqNatureFashionUpLevel.class)

public class ReqNatureFashionUpLevelHandler extends Handler<ReqNatureFashionUpLevel> {

    static final Logger log = LogManager.getLogger(ReqNatureFashionUpLevelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqNatureFashionUpLevel messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            if (player != null) {
                Manager.natureManager.deal().onReqNatureFashionUpLevel(player, messInfo.getNatureType(), messInfo.getId());
            } else {
                log.error("未获取到玩家数据！");
            }

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqNatureFashionUpLevelHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
