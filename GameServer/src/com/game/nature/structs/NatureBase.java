package com.game.nature.structs;

import com.data.bean.Cfg_Skill_Bean;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.cooldown.structs.Cooldown;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.skill.structs.Skill;
import com.game.structs.Entity;
import com.game.structs.Fighter;
import com.game.structs.ServerStr;
import game.core.map.IMapObject;
import game.core.map.Position;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class NatureBase extends Entity {
    /**
     * 主人id
     */
    private long ownerId;
    /**
     * 打架技能
     */
    private ConcurrentHashMap<Integer, Skill> skills = new ConcurrentHashMap<>();
    /**
     * 技能冷却列表
     */
    @JsonIgnore
    private transient ConcurrentHashMap<String, Cooldown> coolDown = new ConcurrentHashMap<>();
    /**
     * NatureXXX表中的id
     * */
    private int currentId;
    /**
     * 当前的模型id，可以是NatureXXX.xlsx中的id，也可以是HuaxingXXX.xlsx中的id
     * */
    private int currentModelId;
    /**
     * 激活的技能的列表
     * */
    private ConcurrentHashMap<Integer, Integer> activeSkillMap = new ConcurrentHashMap<>();

    /**
     * 激活的翅膀模型的列表，这里的模型是在NatureXXX.xlsx中
     * */
    private Set<Integer> activeNatureModelSet = new HashSet<>();
    /**
     * huaxin映射表
     * key是huaxingXXX.xlsx中的id，value是Huaxin实例
     * */
    private ConcurrentHashMap<Integer, Huaxin> huaxins = new ConcurrentHashMap<>();
    /**
     * 总战力
     * */
    private int power = 0;
    /**
     * 是否显示穿戴,true:显示,false:不显示
     * */
    private boolean wearShow;

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public void setSkills(ConcurrentHashMap<Integer, Skill> skills) {
        this.skills = skills;
    }

    public ConcurrentHashMap<String, Cooldown> getCoolDown() {
        return coolDown;
    }

    public void setCoolDown(ConcurrentHashMap<String, Cooldown> coolDown) {
        this.coolDown = coolDown;
    }

    public int getCurrentId() {
        return currentId;
    }

    public void setCurrentId(int currentId) {
        this.currentId = currentId;
    }

    public ConcurrentHashMap<Integer, Integer> getActiveSkillMap() {
        return activeSkillMap;
    }

    public void setActiveSkillMap(ConcurrentHashMap<Integer, Integer> activeSkillMap) {
        this.activeSkillMap = activeSkillMap;
    }

    public int getCurrentModelId() {
        return currentModelId;
    }

    public void setCurrentModelId(int currentModelId) {
        this.currentModelId = currentModelId;
    }

    public Set<Integer> getActiveNatureModelSet() {
        return activeNatureModelSet;
    }

    public void setActiveNatureModelSet(Set<Integer> activeNatureModelSet) {
        this.activeNatureModelSet = activeNatureModelSet;
    }

    public ConcurrentHashMap<Integer, Huaxin> getHuaxins() {
        return huaxins;
    }

    public void setHuaxins(ConcurrentHashMap<Integer, Huaxin> huaxins) {
        this.huaxins = huaxins;
    }

    public void updateWingId(int value) {
        this.currentId += value;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void addPower(int value) {
        this.power += value;
    }

    public boolean isWearShow() {
        return wearShow;
    }

    public void setWearShow(boolean wearShow) {
        this.wearShow = wearShow;
    }

    //重写的方法
    @Override
    public boolean isNeedCheckCanMove() {
        return false;
    }

    @Override
    public int getType() {
        return Fighter.NATURE_TYPE;
    }

    @Override
    public String getName() {
        return ServerStr.getChatTableName(super.getName());
    }

    @Override
    public String getSrcName() {
        return null;
    }

    @Override
    public boolean canSee(IMapObject player) {
        return true;
    }

    @Override
    public HashMap<Integer, List<Integer>> gainHideTaskIds() {
        return null;
    }

    @Override
    public void changeCurPos(Position pos) {
        curGps.setPos(pos);
    }

    @Override
    public void changeCurPos(Position pos, boolean isBroadcast) {
        curGps.setPos(pos);
    }

    @Override
    public void doDie(Fighter attacker) {

    }

    @Override
    public void beAttack(Fighter attacker, Cfg_Skill_Bean skill, int skillLevel, long damage) {

    }

    @Override
    public void onHpChange(Fighter attacker) {

    }

    @Override
    public String toString() {
        return "name:" + this.name + "_modelID:" + this.modelId + " id=" + this.getId() + " owner=" + this.ownerId;
    }

    @Override
    public void reset() {
        this.clearHatred();
        this.resetState();
        this.getFightStates().clear();
        this.setCurHp(this.getAttribute().MaxHP());
        Manager.cooldownManager.cleanAllCooldown(this);
    }

    @Override
    public int gainFinalMoveSpeed() {
        Player owner = Manager.playerManager.getPlayerOnline(ownerId);
        if (owner == null) {
            return 0;
        }
        return owner.gainFinalMoveSpeed();
    }

    @Override
    public long getBrithProtect() {
        return 0;
    }

    @Override
    public long getFixDecHp() {
        return 0;
    }

    @Override
    public ConcurrentHashMap<String, Cooldown> getCooldowns() {
        return coolDown;
    }

    @Override
    public ConcurrentHashMap<Integer, Skill> getSkills() {
        return skills;
    }

    @Override
    public long getFightPoint() {
        return Manager.playerAttAttributeManager.deal().calcFightPower(attribute); //总战力
    }

    @Override
    public boolean isDie() {
        return false;
    }

    @Override
    public void release() {

    }
}
