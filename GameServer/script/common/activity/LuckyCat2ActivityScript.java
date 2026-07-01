package common.activity;


import com.data.ItemChangeReason;
import com.game.activity.script.IActivityScript;
import com.game.activity.struct.ActivityConfig;
import com.game.activity.struct.ActivityType;
import com.game.backpack.structs.ItemCoinType;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.utils.RandomUtils;
import game.core.util.IDConfigUtil;
import game.core.util.JsonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 招财猫2
 */
public class LuckyCat2ActivityScript implements IActivityScript {

    private static final Logger log = LogManager.getLogger(LuckyCat2ActivityScript.class);

    private static final String LUCK_CAT = "LUCK_CAT";
    private static final String configData = "configData";
    final transient String totalCanDrawNum = "totalCanDrawNum";//累积可以抽奖的次数
    final transient String drawNum = "drawNum";            //当前抽奖次数
    final transient String totalDrawNum = "totalDrawNum";  //累积抽奖次数
    final transient String serverDrawNumMap = "serverDrawNumMap";//全服档位抽奖次数map
    final transient String roleRechargeCount = "roleRechargeCount";//角色充值元宝数
    final transient String drawRecord = "drawRecord";      //抽奖记录


    @Override
    public void onReqActivityDeal(Player player, String dataStr, ActivityConfig actCfg) {
        int actType = actCfg.getType();
        //if(!Manager.activityManager.deal().checkLevel(player.getLevel(), actCfg)){
        //    return;
        //}

        LuckyCat2ActivityScript.LuckyCatActivity luckyCatActivity = (LuckyCat2ActivityScript.LuckyCatActivity) actCfg.getCustomCfgMap().get(configData);
        if (luckyCatActivity == null) {
            log.error("招财猫活动自定义配置参数不存在");
            return;
        }

        Integer[] rateList = luckyCatActivity.getRate();
        if(rateList.length!=8){
            return;
        }

        List<Integer[]> gearList = luckyCatActivity.getGear();
        if(gearList.isEmpty()){
            return;
        }

        ConcurrentHashMap<String, Object> roleActData = Manager.activityManager.deal().getRoleActivityData(player.getId(), actType);
        int num = (Integer)roleActData.getOrDefault(drawNum, 0);
        if(num<=0){
            return;
        }
        int totalUseNum = (Integer)roleActData.getOrDefault(totalDrawNum, 0);
//        long rechargeCount = (Long)roleActData.getOrDefault(roleRechargeCount, 0L);

        ConcurrentHashMap<String, Object> actData = Manager.activityManager.deal().getActivityData(actType);

        LinkedHashMap<String, Integer> serverDrawMap;
        if(actData.get(serverDrawNumMap) == null){
            serverDrawMap = new LinkedHashMap<>();
            for (Integer[] gear:luckyCatActivity.getGear()) {
                serverDrawMap.put(String.valueOf(gear[1]), 0);
            }
            actData.put(serverDrawNumMap, serverDrawMap);
        }else{
            serverDrawMap = (LinkedHashMap<String, Integer>)actData.get(serverDrawNumMap);
        }

        int cost = 0;
        int ranMin = 0;
        int ranMax = 7;
        String rechargeKey;
        int serverLimitCount = 0;
        if(gearList.size()>totalUseNum){
            ranMin = gearList.get(totalUseNum)[2];
            ranMax = gearList.get(totalUseNum)[3];
            cost = gearList.get(totalUseNum)[0];
            rechargeKey = gearList.get(totalUseNum)[1].toString();
            serverLimitCount = gearList.get(totalUseNum)[4];
        }else{//取最后一档
            cost = gearList.get(gearList.size()-1)[0];
            rechargeKey = gearList.get(gearList.size()-1)[1].toString();
            serverLimitCount = gearList.get(gearList.size()-1)[4];
        }

        //检查全服抽奖限制
        if(serverLimitCount>0&&serverDrawMap.get(rechargeKey)>=serverLimitCount){
            return;
        }

        //执行抽奖逻辑
        //计算权重
        Integer[] weights = luckyCatActivity.getWeight();
        List<LuckyCat2ActivityScript.Weight> weightList = new ArrayList<>();
        for (int i = ranMin; i <= ranMax; i++) {
            LuckyCat2ActivityScript.Weight weight = new LuckyCat2ActivityScript.Weight();
            weight.setIndex(i);
            weight.setRate(weights[i]);
            weightList.add(weight);
        }

        LuckyCat2ActivityScript.Weight weight = randomWeight(weightList);
        int rate = rateList[weight.getIndex()];

//        int rate = rateList[RandomUtils.random(ranMin,ranMax)];
        int money = (cost * rate) / 100;
        //扣除剩余次数
        roleActData.put(drawNum, num-1);
        //累积抽奖次数
        totalUseNum+=1;
        roleActData.put(totalDrawNum, totalUseNum);
        serverDrawMap.put(rechargeKey, serverDrawMap.get(rechargeKey)==null?1:serverDrawMap.get(rechargeKey)+1);
        actData.put(serverDrawNumMap,serverDrawMap);

        if(actData.get(drawRecord) == null){
            actData.put(drawRecord, new ArrayList<>());
        }
        List<String> records = (List<String>) actData.getOrDefault(drawRecord, new ArrayList<String>());
        if(records.size()>=20){
            records.remove(0);
        }
        records.add(new StringBuilder(player.getName()).append("_").append(rate).append("_").append(money).toString());
        actData.put(drawRecord, records);

        //扣除货币
        long actionId = IDConfigUtil.getLogId();
        Manager.currencyManager.manager().onDecItemCoin(player, cost, ItemChangeReason.ActivityLuckyCat2Dec, actionId, ItemCoinType.BindGemCoin);

        //发放奖励
        Manager.currencyManager.manager().onAddItemCoin(player, ItemCoinType.BindGemCoin, money, ItemChangeReason.ActivityLuckyCat2Get, actionId);

        //发送抽奖成功消息
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("rate", rate);
        resultMap.put("money", money);
        resultMap.put("serverDrawMap", serverDrawMap);
        Manager.activityManager.deal().sendActivityDealMessage(player, actType, JsonUtils.toJSONString(resultMap));

        //发送活动数据改变消息
//        Manager.activityManager.deal().sendActivityDataChange(player, actCfg.getType());
        //保存角色活动数据
        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));
        //保存活动数据
        Manager.activityManager.deal().saveActData(actCfg.getType(), actData);
        //记录BI
//        Manager.biManager.getScript().biActivity(player, ItemChangeReason.ActivityLuckyCatGet, actType, actCfg.getId());
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.LuckyCat2, ItemChangeReason.ActivityLuckyCat2Get);
    }

    @Override
    public boolean parseCustomConfig(ActivityConfig actCfg, String customStr) {
        LuckyCat2ActivityScript.LuckyCatActivity data = JsonUtils.toJavaObject(customStr, LuckyCat2ActivityScript.LuckyCatActivity.class);
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
        int actType = actCfg.getType();
        LuckyCat2ActivityScript.LuckyCatActivity luckyCatActivity = (LuckyCat2ActivityScript.LuckyCatActivity) actCfg.getCustomCfgMap().get(configData);
        if (luckyCatActivity == null) {
            log.error("招财猫活动自定义配置参数不存在");
            return null;
        }

        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(roleId, actType);
        Integer num = (Integer) roleActDataMap.getOrDefault(drawNum, 0);
        Integer totalNum = (Integer) roleActDataMap.getOrDefault(totalDrawNum, 0);
        Integer totalCanNum = (Integer) roleActDataMap.getOrDefault(totalCanDrawNum, 0);

        long roleTimeRecharge  =  Manager.activityManager.getRoleTimeRecharge(roleId,actCfg);
        if (roleActDataMap.containsKey(roleRechargeCount)){
            long rechargeCount = Long.parseLong(roleActDataMap.get(roleRechargeCount).toString());
            if (rechargeCount <=0){
                roleActDataMap.put(roleRechargeCount, roleTimeRecharge );
            }
        }else {
            roleActDataMap.putIfAbsent(roleRechargeCount,  roleTimeRecharge);
        }

        List<Integer[]> gearList = luckyCatActivity.getGear();

        long rechargeCount = Long.parseLong(roleActDataMap.get(roleRechargeCount).toString());

        int addNum = 0;
        for (int i = totalCanNum; i < gearList.size(); i++) {
            if(rechargeCount>=gearList.get(i)[1]){
                addNum++;
            }
        }
        if(addNum>0){
            roleActDataMap.put(drawNum, num+addNum);
            roleActDataMap.put(totalCanDrawNum, totalCanNum+addNum);
        }
        //加入活动公共数据
        ConcurrentHashMap<String, Object> actData = Manager.activityManager.deal().getActivityData(actType);

        LinkedHashMap<String, Integer> serverDrawMap;
        if(actData.get(serverDrawNumMap) == null){
            serverDrawMap = new LinkedHashMap<>();
            for (Integer[] gear:luckyCatActivity.getGear()) {
                serverDrawMap.put(String.valueOf(gear[1]), 0);
            }
            actData.put(serverDrawNumMap, serverDrawMap);
        }else{
            serverDrawMap = (LinkedHashMap<String, Integer>)actData.get(serverDrawNumMap);
        }

        if(actData == null){
            actData = new ConcurrentHashMap<>();
            Manager.activityManager.getActDatas().put(actType, actData);
            //只返回玩家数据
            return JsonUtils.toJSONString(roleActDataMap);
        }
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("num", num);
        resultMap.put("totalNum", totalCanNum);
        resultMap.put("totalUseNum", totalNum);
        resultMap.put("rechargeCount", rechargeCount);
        if(actData.get(drawRecord) != null){
            List<String> records = (List<String>)actData.get(drawRecord);
            if(records.size()>0){
                StringBuilder sb = new StringBuilder();
                for (int i = records.size()-1; i >= 0; i--) {
                    sb.append(records.get(i)).append(";");
                }
                resultMap.put("record", sb.toString());
            }
        }
        resultMap.put("serverDrawMap", serverDrawMap);
        return JsonUtils.toJSONString(resultMap);
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

        int actType = actCfg.getType();
        LuckyCat2ActivityScript.LuckyCatActivity luckyCatActivity = (LuckyCat2ActivityScript.LuckyCatActivity) actCfg.getCustomCfgMap().get(configData);
        if (luckyCatActivity == null) {
            log.error("招财猫活动自定义配置参数不存在");
            return;
        }

        if(!actCfg.isActiviting()){
            return;
        }
        if (!actCfg.isRecordTime()){
            log.info("招财猫 活动 不在记录充值时间范围");
            return;
        }

        ConcurrentHashMap<String, Object> roleActData = Manager.activityManager.deal().getRoleActivityData(player.getId(), actType);
        long alreadyRecharge = Manager.activityManager.getRoleTimeRecharge(player.getId(),actCfg);
        long rechargeCount =  Long.parseLong(roleActData.getOrDefault(roleRechargeCount, alreadyRecharge).toString());
        int totalCanNum = (int)roleActData.getOrDefault(totalCanDrawNum, 0);
        int num = (int)roleActData.getOrDefault(drawNum, 0);

        List<Integer[]> gearList = luckyCatActivity.getGear();
        int addNum = 0;
        for (int i = totalCanNum; i < gearList.size(); i++) {
            if(rechargeCount+rechargeNum>=gearList.get(i)[1]){
                addNum++;
            }
        }

        if(addNum>0){
            roleActData.put(drawNum, num+addNum);
            roleActData.put(totalCanDrawNum, totalCanNum+addNum);
        }

        roleActData.put(roleRechargeCount, rechargeCount+rechargeNum);

        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));
        Manager.activityManager.deal().sendActivityDataChange(player, actType);
    }

    @Override
    public void consumeDeal(Player player, int coinType, int consumeNum, ActivityConfig actCfg) {

    }

    @Override
    public void reload() {

        if(Manager.activityManager == null){
            return;
        }
        List<ActivityConfig> actCfgList = Manager.activityManager.deal().getActCfgListByActLogicID(ActivityType.LuckyCat2);
        for (ActivityConfig activityConfig:actCfgList) {
            if (activityConfig == null) {
                return;
            }
            Object old = activityConfig.getCustomCfgMap().get(LUCK_CAT);
            if (old == null) {
                return;
            }
            String customStr = JsonUtils.toJSONString(old);
            LuckyCat2ActivityScript newData = JsonUtils.toJavaObject(customStr, LuckyCat2ActivityScript.class);
            activityConfig.getCustomCfgMap().put(LUCK_CAT, newData);
        }
    }
    /**
     * 随机权重
     * @param params
     * @return
     */
    private LuckyCat2ActivityScript.Weight randomWeight(List<LuckyCat2ActivityScript.Weight> params) {
        TreeMap<Float, LuckyCat2ActivityScript.Weight> weightMap = new TreeMap<>();
        for (int i = 0; i < params.size(); i++) {
            LuckyCat2ActivityScript.Weight param = params.get(i);
            Float weight = weightMap.size() == 0 ? 0f : weightMap.lastKey();
            weight += param.getRate();
            weightMap.put(weight, param);
        }
        float randomWeight = RandomUtils.randomFloatValue(0f, weightMap.lastKey());
        SortedMap<Float, LuckyCat2ActivityScript.Weight> sort = weightMap.tailMap(randomWeight, false);
        return weightMap.get(sort.firstKey());
    }


    @Override
    public int getId() {
        return ScriptEnum.LuckyCat2ActivityScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    /**
     * 奖池权重
     */
    static class Weight {
        int index;  //奖池索引
        int rate;   //权重
        //region 方法

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getRate() {
            return rate;
        }

        public void setRate(int rate) {
            this.rate = rate;
        }


        //endregion
    }
    //招财猫活动数据
    static class LuckyCatActivity {
        private String client;  //前端数据

        //客户端参数：充值倍率120_140_160_180_200_220_240_260
        private Integer[] rate;
        //奖池序号对应的权重
        private Integer[] weight;
        //客户端参数：次数对应消耗元宝 100_1000_0_3_5;200_2000_2_4_5;300_3000_5_7_5  消耗元宝数_需充值数_抽奖序号范围小_抽奖序号范围大_当前档位可抽奖次数
        private List<Integer[]> gear;

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public Integer[] getRate() {
            return rate;
        }

        public void setRate(Integer[] rate) {
            this.rate = rate;
        }

        public Integer[] getWeight() {
            return weight;
        }

        public void setWeight(Integer[] weight) {
            this.weight = weight;
        }

        public List<Integer[]> getGear() {
            return gear;
        }

        public void setGear(List<Integer[]> gear) {
            this.gear = gear;
        }
    }
}
