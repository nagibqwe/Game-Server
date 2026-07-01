package com.game.db.bean;

import game.core.db.BaseBean;
import game.core.util.StringUtils;

public class RechargeBean extends BaseBean {
    private String orderNo;

    private Long userId;

    private Long roleId;

    private Integer srvId;

    private Integer goodsId;        //x8server 配置id

    private String goodsType;

    private String goodsExt;

    private String goodsName;

    private String goodsCfg;        //游戏服充值配置ID 回掉

    private Integer totalFee;

    private Integer itemId;

    private Integer gameMoney;

    private String extParam;

    private String signType;

    private String sign;

    private Long addTime;

    private Byte status;

    private Byte src;

    private String data;
    /**
     * 充值货币类型 THB，HKD，CNY
     */
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
    private int trade_status;


    /**
     * 计算到游戏累充值
     */
    private int totalRecharge;

    /**
     * vip经验加成
     */
    private int totalVipPower;




    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Integer getSrvId() {
        return srvId;
    }

    public void setSrvId(Integer srvId) {
        this.srvId = srvId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    /**
     * 获取商品映射ID
     * @return
     */
    public int getGoodsCfgId() {
        if (this.getGoodsCfg() == null)
            return 0;
        return StringUtils.parseInt(this.getGoodsCfg(), 0);
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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsCfg() {
        return goodsCfg;
    }

    public void setGoodsCfg(String goodsCfg) {
        this.goodsCfg = goodsCfg;
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getGameMoney() {
        return gameMoney;
    }

    public void setGameMoney(Integer gameMoney) {
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

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getSrc() {
        return src;
    }

    public void setSrc(Byte src) {
        this.src = src;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(String moneyType) {
        this.moneyType = moneyType;
    }

    public String getNotify_time() {
        return notify_time;
    }

    public void setNotify_time(String notify_time) {
        this.notify_time = notify_time;
    }


    public String getNotify_id() {
        return notify_id;
    }

    public void setNotify_id(String notify_id) {
        this.notify_id = notify_id;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public int getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(int trade_status) {
        this.trade_status = trade_status;
    }

    public int getTotalRecharge() {
        return totalRecharge;
    }
    public void setTotalRecharge(int totalRecharge) {
        this.totalRecharge = totalRecharge;
    }

    public int getTotalVipPower() {
        return totalVipPower;
    }

    public void setTotalVipPower(int totalVipPower) {
        this.totalVipPower = totalVipPower;
    }
}