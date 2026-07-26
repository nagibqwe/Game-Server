package com.gm.project.gmtool.mail.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * Список писем对象 t_mail
 * 
 * @author gm
 * @date 2021-08-30
 */
public class MailData extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @Excel(name = "id")
    private Long id;

    /** Название платформы */
    @Excel(name = "Название платформы")
    private String groupName;

    /** СерверНомер */
    @Excel(name = "СерверНомер")
    private Integer serverId;

    /** ID персонажа列表 */
    @Excel(name = "ID персонажа列表")
    private String roleIds;

    /** Тема письма */
    @Excel(name = "Тема письма")
    private String title;

    /** Текст письма */
    @Excel(name = "Текст письма")
    private String content;

    /** ПисьмоВложениеСписок предметов */
    @Excel(name = "ПисьмоВложениеСписок предметов")
    private String items;

    /** Письмо发送理由 */
    @Excel(name = "Письмо发送理由")
    private String reason;

    /** ПисьмоВремя создания */
    @Excel(name = "ПисьмоВремя создания")
    private String createDate;

    /** Письмо创建的后台账号名 */
    @Excel(name = "Письмо创建的后台账号名")
    private String createUser;

    /** Письмо审核的后台账号名 */
    @Excel(name = "Письмо审核的后台账号名")
    private String adminUser;

    /** Письмо审核的Дата */
    @Excel(name = "Письмо审核的Дата")
    private String adminDate;

    /** 审核ДаНет通过 */
    @Excel(name = "审核ДаНет通过")
    private Integer adminState;

    /** 发送到游戏服的Статус值 */
    @Excel(name = "发送到游戏服的Статус值")
    private Integer sendState;

    /** 发送到服务Назад的РезультатИнформация */
    @Excel(name = "发送到服务Назад的РезультатИнформация")
    private String sendErrorMess;

    /** Письмо的Удалить标志 */
    @Excel(name = "Письмо的Удалить标志")
    private Integer isDelete;

    /** ДаНет已经发送过 */
    @Excel(name = "ДаНет已经发送过")
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
    public void setServerId(Integer serverId)
    {
        this.serverId = serverId;
    }

    public Integer getServerId()
    {
        return serverId;
    }
    public void setRoleIds(String roleIds)
    {
        this.roleIds = roleIds;
    }

    public String getRoleIds()
    {
        return roleIds;
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
            .append("serverId", getServerId())
            .append("roleIds", getRoleIds())
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
