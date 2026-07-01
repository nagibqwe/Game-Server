package com.game.title.log;

import com.game.player.structs.Player;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.CommonLogBean;

/**
 * 称号日志
 */
public class TitleChangeLog extends CommonLogBean {

    private int titleId;            //称号id
    private int operateType;        //0激活称号，1延长称号过期时间
    private String item;            //消耗的物品
    private int lastExpirationTime; //操作前称号过期时间，激活称号是-1
    private int nowExpirationTime;  //称号过期时间

    public void setPlayer(Player p) {
        this.setPlayerInfo(p.getPlatformName(), p.getCreateServerId(), p.getUserId(), p.getId(), p.getName());
    }

    @Log(logField = "titleId", fieldType = "int", index = "0")
    public int getTitleId() {
        return titleId;
    }

    public void setTitleId(int titleId) {
        this.titleId = titleId;
    }

    @Log(logField = "operateType", fieldType = "int", index = "0")
    public int getOperateType() {
        return operateType;
    }

    public void setOperateType(int operateType) {
        this.operateType = operateType;
    }

    @Log(logField = "item", fieldType = "varchar(255)", index = "0")
    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    @Log(logField = "lastExpirationTime", fieldType = "int", index = "0")
    public int getLastExpirationTime() {
        return lastExpirationTime;
    }

    public void setLastExpirationTime(int lastExpirationTime) {
        this.lastExpirationTime = lastExpirationTime;
    }

    @Log(logField = "nowExpirationTime", fieldType = "int", index = "0")
    public int getNowExpirationTime() {
        return nowExpirationTime;
    }

    public void setNowExpirationTime(int nowExpirationTime) {
        this.nowExpirationTime = nowExpirationTime;
    }

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }
}
