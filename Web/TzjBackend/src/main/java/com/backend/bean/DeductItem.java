package com.backend.bean;

import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 道具扣除的bean
 */
@Table("t_deduct_item")
public class DeductItem {

	@Id
	@Comment("道具扣除ID")
	private int id;
	
	@Column
	@Comment("服务ID")
	private int serverId;
	
	@Column
	@Comment("物品ID")
	private int itemId;
	
	@Column
	@Comment("角色ID")
	private String roleId;
	
	@Column
	@Comment("欲扣除的数量")
	private int dedCount;
	
	@Column
	@Comment("真实扣除的数量")
	private int realCount;
	
	@Column
	@Comment("是否发送邮件，0 不发送 1 发送")
	private int isMail;
	
	@Column
	@Comment("是否绑定: 1 绑定，0 不绑定")
	private int isBind;
	
	@Column
	@Comment("扣除时间")
	private Date dedTime;
	
	@Column
	@Comment("扣除原因")
	private String reason;
	
	@Column
	@Comment("邮件标题")
	private String mailTitle;
	
	@Column
	@Comment("邮件标题")
	private String mailContent;
	
	@Column
	@Comment("是否删除，0 ：不删除， 1： 删除")
	private int isDelete;

	@Column
	@Comment("发起者名字")
	private  String sendUser;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public int getDedCount() {
		return dedCount;
	}

	public void setDedCount(int dedCount) {
		this.dedCount = dedCount;
	}
	
	public int getRealCount() {
		return realCount;
	}

	public void setRealCount(int realCount) {
		this.realCount = realCount;
	}

	public int getIsMail() {
		return isMail;
	}

	public void setIsMail(int isMail) {
		this.isMail = isMail;
	}

	public Date getDedTime() {
		return dedTime;
	}

	public void setDedTime(Date dedTime) {
		this.dedTime = dedTime;
	}

	public int isIsBind() {
		return isBind;
	}

	public void setIsBind(int isBind) {
		this.isBind = isBind;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getMailTitle() {
		return mailTitle;
	}

	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}

	public String getMailContent() {
		return mailContent;
	}

	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	public String getSendUser() {
		return sendUser;
	}

	public void setSendUser(String sendUser) {
		this.sendUser = sendUser;
	}
}
