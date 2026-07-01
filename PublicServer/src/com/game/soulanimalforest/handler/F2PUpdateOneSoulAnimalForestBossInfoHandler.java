package com.game.soulanimalforest.handler;

import com.game.soulanimalforest.manager.SoulAnimalForestManager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SoulAnimalForestMessage.F2PUpdateOneSoulAnimalForestBossInfo;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //跨服通知所有的战斗服的单个BOSS更新，因为刷新了下一波的出生怪，有可能怪的下一次刷新时间是会变的
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = F2PUpdateOneSoulAnimalForestBossInfo.MsgID.eMsgID_VALUE, clazz = F2PUpdateOneSoulAnimalForestBossInfo.class)

public class F2PUpdateOneSoulAnimalForestBossInfoHandler extends Handler<F2PUpdateOneSoulAnimalForestBossInfo> {

    static final Logger log = LogManager.getLogger(F2PUpdateOneSoulAnimalForestBossInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, F2PUpdateOneSoulAnimalForestBossInfo messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            SoulAnimalForestManager.getInstance().manager().onF2PUpdateOneSoulAnimalForestBossInfo(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2PUpdateOneSoulAnimalForestBossInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
