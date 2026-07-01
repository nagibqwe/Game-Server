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
 * @Desc TODO 元旦首领 300011
 * @Date 2020/10/16 10:41
 * @Auth ZUncle
 */
public class HolidayBossScript implements IActivityScript {

    final transient Logger logger = LogManager.getLogger(HolidayBossScript.class);

    //region 数据索引
    final transient String Client = "client";
    final transient String ActivityData = "ActivityData";
    final transient String GoodsLimit = "GoodsLimit";

    //endregion

    //region 数据结构

    /**
     * 掉落
     */
    static class BossGift {
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
     * 活动出售道具
     */
    static class Goods {
        int id;             //商品ID
        int price;          //单价
        int coin;           //出售货币
        int limit;          //单日限购
        RewardData item;    //出售道具

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

        public int getCoin() {
            return coin;
        }

        public void setCoin(int coin) {
            this.coin = coin;
        }

        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public RewardData getItem() {
            return item;
        }

        public void setItem(RewardData item) {
            this.item = item;
        }
    }

    /**
     * 限购
     */
    static class LimitBuy implements ICount {
        ConcurrentHashMap<String, Count> counts = new ConcurrentHashMap<>();

        /**
         * 获取技术数据
         *
         * @return
         */
        @Override
        public ConcurrentHashMap<String, Count> getCounts() {
            return counts;
        }
    }

    /**
     * 活动数据
     */
    static class HolidayBossActivity {
        String client;                              //前端数据
        List<Integer> bossList;                     //boss列表
        List<BossGift> bossGiftPool;                //boss掉落奖池
        HashMap<Integer, List<BossGift>> boxGiftPool;//宝箱掉落奖池
        HashMap<Integer, Goods> goods;              //限购商品数据

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public List<Integer> getBossList() {
            return bossList;
        }

        public void setBossList(List<Integer> bossList) {
            this.bossList = bossList;
        }

        public List<BossGift> getBossGiftPool() {
            return bossGiftPool;
        }

        public void setBossGiftPool(List<BossGift> bossGiftPool) {
            this.bossGiftPool = bossGiftPool;
        }

        public HashMap<Integer, List<BossGift>> getBoxGiftPool() {
            return boxGiftPool;
        }

        public void setBoxGiftPool(HashMap<Integer, List<BossGift>> boxGiftPool) {
            this.boxGiftPool = boxGiftPool;
        }

        public HashMap<Integer, Goods> getGoods() {
            return goods;
        }

        public void setGoods(HashMap<Integer, Goods> goods) {
            this.goods = goods;
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

        int goodsId = msg.get("goodsId"); //itemId=购买道具ID

        HolidayBossActivity activity = (HolidayBossActivity) actCfg.getCustomCfgMap().get(ActivityData);

        Goods goods = activity.getGoods().get(goodsId);
        if (goods == null) {
            return;
        }
        ConcurrentHashMap<String, Object> activityData = Manager.activityManager.deal().getRoleActivityData(player.getId(),actCfg.getType());
        String data = (String) activityData.getOrDefault(GoodsLimit, "{}");
        LimitBuy limitBuy = JsonUtils.parseObject(data, new TypeReference<LimitBuy>() {
        });

        //检测限购
        long count = Manager.countManager.getCount(limitBuy, BaseCountType.Activity, goods.getId());
        if (count >= goods.getLimit()) {
            return;
        }
        //购买
        long logId = IDConfigUtil.getLogId();
        if (!Manager.backpackManager.manager().onRemoveItem(player, goods.getCoin(), goods.getPrice(), ItemChangeReason.HolidayBossShopCost, logId)) {
            return;
        }
        List<Item> items = Item.createItems(goods.getItem().getI(), goods.getItem().getN(), goods.getItem().getB() == 1);
        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.HolidayBossShopGet, logId)) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.HolidayBossShopGet);
        }
        //增加一次购买记录
        Manager.countManager.addCount(limitBuy, BaseCountType.Activity, goodsId, Count.RefreshType.CountType_Day, 1);
        activityData.put(GoodsLimit, JsonUtils.toJSONString(limitBuy));

        HashMap<String, Object> message = new HashMap<>();
        message.put("goodsId", goodsId);
        message.put("count", count + 1);
        Manager.activityManager.deal().sendActivityDealMessage(player, actCfg.getType(), JsonUtils.toJSONString(message));
        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));

        logger.info("购买首领礼包 goodId={} player={}", goodsId, player);
        //记录BI
//        Manager.biManager.getScript().biActivity(player, ItemChangeReason.HolidayBossShopGet, actCfg.getType(), actCfg.getId());
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.HolidayBoss, ItemChangeReason.HolidayBossShopGet);
    }

    /**
     * 解析活动自己的自定义配置
     *
     * @param actCfg
     * @param customStr
     */
    @Override
    public boolean parseCustomConfig(ActivityConfig actCfg, String customStr) {
        HolidayBossActivity draw = JsonUtils.parseObject(customStr, HolidayBossActivity.class);
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

        HolidayBossActivity activity = (HolidayBossActivity) actCfg.getCustomCfgMap().get(ActivityData);

        ConcurrentHashMap<String, Object> activityData = Manager.activityManager.deal().getRoleActivityData(roleId,actCfg.getType());
        String data = (String) activityData.getOrDefault(GoodsLimit, "{}");
        LimitBuy limitBuy = JsonUtils.parseObject(data, new TypeReference<LimitBuy>() {
        });

        List<HashMap<String, Object>> shopHistory = new ArrayList<>();

        for (Goods goods : activity.getGoods().values()) {
            long count = Manager.countManager.getCount(limitBuy, BaseCountType.Activity, goods.getId());
            if (count <=0) {
                continue;
            }
            HashMap<String, Object> cell = new HashMap<>();
            cell.put("id", goods.getId());
            cell.put("buy", count);
            shopHistory.add(cell);
        }

        HashMap<String, Object> message = new HashMap<>();
        message.put("shopHistory", shopHistory);  //首领商店购买记录

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

        HolidayBossActivity activity = (HolidayBossActivity) actCfg.getCustomCfgMap().get(ActivityData);

        if (!activity.getBossList().contains(bossId)) {
            return false;
        }
        BossGift gift = random(activity.getBossGiftPool());

        List<Item> items = Item.createItems(player.getCareer(), gift.getGift());

        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.HolidayBossDropGet, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.HolidayBossDropGet);
        }
        logger.info("活动掉落 bossId={} player={}", bossId, player);
        //记录BI
//        Manager.biManager.getScript().biActivity(player, ItemChangeReason.HolidayBossDropGet, actCfg.getType(), actCfg.getId());
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.HolidayBoss, ItemChangeReason.HolidayBossDropGet);
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

        HolidayBossActivity activity = (HolidayBossActivity) actCfg.getCustomCfgMap().get(ActivityData);

        List<BossGift> pool = activity.getBoxGiftPool().get(boxId);
        if (pool == null) {
            return false;
        }
        BossGift gift = random(pool);

        List<Item> items = Item.createItems(player.getCareer(), gift.getGift());

        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.HolidayBossGiftDropGet, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.HolidayBossGiftDropGet);
        }
        logger.info("活动礼包掉落 boxId={} player={}", boxId, player);
        //记录BI
//        Manager.biManager.getScript().biActivity(player, ItemChangeReason.HolidayBossGiftDropGet, actCfg.getType(), actCfg.getId());
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.HolidayBoss, ItemChangeReason.HolidayBossGiftDropGet);
        return true;
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
        List<ActivityConfig> actCfgList = Manager.activityManager.deal().getActCfgListByActLogicID(ActivityType.HolidayBoss);
        for (ActivityConfig activityConfig:actCfgList) {
            if (activityConfig == null) {
                return;
            }
            Object old = activityConfig.getCustomCfgMap().get(ActivityData);
            if (old == null) {
                return;
            }
            String customStr = JsonUtils.toJSONString(old);
            HolidayBossActivity newData = JsonUtils.parseObject(customStr, HolidayBossActivity.class);
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
        return ScriptEnum.HolidayBossScript;
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
    private BossGift random(List<BossGift> params) {
        TreeMap<Float, BossGift> weightMap = new TreeMap<>();
        for (BossGift param : params) {
            Float weight = weightMap.size() == 0 ? 0f : weightMap.lastKey();
            weight += param.getWeight();
            weightMap.put(weight, param);
        }
        float randomWeight = RandomUtils.randomFloatValue(0f, weightMap.lastKey());
        SortedMap<Float, BossGift> sort = weightMap.tailMap(randomWeight, false);
        return weightMap.get(sort.firstKey());
    }

    public static void testBuild() {
        //任务队列
        HashMap<Integer, HashMap<String, Object>> goods = new HashMap<>();
        for (int id = 1; id < 5; id++) {
            RewardData item = new RewardData();
            item.setI(2001);
            item.setN(1);
            item.setB(1);
            item.setC(9);
            HashMap<String, Object> good = new HashMap<>();
            good.put("id", id);
            good.put("price", 50);
            good.put("coin", ItemCoinType.GemCoin);
            good.put("limit", id);
            good.put("item", item);
            goods.put(id, good);
        }

        List<RewardData> items = new ArrayList<>();
        for (int i = 1; i < 3; i++) {
            RewardData item = new RewardData();
            item.setI(2001);
            item.setN(i);
            item.setB(1);
            item.setC(9);
            items.add(item);
        }
        RewardData itemShow = new RewardData();
        itemShow.setI(2001);
        itemShow.setN(1);
        itemShow.setB(1);
        itemShow.setC(9);

        RewardData magicShow = new RewardData();
        magicShow.setI(15005);
        magicShow.setN(1);
        magicShow.setB(1);
        magicShow.setC(9);

        List<Integer> bossList = new ArrayList<>();
        for (int id = 11101; id <= 11108; id++ ) {
            bossList.add(id);
        }

        List<HashMap<String, Object>> bossGiftPool = new ArrayList<>();
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
            HashMap<String, Object> boss = new HashMap<>();
            boss.put("weight", c + 1);
            boss.put("gift", list);
            bossGiftPool.add(boss);
        }

        HashMap<Integer,Object> boxGiftPool = new HashMap<>();
        boxGiftPool.put(81003, bossGiftPool);

        HashMap<String, Object> client = new HashMap();
        client.put("magicId", magicShow);        //奖励展示
        client.put("boxId", itemShow);           //奖励展示
        client.put("showItems", items);      //奖励展示列表
        client.put("goods", goods.values());

        HashMap<String, Object> custom = new HashMap();
        custom.put("bossList", bossList);
        custom.put("bossGiftPool", bossGiftPool);
        custom.put("boxGiftPool", boxGiftPool);
        custom.put("goods", goods);
        custom.put("client", JsonUtils.toJSONString(client));

        ActivityConfigBean acb = new ActivityConfigBean();
        acb.setId(ActivityManager.getInstance().deal().toActType(ActivityType.HolidayBoss,0));
        acb.setType(ActivityManager.getInstance().deal().toActType(ActivityType.HolidayBoss,0));
        acb.setMinLv(0);
        acb.setMaxLv(801);
        acb.setTag((byte) 1);
        acb.setSort((byte) 1);
        acb.setName("元旦首领");
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
