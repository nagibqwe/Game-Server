package com.kits.project.gmtool.dblog.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.kits.framework.aspectj.lang.annotation.Excel;
import com.kits.framework.web.domain.BaseEntity;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 日志库列对象 t_dblog
 * 
 * @author gm
 * @date 2021-04-23
 */
public class TDblog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long id;

    /** 服务器ID */
    @Excel(name = "服务器ID")
    private Long serverId;

    /** 服务器名称 */
    @Excel(name = "服务器名称")
    private String serverName;

    /** 平台名 */
    @Excel(name = "平台名")
    private String groupName;

    /** 类型 */
    @Excel(name = "类型")
    private Long type;

    /** 服务器IP及端口 */
    @Excel(name = "服务器IP及端口")
    private String serverIpPort;

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
    private String owerlist;

    /** 合服标识 */
    @Excel(name = "合服标识")
    private Integer isHeFu;

    /** 合服目标服ID */
    @Excel(name = "合服目标服ID")
    private Long hefuServerID;

    /** 合服时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "合服时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date hefuTime;

    /** 服务器类型 */
    @Excel(name = "服务器类型")
    private Integer serverType;

    /** 是否删除 */
    @Excel(name = "是否删除")
    private Integer isDeleted;

    /** 开服时间 */
    @Excel(name = "开服时间")
    private String serverOpenTime;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setServerId(Long serverId)
    {
        this.serverId = serverId;
    }

    public Long getServerId()
    {
        return serverId;
    }
    public void setServerName(String serverName)
    {
        this.serverName = serverName;
    }

    public String getServerName()
    {
        return serverName;
    }
    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    public String getGroupName()
    {
        return groupName;
    }
    public void setType(Long type)
    {
        this.type = type;
    }

    public Long getType()
    {
        return type;
    }
    public void setServerIpPort(String serverIpPort)
    {
        this.serverIpPort = serverIpPort;
    }

    public String getServerIpPort()
    {
        return serverIpPort;
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
    public void setOwerlist(String owerlist)
    {
        this.owerlist = owerlist;
    }

    public String getOwerlist()
    {
        return owerlist;
    }
    public void setIsHeFu(Integer isHeFu)
    {
        this.isHeFu = isHeFu;
    }

    public Integer getIsHeFu()
    {
        return isHeFu;
    }
    public void setHefuServerID(Long hefuServerID)
    {
        this.hefuServerID = hefuServerID;
    }

    public Long getHefuServerID()
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
    public void setIsDeleted(Integer isDeleted)
    {
        this.isDeleted = isDeleted;
    }

    public Integer getIsDeleted()
    {
        return isDeleted;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("serverId", getServerId())
            .append("serverName", getServerName())
            .append("groupName", getGroupName())
            .append("type", getType())
            .append("serverIpPort", getServerIpPort())
            .append("dbname", getDbname())
            .append("dbuser", getDbuser())
            .append("dbpassword", getDbpassword())
            .append("owerlist", getOwerlist())
            .append("isHeFu", getIsHeFu())
            .append("hefuServerID", getHefuServerID())
            .append("hefuTime", getHefuTime())
            .append("serverType", getServerType())
            .append("isDeleted", getIsDeleted())
            .append("serverOpenTime", getServerOpenTime())
            .toString();
    }

    public String getServerOpenTime() {
        return serverOpenTime;
    }

    public void setServerOpenTime(String serverOpenTime) {
        this.serverOpenTime = serverOpenTime;
    }
}
