package com.game.structs;

import com.data.bean.Cfg_Skill_Bean;
import com.game.attribute.BaseLongAttribute;
import com.game.cooldown.structs.ICoolDown;
import com.game.fight.structs.FightSkillEvent;
import com.game.revive.structs.ReviveData;
import com.game.skill.structs.SkillLockTrajectory;
import game.core.map.Position;
import game.core.map.IMapObject;
import com.game.behavior.structs.type.AttackMoveBehavior;
import com.game.buff.structs.Buff;
import com.game.fight.state.FightState;
import com.game.fight.structs.FightEnum;
import com.game.map.structs.MapGps;
import com.game.skill.structs.Skill;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 战斗实体接口
 */
public interface Fighter extends ICoolDown, IMapObject {

    byte PLAYER_TYPE = 1;
    byte PET_TYPE = 2;
    byte MONSTER_TYPE = 3;
    byte NPC_TYPE = 4;
    byte ROBOT_TYPE = 5;
    byte SKILLMAGIC_TYPE = 6;
    byte NATURE_TYPE = 7;

    /**
     * 用于实例类型
     *
     * @return
     */
    int getType();

    /**
     * 获得名字
     *
     * @return
     */
    String getName();

    /**
     * 获得原始名字
     *
     * @return
     */
    String getSrcName();

    /**
     * 设置坐标
     *
     * @param pos
     */
    void changeCurPos(Position pos);

    /**
     * @param pos
     * @param isBroadcast
     */
    void changeCurPos(Position pos, boolean isBroadcast);

    //获取当前血量
    long getCurHp();

    //设置当前血量
    void setCurHp(long curHp);

    long getCurWakan();

    void setCurWakan(long curWakan);

    //名字+ID的一个字串
    String nameIdString();

    /**
     * 获取BUFF
     *
     * @return
     */
    List<Buff> getBuffs();

    //获取受击移动事件
    List<AttackMoveBehavior> getAmbs();

    //获取把方向追击点
    Position getPursuePos(Fighter attacker, float dis);

    Position getAiPos(Fighter attacker, float dis);

    //获取召唤物列表
    List<Long> getMagics();

    /**
     * 是否死亡
     *
     * @return
     */
    boolean isDie();

    /**
     * 获取等级
     *
     * @return
     */
    int getLevel();

    /**
     * 获取战斗状态
     *
     * @return
     */
    int getFightState();

    /**
     * 增加战斗状态
     *
     * @param state
     */
    void addFightState(FightEnum state);

    /**
     * 移除战斗状态
     *
     * @param state
     */
    void removeFightState(FightEnum state);

    //获取战斗属性
    BaseLongAttribute getAttribute();

    /**
     * 死亡回调
     *
     * @param attacker
     */
    void doDie(Fighter attacker);

    /**
     * 受到伤害回答
     *
     * @param attacker   攻击者
     * @param skill      技能配置
     * @param skillLevel 技能等级
     * @param damage     伤害值
     */
    void beAttack(Fighter attacker, Cfg_Skill_Bean skill, int skillLevel, long damage);

    /**
     * 获取阵营
     *
     * @return
     */
    int getCamp();

    /**
     * 血量改变
     *
     * @param attacker 攻击者
     */
    void onHpChange(Fighter attacker);

    float getRadius();

    Skill getUseSkill();

    void setUseSkill(Skill useSkill);

    int getState();

    //获取出生保护
    long getBrithProtect();

    //获取固定掉血
    long getFixDecHp();

    //获取技能列表
    ConcurrentHashMap<Integer, Skill> getSkills();

    //获取技能加成
    ConcurrentHashMap<Integer, Integer> getSkilladds();

    //添加状态
    void addFightState(FightState state, int time);

    //获取方向
    Position getDir();

    //获取地图信息
    MapGps getCurGps();

    //获取当前引导技能
    Skill getCurSlowSkill();

    void setCurSlowSkill(Skill curSlowSkill);

    //是否统一地图
    boolean isEqualMap(Fighter comper);

    //战斗力
    long getFightPoint();

    //预执行事件
    List<FightSkillEvent> willEvents();

    //仇恨列表
    List<Hatred> getHatreds();

    //弹道
    List<SkillLockTrajectory> getLockTrajectories();

    //设置变身ID
    void setChangeModelID(int modelID);

    //获得变身ID
    int getChangeModleID();

    //获取变身状态 true==变身中，否则不是
    boolean getChangeModelState();

    ReviveData getReviveData();

    void setReviveData(ReviveData reviveData);

}
