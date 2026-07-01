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
 * 聚宝盆 300026
 */
public class CornucopiaActivityScript implements IActivityScript, IActivityLucky {

    final transient Logger logger = LogManager.getLogger(CornucopiaActivityScript.class);
    private static final String configData = "configData";
    final transient String totalCount = "totalCount";            //累计抽奖次数
    final transient String lowestDrawCount = "lowestDrawCount";  //抽奖保底次数
    final transient String lowestDrawMap = "lowestDrawMap";      //抽奖保底状态map
    final transient String lowestGoldCount = "lowestGoldCount";  //元宝池保底抽奖次数
    final transient String totalGold = "totalGold";              //元宝池累计元宝数量
    final transient String roleDailyGoldCount = "roleDailyGoldCount";//每日角色中元宝大奖次数
    final transient String dailyAddGold = "dailyAddGold";        //每日系统增加的元宝数
    final transient String accReward = "accReward";              //累计次数奖励
    final transient String activeReward = "activeReward";        //每日活跃奖励
    final transient String playerHistory = "playerHistory";      //个人抽奖记录
    final transient String serverHistory = "serverHistory";      //全服抽奖记录
    final transient String goldHistory = "goldHistory";          //全服元宝池抽奖记录
    final transient int playerHistoryLen = 10;                   //个人抽奖记录长度
    final transient int serverHistoryLen = 10;                   //服务器抽奖记录长度
    final transient int goldHistoryLen = 10;                     //元宝池抽奖记录长度

    @Override
    public void onReqActivityDeal(Player player, String dataStr, ActivityConfig actCfg) {
        CornucopiaActivity cornucopiaActivity = (CornucopiaActivity) actCfg.getCustomCfgMap().get(configData);
        if (cornucopiaActivity == null) {
            logger.error("聚宝盆活动自定义配置参数不存在");
            return;
        }
        //重要的参数在这里验证一下，不然要gg
        if (cornucopiaActivity.getOneCostGold() < 1 || cornucopiaActivity.getOneCostItem() < 1) {
            logger.error("聚宝盆活动中配置的消耗为0");
            return;
        }
        HashMap<String, Integer> msg = JsonUtils.parseObject(dataStr, new TypeReference<HashMap<String, Integer>>() {});
        int operate = msg.get("operate"); //1=抽奖2=领取累计奖励3=领取活跃值奖励
        if (operate == 1) {//抽奖
            Integer once = msg.get("once");
            if (once == null) {
                logger.error("聚宝盆活动请求抽奖参数为空");
                return;
            }

            dealDraw(player, once == 1, cornucopiaActivity, actCfg);
        } else if (operate == 2) {//领取累计奖励
            Integer count = msg.get("count");
            if (count == null) {
                logger.error("聚宝盆活动请求领取累计奖励参数为空");
                return;
            }
            dealCountReward(player, count, cornucopiaActivity, actCfg);
        } else if (operate == 3) {//领取每日活跃奖励
            Integer count = msg.get("count");
            if (count == null) {
                logger.error("聚宝盆活动请求领取每日活跃奖励参数为空");
                return;
            }
            dealActiveReward(player, count, cornucopiaActivity, actCfg);
        }
    }

    /**
     * 抽奖一次或十次
     */
    private void dealDraw(Player player, boolean once, CornucopiaActivity cornucopiaActivity, ActivityConfig actCfg) {
        int useItemTimes = 0, useGoldTimes = 0;
        int drawCount = once ? 1 : 10;
        int costItemId = cornucopiaActivity.getItemId();
        int costItemCount = 1; //单次道具固定扣除数量为1
        int costGoldCount = cornucopiaActivity.getOneCostGold();
        if(once){
            //优先使用道具
            if (!Manager.backpackManager.manager().onRemoveItem(player, costItemId, costItemCount, ItemChangeReason.CornucopiaCost, IDConfigUtil.getLogId())) {
                //道具没扣成功，就是没有道具，则扣元宝
                if (!Manager.currencyManager.manager().onDecItemCoin(player, costGoldCount, ItemChangeReason.CornucopiaCost, IDConfigUtil.getLogId(), ItemCoinType.GemCoin)) {
                    //元宝不够
//                    logger.error("聚宝盆活动抽奖扣除的元宝不足");
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
        }else{//十连抽
            //当前有多少个道具
            int itemNum = Manager.backpackManager.manager().getItemNum(player, costItemId);
            if (itemNum < costItemCount) {//抽一次的道具都不够
                //扣十连抽的元宝
                if (!Manager.currencyManager.manager().onDecItemCoin(player, costGoldCount * 10, ItemChangeReason.CornucopiaCost, IDConfigUtil.getLogId(), ItemCoinType.GemCoin)) {
                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.CurrencyNotEnough, Manager.backpackManager.manager().getName(ItemCoinType.GemCoin));
                    return;
                }
                useGoldTimes = 10;
            } else if (itemNum < costItemCount * 10) {//道具数量小于十连抽需要的，要看金元宝够不够差价
                //元宝不够
                if (Manager.currencyManager.manager().getCurrencyIntNum(player, ItemCoinType.GemCoin) < (10 - itemNum) * costGoldCount) {
                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.CurrencyNotEnough, Manager.backpackManager.manager().getName(ItemCoinType.GemCoin));
                    return;
                }else {//元宝够,则同时扣元宝和所有道具
                    //为了保险，这里再判定一次
                    if (!Manager.backpackManager.manager().onRemoveItem(player, costItemId, itemNum, ItemChangeReason.CornucopiaCost, IDConfigUtil.getLogId())) {
                        logger.error("1不应该走到这里" + player.getId());
                        return;
                    }
                    if (!Manager.currencyManager.manager().onDecItemCoin(player, (10 - itemNum) * costGoldCount, ItemChangeReason.CornucopiaCost, IDConfigUtil.getLogId(), ItemCoinType.GemCoin)) {
                        logger.error("2不应该走到这里" + player.getId());
                        return;
                    }
                    useItemTimes = itemNum;
                    useGoldTimes = 10 - itemNum;
                }
            } else {//道具大于等于10个，直接扣10个
                //为了保险，这里再判定一次
                if (!Manager.backpackManager.manager().onRemoveItem(player, costItemId, costItemCount*10, ItemChangeReason.CornucopiaCost, IDConfigUtil.getLogId())) {
                    logger.error("3不应该走到这里" + player.getId());
                    return;
                }
                useItemTimes = 10;
            }
        }

        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(), actCfg.getType());
        Integer totalDrawCount = (Integer) roleActDataMap.getOrDefault(totalCount, 0);
        Integer lowestCount = (Integer) roleActDataMap.getOrDefault(lowestDrawCount, 0);
        Integer goldCount = (Integer) roleActDataMap.getOrDefault(lowestGoldCount, 0);
        Integer dailyGoldCount = (Integer) roleActDataMap.getOrDefault(roleDailyGoldCount, 0);

        ConcurrentHashMap<String, Object> actDataMap = Manager.activityManager.deal().getActivityData(actCfg.getType());
        Integer totalPoolGold = (Integer)actDataMap.getOrDefault(totalGold, cornucopiaActivity.getGoldInitCount());

        //抽奖记录表，用于返给前端
        List<String> playerHistorys;
        List<String> serverHistorys;
        List<String> goldHistorys;
        if(roleActDataMap.get(playerHistory) != null){
            playerHistorys = (List<String>)roleActDataMap.get(playerHistory);
        }else{
            playerHistorys = new ArrayList<>();
            roleActDataMap.put(playerHistory, playerHistorys);
        }
        if(actDataMap.get(serverHistory) != null){
            serverHistorys = (List<String>)actDataMap.get(serverHistory);
        }else{
            serverHistorys = new ArrayList<>();
            actDataMap.put(serverHistory,serverHistorys);
        }
        if(actDataMap.get(goldHistory) != null){
            goldHistorys = (List<String>)actDataMap.get(goldHistory);
        }else{
            goldHistorys = new ArrayList<>();
            actDataMap.put(goldHistory,goldHistorys);
        }

        //保底序号MAP<保底序号, 状态>
        LinkedHashMap<String, Integer> lowestMap;
        if(roleActDataMap.get(lowestDrawMap) != null){
            lowestMap = (LinkedHashMap<String, Integer>) roleActDataMap.get(lowestDrawMap);
        }else{
            lowestMap = new LinkedHashMap<>();
            //初始化保底序号MAP
            for (int i = 0; i < cornucopiaActivity.getLowestData().size(); i++) {
                lowestMap.put(String.valueOf(i), 0);
            }
            roleActDataMap.put(lowestDrawMap, lowestMap);
        }

        HashMap<Integer, LowestData> lowestDatas = cornucopiaActivity.getLowestData();
        HashMap<Integer, LevelWeight> levelWeightMap = cornucopiaActivity.getLevelWeightMap();
        int maxLowestCount = calcLowestMax(lowestDatas);
        //抽奖奖励
        List<RewardData> lastList = new ArrayList<>();
        //元宝池奖励
        List<Integer> goldList = new ArrayList<>();
        int bigCount = 0;

        //优先级规则：保底>幸运值>抽奖限制系数
        //开始抽奖
        for (int times = 0; times < drawCount; times++) {
            boolean useGold = false;
            if(useItemTimes-(times+1)<=0){
                useGold = true;
            }
            //0=初始状态 1=保底 2=幸运值
            int tag = 0;
            int lv = -1;

            totalDrawCount+=1;
            lowestCount += 1;

            //先检查是否满足保底
            List<RewardData> drawReward = null;
            LowestData lowestData = getCurLowestData(lowestCount, lowestDatas);
            if(lowestData != null){
                List<RewardData> lowestRewards = getLowestReward(lowestCount, lowestData, lowestMap);
                if(lowestRewards!=null){
                    tag = 1;
                    drawReward = lowestRewards;
                    //设置当前保底档位状态为已抽取
                    lowestMap.put(String.valueOf(lowestData.getIndex()), 1);
                    //检查是否所有保底档位都抽取完
                    if(lowestCount >= maxLowestCount && checkAllLowest(lowestMap)){
                        lowestCount = 0;//重置当前循环的保底抽奖次数
                        clearLowestMap(cornucopiaActivity.getLowestData(), lowestMap);
                    }
                }
            }

            boolean isTriggerLucky = false;
            if(tag==0){
                //增加幸运值
                incrLucky(player, cornucopiaActivity);
                isTriggerLucky = isTriggerLucky(player, cornucopiaActivity);
                if(isTriggerLucky){//幸运值达到直接获得大奖
                    tag=2;
                    lv=0;
                }else{//如果没有触发幸运值，则进行普通抽奖

                    //检查随机各等级资格,获得排除的奖品等级
                    List<Integer> exLv = getExList(cornucopiaActivity, totalDrawCount);
                    //先随机出奖品等级
                    lv = getRandomLevelByWeight(levelWeightMap, useGold, exLv);
                }
            };

            //在对应的奖池中抽奖
            if(drawReward == null){
                drawReward = getRandomRewardByWeight(cornucopiaActivity.getRewardPoolMap().get(lv), useGold);
            }

            RewardData rewardData = getRewardByCareer(player.getCareer(), drawReward);

            //由幸运值触发则清理幸运值
            RewardData lucky = Utils.findOne(cornucopiaActivity.getLuckyAwardList(), i -> i.getC() == 9 || i.getC() == player.getCareer());
            RewardData lastReward = isTriggerLucky ? lucky : rewardData;

            //进最近一次抽奖记录表，用于返给前端
            lastList.add(lastReward);

            List<Item> items = Item.createItems(lastReward.getI(), lastReward.getN(), lastReward.getB() == 1);
            cleanLucky(player, cornucopiaActivity, items);

            //添加抽奖历史记录
            if(playerHistorys.size()>playerHistoryLen){
                playerHistorys.remove(0);
            }
            playerHistorys.add(new StringBuilder(String.valueOf(lastReward.getI())).append("_").append(String.valueOf(lastReward.getN())).toString());

            if(serverHistorys.size()>serverHistoryLen){
                serverHistorys.remove(0);
            }
            serverHistorys.add(new StringBuilder(player.getName()).append("_").append(String.valueOf(lastReward.getI())).append("_").append(String.valueOf(lastReward.getN())).toString());

            //测试日志
//            if (ServerConfig.isTestServer()) {
//                long variant = Manager.countManager.getVariant(player, VariantType.ACTIVITY_LUCKY_VALUE);
//                Manager.chatManager.sendSystemStrToPlayer(player, "当前幸运值=" + variant);
//            }

            if (lv == 0){//大奖发通知
                Cfg_Item_Bean itemBean = CfgManager.getCfg_Item_Container().getValueByKey(lastReward.getI());
                String itemName = ServerStr.getChatTableName(itemBean.getName());
                int itemNum = lastReward.getN();
                MessageUtils.notify_AllServer(Notify.EXCLUSIVE_NOTIFY, ChatChannel.CHATCHANNEL_SYSTEM, MessageString.luck_draw_radio_notice4,
                        ServerConfig.getServerId(),
                        player.getId(),
                        player.getName(),
                        actCfg.getName(),
                        itemName,
                        itemNum);
            }

            //TODO 元宝池抽奖
            //保底次数增加
            int curGoldCount = goldCount+1;
            //消耗的元宝加入元宝池
            if(totalPoolGold+costGoldCount>cornucopiaActivity.getGoldMaxCount()){
                totalPoolGold = cornucopiaActivity.getGoldMaxCount();
            }else{
                totalPoolGold += costGoldCount;
            }

            int min = cornucopiaActivity.getGoldBigMin();
            int max = cornucopiaActivity.getGoldBigMax();
            //达到每日最大中奖次数
            if(dailyGoldCount>=cornucopiaActivity.getGoldDailyCount()){
                if(curGoldCount<=max){
                    goldCount+=1;
                }
//                logger.info("达到每日最大中奖次数，count："+dailyGoldCount);
                continue;
            }

            //检查是否有资格触发大奖
            if(curGoldCount<=cornucopiaActivity.getLimitGold()){
                //不会抽到大奖直接跳到下一抽
                goldCount+=1;
                continue;
            }

            //开始概率抽奖
            int goldPro = useGold?cornucopiaActivity.getGoldPro():cornucopiaActivity.getGoldItemPro();
            boolean bigBomb = false;

            if(min<=curGoldCount&&curGoldCount<=max){//次数到达保底范围
                if(RandomUtils.defaultIsGenerate(getGoldLowestPro(min,max,curGoldCount))){
                    bigBomb = true;
                }else if(curGoldCount == max){//到达保底最大值
                    bigBomb = true;
                }
            }else{//普通抽奖
                bigBomb = RandomUtils.defaultIsGenerate(goldPro);
            }

            if(bigBomb){
                int bigGold = (int)(totalPoolGold*((float)cornucopiaActivity.goldPoolPer/10000.0f));
                //奖励如果大于单次可抽出的上限，则只发送上限数量的元宝
                if(bigGold>cornucopiaActivity.getGoldOneMaxCount()){
                    bigGold = cornucopiaActivity.getGoldOneMaxCount();
                }
                totalPoolGold=totalPoolGold-bigGold;
                bigCount += 1;
                dailyGoldCount += 1;
                //抽奖记录
                if(goldHistorys.size()>goldHistoryLen){
                    goldHistorys.remove(0);
                }
                goldHistorys.add(new StringBuilder(new StringBuilder(player.getName()).append("_").append(String.valueOf(bigGold))).toString());
                goldList.add(bigGold);
                goldCount = 0;//保底次数清空
            }else{
                goldCount += 1;
            }
        }

        //奖励直接发背包
        List<Item> items = Item.createItems(player.getCareer(), lastList);
        //抽奖赠送奖励
        int giftNum = cornucopiaActivity.getGiftData().getN()*useGoldTimes;
        if(giftNum>0){
            Item giftItem = Item.createItem(cornucopiaActivity.getGiftData().getI(), giftNum, cornucopiaActivity.getGiftData().getB() == 1);
            items.add(giftItem);
        }

        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.CornucopiaGet, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.CornucopiaGet);
        }

        //元宝池奖励
        int addGoldReward=0;
        for (int gold:goldList) {
            addGoldReward+=gold;
        }
        if(addGoldReward>0){
            if (!Manager.currencyManager.manager().onAddItemCoin(player, ItemCoinType.GemCoin, addGoldReward, ItemChangeReason.CornucopiaGoldGet, IDConfigUtil.getLogId())) {
                List<Item> addGoldItem = Item.createItems(ItemCoinType.GemCoin, addGoldReward, true);
                Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                        MessageString.System, MessageString.NoBagCell, addGoldItem, ItemChangeReason.CornucopiaGoldGet);
            }
        }

        //存储数据
        roleActDataMap.put(totalCount, totalDrawCount);
        roleActDataMap.put(lowestDrawCount, lowestCount);
        roleActDataMap.put(lowestDrawMap, lowestMap);
        roleActDataMap.put(lowestGoldCount, goldCount);
        roleActDataMap.put(playerHistory, playerHistorys);
        roleActDataMap.put(roleDailyGoldCount, dailyGoldCount);

        actDataMap.put(totalGold, totalPoolGold);
        actDataMap.put(serverHistory, serverHistorys);
        actDataMap.put(goldHistory, goldHistorys);
        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));
        Manager.activityManager.deal().saveActData(actCfg.getType(), actDataMap);

        HashMap<String, Object> result = new HashMap<>();
        result.put("operate", 1);//0上线 1抽奖 2领取次数奖励
        result.put("drawCount", totalDrawCount);
        result.put("drawLowestMap", lowestMap);
        result.put("drawLowestCount", lowestCount);//当前保底抽奖次数
        result.put("gold", totalPoolGold);
        result.put("reward", lastList);
        result.put("goldReward", goldList);
        result.put("selfHistory", playerHistorys.subList(playerHistorys.size()-drawCount, playerHistorys.size()));//只发增量
        result.put("serverHistory", serverHistorys.subList(serverHistorys.size()-drawCount, serverHistorys.size()));//只发增量
        result.put("goldHistory", goldHistorys.subList(goldHistorys.size()-bigCount, goldHistorys.size()));//只发增量
        result.put("countReward", new TreeMap<Integer, Integer>());

        ActivityMessage.ResActivityDeal.Builder pb = ActivityMessage.ResActivityDeal.newBuilder();
        pb.setData(JsonUtils.toJSONString(result));
        pb.setType(actCfg.getType());
//        logger.info("=========抽奖信息:"+pb.build().toString());
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
     * 100%/（最大-最小）*(当前次n-最小)【最小正整数，小于0即等于0】=保底触发概率。
     * 当前次应该是包底开始次数，而不是总抽奖次数
     * @param min
     * @param max
     * @param goldCount
     * @return
     */
    private int getGoldLowestPro(int min, int max, Integer goldCount) {
        float base = 1/(float)(max-min)*(float)(goldCount-min<=0?0:goldCount-min);
        return (int) (Math.pow(base, 13)*10000);
    }

    private RewardData getRewardByCareer(byte career, List<RewardData> randomReward) {
        for (RewardData cfg : randomReward) {
            if (cfg.getC() == 9 || cfg.getC() == career) {
                return cfg;
            }
        }
        return randomReward.get(0);
    }

    private List<Integer> getExList(CornucopiaActivity cornucopiaActivity, int totalCount) {
        List<Integer> exList = new ArrayList<>();
        for (int i = 0; i <= 2; i++) {//三等奖（最低奖不可能排除，不考虑）
            if(i==0&&totalCount<=cornucopiaActivity.getLimitLv()){
                exList.add(0);
            }else if(i==1&&totalCount<=cornucopiaActivity.getLimitLv1()){
                exList.add(1);
            }else if(i==2&&totalCount<=cornucopiaActivity.getLimitLv2()){
                exList.add(2);
            }
        }
        return exList;
    }

    private int getRandomLevelByWeight(HashMap<Integer, LevelWeight> levelWeightMap, boolean useGold, List<Integer> exLv) {
        int result = 3;
        Integer weightSum = 0;
        for (Map.Entry<Integer, LevelWeight> data : levelWeightMap.entrySet()) {
            if(exLv.contains(data.getKey())){
                continue;
            }
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
        for (Map.Entry<Integer, LevelWeight> data : levelWeightMap.entrySet()) {
            if(exLv.contains(data.getKey())){
                continue;
            }
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

    private List<RewardData> getRandomRewardByWeight(List<RewardPoolData> list, boolean useGold) {
        List<RewardData> result = null;
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
                result = data.getRewardData();
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

    private LowestData getCurLowestData(Integer count, HashMap<Integer, LowestData> lowestDatas){
        for (LowestData lowestData:lowestDatas.values()) {
            if(lowestData.getMin()<=count&&count<=lowestData.getMax()){
                return lowestData;
            }
        }
        return null;
    }

//    private int getLowestLevel(Integer count, HashMap<Integer, Integer> lowestData) {
//        int result = -1;
//        for (Map.Entry<Integer, Integer> entry:lowestData.entrySet()) {
//            int type = entry.getKey();
//            if(type == 3){//排除最低奖励
//                continue;
//            }
//            int lowestCount = entry.getValue();
//            if(lowestCount<=0){
//                continue;
//            }
//            if (count/lowestCount>1?count%lowestCount==0:count==lowestCount) {
//                result = type;
//                break;
//            }
//        }
//        return result;
//    }

    /**
     * 领取累积次数奖励
     */
    private void dealCountReward(Player player, int count, CornucopiaActivity cornucopiaActivity, ActivityConfig actCfg) {
        if (cornucopiaActivity.getAccRewardMap().get(count)==null) {
            logger.error("领取累积次数奖励时档次不合法" + count);
            return;
        }
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(), actCfg.getType());
        Integer totalDrawCount = (Integer) roleActDataMap.getOrDefault(totalCount, 0);

        ConcurrentHashMap<String, Object> actDataMap = Manager.activityManager.deal().getActivityData(actCfg.getType());
        Integer totalPoolGold = (Integer)actDataMap.getOrDefault(totalGold, cornucopiaActivity.getGoldInitCount());

        LinkedHashMap<String, Integer> countRewardMap = getCountRewardMap(cornucopiaActivity, roleActDataMap);
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
        List<RewardData> reward = cornucopiaActivity.getAccRewardMap().get(count);
        List<Item> items = Item.createItems(player.getCareer(), reward);
        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.CornucopiaCountGet, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.CornucopiaCountGet);
        }

        //设置奖励领取状态
        countRewardMap.put(String.valueOf(count), 1);
        roleActDataMap.put(accReward, countRewardMap);

        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));
//        Manager.activityManager.deal().saveActData(actCfg.getType(), actDataMap);
        //返给前端
        HashMap<String, Object> result = new HashMap<>();
        result.put("operate", 2);//0上线 1抽奖 2领取次数奖励
        result.put("drawCount", totalDrawCount);
        result.put("gold", totalPoolGold);
        result.put("reward", new ArrayList<>());
        result.put("goldReward", new ArrayList<>());
        result.put("selfHistory", new ArrayList<>());
        result.put("serverHistory", new ArrayList<>());
        result.put("goldHistory", new ArrayList<>());
        result.put("countReward", countRewardMap);

        ActivityMessage.ResActivityDeal.Builder pb = ActivityMessage.ResActivityDeal.newBuilder();
        pb.setData(JsonUtils.toJSONString(result));
        pb.setType(actCfg.getType());
        MessageUtils.send_to_player(player, ActivityMessage.ResActivityDeal.MsgID.eMsgID_VALUE, pb.build().toByteArray());
    }

    private LinkedHashMap<String, Integer> getCountRewardMap(CornucopiaActivity cornucopiaActivity, ConcurrentHashMap<String, Object> roleActDataMap) {
        LinkedHashMap<String, Integer> countRewardMap;
        if(roleActDataMap.get(accReward)!=null){
            countRewardMap = (LinkedHashMap<String, Integer>)roleActDataMap.get(accReward);
        }else{
            countRewardMap = new LinkedHashMap<>();
            for (Integer accCount:cornucopiaActivity.getAccRewardMap().keySet()) {
                countRewardMap.put(String.valueOf(accCount), 0);
            }
            roleActDataMap.put(accReward, countRewardMap);
        }
        return countRewardMap;
    }

    /**
     * 领取活跃值奖励
     */
    private void dealActiveReward(Player player, int count, CornucopiaActivity cornucopiaActivity, ActivityConfig actCfg) {
        if (cornucopiaActivity.getFreeGiftMap().get(count)==null) {
            logger.error("领取每日活跃奖励时档次不合法" + count);
            return;
        }
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(), actCfg.getType());
        Integer totalDrawCount = (Integer) roleActDataMap.getOrDefault(totalCount, 0);

        ConcurrentHashMap<String, Object> actDataMap = Manager.activityManager.deal().getActivityData(actCfg.getType());
        Integer totalPoolGold = (Integer)actDataMap.getOrDefault(totalGold, cornucopiaActivity.getGoldInitCount());

        LinkedHashMap<String, Integer> activeRewardMap = getActiveRewardMap(cornucopiaActivity, roleActDataMap);
        int state = activeRewardMap.get(String.valueOf(count));
        if(state == 1){//已经领了
            logger.error("领取每日活跃奖励失败,奖励已经领取。 count="+count);
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.TOUZI_GETFAIL);
            return;
        }

        //检查领奖的活跃值是否够了
        if(player.getDailyActiveData().getActiveNum()<count){
            logger.error("领取每日活跃奖励失败,活跃值不足。 count="+count+",curCount="+player.getDailyActiveData().getActiveNum());
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.TOUZI_GETFAIL);
            return;
        }

        //发奖
        List<RewardData> reward = cornucopiaActivity.getFreeGiftMap().get(count);
        List<Item> items = Item.createItems(player.getCareer(), reward);
        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.CornucopiaActiveGet, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.CornucopiaActiveGet);
        }

        //设置奖励领取状态
        activeRewardMap.put(String.valueOf(count), 1);
        roleActDataMap.put(activeReward, activeRewardMap);

        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));
//        Manager.activityManager.deal().saveActData(actCfg.getType(), actDataMap);
        //返给前端
        HashMap<String, Object> result = new HashMap<>();
        result.put("operate", 3);//0上线 1抽奖 2领取累计次数奖励 3领取活跃奖励
        result.put("drawCount", totalDrawCount);
        result.put("gold", totalPoolGold);
        result.put("reward", new ArrayList<>());
        result.put("goldReward", new ArrayList<>());
        result.put("selfHistory", new ArrayList<>());
        result.put("serverHistory", new ArrayList<>());
        result.put("goldHistory", new ArrayList<>());
        result.put("countReward", new HashMap<>());
        result.put("activeState", activeRewardMap);

        ActivityMessage.ResActivityDeal.Builder pb = ActivityMessage.ResActivityDeal.newBuilder();
        pb.setData(JsonUtils.toJSONString(result));
        pb.setType(actCfg.getType());
        MessageUtils.send_to_player(player, ActivityMessage.ResActivityDeal.MsgID.eMsgID_VALUE, pb.build().toByteArray());
    }

    private LinkedHashMap<String, Integer> getActiveRewardMap(CornucopiaActivity cornucopiaActivity, ConcurrentHashMap<String, Object> roleActDataMap) {
        LinkedHashMap<String, Integer> activeRewardMap;
        if(roleActDataMap.get(activeReward)!=null){
            activeRewardMap = (LinkedHashMap<String, Integer>)roleActDataMap.get(activeReward);
        }else{
            activeRewardMap = new LinkedHashMap<>();
            for (Integer count:cornucopiaActivity.getFreeGiftMap().keySet()) {
                activeRewardMap.put(String.valueOf(count), 0);
            }
            roleActDataMap.put(activeReward, activeRewardMap);
        }
        return activeRewardMap;
    }

    @Override
    public boolean parseCustomConfig(ActivityConfig actCfg, String customStr) {
        CornucopiaActivity data = JsonUtils.parseObject(customStr, CornucopiaActivity.class);
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
        CornucopiaActivity cornucopiaActivity = (CornucopiaActivity) actCfg.getCustomCfgMap().get(configData);
        if (cornucopiaActivity == null) {
            logger.error("许愿池活动活动自定义配置参数不存在");
            return null;
        }

        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(roleId, actCfg.getType());
        Integer totalDrawCount = (Integer) roleActDataMap.getOrDefault(totalCount, 0);
//        Integer goldCount= (Integer) roleActDataMap.getOrDefault(lowestGoldCount, 0);
        Integer lowestCount = (Integer) roleActDataMap.getOrDefault(lowestDrawCount, 0);

        ConcurrentHashMap<String, Object> actDataMap = Manager.activityManager.deal().getActivityData(actCfg.getType());
        Integer totalPoolGold = (Integer)actDataMap.getOrDefault(totalGold, cornucopiaActivity.getGoldInitCount());

        //抽奖记录表，用于返给前端
        List<String> playerHistorys;
        List<String> serverHistorys;
        List<String> goldHistorys;
        if(roleActDataMap.get(playerHistory) != null){
            playerHistorys = (List<String>)roleActDataMap.get(playerHistory);
        }else{
            playerHistorys = new ArrayList<>();
            roleActDataMap.put(playerHistory, playerHistorys);
        }
        if(actDataMap.get(serverHistory) != null){
            serverHistorys = (List<String>)actDataMap.get(serverHistory);
        }else{
            serverHistorys = new ArrayList<>();
            actDataMap.put(serverHistory,serverHistorys);
        }
        if(actDataMap.get(goldHistory) != null){
            goldHistorys = (List<String>)actDataMap.get(goldHistory);
        }else{
            goldHistorys = new ArrayList<>();
            actDataMap.put(goldHistory,goldHistorys);
        }

        //保底序号MAP<保底序号, 状态>
        LinkedHashMap<String, Integer> lowestMap;
        if(roleActDataMap.get(lowestDrawMap) != null){
            lowestMap = (LinkedHashMap<String, Integer>) roleActDataMap.get(lowestDrawMap);
        }else{
            lowestMap = new LinkedHashMap<>();
            //初始化保底序号MAP
            for (int i = 0; i < cornucopiaActivity.getLowestData().size(); i++) {
                lowestMap.put(String.valueOf(i), 0);
            }
            roleActDataMap.put(lowestDrawMap, lowestMap);
        }

        LinkedHashMap<String, Integer> countRewardMap = getCountRewardMap(cornucopiaActivity, roleActDataMap);
        LinkedHashMap<String, Integer> activeRewardMap = getActiveRewardMap(cornucopiaActivity, roleActDataMap);

        HashMap<String, Object> result = new HashMap<>();
        result.put("operate", 0);//0上线 1抽奖 2领取次数奖励
        result.put("drawCount", totalDrawCount);
        result.put("drawLowestMap", lowestMap);
        result.put("drawLowestCount", lowestCount);//当前保底抽奖次数
        result.put("gold", totalPoolGold);
        result.put("reward", new ArrayList<>());
        result.put("goldReward", new ArrayList<>());
        result.put("selfHistory", playerHistorys);
        result.put("serverHistory", serverHistorys);
        result.put("goldHistory", goldHistorys);
        result.put("countReward", countRewardMap);
        result.put("activeState", activeRewardMap);
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
        roleActDataMap.put(roleDailyGoldCount, 0);

        CornucopiaActivity cornucopiaActivity = (CornucopiaActivity) actCfg.getCustomCfgMap().get(configData);
        if (cornucopiaActivity != null) {
            LinkedHashMap<String, Integer> activeRewardMap = new LinkedHashMap<>();
            for (Integer count:cornucopiaActivity.getFreeGiftMap().keySet()) {
                activeRewardMap.put(String.valueOf(count), 0);
            }
            roleActDataMap.put(activeReward, activeRewardMap);
        }
    }

    @Override
    public void fiveClockPlayerDeal(Player player, ActivityConfig actCfg) {

    }

    @Override
    public void zeroClockDeal(ActivityConfig actCfg) {
        ConcurrentHashMap<String, Object> actDataMap = Manager.activityManager.deal().getActivityData(actCfg.getType());
        if(actDataMap!=null){
            //零点清空每日系统增加上限
            actDataMap.put(dailyAddGold, 0);
            Manager.activityManager.deal().saveActData(actCfg.getType(), actDataMap);
        }
    }

    @Override
    public void fiveClockDeal(ActivityConfig actCfg) {

    }


    @Override
    public void everyHourDeal(ActivityConfig actCfg) {
        int curHour = TimeUtils.getDayOfHour(TimeUtils.Time());
        if(1<curHour&&curHour<12){//检查时间 每天12点开始~1点结束
            return;
        }

        CornucopiaActivity cornucopiaActivity = (CornucopiaActivity) actCfg.getCustomCfgMap().get(configData);
        ConcurrentHashMap<String, Object> actDataMap = Manager.activityManager.deal().getActivityData(actCfg.getType());
        Integer totalPoolGold = (Integer)actDataMap.getOrDefault(totalGold, cornucopiaActivity.getGoldInitCount());
        Integer dailySysAddGold = (Integer)actDataMap.getOrDefault(dailyAddGold, 0);

        if(totalPoolGold>=cornucopiaActivity.getSysAddBaseValue()){
            //大于基准值
            return;
        }
        if(dailySysAddGold>=cornucopiaActivity.getSysAddLimit()){
            //大于上限
            return;
        }

        int addGold = cornucopiaActivity.getSysAddCount();
        addGold=addGold+(int)(addGold*((float)RandomUtils.random(1000, 3000)/10000.0f));

        if(totalPoolGold+addGold>cornucopiaActivity.getGoldMaxCount()){
            totalPoolGold = cornucopiaActivity.getGoldMaxCount();
        }else{
            totalPoolGold+=addGold;
        }
        dailySysAddGold+=addGold;

        actDataMap.put(totalGold, totalPoolGold);
        actDataMap.put(dailyAddGold, dailySysAddGold);

        Manager.activityManager.deal().saveActData(actCfg.getType(), actDataMap);
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
        List<ActivityConfig> actCfgList = Manager.activityManager.deal().getActCfgListByActLogicID(ActivityType.Cornucopia);
        for (ActivityConfig activityConfig:actCfgList) {
            if (activityConfig == null) {
                return;
            }
            Object old = activityConfig.getCustomCfgMap().get(configData);
            if (old == null) {
                return;
            }
            String customStr = JsonUtils.toJSONString(old);
            CornucopiaActivity newData = JsonUtils.parseObject(customStr, CornucopiaActivity.class);
            activityConfig.getCustomCfgMap().put(configData, newData);
        }
    }

    @Override
    public int getId() {
        return ScriptEnum.CornucopiaScript;
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

    //奖品等级权重
    static class LevelWeight {
        private int itemWeight;  //道具抽奖权重
        private int goldWeight;  //元宝抽奖权重

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

    //奖池信息
    static class RewardPoolData {
        private int itemWeight;  //道具抽奖权重
        private int goldWeight; //元宝抽奖权重
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

        public List<RewardData> getRewardData() {
            return rewardData;
        }

        public void setRewardData(List<RewardData> rewardData) {
            this.rewardData = rewardData;
        }
    }

    //聚宝盆活动数据
    static class CornucopiaActivity extends ActivityLucky {
        private String client;  //前端数据
        //奖池抽奖
        private int limitLv;//抽n次大奖之后才可能中大奖
        private int limitLv1;//抽n次一等奖之后才可能中大奖
        private int limitLv2;//抽n次二等奖之后才可能中大奖
        private int limitLv3;//抽n次三等奖之后才可能中大奖

        private int itemId;     //道具ID
        private int oneCostItem;//一次消耗道具
        private int tenCostItem;//十连抽消耗道具
        private int oneCostGold;//一次消耗元宝
        private int tenCostGold;//十连抽消耗元宝
        private RewardData giftData;//每抽赠送道具

        //元宝奖池
        private int goldItemPro;    //元宝池道具抽奖触发概率
        private int goldPro;        //元宝池元宝抽奖触发概率
        private int goldInitCount;  //元宝奖池初始数量
        private int goldMaxCount;   //元宝奖池最大上限
        private int goldOneMaxCount;//元宝奖池单次最大可获得数量
        private int goldPoolPer;    //中奖比例，以此比例X奖池当前总值奖励给玩家
        private int goldDailyCount; //元宝池角色每日可中奖次数
        private int goldBigMin;     //元宝保底中奖最小次数
        private int goldBigMax;     //元宝保底中奖最大次数
        private int limitGold;      //抽n次元宝池才可能中大奖

        private int sysAddBaseValue;//系统投注基准，小于此值，系统开始投入
        private int sysAddCount;    //系统单次投入数量
        private int sysAddLimit;    //系统每日投入上限

        //奖品等级权重 <奖品等级, 奖品等级权重>
        private HashMap<Integer, LevelWeight> levelWeightMap;
        //奖池信息 <奖品等级, 奖励信息>
        private HashMap<Integer, List<RewardPoolData>> rewardPoolMap;
        //保底奖励 <序号，保底信息>
        private HashMap<Integer, LowestData> lowestData;
//        //保底奖励  Map<保底次数，奖品等级>   达到保底次数之后随机从的对应的奖品等级的池子中取一个奖品
//        private HashMap<Integer, Integer> lowestData;
        //累计领奖 <累计领奖次数,累计奖励>
        private HashMap<Integer, List<RewardData>> accRewardMap;
        //活跃值奖励领取 <每日达到的活跃值,活跃奖励>
        private HashMap<Integer, List<RewardData>> freeGiftMap;

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public int getLimitLv() {
            return limitLv;
        }

        public void setLimitLv(int limitLv) {
            this.limitLv = limitLv;
        }

        public int getLimitLv1() {
            return limitLv1;
        }

        public void setLimitLv1(int limitLv1) {
            this.limitLv1 = limitLv1;
        }

        public int getLimitLv2() {
            return limitLv2;
        }

        public void setLimitLv2(int limitLv2) {
            this.limitLv2 = limitLv2;
        }

        public int getLimitLv3() {
            return limitLv3;
        }

        public void setLimitLv3(int limitLv3) {
            this.limitLv3 = limitLv3;
        }

        public int getItemId() {
            return itemId;
        }

        public void setItemId(int itemId) {
            this.itemId = itemId;
        }

        public int getOneCostItem() {
            return oneCostItem;
        }

        public void setOneCostItem(int oneCostItem) {
            this.oneCostItem = oneCostItem;
        }

        public int getTenCostItem() {
            return tenCostItem;
        }

        public void setTenCostItem(int tenCostItem) {
            this.tenCostItem = tenCostItem;
        }

        public int getOneCostGold() {
            return oneCostGold;
        }

        public void setOneCostGold(int oneCostGold) {
            this.oneCostGold = oneCostGold;
        }

        public int getTenCostGold() {
            return tenCostGold;
        }

        public void setTenCostGold(int tenCostGold) {
            this.tenCostGold = tenCostGold;
        }

        public RewardData getGiftData() {
            return giftData;
        }

        public void setGiftData(RewardData giftData) {
            this.giftData = giftData;
        }

        public int getGoldItemPro() {
            return goldItemPro;
        }

        public void setGoldItemPro(int goldItemPro) {
            this.goldItemPro = goldItemPro;
        }

        public int getGoldPro() {
            return goldPro;
        }

        public void setGoldPro(int goldPro) {
            this.goldPro = goldPro;
        }

        public int getGoldInitCount() {
            return goldInitCount;
        }

        public void setGoldInitCount(int goldInitCount) {
            this.goldInitCount = goldInitCount;
        }

        public int getGoldMaxCount() {
            return goldMaxCount;
        }

        public void setGoldMaxCount(int goldMaxCount) {
            this.goldMaxCount = goldMaxCount;
        }

        public int getGoldOneMaxCount() {
            return goldOneMaxCount;
        }

        public void setGoldOneMaxCount(int goldOneMaxCount) {
            this.goldOneMaxCount = goldOneMaxCount;
        }

        public int getGoldDailyCount() {
            return goldDailyCount;
        }

        public void setGoldDailyCount(int goldDailyCount) {
            this.goldDailyCount = goldDailyCount;
        }

        public int getGoldPoolPer() {
            return goldPoolPer;
        }

        public void setGoldPoolPer(int goldPoolPer) {
            this.goldPoolPer = goldPoolPer;
        }

        public int getSysAddBaseValue() {
            return sysAddBaseValue;
        }

        public void setSysAddBaseValue(int sysAddBaseValue) {
            this.sysAddBaseValue = sysAddBaseValue;
        }

        public int getSysAddCount() {
            return sysAddCount;
        }

        public void setSysAddCount(int sysAddCount) {
            this.sysAddCount = sysAddCount;
        }

        public int getSysAddLimit() {
            return sysAddLimit;
        }

        public void setSysAddLimit(int sysAddLimit) {
            this.sysAddLimit = sysAddLimit;
        }

        public int getGoldBigMin() {
            return goldBigMin;
        }

        public void setGoldBigMin(int goldBigMin) {
            this.goldBigMin = goldBigMin;
        }

        public int getGoldBigMax() {
            return goldBigMax;
        }

        public void setGoldBigMax(int goldBigMax) {
            this.goldBigMax = goldBigMax;
        }

        public int getLimitGold() {
            return limitGold;
        }

        public void setLimitGold(int limitGold) {
            this.limitGold = limitGold;
        }

        public HashMap<Integer, LevelWeight> getLevelWeightMap() {
            return levelWeightMap;
        }

        public void setLevelWeightMap(HashMap<Integer, LevelWeight> levelWeightMap) {
            this.levelWeightMap = levelWeightMap;
        }

        public HashMap<Integer, List<RewardPoolData>> getRewardPoolMap() {
            return rewardPoolMap;
        }

        public void setRewardPoolMap(HashMap<Integer, List<RewardPoolData>> rewardPoolMap) {
            this.rewardPoolMap = rewardPoolMap;
        }

        public HashMap<Integer, LowestData> getLowestData() {
            return lowestData;
        }

        public void setLowestData(HashMap<Integer, LowestData> lowestData) {
            this.lowestData = lowestData;
        }

        public HashMap<Integer, List<RewardData>> getAccRewardMap() {
            return accRewardMap;
        }

        public void setAccRewardMap(HashMap<Integer, List<RewardData>> accRewardMap) {
            this.accRewardMap = accRewardMap;
        }

        public HashMap<Integer, List<RewardData>> getFreeGiftMap() {
            return freeGiftMap;
        }

        public void setFreeGiftMap(HashMap<Integer, List<RewardData>> freeGiftMap) {
            this.freeGiftMap = freeGiftMap;
        }

        //endregion
    }
}
