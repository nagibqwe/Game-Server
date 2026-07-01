package com.game.treasurehuntxianjia.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.TreasureHuntXianjiaMessage.ReqTreasureHuntMibao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求密保
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqTreasureHuntMibao.MsgID.eMsgID_VALUE, clazz = ReqTreasureHuntMibao.class)

public class ReqTreasureHuntMibaoHandler extends Handler<ReqTreasureHuntMibao> {

    static final Logger log = LogManager.getLogger(ReqTreasureHuntMibaoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqTreasureHuntMibao messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.treasureHuntXianjiaManager.deal().onReqTreasureHuntMibao(player,messInfo.getType(),messInfo.getId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqTreasureHuntMibaoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
