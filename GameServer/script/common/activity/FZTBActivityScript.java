package common.activity;


import com.data.ItemChangeReason;
import com.data.MessageString;
import com.game.activity.script.IActivityScript;
import com.game.activity.struct.ActivityConfig;
import com.game.activity.struct.ActivityType;

import com.game.activity.struct.RewardData;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.chat.structs.Notify;
import com.game.db.bean.ActivityConfigBean;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;

import com.game.server.DbSqlName;
import com.game.utils.MessageUtils;
import game.core.json.TypeReference;
import game.core.util.IDConfigUtil;
import game.core.util.JsonUtils;
import game.core.util.TimeUtils;
import game.core.util.WeightCalc;
import game.message.ActivityMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hebin
 * @desc 芳泽探宝 29000
 * @date Created on 2021/11/30 18:06
 **/
public class FZTBActivityScript implements IActivityScript {
    private static final Logger logger = LogManager.getLogger(FZTBActivityScript.class);
    private static final String configData = "configData";
    final transient String Client = "client";


    private static final String mapDataMapKey = "mapDataMap";
    private static final String openListKey = "openList";

    private static final String receiveRewardListKey = "receiveRewardList";

    private static final String consumeMapKey = "consumeMap";
    @Override
    public int getId() {
        return ScriptEnum.FZTBActivityScript;
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
        List<ActivityConfig> actCfgList = Manager.activityManager.deal().getActCfgListByActLogicID(ActivityType.FZTB);
        for (ActivityConfig activityConfig:actCfgList) {
            if (activityConfig == null) {
                return;
            }
            Object old = activityConfig.getCustomCfgMap().get(configData);
            if (old == null) {
                return;
            }
            String customStr = JsonUtils.toJSONString(old);
            FZTBActivityScript.FZTBActivity newData = JsonUtils.toJavaObject(customStr, FZTBActivityScript.FZTBActivity.class);
            activityConfig.getCustomCfgMap().put(configData, newData);
        }
    }

    @Override
    public void onReqActivityDeal(Player player, String dataStr, ActivityConfig actCfg) {
        logger.error("onReqActivityDeal:"+dataStr);
        ActivityConfig activityConfig = actCfg;
        if (activityConfig == null) {
            logger.error("方泽探宝活动配置不存在");
            return;
        }
        int actType = actCfg.getType();
        FZTBActivityScript.FZTBActivity fZTBActivity = ( FZTBActivityScript.FZTBActivity) activityConfig.getCustomCfgMap().get(configData);
        if (fZTBActivity == null) {
            logger.error("方泽探宝活动自定义配置参数不存在");
            return;
        }
        HashMap<String, Integer> msg = JsonUtils.parseObject(dataStr, new TypeReference<HashMap<String, Integer>>() {});
        int operate = msg.get("operate"); //1=抽奖 2 = 重置
        if (operate == 1) {//抽奖
            Integer mapIndex = msg.get("mapIndex");
            if (mapIndex == null) {
                logger.error("方泽探宝活动请求抽奖参数 mapIndex 为空");
                return;
            }
            Integer cellIndex = msg.get("cellIndex");
            if (cellIndex == null) {
                logger.error("方泽探宝活动请求抽奖参数 cellIndex 为空");
                return;
            }
            //下标溢出
            if(mapIndex<0 || mapIndex >= fZTBActivity.FZTBMapList.size()){
                return;
            }
            //得到图的配置
            FZTBMapBean fZTBMapBean = fZTBActivity.FZTBMapList.get(mapIndex);


            //玩家数据
            ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(), actType);
            if (roleActDataMap == null){
                return;
            }
            //roleActDataMap.putIfAbsent(mapDataMapKey,new HashMap<Integer,HashMap<String,Object>>());



            //抽奖图数据
            HashMap<Integer,HashMap<String,Object>> mapDataMap = null;
            if(!roleActDataMap.containsKey(mapDataMapKey)){
                mapDataMap = new HashMap<Integer,HashMap<String,Object>>();
                roleActDataMap.put(mapDataMapKey,mapDataMap);
            }else {
                mapDataMap = (HashMap<Integer,HashMap<String,Object>>)roleActDataMap.get(mapDataMapKey);
            }
            //得到 当前图的 记录
            HashMap<String,Object> mapIndexData = getMapIndexData(mapDataMap,mapIndex);

            //得到开启列表
            List<Integer> openList = getMapIndexOpenList(mapIndexData);
            //已经开启该保险了
            if(openList.contains(cellIndex)){
                return;
            }
            //领取记录
            List<Integer> receiveRewardList = getMapIndexReceiveRewardList(mapIndexData);

            //抽奖道具id
            int drawItemId = fZTBActivity.drawItemId;


            Map<Integer,Integer> consumeMap = getMapIndexConsumeMapKey(mapIndexData);

            int drawIndex =  consumeMap.get(cellIndex);

            //抽奖道具消耗数量
            int drawItemNum = fZTBMapBean.costNumList.get(drawIndex);

            //判断是否充足
//            if(!Manager.backpackManager.manager().canDeleteItemNum(player, drawItemId, drawItemNum)){
//                return;
//            }
            //个数
            int remainCount = Manager.backpackManager.manager().getItemNum(player, drawItemId);
            long coin = Manager.currencyManager.manager().getCurrencyNum(player, ItemCoinType.GemCoin);
            int needLiyu = 0;
            int needItem = 0;
            if(remainCount < drawItemNum){
                needLiyu = (drawItemNum - remainCount) * fZTBActivity.drawItemNeedLingyu;
                needItem = remainCount;
            }else {
                needItem = drawItemNum;

            }
            //灵玉不够
            if(needLiyu > coin){
                return;
            }


            //奖池
            WeightCalc<FZTBMapRewardWeightCalc> weightCalc = new WeightCalc<>();
            //权重
            for(int i = 0;i< fZTBMapBean.FZTBMapRewardBeanList.size();i++){
                //已经领取的 不进入奖池
                if(receiveRewardList.contains(i)){
                    continue;
                }
                //最低抽奖次数
                if( receiveRewardList.size() >= fZTBMapBean.FZTBMapRewardBeanList.get(i).minDrawNum ){
                    FZTBMapRewardWeightCalc calc = new FZTBMapRewardWeightCalc();
                    calc.index = i;
                    calc.reward = fZTBMapBean.FZTBMapRewardBeanList.get(i);
                    weightCalc.addObject(calc,fZTBMapBean.FZTBMapRewardBeanList.get(i).weight);
                }
            }
            FZTBMapRewardWeightCalc fZTBMapRewardWeightCalc =  weightCalc.getObject();
            //7.整理奖励物品,判断背包是否有足够空间
            List<Item> items = new ArrayList<>();
            for (RewardData rd : fZTBMapRewardWeightCalc.reward.reward) {
                if (rd.getC() == player.getCareer() || rd.getC() == 9) {
                    items.add(Item.createItem(rd.getI(), rd.getN(), rd.getB() > 0));
                }
            }
            if (Manager.backpackManager.manager().onHasAddSpaces(player, items) != 0) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.NoBagCell);
                return;
            }
            //发放奖励
            Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.FZTBActivityGet, IDConfigUtil.getLogId());

            long logId = IDConfigUtil.getLogId();
            if(needItem>0){
                //扣除道具
                Manager.backpackManager.manager().onRemoveItem(player, drawItemId, needItem, ItemChangeReason.FZTBActivityDec, logId);
            }

            if(needLiyu>0){
                Manager.currencyManager.manager().decGold(player, needLiyu, ItemChangeReason.FZTBActivityDec, logId);
            }

            //记录抽过的奖励下标
            receiveRewardList.add(fZTBMapRewardWeightCalc.index);
            //记录抽奖展示打开
            openList.add(cellIndex);

            if(receiveRewardList.size()>= fZTBMapBean.openNextMapDrawNum){
                mapIndexData.put("isOpenNextMap",true);
            }

            //抽奖道具奖励
            if(receiveRewardList.size() == fZTBMapBean.FZTBMapRewardBeanList.size())
            {
                List<Item> itemsx = new ArrayList<>();
                Item item = Item.createItem(drawItemId, fZTBMapBean.allFinishDrawItemNum,true);
                itemsx.add(item);
                Manager.backpackManager.manager().addItems(player, itemsx, ItemChangeReason.FZTBActivityDraw9Get, IDConfigUtil.getLogId());
            }
           // result.put("cellIndex",  msg.get("cellIndex"));
            //返给前端
            sendResActivityDeal(mapIndexData,player,msg,actCfg);

        } else if (operate == 2) { //重置本图
            Integer mapIndex = msg.get("mapIndex");
            if (mapIndex == null) {
                logger.error("方泽探宝活动请求抽奖参数 mapIndex 为空");
                return;
            }

            //下标溢出
            if(mapIndex<0 || mapIndex >= fZTBActivity.FZTBMapList.size()){
                return;
            }
            //玩家数据
            ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(), actType);
            if (roleActDataMap == null){
                return;
            }
            //抽奖图数据
            HashMap<Integer,HashMap<String,Object>> mapDataMap = (HashMap<Integer,HashMap<String,Object>>)roleActDataMap.get("mapDataMap");

            //得到 当前图的 记录
            HashMap<String,Object> mapIndexData = getMapIndexData(mapDataMap,mapIndex);
            //得到图的配置
            FZTBMapBean fZTBMapBean = fZTBActivity.FZTBMapList.get(mapIndex);
            //得到开启列表
            List<Integer> openList = getMapIndexOpenList(mapIndexData);
            // 必须全部开启了
            if(openList.size()  < fZTBMapBean.FZTBMapRewardBeanList.size()){
                return;
            }

            long coin = Manager.currencyManager.manager().getCurrencyNum(player, ItemCoinType.GemCoin);
            int needLiyu =fZTBMapBean.resetNeedlingyu;
            //灵玉不够
            if(needLiyu > coin){
                return;
            }

            //领取记录
            List<Integer> receiveRewardList = getMapIndexReceiveRewardList(mapIndexData);
            receiveRewardList.clear();
            openList.clear();

            long logId = IDConfigUtil.getLogId();
            if(needLiyu>0){
                Manager.currencyManager.manager().decGold(player, needLiyu, ItemChangeReason.FZTBActivityResetDrawMapDec, logId);
            }

            //返给前端
            sendResActivityDeal(mapIndexData,player,msg,actCfg);

        }else if (operate == 3) { //查看宝箱 消耗次数操作
            Integer mapIndex = msg.get("mapIndex");
            if (mapIndex == null) {
                logger.error("方泽探宝活动请求抽奖参数 mapIndex 为空");
                return;
            }
            Integer cellIndex = msg.get("cellIndex");
            if (cellIndex == null) {
                logger.error("方泽探宝活动请求抽奖参数 cellIndex 为空");
                return;
            }
            //下标溢出
            if(mapIndex<0 || mapIndex >= fZTBActivity.FZTBMapList.size()){
                return;
            }
            //玩家数据
            ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(), actType);
            if (roleActDataMap == null){
                return;
            }

            //抽奖图数据
            HashMap<Integer,HashMap<String,Object>> mapDataMap = null;
            if(!roleActDataMap.containsKey(mapDataMapKey)){
                mapDataMap = new HashMap<Integer,HashMap<String,Object>>();
                roleActDataMap.put(mapDataMapKey,mapDataMap);
            }else {
                mapDataMap = (HashMap<Integer,HashMap<String,Object>>)roleActDataMap.get(mapDataMapKey);
            }
            //抽奖图数据
            mapDataMap = (HashMap<Integer,HashMap<String,Object>>)roleActDataMap.get("mapDataMap");

            //得到 当前图的 记录
            HashMap<String,Object> mapIndexData = getMapIndexData(mapDataMap,mapIndex);
            Map<Integer,Integer> consumeMap = getMapIndexConsumeMapKey(mapIndexData);
            if(consumeMap.containsKey(cellIndex)){
                return;
            }
            consumeMap.put(cellIndex,consumeMap.size());
            sendResActivityDeal(mapIndexData,player,msg,actCfg);
        }
    }

    private void sendResActivityDeal(HashMap<String,Object> mapIndexData,Player player, HashMap<String, Integer> msg, ActivityConfig actCfg){

        //得到消耗列表
        Map<Integer,Integer> consumeMap = getMapIndexConsumeMapKey(mapIndexData);
        List<Integer> openList = getMapIndexOpenList(mapIndexData);
        List<Integer> receiveRewardList = getMapIndexReceiveRewardList(mapIndexData);
        //返给前端
        HashMap<String, Object> result = new HashMap<>();
        result.put("operate", msg.get("operate")); //操作
        result.put("mapIndex", msg.get("mapIndex")); //抽奖图id
        result.put("openList",  openList); //开启列表
        result.put(receiveRewardListKey,  receiveRewardList); //领取奖励列表
        result.put(consumeMapKey,  consumeMap); //消耗列表
        if(mapIndexData.containsKey("isOpenNextMap")){
            result.put("isOpenNextMap", mapIndexData.get("isOpenNextMap")); //判断是否开启
        }
        if(msg.containsKey("cellIndex")){
            result.put("cellIndex", msg.get("cellIndex")); //抽奖图id
        }

        ActivityMessage.ResActivityDeal.Builder pb = ActivityMessage.ResActivityDeal.newBuilder();
        pb.setData(JsonUtils.toJSONString(result));
        pb.setType(actCfg.getType());
        MessageUtils.send_to_player(player, ActivityMessage.ResActivityDeal.MsgID.eMsgID_VALUE, pb.build().toByteArray());

    }


    public  List<Integer> getMapIndexReceiveRewardList(HashMap<String,Object> mapIndexData){
        if(mapIndexData.containsKey(receiveRewardListKey)){
            List<Integer> openList = (List<Integer>)mapIndexData.get(receiveRewardListKey);
            return openList;
        }else {
            List<Integer>receiveRewardList = new ArrayList<>();
            mapIndexData.put(receiveRewardListKey,receiveRewardList);
            return receiveRewardList;
        }
    }

    public  List<Integer> getMapIndexOpenList(HashMap<String,Object> mapIndexData){
        if(mapIndexData.containsKey(openListKey)){
            List<Integer> openList = (List<Integer>)mapIndexData.get(openListKey);
            return openList;
        }else {
            List<Integer> openList = new ArrayList<>();
            mapIndexData.put(openListKey,openList);
            return openList;
        }
    }
    public  Map<Integer,Integer> getMapIndexConsumeMapKey(HashMap<String,Object> mapIndexData){
        if(mapIndexData.containsKey(consumeMapKey)){
            Map<Integer,Integer> consumeMap = (Map<Integer,Integer>)mapIndexData.get(consumeMapKey);
            return consumeMap;
        }else {
            Map<Integer,Integer> consumeMap = new HashMap<>();
            mapIndexData.put(consumeMapKey,consumeMap);
            return consumeMap;
        }
    }



    public  HashMap<String,Object> getMapIndexData(HashMap<Integer,HashMap<String,Object>> mapDataMap,int mapIndex){
        //判断是否包含
        if(mapDataMap.containsKey(mapIndex))   {
             return mapDataMap.get(mapIndex);
        }else {
            HashMap<String,Object> newMap = new HashMap<>();
            mapDataMap.put(mapIndex,newMap);
            return newMap;
        }
    }



    @Override
    public boolean parseCustomConfig(ActivityConfig actCfg, String customStr) {
        logger.error("parseCustomConfig:"+customStr);
        FZTBActivity fZTBActivity = JsonUtils.parseObject(customStr, FZTBActivityScript.FZTBActivity.class);

        actCfg.getCustomCfgMap().put(configData, fZTBActivity);

        actCfg.getCustomCfgMap().put(Client, fZTBActivity.client);
        return true;
    }

    @Override
    public boolean updateCustomConfig(ActivityConfig actCfg, String customStr) {
        return parseCustomConfig(actCfg, customStr);
    }

    @Override
    public String getActivityDataStr(ActivityConfig actCfg, long roleId) {

        int actType = actCfg.getType();

        //判断玩家是否在线
        Player player = Manager.playerManager.getPlayerOnline(roleId);
        if (player == null) {
            logger.error("getActivityDataStr:玩家已经离线!roleID:" + roleId);
            return "";
        }
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(roleId, actType);
        HashMap<String, Object> result = new HashMap<>();
        result.put(mapDataMapKey, roleActDataMap.get(mapDataMapKey));
        String resultJson = JsonUtils.toJSONString(result);
        return resultJson;
    }

    @Override
    public void rechargeDeal(Player player,int getGoodsCfgId, int rechargeNum, ActivityConfig actCfg) {

    }

    /**
     * 登录加载数据
     *
     * @param player 玩家
     */
    @Override
    public void playerOnline(Player player, ActivityConfig actCfg) {

    }

    /**
     * 0点刷新
     *
     * @param player
     */
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
    public void consumeDeal(Player player,int coinType, int consumeNum, ActivityConfig actCfg){

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
    //
    public static void testBuild() {
        FZTBActivity fZTBActivity = new FZTBActivity();
        fZTBActivity.drawItemId = 1003;
        fZTBActivity.drawItemNeedLingyu = 100;
        fZTBActivity.FZTBMapList = new ArrayList<>();
        //九个图数据
        for(int i =0;i<9;i++){
            FZTBMapBean fZTBMapBean = new FZTBMapBean();

//            fZTBMapBean.bigRewardItem =  new RewardData();
//            fZTBMapBean.bigRewardItem.setI(2001);
//            fZTBMapBean.bigRewardItem.setN(1);
//            fZTBMapBean.bigRewardItem.setB(1);
//            fZTBMapBean.bigRewardItem.setC(9);

            fZTBMapBean.allFinishDrawItemNum = 5;
            fZTBMapBean.openNextMapDrawNum = 2;
            fZTBMapBean.resetNeedlingyu = 300;

            fZTBMapBean.costNumList = new ArrayList<>();
            for(int j =0;j<9;j++){
                fZTBMapBean.costNumList.add(i);
            }

            fZTBMapBean.FZTBMapRewardBeanList = new ArrayList<>();
            for(int m =0;m<9;m++){
                FZTBMapRewardBean fZTBMapRewardBean = new FZTBMapRewardBean();

                fZTBMapRewardBean.minDrawNum = 1;
                fZTBMapRewardBean.weight = 100;


                RewardData rewardData =  new RewardData();
                rewardData.setI(2001);
                rewardData.setN(1);
                rewardData.setB(1);
                rewardData.setC(9);

                fZTBMapRewardBean.reward = new ArrayList<>();
                fZTBMapRewardBean.reward.add(rewardData);


                fZTBMapBean.FZTBMapRewardBeanList.add(fZTBMapRewardBean);
            }

            fZTBActivity.FZTBMapList.add(fZTBMapBean);
        }



        ActivityConfigBean acb = new ActivityConfigBean();
        acb.setId(Manager.activityManager.deal().toActType(ActivityType.FZTB,0));
        acb.setType(Manager.activityManager.deal().toActType(ActivityType.FZTB,0));
        acb.setMinLv(0);
        acb.setMaxLv(801);
        acb.setTag((byte) 1);
        acb.setSort((byte) 1);
        acb.setName("方泽探宝");
        acb.setState((byte)1);
        acb.setBeginTime(TimeUtils.Time() - 24 * 60 * 60 * 1000L);
        acb.setEndTime(TimeUtils.Time() + 30 * 24 * 60 * 60 * 1000L);
        acb.setIsDelete((byte) 0);
        acb.setIsOpenServer((byte) 1);
        fZTBActivity.client = JsonUtils.toJSONString(fZTBActivity);

        acb.setCustom(JsonUtils.toJSONString(fZTBActivity));




        Manager.activityManager.deal().registerActivityBean(acb);
        if (Manager.activityManager.getConfigDao().isExitActivity(acb.getId())) {
            Manager.activityManager.getConfigDao().update(DbSqlName.ACTIVITYCONFIG_UPDATE.getName(), acb);
        } else {
            Manager.activityManager.getConfigDao().update(DbSqlName.ACTIVITYCONFIG_INSERT.getName(), acb);
        }

    }
    //endregion
    //endregion



    /**
     * 活动数据
     */
    static class FZTBActivity {
        public String client;                              //前端数据

        /**
         * 抽奖道具id
         */
        public int drawItemId;
        /**
         * 抽奖道具对应灵玉价值
         */
        public int drawItemNeedLingyu;

        public List<FZTBMapBean> FZTBMapList; //方泽探宝
    }
    static class FZTBMapBean {
        /**
         * 奖励全部获得后奖励得抽奖道具数量
         */
        public int allFinishDrawItemNum;
        /**
         * 开启下一个地图需要完成得探宝次数
         */
        public int openNextMapDrawNum;
        /**
         * 重置本图需要的灵玉数量
         */
        public int resetNeedlingyu;
        /**
         * 方泽探宝奖励设置数组 9个
         */
        public List<FZTBMapRewardBean> FZTBMapRewardBeanList;
        /**
         * 消耗道具数量
         */
        public List<Integer> costNumList;
    }
    /**
     * 方泽探宝奖励设置
     */
    static class FZTBMapRewardBean {
        public int weight; //权重
        public int minDrawNum;//最低抽奖数
        public List< RewardData> reward; //通用只会有一个
    }

    static class FZTBMapRewardWeightCalc {
        public int index; //权重
        public FZTBMapRewardBean reward;
    }

}
