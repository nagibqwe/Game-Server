package com.game.boss.client;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import game.core.command.Handler;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.BossMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.session.IoSession;

/**
 * 服务器返回的世界boss剩余收益次数
 */
public class ResOpenDreamBossHandler extends Handler {

    private static final Logger log = LogManager.getLogger(ResOpenDreamBossHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            BossMessage.ResOpenDreamBoss messInfo = (BossMessage.ResOpenDreamBoss) mess.getData();
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            if (player == null) {
                return;
            }
            Manager.bossManager.deal().onResOpenDreamBoss(player,messInfo);
//            Manager.bossManager.deal().updateBossCount(player, messInfo.getBossType(), messInfo.getRemainRankCount());
            log.info("ResOpenDreamBoss>" + player.getInfo() + "收到世界boss面板信息，收益次数：" + messInfo.getRemainCount()+",type="+messInfo.getBossType());
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResRecycleHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e, e);
        }
    }

}
