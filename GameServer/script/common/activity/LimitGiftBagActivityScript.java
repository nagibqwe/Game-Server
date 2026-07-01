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
 * 限购礼包
 * Created by 542 on 2020/8/28. 300004
 */
public class LimitGiftBagActivityScript  implements IActivityScript {

    private final static String LimitGiftBagDataStr = "limitGiftBagData";
    private final static String BuyNum = "buyNum";
    private final static String BuyID  = "buyId";
    private final static String GiftBuyData   = "giftBuyData";
    private static final Logger LOGGER = LogManager.getLogger(LimitGiftBagActivityScript.class);

    @Override
    public int getId() {
        return ScriptEnum.LimitedTGiftBagActivityScript;
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
        List<ActivityConfig> actCfgList = Manager.activityManager.deal().getActCfgListByActLogicID(ActivityType.LimitGiftBag);
        for (ActivityConfig activityConfig:actCfgList) {

            if (activityConfig == null) {
                return;
            }
            Object old = activityConfig.getCustomCfgMap().get(LimitGiftBagDataStr);
            if (old == null) {
                return;
            }
            String customStr = JsonUtils.toJSONString(old);
            LimitGiftBagData newData = JsonUtils.toJavaObject(customStr, LimitGiftBagData.class);
            activityConfig.getCustomCfgMap().put(LimitGiftBagDataStr, newData);
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

        HashMap<String,Integer> buyInfo =  (HashMap<String,Integer>)roleActDataMap.get(GiftBuyData);
        buyInfo.putIfAbsent(buyID+"",0);
        int alreadyBuyNum = buyInfo.get(buyID+"");


        LimitGiftBagData limitGiftBagData =  (LimitGiftBagData)actCfg.getCustomCfgMap().get(LimitGiftBagDataStr);
        if (!limitGiftBagData.getLimitGiftDataHashMap().containsKey(buyID)){
            LOGGER.error("购买ID 错误"   + buyID);
            return;
        }
        LimitGiftData limitGiftData = limitGiftBagData.getLimitGiftDataHashMap().get(buyID);

        int allBuyNum  = buyNum  + alreadyBuyNum;
        if (allBuyNum > limitGiftData.getBuyNum()){
            LOGGER.error("超过  最高购买次数 "   + allBuyNum);
            return;
        }
        int allprice = buyNum * limitGiftData.getPrice();
        //扣钱
        if (!Manager.currencyManager.manager().canDecItemCoin(player, allprice,limitGiftData.getCostCoinType())) {
            LOGGER.info("元宝不足！");
            return;
        }
        Manager.currencyManager.manager().onDecItemCoin(player, allprice, ItemChangeReason.LimitGiftBagAcitvityDec, 0, limitGiftData.getCostCoinType());


        List<Item> itemList = new ArrayList<>();
        for (int i =0;i < buyNum;i++){
            for (RewardData rewardData: limitGiftData.getRewardDatas()){
                if (Manager.activityManager.checkCareer(player, rewardData.getC())){
                    itemList.addAll(Item.createItems(rewardData.getI(), rewardData.getN(), rewardData.getB() == 1));
                }
            }
        }
        long actionId = IDConfigUtil.getLogId();
        if ( !Manager.backpackManager.manager().addItems(player,itemList,ItemChangeReason.LimitGiftBagAcitvityGet, actionId)){
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, itemList,ItemChangeReason.LimitGiftBagAcitvityGet, actionId);
        }
        buyInfo.put(buyID+"",allBuyNum);

        Manager.activityManager.deal().onReqActivity(player,actType);
        //保存角色活动数据
        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));

        //记录BI
//        Manager.biManager.getScript().biActivity(player, ItemChangeReason.LimitGiftBagAcitvityGet, actCfg.getType(), actCfg.getId());
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.LimitGiftBag, ItemChangeReason.LimitGiftBagAcitvityGet, limitGiftData.getId(), limitGiftData.getGiftName());
    }

    @Override
    public boolean parseCustomConfig(ActivityConfig actCfg, String customStr) {
        LimitGiftBagActivityScript.LimitGiftBagData data = JsonUtils.parseObject(customStr,LimitGiftBagActivityScript.LimitGiftBagData.class);
        actCfg.getCustomCfgMap().put(LimitGiftBagDataStr,data);
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

        roleActDataMap.putIfAbsent(GiftBuyData,  new  HashMap<String,Integer>() );
        HashMap<String,Integer> buyInfo =  (HashMap<String,Integer>) roleActDataMap.get(GiftBuyData);
        HashMap<String,Object> data = new HashMap<>();
        data.put(GiftBuyData,buyInfo);
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

    }

    @Override
    public void consumeDeal(Player player, int coinType, int consumeNum, ActivityConfig actCfg) {

    }
    static class LimitGiftBagData{

        private String client ;

        private HashMap<Integer,LimitGiftData> limitGiftDataHashMap;

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public HashMap<Integer, LimitGiftData> getLimitGiftDataHashMap() {
            return limitGiftDataHashMap;
        }

        public void setLimitGiftDataHashMap(HashMap<Integer, LimitGiftData> limitGiftDataHashMap) {
            this.limitGiftDataHashMap = limitGiftDataHashMap;
        }
    }
    static class LimitGiftData{

        private int id;//购买ID

        private int price;//购买价格

        private float discount;//折扣

        private String giftName;//礼包名字

        private int buyNum;//限购次数

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
    }
}


