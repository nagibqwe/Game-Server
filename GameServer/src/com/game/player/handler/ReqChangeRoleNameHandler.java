package com.game.player.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PlayerMessage.ReqChangeRoleName;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求角色改名
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqChangeRoleName.MsgID.eMsgID_VALUE, clazz = ReqChangeRoleName.class)

public class ReqChangeRoleNameHandler extends Handler<ReqChangeRoleName> {

    static final Logger log = LogManager.getLogger(ReqChangeRoleNameHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqChangeRoleName messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            if (player != null) {
                log.info("玩家" + player.getName() + "使用角色改名卡进行角色改名，newName=" + messInfo.getNewName());
                Manager.playerManager.deal(ScriptEnum.ChangeRoleNameBaseScript).changeName(player, messInfo.getNewName());
            } else {
                log.error("未获取到玩家数据！");
            }

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqChangeRoleNameHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
