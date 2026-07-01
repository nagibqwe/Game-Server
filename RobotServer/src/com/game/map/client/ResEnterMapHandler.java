package com.game.map.client;

import com.data.CfgManager;
import com.data.bean.Cfg_Mapsetting_Bean;
import com.game.manager.Manager;
import com.game.map.structs.MapDefine;
import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import game.core.command.Handler;
import game.core.message.RMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import game.core.util.TimeUtils;
import game.message.MapMessage.ResEnterMap;
import org.apache.mina.core.session.IoSession;

/**
 * makehandler v1.5 以下协议 gameserver ->
 * client//////////////////////////////////////////////////////////////////////////////////////////进入地图返回
 */
public class ResEnterMapHandler extends Handler {

    private final Logger log = LogManager.getLogger(ResEnterMapHandler.class);
    private final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            ResEnterMap messInfo = (ResEnterMap) mess.getData();
            if (messInfo.getResult() != 0) {
                return;
            }
            if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                return;
            }
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            if(messInfo.getResult() < 0){
                log.info(player.getInfo() + " 进入地图：" + messInfo.getMapDataID() + " ,失败 ：" + messInfo.getResult());
                return;
            }
            player.getCurPos().setX(messInfo.getPos().getX());
            player.getCurPos().setY(messInfo.getPos().getY());
            player.setMapModelId(messInfo.getMapDataID());
//            Cfg_Mapsetting_Bean bean = CfgManager.getCfg_Mapsetting_Container().getValueByKey(messInfo.getMapDataID());
//            //已经不在副本了
//            if (bean.getType() != MapDefine.CopyMap) {
//                if (player.isCloneState()) {
//                    player.setCloneState(false);
//                    player.closeCloseState();
//                }
//            }

            log.info("ResEnterMap>" + player.getInfo() + " 进入地图：" + player.getMapModelId() + " ,坐标 ：" + player.getCurPos());
            player.waitDoTime(2000);//等待2秒再去吧
            //客户端准备完毕，请求加载完成
            Manager.registerManager.deal().reqLoadFinish(player);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResEnterMapHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e, e);
        }
    }

}
