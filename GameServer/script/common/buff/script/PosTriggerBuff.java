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
import com.game.player.manager.PlayerManager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.skill.structs.Skill;
import com.game.structs.Entity;
import com.game.structs.Fighter;
import com.game.team.manager.TeamManager;
import com.game.team.structs.TeamInfo;
import com.game.utils.Utils;
import game.core.map.Position;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;

/**
 * @author huhu 位置变动会触发进入和退出事件 除了移动主动触发的,其他都只影响自己 param1 监听半径,单位米
 * condi:[LOGIC_AREAIN]_[ACTION_TYPE]_[ID]
 */
public final class PosTriggerBuff implements IScript, IBuffBehavior {

    final Logger log = LogManager.getLogger(PosTriggerBuff.class);

    @Override
    public int add(Buff buff,Fighter src, Fighter target) {
        action(buff, src, target);
        return 0;
    }

    @Override
    public void overlap(Buff buff, Fighter owner) {
    }

    @Override
    public int timeout(Buff buff,Fighter target) {
        return 0;
    }

    @Override
    public int remove(Buff buff,Fighter target) {
        return 0;
    }

    @Override
    public int action(Buff buff, Fighter attacker, Fighter owner) {

        if (!(owner instanceof Player)) {
            return 0;
        }
        Player tF = (Player)owner;
        Cfg_Buff_Bean config = CfgManager.getCfg_Buff_Container().getValueByKey(buff.getBuffId());

        // 根据action配置产生对应action
        if (config.getCondi().size() < 1) {
            return 0;
        }
        // 先看触发条件
        for (ReadArray<Integer> params : config.getCondi().getValuees()) {
            if (params.size() < 3) {
                continue;
            }
            if (params.get(0) == BuffDefine.BuffExParamType.TRIGGER_CODI.value()) {
                int ret = checkCondi(buff, tF, params);
                if (ret != 0) {
//                    log.info(String.format("[%d] buff在触发的时候不满足触发条件 ret:%d param[1]:%d param[2]%d", getBuffId(), ret, params.get(1), params.get(2)));
                    return 0;
                }
            }
        }
        for (ReadArray<Integer> params : config.getCondi().getValuees()) {
            if (params.size() < 3) {
                continue;
            }

            ArrayList<Player> targetes = null;

            if (params.get(0) == BuffDefine.BuffExParamType.LOGIC_AREAIN.value()) {
                targetes = findTargetes(tF, config.getParam1(), true);
            }

            if (params.get(0) == BuffDefine.BuffExParamType.LOGIC_AREAOUT.value()) {
                targetes = findTargetes(tF, config.getParam1(), false);
            }
            if (targetes == null) {
                continue;
            }
            if (targetes.size() > 0) {
                // 每秒过来的都自己给自己加就是了
                doAction(buff, tF, tF, params);
            }
        }
        return 0;
    }

    public int checkCondi(Buff buff, Entity src, ReadArray<Integer> params) {
        // 不同的action做不同的事情
        int actiontype = params.get(1);
        if (actiontype == BuffDefine.CondiType.EXIST_BUFF.value()) {
            for (int idx = 2; idx < params.size(); ++idx) {
                int buffid = params.get(idx);
                if (buffid == 0) {
                    continue;
                }
                if (src.getBuffbyId(buffid) == null) {
                    return buffid;
                }
            }
        } else {
            log.error(String.format("buff %d 中有无法解析的action类型:%d", buff == null ? 0 : buff.getBuffId(), actiontype));
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

    /**
     * 查找src的队员,以radius为半径,且同地图
     *
     * @param src
     * @param radius
     * @param isIn 找半径内还是半径外的
     * @return
     */
    public ArrayList<Player> findTargetes(Player src, int radius, boolean isIn) {
        if (src.getTeamId() <= 0) {
            return null;
        }
        TeamInfo team = TeamManager.getInstance().getTeam(src.getTeamId());
        if (team == null) {
            return null;
        }

        ArrayList<Player> teams = new ArrayList<>();
        for (long id : team.getMembers()) {
            if (id == src.getId()) {
                continue;
            }
            Player teamer = PlayerManager.getInstance().getPlayerCache(id);
            if (teamer.gainMapId() != src.gainMapId()) {
                continue;
            }

            float dis = Utils.getDistance(src, teamer);
            if ((dis > radius) != isIn) {
                teams.add(teamer);
            }
        }
        return teams;
    }

    @Override
    public int getId() {
        return ScriptEnum.PosTriggerBuff;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }
}
