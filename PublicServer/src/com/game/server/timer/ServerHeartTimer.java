/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.server.timer;

import com.game.count.structs.CountVariant;
import com.game.manager.Manager;
import com.game.peak.timer.PeakZeroTickEvent;
import com.game.server.MainServer;
import com.game.server.filter.InnerMsgImpl;
import com.game.utils.ServerParamUtil;
import game.core.timer.TimerEvent;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 系统心跳
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class ServerHeartTimer extends TimerEvent {

    private static final Logger LOG = LogManager.getLogger("ServerHeartTimer");

    public ServerHeartTimer() {
        super(-1, 0, 333);//1秒3帧的执行次数
    }

    @Override
    public void action() {
        long now = TimeUtils.Time();
        //处理业务逻辑，交给脚本去处理扩展业务
        try {
            MainServer.getInstance().deal().tick(now);
        } catch (Exception ex) {
            LOG.error(ex, ex);
        }
        try {
            //处理房间计时规则
            Manager.fightManager.deal().OnTick(now);
        } catch (Exception ex) {
            LOG.error(ex, ex);
        }
        try {
            //副本的逻辑管理计时规则
            Manager.zoneManager.manager().OnTick(now);
        } catch (Exception ex) {
            LOG.error(ex, ex);
        }
        try {
            //九天争锋
            Manager.nineDaysFocusedManager.deal().tick(now);
        } catch (Exception ex) {
            LOG.error(ex, ex);
        }
        try {
            //仙侣对决
            Manager.couplefightManager.getScript().OnTick(now);
        } catch (Exception ex) {
            LOG.error(ex, ex);
        }

        if (!TimeUtils.isSameDay(Manager.zoneManager.getBravePeakInfo().getLastModifyTime(), now)) {
            Manager.zoneManager.getBravePeakInfo().clear();
            Manager.zoneManager.getBravePeakInfo().setLastModifyTime(now);
            Manager.zoneManager.addBravePeakData();
        }

        //跨服0点刷新
        if (Manager.countManager.getVariant(() -> ServerParamUtil.counts, CountVariant.ZeroClock) == 0) {
            Manager.countManager.setVariant(() -> ServerParamUtil.counts, CountVariant.ZeroClock, 1);
            InnerMsgImpl.getInstance().addCommand(new PeakZeroTickEvent());
        }
        //跨服5点刷新
        if (Manager.countManager.getVariant(() -> ServerParamUtil.counts, CountVariant.FiveClock) == 0) {
            Manager.countManager.setVariant(() -> ServerParamUtil.counts, CountVariant.FiveClock, 1);
        }

    }

}
