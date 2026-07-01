package com.game.bi.inter;

import com.game.db.bean.ShopBean;
import com.game.guild.structs.Guild;
import com.game.player.structs.Player;
import game.core.script.IScript;

import java.sql.SQLException;
import java.util.List;

/**
 * @explain: desc
 * @time Created on 2020/3/26 20:41.
 * @author: tc
 */
public interface IBi4399Script extends IScript {

    /**
     * 4399字典表创建
     */
    void createDictTo4399() throws SQLException;

	/**
	 * 4399聊天信息监控
	 * @param player
	 * @param channelId
	 * @param toPlatuserId
	 * @param msg
	 * @param params
	 */
    void chatInfoTo4399(Player player, int channelId, String toPlatuserId, String msg, List<String> params);

	/**
	 * 创建角色
	 * @param player
	 */
	void createPlayer(Player player);

	/**
	 * 更新角色
	 * @param player
	 */
	void updatePlayer(Player player);

    /**
     * 创建仙盟
     */
	void createGuild(Player player, Guild guild);

    /**
     * 更新仙盟
     */
	void updateGuild(Guild guild);

    /**
     * 聊天日志
     *
     * @param channel   频道
     * @param msg       内容
     * @param type      是否语音 0语音，1文字
     * @param target_role_id    目标id
     */
    void chatBiTo4399(Player player, int channel, String msg, int type, long target_role_id);

    /**
     * 货币日志
     *
     * @param money_type    货币类型
     * @param amount        数量
     * @param money_remain  剩余数量
     * @param opt           1增加/-1减少
     * @param action_1      行为
     */
    void goldBiTo4399(Player player, int money_type, int amount, int money_remain, int opt, int action_1);

    /**
     * 登录日志
     */
    void loginBiTo4399(Player player);

    /**
     * 在线人数
     */
    void onlineBiTo4399();

    /**
     * 充值日志
     *
     * @param pay_type      充值类型 0测试订单/1正式订单
     * @param order_id      订单号
     * @param pay_money     充值金额
     * @param pay_gold      获得元宝数
     */
    void payBiTo4399(Player player, int pay_type, String order_id, float pay_money, int pay_gold);

    /**
     * 退出日志
     *
     * @param reason        退出原因
     * @param msg           特殊说明
     */
    void quitBiTo4399(Player player, int reason, String msg);

    /**
     * 角色创建日志
     */
    void roleBiTo4399(Player player);

	/**
	 * 角色升级
	 */
	void levelupBiTo4399(Player player,int oldLevel,long oldExp,long curExp);

    /**
     * 商城购买
     *
     * @param num   购买数量
     * @param money 消耗货币数量
     */
    void shopBuyTo4399(Player player, ShopBean shop, int num, int money);

	/**
	 * 装备数据
	 */
//	void itemBiTo4399(Player player, int opt, int action_id, int item_id, int item_num);

	void itemBiTo4399(Player player, int action_id, int item_id, int oldNum, int afterNum);

}
