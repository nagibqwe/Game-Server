package com.game.map.timer;

import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import game.core.timer.TimerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author lw
 * @doc 销毁普通无人地图(5分钟),副本地图回收
 */
public class MapDelTimer extends TimerEvent {

    protected Logger log = LogManager.getLogger(MapDelTimer.class);

    public MapDelTimer() {
        super(-1, 0,60 * 1000);
    }

    @Override
    public void action() {
        try {
            Manager.scriptManager.GetScriptClass(ScriptEnum.MapDelBaseScript).call();
        } catch (Exception ex) {
            log.error(ex, ex);
        }
    }
}
