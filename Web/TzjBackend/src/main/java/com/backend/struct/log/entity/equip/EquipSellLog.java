package com.backend.struct.log.entity.equip;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.CommonLogBean;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;

/**
 * 装备出售日志
 */
@Table(name = "equipselllog", tableType = TableType.Month)
public class EquipSellLog extends CommonLogBean implements IConvertor {

    @FieldDesc
    private String ids;             //装备出售的Id

    @FieldDesc
    private String equipInfo;       //出售装备详情

    @FieldDesc
    private long actionId;          //关联ID

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        return data;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getEquipInfo() {
        return equipInfo;
    }

    public void setEquipInfo(String equipInfo) {
        this.equipInfo = equipInfo;
    }

    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }
}
