package common.buff.script;

import com.data.CfgManager;
import com.data.bean.Cfg_Buff_Bean;
import com.game.buff.script.IBuffBehavior;
import com.game.buff.structs.Buff;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapUtils;
import com.game.monster.structs.Monster;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import game.core.script.IScript;

import java.util.List;

/**
 * @author zenghai
 */
public class SuperPveBuff implements IScript, IBuffBehavior {

    @Override
    public int add(Buff buff, Fighter source, Fighter target) {
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
    public int remove(Buff buff, Fighter owner) {
        return 0;
    }

    @Override
    public int action(Buff buff, Fighter attacker, Fighter owner) {
        if (owner.isDie()) {
            return 0;
        }

        Cfg_Buff_Bean config = CfgManager.getCfg_Buff_Container().getValueByKey(buff.getBuffId());

        MapObject map = Manager.mapManager.getMap(owner.gainMapId());
        List<Fighter> list = MapUtils.getFighter(map, owner.gainCurPos());
        for (Fighter beAttacker : list) {
            if (beAttacker.getCamp() == owner.getCamp() || beAttacker.getCamp() == -1) {
                continue;
            }
            if (!(beAttacker instanceof Monster)) {
                continue;
            }
            beAttacker.setCurHp(beAttacker.getCurHp() - config.getParam1());
            beAttacker.beAttack(owner, null, 0, config.getParam1());
            if (beAttacker.getCurHp() <= 0) {
                beAttacker.setCurHp(0);
                beAttacker.doDie(owner);
                MapUtils.sendDead(owner, beAttacker);
            }
            beAttacker.onHpChange(null);
            MapUtils.sendBuffHp(beAttacker, beAttacker, config.getParam1());
        }

        return 1;
    }

    @Override
    public int getId() {
        return ScriptEnum.SuperPveBuff;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }
}
