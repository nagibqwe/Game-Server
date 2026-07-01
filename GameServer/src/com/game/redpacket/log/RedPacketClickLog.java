/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.redpacket.log;

import com.game.player.structs.Player;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.CommonLogBean;

/**
 * 获取日志实现
 *
 * @author xuchangming <xysoko@qq.com>
 */
public class RedPacketClickLog extends CommonLogBean {

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

    private long rpId;
    private long rpRoleId;
    private int value;

    @Log(fieldType = "bigint", index = "", logField = "rpId")
    public long getRpId() {
        return rpId;
    }

    public void setRpId(long rpId) {
        this.rpId = rpId;
    }

    @Log(fieldType = "bigint", index = "", logField = "rpRoleId")
    public long getRpRoleId() {
        return rpRoleId;
    }

    public void setRpRoleId(long rpRoleId) {
        this.rpRoleId = rpRoleId;
    }

    @Log(fieldType = "int", index = "", logField = "value")
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
