package com.game.equip.client;

import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import game.core.command.Handler;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.EquipMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.session.IoSession;


/**
 * 穿戴装备成功
 */
public class ResEquipWearSuccessHandler extends Handler {

    private static final Logger log = LogManager.getLogger(ResEquipWearSuccessHandler.class);

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            EquipMessage.ResEquipWearSuccess messInfo = (EquipMessage.ResEquipWearSuccess) mess.getData();
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            if (player == null) {
                return;
            }
            log.info("ResEquipWearSuccess>" + player.getInfo() + "穿戴装备成功,modelId=" + messInfo.getEquiped().getItemModelId());
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                log.error("ResEquipWearSuccessHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }

}