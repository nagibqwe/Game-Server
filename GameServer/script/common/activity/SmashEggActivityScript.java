package common.activity;

import com.data.CfgManager;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_Item_Bean;
import com.game.activity.script.IActivityLucky;
import com.game.activity.script.IActivityScript;
import com.game.activity.struct.ActivityConfig;
import com.game.activity.struct.ActivityLucky;
import com.game.activity.struct.ActivityType;
import com.game.activity.struct.RewardData;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.chat.Manager.ChatManager;
import com.game.chat.structs.ChatChannel;
import com.game.chat.structs.Notify;
import com.game.count.structs.VariantType;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.ServerStr;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import com.game.utils.Utils;
import game.core.json.TypeReference;
import game.core.net.Config.ServerConfig;
import game.core.util.IDConfigUtil;
import game.core.util.JsonUtils;
import game.core.util.TimeUtils;
import game.message.ActivityMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 砸金蛋 300027
 */
public class SmashEggActivityScript implements IActivityScript, IActivityLucky {

    final transient Logger logger = LogManager.getLogger(SmashEggActivityScript.class);
    private static final String configData = "configData";
    final transient String totalCount = "totalCount";            //累计抽奖次数
    final transient String dailyCount = "dailyCount";            //每日抽奖次数
    final transient String lowestDrawCount = "lowestDrawCount";  //抽奖保底次数
    final transient String lowestDrawMap = "lowestDrawMap";      //抽奖保底状态map
    final transient String refreshCount = "refreshCount";        //角色刷新彩蛋次数
    final transient String playerEggList = "playerEggList";      //角色彩蛋列表
    final transient String eggRewardMap = "eggRewardMap";        //彩蛋列表对应奖励
    final transient String countReward = "countReward";          //累计次数奖励
    final transient String onlineRewardTime = "onlineRewardTime";//每日在线奖励领奖时间
    final transient String onlineRewardCount = "onlineRewardCount";//每日在线奖励领奖次数
//    final transient String playerHistory = "playerHistory";    //个人抽奖记录
    final transient String serverHistory = "serverHistory";      //全服抽奖记录
//    final transient int playerHistoryLen = 10;                 //个人抽奖记录长度
    final transient int serverHistoryLen = 20;                   //服务器抽奖记录长度
    final transient int eggLen = 8;                              //彩蛋列表长度


    @Override
    public void onReqActivityDeal(Player player, String dataStr, ActivityConfig actCfg) {
        SmashEggActivity smashEggActivity = (SmashEggActivity) actCfg.getCustomCfgMap().get(configData);
        if (smashEggActivity == null) {
            logger.error("砸金蛋活动自定义配置参数不存在");
            return;
        }
        //重要的参数在这里验证一下，不然要gg
        if (smashEggActivity.getOneCostGold() < 1 || smashEggActivity.getOneCostItem() < 1) {
            logger.error("砸金蛋活动中配置的消耗为0");
            return;
        }
        HashMap<String, Integer> msg = JsonUtils.parseObject(dataStr, new TypeReference<HashMap<String, Integer>>() {});
        int operate = msg.get("operate"); //1=刷新彩蛋2=砸蛋3=领取累计次数奖励
        if (operate == 1) {//刷新彩蛋
            refresh(player, smashEggActivity, actCfg);
        } else if (operate == 2) {//砸蛋
            Integer index = msg.get("index");
            if (index == null) {
                logger.error("砸金蛋活动请求抽奖参数为空");
                return;
            }

            dealDraw(player, index, smashEggActivity, actCfg);
        }else if (operate == 3) {//领取累计奖励
            Integer count = msg.get("count");
            if (count == null) {
                logger.error("砸金蛋活动请求抽奖参数为空");
                return;
            }
            dealCountReward(player, count, smashEggActivity, actCfg);
        }else if (operate == 4) {//领取在线时长间隔奖励
            dealOnlineReward(player, smashEggActivity, actCfg);
        }
    }

    private void refresh(Player player, SmashEggActivity smashEggActivity, ActivityConfig actCfg) {
        int costItemId = smashEggActivity.getRefreshItem();
        int costItemCount = 1; //单次道具固定扣除数量为1
        int costGoldCount = smashEggActivity.getRefreshGoldCost();
        boolean useGold = false;
        //优先使用道具
        if (!Manager.backpackManager.manager().onRemoveItem(player, costItemId, costItemCount, ItemChangeReason.SmashEggRefreshCost, IDConfigUtil.getLogId())) {
            //道具没扣成功，就是没有道具，则扣元宝
            if (!Manager.currencyManager.manager().onDecItemCoin(player, costGoldCount, ItemChangeReason.SmashEggRefreshCost, IDConfigUtil.getLogId(), ItemCoinType.GemCoin)) {
                //元宝不够
                logger.error("砸金蛋活动刷新扣除的元宝不足");
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.CurrencyNotEnough, Manager.backpackManager.manager().getName(ItemCoinType.GemCoin));
                return;
            }else{
                useGold = true;
            }
        }

        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(), actCfg.getType());
        Integer roleRefreshCount = (Integer) roleActDataMap.getOrDefault(refreshCount, 0);

        List<LinkedHashMap<String, Integer>> eggList;
        if(roleActDataMap.get(playerEggList) != null){
            eggList = (List<LinkedHashMap<String, Integer>>) roleActDataMap.get(playerEggList);
        }else{
            eggList = new ArrayList<>(eggLen);
//            //初始化彩蛋列表
//            LinkedHashMap<Integer, Integer> egg;
//            for (int i = 0; i < eggLen; i++) {
//                egg = new LinkedHashMap<>();
//                egg.put(getRandomEggByWeight(smashEggActivity.getEggWeightMap(), false), 0);
//                eggList.add(egg);
//            }
            roleActDataMap.put(playerEggList, eggList);
//            Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));
        }

        int lowestCount = smashEggActivity.getRefreshLowest();
        //开始刷新彩蛋
        eggList.clear();
        LinkedHashMap<String, Integer> eggInfo;
        int count = roleRefreshCount+1;
        for (int i = 0; i < 8; i++) {
            eggInfo = new LinkedHashMap<>();
            if(lowestCount>0){
                if(count/lowestCount>1?count%lowestCount==0:count==lowestCount){
                    eggInfo.put("1", 0);
                    eggList.add(eggInfo);//保底刷出彩蛋
                    count = 0;//保底次数清零
                    continue;
                }
            }
            eggInfo.put(String.valueOf(getRandomEggByWeight(smashEggActivity.getEggWeightMap(), useGold)), 0);
            eggList.add(eggInfo);
        }

        //刷新蛋成功,清空获奖列表
        HashMap<Integer, RewardData> rewardMap;
        if(roleActDataMap.get(eggRewardMap) != null){
            rewardMap = (HashMap<Integer, RewardData>) roleActDataMap.get(eggRewardMap);
        }else{
            rewardMap = new HashMap<>(eggLen);
//            roleActDataMap.put(eggRewardMap, rewardMap);
        }

        roleRefreshCount+=1;
        rewardMap.clear();
        roleActDataMap.put(refreshCount, roleRefreshCount);
        roleActDataMap.put(playerEggList, eggList);
        roleActDataMap.put(eggRewardMap, rewardMap);
        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));

        HashMap<String, Object> result = new HashMap<>();
        result.put("operate", 1);//0上线 1刷蛋 2砸蛋 3领取次数奖励
        result.put("refreshEggCount", roleRefreshCount);
        result.put("eggList", eggList);
//        result.put("reward", rewardMap);

        ActivityMessage.ResActivityDeal.Builder pb = ActivityMessage.ResActivityDeal.newBuilder();
        pb.setData(JsonUtils.toJSONString(result));
        pb.setType(actCfg.getType());
//        logger.info("=========刷新信息:"+pb.build().toString());
        MessageUtils.send_to_player(player, ActivityMessage.ResActivityDeal.MsgID.eMsgID_VALUE, pb.build().toByteArray());
    }

    private int getRandomEggByWeight(HashMap<Integer, EggWeight> weightMap, boolean useGold) {
        int result = 3;
        Integer weightSum = 0;
        for (Map.Entry<Integer, EggWeight> data : weightMap.entrySet()) {
            if(useGold){
                weightSum += data.getValue().getGoldWeight();
            }else{
                weightSum += data.getValue().getItemWeight();
            }
        }

        if (weightSum <= 0) {
//            log.info("Error: weightSum=" + weightSum.toString());
            return result;
        }
        Integer n = new Random().nextInt(weightSum); // n in [0, weightSum)
        Integer m = 0;
        int weight = 0;
        for (Map.Entry<Integer, EggWeight> data : weightMap.entrySet()) {
            if(useGold){
                weight = data.getValue().getGoldWeight();
            }else{
                weight = data.getValue().getItemWeight();
            }
            if (m <= n && n < m + weight) {
                result = data.getKey();
//                logger.info("This Random reward is " + data);
                break;
            }
            m += weight;
        }
        return result;
    }

    /**
     * 抽奖一次或十次
     */
    private void dealDraw(Player player, int index, SmashEggActivity smashEggActivity, ActivityConfig actCfg) {
        int useItemTimes = 0, useGoldTimes = 0;
        int drawCount = index>=0 ? 1 : 8;
        int costItemId = smashEggActivity.getCostItemId();
        int costItemCount = 1; //单次道具固定扣除数量为1
        int costGoldCount = smashEggActivity.getOneCostGold();
        int realCount = 0;

        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(), actCfg.getType());
        Integer totalDrawCount = (Integer) roleActDataMap.getOrDefault(totalCount, 0);
        Integer dailyTotalCount = (Integer) roleActDataMap.getOrDefault(dailyCount, 0);
        Integer lowestCount = (Integer) roleActDataMap.getOrDefault(lowestDrawCount, 0);

        //list <彩蛋类型，彩蛋状态>
        List<LinkedHashMap<String, Integer>> eggList;
        if(roleActDataMap.get(playerEggList) != null){
            eggList = (List<LinkedHashMap<String, Integer>>) roleActDataMap.get(playerEggList);
        }else{
            eggList = new ArrayList<>(eggLen);
            //初始化彩蛋列表
            LinkedHashMap<String, Integer> egg;
            for (int i = 0; i < eggLen; i++) {
                egg = new LinkedHashMap<>();
                egg.put(String.valueOf(getRandomEggByWeight(smashEggActivity.getEggWeightMap(), false)), 0);
                eggList.add(egg);
            }
            roleActDataMap.put(playerEggList, eggList);
            Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));
        }

        //保底序号MAP<保底序号, 状态>
        LinkedHashMap<String, Integer> lowestMap;
        if(roleActDataMap.get(lowestDrawMap) != null){
            lowestMap = (LinkedHashMap<String, Integer>) roleActDataMap.get(lowestDrawMap);
        }else{
            lowestMap = new LinkedHashMap<>();
            //初始化保底序号MAP
            for (int i = 0; i < smashEggActivity.getLowestMap().size(); i++) {
                lowestMap.put(String.valueOf(i), 0);
            }
            roleActDataMap.put(lowestDrawMap, lowestMap);
        }

        if(index>=0){//指定抽某一个
            realCount = 1;
            if(eggList.get(index).containsValue(1)){//空蛋
                logger.error("运营活动砸金蛋，玩家"+player.getInfo()+"砸蛋为空蛋,index="+index);
                return;
            }

            if((dailyTotalCount+realCount)>smashEggActivity.getDailyLimitCount()){
                logger.error("运营活动砸金蛋，玩家"+player.getInfo()+"达到每日砸金蛋上限！");
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.C_MONSTERSOUL_MAX);
                return;
            }

            //优先使用道具
            if (!Manager.backpackManager.manager().onRemoveItem(player, costItemId, costItemCount, ItemChangeReason.SmashEggCost, IDConfigUtil.getLogId())) {
                //道具没扣成功，就是没有道具，则扣元宝
                if (!Manager.currencyManager.manager().onDecItemCoin(player, costGoldCount, ItemChangeReason.SmashEggCost, IDConfigUtil.getLogId(), ItemCoinType.GemCoin)) {
                    //元宝不够
//                    logger.error("砸金蛋活动抽奖扣除的元宝不足");
                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.CurrencyNotEnough, Manager.backpackManager.manager().getName(ItemCoinType.GemCoin));
                    return;
                } else {
                    //元宝扣成功了
                    useGoldTimes = 1;
                }
            } else {
                //道具使用成功了
                useItemTimes = 1;
            }
        }else{//多连抽
            int canDrawCount = smashEggActivity.getDailyLimitCount()-dailyTotalCount;
            for (LinkedHashMap<String, Integer> eggInfo:eggList) {
                if(realCount>=canDrawCount){
                    break;
                }
                if(eggInfo.containsValue(1)){//排除空蛋
                    continue;
                }
                realCount+=1;
            }

            if((dailyTotalCount+realCount)>smashEggActivity.getDailyLimitCount()){
                logger.error("运营活动砸金蛋，玩家"+player.getInfo()+"达到每日砸金蛋上限！");
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.C_MONSTERSOUL_MAX);
                return;
            }

            //当前有多少个道具
            int itemNum = Manager.backpackManager.manager().getItemNum(player, costItemId);
            if (itemNum < costItemCount) {//抽一次的道具都不够
                //扣十连抽的元宝
                if (!Manager.currencyManager.manager().onDecItemCoin(player, costGoldCount * realCount, ItemChangeReason.SmashEggCost, IDConfigUtil.getLogId(), ItemCoinType.GemCoin)) {
                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.CurrencyNotEnough, Manager.backpackManager.manager().getName(ItemCoinType.GemCoin));
                    return;
                }
                useGoldTimes = realCount;
            } else if (itemNum < costItemCount * realCount) {//道具数量小于十连抽需要的，要看金元宝够不够差价
                //元宝不够
                if (Manager.currencyManager.manager().getCurrencyIntNum(player, ItemCoinType.GemCoin) < (realCount - itemNum) * costGoldCount) {
                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.CurrencyNotEnough, Manager.backpackManager.manager().getName(ItemCoinType.GemCoin));
                    return;
                }else {//元宝够,则同时扣元宝和所有道具
                    //为了保险，这里再判定一次
                    if (!Manager.backpackManager.manager().onRemoveItem(player, costItemId, itemNum, ItemChangeReason.SmashEggCost, IDConfigUtil.getLogId())) {
                        logger.error("1不应该走到这里" + player.getId());
                        return;
                    }
                    if (!Manager.currencyManager.manager().onDecItemCoin(player, (realCount - itemNum) * costGoldCount, ItemChangeReason.SmashEggCost, IDConfigUtil.getLogId(), ItemCoinType.GemCoin)) {
                        logger.error("2不应该走到这里" + player.getId());
                        return;
                    }
                    useItemTimes = itemNum;
                    useGoldTimes = realCount - itemNum;
                }
            } else {//道具大于等于8个，直接扣8个
                //为了保险，这里再判定一次
                if (!Manager.backpackManager.manager().onRemoveItem(player, costItemId, costItemCount*realCount, ItemChangeReason.SmashEggCost, IDConfigUtil.getLogId())) {
                    logger.error("3不应该走到这里" + player.getId());
                    return;
                }
                useItemTimes = realCount;
            }
        }

        ConcurrentHashMap<String, Object> actDataMap = Manager.activityManager.deal().getActivityData(actCfg.getType());

        //抽奖记录表，用于返给前端
//        List<String> playerHistorys;
        List<String> serverHistorys;
//        if(roleActDataMap.get(playerHistory) != null){
//            playerHistorys = (List<String>)roleActDataMap.get(playerHistory);
//        }else{
//            playerHistorys = new ArrayList<>();
//            roleActDataMap.put(playerHistory, playerHistorys);
//        }
        if(actDataMap.get(serverHistory) != null){
            serverHistorys = (List<String>)actDataMap.get(serverHistory);
        }else{
            serverHistorys = new ArrayList<>();
            actDataMap.put(serverHistory,serverHistorys);
        }

        List<LinkedHashMap<String, Integer>> drawList;
        if(index<0){//一键砸蛋
            drawList = new ArrayList<>();
            int tempCount = 0;
            for (int i = 0; i < eggList.size(); i++) {
                if(tempCount>=realCount){
                    break;
                }
                LinkedHashMap<String, Integer> eggInfo = eggList.get(i);
                drawList.add(eggList.get(i));

                if(eggInfo.containsValue(1)){//空蛋不做计数
                    continue;
                }
                tempCount+=1;
            }
        }else{//指定砸某个蛋
            drawList = new ArrayList<>();
            drawList.add(eggList.get(index));
        }

        //保底次数和奖励
        HashMap<Integer, LowestData> lowestDataMap = smashEggActivity.getLowestMap();
        int maxLowestCount = calcLowestMax(lowestDataMap);

        //获奖缓存
        HashMap<Integer, RewardData> rewardMap;
        if(roleActDataMap.get(eggRewardMap) != null){
            rewardMap = (HashMap<Integer, RewardData>) roleActDataMap.get(eggRewardMap);
        }else{
            rewardMap = new HashMap<>(eggLen);
            roleActDataMap.put(eggRewardMap, rewardMap);
        }

        //客户端获奖,当前抽奖增量
        HashMap<Integer, RewardData> clientReward = new HashMap<>();
        //客户端奖励状态
        HashMap<Integer, Integer> clientBigReward = new HashMap<>();

        //优先级规则：保底>幸运值
        for (int times = 0; times < drawList.size(); times++) {
            //蛋的类型 0空蛋 1彩蛋 2金蛋 3银蛋
            LinkedHashMap<String, Integer> draw = drawList.get(times);
            if(draw.containsValue(1)){//空蛋不处理
                continue;
            }

            //蛋的序号
            int curIndex = index == -1?times:index;
            //蛋类型
            String type = "";
            for (Map.Entry<String, Integer> entry : draw.entrySet()) {
                type = entry.getKey();
            }
            boolean useGold = false;
            if(useItemTimes-(times+1)<=0){
                useGold = true;
            }

            int totalCount = totalDrawCount+(times+1);
            lowestCount += 1;
            int isShow = 0;
            int isBaodi = 0;
            RewardData reward = null;
            //当前保底次数已经达保底范围(保底次数优先级最高)
            LowestData lowestData = getLowestData(lowestCount, lowestDataMap);
            if(lowestData != null){//进入保底范围处理
                List<RewardData> lowestRewards = getLowestReward(lowestCount, lowestData, lowestMap);
                if(lowestRewards!=null){
                    reward = getRewardByCareer(player.getCareer(), lowestRewards);
                    rewardMap.put(curIndex, reward);
                    clientReward.put(curIndex, reward);
                    isShow = 1;
                    isBaodi = 1;
                    //设置当前保底档位状态为已抽取
                    lowestMap.put(String.valueOf(lowestData.getIndex()), 1);
                    //检查是否所有保底档位都抽取完
                    if(lowestCount >= maxLowestCount && checkAllLowest(lowestMap)){
                        lowestCount = 0;//重置当前循环的保底抽奖次数
                        clearLowestMap(smashEggActivity.getLowestMap(), lowestMap);
                    }
                }
            }

            if(isBaodi!=1){
                //检查幸运值
                incrLucky(player, smashEggActivity);//增加幸运值
                if(isTriggerLucky(player, smashEggActivity)){//幸运值达到直接获得大奖
                    reward = Utils.findOne(smashEggActivity.getLuckyAwardList(), i -> i.getC() == 9 || i.getC() == player.getCareer());
                    rewardMap.put(curIndex, reward);
                    clientReward.put(curIndex, reward);
                    isShow = 1;

                    List<Item> items = Item.createItems(reward.getI(), reward.getN(), reward.getB() == 1);
                    cleanLucky(player, smashEggActivity, items);

                    //测试日志
//                    if (ServerConfig.isTestServer()) {
//                        long variant = Manager.countManager.getVariant(player, VariantType.ACTIVITY_LUCKY_VALUE);
//                        Manager.chatManager.sendSystemStrToPlayer(player, "当前幸运值=" + variant);
//                    }
                }else{//普通抽奖
                    //在对应的奖池中抽奖
                    RewardPoolData randomReward = getRandomRewardByWeight(smashEggActivity.getRewardPoolMap().get(Integer.parseInt(type)), useGold);
                    isShow = randomReward.getIsShow();
                    reward = getRewardByCareer(player.getCareer(), randomReward.getRewardData());
                    rewardMap.put(curIndex, reward);
                    clientReward.put(curIndex, reward);
                }
            }

            //抽完蛋设置空蛋
            eggList.get(curIndex).put(type, 1);

            //添加抽奖历史记录
//            if(playerHistorys.size()>playerHistoryLen){
//                playerHistorys.remove(0);
//            }
//            playerHistorys.add(new StringBuilder(String.valueOf(reward.getI())).append("_").append(String.valueOf(reward.getN())).toString());

            if(serverHistorys.size()>serverHistoryLen){
                serverHistorys.remove(0);
            }
            serverHistorys.add(new StringBuilder(player.getName()).append("_").append(String.valueOf(reward.getI())).append("_").append(String.valueOf(reward.getN())).toString());

            if (isShow == 1){//大奖发通知
                Cfg_Item_Bean itemBean = CfgManager.getCfg_Item_Container().getValueByKey(reward.getI());
                String itemName = ServerStr.getChatTableName(itemBean.getName());
                int itemNum = reward.getN();
                MessageUtils.notify_AllServer(Notify.EXCLUSIVE_NOTIFY, ChatChannel.CHATCHANNEL_SYSTEM, MessageString.luck_draw_radio_notice4,
                        ServerConfig.getServerId(),
                        player.getId(),
                        player.getName(),
                        actCfg.getName(),
                        itemName,
                        itemNum);
                clientBigReward.put(curIndex, 1);
            }
        }

        //奖励直接发背包
        List<Item> items = Item.createItems(player.getCareer(), clientReward.values());
        //抽奖赠送奖励,只算花元宝的
        int giftNum = smashEggActivity.getGiftData().getN()*useGoldTimes;
        if(giftNum>0){
            Item giftItem = Item.createItem(smashEggActivity.getGiftData().getI(), giftNum, smashEggActivity.getGiftData().getB() == 1);
            items.add(giftItem);
        }

        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.SmashEggGet, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.SmashEggGet);
        }

        //存储数据
        totalDrawCount+=(useItemTimes+useGoldTimes);
        dailyTotalCount+=(useItemTimes+useGoldTimes);

        //所有蛋如果都为空蛋则需要自动刷新
        int emptyCount = getEmptyCount(eggList);
        if(emptyCount>=eggLen){
            eggList.clear();
            rewardMap.clear();
            LinkedHashMap<String, Integer> eggInfo;
            for (int i = 0; i < eggLen; i++) {
                eggInfo = new LinkedHashMap<>();
                eggInfo.put(String.valueOf(getRandomEggByWeight(smashEggActivity.getEggWeightMap(), false)), 0);
                eggList.add(eggInfo);
            }
        }

        roleActDataMap.put(totalCount, totalDrawCount);
        roleActDataMap.put(dailyCount, dailyTotalCount);
        roleActDataMap.put(lowestDrawCount, lowestCount);
        roleActDataMap.put(lowestDrawMap, lowestMap);
        roleActDataMap.put(playerEggList, eggList);
        roleActDataMap.put(eggRewardMap, rewardMap);
//        roleActDataMap.put(playerHistory, playerHistorys);

        actDataMap.put(serverHistory, serverHistorys);
        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));
        Manager.activityManager.deal().saveActData(actCfg.getType(), actDataMap);

        HashMap<String, Object> result = new HashMap<>();
        result.put("operate", 2);//0上线 1刷蛋 2砸蛋 3领取次数奖励
        result.put("drawCount", totalDrawCount);
        result.put("dailyCount", dailyTotalCount);
        result.put("drawLowestMap", lowestMap);
        result.put("drawLowestCount", lowestCount);//当前保底抽奖次数
        result.put("eggList", eggList);
        result.put("reward", clientReward);
        result.put("bigReward", clientBigReward);
//        result.put("selfHistory", playerHistorys.subList(playerHistorys.size()-drawCount, playerHistorys.size()));//只发增量
        result.put("serverHistory", serverHistorys.subList(serverHistorys.size()-drawCount, serverHistorys.size()));//只发增量

        ActivityMessage.ResActivityDeal.Builder pb = ActivityMessage.ResActivityDeal.newBuilder();
        pb.setData(JsonUtils.toJSONString(result));
        pb.setType(actCfg.getType());
//        logger.info("=========砸蛋信息:"+pb.build().toString());
        MessageUtils.send_to_player(player, ActivityMessage.ResActivityDeal.MsgID.eMsgID_VALUE, pb.build().toByteArray());
    }

    private int calcLowestMax(HashMap<Integer, LowestData> lowestDataMap) {
        int max = 0;
        for (int key : lowestDataMap.keySet()){
            LowestData data = lowestDataMap.get(key);
            max = Math.max(max, data.getMax());
        }
        return max;
    }

    private int getEmptyCount(List<LinkedHashMap<String, Integer>> eggList) {
        int emptyCount = 0;
        for (LinkedHashMap<String, Integer> eggInfo:eggList) {
            if(eggInfo.containsValue(1)){
                emptyCount+=1;
            }
        }
        return emptyCount;
    }

    private RewardData getRewardByCareer(byte career, List<RewardData> randomReward) {
        for (RewardData cfg : randomReward) {
            if (cfg.getC() == 9 || cfg.getC() == career) {
                return cfg;
            }
        }
        return randomReward.get(0);
    }

    private RewardPoolData getRandomRewardByWeight(List<RewardPoolData> list, boolean useGold) {
        RewardPoolData result = null;
        Integer weightSum = 0;
        for (RewardPoolData data : list) {
            if(useGold){
                weightSum += data.getGoldWeight();
            }else{
                weightSum += data.getItemWeight();
            }
        }

        if (weightSum <= 0) {
//            log.info("Error: weightSum=" + weightSum.toString());
            return null;
        }
        Integer n = new Random().nextInt(weightSum); // n in [0, weightSum)
        Integer m = 0;
        int weight = 0;
        for (RewardPoolData data : list) {
            if(useGold){
                weight = data.getGoldWeight();
            }else{
                weight = data.getItemWeight();
            }
            if (m <= n && n < m + weight) {
                result = data;
//                logger.info("This Random reward is " + data);
                break;
            }
            m += weight;
        }
        return result;
    }

    private List<RewardData> getLowestReward(Integer count, LowestData lowestData, LinkedHashMap<String, Integer> lowestMap) {
        int state = lowestMap.get(String.valueOf(lowestData.getIndex()));
        if(state != 0){//已经领过当前档位的保底奖励
            return null;
        }

        LowestPro lowestPro = getLowestPro(count, lowestData.getProList());
        if(lowestPro == null){
            return null;
        }

        int pro = lowestPro.getPro();
        if(count==lowestData.getMax()){
            pro = 10000;
        }

        if(RandomUtils.defaultIsGenerate(pro)){
            return lowestData.getRewardData();
        }
        return null;
    }

    private LowestPro getLowestPro(Integer count, List<LowestPro> proList) {
        for (LowestPro pro:proList) {
            if(pro.getMin()<=count&&count<=pro.getMax()){
                return pro;
            }
        }
        return null;
    }

    private LowestData getLowestData(Integer count, HashMap<Integer, LowestData> lowestDatas) {
        for (LowestData lowestData:lowestDatas.values()) {
            if(lowestData.getMin()<=count&&count<=lowestData.getMax()){
                return lowestData;
            }
        }
        return null;
    }

    private void clearLowestMap(HashMap<Integer, LowestData> lowestData, LinkedHashMap<String, Integer> lowestMap) {
        lowestMap.clear();
        //初始化保底序号MAP
        for (int i = 0; i < lowestData.size(); i++) {
            lowestMap.put(String.valueOf(i), 0);
        }
    }

    private boolean checkAllLowest(LinkedHashMap<String, Integer> lowestMap) {
        for (Integer state:lowestMap.values()) {
            if(state == 0){
                return false;
            }
        }
        return true;
    }

    /**
     * 领取累积次数奖励
     */
    private void dealCountReward(Player player, int count, SmashEggActivity smashEggActivity, ActivityConfig actCfg) {
        if (smashEggActivity.getCountRewardMap().get(count)==null) {
            logger.error("领取累积次数奖励时档次不合法" + count);
            return;
        }
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(), actCfg.getType());
        Integer totalDrawCount = (Integer) roleActDataMap.getOrDefault(totalCount, 0);

//        ConcurrentHashMap<String, Object> actDataMap = Manager.activityManager.deal().getActivityData(actCfg.getType());
//        Integer totalPoolGold = (Integer)actDataMap.getOrDefault(totalGold, cornucopiaActivity.getGoldInitCount());

        LinkedHashMap<String, Integer> countRewardMap = getCountRewardMap(smashEggActivity, roleActDataMap);
        int state = countRewardMap.get(String.valueOf(count));
        if(state == 1){//已经领了
            logger.error("领取累积次数奖励失败,奖励已经领取。 count="+count);
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.TOUZI_GETFAIL);
            return;
        }

        //检查领奖次数是否够了
        if(totalDrawCount<count){
            logger.error("领取累积次数奖励失败,抽奖次数不足。 count="+count);
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.TOUZI_GETFAIL);
            return;
        }

        //发奖
        List<RewardData> reward = smashEggActivity.getCountRewardMap().get(count);
        List<Item> items = Item.createItems(player.getCareer(), reward);
        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.SmashEggCountGet, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.SmashEggCountGet);
        }

        //设置奖励领取状态
        countRewardMap.put(String.valueOf(count),1);
        roleActDataMap.put(countReward, countRewardMap);

        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));
//        Manager.activityManager.deal().saveActData(actCfg.getType(), actDataMap);
        //返给前端
        HashMap<String, Object> result = new HashMap<>();
        result.put("operate", 3);//0上线 1刷蛋 2抽奖 3领取次数奖励
//        result.put("drawCount", totalDrawCount);
//        result.put("reward", new ArrayList<>());
//        result.put("selfHistory", new ArrayList<>());
//        result.put("serverHistory", new ArrayList<>());
//        result.put("goldHistory", new ArrayList<>());
        result.put("countReward", countRewardMap);

        ActivityMessage.ResActivityDeal.Builder pb = ActivityMessage.ResActivityDeal.newBuilder();
        pb.setData(JsonUtils.toJSONString(result));
        pb.setType(actCfg.getType());
        MessageUtils.send_to_player(player, ActivityMessage.ResActivityDeal.MsgID.eMsgID_VALUE, pb.build().toByteArray());
    }

    private LinkedHashMap<String, Integer> getCountRewardMap(SmashEggActivity smashEggActivity, ConcurrentHashMap<String, Object> roleActDataMap) {
        LinkedHashMap<String, Integer> countRewardMap;
        if(roleActDataMap.get(countReward)!=null){
            countRewardMap = (LinkedHashMap<String, Integer>)roleActDataMap.get(countReward);
        }else{
            countRewardMap = new LinkedHashMap<>();
            for (Integer accCount: smashEggActivity.getCountRewardMap().keySet()) {
                countRewardMap.put(String.valueOf(accCount), 0);
            }
            roleActDataMap.put(countReward, countRewardMap);
        }
        return countRewardMap;
    }

    /**
     * 领取在线时长间隔奖励
     */
    private void dealOnlineReward(Player player, SmashEggActivity smashEggActivity, ActivityConfig actCfg) {
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(), actCfg.getType());
        Integer totalDrawCount = (Integer) roleActDataMap.getOrDefault(totalCount, 0);
        Integer onlineTime = (Integer) roleActDataMap.getOrDefault(onlineRewardTime, 0);
        Integer onlineCount = (Integer) roleActDataMap.getOrDefault(onlineRewardCount, 0);

//        ConcurrentHashMap<String, Object> actDataMap = Manager.activityManager.deal().getActivityData(actCfg.getType());
//        Integer totalPoolGold = (Integer)actDataMap.getOrDefault(totalGold, cornucopiaActivity.getGoldInitCount());
        if(onlineCount>=smashEggActivity.getTimesLimit()){
            logger.error("领取在线时长奖励失败,次数达上限。 count="+onlineCount);
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.TOUZI_GETFAIL);
            return;
        }

        int now = (int) (TimeUtils.Time()/1000);
        if(!(onlineTime==0||now - onlineTime > smashEggActivity.getOnLineTime() * 60)){
            logger.error("领取在线时长奖励失败,领取时间不对。 onlineTime="+onlineTime);
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.TOUZI_GETFAIL);
            return;
        }

        //发奖
        List<RewardData> reward = smashEggActivity.getFreeGift();
        List<Item> items = Item.createItems(player.getCareer(), reward);
        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.SmashEggOnlineGet, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.SmashEggOnlineGet);
        }

        //设置奖励领取状态
        onlineTime = now;
        onlineCount += 1;
        roleActDataMap.put(onlineRewardTime, onlineTime);
        roleActDataMap.put(onlineRewardCount, onlineCount);

        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));
//        Manager.activityManager.deal().saveActData(actCfg.getType(), actDataMap);
        //返给前端
        HashMap<String, Object> result = new HashMap<>();
        result.put("operate", 4);//0上线 1刷蛋 2抽奖 3领取次数奖励 4领取在线时长奖励
//        result.put("drawCount", totalDrawCount);
//        result.put("reward", new ArrayList<>());
//        result.put("selfHistory", new ArrayList<>());
//        result.put("serverHistory", new ArrayList<>());
//        result.put("goldHistory", new ArrayList<>());
        result.put("onlineTime", onlineTime);
        result.put("onlineCount", onlineCount);

        ActivityMessage.ResActivityDeal.Builder pb = ActivityMessage.ResActivityDeal.newBuilder();
        pb.setData(JsonUtils.toJSONString(result));
        pb.setType(actCfg.getType());
        MessageUtils.send_to_player(player, ActivityMessage.ResActivityDeal.MsgID.eMsgID_VALUE, pb.build().toByteArray());
    }


    @Override
    public boolean parseCustomConfig(ActivityConfig actCfg, String customStr) {
        SmashEggActivity data = JsonUtils.parseObject(customStr, SmashEggActivity.class);
        actCfg.getCustomCfgMap().put(configData, data);
        actCfg.getCustomCfgMap().put("client", data.getClient());
        return true;
    }

    @Override
    public boolean updateCustomConfig(ActivityConfig actCfg, String customStr) {
        return parseCustomConfig(actCfg, customStr);
    }

    @Override
    public String getActivityDataStr(ActivityConfig actCfg, long roleId) {
        SmashEggActivity smashEggActivity = (SmashEggActivity) actCfg.getCustomCfgMap().get(configData);
        if (smashEggActivity == null) {
            logger.error("许愿池活动活动自定义配置参数不存在");
            return null;
        }

        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(roleId, actCfg.getType());
        Integer totalDrawCount = (Integer) roleActDataMap.getOrDefault(totalCount, 0);
        Integer dailyTotalCount = (Integer) roleActDataMap.getOrDefault(dailyCount, 0);
        Integer roleRefreshCount = (Integer) roleActDataMap.getOrDefault(refreshCount, 0);
        Integer lowestCount = (Integer) roleActDataMap.getOrDefault(lowestDrawCount, 0);
        Integer onlineTime = (Integer) roleActDataMap.getOrDefault(onlineRewardTime, 0);
        Integer onlineCount = (Integer) roleActDataMap.getOrDefault(onlineRewardCount, 0);

        List<LinkedHashMap<String, Integer>> eggList;
        if(roleActDataMap.get(playerEggList) != null){
            eggList = (List<LinkedHashMap<String, Integer>>) roleActDataMap.get(playerEggList);
        }else{
            eggList = new ArrayList<>(eggLen);
            //初始化彩蛋列表
            LinkedHashMap<String, Integer> egg;
            for (int i = 0; i < eggLen; i++) {
                egg = new LinkedHashMap<>();
                egg.put(String.valueOf(getRandomEggByWeight(smashEggActivity.getEggWeightMap(), false)), 0);
                eggList.add(egg);
            }
            roleActDataMap.put(playerEggList, eggList);
            Manager.activityManager.deal().saveRoleActData(roleId, Manager.activityManager.getRoleActDatas().get(roleId));
        }

        HashMap<Integer, RewardData> rewardMap;
        if(roleActDataMap.get(eggRewardMap) != null){
            rewardMap = (HashMap<Integer, RewardData>) roleActDataMap.get(eggRewardMap);
        }else{
            rewardMap = new HashMap<>(eggLen);
            roleActDataMap.put(eggRewardMap, rewardMap);
        }

        ConcurrentHashMap<String, Object> actDataMap = Manager.activityManager.deal().getActivityData(actCfg.getType());

        //抽奖记录表，用于返给前端
//        List<String> playerHistorys;
        List<String> serverHistorys;

//        if(roleActDataMap.get(playerHistory) != null){
//            playerHistorys = (List<String>)roleActDataMap.get(playerHistory);
//        }else{
//            playerHistorys = new ArrayList<>();
//            roleActDataMap.put(playerHistory, playerHistorys);
//        }
        if(actDataMap.get(serverHistory) != null){
            serverHistorys = (List<String>)actDataMap.get(serverHistory);
        }else{
            serverHistorys = new ArrayList<>();
            actDataMap.put(serverHistory,serverHistorys);
        }

        //保底序号MAP<保底序号, 状态>
        LinkedHashMap<String, Integer> lowestMap;
        if(roleActDataMap.get(lowestDrawMap) != null){
            lowestMap = (LinkedHashMap<String, Integer>) roleActDataMap.get(lowestDrawMap);
        }else{
            lowestMap = new LinkedHashMap<>();
            //初始化保底序号MAP
            for (int i = 0; i < smashEggActivity.getLowestMap().size(); i++) {
                lowestMap.put(String.valueOf(i), 0);
            }
            roleActDataMap.put(lowestDrawMap, lowestMap);
        }

        LinkedHashMap<String, Integer> countRewardMap = getCountRewardMap(smashEggActivity, roleActDataMap);

        HashMap<String, Object> result = new HashMap<>();
        result.put("operate", 0);//0上线 1刷蛋 2抽奖 3领取次数奖励
        result.put("refreshEggCount", roleRefreshCount);
        result.put("drawCount", totalDrawCount);
        result.put("dailyCount", dailyTotalCount);
        result.put("drawLowestMap", lowestMap);
        result.put("drawLowestCount", lowestCount);//当前保底抽奖次数
        result.put("eggList", eggList);
        result.put("reward", rewardMap);
//        result.put("selfHistory", playerHistorys);
        result.put("serverHistory", serverHistorys);
        result.put("countReward", countRewardMap);
        result.put("onlineTime", onlineTime);
        result.put("onlineCount", onlineCount);
        return JsonUtils.toJSONString(result);
    }

    @Override
    public void activityEndDeal(ActivityConfig actCfg) {

    }

    @Override
    public void playerOnline(Player player, ActivityConfig actCfg) {

    }

    @Override
    public void zeroClockPlayerDeal(Player player, ActivityConfig actCfg) {
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(), actCfg.getType());
        if(roleActDataMap!=null){
            //零点清空每日抽奖上限
            roleActDataMap.put(dailyCount, 0);
//            roleActDataMap.put(onlineRewardTime, 0);
            roleActDataMap.put(onlineRewardCount, 0);
            Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));
        }
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

    @Override
    public boolean bossDrop(Player player, int bossId, ActivityConfig actCfg) {
        return false;
    }

    @Override
    public boolean boxDrop(Player player, int boxId, ActivityConfig actCfg) {
        return false;
    }

    @Override
    public boolean cloneDrop(Player player, int cloneId, ActivityConfig actCfg) {
        return false;
    }

    @Override
    public void rechargeDeal(Player player, int getGoodsCfgId, int rechargeNum, ActivityConfig actCfg) {

    }

    @Override
    public void consumeDeal(Player player, int coinType, int consumeNum, ActivityConfig actCfg) {

    }

    @Override
    public void reload() {
        if(Manager.activityManager == null){
            return;
        }
        List<ActivityConfig> actCfgList = Manager.activityManager.deal().getActCfgListByActLogicID(ActivityType.SmashEgg);
        for (ActivityConfig activityConfig:actCfgList) {
            if (activityConfig == null) {
                return;
            }
            Object old = activityConfig.getCustomCfgMap().get(configData);
            if (old == null) {
                return;
            }
            String customStr = JsonUtils.toJSONString(old);
            SmashEggActivity newData = JsonUtils.parseObject(customStr, SmashEggActivity.class);
            activityConfig.getCustomCfgMap().put(configData, newData);
        }
    }

    @Override
    public int getId() {
        return ScriptEnum.SmashEggScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    /**
     * 增加幸运值
     *
     * @param player
     * @param lucky
     */
    @Override
    public void incrLucky(Player player, ActivityLucky lucky) {
        long variant = Manager.countManager.getVariant(player, VariantType.ACTIVITY_LUCKY_VALUE);
        Manager.countManager.setVariant(player, VariantType.ACTIVITY_LUCKY_VALUE, variant + lucky.getLuckyValue());
    }

    /**
     * 是否触发幸运值
     *
     * @param player
     * @param lucky
     */
    @Override
    public boolean isTriggerLucky(Player player, ActivityLucky lucky) {
        int totalLuckyValue = Manager.activityManager.getTotalLuckyValue();
        long variant = Manager.countManager.getVariant(player, VariantType.ACTIVITY_LUCKY_VALUE);
        return variant >= totalLuckyValue && totalLuckyValue > 0;
    }

    /**
     * 清空幸运值
     *
     * @param player
     * @param lucky
     */
    @Override
    public void cleanLucky(Player player, ActivityLucky lucky, List<Item> items) {
        for (RewardData data : lucky.getLuckyAwardList()) {
            Item one = Utils.findOne(items, item -> item.getItemModelId() == data.getI());
            if (one != null) {
                Manager.countManager.setVariant(player, VariantType.ACTIVITY_LUCKY_VALUE, 0);
            }
        }
    }

    //保底分段概率
    static class LowestPro {
        private int min;
        private int max;
        private int pro;

        public int getMin() {
            return min;
        }

        public void setMin(int min) {
            this.min = min;
        }

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public int getPro() {
            return pro;
        }

        public void setPro(int pro) {
            this.pro = pro;
        }
    }

    //保底信息
    static class LowestData {
        private int index;//保底序号
        private int min;  //最小保底次数
        private int max;  //最大保底次数
        private List<LowestPro> proList;//保底分段概率
        private List<RewardData> rewardData;//保底奖励

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getMin() {
            return min;
        }

        public void setMin(int min) {
            this.min = min;
        }

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public List<LowestPro> getProList() {
            return proList;
        }

        public void setProList(List<LowestPro> proList) {
            this.proList = proList;
        }

        public List<RewardData> getRewardData() {
            return rewardData;
        }

        public void setRewardData(List<RewardData> rewardData) {
            this.rewardData = rewardData;
        }
    }

    //刷新蛋的权重
    static class EggWeight {
        private int itemWeight;  //道具刷新权重
        private int goldWeight;  //元宝刷新权重

        public int getItemWeight() {
            return itemWeight;
        }

        public void setItemWeight(int itemWeight) {
            this.itemWeight = itemWeight;
        }

        public int getGoldWeight() {
            return goldWeight;
        }

        public void setGoldWeight(int goldWeight) {
            this.goldWeight = goldWeight;
        }
    }

    //奖池信息
    static class RewardPoolData {
        private int itemWeight;  //道具抽奖权重
        private int goldWeight; //元宝抽奖权重
        private int isShow; //是否做大奖的特殊展示
        private List<RewardData> rewardData;

        public int getItemWeight() {
            return itemWeight;
        }

        public void setItemWeight(int itemWeight) {
            this.itemWeight = itemWeight;
        }

        public int getGoldWeight() {
            return goldWeight;
        }

        public void setGoldWeight(int goldWeight) {
            this.goldWeight = goldWeight;
        }

        public int getIsShow() {
            return isShow;
        }

        public void setIsShow(int isShow) {
            this.isShow = isShow;
        }

        public List<RewardData> getRewardData() {
            return rewardData;
        }

        public void setRewardData(List<RewardData> rewardData) {
            this.rewardData = rewardData;
        }
    }

    //砸金蛋活动数据
    static class SmashEggActivity extends ActivityLucky {
        private String client;  //前端数据

        //彩蛋刷新
        private int refreshItem;//刷新消耗的道具ID
        private int refreshItemCost = 1;//刷新消耗的道具数量 默认为1
        private int refreshGoldCost;//刷新消耗的元宝数量
        private int refreshLowest;//刷新多少次必出保底彩蛋

        //砸蛋
        private int costItemId;//道具ID
        private int oneCostItem;//抽一次消耗道具
        private int oneCostGold;//抽一次消耗元宝
        private int dailyLimitCount;//每日砸蛋上限次数
        private RewardData giftData;//每抽赠送道具

        private int onLineTime;//在线时长奖励领取间隔
        private int timesLimit;//在线时长奖励次数上限
        private List<RewardData> freeGift;//在线时长奖励

        //刷新彩蛋权重 <彩蛋类型, 权重> 1彩蛋 2金蛋 3银蛋
        private HashMap<Integer, EggWeight> eggWeightMap;
        //彩蛋奖池信息 <彩蛋类型, 奖励信息>
        private HashMap<Integer, List<RewardPoolData>> rewardPoolMap;
//        //保底奖励 <保底次数，保底奖励>
//        private HashMap<Integer, List<RewardData>> lowestMap;
        //保底奖励 <序号，保底信息>
        private HashMap<Integer, LowestData> lowestMap;
        //累计次数领奖 <累计领奖次数,累计奖励>
        private HashMap<Integer, List<RewardData>> countRewardMap;

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public int getRefreshItem() {
            return refreshItem;
        }

        public void setRefreshItem(int refreshItem) {
            this.refreshItem = refreshItem;
        }

        public int getRefreshItemCost() {
            return refreshItemCost;
        }

        public void setRefreshItemCost(int refreshItemCost) {
            this.refreshItemCost = refreshItemCost;
        }

        public int getRefreshGoldCost() {
            return refreshGoldCost;
        }

        public void setRefreshGoldCost(int refreshGoldCost) {
            this.refreshGoldCost = refreshGoldCost;
        }

        public int getRefreshLowest() {
            return refreshLowest;
        }

        public void setRefreshLowest(int refreshLowest) {
            this.refreshLowest = refreshLowest;
        }

        public int getCostItemId() {
            return costItemId;
        }

        public void setCostItemId(int costItemId) {
            this.costItemId = costItemId;
        }

        public int getOneCostItem() {
            return oneCostItem;
        }

        public void setOneCostItem(int oneCostItem) {
            this.oneCostItem = oneCostItem;
        }

        public int getOneCostGold() {
            return oneCostGold;
        }

        public void setOneCostGold(int oneCostGold) {
            this.oneCostGold = oneCostGold;
        }

        public int getDailyLimitCount() {
            return dailyLimitCount;
        }

        public void setDailyLimitCount(int dailyLimitCount) {
            this.dailyLimitCount = dailyLimitCount;
        }

        public RewardData getGiftData() {
            return giftData;
        }

        public void setGiftData(RewardData giftData) {
            this.giftData = giftData;
        }

        public int getOnLineTime() {
            return onLineTime;
        }

        public void setOnLineTime(int onLineTime) {
            this.onLineTime = onLineTime;
        }

        public int getTimesLimit() {
            return timesLimit;
        }

        public void setTimesLimit(int timesLimit) {
            this.timesLimit = timesLimit;
        }

        public List<RewardData> getFreeGift() {
            return freeGift;
        }

        public void setFreeGift(List<RewardData> freeGift) {
            this.freeGift = freeGift;
        }

        public HashMap<Integer, EggWeight> getEggWeightMap() {
            return eggWeightMap;
        }

        public void setEggWeightMap(HashMap<Integer, EggWeight> eggWeightMap) {
            this.eggWeightMap = eggWeightMap;
        }

        public HashMap<Integer, List<RewardPoolData>> getRewardPoolMap() {
            return rewardPoolMap;
        }

        public void setRewardPoolMap(HashMap<Integer, List<RewardPoolData>> rewardPoolMap) {
            this.rewardPoolMap = rewardPoolMap;
        }

        public HashMap<Integer, LowestData> getLowestMap() {
            return lowestMap;
        }

        public void setLowestMap(HashMap<Integer, LowestData> lowestMap) {
            this.lowestMap = lowestMap;
        }

        public HashMap<Integer, List<RewardData>> getCountRewardMap() {
            return countRewardMap;
        }

        public void setCountRewardMap(HashMap<Integer, List<RewardData>> countRewardMap) {
            this.countRewardMap = countRewardMap;
        }
    }
}
