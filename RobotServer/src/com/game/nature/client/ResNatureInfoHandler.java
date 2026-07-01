package com.game.nature.client;

import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import game.core.command.Handler;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.EquipMessage;
import game.message.NatureMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.session.IoSession;


/**
 * 客户端收到法宝信息处理
 */
public class ResNatureInfoHandler extends Handler {

    private static final Logger log = LogManager.getLogger(ResNatureInfoHandler.class);

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            NatureMessage.ResNatureInfo messInfo = (NatureMessage.ResNatureInfo) mess.getData();
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            if (player == null) {
                return;
            }
            player.initNature(messInfo);
            log.info("ResNatureInfo>" + player.getInfo() + "收到造化信息,type=" + messInfo.getNatureType());
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                log.error("ResNatureInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }

}