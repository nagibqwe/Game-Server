package com.game.map.client;
import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import com.game.utils.Utils;
import game.core.command.Handler;
import game.core.message.RMessage;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 
import game.core.util.TimeUtils;
import game.message.MapMessage.ResMapMonster;
import org.apache.mina.core.session.IoSession;


/**
* makehandler  v1.5
*给周围玩家发送Monster出生消息
*/
public class ResMapMonsterHandler extends Handler{

    private final Logger log = LogManager.getLogger(ResMapMonsterHandler.class);
    private final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            ResMapMonster messInfo = (ResMapMonster) mess.getData();
            if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                return;
            }
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            player.addMapMonster(Utils.makeMapMonster(messInfo.getMonserInfo()));
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResMapMonsterHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e,e);
        }
    }

}