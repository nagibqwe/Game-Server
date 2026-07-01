package com.backend.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("t_recharge")
public class Recharge {
	
	@Id
	@Column 
	@Comment("充值ID")
	private int id;

	@Column 
	@Comment("充值服务器id")
	private int serverId;
	
	@Column
	@Comment("角色ID")
	private long roleId;
	
	@Column
	@Comment("充值数量")
	private int rechargeNumber;

	@Column
	@Comment("充值累积数量")
	private int rechargeTotalGold;

	@Column
	@Comment("充值Vip经验")
	private int rechargeVipExp;
	
	@Column
	@Comment("创建者")
	private String createUser;	
	
	@Column
	@Comment("创建时间")
	private String createTime;	
	
	@Column
	@Comment("充值状态,0为待审核，1为通过，2为失败")
	private int rechargeState;
	
	@Column
	@Comment("审核者")
	private String approvalUser;
	
	@Column
	@Comment("审核时间")
	private String approvalTime;
	
	@Column
	@Comment("充值理由")
	private String reason;
	
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

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public int getRechargeNumber() {
		return rechargeNumber;
	}

	public void setRechargeNumber(int rechargeNumber) {
		this.rechargeNumber = rechargeNumber;
	}

	public int getRechargeTotalGold() {
		return rechargeTotalGold;
	}

	public void setRechargeTotalGold(int rechargeTotalGold) {
		this.rechargeTotalGold = rechargeTotalGold;
	}

	public int getRechargeVipExp() {
		return rechargeVipExp;
	}

	public void setRechargeVipExp(int rechargeVipExp) {
		this.rechargeVipExp = rechargeVipExp;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getRechargeState() {
		return rechargeState;
	}

	public void setRechargeState(int rechargeState) {
		this.rechargeState = rechargeState;
	}

	public String getApprovalUser() {
		return approvalUser;
	}

	public void setApprovalUser(String approvalUser) {
		this.approvalUser = approvalUser;
	}

	public String getApprovalTime() {
		return approvalTime;
	}

	public void setApprovalTime(String approvalTime) {
		this.approvalTime = approvalTime;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	
}
