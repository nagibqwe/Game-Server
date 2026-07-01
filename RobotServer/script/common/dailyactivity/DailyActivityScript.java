package common.dailyactivity;

import com.data.CfgManager;
import com.data.bean.Cfg_Clone_map_Bean;
import com.data.bean.Cfg_Daily_Bean;
import com.data.bean.Cfg_Mapsetting_Bean;
import com.data.container.Cfg_Daily_Container;
import com.game.dailyactivity.script.IDailyActivityScript;
import com.game.manager.Manager;
import com.game.player.structs.EventDefine;
import com.game.player.structs.Player;
import com.game.script.ScriptEnum;
import com.game.utils.RandomUtils;
import game.message.DailyactiveMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class DailyActivityScript implements IDailyActivityScript {

    private static final Logger log = LogManager.getLogger(DailyActivityScript.class);

    @Override
    public int getId() {
        return ScriptEnum.DailyActiveBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public int enterDaily(Player player, int dailyId) {
        int afterTime = 0;
        //检查等级是否满足
       Cfg_Daily_Bean bean = Cfg_Daily_Container.GetInstance().getValueByKey(dailyId);
       if (bean == null) {
           log.error("日常副本配置没找到，ID=" + bean.getId());
           return afterTime;
       }
       if (player.getLevel() < bean.getOpenLevel()) {
           player.chatGM("&setlevel " + bean.getOpenLevel());
           return afterTime;
       }
       if (bean.getCloneID().isEmpty()) {
           log.info("日常副本没有配置副本ID字段，ID=" + bean.getId());
           return afterTime;
       }
        int modelId = 0;
        int maxNum = 9999;
        int curNum = 0;
        for (int i = 0; i < bean.getCloneID().size(); i++) {
            Cfg_Clone_map_Bean cloneMapBean = CfgManager.getCfg_Clone_map_Container().getValueByKey(bean.getCloneID().get(i));
            if (cloneMapBean == null) {
                log.info("副本配置没找到，ID=" + cloneMapBean.getId());
                continue;
            }
            if (player.getLevel() < cloneMapBean.getMin_lv() || player.getLevel() > cloneMapBean.getMax_lv()) {
                continue;
            }

            Cfg_Mapsetting_Bean mapBean = CfgManager.getCfg_Mapsetting_Container().getValueByKey(cloneMapBean.getMapid());
            if (mapBean == null) {
                log.info("地图配置没找到，ID=" + mapBean.getMap_id());
                continue;
            }
            Manager.bossManager.getNumMap().putIfAbsent(cloneMapBean.getId(),0);
            curNum = Manager.bossManager.getNumMap().get(cloneMapBean.getId());
            if(curNum>=mapBean.getOnline()){
                continue;
            }
            if (curNum <= maxNum){
                maxNum = curNum;
                modelId = cloneMapBean.getId();
            }
        }
//        List<Integer> list= Arrays.asList(1509,1510,1545);
//        modelId = RandomUtils.random(list.size());

        if(modelId>0){
            log.error("sendReqJoinDaily sendReqJoinDaily modelId  " + modelId   +  " curNum   "  + curNum);
            Manager.bossManager.getNumMap().put(modelId,curNum + 1);
            sendReqJoinDaily(player, dailyId, modelId);
            afterTime = 5000;
        }else {
            log.error("sendReqJoinDaily   fail stop  ");
            player.setEventType(EventDefine.Event_RandMove);
        }
        return afterTime;
    }

    @Override
    public int enterDaily(Player player, int dailyId, int modelId) {
        if(modelId <= 0){
            return enterDaily(player,dailyId);
        }

        int afterTime = 1000;
        //检查等级是否满足
        Cfg_Daily_Bean bean = Cfg_Daily_Container.GetInstance().getValueByKey(dailyId);
        if (bean == null) {
            log.error("日常副本配置没找到，ID=" + bean.getId());
            return afterTime;
        }
        if (player.getLevel() < bean.getOpenLevel()) {
            player.chatGM("&setlevel " + bean.getOpenLevel());
            return afterTime;
        }

        Cfg_Clone_map_Bean cloneMapBean = CfgManager.getCfg_Clone_map_Container().getValueByKey(modelId);
        if (cloneMapBean == null) {
            log.error("副本配置没找到，ID=" + cloneMapBean.getId());
            return afterTime;
        }
        if (player.getLevel() < cloneMapBean.getMin_lv() && player.getLevel() > cloneMapBean.getMax_lv()) {
            return afterTime;
        }

        sendReqJoinDaily(player, dailyId, modelId);
        afterTime = 2000;
        return afterTime;
    }

    private void sendReqJoinDaily(Player player, int dailyId, int modelId) {
        DailyactiveMessage.ReqJoinDaily.Builder msg = DailyactiveMessage.ReqJoinDaily.newBuilder();
        msg.setDailyId(dailyId);
        msg.setParam(modelId);
        player.sendMsg(DailyactiveMessage.ReqJoinDaily.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        log.info(player.getInfo()+"请求进入日常副本,type="+dailyId+",id="+modelId);
    }
}
