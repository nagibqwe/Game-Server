package com.game.marriage.script;

import com.game.map.structs.MapObject;
import com.game.marriage.struct.Marriage;
import com.game.marriage.struct.MarryChild;
import com.game.marriage.struct.MarryTask;
import com.game.player.structs.Player;
import com.game.skill.structs.Skill;
import game.core.script.IScript;

import java.util.List;

/**
 * @Description
 * @auther admin
 * @create 2020-06-02 16:07
 */
public interface IMarriageScript extends IScript {

    /**
     * 获取婚宴场景
     * @return
     */
    MapObject  getWeddingScene();

    /**
     * 是否夫妻
     * @param player
     * @param target
     * @return
     */
    boolean isCouple(Player player, Player target);

    /**
     *  获取仙娃技能
     * @param player
     * @return
     */
    List<Skill> sumAllChildSkill(Player player);
    /**
     * 仙娃改名
     *
     * @param player
     * @param name
     */
    void reqMarryChildChangeName(Player player, int childId, String name);

    /**
     * 计算仙娃 配置表ID
     * @param child
     * @return
     */
    int calcChildLevelId(MarryChild child);
    /**
     * 升级仙娃
     *
     * @param player
     * @param itemId
     * @param oneKey
     */
    void reqUpgradeMarryChild(Player player, int childId, int itemId, boolean oneKey);

    /**
     * 请求激活仙娃
     *
     * @param player
     */
    void reqOpenMarryChild(Player player, int childId);

    /**
     * 拒绝购买仙匣
     *
     * @param player
     */
    void reqRefuseBuyMarryBox(Player player);

    /**
     * 领取仙匣奖励
     *
     * @param player
     */
    void reqMarryBoxReward(Player player, int type);

    /**
     * 请求 伴侣购买仙匣
     *
     * @param player
     */
    void reqCallBuyMarryBox(Player player);

    /**
     * 购买仙匣
     *
     * @param player
     */
    void reqBuyMarryBox(Player player);

    /**
     * 发送仙匣数据
     * @param player
     */
    void sendMarryBox(Player player);
    /**
     * 获取心锁等级
     *
     * @param player
     * @return
     */
    int calcMarryLockLevelId(Player player);

    /**
     * 仙缘心锁
     *
     * @param player
     * @param oneKey
     */
    void reqUpGradeMarryLock(Player player, int itemId, boolean oneKey);

    /**
     * 获取婚礼称号
     *
     * @param player
     * @param id
     */
    void reqRewardTitle(Player player, int id);

    /**
     * 请求结婚
     *
     * @param player
     * @param roleId
     * @param type
     */
    void reqMarriage(Player player, long roleId, int type, boolean isNotice, String notice);

    /**
     * 同意或者拒绝
     *
     * @param player
     * @param roleId
     * @param isAgree
     */
    void reqBeMarriage(Player player, long roleId, boolean isAgree);

    /**
     * 请求预约婚礼
     *
     * @param player
     * @param id
     */
    void reqSelectMarriage(Player player, int id);

    /**
     * 请求离婚
     */
    void reqDivorce(Player player, int type);

    /**
     * 请求确认离婚
     *
     * @param player
     */
    void reqAffirmDivorce(Player player, int opt);

    /**
     * 请求婚姻信息
     *
     * @param player
     */
    void reqMarriageData(Player player);

    /**
     * 请求索要请帖
     *
     * @param player
     */
    void reqDemandInvit(Player player, int key);

    /**
     * 请求邀请
     *
     * @param player
     * @param roleId
     * @param type
     */
    void reqInvit(Player player, long roleId, int type);

    /**
     * 同意索要请帖
     *
     * @param player
     * @param roleId
     */
    void reqAgreeInvit(Player player, long roleId, boolean isAgree);

    /**
     * 购买邀请上限
     *
     * @param player
     */
    void reqPurInvitNum(Player player);

    /**
     * 离婚
     *
     * @param player
     * @param bePlayer
     * @param marriage
     * @param type
     */
    void divorce(Player player, Player bePlayer, Marriage marriage, int type);

    /**
     * 获取婚宴对象
     *
     * @param player
     * @param marriage
     */
    Player getMarraige(Player player, Marriage marriage);

    /**
     * 上线
     *
     * @param player
     */
    void online(Player player);

    /**
     * 婚宴tick时间
     */
    void tick();

    /**
     * 清理档次婚宴
     */
    void clear();


    /**
     * 请求发送弹幕
     *
     * @param player
     * @param context
     */
    void reqMarrySendBulletScreen(Player player, String context);

    /**
     * 赠送道具
     *
     * @param player
     * @param index
     * @param roleID
     */
    void reqMarrySendItem(Player player, int index, long roleID);


    /**
     * 使用道具
     *
     * @param player
     * @param id
     */
    void reqMarryUseItem(Player player, int id);

    /**
     * 执行道具效果
     * @param player
     * @param id
     */
    void doItemEffect(Player player, int id);

    /**
     * 购买道具
     *
     * @param player
     * @param id
     * @param num
     */
    void reqMarryCopyBuy(Player player, int id, int num);


    /**
     * 请求婚礼列表
     *
     * @param player
     */
    void reqMarryBlessList(Player player);

    /**
     * 仙娃出战，召回
     * @param player
     * @param childId
     * @param opt
     */
    void reqChildCall(Player player, int childId, int opt);

    /**
     * 任务
     * @param player
     * @param task
     * @param param
     */
    void actionTask(Player player, MarryTask task, int param);

    /**
     * 请求仙缘任务列表
     */
    void sendMarryTask(Player player);

    /**
     * 请求领取任务奖励
     * @param player
     * @param taskId
     */
    void reqMarryTaskReward(Player player, int taskId);

    /**
     * 购买热度值
     * @param player
     */
    void reqMarryCopyBuyHot(Player player);

    /**
     * 婚姻副本签到请求
     * @param player
     */
    void reqMarryCopySign(Player player);

    /**
     * 送祝福
     * @param player
     * @param marryId
     */
    void reqMarryBless(Player player, long marryId);
}
