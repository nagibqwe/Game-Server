package common.monster;

import com.data.CfgManager;
import com.data.bean.Cfg_Monster_Bean;
import com.game.behavior.manager.BehaviorManager;
import com.game.behavior.structs.BehaviorType;
import com.game.behavior.structs.type.RunBackBehavior;
import com.game.buff.manager.BuffManager;
import com.game.manager.Manager;
import com.game.map.manager.MapManager;
import com.game.map.structs.GroundBuff;
import com.game.map.structs.MapObject;
import com.game.monster.structs.Monster;
import com.game.monster.structs.MonsterDefine;
import com.game.script.structs.ScriptEnum;
import com.game.skill.structs.Skill;
import com.game.structs.EntityState;
import com.game.structs.Fighter;
import com.game.structs.Hatred;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import com.game.utils.Utils;
import com.game.yed.YedAI;
import com.game.yed.YedMgr;
import com.game.yed.scripts.YedMethodScript;
import game.core.map.Position;
import game.core.script.IScript;
import game.core.util.TimeUtils;
import game.message.FightMessage;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author admin
 */
public class MonsterYedAiScript implements IScript,YedMethodScript {

    protected Logger log = LogManager.getLogger(MonsterYedAiScript.class);

    @Override
    public int getId() {
        return ScriptEnum.MonsterYedAiBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    /**
     * 根据指定的怪物ID产生一个groundBuff
     *
     * @param ai 脚本
     * @param gbId 地形BUFFID
     * @param type 触发的类型， 如果是1是由客户端踏一次生效，如果是2则由配置表TICK计算生效
     */
    private boolean Sa_AddGroundBuff(YedAI ai, int gbId, int type) {
        try {
            Monster monster = (Monster) ai.getOwnerparams()[0];
            MapObject map = Manager.mapManager.getMap(monster.gainMapId());
//            return Manager.mapManager.createGroundBuff(map, new Position(monster.gainCurPos().ceilX(), monster.gainCurPos().ceilY()), gbId, type, -1) != null;
        } catch (Exception e) {
            log.fatal(e, e);
        }
        return false;
    }

    /**
     * 根据参数指定创建一个地形BUFF
     *
     * @param ai 脚本
     * @param gbId 地形BUFFID
     * @param x x坐标
     * @param y y坐标
     * @param type 触发的类型， 如果是1是由客户端踏一次生效，如果是2则由配置表TICK计算生效
     * @param groupId 指定是否生效与玩家同阵营， 如果是生效怪物请使用-1
     */
    private boolean Sa_createGroundBuff(YedAI ai, int gbId, int type, int x, int y, int groupId) {
        try {
            Monster monster = (Monster) ai.getOwnerparams()[0];
            MapObject map = Manager.mapManager.getMap(monster.gainMapId());
            if (x < 0) {
                return false;
            }

            if (y < 0) {
                return false;
            }

            Position pos = new Position(x, y);
            if (!Utils.isCanMove(map, pos)) {
                log.error("设置地形BUFF中设置在了阻档中" + pos);
                return false;
            }

//            return Manager.mapManager.createGroundBuff(map, pos, gbId, type, groupId) != null;
        } catch (Exception e) {
            log.fatal(e, e);
        }
        return false;
    }

    /**
     * 设置是否使用普通攻击
     *
     * @param ai
     * @param isUserNormalSkill
     */
    private void Sa_setUserNormalSkill(YedAI ai, boolean isUserNormalSkill) {
        try {
            Monster monster = (Monster) ai.getOwnerparams()[0];
            monster.setUserNormalSkill(isUserNormalSkill);
            YedMgr.getInstance().getAiLogger().trace(String.format("monsterai info Sa_setUserNormalSkill isUserNormalSkill:%s monster:%s", isUserNormalSkill ? "true" : "false", monster.nameIdString()));
        } catch (Exception e) {
            YedMgr.getInstance().getAiLogger().error(e, e);
        }
    }

    private void Sa_setTrapMonster(YedAI ai, boolean btrap) {

        try {
            Monster monster = (Monster) ai.getOwnerparams()[0];
            monster.setTrap(btrap);

            YedMgr.getInstance().getAiLogger().trace(String.format("monsterai info Sa_setTrapMonster btrap:%s monster:%s", btrap ? "true" : "false", monster));
        } catch (Exception e) {
            YedMgr.getInstance().getAiLogger().error(e, e);
        }

    }

    /**
     * 添加一个自杀接口
     *
     * @param ai
     */
    private void Sa_monsterKillSelf(YedAI ai) {
        try {
            Monster monster = (Monster) ai.getOwnerparams()[0];
            monster.setCurHp(0);
            monster.doDie(monster);
            FightMessage.ResObjDead.Builder msg = FightMessage.ResObjDead.newBuilder();
//            msg.setKillerId(monster.getId());
//            msg.setKillerName(monster.getName());
//            msg.setTransMark(0);
//            String destStr = "";
//            Utils.getMarkAfterString(monster.getName(), destStr);
            StringBuilder destStr = new StringBuilder();
            boolean isMark = Utils.getMarkAfterString(monster.getName(), destStr);
            msg.setTransMark(isMark ? 1 : 0);
            msg.setKillerName(destStr.toString());
//            msg.setKillerName();
            msg.setDeaderId(monster.getId());
            MessageUtils.send_to_roundPlayer(monster, FightMessage.ResObjDead.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            YedMgr.getInstance().getAiLogger().warn(String.format("monsterai info Sa_monsterKillSelf monster:%s", monster));
        } catch (Exception e) {
            YedMgr.getInstance().getAiLogger().error(e, e);
        }
    }

    private void Sa_setUseNewAi(YedAI ai, boolean bnew) {

        try {
            Monster monster = (Monster) ai.getOwnerparams()[0];
            monster.setUseNewAi(bnew);

            YedMgr.getInstance().getAiLogger().warn(String.format("monsterai info Sa_setUseNewAi btrap:%s monster:%s", bnew ? "true" : "false", monster));
        } catch (Exception e) {
            YedMgr.getInstance().getAiLogger().error(e, e);
        }
    }

    private void Sa_AutoAttack(YedAI ai) {
        try {
            Monster monster = (Monster) ai.getOwnerparams()[0];
            Cfg_Monster_Bean config = CfgManager.getCfg_Monster_Container().getValueByKey(monster.getModelId());
//            if (monster.getEnterBattlePos() == null) {
//                monster.setEnterBattlePos(monster.gainCurPos());
//            }
            //在战斗的时候才触发返回
            if (monster.isInBattle()) {
                float rec = Utils.getDistance(monster.gainCurPos(), monster.getInitPos());
                boolean isInit = false;
                if (config.getRecoversuit() / 100f < rec) {
                    isInit = true;
                    //LOGGER.info(config.getName() + " 脱战" + config.getRecoversuit());
                }
                //选取主目标
                if (monster.getHatreds().isEmpty()) {
                    //如果当前仇恨列表为空， 检查怪物是不是主动怪
                    if (config.getAttack_type() == MonsterDefine.MonsterActive) {
                        //检查周围是否有玩家
                        if (!Manager.monsterManager.ai().doAngerAi(monster, config)) {
                            isInit = true;
//                            LOGGER.info(config.getName() + " 没有找到玩家！");
                        }
                    } else {
                        isInit = true;
                        //LOGGER.info(config.getName() + " 仇恨为空！");
                    }
                }

                if (isInit) {
                    //重置状态
                    Manager.monsterManager.ai().reset(monster, config);

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
                    Manager.monsterManager.ai().sendEntitySpeed(monster);
                    Manager.monsterManager.ai().sendMonsterRun(monster);
                    Manager.mapManager.sendMoveMessage(monster, roads);
                    BehaviorManager.InsertOnlyBehavior(monster, new RunBackBehavior(monster));
                    return;
                }
            }

            //设置进入战斗时间
            if (monster.getEnterFight() <= 0) {
                monster.setEnterFight(TimeUtils.Time());
            }

            //获取主目标
            Hatred curMainTarget = Manager.monsterManager.ai().getCurMainTarget(monster);
            if (curMainTarget == null) {
                curMainTarget = Manager.monsterManager.ai().changeMainTarget(monster);
            }
            //无攻击目标
            if (curMainTarget == null) {
                return;
            }
            // 普通攻击优先级低,如果上次使用的是普通,那么清空一下,看看其他技能可用了不
            if (monster.getUseSkill() != null && monster.getUseSkill().isNormal()) {
                monster.setUseSkill(null);
            }

            Manager.monsterManager.ai().tryUseSkill(monster, config, curMainTarget);
        } catch (Exception ex) {
            log.fatal(ex, ex);
        }
    }

    private void Sa_pushAutoSkill(YedAI ai, int skillid) {

        try {
            Monster monster = (Monster) ai.getOwnerparams()[0];

            if (monster.getSkills().containsKey(skillid)) {
                return;
            }

            Skill skill = new Skill();
            skill.setSkillId(skillid);
            skill.setNormal(false);
            monster.getSkills().put(skill.getSkillId(), skill);

            YedMgr.getInstance().getAiLogger().warn(String.format("monsterai info Sa_pushAutoSkill skillid:%d monster:%s", skillid, monster));
        } catch (Exception e) {
            YedMgr.getInstance().getAiLogger().fatal(e, e);
        }
    }

    private void Sa_popAutoSkill(YedAI ai, int skillid) {

        try {
            Monster monster = (Monster) ai.getOwnerparams()[0];

            monster.getSkills().remove(skillid);

            YedMgr.getInstance().getAiLogger().warn(String.format("monsterai info Sa_popAutoSkill skillid:%d monster:%s", skillid, monster));
        } catch (Exception e) {
            YedMgr.getInstance().getAiLogger().fatal(e, e);
        }
    }

    private void Sa_randAddBuffToEnemy(YedAI ai, int enemycount, int buffid) {

        try {
            Monster monster = (Monster) ai.getOwnerparams()[0];

            List<Fighter> fighter = new ArrayList<>();

            if (monster.getHatreds().size() <= enemycount) {
                for (Hatred hat : monster.getHatreds()) {
                    Fighter defer = hat.getTarget();
                    fighter.add(defer);
                }
            } else {
                ArrayList<Integer> idxlist = new ArrayList<>();
                for (int i = 0; i < monster.getHatreds().size(); ++i) {
                    idxlist.add(i);
                }
                while (fighter.size() < enemycount) {
                    int idx = RandomUtils.random(0, idxlist.size() - 1);
                    fighter.add(monster.getHatreds().get(idxlist.get(idx)).getTarget());
                    idxlist.remove(idx);
                }
            }

            for (Fighter f : fighter) {
                BuffManager.getInstance().deal().onAddBuff(monster, f, buffid);
            }

            YedMgr.getInstance().getAiLogger().warn(String.format("monsterai info randAddBuffToEnemy enemycount:%d monster:%s buffid:%d fighter.size:%d", enemycount, monster, buffid, fighter.size()));
        } catch (Exception e) {
            YedMgr.getInstance().getAiLogger().fatal(e, e);
        }

    }

    /**
     * 设置当前转阶段状态 如果正在转阶段,那么就表示其他的ai不能再有移动,攻击等影响转阶段的行为
     *
     * @param ai
     * @param state
     */
    private void Sa_setChangeStepState(YedAI ai, String state) {

        try {
            Monster monster = (Monster) ai.getOwnerparams()[0];

            monster.setChangeStepState(state);

            // 重置状态
            monster.resetState();
            BehaviorManager.CancelAllBehavior(monster);
            monster.setCurSlowSkill(null);
            YedMgr.getInstance().getAiLogger().warn(String.format("monsterai info Sa_setChangeStepState state:%s monster:%s", state, monster));
        } catch (Exception e) {
            YedMgr.getInstance().getAiLogger().error(e, e);
        }
    }

    /**
     * 获取当前转阶段状态
     *
     * @param ai
     * @return
     */
    private String Sa_getChangeStepState(YedAI ai) {

        try {
            Monster monster = (Monster) ai.getOwnerparams()[0];
            YedMgr.getInstance().getAiLogger().trace(String.format("monsterai info Sa_getChangeStepState state:%s monster:%s", monster.getChangeStepState(), monster));

            return monster.getChangeStepState();
        } catch (Exception e) {
            YedMgr.getInstance().getAiLogger().error(e, e);
        }
        return "";
    }

    /**
     * 调用一次,往待用技能的最前面加一个
     *
     * @param ai
     * @param skillid
     */
    private void Sa_UseSkill(YedAI ai, int skillid) {

        try {
            Monster monster = (Monster) ai.getOwnerparams()[0];

            for (Skill s : monster.getAipushLists()) {
                if (s.getSkillId() == skillid) {
                    return;
                }
            }

            Skill skill = new Skill();
            skill.setSkillId(skillid);
            monster.getAipushLists().offer(skill);
            YedMgr.getInstance().getAiLogger().warn(String.format("monsterai info Sa_useSkill state:%s monster:%s", monster.getChangeStepState(), monster));
        } catch (Exception e) {
            YedMgr.getInstance().getAiLogger().error(e, e);
        }
    }

    /**
     * 去除地图里面的地型BUFF
     *
     * @param ai
     * @param groudBuffId
     */
    private void Sa_removeGroudBuff(YedAI ai, int groudBuffId) {
        try {
            Monster monster = (Monster) ai.getOwnerparams()[0];
            MapObject map = Manager.mapManager.getMap(monster.gainMapId());
            List<GroundBuff> gIds = new ArrayList<>();
            for (GroundBuff gb : map.getGroundBuffs().values()) {
                if (gb.getModelId() == groudBuffId) {
                    gIds.add(gb);
                }
            }
            //删除地形BUFF
            for (GroundBuff gb : gIds) {
                Manager.mapManager.manager().onQuitMap(map, gb, true);
            }
        } catch (Exception e) {
            log.fatal(e, e);
        }
    }

    /**
     * 增加移动的步数处理
     */
    private void Sa_addMovePos(YedAI ai, int x, int y) {
        try {
            Monster monster = (Monster) ai.getOwnerparams()[0];
            Position pos = MapManager.getPos(x, y);
            monster.getWillMovePos().add(pos);
        } catch (Exception e) {
            log.fatal(e, e);
        }
    }

    /**
     * 当前怪物是否已经走完了所有的步数
     *
     * @param ai
     * @return
     */
    private boolean Sa_monsterWillPosOver(YedAI ai) {
        try {
            Monster monster = (Monster) ai.getOwnerparams()[0];
            return monster.getWillMovePos().size() <= monster.getMoveStep();
        } catch (Exception e) {
            log.fatal(e, e);
        }
        return false;
    }

    /**
     * 检查当前怪物是否已经到达移到的点，如果已经到了返回， 没到就重新计算到达点
     *
     * @param ai
     * @param dis
     * @return
     */
    private boolean Sa_isMoveArrivedWillPos(YedAI ai, float dis) {
        try {
            Monster monster = (Monster) ai.getOwnerparams()[0];
            int len = monster.getWillMovePos().size();
            if (len <= monster.getMoveStep()) {
                log.info("mon:" + monster.nameIdString() + " 的移到长度为：" + len + " 当前到的步数" + monster.getMoveStep());
                return false;
            }

            Position pos = monster.getWillMovePos().get(monster.getMoveStep());

            float curdis = Utils.getDistance(monster.gainCurPos(), pos);
            if (curdis < dis) {
                monster.setDoStep(0);
                monster.setInitPos(pos);//初始化到达的位置，防止怪物回到最开始的初始点
                return true;
            }

            if (monster.getDoStep() != monster.getMoveStep()) {
                monster.entityScript().runToPosition(monster, pos);
                monster.setDoStep(monster.getMoveStep());
            } else {
                boolean is = BehaviorManager.HasBehavior(monster, BehaviorType.Move);
                if (!is) {
                    monster.entityScript().runToPosition(monster, pos);
                }
            }
            return false;
        } catch (Exception e) {
            log.fatal(e, e);
        }
        return false;
    }

    /**
     * 增加移到的步数
     *
     * @param ai
     */
    private void Sa_addMoveStep(YedAI ai) {
        try {
            Monster monster = (Monster) ai.getOwnerparams()[0];
            int lastNo = monster.getMoveStep();
            monster.setMoveStep(lastNo + 1);
            log.info(monster.nameIdString() + " will be from " + lastNo + "To " + monster.getMoveStep());
        } catch (Exception e) {
            log.fatal(e, e);
        }
    }


    @Override
    public boolean execAiMethod(YedAI ai, String methodName, Object[] args) {
        if (methodName == null || methodName.equals("")) {
            return false;
        }
        switch (methodName) {
            case "AddGroundBuff": {
                Sa_AddGroundBuff(ai, Double.valueOf(args[0].toString()).intValue(), Double.valueOf(args[1].toString()).intValue());
                break;
            }
            case "createGroundBuff": {
                Sa_createGroundBuff(ai, Double.valueOf(args[0].toString()).intValue(), Double.valueOf(args[1].toString()).intValue()
                        , Double.valueOf(args[2].toString()).intValue(), Double.valueOf(args[3].toString()).intValue()
                        , Double.valueOf(args[4].toString()).intValue());
                break;
            }
            case "setUserNormalSkill": {
                Sa_setUserNormalSkill(ai,Boolean.getBoolean(args[0].toString()));
                break;
            }
            case "setTrapMonster": {
                Sa_setTrapMonster(ai,Boolean.getBoolean(args[0].toString()));
                break;
            }
            case "monsterKillSelf": {
                Sa_monsterKillSelf(ai);
                break;
            }
            case "setUseNewAi": {
                Sa_setUseNewAi(ai, Boolean.getBoolean(args[0].toString()));
                break;
            }
            case "AutoAttack": {
                Sa_AutoAttack(ai);
                break;
            }
            case "pushAutoSkill": {
                Sa_pushAutoSkill(ai,Double.valueOf(args[0].toString()).intValue());
                break;
            }
            case "popAutoSkill": {
                Sa_popAutoSkill(ai,Double.valueOf(args[0].toString()).intValue());
                break;
            }
            case "randAddBuffToEnemy": {
                Sa_randAddBuffToEnemy(ai,Double.valueOf(args[0].toString()).intValue(),Double.valueOf(args[1].toString()).intValue());
                break;
            }
            case "setChangeStepState": {
                Sa_setChangeStepState(ai,args[0].toString());
                break;
            }
            case "getChangeStepState": {
                ai.setFuncRet(Sa_getChangeStepState(ai));
                break;
            }
            case "UseSkill": {
                Sa_UseSkill(ai,Double.valueOf(args[0].toString()).intValue());
                break;
            }
            case "removeGroudBuff": {
                Sa_removeGroudBuff(ai,Double.valueOf(args[0].toString()).intValue());
                break;
            }
            case "addMovePos": {
                Sa_addMovePos(ai,Double.valueOf(args[0].toString()).intValue(),Double.valueOf(args[1].toString()).intValue());
                break;
            }
            case "monsterWillPosOver": {
                ai.setFuncRet(Sa_monsterWillPosOver(ai));
                break;
            }
            case "isMoveArrivedWillPos": {
                ai.setFuncRet(Sa_isMoveArrivedWillPos(ai,Double.valueOf(args[0].toString()).floatValue()));
                break;
            }
            case "addMoveStep": {
                Sa_addMoveStep(ai);
                break;
            }

        }
        return true;
    }
}
