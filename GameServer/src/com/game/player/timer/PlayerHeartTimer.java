package com.game.player.timer;

import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.player.structs.Player;
import game.core.timer.TimerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 玩家每秒心跳
 *
 * @author admin
 */
public class PlayerHeartTimer extends TimerEvent {

    private static final Logger log = LogManager.getLogger(PlayerHeartTimer.class);
    private final MapObject map;

    public PlayerHeartTimer(MapObject mapObject) {
        super(-1, 0,1000);
        this.map = mapObject;
    }

    public MapObject getMap() {
        return map;
    }

    @Override
    public void action() {
        try {
            Manager.heartManager.deal().PlayerHeartTimer(this);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

}
