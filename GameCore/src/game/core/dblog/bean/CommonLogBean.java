/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.dblog.bean;

import game.core.dblog.base.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 普通日志
 *
 * @author Administrator
 */
public abstract class CommonLogBean extends BaseLogBean
{
    protected static final Logger log = LogManager.getLogger("CommonLogBean");

    protected String platform;//渠道名

    protected int createSid;//角色创建服务器ID

    protected long userId;//用户ID

//    private String funcellUUID;//Funcell平台ID
    protected long roleId;//角色ID

    protected String roleName;//角色名称


    public void setPlayerInfo(String platform, int createSid, long userId, long roleId, String roleName)
    {
        this.platform = platform;
        this.createSid = createSid;
        this.userId = userId;
        this.roleId = roleId;
        this.roleName = roleName;
    }

    @Log(logField = "platformName", fieldType = "varchar(100)", index = "0")
    public String getPlatform()
    {
        return platform;
    }

    public void setPlatform(String platform)
    {
        this.platform = platform;
    }

    @Log(logField = "sid", fieldType = "int", index = "2")
    public int getCreateSid()
    {
        return createSid;
    }

    public void setCreateSid(int createSid)
    {
        this.createSid = createSid;
    }

    @Log(logField = "userId", fieldType = "bigint", index = "0")
    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }

//    public String getFuncellUUID()
//    {
//        return funcellUUID;
//    }
//
//    public void setFuncellUUID(String funcellUUID)
//    {
//        this.funcellUUID = funcellUUID;
//    }
    @Log(logField = "roleId", fieldType = "bigint", index = "3")
    public long getRoleId()
    {
        return roleId;
    }

    public void setRoleId(long roleId)
    {
        this.roleId = roleId;
    }

    @Log(logField = "roleName", fieldType = "varchar(100)", index = "0")
    public String getRoleName()
    {
        return roleName;
    }

    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }

    @Override
    public void logToFile()
    {
        log.error(buildSql());
    }

}
