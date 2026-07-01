package com.game.pet.structs;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 宠物
 */
public class Pet extends Entity{
    /**
     * 主人ID
     */
    private long ownerId;

    /**
     * 主人名字
     */
    private String ownerName;

    /**
     * 宠物打架技能
     */
    private ConcurrentHashMap<Integer, Skill> baseSkills = new ConcurrentHashMap<>();

    /**
     * 宠物带给玩家的手动技能
     */
    private int manualSkill;

    /**
     * 宠物的被动技能
     */
    private List<Integer> passivitySkill = new ArrayList<>();

    /**
     * 技能冷却列表
     */
    @JsonIgnore
    private transient ConcurrentHashMap<String, Cooldown> coolDown = new ConcurrentHashMap<>();

    /**
     * 阶数
     */
    private int stage = 1;

    ////////////////////////////重写的方法/////////////////////////////////////////////////////////

    @Override
    public boolean isNeedCheckCanMove() {
        return false;
    }

    @Override
    public int getType() {
        return Fighter.PET_TYPE;
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
        return baseSkills;
    }

    @Override
    public long getFightPoint() {
        return Manager.playerAttAttributeManager.deal().calcFightPower(attribute) ;//总战力
    }

    @Override
    public boolean isDie() {
        return false;
    }

    ///////////////////////////getter and setter/////////////////////////////////////////////

    /**
     * 获取 主人ID
     *
     * @return ownerId 主人ID
     */
    public long getOwnerId() {
        return this.ownerId;
    }

    /**
     * 设置 主人ID
     *
     * @param ownerId 主人ID
     */
    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * 获取 宠物打架技能
     *
     * @return baseSkills 宠物打架技能
     */
    public ConcurrentHashMap<Integer, Skill> getBaseSkills() {
        return this.baseSkills;
    }

    /**
     * 设置 宠物打架技能
     *
     * @param baseSkills 宠物打架技能
     */
    public void setBaseSkills(ConcurrentHashMap<Integer, Skill> baseSkills) {
        this.baseSkills = baseSkills;
    }

    /**
     * 获取 宠物带给玩家的手动技能
     *
     * @return manualSkill 宠物带给玩家的手动技能
     */
    public int getManualSkill() {
        return this.manualSkill;
    }

    /**
     * 设置 宠物带给玩家的手动技能
     *
     * @param manualSkill 宠物带给玩家的手动技能
     */
    public void setManualSkill(int manualSkill) {
        this.manualSkill = manualSkill;
    }

    /**
     * 获取 宠物的被动技能
     *
     * @return passivitySkill 宠物的被动技能
     */
    public List<Integer> getPassivitySkill() {
        return this.passivitySkill;
    }

    /**
     * 设置 宠物的被动技能
     *
     * @param passivitySkill 宠物的被动技能
     */
    public void setPassivitySkill(List<Integer> passivitySkill) {
        this.passivitySkill = passivitySkill;
    }

    /**
     * 获取 技能冷却列表
     *
     * @return coolDown 技能冷却列表
     */
    public ConcurrentHashMap<String, Cooldown> getCoolDown() {
        return this.coolDown;
    }

    /**
     * 设置 技能冷却列表
     *
     * @param coolDown 技能冷却列表
     */
    public void setCoolDown(ConcurrentHashMap<String, Cooldown> coolDown) {
        this.coolDown = coolDown;
    }

    /**
     * 获取 阶数
     *
     * @return stage 阶数
     */
    public int getStage() {
        return this.stage;
    }

    /**
     * 设置 阶数
     *
     * @param stage 阶数
     */
    public void setStage(int stage) {
        this.stage = stage;
    }

    /**
     * 获取 主人名字
     *
     * @return ownerName 主人名字
     */
    public String getOwnerName() {
        return this.ownerName;
    }

    /**
     * 设置 主人名字
     *
     * @param ownerName 主人名字
     */
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    @Override
    public void release() {

    }
}
