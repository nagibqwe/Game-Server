package com.backend.struct.log.entity.equip;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.CommonLogBean;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;

/**
 * 宝石精炼日志
 */
@Table(name = "gemrefinelog", tableType = TableType.Month)
public class GemRefineLog extends CommonLogBean implements IConvertor {

    @FieldDesc
    private int part;               //部位id,0~7

    @FieldDesc
    private int operateType;        //操作类型：0快速精炼 1智能精炼

    @FieldDesc
    private int lastLevel;          //精炼前等级

    @FieldDesc
    private int nowLevel;           //精炼后等级

    @FieldDesc
    private long lastExp;           //精炼前经验

    @FieldDesc
    private long nowExp;            //精炼后经验

    @FieldDesc
    private long addExp;            //增加的经验

    @FieldDesc
    private long actionId;          //操作ID

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

    public int getOperateType() {
        return operateType;
    }

    public void setOperateType(int operateType) {
        this.operateType = operateType;
    }

    public int getLastLevel() {
        return lastLevel;
    }

    public void setLastLevel(int lastLevel) {
        this.lastLevel = lastLevel;
    }

    public int getNowLevel() {
        return nowLevel;
    }

    public void setNowLevel(int nowLevel) {
        this.nowLevel = nowLevel;
    }

    public long getLastExp() {
        return lastExp;
    }

    public void setLastExp(long lastExp) {
        this.lastExp = lastExp;
    }

    public long getNowExp() {
        return nowExp;
    }

    public void setNowExp(long nowExp) {
        this.nowExp = nowExp;
    }

    public long getAddExp() {
        return addExp;
    }

    public void setAddExp(long addExp) {
        this.addExp = addExp;
    }

    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }
}
