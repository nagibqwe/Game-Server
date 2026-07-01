package com.game.fight.script;

import com.game.fight.structs.FightSkillEvent;
import com.game.map.structs.MapObject;
import com.game.player.structs.Player;
import com.game.skill.config.SkillEvent;
import com.game.skill.config.SkillVisual;
import com.game.skill.structs.Skill;
import com.game.skill.structs.SkillLockTrajectory;
import com.game.skill.structs.SkillMagic;
import com.game.structs.Entity;
import com.game.structs.Fighter;
import com.game.structs.STCheckerMaker;
import game.core.map.Position;
import game.core.util.AutoIncrementIntArray;
import game.message.FightMessage;
import game.message.FightMessage.ReqChangeAttackDir;
import game.message.FightMessage.ReqPlayHit;
import game.message.FightMessage.ReqPlayLockTrajectory;
import game.message.FightMessage.ReqPlaySelfMove;
import game.message.FightMessage.ReqPlaySkillObject;
import game.message.FightMessage.ReqRollMove;
import game.message.FightMessage.ReqUseSkill;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 */
public interface IFightManagerScript {

    /**
     * 怪物 宠物 机器人messInfo参数为null 玩家攻击 skill beAttacks参数null
     *
     * @param fighter
     * @param skill
     * @param beAttacks
     * @param messInfo
     */
    void attack(Fighter fighter, Skill skill, List<Fighter> beAttacks, FightMessage.ReqUseSkill messInfo);

    /**
     * 玩家释放技能
     *
     * @param player
     * @param mess
     */
    void OnReqUseSkill(Player player, ReqUseSkill mess);

    //攻击伤害技能谋算
    void onReqPlayHit(Player player, ReqPlayHit mess);

    //计算锁定追踪协议
    void onReqPlayLockTrajectory(Player player, ReqPlayLockTrajectory mess);

    //玩家自移动服务
    void onReqPlaySelfMove(Player player, ReqPlaySelfMove mess);

    //简单召唤技能处理
    void onReqPlaySkillObject(Player player, ReqPlaySkillObject mess);

    //玩家翻滚
    void OnReqRollMove(Player player, ReqRollMove mess);

    //攻击方向转动
    void OnReqChangeAttackDir(Player player, ReqChangeAttackDir mess);

    //检测pk
    boolean isCanPk(Fighter attacker, Fighter defer);

    //检查战斗技能
    Skill getFightSkill(Entity player, MapObject map, int skillId);

    /**
     * 获取当前可用的技能
     *
     * @param fighter
     * @return
     */
    Skill getCanUseSkill(Entity fighter);

    /**
     * 获取范技能围内的敌人
     *
     * @param attacker
     * @param defer
     * @param skill
     * @param sv
     * @return
     */
    ArrayList<Fighter> getAnemys(Entity attacker, Fighter defer, Skill skill, SkillVisual sv);

    /**
     * 获取技能在某个目标点范围内的Fighter 筛选掉不能攻击的对象
     *
     * @param map           地图
     * @param attacker      攻击者
     * @param point         原点
     * @param dir           朝向
     * @param skill         使用技能
     * @param sv            技能参数
     * @param attackchecker 过滤器
     * @return
     */
    ArrayList<Fighter> getEnemysInSkillArea(MapObject map, Fighter attacker, Position point, Position dir, Skill skill, SkillVisual sv, STCheckerMaker attackchecker);

    /**
     * 立即添加一个召唤物 在固定位置
     *
     * @param attacker
     * @param skill
     */
    void addSummonImmediately(Fighter attacker, Skill skill, Position pos, Position dir);

    /**
     * 实现各种特殊效果,和buff的doaction差不多
     *
     * @param actionParam
     * @param targets
     */
    void doAction(Fighter src, List<AutoIncrementIntArray> actionParam, List<Fighter> targets);

    /**
     * 处理执行技能事件处理
     *
     * @param attacker 攻击发起者
     * @param event    事件值
     * @param serial   技能处理编号值
     */
    void doSkillEvent(Fighter attacker, FightSkillEvent event, int serial);

    /**
     * 检查战斗对象身上的延时效果技能事件，到时间则执行
     *
     * @param fighter 执行体
     */
    void onFightSkillEvent(Fighter fighter);

    //召唤物的攻击效果处理
    void attack(SkillMagic magic, Fighter owner, Skill skill, SkillEvent se, List<Fighter> targets);

    //处理弹道的事件
    boolean doSkillLockTrajectoryEndToDelete(Fighter fighter, SkillLockTrajectory st);

    /**
     * 获取变身技能，如果没有，就返回null
     *
     * @param player
     * @return
     */
    Skill getBianshenSkill(Player player, int bianshenSkillId);
}
