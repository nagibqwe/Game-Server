package com.kits.project.serverListConfig.x8server.domain;

import com.kits.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.kits.framework.web.domain.BaseEntity;


/**
 * 游戏区服对象 t_x8_game_server
 * 
 * @author gm
 * @date 2021-07-08
 */
public class TX8GameServer extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 区服编号 */
    private Long svrId;

    /** 区服名称 */
    @Excel(name = "区服名称")
    private String svrName;

    /** 游戏ID */
    @Excel(name = "游戏ID")
    private Long gameId;

    /** 服务器IP */
    @Excel(name = "服务器IP")
    private String svrIp;

    /** 端口 */
    @Excel(name = "端口")
    private Long svrPort;

    /** 区服支付回调地址 */
    @Excel(name = "区服支付回调地址")
    private String svrPayCallback;

    /** 扩展数据 */
    @Excel(name = "扩展数据")
    private String svrExtconfig;

    /** 实际区服编号 */
    @Excel(name = "实际区服编号")
    private String mapSvrId;

    /** 游戏服类型 0-内部测试 1-正常 */
    @Excel(name = "游戏服类型 0-内部测试 1-正常")
    private Long groupType;

    /** 所属地区 */
    @Excel(name = "所属地区")
    private Long areaId;

    /** 合服状态0 - 正常服（没有合服）1 - 合服并保留2 - 合服并删除（合并前为正常服）3 - 合服并删除（合并前为合服并保留） */
    @Excel(name = "合服状态0 - 正常服", readConverterExp = "没=有合服")
    private Integer mergeStatus;

    /**  */
    @Excel(name = "")
    private String createUser;

    /**  */
    @Excel(name = "")
    private String updateUser;

    /** 0 - 来源平台手动添加 ；1 - gm接口定时任务主动刷新 */
    @Excel(name = "0 - 来源平台手动添加 ；1 - gm接口定时任务主动刷新")
    private Integer source;

    /** 对外所属地区 */
    @Excel(name = "对外所属地区")
    private String mapAreaId;

    public void setSvrId(Long svrId)
    {
        this.svrId = svrId;
    }

    public Long getSvrId()
    {
        return svrId;
    }
    public void setSvrName(String svrName)
    {
        this.svrName = svrName;
    }

    public String getSvrName()
    {
        return svrName;
    }
    public void setGameId(Long gameId)
    {
        this.gameId = gameId;
    }

    public Long getGameId()
    {
        return gameId;
    }
    public void setSvrIp(String svrIp)
    {
        this.svrIp = svrIp;
    }

    public String getSvrIp()
    {
        return svrIp;
    }
    public void setSvrPort(Long svrPort)
    {
        this.svrPort = svrPort;
    }

    public Long getSvrPort()
    {
        return svrPort;
    }
    public void setSvrPayCallback(String svrPayCallback)
    {
        this.svrPayCallback = svrPayCallback;
    }

    public String getSvrPayCallback()
    {
        return svrPayCallback;
    }
    public void setSvrExtconfig(String svrExtconfig)
    {
        this.svrExtconfig = svrExtconfig;
    }

    public String getSvrExtconfig()
    {
        return svrExtconfig;
    }
    public void setMapSvrId(String mapSvrId)
    {
        this.mapSvrId = mapSvrId;
    }

    public String getMapSvrId()
    {
        return mapSvrId;
    }
    public void setGroupType(Long groupType)
    {
        this.groupType = groupType;
    }

    public Long getGroupType()
    {
        return groupType;
    }
    public void setAreaId(Long areaId)
    {
        this.areaId = areaId;
    }

    public Long getAreaId()
    {
        return areaId;
    }
    public void setMergeStatus(Integer mergeStatus)
    {
        this.mergeStatus = mergeStatus;
    }

    public Integer getMergeStatus()
    {
        return mergeStatus;
    }
    public void setCreateUser(String createUser)
    {
        this.createUser = createUser;
    }

    public String getCreateUser()
    {
        return createUser;
    }
    public void setUpdateUser(String updateUser)
    {
        this.updateUser = updateUser;
    }

    public String getUpdateUser()
    {
        return updateUser;
    }
    public void setSource(Integer source)
    {
        this.source = source;
    }

    public Integer getSource()
    {
        return source;
    }
    public void setMapAreaId(String mapAreaId)
    {
        this.mapAreaId = mapAreaId;
    }

    public String getMapAreaId()
    {
        return mapAreaId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("svrId", getSvrId())
            .append("svrName", getSvrName())
            .append("gameId", getGameId())
            .append("svrIp", getSvrIp())
            .append("svrPort", getSvrPort())
            .append("svrPayCallback", getSvrPayCallback())
            .append("svrExtconfig", getSvrExtconfig())
            .append("mapSvrId", getMapSvrId())
            .append("updateTime", getUpdateTime())
            .append("createTime", getCreateTime())
            .append("groupType", getGroupType())
            .append("areaId", getAreaId())
            .append("mergeStatus", getMergeStatus())
            .append("createUser", getCreateUser())
            .append("updateUser", getUpdateUser())
            .append("source", getSource())
            .append("mapAreaId", getMapAreaId())
            .toString();
    }
}
