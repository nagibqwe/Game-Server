package com.game.gm.handler;

import game.core.message.Message;
import game.core.message.RMessage;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.util.TimeUtils;
import game.message.GmMessage.GmCommandClientToServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * makehandler  v1.1
 * //Client->Game 由客户端发出的游戏内部GM命令
 */
@Message(id = GmCommandClientToServer.MsgID.eMsgID_VALUE, clazz = GmCommandClientToServer.class)
public class GmCommandClientToServerHandler extends Handler<GmCommandClientToServer> {

    static final Logger logger = LogManager.getLogger(GmCommandClientToServerHandler.class);

    @Override
    public void action(RMessage session, GmCommandClientToServer message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            if (player != null) {
                //manager.gmCommandManager.clientGmDeal(player, message.getCommand());
            } else {
                logger.error("游戏内部GM命令处理，未获取到玩家数据！");
            }

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
