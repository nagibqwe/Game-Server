/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.soulanimalforest.timer;

import com.game.manager.Manager;
import game.core.timer.TimerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 魂兽森林的计时器
 *
 * @author xuchangming <xysoko@qq.com>
 */
public class SoulAnimalForestBossBirthTimer extends TimerEvent {

    private static final Logger LOG = LogManager.getLogger("SoulAnimalForestBossBirthTimer");

    public SoulAnimalForestBossBirthTimer() {
        super(-1, 0,1000);
    }

    @Override
    public void action() {
        try {
            Manager.soulAnimalForestManager.manager().tickBossData();
        } catch (Exception e) {
            LOG.error("魂兽森林的计时器出异常了！", e);
        }
    }

}
