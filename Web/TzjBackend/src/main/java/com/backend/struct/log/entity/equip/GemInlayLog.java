package com.backend.struct.log.entity.equip;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.CommonLogBean;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;
/**
 * 宝石镶嵌日志
 */
@Table(name = "geminlaylog", tableType = TableType.Month)
public class GemInlayLog extends CommonLogBean implements IConvertor {

    @FieldDesc
    private int part;               //部位id,0~7

    @FieldDesc
    private int type;               //类型：1宝石 2仙玉

    @FieldDesc
    private int gemIndex;           //宝石位置，从0开始

    @FieldDesc
    private int operateType;        //操作类型：0开启孔位，1宝石镶嵌，2宝石替换，3宝石升级, 4宝石卸下

    @FieldDesc
    private int lastId;             //操作前宝石或仙玉的id，-1表示孔位未开启

    @FieldDesc
    private int nowId;              //宝石或仙玉的id，0表示孔位开启

    @FieldDesc
    private long actionId;          //关联ID

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        return data;
    }

    public int getPart() {
        return part;
    }

    public void setPart(int part) {
        this.part = part;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getGemIndex() {
        return gemIndex;
    }

    public void setGemIndex(int gemIndex) {
        this.gemIndex = gemIndex;
    }

    public int getOperateType() {
        return operateType;
    }

    public void setOperateType(int operateType) {
        this.operateType = operateType;
    }

    public int getLastId() {
        return lastId;
    }

    public void setLastId(int lastId) {
        this.lastId = lastId;
    }

    public int getNowId() {
        return nowId;
    }

    public void setNowId(int nowId) {
        this.nowId = nowId;
    }

    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }
}
