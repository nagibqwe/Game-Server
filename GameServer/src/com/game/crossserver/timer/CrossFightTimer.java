/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.crossserver.timer;

import com.game.manager.Manager;
import game.core.timer.TimerEvent;
import game.core.util.TimeUtils;

/**
 * 跨服的事件计时器
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class CrossFightTimer extends TimerEvent {

    public CrossFightTimer() {
        super(-1, 0, 20 * 1000);//30秒一次
    }

    @Override
    public void action() {
        Manager.crossServerManager.getCrossServer().OnCrossHeartTick(System.currentTimeMillis());
    }

}
