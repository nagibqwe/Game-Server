package common.buff.script;

import com.game.behavior.manager.BehaviorManager;
import com.game.behavior.structs.BehaviorType;
import com.game.buff.script.IBuffBehavior;
import com.game.buff.structs.Buff;
import com.game.fight.structs.FightEnum;
import com.game.manager.Manager;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Entity;
import com.game.structs.Fighter;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 眩晕BUFF
 *
 * @author xuchangming <xysoko@qq.com>
 */
public class DizzinessBuff implements IScript, IBuffBehavior {

    final static Logger log = LogManager.getLogger(DingShengBuff.class);

    @Override
    public int add(Buff buff,Fighter source, Fighter target) {
        //死亡的时候不要取消其它行为
        if (!target.isDie()) {
            //清除所有的行为
            if (BehaviorManager.HasBehavior((Entity) target, BehaviorType.Revive)) {
                log.error(source.getName() + "(" + source.getId() + ") 对 " + target.getName() + "(" + target.getId() + ")施加眩晕技能id=" + buff.getBuffId() + "时， 正在死亡复活状态！");
            } else {
                BehaviorManager.CancelAllBehavior((Entity) target);
            }
        }
//        log.info(source.getName() + "(" + source.getId() + ") 对 " + target.getName() + "(" + target.getId() + ")施加眩晕技能id=" + getBuffId());
        //清除当前的引导技能
        if (target.getCurSlowSkill() != null) {
            target.setCurSlowSkill(null);
        }
        //通知固定位置
        target.changeCurPos(target.gainCurPos(), true);
        if (!target.isDie()) {
            ((Entity) target).resetState();
        }

        if (target instanceof Player) {
            Player player = (Player) target;
            player.addFightState(FightEnum.SkillFreeze);
        }
        if (target instanceof Monster) {
            Monster monster = (Monster) target;
//            if (monster.isTdMonster()) {
                Manager.mapManager.synStopMove(monster, true);
//            }
        }
        //清理玩家的事件行为
        target.willEvents().clear();
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
        if (owner instanceof Player) {
            Player player = (Player) owner;
            player.removeFightState(FightEnum.SkillFreeze);
        }

        if (owner instanceof Monster) {
            Monster monster = (Monster) owner;
            if (monster.isTdMonster()) {
                Manager.mapManager.sendMoveMessage(monster, monster.getRoads());
            }
        }
        return 0;
    }

    @Override
    public int action(Buff buff, Fighter attacker, Fighter owner) {
        return 0;
    }

    @Override
    public int getId() {
        return ScriptEnum.DizzinessBuff;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }
}
