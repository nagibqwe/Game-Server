package com.backend.struct.log.entity.trade;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;

/**
 * 从背包移到交易栏中交易物品或交易货币的log
 */
@Table(name = "tradeitemlog", tableType = TableType.Month)
public class TradeItemLog implements IConvertor {

    @FieldDesc
    private long playerId;           //玩家Id

    @FieldDesc
    private int itemOrCoin;          //交易的是物品还是货币，1：物品，2：货币

    @FieldDesc
    private String tradeItem;        //交易的是物品(物品Item实例JSON化后的字串记录)

    @FieldDesc
    private int tradeGridNum;        //交易的是物品其放于交易栏中的编号

    @FieldDesc
    private int tradeCoinType;       //交易的是货币其类型

    @FieldDesc
    private int tradeNum;            //交易的数量

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        return data;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public int getItemOrCoin() {
        return itemOrCoin;
    }

    public void setItemOrCoin(int itemOrCoin) {
        this.itemOrCoin = itemOrCoin;
    }

    public String getTradeItem() {
        return tradeItem;
    }

    public void setTradeItem(String tradeItem) {
        this.tradeItem = tradeItem;
    }

    public int getTradeGridNum() {
        return tradeGridNum;
    }

    public void setTradeGridNum(int tradeGridNum) {
        this.tradeGridNum = tradeGridNum;
    }

    public int getTradeCoinType() {
        return tradeCoinType;
    }

    public void setTradeCoinType(int tradeCoinType) {
        this.tradeCoinType = tradeCoinType;
    }

    public int getTradeNum() {
        return tradeNum;
    }

    public void setTradeNum(int tradeNum) {
        this.tradeNum = tradeNum;
    }
}
