package common.behavior;

import com.game.behavior.structs.BaseBehavior;
import com.game.behavior.structs.IBehavior;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import com.game.skill.script.ISkillMagicAiScript;
import com.game.structs.Entity;
import com.game.yed.YedAI;
import com.game.yed.YedMgr;
import game.core.map.IMapObject;
import game.core.script.IScript;
import org.apache.logging.log4j.Logger;


/**
 * Created by huhu on 2017/9/26.
 */
public class MagicAiBehaviorScript implements IScript, IBehavior {
    @Override
    public int getId() {
        return ScriptEnum.MagicAiBehaviorCommonScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public boolean Cancel(BaseBehavior behavior) {
        final Logger logger = YedMgr.getInstance().getAiLogger();
        logger.fatal("MagicAiBehavior Cancel", new Throwable("调用cancel的堆栈"));
        return false;
    }

    @Override
    public void action(BaseBehavior behavior) {

        final Logger logger = YedMgr.getInstance().getAiLogger();

        IMapObject mapObject = behavior.getOwner();
        if(!(mapObject instanceof Entity)){
            logger.fatal(String.format("MagicAiBehavior 无法获得entity类型的owner %s", mapObject == null ? "null" : mapObject.toString()));
            return;
        }
        Entity owner = (Entity)mapObject;
        YedAI ai = owner.gainRunningAi();
        if(ai == null){
            logger.error(String.format("MagicAiBehavior 添加了ai behavior之后没找到running yedai %s", mapObject == null ? "null" : mapObject.toString()));
            return;
        }
        ISkillMagicAiScript ib = (ISkillMagicAiScript)Manager.scriptManager.GetScriptClass(ScriptEnum.SkillMagicAiCommonScript);
        if(ib == null){
            logger.error(String.format("MagicAiBehavior MagicBehaviorScript 脚本没找到 %s", mapObject == null ? "null" : mapObject.toString()));
            return;
        }

        ib.AiUpdate(owner);
    }
}
