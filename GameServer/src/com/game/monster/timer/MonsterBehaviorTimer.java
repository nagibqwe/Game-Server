package com.game.monster.timer;

import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import game.core.timer.TimerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author soko <xuchangming@haowan123.com>
 */
public class MonsterBehaviorTimer extends TimerEvent {

    static final Logger log = LogManager.getLogger(MonsterBehaviorTimer.class);
    private final MapObject map;
    private long beginTime = 0;

    public MonsterBehaviorTimer(MapObject mm) {
        super(-1, 0, 150);
        this.map = mm;
        beginTime = System.currentTimeMillis();//如果有改时间也不能改变此值，因为需要精确的时间运转毫秒值
    }

    @Override
    public void action() {
        long now = System.currentTimeMillis();
        long offset = now - beginTime;
        beginTime = now;
        try {
            Manager.monsterManager.ai().behavior(map, offset);
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
