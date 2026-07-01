package com.game.fight.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.FightMessage.ReqUseSkill;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //使用技能
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqUseSkill.MsgID.eMsgID_VALUE, clazz = ReqUseSkill.class)

public class ReqUseSkillHandler extends Handler<ReqUseSkill> {

    static final Logger log = LogManager.getLogger(ReqUseSkillHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqUseSkill message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.fightManager.deal().OnReqUseSkill(player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
