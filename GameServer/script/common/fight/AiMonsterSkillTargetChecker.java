package common.fight;

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
 * Created by huhu on 2017/8/23.
 * 怪物包含了怪物所有的判断
 * 判断不是同阵营就能打
 */
public class AiMonsterSkillTargetChecker implements ISkillTargetChecker, IScript {
    private static final Logger log = LogManager.getLogger(AiMonsterSkillTargetChecker.class);

    @Override
    public int getId() {
        return ScriptEnum.AiMonsterSkillTargetCheckerCommonScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public int Invoke(Fighter attacker, Fighter defer, STCheckerMaker state) {
        if(state != null && state.enemys != null){
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
        // 怪是一定不能打怪的
        if (defer instanceof Monster) {
            return -1;
        }
        if (defer instanceof Player) {
            return checkPlayer(attacker, defer);
        }
        if (defer instanceof Pet) {
            return checkPet(attacker, defer);
        }
        return -2;
    }

    private int checkPlayer(Fighter attacker, Fighter defer) {
        if (attacker.getCamp() == defer.getCamp() || defer.getCamp() == -1) {
            return -3;
        }
        return 0;
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
