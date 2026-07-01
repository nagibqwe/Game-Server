package common.activity;

import com.data.ItemChangeReason;
import com.data.MessageString;

import com.game.activity.script.IActivityScript;
import com.game.activity.struct.ActivityConfig;
import com.game.activity.struct.ActivityType;
import com.game.activity.struct.RewardData;
import com.game.backpack.structs.Item;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.count.structs.ICount;
import com.game.db.bean.ActivityConfigBean;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.server.DbSqlName;
import com.game.utils.RandomUtils;
import game.core.json.TypeReference;
import game.core.util.IDConfigUtil;
import game.core.util.JsonUtils;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Desc TODO 300013
 * @Date 2020/10/16 10:42
 * @Auth ZUncle
 */
public class HolidayWordsScript implements IActivityScript {

    final transient Logger logger = LogManager.getLogger(HolidayWordsScript.class);

    //region 数据KEY
    final transient String Client = "client";
    final transient String ActivityData = "ActivityData";
    final transient String WorldsLimit = "WorldsLimit";
    //endregion

    //region 数据结构

    static class WordsGift {
        int id;                    //兑换ID
        List<RewardData> words;    //集字列表
        List<RewardData> box;      //箱子奖励
        int limit;                 //单日兑换上限

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<RewardData> getWords() {
            return words;
        }

        public void setWords(List<RewardData> words) {
            this.words = words;
        }

        public List<RewardData> getBox() {
            return box;
        }

        public void setBox(List<RewardData> box) {
            this.box = box;
        }

        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }
    }

    /**
     * 活动副本掉落
     */
    static class CloneGift {
        int weight;         //权重
        List<RewardData> gift;    //物品

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public List<RewardData> getGift() {
            return gift;
        }

        public void setGift(List<RewardData> gift) {
            this.gift = gift;
        }
    }

    /**
     * 数据配置
     */
    static class WordsActivity {

        String client;                                                   //前端数据
        HashMap<Integer, WordsGift> gift = new HashMap<>();              //兑换配置
        HashMap<Integer, List<CloneGift>> clonePool = new HashMap<>();   //<cloneId, Pool>  副本奖励
        HashMap<Integer, List<CloneGift>> bossPool = new HashMap<>();    //<poolId, Pool>   boss掉落
        HashMap<Integer, Integer> boss = new HashMap<>();                //<bossId, poolId> boss列表

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public HashMap<Integer, List<CloneGift>> getClonePool() {
            return clonePool;
        }

        public void setClonePool(HashMap<Integer, List<CloneGift>> clonePool) {
            this.clonePool = clonePool;
        }

        public HashMap<Integer, List<CloneGift>> getBossPool() {
            return bossPool;
        }

        public void setBossPool(HashMap<Integer, List<CloneGift>> bossPool) {
            this.bossPool = bossPool;
        }

        public HashMap<Integer, Integer> getBoss() {
            return boss;
        }

        public void setBoss(HashMap<Integer, Integer> boss) {
            this.boss = boss;
        }

        public HashMap<Integer, WordsGift> getGift() {
            return gift;
        }

        public void setGift(HashMap<Integer, WordsGift> gift) {
            this.gift = gift;
        }
    }

    /**
     * 限购
     */
    static class LimitBuy implements ICount {
        ConcurrentHashMap<String, Count> counts = new ConcurrentHashMap<>();

        /**
         * 获取计数
         *
         * @return
         */
        @Override
        public ConcurrentHashMap<String, Count> getCounts() {
            return counts;
        }
    }

    //endregion


    /**
     * 请求操作运营活动
     *
     * @param player
     * @param dataStr
     */
    @Override
    public void onReqActivityDeal(Player player, String dataStr, ActivityConfig actCfg) {

        HashMap<String, Integer> msg = JsonUtils.parseObject(dataStr, new TypeReference<HashMap<String, Integer>>() {
        });

        int id = msg.get("id"); //兑换ID
        WordsActivity wordsActivity = (WordsActivity) actCfg.getCustomCfgMap().get(ActivityData);
        WordsGift wordsGift = wordsActivity.getGift().get(id);
        if (wordsGift == null) {
            return;
        }
        ConcurrentHashMap<String, Object> activityData = Manager.activityManager.deal().getRoleActivityData(player.getId(),actCfg.getType());

        String data = (String) activityData.getOrDefault(WorldsLimit, "{}");
        LimitBuy limitBuy = JsonUtils.parseObject(data, new TypeReference<LimitBuy>() {
        });

        //检测兑换限制(兑换限制为0次时则无限制)
        long count = Manager.countManager.getCount(limitBuy, BaseCountType.Activity, wordsGift.getId());
        if (count >= wordsGift.getLimit() && wordsGift.getLimit() != 0) {
            return;
        }
        //购买
        HashMap<Integer, Integer> costItem = new HashMap<>();
        for (RewardData cost : wordsGift.getWords()) {
            costItem.put(cost.getI(), costItem.getOrDefault(cost.getI(), 0) + cost.getN());
        }
        for (Map.Entry<Integer, Integer> cost : costItem.entrySet()) {
            if (!Manager.backpackManager.manager().canDeleteItemNum(player, cost.getKey(), cost.getValue())) {
                return;
            }
        }
        long logId = IDConfigUtil.getLogId();
        for (Map.Entry<Integer, Integer> cost : costItem.entrySet()) {
            Manager.backpackManager.manager().onRemoveItem(player, cost.getKey(), cost.getValue(), ItemChangeReason.HolidayActivityWordsCost, logId);
        }
        //TODO 发放道具
        List<Item> items = Item.createItems(player.getCareer(), wordsGift.getBox());
        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.HolidayActivityWordsGet, logId)) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.HolidayActivityWordsGet);
        }
        //增加一次购买记录
        Manager.countManager.addCount(limitBuy, BaseCountType.Activity, id, Count.RefreshType.CountType_Forever, 1);
        activityData.put(WorldsLimit, JsonUtils.toJSONString(limitBuy));

        HashMap<String, Object> message = new HashMap<>();
        message.put("id", id);
        message.put("count", count + 1);
        Manager.activityManager.deal().sendActivityDealMessage(player, actCfg.getType(), JsonUtils.toJSONString(message));
        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));

        logger.info("兑换集字 Id={} player={}", id, player);
        //记录BI
//        Manager.biManager.getScript().biActivity(player, ItemChangeReason.HolidayActivityWordsGet,actCfg.getType(), actCfg.getId());
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.HolidayWords, ItemChangeReason.HolidayActivityWordsGet);
    }

    /**
     * 解析活动自己的自定义配置
     *
     * @param actCfg
     * @param customStr
     */
    @Override
    public boolean parseCustomConfig(ActivityConfig actCfg, String customStr) {
        WordsActivity draw = JsonUtils.parseObject(customStr, WordsActivity.class);
        actCfg.getCustomCfgMap().put(ActivityData, draw);
        actCfg.getCustomCfgMap().put(Client, draw.getClient());
        return true;
    }

    /**
     * 活动配置更新处理
     *
     * @param actCfg
     * @param customStr
     */
    @Override
    public boolean updateCustomConfig(ActivityConfig actCfg, String customStr) {
        return parseCustomConfig(actCfg, customStr);
    }

    /**
     * 生成活动数据字符串
     *
     * @param roleId
     */
    @Override
    public String getActivityDataStr(ActivityConfig actCfg, long roleId) {



        WordsActivity activity = (WordsActivity) actCfg.getCustomCfgMap().get(ActivityData);

        ConcurrentHashMap<String, Object> activityData = Manager.activityManager.deal().getRoleActivityData(roleId, actCfg.getType());
        String data = (String) activityData.getOrDefault(WorldsLimit, "{}");
        LimitBuy limitBuy = JsonUtils.parseObject(data, new TypeReference<LimitBuy>() {
        });

        List<HashMap<String, Object>> message = new ArrayList<>(); //礼物兑换记录
        for (Integer id : activity.getGift().keySet()) {
            HashMap<String, Object> cell = new HashMap<>();
            cell.put("id", id);
            cell.put("count", Manager.countManager.getCount(limitBuy, BaseCountType.Activity, id));
            message.add(cell);
        }
        return JsonUtils.toJSONString(message);
    }

    /**
     * 活动结束时每个活动的特殊处理
     */
    @Override
    public void activityEndDeal(ActivityConfig actCfg) {

    }

    /**
     * 玩家上线处理
     *
     * @param player
     */
    @Override
    public void playerOnline(Player player, ActivityConfig actCfg) {

    }

    /**
     * 0点处理某个玩家活动数据
     *
     * @param player
     */
    @Override
    public void zeroClockPlayerDeal(Player player, ActivityConfig actCfg) {

    }

    /**
     * 5点处理某个玩家活动数据
     *
     * @param player
     */
    @Override
    public void fiveClockPlayerDeal(Player player, ActivityConfig actCfg) {

    }

    /**
     * 0点处理运营活动数据
     */
    @Override
    public void zeroClockDeal(ActivityConfig actCfg) {

    }

    /**
     * 5点处理运营活动数据
     */
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

        WordsActivity activity = (WordsActivity) actCfg.getCustomCfgMap().get(ActivityData);

        if (!activity.getBoss().containsKey(bossId)) {
            return false;
        }
        int poolId = activity.getBoss().get(bossId);
        List<CloneGift> cloneGifts = activity.getBossPool().get(poolId);
        CloneGift gift = random(cloneGifts);

        List<Item> items = Item.createItems(player.getCareer(), gift.getGift());

        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.HolidayBossDropGet, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.HolidayBossDropGet);
        }
        logger.info("集字活动掉落 bossId={} player={}", bossId, player);
        //记录BI
//        Manager.biManager.getScript().biActivity(player, ItemChangeReason.HolidayBossDropGet, actCfg.getType(), actCfg.getId());
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.HolidayWords, ItemChangeReason.HolidayBossDropGet);
        return true;
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

        WordsActivity wordsActivity = (WordsActivity) actCfg.getCustomCfgMap().get(ActivityData);
        List<CloneGift> pool = wordsActivity.getClonePool().get(cloneId);
        if (pool == null) {
            return false;
        }
        CloneGift random = random(pool);
        //TODO 发放活动副本奖励
        List<Item> items = Item.createItems(player.getCareer(), random.getGift());

        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.HolidayActivityWordsCloneDrop, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.HolidayActivityWordsCloneDrop);
        }
        //记录BI
//        Manager.biManager.getScript().biActivity(player, ItemChangeReason.HolidayActivityWordsCloneDrop, actCfg.getType(), actCfg.getId());
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.HolidayWords, ItemChangeReason.HolidayActivityWordsCloneDrop);
        return true;
    }

    /**
     * 充值后的处理
     *
     * @param player
     * @param getGoodsCfgId
     * @param rechargeNum
     */
    @Override
    public void rechargeDeal(Player player, int getGoodsCfgId, int rechargeNum, ActivityConfig actCfg) {

    }

    /**
     * 活动消耗
     *
     * @param player
     * @param coinType
     * @param consumeNum
     */
    @Override
    public void consumeDeal(Player player, int coinType, int consumeNum, ActivityConfig actCfg) {

    }

    @Override
    public void reload() {
        if(Manager.activityManager == null){
            return;
        }
        List<ActivityConfig> actCfgList = Manager.activityManager.deal().getActCfgListByActLogicID(ActivityType.HolidayWords);
        for (ActivityConfig activityConfig:actCfgList) {
            if (activityConfig == null) {
                return;
            }
            Object old = activityConfig.getCustomCfgMap().get(ActivityData);
            if (old == null) {
                return;
            }
            String customStr = JsonUtils.toJSONString(old);
            WordsActivity newData = JsonUtils.parseObject(customStr, WordsActivity.class);
            activityConfig.getCustomCfgMap().put(ActivityData, newData);
        }
    }

    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.HolidayWordsScript;
    }

    /**
     * 调用脚本
     *
     * @param args 参数
     * @return
     */
    @Override
    public Object call(Object... args) {
        return null;
    }

    /**
     * 随机权重
     *
     * @param params
     * @return
     */
    private CloneGift random(List<CloneGift> params) {
        TreeMap<Float, CloneGift> weightMap = new TreeMap<>();
        for (CloneGift param : params) {
            Float weight = weightMap.size() == 0 ? 0f : weightMap.lastKey();
            weight += param.getWeight();
            weightMap.put(weight, param);
        }
        float randomWeight = RandomUtils.randomFloatValue(0f, weightMap.lastKey());
        SortedMap<Float, CloneGift> sort = weightMap.tailMap(randomWeight, false);
        return weightMap.get(sort.firstKey());
    }

    public static void testBuild() {
        //副本奖励池子
        List<HashMap<String, Object>> pool = new ArrayList<>();
        for (int c = 0; c < 50; c++) {
            List<RewardData> list = new ArrayList<>();
            for (int i = 1; i < 3; i++) {
                RewardData item = new RewardData();
                item.setI(2001);
                item.setN(i);
                item.setB(1);
                item.setC(9);
                list.add(item);
            }
            HashMap<String, Object> clone = new HashMap<>();
            clone.put("weight", c + 1);
            clone.put("gift", list);
            pool.add(clone);
        }

        //副本奖励池子
        HashMap<Integer, Object> clonePool = new HashMap<>();
        clonePool.put(61001, pool);
        clonePool.put(61002, pool);

        //集字配置
        HashMap<Integer, Object> gift = new HashMap<>();
        for (int i = 1; i < 10; i++) {
            List<Object> words = new ArrayList<>();
            words.add(new RewardData().setI(2001).setN(i).setB(1).setC(9));
            words.add(new RewardData().setI(2002).setN(i).setB(1).setC(9));
            words.add(new RewardData().setI(2003).setN(i).setB(1).setC(9));
            words.add(new RewardData().setI(2004).setN(i).setB(1).setC(9));

            List<Object> box = new ArrayList<>();
            box.add(new RewardData().setI(2001).setN(i).setB(1).setC(0));
            box.add(new RewardData().setI(2002).setN(i).setB(1).setC(1));

            HashMap<String, Object> bean = new HashMap<>();
            bean.put("id", i);
            bean.put("limit", i);
            bean.put("box", box);
            bean.put("words", words);
            gift.put(i, bean);
        }
        //boss奖励配置
        HashMap<Integer, Object> bossPool = new HashMap<>();
        bossPool.put(1, pool);
        bossPool.put(2, pool);
        bossPool.put(3, pool);

        //boss配置
        HashMap<Integer, Integer> boss = new HashMap<>();
        for (int id = 11101; id <= 11102; id++) {
            boss.put(id, 1);
        }
        for (int id = 11103; id <= 11106; id++) {
            boss.put(id, 2);
        }
        for (int id = 11107; id <= 11108; id++) {
            boss.put(id, 3);
        }
        HashMap<String, Object> custom = new HashMap();
        custom.put("bossPool", bossPool);
        custom.put("boss", boss);
        custom.put("clonePool", clonePool);
        custom.put("gift", gift);
        custom.put("client", JsonUtils.toJSONString(gift.values()));

        ActivityConfigBean acb = new ActivityConfigBean();
        acb.setId(Manager.activityManager.deal().toActType(ActivityType.HolidayWords,0));
        acb.setType(Manager.activityManager.deal().toActType(ActivityType.HolidayWords,0));
        acb.setMinLv(0);
        acb.setMaxLv(801);
        acb.setTag((byte) 1);
        acb.setSort((byte) 1);
        acb.setName("集字活动");
        acb.setBeginTime(TimeUtils.Time() - 24 * 60 * 60 * 1000L);
        acb.setEndTime(TimeUtils.Time() + 30 * 24 * 60 * 60 * 1000L);
        acb.setIsDelete((byte) 0);
        acb.setCustom(JsonUtils.toJSONString(custom));
        Manager.activityManager.deal().registerActivityBean(acb);
        if (Manager.activityManager.getConfigDao().isExitActivity(acb.getId())) {
            Manager.activityManager.getConfigDao().update(DbSqlName.ACTIVITYCONFIG_UPDATE.getName(), acb);
        } else {
            Manager.activityManager.getConfigDao().update(DbSqlName.ACTIVITYCONFIG_INSERT.getName(), acb);
        }
    }

}
