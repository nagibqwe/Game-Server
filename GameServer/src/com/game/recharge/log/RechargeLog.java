package com.game.recharge.log;

import com.game.player.structs.Player;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import game.core.dblog.bean.CommonLogBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @explain: desc
 * @time Created on 2020/1/19 15:54.
 * @author: tc
 */
public class RechargeLog extends CommonLogBean {
	private String orderNo;

	private Integer goodsId;

	private String goodsType;

	private String goodsExt;

	private String goodsName;

	private String goodsCfg;

	private Integer totalFee;

	private Integer itemId;

	private Integer gameMoney;

	private String extParam;

	private String signType;

	private String sign;

	private Long addTime;

	private Byte status;

	private Integer statusReason;

	private Byte src;

	private String data;

	private String moneyType;

	/**
	 * 异步通知时间
	 */
	private String notify_time;
	/**
	 * 异步通知ID
	 */
	private String notify_id;

	/**
	 *第三方支付订单
	 */
	private String trade_no;

	/**
	 *支付成功,目前就只有此类型
	 */
	private Integer trade_status;



	public void setPlayer(Player p) {
		this.setPlayerInfo(p.getPlatformName(), p.getCreateServerId(), p.getUserId(), p.getId(), p.getName());
	}

	@Log(logField = "orderNo", fieldType = "varchar(100)", index = "0")
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	@Log(logField = "goodsId", fieldType = "int", index = "1")
	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	@Log(logField = "goodsType", fieldType = "varchar(100)", index = "0")
	public String getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}

	@Log(logField = "goodsExt", fieldType = "varchar(100)", index = "0")
	public String getGoodsExt() {
		return goodsExt;
	}

	public void setGoodsExt(String goodsExt) {
		this.goodsExt = goodsExt;
	}

	@Log(logField = "goodsName", fieldType = "varchar(100)", index = "0")
	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	@Log(logField = "goodsCfg", fieldType = "varchar(100)", index = "0")
	public String getGoodsCfg() {
		return goodsCfg;
	}

	public void setGoodsCfg(String goodsCfg) {
		this.goodsCfg = goodsCfg;
	}

	@Log(logField = "totalFee", fieldType = "int", index = "0")
	public Integer getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Integer totalFee) {
		this.totalFee = totalFee;
	}

	@Log(logField = "itemId", fieldType = "int", index = "0")
	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	@Log(logField = "gameMoney", fieldType = "int", index = "0")
	public Integer getGameMoney() {
		return gameMoney;
	}

	public void setGameMoney(Integer gameMoney) {
		this.gameMoney = gameMoney;
	}

	@Log(logField = "extParam", fieldType = "varchar(100)", index = "0")
	public String getExtParam() {
		return extParam;
	}

	public void setExtParam(String extParam) {
		this.extParam = extParam;
	}

	@Log(logField = "signType", fieldType = "varchar(100)", index = "0")
	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	@Log(logField = "sign", fieldType = "varchar(100)", index = "0")
	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	@Log(logField = "addTime", fieldType = "bigint", index = "0")
	public Long getAddTime() {
		return addTime;
	}

	public void setAddTime(Long addTime) {
		this.addTime = addTime;
	}

	@Log(logField = "status", fieldType = "tinyint", index = "0")
	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	@Log(logField = "statusReason", fieldType = "int", index = "0")
	public Integer getStatusReason() {
		return statusReason;
	}

	public void setStatusReason(Integer statusReason) {
		this.statusReason = statusReason;
	}

	@Log(logField = "src", fieldType = "tinyint", index = "0")
	public Byte getSrc() {
		return src;
	}

	public void setSrc(Byte src) {
		this.src = src;
	}

	@Log(logField = "data", fieldType = "TEXT", index = "0")
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Log(logField = "moneyType", fieldType = "TEXT", index = "0")
	public String getMoneyType() {
		return moneyType;
	}

	public void setMoneyType(String moneyType) {this.moneyType = moneyType;}
	@Log(logField = "notify_time", fieldType = "TEXT", index = "0")
	public String getNotify_time() {return notify_time;}

	public void setNotify_time(String notify_time) {
		this.notify_time = notify_time;
	}

	@Log(logField = "notify_id", fieldType = "TEXT", index = "0")
	public String getNotify_id() {return notify_id;}

	public void setNotify_id(String notify_id) {this.notify_id = notify_id;}

	@Log(logField = "trade_no", fieldType = "TEXT", index = "0")
	public String getTrade_no() {return trade_no;}

	public void setTrade_no(String trade_no) {this.trade_no = trade_no;}

	@Log(logField = "trade_status", fieldType = "int", index = "0")
	public Integer getTrade_status() {return trade_status;}

	public void setTrade_status(Integer trade_status) {this.trade_status = trade_status;}

	/**
	 * 日志多长时间建一次表
	 *
	 * @return
	 */
	@Override
	public TableCheckStepEnum getRollingStep() {
		return TableCheckStepEnum.UNROLL;
	}

	private static final Logger logger = LogManager.getLogger("RechargeManager");

	@Override
	public void logToFile() {
		logger.error(buildSql());
	}
}
