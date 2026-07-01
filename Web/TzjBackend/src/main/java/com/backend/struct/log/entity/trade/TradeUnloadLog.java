package com.backend.struct.log.entity.trade;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;

/**
 * 交易从交易栏卸下交易物品到背包中的log
 */
@Table(name = "tradeunloadlog", tableType = TableType.Month)
public class TradeUnloadLog implements IConvertor {

    @FieldDesc
    private long playerId;           //玩家Id

    @FieldDesc
    private int unloadGridNum;       //卸下的是物品则所在交易栏中的编号

    @FieldDesc
    private String unloadItem;       //卸下的物品(卸下的Item实例JSON化后的字串)

    @FieldDesc
    private int unloadGoldNum;       //卸下的非绑元宝数量

    @FieldDesc
    private int unloadMoneyNum;      //卸下的非绑金币数量

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

    public int getUnloadGridNum() {
        return unloadGridNum;
    }

    public void setUnloadGridNum(int unloadGridNum) {
        this.unloadGridNum = unloadGridNum;
    }

    public String getUnloadItem() {
        return unloadItem;
    }

    public void setUnloadItem(String unloadItem) {
        this.unloadItem = unloadItem;
    }

    public int getUnloadGoldNum() {
        return unloadGoldNum;
    }

    public void setUnloadGoldNum(int unloadGoldNum) {
        this.unloadGoldNum = unloadGoldNum;
    }

    public int getUnloadMoneyNum() {
        return unloadMoneyNum;
    }

    public void setUnloadMoneyNum(int unloadMoneyNum) {
        this.unloadMoneyNum = unloadMoneyNum;
    }
}
