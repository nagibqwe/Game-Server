package com.game.fight.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.FightMessage.ReqPlaySkillObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //播放技能召唤物，有可能是简单召唤物，也有可能是复杂召唤物
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqPlaySkillObject.MsgID.eMsgID_VALUE, clazz = ReqPlaySkillObject.class)

public class ReqPlaySkillObjectHandler extends Handler<ReqPlaySkillObject> {

    static final Logger log = LogManager.getLogger(ReqPlaySkillObjectHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqPlaySkillObject message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.fightManager.deal().onReqPlaySkillObject(player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
