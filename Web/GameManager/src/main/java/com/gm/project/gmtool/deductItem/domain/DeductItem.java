package com.gm.project.gmtool.deductItem.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * 道具扣除对象 t_deduct_item
 * 
 * @author gm
 * @date 2021-10-30
 */
public class DeductItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 道具扣除ID */
    private Integer id;

    /** 服务ID */
    @Excel(name = "服务ID")
    private Integer serverId;

    /** 物品ID */
    @Excel(name = "物品ID")
    private Integer itemId;

    /** 角色ID */
    @Excel(name = "角色ID")
    private String roleId;

    /** 欲扣除的数量 */
    @Excel(name = "欲扣除的数量")
    private Integer dedCount;

    /** 真实扣除的数量 */
    @Excel(name = "真实扣除的数量")
    private Integer realCount;

    /** 是否发送邮件，0 不发送 1 发送 */
    @Excel(name = "是否发送邮件，0 不发送 1 发送")
    private Integer isMail;

    /** 是否绑定 true 绑定，false 不绑定 */
    @Excel(name = "是否绑定 true 绑定，false 不绑定")
    private Integer isBind;

    /** 扣除时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "扣除时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dedTime;

    /** 扣除原因 */
    @Excel(name = "扣除原因")
    private String reason;

    /** 邮件标题 */
    @Excel(name = "邮件标题")
    private String mailTitle;

    /** 邮件标题 */
    @Excel(name = "邮件标题")
    private String mailContent;

    /** 是否删除，0 ：不删除， 1： 删除 */
    @Excel(name = "是否删除，0 ：不删除， 1： 删除")
    private Integer isDelete;

    /** 发起者名字 */
    @Excel(name = "发起者名字")
    private String sendUser;

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }
    public void setServerId(Integer serverId)
    {
        this.serverId = serverId;
    }

    public Integer getServerId()
    {
        return serverId;
    }
    public void setItemId(Integer itemId)
    {
        this.itemId = itemId;
    }

    public Integer getItemId()
    {
        return itemId;
    }
    public void setRoleId(String roleId)
    {
        this.roleId = roleId;
    }

    public String getRoleId()
    {
        return roleId;
    }
    public void setDedCount(Integer dedCount)
    {
        this.dedCount = dedCount;
    }

    public Integer getDedCount()
    {
        return dedCount;
    }
    public void setRealCount(Integer realCount)
    {
        this.realCount = realCount;
    }

    public Integer getRealCount()
    {
        return realCount;
    }
    public void setIsMail(Integer isMail)
    {
        this.isMail = isMail;
    }

    public Integer getIsMail()
    {
        return isMail;
    }
    public void setIsBind(Integer isBind)
    {
        this.isBind = isBind;
    }

    public Integer getIsBind()
    {
        return isBind;
    }
    public void setDedTime(Date dedTime)
    {
        this.dedTime = dedTime;
    }

    public Date getDedTime()
    {
        return dedTime;
    }
    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public String getReason()
    {
        return reason;
    }
    public void setMailTitle(String mailTitle)
    {
        this.mailTitle = mailTitle;
    }

    public String getMailTitle()
    {
        return mailTitle;
    }
    public void setMailContent(String mailContent)
    {
        this.mailContent = mailContent;
    }

    public String getMailContent()
    {
        return mailContent;
    }
    public void setIsDelete(Integer isDelete)
    {
        this.isDelete = isDelete;
    }

    public Integer getIsDelete()
    {
        return isDelete;
    }
    public void setSendUser(String sendUser)
    {
        this.sendUser = sendUser;
    }

    public String getSendUser()
    {
        return sendUser;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("serverId", getServerId())
            .append("itemId", getItemId())
            .append("roleId", getRoleId())
            .append("dedCount", getDedCount())
            .append("realCount", getRealCount())
            .append("isMail", getIsMail())
            .append("isBind", getIsBind())
            .append("dedTime", getDedTime())
            .append("reason", getReason())
            .append("mailTitle", getMailTitle())
            .append("mailContent", getMailContent())
            .append("isDelete", getIsDelete())
            .append("sendUser", getSendUser())
            .toString();
    }
}
