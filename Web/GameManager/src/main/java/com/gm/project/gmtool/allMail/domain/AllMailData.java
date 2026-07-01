package com.gm.project.gmtool.allMail.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * 全服邮件对象 t_mail_all
 * 
 * @author gm
 * @date 2021-08-30
 */
public class AllMailData extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @Excel(name = "id")
    private Long id;

    /** 平台名字 */
    @Excel(name = "平台名字")
    private String groupName;

    /** 最小等级 */
    @Excel(name = "最小等级")
    private Integer minLv;

    /** 最大等级 */
    @Excel(name = "最大等级")
    private Integer maxLv;

    /** 职业 */
    @Excel(name = "职业")
    private Integer career;

    /** 服务器列表 */
    @Excel(name = "服务器列表")
    private String serverIdList;

    /** 邮件标题 */
    @Excel(name = "邮件标题")
    private String title;

    /** 邮件内容 */
    @Excel(name = "邮件内容")
    private String content;

    /** 邮件附件物品列表 */
    @Excel(name = "邮件附件物品列表")
    private String items;

    /** 邮件发送理由 */
    @Excel(name = "邮件发送理由")
    private String reason;

    /** 邮件创建时间 */
    @Excel(name = "邮件创建时间")
    private String createDate;

    /** 邮件创建的后台账号名 */
    @Excel(name = "邮件创建的后台账号名")
    private String createUser;

    /** 邮件审核的后台账号名 */
    @Excel(name = "邮件审核的后台账号名")
    private String adminUser;

    /** 邮件审核的日期 */
    @Excel(name = "邮件审核的日期")
    private String adminDate;

    /** 审核是否通过 */
    @Excel(name = "审核是否通过")
    private Integer adminState;

    /** 发送到游戏服的状态值 */
    @Excel(name = "发送到游戏服的状态值")
    private Integer sendState;

    /** 发送到服务返回的结果信息 */
    @Excel(name = "发送到服务返回的结果信息")
    private String sendErrorMess;

    /** 邮件的删除标志 */
    @Excel(name = "邮件的删除标志")
    private Integer isDelete;

    /** 是否已经发送过 */
    @Excel(name = "是否已经发送过")
    private Integer sended;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    public String getGroupName()
    {
        return groupName;
    }
    public void setMinLv(Integer minLv)
    {
        this.minLv = minLv;
    }

    public Integer getMinLv()
    {
        return minLv;
    }
    public void setMaxLv(Integer maxLv)
    {
        this.maxLv = maxLv;
    }

    public Integer getMaxLv()
    {
        return maxLv;
    }
    public void setCareer(Integer career)
    {
        this.career = career;
    }

    public Integer getCareer()
    {
        return career;
    }
    public void setServerIdList(String serverIdList)
    {
        this.serverIdList = serverIdList;
    }

    public String getServerIdList()
    {
        return serverIdList;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }
    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }
    public void setItems(String items)
    {
        this.items = items;
    }

    public String getItems()
    {
        return items;
    }
    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public String getReason()
    {
        return reason;
    }
    public void setCreateDate(String createDate)
    {
        this.createDate = createDate;
    }

    public String getCreateDate()
    {
        return createDate;
    }
    public void setCreateUser(String createUser)
    {
        this.createUser = createUser;
    }

    public String getCreateUser()
    {
        return createUser;
    }
    public void setAdminUser(String adminUser)
    {
        this.adminUser = adminUser;
    }

    public String getAdminUser()
    {
        return adminUser;
    }
    public void setAdminDate(String adminDate)
    {
        this.adminDate = adminDate;
    }

    public String getAdminDate()
    {
        return adminDate;
    }
    public void setAdminState(Integer adminState)
    {
        this.adminState = adminState;
    }

    public Integer getAdminState()
    {
        return adminState;
    }
    public void setSendState(Integer sendState)
    {
        this.sendState = sendState;
    }

    public Integer getSendState()
    {
        return sendState;
    }
    public void setSendErrorMess(String sendErrorMess)
    {
        this.sendErrorMess = sendErrorMess;
    }

    public String getSendErrorMess()
    {
        return sendErrorMess;
    }
    public void setIsDelete(Integer isDelete)
    {
        this.isDelete = isDelete;
    }

    public Integer getIsDelete()
    {
        return isDelete;
    }
    public void setSended(Integer sended)
    {
        this.sended = sended;
    }

    public Integer getSended()
    {
        return sended;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("groupName", getGroupName())
            .append("minLv", getMinLv())
            .append("maxLv", getMaxLv())
            .append("career", getCareer())
            .append("serverIdList", getServerIdList())
            .append("title", getTitle())
            .append("content", getContent())
            .append("items", getItems())
            .append("reason", getReason())
            .append("createDate", getCreateDate())
            .append("createUser", getCreateUser())
            .append("adminUser", getAdminUser())
            .append("adminDate", getAdminDate())
            .append("adminState", getAdminState())
            .append("sendState", getSendState())
            .append("sendErrorMess", getSendErrorMess())
            .append("isDelete", getIsDelete())
            .append("sended", getSended())
            .toString();
    }
}
