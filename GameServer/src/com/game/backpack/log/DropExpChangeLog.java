package com.game.backpack.log;

import com.game.player.structs.Player;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import game.core.dblog.bean.CommonLogBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 掉落经验变化日志
 */
public class DropExpChangeLog extends CommonLogBean {

    protected static final Logger log = LogManager.getLogger("DropExpChangeLog");

    private int lastTime;       //上一个时间点
    private long changeNum;     //变化值
    private int mapId;          //地图ID值

    public void setPlayer(Player p) {
        this.setPlayerInfo(p.getPlatformName(), p.getCreateServerId(), p.getUserId(), p.getId(), p.getName());
    }

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Override
    public void logToFile() {
        log.error(buildSql());
    }

    @Log(logField = "mapId", fieldType = "int", index = "0")
    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

    @Log(logField = "lastTime", fieldType = "int", index = "0")
    public int getLastTime() {
        return lastTime;
    }

    public void setLastTime(int rolelevel) {
        this.lastTime = rolelevel;
    }

    @Log(logField = "changeNum", fieldType = "bigint", index = "0")
    public long getChangeNum() {
        return changeNum;
    }

    public void setChangeNum(long changeNum) {
        this.changeNum = changeNum;
    }
}
