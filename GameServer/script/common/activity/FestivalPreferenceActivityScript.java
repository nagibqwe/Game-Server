package common.activity;

import com.data.ItemChangeReason;
import com.data.MessageString;

import com.game.activity.script.IActivityScript;
import com.game.activity.struct.ActivityConfig;
import com.game.activity.struct.ActivityType;
import com.game.activity.struct.RewardData;
import com.game.backpack.structs.Item;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import game.core.util.IDConfigUtil;
import game.core.util.JsonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 节日特惠 300014
 * Created by 542 cxl 2020/10/15.
 */
public class FestivalPreferenceActivityScript implements IActivityScript {


    static class FestivalPreferenceData{
        private String client ;

        private HashMap<Integer,List<RewardData>> festivalRewardList = new HashMap<>();


        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public HashMap<Integer, List<RewardData>> getFestivalRewardList() {
            return festivalRewardList;
        }

        public void setFestivalRewardList(HashMap<Integer, List<RewardData>> festivalRewardList) {
            this.festivalRewardList = festivalRewardList;
        }
    }
    public static final Logger LOGGER = LogManager.getLogger(FestivalPreferenceActivityScript.class);
    private static final String FestivalPreferenceDataStr = "festivalPreferenceData";
    private static final String buyGoodsId = "buyGoodsId";


    @Override
    public int getId() {
        return ScriptEnum.FestivalPreferenceScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    @Override
    public void fiveClockPlayerDeal(Player player, ActivityConfig actCfg) {


    }

    @Override
    public void fiveClockDeal(ActivityConfig actCfg) {


    }

    @Override
    public void everyHourDeal(ActivityConfig actCfg) {

    }

    /**
     * 活动掉落
     *
     * @param player
     * @param bossId
     * @return
     */
    @Override
    public boolean bossDrop(Player player, int bossId, ActivityConfig actCfg) {
        return false;
    }

    /**
     * 活动掉落
     *
     * @param player
     * @param boxId
     * @return
     */
    @Override
    public boolean boxDrop(Player player, int boxId, ActivityConfig actCfg) {
        return false;
    }

    /**
     * 副本掉落
     *
     * @param player
     * @param cloneId
     * @return
     */
    @Override
    public boolean cloneDrop(Player player, int cloneId, ActivityConfig actCfg) {
        return false;
    }

    @Override
    public void reload() {
        if(Manager.activityManager == null){
            return;
        }
        List<ActivityConfig> actCfgList = Manager.activityManager.deal().getActCfgListByActLogicID(ActivityType.FestivalPreference);
        for (ActivityConfig activityConfig:actCfgList) {
            if (activityConfig == null) {
                return;
            }
            Object old = activityConfig.getCustomCfgMap().get(FestivalPreferenceDataStr);
            if (old == null) {
                return;
            }
            String customStr = JsonUtils.toJSONString(old);
            FestivalPreferenceData newData = JsonUtils.toJavaObject(customStr, FestivalPreferenceData.class);
            activityConfig.getCustomCfgMap().put(FestivalPreferenceDataStr, newData);
        }

    }

    @Override
    public void onReqActivityDeal(Player player, String dataStr, ActivityConfig actCfg) {

    }

    @Override
    public boolean parseCustomConfig(ActivityConfig actCfg, String customStr) {
        FestivalPreferenceData data = JsonUtils.parseObject(customStr,FestivalPreferenceData.class);
        actCfg.getCustomCfgMap().put(FestivalPreferenceDataStr,data);
        actCfg.getCustomCfgMap().put("client",data.getClient());
        return true;
    }

    @Override
    public boolean updateCustomConfig(ActivityConfig actCfg, String customStr) {
        return false;
    }

    @Override
    public String getActivityDataStr(ActivityConfig actCfg, long roleId) {
        int actType = actCfg.getType();
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(roleId, actType);
        roleActDataMap.putIfAbsent(buyGoodsId,new ArrayList<Integer>());

        List<Integer> buyGoodsList = (ArrayList<Integer>)roleActDataMap.get(buyGoodsId);
        HashMap<String,Object> exdata = new HashMap<>();
        exdata.put(buyGoodsId,buyGoodsList);
        return JsonUtils.toJSONString(exdata);
    }

    @Override
    public void activityEndDeal(ActivityConfig actCfg) {

    }

    @Override
    public void playerOnline(Player player, ActivityConfig actCfg) {

    }

    @Override
    public void zeroClockPlayerDeal(Player player, ActivityConfig actCfg) {

    }

    @Override
    public void zeroClockDeal(ActivityConfig actCfg) {

        if (actCfg == null) {
            return;
        }
        int actType = actCfg.getType();
        if (!actCfg.getCustomCfgMap().containsKey(FestivalPreferenceDataStr)){
            return;
        }
        List<Long> roleIds = Manager.activityManager.deal().getRoleIdList(actType);
        for (long roleid : roleIds) {
            ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(roleid, actType);
            roleActDataMap.put(buyGoodsId,new ArrayList<Integer>());

            //保存角色活动数据
            Manager.activityManager.deal().saveRoleActData(roleid, Manager.activityManager.getRoleActDatas().get(roleid));
            Player player =   Manager.playerManager.getPlayerOnline(roleid);
            if (player == null){
                continue;
            }
            Manager.activityManager.deal().onReqActivity(player,actType);
        }
        LOGGER.info("节日特惠 凌晨0点刷新成功");
    }

    @Override
    public void rechargeDeal(Player player,int getGoodsCfgId, int rechargeNum, ActivityConfig actCfg) {

        if (actCfg == null) {
            return;
        }
        int actType = actCfg.getType();
        if (!actCfg.getCustomCfgMap().containsKey(FestivalPreferenceDataStr)){
            return;
        }
        FestivalPreferenceData festivalPreferenceData = (FestivalPreferenceData)actCfg.getCustomCfgMap().get(FestivalPreferenceDataStr);
        if (!festivalPreferenceData.festivalRewardList.containsKey(getGoodsCfgId)){
            return;
        }
        List<RewardData> rewardDatas =  festivalPreferenceData.getFestivalRewardList().get(getGoodsCfgId);
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(), actType);
        roleActDataMap.putIfAbsent(buyGoodsId,new ArrayList<Integer>());
        List<Integer> buyGoodsList = (ArrayList<Integer>)roleActDataMap.get(buyGoodsId);
        if (buyGoodsList.contains(getGoodsCfgId)){
            LOGGER.info("节日特惠 已购买 {} ", getGoodsCfgId);
            return;
        }
        List<Item> itemList = new ArrayList<>();
        for (RewardData rewardData :rewardDatas){
            if (Manager.activityManager.checkCareer(player, rewardData.getC())){
                Item item =  Item.createItem(rewardData.getI(),rewardData.getN(),rewardData.getB() > 0);
                if (item == null){
                    continue;
                }
                itemList.add(item);
            }
        }
        if (itemList.size() > 0){
            long actionId = IDConfigUtil.getLogId();
            if ( !Manager.backpackManager.manager().addItems(player,itemList, ItemChangeReason.FestivalPreferenceGet, actionId)){
                Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                        MessageString.System, MessageString.NoBagCell, itemList, ItemChangeReason.FestivalPreferenceGet, actionId);
            }
//            Manager.biManager.getScript().biActivity(player, ItemChangeReason.FestivalPreferenceGet, actCfg.getType(), actCfg.getId());
            Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.FestivalPreference, ItemChangeReason.FestivalPreferenceGet, getGoodsCfgId);
        }
        buyGoodsList.add(getGoodsCfgId);
        roleActDataMap.put(buyGoodsId,buyGoodsList);

        Manager.activityManager.deal().onReqActivity(player,actType);
        //保存角色活动数据
        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));

        LOGGER.info("节日特惠 购买成功 player.getId() {} getGoodsCfgId {} ", player.getId(), getGoodsCfgId);

    }

    @Override
    public void consumeDeal(Player player, int coinType, int consumeNum, ActivityConfig actCfg) {

    }
}
