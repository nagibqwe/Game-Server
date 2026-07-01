package common.activity;

import com.data.ItemChangeReason;
import com.data.MessageString;

import com.game.activity.script.IActivityScript;
import com.game.activity.struct.ActivityConfig;
import com.game.activity.struct.ActivityType;
import com.game.backpack.structs.Item;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.chat.structs.Notify;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import game.core.util.IDConfigUtil;
import game.core.util.JsonUtils;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by cxl on 2020/8/12. 300009
 */
public class GroupBuyActivityScript implements IActivityScript {

    private static final String GroupBuyData =  "GroupBuyData";
    private static final String BuyPrice = "buyPrice";
    private static final String BuyNum = "buyNum";
    private static final Logger LOGGER = LogManager.getLogger(GroupBuyActivityScript.class);

    @Override
    public void reload() {
        if(Manager.activityManager == null){
            return;
        }
        List<ActivityConfig> actCfgList = Manager.activityManager.deal().getActCfgListByActLogicID(ActivityType.GroupBuy);
        for (ActivityConfig activityConfig:actCfgList) {
            if (activityConfig == null) {
                return;
            }
            Object old = activityConfig.getCustomCfgMap().get(GroupBuyData);
            if (old == null) {
                return;
            }
            String customStr = JsonUtils.toJSONString(old);
            GroupBuyData newData = JsonUtils.toJavaObject(customStr, GroupBuyData.class);
            activityConfig.getCustomCfgMap().put(GroupBuyData, newData);
        }
    }

    @Override
    public int getId() {
        return ScriptEnum.GroupBuyActivityScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    /**
     * 解析活动自己的自定义配置
     */
   public boolean parseCustomConfig(ActivityConfig actCfg, String customStr){

       GroupBuyData data = JsonUtils.toJavaObject(customStr, GroupBuyData.class);
       actCfg.getCustomCfgMap().put(GroupBuyData,data);
       actCfg.getCustomCfgMap().put("client",data.getClient());
       return true;
   }

    /**
     * 活动配置更新处理
     */
    public boolean updateCustomConfig(ActivityConfig actCfg, String customStr){
        return  parseCustomConfig( actCfg,  customStr);
    }


    /**
     * 生成活动数据字符串
     */
   public  String getActivityDataStr(ActivityConfig actCfg, long roleId){

       int actType = actCfg.getType();
       ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(roleId, actType);
       ConcurrentHashMap<String, Object> actDataMap =   Manager.activityManager.getActDatas().get(actType);
       if (actDataMap == null){
           actDataMap = new  ConcurrentHashMap<String, Object>();
           Manager.activityManager.getActDatas().put(actType,actDataMap);
       }
       actDataMap.putIfAbsent(BuyNum,0);
       roleActDataMap.putIfAbsent(BuyPrice , 0);
       int buyPrice = (int)roleActDataMap.get(BuyPrice);
       HashMap<String,Object> groupData = new HashMap<>();
       groupData.put("isSelfBuy",buyPrice>0);
       groupData.put(BuyNum,actDataMap.get(BuyNum));
       return JsonUtils.toJSONString(groupData);
   }

    public void playerOnline(Player player, ActivityConfig actCfg){


    }
    /**
     * 0点处理
     */
    public void zeroClockPlayerDeal(Player player, ActivityConfig actCfg){


    }

    @Override
    public void fiveClockPlayerDeal(Player player, ActivityConfig actCfg) {

    }

    @Override
    public void zeroClockDeal(ActivityConfig actCfg) {
        if (actCfg == null){
            return;
        }
        int actType = actCfg.getType();
        if (!actCfg.getCustomCfgMap().containsKey(GroupBuyData)) {
            return;
        }
        ConcurrentHashMap<String, Object> actDataMap =   Manager.activityManager.getActDatas().get(actType);
        if (actDataMap == null){
            actDataMap = new ConcurrentHashMap<String, Object>();
            Manager.activityManager.getActDatas().put(actType,actDataMap);
        }
        int buyNum = 0;
        actDataMap.putIfAbsent(BuyNum,0);
        buyNum =  (int)actDataMap.get(BuyNum);
        GroupBuyData groupBuyData = (GroupBuyData)actCfg.getCustomCfgMap().get(GroupBuyData);
        HashMap<Integer,Object[]> countDiscountList = groupBuyData.getCountDiscountList();
        List<Integer> numList = new ArrayList<Integer>(countDiscountList.keySet());
        Collections.sort(numList);
        int maxDisNum = numList.get(numList.size() -1);


        int startDay =  TimeUtils.getCurDayByTime(actCfg.getBeginTime());
        int curDay   =  TimeUtils.getCurDayByTime(TimeUtils.Time());
        int overDay  =  TimeUtils.getCurDayByTime(actCfg.getEndTime());
        int intervalDay =  curDay - startDay;
        float continuousDay = (overDay - startDay)-1;//活动持续天数
        if (intervalDay <= continuousDay){
            int everyDayAdd =  continuousDay <=0 ? maxDisNum: (int) Math.ceil(maxDisNum /continuousDay);
            int needAdd =   intervalDay * everyDayAdd;

            buyNum = buyNum < needAdd?needAdd:buyNum;
            actDataMap.put(BuyNum,buyNum);
        }
        //保存活动数据
        Manager.activityManager.deal().saveActData(actCfg.getType(), actDataMap);
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
        if (actCfg == null){
            return;
        }
        int actType = actCfg.getType();
        if (!actCfg.getCustomCfgMap().containsKey(GroupBuyData)) {
            return;
        }
        ConcurrentHashMap<String, Object> actDataMap =   Manager.activityManager.getActDatas().get(actType);
        if (actDataMap == null){
            actDataMap = new ConcurrentHashMap<String, Object>();
            Manager.activityManager.getActDatas().put(actType,actDataMap);
        }
        actDataMap.putIfAbsent(BuyNum,0);
        GroupBuyData groupBuyData = (GroupBuyData)actCfg.getCustomCfgMap().get(GroupBuyData);
        HashMap<Integer,Object[]> countDiscountList = groupBuyData.getCountDiscountList();
        int costCoinType =  groupBuyData.getCostCoinType();
        List<Integer> numList = new ArrayList<Integer>(countDiscountList.keySet());
        Collections.sort(numList);
        int minPrice = 999999999;
        for ( Object[] priceArr:countDiscountList.values()){
            int pc = (int)priceArr[1];
            if (pc < minPrice){
                minPrice =  pc;
            }
        }
        //返钱
        returnMoney( actType, costCoinType, minPrice,actCfg);
        actDataMap.put(BuyNum,0);

    }

    public void consumeDeal(Player player,int coinType, int consumeNum, ActivityConfig actCfg){

    }


    private void returnMoney(int actType,int costCoinType,int minPrice, ActivityConfig actCfg){
        //返钱
        for (Map.Entry<Long, ConcurrentHashMap<Integer, ConcurrentHashMap<String, Object>>> entry: Manager.activityManager.getRoleActDatas().entrySet()){
            ConcurrentHashMap<String, Object> roleActData =   entry.getValue().get(actType);
            if (roleActData == null){
                continue;
            }
            if (!roleActData.containsKey(BuyPrice)){
                continue;
            }
            int buyPrice =  (int)roleActData.get(BuyPrice);
            int retrunMoney =  buyPrice - minPrice <=0 ? 0: buyPrice - minPrice;

            if (retrunMoney>0){
                List<Item> itemList = Item.createItems(costCoinType, retrunMoney, false);
                String itemName =   Manager.backpackManager.manager().getName(costCoinType) ;
                String content = MessageString.Group_Buy_Reward_Mail +
                        "@_@" + buyPrice + "@_@" +itemName +"@_@" + minPrice +
                        "@_@" + itemName + "@_@" +  retrunMoney +"@_@"+ itemName;

                Manager.mailManager.sendMailToPlayer(entry.getKey(), MessageString.System, MessageString.System,
                        MessageString.Group_Buy_Reward_Mail_Title,content, itemList,ItemChangeReason.GroupBuyActivityReturnGet);
            }

            roleActData.put(BuyPrice,0);
            entry.getValue().put(actType, roleActData);
            //保存玩家数据
            Manager.activityManager.deal().saveRoleActData(entry.getKey(), Manager.activityManager.getRoleActDatas().get(entry.getKey()));
        }
    }

    /**
     * 充值后的处理
     * @param player
     */
   public void rechargeDeal(Player player, int getGoodsCfgId,int rechargeNum, ActivityConfig actCfg){

   }

    public void onReqActivityDeal(Player player, String dataStr, ActivityConfig actCfg){
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

        //---查询自己购买情况
        ConcurrentHashMap<Integer, ConcurrentHashMap<String, Object>> roleActDataMap= Manager.activityManager.getRoleActDatas().get(player.getId());
        if(roleActDataMap == null){
            roleActDataMap = new ConcurrentHashMap<>();
            Manager.activityManager.getRoleActDatas().put(player.getId(), roleActDataMap);
            return;
        }
        ConcurrentHashMap<String, Object> roleActData = roleActDataMap.get(actType);
        if(roleActData == null){
            roleActData = new ConcurrentHashMap<>();
            roleActDataMap.put(actType, roleActData);
            return;
        }
        roleActData.putIfAbsent(BuyPrice , 0);
        int isBuy = (int)roleActData.get(BuyPrice);
        if (isBuy > 0){
            LOGGER.error("已经购买");
            return;
        }

        GroupBuyData groupBuyData = (GroupBuyData)actCfg.getCustomCfgMap().get(GroupBuyData);
        HashMap<Integer,Object[]> countDiscountList =  groupBuyData.getCountDiscountList();
        int itemID =  groupBuyData.getItemId().get((int)player.getCareer());
        int itemNum = groupBuyData.getItemNum();
        int costCoinType =  groupBuyData.getCostCoinType();
        int oriPrice =groupBuyData.getOriPrice();


        //---查询活动购买数量
        ConcurrentHashMap<String, Object> actDataMap =   Manager.activityManager.getActDatas().get(actType);
        if (actDataMap == null){
            actDataMap = new ConcurrentHashMap<String, Object>();
            Manager.activityManager.getActDatas().put(actType,actDataMap);
        }
        actDataMap.putIfAbsent(BuyNum, 0);
        int buyNum = (int)actDataMap.get(BuyNum);
        int maxNum = 0;
        for (Map.Entry<Integer,Object[]> entry: countDiscountList.entrySet()){
            int needNum = entry.getKey();
            if (buyNum >= needNum && needNum >= maxNum){
                oriPrice = (int)entry.getValue()[1];
                maxNum = needNum;
            }
        }
        //算折扣
        if (!Manager.currencyManager.manager().canDecItemCoin(player, oriPrice,costCoinType)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.MyMoneyNotEnough);
            return;
        }
        Manager.currencyManager.manager().onDecItemCoin(player, oriPrice, ItemChangeReason.GroupBuyActivityDec, 0, costCoinType);
        List<Item> itemList = Item.createItems(itemID, itemNum, true);
        long actionId = IDConfigUtil.getLogId();
        if ( !Manager.backpackManager.manager().addItems(player,itemList,ItemChangeReason.GroupBuyActivityGet, actionId)){
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, itemList,ItemChangeReason.GroupBuyActivityGet, actionId);
        }

        buyNum++;
        actDataMap.put(BuyNum,buyNum);
        roleActData.put(BuyPrice,oriPrice);
        roleActDataMap.put(actType, roleActData);

        Manager.activityManager.deal().onReqActivity(player,actType);
        //保存玩家数据
        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));
        //保存活动数据
        Manager.activityManager.deal().saveActData(actCfg.getType(), roleActData);
        //记录BI
//        Manager.biManager.getScript().biActivity(player, ItemChangeReason.GroupBuyActivityGet, actType, actCfg.getId());
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.GroupBuy, ItemChangeReason.GroupBuyActivityGet);
    }
    static class  GroupBuyData{

        private String client ;

        private  HashMap<Integer,Integer> itemId ;

        private int itemNum;

        private int costCoinType;

        private int oriPrice;

        private  HashMap<Integer,Object[]> countDiscountList;

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public HashMap<Integer, Integer> getItemId() {
            return itemId;
        }

        public void setItemId(HashMap<Integer, Integer> itemId) {
            this.itemId = itemId;
        }

        public int getItemNum() {
            return itemNum;
        }

        public void setItemNum(int itemNum) {
            this.itemNum = itemNum;
        }

        public int getCostCoinType() {
            return costCoinType;
        }

        public void setCostCoinType(int costCoinType) {
            this.costCoinType = costCoinType;
        }

        public int getOriPrice() {
            return oriPrice;
        }

        public void setOriPrice(int oriPrice) {
            this.oriPrice = oriPrice;
        }

        public HashMap<Integer, Object[]> getCountDiscountList() {
            return countDiscountList;
        }

        public void setCountDiscountList(HashMap<Integer, Object[]> countDiscountList) {
            this.countDiscountList = countDiscountList;
        }
    }
}
