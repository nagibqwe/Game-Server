package com.game.pet.structs;

import com.game.nature.structs.Nature;
import com.game.player.structs.Player;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 宠物相关数据
 */
public class ActivePet {
    /**
     * 玩家激活的宠物列表<宠物模型Id, Pet>
     */
    private ConcurrentHashMap<Integer, Pet> pets = new ConcurrentHashMap<>();

    /**
     * 出战的宠物模型Id，为0时表示没有出战的宠物
     */
    private int fightPet;

    /**
     * 宠物等级（所有宠物共享）
     */
    private int level = 1;

    /**
     * 宠物经验
     */
    private long exp;

    /**
     * 宠物御魂数据
     * */
    private Nature nature = new Nature();

    /**
     * 宠物助阵数据 助阵位置id-助阵数据
     * */
    private ConcurrentHashMap<Integer, PetAssistant> assistants = new ConcurrentHashMap<>();

    /**
     * 是否保存了宠物自动分解
     * */
    private boolean autoDecompose = false;

    //////////////////////////////getter and setter//////////////////////////////////////////////

    /**
     * 获取 玩家激活的宠物列表<宠物模型Id Pet>
     *
     * @return pets 玩家激活的宠物列表<宠物模型Id Pet>
     */
    public ConcurrentHashMap<Integer, Pet> getPets() {
        return this.pets;
    }

    /**
     * 设置 玩家激活的宠物列表<宠物模型Id Pet>
     *
     * @param pets 玩家激活的宠物列表<宠物模型Id Pet>
     */
    public void setPets(ConcurrentHashMap<Integer, Pet> pets) {
        this.pets = pets;
    }

    /**
     * 获取 出战的宠物模型Id，为0时表示没有出战的宠物
     *
     * @return fightPet 出战的宠物模型Id，为0时表示没有出战的宠物
     */
    public int getFightPet() {
        return this.fightPet;
    }

    /**
     * 设置 出战的宠物模型Id，为0时表示没有出战的宠物
     *
     * @param fightPet 出战的宠物模型Id，为0时表示没有出战的宠物
     */
    public void setFightPet(int fightPet) {
        this.fightPet = fightPet;
    }

    /**
     * 获取 宠物等级（所有宠物共享）
     *
     * @return level 宠物等级（所有宠物共享）
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * 设置 宠物等级（所有宠物共享）
     *
     * @param level 宠物等级（所有宠物共享）
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * 获取 宠物经验
     *
     * @return exp 宠物经验
     */
    public long getExp() {
        return this.exp;
    }

    /**
     * 设置 宠物经验
     *
     * @param exp 宠物经验
     */
    public void setExp(long exp) {
        this.exp = exp;
    }

    public Nature getNature() {
        return nature;
    }

    public void setNature(Nature nature) {
        this.nature = nature;
    }

    public ConcurrentHashMap<Integer, PetAssistant> getAssistants() {
        return assistants;
    }

    public void setAssistants(ConcurrentHashMap<Integer, PetAssistant> assistants) {
        this.assistants = assistants;
    }

    public boolean isAutoDecompose() {
        return autoDecompose;
    }

    public void setAutoDecompose(boolean autoDecompose) {
        this.autoDecompose = autoDecompose;
    }

    public int getEquipLevelTotal() {
        int total = 0;
        for(PetAssistant a : assistants.values()){
            for(PetEquipPart p : a.getParts().values()){
                total += p.getStrengthLv();
            }
        }
        return total;
    }
}
