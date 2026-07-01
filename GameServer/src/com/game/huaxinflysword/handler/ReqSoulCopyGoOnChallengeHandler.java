package com.game.huaxinflysword.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HuaxinFlySwordMessage.ReqSoulCopyGoOnChallenge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //剑灵副本内请求刷怪
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqSoulCopyGoOnChallenge.MsgID.eMsgID_VALUE, clazz = ReqSoulCopyGoOnChallenge.class)

public class ReqSoulCopyGoOnChallengeHandler extends Handler<ReqSoulCopyGoOnChallenge> {

    static final Logger log = LogManager.getLogger(ReqSoulCopyGoOnChallengeHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqSoulCopyGoOnChallenge message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.scriptManager.GetScriptClass(ScriptEnum.SwordSoulTowerMapScript).call(player, "goOnChallenge");

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
