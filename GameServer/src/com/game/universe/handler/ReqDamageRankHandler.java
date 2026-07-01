package com.game.universe.handler;

import com.game.connectfightserver.manager.ConnectFightManager;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.server.GameServer;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MSG_UniverseMessage.ReqDamageRank;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求伤害排行
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqDamageRank.MsgID.eMsgID_VALUE, clazz = ReqDamageRank.class)

public class ReqDamageRankHandler extends Handler<ReqDamageRank> {

    static final Logger log = LogManager.getLogger(ReqDamageRankHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqDamageRank messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            if (player == null) {
                return;
            }
            if(GameServer.getInstance().IsFightServer()){
                Manager.universeManager.deal().onReqDamageRank(player, messInfo);
            }else{
                ConnectFightManager.GetInstance().send_to_fight(player.playerCrossData.toFightSid,player.getId(),mess);
            }

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqDamageRankHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
