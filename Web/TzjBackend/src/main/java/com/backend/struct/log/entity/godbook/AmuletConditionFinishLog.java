package com.backend.struct.log.entity.godbook;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;

/**
 * 符咒条件达成日志
 */
@Table(name = "amuletconditionfinishlog", tableType = TableType.Month)
public class AmuletConditionFinishLog implements IConvertor {

    @FieldDesc
    private int conditionId;        //符咒条件配置表ID

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        return data;
    }

    public int getConditionId() {
        return conditionId;
    }

    public void setConditionId(int conditionId) {
        this.conditionId = conditionId;
    }
}
