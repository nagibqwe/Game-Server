/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.copymap.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 副本日志的进出记录
 */
public class CopyMapLogRecord extends BaseLogBean {

    private static final Logger log = LogManager.getLogger(CopyMapLogRecord.class);

    private long playerid;        //玩家ID
    private int zonemodelid;    //通关副本ID
    private String name;        //玩家名字
    private String platform;//平台玩家
    private int type;//扫荡类型 0手动，1自动，2连续扫荡 3多人副本
    private int sid;//
    private int isSuccess;//
    private long score;//获得积分
    private String reward;//当前所得奖励
    private String dcReward;//档次奖励
    private long actionId;//行为唯一ID
    private int copyOverTime;//副本完成时间
    private int star;//星级
    private long teamId;//组队ID值

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Override
    public void logToFile() {
        log.info(buildSql());
    }

    @Log(logField = "playerid", fieldType = "bigint", index = "2")
    public long getPlayerid() {
        return playerid;
    }

    public void setPlayerid(long playerid) {
        this.playerid = playerid;
    }

    @Log(logField = "name", fieldType = "varchar(200)", index = "0")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Log(logField = "platformName", fieldType = "varchar(64)", index = "0")
    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @Log(logField = "zonemodelid", fieldType = "int", index = "3")
    public int getZonemodelid() {
        return zonemodelid;
    }

    public void setZonemodelid(int zonemodelid) {
        this.zonemodelid = zonemodelid;
    }

    @Log(logField = "type", fieldType = "int", index = "0")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Log(logField = "sid", fieldType = "int", index = "4")
    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    @Log(logField = "isSuccess", fieldType = "int", index = "0")
    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }

    @Log(logField = "score", fieldType = "bigint", index = "0")
    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    @Log(logField = "reward", fieldType = "varchar(255)", index = "0")
    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    @Log(logField = "dcReward", fieldType = "varchar(400)", index = "0")
    public String getDcReward() {
        return dcReward;
    }

    public void setDcReward(String dcReward) {
        this.dcReward = dcReward;
    }

    @Log(logField = "actionId", fieldType = "bigint", index = "0")
    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }

    @Log(logField = "copyOverTime", fieldType = "int", index = "0")
    public int getCopyOverTime() {
        return copyOverTime;
    }

    public void setCopyOverTime(int copyOverTime) {
        this.copyOverTime = copyOverTime;
    }

    @Log(logField = "star", fieldType = "int", index = "0")
    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    @Log(logField = "teamId", fieldType = "bigint", index = "0")
    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

}
