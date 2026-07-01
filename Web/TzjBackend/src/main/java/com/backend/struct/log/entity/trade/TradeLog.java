package com.backend.struct.log.entity.trade;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;

/**
 * 交易日志
 */
@Table(name = "tradelog", tableType = TableType.Month)
public class TradeLog implements IConvertor {

    @FieldDesc
    private int tradeAction;              //交易动作，1：交易申请，2：交易取消，3：交易锁定，4：交易失败，5：交易成功，6：交易异常(交易中掉线情况)

    @FieldDesc
    private long aPlayerId;               //交易A玩家Id

    @FieldDesc
    private String aPlayerName;           //交易A玩家Name

    @FieldDesc
    private long bPlayerId;               //交易B玩家Id

    @FieldDesc
    private String bPlayerName;           //交易B玩家Name

    @FieldDesc
    private String aItemList;             //A玩家交易的物品列表，不包括货币

    @FieldDesc
    private int aGoldNum;                 //A玩家交易的非绑元宝数量

    @FieldDesc
    private int aMoneyNum;                //A玩家交易的非绑金币数量

    @FieldDesc
    private String bItemList;             //B玩家交易的物品列表，不包括货币

    @FieldDesc
    private int bGoldNum;                 //B玩家交易的非绑元宝数量

    @FieldDesc
    private int bMoneyNum;                //B玩家交易的非绑金币数量

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        return data;
    }
}
