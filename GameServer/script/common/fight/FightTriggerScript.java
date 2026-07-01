package common.fight;

import com.data.CfgManager;
import com.data.bean.Cfg_Skill_Bean;
import com.data.bean.Cfg_Skill_Trigger_Bean;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArray;
import com.game.attribute.BaseIntAttribute;
import com.game.cooldown.structs.CooldownTypes;
import com.game.fight.script.IFightTriggerScirpt;
import com.game.fight.structs.FightSkillEvent;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapUtils;
import com.game.nature.structs.HuaxinEntity;
import com.game.pet.structs.Pet;
import com.game.script.structs.ScriptEnum;
import com.game.skill.config.SkillEvent;
import com.game.skill.config.SkillEventContainer;
import com.game.skill.config.SkillVisual;
import com.game.skill.structs.Skill;
import com.game.skill.structs.SkillDefine;
import com.game.skill.structs.SkillMagic;
import com.game.structs.Fighter;
import com.game.utils.RandomUtils;
import game.core.map.Position;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.List;

/**
 * @Description
 * @auther lw
 * @create 2019-12-18 14:10
 */
public class FightTriggerScript implements IFightTriggerScirpt {

    private static final Logger log = LogManager.getLogger(FightTriggerScript.class);

    @Override
    public void trigger(MapObject mapObject, Fighter source, Fighter target, BaseIntAttribute ba, BaseIntAttribute bf, int type, boolean isTrigger) {
        if (mapObject == null) {
            return;
        }

        if (isTrigger) {
            return;
        }

        if (source == null) {
            return;
        }

        if (source.getType() == Fighter.NATURE_TYPE) {
            return;
        }

        if (source.getType() == Fighter.SKILLMAGIC_TYPE) {
            source = MapUtils.getFighter(mapObject, ((SkillMagic)source).getOwnerId());
            if (source.getType() == Fighter.NATURE_TYPE) {
                return;
            }
        }

        if (target.getType() == Fighter.NATURE_TYPE) {
            return;
        }

        if (target.getType() == Fighter.SKILLMAGIC_TYPE) {
            target = MapUtils.getFighter(mapObject, ((SkillMagic)target).getOwnerId());
            if (target.getType() == Fighter.NATURE_TYPE) {
                return;
            }
        }

        //人,宠,怪物
        Iterator<Skill> iter = source.getSkills().values().iterator();
        while (iter.hasNext()) {
            Skill skill = iter.next();
            if (skill == null) {
                iter.remove();
                continue;
            }
            Cfg_Skill_Bean bean = CfgManager.getCfg_Skill_Container().getValueByKey(skill.getSkillId());
            if (bean == null) {
                iter.remove();
                continue;
            }
            if (bean.getType() != SkillDefine.SkillType_UnActive) {
                continue;
            }
            triggerEffect(source, target, ba, bf, type, bean);
        }
    }

    @Override
    public void activeTrigger(Cfg_Skill_Bean bean, MapObject mapObject, Fighter source, Fighter target, BaseIntAttribute ba, BaseIntAttribute bf, int type, boolean isTrigger) {
        if (bean.getType() != SkillDefine.SkillType_Active) {
            return;
        }
        if (mapObject == null) {
            return;
        }

        if (isTrigger) {
            return;
        }

        if (source == null) {
            return;
        }

      //  if (source.getType() == Fighter.NATURE_TYPE) {
      //      return;
      //  }

        //if (source.getType() == Fighter.SKILLMAGIC_TYPE) {
        //    source = MapUtils.getFighter(mapObject, ((SkillMagic)source).getOwnerId());
        //    if (source.getType() == Fighter.NATURE_TYPE) {
        //        return;
        //    }
        //}

        if (target.getType() == Fighter.NATURE_TYPE) {
            return;
        }

        if (target.getType() == Fighter.SKILLMAGIC_TYPE) {
            target = MapUtils.getFighter(mapObject, ((SkillMagic)target).getOwnerId());
            if (target.getType() == Fighter.NATURE_TYPE) {
                return;
            }
        }

        triggerEffect(source, target, ba, bf, type, bean);
    }


    private void triggerEffect(Fighter source, Fighter target, BaseIntAttribute ba, BaseIntAttribute bf, int type, Cfg_Skill_Bean skill_bean) {
        for (int triggerId: skill_bean.getTrigger().getValue()) {
            Cfg_Skill_Trigger_Bean triggerBean = CfgManager.getCfg_Skill_Trigger_Container().getValueByKey(triggerId);
            if (triggerBean == null) {
                continue;
            }

            if (type != triggerBean.getTrigger_Conditions()) {
                continue;
            }

            if (triggerBean.getTrigger_type() == 1 && target.getType() != Fighter.PLAYER_TYPE) {
                continue;
            }

            if (triggerBean.getTrigger_type() == 2 && target.getType() != Fighter.MONSTER_TYPE) {
                continue;
            }


            if (triggerBean.getTarget() == SkillDefine.TARGET_SELF) {
                triggerEffect(source, source,triggerBean, ba);
                continue;
            }

            if (triggerBean.getTarget() == SkillDefine.TARGET_OTHER) {
                triggerEffect(target,source, triggerBean, bf);
                continue;
            }

            if (triggerBean.getTarget() == SkillDefine.TARGET_OWNER) {
                if (source.getType() == Fighter.PET_TYPE) {
                    Pet pet = (Pet) source;
                    Fighter trigger = Manager.playerManager.getPlayerOnline(pet.getOwnerId());
                    triggerEffect(trigger,trigger, triggerBean, ba);
                    continue;
                }
            }
        }
    }
    private void triggerEffect(Fighter trigger,Fighter own, Cfg_Skill_Trigger_Bean triggerBean, BaseIntAttribute triggerAtt) {
        if (isTriggerEffect(trigger,own, triggerBean)) {
//            log.info(trigger.getName() + " 触发Id:" + triggerBean.getId() + " 触发技能使用");
            for (ReadArray<Integer> aii : triggerBean.getEffect().getValuees()) {
                //增加属性
                if (aii.get(0) == 1) {
                    if (!isTrigger(trigger, aii.get(3), triggerBean.getId(), false)) {
                        continue;
                    }
                    triggerAtt.addAttribute(aii.get(1), aii.get(2));
                    //log.info(trigger.getName() + "触发Id:" + triggerSkill.getId());
                }

                //增加buff
                if (aii.get(0) == 2) {
                    if (!isTrigger(trigger, aii.get(2), triggerBean.getId(), true)) {
                        continue;
                    }

                    //如果是载体施法 找出 父亲
                    if (trigger instanceof  SkillMagic){
                       // target = MapUtils.getFighter(mapObject, ((SkillMagic)trigger).getOwnerId());
                        MapObject mapObject = Manager.mapManager.getMap( trigger.getCurGps().getMapId());
                        if (mapObject !=null){
                            trigger = MapUtils.getFighter(mapObject, ((SkillMagic) trigger).getOwnerId());
                            if (trigger instanceof HuaxinEntity){
                                trigger = MapUtils.getFighter(mapObject, ((HuaxinEntity) trigger).getOwnerId());
                            }
                        }
                    }

                    Manager.buffManager.deal().onAddBuff(own, trigger, aii.get(1));
                    Manager.cooldownManager.addCooldown(trigger, CooldownTypes.SkillTrigger, triggerBean.getId() + "", aii.get(3));
                    //log.info(fighter.getName() + " 触发Id:" + tb.getId() + " 增加Buff：" + aii.get(1));


                }

                //清除buff
                if (aii.get(0) == 3) {
                    if (!isTrigger(trigger, aii.get(1), triggerBean.getId(), true)) {
                        continue;
                    }
                    Manager.buffManager.deal().removeAllHarmBuff(trigger);
                    Manager.cooldownManager.addCooldown(trigger, CooldownTypes.SkillTrigger, triggerBean.getId() + "", aii.get(2));
                    //log.info(fighter.getName() + " 触发Id:" + tb.getId() + " 移除所有负面Buff");
                }

                //使用技能
                if (aii.get(0) == 4) {
                    if (!isTrigger(trigger, aii.get(2), triggerBean.getId(), true)) {
                        continue;
                    }
                    Manager.cooldownManager.addCooldown(trigger, CooldownTypes.DOUBLE_SKILL_CD, triggerBean.getId() + "", aii.get(3));
                    Skill skill = new Skill();
                    skill.setSkillId(aii.get(1));
                    SkillVisual sv = getSkillVisual(aii.get(1));
                    if (sv == null) {
                        return;
                    }
                    //log.info(fighter.getName() + " 触发Id:" + tb.getId() + " 触发技能使用");
                    pushEventByPassive(trigger, skill, sv, null, null, 0, 0);
                }
            }
        }
    }

    private boolean isTriggerEffect(Fighter trigger,Fighter own, Cfg_Skill_Trigger_Bean triggerBean) {
        if (triggerBean.getHp() != null && triggerBean.getHp().size() >= 2) {
            float curHp = trigger.getCurHp();
            float preHp  =  (curHp / trigger.getAttribute().MaxHP());
            if (triggerBean.getHp().get(0) == 0 && preHp *10000 >  triggerBean.getHp().get(1)) {
                return false;
            }

            if (triggerBean.getHp().get(0) == 1 && preHp *10000 <  triggerBean.getHp().get(1)) {
                return false;
            }
        }

        if (triggerBean.getBuff() != 0 && !Manager.buffManager.deal().isExist(trigger, triggerBean.getBuff())) {
            return false;
        }
        if (triggerBean.getBuff_num() != null && triggerBean.getBuff_num().size() >= 2) {
            if (Manager.buffManager.deal().getBuffNum(trigger, triggerBean.getBuff_num().get(0)) < triggerBean.getBuff_num().get(1)) {
                return false;
            }
        }
        if (triggerBean.getSkill()>0){
            if (own instanceof  SkillMagic){
                MapObject mapObject = Manager.mapManager.getMap( own.getCurGps().getMapId());
                if (mapObject !=null){
                    own = MapUtils.getFighter(mapObject, ((SkillMagic) own).getOwnerId());
                }
            }
           if (!own.getSkills().containsKey(triggerBean.getSkill())){
               return false;
           }
        }
        return true;
    }

    private boolean isTrigger(Fighter trigger, int randomNum, int triggerId, boolean checkCd) {
        if (RandomUtils.random(10000) > randomNum) {
            return false;
        }

        if (checkCd && Manager.cooldownManager.isCooldowning(trigger, CooldownTypes.SkillTrigger, triggerId + "")) {
            return false;
        }
        return true;
    }

    private SkillVisual getSkillVisual(int skillId) {
        Cfg_Skill_Bean skillBean = CfgManager.getCfg_Skill_Container().getValueByKey(skillId);
        return SkillEventContainer.getInstance().getVisuals().get(skillBean.getVisualDef());
    }


    private void pushEventByPassive(Fighter attacker, Skill skill, SkillVisual sv, Position dirPos, List<Fighter> beAttacks, int x, int y) {
        for (SkillEvent se : sv.getEventList()) {
            FightSkillEvent fse = new FightSkillEvent();
            fse.setMapId(attacker.gainMapId());
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
            fse.setCopy(true);
            Manager.fightManager.deal().doSkillEvent(attacker, fse, se.getEventID());
        }
    }

    @Override
    public int getId() {
        return ScriptEnum.FightTriggerBaseScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }
}
