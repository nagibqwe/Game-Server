package common.activity;

import com.data.ItemChangeReason;
import com.data.MessageString;
import com.fasterxml.jackson.annotation.JsonIgnore;

import com.game.activity.script.IActivityScript;
import com.game.activity.struct.ActivityConfig;
import com.game.activity.struct.ActivityType;
import com.game.activity.struct.RewardData;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.bi.enums.ResourceType;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.db.bean.ActivityConfigBean;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerWorldInfo;
import com.game.script.structs.ScriptEnum;
import com.game.server.DbSqlName;
import game.core.json.TypeReference;
import game.core.util.IDConfigUtil;
import game.core.util.JsonUtils;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Desc TODO 300018
 * @Date 2020/10/16 10:42
 * @Auth ZUncle
 */
public class HolidayScoreRankScript implements IActivityScript {

    final transient Logger logger = LogManager.getLogger(HolidayScoreRankScript.class);

    //region 数据KEY
    final transient String Client = "client";
    final transient String ActivityData = "ActivityData";
    final transient String RewardRecord = "RewardRecord";
    final transient String CostRecord = "CostRecord";
    final transient String RankList = "RankList";
    final transient String Rank = "Rank";
    //endregion

    //region 数据结构
    static class RankRole {
        long roleId;        //玩家ID
        long cost;          //消耗元宝数量
        long time;          //消耗时间
        @JsonIgnore
        int rank;           //排名

        public RankRole() {
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public RankRole(long roleId) {
            this.roleId = roleId;
        }

        public long getRoleId() {
            return roleId;
        }

        public void setRoleId(long roleId) {
            this.roleId = roleId;
        }

        public long getCost() {
            return cost;
        }

        public void setCost(long cost) {
            this.cost = cost;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }
    }

    static class RankReward {
        int start;          //开始名次
        int tail;            //结束名次
        int score;          //积分条件
        List<RewardData> items; //奖励

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public int getTail() {
            return tail;
        }

        public void setTail(int tail) {
            this.tail = tail;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public List<RewardData> getItems() {
            return items;
        }

        public void setItems(List<RewardData> items) {
            this.items = items;
        }
    }

    static class ScoreReward {
        int score;              //积分条件
        List<RewardData> items; //奖励

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public List<RewardData> getItems() {
            return items;
        }

        public void setItems(List<RewardData> items) {
            this.items = items;
        }
    }

    /**
     * 活动数据
     */
    static class ScoreRankActivity {
        String client;              //前端数据
        List<RankReward> ranks;     //排名奖励
        HashMap<Integer, ScoreReward> scores;  //积分奖励
        int limit;                  //排名展示
        int gold;                   //一个积分等于多少元宝

        public int getGold() {
            return gold;
        }

        public void setGold(int gold) {
            this.gold = gold;
        }

        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public List<RankReward> getRanks() {
            return ranks;
        }

        public void setRanks(List<RankReward> ranks) {
            this.ranks = ranks;
        }

        public HashMap<Integer, ScoreReward> getScores() {
            return scores;
        }

        public void setScores(HashMap<Integer, ScoreReward> scores) {
            this.scores = scores;
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

        int score = msg.get("score"); //积分奖励

        ScoreRankActivity activity = (ScoreRankActivity) actCfg.getCustomCfgMap().get(ActivityData);
        ScoreReward gift = activity.getScores().get(score);
        if (gift == null) {
            return;
        }

        ConcurrentHashMap<String, Object> activityData = Manager.activityManager.deal().getRoleActivityData(player.getId(), actCfg.getType());

        String roleStr = (String) activityData.getOrDefault(CostRecord, "{}");
        RankRole role = JsonUtils.parseObject(roleStr, new TypeReference<RankRole>() {
        });

        //运营如果配置了的消费时间在 活动开始前，那么要去抓取活动开始之前的消费记录
        if (role.getCost() <= 0) {
            long timeConsumption = Manager.activityManager.getRoleTimeConsumption(player.getId(), actCfg);
            role.setCost(timeConsumption);
        }
        //积分是否达到
        if (score > role.getCost() / activity.getGold()) {
            return;
        }
        HashMap<Integer, Boolean> record = (HashMap<Integer, Boolean>) activityData.getOrDefault(RewardRecord, new HashMap<Integer, Boolean>());
        if (record.containsKey(score)) {
            return;
        }
        record.put(score, true);
        activityData.put(RewardRecord, record);

        //TODO 发放奖励
        List<Item> items = Item.createItems(player.getCareer(), gift.getItems());

        long actionId = IDConfigUtil.getLogId();
        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.HolidayActivityScoreRankReachGet, actionId)) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.HolidayActivityScoreRankReachGet, actionId);
        }

        HashMap<String, Object> message = new HashMap<>();
        message.put("score", score);
        Manager.activityManager.deal().sendActivityDealMessage(player, actCfg.getType(), JsonUtils.toJSONString(message));
        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));

        logger.info("积分排名.积分奖励 score={} player={}", score, player);
        //BI记录
//        Manager.biManager.getScript().biActivity(player, ItemChangeReason.HolidayActivityScoreRankReachGet, actCfg.getType(), actCfg.getId());
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.HolidayScore, ItemChangeReason.HolidayActivityScoreRankReachGet);

    }

    /**
     * 解析活动自己的自定义配置
     *
     * @param actCfg
     * @param customStr
     */
    @Override
    public boolean parseCustomConfig(ActivityConfig actCfg, String customStr) {
        ScoreRankActivity draw = JsonUtils.parseObject(customStr, ScoreRankActivity.class);
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

        ScoreRankActivity activity = (ScoreRankActivity) actCfg.getCustomCfgMap().get(ActivityData);

        ConcurrentHashMap<String, Object> activityData = Manager.activityManager.deal().getRoleActivityData(roleId, actCfg.getType());

        HashMap<Integer, Boolean> record = (HashMap<Integer, Boolean>) activityData.getOrDefault(RewardRecord, new HashMap<Integer, Boolean>());

        String roleStr = (String) activityData.getOrDefault(CostRecord, "{}");
        RankRole role = JsonUtils.parseObject(roleStr, new TypeReference<RankRole>() {
        });
        //运营如果配置了的消费时间在 活动开始前，那么要去抓取活动开始之前的消费记录
        if (role.getCost() <= 0) {
            long timeConsumption = Manager.activityManager.getRoleTimeConsumption(roleId, actCfg);
            role.setCost(timeConsumption);
        }
        HashMap<String, Object> self = new HashMap<>();

        self.put("score", role.getCost() / activity.getGold());
        self.put("rank", activityData.getOrDefault(Rank, 0));

        ConcurrentHashMap<String, Object> serverActivityData = Manager.activityManager.deal().getActivityData(actCfg.getType());

        List<HashMap<String, Object>> ranks = (List<HashMap<String, Object>>) serverActivityData.getOrDefault(RankList, new ArrayList<>());
        for (HashMap<String, Object> rank : ranks) {
            Object id = rank.get("id");
            PlayerWorldInfo playerWorldInfo = Manager.playerManager.getPlayerWorldInfo((long) id);
            rank.put("name", playerWorldInfo.getRolename());
            rank.put("vip", playerWorldInfo.getPlayerVip());
        }

        HashMap<String, Object> message = new HashMap<>();
        message.put("giftHistory", record.keySet());  //礼物兑换记录
        message.put("ranks", ranks); //排行榜
        message.put("myRank", self);                    //个人记录
        message.put("timeout", (actCfg.getEndTime() - TimeUtils.Time()) / 1000);    //活动结束倒计时
        return JsonUtils.toJSONString(message);
    }

    /**
     * 特殊排序
     *
     * @param scores
     */
    void sort(List<RankRole> scores, ScoreRankActivity activity, ActivityConfig actCfg) {
        activity.getRanks().sort(Comparator.comparingLong(RankReward::getStart));
        scores.sort(Comparator.comparingLong(RankRole::getCost).thenComparingLong(RankRole::getTime).reversed());

        //TODO 根据积分重排
        int rank = 1;
        for (RankRole role : scores) {
            long score = role.getCost() / activity.getGold();
            for (RankReward bean : activity.getRanks()) {
                if (score < bean.getScore()) {
                    rank = Math.max(rank, bean.getTail() + 1);
                    continue;
                }
                if (rank < bean.getStart() || rank > bean.getTail()) {
                    rank = Math.max(rank, bean.getTail() + 1);
                    continue;
                }
                break;
            }
            role.setRank(rank);
            rank = rank + 1;
        }
    }

    /**
     * 活动结束时每个活动的特殊处理
     */
    @Override
    public void activityEndDeal(ActivityConfig actCfg) {

        ScoreRankActivity activity = (ScoreRankActivity) actCfg.getCustomCfgMap().get(ActivityData);

        List<RankRole> scores = new ArrayList<>();
        for (ConcurrentHashMap<Integer, ConcurrentHashMap<String, Object>> activitys : Manager.activityManager.getRoleActDatas().values()) {
            ConcurrentHashMap<String, Object> activityTemp = activitys.get(actCfg.getType());
            if (activityTemp == null) {
                continue;
            }
            if (activityTemp.containsKey(CostRecord)) {
                String roleStr = (String) activityTemp.getOrDefault(CostRecord, "{}");
                RankRole role = JsonUtils.parseObject(roleStr, new TypeReference<RankRole>() {
                });
                //运营如果配置了的消费时间在 活动开始前，那么要去抓取活动开始之前的消费记录
                if (role.getCost() <= 0) {
                    long timeConsumption = Manager.activityManager.getRoleTimeConsumption(role.getRoleId(), actCfg);
                    role.setCost(timeConsumption);
                }
                scores.add(role);
            }
        }
        sort(scores, activity, actCfg);
        for (RankRole role : scores) {
            RankReward rankReward = calcReward(activity, role, actCfg);
            if (rankReward == null) {
                continue;
            }
            PlayerWorldInfo player = Manager.playerManager.getPlayerWorldInfo(role.getRoleId());
            if (player == null) {
                continue;
            }
            //TODO 发放奖励
            List<Item> items = Item.createItems(player.getCareer(), rankReward.getItems());

            ConcurrentHashMap<String, Object> activityData = Manager.activityManager.deal().getRoleActivityData(role.getRoleId(), actCfg.getType());
            HashMap<Integer, Boolean> record = (HashMap<Integer, Boolean>) activityData.getOrDefault(RewardRecord, new HashMap<Integer, Boolean>());

            //未领取积分奖励
            for (ScoreReward sr : activity.getScores().values()) {
                if (record.containsKey(sr.getScore())) {
                    continue;
                }
                //积分是否达到
                if (role.getCost() / activity.getGold() < sr.getScore()) {
                    continue;
                }
                items.addAll(Item.createItems(player.getCareer(), sr.getItems()));
            }
            Manager.mailManager.sendMailToPlayer(role.getRoleId(), MessageString.System, MessageString.System, MessageString.GOLD_COST_RANK_TITLE, MessageString.GOLD_COST_RANK, items, ItemChangeReason.HolidayActivityScoreRankReachGet);
        }
        logger.info("元宝积分活动结束。发放排名奖励 ！！！");
    }

    /**
     * 计算排名奖励
     *
     * @param activity
     * @param role
     */
    RankReward calcReward(ScoreRankActivity activity, RankRole role, ActivityConfig actCfg) {
        for (RankReward reward : activity.getRanks()) {
            if (role.getRank() >= reward.getStart() && role.getRank() <= reward.getTail()) {
                return reward;
            }
        }
        return null;
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

        if (ItemCoinType.GemCoin != coinType) {
            return;
        }

        ConcurrentHashMap<String, Object> activityData = Manager.activityManager.deal().getRoleActivityData(player.getId(), actCfg.getType());
        String roleStr = (String) activityData.getOrDefault(CostRecord, "{}");
        RankRole rr = JsonUtils.parseObject(roleStr, new TypeReference<RankRole>() {
        });
        rr.setRoleId(player.getId());
        //运营如果配置了的消费时间在 活动开始前，那么要去抓取活动开始之前的消费记录
        if (rr.getCost() <= 0) {
            long timeConsumption = Manager.activityManager.getRoleTimeConsumption(player.getId(), actCfg);
            rr.setCost(timeConsumption);
        }
        rr.setCost(rr.getCost() + consumeNum);
        rr.setTime(TimeUtils.Time());
        activityData.put(CostRecord, JsonUtils.toJSONString(rr));


        ScoreRankActivity activity = (ScoreRankActivity) actCfg.getCustomCfgMap().get(ActivityData);
        ConcurrentHashMap<String, Object> serverActivityData = Manager.activityManager.deal().getActivityData(actCfg.getType());

        List<RankRole> scores = new ArrayList<>();
        for (ConcurrentHashMap<Integer, ConcurrentHashMap<String, Object>> activitys : Manager.activityManager.getRoleActDatas().values()) {
            ConcurrentHashMap<String, Object> activityTemp = activitys.get(actCfg.getType());
            if (activityTemp == null) {
                continue;
            }
            if (activityTemp.containsKey(CostRecord)) {
                roleStr = (String) activityTemp.getOrDefault(CostRecord, "{}");
                RankRole role = JsonUtils.parseObject(roleStr, new TypeReference<RankRole>() {
                });
                scores.add(role);
            }
        }
        sort(scores, activity, actCfg);
        List<HashMap<String, Object>> ranks = new ArrayList<>();
        for (RankRole role : scores) {
            ConcurrentHashMap<String, Object> activityTemp = Manager.activityManager.deal().getRoleActivityData(role.getRoleId(), actCfg.getType());
            activityTemp.put(Rank, role.getRank());
            if (ranks.size() <= activity.getLimit()) {
                PlayerWorldInfo playerWorldInfo = Manager.playerManager.getPlayerWorldInfo(role.getRoleId());
                HashMap<String, Object> rank = new HashMap<>();
                rank.put("id", role.getRoleId());
                rank.put("name", playerWorldInfo.getRolename());
                rank.put("score", role.getCost() / activity.getGold());
                rank.put("rank", role.getRank());
                rank.put("vip", playerWorldInfo.getPlayerVip());
                ranks.add(rank);
            }
        }
        serverActivityData.put(RankList, ranks);
        logger.info("双旦。元宝积分排名 rank={} player={}", activityData.get(Rank), player);

        BigInteger bigChange = BigInteger.valueOf(consumeNum);
        BigInteger afterNum = BigInteger.valueOf(rr.getCost());
        BigInteger beforeNum = afterNum.subtract(bigChange);
        Manager.biManager.getScript().biResource(player, 1, ResourceType.HolidayScoreRankScore.getId(), bigChange, beforeNum, afterNum, 0, 0, ItemChangeReason.HolidayActivityScoreRankReachGet, IDConfigUtil.getLogId());
    }

    @Override
    public void reload() {
        if (Manager.activityManager == null) {
            return;
        }
        List<ActivityConfig> actCfgList = Manager.activityManager.deal().getActCfgListByActLogicID(ActivityType.HolidayScoreRank);
        for (ActivityConfig activityConfig : actCfgList) {
            if (activityConfig == null) {
                return;
            }
            Object old = activityConfig.getCustomCfgMap().get(ActivityData);
            if (old == null) {
                return;
            }
            String customStr = JsonUtils.toJSONString(old);
            ScoreRankActivity newData = JsonUtils.parseObject(customStr, ScoreRankActivity.class);
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
        return ScriptEnum.HolidayScoreRankScript;
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

    public static void testBuild() {

        //积分排名奖励
        List<HashMap<String, Object>> ranks = new ArrayList<>();
        for (int c = 1; c < 50; ) {
            List<RewardData> list = new ArrayList<>();
            for (int i = 1; i < 3; i++) {
                RewardData item = new RewardData();
                item.setI(2001);
                item.setN(i);
                item.setB(1);
                item.setC(9);
                list.add(item);
            }
            int start = c;
            int end = start + 5;
            c = end + 1;
            HashMap<String, Object> rank = new HashMap<>();
            rank.put("start", start);
            rank.put("tail", end);
            rank.put("score", 5000 - start * 100);
            rank.put("items", list);
            ranks.add(rank);
        }
        //积分奖励
        HashMap<Integer, Object> scores = new HashMap<>();
        for (int c = 1; c < 10; c++) {
            List<RewardData> list = new ArrayList<>();
            for (int i = 1; i < 5; i++) {
                RewardData item = new RewardData();
                item.setI(2001);
                item.setN(i);
                item.setB(1);
                item.setC(9);
                list.add(item);
            }
            HashMap<String, Object> rank = new HashMap<>();
            rank.put("score", c * 250);
            rank.put("items", list);
            scores.put(c * 250, rank);
        }

        HashMap<String, Object> client = new HashMap();
        client.put("ranks", ranks);
        client.put("scores", scores.values());


        HashMap<String, Object> custom = new HashMap();
        custom.put("ranks", ranks);
        custom.put("scores", scores);
        custom.put("limit", 50);
        custom.put("gold", 10);
        custom.put("client", JsonUtils.toJSONString(client));

        ActivityConfigBean acb = new ActivityConfigBean();
        acb.setId(Manager.activityManager.deal().toActType(ActivityType.HolidayScoreRank, 0));
        acb.setType(Manager.activityManager.deal().toActType(ActivityType.HolidayScoreRank, 0));
        acb.setMinLv(0);
        acb.setMaxLv(801);
        acb.setTag((byte) 1);
        acb.setSort((byte) 1);
        acb.setName("积分排名");
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
