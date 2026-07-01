package com.game.hook.log;

import com.game.player.structs.Player;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import game.core.dblog.bean.CommonLogBean;

/**
 * 离线挂机日志
 */
public class HookLog extends CommonLogBean {

    private long lastOfflineTime;       //上次离线时间
    private long onlineTime;            //上线时间
    private int lastLv;                 //离线时等级
    private int currentLv;              //当前等级
    private long addExp;                //增加经验值
    private String items;               //获得物品

    public void setPlayer(Player p) {
        this.setPlayerInfo(p.getPlatformName(), p.getCreateServerId(), p.getUserId(), p.getId(), p.getName());
    }

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }

    @Log(logField = "lastOfflineTime", fieldType = "bigint", index = "0")
    public long getLastOfflineTime() {
        return lastOfflineTime;
    }

    public void setLastOfflineTime(long lastOfflineTime) {
        this.lastOfflineTime = lastOfflineTime;
    }

    @Log(logField = "onlineTime", fieldType = "bigint", index = "0")
    public long getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(long onlineTime) {
        this.onlineTime = onlineTime;
    }

    @Log(logField = "lastLv", fieldType = "int", index = "0")
    public int getLastLv() {
        return lastLv;
    }

    public void setLastLv(int lastLv) {
        this.lastLv = lastLv;
    }

    @Log(logField = "currentLv", fieldType = "int", index = "0")
    public int getCurrentLv() {
        return currentLv;
    }

    public void setCurrentLv(int currentLv) {
        this.currentLv = currentLv;
    }

    @Log(logField = "addExp", fieldType = "bigint", index = "0")
    public long getAddExp() {
        return addExp;
    }

    public void setAddExp(long addExp) {
        this.addExp = addExp;
    }

    @Log(logField = "item", fieldType = "varchar(1024)", index = "0")
    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }
}
