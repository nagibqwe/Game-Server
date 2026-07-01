package common.Entity;

import com.game.buff.manager.BuffManager;
import com.game.chat.structs.Notify;
import com.game.manager.Manager;
import com.game.map.manager.MapGpsUtil;
import com.game.map.manager.MapManager;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapGps;
import com.game.map.structs.MapUtils;
import com.game.monster.manager.MonsterManager;
import com.game.monster.script.IMonsterAi;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.robot.ai.RobotAi;
import com.game.robot.manager.RobotManager;
import com.game.robot.struct.Robot;
import com.game.script.structs.ScriptEnum;
import com.game.skill.structs.SkillMagic;
import com.game.structs.AttributeType;
import com.game.structs.Entity;
import com.game.structs.EntityState;
import com.game.structs.Fighter;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import com.game.yed.YedAI;
import com.game.yed.YedMgr;
import com.game.yed.scripts.YedMethodScript;
import game.core.map.Position;
import game.core.script.IScript;
import game.message.MapMessage;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by huhu on 2017/9/26.
 */
public class EntityAiScript implements IScript,YedMethodScript {

    final static private Logger logger = YedMgr.getInstance().getAiLogger();

    @Override
    public int getId() {
        return ScriptEnum.EntityAiCommonScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    /**
     * 设置移动速度等级,目前移动基数是2000,Global.MoveSpeedOffset字段控制
     *
     * @param ai
     * @param speed
     */
    private void Sa_SetSpeed(YedAI ai, int speed) {
        Object[] ps = ai.getOwnerparams();
        if (ps == null || ps.length < 1 || ps[Entity.AiParam.entity.value] == null || !(ps[Entity.AiParam.entity.value] instanceof Entity)) {
            logger.error("跑ai Sa_SetSpeed,参数没设置对 需要一个entity");
            return;
        }
        Entity op = (Entity) ai.getOwnerparams()[Entity.AiParam.entity.value];
        op.getAttribute().setAttribute(AttributeType.ATTR_Speed, speed);
        op.getAttribute().calFinalMoveSpeed();
        logger.warn(String.format("ai action SetSpeed op:%s speed:%d final:%d", op, speed, op.getAttribute().gainFinalMoveSpeed()));
        MapUtils.synMoveSpeed(op);
    }

    /**
     * 是否处在某个位置上
     *
     * @param ai
     * @param x
     * @param y
     * @param dis 误差距离(单位米)
     * @return
     */
    private boolean Sa_IsArrivedPos(YedAI ai, float x, float y, float dis) {
        Object[] ps = ai.getOwnerparams();
        if (ps == null || ps.length < 1 || ps[Entity.AiParam.entity.value] == null || !(ps[Entity.AiParam.entity.value] instanceof Entity)) {
            logger.error("跑ai Sa_MoveTo,参数没设置对 需要一个entity");
            return false;
        }
        Entity op = (Entity) ai.getOwnerparams()[Entity.AiParam.entity.value];
        float curdis = Utils.getDistance(op.gainCurPos(), new Position(x, y));
        logger.info(String.format("ai condition Sa_IsArrivedPos op:%s x:%.2f y:%.2f curdis:%.2f dis:%.2f op.pos:%s", op, x, y, curdis, dis, op.gainCurPos()));
        return curdis < dis;
    }

    /**
     * 移动到该场景上的一个固定位置
     *
     * @param ai
     * @param posx
     * @param posy
     */
    private void Sa_MoveTo(YedAI ai, float posx, float posy) {
        Object[] ps = ai.getOwnerparams();
        if (ps == null || ps.length < 1 || ps[Entity.AiParam.entity.value] == null || !(ps[Entity.AiParam.entity.value] instanceof Entity)) {
            logger.error("跑ai Sa_MoveTo,参数没设置对 需要一个entity");
            return;
        }
        Entity op = (Entity) ai.getOwnerparams()[Entity.AiParam.entity.value];
        logger.warn(String.format("ai action Sa_MoveTo op:%s x:%.2f y:%.2f", op.toString(), posx, posy));

        try {
            op.entityScript().runToPosition(op, new Position(posx, posy));
        } catch (Exception e) {
            logger.fatal(e, e);
        }
    }

    /**
     * 朝当前目标方向移动dis米
     *
     * @param ai
     * @param dis
     */
    private void Sa_MoveToRefTarget(YedAI ai, float dis) {
        Object[] ps = ai.getOwnerparams();
        if (ps == null || ps.length < 1 || ps[Entity.AiParam.entity.value] == null || !(ps[Entity.AiParam.entity.value] instanceof Entity)) {
            logger.error("跑ai Sa_MoveToRefTarget,参数没设置对 需要一个entity");
            return;
        }
        Entity op = (Entity) ai.getOwnerparams()[Entity.AiParam.entity.value];

        try {
            MapObject map = MapManager.getInstance().getMap(op.gainMapId());
            if (map == null) {
                logger.error("Sa_MoveToRefTarget 释放者的地图信息没了...");
                return;
            }
            // 有些在用这个,这个找不到就换仇恨列表里的0
            Fighter target = op.gainCurAttackTarget(map);
            if (target == null || target.isDie()) {
                target = op.gainFirstHatredTarget();
            }
            if (target == null) {
                logger.warn(String.format("ai action Sa_MoveToRefTarget op:%s dis:%.2f target null", op.toString(), dis));
                return;
            }
            Position dir = Utils.getDir(op.gainCurPos(), target.gainCurPos());
            op.setDir(dir);
            Position pos = Utils.getPosByDir(op.gainCurPos(), dir, dis);
            logger.warn(String.format("ai action Sa_MoveToRefTarget op:%s dis:%.2f to pos:%s targetpos:%s", op, dis, pos.toString(), target.gainCurPos().toString()));
            op.entityScript().runToPosition(op, pos);
        } catch (Exception e) {
            logger.fatal(e, e);
        }
    }

    /**
     * 朝angle方向,从自己开始,走dis米
     *
     * @param ai
     * @param angle
     * @param dis
     */
    private void Sa_MoveToRef(YedAI ai, float angle, float dis) {
        Object[] ps = ai.getOwnerparams();
        if (ps == null || ps.length < 1 || ps[Entity.AiParam.entity.value] == null || !(ps[Entity.AiParam.entity.value] instanceof Entity)) {
            logger.error("跑ai Sa_MoveToRef,参数没设置对 需要一个entity");
            return;
        }
        Entity op = (Entity) ai.getOwnerparams()[Entity.AiParam.entity.value];

        try {
            Entity realop = op;
            if (op instanceof SkillMagic) {
                realop = ((SkillMagic) op).gainOwner();
            }
            Position dir = Utils.getDir(angle);
            realop.setDir(dir);
            Position pos = Utils.getPosByDir(realop.gainCurPos(), dir, dis);
            logger.warn(String.format("ai action Sa_MoveToRef op:%s angle:%.2f dis:%.2f to pos:%s", op, angle, dis, pos.toString()));
            op.entityScript().runToPosition(op, pos);
        } catch (Exception e) {
            logger.fatal(e, e);
        }
    }

    /**
     * 跟着当前攻击对象走
     *
     * @param ai
     * @param dis 可以相差多远(单位米)
     */
    private void Sa_FlowTarget(YedAI ai, float dis) {
        Object[] ps = ai.getOwnerparams();
        if (ps == null || ps.length < 1 || ps[Entity.AiParam.entity.value] == null || !(ps[Entity.AiParam.entity.value] instanceof Entity)) {
            logger.error("跑ai Sa_FlowTarget,参数没设置对 需要一个entity");
            return;
        }
        Entity op = (Entity) ai.getOwnerparams()[Entity.AiParam.entity.value];

        try {
            MapObject map = MapManager.getInstance().getMap(op.gainMapId());
            if (map == null) {
                logger.error("Sa_FlowTarget 释放者的地图信息没了...");
                return;
            }
            // 有些在用这个,这个找不到就换仇恨列表里的0
            Fighter target = op.gainCurAttackTarget(map);
            if (target == null || target.isDie()) {
                target = op.gainFirstHatredTarget();
            }
            logger.warn(String.format("ai action Sa_FlowTarget op:%s target:%s dis:%.2f", op, target == null ? "null" : target.toString(), dis));
            if (target == null) {
                return;
            }
            op.entityScript().runToTarget(op, target, dis);
        } catch (Exception e) {
            logger.fatal(e, e);
        }

    }

    private void Sa_talk(YedAI ai, int stringid) {
        MapMessage.ResShowMonsterPop.Builder bmsg = MapMessage.ResShowMonsterPop.newBuilder();

        Entity op = (Entity) ai.getOwnerparams()[Entity.AiParam.entity.value];
        logger.warn(String.format("ai action Sa_talk stringid:%s ", stringid));

        bmsg.setMonsterid(op.getId());
        bmsg.setTalkid(stringid);

        // 目前是直接发到周围玩家那里
        MessageUtils.send_to_roundPlayer(op, MapMessage.ResShowMonsterPop.MsgID.eMsgID_VALUE, bmsg.build().toByteArray());
    }

    /**
     * 设置当前物体为不显示
     *
     * @param ai
     * @param see
     */
    private void Sa_setPlayerCanSee(YedAI ai, boolean see) {
        Entity op = (Entity) ai.getOwnerparams()[Entity.AiParam.entity.value];
        op.seShow(see);
        logger.warn(String.format("ai action Sa_setPlayerCanSee stringid:%s  see=" + see, op.getName()));
    }

    private void Sa_addbufftome(YedAI ai, int buffid) {
        Object[] ps = ai.getOwnerparams();
        if (ps == null || ps.length < 1 || ps[Entity.AiParam.entity.value] == null || !(ps[Entity.AiParam.entity.value] instanceof Entity)) {
            logger.error("跑ai Sa_addbufftome,参数没设置对 需要一个entity");
            return;
        }
        Entity op = (Entity) ai.getOwnerparams()[Entity.AiParam.entity.value];

        try {
            BuffManager.getInstance().deal().onAddBuff(op, op, buffid);
            logger.warn("跑ai Sa_addbufftome, op:%s buffid:%d", op, buffid);

        } catch (Exception e) {
            logger.fatal(e, e);
        }
    }

    private void Sa_setCamp(YedAI ai, int camp) {
        Object[] ps = ai.getOwnerparams();
        if (ps == null || ps.length < 1 || ps[Entity.AiParam.entity.value] == null || !(ps[Entity.AiParam.entity.value] instanceof Entity)) {
            logger.error("跑ai Sa_setCamp,参数没设置对 需要一个entity");
            return;
        }
        Entity op = (Entity) ai.getOwnerparams()[Entity.AiParam.entity.value];

        try {
            op.setCamp(camp, true);
            logger.warn("跑ai Sa_setCamp, op:%s camp:%d", op, camp);

        } catch (Exception e) {
            logger.fatal(e, e);
        }

    }

    /**
     * 播放剧情
     *
     * @param ai
     */
    private void Sa_playCine(YedAI ai, int cinematicID) {
        try {
            Entity op = (Entity) ai.getOwnerparams()[Entity.AiParam.entity.value];
            MapObject map = MapManager.getInstance().getMap(op.gainMapId());
            MapMessage.ResPlayCinematic.Builder msg = MapMessage.ResPlayCinematic.newBuilder();
            msg.setCinematicID(cinematicID);
            MessageUtils.send_to_map(map, MapMessage.ResPlayCinematic.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            logger.info(String.format("copyai info Sa_playCine cinematicID:%d", cinematicID));
        } catch (Exception e) {
            logger.fatal(e, e);
        }
    }

    private void Sa_setCopyAi(YedAI ai, String ainame) {
        try {
            Entity op = (Entity) ai.getOwnerparams()[Entity.AiParam.entity.value];
            MapObject map = MapManager.getInstance().getMap(op.gainMapId());
//            CopyMapManager.getInstance().changeCopyMapYedAi(ainame, map);
        } catch (Exception e) {
            logger.fatal(e, e);
        }

    }

    /**
     * 开关动态阻挡
     *
     * @param ai
     * @param blockname
     * @param bopen
     */
    private void Sa_setBlockDoor(YedAI ai, String blockname, boolean bopen) {
        try {
            Entity op = (Entity) ai.getOwnerparams()[Entity.AiParam.entity.value];
            MapObject map = MapManager.getInstance().getMap(op.gainMapId());

            MapManager.getInstance().setBlockDoor(map, blockname, bopen);

            logger.warn(String.format("copyai info Sa_setBlockDoor blockname:%s bopen:%s", blockname, bopen));
        } catch (Exception e) {
            logger.fatal(e, e);
        }

    }

    /**
     * 开关动态阻挡
     *
     * @param ai
     */
    private boolean Sa_IsMonsterDieAll(YedAI ai, int monsterid) {
        try {
            Entity op = (Entity) ai.getOwnerparams()[Entity.AiParam.entity.value];
            MapObject map = MapManager.getInstance().getMap(op.gainMapId());
            if (map == null) {
                return false;
            }
            
            Collection<Monster> mlist = map.getMonsters().values();
            boolean isdieall = true;
            for (Monster m : mlist) {
                if (m.getModelId() == monsterid) {
                    if (!m.isDie()) {
                        isdieall = false;
                        break;
                    }
                }
            }
            logger.info(String.format("copyai info Sa_IsMonsterDieAll monsterid:%d isdieall:%s", monsterid, isdieall ? "true" : "false"));
            return isdieall;
        } catch (Exception e) {
            logger.fatal(e, e);
        }
        return false;
    }

    private void Sa_createMonster(YedAI ai, int configid, String ainame, float x, float y, boolean busemaker, float angle) {
        try {
            Entity op = (Entity) ai.getOwnerparams()[Entity.AiParam.entity.value];
            MapObject map = MapManager.getInstance().getMap(op.gainMapId());

            Position pos;
            if (x == 99999) {
                pos = map.getBrithPos();
            } else {
                pos = new Position(x, y);
            }
            Monster nm = MonsterManager.getInstance().createMonster(map, pos, configid, Utils.getDir(angle), -3);
            if (nm != null) {
                if (busemaker) {
                    nm.setMakerId(op.getMakerId());
                } else {
                    nm.setMakerId(op.getId());
                }
                if (!ainame.equals("")) {
                    Manager.monsterManager.ai().setYedAi(nm, ainame);
                }
            }
            logger.warn(String.format("copyai info Sa_createMonster nm:%s ai:%s config:%d", nm == null ? "null" : nm.toString(), ainame, configid));
        } catch (Exception e) {
            logger.fatal(e, e);
        }

    }

    /**
     * 创建一个机器人
     *
     * @param ai
     */
    private void Sa_createRobot(YedAI ai, int configid, String ainame, float x, float y, boolean busemaker, float angle) {
        try {
            Entity op = (Entity) ai.getOwnerparams()[Entity.AiParam.entity.value];
            MapObject map = MapManager.getInstance().getMap(op.gainMapId());

            Robot ro = RobotManager.getInstance().deal().OnMakeByRobotConfig(configid);
            if (ro != null) {

                if (busemaker) {
                    ro.setMakerId(op.getMakerId());
                } else {
                    ro.setMakerId(op.getId());
                }

                MapGps gps = map.getGps(new Position(0, 0));
                MapGpsUtil.CopyGPS(gps, ro.getCurGps());
                if (x == 99999) {
                    ro.changeCurPos(map.getBrithPos());
                } else {
                    ro.changeCurPos(new Position(x, y));
                }
                Position dir = Utils.getDir(angle);
                ro.setDir(dir);
                Manager.mapManager.manager().onEnterMap(ro);

                ro.setAi(RobotAi.CUSTOM);

                if (!ainame.equals("")) {
                    YedAI yai = new YedAI(RobotManager.getInstance().yedAi());
                    yai.Load(ainame, false);
                    ro.changeRunningAi(yai);
                }
            }

            logger.warn(String.format("copyai info Sa_createRobot ro:%s ai:%s config:%d", ro == null ? "null" : ro.toString(), ainame, configid));
        } catch (Exception e) {
            logger.fatal(e, e);
        }
    }

    private void Sa_createCopyMonster(YedAI ai, int copyid, int gateno, String ainame) {
        try {
            Entity op = (Entity) ai.getOwnerparams()[Entity.AiParam.entity.value];
            MapObject map = MapManager.getInstance().getMap(op.gainMapId());
            List<Monster> rolist = Manager.copyMapManager.manager().copyMapRefreshMonster(map, copyid, gateno);
            if (rolist != null && rolist.size() > 0 && !ainame.equals("")) {
                for (Monster ro : rolist) {
                    YedAI yai = new YedAI(RobotManager.getInstance().yedAi());
                    yai.Load(ainame, false);
                    ro.changeRunningAi(yai);
                }
            }

            logger.warn(String.format("copyai info Sa_createCopyMonster copyid:%d ainame:%s gateno:%d", copyid, ainame, gateno));
        } catch (Exception e) {
            logger.fatal(e, e);
        }
    }

    private void Sa_talktarget(YedAI ai, int robotid, int stringid) {
        try {
            Entity op = (Entity) ai.getOwnerparams()[Entity.AiParam.entity.value];
            MapObject map = MapManager.getInstance().getMap(op.gainMapId());
            MapMessage.ResShowMonsterPop.Builder bmsg = MapMessage.ResShowMonsterPop.newBuilder();
            Collection<Robot> robots = map.getRobots().values();
            Entity opt = null;
            for (Robot r : robots) {
                if (r.getModelId() == robotid) {
                    opt = r;
                    break;
                }
            }
            if (opt == null) {
                for (Monster r : map.getMonsters().values()) {
                    if (r.getModelId() == robotid) {
                        opt = r;
                        break;
                    }
                }
            }

            logger.warn(String.format("ai action Sa_talktarget stringid:%s op:%s", stringid, opt == null ? "null" : opt.toString()));
            if (opt == null) {
                return;
            }
            bmsg.setMonsterid(opt.getId());
            bmsg.setTalkid(stringid);

            // 目前是直接发到周围玩家那里
            MessageUtils.send_to_roundPlayer(opt, MapMessage.ResShowMonsterPop.MsgID.eMsgID_VALUE, bmsg.build().toByteArray());
        } catch (Exception e) {
            logger.fatal(e, e);
        }
    }

    private void Sa_finishStep(YedAI ai, int step) {
        try {
            Entity op = (Entity) ai.getOwnerparams()[Entity.AiParam.entity.value];
            MapObject map = MapManager.getInstance().getMap(op.gainMapId());
//            IScript is = ScriptManager.getInstance().GetScriptClass(map.getScriptId());
//            if (is instanceof ICopyMapAiControl) {
//                ICopyMapAiControl cac = (ICopyMapAiControl) is;
//                cac.OnFinishStep(context, map, step);
//            }
//            logger.warn(String.format("ai action Sa_finishStep step:%d is instanceof ICopyMapAiControl:%s", step, is instanceof ICopyMapAiControl ? "true" : false));
        } catch (Exception e) {
            logger.fatal(e, e);
        }
    }

    /**
     * 副本的进度设置，
     *
     * @Param ai 副本的AI逻辑
     * @Param bigStep 副本的大阶段编号
     * @Param smallStep 副本的小阶段编号
     */
    private void Sa_setCloneStep(YedAI ai, int bigStep, int smallStep) {
        try {
            Entity op = (Entity) ai.getOwnerparams()[Entity.AiParam.entity.value];
            MapObject map = MapManager.getInstance().getMap(op.gainMapId());
//            IScript is = ScriptManager.getInstance().GetScriptClass(map.getScriptId());
//            if (is instanceof ICopyMapAiControl) {
//                ICopyMapAiControl cac = (ICopyMapAiControl) is;
//                cac.onCloneStep(context, bigStep, smallStep);
//            }
//            logger.warn(String.format("ai action Sa_finishStep bigStep %d ,step:%d is instanceof ICopyMapAiControl:%s", bigStep, smallStep, is instanceof ICopyMapAiControl ? "true" : false));
        } catch (Exception e) {
            logger.fatal(e, e);
        }
    }

    private void Sa_finishCopy(YedAI ai) {
        try {
            Entity op = (Entity) ai.getOwnerparams()[Entity.AiParam.entity.value];
            MapObject map = MapManager.getInstance().getMap(op.gainMapId());
//            IScript is = ScriptManager.getInstance().GetScriptClass(map.getScriptId());
//            if (is instanceof ICopyMapAiControl) {
//                ICopyMapAiControl cac = (ICopyMapAiControl) is;
//                cac.OnFinishCopy(context, map);
//            }
//            logger.warn(String.format("ai action Sa_finishCopy is instanceof ICopyMapAiControl:%s", is instanceof ICopyMapAiControl ? "true" : false));
        } catch (Exception e) {
            logger.fatal(e, e);
        }
    }

    private boolean Sa_IsMakerArrivedPos(YedAI ai, float x, float y, float dis) {

        Entity op = (Entity) ai.getOwnerparams()[Entity.AiParam.entity.value];
        try {
            MapObject map = MapManager.getInstance().getMap(op.gainMapId());
            Player p = map.getPlayer(op.getMakerId());
            if (p != null) {

                float curdis = Utils.getDistance(p.gainCurPos(), new Position(x, y));
                logger.info(String.format("ai condition Sa_isMakerArrivedPos p:%s x:%.2f y:%.2f curdis:%.2f dis:%.2f op.pos:%s", p, x, y, curdis, dis, p.gainCurPos()));
                return curdis < dis;
            }
            logger.info(String.format("ai condition Sa_isMakerArrivedPos p:%s x:%.2f y:%.2f dis:%.2f", null, x, y, dis));
        } catch (Exception e) {
            logger.fatal(e, e);
        }
        return false;
    }

    private boolean Sa_IsInFight(YedAI ai) {

        Entity op = (Entity) ai.getOwnerparams()[Entity.AiParam.entity.value];
        try {

            logger.info(String.format("ai condition Sa_IsInFight p:%s isin:%s", op, op.isInBattle() ? "true" : "false"));
            return op.isInBattle();
        } catch (Exception e) {
            logger.fatal(e, e);
        }
        return false;
    }

    private void Sa_StopMove(YedAI ai) {

        Entity op = (Entity) ai.getOwnerparams()[Entity.AiParam.entity.value];
        try {
            op.stopMove();
            logger.info(String.format("ai condition Sa_StopMove p:%s isin:%s", op, op.isInBattle() ? "true" : "false"));
        } catch (Exception e) {
            logger.fatal(e, e);
        }
    }

    private boolean Sa_IsHpPerIn(YedAI ai, float min, float max) {
        Entity op = (Entity) ai.getOwnerparams()[Entity.AiParam.entity.value];
        try {
            min *= 100;
            max *= 100;
            int p = op.checkHpPercent();

            boolean isMin = Float.compare(p, min) >= 0;
            boolean isMax = Float.compare(p, max) <= 0;
            boolean ret = (isMin && isMax);
            logger.info(String.format("ai condition Sa_IsHpPerIn op:%s p:%d isin:%s min:%.2f max:%.2f", op, p, ret ? "true" : "false", min, max));
            return ret;
        } catch (Exception e) {
            logger.fatal(e, e);
        }
        return false;
    }

    private void Sa_removebufftome(YedAI ai, int buffid) {
        Object[] ps = ai.getOwnerparams();
        if (ps == null || ps.length < 1 || ps[Entity.AiParam.entity.value] == null || !(ps[Entity.AiParam.entity.value] instanceof Entity)) {
            logger.error("跑ai Sa_addbufftome,参数没设置对 需要一个entity");
            return;
        }
        Entity op = (Entity) ai.getOwnerparams()[Entity.AiParam.entity.value];

        try {
            BuffManager.getInstance().deal().onRemoveBuff(op, buffid);
            logger.warn(String.format("跑ai Sa_removebufftome, op:%s buffid:%d", op, buffid));

        } catch (Exception e) {
            logger.fatal(e, e);
        }
    }

    private void Sa_notifyAllPlayer(YedAI ai, int type, String content) {
        Object[] ps = ai.getOwnerparams();
        if (ps == null || ps.length < 1 || ps[Entity.AiParam.entity.value] == null || !(ps[Entity.AiParam.entity.value] instanceof Entity)) {
            logger.error("跑ai Sa_notifyAllPlayer,参数没设置对 需要一个entity");
            return;
        }

        try {
            Entity op = (Entity) ai.getOwnerparams()[Entity.AiParam.entity.value];
            MapObject map = MapManager.getInstance().getMap(op.gainMapId());
            for (Player p : map.getPlayers().values()) {
                MessageUtils.notify_player(p, Notify.values()[type], content);
            }

        } catch (Exception e) {
            logger.fatal(e, e);
        }
    }

    private void Sa_setPlayerRebirthPoint(YedAI ai, int camp, int idx, float x, float y) {
        Object[] ps = ai.getOwnerparams();
        if (ps == null || ps.length < 1 || ps[Entity.AiParam.entity.value] == null || !(ps[Entity.AiParam.entity.value] instanceof Entity)) {
            logger.error("跑ai Sa_setPlayerRebirthPoint,参数没设置对 需要一个entity");
            return;
        }

        try {
            Entity op = (Entity) ai.getOwnerparams()[Entity.AiParam.entity.value];
            MapObject map = MapManager.getInstance().getMap(op.gainMapId());
            List<Position> bs = map.getBriths();
//            if (!bs.containsKey(camp)) {
//                logger.error("跑ai Sa_setPlayerRebirthPoint,参数没对,camp(" + camp + ")找不到合适的");
//                return;
//            }
//            List<BrithPoint> bl = bs.get(camp);
//            if (bl.size() <= idx) {
//                logger.error(String.format("跑ai Sa_setPlayerRebirthPoint,参数没对,idx size没对%d", idx));
//                return;
//            }
//            bl.get(idx).setX(x);
//            bl.get(idx).setY(y);
            bs.get(idx).setX(x);
            bs.get(idx).setY(y);
            logger.warn(String.format("跑ai Sa_setPlayerRebirthPoint, 设置为新的位置%.2f %.2f", x, y));
        } catch (Exception e) {
            logger.fatal(e, e);
        }
    }

    private void Sa_setPlayerRelivePoint(YedAI ai, int camp, float x, float y) {
        Object[] ps = ai.getOwnerparams();
        if (ps == null || ps.length < 1 || ps[Entity.AiParam.entity.value] == null || !(ps[Entity.AiParam.entity.value] instanceof Entity)) {
            logger.error("跑ai Sa_setPlayerRelivePoint,参数没设置对 需要一个entity");
            return;
        }

        try {
            Entity op = (Entity) ai.getOwnerparams()[Entity.AiParam.entity.value];
            MapObject map = MapManager.getInstance().getMap(op.gainMapId());
//            List<BrithPoint> bl = map.getRelives();
            List<Position> b1 = map.getRelives();
//            if (bl.size() <= camp) {
//                logger.error(String.format("跑ai Sa_setPlayerRelivePoint,参数没对,idx size没对%d", camp));
//                return;
//            }
//            bl.get(camp).setX(x);
//            bl.get(camp).setY(y);
            b1.get(0).setX(x);
            b1.get(0).setY(y);
            logger.warn(String.format("跑ai Sa_setPlayerRelivePoint, 设置为新的位置%.2f %.2f", x, y));
        } catch (Exception e) {
            logger.fatal(e, e);
        }
    }

    private void Sa_removeMonster(YedAI ai, int monsterid) {
        Object[] ps = ai.getOwnerparams();
        if (ps == null || ps.length < 1 || ps[Entity.AiParam.entity.value] == null || !(ps[Entity.AiParam.entity.value] instanceof Entity)) {
            logger.error("跑ai Sa_removeMonster,参数没设置对 需要一个entity");
            return;
        }

        try {
            Entity op = (Entity) ai.getOwnerparams()[Entity.AiParam.entity.value];
            MapObject map = MapManager.getInstance().getMap(op.gainMapId());
            ArrayList<Monster> ml = new ArrayList<>();
            for (Monster m : map.getMonsters().values()) {
                if (m.getModelId() == monsterid) {
                    ml.add(m);
                }
            }
            for (Monster m : ml) {
                Manager.mapManager.manager().onQuitMap(map, m, true);
            }
            logger.warn(String.format("跑ai Sa_removeMonster, 清理掉了所有id为%d的怪", monsterid));
        } catch (Exception e) {
            logger.fatal(e, e);
        }

    }

    //怪物是否跑动
    private void Sa_setIsRun(YedAI ai, boolean brun) {
        try {
            Entity op = (Entity) ai.getOwnerparams()[Entity.AiParam.entity.value];
            if (brun) {
                op.addState(EntityState.Run);
            } else {
                op.removeSate(EntityState.Run);
            }
            MapMessage.ResUpdateMoveState.Builder msg = MapMessage.ResUpdateMoveState.newBuilder();
            msg.setId(op.getId());
            msg.setIsRun(EntityState.Run.compare(op.getState()));
            MessageUtils.send_to_roundPlayer(op, MapMessage.ResUpdateMoveState.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            logger.warn(String.format("跑ai Sa_setIsRun, %s 设置是否跑动作 %b", op, msg.getIsRun()));
        } catch (Exception e) {
            logger.fatal(e, e);
        }
    }

    //怪物检查当前地图的怪物数量
    private int Sa_getMonstercount(YedAI ai, int monsterid) {
        Entity op = (Entity) ai.getOwnerparams()[Entity.AiParam.entity.value];
        try {
            int ret = 0;
            MapObject map = MapManager.getInstance().getMap(op.gainMapId());
            for (Monster m : map.getMonsters().values()) {
                if (m.getModelId() == monsterid) {
                    ret++;
                }
            }
            logger.warn(String.format("copyai info Sa_getMonstercount ret:%d monsterid:%d", ret, monsterid));
            return ret;
        } catch (Exception e) {
            logger.fatal(e, e);
        }
        return 0;
    }

    /**
     * 设置地图上两个怪物是共生体, 此函数的调用必须是两个怪物都已经创建的情况下才能产生，也只支持两个玩家
     *
     * @param ai 执行体
     * @param monsterid 主怪物ID
     * @param otherMonsterId 次怪物ID
     * @return 返回是否成功创建了关系
     */
    private boolean Sa_setSameMonster(YedAI ai, int monsterid, int otherMonsterId) {
        Entity op = (Entity) ai.getOwnerparams()[Entity.AiParam.entity.value];
        try {
            Monster mainMonster = null;
            Monster otherMonster = null;
            MapObject map = MapManager.getInstance().getMap(op.gainMapId());
            for (Monster m : map.getMonsters().values()) {
                if (m.getModelId() == monsterid) {
                    mainMonster = m;
                }
                if (m.getModelId() == otherMonsterId) {
                    otherMonster = m;
                }
            }

            if (mainMonster == null) {
                return false;
            }

            if (otherMonster == null) {
                return false;
            }

            mainMonster.setLineOtherMonsterId(otherMonster.getId());
            otherMonster.setLineOtherMonsterId(mainMonster.getId());
            logger.warn(String.format("copyai info Sa_setSameMonster ret:%s linK:%s", mainMonster.nameIdString(), otherMonster.nameIdString()));
            return true;
        } catch (Exception e) {
            logger.fatal(e, e);
        }
        return false;
    }

    private boolean Sa_isPlayerDieAll(YedAI ai) {
        try {
            Entity op = (Entity) ai.getOwnerparams()[Entity.AiParam.entity.value];
            MapObject map = MapManager.getInstance().getMap(op.gainMapId());
            boolean isHavePlayerLive = false;
            for (Player player : map.getPlayers().values()) {
                if (!player.isDie()) {
                    isHavePlayerLive = true;
                }
            }
            logger.info(String.format("ai condition Sa_isPlayerDieAll op:%s isin:%s", map.getName(), !isHavePlayerLive ? "true" : "false"));
            return !isHavePlayerLive;
        } catch (Exception e) {
            logger.fatal(e, e);
        }
        return false;
    }

    @Override
    public boolean execAiMethod(YedAI ai, String methodName, Object[] args) {
        if(methodName==null || methodName.equals("")){
            return false;
        }
        switch (methodName) {
            case "isPlayerDieAll": {
                ai.setFuncRet(Sa_isPlayerDieAll(ai));
                break;
            }
            case "setSameMonster": {
                ai.setFuncRet(Sa_setSameMonster(ai,Double.valueOf(args[0].toString()).intValue(),Double.valueOf(args[1].toString()).intValue()));
                break;
            }case "getMonstercount": {
                ai.setFuncRet(Sa_getMonstercount(ai,Double.valueOf(args[0].toString()).intValue()));
                break;
            }
            case "setIsRun": {
                Sa_setIsRun(ai,Boolean.getBoolean(args[0].toString()));
                break;
            }case "removeMonster": {
                Sa_removeMonster(ai,Double.valueOf(args[0].toString()).intValue());
                break;
            }case "setPlayerRelivePoint": {
                Sa_setPlayerRelivePoint(ai,Double.valueOf(args[0].toString()).intValue(),Double.valueOf(args[1].toString()).intValue()
                        ,Double.valueOf(args[2].toString()).intValue());
                break;
            }
            case "setPlayerRebirthPoint": {
                Sa_setPlayerRebirthPoint(ai,Double.valueOf(args[0].toString()).intValue(),Double.valueOf(args[1].toString()).intValue()
                        ,Double.valueOf(args[2].toString()).floatValue(),Double.valueOf(args[3].toString()).floatValue());
                break;
            }
            case "notifyAllPlayer": {
                Sa_notifyAllPlayer(ai,Double.valueOf(args[0].toString()).intValue(),args[1].toString());
                break;
            }case "removebufftome": {
                Sa_removebufftome(ai,Double.valueOf(args[0].toString()).intValue());
                break;
            }case "IsHpPerIn": {
                ai.setFuncRet(Sa_IsHpPerIn(ai,Double.valueOf(args[0].toString()).floatValue(),Double.valueOf(args[1].toString()).floatValue()));
                break;
            }case "StopMove": {
                Sa_StopMove(ai);
                break;
            }case "IsInFight": {
                ai.setFuncRet(Sa_IsInFight(ai));
                break;
            }
            case "IsMakerArrivedPos": {
                ai.setFuncRet(Sa_IsMakerArrivedPos(ai,Double.valueOf(args[0].toString()).floatValue(),Double.valueOf(args[1].toString()).floatValue()
                        ,Double.valueOf(args[2].toString()).floatValue()));
                break;
            }
            case "finishCopy": {
                Sa_finishCopy(ai);
                break;
            }
            case "setCloneStep": {
                Sa_setCloneStep(ai,Double.valueOf(args[0].toString()).intValue(),Double.valueOf(args[1].toString()).intValue());
                break;
            }
            case "finishStep": {
                Sa_finishStep(ai,Double.valueOf(args[0].toString()).intValue());
                break;
            }
            case "createCopyMonster": {
                Sa_createCopyMonster(ai,Double.valueOf(args[0].toString()).intValue(),Double.valueOf(args[1].toString()).intValue()
                        ,args[2].toString());
                break;
            }
            case "createRobot": {
                Sa_createRobot(ai,Double.valueOf(args[0].toString()).intValue(),args[1].toString(),Double.valueOf(args[2].toString()).floatValue()
                        ,Double.valueOf(args[3].toString()).floatValue(),Boolean.getBoolean(args[4].toString()),Double.valueOf(args[5].toString()).floatValue());
                break;
            }
            case "createMonster": {
                Sa_createMonster(ai,Double.valueOf(args[0].toString()).intValue(),args[1].toString(),Double.valueOf(args[2].toString()).floatValue()
                        ,Double.valueOf(args[3].toString()).floatValue(),Boolean.getBoolean(args[4].toString()),Double.valueOf(args[5].toString()).floatValue());
                break;
            }
            case "IsMonsterDieAll": {
                ai.setFuncRet(Sa_IsMonsterDieAll(ai,Double.valueOf(args[0].toString()).intValue()));
                break;
            }
            case "setBlockDoor": {
                Sa_setBlockDoor(ai,args[0].toString(),Boolean.getBoolean(args[1].toString()));
                break;
            }
            case "setCopyAi": {
                Sa_setCopyAi(ai,args[0].toString());
                break;
            }
            case "playCine": {
                Sa_playCine(ai,Double.valueOf(args[0].toString()).intValue());
                break;
            }
            case "setCamp": {
                Sa_setCamp(ai,Double.valueOf(args[0].toString()).intValue());
                break;
            }
            case "addbufftome": {
                Sa_addbufftome(ai,Double.valueOf(args[0].toString()).intValue());
                break;
            }
            case "setPlayerCanSee": {
                Sa_setPlayerCanSee(ai,Boolean.getBoolean(args[0].toString()));
                break;
            }
            case "talktarget": {
                Sa_talktarget(ai,Double.valueOf(args[0].toString()).intValue(),Double.valueOf(args[1].toString()).intValue());
                break;
            }

            case "talk": {
                Sa_talk(ai,Double.valueOf(args[0].toString()).intValue());
                break;
            }
            case "FlowTarget": {
                Sa_FlowTarget(ai,Double.valueOf(args[0].toString()).floatValue());
                break;
            }
            case "MoveToRef": {
                Sa_MoveToRef(ai,Double.valueOf(args[0].toString()).floatValue(),Double.valueOf(args[0].toString()).floatValue());
                break;
            }
            case "MoveToRefTarget": {
                Sa_MoveToRefTarget(ai,Double.valueOf(args[0].toString()).floatValue());
                break;
            }

            case "MoveTo": {
                Sa_MoveTo(ai,Double.valueOf(args[0].toString()).floatValue(),Double.valueOf(args[0].toString()).floatValue());
                break;
            }
            case "IsArrivedPos": {
                ai.setFuncRet(Sa_IsArrivedPos(ai,Double.valueOf(args[0].toString()).floatValue(),Double.valueOf(args[0].toString()).floatValue()
                        ,Double.valueOf(args[0].toString()).floatValue()));
                break;
            }
            case "SetSpeed": {
                Sa_SetSpeed(ai,Double.valueOf(args[0].toString()).intValue());
                break;
            }

        }
        return true;
    }
}
