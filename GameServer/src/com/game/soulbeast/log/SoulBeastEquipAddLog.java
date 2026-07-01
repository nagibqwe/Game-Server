package com.game.soulbeast.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.CommonLogBean;

/**
 * 魂兽装备增加日志
 */
public class SoulBeastEquipAddLog extends CommonLogBean {

    private int type;               //类型 1：是装备 2：是道具
    private int modelItemId;        //物品ID
    private int color;              //颜色
    private int star;               //星级
    private int part;               //部位
    private int reason;             //增加原因
    private String extraAttribute;  //额外属性

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Override
    public void logToFile() {
        log.error(buildSql());
    }

    @Log(logField = "modelItemId", fieldType = "int", index = "0")
    public int getModelItemId() {
        return modelItemId;
    }

    public void setModelItemId(int modelItemId) {
        this.modelItemId = modelItemId;
    }

    @Log(logField = "color", fieldType = "int", index = "0")
    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Log(logField = "star", fieldType = "int", index = "0")
    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    @Log(logField = "part", fieldType = "int", index = "0")
    public int getPart() {
        return part;
    }

    public void setPart(int part) {
        this.part = part;
    }

    @Log(logField = "reason", fieldType = "int", index = "0")
    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    @Log(logField = "extraAttribute", fieldType = "varchar(500)", index = "0")
    public String getExtraAttribute() {
        return extraAttribute;
    }

    public void setExtraAttribute(String extraAttribute) {
        this.extraAttribute = extraAttribute;
    }

    @Log(logField = "type", fieldType = "int", index = "0")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
