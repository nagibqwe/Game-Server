package com.game.map.timer;

import com.game.map.structs.MapObject;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import com.game.structs.IActionScript;
import game.core.script.IScript;
import game.core.timer.TimerEvent;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author lw
 * @doc 地图心跳定时器
 */
public class MapHeartTimer extends TimerEvent {

    protected Logger log = LogManager.getLogger(MapHeartTimer.class);

    private final MapObject map;

    public MapHeartTimer(MapObject mp) {
        super(-1, 0,333);
        this.map = mp;
    }

    @Override
    public void action() {
        long beginTime = TimeUtils.Time();

        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.MapCleanerBaseScript);
        IActionScript ias = (IActionScript) is;
        ias.action(map, 333);

        long dealtime = TimeUtils.Time() - beginTime;
        if (dealtime > 300) {
            log.error("mapID" + map.getMapModelId() + "MonsterAiTimer use time:" + dealtime);
        }
    }
}
