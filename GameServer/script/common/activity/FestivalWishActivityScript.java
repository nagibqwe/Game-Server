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
import com.game.bi.struct.BIActiityTypeEnum;
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
 * @author cc
 * @date 20101021
 * 节日许愿活动 300019
 */
public class FestivalWishActivityScript implements IActivityScript, IActivityLucky {

    public static final Logger LOGGER = LogManager.getLogger(FestivalWishActivityScript.class);

    private static final String configData = "configData";

    //活动个人数据存储的key
    private static final String WishValue = "wishValue";    //当前祝福值
    private static final String Score = "score";            //当前积分
    private static final String BoxState = "boxState";      //保底宝箱领没领的位运算。第一个宝箱是1<<0。true就是领过了
    private static final String GoldTimes = "goldTimes";    //用元宝抽了多少次了
    private static final String TempBag = "tempBag";        //临时背包
    //保底
    final transient String P_Lowest = "P_Lowest";                //玩家保底奖励进度
    final transient String S_Lowest_Max = "S_Lowest_Max";        //保底奖励最大进度
    final transient String P_Lowest_State = "P_Lowest_State";    //玩家保底奖励状态

    @Override
    public void onReqActivityDeal(Player player, String dataStr, ActivityConfig actCfg) {
        if (actCfg == null) {
            LOGGER.error("节日许愿活动活动配置不存在");
            return;
        }
        FestivalWish festivalWish = (FestivalWish) actCfg.getCustomCfgMap().get(configData);
        if (festivalWish == null) {
            LOGGER.error("节日许愿活动活动自定义配置参数不存在");
            return;
        }
        //重要的参数在这里验证一下，不然要gg
        if (festivalWish.getOneCostGold() < 1 || festivalWish.getTenCostGold() < 1 || festivalWish.getOneCostKey() < 1 || festivalWish.getTenCostKey() < 1) {
            LOGGER.error("节日许愿活动活动中配置的消耗为0");
            return;
        }
        int startDay = TimeUtils.getCurDayByTime(actCfg.getBeginTime());
        int curDay = TimeUtils.getCurDayByTime(TimeUtils.Time());
        int overDay = TimeUtils.getCurDayByTime(actCfg.getEndTime());
        if (curDay < startDay || curDay > overDay) {
            LOGGER.error("活动时间已结束" + actCfg.getType());
            return;
        }
        HashMap<String, Integer> msg = JsonUtils.parseObject(dataStr, new TypeReference<HashMap<String, Integer>>() {
        });
        Integer wish = msg.get("wish");
        //许愿
        if (wish != null) {
            wish(wish == 1, player, festivalWish, actCfg);
            return;
        }
        //领取保底宝箱
        Integer getBox = msg.get("getBox");
        if (getBox != null) {
            getBox(getBox, player, festivalWish, actCfg);
            return;
        }
        //临时仓库进背包
        if (msg.get("bag") != null) {
            tempStoreToBag(player, actCfg);
//            Manager.biManager.getScript().biActivity(player, ItemChangeReason.FestvialWishExtract, actCfg.getType(), actCfg.getId());
            Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.FestvialWish, ItemChangeReason.FestvialWishExtract);
            return;
        }
        LOGGER.error("非法请求" + dataStr);
    }

    /**
     * 许愿一次或十次
     */
    private void wish(boolean once, Player player, FestivalWish festivalWish, ActivityConfig actCfg) {
        int useKeyTimes = 0, useGoldTimes = 0;  //本次抽奖中，将要使用钥匙和元宝的次数

        if (once) {
            //优先使用钥匙
            if (!Manager.backpackManager.manager().onRemoveItem(player, festivalWish.getKeyId(), festivalWish.getOneCostKey(), ItemChangeReason.FestvialWishCost, IDConfigUtil.getLogId())) {
                //钥匙没扣成功，就是没有钥匙，则扣元宝
                if (!Manager.currencyManager.manager().onDecItemCoin(player, festivalWish.getOneCostGold(), ItemChangeReason.FestvialWishCost, IDConfigUtil.getLogId(), ItemCoinType.GemCoin)) {
                    //元宝不够
                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.CurrencyNotEnough, Manager.backpackManager.manager().getName(ItemCoinType.GemCoin));
                    return;
                } else {
                    //元宝扣成功了
                    useGoldTimes = 1;
                }
            } else {
                //钥匙使用成功了
                useKeyTimes = 1;
            }
        }
        //十连抽
        else {
            //当前有多少个钥匙
            int keyNum = Manager.backpackManager.manager().getItemNum(player, festivalWish.getKeyId());
            //抽一次的钥匙都不够
            if (keyNum < festivalWish.getOneCostKey()) {
                //扣十连抽的元宝
                if (!Manager.currencyManager.manager().onDecItemCoin(player, festivalWish.getTenCostGold(), ItemChangeReason.FestvialWishCost, IDConfigUtil.getLogId(), ItemCoinType.GemCoin)) {
                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.CurrencyNotEnough, Manager.backpackManager.manager().getName(ItemCoinType.GemCoin));
                    return;
                }
                useGoldTimes = 10;
            }
            //钥匙数量小于十连抽需要的钥匙，要看金元宝够不够差价
            else if (keyNum < festivalWish.getTenCostKey()) {
                //元宝不够
                if (Manager.currencyManager.manager().getCurrencyIntNum(player, ItemCoinType.GemCoin) < (10 - keyNum) * festivalWish.getOneCostGold()) {
                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.CurrencyNotEnough, Manager.backpackManager.manager().getName(ItemCoinType.GemCoin));
                    return;
                }
                //元宝够。则同时扣元宝和所有钥匙
                else {
                    //为了保险，这里再判定一次
                    if (!Manager.backpackManager.manager().onRemoveItem(player, festivalWish.getKeyId(), keyNum, ItemChangeReason.FestvialWishCost, IDConfigUtil.getLogId())) {
                        LOGGER.error("1不应该走到这里" + player.getId());
                        return;
                    }
                    if (!Manager.currencyManager.manager().onDecItemCoin(player, (10 - keyNum) * festivalWish.getOneCostGold(), ItemChangeReason.FestvialWishCost, IDConfigUtil.getLogId(), ItemCoinType.GemCoin)) {
                        LOGGER.error("2不应该走到这里" + player.getId());
                        return;
                    }
                    useKeyTimes = keyNum;
                    useGoldTimes = 10 - keyNum;
                }
            }
            //钥匙大于等于10个，直接扣10个
            else {
                //为了保险，这里再判定一次
                if (!Manager.backpackManager.manager().onRemoveItem(player, festivalWish.getKeyId(), festivalWish.getTenCostKey(), ItemChangeReason.FestvialWishCost, IDConfigUtil.getLogId())) {
                    LOGGER.error("3不应该走到这里" + player.getId());
                    return;
                }
                useKeyTimes = 10;
            }
        }
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(), actCfg.getType());
        Integer score = (Integer) roleActDataMap.getOrDefault(Score, 0);
        Integer wishValue = (Integer) roleActDataMap.getOrDefault(WishValue, 0);
        Integer goldTimes = (Integer) roleActDataMap.getOrDefault(GoldTimes, 0);
        List<RewardData> tempBag = (List<RewardData>) roleActDataMap.getOrDefault(TempBag, new ArrayList<RewardData>());

        //进最近一次抽奖记录表，用于返给前端
        ArrayList<RewardData> lastList = new ArrayList<>();

        //一连抽循环一次，十连抽循环十次
        for (int times = 0; times < (once ? 1 : 10); times++) {
            //增加幸运值
            incrLucky(player, festivalWish);

            //触发保底奖励
            FestivalWishActivityScript.LowestData triggerLowestLucky = isTriggerLowestLucky(roleActDataMap, null, festivalWish);
            //是否触发幸运值奖励
            boolean isTriggerLucky = isTriggerLucky(player, festivalWish);
            //是否触发保底奖励
            boolean isTriggerLowestLucky = triggerLowestLucky != null;

            //0=小大奖都有可能爆，1=必定爆大奖 2=必定爆小奖
            int tag = 0;

            //当前祝福值已经达到上限，要爆大奖了。（祝福值优先级最高）
            if (wishValue >= festivalWish.getWishMax() || isTriggerLucky || isTriggerLowestLucky) {
                tag = 1;
            }
            //用钥匙，必定爆小奖
            else if (useKeyTimes > 0) {
                tag = 2;
            }
            //花费元宝次数没有达到运营需求，必定爆小奖
            else if (goldTimes < festivalWish.getWasteGoldCount()) {
                tag = 2;
            } else {
                tag = 0;
            }
            //只能随机到的奖励
            ArrayList<RewardItemTemp> list = new ArrayList<>();
            for (int i = 0; i < festivalWish.getRewardPool().size(); i++) {
                RewardPoolItem item = festivalWish.getRewardPool().get(i);
                //必定爆小奖时，剔除大奖
                if (item.isBig && tag == 2) {
                    continue;
                }
                //必定爆大奖时，剔除小奖
                if (!item.isBig && tag == 1) {
                    continue;
                }
                //非本职业的，剔除
                if (item.rewardData.getC() != 9 && item.rewardData.getC() != player.getCareer()) {
                    continue;
                }
                RewardItemTemp temp = new RewardItemTemp(item.rewardData, item.keyWeight, item.goldWeight, item.isBig);
                //要加上前面那个奖励的权重
                if (list.size() > 0) {
                    temp.keyWeight += list.get(list.size() - 1).keyWeight;
                    temp.goldWeight += list.get(list.size() - 1).goldWeight;
                }
                list.add(temp);
            }
            //ok，奖励列表生成好了，开始按权重拼人品了
            int ran = 0;
            //用钥匙抽就使用钥匙的权重。用元宝抽就用元宝的权重
            if (useKeyTimes > 0) {
                ran = new Random().nextInt(list.get(list.size() - 1).keyWeight);
            } else {
                ran = new Random().nextInt(list.get(list.size() - 1).goldWeight);
            }
            //随机到的奖励：reward
            RewardItemTemp reward = null;
            for (RewardItemTemp temp : list) {
                if (useKeyTimes > 0) {
                    if (ran < temp.keyWeight) {
                        reward = temp;
                        break;
                    }
                } else {
                    if (ran < temp.goldWeight) {
                        reward = temp;
                        break;
                    }
                }
            }

//            RewardData lucky = Utils.findOne(festivalWish.getLuckyAwardList(), i -> i.getC() == 9 || i.getC() == player.getCareer());
//            RewardData lowest = triggerLowestLucky == null ? null : Utils.findOne(triggerLowestLucky.getRewardData(), i -> i.getC() == 9 || i.getC() == player.getCareer());
            RewardData lastReward = reward.rewardData;
            if (isTriggerLucky) {
                lastReward = Utils.findOne(festivalWish.getLuckyAwardList(), i -> i.getC() == 9 || i.getC() == player.getCareer());
//                LOGGER.info("节日许愿活动触发幸运奖励");
            } else if (isTriggerLowestLucky) {
                lastReward = Utils.findOne(triggerLowestLucky.getRewardData(), i -> i.getC() == 9 || i.getC() == player.getCareer());
//                LOGGER.info("节日许愿活动触发保底奖励");
            }

            if (lastReward == null) {
                LOGGER.error("节日许愿活动isLucky={} luckReward={} drawReward={} 抽奖为空", isTriggerLucky, festivalWish.getLuckyAwardList(), reward.rewardData);
                continue;
            }

            //奖励进临时仓库
            tempBag.add(lastReward);
            //进最近一次抽奖记录表，用于返给前端
            lastList.add(lastReward);

            List<Item> items = Item.createItems(lastReward.getI(), lastReward.getN(), lastReward.getB() == 1);
            if (isTriggerLucky) {
                cleanLucky(player, festivalWish, items);
            }

            //如果这次是花费元宝抽奖的，那么元宝抽奖次数++
            if (useGoldTimes > 0) {
                goldTimes++;
            }

            //没爆大奖，祝福值增加。爆了大奖，祝福值清0
            boolean isBig = false;
            if (reward.isBig || isTriggerLucky || isTriggerLowestLucky) {
                isBig = true;
                wishValue = 0;
            } else {
                wishValue += festivalWish.addWish;
            }

            //积分增加
            score += festivalWish.addScore;

            //抽完了一次，使用钥匙和元宝的次数--。到下次循环的时候，判断哪个>0。就是用哪种方式抽的
            useGoldTimes--;
            useKeyTimes--;

            //测试日志
//            if (ServerConfig.isTestServer()) {
//                long variant = Manager.countManager.getVariant(player, VariantType.ACTIVITY_LUCKY_VALUE);
//                Manager.chatManager.sendSystemStrToPlayer(player, "当前幸运值=" + variant);
//            }
            if (isBig) {
                LOGGER.info("节日许愿，玩家{} 中大奖{} 类型{}", player.getId(), reward.rewardData.getI(), isTriggerLucky ? "幸运大奖" : isTriggerLowestLucky ? "保底大奖" : "普通大奖");
                Cfg_Item_Bean itemBean = CfgManager.getCfg_Item_Container().getValueByKey(reward.rewardData.getI());
                String itemName = ServerStr.getChatTableName(itemBean.getName());
                int itemNum = reward.rewardData.getN();
                MessageUtils.notify_AllServer(Notify.EXCLUSIVE_NOTIFY, ChatChannel.CHATCHANNEL_SYSTEM, MessageString.luck_draw_radio_notice4,
                        ServerConfig.getServerId(),
                        player.getId(),
                        player.getName(),
                        actCfg.getName(),
                        itemName,
                        itemNum);
            }
        }
        //存储数据
        roleActDataMap.put(Score, score);
        roleActDataMap.put(WishValue, wishValue);
        roleActDataMap.put(GoldTimes, goldTimes);
        roleActDataMap.put(TempBag, tempBag);
        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));

        HashMap<String, Object> result = new HashMap<>();
        result.put("boxBin", roleActDataMap.getOrDefault(BoxState, 0));
        result.put("score", score);
        result.put("last", lastList);
        result.put("wish", wishValue);

        List<Integer> lowestRecord = (List<Integer>) roleActDataMap.getOrDefault(P_Lowest_State, new ArrayList<>());
        HashMap<Integer, Integer> lowestMap = new HashMap<>();
        for (Integer ii : festivalWish.getLowestData().keySet()) {
            lowestMap.put(ii, lowestRecord.contains(ii) ? 1 : 0);
        }
        result.put("lowestMap", lowestMap);
        result.put("lowestCount", roleActDataMap.getOrDefault(P_Lowest, 0));   //当前保底时的抽奖次数
        ActivityMessage.ResActivityDeal.Builder pb = ActivityMessage.ResActivityDeal.newBuilder();
        pb.setData(JsonUtils.toJSONString(result));
        pb.setType(actCfg.getType());
        MessageUtils.send_to_player(player, ActivityMessage.ResActivityDeal.MsgID.eMsgID_VALUE, pb.build().toByteArray());

        //BI
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.FestvialWish, ItemChangeReason.FestvialWishGet, once ? 1 : 10);
    }

    /**
     * 领取保底奖励。第一个宝箱传0
     */
    private void getBox(int index, Player player, FestivalWish festivalWish, ActivityConfig actCfg) {
        if (index < 0 || index > festivalWish.getLowestRewards().size() - 1) {
            LOGGER.error("领取保底奖励时档次不合法" + index);
            return;
        }
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(), actCfg.getType());
        Integer score = (Integer) roleActDataMap.getOrDefault(Score, 0);
        Integer state = (Integer) roleActDataMap.getOrDefault(BoxState, 0);

        //后来奖励的配置变更了，一个保底分数会配多个职业的道具。所以这里进行支持
        //使用treeMap,将所有奖励整合为：保底分30-奖励列表、保底分100分-奖励列表、……
        TreeMap<Integer, ArrayList<LowestReward>> lowestRewardsTreeMap = new TreeMap<>();
        for (LowestReward lr : festivalWish.lowestRewards) {
            ArrayList<LowestReward> list = lowestRewardsTreeMap.get(lr.getScore());
            if (list == null) {
                list = new ArrayList<>();
                lowestRewardsTreeMap.put(lr.getScore(), list);
            }
            list.add(lr);
        }
        LowestReward lowestReward = null;
        int i = 0;
        for (ArrayList<LowestReward> list : lowestRewardsTreeMap.values()) {
            if (i != index) {
                i++;
                continue;
            }
            //该保底积分只配了一个奖励
            if (list.size() == 1) {
                lowestReward = list.get(0);
            }
            //该保底积分配置了多个奖励，则得到的是同职业的那个
            else {
                for (LowestReward reward : list) {
                    if (reward.getRewardData().getC() == player.getCareer() || reward.getRewardData().getC() == 9) {
                        lowestReward = reward;
                        break;
                    }
                }
            }
            i++;
        }

        if (score < lowestReward.score) {
            LOGGER.error("分数没达到");
            return;
        }
        if ((state & 1 << index) > 0) {
            LOGGER.error("已经领取过了");
            return;
        }
        state |= 1 << index;

        //保存已领取状态
        roleActDataMap.put(BoxState, state);
        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));
        //得奖励
        RewardData rewardData = lowestReward.getRewardData();
        List<Item> itemList = Item.createItems(rewardData.getI(), rewardData.getN(), rewardData.getB() == 1);
        if (!Manager.backpackManager.manager().addItems(player, itemList, ItemChangeReason.FestvialWishGet, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, itemList, ItemChangeReason.FestvialWishGet);
        }
        //返给前端
        HashMap<String, Object> result = new HashMap<>();
        result.put("boxBin", state);
        result.put("score", score);
        result.put("last", new ArrayList<>());
        result.put("wish", roleActDataMap.getOrDefault(WishValue, 0));

        List<Integer> lowestRecord = (List<Integer>) roleActDataMap.getOrDefault(P_Lowest_State, new ArrayList<>());
        HashMap<Integer, Integer> lowestMap = new HashMap<>();
        for (Integer ii : festivalWish.getLowestData().keySet()) {
            lowestMap.put(ii, lowestRecord.contains(ii) ? 1 : 0);
        }
        result.put("lowestMap", lowestMap);
        result.put("lowestCount", roleActDataMap.getOrDefault(P_Lowest, 0));   //当前保底时的抽奖次数

        ActivityMessage.ResActivityDeal.Builder pb = ActivityMessage.ResActivityDeal.newBuilder();
        pb.setData(JsonUtils.toJSONString(result));
        pb.setType(actCfg.getType());
        MessageUtils.send_to_player(player, ActivityMessage.ResActivityDeal.MsgID.eMsgID_VALUE, pb.build().toByteArray());

        //BI
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.FestvialWish, ItemChangeReason.FestvialWishScoreAwardGet, index);
    }

    /**
     * 临时仓库进背包
     */
    private void tempStoreToBag(Player player, ActivityConfig actCfg) {
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(), actCfg.getType());
        List<Object> tempBag = (List<Object>) roleActDataMap.getOrDefault(TempBag, new ArrayList<Object>());
        //临时仓库中有道具
        if (!tempBag.isEmpty()) {
            for (Object obj : tempBag) {
                RewardData rewardData = null;
                //玩家数据经历过数据库存储，再取出来的时候就是 LinkedHashMap<String, Integer>类型
                if (obj == null) {
                    continue;
                }
                if (obj instanceof LinkedHashMap) {
                    rewardData = new RewardData();
                    LinkedHashMap<String, Integer> map = (LinkedHashMap<String, Integer>) obj;
                    rewardData.setI(map.get("i"));
                    rewardData.setN(map.get("n"));
                    rewardData.setB(map.get("b"));
                    rewardData.setC(map.get("c"));
                }
                //玩家数据一直在内存的话，就是rewardData类型
                else {
                    rewardData = (RewardData) obj;
                }
                List<Item> itemList = Item.createItems(rewardData.getI(), rewardData.getN(), rewardData.getB() == 1);
                if (!Manager.backpackManager.manager().addItems(player, itemList, ItemChangeReason.FestvialWishExtract, IDConfigUtil.getLogId())) {
                    Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System, MessageString.System, String.valueOf(MessageString.FestivalWishStoreOnekeyExtract), itemList, ItemChangeReason.FestvialWishExtract);
                }
            }
            //清空仓库。存储
            roleActDataMap.put(TempBag, new ArrayList<RewardData>());
            Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));
        }
        //返回最新数据
        Manager.activityManager.deal().onReqActivity(player, actCfg.getType());
    }

    @Override
    public boolean parseCustomConfig(ActivityConfig actCfg, String customStr) {
        FestivalWish data = JsonUtils.parseObject(customStr, FestivalWish.class);
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
        FestivalWish festivalWish = (FestivalWish) actCfg.getCustomCfgMap().get(configData);
        if (festivalWish == null) {
            LOGGER.error("许愿池活动活动自定义配置参数不存在");
            return null;
        }
        int startDay = TimeUtils.getCurDayByTime(actCfg.getBeginTime());
        int curDay = TimeUtils.getCurDayByTime(TimeUtils.Time());
        int overDay = TimeUtils.getCurDayByTime(actCfg.getEndTime());
        if (curDay < startDay || curDay > overDay) {
            LOGGER.error("活动时间已结束" + actCfg.getType());
            return null;
        }
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(roleId, actCfg.getType());
        Integer score = (Integer) roleActDataMap.getOrDefault(Score, 0);
        Integer boxState = (Integer) roleActDataMap.getOrDefault(BoxState, 0);
        Integer wish = (Integer) roleActDataMap.getOrDefault(WishValue, 0);
        List<RewardData> tempBag = (List<RewardData>) roleActDataMap.getOrDefault(TempBag, new ArrayList<RewardData>());

        HashMap<String, Object> result = new HashMap<>();
        result.put("boxBin", boxState);
        result.put("score", score);
        result.put("store", tempBag);
        result.put("wish", wish);

        List<Integer> lowestRecord = (List<Integer>) roleActDataMap.getOrDefault(P_Lowest_State, new ArrayList<>());
        HashMap<Integer, Integer> lowestMap = new HashMap<>();
        for (Integer ii : festivalWish.getLowestData().keySet()) {
            lowestMap.put(ii, lowestRecord.contains(ii) ? 1 : 0);
        }
        result.put("lowestMap", lowestMap);
        result.put("lowestCount", roleActDataMap.getOrDefault(P_Lowest, 0));   //当前保底时的抽奖次数
        return JsonUtils.toJSONString(result);
    }

    @Override
    public void activityEndDeal(ActivityConfig actCfg) {

        for (Map.Entry<Long, ConcurrentHashMap<Integer, ConcurrentHashMap<String, Object>>> role : Manager.activityManager.getRoleActDatas().entrySet()) {
            ConcurrentHashMap<String, Object> activityData = Manager.activityManager.deal().getRoleActivityData(role.getKey(), actCfg.getType());

            List<Object> tempBag = (List<Object>) activityData.getOrDefault(TempBag, new ArrayList<>());
            List<Item> items = new ArrayList<>();
            for (Object obj : tempBag) {
                RewardData rewardData;
                if (obj == null) {
                    continue;
                }
                //玩家数据经历过数据库存储，再取出来的时候就是 LinkedHashMap<String, Integer>类型
                if (obj instanceof LinkedHashMap) {
                    rewardData = new RewardData();
                    LinkedHashMap<String, Integer> map = (LinkedHashMap<String, Integer>) obj;
                    rewardData.setI(map.get("i"));
                    rewardData.setN(map.get("n"));
                    rewardData.setB(map.get("b"));
                    rewardData.setC(map.get("c"));
                }
                //玩家数据一直在内存的话，就是rewardData类型
                else {
                    rewardData = (RewardData) obj;
                }
                List<Item> temp = Item.createItems(rewardData.getI(), rewardData.getN(), rewardData.getB() == 1);
                items.addAll(temp);
            }
            if (!items.isEmpty()) {
                Manager.mailManager.sendMailToPlayer(role.getKey(), 1, MessageString.System, MessageString.System, String.valueOf(MessageString.FestivalWishStoreOnekeyExtract), items, ItemChangeReason.FestvialWishGet);
                //清空仓库。存储
                activityData.put(TempBag, new ArrayList<RewardData>());
            }
        }
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
        if (Manager.activityManager == null) {
            return;
        }
        List<ActivityConfig> actCfgList = Manager.activityManager.deal().getActCfgListByActLogicID(ActivityType.FestivalWish);
        for (ActivityConfig activityConfig : actCfgList) {
            if (activityConfig == null) {
                return;
            }
            Object old = activityConfig.getCustomCfgMap().get(configData);
            if (old == null) {
                return;
            }
            String customStr = JsonUtils.toJSONString(old);
            FestivalWish newData = JsonUtils.toJavaObject(customStr, FestivalWish.class);
            activityConfig.getCustomCfgMap().put(configData, newData);
        }
    }

    @Override
    public int getId() {
        return ScriptEnum.FestivalWishActivityScript;
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

    public static class FestivalWish extends ActivityLucky {
        private String client;
        private int keyId;      //钥匙id
        private int oneCostKey;//一次消耗钥匙
        private int tenCostKey;//十连抽消耗钥匙
        private int oneCostGold;//一次消耗元宝
        private int tenCostGold;//十连抽消耗元宝
        private int addWish;    //每次增加的祝福值
        private int addScore;   //每次增加的积分
        private int wishMax;    //祝福值上限（祝福值达到上限必定爆大奖,即使元宝抽奖次数没有达到wasteGoldCount）
        private int wasteGoldCount;   //抽奖X次才会出大奖（只计算元宝抽奖次数）
        private List<LowestReward> lowestRewards;//保底奖励
        private List<RewardPoolItem> rewardPool;//奖池（普通奖励和超级大奖）
        private HashMap<Integer, FestivalWishActivityScript.LowestData> lowestData;  //保底奖励 <序号，保底信息>

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public int getKeyId() {
            return keyId;
        }

        public void setKeyId(int keyId) {
            this.keyId = keyId;
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

        public int getAddWish() {
            return addWish;
        }

        public void setAddWish(int addWish) {
            this.addWish = addWish;
        }

        public int getAddScore() {
            return addScore;
        }

        public void setAddScore(int addScore) {
            this.addScore = addScore;
        }

        public int getWishMax() {
            return wishMax;
        }

        public void setWishMax(int wishMax) {
            this.wishMax = wishMax;
        }

        public int getWasteGoldCount() {
            return wasteGoldCount;
        }

        public void setWasteGoldCount(int wasteGoldCount) {
            this.wasteGoldCount = wasteGoldCount;
        }

        public List<LowestReward> getLowestRewards() {
            return lowestRewards;
        }

        public void setLowestRewards(List<LowestReward> lowestRewards) {
            this.lowestRewards = lowestRewards;
        }

        public List<RewardPoolItem> getRewardPool() {
            return rewardPool;
        }

        public void setRewardPool(List<RewardPoolItem> rewardPool) {
            this.rewardPool = rewardPool;
        }

        public int getOneCostKey() {
            return oneCostKey;
        }

        public void setOneCostKey(int oneCostKey) {
            this.oneCostKey = oneCostKey;
        }

        public int getTenCostKey() {
            return tenCostKey;
        }

        public void setTenCostKey(int tenCostKey) {
            this.tenCostKey = tenCostKey;
        }

        public HashMap<Integer, FestivalWishActivityScript.LowestData> getLowestData() {
            return lowestData;
        }

        public void setLowestData(HashMap<Integer, FestivalWishActivityScript.LowestData> lowestData) {
            this.lowestData = lowestData;
        }
    }

    //保底奖励
    public static class LowestReward {
        private RewardData rewardData;
        private int score;

        public RewardData getRewardData() {
            return rewardData;
        }

        public void setRewardData(RewardData rewardData) {
            this.rewardData = rewardData;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }
    }

    //奖池奖励条目
    public static class RewardPoolItem {
        private RewardData rewardData;
        private int keyWeight;  //钥匙开启的权重
        private int goldWeight; //元宝开启的权重
        private boolean isBig;  //是否是大奖

        public RewardData getRewardData() {
            return rewardData;
        }

        public void setRewardData(RewardData rewardData) {
            this.rewardData = rewardData;
        }

        public int getKeyWeight() {
            return keyWeight;
        }

        public void setKeyWeight(int keyWeight) {
            this.keyWeight = keyWeight;
        }

        public int getGoldWeight() {
            return goldWeight;
        }

        public void setGoldWeight(int goldWeight) {
            this.goldWeight = goldWeight;
        }

        public boolean getIsBig() {
            return isBig;
        }

        public void setIsBig(boolean big) {
            isBig = big;
        }
    }

    //玩家许愿时产生的临时变量。主要用于算权重
    static class RewardItemTemp {
        public RewardData rewardData;
        public int keyWeight;  //钥匙开启的累加权重
        public int goldWeight; //元宝开启的累加权重
        public boolean isBig;  //是否是大奖

        public RewardItemTemp(RewardData rewardData, int keyWeight, int goldWeight, boolean isBig) {
            this.rewardData = rewardData;
            this.keyWeight = keyWeight;
            this.goldWeight = goldWeight;
            this.isBig = isBig;
        }
    }

    //保底信息
    static class LowestData {
        private int index;//保底序号
        private int min;  //最小保底次数
        private int max;  //最大保底次数
        private List<LowestPro> proList;//保底分段概率
        private List<RewardData> rewardData;//保底奖励

        //region
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
        //endregion
    }

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

    /**
     * 保底奖励抽奖
     *
     * @param activityData
     * @param serverActivityData
     * @param config
     * @return
     */
    public FestivalWishActivityScript.LowestData isTriggerLowestLucky(ConcurrentHashMap<String, Object> activityData, ConcurrentHashMap<String, Object> serverActivityData, FestivalWishActivityScript.FestivalWish config) {

        int last = (int) activityData.getOrDefault(P_Lowest, 0);
        int maxLowest = calcLowestMax(config);

        List<Integer> triggerState = (List<Integer>) activityData.getOrDefault(P_Lowest_State, new ArrayList<>());
        if (triggerState.size() >= config.getLowestData().size() && last >= maxLowest) {
            last = 0;
            activityData.put(P_Lowest, 0);
            triggerState.clear();
        }

        int curLowest = last + 1;
        activityData.put(P_Lowest, curLowest);

        FestivalWishActivityScript.LowestData lowest = Utils.findOne(config.getLowestData().values(), o -> curLowest >= o.getMin() && curLowest <= o.getMax());
        if (lowest == null) {
            return null;
        }
        if (triggerState.contains(lowest.getIndex())) {
            return null;
        }

        //分段概率
        LowestPro lowestPro = getLowestPro(curLowest, lowest.getProList());
        if (lowestPro == null) {
            return null;
        }
        int pro = lowestPro.getPro();

        if (curLowest == getLowestMax(lowest) || RandomUtils.defaultIsGenerate(pro)) {
            triggerState.add(lowest.getIndex());
            activityData.put(P_Lowest_State, triggerState);
            return lowest;
        }

//        float base = 1f / (lowest.getMax() - lowest.getMin()) * (Math.max(curLowest - lowest.getMin(), 0));
//        if (curLowest == lowest.getMax() || RandomUtils.defaultIsGenerate((int) (Math.pow(base, 13) * 10000))) {
//            triggerState.add(lowest.getIndex());
//            activityData.put(P_Lowest_State, triggerState);
//            return lowest;
//        }
        return null;
    }

    private int getLowestMax(LowestData lowest) {
        int max = 0;
        for (LowestPro pro : lowest.getProList()) {
            if (pro.getMax() > max) {
                max = pro.getMax();
            }
        }
        return max;
    }

    /**
     * 获取分段概率
     *
     * @param count
     * @param proList
     * @return
     */
    private LowestPro getLowestPro(Integer count, List<LowestPro> proList) {
        for (LowestPro pro : proList) {
            if (pro.getMin() <= count && count <= pro.getMax()) {
                return pro;
            }
        }
        return null;
    }

    /**
     * 计算最大保底次数
     *
     * @param config
     * @return
     */
    private int calcLowestMax(FestivalWish config) {
        int max = 0;
        for (int key : config.getLowestData().keySet()) {
            FestivalWishActivityScript.LowestData bean = config.getLowestData().get(key);
            max = Math.max(max, bean.getMax());
        }
        return max;
    }

}
