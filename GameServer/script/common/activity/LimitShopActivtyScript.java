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
 * 限时商城 100016
 * Created by 542 on 2020/8/28.
 */
public class LimitShopActivtyScript  implements IActivityScript {

    private final static String LimitShopDataStr = "limitShopData";
    private final static String BuyNum = "buyNum";
    private final static String BuyID  = "buyId";
    private final static String GiftBuyData   = "giftBuyData";
    private final static String ServerBuyNum  = "serverBuyNum";//全部限购
    private static final Logger LOGGER = LogManager.getLogger(LimitShopActivtyScript.class);

    @Override
    public int getId() {
        return ScriptEnum.LimitShopActivtyScript;
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
        List<ActivityConfig> actCfgList = Manager.activityManager.deal().getActCfgListByActLogicID(ActivityType.LimitShopActivty);
        for (ActivityConfig activityConfig:actCfgList) {
            if (activityConfig == null) {
                return;
            }
            Object old = activityConfig.getCustomCfgMap().get(LimitShopDataStr);
            if (old == null) {
                return;
            }
            String customStr = JsonUtils.toJSONString(old);
            LimitShopData newData = JsonUtils.toJavaObject(customStr, LimitShopData.class);
            activityConfig.getCustomCfgMap().put(LimitShopDataStr, newData);
        }

    }

    @Override
    public void onReqActivityDeal(Player player, String dataStr, ActivityConfig actCfg) {

        HashMap<String, Object>  buyGiftMap =  JsonUtils.parseObject(dataStr, new TypeReference<HashMap<String, Object>>(){});

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

        int buyID = (int)buyGiftMap.get(BuyID);
        int buyNum = (int)buyGiftMap.get(BuyNum);
        if (buyNum<=0){
            return;
        }
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(), actType);
        roleActDataMap.putIfAbsent(GiftBuyData,  new  HashMap<String,Integer>());

        ConcurrentHashMap<String, Object> actDataMap =   Manager.activityManager.getActDatas().get(actType);
        actDataMap.putIfAbsent(ServerBuyNum, new  HashMap<String,Integer>());

        HashMap<String,Integer> buyInfo =  (HashMap<String,Integer>)roleActDataMap.get(GiftBuyData);
        HashMap<String,Integer> serverbuyInfo =  (HashMap<String,Integer>)actDataMap.get(ServerBuyNum);
        buyInfo.putIfAbsent(buyID+"",0);
        serverbuyInfo.putIfAbsent(buyID+"" ,0);
        int alreadyBuyNum = buyInfo.get(buyID+"");
        int serverBuyNum = serverbuyInfo.get(buyID +"");


        LimitShopData limitGiftBagData =  (LimitShopData)actCfg.getCustomCfgMap().get(LimitShopDataStr);
        if (!limitGiftBagData.getLimitGiftDataHashMap().containsKey(buyID)){
            LOGGER.error("购买ID 错误"   + buyID);
            return;
        }
        LimitShopTargetData limitGiftData = limitGiftBagData.getLimitGiftDataHashMap().get(buyID);

        int allBuyNum  = buyNum  + alreadyBuyNum;
        if ( limitGiftData.getBuyNum() != 0 && allBuyNum > limitGiftData.getBuyNum()){
            LOGGER.error("超过  最高购买次数 "   + allBuyNum);
            return;
        }
        int allServerBuyNum = buyNum + serverBuyNum;
        if (limitGiftData.getServerBuyNum() !=0 && allServerBuyNum > limitGiftData.getServerBuyNum()){
            LOGGER.error("超过  全服 最高购买次数 "   + allServerBuyNum);
            return;
        }
        int allprice = buyNum * limitGiftData.getPrice();
        //扣钱
        if (!Manager.currencyManager.manager().canDecItemCoin(player, allprice,limitGiftData.getCostCoinType())) {
            LOGGER.info("元宝不足！");
            return;
        }
        Manager.currencyManager.manager().onDecItemCoin(player, allprice, ItemChangeReason.LimitShopActivetyDec, 0, limitGiftData.getCostCoinType());


        List<Item> itemList = new ArrayList<>();
        for (int i = 0; i < buyNum;i++){
            for (RewardData rewardData: limitGiftData.getRewardDatas()){
                if (Manager.activityManager.checkCareer(player, rewardData.getC())){
                    itemList.addAll(Item.createItems(rewardData.getI(), rewardData.getN(), rewardData.getB() == 1));
                }
            }
        }
        long actionId = IDConfigUtil.getLogId();
        if ( !Manager.backpackManager.manager().addItems(player,itemList,ItemChangeReason.LimitShopActivetyGet, actionId)){
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, itemList,ItemChangeReason.LimitShopActivetyGet, actionId);
        }
        buyInfo.put(buyID+"",allBuyNum);
        serverbuyInfo.put(buyID+"",allServerBuyNum);

        Manager.activityManager.deal().onReqActivity(player,actType);
        //保存角色活动数据
        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));
//        Manager.biManager.getScript().biActivity(player, ItemChangeReason.LimitShopActivetyGet, actType, actCfg.getId());
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.LimitShopActivity, ItemChangeReason.LimitShopActivetyGet, buyID, limitGiftData.getGiftName());
    }

    @Override
    public boolean parseCustomConfig(ActivityConfig actCfg, String customStr) {
        LimitShopData data = JsonUtils.parseObject(customStr,LimitShopData.class);
        actCfg.getCustomCfgMap().put(LimitShopDataStr,data);
        actCfg.getCustomCfgMap().put("client",data.getClient());



        return true;
    }

    @Override
    public boolean updateCustomConfig(ActivityConfig actCfg, String customStr) {
        parseCustomConfig(actCfg,customStr);
        return true;
    }

    @Override
    public String getActivityDataStr(ActivityConfig actCfg, long roleId) {

        int actType = actCfg.getType();
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(roleId, actType);
        ConcurrentHashMap<String, Object> actDataMap =   Manager.activityManager.deal().getActivityData(actType);
        actDataMap.putIfAbsent(ServerBuyNum, new  HashMap<String,Integer>());
        roleActDataMap.putIfAbsent(GiftBuyData,  new  HashMap<String,Integer>());
        HashMap<String,Integer> buyInfo =  (HashMap<String,Integer>) roleActDataMap.get(GiftBuyData);
        HashMap<String,Integer> serverbuyInfo = (HashMap<String,Integer>) actDataMap.get(ServerBuyNum);
        HashMap<String,Object> data = new HashMap<>();
        data.put(GiftBuyData,buyInfo);
        data.put(ServerBuyNum,serverbuyInfo);
        return JsonUtils.toJSONString(data);
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
    public void zeroClockDeal( ActivityConfig actCfg) {

    }

    @Override
    public void fiveClockDeal( ActivityConfig actCfg) {

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
    public void activityEndDeal( ActivityConfig actCfg) {

    }

    @Override
    public void consumeDeal(Player player, int coinType, int consumeNum, ActivityConfig actCfg) {

    }
    static class LimitShopData{

        private String client ;

        private HashMap<Integer,LimitShopTargetData> limitGiftDataHashMap;

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public HashMap<Integer, LimitShopTargetData> getLimitGiftDataHashMap() {
            return limitGiftDataHashMap;
        }

        public void setLimitGiftDataHashMap(HashMap<Integer, LimitShopTargetData> limitGiftDataHashMap) {
            this.limitGiftDataHashMap = limitGiftDataHashMap;
        }
    }
    static class LimitShopTargetData{

        private int id;//购买ID

        private int price;//购买价格

        private float discount;//折扣

        private String giftName;//礼包名字

        private int buyNum;//个人限购次数

        private int serverBuyNum;//全服限购次数

        private int costCoinType;//购买货币类型

        private List<RewardData> rewardDatas;//奖励


        public int getCostCoinType() {
            return costCoinType;
        }

        public void setCostCoinType(int costCoinType) {
            this.costCoinType = costCoinType;
        }

        public int getBuyNum() {
            return buyNum;
        }

        public void setBuyNum(int buyNum) {
            this.buyNum = buyNum;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public float getDiscount() {
            return discount;
        }

        public void setDiscount(float discount) {
            this.discount = discount;
        }

        public String getGiftName() {
            return giftName;
        }

        public void setGiftName(String giftName) {
            this.giftName = giftName;
        }

        public List<RewardData> getRewardDatas() {
            return rewardDatas;
        }

        public void setRewardDatas(List<RewardData> rewardDatas) {
            this.rewardDatas = rewardDatas;
        }

        public int getServerBuyNum() {
            return serverBuyNum;
        }

        public void setServerBuyNum(int serverBuyNum) {
            this.serverBuyNum = serverBuyNum;
        }
    }
}


