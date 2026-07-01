package com.game.skill.structs;

import java.util.ArrayList;
import java.util.List;

public class SkillCellAtt {

    private int id ;                                     //技能格子Id

    private int level;                                   //当前格子等级

    private int pos;                                     //当前技能位置

    private int curBranch;                               //当前穿戴的分支

    private int lastId;                                  //上次的格子Id

    private List<Integer> ownBranch = new ArrayList<>(); //当前拥有的分支

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getCurBranch() {
        return curBranch;
    }

    public void setCurBranch(int curBranch) {
        this.curBranch = curBranch;
    }

    public int getLastId() {
        return lastId;
    }

    public void setLastId(int lastId) {
        this.lastId = lastId;
    }

    public List<Integer> getOwnBranch() {
        return ownBranch;
    }

    public void setOwnBranch(List<Integer> ownBranch) {
        this.ownBranch = ownBranch;
    }
}
