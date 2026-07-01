/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.dblog.bean;

import game.core.dblog.base.Log;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 * 双方交互日志（适用集市、交易和邮件等）
 * @author Administrator
 */
public abstract class MultiLogBean extends BaseLogBean
{
    protected static final Logger log = LogManager.getLogger("MultiLogBean");
    
    protected String selfPlatform;//渠道名
    
    protected int selfCreateSid;//角色创建服务器ID
    
    protected long selfUserId;//用户ID
    
//    protected String selfFuncellUUID;//Funcell平台ID
    
    protected long selfRoleId;//角色ID
    
    protected String selfRoleName;//角色名称
    
    
    protected String otherPlatform;//渠道名
    
    protected int otherCreateSid;//角色创建服务器ID
    
    protected long otherUserId;//用户ID
    
//    protected String otherFuncellUUID;//Funcell平台ID
    
    protected long otherRoleId;//角色ID
    
    protected String otherRoleName;//角色名称
    
    public void setSelfPlayerInfo(String platform, int createSid, long userId, long roleId, String roleName)
    {
        this.selfPlatform = platform;
        this.selfCreateSid = createSid;
        this.selfUserId = userId;
        this.selfRoleId = roleId;
        this.selfRoleName = roleName;
    }
    
    public void setOtherPlayerInfo(String platform, int createSid, long userId, long roleId, String roleName)
    {
        this.otherPlatform = platform;
        this.otherCreateSid = createSid;
        this.otherUserId = userId;
        this.otherRoleId = roleId;
        this.otherRoleName = roleName;
    }

    @Log(logField = "selfPlatform", fieldType = "varchar(100)", index = "0")
    public String getSelfPlatform()
    {
        return selfPlatform;
    }

    public void setSelfPlatform(String selfPlatform)
    {
        this.selfPlatform = selfPlatform;
    }

    @Log(logField = "selfCreateSid", fieldType = "int", index = "2")
    public int getSelfCreateSid()
    {
        return selfCreateSid;
    }

    public void setSelfCreateSid(int selfCreateSid)
    {
        this.selfCreateSid = selfCreateSid;
    }

    @Log(logField = "selfUserId", fieldType = "bigint", index = "0")
    public long getSelfUserId()
    {
        return selfUserId;
    }

    public void setSelfUserId(long selfUserId)
    {
        this.selfUserId = selfUserId;
    }

    @Log(logField = "selfRoleId", fieldType = "bigint", index = "3")
    public long getSelfRoleId()
    {
        return selfRoleId;
    }

    public void setSelfRoleId(long selfRoleId)
    {
        this.selfRoleId = selfRoleId;
    }

    @Log(logField = "selfRoleName", fieldType = "varchar(100)", index = "0")
    public String getSelfRoleName()
    {
        return selfRoleName;
    }

    public void setSelfRoleName(String selfRoleName)
    {
        this.selfRoleName = selfRoleName;
    }
    
    @Log(logField = "otherPlatform", fieldType = "varchar(100)", index = "0")
    public String getOtherPlatform()
    {
        return otherPlatform;
    }

    public void setOtherPlatform(String otherPlatform)
    {
        this.otherPlatform = otherPlatform;
    }

    @Log(logField = "otherCreateSid", fieldType = "int", index = "2")
    public int getOtherCreateSid()
    {
        return otherCreateSid;
    }

    public void setOtherCreateSid(int otherCreateSid)
    {
        this.otherCreateSid = otherCreateSid;
    }

    @Log(logField = "otherUserId", fieldType = "bigint", index = "0")
    public long getOtherUserId()
    {
        return otherUserId;
    }

    public void setOtherUserId(long otherUserId)
    {
        this.otherUserId = otherUserId;
    }

    @Log(logField = "otherRoleId", fieldType = "bigint", index = "3")
    public long getOtherRoleId()
    {
        return otherRoleId;
    }

    public void setOtherRoleId(long otherRoleId)
    {
        this.otherRoleId = otherRoleId;
    }

    @Log(logField = "otherRoleName", fieldType = "varchar(100)", index = "0")
    public String getOtherRoleName()
    {
        return otherRoleName;
    }

    public void setOtherRoleName(String otherRoleName)
    {
        this.otherRoleName = otherRoleName;
    }
}
