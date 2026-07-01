package com.backend.struct.log.entity.register;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.CommonLogBean;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;

/**
 * 登录登出日志
 */
@Table(name = "roleloginlog", tableType = TableType.Month)
public class RoleLoginLog extends CommonLogBean implements IConvertor {

    @FieldDesc(show = false)
    private int id;

    @FieldDesc(show = false)
    private String machineCode;

    @FieldDesc(show = false)
    private int serverId;

    @FieldDesc(show = false)
    private int sex;

    @FieldDesc
    private long onlineTime;

    @FieldDesc
    private String loginRoleIP;

    @FieldDesc(show = false)
    private String os;

    @FieldDesc(show = false)
    private String roleName;

    @FieldDesc
    private int type;

    @FieldDesc
    private int level;

    @FieldDesc(show = false)
    private String platUserName;

    @FieldDesc(show = false)
    private String funcellUUid;

    @FieldDesc(show = false)
    private long createTime;

    @FieldDesc
    private String platformName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public long getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(long onlineTime) {
        this.onlineTime = onlineTime;
    }

    public String getLoginRoleIP() {
        return loginRoleIP;
    }

    public void setLoginRoleIP(String loginRoleIP) {
        this.loginRoleIP = loginRoleIP;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getPlatUserName() {
        return platUserName;
    }

    public void setPlatUserName(String platUserName) {
        this.platUserName = platUserName;
    }

    public String getFuncellUUid() {
        return funcellUUid;
    }

    public void setFuncellUUid(String funcellUUid) {
        this.funcellUUid = funcellUUid;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        data.put("type", "1".equals(data.get("type"))  ? "登入" : "登出");
        return data;
    }
}
