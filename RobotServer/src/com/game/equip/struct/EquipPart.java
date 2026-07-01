package com.game.equip.struct;

import com.data.CfgManager;
import com.data.bean.Cfg_Equip_Bean;

import java.util.HashMap;

/**
 * 该类代表装备部位，所有和部位相关的信息均在该类
 * */
public class EquipPart {
    /**
     * 每个部位的id，该id只代表玩家部位
     * 取值范围 0-8
     * */
    private int type;
    /**
     * 当前部位强化等级
     * */
    private int level;
    /**
     * 当前熟练度/经验
     * */
    private int currentExp;
    /**
     * 穿戴的装备
     * */
    private Equip equip;

    /**
     * 装备洗练次数
     */
    private int washNum = 0;



    public int getLevel() {
        return level;
    }

    public int getWashNum() {
        return washNum;
    }

    public void setWashNum(int washNum) {
        this.washNum = washNum;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void addLevel(int value) {
        this.level += value;
    }

    public void setCurrentExp(int currentExp) {
        this.currentExp = currentExp;
    }

    public int getCurrentExp() {
        return currentExp;
    }

    public void addCurrentExp(int value) {
        this.currentExp += value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    /**
     * 获取强化配置表id
     * */
    public int getStrengthenExcelId() {
        /**
         * 取部位等级和装备支持的强化等级上限两个值的最小者
         * */
        int tempLevel = -1;
        if(null != equip) {
            Cfg_Equip_Bean bean = CfgManager.getCfg_Equip_Container().getValueByKey(equip.getItemModelId());
            if(null != bean) {
                tempLevel = level > bean.getLevel_max() ? bean.getLevel_max() : level;
            }
        }
        return (type + EquipPartBaseType.PLAYER) * 1000 + tempLevel;
    }

    public Equip getEquip() {
        return equip;
    }

    public void setEquip(Equip equip) {
        this.equip = equip;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EquipPart{");
        builder.append("type=");
        builder.append(type);
        builder.append(", level=");
        builder.append(level);
        builder.append(", currentExp=");
        builder.append(currentExp);
        builder.append(", equip=");
        builder.append(equip);
        builder.append('}');
        return builder.toString();
    }
}
