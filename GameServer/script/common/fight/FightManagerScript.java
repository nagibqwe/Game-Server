package common.fight;

import com.data.CfgManager;
import com.data.Global;
import com.data.bean.*;
import com.data.struct.ReadArray;
import com.game.attribute.BaseIntAttribute;
import com.game.backpack.structs.ItemCoinType;
import com.game.behavior.manager.BehaviorManager;
import com.game.behavior.structs.BehaviorType;
import com.game.behavior.structs.type.AttackMoveBehavior;
import com.game.behavior.structs.type.MagicDirMoveBehavior;
import com.game.behavior.structs.type.MagicMoveBehavior;
import com.game.behavior.structs.type.SkillMoveBehavior;
import com.game.buff.manager.BuffManager;
import com.game.buff.structs.Buff;
import com.game.buff.structs.BuffDefine;
import com.game.cooldown.structs.CooldownTypes;
import com.game.fight.manager.FightManager;
import com.game.fight.script.IFightManagerScript;
import com.game.fight.state.Type.SuperArmorState;
import com.game.fight.structs.FightEffect;
import com.game.fight.structs.FightEnum;
import com.game.fight.structs.FightFanalEnum;
import com.game.fight.structs.FightSkillEvent;
import com.game.horse.structs.HorseRideStateEnum;
import com.game.manager.Manager;
import com.game.map.manager.MapGpsUtil;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapUtils;
import com.game.monster.structs.Monster;
import com.game.nature.structs.HuaxinEntity;
import com.game.pet.structs.Pet;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerDefine;
import com.game.robot.ai.RobotAi;
import com.game.robot.struct.Robot;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.skill.config.FindTargetInfo;
import com.game.skill.config.SkillEvent;
import com.game.skill.config.SkillEventContainer;
import com.game.skill.config.SkillVisual;
import com.game.skill.config.event.*;
import com.game.skill.structs.Skill;
import com.game.skill.structs.SkillDefine;
import com.game.skill.structs.SkillLockTrajectory;
import com.game.skill.structs.SkillMagic;
import com.game.structs.*;
import com.game.task.structs.TaskHelp;
import com.game.team.structs.TeamInfo;
import com.game.utils.*;
import game.core.map.Position;
import game.core.script.IScript;
import game.core.util.AutoIncrementIntArray;
import game.core.util.BeanUtil;
import game.core.util.TimeUtils;
import game.message.FightMessage;
import game.message.FightMessage.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;


public class FightManagerScript implements IScript, IFightManagerScript {

    private static final Logger log = LogManager.getLogger(FightManagerScript.class);

    private final int CritType_None = 0;  //常态
    private final int CritType_Pursue = 1;  //追击
    private final int CritType_DHit = 2;  //连击
    private final int CritType_HitDef = 3;  //会心
    private final int CritType_Crit = 4;  //暴击

    @Override
    public int getId() {
        return ScriptEnum.FightManagerBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void onReqPlayHit(Player player, FightMessage.ReqPlayHit mess) {
    }

    @Override
    public void onReqPlayLockTrajectory(Player player, FightMessage.ReqPlayLockTrajectory mess) {

    }

    @Override
    public void onReqPlaySkillObject(Player player, FightMessage.ReqPlaySkillObject mess) {

    }

    @Override
    public void OnReqRollMove(Player player, ReqRollMove mess) {
        if (Manager.buffManager.deal().haveDing(player)) {
            log.error("不能翻滚 player" + player.nameIdString() + "正在定身中！");
            return;
        }
        if (Manager.buffManager.deal().haveDizziness(player)) {
            log.error("不能翻滚 player" + player.nameIdString() + "正在眩晕状态！");
            return;
        }
        if (player.isDie()) {
            log.error("死亡不能翻滚 player" + player.nameIdString());
            return;
        }
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        Position beginPos = new Position(mess.getSelfX(), mess.getSelfY());
        if (Utils.getDistance(beginPos, player.gainCurPos()) > 5) {
            log.error("不能翻滚 player" + player.nameIdString() + "开始点与本身玩家的搁置有5米远的差距");
            return;
        }
        Position movePos = new Position(mess.getMoveToX(), mess.getMoveToY());
        if (!Utils.isCanMove(map, beginPos, movePos)) {
            log.error("翻滚目标是阻挡 “" + beginPos + "”,pos=" + movePos + player.nameIdString());
            return;
        }
        Cfg_RollDodge_Bean config = CfgManager.getCfg_RollDodge_Container().getValueByKey(player.getRollLevel());
        if (Utils.getDistance(beginPos, movePos) > config.getMax_dis() * 1.3f / 100f) {
            log.error("翻滚距离过长 “" + beginPos + "”,pos=" + movePos + "roll=" + Utils.getDistance(beginPos, movePos) + "config=" + config.getMax_dis() * 1.3f / 100f + player);
            return;
        }
        if (Manager.cooldownManager.isCooldowning(player, CooldownTypes.RollCD, null, 50)) {
            log.error("翻滚冷却中 pos=" + movePos + player.nameIdString());
            return;
        }
        Manager.cooldownManager.addCooldown(player, CooldownTypes.RollCD, null, config.getCd_time());
        if (EntityState.Move.compare(player.getState())) {
            Manager.mapManager.synStopMove(player, true);
        }
        // player.setBrithProtect(0);
        player.getAmbs().clear();
        player.willEvents().clear();
        BehaviorManager.CancelAllBehavior(player);
        player.changeCurPos(movePos, true);

        ResRollMove.Builder msg = ResRollMove.newBuilder();
        msg.setUserID(player.getId());
        msg.setMoveToX(movePos.getX());
        msg.setMoveToY(movePos.getY());
        msg.setSelfX(beginPos.getX());
        msg.setSelfY(beginPos.getY());
        MessageUtils.send_to_roundPlayer(player, ResRollMove.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        //增加500毫秒霸体时间
        player.addFightState(new SuperArmorState(), config.getSuper_armor_time());
    }

    @Override
    public void OnReqUseSkill(Player player, FightMessage.ReqUseSkill mess) {
        if (player == null) {
            return;
        }
        Pet pet = Manager.petManager.getBattlePet(player);
        if (pet != null && pet.getId() == mess.getInfo().getUserID()) {
            Position dir = new Position(mess.getInfo().getDirX(), mess.getInfo().getDirY());
            pet.setDir(dir);
            this.attack(pet, null, null, mess);
            return;
        }

        if (player.getFlyswordAllInfo().getCurFlySwordSkillId() > 0 && player.getCurHuaxinEntity() != null
                && player.getCurHuaxinEntity().getId() == mess.getInfo().getUserID()) {
            Position target = new Position(mess.getUsePosX(), mess.getUsePosY());
            player.getCurHuaxinEntity().changeCurPos(target);
            player.getCurHuaxinEntity().setCurAttackTargetId(mess.getCurTargetId());
            Position pos = new Position(mess.getInfo().getDirX(), mess.getInfo().getDirY());
            player.getCurHuaxinEntity().setDir(pos);
            attack(player.getCurHuaxinEntity(), null, null, mess);
            return;
        }


        Position dir = new Position(mess.getInfo().getDirX(), mess.getInfo().getDirY());
        player.setDir(dir);
        player.targetDeferId = mess.getCurTargetId();
        //查看释放者是宠物还是玩家本人
        this.attack(player, null, null, mess);
    }

    @Override
    public void onReqPlaySelfMove(Player player, FightMessage.ReqPlaySelfMove mess) {

    }

    //获取冷却时间
    private int getCD(Cfg_Skill_Bean skillBean, SkillVisual sv) {
        if (skillBean.getCd() > sv.getCd()) {
            return skillBean.getCd();
        }
        return sv.getCd();
    }

    @Override
    public Skill getFightSkill(Entity player, MapObject map, int skillId) {
        return player.getSkills().get(skillId);
    }

    /**
     * 通用有进入技能事件处理器的统一流程入口
     *
     * @param attacker  攻击者
     * @param skill     使用技能
     * @param sv        效果
     * @param dirPos    方向
     * @param beAttacks 攻击者集合，可以不指定
     * @param copy      是否是被动技能触发
     */
    private void pushEvent(Fighter attacker, Skill skill, SkillVisual sv, Position dirPos, List<Fighter> beAttacks, int x, int y, boolean copy) {
        for (SkillEvent se : sv.getEventList()) {
            FightSkillEvent fse = new FightSkillEvent();
            fse.setMapId(attacker.gainMapId());
            //设置开始生效的时间
            fse.setStartTime(TimeUtils.Time() + se.getFpsTime());
            fse.setSe(se);
            fse.setDirPos(dirPos);
            fse.setSkill(skill);
            fse.setTargets(beAttacks);
            fse.setCanPlayerValid(sv.isCanPlayerValid());
            fse.setSkillVisualName(sv.getId());
            fse.setSerial(skill.getSerial());
            fse.getMgpos()[0] = x;
            fse.getMgpos()[1] = y;
            fse.setCopy(copy);


            if (se.getEventFrame() == 0) {
                //需要马上就要执行的事件
                doSkillEvent(attacker, fse, se.getEventID());
            } else {
                attacker.willEvents().add(fse);
            }
        }
    }

    @Override
    public void doSkillEvent(Fighter attacker, FightSkillEvent event, int serial) {

        SkillEvent se = event.getSe();

        if (se instanceof PlayHitEvent) {
            doPlayHitEvent(attacker, event);
        }

        if (se instanceof PlayLockTrajectoryEvent) {

            doPlayLockTrajectoryEvent(attacker, event);
        }

        if (se instanceof PlaySimpleSkillObjectEvent) {
            doPlaySimpleSkillObjectEvent(attacker, event);
        }

        if (se instanceof PlaySkillObjectEvent) {
            doPlaySkillObjectEvent(attacker, event);
        }

        if (se instanceof PlaySelfMoveEvent) {
            doPlaySelfMoveEvent(attacker, event);
        }
    }

    //针对福地boss的不攻击问题
    private boolean testCheckFigher(Fighter fighter) {
        if ((fighter instanceof Monster) && (fighter.gainMapModelId() == 2001 || fighter.gainMapModelId() == 2002 || fighter.gainMapModelId() == 2003 || fighter.gainMapModelId() == 2004)) {
            return true;
        }
        return false;
    }

    private void doPlayHitEvent(Fighter fighter, FightSkillEvent event) {

        if (!(fighter instanceof Pet) && (fighter.getType() != Fighter.NATURE_TYPE && fighter.getType() != Fighter.SKILLMAGIC_TYPE) && (fighter.isDie() || fighter.getCurHp() <= 0)) {
            return;
        }

        //被攻击者处于眩晕状态，则不攻击
        if (Manager.buffManager.deal().haveDizziness(fighter)) {
            log.error(" player" + fighter.nameIdString() + "正在眩晕状态！");
            return;
        }

        //如果处于定身状态
        if (Manager.buffManager.deal().haveDing(fighter)) {
            log.error(" player" + fighter.nameIdString() + "正在定身状态！");
            return;
        }

        PlayHitEvent hitEvent = (PlayHitEvent) event.getSe();
        //查找目标
        List<Fighter> targets = getAnemys(fighter, hitEvent.getFindInfo());
        event.setTargets(targets);

//        if (targets == null) {
//            targets = getAnemys(fighter, hitEvent.getFindInfo());
//            event.setTargets(targets);
//        }
        //开始计算伤害
        if (targets == null || targets.size() <= 0) {
            if (testCheckFigher(fighter)) {
                log.info("111最终目标为空:" + " 攻击者:" + fighter.getName() + " 堆栈:" + BeanUtil.getStack());
            }
            return;
        }

        //计算伤害值
        doHitAndSend(fighter, hitEvent.getUniqueID(), event.getSkill(), event.getSe(), event.getStartTime(), event.getTargets(), event.isCanPlayerValid(), event.isCopy());
    }

    private void doHitAndSend(Fighter fighter, int uniqueId, Skill skill, SkillEvent se, long startTime, List<Fighter> targets, boolean canPlayerValid, boolean copy) {

        if (!(fighter instanceof Pet) && (fighter.getType() != Fighter.NATURE_TYPE && fighter.getType() != Fighter.SKILLMAGIC_TYPE) && (fighter.getCurHp() <= 0 || fighter.isDie())) {
            return;
        }

        ResPlayHitEffect.Builder msg = ResPlayHitEffect.newBuilder();
        msg.setUserID(fighter instanceof SkillMagic ? ((SkillMagic) fighter).getOwnerId() : fighter.getId());
        msg.setHitEventID(uniqueId);
        for (Fighter defer : targets) {
            if (defer.isDie()) {
                continue;
            }
            HitEffectInfo.Builder info = calcHitEffectInfo(fighter, defer, skill, se, startTime, targets.size(), canPlayerValid, copy);
            if (info != null) {
                msg.addTargets(info);
            }
        }
        //发步伤害
        MessageUtils.send_to_roundPlayer(fighter, ResPlayHitEffect.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 计算伤害的处理流程
     *
     * @param attacker 攻击者
     * @param defer    受击者
     * @return 返回结果值
     */
    private HitEffectInfo.Builder calcHitEffectInfo(Fighter attacker, Fighter defer, Skill skill, SkillEvent se, long startTime, int targetSize, boolean canPlayerValid, boolean copy) {
        //宠物不受伤害
        if (defer instanceof Pet || defer instanceof HuaxinEntity) {
            return null;
        }
        //飞剑攻击人不计算伤害
        if (attacker instanceof HuaxinEntity) {
            if (defer instanceof Player) {
                return null;
            }
        }
        //被攻击结束打坐状态和骑乘状态
        if (defer instanceof Player) {
            Player player = (Player) defer;
            player.removeSate(EntityState.Sitting);
            if(attacker instanceof Player && player.getHorse().getRideState() != HorseRideStateEnum.UnRide){
                Manager.horseManager.deal().onReqChangeRideState((Player) defer, HorseRideStateEnum.UnRide.getState());
            }
        }

        SkillVisual sv = getSkillVisual(skill.getSkillId());
        if (sv == null) {
            return null;
        }
        MapObject map = Manager.mapManager.getMap(attacker.gainMapId());
        Cfg_Skill_Bean skillBean = CfgManager.getCfg_Skill_Container().getValueByKey(skill.getSkillId());
        int skillLevel = skill.getLevel();

        long damage;
        int type = 0;
        HitEffectInfo.Builder pack = HitEffectInfo.newBuilder();
        pack.setTargetID(defer.getId());
        if (defer.isDie()) {
            MapUtils.sendDead(attacker, defer);
            return null;
        }
//        if (attacker instanceof Nature) {
//            damage = FabaoDamage(map, (Nature) attacker);
//        } else if ((attacker instanceof SkillMagic && MapUtils.getFighter(map, ((SkillMagic)attacker).getOwnerId()) instanceof Nature)) {
//            damage = FabaoDamage(map, (Nature) MapUtils.getFighter(map, ((SkillMagic)attacker).getOwnerId()));
//        } else {
        BaseIntAttribute attackStateAtt = new BaseIntAttribute(AttributeType.ATTR_MAX);
        BaseIntAttribute deferStateAtt = new BaseIntAttribute(AttributeType.ATTR_MAX);
        //获取攻击者状态属性
        Manager.fightManager.trigger().trigger(map, attacker, defer, attackStateAtt, deferStateAtt, SkillDefine.ATTACK, copy);
        Manager.fightManager.trigger().trigger(map, defer, attacker, deferStateAtt, attackStateAtt, SkillDefine.BE_ATTACK, copy);
        Manager.fightManager.trigger().activeTrigger(skillBean, map, attacker, defer, attackStateAtt, deferStateAtt, SkillDefine.ATTACK, copy);

        type = calcCrit(map, attacker, defer, attackStateAtt, deferStateAtt);
        calcCritStateTrigger(skillBean, map, attacker, defer, attackStateAtt, deferStateAtt, type, copy);

        damage = calcDamage(pack, map, attacker, defer, skillBean, skill.getLevel(), type, targetSize, attackStateAtt, deferStateAtt, se);
//        }

        //受击者血量处理//扣除的真实血量
        long decHpEnd = 0;
        //处理固定血怪物
        boolean isValid = false;
        if ((attacker instanceof Player || attacker instanceof HuaxinEntity || attacker instanceof SkillMagic || attacker instanceof Pet) && defer instanceof Monster) {
            Monster monster = (Monster) defer;
            Cfg_Monster_Bean monsterBean = CfgManager.getCfg_Monster_Container().getValueByKey(monster.getModelId());
            if (monsterBean.getBeHurtType() == 0) {
                decHpEnd = damage;
                pack.setDamageHp(damage);
            }
            if (monsterBean.getBeHurtType() == 1) {
                decHpEnd = getDecHpEnd(map, attacker, monster, monsterBean, skillBean);
                pack.setDamageHp(decHpEnd);
            }
            if (monsterBean.getBeHurtType() == 2) {
                decHpEnd = getDecHpEnd(map, attacker, monster, monsterBean, skillBean);
                pack.setDamageHp(damage);
            }

            if (monsterBean.getBeHurtType() == 3) {
                long now = TimeUtils.Time();
                addFighter(map, monster, attacker);
                if (skillBean.getXp_skill_damage() != 0) {
                    decHpEnd = getDecHpEnd(map, getFighter(monster), monster, monsterBean, skillBean);
                    monster.getAllFighter().clear();
                    monster.setLastAttackTime(now);
                    isValid = true;
                } else if (now - monster.getLastAttackTime() >= 1000L) {
                    decHpEnd = getDecHpEnd(map, getFighter(monster), monster, monsterBean, skillBean);
                    monster.getAllFighter().clear();
                    monster.setLastAttackTime(now);
                } else {
                    decHpEnd = 0;
                }
                pack.setDamageHp(damage);
            }
            if (monsterBean.getBeHurtType() == 4) {
                decHpEnd = 0;
                pack.setDamageHp(damage);
            }
            //新加怪物伤害类型一定时间内消耗配置上线血量
            if (monsterBean.getBeHurtType() == 5){
                long now = TimeUtils.Time();
                decHpEnd = getDecHpEnd(map, attacker, monster, monsterBean, skillBean);
                if ((now - monster.getLastAttackTime()) >= Global.Monster_blood_max_cd ){
                    decHpEnd = decHpEnd >= monsterBean.getBeHurtValue() ? monsterBean.getBeHurtValue():decHpEnd;
                    monster.setCd_hurt(decHpEnd);
                    monster.setLastAttackTime(now);
                }else {
                    decHpEnd = monster.getCd_hurt() + decHpEnd >= monsterBean.getBeHurtValue()? (monsterBean.getBeHurtValue() - monster.getCd_hurt()) :decHpEnd;
                    monster.setCd_hurt(monster.getCd_hurt() + decHpEnd);
                }
                pack.setDamageHp(damage);
            }
        } else {
            decHpEnd = damage;
            pack.setDamageHp(damage);
        }

        if (defer.getCurHp() < decHpEnd) {
            decHpEnd = defer.getCurHp();
        }

        boolean pojia = calcBreakArmor(defer, skillBean, skillLevel);
        defer.setCurHp(defer.getCurHp() - decHpEnd);

        if ((attacker instanceof Player || attacker instanceof HuaxinEntity || attacker instanceof SkillMagic || attacker instanceof Pet) && defer instanceof Monster) {
            Cfg_Monster_Bean monsterBean = CfgManager.getCfg_Monster_Container().getValueByKey(((Monster) defer).getModelId());
            if (monsterBean.getBeHurtType() == 2 || monsterBean.getBeHurtType() == 3 || monsterBean.getBeHurtType() == 4 || monsterBean.getBeHurtType() == 5 ) {
                defer.beAttack(attacker, skillBean, skillLevel, damage);
            } else {
                defer.beAttack(attacker, skillBean, skillLevel, decHpEnd);
            }
        } else {
            defer.beAttack(attacker, skillBean, skillLevel, decHpEnd);
        }

        if (decHpEnd > 0) {
            defer.onHpChange(attacker);
        }

        pack.setIsDead(false);

        if (defer.getCurHp() <= 0) {
            defer.setCurHp(0);
            defer.doDie(attacker);
            pack.setIsDead(true);
            sendDead(attacker, defer);
        }

        if (attacker.getCurHp() <= 0) {
            attacker.doDie(defer);
            sendDead(defer, attacker);
        }

        float hitDis = se.getHitDis(se.getHitType());
        Position targetPos;

        int effect = calcEffect(attacker, defer, se, type, canPlayerValid, pojia);

        if (isValid) {
            effect = FightEffect.MultipleAtk.addEffect(effect);
        }

        if (hitDis <= 0) {
            targetPos = defer.gainCurPos();
        } else {
            Position dir;
            if (FightEffect.Catch.compareTo(effect)) {
                dir = Utils.getDir(defer.gainCurPos(), attacker.gainCurPos());
                if (Utils.getDistance(defer.gainCurPos(), attacker.gainCurPos()) <= hitDis) {
                    targetPos = attacker.gainCurPos();
                } else {
                    targetPos = Utils.getPosByDir(defer.gainCurPos(), dir, hitDis);
                }
            } else {
                dir = Utils.getDir(attacker.gainCurPos(), defer.gainCurPos());
                targetPos = Utils.getPosByDir(defer.gainCurPos(), dir, hitDis);
            }

            if (!Utils.isCanMove(map, defer.gainCurPos(), targetPos)) {
                targetPos = defer.gainCurPos();
            }
        }
        boolean isPosTrue = calcEvent(attacker, defer, se, se.getFpsTime(), targetPos.getX(), targetPos.getY(), startTime, effect);
        if (isPosTrue) {
            pack.setPosx(targetPos.getX());
            pack.setPosy(targetPos.getY());
        } else {
            pack.setPosx(defer.gainCurPos().getX());
            pack.setPosy(defer.gainCurPos().getY());
        }
        pack.setEffect(effect);
        return pack;
    }

    private long getDecHpEnd(MapObject mapObject, Fighter attacker, Monster defer, Cfg_Monster_Bean monsterBean, Cfg_Skill_Bean skill_bean) {
        if (attacker == null) {
            return 0;
        }
        //时长怪 根据 人数 添加 伤害比列
        long decHpEnd = monsterBean.getBeHurtValue();

        if (skill_bean.getXp_skill_damage() != 0 && monsterBean.getBeHurtType() == 3) {
            decHpEnd = monsterBean.getBeHurtValue() * (skill_bean.getXp_skill_damage() / 10000L);
        }

        float maxFight = Global.Number_blood_Max.get(0);
        float fighterSize = defer.getAllFighter().size();
        fighterSize = fighterSize > maxFight ? (maxFight + 1) : fighterSize;
        fighterSize = monsterBean.getBeHurtType() == 3 ? fighterSize : 1;
        float hurtPer = Global.Number_blood_Max.get(1) / 100f;
        //一血怪的血量显示带上战力压制

        if (defer.getScore() == 0) {
            int addhurt = (int) ((((fighterSize - 1) / maxFight) * hurtPer) * decHpEnd);
            return decHpEnd + addhurt;
        }


        double fight;
        if (attacker instanceof SkillMagic) {
            attacker = MapUtils.getFighter(mapObject, ((SkillMagic) attacker).getOwnerId());
        }

        if (attacker instanceof HuaxinEntity) {
            attacker = MapUtils.getFighter(mapObject, ((HuaxinEntity) attacker).getOwnerId());
        }

        fight = attacker.getFightPoint() * 1f / defer.getScore();

        float limitMax = Global.Damage_MonsterPveNum5 / 100f;
        float max = Global.Damage_MonsterPveNum6 / 100f;
        float limitMin = Global.Damage_MonsterPveNum7 / 100f;
        float min = Global.Damage_MonsterPveNum8 / 100f;
        fight = fight >= limitMax ? max : (fight < limitMin ? min : fight);
//        float max = Global.Damage_FightFixpressMax / 10000f;
//        float min = Global.Damage_FightFixpressMin / 10000f;
//        fight = fight >= max ? max : (fight < min ? min : fight);
        decHpEnd *= fight;
        float ATTR_Monster_Dec = (1 - defer.getAttribute().getAdditionValue(AttributeType.ATTR_Monster_Dec) * 1F / 10000);
        decHpEnd *= ATTR_Monster_Dec;
        float hurtPerValue = ((((fighterSize - 1) / maxFight) * hurtPer) * decHpEnd);
        decHpEnd += hurtPerValue;
        return decHpEnd;
    }

    private void addFighter(MapObject mapObject, Monster monster, Fighter fighter) {
        if (fighter instanceof SkillMagic) {
            fighter = MapUtils.getFighter(mapObject, ((SkillMagic) fighter).getOwnerId());
        }

        if (fighter instanceof HuaxinEntity) {
            fighter = MapUtils.getFighter(mapObject, ((HuaxinEntity) fighter).getOwnerId());
        }

        if (fighter instanceof Player) {
            monster.getAllFighter().add(fighter);
        }
    }

    private Fighter getFighter(Monster monster) {
        Fighter maxFighter = null;
        for (Fighter fighter : monster.getAllFighter()) {
            if (maxFighter == null) {
                maxFighter = fighter;
                continue;
            }

            if (maxFighter.getFightPoint() < fighter.getFightPoint()) {
                maxFighter = fighter;
            }
        }
        return maxFighter;
    }

    @Override
    public void attack(SkillMagic magic, Fighter owner, Skill skill, SkillEvent se, List<Fighter> targets) {
//        log.info( magic.nameIdString() + " 发伤害！" + magic.getType() + "===" + magic.getSkillId()+ "====" + magic.isDie() + "==hp"+ magic.getCurHp());
        doHitAndSend(magic, se.getUniqueID(), skill, se, TimeUtils.Time(), targets, true, false);
    }

    private void doPlayLockTrajectoryEvent(Fighter fighter, FightSkillEvent fevent) {
        PlayLockTrajectoryEvent event = (PlayLockTrajectoryEvent) fevent.getSe();
        //查找目标
        List<Fighter> targets = fevent.getTargets();
        if (targets == null) {
            targets = getAnemys(fighter, event.getFindInfo());
            fevent.setTargets(targets);
        }
        //开始计算伤害
        if (targets == null || targets.size() <= 0) {
            log.error("当前没有可攻击的对象了！");
            return;
        }
        //可以计算重复的目标
        if (event.getCanHitRepeatTarget() == 1 && targets.size() < event.getFindInfo().getMaxTargetCount()) {
            int size = targets.size();
            int oldLen = targets.size();
            int index = 0;
            while (size < event.getFindInfo().getMaxTargetCount()) {
                targets.add(targets.get(index));
                size += 1;
                index += 1;
                if (index >= oldLen) {
                    index = 0;
                }
            }
        }
        ResPlayLockTrajectory.Builder msg = ResPlayLockTrajectory.newBuilder();
        msg.setInfo(Manager.skillManager.buildSkillBaseInfo(fighter, fevent.getSkill().getSkillId(), fevent.getSerial(), null));
        msg.setEventID(event.getEventID());
        for (Fighter defer : targets) {
            msg.addTargetList(defer.getId());
            SkillLockTrajectory st = new SkillLockTrajectory();
            st.setEvent(event);
            st.setInitPos(fighter.gainCurPos());
            st.setCurPos(fighter.gainCurPos());
            st.setMapId(fighter.gainMapId());
            st.setStartTime(TimeUtils.Time());
            st.setTargetId(defer.getId());
            st.setSkillId(fevent.getSkill().getSkillId());
            fighter.getLockTrajectories().add(st);
        }
        MessageUtils.send_to_roundPlayer(fighter, ResPlayLockTrajectory.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private void doPlaySimpleSkillObjectEvent(Fighter fighter, FightSkillEvent fevent) {
        if (fighter.isDie()) {
            return;
        }

        MapObject map = Manager.mapManager.getMap(fighter.gainMapId());
        PlaySimpleSkillObjectEvent event = (PlaySimpleSkillObjectEvent) fevent.getSe();
        //产生一个召唤物
        SkillMagic magic = Manager.skillManager.createMagic(fevent.getSkillVisualName());
        magic.setOwnerId(fighter.getId());
        magic.setSkillId(fevent.getSkill().getSkillId());
        MapGpsUtil.CopyGPS(fighter.getCurGps(), magic.getCurGps());
        magic.setIntType(event.getPosType());
        //释放一个目标的召唤物
        if (event.getPosType() == FightFanalEnum.MagicPosType.MainTargetPos.getValue()) {
            Fighter mainTarget = ((Entity) fighter).gainCurAttackTarget(map);
            Position targetPos = null;
            if (mainTarget != null) {
                targetPos = mainTarget.gainCurPos();
                float dis = Utils.getDistance(fighter.gainCurPos(), targetPos);
                if (dis < (event.getMaxDis() * 1.5f) + fighter.getRadius()) {
                    magic.changeCurPos(targetPos);
                }
            }

            if (targetPos == null) {
                Position dirpos = fevent.getDirPos() == null ? fighter.getDir() : fevent.getDirPos();
                targetPos = Utils.getCanFightPosByDir(map, fighter.gainCurPos(), dirpos, event.getMaxDis());
                magic.changeCurPos(targetPos);
            }
            //如果是负值则不做设置处理
            if (fevent.getX() > 0 && fevent.getY() > 0) {
                Position dirpos = new Position(fevent.getX(), fevent.getY());
                float dis = Utils.getDistance(fighter.gainCurPos(), dirpos);
                if (dis < (event.getFindInfo().getAttackDis() * 5f) + fighter.getRadius()) {
                    magic.changeCurPos(dirpos);
                }
            }
        }
        //放入伤害数据
        for (int i = 0; i < event.getHitCount(); ++i) {
            magic.getSerials().add(event.getEventID());
        }
        //生效的时间值
        magic.setStart(TimeUtils.Time() + (long) (event.getStartHitFrame() * SkillVisual.OneFrameTime * 1000));
        //加入， 进入地图
        fighter.getMagics().add(magic.getId());
        Manager.mapManager.manager().onEnterMap(magic);

        //发送协议
        ResPlaySimpleSkillObject.Builder msg = ResPlaySimpleSkillObject.newBuilder();
        msg.setInfo(Manager.skillManager.buildSkillBaseInfo(fighter, fevent.getSkill().getSkillId(), fevent.getSerial(), null));
        msg.setEventID(event.getEventID());
        msg.setID(fighter.getId());
        msg.setPosX(magic.gainX());
        msg.setPosY(magic.gainY());
        MessageUtils.send_to_roundPlayer(fighter, ResPlaySimpleSkillObject.MsgID.eMsgID_VALUE, msg.build().toByteArray());

    }

    private void doPlaySkillObjectEvent(Fighter attacker, FightSkillEvent fevent) {
        PlaySkillObjectEvent magicEvent = (PlaySkillObjectEvent) fevent.getSe();
        SkillVisual sv2 = SkillEventContainer.getInstance().getVisuals().get(magicEvent.getSkillName());
        if (sv2 == null) {
            log.error("玩家无这个技能效果配置缺失skillId=" + magicEvent.getSkillName() + "monster" + attacker);
            return;
        }
        MapObject map = Manager.mapManager.getMap(attacker.gainMapId());

        SkillMagic magic = Manager.skillManager.createMagic(magicEvent.getSkillName());
        magic.setOwnerId(attacker.getId());
        magic.setSkillId(fevent.getSkill().getSkillId());
        MapGpsUtil.CopyGPS(attacker.getCurGps(), magic.getCurGps());
        magic.setIntType(magicEvent.getPosType());
        magic.setInitTime(TimeUtils.Time());
        //当前世间到召唤事件生效时间
        magic.setStart(TimeUtils.Time());

        for (SkillEvent event : sv2.getEventList()) {
            if (event instanceof PlayHitEvent) {
                magic.getSerials().add(event.getEventID());
                magic.setRadius(event.getHitDis(0));
            }
        }
        //释放一个目标的召唤物
        if (magicEvent.getPosType() == FightFanalEnum.MagicPosType.MainTargetPos.getValue()) {
            Fighter mainTarget = ((Entity) attacker).gainCurAttackTarget(map);
            Position targetPos = null;
            if (mainTarget != null) {
                targetPos = mainTarget.gainCurPos();
                float dis = Utils.getDistance(attacker.gainCurPos(), targetPos);
                if (dis < (magicEvent.getMaxDis() * 1.5f) + attacker.getRadius()) {
                    magic.changeCurPos(targetPos);
                }
            }

            if (targetPos == null) {
                Position dirpos = fevent.getDirPos() == null ? attacker.getDir() : fevent.getDirPos();
                targetPos = Utils.getCanFightPosByDir(map, attacker.gainCurPos(), dirpos, magicEvent.getMaxDis());
                magic.changeCurPos(targetPos);
            }
            //如果是负值则不做设置处理
            if (fevent.getX() > 0 && fevent.getY() > 0) {
                Position dirpos = new Position(fevent.getX(), fevent.getY());
                float dis = Utils.getDistance(attacker.gainCurPos(), dirpos);
                if (dis < (magicEvent.getMaxDis() * 5f) + attacker.getRadius()) {
                    magic.changeCurPos(dirpos);
                }
            }
        }

        if (magicEvent.getMoveType() != 2) {
            //加入， 进入地图
            attacker.getMagics().add(magic.getId());
            Manager.mapManager.manager().onEnterMap(magic);
        }

        ResPlaySkillObject.Builder msg = ResPlaySkillObject.newBuilder();
        msg.setPosX(magic.gainX());
        msg.setPosY(magic.gainY());
        msg.setID(magic.getId());
        msg.setOwnerID(attacker.getId());
        msg.setVisualDef(magicEvent.getSkillName());
        msg.setMoveSpeed(magicEvent.getMoveSpeed());
        msg.setMpveAddSpeed(magicEvent.getMoveAddSpeed());
        //计算玩家的移动点
        if (magicEvent.getMoveType() == 0) {
            MessageUtils.send_to_roundPlayer(attacker, ResPlaySkillObject.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            return;
        }

        //定点移动
        if (magicEvent.getMoveType() == 1) {
            Position dir = getPlayerAttackDir(map, attacker);
            //计算方向
            double x = dir.getX();
            double y = dir.getY();

            if (Float.compare(magicEvent.getFixDirOffsetAngle(), 0f) != 0) {

                double cos = Math.cos(magicEvent.getFixDirOffsetAngle() * Math.PI / 180);
                double sin = Math.sin(magicEvent.getFixDirOffsetAngle() * Math.PI / 180);
                double fsin = Math.sin(-1 * magicEvent.getFixDirOffsetAngle() * Math.PI / 180);

                x = dir.getX() * cos + dir.getY() * sin;
                y = dir.getY() * cos + dir.getX() * fsin;
            }

            MagicMoveBehavior mmb = new MagicMoveBehavior(magic);
            mmb.setaSpeed(magicEvent.getMoveAddSpeed());
            mmb.setSpeed(magicEvent.getMoveSpeed());
            mmb.setBeginTime(TimeUtils.Time());
            mmb.setDirPos(new Position((float) x, (float) y));
            mmb.setLastDis(0);
            BehaviorManager.InsertBehavior(magic, mmb);

            float t = sv2.getCd() / 1000.0f;
            double s = magicEvent.getMoveSpeed() * t + 0.5d * magicEvent.getMoveAddSpeed() * Math.pow(t, 2);

            Position pos = Utils.getPosByDir(magic.gainCurPos(), mmb.getDirPos(), (float) s);

//            log.error(" dir =" + mmb.getDirPos() + "name="+ attacker.nameIdString()+" player.dir=" + attacker.getDir());
//           log.error(" cur=" + magic.gainCurPos() + ", targetPos=" + pos + ", len =" + s);
            msg.addMovePosList(MapUtils.getPos(magic.gainCurPos()));
            msg.addMovePosList(MapUtils.getPos(pos));
            MessageUtils.send_to_roundPlayer(attacker, ResPlaySkillObject.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            return;
        }

        //追踪移动
        if (magicEvent.getMoveType() == 2) {
            Fighter mainTarget = getFirstDefer(attacker, 10);
            if (mainTarget == null) {
                return;
            }
            Position dirPos = Utils.getDir(attacker.gainCurPos(), mainTarget.gainCurPos());
            MagicDirMoveBehavior mmb = new MagicDirMoveBehavior(magic);
            mmb.setaSpeed(magicEvent.getMoveAddSpeed());
            mmb.setSpeed(magicEvent.getMoveSpeed());
            mmb.setBeginTime(TimeUtils.Time());
            mmb.setDirPos(dirPos);
            mmb.setLastDis(0);
            mmb.setDefer(mainTarget);
            mmb.setXy(mainTarget.gainCurPos().ceilX(), mainTarget.gainCurPos().ceilY());
            mmb.setSkillVisalName(magicEvent.getSkillName());
            BehaviorManager.InsertBehavior(magic, mmb);
            //加入， 进入地图
            attacker.getMagics().add(magic.getId());
            Manager.mapManager.manager().onEnterMap(magic);
            msg.addMovePosList(MapUtils.getPos(magic.gainCurPos()));
            msg.addMovePosList(MapUtils.getPos(mainTarget.gainCurPos()));
        }

        MessageUtils.send_to_roundPlayer(attacker, ResPlaySkillObject.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private void doPlaySelfMoveEvent(Fighter fighter, FightSkillEvent event) {
        PlaySelfMoveEvent event1 = (PlaySelfMoveEvent) event.getSe();

        MapObject map = Manager.mapManager.getMap(fighter.gainMapId());
        if (map == null) {
            return;
        }
        Entity mover = (Entity) fighter;
        Fighter target = mover.gainCurAttackTarget(map);

        if (target != null) {
            //修改方向
            mover.setDir(Utils.getDir(fighter.gainCurPos(), target.gainCurPos()));
            ResChangeAttackDirRes.Builder msg = ResChangeAttackDirRes.newBuilder();
            msg.setDirX(mover.getDir().getX());
            msg.setDirY(mover.getDir().getY());
            msg.setRoleId(mover.getId());
            MessageUtils.send_to_roundPlayer(mover, ResChangeAttackDirRes.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
/**
 *
 TargetFace = 0,     //目标面前
 TargetBack = 1,     //目标背后
 FixedDis = 2,       //固定距离
 MoveBack = 3,       //后退
 */
        int moveType = event1.getMoveType();
        Position targetPos = null;
        float speed = mover.gainFinalMoveSpeed() / 100.0f;
        if (speed <= 0) {
            log.info(fighter.nameIdString() + "没有移动参数！");
            return;
        }
        switch (moveType) {
            case 0: {
                float dis;
                if (target != null) {
                    dis = Utils.getDistance(fighter.gainCurPos(), target.gainCurPos());
                    if (dis > event1.getMoveDis()) {
                        dis = event1.getMoveDis();
                    } else {
                        dis -= target.getRadius();
                    }
                } else {
                    dis = event1.getNoTargetDis();
//                    dis = speed * event1.getMoveTime();
//                    if (dis > event1.getNoTargetDis()) {
//                    }
                }
                targetPos = Utils.getMovePosByDir(map, fighter.gainCurPos(), fighter.getDir(), dis);
            }
            break;
            case 1: {
                float dis = 10f;
                if (target != null) {
                    dis = Utils.getDistance(fighter.gainCurPos(), target.gainCurPos());
                    if (dis > event1.getMoveDis()) {
                        dis = event1.getMoveDis();
                    } else {
                        dis += target.getRadius();
                    }
                } else {
                    dis = speed * event1.getMoveTime();
                    if (dis > event1.getNoTargetDis()) {
                        dis = event1.getNoTargetDis();
                    }
                }
                targetPos = Utils.getMovePosByDir(map, fighter.gainCurPos(), fighter.getDir(), dis);
            }
            break;
            case 2:
                targetPos = Utils.getMovePosByDir(map, fighter.gainCurPos(), fighter.getDir(), event1.getMoveDis());
                break;
            case 3: {
                float moveDis = speed * event1.getMoveTime();
                targetPos = Utils.getMovePosByDir(map, fighter.gainCurPos(), fighter.getDir(), moveDis);
            }
            break;
            default:
                break;
        }
        if (targetPos == null) {
            return;
        }

        ResPlaySelfMove.Builder msg = ResPlaySelfMove.newBuilder();
        msg.setEventID(event1.getEventID());
        msg.setInfo(Manager.skillManager.buildSkillBaseInfo(fighter, event.getSkill().getSkillId(), event.getSerial(), target));
        msg.setCurPosX(((Entity) fighter).gainX());
        msg.setCurPosY(((Entity) fighter).gainY());
        msg.setTarPosX(targetPos.getX());
        msg.setTarPosY(targetPos.getY());
        MessageUtils.send_to_roundPlayer(fighter, ResPlaySelfMove.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        //设置玩家的移动目标
        SkillMoveBehavior mb = new SkillMoveBehavior(mover);

        float moveSpeed = event1.getMoveTime();

        if (moveSpeed == 0) {
            moveSpeed = speed;
        } else {
            moveSpeed = Utils.getDistance(mover.gainCurPos(), targetPos) / event1.getMoveTime();
            if (moveSpeed == 0) {
                moveSpeed = speed;
            }
        }


        mb.setSpeed(moveSpeed);
        mb.setTarget(targetPos);
        BehaviorManager.InsertOnlyBehavior(mover, mb);
//        mover.changeCurPos(targetPos, true);
    }

    /**
     * 计算是否进入伤害列表
     *
     * @param anemys    伤害列表
     * @param find      查找规则
     * @param fighter   攻击者
     * @param defer     防御者
     * @param attackDir 攻击方向
     * @return 返回是否加入了伤害列表
     */
    private boolean checkDeferEnterHitList(List<Fighter> anemys, FindTargetInfo find, Fighter fighter, Fighter defer, Position attackDir) {

        if (defer.getId() == fighter.getId()) {
            return false;
        }
        Fighter owner = null;
        if (fighter instanceof Pet) {
            Pet pet = (Pet) fighter;
            if (pet.getOwnerId() == defer.getId()) {
                return false;
            }
            MapObject map = Manager.mapManager.getMap(pet.gainMapId());
            owner = MapUtils.getFighter(map, pet.getOwnerId());
            if (owner == null) {
                return false;
            }
        }

        if (fighter instanceof HuaxinEntity) {
            HuaxinEntity huaxin = (HuaxinEntity) fighter;
            if (huaxin.getOwnerId() == defer.getId()) {
                return false;
            }
            if (defer instanceof Player) {
                return false;
            }
            if (defer instanceof Robot) {
                return false;
            }
            MapObject map = Manager.mapManager.getMap(huaxin.gainMapId());
            owner = MapUtils.getFighter(map, huaxin.getOwnerId());
            if (owner == null) {
                return false;
            }
        }

        if (defer instanceof Monster) {
            if (TimeUtils.Time() < defer.getBrithProtect()) {
                return false;
            }
        }
        if (defer instanceof Player) {
            if (TimeUtils.Time() < defer.getBrithProtect()) {
                if (testCheckFigher(fighter)) {
                    log.info("出生保护? " + fighter.getName() + " 堆栈:" + BeanUtil.getStack());
                }
                return false;
            }
        }
        if (ShapeUtils.inAttackArea(find.getShape(), fighter.gainCurPos(), attackDir, defer.gainCurPos()) && !anemys.contains(defer)) {
            if (fighter instanceof Pet) {
                if (isCanPk(owner, defer)) {
                    anemys.add(defer);
                    return true;
                } else {
                    return false;
                }
            }
            if (isCanPk(fighter, defer)) {
                anemys.add(defer);
                return true;
            }
        }

        return false;
    }

    private Fighter getFirstDefer(Fighter fighter, float dis) {
        MapObject map = Manager.mapManager.getMap(fighter.gainMapId());
        if (map == null) {
            return null;
        }
        Entity attacker = (Entity) fighter;

        Fighter curTarget = attacker.gainCurAttackTarget(map);
        if (curTarget != null) {
            return curTarget;
        }

        if (fighter.getHatreds().size() > 0) {
            return fighter.getHatreds().get(0).getTarget();
        }

        //检查周围的所有玩家
        List<Fighter> tempEnemys = MapUtils.getFighter(map, fighter.gainCurPos(), dis, MapUtils.FighterFlag.All.v);

        if (tempEnemys == null || tempEnemys.isEmpty()) {
            return null;
        }

        for (Fighter defer : tempEnemys) {
            if (isCanPk(fighter, defer)) {
                return defer;
            }
        }

        return null;
    }

    /**
     * 获得攻击玩家的攻击方向
     *
     * @param map      地图
     * @param attacker 攻击者
     * @return 返回正确的攻击方向
     */
    private Position getPlayerAttackDir(MapObject map, Fighter attacker) {
        if (attacker instanceof Player) {
            Player player = (Player) attacker;
            Fighter defer = MapUtils.getFighter(map, player.targetDeferId);
            if (defer == null) {
                return player.getDir();
            } else {
                return Utils.getDir(player.gainCurPos(), defer.gainCurPos());
            }
        }
        return attacker.getDir();
    }

    /**
     * 计算伤害列表中
     *
     * @param fighter 攻击者
     * @param find    查找范围规
     * @return 可伤害的列表
     */
    private List<Fighter> getAnemys(Fighter fighter, FindTargetInfo find) {
        List<Fighter> anemys = new ArrayList<>();

        MapObject map = Manager.mapManager.getMap(fighter.gainMapId());
        if (map == null) {
            if (testCheckFigher(fighter)) {
                log.info("地图为空,目标数量:" + anemys.size() + " 攻击者:" + fighter.getName() + " 堆栈:" + BeanUtil.getStack());
            }
            return anemys;
        }

        //检查当前攻击目标是否还存在
        int attackSize = 0;
        Player player = fighter instanceof Player ? (Player) fighter : null;
        if (player != null) {
            Fighter curTarget = player.gainCurAttackTarget(map);
            if (curTarget != null) {
                if (checkDeferEnterHitList(anemys, find, fighter, curTarget, getPlayerAttackDir(map, fighter))) {
                    attackSize += 1;
                }
            } else {
                player.setCurAttackTargetId(0);
            }
        }

        //检测仇恨列表是否有可攻击的对象
        if (fighter.getHatreds() != null && fighter.getHatreds().size() > 0) {
            for (Hatred hat : fighter.getHatreds()) {
                if (attackSize >= find.getMaxTargetCount()) {
                    break;
                }
                Fighter defer = hat.getTarget();
                if (checkDeferEnterHitList(anemys, find, fighter, defer, getPlayerAttackDir(map, fighter))) {
                    attackSize += 1;
                }
            }
        }
        //设置主攻击者对象
        if (player != null && anemys.size() > 0) {
            if (player.gainCurAttackTargetId() == 0) {
                player.setCurAttackTargetId(anemys.get(0).getId());
            }
        }

        if (attackSize >= find.getMaxTargetCount()) {
            if (testCheckFigher(fighter)) {
                log.info("检测仇恨列表的超出最大数量,目标数量:" + anemys.size() + " 攻击者:" + fighter.getName() + " 堆栈:" + BeanUtil.getStack());
            }
            return anemys;
        }

        //检查周围的所有可攻击实体
        List<Fighter> tempEnemys = MapUtils.getFighter(map, fighter.gainCurPos(), find.getAttackDis(), MapUtils.FighterFlag.All.v);

        if (tempEnemys == null || tempEnemys.isEmpty()) {
            if (testCheckFigher(fighter)) {
                log.info("检测所有可攻击对象为空,目标数量:" + anemys.size() + " 攻击者:" + fighter.getName() + " 堆栈:" + BeanUtil.getStack());
            }
            return anemys;
        }

        for (Fighter defer : tempEnemys) {
            if (attackSize >= find.getMaxTargetCount()) {
                break;
            }
            if (checkDeferEnterHitList(anemys, find, fighter, defer, getPlayerAttackDir(map, fighter))) {
                attackSize += 1;
            }
        }

        //设置主攻击者对象
        if (player != null && anemys.size() > 0) {
            if (player.gainCurAttackTargetId() == 0) {
                player.setCurAttackTargetId(anemys.get(0).getId());
            }
        }
        return anemys;
    }

    @Override
    public void addSummonImmediately(Fighter attacker, Skill skill, Position pos, Position dir) {
        int skillid = skill.getSkillId();
        Cfg_Skill_Bean skillBean = CfgManager.getCfg_Skill_Container().getValueByKey(skillid);
        if (skillBean == null) {
            log.error(String.format("addSummonImmediately,发现对应的技能编号[%d]没对,找不到技能配置", skillid));
            return;
        }
        //获取技能效果配置
        SkillVisual sv = SkillEventContainer.getInstance().getVisuals().get(skillBean.getVisualDef());
        if (sv == null) {
            log.error("addSummonImmediately sv null " + skillid);
            return;
        }
        long nextInit = TimeUtils.Time();

        List<PlaySkillObjectEvent> magicEvents = getMagicEvent(sv);
        for (PlaySkillObjectEvent magicEvent : magicEvents) {
            SkillVisual sv2 = SkillEventContainer.getInstance().getVisuals().get(magicEvent.getSkillName());
            if (sv2 == null) {
                log.error(String.format("addSummonImmediately sv2 == null skillid:%d event:%s", skillid, magicEvent.getSkillName()));
                continue;
            }

            SkillMagic magic = Manager.skillManager.createMagic(magicEvent.getSkillName());
            magic.setOwnerId(attacker.getId());
            magic.setSkillinfo(skill);
            MapGpsUtil.CopyGPS(attacker.getCurGps(), magic.getCurGps());
            if (pos != null) {
                magic.changeCurPos(pos, false);
            }
            //magic.changeCurPos(pos);
            // buff 加的不管位置类型,直接就在buff拥有者脚底下
            magic.setIntType(0);
            magic.setStart(TimeUtils.Time() + magicEvent.getFpsTime()); //当前世间到召唤事件生效时间
            magic.setInitTime(nextInit);
            magic.setDir(dir);
            nextInit = magic.getStart();

            for (SkillEvent event : sv2.getEventList()) {
                if (event instanceof PlayHitEvent) {
                    magic.getSerials().add(event.getEventID());
                }
            }
            attacker.getMagics().add(magic.getId());
            Manager.mapManager.manager().onEnterMap(magic);
        }
    }

    @Override
    public void doAction(Fighter src, List<AutoIncrementIntArray> actionParam, List<Fighter> targets) {
        final int idxActiontype = 0;
        final int idxActionParamStart = 1;
        final int idxPosx = 2;
        final int idxPosy = 3;
        for (AutoIncrementIntArray params : actionParam) {
            // 不同的action做不同的事情
            int actiontype = params.get(idxActiontype);
            if (actiontype == BuffDefine.ACTION_TYPE.ADDBUFF.v) {
                for (Fighter target : targets) {
                    for (int idx = idxActionParamStart; idx < params.length(); ++idx) {
                        int buffid = params.get(idx);
                        if (buffid == 0) {
                            continue;
                        }
                        BuffManager.getInstance().deal().onAddBuff(src, target, buffid);
                        //buff.pushAdd(src, target, buffid);
                        log.info(String.format("[%s%d]给[%s%d]添加了%d buff", src.getName(), src.getId(), target.getName(), target.getId(), buffid));
                    }
                }
            } else if (actiontype == BuffDefine.ACTION_TYPE.REMOVEBUFF.v) {
                for (Fighter target : targets) {
                    for (int idx = idxActionParamStart; idx < params.length(); ++idx) {
                        int buffid = params.get(idx);
                        if (buffid == 0) {
                            continue;
                        }
                        BuffManager.getInstance().deal().onRemoveBuff((Entity) target, buffid);
                        //buff.pushRemove(target, buffid);
                        log.info(String.format("[%s%d]给[%s%d]移除了%d buff", src.getName(), src.getId(), target.getName(), target.getId(), buffid));
                    }
                }
            } else if (actiontype == BuffDefine.ACTION_TYPE.CREATESUMMON.v) {
                int skillid = params.get(idxActionParamStart);
                // 看看后面还有没有更多的参数x,y
                Position pos = null;
                if (params.length() > idxPosy) {
                    pos = new Position(params.get(idxPosx), params.get(idxPosy));
                }
                Cfg_Skill_Bean skillBean = CfgManager.getCfg_Skill_Container().getValueByKey(skillid);
                if (skillBean == null) {
                    log.error(String.format("当试图创造一个召唤物时,发现对应的技能编号[%d]没对,找不到技能配置", skillid));
                    return;
                }
                Skill skill = new Skill();
                skill.setSkillId(skillid);
                skill.setLevel(1);
                FightManager.getInstance().deal().addSummonImmediately(src, skill, pos, new Position(0, 0));
            } else {
                log.error(String.format("有无法解析的action类型:%d", actiontype));
            }
        }
    }

    /**
     * 技能配置检查
     *
     * @param skill
     */
    private void skillConfigCheck(Fighter fighter, Skill skill, List<Fighter> beAttacks, FightMessage.ReqUseSkill messInfo) {
        Player player = null;
        //是否是宠物手动技能
        boolean isPetSkill = false;
        if (fighter instanceof Player) {
            player = (Player) fighter;
            isPetSkill = checkPetSkill(player, skill.getSkillId());
        }
        //获取技能等级表
        Cfg_Skill_Bean skillBean = CfgManager.getCfg_Skill_Container().getValueByKey(skill.getSkillId());
        if (skillBean == null) {
            log.error("无技能配置skillId=" + skill.getSkillId());
            return;
        }
        //是否主动技能
        if (skillBean.getType() != SkillDefine.SkillType_Active) {
            log.error("技能类型错误skillId=" + skill.getSkillId());
            return;
        }
        //获取技能效果配置
        SkillVisual sv = SkillEventContainer.getInstance().getVisuals().get(skillBean.getVisualDef());
        if (sv == null) {
            log.error("技能skillcfg.xml缺失 skillId=" + skill.getSkillId());
            return;
        }
        int cd = 0;
        if (fighter instanceof Player) {
            // 检测CD
            if (Manager.cooldownManager.isCooldowning(player, CooldownTypes.SKILL, String.valueOf(skill.getSkillId()), 5)) {
                long cd_skill = Manager.cooldownManager.getCooldownTime(player, CooldownTypes.SKILL, String.valueOf(skill.getSkillId()));
                log.error("技能冷却中 skillId=" + skill.getSkillId() + " cd=" + cd_skill + " player" + player.nameIdString());
                sendUseSkillError(player, skill);
                return;
            }
            // 检测公共CD
            if (Manager.cooldownManager.isCooldowning(player, CooldownTypes.SKILL_PUBLIC, String.valueOf(skill.getSkillId()), 5)) {
                long pcd = Manager.cooldownManager.getCooldownTime(player, CooldownTypes.SKILL_PUBLIC, String.valueOf(skill.getSkillId()));
                log.error("公共冷却中 skillId=" + skill.getSkillId() + " public=" + pcd + " player" + player.nameIdString());
                sendUseSkillError(player, skill);
                return;
            }
            //检查宠物手动技能公共CD
            if (isPetSkill && Manager.cooldownManager.isCooldowning(player, CooldownTypes.Player_Pet_CD_ManualSkill, null, 5)) {
                long pcd = Manager.cooldownManager.getCooldownTime(player, CooldownTypes.Player_Pet_CD_ManualSkill, null);
                log.error("宠物手动技能冷却中 skillId=" + skill.getSkillId() + " public=" + pcd + " player" + player.nameIdString());
                sendUseSkillError(player, skill);
                return;
            }
            cd = getCD(skillBean, sv);
        } else if (fighter instanceof HuaxinEntity) {
            // 检测CD
            MapObject map = Manager.mapManager.getMap(fighter.gainMapId());
            player = (Player) MapUtils.getOwnFighter(map, fighter);
            if (Manager.cooldownManager.isCooldowning(player, CooldownTypes.Player_FlySowrd_CD_Skill, String.valueOf(skill.getSkillId()), 100)) {
                long cd_skill = Manager.cooldownManager.getCooldownTime(player, CooldownTypes.Player_FlySowrd_CD_Skill, String.valueOf(skill.getSkillId()));
                log.error("飞剑技能冷却中 skillId=" + skill.getSkillId() + " cd=" + cd_skill + "player:" + player.nameIdString());
//                sendUseSkillError(player, skill);
                return;
            }
            cd = getCD(skillBean, sv);
        } else {
            //在延长100毫秒
            cd = getCD(skillBean, sv) + 100;
        }

        if (fighter instanceof Pet) {
            Manager.cooldownManager.addCooldown(fighter, CooldownTypes.Player_Pet_Skill, String.valueOf(skill.getSkillId()), cd);
            Manager.cooldownManager.addCooldown(fighter, CooldownTypes.Player_Pet_CD_Skill, null, sv.getCd());
        } else if (fighter instanceof HuaxinEntity) {
            Manager.cooldownManager.addCooldown(player, CooldownTypes.Player_FlySowrd_CD_Skill, String.valueOf(skill.getSkillId()), cd);
//            Manager.cooldownManager.addCooldown(fighter, CooldownTypes.Player_Fabao_CD_Skill, null, sv.getCd());
        } else if (fighter instanceof Monster || fighter instanceof Robot) {
            Manager.cooldownManager.addCooldown(fighter, CooldownTypes.MonsterSkill, String.valueOf(skill.getSkillId()), cd);
            Manager.cooldownManager.addCooldown(fighter, CooldownTypes.MonsterAttackCD, null, sv.getCd());
        } else if (fighter instanceof Player) {
            if (isPetSkill) {
                Manager.cooldownManager.addCooldown(player, CooldownTypes.Player_Pet_CD_ManualSkill, null, cd);
            }
            Manager.cooldownManager.addCooldown(player, CooldownTypes.SKILL, String.valueOf(skill.getSkillId()), cd);
            Manager.cooldownManager.addCooldown(player, CooldownTypes.SKILL_PUBLIC, String.valueOf(skill.getSkillId()), sv.getCd());
        }
        if (!(fighter instanceof Player)) {
            if (!(fighter instanceof HuaxinEntity)) {
                if (beAttacks.isEmpty() || beAttacks.size() == 0) {
                    return;
                }
            }
            if (fighter instanceof Robot) {
                Robot robot = (Robot) fighter;
                robot.setInBattle(true);
                robot.setLastFight(TimeUtils.Time());
            }
            skill.setSerial(skill.getSerial() + 1);
            ResUseSkill.Builder msg = ResUseSkill.newBuilder();
            SkillBaseInfo.Builder info = null;
            if (beAttacks.isEmpty() || beAttacks.size() == 0) {
                info = messInfo.getInfo().toBuilder();
            } else {
                info = Manager.skillManager.buildSkillBaseInfo(fighter, skill.getSkillId(), skill.getSerial(), beAttacks.get(0));
                msg.setInfo(info);
            }
            msg.setInfo(info);
            MessageUtils.send_to_roundPlayer(fighter, ResUseSkill.MsgID.eMsgID_VALUE, msg.build().toByteArray(), true);
            pushEvent(fighter, skill, sv, new Position(info.getDirX(), info.getDirY()), beAttacks, 0, 0, false);

        } else {
            BehaviorManager.CancelBehaviorByType(player, BehaviorType.AttackMove);
            if (!player.isInBattle()) {
                player.setInBattle(true);
            }
            //玩家每次使用技能都要设置一次最后战斗时间
            player.setLastFight(TimeUtils.Time());
            //使用技能处理
            ResUseSkill.Builder msg = ResUseSkill.newBuilder();
            msg.setInfo(messInfo.getInfo());
            MessageUtils.send_to_roundPlayer(player, ResUseSkill.MsgID.eMsgID_VALUE, msg.build().toByteArray(), true);
            Position dirPos = new Position(messInfo.getInfo().getDirX(), messInfo.getInfo().getDirY());
            pushEvent(player, skill, sv, dirPos, null, (int) messInfo.getUsePosX(), (int) messInfo.getUsePosY(), false);
        }
        if (sv.getEventList().size() < 1) {
            return;
        }
    }

    /**
     * 检查是否是出战宠物的手动技能
     */
    private boolean checkPetSkill(Player player, int skillId) {
        Pet pet = Manager.petManager.getBattlePet(player);
        if (pet == null) {
            return false;
        }
        if (skillId == pet.getManualSkill()) {
            return true;
        }
        return false;
    }

    /**
     * 怪物 宠物 机器人messInfo参数为null 玩家攻击 skill beAttacks参数null
     *
     * @param attacker
     * @param skill
     * @param beAttacks
     * @param messInfo
     */
    @Override
    public void attack(Fighter attacker, Skill skill, List<Fighter> beAttacks, FightMessage.ReqUseSkill messInfo) {
        MapObject map = Manager.mapManager.getMap(attacker.gainMapId());
        if (map == null) {
            log.error("map  == null  attacker.getType  " + attacker.getType());
            return;
        }

        if (beAttacks == null) {
            beAttacks = new ArrayList<>();
        }

        if (attacker instanceof Pet) {
            Pet pet = (Pet) attacker;
            Fighter owner = MapUtils.getFighter(map, pet.getOwnerId());
            if (owner == null) {
                Manager.mapManager.manager().onQuitMap(map, pet, true);
                return;
            }
            Fighter defer = MapUtils.getFighter(map, messInfo.getCurTargetId());
            if (defer == null) {
                defer = MapUtils.getFighter(map, ((Player) owner).gainCurAttackTargetId());
            }
            if (defer != null) {
                beAttacks.add(defer);
            }

            skill = getFightSkill(pet, map, messInfo.getInfo().getSkillID());
            if (skill == null) {
                log.error("宠物无这个技能，skillId = " + messInfo.getInfo().getSkillID() + "，player = " + owner.nameIdString());
                return;
            }
            skill.setSerial(messInfo.getInfo().getSerial());
        } else if (attacker instanceof HuaxinEntity) {
            HuaxinEntity huaxin = (HuaxinEntity) attacker;
            Fighter owner = MapUtils.getFighter(map, huaxin.getOwnerId());
            if (owner == null) {
                Manager.mapManager.manager().onQuitMap(map, huaxin, true);
                return;
            }
            Fighter defer = MapUtils.getFighter(map, messInfo.getCurTargetId());
            if (defer == null) {
                defer = MapUtils.getFighter(map, ((Player) owner).gainCurAttackTargetId());
            }
            if (defer != null) {
//                if (defer instanceof Player )
//                    return;
                if (!(defer instanceof Robot))
                    beAttacks.add(defer);
            }

            skill = getFightSkill(huaxin, map, messInfo.getInfo().getSkillID());
            if (skill == null) {
                log.error("飞剑无这个技能，skillId = " + messInfo.getInfo().getSkillID() + "，player = " + owner.nameIdString());
                return;
            }
            skill.setSerial(messInfo.getInfo().getSerial());
        } else if (attacker instanceof Monster || attacker instanceof Robot) {
            if (!checkPlayerState(attacker, false, null)) {
                return;
            }
        } else if (attacker instanceof Player) {
            Player player = (Player) attacker;
            player.setBrithProtect(0);
            FightMessage.SkillBaseInfo mess = messInfo.getInfo();
            if (mess.getUserID() != player.getId()) {
                return;
            }
            if (!checkPlayerState(attacker, true, mess)) {
                return;
            }
            if (player.isDie()) {
                return;
            }
            skill = getFightSkill(player, map, mess.getSkillID());
            //是否变身技能
            if (skill == null) {
                skill = getBianshenSkill(player, mess.getSkillID());
            }
            if (skill == null) {
                log.error("玩家无这个技能skillId=" + mess.getSkillID() + "player" + player.nameIdString());
                return;
            }
            skill.setSerial(messInfo.getInfo().getSerial());
        }
        skillConfigCheck(attacker, skill, beAttacks, messInfo);
    }

    private boolean checkPlayerState(Fighter attacker, boolean isNotify, FightMessage.SkillBaseInfo mess) {
        Player player = null;
        if (attacker instanceof Player) {
            player = (Player) attacker;
//            BehaviorManager.CancelBehaviorByType(player, BehaviorType.Move);
//            BehaviorManager.CancelBehaviorByType(player, BehaviorType.DirMove);
        }
        //去掉隐身BUFF
        Manager.buffManager.deal().removeBuffInvisible((Entity) attacker);
        //攻击者处于定身状态，则不攻击
        if (Manager.buffManager.deal().haveDing(attacker)) {
            log.error(attacker.getName() + "(" + attacker.getId() + ") 正在定身中， 不可攻击！");
            if (player != null) {
                Manager.mapManager.synStopMove(player, true);
                log.error(player.nameIdString() + " 玩家正在定身中");
                ResUseSkillError.Builder msg = ResUseSkillError.newBuilder();
                msg.setSkillId(mess.getSkillID());
                msg.setSerial(mess.getSerial());
                MessageUtils.send_to_player(player, ResUseSkillError.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            }
            return false;
        }
        //被攻击者处于眩晕状态，则不攻击
        if (Manager.buffManager.deal().haveDizziness(attacker)) {
            log.error(attacker.getName() + "(" + attacker.getId() + ") 正在眩晕中， 不可攻击！");
            if (player != null) {
                Manager.mapManager.synStopMove(player, true);
                ResUseSkillError.Builder msg = ResUseSkillError.newBuilder();
                msg.setSkillId(mess.getSkillID());
                msg.setSerial(mess.getSerial());
                MessageUtils.send_to_player(player, ResUseSkillError.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            }
            return false;
        }
        return true;
    }

    private void calcCritStateTrigger(Cfg_Skill_Bean skillBean, MapObject mapObject, Fighter attacker, Fighter defer, BaseIntAttribute attackStateAtt, BaseIntAttribute deferStateAtt, int type, boolean copy) {
        switch (type) {
            case CritType_Pursue:
                Manager.fightManager.trigger().trigger(mapObject, defer, attacker, deferStateAtt, attackStateAtt, SkillDefine.BE_PURSUE, copy);
                Manager.fightManager.trigger().trigger(mapObject, attacker, defer, attackStateAtt, deferStateAtt, SkillDefine.PURSUE, copy);
                Manager.fightManager.trigger().activeTrigger(skillBean, mapObject, attacker, defer, attackStateAtt, deferStateAtt, SkillDefine.PURSUE, copy);


                break;
            case CritType_DHit:
                Manager.fightManager.trigger().trigger(mapObject, defer, attacker, deferStateAtt, attackStateAtt, SkillDefine.BE_DOUBLE, copy);
                Manager.fightManager.trigger().trigger(mapObject, attacker, defer, attackStateAtt, deferStateAtt, SkillDefine.DOUBLE, copy);
                Manager.fightManager.trigger().activeTrigger(skillBean, mapObject, attacker, defer, attackStateAtt, deferStateAtt, SkillDefine.DOUBLE, copy);

                break;
            case CritType_HitDef:
                Manager.fightManager.trigger().trigger(mapObject, defer, attacker, deferStateAtt, attackStateAtt, SkillDefine.BE_CRITICAL, copy);
                Manager.fightManager.trigger().trigger(mapObject, attacker, defer, attackStateAtt, deferStateAtt, SkillDefine.CRITICAL_HITS, copy);
                Manager.fightManager.trigger().activeTrigger(skillBean, mapObject, attacker, defer, attackStateAtt, deferStateAtt, SkillDefine.CRITICAL_HITS, copy);

                break;
            case CritType_Crit:
                Manager.fightManager.trigger().trigger(mapObject, defer, attacker, deferStateAtt, attackStateAtt, SkillDefine.BE_CRIT, copy);
                Manager.fightManager.trigger().trigger(mapObject, attacker, defer, attackStateAtt, deferStateAtt, SkillDefine.CRIT, copy);
                Manager.fightManager.trigger().activeTrigger(skillBean, mapObject, attacker, defer, attackStateAtt, deferStateAtt, SkillDefine.CRIT, copy);
        }
    }

    //事件处理器
    private boolean calcEvent(Fighter attacker, Fighter defer, SkillEvent event, int delay, float x, float y, long start, int effect) {
        if (!(defer instanceof Entity)) {
            return false;
        }
        delay = (int) (delay * 0.7);
        Entity eDefer = (Entity) defer;

        if (FightEffect.AttackHit.compareTo(effect)) {
            AttackMoveBehavior amb = new AttackMoveBehavior(eDefer);
            amb.setStart(start + delay);
            amb.setSpeed(event.getSpeed(SkillDefine.SkillAttackMoveType_AttackHit));
            amb.setHitType(SkillDefine.SkillAttackMoveType_AttackHit);
            amb.setEnd(start + delay + event.getRunTime(SkillDefine.SkillAttackMoveType_AttackHit));
            amb.setTarget(new Position(x, y));
            eDefer.getAmbs().add(amb);
            //LOGGER.error("僵直开始时间=" + amb.getStart() + "僵直结束时间=" + amb.getEnd() + "僵直时间=" + (amb.getEnd() - amb.getStart()));
            return true;
        }

        if (FightEffect.AttackFly.compareTo(effect)) {
            AttackMoveBehavior amb = new AttackMoveBehavior(eDefer);
            amb.setStart(start + delay);
            amb.setSpeed(event.getSpeed(SkillDefine.SkillAttackMoveType_Fly));
            amb.setHitType(SkillDefine.SkillAttackMoveType_Fly);
            amb.setEnd(start + delay + event.getRunTime(SkillDefine.SkillAttackMoveType_Fly));
            amb.setTarget(new Position(x, y));
            eDefer.getAmbs().add(amb);
            //LOGGER.error("击飞开始时间=" + amb.getStart() + "击飞结束时间=" + amb.getEnd() + "击飞时间=" + (amb.getEnd() - amb.getStart()));
            return true;
        }

        if (FightEffect.AttackBack.compareTo(effect)) {
            AttackMoveBehavior amb = new AttackMoveBehavior(eDefer);
            amb.setStart(start + delay);
            amb.setSpeed(event.getSpeed(SkillDefine.SkillAttackMoveType_Back));
            amb.setHitType(SkillDefine.SkillAttackMoveType_Back);
            amb.setEnd(start + delay + event.getRunTime(SkillDefine.SkillAttackMoveType_Back));
            amb.setTarget(new Position(x, y));
            eDefer.getAmbs().add(amb);
            //LOGGER.error("击退开始时间=" + amb.getStart() + "击退结束时间=" + amb.getEnd() + "击退时间=" + (amb.getEnd() - amb.getStart()));
            return true;
        }

        if (FightEffect.Catch.compareTo(effect)) {
            AttackMoveBehavior amb = new AttackMoveBehavior(eDefer);
            amb.setStart(start + delay);
            amb.setSpeed(event.getSpeed(SkillDefine.SkillAttackMoveType_Catch));
            amb.setHitType(SkillDefine.SkillAttackMoveType_Catch);
            amb.setEnd(start + delay + event.getRunTime(SkillDefine.SkillAttackMoveType_Catch));
            amb.setTarget(new Position(x, y));
            eDefer.getAmbs().add(amb);
//            log.error("抓取开始时间=" + amb.getStart() + "抓取结束时间=" + amb.getEnd() + "抓取时间=" + (amb.getEnd() - amb.getStart()));
            return true;
        }

        return false;

    }

    //处理技能效果
    private int calcEffect(Fighter attacker, Fighter defer, SkillEvent event, int critType, boolean canPlayerValid, boolean pojia) {
        int effect = 0;
        switch (critType) {
            case CritType_HitDef:
                effect = FightEffect.HitDef.addEffect(effect);
                break;
            case CritType_DHit:
                effect = FightEffect.DHit.addEffect(effect);
                break;
            case CritType_Pursue:
                effect = FightEffect.Pursue.addEffect(effect);
                break;
            case CritType_Crit:
                effect = FightEffect.Crit.addEffect(effect);
                break;
            default:
        }

        //是否对玩家起作用
        if (defer instanceof Player) {
            if (!canPlayerValid) {
                return effect;
            }
        }

        //是否对玩家起作用
        if (defer instanceof Robot) {
            if (!canPlayerValid) {
                return effect;
            }
        }

        //如果是怪物, 如果还有护甲， 则没有其它受击效果
        if (defer instanceof Monster) {
            if (!pojia) {
                return effect;
            }
        }

        if (defer instanceof Monster) {
            Monster monster = (Monster) defer;
            if (monster.getMonsterType() != 1) {
                return effect;
            }
        }

        //免控效果处理
        if (Manager.buffManager.deal().haveMiaokong(defer)) {
            log.error(defer.getName() + "(" + defer.getId() + ") 正在免控中， 不产生击退，击飞效果！");
            return effect;
        }

//        if (FightEnum.SuperArmorCount.compare(defer.getFightState())) {
////            LOGGER.info("霸体状态" + defer);
//            //抓取： 可以替代”击退“”僵直“”击飞“状态，没有无效的目标状态，
//            if (event.getHitType() == SkillDefine.SkillAttackMoveType_Catch) {
//                return FightEffect.Catch.addEffect(effect);
//            }
//            return effect;
//        }
        int curHitType = getCurHitType(defer);

        // 僵直: 只能替代僵直状态，如果目标状态师“击退”和“抓取“就无效，目标状态是击飞，便根据配置的数据进行追击击飞
        if (event.getHitType() == SkillDefine.SkillAttackMoveType_AttackHit) {
            if (curHitType == SkillDefine.SkillAttackMoveType_Fly) {
                return FightEffect.AttackFly.addEffect(effect);
            }
            if (curHitType == SkillDefine.SkillAttackMoveType_AttackHit) {
                return FightEffect.AttackHit.addEffect(effect);
            }
            return effect;
        }

        //击退： 只能替代”击退“和”僵直“状态，如果目标状态是”抓取“就无效，目标状态师击飞，便根据配置的数据进行追击击飞
        if (event.getHitType() == SkillDefine.SkillAttackMoveType_Back) {
            if (curHitType == SkillDefine.SkillAttackMoveType_Catch) {
                return effect;
            }
            if (curHitType == SkillDefine.SkillAttackMoveType_Fly) {
                return FightEffect.AttackFly.addEffect(effect);
            }

            return FightEffect.AttackBack.addEffect(effect);
        }

        // 击飞： 可以替代”击退“”僵直“状态，如果目标状态是”抓取“就无效
        if (event.getHitType() == SkillDefine.SkillAttackMoveType_Fly) {
            if (curHitType == SkillDefine.SkillAttackMoveType_Catch) {
                return effect;
            }
            return FightEffect.AttackFly.addEffect(effect);
        }

        //抓取： 可以替代”击退“”僵直“”击飞“状态，没有无效的目标状态，
        if (event.getHitType() == SkillDefine.SkillAttackMoveType_Catch) {
            return FightEffect.Catch.addEffect(effect);
        }

        return effect;
    }

    //获取正在执行的受击事件
    private int getCurHitType(Fighter fighter) {
        AttackMoveBehavior move = (AttackMoveBehavior) BehaviorManager.GetBehavior((Entity) fighter, BehaviorType.AttackMove);
        if (move == null) {
            return SkillDefine.SkillAttackMoveType_None;
        }
        return move.getHitType();
    }

    //计算精准加成
    private float calcHit(Fighter attacker, Fighter defer) {
        float rate;
        if (attacker.getAttribute().getAdditionValue(AttributeType.ATTR_Hit) == 0) {
            rate = 0F;
        } else {
            rate = attacker.getAttribute().getAdditionValue(AttributeType.ATTR_HitPer) + attacker.getAttribute().getAdditionValue(AttributeType.ATTR_Hit) * 1F
                    / (attacker.getAttribute().getAdditionValue(AttributeType.ATTR_Hit) + defer.getAttribute().getAdditionValue(AttributeType.ATTR_HitBreak))
                    - defer.getAttribute().getAdditionValue(AttributeType.ATTR_HtBreakPer);
        }

        if (rate <= (Global.Damage_Parm1 / 10000F)) {
            rate = Global.Damage_Parm1 / 10000F;
        }

        if (rate >= Global.Damage_Parm2 / 10000F) {
            rate = Global.Damage_Parm2 / 10000F;
        }
        return rate;
    }

    //计算破甲加成
    private float calcDefBreak(Fighter attacker, Fighter defer) {
        float rate;
        if (attacker.getAttribute().getAdditionValue(AttributeType.ATTR_DefBreak) == 0) {
            return 0F;
        } else {
            rate = attacker.getAttribute().getAdditionValue(AttributeType.ATTR_DefBreak) * 1F / (attacker.getAttribute().getAdditionValue(AttributeType.ATTR_DefBreak)
                    + defer.getAttribute().getAdditionValue(AttributeType.ATTR_DefBreak));
        }

        if (rate <= Global.Damage_Parm3 / 10000F) {
            rate = Global.Damage_Parm3 / 10000F;
        }

        if (rate >= Global.Damage_Parm4 / 10000F) {
            rate = Global.Damage_Parm4 / 10000F;
        }
        rate = attacker.getAttribute().getAdditionValue(AttributeType.ATTR_DefBreak) * rate;
        return rate;
    }

    //获取召唤事件
    private List<PlaySkillObjectEvent> getMagicEvent(SkillVisual sv) {
        List<PlaySkillObjectEvent> magics = new ArrayList<>();
        for (SkillEvent event : sv.getEventList()) {
            if (event instanceof PlaySkillObjectEvent) {
                magics.add((PlaySkillObjectEvent) event);
            }
        }
        return magics;
    }

    // TODO: 2019/4/28 只有取一次下级数据
    //获取普通技能事件次数
    private int getPlayEventNum(SkillVisual sv) {
        int num = 0;
        for (SkillEvent event : sv.getEventList()) {
            if (event instanceof PlayHitEvent) {
                num++;
            }

            if (event instanceof PlaySkillObjectEvent) {
                PlaySkillObjectEvent objectEvent = (PlaySkillObjectEvent) event;
                num = num + getPlayEventNum1(SkillEventContainer.getInstance().getVisuals().get(objectEvent.getSkillName()));
            }
        }
        return num;
    }

    private int getPlayEventNum1(SkillVisual sv) {
        int num = 0;
        for (SkillEvent event : sv.getEventList()) {
            if (event instanceof PlayHitEvent) {
                num++;
            }
        }
        return num;
    }

    private int checkTarget(Player player, Fighter fighter, SkillVisual sv, boolean checkLen) {
        //TODO 检测能否攻击
        if (fighter == null) {
            return CheckType_None;
        }
        //检测攻击距离 5%的误差
        if (checkLen) {
            float dis = Utils.getDistance(player.gainCurPos(), fighter.gainCurPos());
            if (dis > (sv.getAttackDis() * 1.5f) + player.getRadius() + fighter.getRadius()) {
                return CheckType_Miss;
            }
        }

        if (!isCanPk(player, fighter)) {
            return CheckType_None;
        }

        return CheckType_Succ;
    }

    private int checkTarget(Monster monster, Fighter fighter, SkillVisual sv) {
        //TODO 检测能否攻击
        if (fighter == null) {
            return CheckType_None;
        }

        return CheckType_Succ;
    }

    private int checkTarget(Pet pet, Fighter fighter, SkillVisual sv) {
        //TODO 检测能否攻击
        if (fighter == null) {
            return CheckType_None;
        }

        return CheckType_Succ;
    }

    private int checkTarget(Robot robot, Fighter fighter, SkillVisual sv) {
        //TODO 检测能否攻击
        if (robot == null) {
            return CheckType_None;
        }

        return CheckType_Succ;
    }

    private final int CheckType_Succ = 0; //可攻击
    private final int CheckType_None = -1;   // 未知过滤
    private final int CheckType_Invincible = -2; //无敌
    private final int CheckType_Miss = -3;   //躲避

    private int checkTarget(Fighter attacker, Fighter fighter, SkillVisual sv, boolean checkLen) {
        if (fighter == null) {
            return CheckType_None;
        }
        //如果处于定身状态
        if (Manager.buffManager.deal().haveDing(fighter)) {
            log.info(fighter.getName() + "(" + fighter.getId() + ") 正处理定身中， 不能被攻击！");
            return CheckType_None;
        }
        if (EntityState.Dead.compare(fighter.getState())) {
            return CheckType_None;
        }
        if (EntityState.RunBack.compare(fighter.getState())) {
            return CheckType_Invincible;
        }

        //有霸体状态时，则为弹出MISS
        if (FightEnum.SuperArmorCount.compare(fighter.getFightState())) {
            log.info(fighter.getName() + " (" + fighter.getId() + ") 正处于霸体状态中！");
            return CheckType_None;
        }
        //有保护时弹出MISS
        if (TimeUtils.Time() < fighter.getBrithProtect()) {
            return CheckType_Miss;
        }

        if (attacker instanceof Player) {
            return checkTarget((Player) attacker, fighter, sv, checkLen);
        }
        if (attacker instanceof Monster) {
            return checkTarget((Monster) attacker, fighter, sv);
        }
        if (attacker instanceof Pet) {
            return checkTarget((Pet) attacker, fighter, sv);
        }

        if (attacker instanceof Robot) {
            return checkTarget((Robot) attacker, fighter, sv);
        }

        return CheckType_None;
    }

    //获取暴击类型
    private int calcCrit(MapObject map, Fighter attacker, Fighter defer, BaseIntAttribute attackStateAtt, BaseIntAttribute deferStateAtt) {
        if (attacker instanceof SkillMagic) {
            attacker = MapUtils.getFighter(map, ((SkillMagic) attacker).getOwnerId());
        }

        if (attacker instanceof HuaxinEntity) {
            attacker = MapUtils.getFighter(map, ((HuaxinEntity) attacker).getOwnerId());
        }

        if (attacker instanceof Pet) {
            attacker = MapUtils.getFighter(map, ((Pet) attacker).getOwnerId());
        }
        float deferAtkBreakPer = defer.getAttribute().getAdditionValue(AttributeType.ATTR_AtkBreakPer) + deferStateAtt.getAdditionValue(AttributeType.ATTR_AtkBreakPer) / 10000F;
        float attackerAtkPer = attacker.getAttribute().getAdditionValue(AttributeType.ATTR_AtkPer) + attackStateAtt.getAdditionValue(AttributeType.ATTR_AtkPer) / 10000F;

        if (deferAtkBreakPer >= (Global.Damage_Parm14 / 10000F)) {
            deferAtkBreakPer = Global.Damage_Parm14 / 10000F;
        }

        if (deferAtkBreakPer <= (Global.Damage_Parm13 / 10000F)) {
            deferAtkBreakPer = Global.Damage_Parm13 / 10000F;
        }

        if (attackerAtkPer >= (Global.Damage_Parm12 / 10000F)) {
            attackerAtkPer = Global.Damage_Parm12 / 10000F;
        }

        if (attackerAtkPer <= (Global.Damage_Parm11 / 10000F)) {
            attackerAtkPer = Global.Damage_Parm11 / 10000F;
        }

        //是否追击
        float res = (attacker.getAttribute().getAdditionValue(AttributeType.ATTR_PursueOdds) + attackStateAtt.getAdditionValue(AttributeType.ATTR_PursueOdds)) / 10000F
                * (1 - deferAtkBreakPer)
                - (defer.getAttribute().getAdditionValue(AttributeType.ATTR_SeeResilienceOdds) + deferStateAtt.getAdditionValue(AttributeType.ATTR_SeeResilienceOdds)) / 10000F
                * (1 - attackerAtkPer);

        if (res != 0) {
            if (res > (Global.Damage_Parm9 / 10000F)) {
                res = Global.Damage_Parm9 / 10000F;
            }

            if (RandomUtils.randomFloatValue(0f, 1f) < res) {
                return CritType_Pursue;
            }
        }

        //是否连击
        res = (attacker.getAttribute().getAdditionValue(AttributeType.ATTR_DHitOdds) + attackStateAtt.getAdditionValue(AttributeType.ATTR_DHitOdds)) / 10000F
                * (1 - deferAtkBreakPer)
                - (defer.getAttribute().getAdditionValue(AttributeType.ATTR_GuardOdds) + deferStateAtt.getAdditionValue(AttributeType.ATTR_GuardOdds)) / 10000F
                * (1 - attackerAtkPer);

        if (res != 0) {
            if (res > (Global.Damage_Parm7 / 10000F)) {
                res = Global.Damage_Parm7 / 10000F;
            }

            if (RandomUtils.randomFloatValue(0f, 1f) < res) {
                return CritType_DHit;
            }
        }


        //是否会心
        res = (attacker.getAttribute().getAdditionValue(AttributeType.ATTR_HitOdds) + attackStateAtt.getAdditionValue(AttributeType.ATTR_HitOdds)) / 10000F
                * (1 - deferAtkBreakPer)
                - (defer.getAttribute().getAdditionValue(AttributeType.ATTR_HitDefOdds) + deferStateAtt.getAdditionValue(AttributeType.ATTR_HitDefOdds)) / 10000F
                * (1 - attackerAtkPer);

        if (res != 0) {
            if (res > (Global.Damage_Parm5 / 10000F)) {
                res = Global.Damage_Parm5 / 10000F;
            }

            if (RandomUtils.randomFloatValue(0f, 1f) < res) {
                return CritType_HitDef;
            }
        }

        //是否暴击
        res = (attacker.getAttribute().getAdditionValue(AttributeType.ATTR_CriticalPer) + attackStateAtt.getAdditionValue(AttributeType.ATTR_CriticalPer)) / 10000F
                - (defer.getAttribute().getAdditionValue(AttributeType.ATTR_CriticalBreakPer) + deferStateAtt.getAdditionValue(AttributeType.ATTR_CriticalBreakPer)) / 10000F;

        if ((attacker.getAttribute().getAdditionValue(AttributeType.ATTR_Critical) + attackStateAtt.getAdditionValue(AttributeType.ATTR_Critical)) != 0) {
            res += (attacker.getAttribute().getAdditionValue(AttributeType.ATTR_Critical) + attackStateAtt.getAdditionValue(AttributeType.ATTR_Critical)) * 1F /
                    (attacker.getAttribute().getAdditionValue(AttributeType.ATTR_Critical) + attackStateAtt.getAdditionValue(AttributeType.ATTR_Critical)
                            + defer.getAttribute().getAdditionValue(AttributeType.ATTR_Resilience) + deferStateAtt.getAdditionValue(AttributeType.ATTR_Resilience));
        }

        //控制区间

        if (res > (Global.Damage_CritMax / 10000f)) {
            res = Global.Damage_CritMax / 10000f;
        }

        if (res < (Global.Damage_CritMin / 10000f)) {
            res = Global.Damage_CritMin / 10000f;
        }

        if (RandomUtils.randomFloatValue(0f, 1f) < res) {
            return CritType_Crit;
        }

        return CritType_None;
    }

    //获取幸运随机
    private float calcLuckRate(Fighter attacker) {
        float rd = Global.Damage_Min1 / 10000F + Global.Damage_Min3 / 10000F * (attacker.getAttribute().getAdditionValue(AttributeType.ATTR_Luck) / Global.Damage_Min2);
        if (rd > Global.Damage_Max / 10000F) {
            return Global.Damage_Max / 10000F;
        }
        return RandomUtils.randomFloatValue(rd, Global.Damage_Max / 10000F);
    }

    private long calcDamage(HitEffectInfo.Builder builder, MapObject map, Fighter fighter, Fighter defer, Cfg_Skill_Bean skillBean, int skillLevel, int rateType, int targetCount, BaseIntAttribute asa, BaseIntAttribute dsa, SkillEvent se) {
        if (fighter == null) {
            return 0;
        }

        SkillVisual sv = getSkillVisual(skillBean.getId());
        if (sv == null) {
            return 0;
        }

        int hitNum = getPlayEventNum(sv);

        if (hitNum == 0) {
            return 0;
        }

        Fighter attacker = fighter;
        if (fighter instanceof SkillMagic) {
            attacker = MapUtils.getFighter(map, ((SkillMagic) fighter).getOwnerId());
        }

        if (attacker instanceof HuaxinEntity) {
            attacker = MapUtils.getFighter(map, ((HuaxinEntity) attacker).getOwnerId());
        }

        if (attacker instanceof Pet) {
            attacker = MapUtils.getFighter(map, ((Pet) attacker).getOwnerId());
        }

        boolean isPlayer = false;
        if (attacker instanceof Player) {
            isPlayer = true;
        }

        if (defer instanceof Pet) {
            return 0;
        }

        if (defer.getFixDecHp() > 0) {
            return defer.getFixDecHp();
        }

        //精准加成
        float hit = calcHit(attacker, defer);

        if (isPlayer) {
            log.debug(attacker.getName() + " 精准加成：" + hit);
        }

        //破甲加成
        float defBreak = calcDefBreak(attacker, defer);

        if (isPlayer) {
            log.debug(attacker.getName() + " 破甲加成：" + defBreak);
        }

        long att = attacker.getAttribute().getAdditionValue(AttributeType.ATTR_Atk) + asa.getAdditionValue(AttributeType.ATTR_Atk);

        if (att <= 0) {
            log.error(String.format("技能有问题了,att 算出来是0!!! 导致没有伤害!!! skillid:%d attacker:%s ", skillBean.getId(), attacker.nameIdString()));
        }
        if (isPlayer) {
            log.debug(attacker.getName() + " 攻击具体：" + att);
        }

        long dtt = defer.getAttribute().getAdditionValue(AttributeType.ATTR_Def) + dsa.getAdditionValue(AttributeType.ATTR_Def);

        double damage = (att * hit + defBreak) * (att * hit + defBreak) / (att + defBreak + dtt * Global.Damage_Defense / 10000F);

        //伤害加成or减少
        float fmrate = 1 + (attacker.getAttribute().getAdditionValue(AttributeType.ATTR_AddHarm) + asa.getAdditionValue(AttributeType.ATTR_AddHarm)) / 10000f - (defer.getAttribute().getAdditionValue(AttributeType.ATTR_DecHarm) + dsa.getAdditionValue(AttributeType.ATTR_DecHarm)) / 10000f;

        damage = damage * fmrate;

        fmrate = skillBean.getDamageOffset() / hitNum * (1f + attacker.getSkilladds().getOrDefault(skillBean.getId(), 0) / 10000f);

        float luckvalue = calcLuckRate(attacker);

        //伤害随机
        damage = damage * luckvalue * fmrate / 10000f;
        if (isPlayer) {
//            log.info(attacker.getName() + " 被动技能加成：" + fmrate + ",幸运值：" + luckvalue + ", 结果=" + damage + "hitNum:" + hitNum + "skill:" + skillBean.getId());
        }

        switch (rateType) {
            case CritType_Pursue: {
                damage = (damage * (Global.Damage_Parm10 / 10000f));
                if (isPlayer) {
                    log.debug(attacker.getName() + " 追击：" + damage + ", global：" + Global.Damage_Parm10);
                }
            }
            break;
            case CritType_DHit: {
                damage = (damage * (Global.Damage_Parm8 / 10000f));
                if (isPlayer) {
                    log.debug(attacker.getName() + " 连击：" + damage + ", global：" + Global.Damage_Parm8);
                }
            }
            break;
            case CritType_HitDef: {
                damage = (damage * (Global.Damage_Parm6 / 10000f));
                if (isPlayer) {
                    log.debug(attacker.getName() + " 会心：" + damage + ", global：" + Global.Damage_Parm6);
                }
            }
            break;
            case CritType_Crit: {
                damage = (damage * (Global.Damage_Parm15 / 10000f));
                if (isPlayer) {
                    log.debug(attacker.getName() + " 暴击：" + damage + ", global：" + Global.Damage_Parm15);
                }
            }
            break;
            default:
                break;
        }

        int skillWei = skillLevel % 100;
        if (skillWei == 0) {
            skillWei = 10;
        }

        //无视防御
        damage += attacker.getAttribute().getAdditionValue(AttributeType.ATTR_DisDef) + asa.getAdditionValue(AttributeType.ATTR_DisDef) + skillBean.getDamageBase() / hitNum + skillBean.getDamageUp() / hitNum * skillWei;
        if (isPlayer) {
            log.debug(attacker.getName() + " 无视距离深度：" + damage);
        }
        //伤害分担
        if (skillBean.getShare() < 0) {
            return (long) (damage / targetCount);
        } else if (skillBean.getShare() > 0) {
            return (long) (damage * targetCount);
        }


//        Fighter a = fighter;
//        if (fighter instanceof SkillMagic) {
//            a = MapUtils.getFighter(map, ((SkillMagic) fighter).getOwnerId());
//        }
//
//        if (a instanceof HuaxinEntity) {
//            return (long) damage;
//        }

        //m vs m
        if (attacker instanceof Monster && defer instanceof Monster) {
            return (long) damage;
        }
        //p vs o
        if (attacker instanceof Pet || defer instanceof Pet) {
            return (long) damage;
        }

        boolean ext = (attacker instanceof Robot);
        int type = 1;
        if (!ext) {
            ext = (defer instanceof Robot);//机器人也走PK模式
        }
        if (ext) {
            return calcPvr(attacker, defer, damage, asa, dsa);
        }

        ext = (attacker instanceof Monster);
        type = 1;
        if (!ext) {
            type = 2;
            ext = (defer instanceof Monster);//机器人也走PK模式
        }
        //如果攻击者或者防御是怪物
        Cfg_Monster_Bean bean;
        if (ext) {
            if (type == 1) {
                bean = CfgManager.getCfg_Monster_Container().getValueByKey(((Monster) attacker).getModelId());
            } else {
                bean = CfgManager.getCfg_Monster_Container().getValueByKey(((Monster) defer).getModelId());
            }
            return calcPve(attacker, defer, damage, bean, asa, dsa);
        }

        return calcPvp(builder, attacker, defer, damage, asa, dsa, se);

    }

    private long calcPvr(Fighter attacker, Fighter defer, double base, BaseIntAttribute asa, BaseIntAttribute dsa) {
        float rate = 1 + (attacker.getAttribute().getAdditionValue(AttributeType.ATTR_AtkMonsterHarmAdd) + asa.getAdditionValue(AttributeType.ATTR_AtkMonsterHarmAdd)) / 10000f - (defer.getAttribute().getAdditionValue(AttributeType.ATTR_AtkMonsterHarmDec) + dsa.getAdditionValue(AttributeType.ATTR_AtkMonsterHarmDec)) / 10000f;
        float max = Global.Damage_PveMax / 100f;
        float min = Global.Damage_PveMin / 100f;
        rate = rate >= max ? max : (rate < min ? min : rate);

        //增加战力压制功能
        double fight = 0;
        boolean isAtMonster = attacker instanceof Robot;

        if (isAtMonster) {
            fight = ((Robot) attacker).getFightPoint() * 1f / defer.getFightPoint();
            float limitMax = Global.Damage_MonsterPveNum1 / 100f;
            max = Global.Damage_MonsterPveNum2 / 100f;
            float limitMin = Global.Damage_MonsterPveNum3 / 100f;
            min = Global.Damage_MonsterPveNum4 / 100f;
            fight = fight >= limitMax ? max : (fight < limitMin ? min : fight);
            return (long) (base * rate * fight);
        }


        fight = attacker.getFightPoint() * 1f / defer.getFightPoint();
        float limitMax = Global.Damage_Robot_PveNum9 / 100f;
        max = Global.Damage_Robot_PveNum10 / 100f;
        min = Global.Damage_Robot_PveNum11 / 100f;
        fight = fight >= limitMax ? max : (fight < min ? min : fight);
        //LOGGER.info(attacker.getName() + " 最终伤害：" + (base * rate * fight) + ", 精准与坚固比：" + rate);
        return (long) (base * rate * fight);

    }

    private long calcPve(Fighter attacker, Fighter defer, double base, Cfg_Monster_Bean bean, BaseIntAttribute asa, BaseIntAttribute dsa) {
        float rate = 1 + (attacker.getAttribute().getAdditionValue(AttributeType.ATTR_AtkMonsterHarmAdd) + asa.getAdditionValue(AttributeType.ATTR_AtkMonsterHarmAdd)) / 10000f - (defer.getAttribute().getAdditionValue(AttributeType.ATTR_AtkMonsterHarmDec) + dsa.getAdditionValue(AttributeType.ATTR_AtkMonsterHarmDec)) / 10000f;
        float max = Global.Damage_PveMax / 100f;
        float min = Global.Damage_PveMin / 100f;
        rate = rate >= max ? max : (rate < min ? min : rate);

        //增加战力压制功能
        double fight;
        boolean isAtMonster = attacker instanceof Monster;
        //攻击是怪物
        if (isAtMonster) {
            int hpPercent = (int) (defer.getCurHp() * 10000.0 / defer.getAttribute().MaxHP());
            for (int i = 0; i < Global.Monster_Att_player_Hp_Lock.size(); i++) {
                if (bean.getId() == Global.Monster_Att_player_Hp_Lock.get(i).get(0)
                        && hpPercent < Global.Monster_Att_player_Hp_Lock.get(i).get(1)) {
                    return 0;
                }
            }

            if (((Monster) attacker).getScore() < 1) {
                return (long) (base * rate);
            }
            fight = ((Monster) attacker).getScore() * 1f / defer.getFightPoint();
            float limitMax = Global.Damage_MonsterPveNum1 / 100f;
            max = Global.Damage_MonsterPveNum2 / 100f;
            float limitMin = Global.Damage_MonsterPveNum3 / 100f;
            min = Global.Damage_MonsterPveNum4 / 100f;
            fight = fight >= limitMax ? max : (fight < limitMin ? min : fight);
            return (long) (base * rate * fight);
        }

        if (((Monster) defer).getScore() < 1) {
            return (long) (base * rate);
        }

        //被攻击者是一血怪的时候，使用特殊压制
        fight = attacker.getFightPoint() * 1f / ((Monster) defer).getScore();
        if (bean.getBeHurtType() != 0) {
            max = Global.Damage_FightFixpressMax / 10000f;
            min = Global.Damage_FightFixpressMin / 10000f;
            fight = fight >= max ? max : (fight < min ? min : fight);
//            log.error("被攻击者是一血怪，怪物 = " + bean.getId() + "，玩家打出的伤害 = " + base + "，基础比例 = " + rate + "，" + "，压制系数 = " + fight);
            return (long) (base * rate * fight);
        }

        float limitMax = Global.Damage_PlayerPveNum1 / 100f;
        max = Global.Damage_PlayerPveNum2 / 100f;
        min = Global.Damage_PlayerPveNum3 / 100f;
        fight = fight >= limitMax ? max : min;
        //LOGGER.info(attacker.getName() + " 最终伤害：" + (base * rate * fight) + ", 精准与坚固比：" + rate);
        return (long) (base * rate * fight);
    }

    private long calcPvp(HitEffectInfo.Builder builder, Fighter attacker, Fighter defer, double base, BaseIntAttribute asa, BaseIntAttribute dsa, SkillEvent se) {

        float rate = 1 + (attacker.getAttribute().getAdditionValue(AttributeType.ATTR_AtkRoleHarmAdd) + asa.getAdditionValue(AttributeType.ATTR_AtkRoleHarmAdd)) / 10000f - (defer.getAttribute().getAdditionValue(AttributeType.ATTR_AtkRoleHarmDec) + dsa.getAdditionValue(AttributeType.ATTR_AtkRoleHarmDec)) / 10000f;
        float max = Global.Damage_PvpMax / 100f;
        float min = Global.Damage_PvpMin / 100f;
        rate = rate >= max ? max : (rate < min ? min : rate);


        float fight = attacker.getFightPoint() * 1f / defer.getFightPoint();
        float limitMax = Global.Damage_FightRepressMax1 / 100f;
        max = Global.Damage_FightRepressMax2 / 100f;
        float limitMin = Global.Damage_FightRepressMin1 / 100f;
        min = Global.Damage_FightRepressMin2 / 100f;
        fight = fight >= limitMax ? max : (fight < limitMin ? min : fight);

        float wakanRate = 0;
        float decWakan = defer.getAttribute().getAdditionValue(AttributeType.ATTR_Wakan_Dec) / 10000F;
        long maxWakan = defer.getAttribute().getAdditionValue(AttributeType.ATTR_Wakan);

        if (maxWakan != 0 && defer.getCurWakan() != 0) {
            wakanRate = defer.getCurWakan() * 1F / maxWakan;
            wakanRate = (Global.Lingli_LingLiJianShang_Max / 10000F - decWakan) * wakanRate + decWakan;
            builder.setDamageWaken((int) (wakanRate * 100));
        }
        if (se.getHitEffect().equals("lingqi") && maxWakan != 0 && defer.getCurWakan() != 0) {
//          wakanRate = wakanRate <= decWakan ? decWakan : wakanRate;
            if (!Manager.cooldownManager.isCooldowning(defer, CooldownTypes.LINGLI_CD, String.valueOf(AttributeType.ATTR_Wakan_Dec), 0)) {
                //灵力改变
                long nowWakan = defer.getCurWakan() - attacker.getAttribute().getAdditionValue(AttributeType.ATTR_Wakan_Atk);
                defer.setCurWakan(nowWakan);
                if (nowWakan <= 0) {
                    ((Entity) defer).setBreakWakanFight(TimeUtils.Time());
                }
                //延迟300，策划方新宇喊写死
                Manager.cooldownManager.addCooldown(defer, CooldownTypes.LINGLI_CD, String.valueOf(AttributeType.ATTR_Wakan_Dec), 300);
            }
        }
        //log.error(attacker.getName() + "(" + attacker.getFightPoint() + ") VS " + defer.getName() + "(" + defer.getFightPoint() + ") 终伤：" + (base * rate * fight) + ",战力比：" + fight + "灵力系数:" + wakanRate);
        return (long) ((base * rate * fight) * (1 - wakanRate));
    }

    //发送死亡消息
    public void sendDead(Fighter attacker, Fighter dead) {

        if (dead instanceof Player) {
            MapUtils.sendDead(attacker, dead);
        } else {
            FightMessage.ResObjDead.Builder msg = FightMessage.ResObjDead.newBuilder();
            if (null == attacker) {
                msg.setTransMark(0);
                msg.setKillerName("");
            } else {
                StringBuilder destStr = new StringBuilder();
                boolean isMark = Utils.getMarkAfterString(attacker.getName(), destStr);
                msg.setTransMark(isMark ? 1 : 0);
                msg.setKillerName(destStr.toString());
            }
            msg.setDeaderId(dead.getId());
            MessageUtils.send_to_roundPlayer(dead, FightMessage.ResObjDead.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    //技能使用失败消息
    private void sendUseSkillError(Player player, Skill skill) {
        ResUseSkillError.Builder msg = ResUseSkillError.newBuilder();
        msg.setSkillId(skill.getSkillId());
        msg.setSerial(skill.getSerial());
        MessageUtils.send_to_player(player, ResUseSkillError.MsgID.eMsgID_VALUE, msg.build().toByteArray());

    }

    @Override
    public void OnReqChangeAttackDir(Player player, FightMessage.ReqChangeAttackDir mess) {
        ResChangeAttackDirRes.Builder msg = ResChangeAttackDirRes.newBuilder();
        msg.setDirX(mess.getDirX());
        msg.setDirY(mess.getDirY());
        msg.setRoleId(player.getId());
        MessageUtils.send_to_roundPlayer(player, ResChangeAttackDirRes.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    public SkillVisual getSkillVisual(int skillId) {
        Cfg_Skill_Bean skillBean = CfgManager.getCfg_Skill_Container().getValueByKey(skillId);
        return SkillEventContainer.getInstance().getVisuals().get(skillBean.getVisualDef());
    }

    @Override
    public boolean isCanPk(Fighter attacker, Fighter defer) {
        if (defer == null) {
            return false;
        }

        if (defer instanceof Pet) {
            return false;
        }

        if (attacker.getId() == defer.getId()) {
            return false;
        }

        MapObject map = Manager.mapManager.getMap(defer.gainMapId());

        Cfg_Mapsetting_Bean bean = CfgManager.getCfg_Mapsetting_Container().getValueByKey(map.getMapModelId());
        if (defer instanceof Monster) {

            if (bean.getScene_came_match_type() == 0) {
                if (attacker.getCamp() == defer.getCamp()) {
                    return false;
                }
            } else {
                int res = attacker.getCamp() & (1 << (defer.getCamp() - 1));
                if (res != 0) {
                    return false;
                }
            }
        }

        if (defer instanceof Player && attacker instanceof Robot) {
            if (((Robot) attacker).getAi() == RobotAi.Help) {
                return false;
            }
        }
        if (defer instanceof Robot && attacker instanceof Player) {
            if (((Robot) defer).getAi() == RobotAi.Help) {
                return false;
            }
        }
        if (defer instanceof Robot && attacker instanceof Robot) {
            if (((Robot) defer).getAi() == RobotAi.Help
                    || ((Robot) attacker).getAi() == RobotAi.Help) {
                return false;
            }
            Robot r1 = (Robot) defer;
            Robot r2 = (Robot) attacker;
            if (r1.getAi() == RobotAi.JJC || r2.getAi() == RobotAi.JJC) {
                if(r1.getCamp() == r2.getCamp()){
                    return false;
                }
            }
        }
        if (defer instanceof Robot && attacker instanceof Pet) {
            if (((Robot) defer).getAi() == RobotAi.Help) {
                return false;
            }
        }
        if (attacker instanceof Monster) {
            if (bean.getScene_came_match_type() == 1) {
                int res = defer.getCamp() & (1 << (attacker.getCamp() - 1));
                if (res != 0) {
                    if (testCheckFigher(attacker)) {
                        log.info("阵营匹配问题 攻击阵营:" + attacker.getCamp() + " 被攻击者阵营: " + defer.getCamp() + ";;;" + attacker.getName() + " 场景阵营匹配类型: " + bean.getScene_came_match_type() + " 堆栈:" + BeanUtil.getStack());
                    }
                    return false;
                }
            } else {
                if (attacker.getCamp() == defer.getCamp()) {
                    if (testCheckFigher(attacker)) {
                        log.info("阵营相同 攻击阵营:" + attacker.getCamp() + " 被攻击者阵营: " + defer.getCamp() + ";;;" + attacker.getName() + " 堆栈:" + BeanUtil.getStack());
                    }
                    return false;
                }
            }
        }
        if (!(attacker instanceof Player)) {
            return true;
        }

        Player aPlayer = (Player) attacker;

        if (!(defer instanceof Player)) {
            return true;
        }

        Player dPlayer = (Player) defer;
        if (map.isSafe()) {
            return false;
        }

        boolean isChangeModelCanPk = checkChangeModelPk(aPlayer, dPlayer);

        //如果不是战斗服， 则检查仇恨列表与， 可攻击及红名
        if (!GameServer.getInstance().IsFightServer()) {
            //队伍中不能相互攻击
            if (aPlayer.getTeamId() != 0 && aPlayer.getTeamId() == dPlayer.getTeamId()) {
                return false;
            }
            //在仇恨列表 可攻击
            if (aPlayer.getPklist().containsKey(dPlayer.getId())) {
                return isChangeModelCanPk;
            }
            //红名可攻击
            if (dPlayer.getCurrencys().get(ItemCoinType.GoodEvil) >= PlayerDefine.PkRedName) {
                return isChangeModelCanPk;
            }
        }

        //和平模式下
        if (aPlayer.getPkState() == PlayerDefine.PkStatePeace) {
            return false;
        }

        //组队模式
        if (aPlayer.getPkState() == PlayerDefine.PkStateTeam) {
            TeamInfo ateam = Manager.teamManager.getTeam(aPlayer.getTeamId());
            if (ateam == null) {
                return isChangeModelCanPk;
            }
            TeamInfo dteam = Manager.teamManager.getTeam(dPlayer.getTeamId());
            if (dteam == null) {
                return isChangeModelCanPk;
            }
            return (ateam.getTeamId() != dteam.getTeamId() && isChangeModelCanPk);

        }

        if (aPlayer.getPkState() == PlayerDefine.PkStateServer) {
            return (aPlayer.getCurServerId() != dPlayer.getCurServerId() && isChangeModelCanPk);
        }

        if (aPlayer.getPkState() == PlayerDefine.PkStateCamp) {
            if (bean.getScene_came_match_type() == 1) {
                //怪物占据低16位 65535
                if ((defer.getCamp() >> 16) == (attacker.getCamp() >> 16)) {
                    return false;
                }
            }
            return (aPlayer.getCamp() != dPlayer.getCamp() && isChangeModelCanPk);
        }

        if (aPlayer.getPkState() == PlayerDefine.PkStateGuild) {
            if (aPlayer.getGuildId() <= 0) {
                return isChangeModelCanPk;
            }
            if (dPlayer.getGuildId() <= 0) {
                return isChangeModelCanPk;
            }
            return (aPlayer.getGuildId() != dPlayer.getGuildId() && isChangeModelCanPk);
        }
        return false;
    }

    private boolean calcBreakArmor(Fighter defer, Cfg_Skill_Bean skillBean, int skillLevel) {

        if (!(defer instanceof Monster)) {
            return true;
        }

        if (((Monster) defer).getArmor() < 1) {
            return true;
        }
        Monster monster = (Monster) defer;
        int armor = monster.getArmor();
        //不能破甲
        if (monster.isArmorTrue()) {
            return false;
        }

        //计算护甲的厚度
        int base = skillBean.getArmordamageBase();
        int level = skillLevel % 10;
        if (level == 0) {
            level = 10;
        }
        int pojiavalue = base + skillBean.getArmordamageUp() * skillLevel * (level - 1);
        if (armor > pojiavalue) {
            monster.setArmor(armor - pojiavalue);
        } else {
            monster.setArmor(0);
        }
        return false;
    }

    /**
     * 获取怪物攻击技能
     *
     * @param fighter 需要查看技能的玩家
     * @return 返回可用技能列表
     */
    @Override
    public Skill getCanUseSkill(Entity fighter) {

        if (fighter.getCurSlowSkill() != null) {

            Skill skill = fighter.getCurSlowSkill();

            if (skill.getSlows().isEmpty()) {
                fighter.setCurSlowSkill(null);
                return null;
            }
            if (skill.getStart() + skill.getSlows().get(0).getFpsTime() > TimeUtils.Time()) {
                return null;
            }
            return skill;
        }

        if (Manager.cooldownManager.isCooldowning(fighter, CooldownTypes.MonsterAttackCD, null)) {
            return null;
        }

        if (fighter.getSkills().isEmpty()) {
            return null;
        }

        if (fighter.getUseSkill() != null) {
            return fighter.getUseSkill();
        }

        Queue<Skill> aipushs = fighter.getAipushLists();
        if (!aipushs.isEmpty()) {
            fighter.setUseSkill(aipushs.poll());
            return fighter.getUseSkill();
        }
        List<Skill> readys = fighter.getReadyLists();
        if (!readys.isEmpty()) {
            fighter.setUseSkill(readys.remove(0));
            return fighter.getUseSkill();
        }
        for (Skill skill : fighter.getSkills().values()) {
            if (Manager.cooldownManager.isCooldowning(fighter, CooldownTypes.MonsterSkill, String.valueOf(skill.getSkillId()))) {
                continue;
            }
            Cfg_Skill_Bean config = CfgManager.getCfg_Skill_Container().getValueByKey(skill.getSkillId());
            if (config == null) {
                continue;
            }
            if (config.getType() != SkillDefine.SkillType_Active) {
                continue;
            }

            if (fighter instanceof Monster) {
                //怪物设置不能使用普攻技能
                if (!((Monster) fighter).isUserNormalSkill()) {
                    if (skill.isIsNormal()) {
                        continue;
                    }
                }
            }

            readys.add(skill);
        }
        if (readys.isEmpty()) {
            return null;
        }

        Skill skill = readys.remove(0);

        fighter.setUseSkill(skill);
        return skill;
    }

    /**
     * 获取查找事件
     *
     * @param sv 效果
     * @return 返回是否有伤害事件
     */
    private SkillEvent getFindEvent(SkillVisual sv) {
        for (SkillEvent event : sv.getEventList()) {
//            if (event instanceof PlaySkillObjectEvent) {
//                continue;
//            }

            if (event instanceof CommonEvent) {
                continue;
            }
            return event;
        }
        return null;
    }

    /**
     * 获取攻击目标列表
     *
     * @param attacker 攻击者
     * @param defer    防御
     * @param skill    技能
     * @param sv       效果列表
     * @return 返回目标列表
     */
    @Override
    public ArrayList<Fighter> getAnemys(Entity attacker, Fighter defer, Skill skill, SkillVisual sv) {
        ArrayList<Fighter> anemys = new ArrayList<>();

        SkillEvent enent = getFindEvent(sv);
        if (enent == null) {
            return anemys;
        }
        if (defer != null) {
            if (ShapeUtils.inAttackArea(enent.getShape(), attacker.gainCurPos(), attacker.getDir(), defer.gainCurPos())) {
                boolean isAdd = true;
                if (EntityState.Dead.compare(defer.getState())) isAdd = false;
                if (isAdd && defer.getCurHp() < 1) isAdd = false;
                if (isAdd && defer instanceof Pet) isAdd = false;
                if (isAdd && !isCanPk(attacker, defer)) isAdd = false;
                if (isAdd) anemys.add(defer);
            }
        }

        for (Hatred hat : attacker.getHatreds()) {
            if (anemys.size() >= sv.getMaxAttack()) {
                return anemys;
            }
            Fighter odefer = hat.getTarget();
            if (odefer == null) {
                continue;
            }
            if (EntityState.Dead.compare(odefer.getState())) {
                continue;
            }

            if (odefer.getCurHp() < 1) {
                continue;
            }
            if (anemys.contains(odefer)) {
                continue;
            }

            if (odefer instanceof Pet) {
                continue;

            }
            if (!isCanPk(attacker, odefer)) {
                continue;
            }
            if (ShapeUtils.inAttackArea(enent.getShape(), attacker.gainCurPos(), attacker.getDir(), hat.getTarget().gainCurPos())) {
                anemys.add(hat.getTarget());
            }
        }

        return anemys;
    }

    @Override
    public ArrayList<Fighter> getEnemysInSkillArea(MapObject map, Fighter attacker, Position point, Position dirs, Skill skill, SkillVisual sv, STCheckerMaker attackchecker) {
        ArrayList<Fighter> enemys = new ArrayList<>();

        SkillEvent event = getFindEvent(sv);
        if (event == null) {
            return enemys;
        }

        Shape skillshape = event.getShape();
        Position dis = ShapeUtils.inAttackDis(skillshape);

        Position areadis = Manager.mapManager.getAreaLen(map, dis);

        enemys = MapUtils.getFighterNear(map, point, (int) Math.ceil(areadis.getX()), (int) Math.ceil(areadis.getY()), MapUtils.FighterFlag.All.v);

//        ArrayList<String> dislist = new ArrayList<>();
        Iterator<Fighter> mIter = enemys.iterator();
        while (mIter.hasNext()) {
            Fighter defer = mIter.next();
            // 不在范围内的先去掉
            if (!ShapeUtils.inAttackArea(event.getShape(), attacker.gainCurPos(), dirs, defer.gainCurPos())) {
                mIter.remove();
                continue;
            }
            ISkillTargetChecker is = (ISkillTargetChecker) Manager.scriptManager.GetScriptClass(attackchecker.checkerScriptID);
            int ret = is.Invoke(attacker, defer, attackchecker);
            if (ret != 0) {
                mIter.remove();
            }
        }

        while (enemys.size() > attackchecker.maxTargetCount - attackchecker.enemys.size()) {
            enemys.remove(enemys.size() - 1);
        }
        return enemys;
    }


    /**
     * 获取变身技能，如果没有，就返回null
     *
     * @param player
     * @return
     */
    @Override
    public Skill getBianshenSkill(Player player, int bianshenSkillId) {
        Cfg_Skill_Bean skillBean = CfgManager.getCfg_Skill_Container().getValueByKey(bianshenSkillId);
        if (skillBean == null) {
            return null;
        }
        for (Buff buff : player.getBuffs()) {
            Cfg_Buff_Bean config = CfgManager.getCfg_Buff_Container().getValueByKey(buff.getBuffId());
            if (config == null) {
                continue;
            }
            if (config.getType() == BuffDefine.Type_Bianshen) {
                if (config.getCondi() == null || config.getCondi().size() < 1) {
                    log.error(TaskHelp.getPlayerInfo(player) + "在使用变身技能时发现配置错误：" + buff.getBuffId());
                    return null;
                }
                ReadArray<Integer> autoIncrementIntArray = config.getCondi().get(0);
                for (int i : autoIncrementIntArray.getValue()) {
                    if (i == bianshenSkillId) {
                        Skill skill = new Skill();
                        skill.setSkillId(bianshenSkillId);
                        skill.setLevel(1);
                        return skill;
                    }
                }
                break;
            }
        }
        return null;
    }

    /**
     * 计算技能事件，用于处理技能延时事件
     *
     * @param fighter 攻击 者
     */
    @Override
    public void onFightSkillEvent(Fighter fighter) {
        if (fighter.willEvents().size() < 1) {
            return;
        }
        long curTime = TimeUtils.Time();

        Iterator<FightSkillEvent> fseIter = fighter.willEvents().iterator();
        while (fseIter.hasNext()) {
            FightSkillEvent fse = fseIter.next();
            if (fse == null) {
                fseIter.remove();
                continue;
            }
            if (fse.getMapId() != fighter.gainMapId()) {
                fseIter.remove();
                continue;
            }

            if (curTime < fse.getStartTime()) {
                continue;
            }
            //执行事件任务
            doSkillEvent(fighter, fse, fse.getSe().getEventID());
            fseIter.remove();
        }
    }

    @Override
    public boolean doSkillLockTrajectoryEndToDelete(Fighter fighter, SkillLockTrajectory st) {
        //地图已经不一致了
        if (fighter.gainMapId() != st.getMapId()) {
            log.info(fighter.nameIdString() + "已经不在地图:" + st.getMapId());
            return true;
        }

        long now = TimeUtils.Time();
        long offset = now - st.getStartTime();

        MapObject map = Manager.mapManager.getMap(fighter.gainMapId());

        Fighter defer = MapUtils.getFighter(map, st.getTargetId());
        if (defer == null) {
            log.info(st.getTargetId() + "玩家已经不在地图上了！");
            return true;
        }
        //检查是否可以产生伤害了
        PlayLockTrajectoryEvent event = st.getEvent();
        float dis = Utils.getDistance(st.getCurPos(), defer.gainCurPos());
        if (dis < (defer.getRadius() + event.getVfxRadius())) {
            Skill skill = getFightSkill((Entity) fighter, map, st.getSkillId());
            List<Fighter> targets = new ArrayList<>(1);
            targets.add(defer);
            doHitAndSend(fighter, event.getUniqueID(), skill, event, now, targets, true, false);
            return true;
        }
        //进行移动
        float moveSpeed = event.getFlySpeed() + event.getFlyAddSpeed() * (offset / 1000.0f);
        float range = moveSpeed * (offset / 1000.0f);
        //如果两点的距离已经超过了， 则他们是相遇了，下一个回合计算伤害
        if (Float.compare(dis, range) < 0) {
            range = dis;
        }
        Position pos = Utils.getDirPos(st.getCurPos(), defer.gainCurPos(), range);
        st.setCurPos(pos);
        return false;
    }

    //变身个状态下 判断是否可以攻击
    private boolean checkChangeModelPk(Fighter attacker, Fighter defer) {

        Cfg_Change_model_Bean change_model_bean = null;
        if (attacker.getChangeModelState() && defer.getChangeModelState()) {
            change_model_bean = CfgManager.getCfg_Change_model_Container().getValueByKey(attacker.getChangeModleID());
            if (change_model_bean == null) {
                log.error("change_model_bean  == null" + attacker.getChangeModleID());
                return true;
            }
            return change_model_bean.getIsAttChange() > 0;
        }
        if (attacker.getChangeModelState() && !defer.getChangeModelState()) {
            change_model_bean = CfgManager.getCfg_Change_model_Container().getValueByKey(attacker.getChangeModleID());
            if (change_model_bean == null) {
                log.error("change_model_bean  == null" + attacker.getChangeModleID());
                return false;
            }
            return change_model_bean.getIsAttRole() > 0;
        }
        if (!attacker.getChangeModelState() && defer.getChangeModelState()) {
            change_model_bean = CfgManager.getCfg_Change_model_Container().getValueByKey(defer.getChangeModleID());
            if (change_model_bean == null) {
                log.error("change_model_bean  == null" + defer.getChangeModleID());
                return false;
            }
            return change_model_bean.getIsRoleAtt() > 0;
        }
        return true;
    }
}
