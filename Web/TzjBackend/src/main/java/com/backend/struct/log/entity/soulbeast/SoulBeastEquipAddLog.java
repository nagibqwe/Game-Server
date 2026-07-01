package com.backend.struct.log.entity.soulbeast;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.CommonLogBean;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;

/**
 * 魂兽装备增加日志
 */
@Table(name = "soulbeastequipaddlog", tableType = TableType.Month)
public class SoulBeastEquipAddLog extends CommonLogBean implements IConvertor {

    @FieldDesc
    private int type;               //类型 1：是装备 2：是道具

    @FieldDesc
    private int modelItemId;        //物品ID

    @FieldDesc
    private int color;              //颜色

    @FieldDesc
    private int star;               //星级

    @FieldDesc
    private int part;               //部位

    @FieldDesc
    private int reason;             //增加原因

    @FieldDesc
    private String extraAttribute;  //额外属性

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        return data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getModelItemId() {
        return modelItemId;
    }

    public void setModelItemId(int modelItemId) {
        this.modelItemId = modelItemId;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getPart() {
        return part;
    }

    public void setPart(int part) {
        this.part = part;
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public String getExtraAttribute() {
        return extraAttribute;
    }

    public void setExtraAttribute(String extraAttribute) {
        this.extraAttribute = extraAttribute;
    }
}
