package com.game.map.client;

import com.game.map.structs.MapMonster;
import com.game.map.structs.MapPeople;
import game.core.command.Handler;
import game.core.util.TimeUtils;
import game.message.MapMessage.ResUpdateCamp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.session.IoSession;
import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import game.core.message.RMessage;

/**
 * makehandler v1.7 for netty 更新阵营
 */
public class ResUpdateCampHandler extends Handler {

    private static final Logger log = LogManager.getLogger(ResUpdateCampHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            ResUpdateCamp messInfo = (ResUpdateCamp) mess.getData();
            if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                return;
            }
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            if (player == null) {
                return;
            }

            //设置场景阵营中的玩家阵营
            if (player.getId() == messInfo.getId()) {
                player.setCampNo(messInfo.getCamp());
            } else {
                MapPeople mp = player.getMapPeopleById(messInfo.getId());
                if (mp != null) {
                    mp.setCampNo(messInfo.getCamp());
                    return;
                }

                MapMonster mm = player.getMapMonsterById(messInfo.getId());
                if (mm != null) {
                    mm.setCampNo(messInfo.getCamp());
                }
            }

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResUpdateCampHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e, e);
        }
    }

}
