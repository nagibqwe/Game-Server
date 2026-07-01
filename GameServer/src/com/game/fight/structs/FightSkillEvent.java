package com.game.fight.structs;

import com.game.skill.config.SkillEvent;
import com.game.skill.structs.Skill;
import com.game.structs.Fighter;
import game.core.map.Position;

import java.util.List;

/**
 * Created by soko(xysoko@qq.com) on 2018/9/10. copyright 巨匠@雨墨
 */
public class FightSkillEvent {
    //地图ID值
    private long mapId;
    //开始执行的时间
    private long startTime;
    //事件体
    private SkillEvent se;

    //同步方向
    private Position dirPos;
    //技能ID
    private Skill skill;
    //目标群体
    private List<Fighter> targets;

    //加入事件的名字
    private String skillVisualName ="";

    //事件执行序列值
    private int serial = 0;

    //客户端召唤物的位置
    private int [] mgpos = {0,0};

    //是否对玩家产生特效
    private boolean canPlayerValid = true;

    //是否是被动技能触发的
    private boolean copy;

    public boolean isCopy() {
        return copy;
    }

    public void setCopy(boolean copy) {
        this.copy = copy;
    }

    //技能效果是否对玩家有效
    public boolean isCanPlayerValid(){ return canPlayerValid;}

    public void setCanPlayerValid(boolean canPlayerValid) {
        this.canPlayerValid = canPlayerValid;
    }

    public String getSkillVisualName() {
        return skillVisualName;
    }

    public void setSkillVisualName(String skillVisualName) {
        this.skillVisualName = skillVisualName;
    }

    public List<Fighter> getTargets() {
        return targets;
    }

    public void setTargets(List<Fighter> targets) {
        this.targets = targets;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public Position getDirPos() {
        return dirPos;
    }

    public void setDirPos(Position dirPos) {
        this.dirPos = dirPos;
    }

    public long getMapId() {
        return mapId;
    }

    public void setMapId(long mapId) {
        this.mapId = mapId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public SkillEvent getSe() {
        return se;
    }

    public void setSe(SkillEvent se) {
        this.se = se;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public int[] getMgpos() {
        return mgpos;
    }

    public int getX(){
        return mgpos[0];
    }
    public int getY(){
        return mgpos[1];
    }
}
