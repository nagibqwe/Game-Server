package com.game.holyEquip.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.CommonLogBean;

/**
 * Created by CXL on 2019/10/31.
 */
public class HolyEquipLog extends CommonLogBean {

    private int modelItemId;        //物品ID
    private int color;              //颜色
    private int grade;              //阶
    private int part;               //部位
    private int changeNum;             //改变数量 大于0增加 小于0减少
    private int reason;             //增加原因

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

    @Log(logField = "grade", fieldType = "int", index = "0")
    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
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

    @Log(logField = "changeNum", fieldType = "int", index = "0")
    public int getChangeNum() {
        return changeNum;
    }

    public void setChangeNum(int changeNum) {
        this.changeNum = changeNum;
    }
}
