package common.player;

import com.data.Global;
import com.game.behavior.manager.BehaviorManager;
import com.game.behavior.structs.BehaviorType;
import com.game.behavior.structs.type.AttackMoveBehavior;
import com.game.buff.script.IBuffScript;
import com.game.fight.script.IFightManagerScript;
import com.game.fight.state.FightState;
import com.game.fight.structs.FightEnum;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapUtils;
import com.game.nature.structs.HuaxinEntity;
import com.game.pet.structs.Pet;
import com.game.player.script.IPlayerAi;
import com.game.player.structs.Player;
import com.game.robot.struct.Robot;
import com.game.script.structs.ScriptEnum;
import com.game.skill.config.FindTargetInfo;
import com.game.skill.config.SkillEvent;
import com.game.skill.config.SkillEventContainer;
import com.game.skill.config.SkillVisual;
import com.game.skill.config.event.PlayHitEvent;
import com.game.skill.config.event.PlaySimpleSkillObjectEvent;
import com.game.skill.structs.Skill;
import com.game.skill.structs.SkillDefine;
import com.game.skill.structs.SkillMagic;
import com.game.structs.*;
import com.game.utils.Utils;
import game.core.script.IScript;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author admin
 */
public class PlayerAiScript implements IScript, IPlayerAi {

    protected Logger log = LogManager.getLogger(PlayerAiScript.class);

    @Override
    public int getId() {
        return ScriptEnum.PlayerAiCommonScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void OnPlayerAi(Player player) {
        try {

            MapObject map = Manager.mapManager.getMap(player.gainMapId());
            if(map == null){
                log.error("map is null {}",player.getCurGps().getModelId());
                return;
            }

            OnBeAttackMoveAi(player);
            //开始攻击
            IFightManagerScript is = (IFightManagerScript) Manager.scriptManager.GetScriptClass(ScriptEnum.FightManagerBaseScript);
            is.onFightSkillEvent(player);
            Pet pet = Manager.petManager.getBattlePet(player);
            if (pet != null) {
                is.onFightSkillEvent(pet);
            }
            HuaxinEntity entity = player.getCurHuaxinEntity();
            if (entity != null && entity.getExcelId() > 0) {
                if (entity.willEvents().size() > 0) {
                    /*循环玩家的ai事件*/
                    is.onFightSkillEvent(entity);
                }
                OnMagicAiOfFabao(map, player, entity);
            }

            OnMagicAi(map, player);

            OnFightStateAi(player);

            OnBuffAi(player);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    /**
     * 法宝载体技能
     *
     * @param huaxin
     */
    private void OnMagicAiOfFabao(MapObject map, Player owner, HuaxinEntity huaxin) {
        long curTime = TimeUtils.Time();
        //开始攻击
        IFightManagerScript is = (IFightManagerScript) Manager.scriptManager.GetScriptClass(ScriptEnum.FightManagerBaseScript);
        if (huaxin.getMagics().isEmpty()) {
            return;
        }
        Iterator<Long> iter = huaxin.getMagics().iterator();
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

            Iterator<Integer> iterator = magic.getSerials().iterator();

            Skill skill = magic.getSkillId() == 0 ? magic.getSkillinfo() : is.getFightSkill(huaxin, map, magic.getSkillId());
            if (skill == null) {
                skill = is.getBianshenSkill(owner, magic.getSkillId());
            }
            if (skill == null) {
                log.error("未知技能ID={}", magic.getSkillId());
                return;
            }
            int i = -1;
            while (iterator.hasNext()) {
                i += 1;
                int serialId = iterator.next();
                SkillEvent se = sv.getEvents().get(serialId);
                if (se instanceof PlayHitEvent) {
                    PlayHitEvent event = (PlayHitEvent) se;
                    if (onPlayHitEvent(curTime, magic, event, is, owner, skill)) {
                        iterator.remove();
                    }
                    continue;
                }

                if (se instanceof PlaySimpleSkillObjectEvent) {
                    PlaySimpleSkillObjectEvent psso = (PlaySimpleSkillObjectEvent) se;
                    long beginTime = (long) (i * psso.getHitInterval() * 1000 * SkillVisual.OneFrameTime);
                    if (onSimpleSkillObjectEvent(curTime, beginTime, magic, psso, is, owner, skill)) {
                        iterator.remove();
                    }
                }
            }
        }
    }

    private boolean OnBeAttackMoveAi(Player player) {
        if (player.getAmbs().isEmpty()) {
            return getCurHitType(player) != SkillDefine.SkillAttackMoveType_None;
        }
        long curTime = TimeUtils.Time();
        List<AttackMoveBehavior> ambs = new ArrayList<>(player.getAmbs());
        for (AttackMoveBehavior amb : ambs) {
            //时间错乱的抛弃了
            if (amb.getEnd() > curTime + 60000) {
                player.getAmbs().remove(amb);
                log.error("时间过了：" + amb.getTarget());
                continue;
            }

//            if (FightEnum.SuperArmorCount.compare(player.getState())) {
//                //LOGGER.error("霸体时间过滤=" + player);
//                //如果不是抓取
//                if (amb.getHitType() != SkillDefine.SkillAttackMoveType_Catch) {
//                    player.getAmbs().remove(amb);
//                    continue;
//                }
//            }
            if (amb.getStart() < curTime) {
                //LOGGER.error("取消行为时间=" + TimeUtils.Time() + "下一个开始时间=" + amb.getStart());
                player.getAmbs().remove(amb);
                player.addFightState(FightEnum.SkillFreeze);
                BehaviorManager.CancelBehaviorByType(player, BehaviorType.AttackMove);
                //打断怪物的引导技能
                if (player.getCurSlowSkill() != null) {
                    player.setCurSlowSkill(null);
                }
                if (amb.getHitType() == SkillDefine.SkillAttackMoveType_Catch) {
                    amb.setSpeed(Utils.getDistance(player.gainCurPos(), amb.getTarget()) / (amb.getEnd() - amb.getStart()) * 1000);
                }
                BehaviorManager.InsertBehavior(player, amb);
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


    //玩家召唤技能Tick
    private void OnMagicAi(MapObject map, Player player) {
        long curTime = TimeUtils.Time();
        //开始攻击
        IFightManagerScript is = (IFightManagerScript) Manager.scriptManager.GetScriptClass(ScriptEnum.FightManagerBaseScript);

        if (player.getMagics().isEmpty()) {
            return;
        }

        Iterator<Long> iter = player.getMagics().iterator();
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

            SkillVisual sv = SkillEventContainer.getInstance().getVisuals().get(magic.getMagicIndex());

            //开始检测攻击事件
            if (magic.getSerials().isEmpty() && curTime > magic.getStart() + sv.getCd()) {
                Manager.mapManager.manager().onQuitMap(map, magic, true);
                continue;
            }

            Iterator<Integer> iterator = magic.getSerials().iterator();

            Skill skill = magic.getSkillId() == 0 ? magic.getSkillinfo() : is.getFightSkill(player, map, magic.getSkillId());
            if (skill == null) {
                skill = is.getBianshenSkill(player, magic.getSkillId());
            }
            if (skill == null) {
                log.error("2未知技能ID={}", magic.getSkillId());
                return;
            }
            int i = -1;
            while (iterator.hasNext()) {
                i += 1;
                int serialId = iterator.next();
                SkillEvent se = sv.getEvents().get(serialId);
                if (se instanceof PlayHitEvent) {
                    PlayHitEvent event = (PlayHitEvent) se;
                    if (onPlayHitEvent(curTime, magic, event, is, player, skill)) {
                        iterator.remove();
                    }
                    continue;
                }

                if (se instanceof PlaySimpleSkillObjectEvent) {
                    PlaySimpleSkillObjectEvent psso = (PlaySimpleSkillObjectEvent) se;
                    long beginTime = (long) (i * psso.getHitInterval() * 1000 * SkillVisual.OneFrameTime);
                    if (onSimpleSkillObjectEvent(curTime, beginTime, magic, psso, is, player, skill)) {
                        iterator.remove();
                    }
                }
            }
        }
    }

    private boolean onPlayHitEvent(long curTime, SkillMagic magic, PlayHitEvent event, IFightManagerScript is, Player player, Skill skill) {
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

    private boolean onSimpleSkillObjectEvent(long curTime, long nextFpsTime, SkillMagic magic, PlaySimpleSkillObjectEvent event, IFightManagerScript is, Player player, Skill skill) {
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
    public List<Fighter> getMagicTargets(Player player, SkillMagic magic, FindTargetInfo find) {

        MapObject map = Manager.mapManager.getMap(magic.gainMapId());

        List<Fighter> fighter = MapUtils.getFighter(map, magic.gainCurPos());

        Fighter fabao = MapUtils.getFighter(map, magic.getOwnerId());
        IFightManagerScript is = (IFightManagerScript) Manager.scriptManager.GetScriptClass(ScriptEnum.FightManagerBaseScript);

        Iterator<Fighter> iter = fighter.iterator();
        while (iter.hasNext()) {
            Fighter next = iter.next();
            float dis = Utils.getDistance(magic.gainCurPos(), next.gainCurPos());
            if (dis > find.getAttackDis()) {
                iter.remove();
                continue;
            }
            if (player.getId() == next.getId()) {
                iter.remove();
                continue;
            }
            if (fabao instanceof HuaxinEntity &&
                    (next instanceof Player || next instanceof Robot)) {
                iter.remove();
                continue;
            }
            if (next instanceof Pet) {
                Pet pet = (Pet) next;
                if (pet.getOwnerId() == player.getId()) {
                    iter.remove();
                    continue;
                }
            }

            if (TimeUtils.Time() < next.getBrithProtect()) {
                iter.remove();
                continue;
            }
            if (next instanceof Pet) {
                Pet oPet = (Pet) next;
                next = map.getPlayer(oPet.getOwnerId());
            }

            if (!is.isCanPk(player, next)) {
                iter.remove();
            }

        }
        iter = fighter.iterator();
        while (iter.hasNext()) {
            iter.next();
            if (fighter.size() > find.getMaxTargetCount()) {
                iter.remove();
                continue;
            }
            break;
        }

        return fighter;
    }

    //状态管理
    private void OnFightStateAi(Player player) {

        if (player.getFightStates().isEmpty()) {
            return;
        }
        long curTime = TimeUtils.Time();
        Iterator<FightState> iter = player.getFightStates().iterator();
        while (iter.hasNext()) {
            FightState state = iter.next();
            if (state.getOverTime() < curTime) {
                iter.remove();
                state.romove(player);
                //LOGGER.info("结束状态 state=" + state + player);
            }
        }

    }

    private void OnBuffAi(Player player) {
        if (player.getBuffs().isEmpty()) {
            return;
        }
        try {
            Manager.buffManager.deal().tick(player);
        } catch (Exception e) {
            log.error(e, e);
        }
    }


    private float getPetMaxFollow(Player player) {
//        float follow = Global.GetIntValue(Global.PetFollowDis) / 100f;
        if (player.isInBattle()) {
            return Global.Pet_fight_rice / 100f;
        }
//        if (player.getPetAiState() == PetDefine.PetAiStateFollow && player.getHatreds().isEmpty()) {
//            follow = Global.GetIntValue(Global.PetFollowDis) / 100f;
//        }
        return Global.Pet_rest_rice / 100f;
    }


}
