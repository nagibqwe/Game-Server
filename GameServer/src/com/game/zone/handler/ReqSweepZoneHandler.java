package com.game.zone.handler;

import com.data.CfgManager;
import com.data.bean.Cfg_Clone_map_Bean;
import com.data.bean.Cfg_Mapsetting_Bean;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.ZoneMessage.ReqSweepZone;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //扫荡副本
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqSweepZone.MsgID.eMsgID_VALUE, clazz = ReqSweepZone.class)

public class ReqSweepZoneHandler extends Handler<ReqSweepZone> {

    static final Logger log = LogManager.getLogger(ReqSweepZoneHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqSweepZone messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            if (null == player) {
                return;
            }
            if (messInfo.getModelId() < 1) {
                return;
            }

            Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(messInfo.getModelId());
            if (null == bean) {
                logger.error("Cfg_Clone_mapBean配置表不存在：" + messInfo.getModelId());
                return;
            }
            Cfg_Mapsetting_Bean mapBean = CfgManager.getCfg_Mapsetting_Container().getValueByKey(bean.getMapid());
            if (mapBean == null) {
                return;
            }

            Manager.scriptManager.GetScriptClass(mapBean.getIsscript()).call(player, "sweepCopy", bean, messInfo.getParam());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqSweepZoneHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
