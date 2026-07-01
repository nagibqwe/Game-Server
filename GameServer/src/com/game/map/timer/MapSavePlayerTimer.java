package com.game.map.timer;

import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.player.structs.Player;
import game.core.timer.TimerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MapSavePlayerTimer extends TimerEvent {

    protected Logger log = LogManager.getLogger(MapSavePlayerTimer.class);

    private final MapObject map;

    public MapSavePlayerTimer(MapObject m) {
        super(-1, 0,500);
        this.map = m;
    }

    @Override
    public void action() {
        for (Player p : map.getPlayers().values()) {
            Manager.playerManager.manager().TickSavePlayer(p);
        }
    }
}
