package com.backend.struct.log.entity.equip;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.CommonLogBean;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;

/**
 * 装备培养日志
 */
@Table(name = "equipwashlog", tableType = TableType.Month)
public class EquipWashLog extends CommonLogBean implements IConvertor {

    @FieldDesc
    private long equipId;               //装备ID

    @FieldDesc
    private int part;                   //部位ID

    @FieldDesc
    private String wash;                //洗练结果

    @FieldDesc
    private String pos;                 //洗练锁定位置

    @FieldDesc
    private long actionId;              //关联ID

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        return data;
    }

    public long getEquipId() {
        return equipId;
    }

    public void setEquipId(long equipId) {
        this.equipId = equipId;
    }

    public int getPart() {
        return part;
    }

    public void setPart(int part) {
        this.part = part;
    }

    public String getWash() {
        return wash;
    }

    public void setWash(String wash) {
        this.wash = wash;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }
}
