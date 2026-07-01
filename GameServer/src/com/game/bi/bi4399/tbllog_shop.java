package com.game.bi.bi4399;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;

public class tbllog_shop extends BaseLogBean {

    // 所属平台，记录SDK platformID_gameID
    private String platform;
    // 设备端：android、ios、web、pc
    private String device;
    // 角色ID
    private long role_id;
    // 平台账号ID
    private String account_name;
    // 商城类型ID
    private int shopId;
    // 玩家等级
    private int dim_level;
    // 职业ID
    private int dim_prof;
    // 货币类型
    private int money_type;
    // 货币数量(总价)
    private int amount;
    // 物品分类1
    private int item_type_1;
    // 物品分类2
    private int item_type_2;
    // 物品ID
    private int item_id;
    // 物品数量
    private int item_number;
    // 事件发生时间（索引）
    private int happend_time;

    public tbllog_shop() {}

    public tbllog_shop(String platform, String device, long role_id, String account_name, int shopId, int dim_level, int dim_prof,
                       int money_type, int amount, int item_type_1, int item_type_2, int item_id, int item_number, int happend_time) {
        this.platform = platform;
        this.device = device;
        this.role_id = role_id;
        this.account_name = account_name;
        this.shopId = shopId;
        this.dim_level = dim_level;
        this.dim_prof = dim_prof;
        this.money_type = money_type;
        this.amount = amount;
        this.item_type_1 = item_type_1;
        this.item_type_2 = item_type_2;
        this.item_id = item_id;
        this.item_number = item_number;
        this.happend_time = happend_time;
    }

    @Log(logField = "platform", fieldType = "varchar(100)", index = "0")
    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @Log(logField = "device", fieldType = "varchar(100)", index = "0")
    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    @Log(logField = "role_id", fieldType = "bigint", index = "0")
    public long getRole_id() {
        return role_id;
    }

    public void setRole_id(long role_id) {
        this.role_id = role_id;
    }

    @Log(logField = "account_name", fieldType = "varchar(100)", index = "0")
    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    @Log(logField = "shopId", fieldType = "int", index = "0")
    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    @Log(logField = "dim_level", fieldType = "int", index = "0")
    public int getDim_level() {
        return dim_level;
    }

    public void setDim_level(int dim_level) {
        this.dim_level = dim_level;
    }

    @Log(logField = "dim_prof", fieldType = "int", index = "0")
    public int getDim_prof() {
        return dim_prof;
    }

    public void setDim_prof(int dim_prof) {
        this.dim_prof = dim_prof;
    }

    @Log(logField = "money_type", fieldType = "int", index = "0")
    public int getMoney_type() {
        return money_type;
    }

    public void setMoney_type(int money_type) {
        this.money_type = money_type;
    }

    @Log(logField = "amount", fieldType = "int", index = "0")
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Log(logField = "item_type_1", fieldType = "int", index = "0")
    public int getItem_type_1() {
        return item_type_1;
    }

    public void setItem_type_1(int item_type_1) {
        this.item_type_1 = item_type_1;
    }

    @Log(logField = "item_type_2", fieldType = "int", index = "0")
    public int getItem_type_2() {
        return item_type_2;
    }

    public void setItem_type_2(int item_type_2) {
        this.item_type_2 = item_type_2;
    }

    @Log(logField = "item_id", fieldType = "int", index = "0")
    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    @Log(logField = "item_number", fieldType = "int", index = "0")
    public int getItem_number() {
        return item_number;
    }

    public void setItem_number(int item_number) {
        this.item_number = item_number;
    }

    @Log(logField = "happend_time", fieldType = "int", index = "1")
    public int getHappend_time() {
        return happend_time;
    }

    public void setHappend_time(int happend_time) {
        this.happend_time = happend_time;
    }

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.UNROLL;
    }

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }
}
