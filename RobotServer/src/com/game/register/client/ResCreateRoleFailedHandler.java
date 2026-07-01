package com.game.register.client;

import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import game.core.command.Handler;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.RegisterMessage.ResCreateRoleFailed;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.session.IoSession;


/**
* makehandler  v1.5
*创建角色失败
*/
public class ResCreateRoleFailedHandler extends Handler{

    private static final Logger log = LogManager.getLogger(ResCreateRoleFailedHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            ResCreateRoleFailed messInfo = (ResCreateRoleFailed) mess.getData();
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            if(messInfo.getReason() == 0){
                log.trace(String.format("player:%d 创建角色成功", player.getUserId()));
            }
            else{
                log.trace(String.format("player:%d 创建角色失败,失败原因:%d", player.getUserId(), messInfo.getReason()));
            }
            player.setCreateRoleRet(messInfo.getReason());
            //Manager.playerManager.SendcreatePlayerMsg(player);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResCreateRoleFailedHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e,e);
        }
    }

}