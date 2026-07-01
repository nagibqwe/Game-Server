package com.backend.bean;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

@Table("t_mail")
@TableIndexes(value={@Index(fields={"isDelete","createDate"}, name="createDate_isdel",unique=false),
		@Index(fields={"createUser","isDelete","createDate"}, name="createUser",unique=false),
		@Index(fields={"sended","isDelete"}, name="sended",unique=false),
		@Index(fields={"isDelete"}, name="isDelete",unique=false)})
public class MailData {

	@Id
	private long id;
	
	@Column
	@Comment("平台名字")
	private String groupName;
	
	@Column
	@Comment("服务器编号")
	private int serverId;
	
	@Column
	@Comment("角色ID列表")
	@ColDefine(type=ColType.TEXT, notNull=true)
	private String roleIds;
	
	@Column
	@Comment("邮件标题")
	@ColDefine(type=ColType.VARCHAR, width=120, notNull=true)
	private String title;
	
	@Column
	@Comment("邮件内容")
	@ColDefine(type=ColType.TEXT, notNull=true)
	private String content;
	
	@Column
	@Comment("邮件附件物品列表")
	@ColDefine(type=ColType.VARCHAR, width=500)
	private String items;
	
	@Column
	@Comment("邮件发送理由")
	@ColDefine(type=ColType.VARCHAR, width=300, notNull=true)
	private String reason;
	
	@Column
	@Comment("邮件创建时间")
	private String createDate;
	
	@Column
	@Comment("邮件创建的后台账号名")
	private String createUser;
	
	@Column
	@Comment("邮件审核的后台账号名")
	private String adminUser;
	
	@Column
	@Comment("邮件审核的日期")
	private String adminDate;
	
	@Column
	@Comment("审核是否通过")
	private int adminState = 0;
	
	@Column
	@Comment("发送到游戏服的状态值")
	private int sendState;
	
	@Column
	@Comment("发送到服务返回的结果信息")
	@ColDefine(type=ColType.VARCHAR, width=300)
	private String sendErrorMess;
	
	@Column
	@Comment("邮件的删除标志")
	private int isDelete = 0;
	
	@Column
	@Comment("是否已经发送过")
	private int sended;
	
	//物品解析
	private String itemStr;
	
	//当前状态值
	private String stateStr;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getItems() {
		return items;
	}

	public void setItems(String items) {
		this.items = items;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getAdminUser() {
		return adminUser;
	}

	public void setAdminUser(String adminUser) {
		this.adminUser = adminUser;
	}

	public String getAdminDate() {
		return adminDate;
	}

	public void setAdminDate(String adminDate) {
		this.adminDate = adminDate;
	}

	public int getAdminState() {
		return adminState;
	}

	public void setAdminState(int adminState) {
		this.adminState = adminState;
	}

	public int getSendState() {
		return sendState;
	}

	public void setSendState(int sendState) {
		this.sendState = sendState;
	}

	public String getSendErrorMess() {
		return sendErrorMess;
	}

	public void setSendErrorMess(String sendErrorMess) {
		this.sendErrorMess = sendErrorMess;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	public int getSended() {
		return sended;
	}

	public void setSended(int sended) {
		this.sended = sended;
	}

	public String getItemStr() {
		return itemStr;
	}

	public void setItemStr(String itemStr) {
		this.itemStr = itemStr;
	}

	public String getStateStr() {
		return stateStr;
	}

	public void setStateStr(String stateStr) {
		this.stateStr = stateStr;
	}
}
