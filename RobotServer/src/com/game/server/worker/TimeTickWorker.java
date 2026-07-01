/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.server.worker;

import com.game.player.structs.Player;
import game.core.concurrent.AbstractWork;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author xuchangming <xysoko@qq.com>
 */
public class TimeTickWorker extends AbstractWork {

    private static final Logger log = LogManager.getLogger(TimeTickWorker.class);

    private final long curtime;
    private final Player player;

    public TimeTickWorker(long curtime, Player player) {
        this.curtime = curtime;
        this.player = player;
    }

    @Override
    public String getKey() {
        return player.getUserId() + "";
    }

    @Override
    public void run() {
        if (player == null) {
            return;
        }

        try {
            player.sendHeartToServer(curtime);
           // player.timeTick(curtime);
        } catch (Exception e) {
            log.info(e, e);
        }
    }

}
