package com.game.activity.manager;

import com.game.activity.cmd.CloneSuccessTrigger;
import com.game.activity.cmd.CoinCostTrigger;
import com.game.activity.script.IActivityManageScript;
import com.game.activity.struct.ActivityConfig;
import com.game.count.structs.Count;
import com.game.count.structs.ICount;
import com.game.db.bean.ActivityConfigBean;
import com.game.db.bean.TagInfoBean;
import com.game.db.dao.*;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import game.core.command.CommandProcessor;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 运营活动数据管理类(活动具体数据由运营在GM后台配置)
 */
public class ActivityManager extends CommandProcessor  implements ICount {


    private static final Logger LOG = LogManager.getLogger("ActivityManager");

    final ConcurrentHashMap<String, Count> counts = new ConcurrentHashMap<>();

    public ActivityManager() {
        super(ActivityManager.class.getSimpleName());
    }

    /**
     * 获取技术数据
     *
     * @return
     */
    @Override
    public ConcurrentHashMap<String, Count> getCounts() {
        return counts;
    }

    @Override
    public void writeError(String message) {
        LOG.error("处理运营活动相关的协议时:" + message);
    }

    @Override
    public void writeError(String message, Throwable t) {
        LOG.error(message, t);
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        /**
         * 一个枚举元素就是一个实例
         */
        INSTANCE;

        ActivityManager processor;

        Singleton() {
            this.processor = new ActivityManager();
        }

        ActivityManager getProcessor() {
            return processor;
        }
    }

    public static ActivityManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    private final ActivityConfigDao configDao = new ActivityConfigDao();

    public ActivityConfigDao getConfigDao() {
        return configDao;
    }

    private final ActivityDataDao dataDao = new ActivityDataDao();

    public ActivityDataDao getDataDao() {
        return dataDao;
    }

    private final RoleActivityDataDao roleDataDao = new RoleActivityDataDao();

    public RoleActivityDataDao getRoleDataDao() {
        return roleDataDao;
    }


    public GoldChangeDao goldChangeDao = new GoldChangeDao();

    public RechargeDao rechargeDao = new RechargeDao();

    public GoldChangeDao getGoldChangeDao() {
        return goldChangeDao;
    }

    public void setGoldChangeDao(GoldChangeDao goldChangeDao) {
        this.goldChangeDao = goldChangeDao;
    }

    private TagInfoDao tagDao = new TagInfoDao();

    public TagInfoDao getTagDao() {
        return tagDao;
    }

    public void setTagDao(TagInfoDao tagDao) {
        this.tagDao = tagDao;
    }

    //===========================运营活动配置============================//

    /**
     * 运营活动幸运值
     */
    public int totalLuckyValue;

    /**
     * 预发布活动配置信息 <活动唯一类型, Map<活动ID, 活动配置信息>>
     */
    private final Map<Integer, Map<Integer, ActivityConfigBean>> preCfgMap = new ConcurrentHashMap<>();

    public Map<Integer, Map<Integer, ActivityConfigBean>> getPreCfgMap() {
        return preCfgMap;
    }

    /**
     * 活动配置信息 <活动唯一类型, 活动配置信息>
     */
    private final Map<Integer, ActivityConfig> actCfgMap = new ConcurrentHashMap<>();

    public Map<Integer, ActivityConfig> getActCfgMap() {
        return actCfgMap;
    }

    //============================运营活动数据=============================//
    /**
     * 活动数据<活动唯一类型, <活动数据字段, 数据>>
     * 活动整体的数据（例如：团购活动中的全服购买人数）
     */
    private final ConcurrentHashMap<Integer, ConcurrentHashMap<String, Object>> actDatas = new ConcurrentHashMap<>();
    /**
     * 角色相关活动数据<角色ID, <活动唯一类型, <活动数据字段, 数据>>>
     */
    private final ConcurrentHashMap<Long, ConcurrentHashMap<Integer, ConcurrentHashMap<String, Object>>> roleActDatas = new ConcurrentHashMap<>();

    private final List<TagInfoBean> tagInfoList = new ArrayList<>();

    public ConcurrentHashMap<Integer, ConcurrentHashMap<String, Object>> getActDatas() {
        return actDatas;
    }

    public ConcurrentHashMap<Long, ConcurrentHashMap<Integer, ConcurrentHashMap<String, Object>>> getRoleActDatas() {
        return roleActDatas;
    }

    public List<TagInfoBean> getTagInfoList() {
        return tagInfoList;
    }

    public int getTotalLuckyValue() {
        return totalLuckyValue;
    }

    public void setTotalLuckyValue(int totalLuckyValue) {
        this.totalLuckyValue = totalLuckyValue;
    }

    /**
     * 运营活动管理脚本
     */
    public IActivityManageScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.ActivityScriptBaseScript);
        if (is instanceof IActivityManageScript) {
            return (IActivityManageScript) is;
        }
        return null;
    }

    public boolean checkCareer(Player player, int career){
        if (career == 9){ //通用
            return true;
        }
        if (player.getCareer() == career){
            return true;
        }
        return false;
    }


    /**
     * 副本完成成功，去触发活动奖励
     * @param player
     * @param cloneId
     */
    public void cloneDropTrigger(Player player, int cloneId) {
        addCommand(new CloneSuccessTrigger(player, cloneId));
    }

    /**
     * 消耗货币，去触发活动更新
     * @param player
     * @param coinType
     * @param coin
     */
    public void coinCostTrigger(Player player, int coinType , int coin){
        addCommand(new CoinCostTrigger(player, coinType,coin));
    }


    /**
     * 获取提前充值抓取数据
     * @param roleId
     * @param actCfg
     * @return
     */
    public long getRoleTimeRecharge(long roleId, ActivityConfig actCfg){
        if (actCfg.getStartRecordTime() >= actCfg.getBeginTime()){
            return 0;
        }
        long endTime = actCfg.getStartRecordTime() + (actCfg.getBeginTime()-actCfg.getStartRecordTime());
        return rechargeDao.getRoleTimeRecharge(roleId ,actCfg.getStartRecordTime(),endTime);
    }

    /**
     *获取提前消费的数据
     * @param roleId
     * @param actCfg
     * @return
     */
    public int getRoleTimeConsumption(long roleId,ActivityConfig actCfg){
        if (actCfg.getStartRecordTime() >= actCfg.getBeginTime()){
            return 0;
        }
        long endTime = actCfg.getStartRecordTime() + (actCfg.getBeginTime()-actCfg.getStartRecordTime());
        return goldChangeDao.getRoleTimeConsumption(roleId,actCfg.getStartRecordTime(),endTime);
    }
}
