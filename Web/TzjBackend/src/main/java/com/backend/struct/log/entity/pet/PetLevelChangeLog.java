package com.backend.struct.log.entity.pet;


import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.CommonLogBean;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;

/**
 * 宠物升级日志
 */
@Table(name = "petlevelchangelog", tableType = TableType.Month)
public class PetLevelChangeLog extends CommonLogBean implements IConvertor {

    @FieldDesc
    private int level;            //当前等级

    @FieldDesc
    private long exp;             //当前经验

    @FieldDesc
    private long addExp;          //本次操作增加的经验

    @FieldDesc
    private String eatEquips;     //本次吞噬的装备配置表id

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        return data;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getExp() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }

    public long getAddExp() {
        return addExp;
    }

    public void setAddExp(long addExp) {
        this.addExp = addExp;
    }

    public String getEatEquips() {
        return eatEquips;
    }

    public void setEatEquips(String eatEquips) {
        this.eatEquips = eatEquips;
    }
}
