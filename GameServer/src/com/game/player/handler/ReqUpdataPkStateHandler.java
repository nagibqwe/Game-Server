package com.game.player.handler;

import com.data.CfgManager;
import com.data.bean.Cfg_Mapsetting_Bean;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerDefine;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PlayerMessage.ReqUpdataPkState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //更改pk模式
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqUpdataPkState.MsgID.eMsgID_VALUE, clazz = ReqUpdataPkState.class)

public class ReqUpdataPkStateHandler extends Handler<ReqUpdataPkState> {

    static final Logger log = LogManager.getLogger(ReqUpdataPkStateHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqUpdataPkState messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();

            if (messInfo.getPkState() < 0 || messInfo.getPkState() > PlayerDefine.PkStateMax) {
                return;
            }

            MapObject map = Manager.mapManager.getMap(player.gainMapId());
            if (map == null) {
                return;
            }

            Cfg_Mapsetting_Bean bean = CfgManager.getCfg_Mapsetting_Container().getValueByKey(map.getMapModelId());
            if (bean == null) {
                return;
            }

            if (bean.getFight_change() == 0) {
                return;
            }

            Manager.playerManager.manager().onUpdatePkState(player, messInfo.getPkState(), true);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqUpdataPkStateHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
