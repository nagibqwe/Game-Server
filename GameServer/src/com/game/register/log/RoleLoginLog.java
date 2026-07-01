package com.game.register.log;

import com.game.player.structs.Player;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.CommonLogBean;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;

/**
 * 玩家登陆日志
 */
public class RoleLoginLog extends CommonLogBean {

    private static final Logger logger = LogManager.getLogger("RoleLoginLog");

    private int sex;                    //性别
    private String loginRoleIP;         //登陆IP
    private int level;                  //等级
    private int type;                   //0下线1上线
    private String funcellUUid;         //funcellUUid
    private String machineCode;         //机器码
    private String platUserName;        //平台账号名
    private String os;                  //玩家接口系统
    private int onlineTime;             //本次退出时的在线时间
    private int createTime;             //角色创建时间

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

    @Log(logField = "sex", fieldType = "TINYINT(4)", index = "0")
    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    @Log(logField = "loginRoleIP", fieldType = "varchar(64)", index = "0")
    public String getLoginRoleIP() {
        return loginRoleIP;
    }

    public void setLoginRoleIP(String loginRoleIP) {
        this.loginRoleIP = loginRoleIP;
    }

    @Log(logField = "level", fieldType = "INT(4)", index = "0")
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Log(logField = "type", fieldType = "TINYINT(4)", index = "0")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Log(logField = "funcellUUid", fieldType = "varchar(64)", index = "0")
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

    @Log(logField = "onlineTime", fieldType = "int", index = "0")
    public int getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(int onlineTime) {
        this.onlineTime = onlineTime;
    }

    @Log(logField = "createTime", fieldType = "int", index = "0")
    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

}
