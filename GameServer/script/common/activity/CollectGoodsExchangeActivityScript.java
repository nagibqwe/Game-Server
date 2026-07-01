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
import game.core.json.TypeReference;
import game.core.util.IDConfigUtil;
import game.core.util.JsonUtils;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 集物兑换 300008
 * Created by cxl on 2020/9/9.
 */
public class CollectGoodsExchangeActivityScript implements IActivityScript {
    private static final String CollectGoodsDataStr = "collectGoodsData";
    private static final String ExChangeList = "exChangeList";//已经兑换的数据发给客户端的
    private static final String ExChange ="exChange"; //兑换
    public static final Logger LOGGER = LogManager.getLogger(CollectGoodsExchangeActivityScript.class);

    @Override
    public int getId() {
        return ScriptEnum.CollectGoodsExChangeScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    @Override
    public void reload() {
        if(Manager.activityManager == null){
            return;
        }
        List<ActivityConfig> actCfgList = Manager.activityManager.deal().getActCfgListByActLogicID(ActivityType.CollectGoodsExChange);
        for (ActivityConfig activityConfig:actCfgList) {
            if (activityConfig == null) {
                return;
            }
            Object old = activityConfig.getCustomCfgMap().get(CollectGoodsDataStr);
            if (old == null) {
                return;
            }
            String customStr = JsonUtils.toJSONString(old);
            CollectGoodsData newData = JsonUtils.toJavaObject(customStr, CollectGoodsData.class);
            activityConfig.getCustomCfgMap().put(CollectGoodsDataStr, newData);
        }
    }

    @Override
    public void onReqActivityDeal(Player player, String dataStr, ActivityConfig actCfg) {
        HashMap<String, Object>  dailyMap =  JsonUtils.parseObject(dataStr, new TypeReference<HashMap<String, Object>>(){});
        if (actCfg == null){
            return;
        }
        int actType = actCfg.getType();

        int startDay =  TimeUtils.getCurDayByTime(actCfg.getBeginTime());
        int curDay   =  TimeUtils.getCurDayByTime(TimeUtils.Time());
        int overDay  =  TimeUtils.getCurDayByTime(actCfg.getEndTime());

        if (curDay < startDay || curDay > overDay){
            LOGGER.error("活动时间已结束"  + actType);
            return;
        }
        int exChangeID = (int)dailyMap.get(ExChange);
        int exNum = (int)dailyMap.get("num");
        if (exNum < 1 ){
            LOGGER.error("兑换数量为 0");
            return;
        }
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(), actType);

        if (roleActDataMap == null){
            return;
        }
        roleActDataMap.putIfAbsent(ExChangeList,new HashMap<Integer,Integer>());
        HashMap<Integer,Integer> exChangeList = (HashMap<Integer,Integer>)roleActDataMap.get(ExChangeList);
        exChangeList.putIfAbsent(exChangeID,0);

        int alreadExChangeNum = exChangeList.get(exChangeID);
        if( dailyMap.get(ExChange) == null){
            LOGGER.error("兑换数据为空");
            return;
        }
        CollectGoodsData collectGoodsData = (CollectGoodsData)actCfg.getCustomCfgMap().get(CollectGoodsDataStr);

        if (!collectGoodsData.getExChangeDataMap().containsKey(exChangeID)){
            LOGGER.error("没有该类型的兑换 道具"  + exChangeID);
            return;
        }
        int exChangeM  = collectGoodsData.exChangeMaterialsId;
        ExChangeData exChangeData = collectGoodsData.getExChangeDataMap().get(exChangeID);
        int exChangeN  = exChangeData.exChangePrice * exNum;
        int maExChangeN = exChangeData.getExChangeTimes();
        if (maExChangeN != 0 && (alreadExChangeNum +  exNum) > maExChangeN){
            LOGGER.error("兑换次数已满"  + alreadExChangeNum);
            return;
        }
        if (!Manager.backpackManager.manager().onRemoveItem(player,exChangeM, exChangeN, ItemChangeReason.CollectGoodsExchangeDel, IDConfigUtil.getLogId())){
            LOGGER.error("材料不足"  + exChangeM);
            return;
        }
        RewardData rewardData = exChangeData.getRewardData();
        List<Item> itemList = new ArrayList<>();
        itemList.addAll(Item.createItems(rewardData.getI(), rewardData.getN() * exNum, rewardData.getB() == 1));
        long actionId = IDConfigUtil.getLogId();
        if ( !Manager.backpackManager.manager().addItems(player,itemList, ItemChangeReason.CollectGoodsExchangeGet, actionId)){
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, itemList, ItemChangeReason.CollectGoodsExchangeGet, actionId);
        }

//        Manager.biManager.getScript().biActivity(player, ItemChangeReason.CollectGoodsExchangeGet, actCfg.getType(), actCfg.getId());
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.CollectGoodsExchange, ItemChangeReason.CollectGoodsExchangeGet, exChangeID);
        exChangeList.put(exChangeID,alreadExChangeNum + exNum);

        Manager.activityManager.deal().onReqActivity(player,actType);
        //保存角色活动数据
        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));


    }

    @Override
    public boolean parseCustomConfig(ActivityConfig actCfg, String customStr)
    {
        CollectGoodsData data = JsonUtils.parseObject(customStr,CollectGoodsData.class);
        actCfg.getCustomCfgMap().put(CollectGoodsDataStr,data);
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
        roleActDataMap.putIfAbsent(ExChangeList,new HashMap<Integer,Integer>());
        HashMap<Integer,Integer> exChangeList = (HashMap<Integer,Integer>)roleActDataMap.get(ExChangeList);
        HashMap<String,Object> exdata = new HashMap<>();
        exdata.put(ExChangeList,exChangeList);
        return JsonUtils.toJSONString(exdata);
    }

    @Override
    public void rechargeDeal(Player player,int getGoodsCfgId, int rechargeNum, ActivityConfig actCfg) {

    }

    @Override
    public void playerOnline(Player player, ActivityConfig actCfg) {

    }

    @Override
    public void zeroClockPlayerDeal(Player player, ActivityConfig actCfg) {

    }

    @Override
    public void fiveClockPlayerDeal(Player player, ActivityConfig actCfg) {

    }

    @Override
    public void zeroClockDeal(ActivityConfig actCfg) {

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
    public void activityEndDeal(ActivityConfig actCfg) {
        if (actCfg == null) {
            return;
        }
        if (!actCfg.getCustomCfgMap().containsKey(CollectGoodsDataStr)){
            return;
        }
        int actType = actCfg.getType();
        CollectGoodsData collectGoodsData = (CollectGoodsData)actCfg.getCustomCfgMap().get(CollectGoodsDataStr);
        int returnCoinType = collectGoodsData.getReturnMoneyCoinType();
        int returnNum  = collectGoodsData.getReturnMoneyCoinNum();
        int exChangeMaterialsId = collectGoodsData.getExChangeMaterialsId();

        List<Long> roleIds = Manager.activityManager.deal().getRoleIdList(actType);
        for (long roleid : roleIds){
           Player player =   Manager.playerManager.getPlayer(roleid);
           if (player == null){
               LOGGER.error("玩家对象不存在 ");
               continue;
           }
           int hasNum =    Manager.backpackManager.manager().getItemNum(player,exChangeMaterialsId);
           if (hasNum>0){
               long action = IDConfigUtil.getLogId();
               if ( !Manager.backpackManager.manager().onRemoveItem(player,exChangeMaterialsId,hasNum,ItemChangeReason.CollectGoodsExchangeDel,action)){
                   LOGGER.error("扣除材料失败 " + returnCoinType);
                   continue;
               }
               int returnAll = returnNum  * hasNum;
               List<Item> itemList = new ArrayList<>();
               itemList.addAll(Item.createItems(returnCoinType, returnAll, true));
               Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                       MessageString.Collect_Goods_Reward_Mail_Title, MessageString.Collect_Goods_Reward_Mail, itemList, ItemChangeReason.CollectGoodsExchangeGet, action);

//               Manager.biManager.getScript().biActivity(player, ItemChangeReason.CollectGoodsExchangeGet,  actCfg.getType(), actCfg.getId());
               Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.CollectGoodsExchange, ItemChangeReason.CollectGoodsExchangeGet, returnCoinType);
           }
        }
    }

    @Override
    public void consumeDeal(Player player, int coinType, int consumeNum, ActivityConfig actCfg) {

    }
    static class CollectGoodsData{

        private String client ;

        private int exChangeMaterialsId;//兑换所需要的材料

        private int returnMoneyCoinType;//退还的货币类型

        private int returnMoneyCoinNum;//单个材料退还的货币数量

        //KEY 商品ID
        private HashMap<Integer,ExChangeData> exChangeDataMap = new HashMap<>();

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public int getExChangeMaterialsId() {
            return exChangeMaterialsId;
        }

        public void setExChangeMaterialsId(int exChangeMaterialsId) {
            this.exChangeMaterialsId = exChangeMaterialsId;
        }

        public int getReturnMoneyCoinType() {
            return returnMoneyCoinType;
        }

        public void setReturnMoneyCoinType(int returnMoneyCoinType) {
            this.returnMoneyCoinType = returnMoneyCoinType;
        }

        public int getReturnMoneyCoinNum() {
            return returnMoneyCoinNum;
        }

        public void setReturnMoneyCoinNum(int returnMoneyCoinNum) {
            this.returnMoneyCoinNum = returnMoneyCoinNum;
        }

        public HashMap<Integer, ExChangeData> getExChangeDataMap() {
            return exChangeDataMap;
        }

        public void setExChangeDataMap(HashMap<Integer, ExChangeData> exChangeDataMap) {
            this.exChangeDataMap = exChangeDataMap;
        }
    }
    static class ExChangeData{
        private int exChangeTimes;//兑换次数 0 代表无限次

        private int exChangePrice;//兑换价格

        private RewardData rewardData;

        public int getExChangeTimes() {
            return exChangeTimes;
        }

        public void setExChangeTimes(int exChangeTimes) {
            this.exChangeTimes = exChangeTimes;
        }

        public RewardData getRewardData() {
            return rewardData;
        }

        public void setRewardData(RewardData rewardData) {
            this.rewardData = rewardData;
        }

        public int getExChangePrice() {
            return exChangePrice;
        }

        public void setExChangePrice(int exChangePrice) {
            this.exChangePrice = exChangePrice;
        }
    }
}
