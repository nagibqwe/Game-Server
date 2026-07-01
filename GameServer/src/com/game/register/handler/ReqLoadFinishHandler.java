package com.game.register.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.QuitGameDefine;
import com.game.server.GameServer;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.RegisterMessage.ReqLoadFinish;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;


/**
* @Desc //加载完成
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqLoadFinish.MsgID.eMsgID_VALUE, clazz = ReqLoadFinish.class)

public class ReqLoadFinishHandler extends Handler<ReqLoadFinish> {

    static final Logger log = LogManager.getLogger(ReqLoadFinishHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqLoadFinish messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.playerManager.loadScript().OnReqLoadFinish(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqLoadFinishHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            GameServer.getInstance().getErrorLogThread().pushErrorExcptionLog("ReqLoadFinishHandler" + e.getMessage(), Arrays.toString(e.getStackTrace()));
            log.error(e, e);
            Manager.playerManager.iQuitGame().QuitGame(mess.getContext(), QuitGameDefine.PlayerLoginTick, false, true);
        }
    }
}
