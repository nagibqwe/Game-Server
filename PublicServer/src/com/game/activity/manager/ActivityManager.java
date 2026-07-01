package com.game.activity.manager;

import com.game.activity.script.IActivityRank;
import com.game.db.bean.ActivityRankBean;
import com.game.db.dao.ActivityRankDao;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import game.core.script.IScript;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 用于跨服活动
 * @author zhaibiao
 */
public class ActivityManager {
    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        ActivityManager manager;

        Singleton() {
            this.manager = new ActivityManager();
        }

        ActivityManager getProcessor() {
            return manager;
        }
    }
    //ActivityManager
    public static ActivityManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
    private static final Logger log = LogManager.getLogger("BraveGloryWarManager");
    
    //排行活动的数据活动Id_排行数据
    private final ConcurrentHashMap<Long, List<ActivityRankBean>> activityRank = new ConcurrentHashMap<>();
    
    //当前存在的活动type_活动Id
    private final ConcurrentHashMap<Integer,Long> exist_activity = new ConcurrentHashMap<>();
    
    private final ActivityRankDao dao = new ActivityRankDao();

    
    public void loadRank(){
       List<ActivityRankBean> list =  dao.selectAll();
       for(ActivityRankBean arb : list){
           
           if(!exist_activity.containsKey(arb.getType())){
               exist_activity.put(arb.getType(), arb.getId());
           }
           
           
           List<ActivityRankBean> beanlist ;
           if(activityRank.containsKey(arb.getId())){
               beanlist = activityRank.get(arb.getId());
           }else{
               beanlist = new ArrayList<>();
               activityRank.put(arb.getId(), beanlist);
           }
           beanlist.add(arb);
       }
    }
    
    
    
    public ConcurrentHashMap<Long, List<ActivityRankBean>> getActivityRank() {
        return activityRank;
    }

    public ConcurrentHashMap<Integer, Long> getExist_activity() {
        return exist_activity;
    }

    public ActivityRankDao getDao() {
        return dao;
    }
    
    public IActivityRank deal() throws Exception {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.ActivityScript);
        if (is instanceof IActivityRank) {
            return (IActivityRank) is;
        }
        throw new Exception("没有实现跨服战场的副本！荣耀之战IBraveGloryWarScript没有找到具体的实例！");
    }
    
    
    
}
