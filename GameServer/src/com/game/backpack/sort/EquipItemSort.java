package com.game.backpack.sort;

import com.data.CfgManager;
import com.data.bean.Cfg_Equip_Bean;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.attribute.BaseIntAttribute;
import com.game.backpack.structs.Equip;
import com.game.player.manager.PlayerAttributeManager;
import com.game.structs.AttributeType;

import java.util.Comparator;

/**
 * Created by 瞿冰冰
 * 2019/8/12
 */
public class EquipItemSort implements Comparator<Equip> {


    @Override
    public int compare(Equip one, Equip two) {
        int count = 7;//比较的条件数量
        int fightPower = getFightPower(two) - getFightPower(one);
        if(fightPower > 0){
            return count -1;
        }else if(fightPower < 0){
            return -1;
        }
        int grade = getEquipGrade(two) - getEquipGrade(one);
        if(grade > 0){
            return count -2;
        }else if(grade < 0){
            return -2;
        }
        int equaip = getEquipQuality(two) - getEquipQuality(one);
        if(equaip > 0){
            return count -3;
        }else if(equaip < 0){
            return -3;
        }
        int diamond = getEquipDiamond(two) - getEquipDiamond(one);
        if(diamond > 0){
            return count -4;
        }else if(diamond < 0){
            return -4;
        }
        int part = getEquipPart(two) - getEquipPart(one);
        if(part > 0){
            return count -5;
        }else if(part <0){
            return -5;
        }
        int modelId = two.getItemModelId() - one.getItemModelId();
        if(modelId > 0){
            return count -6;
        }else if(modelId <0){
            return -6;
        }
        return 0;
    }

    /**
     * 获取装备品质
     * */
    public int getEquipQuality(Equip equip) {
        if(null == equip) {
            return 0;
        }
        Cfg_Equip_Bean bean = CfgManager.getCfg_Equip_Container().getValueByKey(equip.getItemModelId());
        if(null == bean) {
            return 0;
        }
        return bean.getQuality();
    }
    /**
     * 获取装备品阶
     * */
    public int getEquipGrade(Equip equip) {
        if(null == equip) {
            return 0;
        }
        Cfg_Equip_Bean bean = CfgManager.getCfg_Equip_Container().getValueByKey(equip.getItemModelId());
        if(null == bean) {
            return 0;
        }
        return bean.getGrade();
    }
    /**
     * 获取装备品阶
     * */
    public int getFightPower(Equip equip) {
        if(null == equip) {
            return 0;
        }
        Cfg_Equip_Bean bean = CfgManager.getCfg_Equip_Container().getValueByKey(equip.getItemModelId());
        if(null == bean) {
            return 0;
        }
        return bean.getScore();
    }
    /**
     * 获取装备钻石
     * */
    public int getEquipDiamond(Equip equip) {
        if(null == equip) {
            return 0;
        }
        Cfg_Equip_Bean bean = CfgManager.getCfg_Equip_Container().getValueByKey(equip.getItemModelId());
        if(null == bean) {
            return 0;
        }
        return bean.getDiamond_Number();
    }
    /**
     * 获取装备部位
     * */
    public int getEquipPart(Equip equip) {
        if(null == equip) {
            return 0;
        }
        Cfg_Equip_Bean bean = CfgManager.getCfg_Equip_Container().getValueByKey(equip.getItemModelId());
        if(null == bean) {
            return 0;
        }
        return bean.getPart();
    }
}

