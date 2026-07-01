package com.game.map.timer;

import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import game.core.timer.TimerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 地图循环事件timer（地图线程处理）
 **/
public class MapLoopScriptEventTimer extends TimerEvent {

    private final static Logger log = LogManager.getLogger(MapLoopScriptEventTimer.class);
    /**
     * 地图对象
     */
    private final MapObject mapObject;
    /**
     * 额外参数
     */
    private final Object[] params;
    /**
     * 地图唯一id
     */
    private final long mapId;
    /**
     * 脚本id，实现ITimerAction接口
     */
    private final int scriptId;

    /**
     * 脚本调用功能方法
     */
    private final String method;

    /**
     * 定时事件
     *
     * @param scriptId 脚本id，实现ITimerAction接口
     * @param delay 多久之后执行一次事件
     * @param mapObject 地图对象
     * @param params 额外参数
     */
    public MapLoopScriptEventTimer(int scriptId, String method, long delay, MapObject mapObject, Object... params) {
        super(delay);
        this.mapObject = mapObject;
        this.params = params;
        this.mapId = mapObject.getId();
        this.scriptId = scriptId;
        this.method = method;
    }

    /**
     * 循环设置
     *
     * @param scriptId 脚本id，实现ITimerAction接口
     * @param loop 循环次数 -1表示无限次
     * @param delay 多久之后执行第一次
     * @param period 间隔时间
     * @param mapObject 地图对象
     * @param params 额外参数
     */
    public MapLoopScriptEventTimer(int scriptId, String method, int loop, long delay, long period, MapObject mapObject, Object... params) {
        super(loop, delay, period);
        this.mapObject = mapObject;
        this.params = params;
        this.mapId = mapObject.getId();
        this.scriptId = scriptId;
        this.method = method;
    }

    @Override
    public void action() {
        try {
            if (mapId != mapObject.getId()) {
                delete();
                return;
            }

            if(mapObject.isStop()){
                return;
            }

            if (this.method != null && method.length() > 0) {
                Manager.mapManager.base(scriptId).action(mapObject, method, params);
            } else {
                Manager.mapManager.base(scriptId).action(mapObject, "other", params);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }

}
