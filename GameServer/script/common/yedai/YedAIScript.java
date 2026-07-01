package common.yedai;

import com.game.script.structs.ScriptEnum;
import com.game.yed.RandomHelper;
import com.game.yed.YedAI;
import com.game.yed.YedMgr;
import com.game.yed.scripts.YedMethodScript;
import game.core.script.IScript;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

/**
 * @author gaozhaoguang
 * @desc YedAIScript
 * @date Created on 2020/8/15 15:03
 **/
public class YedAIScript implements IScript,YedMethodScript {
    private static final Logger logger = YedMgr.getInstance().getAiLogger();

    @Override
    public int getId() {
        return ScriptEnum.YedAiCommonScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    @Override
    public boolean execAiMethod(YedAI ai, String methodName, Object[] args) {

        if(methodName==null || methodName.equals("")){
            return false;
        }
        boolean result = true;
        switch (methodName) {
            case "Rand": {
                logger.error("YedAIScript:---Rand::"+args[0].toString());
                ai.setFuncRet(Sa_Rand(ai,Double.parseDouble(args[0].toString())));
                break;
            }
            case "Wait": {
                ai.setFuncRet(Sa_Wait(ai,Double.parseDouble(args[0].toString())));
                break;
            }
            case "WaitSelf": {
                ai.setFuncRet(Sa_WaitSelf(ai,args[0].toString(),Double.parseDouble(args[1].toString())));
                break;
            }
            case "CleanWaitkey": {
                Sa_CleanWaitkey(ai);
                break;
            }
            case "WaitOneFrame": {
                ai.setFuncRet(Sa_WaitOneFrame(ai));
                break;
            }
            case "RunYed": {
                Sa_RunYed(ai,args[0].toString());
                break;
            }
            case "IsChildExit": {
                ai.setFuncRet(Sa_IsChildExit(ai));
                break;
            }

            case "print": {
                Sa_print(args[0].toString());
                break;
            }
            case "Print": {
                Sa_Print(args[0].toString());
                break;
            }
            case "SetLogFlag": {
                Sa_SetLogFlag(ai, args[0].toString());
                break;
            }
            default: {
                result = false;
                break;
            }
        }
        return result;
    }

    double Sa_Rand(YedAI ai,double range) {
        return (RandomHelper.Range(0, 10000) % range);
    }

    boolean Sa_Wait(YedAI ai,double time) {
        return (ai.StateTime() >= time) ;
    }

    /**
     * 每个条件自己的定时器
     *
     * @param key
     * @param time
     */
    boolean Sa_WaitSelf(YedAI ai,String key, double time) {
        if (ai.StateTime(key) >= time) {
            ai.getStateTimeG().remove(key);
            return true;
        } else {
            return false;
        }
    }

    void Sa_CleanWaitkey(YedAI ai) {
        ai.getStateTimeG().clear();
    }

    boolean Sa_WaitOneFrame(YedAI ai) {
        return (ai.StateTime() >= 0.01);
    }

    void Sa_RunYed(YedAI ai,String yed) {
        ai.Load(yed, false);
    }

    int Sa_IsChildExit(YedAI ai) {
        if (ai.getYed().curState == null || ai.getYed().getChild() == null) {
            return 1;
        }
        if (ai.getYed().getChild().curState == null) {
            return 0;
        }
        int ret = ai.getYed().getChild().curState.isExit ? 1 : 0;
        if (ret == 1) {
            logger.debug(String.format("ai debug owner:%s Sa_IsChildExit ret:%d", ai.getOwner().toString(), ret));
        }
        return ret;
    }

    void Sa_print(String info) {
        System.out.println(info);
    }

    void Sa_Print(String info) {
        Sa_print(info);
    }

    public synchronized void Sa_SetLogFlag(YedAI ai, String loglevel) {
        YedMgr.getInstance().SetLogFlag(loglevel);
    }


}
