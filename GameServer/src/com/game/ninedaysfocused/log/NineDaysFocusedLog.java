/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.ninedaysfocused.log;

import com.game.player.structs.Player;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.CommonLogBean;

/**
 *
 * 九天争锋
 * @author ClC <xysoko@qq.com>
 */
public class NineDaysFocusedLog extends CommonLogBean {

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Override
    public void logToFile() {
        log.error(buildSql());
    }

    public void setPlayer(Player p) {
        this.setPlayerInfo(p.getPlatformName(), p.getCreateServerId(), p.getUserId(), p.getId(), p.getName());
    }

    private int serverId;
    private int subNum;
    private String rewards;
    private long cloneId;
    private int cloneModelId;

    @Log(fieldType = "int", index = "0", logField = "serverId")
    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    @Log(fieldType = "int", index = "0", logField = "subNum")
    public int getSubNum() {
        return subNum;
    }

    public void setSubNum(int subNum) {
        this.subNum = subNum;
    }

    @Log(fieldType = "varchar(200)", index = "0", logField = "rewards")
    public String getRewards() {
        return rewards;
    }

    public void setRewards(String rewards) {
        this.rewards = rewards;
    }

    @Log(fieldType = "bigint", index = "0", logField = "cloneId")
    public long getCloneId() {
        return cloneId;
    }

    public void setCloneId(long cloneId) {
        this.cloneId = cloneId;
    }

    @Log(fieldType = "int", index = "0", logField = "cloneModelId")
    public int getCloneModelId() {
        return cloneModelId;
    }

    public void setCloneModelId(int cloneModelId) {
        this.cloneModelId = cloneModelId;
    }

}
