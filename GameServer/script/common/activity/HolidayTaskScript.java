package common.activity;

import com.data.CfgManager;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_Activity_task_type_Bean;
import com.data.struct.ReadIntegerArray;

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
import game.core.json.TypeReference;
import game.core.util.IDConfigUtil;
import game.core.util.JsonUtils;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Desc TODO 300012
 * @Date 2020/10/16 10:41
 * @Auth ZUncle
 */
public class HolidayTaskScript implements IActivityScript {

    final transient Logger logger = LogManager.getLogger(HolidayTaskScript.class);

    //region 数据KEY
    final transient String Client = "client";
    final transient String ActivityData = "ActivityData";
    final transient String TaskLimit = "TaskLimit";
    //endregion

    //region 数据结构
    static class HolidayTask {
        int id;             //任务ID
        int taskType;       //任务类型
        int reach;          //进度
        List<RewardData> item;    //奖励

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getTaskType() {
            return taskType;
        }

        public void setTaskType(int taskType) {
            this.taskType = taskType;
        }

        public int getReach() {
            return reach;
        }

        public void setReach(int reach) {
            this.reach = reach;
        }

        public List<RewardData> getItem() {
            return item;
        }

        public void setItem(List<RewardData> item) {
            this.item = item;
        }
    }

    /**
     * 活动数据
     */
    static class TaskActivity {
        String client;              //前端数据
        HashMap<Integer, HolidayTask> tasks;   //任务列表

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public HashMap<Integer, HolidayTask> getTasks() {
            return tasks;
        }

        public void setTasks(HashMap<Integer, HolidayTask> tasks) {
            this.tasks = tasks;
        }
    }

    /**
     * 限购
     */
    static class TaskLimit implements ICount {
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

        int id = msg.get("id"); //领取任务奖励
        TaskActivity wordsActivity = (TaskActivity) actCfg.getCustomCfgMap().get(ActivityData);
        HolidayTask task = wordsActivity.getTasks().get(id);
        if (task == null) {
            return;
        }
        Cfg_Activity_task_type_Bean taskBean = CfgManager.getCfg_Activity_task_type_Container().getValueByKey(task.getTaskType());
        Integer[] arg = {taskBean.getFunctionVariable_id(), task.getReach()};
        int funcProgress = Manager.controlManager.deal().getFuncProgress(player, new ReadIntegerArray(arg));
        if (funcProgress < task.getReach()) {
            return;
        }
        ConcurrentHashMap<String, Object> activity = Manager.activityManager.deal().getRoleActivityData(player.getId(),actCfg.getType());
        String data = (String) activity.getOrDefault(TaskLimit, "{}");
        TaskLimit limit = JsonUtils.parseObject(data, new TypeReference<TaskLimit>() {
        });

        //已奖励领取
        long count = Manager.countManager.getCount(limit, BaseCountType.Activity, task.getId());
        if (count > 0) {
            return;
        }
        //TODO 发放道具
        List<Item> items = Item.createItems(player.getCareer(), task.getItem());

        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.HolidayActivityTaskGet, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.HolidayActivityTaskGet);
        }
        //TODO 增加一次购买记录
        Manager.countManager.addCount(limit, BaseCountType.Activity, id, Count.RefreshType.CountType_Day, 1);
        activity.put(TaskLimit, JsonUtils.toJSONString(limit));

        HashMap<String, Integer> message = new HashMap<>();
        message.put("id", id);
        message.put("isGet", 2);
        Manager.activityManager.deal().sendActivityDealMessage(player, actCfg.getType(), JsonUtils.toJSONString(message));
        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));

        logger.info("活动任务 领奖 id={} player={}", id, player);
        //记录BI
//        Manager.biManager.getScript().biActivity(player, ItemChangeReason.HolidayActivityTaskGet,actCfg.getType(), actCfg.getId());
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.HolidayTask, ItemChangeReason.HolidayActivityTaskGet);
    }

    /**
     * 解析活动自己的自定义配置
     *
     * @param actCfg
     * @param customStr
     */
    @Override
    public boolean parseCustomConfig(ActivityConfig actCfg, String customStr) {
        TaskActivity draw = JsonUtils.parseObject(customStr, TaskActivity.class);
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

        Player player = Manager.playerManager.getPlayerOnline(roleId);
        if (player == null) {
            return "";
        }

        TaskActivity activity = (TaskActivity) actCfg.getCustomCfgMap().get(ActivityData);

        ConcurrentHashMap<String, Object> activityData = Manager.activityManager.deal().getRoleActivityData(roleId, actCfg.getType());
        String data = (String) activityData.getOrDefault(TaskLimit, "{}");
        TaskLimit limit = JsonUtils.parseObject(data, new TypeReference<TaskLimit>() {
        });

        List<HashMap<String, Integer>> message = new ArrayList<>();

        for (HolidayTask task : activity.getTasks().values()) {
            Cfg_Activity_task_type_Bean taskBean = CfgManager.getCfg_Activity_task_type_Container().getValueByKey(task.getTaskType());
            Integer[] arg = {taskBean.getFunctionVariable_id(), task.getReach()};
            int funcProgress = Manager.controlManager.deal().getFuncProgress(player, new ReadIntegerArray(arg));
            int count = (int) Manager.countManager.getCount(limit, BaseCountType.Activity, task.getId());

            HashMap<String, Integer> mTask = new HashMap<>();
            mTask.put("id", task.getId());
            mTask.put("prc", funcProgress);
            mTask.put("isGet", count == 0 ? 1 : 2);
            message.add(mTask);
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

    }

    @Override
    public void reload() {
        if(Manager.activityManager == null){
            return;
        }
        List<ActivityConfig> actCfgList = Manager.activityManager.deal().getActCfgListByActLogicID(ActivityType.HolidayTask);
        for (ActivityConfig activityConfig:actCfgList) {
            if (activityConfig == null) {
                return;
            }
            Object old = activityConfig.getCustomCfgMap().get(ActivityData);
            if (old == null) {
                return;
            }
            String customStr = JsonUtils.toJSONString(old);
            TaskActivity newData = JsonUtils.parseObject(customStr, TaskActivity.class);
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
        return ScriptEnum.HolidayTaskScript;
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

        //任务队列
        HashMap<Integer, HashMap<String, Object>> tasks = new HashMap<>();
        for (int id = 1; id <= 15; id++) {
            List<RewardData> list = new ArrayList<>();
            for (int i = 1; i < 3; i++) {
                RewardData item = new RewardData();
                item.setI(2001);
                item.setN(i);
                item.setB(1);
                item.setC(9);
                list.add(item);
            }
            HashMap<String, Object> task = new HashMap<>();
            task.put("id", id);
            task.put("taskType", id);
            task.put("reach", id * 5);
            task.put("item", list);
            tasks.put(id, task);
        }

        HashMap<String, Object> client = new HashMap();
        client.put("tasks", tasks.values());    //任务列表
        client.put("coinId", 2001); //货币道具ID

        HashMap<String, Object> custom = new HashMap();
        custom.put("tasks", tasks); //任务HashMap
        custom.put("client", JsonUtils.toJSONString(client));

        ActivityConfigBean acb = new ActivityConfigBean();
        acb.setId(Manager.activityManager.deal().toActType(ActivityType.HolidayTask,0));
        acb.setType(Manager.activityManager.deal().toActType(ActivityType.HolidayTask,0));
        acb.setMinLv(0);
        acb.setMaxLv(801);
        acb.setTag((byte) 1);
        acb.setSort((byte) 1);
        acb.setName("活动任务");
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
