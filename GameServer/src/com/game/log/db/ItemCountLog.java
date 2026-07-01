package com.game.log.db;

import game.core.dblog.base.Log;
import java.util.Date;

/**
 * 物品变化日志
 */
public class ItemCountLog {

    private Date date;              //日期
    private int modelId;            //物品ID
    private int type;               //道具类型
    private String name;            //物品名称
    private long produce;           //产生的数量
    private long consume;           //消耗的数量
    private int serverId;           //服务器id
    private long dealTime;           //最后更新时间

    public transient boolean update = false;//是否需要更新
    public ItemCountLog(){}

//    @Override
//    public TableCheckStepEnum getRollingStep() {
//        return TableCheckStepEnum.UNROLL;
//    }

//    @Override
//    public void logToFile() {
//        logger.error(buildSql());
//    }

    public ItemCountLog(int serverId, int modelId, int type, String name){
        this.date = new Date();
        this.modelId = modelId;
        this.type = type;
        this.name = name;
        this.serverId = serverId;
    }

    @Log(logField = "modelId", fieldType = "int", index = "0")
    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    @Log(logField = "type", fieldType = "int", index = "0")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Log(logField = "name", fieldType = "String", index = "0")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Log(logField = "produce", fieldType = "long", index = "0")
    public long getProduce() {
        return produce;
    }

    public void setProduce(long produce) {
        this.produce = produce;
    }

    @Log(logField = "consume", fieldType = "long", index = "0")
    public long getConsume() {
        return consume;
    }

    public void setConsume(long consume) {
        this.consume = consume;
    }

    @Log(logField = "date", fieldType = "Date", index = "0")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Log(logField = "serverId", fieldType = "int", index = "0")
    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    @Log(logField = "dealTime", fieldType = "long", index = "0")
    public long getDealTime() {
        return dealTime;
    }

    public void setDealTime(long dealTime) {
        this.dealTime = dealTime;
    }


}
