package com.gm.project.gmtool.gameInfo.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * 游戏参数信息对象 game_info
 * 
 * @author gm
 * @date 2021-11-15
 */
public class GameInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 游戏ID */
    private Integer gameId;

    /** 第三方充值密钥 */
    @Excel(name = "第三方充值密钥")
    private String rechargeSecretkey;

    /** 自动开服检查起始服务器ID */
    @Excel(name = "自动开服检查起始服务器ID")
    private Integer autoFirstServerId;

    /** 自动开服注册人数条件 */
    @Excel(name = "自动开服注册人数条件")
    private Integer autoUserCount;

    /** 自动开服进度ID */
    @Excel(name = "自动开服进度ID")
    private Integer autoServerId;

    /** 修改时间 */
    @Excel(name = "修改时间")
    private Long time;

    public void setGameId(Integer gameId)
    {
        this.gameId = gameId;
    }

    public Integer getGameId()
    {
        return gameId;
    }

    public void setRechargeSecretkey(String rechargeSecretkey)
    {
        this.rechargeSecretkey = rechargeSecretkey;
    }

    public String getRechargeSecretkey()
    {
        return rechargeSecretkey;
    }
    public void setAutoFirstServerId(Integer autoFirstServerId)
    {
        this.autoFirstServerId = autoFirstServerId;
    }

    public Integer getAutoFirstServerId()
    {
        return autoFirstServerId;
    }
    public void setAutoUserCount(Integer autoUserCount)
    {
        this.autoUserCount = autoUserCount;
    }

    public Integer getAutoUserCount()
    {
        return autoUserCount;
    }
    public void setAutoServerId(Integer autoServerId)
    {
        this.autoServerId = autoServerId;
    }

    public Integer getAutoServerId()
    {
        return autoServerId;
    }
    public void setTime(Long time)
    {
        this.time = time;
    }

    public Long getTime()
    {
        return time;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("gameId", getGameId())
            .append("rechargeSecretkey", getRechargeSecretkey())
            .append("autoFirstServerId", getAutoFirstServerId())
            .append("autoUserCount", getAutoUserCount())
            .append("autoServerId", getAutoServerId())
            .append("time", getTime())
            .toString();
    }
}
