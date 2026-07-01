package common.activity;

import com.data.ItemChangeReason;
import com.data.MessageString;

import com.game.activity.script.IActivityScript;
import com.game.activity.struct.ActivityConfig;
import com.game.activity.struct.ActivityType;
import com.game.activity.struct.RewardData;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author cc
 * 泰国版限时礼包活动（小额消费礼包活动）300017
 */
public class HolidayDailyGiftActivityScript implements IActivityScript {

    public static final Logger LOGGER = LogManager.getLogger(HolidayDailyGiftActivityScript.class);

    private static final String giftData = "AllGiftData";
    private static final String buyCount = "buyCount";

    @Override
    public void onReqActivityDeal(Player player, String dataStr, ActivityConfig actCfg) {
        //活动购买
        if (actCfg == null) {
            LOGGER.error("限时礼包活动活动配置不存在");
            return;
        }
        AllGiftData awardDataList = (AllGiftData) actCfg.getCustomCfgMap().get(giftData);
        if (awardDataList == null) {
            LOGGER.error("限时礼包活动活动自定义配置参数不存在");
            return;
        }
        int startDay = TimeUtils.getCurDayByTime(actCfg.getBeginTime());
        int curDay = Math.max(startDay, TimeUtils.getCurDayByTime(TimeUtils.Time() - 5 * 60 * 60 * 1000));
        int overDay = TimeUtils.getCurDayByTime(actCfg.getEndTime());
        if (curDay < startDay || curDay > overDay) {
            LOGGER.error("活动时间已结束" + actCfg.getType());
            return;
        }
        //今天卖第几个东西（今天是活动的第几天）。活动开始第一天卖第一个，第二天卖第二个……
        int index = curDay - startDay + 1;
        if (index > awardDataList.getServer().size()) {
            LOGGER.error("活动配置的奖励条目少于活动持续的天数");
            return;
        }
        DailyGiftData data = awardDataList.getServer().get(index);
        if (data == null) {
            LOGGER.error("第" + index + "天的奖励数据找不到");
            return;
        }

        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(), actCfg.getType());
        roleActDataMap.putIfAbsent(buyCount, new HashMap<Integer, Integer>());
        HashMap<Integer, Integer> buyInfo = (HashMap<Integer, Integer>) roleActDataMap.get(buyCount);
        //今天的这个奖励已经买了多少次
        Integer alreadyBuy = buyInfo.getOrDefault(index, 0);
        if (alreadyBuy >= data.getLimitTimes()) {
            LOGGER.error("奖励购买次数已达上限");
            return;
        }
        if (data.getPrice() < 1) {
            LOGGER.error("配置的价格有误");
            return;
        }
        //开始购买。。。。。。。。。
        //扣金元宝
        if (!Manager.currencyManager.manager().onDecItemCoin(player, data.getPrice(), ItemChangeReason.LimitTimeGiftActivityBuyDec, IDConfigUtil.getLogId(), data.getCurrencyType())) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.CurrencyNotEnough, Manager.backpackManager.manager().getName(data.getCurrencyType()));
            return;
        }
        //增加道具
//        List<Item> itemList = new ArrayList<>();
//        for (RewardData rewardData : data.getReward().values()) {
//            itemList.addAll(Item.createItems(rewardData.getI(), rewardData.getN(), rewardData.getB() == 1));
//        }
        List<Item> itemList = Item.createItems(player.getCareer(), data.getReward());
        long actionId = IDConfigUtil.getLogId();
        if (!Manager.backpackManager.manager().addItems(player, itemList, ItemChangeReason.LimitTimeGiftActivityGet, actionId)) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, itemList, ItemChangeReason.LimitTimeGiftActivityGet, actionId);
        }
        //增加购买次数并存储
        ((HashMap<Integer, Integer>) roleActDataMap.get(buyCount)).put(index, alreadyBuy + 1);
        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));
        //返回最新数据
        Manager.activityManager.deal().onReqActivity(player, actCfg.getType());
        //BI记录
//        Manager.biManager.getScript().biActivity(player, ItemChangeReason.LimitTimeGiftActivityGet, actCfg.getType(), actCfg.getId());
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.LimitTimeGift, ItemChangeReason.LimitTimeGiftActivityGet, index);
    }

    @Override
    public boolean parseCustomConfig(ActivityConfig actCfg, String customStr) {
        AllGiftData data = JsonUtils.parseObject(customStr, AllGiftData.class);
        actCfg.getCustomCfgMap().put(giftData, data);
        actCfg.getCustomCfgMap().put("client", data.getClient());
        return true;
    }

    @Override
    public boolean updateCustomConfig(ActivityConfig actCfg, String customStr) {
        return parseCustomConfig(actCfg, customStr);
    }

    @Override
    public String getActivityDataStr(ActivityConfig actCfg, long roleId) {

        if (actCfg == null) {
            LOGGER.error("限时礼包活动活动配置不存在");
            return null;
        }
        AllGiftData awardDataList = (AllGiftData) actCfg.getCustomCfgMap().get(giftData);
        if (awardDataList == null) {
            LOGGER.error("限时礼包活动活动自定义配置参数不存在");
            return null;
        }
        int startDay = TimeUtils.getCurDayByTime(actCfg.getBeginTime());
        int curDay = Math.max(startDay, TimeUtils.getCurDayByTime(TimeUtils.Time() - 5 * 60 * 60 * 1000));
        int overDay = TimeUtils.getCurDayByTime(actCfg.getEndTime());
        if (curDay < startDay || curDay > overDay) {
            LOGGER.error("活动时间已结束" + actCfg.getType());
            return null;
        }
        //今天卖第几个东西（今天是活动的第几天）。活动开始第一天卖第一个，第二天卖第二个……
        int index = curDay - startDay + 1;
        if (index > awardDataList.getServer().size()) {
            LOGGER.error("活动配置的奖励条目少于活动持续的天数");
            return null;
        }
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(roleId, actCfg.getType());
        roleActDataMap.putIfAbsent(buyCount, new HashMap<Integer, Integer>());
        HashMap<Integer, Integer> buyInfo = (HashMap<Integer, Integer>) roleActDataMap.get(buyCount);

        //今天的这个奖励已经买了多少次
        Integer alreadyBuy = buyInfo.getOrDefault(index, 0);

        HashMap<String, Object> result = new HashMap<>();
        result.put("day", index);
        result.put("alreadyBuy", alreadyBuy);
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

    }


    @Override
    public void fiveClockPlayerDeal(Player player, ActivityConfig actCfg) {


    }

    @Override
    public void zeroClockDeal(ActivityConfig actCfg) {

        //策划要求该活动是5点刷新。5点的时候要给客户端推送最新的活动动态数据
        if (actCfg == null) {
            return;
        }
        int actType = actCfg.getType();
        AllGiftData awardDataList = (AllGiftData) actCfg.getCustomCfgMap().get(giftData);
        if (awardDataList == null) {
            LOGGER.error("跨天的时候找不到限时礼包活动活动自定义配置");
            return;
        }
        List<Long> roleIds = Manager.activityManager.deal().getRoleIdList(actType);
        for (long roleid : roleIds) {
            Player player = Manager.playerManager.getPlayerOnline(roleid);
            if (player == null) {
                continue;
            }
            Manager.activityManager.deal().onReqActivity(player, actType);
        }
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
        List<ActivityConfig> actCfgList = Manager.activityManager.deal().getActCfgListByActLogicID(ActivityType.HolidayDailyGift);
        for (ActivityConfig activityConfig : actCfgList) {
            if (activityConfig == null) {
                return;
            }
            Object old = activityConfig.getCustomCfgMap().get(giftData);
            if (old == null) {
                return;
            }
            String customStr = JsonUtils.toJSONString(old);
            AllGiftData newData = JsonUtils.toJavaObject(customStr, AllGiftData.class);
            activityConfig.getCustomCfgMap().put(giftData, newData);
        }
    }

    @Override
    public int getId() {
        return ScriptEnum.HolidayDailyGiftScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    public static class AllGiftData {
        private String client;
        private TreeMap<Integer, DailyGiftData> server; //第几天-奖励。按照天数排序

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public TreeMap<Integer, DailyGiftData> getServer() {
            return server;
        }

        public void setServer(TreeMap<Integer, DailyGiftData> server) {
            this.server = server;
        }
    }

    public static class DailyGiftData {
        private int day;     //第几天奖励
        private List< RewardData> reward;
        private int currencyType;   //货币类型
        private int price; //价格
        private int limitTimes;  //限购多少次

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }


        public List<RewardData> getReward() {
            return reward;
        }

        public void setReward(List<RewardData> reward) {
            this.reward = reward;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getLimitTimes() {
            return limitTimes;
        }

        public void setLimitTimes(int limitTimes) {
            this.limitTimes = limitTimes;
        }

        public int getCurrencyType() {
            return currencyType;
        }

        public void setCurrencyType(int currencyType) {
            this.currencyType = currencyType;
        }
    }
}
