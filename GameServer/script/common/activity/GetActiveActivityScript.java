package common.activity;

import com.data.ItemChangeReason;
import com.data.MessageString;
import com.game.activity.manager.ActivityManager;

import com.game.activity.script.IActivityScript;
import com.game.activity.struct.ActivityConfig;
import com.game.activity.struct.ActivityType;
import com.game.activity.struct.RewardData;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.chat.structs.Notify;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import game.core.json.TypeReference;
import game.core.util.IDConfigUtil;
import game.core.util.JsonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gaozhaoguang
 * @desc 活跃兑换奖励 300001
 * @date Created on 2020/9/8 20:31
 **/
public class GetActiveActivityScript implements IActivityScript {
    //日志打印
    private static final Logger logger = LogManager.getLogger(GetActiveActivityScript.class);
    //isGets默认的数据
    private static final HashMap<Integer,Integer> isGetsDefaultIValue = new HashMap<>();

    //保存到活动管理器上的主Key
    private static final String CN_MAIN_KEY = "getactive";
    //发送给客户端的数据
    private static final String CN_RES_CLIENT_KEY = "client";
    //客户端请求的数据中的请求方法
    private static final String CN_C_REQUEST_KEY = "request";
    //客户端发送请求的奖励的
    private static final String CN_C_REQ_AWARD_KEY = "reqAward";
    //客户端发送请求的奖励的参数1
    private static final String CN_C_REQ_AWARD_P1_KEY = "coinNum";
    //玩家记录的活动数据之是否领取奖励
    private static final String CN_P_IS_GETS_KEY = "isGets";
    //玩家记录的活动数据之活跃货币的数量
    private static final String CN_P_HAVE_COIN_NUM_KEY = "haveNum";


    @Override
    public void onReqActivityDeal(Player player, String dataStr, ActivityConfig actCfg) {
        //logger.error("onReqActivityDeal:"+dataStr);
        //msg:{'request':'reqAward','coinNum':0}
        ConcurrentHashMap<String, Object> data = JsonUtils.parseObject(dataStr, new TypeReference<ConcurrentHashMap<String, Object>>() {});
        String reqMethod = Utils.getOrDefaultFromMap(data,CN_C_REQUEST_KEY, "");
        if (reqMethod.equals(CN_C_REQ_AWARD_KEY)) {
            int coinNum = Utils.getOrDefaultFromMap(data,CN_C_REQ_AWARD_P1_KEY,0);
            onReqAwardHandler(player,coinNum, actCfg);
        } else {
            logger.error("接受未知请求:" + reqMethod);
        }
    }

    @Override
    public boolean parseCustomConfig(ActivityConfig actCfg, String customStr) {
        logger.error(customStr);
        GetActiveConfig data = JsonUtils.toJavaObject(customStr, GetActiveConfig.class);
        actCfg.getCustomCfgMap().put(CN_MAIN_KEY, data);
        actCfg.getCustomCfgMap().put(CN_RES_CLIENT_KEY, data.getClient());
        return true;
    }

    @Override
    public boolean updateCustomConfig(ActivityConfig actCfg, String customStr) {
        parseCustomConfig(actCfg,customStr);
        return false;
    }

    @Override
    public String getActivityDataStr(ActivityConfig actCfg, long roleId) {

        //判断玩家是否在线
        Player player = Manager.playerManager.getPlayerOnline(roleId);
        if (player == null) {
            logger.error("getActivityDataStr:玩家已经离线!roleID:" + roleId);
            return "";
        }
        //获取玩家在线活动信息发送
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(roleId,actCfg.getType());
        HashMap<String, Object> groupData = new HashMap<>();
        groupData.put(CN_P_IS_GETS_KEY, Utils.getOrDefaultFromMap(roleActDataMap,CN_P_IS_GETS_KEY,isGetsDefaultIValue));
        groupData.put(CN_P_HAVE_COIN_NUM_KEY, Utils.getOrDefaultFromMap(roleActDataMap,CN_P_HAVE_COIN_NUM_KEY,0));
        return JsonUtils.toJSONString(groupData);
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
    public void activityEndDeal(ActivityConfig actCfg) {

        //清理活动货币
        for(Long roleID:Manager.activityManager.getRoleActDatas().keySet()) {
            Player player = Manager.playerManager.getPlayer(roleID);
            long coin = Manager.currencyManager.manager().getCurrencyNum(player,ItemCoinType.ActiveCoin);
            Manager.currencyManager.manager().onDecItemCoin(player, coin, ItemChangeReason.GetActiveCoinActiveActivity, IDConfigUtil.getLogId(), ItemCoinType.ActiveCoin);
        }
    }

    @Override
    public void consumeDeal(Player player, int coinType, int consumeNum, ActivityConfig actCfg) {
        if (actCfg == null || !actCfg.isActiviting()) {
            return;
        }
        GetActiveConfig cfg = (GetActiveConfig)actCfg.getCustomCfgMap().get(CN_MAIN_KEY);
        if(cfg == null){
            return;
        }
        if(!cfg.getCoinRatio().containsKey(coinType)){
            return;
        }
        int ratio = cfg.getCoinRatio().get(coinType);
        Manager.currencyManager.manager().onAddItemCoin(player, ItemCoinType.ActiveCoin, consumeNum * ratio, ItemChangeReason.GetActiveCoinActiveActivity, IDConfigUtil.getLogId());

    }

    @Override
    public void reload() {
        if(Manager.activityManager == null){
            return;
        }
        List<ActivityConfig> actCfgList = Manager.activityManager.deal().getActCfgListByActLogicID(ActivityType.GetActive);
        for (ActivityConfig activityConfig:actCfgList) {
            if (activityConfig == null) {
                return;
            }
            Object obj = activityConfig.getCustomCfgMap().get(CN_MAIN_KEY);
            if (obj != null) {
                String customStr = JsonUtils.toJSONString(obj);
                GetActiveConfig newData = JsonUtils.toJavaObject(customStr, GetActiveConfig.class);
                activityConfig.getCustomCfgMap().put(CN_MAIN_KEY, newData);
            }
        }
    }

    @Override
    public int getId() {
        return ScriptEnum.GetActiveActivityScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    //region 私有方法,内部定义方法

    /**
     * 领取某个活跃数的奖励
     * @param player
     * @param reqNum
     */
    private void onReqAwardHandler(Player player, Integer reqNum, ActivityConfig actCfg) {
        //判断活动是否开启
        if (!checkActivityValid( actCfg)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.S_ACTIVITY_INVALID);
            return;
        }

        //2.获取玩家的活动数据
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(),actCfg.getType());

        //3.判断当前领取的阶段是否正确
        int holdNum = Manager.currencyManager.manager().getCurrencyIntNum(player,ItemCoinType.ActiveCoin);
        if(reqNum > holdNum){
            logger.error("onReqAwardHandler:请求的活跃货币数量大于持有的货币数量,非法领取奖励! holdNum=" + holdNum + "  reqNum=" + reqNum);
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.S_GETACTIVE_COIN_NOT_ENOUGH);
            return;
        }

        //4.判断是否已经领取
        HashMap<Integer,Integer> isGetData = Utils.getOrDefaultFromMap(roleActDataMap,CN_P_IS_GETS_KEY,null);
        if(isGetData == null){
            isGetData = new HashMap<>();
            roleActDataMap.put("isGets",isGetData);
        }
        boolean isGet = false;
        if(isGetData.containsKey(reqNum)){
            isGet = isGetData.get(reqNum) > 0;
        }
        if(isGet){
            logger.error("onReqAwardHandler:奖励已经领取!");
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.S_AWARD_HAVE_GETED);
            return;
        }

        //5.判断配置是否有效
        GetActiveConfig cfg = (GetActiveConfig) actCfg.getCustomCfgMap().get(CN_MAIN_KEY);
        if(cfg == null || cfg.awardData == null){
            logger.error("onReqAwardHandler:活动配置数据中奖励错误!cfg == null || cfg.awardList == null");
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.S_UNKNOW_ERROR);
            return;
        }

        //6.判断当前请求的数量是否对应奖励
        RewardItemsData itemsData = cfg.awardData.getOrDefault(reqNum,null);
        if(itemsData == null || itemsData.awardList == null || itemsData.awardList.size()==0){
            logger.error("onReqAwardHandler:请求奖励失败,当前档次没有配置奖励数据!reqNum="+reqNum);
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.S_UNKNOW_ERROR);
            return;
        }

        //7.整理奖励物品,判断背包是否有足够空间
        List<Item> items = new ArrayList<>();
        for (RewardData rd : itemsData.awardList) {
            if (rd.getC() == player.getCareer() || rd.getC() == 9) {
                items.add(Item.createItem(rd.getI(), rd.getN(), rd.getB() > 0));
            }
        }
        if (Manager.backpackManager.manager().onHasAddSpaces(player, items) != 0) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.NoBagCell);
            return;
        }

        //8.设置发奖标记
        isGetData.put(reqNum,1);

        //9.开始发放奖励
        List<Item> itemList = Item.clone(items);
        Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.GetActiveActivityGet, IDConfigUtil.getLogId());

        //10.同步给用户,活动数据改变
        Manager.activityManager.deal().sendActivityDataChange(player, actCfg.getType());
        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));

        //11.记录BI数据
//        Manager.biManager.getScript().biActivity(player, ItemChangeReason.GetActiveActivityGet, actCfg.getType(), actCfg.getId());
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.GetActive, ItemChangeReason.GetActiveActivityGet, reqNum);
    }
    /**
     * 判断活动是否还有效
     *
     * @return
     */
    private boolean checkActivityValid(ActivityConfig actCfg) {
        if (actCfg == null) {
            return false;
        }
        if (!actCfg.isActiviting()) {
            logger.error("ActivityConfig is stop: " + actCfg.getType());
            return false;
        }
        return true;
    }
    //endregion

    //region 内部定义的子类

    /**
     * 活跃兑换的配置数据,只定义在脚本内部
     */
    private static class GetActiveConfig{
        //奖励列表
        private ConcurrentHashMap<Integer,RewardItemsData> awardData;

        //货币转换比率
        private ConcurrentHashMap<Integer,Integer> coinRatio;

        //客户端展示数据
        private String client ;

        public ConcurrentHashMap<Integer, RewardItemsData> getAwardData() {
            return awardData;
        }

        public void setAwardData(ConcurrentHashMap<Integer, RewardItemsData> awardData) {
            this.awardData = awardData;
        }

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public ConcurrentHashMap<Integer, Integer> getCoinRatio() {
            return coinRatio;
        }

        public void setCoinRatio(ConcurrentHashMap<Integer, Integer> coinRatio) {
            this.coinRatio = coinRatio;
        }
    }

    private static class RewardItemsData{
        //奖励物品列表
        private List<RewardData> awardList;
        //展示的物品
        private int showItem;

        public List<RewardData> getAwardList() {
            return awardList;
        }

        public void setAwardList(List<RewardData> awardList) {
            this.awardList = awardList;
        }

        public int getShowItem() {
            return showItem;
        }

        public void setShowItem(int showItem) {
            this.showItem = showItem;
        }
    }

    //endregion

    //region //测试代码
    //测试奖励领取
    public static void testAward(Player player, int num) {
        ActivityManager.getInstance().deal().onReqActivityDeal(player, ActivityManager.getInstance().deal().toActType(ActivityType.GetActive ,0), "{'request':'reqAward','coinNum':" + num + "}");
    }
    //endregion
}
