package common.monster;

import com.data.CfgManager;
import com.data.Global;
import com.data.bean.*;
import com.data.struct.ReadArray;
import com.game.behavior.manager.BehaviorManager;
import com.game.behavior.structs.BaseBehavior;
import com.game.behavior.structs.BehaviorType;
import com.game.behavior.structs.type.AttackMoveBehavior;
import com.game.behavior.structs.type.MoveBehavior;
import com.game.behavior.structs.type.RunBackBehavior;
import com.game.behavior.structs.type.RunBehavior;
import com.game.cooldown.structs.CooldownTypes;
import com.game.fight.manager.FightManager;
import com.game.fight.script.IFightManagerScript;
import com.game.fight.state.FightState;
import com.game.fight.structs.FightEnum;
import com.game.manager.Manager;
import com.game.map.manager.MapManager;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapUtils;
import com.game.monster.script.IMonsterAction;
import com.game.monster.script.IMonsterAi;
import com.game.monster.structs.Monster;
import com.game.monster.structs.MonsterDefine;
import com.game.pet.structs.Pet;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.skill.config.FindTargetInfo;
import com.game.skill.config.SkillEvent;
import com.game.skill.config.SkillEventContainer;
import com.game.skill.config.SkillVisual;
import com.game.skill.config.event.PlayHitEvent;
import com.game.skill.config.event.PlaySimpleSkillObjectEvent;
import com.game.skill.structs.Skill;
import com.game.skill.structs.SkillDefine;
import com.game.skill.structs.SkillLockTrajectory;
import com.game.skill.structs.SkillMagic;
import com.game.structs.*;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import com.game.utils.ShapeUtils;
import com.game.utils.Utils;
import com.game.yed.YedAI;
import com.game.yed.scripts.YedMethodScript;
import game.core.map.Position;
import game.core.script.IScript;
import game.core.util.TimeUtils;
import game.message.MapMessage.ResMoveSpeedChange;
import game.message.MapMessage.ResUpdateMoveState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author admin
 */
public class MonsterAiScript implements IScript, IMonsterAi {

    protected Logger log = LogManager.getLogger(MonsterAiScript.class);

    @Override
    public int getId() {
        return ScriptEnum.MonsterAiCommonScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void doYedAi(Monster monster) {
        if (monster.isUseNewAi()) {
            newMonsterAi(monster);
            return;
        }

        // yed ai
        try {
            YedAI runningAi = monster.gainRunningAi();
            if (runningAi != null) {
                runningAi.SetOwner(yedAi());
                runningAi.setOwnerparams(monster);
                runningAi.addCommonTarget(yedBaseAi());
                runningAi.Update();
            }
        } catch (Exception e) {
            log.error(e, e);
        }

        Cfg_Monster_Bean config = CfgManager.getCfg_Monster_Container().getValueByKey(monster.getModelId());
        //掉血AI
        dropHpAi(monster, config);

        try {
            //检查伤害与仇恨
            onCheckDamagerAndHart(monster);
        } catch (Exception e) {
            log.error("输出是这理的1！ 怪物ID=" + monster.getName() + " ," + monster.getModelId());
            log.error(e, e);
        }

        try {
            OnFightStateAi(monster);
        } catch (Exception e) {
            log.error("输出是这理的11！ 怪物ID=" + monster.getName() + " ," + monster.getModelId());
            log.error(e, e);
        }
        try {
            OnMagicAi(monster);
        } catch (Exception e) {
            log.error("输出是这理的12！ 怪物ID=" + monster.getName() + " ," + monster.getModelId());
            log.error(e, e);
        }
        try {
            OnBuffAi(monster);
        } catch (Exception e) {
            log.error("输出是这理的13！ 怪物ID=" + monster.getName() + " ," + monster.getModelId());
            log.error(e, e);
        }

        try {
            //如果处于定身状态
            if (Manager.buffManager.deal().haveDing(monster)) {
                return;
            }
            //被攻击者处于眩晕状态，则不攻击
            if (Manager.buffManager.deal().haveDizziness(monster)) {

//            LOGGER.error(player.nameIdString() + " 正在眩晕中， 不可选线！");
                return;
            }
            setConfigYedAi(monster, config);
        } catch (Exception e) {
            log.error("输出是这理的14！ 怪物ID=" + monster.getName() + " ," + monster.getModelId());
            log.error(e, e);
        }

        try {
            OnRecoverArmor(monster, config);
        } catch (Exception e) {
            log.error("输出是这理的4！ 怪物ID=" + monster.getName() + " ," + monster.getModelId());
            log.error(e, e);
        }


        //如果当前地图都没有人了
        MapObject mp = Manager.mapManager.getMap(monster.gainMapId());
        if (mp == null) {
            //空闲
            OnNullAi(monster, config);
            return;
        }


        if (mp.getPlayers().size() < 1) {
            //空闲
            OnNullAi(monster, config);
            //时长掉血怪不能回血
            if (monster.getCurHp() != monster.getAttribute().MaxHP() && config.getBeHurtType() != 4) {
                monster.reset();
            }
            return;
        }

        try {
            //愤怒
            if (monster.isChangingStep()) {
                return;
            }
            if (monster.getCamp() == -1) {
                log.error(monster.nameIdString() + " 的阵营是-1!");
                return;
            }

            doAngerAi(monster, config);
        } catch (Exception e) {
            log.error("输出是这理的2！ 怪物ID=" + monster.getName() + " ," + monster.getModelId());
            log.error(e, e);
        }

        try {
            //战斗
            if (OnBattleAi(monster, config)) {
                return;
            }
        } catch (Exception e) {
            log.error("输出是这理的4！ 怪物ID=" + monster.getName() + " ," + monster.getModelId());
            log.error(e, e);
        }

        try {
            //巡逻
            if (OnPatrolAi(monster, config)) {
                return;
            }

            //空闲
            OnNullAi(monster, config);
        } catch (Exception e) {
            log.error("输出是这理的3！ 怪物ID=" + monster.getName() + " ," + monster.getModelId());
            log.error(e, e);
        }
    }

    /**
     * 怪物进入战斗掉血
     */
    private void dropHpAi(Monster monster, Cfg_Monster_Bean config) {
        if (config.getBeHurtType() != 4) {
            return;
        }

        long now = TimeUtils.Time();
        if (TimeUtils.Time() - monster.getLastDropHpTime() >= 1000L) {
            float rate = (1 - monster.getAttribute().getAdditionValue(AttributeType.ATTR_Monster_Dec) * 1F / 10000);
//            if (monster.getModelId() == 25001)
//            log.info(monster.getName() + "(" + monster.getId() + ")" + "，当前血量：" + monster.getCurHp());
            long curHp = monster.getCurHp() - (long) (config.getBeHurtValue() * rate);
            if (curHp <= 0) {
                monster.setCurHp(0);
                monster.setDie();
                monster.doDie(monster);
                MapUtils.sendDead(monster, monster);
            } else {
                monster.setCurHp(curHp);
                MapUtils.sendHpChange(monster);
//                if (monster.getModelId() == 25001)
//                log.info(monster.getName() + "(" + monster.getId() + ")" + "，当前血量：" + monster.getCurHp());
            }
            monster.setLastDropHpTime((now / 1000) * 1000);
        }
    }

    /**
     * 脚本AI逻辑处理
     */
    public void newMonsterAi(Monster monster) {
        Cfg_Monster_Bean config = CfgManager.getCfg_Monster_Container().getValueByKey(monster.getModelId());
        dropHpAi(monster, config);
        try {
            //检查伤害与仇恨
            onCheckDamagerAndHart(monster);
        } catch (Exception e) {
            log.error("输出是这理的1！ 怪物ID=" + monster.getName() + " ," + monster.getModelId());
            log.error(e, e);
        }
        try {
            OnFightStateAi(monster);
        } catch (Exception e) {
            log.error("输出是这理的11！ 怪物ID=" + monster.getName() + " ," + monster.getModelId());
            log.error(e, e);
        }
        try {
            OnMagicAi(monster);
        } catch (Exception e) {
            log.error("输出是这理的12！ 怪物ID=" + monster.getName() + " ," + monster.getModelId());
            log.error(e, e);
        }
        try {
            OnBuffAi(monster);
        } catch (Exception e) {
            log.error("输出是这理的13！ 怪物ID=" + monster.getName() + " ," + monster.getModelId());
            log.error(e, e);
        }
        // yed ai
        try {
            YedAI runningAi = monster.gainRunningAi();
            if (runningAi != null) {
                runningAi.SetOwner(yedAi());
                runningAi.setOwnerparams(monster);
                runningAi.addCommonTarget(yedBaseAi());
                runningAi.Update();
            }
        } catch (Exception e) {
            log.error(e, e);
        }

        try {
            //如果处于定身状态
            if (Manager.buffManager.deal().haveDing(monster)) {
                if (monster.getAmbs().size() > 0) {
                    monster.getAmbs().clear();
                }
                return;
            }

            //被攻击者处于眩晕状态，则不攻击
            if (Manager.buffManager.deal().haveDizziness(monster)) {
                if (monster.getAmbs().size() > 0) {
                    monster.getAmbs().clear();
                }
                return;
            }
        } catch (Exception e) {
            log.error("输出是这理的114！ 怪物ID=" + monster.getName() + " ," + monster.getModelId());
            log.error(e, e);
        }
        try {
            setConfigYedAi(monster, config);
        } catch (Exception e) {
            log.error("输出是这理的14！ 怪物ID=" + monster.getName() + " ," + monster.getModelId());
            log.error(e, e);
        }
        try {
            OnRecoverArmor(monster, config);
        } catch (Exception e) {
            log.error("输出是这理的4！ 怪物ID=" + monster.getName() + " ," + monster.getModelId());
            log.error(e, e);
        }


        //如果当前地图都没有人了
        MapObject mp = Manager.mapManager.getMap(monster.gainMapId());
        if (mp == null) {
            //空闲
            OnNullAi(monster, config);
            return;
        }

        if (mp.getPlayers().size() < 1) {
            //空闲
            OnNullAi(monster, config);
            return;
        }

        try {
            //战斗
            OnNeedBattleAi(monster, config);

        } catch (Exception e) {
            log.error("输出是这理的4！ 怪物ID=" + monster.getName() + " ," + monster.getModelId());
            log.error(e, e);
        }
    }

    private boolean OnNeedBattleAi(Monster monster, Cfg_Monster_Bean config) {

        //受击移动
        if (OnBeAttackMoveAi(monster)) {
            return true;
        }

        //不反击的
        if (config.getAttack_type() == MonsterDefine.Monster0) {
            return false;
        }

        //战斗中
        if (monster.isInBattle()) {
            //LOGGER.error("使用技能=" + canUseSkill.getId());
            return true;
        }

        //检查CD与仇恨对象
        doAngerAi(monster, config);
        return false;
    }

    //玩家召唤技能Tick
    private void OnMagicAi(Monster monster) {

        long curTime = TimeUtils.Time();
        MapObject map = Manager.mapManager.getMap(monster.gainMapId());

        if (monster.getMagics().isEmpty()) {
            return;
        }
        //开始攻击
        IFightManagerScript is = (IFightManagerScript) Manager.scriptManager.GetScriptClass(ScriptEnum.FightManagerBaseScript);

        Iterator<Long> iter = monster.getMagics().iterator();
        while (iter.hasNext()) {
            long magicId = iter.next();
            SkillMagic magic = map.getMagic(magicId);
            if (magic == null) {
                iter.remove();
                continue;
            }

            if (curTime < magic.getStart()) {
                continue;
            }

            //开始检测攻击事件
            if (magic.getSerials().isEmpty()) {
                Manager.mapManager.manager().onQuitMap(map, magic, true);
                continue;
            }

            SkillVisual sv = SkillEventContainer.getInstance().getVisuals().get(magic.getMagicIndex());

            //如果已经超过了， 效果去掉
            if (curTime > magic.getStart() + sv.getCd()) {
                Manager.mapManager.manager().onQuitMap(map, magic, true);
                log.info("效果魔法时间到， 去掉了magic =" + magic);
                continue;
            }

            Iterator<Integer> iterator = magic.getSerials().iterator();

            Skill skill = magic.getSkillId() == 0 ? magic.getSkillinfo() : is.getFightSkill(monster, map, magic.getSkillId());
            int i = -1;
            while (iterator.hasNext()) {
                i += 1;
                int serialId = iterator.next();
                SkillEvent se = sv.getEvents().get(serialId);
                if (se instanceof PlayHitEvent) {
                    PlayHitEvent event = (PlayHitEvent) se;
                    if (onPlayHitEvent(curTime, magic, event, is, monster, skill)) {
                        iterator.remove();
                    }
                    continue;
                }

                if (se instanceof PlaySimpleSkillObjectEvent) {
                    PlaySimpleSkillObjectEvent psso = (PlaySimpleSkillObjectEvent) se;
                    long beginTime = (long) (i * psso.getHitInterval() * 1000 * SkillVisual.OneFrameTime);
                    if (onSimpleSkillObjectEvent(curTime, beginTime, magic, psso, is, monster, skill)) {
                        iterator.remove();
                    }
                }
            }
        }
    }

    private boolean onPlayHitEvent(long curTime, SkillMagic magic, PlayHitEvent event, IFightManagerScript is, Monster player, Skill skill) {
        if (curTime < magic.getStart() + event.getFpsTime()) {
            return false;
        }
        //开始攻击
        List<Fighter> targets = getMagicTargets(player, magic, event.getFindInfo());
        if (magic.getActionParam().size() > 0) {
            is.doAction(player, magic.getActionParam(), targets);
        }
        is.attack(magic, player, skill, event, targets);
        return true;
    }

    private boolean onSimpleSkillObjectEvent(long curTime, long nextFpsTime, SkillMagic magic, PlaySimpleSkillObjectEvent event, IFightManagerScript is, Monster player, Skill skill) {
        if (curTime < magic.getStart() + nextFpsTime) {
            return false;
        }
        //开始攻击
        List<Fighter> targets = getMagicTargets(player, magic, event.getFindInfo());
        if (magic.getActionParam().size() > 0) {
            is.doAction(player, magic.getActionParam(), targets);
        }
        is.attack(magic, player, skill, event, targets);
        return true;
    }

    //获取攻击目标
    public List<Fighter> getMagicTargets(Monster monster, SkillMagic magic, FindTargetInfo find) {

        List<Fighter> fighter = new ArrayList<>();

        for (Hatred hat : monster.getHatreds()) {
            if (fighter.size() > find.getMaxTargetCount()) {
                break;
            }
            Fighter defer = hat.getTarget();
            float dis = Utils.getDistance(magic.gainCurPos(), defer.gainCurPos());
            if (dis <= find.getAttackDis()) {
                fighter.add(defer);
            }
        }

        // 如果是个陷阱怪,那么就找附近的玩家 目前认为只会在副本里面用副本怪,如果地图上玩家很多,那么就不能这么写了.
        if (monster.isTrap()) {
            MapObject map = MapManager.getInstance().getMap(monster.gainMapId());
            for (Player p : map.getPlayers().values()) {
                if (ShapeUtils.inAttackArea(find.getShape(), magic.gainCurPos(), magic.getDir(), p.gainCurPos()) && !fighter.contains(p)) {
                    fighter.add(p);
                }
            }
        }

        return fighter;
    }

    public Position getRandomPos(Position curPos, float range) {

        float x = RandomUtils.randomFloatValue(0, 1f);
        float y = (float) Math.sqrt(1 - x * x);
        float dis = RandomUtils.randomFloatValue(0.1f, range);
        x = x * dis;
        x = TimeUtils.Time() % 2 == 0 ? x : -x;
        y = y * dis;
        y = TimeUtils.Time() % 2 == 0 ? y : -y;
        return new Position(curPos.getX() + x, curPos.getY() + y);

    }

    //怪物战斗规则
    //1、优先攻击当前仇恨对象
    //2、当前仇恨对象死亡 或者 无当前仇恨对象，获取仇恨值最高者成为当前仇恨对象
    //3、当仇恨对象为空时脱战，并回跑战斗开始点
    private boolean OnBattleAi(Monster monster, Cfg_Monster_Bean config) {

        //死亡
        if (EntityState.Dead.compare(monster.getState())) {
            return true;
        }
        //受击移动
        if (OnBeAttackMoveAi(monster)) {
            return true;
        }
        //不反击的
        if (config.getAttack_type() == MonsterDefine.Monster0) {
            return false;
        }
        //战斗中
        if (!monster.isInBattle()) {
            return false;
        }
        //脱战半径
        float rec = Utils.getDistance(monster.gainCurPos(), monster.getInitPos());
        if (config.getRecoversuit() / 100f < rec) {
            leaveBattle(monster, config);
            return true;
        }
        if (!Manager.cooldownManager.isCooldowning(monster, CooldownTypes.MonsterCheckHatred, null)) {
            Manager.cooldownManager.addCooldown(monster, CooldownTypes.MonsterCheckHatred, null, 3000);
            //遍历检测一次脱战
            for (Hatred hatred : new ArrayList<>(monster.getHatreds())) {
                Fighter defer = hatred.getTarget();
                if (checkLeaveHatred(monster, config, hatred)) {
                    doLeaveBattleEvent(monster, defer);
                }
            }
        }

        //选取主目标
        if (monster.getHatreds().isEmpty()) {
            if (monster.getWillLeaveBattleTime() == 0) {
                monster.setWillLeaveBattleTime(TimeUtils.Time());
                return true;
            }

            if (TimeUtils.Time() < monster.getWillLeaveBattleTime() + Global.Off_War_Time) {
                return true;
            }

            //如果当前仇恨列表为空， 检查怪物是不是主动怪
            if (config.getAttack_type() != MonsterDefine.MonsterActive) {
                leaveBattle(monster, config);
//              LOGGER.error(config.getName() + " 仇恨为空！");
                return true;

            }
//           LOGGER.error(config.getName() + " 开始检查对象");
            //检查周围是否有玩家
            if (!doAngerAi(monster, config)) {
                leaveBattle(monster, config);
//              LOGGER.error(config.getName() + " 没有找到玩家！");
                return true;
            }
        }

        monster.setWillLeaveBattleTime(0);

        //设置进入战斗时间
        if (monster.getEnterFight() <= 0) {
            monster.setEnterFight(TimeUtils.Time());
        }

        //获取主目标
        Hatred curMainTarget = getCurMainTarget(monster);
        if (curMainTarget == null) {
            curMainTarget = changeMainTarget(monster);
        }
        //无攻击目标
        if (curMainTarget == null) {
            return true;
        }

        try {
            return tryUseSkill(monster, config, curMainTarget);
        } catch (Exception e) {
            log.error(" 出错在这个", e);
        }
        //LOGGER.error("使用技能=" + canUseSkill.getId());
        return true;

    }

    /**
     * 脱战通知脚本
     *
     * @param monster
     * @param defer
     */
    void doLeaveBattleEvent(Monster monster, Fighter defer) {

        MapObject map = Manager.mapManager.getMap(monster.gainMapId());

        if (map.getSetting().getIsscript() < 0) {
            return;
        }
        if (defer instanceof Player) {
            Manager.mapManager.base(map.getSetting().getIsscript()).onLeaveBattle(map, monster, (Player) defer);
            return;
        }
        for (Hatred hatred : monster.getHatreds()) {
            if (hatred.getTarget() instanceof Player) {
                Manager.mapManager.base(map.getSetting().getIsscript()).onLeaveBattle(map, monster, (Player) hatred.getTarget());
            }
        }
    }

    /**
     * 怪我脱战
     *
     * @param monster
     * @param config
     */
    void leaveBattle(Monster monster, Cfg_Monster_Bean config) {
        doLeaveBattleEvent(monster, null);
        //重置状态
        reset(monster, config);

        if (monster.getAttribute().gainFinalMoveSpeed() <= 0) {
            return;
        }

        //回跑 重置属性
        List<Position> roads = new ArrayList<>();
        roads.add(monster.gainCurPos());
        roads.add(monster.getInitPos());
        monster.setRoads(roads);
        monster.addState(EntityState.Run);
        monster.addState(EntityState.Move);
        monster.addState(EntityState.RunBack);
        monster.setSpeedRate(2f);
        sendEntitySpeed(monster);
        sendMonsterRun(monster);
        Manager.mapManager.sendMoveMessage(monster, roads);
        BehaviorManager.InsertOnlyBehavior(monster, new RunBackBehavior(monster));
    }

    @Override
    public boolean tryUseSkill(Monster monster, Cfg_Monster_Bean config, Hatred curMainTarget) {

        //获取能释放的技能
        Skill canUseSkill = FightManager.getInstance().deal().getCanUseSkill(monster);
        if (canUseSkill == null) {
            return true;
        }

        Cfg_Skill_Bean skillBean = CfgManager.getCfg_Skill_Container().getValueByKey(canUseSkill.getSkillId());
        if (skillBean == null) {
            log.error("怪物ID=" + monster.getName() + " ," + monster.getModelId() + " 技能ID=" + canUseSkill.getSkillId());
            return true;
        }

        SkillVisual sv = Manager.skilleventManager.getVisuals().get(skillBean.getVisualDef());
        if (sv == null) {
            return true;
        }

        // 看看当前是不是已经在跑引导技能了,ai的引导技能走behavior
        if (sv.isLockTrajact() && monster.getCurSlowSkill() != null) {
            return true;
        }

        //追击
        if (OnPursueAi(monster, config, canUseSkill, sv, curMainTarget)) {
            return true;
        }

        if (curMainTarget.getTarget() == null) {
            //找下一个怪
            curMainTarget = changeMainTarget(monster);
        }

        if (curMainTarget == null) {
            return true;
        }

        //战斗不可移动
        if (!(monster.getCurSlowSkill() != null && !sv.isCanMove())) {
            //攻击
            if (curMainTarget.getTarget() != null) {
                Position dir = Utils.getDir(monster.gainCurPos(), curMainTarget.getTarget().gainCurPos());
                monster.setDir(dir);
//                LOGGER.error(monster.getName() + ", 对" + curMainTarget.getTarget().nameIdString() + "skill=" + canUseSkill.getSkillId() + "设置转向 dir =" + dir);
            }
        }

        List<Fighter> anemys = FightManager.getInstance().deal().getAnemys(monster, curMainTarget.getTarget(), canUseSkill, sv);
        if (anemys.size() < 1) {
            return true;
        }

        attack(monster, canUseSkill, anemys);
        monster.setUseSkill(null);
        return true;
    }

    /**
     * 地图怪物行为执行
     *
     * @param map
     * @param offset
     */
    @Override
    public void behavior(MapObject map, long offset) {
        Iterator<Monster> iter = map.getMonsters().values().iterator();
        //怪物AI执行入口，　不在增加一个处理事件，　这样，AI行为不会被清除
        Monster monster = null;
        while (iter.hasNext()) {
            try {
                monster = iter.next();
                if (null == monster) {
                    iter.remove();
                    log.error("monster is null!");
                    continue;
                }
                // huhu 召唤物的
                for (long magicid : monster.getMagics()) {
                    SkillMagic magic = map.getMagic(magicid);
                    if (magic != null) {
                        magic.UpdateBehavior(offset);
                    }
                }

                Iterator<SkillLockTrajectory> stIter = monster.getLockTrajectories().iterator();
                while (stIter.hasNext()) {
                    SkillLockTrajectory st = stIter.next();
                    if (Manager.fightManager.deal().doSkillLockTrajectoryEndToDelete(monster, st)) {
                        stIter.remove();
                    }
                }

                Manager.fightManager.deal().onFightSkillEvent(monster);

                doYedAi(monster);

                Iterator<BaseBehavior> it = monster.BehaviorList().iterator();
                while (it.hasNext()) {
                    BaseBehavior bb = it.next();

                    if (bb == null) {
                        log.error("行为数据为空， 则清除！");
                        it.remove();
                        continue;
                    }
                    bb.Update(offset);
                    if (bb.IsOver()) {
                        it.remove();
                    }
                }
            } catch (Exception e) {
                log.error("执行怪物行为的时候出错了！" + monster);
                log.error(e, e);
            }
        }
    }

    //重置怪物状态
    @Override
    public void reset(Monster monster, Cfg_Monster_Bean config) {
//        if (monster.getMonsterType() > 1) {
//            log.info(monster, new Exception("怪物：" + config.getName() + "，开始重置状态了！"));
//        }
        MapObject map = Manager.mapManager.getMap(monster.gainMapId());

        Manager.monsterManager.addMonsterBaseInfo(monster, false, monster.getAttBei());
        monster.setEnterFight(-1);
        monster.getActionAis().clear();
        monster.setInBattle(false);
        monster.clearHatred();
        monster.clearDropRoleIds();
        monster.setWillLeaveBattleTime(0);
        //monster.setEnterBattlePos(null);
        if (config.getRecoverHp() > 0) {
            monster.setCurHp(monster.getAttribute().MaxHP());
            monster.getDamages().clear();
            monster.onHpChange(monster);
        }
        //护甲重置
        if (monster.getArmorState() >= 0) {
            if (monster.getArmor() > 0) {
                if (monster.getArmor() != 0) {
                    monster.setArmorState(1);
                    monster.setArmorbegintime(TimeUtils.Time());
                }
            } else {
                if (monster.getArmorState() != 3) {
                    monster.setArmorState(3);
                    monster.setArmorzerotime(TimeUtils.Time());
                }
            }
        }

        //清理召唤技能
        IScript mapis = Manager.scriptManager.GetScriptClass(ScriptEnum.MonsterBaseScript);
        if (mapis instanceof IMonsterAction) {
            ((IMonsterAction) mapis).clearSkillMagic(map, monster);
        } else {
            log.error("怪物受伤时没有找到处理受伤的实例！");
        }
        //大于0才重置
        if (config.getReset_ai() > 0) {
            monster.getAipushLists().clear();
            YedAI ai = monster.gainRunningAi();
            if (ai == null) {
                return;
            }
            setYedAi(monster, ai.getYed().name);
        }
        // ai.Update(TimeUtils.Time() - ai.lastUpdateTime);
    }

    //追击
    private boolean OnPursueAi(Monster monster, Cfg_Monster_Bean config, Skill useSkill, SkillVisual sv, Hatred hatred) {
        Fighter defer = hatred.getTarget();

        //检测是否脱战
        if (checkLeaveHatred(monster, config, hatred)) {
            doLeaveBattleEvent(monster, defer);
            return true;
        }
        float dis = Utils.getDistance(monster.gainCurPos(), hatred.getTarget().gainCurPos());

        MapObject map = Manager.mapManager.getMap(monster.gainMapId());

        //战斗不可移动
        if (monster.getCurSlowSkill() != null && !sv.isCanMove()) {
            return false;
        }

        if (dis > sv.getAttackDis()) {

            if (Manager.cooldownManager.isCooldowning(monster, CooldownTypes.MonsterPursue, null)) {
                return true;
            }
            Position pursuePos = defer.getAiPos(monster, sv.getAttackDis());

            if (!Utils.isCanMove(map, pursuePos)) {
                pursuePos = defer.gainCurPos();
            }
            List<Position> roads;
            if (Utils.isCanMove(map, monster.gainCurPos(), pursuePos)) {
                roads = new ArrayList<>();
                roads.add(monster.gainCurPos());
                roads.add(pursuePos);
            } else {
                roads = MapUtils.findRoads(map, monster.gainCurPos(), pursuePos, 500);
            }

            monster.setRoads(roads);

            if (!EntityState.Run.compare(monster.getState())) {
                monster.addState(EntityState.Run);
                monster.addState(EntityState.Move);
                monster.setSpeedRate(1f);
                BehaviorManager.InsertOnlyBehavior(monster, new RunBehavior(monster));
                sendEntitySpeed(monster);
                sendMonsterRun(monster);
            }
            Manager.mapManager.sendMoveMessage(monster, roads);

            Manager.cooldownManager.addCooldown(monster, CooldownTypes.MonsterPursue, null, 333);

            return true;
        }

        return EntityState.Move.compare(monster.getState());
    }

    /**
     * 检测怪物仇恨
     *
     * @param monster
     */
    public boolean checkLeaveHatred(Monster monster, Cfg_Monster_Bean config, Hatred hatred) {
        float dis = Utils.getDistance(monster.gainCurPos(), hatred.getTarget().gainCurPos());
        //超过追击范围
        if (dis > config.getPursuit() / 100f + monster.getRadius() + 1f) {
//            log.info("[" + monster + "]追击范围dis=" + dis + " pursuit=" + (config.getPursuit() / 100f) + "目标=" + hatred.getTarget());
            monster.removeHatred(hatred);
            return true;
        }
        Fighter defer = hatred.getTarget();

        if (defer instanceof Pet) {
            monster.removeHatred(hatred);
            return true;
        }
        if (EntityState.ExitGame.compare(defer.getState())) {
            //LOGGER.info("[" + monster + "]追击目标退出游戏 目标=" + hatred.getTarget());
            monster.removeHatred(hatred);
            return true;
        }
        if (EntityState.Dead.compare(defer.getState())) {
            //LOGGER.info("[" + monster + "]追击目标死亡 目标=" + hatred.getTarget());
            monster.removeHatred(hatred);
            return true;
        }
        if (EntityState.RunBack.compare(defer.getState())) {
            monster.removeHatred(hatred);
            return true;
        }
        if (!Utils.isCanFight(defer)) {
            //LOGGER.info("[" + monster + "]追击目标安全区 目标=" + hatred.getTarget());
            monster.removeHatred(hatred);
            return true;
        }
        if (monster.gainMapId() != defer.gainMapId()) {
            monster.removeHatred(hatred);
            return true;
        }
        MapObject map = Manager.mapManager.getMap(monster.gainMapId());
        if (map == null) {
            monster.removeHatred(hatred);
            return true;
        }
        if (monster.gainFinalMoveSpeed() <= 0) {
            //如果是木桩，非受击后30秒内， 理清仇恨值
            if (TimeUtils.Time() - monster.getEndHitTime() > 8000) {
                monster.removeHatred(hatred);//如果是木桩怪， 则不要清理
                return true;
            }
        }
        return false;
    }

    //切换攻击主对象
    @Override
    public Hatred changeMainTarget(Monster monster) {
        if (monster.getHatreds().isEmpty()) {
            return null;
        }
        return monster.getHatreds().get(0);
    }

    //获取主目标
    @Override
    public Hatred getCurMainTarget(Monster monster) {
        if (monster.getHatreds().isEmpty()) {
            return null;
        }
        return monster.getHatreds().get(0);
    }

    /**
     * 巡逻规则
     */
    private boolean OnPatrolAi(Monster monster, Cfg_Monster_Bean config) {

        if (monster.gainFinalMoveSpeed() <= 0) {
            return true;
        }

        if (!EntityState.Stand.compare(monster.getState())) {
            return false;
        }

        if (EntityState.RunBack.compare(monster.getState())) {
            return true;
        }

        if (!monster.getHatreds().isEmpty()) {
            monster.setInBattle(true);
            return true;
        }

        if (TimeUtils.Time() < monster.getBrithProtect()) {
            return true;
        }

        if (Manager.cooldownManager.isCooldowning(monster, CooldownTypes.MonsterPatrol, null)) {
            return false;
        }

        if (RandomUtils.randomFloatValue(0f, 1f) > 0.3f) {
            Manager.cooldownManager.addCooldown(monster, CooldownTypes.MonsterPatrol, null, config.getPatrol_cd() / 2);
            return false;
        }
        Manager.cooldownManager.addCooldown(monster, CooldownTypes.MonsterPatrol, null, config.getPatrol_cd());

        monster.clearRoads();

        MapObject map = Manager.mapManager.getMap(monster.gainMapId());

        Position targetPos = getRandomPos(monster.gainCurPos(), config.getPatrol() / 100f);
        if (Utils.getDistance(monster.getInitPos(), targetPos) > config.getPatrol() / 100f + monster.getRadius()) {
            targetPos = monster.getInitPos();
        }
        if (!Utils.isCanMove(map, monster.gainCurPos(), targetPos)) {
            return false;
        }
        List<Position> roads = new ArrayList<>();
        roads.add(monster.gainCurPos());
        roads.add(targetPos);
        monster.setRoads(roads);
        Manager.mapManager.sendMoveMessage(monster, roads);
        monster.removeSate(EntityState.Stand);
        monster.addState(EntityState.Move);
        MoveBehavior move = new MoveBehavior(monster);
        BehaviorManager.InsertOnlyBehavior(monster, move);

        return true;

    }

    private boolean OnBeAttackMoveAi(Monster monster) {
        if (monster.getAmbs().isEmpty()) {
            return getCurHitType(monster) != SkillDefine.SkillAttackMoveType_None;
        }
        long curTime = TimeUtils.Time();
        List<AttackMoveBehavior> ambs = new ArrayList<>(monster.getAmbs());
        for (AttackMoveBehavior amb : ambs) {
            //时间错乱的抛弃了
            if (amb.getEnd() > curTime + 60000) {
                monster.getAmbs().remove(amb);
                log.error(monster.nameIdString() + "的行为时间过了：" + amb.getTarget());
                continue;
            }

            if (amb.getStart() <= curTime) {
//                LOGGER.error(monster.nameIdString() + "取消行为时间=" + TimeUtils.Time() + "下一个开始时间=" + amb.getStart());
                monster.getAmbs().remove(amb);

                monster.addFightState(FightEnum.SkillFreeze);

                //打断怪物的引导技能
                if (monster.getCurSlowSkill() != null) {
                    monster.setCurSlowSkill(null);
                }
                BehaviorManager.CancelBehaviorByType(monster, BehaviorType.AttackMove);
                if (amb.getHitType() == SkillDefine.SkillAttackMoveType_Catch) {
                    amb.setSpeed(Utils.getDistance(monster.gainCurPos(), amb.getTarget()) / (amb.getEnd() - amb.getStart()) * 1000);
                }
                BehaviorManager.InsertBehavior(monster, amb);
            }
        }
        return true;
    }

    //获取正在执行的受击事件
    private int getCurHitType(Fighter fighter) {
        AttackMoveBehavior move = (AttackMoveBehavior) BehaviorManager.GetBehavior((Entity) fighter, BehaviorType.AttackMove);
        if (move == null) {
            return SkillDefine.SkillAttackMoveType_None;
        }
        return move.getHitType();
    }

    private boolean OnNullAi(Monster monster, Cfg_Monster_Bean config) {
        //无行为状态，重置成站立
        monster.resetState();
        return true;
    }

    //移动速度变化
    @Override
    public void sendEntitySpeed(Entity entity) {
        ResMoveSpeedChange.Builder msg = ResMoveSpeedChange.newBuilder();
        msg.setObjectId(entity.getId());
        msg.setValue(entity.gainFinalMoveSpeed());
        MessageUtils.send_to_roundPlayer(entity, ResMoveSpeedChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    //怪物是否跑动
    @Override
    public void sendMonsterRun(Entity entity) {
        ResUpdateMoveState.Builder msg = ResUpdateMoveState.newBuilder();
        msg.setId(entity.getId());
        msg.setIsRun(EntityState.Run.compare(entity.getState()));
        MessageUtils.send_to_roundPlayer(entity, ResUpdateMoveState.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    //攻击
    private void attack(Monster monster, Skill skill, List<Fighter> beAttacks) {
        IFightManagerScript is = (IFightManagerScript) Manager.scriptManager.GetScriptClass(ScriptEnum.FightManagerBaseScript);
        monster.changeSkillTargetFilter(ScriptEnum.AiMonsterSkillTargetCheckerCommonScript);
        is.attack(monster, skill, beAttacks, null);
    }

    //战斗状态心跳
    private void OnFightStateAi(Monster monster) {
        if (monster.getFightStates().isEmpty()) {
            return;
        }
        long curTime = TimeUtils.Time();
        Iterator<FightState> iter = monster.getFightStates().iterator();
        while (iter.hasNext()) {
            FightState state = iter.next();
            if (state.getOverTime() < curTime) {
                iter.remove();
                state.romove(monster);
                log.info("结束状态 state=" + state + monster);
            }
        }
    }

    private void OnBuffAi(Monster monster) {
        if (monster.getBuffs().isEmpty()) {
            return;
        }
        Manager.buffManager.deal().tick(monster);
    }

    /**
     * 检查周围是否有可攻击玩家
     *
     * @param monster
     * @param config
     * @return
     */
    @Override
    public boolean doAngerAi(Monster monster, Cfg_Monster_Bean config) {

        if (config.getAttack_type() != MonsterDefine.MonsterActive) {
            return false;
        }

        if (TimeUtils.Time() < monster.getBrithProtect()) {
            return false;
        }

        if (Manager.cooldownManager.isCooldowning(monster, CooldownTypes.MonsterAngerCD, null)) {
            return false;
        }

        MapObject map = Manager.mapManager.getMap(monster.gainMapId());
        if (map == null) {
            return false;
        }
        if (map.getPlayers().isEmpty()) {
            monster.clearHatred();
            monster.clearDropRoleIds();
            return false;
        }
        long now = TimeUtils.Time();
        int globaldietime = Global.IDontKown9;//10秒
        List<Fighter> fighters = null;
        boolean isBoss = true;
        if (config.getMonster_type() == MonsterDefine.MonsterType_Normal) {
            isBoss = false;
        }

        if (isBoss) {
            fighters = MapUtils.getFighterNearHaveDie(map, monster.getInitPos(), config.getRecoversuit() / 100, config.getRecoversuit() / 100, monster.getModelId());
            ConcurrentHashMap<Long, Long> playerDieList = monster.getPlayerDieTime();
            for (Iterator<Fighter> it = fighters.iterator(); it.hasNext(); ) {
                Fighter fighter = it.next();
                if (fighter.isDie()) {
                    if (playerDieList.containsKey(fighter.getId())) {
                        long dieTime = playerDieList.get(fighter.getId());
                        //超时，移除仇恨
                        if (now - dieTime > globaldietime) {
                            it.remove();
                        }
                    } else {
                        it.remove();//如果不在包含列表中，也是要移除的
                    }
                }
            }
        } else {
            fighters = MapUtils.getFighterNear(map, monster.getInitPos(), config.getRecoversuit() / 100, config.getRecoversuit() / 100, MapUtils.FighterFlag.NoMonster.v);

        }
        if (fighters.isEmpty()) {
            monster.clearHatred();
            monster.clearDropRoleIds();
            return false;
        }

        IFightManagerScript is = (IFightManagerScript) Manager.scriptManager.GetScriptClass(ScriptEnum.FightManagerBaseScript);

        for (Fighter defer : fighters) {
            if (defer instanceof Pet) {
                continue;
            }

            //仙盟支援时检查boss附近的玩家阵营
            if (defer instanceof Player) {
                if (isBoss) {
                    Manager.worldHelpManager.getScript().checkWorldHelpCamp(monster, defer);
                }
            }

            if (monster.getCamp() == defer.getCamp() || defer.getCamp() == -1) {
                continue;
            }

            if (EntityState.Fly.compare(defer.getState())) {
                continue;
            }
            //加入检查 是否可以PK
            if (!is.isCanPk(monster, defer)) {
                continue;
            }

            float dis = Utils.getDistance(monster.getInitPos(), defer.gainCurPos());
            if (dis < config.getPursuit() / 100f) {

                int hite = 0;
                //如果不是目标不是精英怪， 则仇恨正常，如果是是仇恨值很高
                if (defer instanceof Monster) {
                    hite = ((Monster) defer).getMonsterType() != MonsterDefine.MonsterType_Normal ? 10000 : 0;
                }
                monster.addHatred(defer, hite);
            }
        }

        Manager.cooldownManager.addCooldown(monster, CooldownTypes.MonsterAngerCD, null, 100);

        if (!monster.getHatreds().isEmpty()) {
            monster.setInBattle(true);
        }

        return !monster.getHatreds().isEmpty();
    }

    //策划配置AI
    @Override
    public void setConfigYedAi(Monster monster, Cfg_Monster_Bean config) {

        if (config == null) {
            return;
        }

        if (config.getAi() == null) {
            return;
        }

        if (config.getAi().size() == 0) {
            return;
        }
        try {
            for (int i = 0; i < config.getAi().size(); i++) {
                Cfg_MonsterAi_Bean ai = CfgManager.getCfg_MonsterAi_Container().getValueByKey(config.getAi().get(i));
                if (ai == null) {
                    log.error(monster.nameIdString() + "的AIID=" + config.getAi().get(i) + "在配置表里没有找到！");
                    continue;
                }
                OnConfigAi(monster, ai);
            }

        } catch (Exception e) {
            log.error(e, e);
        }
    }

    @Override
    public void setYedAi(Monster monster, String yed) {
        if (yed.equals("")) {
            return;
        }
        YedAI ai = new YedAI(yedBaseAi());
        ai.Load(yed, false);
        monster.changeRunningAi(ai);
    }

    public void OnConfigAi(Monster monster, Cfg_MonsterAi_Bean ai) {
        if (ai == null) {
            return;
        }
        if (monster.getActionAis().contains(ai)) {
            return;
        }

        if (!OnCheckAiReach(monster, ai)) {
            return;
        }

        if (!OnDealAiReach(monster, ai)) {
        }

        monster.getActionAis().add(ai);

    }

    //1进入BOSS战斗时间 参数: type_时间毫秒
    //2BOSS血量到达XX    参数：type_百分比
    //3BOSS击杀玩家数量  参数：type_击杀数量
    //4场内没有XX职业      参数：type_职业
    //5场内职业数量大于X  参数： type_职业数
    //6场内职业数量小于X  参数：type_职业数
    //7出生时间
    private boolean OnCheckAiReach(Monster monster, Cfg_MonsterAi_Bean bean) {

        if (bean.getReach() == null) {
            return false;
        }

        for (ReadArray ai : bean.getReach().getValuees()) {
            int type = (int) ai.get(0);
            int value = (int) ai.get(1);

            switch (type) {
                case 1:
                    if (monster.getEnterFight() <= 0) {
                        return false;
                    }
                    if (TimeUtils.Time() - monster.getEnterFight() < value) {
                        return false;
                    }
                    break;
                case 7:
                    if (TimeUtils.Time() - monster.getBrithTime() < value - 100) { //添加一个100毫秒的延迟
                        return false;
                    }
                    break;
                case 2:
                    if (monster.checkHpPercent() > value) {
                        return false;
                    }
                    break;
                case 3:
                    if (monster.getKillcount() < value) {
                        return false;
                    }
                    break;
                case 4:
                    if (isHaveCareer(monster, value)) {
                        return false;
                    }
                    break;
                case 5:
                    if (getCareerCount(monster) <= value) {
                        return false;
                    }
                    break;
                case 6:
                    if (getCareerCount(monster) >= value) {
                        return false;
                    }
                    break;
                default:
                    return false;
            }
        }

        return true;
    }

    //1添加某个技能   参数： type_skillID
    //2删除某个技能   参数： type_skillID
    //3添加某个BUFF   参数：type_buffID
    //4删除某个BUFF   参数：type_buffID
    //5替换模型【评估】 参数：type_modelId
    //6移动到某个坐标  参数： type_x_y
    private boolean OnDealAiReach(Monster monster, Cfg_MonsterAi_Bean bean) {

        for (ReadArray ai : bean.getDeal().getValuees()) {
            int type = (int) ai.get(0);
            int value = (int) ai.get(1);
            switch (type) {
                case 1:
                    Skill skill = new Skill();
                    skill.setSkillId(value);
                    monster.getSkills().put(skill.getSkillId(), skill);
                    break;
                case 2:
                    monster.getSkills().remove(value);
                    break;
                case 3:
                    Manager.buffManager.deal().onAddBuff(monster, monster, value);
                    break;
                case 4:
                    Manager.buffManager.deal().onRemoveBuff(monster, value);
                    break;
                case 5:
                    log.error("找客户端商量 怎么做");
                    break;
                case 6:
                    Position pos = new Position(value, (int) ai.get(2));
                    BehaviorManager.CancelBehaviorByType(monster, BehaviorType.Move);
                    BehaviorManager.CancelBehaviorByType(monster, BehaviorType.Run);
                    BehaviorManager.CancelBehaviorByType(monster, BehaviorType.AttackMove);
                    monster.changeCurPos(pos, true);
                    Manager.mapManager.synStopMove(monster, false);
                    break;
                default:

            }
        }
        return true;
    }

    private int getCareerCount(Monster monster) {
        MapObject map = Manager.mapManager.getMap(monster.gainMapId());
        List<Byte> careers = new ArrayList<>();
        for (Player player : map.getPlayers().values()) {
            Byte career = player.getCareer();
            if (!careers.contains(career)) {
                careers.add(career);
            }
        }
        return careers.size();
    }

    private boolean isHaveCareer(Monster monster, int career) {
        MapObject map = Manager.mapManager.getMap(monster.gainMapId());
        for (Player player : map.getPlayers().values()) {
            if (player.getCareer() == career) {
                return true;
            }
        }
        return false;
    }

    /**
     * 护甲恢复处理
     *
     * @param monster 怪物
     * @param config  怪物配置
     */
    private void OnRecoverArmor(Monster monster, Cfg_Monster_Bean config) {
        if (monster.getArmorState() < 0) {
            return;
        }
        long now = TimeUtils.Time();
        //战斗中
        if (monster.isInBattle()) {
            if (monster.getArmorState() != 3 && monster.getArmor() < 1) {
                monster.setArmorState(3);
                monster.setArmorzerotime(now);
            }

            if (monster.getArmorState() == 3) {
                long offset = now - monster.getArmorzerotime();
                if (offset > config.getArmor_CD() * 1000) {
                    monster.setArmorState(0);
                    monster.setArmor(config.getArmor());
                    monster.setArmorbegintime(now);
                    monster.setArmorzerotime(now);
                    monster.onHpChange(monster);
                }
            }
            return;
        }
        //死亡
        if (EntityState.Dead.compare(monster.getState())) {
            return;
        }

        if (monster.getArmorState() < 1) {
            return;
        }

        switch (monster.getArmorState()) {
            case 1: {
                long offset = now - monster.getArmorbegintime();
                if (offset > 5000) {
                    monster.setArmorbegintime(monster.getArmorbegintime() + 5000);
                    monster.setArmorState(2);
                }
            }
            break;
            case 2: {
                long offset = now - monster.getArmorbegintime();
                int value = (int) (offset / 1000);
                value *= (int) (config.getArmor() * 10f / 100);
                int oldar = monster.getArmor();
                int minvar = Math.min(oldar + value, config.getArmor());
                monster.setArmorbegintime(now);
                monster.setArmor(minvar);
                monster.onHpChange(monster);
                if (minvar >= config.getArmor()) {
                    monster.setArmorState(0);
                    monster.setArmorbegintime(now);
                    monster.setArmorzerotime(now);
                }
            }
            break;
            case 3: {
                long offset = now - monster.getArmorzerotime();
                if (offset > config.getArmor_CD() * 1000) {
                    monster.setArmorState(0);
                    monster.setArmor(config.getArmor());
                    monster.setArmorbegintime(now);
                    monster.setArmorzerotime(now);
                    monster.onHpChange(monster);
                }
            }
            break;
        }
    }

    private void onCheckDamagerAndHart(Monster monster) {
        Cfg_Monster_Bean monsterBean = CfgManager.getCfg_Monster_Container().getValueByKey(monster.getModelId());
        if (monsterBean == null) {
            return;
        }

        MapObject map = Manager.mapManager.getMap(monster.gainMapId());
        if (map == null) {
            return;
        }

        for (Iterator<Hatred> it = monster.getHatreds().iterator(); it.hasNext(); ) {
            Hatred hatred = it.next();
            Fighter fighter = MapUtils.getOwnFighter(map, hatred.getTarget());
            if (fighter == null || !map.getPlayers().containsKey(fighter.getId())) {
                it.remove();
                continue;
            }
            if (fighter.getCamp() == monster.getCamp()) {
                it.remove();
                continue;
            }
            if (hatred.getTarget().gainMapId() == monster.gainMapId()) {

                //对距离没有要求则不清理
                if (monsterBean.getMonster_pos() < 1) {
                    continue;
                }

                //在距离之内则不清理，在距离之外就需要清理
                float dis = Utils.getDistance(hatred.getTarget(), monster);
                if (dis < monsterBean.getMonster_pos()) {
                    continue;
                }

                if (!Manager.worldHelpManager.getScript().canClear((Entity) hatred.getTarget())) {
                    continue;
                }
            }

            it.remove();
            Manager.hatredManager.clearHatred(hatred);
        }

        Iterator<Entry<Long, Long>> iter = monster.getDamages().entrySet().iterator();
        while (iter.hasNext()) {

            Entry<Long, Long> en = iter.next();
            long roleId = en.getKey();
            Player player = Manager.playerManager.getPlayerOnline(roleId);
            if (player == null) {
                iter.remove();
                continue;
            }
            //不在同一个地图也要清理
            if (player.gainMapId() != monster.gainMapId()) {
                iter.remove();
                continue;
            }
            if (!map.getPlayers().containsKey(en.getKey())) {
                iter.remove();
                continue;
            }
            //对距离没有要求则不清理
            if (monsterBean.getMonster_pos() < 1) {
                continue;
            }

            //在距离之内则不清理，在距离之外就需要清理
            float dis = Utils.getDistance(player, monster);
            if (dis < monsterBean.getMonster_pos()) {
                continue;
            }

            iter.remove();
        }
    }

    public YedMethodScript yedAi() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.MonsterYedAiBaseScript);
        if (is instanceof YedMethodScript) {
            return (YedMethodScript) is;
        }
        return null;
    }

    public YedMethodScript yedBaseAi() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.EntityAiCommonScript);
        if (is instanceof YedMethodScript) {
            return (YedMethodScript) is;
        }
        return null;
    }

}
