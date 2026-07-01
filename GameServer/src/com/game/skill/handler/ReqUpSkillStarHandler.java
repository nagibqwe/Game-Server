package com.game.skill.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SkillMessage.ReqUpSkillStar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //升级技能（升星）
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqUpSkillStar.MsgID.eMsgID_VALUE, clazz = ReqUpSkillStar.class)

public class ReqUpSkillStarHandler extends Handler<ReqUpSkillStar> {

    static final Logger log = LogManager.getLogger(ReqUpSkillStarHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqUpSkillStar messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)mess.getExecutor();
            Manager.skillManager.deal().onReqUpSkillStar(player,messInfo.getSkillID());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqUpSkillStarHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
