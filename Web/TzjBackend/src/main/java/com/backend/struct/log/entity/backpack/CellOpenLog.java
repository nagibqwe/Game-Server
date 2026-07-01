package com.backend.struct.log.entity.backpack;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.CommonLogBean;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;

/**
 * 背包、仓库格子开启日志
 */
@Table(name = "cellopenlog", tableType = TableType.Month)
public class CellOpenLog extends CommonLogBean implements IConvertor {

    @FieldDesc
    private byte type;              //类型：1包裹、2仓库

    @FieldDesc
    private int resumeGold;         //花费元宝

    @FieldDesc
    private byte actionType;        //开启类型：1 元宝开启  0时间开启

    @FieldDesc
    private int beforeCells;        //开启前格子数

    @FieldDesc
    private int afterCells;         //开启后格子数

    @FieldDesc
    private long actionId;          //关联ID

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        return data;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public int getResumeGold() {
        return resumeGold;
    }

    public void setResumeGold(int resumeGold) {
        this.resumeGold = resumeGold;
    }

    public byte getActionType() {
        return actionType;
    }

    public void setActionType(byte actionType) {
        this.actionType = actionType;
    }

    public int getBeforeCells() {
        return beforeCells;
    }

    public void setBeforeCells(int beforeCells) {
        this.beforeCells = beforeCells;
    }

    public int getAfterCells() {
        return afterCells;
    }

    public void setAfterCells(int afterCells) {
        this.afterCells = afterCells;
    }

    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }
}
