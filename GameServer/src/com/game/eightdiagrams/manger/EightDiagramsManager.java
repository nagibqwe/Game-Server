package com.game.eightdiagrams.manger;

import com.game.db.bean.EightIntegralRankBean;
import com.game.eightdiagrams.script.IEightDiagrams;
import com.game.eightdiagrams.structs.EightDiagramCity;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by cxl on 2019/9/27.
 */
public class EightDiagramsManager {

    private static final Logger log = LogManager.getLogger(EightDiagramsManager.class);


    public static final int Occupy_City_Reward = 1;//占领城市奖励

    public static final int IntegralSettle  = 2;//每场完后积分奖励

    public static final int SeasonSettle  = 3;//赛季奖励

    //组ID 对应 这个组的 13个争夺城市
    private   ConcurrentHashMap<Integer,ConcurrentHashMap<Integer ,EightDiagramCity>> groupCityInfos = new ConcurrentHashMap<>();

    //每期战斗积分---需要记录
    private   ConcurrentHashMap<Integer,ConcurrentHashMap<Long, EightIntegralRankBean>> periodOf_playerBattleInfo = new ConcurrentHashMap<>();

    //每天活动战斗积分
    private  ConcurrentHashMap<Integer,ConcurrentHashMap<Long, EightIntegralRankBean>> dayOf_playerBattleInfo = new ConcurrentHashMap<>();


    private ConcurrentHashMap<Integer ,List<EightIntegralRankBean>>  dayOf_Ranklist100Num = new ConcurrentHashMap<>();

    private ConcurrentHashMap<Integer ,List<EightIntegralRankBean>>   periodOf_Ranklist50Num = new ConcurrentHashMap<>();

    //参与统计，最后发奖用，
    private  ConcurrentHashMap<Integer,ConcurrentHashMap<Long, String>>  participationList = new ConcurrentHashMap<>();

    public boolean eightDiagramsIsOpen = false;
     private enum Singleton {

        INSTANCE;
        EightDiagramsManager manager;

        Singleton() {
            this.manager = new EightDiagramsManager();
        }

        EightDiagramsManager getProcessor() {
            return manager;
        }
    }

    public static EightDiagramsManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public IEightDiagrams deal(){
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.EightDiagramsScript);
        if (is instanceof IEightDiagrams) {
            return (IEightDiagrams) is;
        } else {
            log.error("没有找到具体的接口实现类！不会走到这里，请注意实现！");
            return null;
        }
    }

    public ConcurrentHashMap<Integer,ConcurrentHashMap<Integer ,EightDiagramCity>> getGroupCityInfos(){return groupCityInfos;}

    public  ConcurrentHashMap<Integer,ConcurrentHashMap<Long, EightIntegralRankBean>> getPeriodOf_playerBattleInfo(){return periodOf_playerBattleInfo;}

    public  ConcurrentHashMap<Integer,ConcurrentHashMap<Long, EightIntegralRankBean>> getDayOf_playerBattleInfo(){return dayOf_playerBattleInfo;}

    public  ConcurrentHashMap<Integer,ConcurrentHashMap<Long, String>> getParticipationList(){return participationList;}

    public  ConcurrentHashMap<Integer,List<EightIntegralRankBean>> getDayOf_Ranklist100Num(){return dayOf_Ranklist100Num;}

    public  ConcurrentHashMap<Integer,List<EightIntegralRankBean>> getPeriodOf_Ranklist50Num(){return periodOf_Ranklist50Num;}


}
