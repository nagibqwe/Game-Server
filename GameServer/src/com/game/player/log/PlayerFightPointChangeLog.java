/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.player.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;

/**
 *
 * @author xuchangming <xysoko@qq.com>
 */
public class PlayerFightPointChangeLog extends BaseLogBean {

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }

    private long roleId;

    private long oldPower;

    private long newPower;

    private long oldBuffFight;

    private long newBuffFight;

    private int type;

    @Log(logField = "roleId", fieldType = "bigint", index = "0")
    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    @Log(logField = "oldPower", fieldType = "bigint", index = "0")
    public long getOldPower() {
        return oldPower;
    }

    public void setOldPower(long oldPower) {
        this.oldPower = oldPower;
    }

    @Log(logField = "newPower", fieldType = "bigint", index = "0")
    public long getNewPower() {
        return newPower;
    }

    public void setNewPower(long newPower) {
        this.newPower = newPower;
    }

    @Log(logField = "oldBuffFight", fieldType = "bigint", index = "0")
    public long getOldBuffFight() {
        return oldBuffFight;
    }

    public void setOldBuffFight(long oldBuffFight) {
        this.oldBuffFight = oldBuffFight;
    }

    @Log(logField = "newBuffFight", fieldType = "bigint", index = "0")
    public long getNewBuffFight() {
        return newBuffFight;
    }

    public void setNewBuffFight(long newBuffFight) {
        this.newBuffFight = newBuffFight;
    }

    @Log(logField = "operType", fieldType = "int", index = "0")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
