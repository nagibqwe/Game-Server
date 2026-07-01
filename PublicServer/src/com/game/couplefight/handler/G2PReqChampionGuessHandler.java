package com.game.couplefight.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 游戏服到公共服-请求竞猜
 * @Auther: gouzhongliang
 * @Date: 2021/7/21 15:58
 */
@Message(id = CouplefightMessage.G2PReqChampionGuess.MsgID.eMsgID_VALUE, clazz = CouplefightMessage.G2PReqChampionGuess.class)
public class G2PReqChampionGuessHandler extends Handler<CouplefightMessage.G2PReqChampionGuess> {

    static final Logger log = LogManager.getLogger(G2PReqChampionGuessHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, CouplefightMessage.G2PReqChampionGuess messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.couplefightManager.getScript().reqChampionGuess(mess.getContext(), messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PReqChampionGuessHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
