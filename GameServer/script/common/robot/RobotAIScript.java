package common.robot;

import com.data.CfgManager;
import com.data.bean.Cfg_Skill_Bean;
import com.game.cooldown.structs.CooldownTypes;
import com.game.fight.manager.FightManager;
import com.game.fight.sorter.MonsterFightSorter;
import com.game.manager.Manager;
import com.game.map.manager.MapManager;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapUtils;
import com.game.player.structs.Player;
import com.game.robot.script.IRobotAiScript;
import com.game.robot.struct.Robot;
import com.game.script.structs.ScriptEnum;
import com.game.skill.config.SkillEventContainer;
import com.game.skill.config.SkillVisual;
import com.game.skill.structs.Skill;
import com.game.structs.Fighter;
import com.game.structs.STCheckerMaker;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import com.game.yed.YedAI;
import com.game.yed.YedMgr;
import com.game.yed.scripts.YedMethodScript;
import game.core.map.Position;
import game.core.script.IScript;
import game.message.MapMessage;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;

/**
 * @author admin
 */
public class RobotAIScript implements IScript, IRobotAiScript,YedMethodScript {

    private static final Logger logger = YedMgr.getInstance().getAiLogger();
    

    private boolean bShowAiLog = true;

    @Override
    public int getId() {
        return ScriptEnum.RobotAiCommonScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void OnRobotAi(Robot op, Player maker, MapObject curmap) {
        if (op == null) {
            return;
        }
        if (op.gainRunningAi() == null) {
            return;
        }
        YedAI runningAi = op.gainRunningAi();
        runningAi.SetOwner(this);
        runningAi.setOwnerparams(op, maker, curmap);
        // 找不到的函数就走基类ai里面找,每次都设置是为了动态更新的时候每次取到最新的对象
        runningAi.addCommonTarget(yedBaseAi());
        runningAi.Update();
    }

    private int Sa_FindHateTarget(YedAI ai) {
        Object[] ps = ai.getOwnerparams();
        if (ps == null || ps.length < 1 || ps[Robot.AiParam.robot.value] == null || !(ps[Robot.AiParam.robot.value] instanceof Robot)) {
            logger.error("跑ai Sa_FindHateTarget的时候,参数没设置对 需要一个Robot");
            return 0;
        }
        Robot op = (Robot) ai.getOwnerparams()[Robot.AiParam.robot.value];
        if (op.getHatreds().isEmpty()) {
            // 打主人的敌人
            Player maker = (Player) ai.getOwnerparams()[Robot.AiParam.player.value];
            if(maker.getHatreds().isEmpty()){
                logger.warn(String.format("robot ai %d Sa_FindHateTarget target:empty", op.getId()));
                return 0;
            }
            else{
                Fighter f = maker.gainFirstHatredTarget();
                if(f != null){
                    op.setCurAttackTargetId(f.getId());
                }
                else{
                    return 0;
                }
            }
        }
        else{
            Fighter f = op.gainFirstHatredTarget();
            if(f != null){
                op.setCurAttackTargetId(f.getId());
            }
            else{
                return 0;
            }
        }
        logger.warn(String.format("robot ai %d Sa_FindHateTarget target:%d", op.getId(), op.gainCurAttackTargetId()));
        return 1;
    }

    private int Sa_FindMonsterTarget(YedAI ai, int radius) {
        Object[] ps = ai.getOwnerparams();
        if (ps == null || ps.length < 1 || ps[Robot.AiParam.robot.value] == null || !(ps[Robot.AiParam.robot.value] instanceof Robot)) {
            logger.error("跑ai Sa_FindMonsterTarget,参数没设置对 需要一个Robot");
            return 0;
        }
        Robot op = (Robot) ai.getOwnerparams()[Robot.AiParam.robot.value];
        MapObject m = MapManager.getInstance().getMap(op.gainMapId());
        if (m == null) {
            logger.error("跑ai Sa_FindMonsterTarget,op getmap 出问题了");
            return 0;
        }

        Position rad = MapManager.getInstance().getAreaLen(m, new Position(radius, radius));

        List<Fighter> fighters = MapUtils.getFighterNearNoCheck(m, op.gainCurPos(), (int) Math.ceil(rad.getX()), (int) Math.ceil(rad.getY()), MapUtils.FighterFlag.NoPlayer.v | MapUtils.FighterFlag.NoRobot.v);
        fighters.remove(op);
        if (fighters == null || fighters.isEmpty()) {
            logger.warn("Sa_FindMonsterTarget fighters == null || fighters.isEmpty()");
            return 0;
        }
        MonsterFightSorter sorter = new MonsterFightSorter();
        sorter.op = op;
        Collections.sort(fighters, sorter);
        op.setCurAttackTargetId(fighters.get(0).getId());
        logger.warn(String.format("robot ai %d Sa_FindMonsterTarget target:%d", op.getId(), op.gainCurAttackTargetId()));
        return 1;
    }

    private boolean Sa_IsTargetDead(YedAI ai) {
        Object[] ps = ai.getOwnerparams();
        if (ps == null || ps.length < 1 || ps[Robot.AiParam.robot.value] == null || !(ps[Robot.AiParam.robot.value] instanceof Robot)) {
            logger.error("跑ai Sa_IsTargetDead,参数没设置对 需要一个Robot");
            return true;
        }
        Robot op = (Robot) ai.getOwnerparams()[Robot.AiParam.robot.value];
        MapObject map = MapManager.getInstance().getMap(op.gainMapId());
        if (map == null) {
            logger.error("Sa_IsTargetDead map == null");
            return true;
        }
        Fighter kill = op.gainCurAttackTarget(map);
        if (kill == null) {
            logger.warn(String.format("robot ai %d Sa_IsTargetDead kill == null", op.getId()));
            return true;
        }
        logger.warn(String.format("robot ai %d Sa_IsTargetDead kill.isDie():%s", op.getId(), kill.isDie() ? "true" : "false"));
        return kill.isDie();
    }

    private boolean Sa_WaitSkillCD(YedAI ai) {
        Object[] ps = ai.getOwnerparams();
        if (ps == null || ps.length < 1 || ps[Robot.AiParam.robot.value] == null || !(ps[Robot.AiParam.robot.value] instanceof Robot)) {
            logger.error("跑ai Sa_WaitSkillCD,参数没设置对 需要一个Robot");
            return true;
        }
        Robot op = (Robot) ai.getOwnerparams()[Robot.AiParam.robot.value];

        if (Manager.cooldownManager.isCooldowning(op, CooldownTypes.OFFLINE_AI_ATK_CD, null)) {
            logger.warn(String.format("robot ai %d Sa_WaitSkillCD false", op.getId()));
            return false;
        }
        logger.error(String.format("robot ai %d Sa_WaitSkillCD true", op.getId()));
        return true;
    }

    private boolean Sa_IsDead(YedAI ai) {
        Object[] ps = ai.getOwnerparams();
        if (ps == null || ps.length < 1 || ps[Robot.AiParam.robot.value] == null || !(ps[Robot.AiParam.robot.value] instanceof Robot)) {
            logger.error("跑ai Sa_IsDead,参数没设置对 需要一个Robot");
            return true;
        }
        Robot op = (Robot) ai.getOwnerparams()[Robot.AiParam.robot.value];
        logger.warn(String.format("robot ai %d Sa_IsDead op.isDie:%s", op.getId(), op.isDie() ? "true" : "false"));
        return op.isDie();
    }

    private void Sa_AutoAttack(YedAI ai) {
        Object[] ps = ai.getOwnerparams();
        if (ps == null || ps.length < 1 || ps[Robot.AiParam.robot.value] == null || !(ps[Robot.AiParam.robot.value] instanceof Robot)) {
            logger.error("跑ai Sa_AutoAttack,参数没设置对 需要一个Robot");
            return;
        }
        Robot op = (Robot) ps[Robot.AiParam.robot.value];
        Player maker = (Player) ps[Robot.AiParam.player.value];
        Manager.cooldownManager.addCooldown(op, CooldownTypes.OFFLINE_AI_ATK_CD, null, 333);
        MapObject map = MapManager.getInstance().getMap(op.gainMapId());
        if (map == null) {
            return;
        }
        final Fighter target = op.gainCurAttackTarget(map);
        if (target == null) {
            logger.error("跑ai Sa_AutoAttack,参数getAttackTarget没设置对");
            return;
        }

        Skill skill = FightManager.getInstance().deal().getCanUseSkill(op);
        if (skill == null) {
            return;
        }
        Cfg_Skill_Bean skillBean = CfgManager.getCfg_Skill_Container().getValueByKey(skill.getSkillId());
        if (skillBean == null) {
            logger.error("跑ai Sa_AutoAttack 技能找不到配置:" + skill.getSkillId());
            return;
        }
        SkillVisual sv = SkillEventContainer.getInstance().getVisuals().get(skillBean.getVisualDef());
        if (sv == null) {
            logger.error("跑ai Sa_AutoAttack 技能找不到Visual:" + skill.getSkillId());
            return;
        }

        // 看看当前是不是已经在跑引导技能了,ai的引导技能走behavior
        if (sv.isLockTrajact() && op.getCurSlowSkill() != null) {
            return;
        }
        float dis = Utils.getDistance(op.gainCurPos(), target.gainCurPos());
        // 追击
        if (dis > sv.getAttackDis()) {
            if (Manager.cooldownManager.isCooldowning(op, CooldownTypes.MonsterPursue, null)) {
                return;
            }
            try {
                // 暂时不知道这个跑的函数该挪到哪里,先实现功能
                maker.entityScript().runToTarget(op, target, sv.getAttackDis());
            } catch (Exception e) {
                logger.error("跑ai Sa_AutoAttack 技能找不到Visual:" + skill.getSkillId(), e);
            }
            Manager.cooldownManager.addCooldown(op, CooldownTypes.MonsterPursue, null, 3000);
            return;
        }

        // 调整方向
        Position dir = Utils.getDir(op.gainCurPos(), target.gainCurPos());
        op.setDir(dir);

        // 先看看主目标和仇恨列表里面的
        final List<Fighter> enemys = FightManager.getInstance().deal().getAnemys(op, target, skill, sv);
        // 设置范围技能对象选择过滤器
        op.changeSkillTargetFilter(ScriptEnum.AiPlayerSkillTargetCheckerCommonScript);
        // 再看看技能攻击范围内的
        STCheckerMaker checker = STCheckerMaker.createChecker(ScriptEnum.GuideSkillTargetCheckerCommonScript, enemys, Integer.MAX_VALUE);
        enemys.addAll(FightManager.getInstance().deal().getEnemysInSkillArea(map, op, target.gainCurPos(), dir, skill, sv, checker));

        for(Fighter e : enemys){
            op.addHatred(e, 0);
        }
        FightManager.getInstance().deal().attack(op, skill, enemys,null);
        op.setUseSkill(null);
        logger.warn(String.format("robot ai %d Sa_AutoAttack attack skill:%s enemys:%d", op.getId(), skill.toString(), enemys.size()));
    }

    private float Sa_DistanceFromMaker(YedAI ai){
        Player player = (Player) ai.getOwnerparams()[Robot.AiParam.player.value];
        Robot robot = (Robot) ai.getOwnerparams()[Robot.AiParam.robot.value];
        if(player == null || robot == null) return 0;
        float dis = Utils.getDistance(player.gainCurPos(), robot.gainCurPos());
        logger.info("Sa_Distance will return " + dis);
        return dis;
    }

    private void Sa_MoveToPlayer(YedAI ai, float dis){
        Player player = (Player) ai.getOwnerparams()[Robot.AiParam.player.value];
        Robot op = (Robot) ai.getOwnerparams()[Robot.AiParam.robot.value];
        try {
            player.entityScript().runToTarget(op, player, dis);
            logger.warn(String.format("robot ai %d Sa_MoveToPlayer player:%s dis:%f", op.getId(), player.toString(), dis));
        } catch (Exception e) {
            logger.error("跑ai Sa_MoveToPlayer OfflineDeal异常:", e);
        }
    }

    private void Sa_relive(YedAI ai){
        //Player player = (Player) ai.getOwnerparams()[Robot.AiParam.player.value];
        try {
            Robot op = (Robot) ai.getOwnerparams()[Robot.AiParam.robot.value];
            MapObject map = MapManager.getInstance().getMap(op.gainMapId());
            //原地复活
            op.reset();
            op.onHpChange(op);

            MapMessage.ResRelive.Builder msg = MapMessage.ResRelive.newBuilder();
            msg.setPlayerId(op.getId());
            msg.setMapId(map.getMapModelId());
            msg.setCurPos(MapUtils.getPos(op.gainCurPos()));
            MessageUtils.send_to_roundPlayer(op, MapMessage.ResRelive.MsgID.eMsgID_VALUE, msg.build().toByteArray(), true);
            logger.warn(String.format("robot ai %d Sa_relive", op.getId()));
        } catch (Exception e) {
            logger.error("跑ai Sa_relive OfflineDeal异常:", e);
        }

    }

    public YedMethodScript yedBaseAi() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.EntityAiCommonScript);
        if (is instanceof YedMethodScript) {
            return (YedMethodScript) is;
        }
        return null;
    }

    @Override
    public boolean execAiMethod(YedAI ai, String methodName, Object[] args) {
        if(methodName==null || methodName.equals("")){
            return false;
        }
        switch (methodName) {
            case "relive": {
                Sa_relive(ai);
                break;
            }
            case "MoveToPlayer": {
                Sa_MoveToPlayer(ai,Double.valueOf(args[0].toString()).intValue());
                break;
            }
            case "DistanceFromMaker": {
                ai.setFuncRet(Sa_DistanceFromMaker(ai));
                break;
            }
            case "AutoAttack": {
                Sa_AutoAttack(ai);
                break;
            }
            case "IsDead": {
                ai.setFuncRet(Sa_IsDead(ai));
                break;
            }
            case "WaitSkillCD": {
                ai.setFuncRet(Sa_WaitSkillCD(ai));
                break;
            }
            case "IsTargetDead": {
                ai.setFuncRet(Sa_IsTargetDead(ai));
                break;
            }
            case "FindMonsterTarget": {
                ai.setFuncRet(Sa_FindMonsterTarget(ai,Double.valueOf(args[0].toString()).intValue()));
                break;
            }
            case "FindHateTarget": {
                ai.setFuncRet(Sa_FindHateTarget(ai));
                break;
            }
        }
        return true;
    }
}
