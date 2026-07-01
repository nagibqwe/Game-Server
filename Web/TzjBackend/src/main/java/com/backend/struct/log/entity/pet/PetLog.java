package com.backend.struct.log.entity.pet;


import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.CommonLogBean;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;

/**
 * 宠物激活强化日志
 */
@Table(name = "petlog", tableType = TableType.Month)
public class PetLog extends CommonLogBean implements IConvertor {

    @FieldDesc
    private int actionType;       //操作动作，1：激活，2：强化

    @FieldDesc
    private int petId;            //操作的宠物模型Id

    @FieldDesc
    private int stage;            //宠物的阶数

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        return data;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }
}
