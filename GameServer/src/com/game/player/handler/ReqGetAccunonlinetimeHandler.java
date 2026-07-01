package com.game.player.handler;

import com.game.player.structs.Player;
import com.game.utils.MessageUtils;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PlayerMessage;
import game.message.PlayerMessage.ReqGetAccunonlinetime;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //获取在线时长
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqGetAccunonlinetime.MsgID.eMsgID_VALUE, clazz = ReqGetAccunonlinetime.class)

public class ReqGetAccunonlinetimeHandler extends Handler<ReqGetAccunonlinetime> {

    static final Logger log = LogManager.getLogger(ReqGetAccunonlinetimeHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqGetAccunonlinetime messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            PlayerMessage.ResAccunonlinetime.Builder msg = PlayerMessage.ResAccunonlinetime.newBuilder();
            msg.setTime(player.getAccunonlinetime());
            MessageUtils.send_to_player(player, PlayerMessage.ResAccunonlinetime.MsgID.eMsgID_VALUE, msg.build().toByteArray());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAccunonlinetimeHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
