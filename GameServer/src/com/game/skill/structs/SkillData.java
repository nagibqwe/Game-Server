package com.game.skill.structs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by cxl on 2020/10/12.
 */
public class SkillData {

    //key格子ID，Value等级
    private int cellLevels = 1;
    //字面意思技能
    private List<Integer> skillIds = new ArrayList<>();
    //客户端出站技能位置编排
    private String playedSkillStr = "";
    //激活的经脉列表
    private List<Integer> skillMeridianList = new ArrayList<>();
    //当前心法类型
    private int mentalType;

    private int resetTimes = 0;


    private int repairTimes = 0;


    public int getCellLevels() {
        return cellLevels;
    }

    public void setCellLevels(int cellLevels) {
        this.cellLevels = cellLevels;
    }

    public List<Integer> getSkillIds() {
        return skillIds;
    }

    public void setSkillIds(List<Integer> skillIds) {
        this.skillIds = skillIds;
    }


    public String getPlayedSkillStr() {
        return playedSkillStr;
    }

    public void setPlayedSkillStr(String playedSkillStr) {
        this.playedSkillStr = playedSkillStr;
    }

    public List<Integer> getSkillMeridianList() {
        return skillMeridianList;
    }

    public void setSkillMeridianList(List<Integer> skillMeridianList) {
        this.skillMeridianList = skillMeridianList;
    }

    public int getMentalType() {
        return mentalType;
    }

    public void setMentalType(int mentalType) {
        this.mentalType = mentalType;
    }

    public int getResetTimes() {
        return resetTimes;
    }

    public void setResetTimes(int resetTimes) {
        this.resetTimes = resetTimes;
    }

    public int getRepairTimes() {
        return repairTimes;
    }
    public void setRepairTimes(int repairTimes) {
        this.repairTimes = repairTimes;
    }

}
