package com.game.statestifle.script;

import com.game.player.structs.Player;

/**
 * Created by zcd on 2018/5/4.
 */
public interface IStateStifleScript {

    /**
     * 获取法宝配置表id
     */
    int getConfigIdByLevelAndStar(int level, int star);

    /**
     * 玩家上线推送消息
     */
    void online(Player player);

    /**
     * 法宝主界面
     */
    void openPanel(Player player);

    /**
     * 法宝升级升星
     */
    void levelUp(Player player, boolean oneKey);

    /**
     * 检查法宝条件
     */
    void conditionUpdate(Player player, int type);

    /**
     * 更换模型对应的技能
     */
    boolean addSkillToPlayer(Player player, int oldModelId, int newModelId);

    /**
     * 获取法宝伤害
     */
    int getAttValue(Player player);

    /**
     * 请求激活器灵
     */
    void onReqActiveSoulSpirit(Player player, int id);

    /**
     * 请求器灵晋升升级
     */
    void onReqUpPromoteLevel(Player player, int id);

    /**
     * 请求器灵进化升级
     */
    void onReqUpEvolveLevel(Player player, int id);

    /**
     * 增加器灵晋升所需进度
     *
     * @param type  1：增加经验，2：击杀boss，3：激活法宝化形
     * @param num   增加进度值
     * @param param 额外参数，击杀boss的等级
     */
    void addSoulSpiritProgress(Player player, int type, long num, int param);

    /**
     * 法宝广播消息
     */
    void boradFabaoInfo(Player player);
}
