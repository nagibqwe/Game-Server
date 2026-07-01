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
 * 红包创建的日志
 *
 * @author xuchangming <xysoko@qq.com>
 */
public class RedPacketCreateLog extends CommonLogBean {

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
    private int maxvalue;
    private int kouvalue;
    private int maxNum;
    private int type;
    private int goldType;
    private int groupId;
    private long guildId;
    private String notice;
    private String rpvalues;

    @Log(fieldType = "bigint", index = "0", logField = "rpId")
    public long getRpId() {
        return rpId;
    }

    public void setRpId(long rpId) {
        this.rpId = rpId;
    }

    @Log(fieldType = "int", index = "0", logField = "kouvalue")
    public int getKouvalue() {
        return kouvalue;
    }

    public void setKouvalue(int kouvalue) {
        this.kouvalue = kouvalue;
    }

    @Log(fieldType = "int", index = "0", logField = "rpmaxvalue")
    public int getMaxvalue() {
        return maxvalue;
    }

    public void setMaxvalue(int maxvalue) {
        this.maxvalue = maxvalue;
    }

    @Log(fieldType = "int", index = "0", logField = "rpmaxNum")
    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    @Log(fieldType = "int", index = "0", logField = "rptype")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Log(fieldType = "int", index = "0", logField = "goldType")
    public int getGoldType() {
        return goldType;
    }

    public void setGoldType(int goldType) {
        this.goldType = goldType;
    }

    @Log(fieldType = "int", index = "0", logField = "groupId")
    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    @Log(fieldType = "bigint", index = "0", logField = "guildId")
    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long guildId) {
        this.guildId = guildId;
    }

    @Log(fieldType = "varchar(200)", index = "0", logField = "rpnotice")
    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    @Log(fieldType = "text", index = "0", logField = "rpvalues")
    public String getRpvalues() {
        return rpvalues;
    }

    public void setRpvalues(String rpvalues) {
        this.rpvalues = rpvalues;
    }

}
