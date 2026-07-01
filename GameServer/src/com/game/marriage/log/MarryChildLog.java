package com.game.marriage.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;

/**
 * @Desc TODO
 * @Date 2020/8/14 18:31
 * @Auth ZUncle
 */
public class MarryChildLog extends BaseLogBean {

    long playerId;      //玩家ID
    int childId;        //仙娃ID
    String name;
    int level;
    int exp;
    int opt;            //1=解锁, 2=强化 3=改名

    @Log(fieldType = "bigint", logField = "playerId", index = "1")
    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    @Log(fieldType = "int", logField = "childId", index = "2")
    public int getChildId() {
        return childId;
    }

    public void setChildId(int childId) {
        this.childId = childId;
    }

    @Log(fieldType = "varchar(30)", logField = "name", index = "0")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Log(fieldType = "int", logField = "level", index = "0")
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Log(fieldType = "int", logField = "exp", index = "0")
    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    @Log(fieldType = "int", logField = "opt", index = "0")
    public int getOpt() {
        return opt;
    }

    public void setOpt(int opt) {
        this.opt = opt;
    }

    /**
     * 日志多长时间建一次表
     *
     * @return
     */
    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }
}
