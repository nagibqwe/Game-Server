package com.game.copymap.handler;

import com.game.cooldown.structs.CooldownTypes;
import com.game.manager.Manager;
import com.game.nature.structs.HuaxinEntity;
import com.game.player.structs.Player;
import com.game.skill.structs.Skill;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CopyMapMessage.ReqBossStateClearCd;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //境界boss清理CD
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqBossStateClearCd.MsgID.eMsgID_VALUE, clazz = ReqBossStateClearCd.class)

public class ReqBossStateClearCdHandler extends Handler<ReqBossStateClearCd> {

    static final Logger log = LogManager.getLogger(ReqBossStateClearCdHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqBossStateClearCd message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            HuaxinEntity entity = player.getCurHuaxinEntity();
            if (entity == null) {
                return;
            }

            for (Skill skill : entity.getBaseSkills().values()) {
                Manager.cooldownManager.addCooldown(player, CooldownTypes.Player_FlySowrd_CD_Skill, String.valueOf(skill.getSkillId()), 0);
            }

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqBossStateClearCdHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
