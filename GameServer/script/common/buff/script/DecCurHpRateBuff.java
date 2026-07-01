package common.buff.script;

import com.data.CfgManager;
import com.data.bean.Cfg_Buff_Bean;
import com.game.buff.script.IBuffBehavior;
import com.game.buff.structs.Buff;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapUtils;
import com.game.pet.structs.Pet;
import com.game.script.structs.ScriptEnum;
import com.game.skill.structs.SkillMagic;
import com.game.structs.Fighter;
import game.core.script.IScript;

/**
 * @author zenghai
 */
public class DecCurHpRateBuff implements IScript, IBuffBehavior {

    @Override
    public int add(Buff buff,Fighter source, Fighter target) {
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
    public int remove(Buff buff,Fighter owner) {
        return 0;
    }

    @Override
    public int action(Buff buff,Fighter source, Fighter owner) {
        if (owner.isDie()) {
            return 0;
        }
        MapObject map = Manager.mapManager.getMap(owner.gainMapId());
        if (map == null) {
            return 0;
        }
        Cfg_Buff_Bean config = CfgManager.getCfg_Buff_Container().getValueByKey(buff.getBuffId());
        
        Fighter attacker = MapUtils.getFighter(map, buff.getSourceId());
        if (attacker == null) {
            SkillMagic att = map.getMagic(buff.getSourceId());
            if (att != null) {
                attacker = MapUtils.getFighter(map, att.getOwnerId());
            }
        }
        
        if (attacker instanceof Pet) {
            Pet pet = (Pet) attacker;
            attacker = MapUtils.getFighter(map, pet.getOwnerId());
        }
        
        if (attacker == null) {
            attacker = source;
        }

        long dec = (long) (owner.getCurHp() * (config.getParam1() / 10000f));
        MapUtils.sendBuffHp(attacker, owner, -(int) dec);

        long min = Math.min(owner.getCurHp(), dec);

        owner.setCurHp(owner.getCurHp() - min);
        owner.onHpChange(attacker);

        if (owner.getCurHp() <= 0) {
            owner.doDie(attacker);
            MapUtils.sendDead(attacker, owner);
        }
        return 0;
    }

    @Override
    public int getId() {
        return ScriptEnum.DecCurHpRateBuff;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }
}
