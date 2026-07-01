package com.game.register.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 * 角色创建日志
 */
public class RoleCreateLog extends BaseLogBean {

    private static final Logger logger = LogManager.getLogger("CreateRoleLog");

    private long roleId;                //玩家ID
    private String roleName;            //玩家名字
    private long userId;                //用户ID
    private String platformName;        //平台名
    private int career;                 //职业
    private String createRoleIP;        //登陆IP
    private int serverId;               //服务器ID
    private String funcellUUid;         //funcellUUid
    private String machineCode;         //机器码
    private String platUserName;        //平台用户名
    private String os;                  //玩家接口系统
    private String clientVer;           //客户端版本

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.YEAR;
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

    @Log(logField = "platformName", fieldType = "varchar(64)", index = "0")
    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    @Log(logField = "career", fieldType = "TINYINT(4)", index = "0")
    public int getCareer() {
        return career;
    }

    public void setCareer(int career) {
        this.career = career;
    }

    @Log(logField = "createRoleIP", fieldType = "varchar(64)", index = "0")
    public String getCreateRoleIP() {
        return createRoleIP;
    }

    public void setCreateRoleIP(String createRoleIP) {
        this.createRoleIP = createRoleIP;
    }

    @Log(logField = "serverId", fieldType = "INT(4)", index = "0")
    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    @Log(logField = "funcellUUid", fieldType = "varchar(70)", index = "0")
    public String getFuncellUUid() {
        return funcellUUid;
    }

    public void setFuncellUUid(String funcellUUid) {
        this.funcellUUid = funcellUUid;
    }

    @Log(logField = "machineCode", fieldType = "varchar(70)", index = "0")
    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    @Log(logField = "platUserName", fieldType = "varchar(128)", index = "0")
    public String getPlatUserName() {
        return platUserName;
    }

    public void setPlatUserName(String platUserName) {
        this.platUserName = platUserName;
    }

    @Log(logField = "os", fieldType = "varchar(28)", index = "0")
    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    @Log(logField = "clientVer", fieldType = "varchar(50)", index = "0")
    public String getClientVer() { return clientVer;}

    public void setClientVer(String clientVer) { this.clientVer = clientVer; }

}
