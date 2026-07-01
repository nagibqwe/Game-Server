package com.backend.struct.log.entity.achievement;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.CommonLogBean;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;

/**
 * 成就奖励领取日志
 */
@Table(name = "achievementrewardlog", tableType = TableType.Month)
public class AchievementRewardLog extends CommonLogBean implements IConvertor {

    @FieldDesc
    private int confId;         //配置表ID

    @FieldDesc
    private String itemStr;     //奖励物品

    @FieldDesc
    private int bindGold;       //获得绑元

    @FieldDesc
    private int value;          //获得成就值

    @FieldDesc
    private long actionId;      //关联ID

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        return data;
    }

    public int getConfId() {
        return confId;
    }

    public void setConfId(int confId) {
        this.confId = confId;
    }

    public String getItemStr() {
        return itemStr;
    }

    public void setItemStr(String itemStr) {
        this.itemStr = itemStr;
    }

    public int getBindGold() {
        return bindGold;
    }

    public void setBindGold(int bindGold) {
        this.bindGold = bindGold;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }
}
