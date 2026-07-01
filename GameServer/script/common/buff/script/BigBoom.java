package common.buff.script;

import com.data.CfgManager;
import com.data.bean.Cfg_Buff_Bean;
import com.game.buff.script.IBuffBehavior;
import com.game.buff.structs.Buff;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapUtils;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * @author huhu param1:abs damage param2:半径
 */
public class BigBoom implements IScript, IBuffBehavior {

    final static Logger log = LogManager.getLogger(BigBoom.class);

    @Override
    public int add(Buff buff,Fighter source, Fighter target) {
        return 0;
    }

    @Override
    public void overlap(Buff buff, Fighter owner) {
    }

    @Override
    public int timeout(Buff buff,Fighter target) {

        Cfg_Buff_Bean config = CfgManager.getCfg_Buff_Container().getValueByKey(buff.getBuffId());

        MapObject map = Manager.mapManager.getMap(target.gainMapId());
        int flag = 0;
        if (target.getClass() == Monster.class) {
            flag = MapUtils.FighterFlag.Monster.v;
        } else if (target.getClass() == Player.class) {
            flag = MapUtils.FighterFlag.Player.v | MapUtils.FighterFlag.Robot.v;
        } else {
            log.error("这个buff的作用目标仅仅限于怪物或者玩家,其他对象暂时不支持 buffID:{}当前对象类型:{}", buff.getBuffId(), target.getClass());
            return -1;
        }
        List<Fighter> list = MapUtils.getFighter(map, target.gainCurPos(), config.getParam2(), flag);
        for (Fighter beAttacker : list) {
            beAttacker.setCurHp(beAttacker.getCurHp() - config.getParam1());
            beAttacker.beAttack(target,null, 0, config.getParam1());
            if (beAttacker.getCurHp() <= 0) {
                beAttacker.setCurHp(0);
                beAttacker.doDie(target);
                MapUtils.sendDead(target, beAttacker);
            }
            beAttacker.onHpChange(null);
            MapUtils.sendBuffHp(beAttacker, beAttacker, config.getParam1());
        }

        return 0;
    }

    @Override
    public int remove(Buff buff,Fighter owner) {
        return 0;
    }

    @Override
    public int action(Buff buff, Fighter attacker, Fighter owner) {
        if (owner.isDie()) {
            return 0;
        }

        return 1;
    }

    @Override
    public int getId() {
        return ScriptEnum.BigBoomBuff;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }
}
