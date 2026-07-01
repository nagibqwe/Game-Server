package com.game.fight.client;

import com.game.structs.SessionAttribute;
import game.core.command.Handler;
import game.core.message.RMessage;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 
import game.core.util.TimeUtils;
import game.message.FightMessage.ResUseSkill;
import org.apache.mina.core.session.IoSession;

/**
 * makehandler v1.7 for netty 伤害结果返回
 */
public class ResUseSkillHandler extends Handler {

    private static final Logger log = LogManager.getLogger(ResUseSkillHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            ResUseSkill messInfo = (ResUseSkill) mess.getData();
            if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                return;
            }
//            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
//            Position curPos = new Position((float) player.getPosX(), (float) player.getPosY());
//            Position pos1 = new Position(messInfo.getMoveToX(), messInfo.getMoveToY());
//            double dir = MapUtils.getDistance(curPos, pos1);
//            log.error( player.getUserId() + "玩家坐标（：" + player.getPosX() + "," + player.getPosY() + ")攻动坐标（：" + messInfo.getMoveToX() + "," + messInfo.getMoveToY() + ") d=" + dir);
//            player.setPosX(messInfo.getMoveToX());
//            player.setPosY(messInfo.getMoveToY());
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResUseSkillHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e, e);
        }
    }

}
