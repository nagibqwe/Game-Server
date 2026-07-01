package com.game.treasurehunt.handler;

import com.game.player.structs.Player;
import com.game.treasurehunt.manager.TreasureHuntManager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.TreasureHuntMessage.ReqOnekeyExtract;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求一键提取
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqOnekeyExtract.MsgID.eMsgID_VALUE, clazz = ReqOnekeyExtract.class)

public class ReqOnekeyExtractHandler extends Handler<ReqOnekeyExtract> {

    static final Logger log = LogManager.getLogger(ReqOnekeyExtractHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqOnekeyExtract messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            TreasureHuntManager.getInstance().deal().onekeyExtract(player,messInfo.getType());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqOnekeyExtractHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
