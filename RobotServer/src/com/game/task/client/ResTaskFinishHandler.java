package com.game.task.client;
import com.game.manager.Manager;
import game.core.command.Handler;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 
import game.core.util.TimeUtils;
import game.message.taskMessage.ResTaskFinish;
import org.apache.mina.core.session.IoSession;
import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import game.core.message.RMessage;


/**
* makehandler  v1.7 for netty
*完成任务
*/
public class ResTaskFinishHandler extends Handler{

    private static final Logger log = LogManager.getLogger(ResTaskFinishHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            ResTaskFinish messInfo = (ResTaskFinish) mess.getData();
            if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                return;
            }
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            if (player == null) {
                return;
            }
            Manager.taskManager.mainTask().mainTaskFinish(player,messInfo);
            log.info("ResTaskFinish>"+player.getInfo()+"完成任务ID=" + messInfo.getModelId());
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResTaskFinishHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e,e);
        }
    }

}