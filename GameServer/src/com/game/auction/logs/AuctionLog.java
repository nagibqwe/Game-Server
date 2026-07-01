package com.game.auction.logs;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Description
 * @auther lw
 * @create 2019-10-10 20:29
 */
public class AuctionLog extends BaseLogBean {
    private static final Logger logger = LogManager.getLogger("AuctionLog");

    private long operateId;

    private long auctionId;

    private long itemUid;

    private int itemId;

    private long ownId;

    private int num;

    private long type;

    private int price;

    private String password;

    // 1:上架 2:竞拍 3:一口价购买 4下架 5.仙盟流拍进入世界 6.世界流拍 7.系统结算拍卖成功 8.竞拍成功 9.合成购买
    private int operate;

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }

    @Log(fieldType = "bigint", logField = "operateId", index = "0")
    public long getOperateId() {
        return operateId;
    }

    public void setOperateId(long operateId) {
        this.operateId = operateId;
    }

    @Log(fieldType = "bigint", logField = "auctionId", index = "0")
    public long getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(long auctionId) {
        this.auctionId = auctionId;
    }

    @Log(fieldType = "bigint", logField = "itemUid", index = "0")
    public long getItemUid() {
        return itemUid;
    }

    public void setItemUid(long itemUid) {
        this.itemUid = itemUid;
    }

    @Log(fieldType = "int", logField = "itemId", index = "0")
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    @Log(fieldType = "int", logField = "num", index = "0")
    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Log(fieldType = "bigint", logField = "type", index = "0")
    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    @Log(fieldType = "bigint", logField = "ownId", index = "0")
    public long getOwnId() {
        return ownId;
    }

    public void setOwnId(long ownId) {
        this.ownId = ownId;
    }

    @Log(fieldType = "int", logField = "price", index = "0")
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Log(fieldType = "int", logField = "operate", index = "0")
    public int getOperate() {
        return operate;
    }

    public void setOperate(int operate) {
        this.operate = operate;
    }

    @Log(fieldType = "varchar(45)", logField = "password", index = "0")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
