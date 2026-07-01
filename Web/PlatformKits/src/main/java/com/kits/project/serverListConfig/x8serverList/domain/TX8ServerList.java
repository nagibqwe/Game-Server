package com.kits.project.serverListConfig.x8serverList.domain;

import com.kits.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.kits.framework.web.domain.BaseEntity;


/**
 * 渠道区服策略列对象 t_x8_server_list
 * 
 * @author gm
 * @date 2021-07-08
 */
public class TX8ServerList extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 策略编号 */
    private Long policyId;

    /** 策略名称 */
    @Excel(name = "策略名称")
    private String policyName;

    /** 游戏ID */
    @Excel(name = "游戏ID")
    private Long gameId;

    /** 渠道ID列表 */
    @Excel(name = "渠道ID列表")
    private String chnIds;

    /** 服务器列表 */
    @Excel(name = "服务器列表")
    private String svrPolicyIds;

    /** 更新人 */
    @Excel(name = "更新人")
    private String updateUser;

    /** 状态 */
    @Excel(name = "状态")
    private Long status;

    /**  */
    @Excel(name = "")
    private Long startIdx;

    public void setPolicyId(Long policyId)
    {
        this.policyId = policyId;
    }

    public Long getPolicyId()
    {
        return policyId;
    }
    public void setPolicyName(String policyName)
    {
        this.policyName = policyName;
    }

    public String getPolicyName()
    {
        return policyName;
    }
    public void setGameId(Long gameId)
    {
        this.gameId = gameId;
    }

    public Long getGameId()
    {
        return gameId;
    }
    public void setChnIds(String chnIds)
    {
        this.chnIds = chnIds;
    }

    public String getChnIds()
    {
        return chnIds;
    }
    public void setSvrPolicyIds(String svrPolicyIds)
    {
        this.svrPolicyIds = svrPolicyIds;
    }

    public String getSvrPolicyIds()
    {
        return svrPolicyIds;
    }
    public void setUpdateUser(String updateUser)
    {
        this.updateUser = updateUser;
    }

    public String getUpdateUser()
    {
        return updateUser;
    }
    public void setStatus(Long status)
    {
        this.status = status;
    }

    public Long getStatus()
    {
        return status;
    }
    public void setStartIdx(Long startIdx)
    {
        this.startIdx = startIdx;
    }

    public Long getStartIdx()
    {
        return startIdx;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("policyId", getPolicyId())
            .append("policyName", getPolicyName())
            .append("gameId", getGameId())
            .append("chnIds", getChnIds())
            .append("svrPolicyIds", getSvrPolicyIds())
            .append("updateUser", getUpdateUser())
            .append("status", getStatus())
            .append("startIdx", getStartIdx())
            .append("updateTime", getUpdateTime())
            .append("createTime", getCreateTime())
            .toString();
    }
}
