package com.game.register.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 * 玩家删除恢复日志
 */
public class RoleDoLog extends BaseLogBean {

    private static final Logger logger = LogManager.getLogger("RoleDoLog");

    private long roleId;        //角色ID
    private String roleName;    //角色名
    private long userId;        //用户ID
    private int career;         //职业
    private int serverId;       //服务器ID
    private int lv;             //等级
    private int type;           //0表示删除，1表示恢复角色

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.UNROLL;
    }

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }

    @Log(logField = "roleId", fieldType = "BIGINT(20)", index = "0")
    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    @Log(logField = "roleName", fieldType = "varchar(200)", index = "0")
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Log(logField = "userId", fieldType = "BIGINT(20)", index = "0")
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Log(logField = "career", fieldType = "TINYINT(4)", index = "0")
    public int getCareer() {
        return career;
    }

    public void setCareer(int career) {
        this.career = career;
    }

    @Log(logField = "serverId", fieldType = "INT(4)", index = "0")
    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    @Log(logField = "lv", fieldType = "INT(4)", index = "0")
    public int getLv() {
        return lv;
    }

    public void setLv(int lv) {
        this.lv = lv;
    }
    
    @Log(logField = "type", fieldType = "TINYINT(4)", index = "0")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
