package com.gm.project.gmtool.db.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * 日志库列对象 t_db
 * 
 * @author gm
 * @date 2021-09-08
 */
public class TDb extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Integer id;

    /** 服务器名称 */
    @Excel(name = "服务器名称")
    private String serverName;

    /** 类型1:日志库 */
    @Excel(name = "类型1:日志库")
    private Integer type;

    /** 服务器ID */
    @Excel(name = "服务器ID")
    private Integer serverId;

    /** 平台名 */
    @Excel(name = "平台名")
    private String groupName;

    /** 数据库IP */
    @Excel(name = "数据库IP")
    private String dbIp;

    /** 数据库Port */
    @Excel(name = "数据库Port")
    private Integer dbPort;

    /** 数据库名称 */
    @Excel(name = "数据库名称")
    private String dbname;

    /** 数据库用户名 */
    @Excel(name = "数据库用户名")
    private String dbuser;

    /** 数据库密码 */
    @Excel(name = "数据库密码")
    private String dbpassword;

    /** 合服列表 */
    @Excel(name = "合服列表")
    private String serverIdList;

    /** 合服标识 */
    @Excel(name = "合服标识")
    private Integer isHeFu;

    /** 合服目标服ID */
    @Excel(name = "合服目标服ID")
    private Integer hefuServerID;

    /** 合服时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "合服时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date hefuTime;

    /** 服务器类型 0:测试服 1:正式服 2:登录服 3:公共服 4:跨服 */
    @Excel(name = "服务器类型 0:测试服 1:正式服 2:登录服 3:公共服 4:跨服")
    private Integer serverType;

    /** 开服时间 */
    @Excel(name = "开服时间")
    private String serverOpenTime;

    /** 更新时间 */
    @Excel(name = "更新时间")
    private Date updateDate;

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }
    public void setServerName(String serverName)
    {
        this.serverName = serverName;
    }

    public String getServerName()
    {
        return serverName;
    }
    public void setType(Integer type)
    {
        this.type = type;
    }

    public Integer getType()
    {
        return type;
    }
    public void setServerId(Integer serverId)
    {
        this.serverId = serverId;
    }

    public Integer getServerId()
    {
        return serverId;
    }
    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    public String getGroupName()
    {
        return groupName;
    }
    public void setDbIp(String dbIp)
    {
        this.dbIp = dbIp;
    }

    public String getDbIp()
    {
        return dbIp;
    }
    public void setDbPort(Integer dbPort)
    {
        this.dbPort = dbPort;
    }

    public Integer getDbPort()
    {
        return dbPort;
    }
    public void setDbname(String dbname)
    {
        this.dbname = dbname;
    }

    public String getDbname()
    {
        return dbname;
    }
    public void setDbuser(String dbuser)
    {
        this.dbuser = dbuser;
    }

    public String getDbuser()
    {
        return dbuser;
    }
    public void setDbpassword(String dbpassword)
    {
        this.dbpassword = dbpassword;
    }

    public String getDbpassword()
    {
        return dbpassword;
    }
    public void setServerIdList(String serverIdList)
    {
        this.serverIdList = serverIdList;
    }

    public String getServerIdList()
    {
        return serverIdList;
    }
    public void setIsHeFu(Integer isHeFu)
    {
        this.isHeFu = isHeFu;
    }

    public Integer getIsHeFu()
    {
        return isHeFu;
    }
    public void setHefuServerID(Integer hefuServerID)
    {
        this.hefuServerID = hefuServerID;
    }

    public Integer getHefuServerID()
    {
        return hefuServerID;
    }
    public void setHefuTime(Date hefuTime)
    {
        this.hefuTime = hefuTime;
    }

    public Date getHefuTime()
    {
        return hefuTime;
    }
    public void setServerType(Integer serverType)
    {
        this.serverType = serverType;
    }

    public Integer getServerType()
    {
        return serverType;
    }
    public void setServerOpenTime(String serverOpenTime)
    {
        this.serverOpenTime = serverOpenTime;
    }

    public String getServerOpenTime()
    {
        return serverOpenTime;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("serverName", getServerName())
            .append("type", getType())
            .append("serverId", getServerId())
            .append("groupName", getGroupName())
            .append("dbIp", getDbIp())
            .append("dbPort", getDbPort())
            .append("dbname", getDbname())
            .append("dbuser", getDbuser())
            .append("dbpassword", getDbpassword())
            .append("serverIdList", getServerIdList())
            .append("isHeFu", getIsHeFu())
            .append("hefuServerID", getHefuServerID())
            .append("hefuTime", getHefuTime())
            .append("serverType", getServerType())
            .append("serverOpenTime", getServerOpenTime())
            .append("updateDate", getUpdateDate())
            .toString();
    }
}
