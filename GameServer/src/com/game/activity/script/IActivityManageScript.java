package com.game.activity.script;

import com.game.activity.struct.ActivityConfig;
import com.game.db.bean.ActivityConfigBean;
import com.game.player.structs.Player;
import game.core.script.IRunScript;
import io.netty.channel.Channel;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 运营活动接口基础类
 */
public interface IActivityManageScript extends IRunScript {
    /**
     * 请求活动信息
     * @param player
     * @param actType 活动类型
     */
    void onReqActivity(Player player, int actType);

    /**
     * 请求操作运营活动
     */
    void onReqActivityDeal(Player player, int actType, String dataStr);

    /**
     * 返回操作运营活动
     */
    void sendActivityDealMessage(Player player, int actType, String dataStr);

    /**
     * 登录加载数据
     * @param player 玩家
     */
    void playerOnline(Player player);

    /**
     * 获取角色活动数据
     */
    ConcurrentHashMap<String, Object> getRoleActivityData(long roleId, int actType);

    /**
     * 获取角色活动数据
     */
    ConcurrentHashMap<String, Object> getActivityData(int actType);

    /**
     * 注册活动到内存
     * @param actBean 活动配置实体
     * @return boolean
     */
    boolean registerActivityBean(ActivityConfigBean actBean);
    /**
     * 发送活动配置改变消息
     * @param player
     */
    void sendActivityConfigChange(Player player, ActivityConfig actCfg);
    /**
     * 发送活动数据改变消息
     */
    void sendActivityDataChange(Player player, int actType);
    /**
     * 后台设置活动
     * @param actBean 后台活动信息
     */
    boolean w2gSyncActivity(ActivityConfigBean actBean);
    /**
     * 后台批量发布活动
     * @param activityBeans 活动信息列表
     */
    List<Integer> w2gBatchSyncActivity(List<ActivityConfigBean> activityBeans);

    /**
     * 批量删除活动次出不Send到后台
     */
    void batchDelActivity(int actType);

    /**
     * 后台删除设置活动
     * @param b2wSession 会话
     */
    void w2gSyncDeleteActivity(int actType, Channel b2wSession);

    void gmSetActivity(Map<String, Object> cmdMap);

    /**
     * 检查活动开放等级
     * @param level
     * @param actCfg
     * @return
     */
    boolean checkLevel(int level, ActivityConfig actCfg);
    /**
     * 检查是否有活动开启或关闭
     */
    void checkAllActivity();
    /**
     * 加载活动数据
     */
    void load();
    /**
     * 加载活动标签数据
     */
    int loadTagInfo();
    /**
     * 零点玩家活动数据处理
     * @param player 玩家
     */
    void zeroClockPlayerDeal(Player player);

    /**
     * 五点玩家活动数据处理
     * @param player 玩家
     */
    void fiveClockPlayerDeal(Player player);

    /**
     * 零点处理运营活动数据
     */
    void zeroClockDeal();

    /**
     * 五点处理运营活动数据
     */
    void fiveClockDeal();


    void everyHourDeal();

    /**
     * 充值后回调处理
     */
    void rechargeDeal(Player player, int getGoodsCfgId, int rechargeNum);

    /**
     * 活动消耗
     */
    void consumeDeal(Player player, int coinType, int consumeNum);

    /**
     * 获取参加活动的角色ID
     * @param actType
     * @return
     */
    List<Long> getRoleIdList(int actType);

    /**
     * 获取活动掉落
     * @param player
     * @return
     */
    void bossDrop(Player player, int bossId);

    /**
     * 活动宝箱掉落
     * @param player
     * @param boxId
     * @return
     */
    boolean boxDrop(Player player, int boxId);

    /**
     * 副本活动掉落
     * @param player
     * @param cloneId
     * @return
     */
    boolean cloneDrop(Player player, int cloneId);

    /**
     * 清理活动
     */
    void cleanActivity(int actType);

    /**
     * 发送运营活动标签库信息
     */
    void sendActivityTagInfo(Player player);
    /**
     * 发送运营活动标签库信息
     */
    void sendActivityTagInfo(Player player, String data);

    IActivityScript getScript(int actType);

    /**
     * 根据活动类型获取同大类型活动的配置
     * @param actType
     * @return
     */
    List<ActivityConfig> getActCfgListByActLogicID(int actType);

    /**
     * 把活动类型转换为活动逻辑ID
     * @param actType 活动唯一类型ID
     * @return
     */
    int toActLogicID(int actType);

    /**
     * 通过活动逻辑ID和节日ID,获取活动类型
     * @param logicID
     * @param festivalID
     * @return
     */
    int toActType(int logicID,int festivalID);

    void saveActConfig(ActivityConfig actCfg, int state);

    void delActConfig(int type);

    void saveActData(int type, ConcurrentHashMap<String, Object> actDataMap);

    void delActData(int type);

    void saveRoleActData(long roleId, ConcurrentHashMap<Integer, ConcurrentHashMap<String, Object>> actDataMap);
}

