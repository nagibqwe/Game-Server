package common.activity;

import com.data.ItemChangeReason;
import com.data.MessageString;
import com.game.activity.script.IActivityScript;
import com.game.activity.struct.ActivityConfig;
import com.game.activity.struct.ActivityType;
import com.game.activity.struct.RewardData;
import com.game.backpack.structs.Item;
import com.game.bi.enums.SignType;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.db.bean.ActivityConfigBean;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import com.game.utils.ServerParamUtil;
import game.core.json.TypeReference;
import game.core.util.IDConfigUtil;
import game.core.util.JsonUtils;
import game.core.util.TimeUtils;
import game.message.ActivityMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 祝福活动（活动每日签到活动）300022
 * */
public class FestvialSignScript implements IActivityScript {

    public static final Logger LOGGER = LogManager.getLogger(FestvialSignScript.class);

    private static final String configData = "configData";
    private static final String signState = "signState";                //签到天数的位运算，第0天是第0位
    private static final String serverBoxState = "serverBoxState";      //服务器宝箱天数领取的位运算，第0天是第0位
    private static final String CN_SERVER_COUNT_KEY = "serverCount";

    @Override
    public void onReqActivityDeal(Player player, String dataStr, ActivityConfig actCfg) {
        ActivityConfig activityConfig = actCfg;
        if (activityConfig == null) {
            LOGGER.error("祝福活动配置不存在");
            return;
        }
        FestivalSign festivalSign = (FestivalSign) activityConfig.getCustomCfgMap().get(configData);
        if (festivalSign == null) {
            LOGGER.error("祝福活动活动自定义配置参数不存在");
            return;
        }
        int startDay = TimeUtils.getCurDayByTime(activityConfig.getBeginTime());
        int curDay = TimeUtils.getCurDayByTime(TimeUtils.Time());
        int overDay = TimeUtils.getCurDayByTime(activityConfig.getEndTime());
        if (curDay < startDay || curDay > overDay) {
            LOGGER.error("活动时间已结束");
            return;
        }
        HashMap<String, Integer> msg = JsonUtils.parseObject(dataStr, new TypeReference<HashMap<String, Integer>>() {});
        //签到。day是要签哪一天
        if (msg.containsKey("day")) {
            sign(player, festivalSign, startDay, curDay, msg.get("day") - 1,actCfg);
        }
        //领取宝箱
        else if (msg.containsKey("reward")) {
            getBox(player, festivalSign, msg.get("reward"),actCfg);
        }

        ConcurrentHashMap<String, Object> actDataMap = Manager.activityManager.deal().getActivityData(actCfg.getType());
        //返回前端玩家数据
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(), activityConfig.getType());
        HashMap<String, Object> result = new HashMap<>();
        result.put("signBin", roleActDataMap.getOrDefault(signState, 0));
        result.put("serverBin", roleActDataMap.getOrDefault(serverBoxState, 0));
        result.put("serverCount", actDataMap.getOrDefault(CN_SERVER_COUNT_KEY,0));
        result.put("curDay", curDay - startDay + 1);
        ActivityMessage.ResActivityDeal.Builder pb = ActivityMessage.ResActivityDeal.newBuilder();
        pb.setData(JsonUtils.toJSONString(result));
        pb.setType(activityConfig.getType());
        MessageUtils.send_to_player(player, ActivityMessage.ResActivityDeal.MsgID.eMsgID_VALUE, pb.build().toByteArray());
    }

    //签到或不签。signDay是要签到或补签的天数。从0开始
    private void sign(Player player, FestivalSign festivalSign, int startDay, int curDay, int signDay, ActivityConfig actCfg) {
        if (signDay < 0 || signDay > curDay - startDay) {
            LOGGER.error("signDaybay不合法" + signDay);
            return;
        }
        //签到的奖励
        List<RewardData> rewardData = festivalSign.daily.get(signDay).rewards;
        if (rewardData == null || rewardData.isEmpty()) {
            LOGGER.error("奖励为空" + signDay);
            return;
        }
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(),actCfg.getType());
        int signBin = (Integer) roleActDataMap.getOrDefault(signState, 0);

        if ((signBin & 1 << signDay) > 0) {
            LOGGER.error("今天已经签过了");
            return;
        }

        //补签扣货币
        if (signDay != curDay - startDay) {
            if (! Manager.backpackManager.manager().removeItemOrCurrency(player, festivalSign.getBuqianid(), festivalSign.getBuqianCost(), IDConfigUtil.getLogId(), ItemChangeReason.NewYearWishSignCost)){
                LOGGER.error("货币不足");
                return;
            }
        }

        //保存状态
        roleActDataMap.put(signState, signBin | 1 << signDay);
        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));

        //发奖
        List<Item> itemList = new ArrayList<>();
        rewardData.forEach(data ->{
            if(data.getC() == player.getCareer() || data.getC() == 9) {
                itemList.addAll(Item.createItems(data.getI(), data.getN(), data.getB() == 1));
            }
        });
        if (!Manager.backpackManager.manager().addItems(player, itemList, ItemChangeReason.NewYearWishSignGet, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, itemList, ItemChangeReason.NewYearWishSignGet);
        }

        //服务器次数++
        ConcurrentHashMap<String, Object> actDataMap = Manager.activityManager.deal().getActivityData(actCfg.getType());
        int serverCount = (int)actDataMap.getOrDefault(CN_SERVER_COUNT_KEY,0);
        serverCount++;
        actDataMap.put(CN_SERVER_COUNT_KEY,serverCount);

        Manager.biManager.getScript().biSign(player, SignType.FestvialWish.getId(), 1, curDay - startDay + 1, serverCount);
    }

    //领取宝箱
    private void getBox(Player player, FestivalSign festivalSign, int id, ActivityConfig actCfg) {
        //签到的奖励
        ServerSignAward serverSignAward = null;
        int index = 0;
        for (ServerSignAward award : festivalSign.total) {
            if (award.id == id) {
                serverSignAward = award;
                break;
            }
            index ++;
        }
        if (serverSignAward == null || serverSignAward.rewards == null || serverSignAward.rewards.isEmpty()) {
            LOGGER.error("奖励为空" + id);
            return;
        }
        ConcurrentHashMap<String, Object> actDataMap = Manager.activityManager.deal().getActivityData(actCfg.getType());
        int serverCount = (int)actDataMap.getOrDefault(CN_SERVER_COUNT_KEY,0);
        if (serverCount < serverSignAward.need) {
            LOGGER.error("服务器次数还未达到");
            return;
        }
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(), actCfg.getType());
        int signBin = (Integer) roleActDataMap.getOrDefault(serverBoxState, 0);

        if ((signBin & 1 << index) > 0) {
            LOGGER.error("已经领取过了");
            return;
        }

        //保存状态
        roleActDataMap.put(serverBoxState, signBin | 1 << index);
        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));

        //发奖
        List<Item> itemList = new ArrayList<>();
        serverSignAward.rewards.forEach(data ->{
            if(data.getC() == player.getCareer() || data.getC() == 9) {
                itemList.addAll(Item.createItems(data.getI(), data.getN(), data.getB() == 1));
            }
        });
        if (!Manager.backpackManager.manager().addItems(player, itemList, ItemChangeReason.NewYearWishSignGet, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, itemList, ItemChangeReason.NewYearWishSignGet);
        }
        //记录BI
//        Manager.biManager.getScript().biActivity(player, ItemChangeReason.NewYearWishSignGet,actCfg.getType(), actCfg.getId());
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.NewYearWish, ItemChangeReason.NewYearWishSignGet);
    }

    @Override
    public boolean parseCustomConfig(ActivityConfig actCfg, String customStr) {
        FestivalSign data = JsonUtils.parseObject(customStr, FestivalSign.class);
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
        ActivityConfig activityConfig = actCfg;
        if (activityConfig == null) {
            LOGGER.error("祝福活动配置不存在");
            return null;
        }
        FestivalSign festivalSign = (FestivalSign) activityConfig.getCustomCfgMap().get(configData);
        if (festivalSign == null) {
            LOGGER.error("祝福活动活动自定义配置参数不存在");
            return null;
        }
        int startDay = TimeUtils.getCurDayByTime(activityConfig.getBeginTime());
        int curDay = TimeUtils.getCurDayByTime(TimeUtils.Time());
        int overDay = TimeUtils.getCurDayByTime(activityConfig.getEndTime());
        if (curDay < startDay || curDay > overDay) {
            LOGGER.error("活动时间已结束");
            return null;
        }

        ConcurrentHashMap<String, Object> actDataMap = Manager.activityManager.deal().getActivityData(actCfg.getType());
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(roleId, actCfg.getType());
        HashMap<String, Object> result = new HashMap<>();
        result.put("signBin", roleActDataMap.getOrDefault(signState, 0));
        result.put("serverBin", roleActDataMap.getOrDefault(serverBoxState, 0));
        result.put("serverCount", actDataMap.getOrDefault(CN_SERVER_COUNT_KEY,0));
        result.put("curDay", curDay - startDay + 1);
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
        List<ActivityConfig> actCfgList = Manager.activityManager.deal().getActCfgListByActLogicID(ActivityType.XinNianZhuFu);
        for (ActivityConfig activityConfig:actCfgList) {
            if (activityConfig == null) {
                return;
            }
            Object old = activityConfig.getCustomCfgMap().get(configData);
            if (old == null) {
                return;
            }
            String customStr = JsonUtils.toJSONString(old);
            FestivalSign newData = JsonUtils.toJavaObject(customStr, FestivalSign.class);
            activityConfig.getCustomCfgMap().put(configData, newData);
        }
    }

    @Override
    public int getId() {
        return ScriptEnum.XinNianZhuFuScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    public static class FestivalSign {
        private String client;
        private List<DailySignAward> daily;
        private List<ServerSignAward> total;
        private int buqianid;
        private int buqianCost;

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public List<DailySignAward> getDaily() {
            return daily;
        }

        public void setDaily(List<DailySignAward> daily) {
            this.daily = daily;
        }

        public List<ServerSignAward> getTotal() {
            return total;
        }

        public void setTotal(List<ServerSignAward> total) {
            this.total = total;
        }

        public int getBuqianid() {
            return buqianid;
        }

        public void setBuqianid(int buqianid) {
            this.buqianid = buqianid;
        }

        public int getBuqianCost() {
            return buqianCost;
        }

        public void setBuqianCost(int buqianCost) {
            this.buqianCost = buqianCost;
        }
    }

    //每日签到奖励
    public static class DailySignAward {
        private List<RewardData> rewards;
        private int day;
        private String modelId;    //前端用

        public List<RewardData> getRewards() {
            return rewards;
        }

        public void setRewards(List<RewardData> rewards) {
            this.rewards = rewards;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public String getModelId() {
            return modelId;
        }

        public void setModelId(String modelId) {
            this.modelId = modelId;
        }
    }

    //服务器次数奖励
    public static class ServerSignAward {
        private List<RewardData> rewards;
        private int id;
        private int need;

        public List<RewardData> getRewards() {
            return rewards;
        }

        public void setRewards(List<RewardData> rewards) {
            this.rewards = rewards;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getNeed() {
            return need;
        }

        public void setNeed(int need) {
            this.need = need;
        }
    }

//    public static void main(String[] args) {
//        new FestvialSignScript().gm();
//    }
//    public void gm() {
//        FestivalSign festivalSign = new FestivalSign();
//        festivalSign.setBuqianid(1);
//        festivalSign.setBuqianCost(50);
//        List<DailySignAward> dailySignAwardList = new ArrayList<>();
//        List<ServerSignAward> serverSignAwardList = new ArrayList<>();
//
//        DailySignAward dailySignAward = new DailySignAward();
//        ArrayList<RewardData> list = new ArrayList<>();
//        RewardData data1 = new RewardData();
//        data1.setN(1);
//        data1.setB(2);
//        data1.setC(3);
//        data1.setI(1019);
//        list.add(data1);
//        dailySignAward.setModelId(1019);
//        dailySignAward.setRewards(list);
//
//        DailySignAward dailySignAward2 = new DailySignAward();
//        ArrayList<RewardData> list2 = new ArrayList<>();
//        RewardData data12 = new RewardData();
//        data12.setN(1);
//        data12.setB(2);
//        data12.setC(3);
//        data12.setI(1019);
//        list2.add(data12);
//        dailySignAward2.setModelId(1019);
//        dailySignAward2.setDay(1);
//        dailySignAward2.setRewards(list2);
//
//        dailySignAwardList.add(dailySignAward);
//        dailySignAwardList.add(dailySignAward2);
//        festivalSign.setDaily(dailySignAwardList);
//
//
//        ServerSignAward ServerSignAward = new ServerSignAward();
//        ArrayList<RewardData> list3 = new ArrayList<>();
//        RewardData data13 = new RewardData();
//        data13.setN(1);
//        data13.setB(2);
//        data13.setC(3);
//        data13.setI(1019);
//        list3.add(data13);
//        ServerSignAward.setNeed(100);
//        ServerSignAward.setId(1);
//        ServerSignAward.setRewards(list3);
//
//        ServerSignAward ServerSignAward2 = new ServerSignAward();
//        ArrayList<RewardData> list23 = new ArrayList<>();
//        RewardData data123 = new RewardData();
//        data123.setN(1);
//        data123.setB(2);
//        data123.setC(3);
//        data123.setI(1019);
//        list23.add(data123);
//        ServerSignAward2.setNeed(500);
//        ServerSignAward2.setId(2);
//        ServerSignAward2.setRewards(list23);
//
//        serverSignAwardList.add(ServerSignAward);
//        serverSignAwardList.add(ServerSignAward2);
//        festivalSign.setTotal(serverSignAwardList);
//
//        //String s = "{\"keyid\":1033,\"oneCostKey\":1,\"tenCostKey\":10,\"oneCostGold\":50,\"tenCostGold\":500,\"lowestRewards\":[{\"b\":0,\"c\":1,\"i\":12003,\"n\":1,\"s\":50},{\"b\":0,\"c\":1,\"i\":12003,\"n\":1,\"s\":200},{\"b\":0,\"c\":1,\"i\":12003,\"n\":1,\"s\":500},{\"b\":0,\"c\":1,\"i\":12003,\"n\":1,\"s\":1000},{\"b\":0,\"c\":1,\"i\":12003,\"n\":1,\"s\":2000}],\"rewardPool\":[{\"b\":0,\"c\":1,\"i\":10001,\"n\":1,\"isS\":1,\"isB\":1},{\"b\":0,\"c\":1,\"i\":10002,\"n\":1,\"isS\":1,\"isB\":1},{\"b\":0,\"c\":1,\"i\":10003,\"n\":1,\"isS\":1,\"isB\":1},{\"b\":0,\"c\":1,\"i\":10004,\"n\":1,\"isS\":1,\"isB\":1},{\"b\":0,\"c\":1,\"i\":10005,\"n\":1,\"isS\":1,\"isB\":1},{\"b\":0,\"c\":1,\"i\":10006,\"n\":1,\"isS\":1,\"isB\":1},{\"b\":0,\"c\":1,\"i\":10007,\"n\":1,\"isS\":1,\"isB\":1},{\"b\":0,\"c\":1,\"i\":10008,\"n\":1,\"isS\":1,\"isB\":1},{\"b\":0,\"c\":1,\"i\":10009,\"n\":1,\"isS\":1,\"isB\":1},{\"b\":0,\"c\":1,\"i\":10010,\"n\":1,\"isS\":1,\"isB\":1},{\"b\":0,\"c\":1,\"i\":10011,\"n\":1,\"isS\":1,\"isB\":1},{\"b\":0,\"c\":1,\"i\":10012,\"n\":1,\"isS\":1,\"isB\":1},{\"b\":0,\"c\":1,\"i\":10013,\"n\":1,\"isS\":1,\"isB\":1},{\"b\":0,\"c\":1,\"i\":10014,\"n\":1,\"isS\":0,\"isB\":1},{\"b\":0,\"c\":1,\"i\":10015,\"n\":1,\"isS\":0,\"isB\":1},{\"b\":0,\"c\":1,\"i\":10016,\"n\":1,\"isS\":0,\"isB\":1},{\"b\":0,\"c\":1,\"i\":10017,\"n\":1,\"isS\":0,\"isB\":1},{\"b\":0,\"c\":1,\"i\":10018,\"n\":1,\"isS\":0,\"isB\":1}]}";
//        String s =
//                "{\"daily\":[{\"day\":1,\"modelId\":1,\"r\":1,\"s\":1,\"x\":0,\"y\":0,\"rx\":0,\"ry\":1,\"rz\":1,\"rewards\":[{\"b\":0,\"c\":1,\"i\":12003,\"n\":1},{\"b\":0,\"c\":0,\"i\":12004,\"n\":1}]},{\"day\":2,\"modelId\":2,\"r\":1,\"s\":1,\"x\":0,\"y\":0,\"rx\":0,\"ry\":1,\"rz\":1,\"rewards\":[{\"b\":0,\"c\":1,\"i\":12003,\"n\":1},{\"b\":0,\"c\":0,\"i\":12004,\"n\":1}]}],\"total\":[{\"id\":1,\"need\":100,\"rewards\":[{\"b\":0,\"c\":1,\"i\":12003,\"n\":1},{\"b\":0,\"c\":0,\"i\":12004,\"n\":1}]},{\"id\":2,\"need\":500,\"rewards\":[{\"b\":0,\"c\":1,\"i\":12003,\"n\":1},{\"b\":0,\"c\":0,\"i\":12004,\"n\":1}]}],\"buqianid\":1,\"buqianCost\":50}"
//                ;
//        festivalSign.setClient(s);
//                s = JsonUtils.toJSONString(festivalSign);
//
//
//        System.err.println(s);
//
//        ActivityConfigBean acb2 = new ActivityConfigBean();
//        acb2.setId(33);
//        acb2.setType(ActivityType.XinNianZhuFu);
//        acb2.setMinLv(1);
//        acb2.setMaxLv(800);
//        acb2.setTag((byte) 1);
//        acb2.setSort((byte) 1);
//        acb2.setName("新春祝福");
//        acb2.setBeginTime(1608480000000L);
//        acb2.setEndTime(1608861363000L);
//        acb2.setIsDelete((byte) 0);
//        acb2.setCustom(s);
//        //System.err.println(JsonUtils.toJSONStringWriteClassName(allGiftData));
//
//        Manager.activityManager.deal().registerActivityBean(acb2);
//
//        ActivityConfig activityConfig2 = Manager.activityManager.getActCfgMap().get(ActivityType.XinNianZhuFu);
//        System.err.println("~~~~~~"+activityConfig2);
//        System.err.println(activityConfig2.getCustomCfgMap().toString());
//        //System.err.println(ActivityManager.getInstance().deal().onReqActivity(null,ActivityType.FestivalWish ););
//    }
}
