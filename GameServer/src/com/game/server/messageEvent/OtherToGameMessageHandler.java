/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.server.messageEvent;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.server.GameServer;
import com.game.server.impl.MapServer;
import com.game.thread.InnerServerProcessor;
import com.game.utils.MessageUtils;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import game.core.disruptor.CommonQueue;
import game.core.disruptor.DisruptorOrderPoolExecutor;
import game.core.disruptor.TaskEvent;
import game.core.message.RMessage;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author xuchangming <xysoko@qq.com>
 */
public class OtherToGameMessageHandler implements WorkHandler<TaskEvent<OtherToGameMessage>>, EventHandler<TaskEvent<OtherToGameMessage>> {

    private final static Logger logger = LogManager.getLogger(RMessageWorkHandler.class);
    private DisruptorOrderPoolExecutor<TaskEvent<OtherToGameMessage>, Long, OtherToGameMessage> disruptorPool;

    public void setDisruptorPool(DisruptorOrderPoolExecutor<TaskEvent<OtherToGameMessage>, Long, OtherToGameMessage> disruptorPool) {
        this.disruptorPool = disruptorPool;
    }

    @Override
    public void onEvent(TaskEvent<OtherToGameMessage> t, long l, boolean bln) throws Exception {
        onEvent(t);
    }

    @Override
    public void onEvent(TaskEvent<OtherToGameMessage> t) throws Exception {
//        logger.error("处理 A =" + t.getType() + ", B =" + t.getObj());
        OtherToGameMessage otm = t.getObj();
        try {
            int type = t.getObj().getType();
            if (type == 1) {
                dealServerhandler(t.getObj());
            } else if (type == 2) {
                dealPlayerHandler(t.getObj());
            } else {
                logger.error("错误的消息队列处理");
            }
        } catch (Exception e) {
            logger.error(e, e);
        } finally {
            t.setObj(null);
            t.setType(0);
        }
        if (null == disruptorPool) {
            return;
        }

        afterEvent(otm);
    }

    private void dealServerhandler(OtherToGameMessage obj) {
        RMessage buf = obj.getMsg();
        // 由命令分发器来负责分派线程去执行handler
        if (buf.getSrcId() > 0) {
            Player player = Manager.playerManager.getPlayerOnline(buf.getSrcId());
            if (player != null) {
                //获取玩家所在线程
                long mapId = player.gainMapId();
                MapServer map = GameServer.getInstance().getMServer(mapId);
                if (map != null) {
                    buf.setExecutor(player);
                    map.addCommand(buf);
                    return;
                }
            }
        }
        InnerServerProcessor.getInstance().addCommand(buf);
    }

    private void dealPlayerHandler(OtherToGameMessage obj) {
        List<Long> roles = obj.getRoleIds();
        //loger.error("收到 id=" + msg.getId() + ", rsize=" + msg.getRoleIds().size() + ", serId=" + msg.getSendId());
        //转发给用户
        if (roles.isEmpty()) {
            MessageUtils.send_to_all_player(obj.getMsgId(), obj.getData());
        } else {
            //个别发送
            for (long roleId : roles) {
                MessageUtils.send_to_player(roleId, obj.getMsgId(), obj.getData());
            }
        }
    }

    private void afterEvent(OtherToGameMessage obj) {

        try {
            CommonQueue<OtherToGameMessage> queue = this.disruptorPool.getTasksQueue(obj.getFromId());
            if (queue == null) {
                return;
            }

            OtherToGameMessage sm = queue.getNewValue();
            if (sm == null) {
                return;
            }
//            logger.info("处理消息ID=" + sm.getId());
            //压入新的队列
            this.disruptorPool.publishNext(sm.getFromId(), sm);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
