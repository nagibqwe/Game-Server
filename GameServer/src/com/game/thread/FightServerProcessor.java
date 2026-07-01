/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.thread;

import com.game.server.GameServer;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.server.impl.MapServer;
import game.core.message.RMessage;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;

/**
 * <b>命令分发线程</b>
 *
 * @author ChenLong
 */
public class FightServerProcessor {

    private static final Logger log = LogManager.getLogger(FightServerProcessor.class);

    private FightServerProcessor() {
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        FightServerProcessor processor;

        Singleton() {
            this.processor = new FightServerProcessor();
        }

        FightServerProcessor getProcessor() {
            return processor;
        }
    }

    /**
     * 获取DispatchProcessor的实例对象.
     *
     * @return
     */
    public static FightServerProcessor getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    /**
     * 开始接收网络过来的消息，进行分发处理
     *
     * @param mess 消息协议
     */
    public void doAction(RMessage mess) {
        try {
            if (null == mess) {
                return;
            }
            int msgid = mess.getId();
//            int sourceId = MessageNumber.getSource(msgid);
            long roleId = mess.getSrcId();

            //如果是服务器的其它信息
            if (roleId < 1) {
                GameServer.getInstance().getMainThread().addCommand(mess);
                return;
            }

//            int functionID = MessageNumber.getFunction(msgid);
            Player player = Manager.playerManager.getPlayerCache(roleId);
            if (player == null) {
                GameServer.getInstance().getMainThread().addCommand(mess);
                return;
            }

//            handler.setAttribute(Handler.PLAYER, player);
            mess.setExecutor(player);
            //获取玩家所在线程
            long mapId = player.gainMapId();
            int line = player.gainLine();
            MapServer map = GameServer.getInstance().getMServer(mapId);
            if (map == null) {
                GameServer.getInstance().getMainThread().addCommand(mess);
                log.error("消息ID:" + msgid + " , player =" + player + " 已经找不到可执行的线程了，地图：" + mapId + " line :" + line);
                return;
            }

//            if (Global.isOutNetMess) {
//            LOGGER.error(player.nameIdString() + "消息：" + mess.getId() + ", orderId=" + mess.getOrder() + ",t=" + TimeUtils.Time());
//            }

            map.addCommand(mess);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

}
