package common.fight;

import com.game.script.structs.ScriptEnum;
import com.game.skill.structs.SkillMagic;
import com.game.structs.Fighter;
import com.game.structs.ISkillTargetChecker;
import com.game.structs.STCheckerMaker;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;

/**
 * Created by huhu on 2017/8/23.
 * 只判断不要打到自己就是了,是最快的判断
 */
public class AiDefaultSkillTargetChecker implements ISkillTargetChecker, IScript {
    private static final Logger log = LogManager.getLogger(AiDefaultSkillTargetChecker.class);

    @Override
    public int getId() {
        return ScriptEnum.AiDefaultSkillTargetCheckerCommonScript;
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
        if(defer.getId() == attacker.getId()) {
            return -2;
        }
        if(attacker instanceof SkillMagic){
            return checkMagic((SkillMagic)attacker, defer);
        }
        return 0;
    }

    private int checkMagic(SkillMagic attacker, Fighter defer) {
        if(attacker.getOwnerId() == defer.getId()) {
            return -2;
        }
        return 0;
    }
}
