package common.skill;

import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import com.game.skill.script.ISkillMagicAiScript;
import com.game.skill.structs.SkillMagic;
import com.game.structs.Entity;
import com.game.yed.YedAI;
import com.game.yed.YedMgr;
import com.game.yed.scripts.YedMethodScript;
import game.core.script.IScript;
import org.apache.logging.log4j.Logger;

/**
 * @author admin
 */
public class SkillMagicAiScript implements IScript, ISkillMagicAiScript,YedMethodScript {
    final static private Logger logger = YedMgr.getInstance().getAiLogger();
    @Override
    public int getId() {
        return ScriptEnum.SkillMagicAiCommonScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void AiUpdate(Entity owner) {
        if (owner == null) {
            return;
        }
        if (owner.gainRunningAi() == null) {
            return;
        }
        YedAI runningAi = owner.gainRunningAi();
        runningAi.SetOwner(this);
        runningAi.setOwnerparams(owner);
        runningAi.cleanCommonTarget();
        // 找不到的函数就走基类ai里面找,每次都设置是为了动态更新的时候每次取到最新的对象
        runningAi.addCommonTarget(yedBaseAi());
        runningAi.Update();
    }
    public YedMethodScript yedBaseAi() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.EntityAiCommonScript);
        if (is instanceof YedMethodScript) {
            return (YedMethodScript) is;
        }
        return null;
    }
    /**
     * 设置这个召唤物的action行为参数
     * @param ai
     * @param param
     */
    private void Sa_SetActionParam(YedAI ai, String param){
        Object[] ps = ai.getOwnerparams();
        if (ps == null || ps.length < 1 || ps[Entity.AiParam.entity.value] == null || !(ps[Entity.AiParam.entity.value] instanceof SkillMagic)) {
            logger.error("跑ai Sa_SetActionParam,参数没设置对 需要一个 SkillMagic");
            return;
        }
        SkillMagic op = (SkillMagic) ai.getOwnerparams()[Entity.AiParam.entity.value];
        op.setActionParam(param);
        logger.warn(String.format("ai action Sa_SetActionParam op:%s param:%s", op, param));
    }

    @Override
    public boolean execAiMethod(YedAI ai, String methodName, Object[] args) {
        if(methodName==null || methodName.equals("")){
            return false;
        }
        switch (methodName) {
            case "SetActionParam": {
                Sa_SetActionParam(ai,args[0].toString());
                break;
            }
        }
        return true;
    }
}
