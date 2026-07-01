package com.gm.project.gmtool.updateNotice.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * 更新公告对象 t_update_notice
 * 
 * @author gm
 * @date 2021-10-30
 */
public class UpdateNotice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;

    /** 服务器ID */
    @Excel(name = "服务器ID")
    private String serverIds;

    /** 公告内容 */
    @Excel(name = "公告内容")
    private String content;

    /** 公告奖励 */
    @Excel(name = "公告奖励")
    private String reward;

    /** 操作类型，0 ：只更新公告， 1： 重置奖励 */
    @Excel(name = "操作类型，0 ：只更新公告， 1： 重置奖励")
    private Integer type;

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }
    public void setServerIds(String serverIds)
    {
        this.serverIds = serverIds;
    }

    public String getServerIds()
    {
        return serverIds;
    }
    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }
    public void setReward(String reward)
    {
        this.reward = reward;
    }

    public String getReward()
    {
        return reward;
    }
    public void setType(Integer type)
    {
        this.type = type;
    }

    public Integer getType()
    {
        return type;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("serverIds", getServerIds())
            .append("content", getContent())
            .append("reward", getReward())
            .append("type", getType())
            .toString();
    }
}
