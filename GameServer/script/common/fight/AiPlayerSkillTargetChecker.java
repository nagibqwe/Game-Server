package common.fight;

import com.game.monster.structs.Monster;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import com.game.structs.ISkillTargetChecker;
import com.game.structs.STCheckerMaker;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;

/**
 * Created by huhu on 2017/8/23.
 * 只给playerai使用,ai不会主动攻击角色,所以只打怪,不用isCanPk也是为了速度
 * 正常的角色是客户端发给服务器目标来攻击的
 */
public class AiPlayerSkillTargetChecker implements ISkillTargetChecker, IScript {
    private static final Logger log = LogManager.getLogger(AiPlayerSkillTargetChecker.class);

    @Override
    public int getId() {
        return ScriptEnum.AiPlayerSkillTargetCheckerCommonScript;
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
        if (!(defer instanceof Monster)) {
            return -1;
        }
        return 0;
    }
}
