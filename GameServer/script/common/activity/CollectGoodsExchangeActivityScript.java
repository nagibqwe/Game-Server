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
 * Обмен предметов 300008
 * Created by cxl on 2020/9/9.
 */
public class CollectGoodsExchangeActivityScript implements IActivityScript {
    private static final String CollectGoodsDataStr = "collectGoodsData";
    private static final String ExChangeList = "exChangeList";//Данные об уже совершённых обменах для клиента
    private static final String ExChange ="exChange"; //Обмен
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
            LOGGER.error("Время активности истекло"  + actType);
            return;
        }
        int exChangeID = (int)dailyMap.get(ExChange);
        int exNum = (int)dailyMap.get("num");
        if (exNum < 1 ){
            LOGGER.error("Количество обмена равно 0");
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
            LOGGER.error("Данные обмена пусты");
            return;
        }
        CollectGoodsData collectGoodsData = (CollectGoodsData)actCfg.getCustomCfgMap().get(CollectGoodsDataStr);

        if (!collectGoodsData.getExChangeDataMap().containsKey(exChangeID)){
            LOGGER.error("Нет такого типа обмена"  + exChangeID);
            return;
        }
        int exChangeM  = collectGoodsData.exChangeMaterialsId;
        ExChangeData exChangeData = collectGoodsData.getExChangeDataMap().get(exChangeID);
        int exChangeN  = exChangeData.exChangePrice * exNum;
        int maExChangeN = exChangeData.getExChangeTimes();
        if (maExChangeN != 0 && (alreadExChangeNum +  exNum) > maExChangeN){
            LOGGER.error("Лимит обмена исчерпан"  + alreadExChangeNum);
            return;
        }
        if (!Manager.backpackManager.manager().onRemoveItem(player,exChangeM, exChangeN, ItemChangeReason.CollectGoodsExchangeDel, IDConfigUtil.getLogId())){
            LOGGER.error("Недостаточно материалов"  + exChangeM);
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

        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.CollectGoodsExchange, ItemChangeReason.CollectGoodsExchangeGet, exChangeID);
        exChangeList.put(exChangeID,alreadExChangeNum + exNum);

        Manager.activityManager.deal().onReqActivity(player,actType);
        //Сохранение данных активности игрока
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
     * Выпадение с босса
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
     * Выпадение из сундука
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
     * Выпадение в подземелье
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
               LOGGER.error("Игрок не найден ");
               continue;
           }
           int hasNum =    Manager.backpackManager.manager().getItemNum(player,exChangeMaterialsId);
           if (hasNum>0){
               long action = IDConfigUtil.getLogId();
               if ( !Manager.backpackManager.manager().onRemoveItem(player,exChangeMaterialsId,hasNum,ItemChangeReason.CollectGoodsExchangeDel,action)){
                   LOGGER.error("Ошибка списания материалов " + returnCoinType);
                   continue;
               }
               int returnAll = returnNum  * hasNum;
               List<Item> itemList = new ArrayList<>();
               itemList.addAll(Item.createItems(returnCoinType, returnAll, true));
               Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                       MessageString.Collect_Goods_Reward_Mail_Title, MessageString.Collect_Goods_Reward_Mail, itemList, ItemChangeReason.CollectGoodsExchangeGet, action);

               Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.CollectGoodsExchange, ItemChangeReason.CollectGoodsExchangeGet, returnCoinType);
           }
        }
    }

    @Override
    public void consumeDeal(Player player, int coinType, int consumeNum, ActivityConfig actCfg) {

    }
    static class CollectGoodsData{

        private String client ;

        private int exChangeMaterialsId;//Материалы для обмена

        private int returnMoneyCoinType;//Тип возвращаемой валюты

        private int returnMoneyCoinNum;//Количество возвращаемой валюты за 1 материал

        //KEY ID товара
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
        private int exChangeTimes;//Лимит обмена, 0 - безлимитно

        private int exChangePrice;//Цена обмена

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