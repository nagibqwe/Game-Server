package com.game.fight.client;
import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import game.core.command.Handler;
import game.core.message.RMessage;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 
import game.core.util.TimeUtils;
import game.message.FightMessage.ResUseSkillError;
import org.apache.mina.core.session.IoSession;


/**
* makehandler  v1.7 for netty
*技能使用失败返回
*/
public class ResUseSkillErrorHandler extends Handler{

    private static final Logger log = LogManager.getLogger(ResUseSkillErrorHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            ResUseSkillError messInfo = (ResUseSkillError) mess.getData();
          if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                return;
            }
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            //log.error(player.getUserId() + " 坐标（" + player.getPos()+")");
            player.skillErrorMoveTo();
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResUseSkillErrorHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e,e);
        }
    }

}