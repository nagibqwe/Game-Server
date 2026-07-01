package com.game.heart.log;

import com.game.player.structs.Player;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.CommonLogBean;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;

public class RoleKickOutLog extends CommonLogBean {

    private static final Logger logger = LogManager.getLogger("RoleForbidLog");

    private long roleId;            //角色id
    private long userId;            //账号id
    private String name;            //角色名
    private int serverId;           //服务器id
    private int reason;             //玩家踢下线原因，0心跳过快，1没有心跳
    private String context;         //玩家发生时的文字记录进去，方便分析

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

    @Log(fieldType = "bigint", logField = "roleid", index = "0")
    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    @Log(fieldType = "bigint", logField = "userId", index = "0")
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Log(fieldType = "varchar(200)", logField = "name", index = "0")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Log(fieldType = "int", logField = "serverId", index = "0")
    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    @Log(fieldType = "int", logField = "reason", index = "0")
    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    @Log(fieldType = "varchar(600)", logField = "context", index = "0")
    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

}
