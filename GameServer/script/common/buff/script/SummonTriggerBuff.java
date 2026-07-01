package common.buff.script;

import com.data.CfgManager;
import com.data.bean.Cfg_Buff_Bean;
import com.data.bean.Cfg_Skill_Bean;
import com.data.struct.ReadArray;
import com.game.buff.script.IBuffBehavior;
import com.game.buff.structs.Buff;
import com.game.buff.structs.BuffDefine;
import com.game.buff.timer.AsynBuffAddTimer;
import com.game.buff.timer.AsynBuffDeleteTimer;
import com.game.fight.manager.FightManager;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.script.structs.ScriptEnum;
import com.game.skill.structs.Skill;
import com.game.structs.Fighter;
import game.core.map.Position;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @author huhu 位置变动会触发进入和退出事件 除了移动主动触发的,其他都只影响自己 param1 监听半径,单位米
 * condi:[LOGIC_AREAIN]_[ACTION_TYPE]_[ID]
 */
public final class SummonTriggerBuff implements IScript, IBuffBehavior {

    protected static final Logger log = LogManager.getLogger(SummonTriggerBuff.class);

    @Override
    public int add(Buff buff, Fighter src, Fighter target) {
        action(buff, src, target);
        return 0;
    }

    @Override
    public void overlap(Buff buff, Fighter owner) {
    }

    @Override
    public int timeout(Buff buff, Fighter target) {
        return 0;
    }

    @Override
    public int remove(Buff buff, Fighter target) {
        return 0;
    }

    @Override
    public int action(Buff buff, Fighter attacker, Fighter owner) {
        Cfg_Buff_Bean config = CfgManager.getCfg_Buff_Container().getValueByKey(buff.getBuffId());
        if (config.getCondi().size() < 1) {
            return 0;
        }
        for (ReadArray<Integer> params : config.getCondi().getValuees()) {
            if (params.size() < 3) {
                continue;
            }

            if (params.get(0) == BuffDefine.BuffExParamType.NORMAL_TRIGGER_ACTION.value()) {
                doAction(buff, attacker, owner, params);
            }
        }
        return 0;
    }

    public void doAction(Buff buff, Fighter src, Fighter target, ReadArray<Integer> params) {
        final int idxActiontype = 1;
        final int idxActionParamStart = 2;
        final int idxPosx = 3;
        final int idxPosy = 4;

        // 不同的action做不同的事情
        int actiontype = params.get(idxActiontype);
        if (actiontype == BuffDefine.ACTION_TYPE.ADDBUFF.v) {
            for (int idx = idxActionParamStart; idx < params.size(); ++idx) {
                int buffid = params.get(idx);
                if (buffid == 0) {
                    continue;
                }
                AsynBuffAddTimer buffAddTimer = new AsynBuffAddTimer(src, target, buffid);
                MapObject map = Manager.mapManager.getMap(src.gainMapId());
                map.addTimerEvent(buffAddTimer);
            }
        } else if (actiontype == BuffDefine.ACTION_TYPE.REMOVEBUFF.v) {
            for (int idx = idxActionParamStart; idx < params.size(); ++idx) {
                int buffid = params.get(idx);
                if (buffid == 0) {
                    continue;
                }
                AsynBuffDeleteTimer buffDeleteTimer = new AsynBuffDeleteTimer(target, buffid);
                MapObject map = Manager.mapManager.getMap(src.gainMapId());
                map.addTimerEvent(buffDeleteTimer);
            }
        } else if (actiontype == BuffDefine.ACTION_TYPE.CREATESUMMON.v) {
            int skillid = params.get(idxActionParamStart);
            // 看看后面还有没有更多的参数x,y
            Position pos = null;
            if (params.size() > idxPosy) {
                pos = new Position(params.get(idxPosx), params.get(idxPosy));
            }
            Cfg_Skill_Bean skillBean = CfgManager.getCfg_Skill_Container().getValueByKey(skillid);
            if (skillBean == null) {
                log.error(String.format("当buff[%d]试图创造一个召唤物时,发现对应的技能编号[%d]没对,找不到技能配置", buff.getBuffId(), skillid));
                return;
            }
            Skill skill = new Skill();
            skill.setSkillId(skillid);
            skill.setLevel(1);
            FightManager.getInstance().deal().addSummonImmediately(target, skill, pos, new Position(0, 0));
        } else {
            log.error(String.format("buff %d 中有无法解析的action类型:%d", buff.getBuffId(), actiontype));
        }
    }

    @Override
    public int getId() {
        return ScriptEnum.TriggerSummonBuff;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }
}
