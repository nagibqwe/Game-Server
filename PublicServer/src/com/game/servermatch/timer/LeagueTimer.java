/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.servermatch.timer;

import com.game.servermatch.manager.ServerMatchManager;
import game.core.timer.TimerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 联赛时间相关处理
 * @author zhaibiao
 */
public class LeagueTimer extends TimerEvent{

    private static final Logger LOG = LogManager.getLogger("LeagueTimer");
    
    public LeagueTimer() {
        super(-1, 0,1000);
    }

//    指定开启时间，后面逻辑不做执行时间判断，需要保存执行时间变量
    @Override
    public void action() {
        if (ServerMatchManager.isLoad) {
            ServerMatchManager.timeAxis.tick();
        }
    }
    
}
