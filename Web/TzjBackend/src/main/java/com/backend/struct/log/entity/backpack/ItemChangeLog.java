package com.backend.struct.log.entity.backpack;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.CommonLogBean;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;
import com.backend.manager.ItemManager;

import java.util.Map;

/**
 * 物品变化日志
 */
@Table(name = "itemchangelog", tableType = TableType.Month)
public class ItemChangeLog extends CommonLogBean implements IConvertor {

    @FieldDesc
    private int roleLevel;          //角色等级

    @FieldDesc(selectKey = true)
    private long itemId;            //物品ID

    @FieldDesc(selectKey = true)
    private int modelId;            //物品模型ID

    @FieldDesc
    private int changeNum;          //变化的数量

    @FieldDesc
    private int oldNum;             //变化前的数量

    @FieldDesc
    private int newNum;             //变化后的数量

    @FieldDesc(selectKey = true)
    private int reason;             //原因码

    @FieldDesc
    private long actionId;          //关联ID

    @FieldDesc
    private String changeAction;    //变更类型

    @FieldDesc
    private int coinType;           //消耗货币类型

    @FieldDesc
    private float costNum;          //消耗货币数量

    @FieldDesc(show = false)
    private int cellId;             //格子位置

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        Integer itemModelId = Integer.valueOf(data.get("modelId"));
        data.put("modelId", ItemManager.getInstance().getItemName(itemModelId));
        return data;
    }

    public int getRoleLevel() {
        return roleLevel;
    }

    public void setRoleLevel(int roleLevel) {
        this.roleLevel = roleLevel;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public int getChangeNum() {
        return changeNum;
    }

    public void setChangeNum(int changeNum) {
        this.changeNum = changeNum;
    }

    public int getOldNum() {
        return oldNum;
    }

    public void setOldNum(int oldNum) {
        this.oldNum = oldNum;
    }

    public int getNewNum() {
        return newNum;
    }

    public void setNewNum(int newNum) {
        this.newNum = newNum;
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }

    public String getChangeAction() {
        return changeAction;
    }

    public void setChangeAction(String changeAction) {
        this.changeAction = changeAction;
    }

    public int getCoinType() {
        return coinType;
    }

    public void setCoinType(int coinType) {
        this.coinType = coinType;
    }

    public float getCostNum() {
        return costNum;
    }

    public void setCostNum(float costNum) {
        this.costNum = costNum;
    }

    public int getCellId() {
        return cellId;
    }

    public void setCellId(int cellId) {
        this.cellId = cellId;
    }
}
