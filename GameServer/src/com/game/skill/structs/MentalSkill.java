package com.game.skill.structs;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by cxl on 2019/12/16.
 * 心法技能
 */
public class MentalSkill {


    private int mentalID ;//心法ID

    private int baseSkillID;//默认基础技能

    private ConcurrentHashMap<Integer,Integer> skillID = new ConcurrentHashMap<>();


    public int getMentalID() {
        return mentalID;
    }

    public void setMentalID(int mentalID) {
        this.mentalID = mentalID;
    }


    public int getBaseSkillID() {
        return baseSkillID;
    }

    public void setBaseSkillID(int baseSkillID) {
        this.baseSkillID = baseSkillID;
    }

    public ConcurrentHashMap<Integer,Integer> getSkillID() {
        return skillID;
    }

    public void setSkillID(ConcurrentHashMap<Integer,Integer> skillID) {
        this.skillID = skillID;
    }
}
