package common.fight;

import com.game.monster.structs.Monster;
import com.game.pet.structs.Pet;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Entity;
import com.game.structs.Fighter;
import com.game.structs.ISkillTargetChecker;
import com.game.structs.STCheckerMaker;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by huhu on 2017/8/23.
 */
public class GuideSkillTargetChecker implements ISkillTargetChecker, IScript {
    private static final Logger log = LogManager.getLogger(GuideSkillTargetChecker.class);

    @Override
    public int getId() {
        return ScriptEnum.GuideSkillTargetCheckerCommonScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public int Invoke(Fighter attacker, Fighter defer, STCheckerMaker state) {
        if (state != null && state.enemys != null) {
            if (state.enemys.size() >= state.maxTargetCount)
                return -3;
            if (state.enemys.contains(defer))
                return -2;
        }
        if (attacker instanceof Entity) {
            Entity a = (Entity) attacker;
            if (a.gainSkillTargetFilter() == null) {
                int scriptid = ScriptEnum.AiDefaultSkillTargetCheckerCommonScript;
                if (a instanceof Monster) {
                    scriptid = ScriptEnum.AiMonsterSkillTargetCheckerCommonScript;
                } else if (a instanceof Pet) {
                    scriptid = ScriptEnum.AiPetSkillTargetCheckerCommonScript;
                } else if (a instanceof Player) {
                    scriptid = ScriptEnum.AiPlayerSkillTargetCheckerCommonScript;
                }
                // 设置一个除了自己都能打的搜索条件
                a.changeSkillTargetFilter(scriptid);
            }
            return a.gainSkillTargetFilter().Invoke(attacker, defer, null);
        }
        return -5;
    }
}
