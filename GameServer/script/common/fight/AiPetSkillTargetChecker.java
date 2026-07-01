package common.fight;

import com.game.fight.script.IFightManagerScript;
import com.game.manager.Manager;
import com.game.monster.structs.Monster;
import com.game.pet.structs.Pet;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import com.game.structs.ISkillTargetChecker;
import com.game.structs.STCheckerMaker;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Created by huhu on 2017/8/23. 宠物的ai包含了正常的所有判断,所以最终调用isCanPk
 */
public class AiPetSkillTargetChecker implements ISkillTargetChecker, IScript {

    private static final Logger log = LogManager.getLogger(AiPetSkillTargetChecker.class);

    @Override
    public int getId() {
        return ScriptEnum.AiPetSkillTargetCheckerCommonScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public int Invoke(Fighter attacker, Fighter defer, STCheckerMaker state) {
        if (state != null && state.enemys != null) {
            if (state.enemys.size() >= state.maxTargetCount) {
                return -3;
            }
            if (state.enemys.contains(defer)) {
                return -2;
            }
        }
        return AttackTargetChecker(attacker, defer);
    }

    private int AttackTargetChecker(Fighter attacker, Fighter defer) {
        if (defer instanceof Player) {
            return checkPlayer(attacker, defer);
        }
        if (defer instanceof Pet) {
            return checkPet(attacker, defer);
        }
        if (defer instanceof Monster) {
            IFightManagerScript is = (IFightManagerScript) Manager.scriptManager.GetScriptClass(ScriptEnum.FightManagerBaseScript);
            if (is.isCanPk(attacker, defer)) {
                return 0;
            }
        }
        return -2;
    }

    private int checkPlayer(Fighter attacker, Fighter defer) {
        Pet p = (Pet) attacker;
        Player pl = Manager.playerManager.getPlayerCache(p.getOwnerId());
        if (pl == null) {
            return -2;
        }

        if (null != pl.haveHatreds(defer)) {
            return 0;
        }
        
        IFightManagerScript is = (IFightManagerScript) Manager.scriptManager.GetScriptClass(ScriptEnum.FightManagerBaseScript);
        if (is.isCanPk(pl, defer)) {
            return 0;
        }
        return -1;
    }

    private int checkPet(Fighter attacker, Fighter defer) {
        Pet p = (Pet) defer;
        Player pl = Manager.playerManager.getPlayerCache(p.getOwnerId());
        if (pl == null) {
            return 0;
        }

        return checkPlayer(attacker, pl);
    }
}
