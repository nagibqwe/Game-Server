package com.game.nature.structs;

import com.data.bean.Cfg_Skill_Bean;
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
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by 542 on 2020/5/21.
 */
public class HuaxinEntity  extends Entity {

    /**
     * 主人id
     */
    private long ownerId;
    /**
     *配置表ID
     * */
    private int excelId;

    /**
     * 打架技能
     */
    private ConcurrentHashMap<Integer, Skill> baseSkills = new ConcurrentHashMap<>();
    /**
     * 技能冷却列表
     */
    private transient ConcurrentHashMap<String, Cooldown> coolDown = new ConcurrentHashMap<>();


    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public int getExcelId() {
        return excelId;
    }

    public void setExcelId(int excelId) {
        this.excelId = excelId;
    }

    public ConcurrentHashMap<Integer, Skill> getBaseSkills() {
        return baseSkills;
    }

    public void setBaseSkills(ConcurrentHashMap<Integer, Skill> baseSkills) {
        this.baseSkills = baseSkills;
    }

    public ConcurrentHashMap<String, Cooldown> getCoolDown() {
        return coolDown;
    }

    public void setCoolDown(ConcurrentHashMap<String, Cooldown> coolDown) {
        this.coolDown = coolDown;
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
        return super.getName();
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
        return Manager.playerAttAttributeManager.deal().calcFightPower(attribute);//总战力
    }

    @Override
    public boolean isDie() {
        return false;
    }

    @Override
    public void release() {

    }

}
