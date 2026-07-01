package com.backend.struct.log.entity.recharge;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.CommonLogBean;
import com.backend.struct.log.IConvertor;

import java.util.Map;

/**
 * 充值日志
 */
@Table(name = "rechargelog")
public class RechargeLog extends CommonLogBean implements IConvertor {

	@FieldDesc(selectKey = true)
	private String orderNo;					//订单编号

	@FieldDesc
	private long userId;					//用户ID

	@FieldDesc
	private long roleId;					//角色ID

	@FieldDesc
	private int goodsId;				//商品ID

	@FieldDesc
	private String goodsType;				//商品类型

	@FieldDesc
	private String goodsExt;				//商品额外参数

	@FieldDesc
	private int totalFee;				//金额（分）

	@FieldDesc
	private int itemId;					//道具ID

	@FieldDesc
	private int gameMoney;				//游戏货币

	@FieldDesc
	private String extParam;				//额外参数

	@FieldDesc(show = false)
	private String signType;				//签名类型

	@FieldDesc(show = false)
	private String sign;					//签名

	@FieldDesc(show = false)
	private long addTime;					//时间

	@FieldDesc
	private byte status;					//状态

	@FieldDesc
	private int statusReason;			//状态码

	@FieldDesc(show = false)
	private byte src;						//src

	@FieldDesc(show = false)
	private String data;					//data

	@Override
	public Map<String, String> convert(Map<String, String> data) {
		return data;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	@Override
	public long getUserId() {
		return userId;
	}

	@Override
	public void setUserId(long userId) {
		this.userId = userId;
	}

	@Override
	public long getRoleId() {
		return roleId;
	}

	@Override
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public int getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}

	public String getGoodsExt() {
		return goodsExt;
	}

	public void setGoodsExt(String goodsExt) {
		this.goodsExt = goodsExt;
	}

	public int getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(int totalFee) {
		this.totalFee = totalFee;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getGameMoney() {
		return gameMoney;
	}

	public void setGameMoney(int gameMoney) {
		this.gameMoney = gameMoney;
	}

	public String getExtParam() {
		return extParam;
	}

	public void setExtParam(String extParam) {
		this.extParam = extParam;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public long getAddTime() {
		return addTime;
	}

	public void setAddTime(long addTime) {
		this.addTime = addTime;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public int getStatusReason() {
		return statusReason;
	}

	public void setStatusReason(int statusReason) {
		this.statusReason = statusReason;
	}

	public byte getSrc() {
		return src;
	}

	public void setSrc(byte src) {
		this.src = src;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
