package com.game.eightdiagrams.manager;

import com.game.db.bean.EightIntegralRankBean;
import com.game.db.dao.EightIntegralRankDao;
import com.game.eightdiagrams.script.IEightDiagrams;
import com.game.eightdiagrams.structs.EightDiageramSeverInfo;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by cxl on 2019/9/20.
 */
public class EightDiagramsManager {

    private static final Logger log = LogManager.getLogger(EightDiagramsManager.class);

    private boolean   eightDiagramsIsOpen = false;

    public static final int Top50Num = 50;

    public static final int Top100Num = 100;

    private EightIntegralRankDao eightIntegralRankDao = new EightIntegralRankDao();


    //服务器plat_severid 对应组ID
    private ConcurrentHashMap<String, Integer> serverPlat_SidToGroup = new ConcurrentHashMap<>();

    //roleid 城市ID
    private ConcurrentHashMap<Long, Integer> playerWithCiytIDList = new ConcurrentHashMap<>();

    //每天战斗的积分信息
    private ConcurrentHashMap<Integer,ConcurrentHashMap<Long, EightIntegralRankBean>> dayOf_playerBattleInfo = new ConcurrentHashMap<>();

    //每期战斗积分---需要记录
    private ConcurrentHashMap<Integer, ConcurrentHashMap<Long, EightIntegralRankBean>> periodOf_playerBattleInfo = new ConcurrentHashMap<>();

    private ConcurrentHashMap<Integer ,List<EightIntegralRankBean>>  dayOf_Ranklist50Num = new ConcurrentHashMap<>();

    private ConcurrentHashMap<Integer ,List<EightIntegralRankBean>>   periodOf_Ranklist50Num = new ConcurrentHashMap<>();

    //serverid colorID
    private ConcurrentHashMap<Integer, Integer> serverWithColorIDList = new ConcurrentHashMap<>();

    private ConcurrentHashMap<Long,Integer>  groupIDForRoleID = new ConcurrentHashMap<>();


    public  void load(){
        List<EightIntegralRankBean> periodRankBeans =   eightIntegralRankDao.selectAll();
       if (periodRankBeans !=null  && periodRankBeans.size() > 0){
            for (EightIntegralRankBean bean : periodRankBeans){
                ConcurrentHashMap<Long, EightIntegralRankBean>  battleInfoMap = null;
                if (periodOf_playerBattleInfo.containsKey(bean.getGroupId())){
                    battleInfoMap = periodOf_playerBattleInfo.get(bean.getGroupId());
                    battleInfoMap.put(bean.getRoleID(), bean);
                }else {
                    battleInfoMap =new ConcurrentHashMap<>();
                    battleInfoMap.put(bean.getRoleID(),bean);
                    periodOf_playerBattleInfo.put(bean.getGroupId(), battleInfoMap);
                }
            }
           deal().loadPeriodRank();
       }
    }
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
    public ConcurrentHashMap<String, Integer> getServerPlat_SidToGroup(){return serverPlat_SidToGroup;}

    public void setEightDiagramsIsOpen(boolean eightDiagramsIsOpen){this.eightDiagramsIsOpen = eightDiagramsIsOpen;}

    public boolean getEightDiagramsIsOpen(){return eightDiagramsIsOpen;}

    public  ConcurrentHashMap<Integer,ConcurrentHashMap<Long, EightIntegralRankBean>> getDayOf_playerBattleInfo(){return  dayOf_playerBattleInfo;}

    public ConcurrentHashMap<Long, Integer> getPlayerWithCiytIDList(){return playerWithCiytIDList;}

    public ConcurrentHashMap<Integer, Integer> getServerWithColorIDList(){return serverWithColorIDList;}

    public  ConcurrentHashMap<Integer,List<EightIntegralRankBean>> getDayOf_Ranklist50Num(){return dayOf_Ranklist50Num;}

    public  ConcurrentHashMap<Integer,List<EightIntegralRankBean>> getPeriodOf_Ranklist50Num(){return periodOf_Ranklist50Num;}

    public ConcurrentHashMap<Long, Integer> getGroupIDForRoleID() {
        return groupIDForRoleID;
    }

    public void setGroupIDForRoleID(ConcurrentHashMap<Long, Integer> groupIDForRoleID) {
        this.groupIDForRoleID = groupIDForRoleID;
    }

    public  ConcurrentHashMap<Integer, ConcurrentHashMap<Long, EightIntegralRankBean>> getPeriodOf_playerBattleInfo() {
        return periodOf_playerBattleInfo;
    }

    public  void setPeriodOf_playerBattleInfo(ConcurrentHashMap<Integer, ConcurrentHashMap<Long, EightIntegralRankBean>> periodOf_playerBattleInfo) {
        periodOf_playerBattleInfo = periodOf_playerBattleInfo;
    }

    public EightIntegralRankDao getEightIntegralRankDao() {
        return eightIntegralRankDao;
    }

    public void setEightIntegralRankDao(EightIntegralRankDao eightIntegralRankDao) {
        this.eightIntegralRankDao = eightIntegralRankDao;
    }
}
