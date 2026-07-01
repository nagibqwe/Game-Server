package com.game.bi.inter;

import com.game.bi.struct.BIActiityTypeEnum;
import com.game.player.structs.Player;
import game.core.script.IScript;
import game.message.BIMessage;

import java.math.BigInteger;
import java.util.List;

/**
 * @explain: desc
 * @time Created on 2019/12/30 15:10.
 * @author: tc
 */
public interface IBiScript extends IScript {
    /**
     * init
     */
    void init();

    /**
     * 保存bi数据
     */
    void save();

    /**
     * biBase
     *
     * @param player
     */
    void biUpdateBase(Player player);

    /**
     * fixme example
     *
     * @param player
     */
    void example(Player player);

    /**
     * 选择角色进入游戏时发送设备相关信息
     * @param player
     * @param biDevice
     */
    void onReqBiDevice(Player player, BIMessage.ReqBiDevice biDevice);

    /**
     * 具体的BI数据，内部数据格式由对接人协商负责
     *
     * @param player
     * @param bi
     */
    void onReqBi(Player player, BIMessage.ReqBi bi);

    /**
     * 客户端请求数据
     *
     * @param player
     * @param uiBi
     */
    void onReqUiBi(Player player, BIMessage.ReqUiBi uiBi);

    /**
     * 玩家上线，上线原因，登录还是跨天
     *
     * @param player
     * @param status 普通登录=1，掉线重连=2 ，跨天登记=3
     * @param is_retrieve 是否有找回资源资格,0:没有,1:有
     */
    void biLogin(Player player, int status, int is_retrieve);

    /**
     * 下线
     * @param player
     */
    void biLogout(Player player, int reason, int is_retrieve);

    /**
     * 在线人数
     *
     * @param playerList
     */
    void biOlineNum(List<Player> playerList);

    /**
     * 充值
     *
     * @param player
     * @param goodId
     * @param orderId
     * @param goodsName     商品名
     * @param orderAmount   实际支付金额(RMB,单位分)
     * @param status        订单状态：未支付=1，支付前取消=2，支付成功=3，发货失败=4， 确认发货订单完成=5
     * @param diamondAmount 订单完成后立即获得的1级货币数量，不含后续每日领取类奖励
     * @param src           充值来源
     * @param moneyType     充值的货币类型
     */
    void biPay(Player player, int goodId, String orderId, String goodsName, int orderAmount, int status, int diamondAmount, int src, String moneyType);

    /**
     * 商城
     *
     * @param player
     * @param mall_type  用于区分钻石/金币/成就等商城类型
     * @param product_type  商品类型
     * @param item_id    商城购买的道具id
     * @param item_num   单次购买的道具数量
     * @param money_type 支付货币类型
     * @param amount     支付货币数量
     * @param price      商城出售的单个物品原价
     * @param location   物品上架的位置编号
     */
    void biMall(Player player, int product_type, int mall_type, int item_id, int item_num, int money_type, int amount, int price, int location);


    /**
     * 婚姻
     * @param player
     * @param targetId
     * @param targetName
     * @param opt 操作类型，1低级结婚/2高级结婚/3豪华婚礼/4申诉离婚/5强制离婚
     */
    void biMarry(Player player, long targetId, String targetName, int opt);

    /**
     * 剑阁关卡 快速收益
     * @param player
     * @param stage_id 关卡ID
     */
    void biRoomResult(Player player, int stage_id);

    /**
     * 抽奖
     * @param player
     * @param draw_id
     * @param act_type 1:单抽，2:10连抽，3:50连抽
     */
    void biDraw(Player player, int draw_id, int act_type);

    /**
     * 物品
     *
     * @param player
     * @param uid         道具唯一ID
     * @param item_type   道具类型
     * @param item_level  道具品质/颜色
     * @param item_id     道具id
     * @param item_name   道具名
     * @param item_num    变更数量
     * @param after_num   变更后的剩余数量
     * @param op_type     变化途径对应的系统
     * @param op_id       全局唯一的行为id
     * @param change_type 物品增加or减少 0减少1增加
     * @param scene       场景ID
     */
    void biItem(Player player, long uid, int item_type, int item_level, int item_id, String item_name, long item_num, long after_num, int op_type, long op_id, int change_type, int scene);

    /**
     * @param player
     * @param ip          用户登陆ip
     * @param old_level   升级前的等级
     * @param after_level 升级后的等级
     * @param exp_num     增加经验
     * @param after_exp   总经验
     * @param op_type     变化途径对应的系统
     * @param op_id       全局唯一的行为id
     */
    void biExp(Player player, String ip, int old_level, int after_level, long exp_num, long after_exp, int op_type, long op_id);

    /**
     * @param player
     * @param ip          用户登陆ip
     * @param money_type  货币类型
     * @param amount      变化数量
     * @param before_num  变化之前的数量
     * @param after_num   总数量
     * @param op_type     变化途径对应的系统
     * @param op_id       全局唯一的行为id
     * @param change_type 1增加  0减少
     * @param scene       场景ID
     */
    void biMoney(Player player, String ip, int money_type, int amount, long before_num, long after_num, int op_type, long op_id, int change_type, int scene);

    /**
     * @param player
     * @param money_type  货币类型
     * @param amount      变化数量
     * @param after_num   总数量
     * @param op_type     变化途径对应的系统
     * @param op_id       全局唯一的行为id
     * @param change_type 1增加  0减少
     */
    void biResource(Player player, int change_type, int money_type, BigInteger amount, BigInteger before_num, BigInteger after_num, int before_level, int afterLevel, int op_type, long op_id);

    /**
     * 创建角色
     *
     * @param player
     * @param device
     * @param career_id
     * @param gender
     */
    void biCreate(Player player, BIMessage.Device device, int career_id, int gender, String ip);

    /**
     * 公会成员
     *
     * @param player
     * @param guild_id
     * @param guild_level
     * @param change_type 1创建2加入3主动解散工会4工会解散被踢出5被管理员踢出6主动退出7人数不足自动解散8会长丢失解散公会9会长不存在解散公会
     * @param status      1成员2长老3副会长4会长
     * @param member
     */
    void biGuildMember(Player player, long guild_id, int guild_level, int change_type, int status, int member, long guildPower);

    /**
     * 公会升级
     *
     * @param player
     * @param guild_id           行会ID
     * @param guild_name         公会名
     * @param level_type         升级类型 1仙盟大厅2仙盟商店3仙盟驻地
     * @param before_guild_level 行会升级前等级
     * @param later_guild_level  行会升级后等级
     */
    void biGuildUpgrade(Player player, long guild_id, String guild_name, int level_type, int before_guild_level, int later_guild_level);

    /**
     * 副本
     *
     * @param player
     * @param instance_id     副本id
     * @param instance_type   副本类型
     * @param instance_name   副本名字
     * @param instance_subName   副本子名字
     * @param instance_level  副本等级
     * @param instance_status 副本状态 0进入 1退出 2扫荡
     * @param instance_result 副本结果 1成功 2失败 3超时 4放弃
     * @param instance_diff   副本难度
     * @param floor   副本层
     */
    void biInstance(Player player, int instance_id, int instance_type, String instance_name, String instance_subName, int instance_level,
                    int instance_status, Integer instance_result, String instance_diff, String floor);

    /**
     * 聊天
     *
     * @param chat_source 聊天频道 0:世界/1:公会/2:队伍/3:私聊
     * @param sys_flag    是否系统消息
     * @param chat_txt    聊天内容
     * @param obj_account_id   聊天对象账号
     * @param obj_role_id   聊天对象角色
     */
    void biChat(Player player, int chat_source, int sys_flag, String chat_txt, long obj_account_id, long obj_role_id, String object_name, String object_ip, String object_device_id);


    void biRole_info(Player player);

    /**
     * 任务
     *
     * @param player
     * @param task_id      任务id
     * @param task_type    任务类型
     * @param task_status  操作类型 1:接收任务 2:完成任务 3:放弃任务 4:完成并领取奖励
     * @param task_name    任务名字
     * @param task_subtype 任务子类型
     */
    void biTask(Player player, int task_id, int task_type, int task_status, String task_name, int task_subtype, int task_level);

    /**
     * 场景（世界地图）进出记录
     *
     * @param player
     * @param scene      场景ID
     * @param scene_name 场景名称
     * @param type       操作类型 1上线进入/2下线离开/3进入场景/4离开场景
     * @param stay_time  场景停留时间 离线时记录停留时间
     * @param stay_time  场景停留时间 离线时记录停留时间
     */
    void biScene(Player player, int scene, String scene_name, int type, int stay_time);

    /**
     * 成长日志
     *
     * @param player
     * @param grow_type  成长类型 1坐骑2造化3法宝4符咒5识海6技能7宠物8心法9坐骑御魂10造化阵魂11宠物御魂12坐骑化形13造化化形14法宝化形15器灵16心法技能17魂兽助战
     *                   仙娃18 心锁19 剑灵20
     * @param act_type   操作类型  1激活2升级3培养
     * @param grow_id    成长id 技能id，坐骑id，仙羽类型
     */
    void biGrow(Player player, int grow_type, int grow_subtype, int act_type, int before_target_Id, int after_target_id, int grow_id);

    /**
     * BOSS击杀
     *
     * @param player
     * @param instance_id  副本id
     * @param instance_name 副本名字
     * @param instance_type 副本类型
     * @param instance_level 副本等级
     * @param boss_type boss_type 1普通怪2精英怪3BOSS
     * @param boss_id   BOSSid
     * @param boss_name BOSS名称
     * @param dps       累计伤害
     * @param dps_rank  伤害排名
     * @param guild_id  公会ID
     * @param sub_type  boss类型(1，世界BOSS；2，BOSS之家；3，套装BOSS)
     */
    void biBoss(Player player, Integer instance_id, String instance_name, Integer instance_type, Integer instance_level, int boss_type, int boss_id, String boss_name, int level, long dps, int dps_rank, long guild_id, int sub_type, int guild_rank);

    /**
     * 死亡记录
     *
     * @param player
     * @param instance_id  副本id
     * @param instance_name 副本名字
     * @param instance_type 副本类型
     * @param instance_level 副本等级
     * @param killer_type   击杀者类型 1玩家 2NPC
     * @param killer_id     击杀者ID 如PVP击杀记录玩家id，如PVE击杀记录NPCid
     * @param killer_name   击杀者角色名 如PVP击杀记录玩家角色名，如PVE击杀记录NPC名字
     * @param killer_level  击杀者等级
     * @param killer_combat 击杀者战力
     * @param res_type      复活方式 0GM目标复活1复活点2原地3自动
     */
    void biDeath(Player player, Integer instance_id, String instance_name, Integer instance_type, Integer instance_level, int killer_type, long killer_id, String killer_name, int killer_level, long killer_combat, int res_type);

    /**
     * 装备变化
     *
     * @param player
     * @param act_type   操作类型 1穿戴2合成3强化4洗练5宝石镶嵌6宝石升级7套装激活
     * @param equip_type 装备类型 1角色装备2仙甲3圣装4神兽5灵体3阶~15灵体13阶
     * @param part       部位
     * @param item_id    装备id
     * @param item_name  装备名称
     * @param star       星级
     * @param lev        等阶
     * @param col        颜色
     * @param str        强化等级
     * @param bind       是否绑定
     * @param suit       套装类型 0无 1传世 2洞虚 3破天
     * @param gem_num    宝石孔位
     * @param gem_pos    宝石位置 从0开始
     * @param gem_set    装备宝石数量
     * @param gem_rating 宝石评分
     * @param gem_id     宝石ID
     */
    void biEquip(Player player, int act_type, int equip_type, int part, int item_id, String item_name, int star, int lev, int col, int str, int bind, int suit, int gem_num, int gem_pos, int gem_set, int gem_rating, int gem_id);

    /**
     * 装备变化
     *
     * @param player
     * @param act_type   操作类型 1穿戴2合成3强化4洗练5宝石镶嵌6宝石升级7套装激活
     * @param equip_type 装备类型 1角色装备2仙甲3圣装4神兽5灵体3阶~15灵体13阶
     * @param part       部位
     * @param item_id    装备id
     * @param item_name  装备名称
     * @param star       星级
     * @param lev        等阶
     * @param col        颜色
     * @param str        强化等级
     * @param bind       是否绑定
     * @param suit       套装类型 0无 1传世 2洞虚 3破天
     * @param gem_num    宝石孔位
     * @param gem_pos    宝石位置 从0开始
     * @param gem_set    装备宝石数量
     * @param gem_rating 宝石评分
     * @param gem_id     宝石ID
     * @param lock_num   在装备发生变化时锁定属性的数量
     * @param lock_type  类型 0:无锁定 1:材料锁定 2:货币锁定
     * @param change_score 变更总评分值
     * @param before_score 变更前总评分值
     * @param after_score  变更后总评分值
     * @param refine_level 精炼等级
     */
    void biEquip(Player player, int act_type, int equip_type, int part, int item_id, String item_name, int star, int lev, int col, int str, int bind, int suit, int gem_num, int gem_pos, int gem_set, int gem_rating, int gem_id,
                 Integer lock_num,
                 Integer lock_type,
                 Integer change_score,
                 Integer before_score,
                 Integer after_score,
                 Integer refine_level);

    /**
     * 境界任务/成就/称号
     *
     * @param player
     * @param up_type  类型 1称号 2目标ID
     * @param act_type 操作类型 0未达成 1达成 2领取 3使用
     * @param stage    当前境界进度
     * @param taskId   任务id 如类型为成就/称号则记录 对应成就/称号id
     * @param progress 完成进度	1/3	如类型为境界任务则记录完成进度，如类型为其他则为空
     */
    void biRealm(Player player, int up_type, int act_type, int stage, int taskId, String progress);

    void biActivity(Player player, int reason, int activity_type, int id);

    void biActivity(Player player, BIActiityTypeEnum activity, int reward);

    void biActivity(Player player, BIActiityTypeEnum activity, int rewardGroup, int round, int level);

    void biActivity(Player player, BIActiityTypeEnum activity, int rewardGroup, int reward);

    void biActivity(Player player, BIActiityTypeEnum activity, int rewardGroup, int reward, String rewardName);

    void biActivity(Player player, BIActiityTypeEnum activity, int subId, String subName, int rewardGroup, int round, int level);

    void biActivity(Player player, BIActiityTypeEnum activity, int subId, String subName, int rewardGroup, int reward, String rewardName, int round, int level);

    /**
     * 拍卖行
     *
     * @param role_id     角色id/行会id
     * @param role_level  角色等级/公会等级
     * @param role_name   角色名/行会名
     * @param role_type   1个人 2仙盟
     * @param target_type 目标类型 1:个人 2:仙盟 3:活动
     * @param op_type     操作类型 1上架2竞拍3竞拍成交4一口价成交5超时下架6转至世界拍卖7主动下架
     * @param item_id     物品id
     * @param item_type   物品类型
     * @param item_colour 物品颜色
     * @param item_lev    物品品阶
     * @param item_star   物品星级
     * @param item_no     上架物品唯一id
     * @param item_name   物品名称
     * @param item_num    物品数量
     * @param bid_price   竞标价
     * @param fixed_price 一口价
     * @param guild_id    公会ID
     */
    void biAuction(long role_id, int role_level, String role_name, long targetId, int role_type, int op_type, int item_id, int item_type, int item_colour, int item_lev, int item_star, long item_no, String item_name, int item_num, int bid_price, int fixed_price, long guild_id);

    /**
     * 排行榜
     *
     * @param role_id    角色id/行会id
     * @param role_name  角色名/行会名
     * @param rank_type  排行类型
     * @param ranking    排行名次
     * @param rank_value 排行值
     */
    void biRank(int targetType, long role_id, String role_name, int rank_type, int ranking, String rank_value);

    /**
     * 公会战
     *
     * @param guild_id    公会ID
     * @param guild_name  公会名
     * @param guild_level 公会等级
     * @param rate_level  公会战评级等级 1天仙仙盟2神仙仙盟3金仙仙盟4鬼仙仙盟
     * @param type        战场类型 1每轮结束 2全部结束
     * @param camp        战场阵营 0攻方 1守方
     * @param round       战场轮数
     */
    void biGuildWar(long guild_id, String guild_name, int guild_level, int rate_level, int type, int camp, int round);

    /**
     * 载具
     *
     * @param scene   场景ID
     * @param buff_id 载具buffId
     */
    void biVehicle(Player player, int scene, int buff_id);

    /**
     * 事件变化
     *
     * @param player
     * @param event_type   事件类型 1天墟战场怒气2神兽岛兽血水晶3神兽岛兽灵水晶
     * @param change_value 当前境界进度
     * @param after_value  任务id 如类型为成就/称号则记录 对应成就/称号id
     */
    void biEvent(Player player, int event_type, int change_value, int after_value);

    /**
     * 公会贡献
     *
     * @param guild_id    公会ID
     * @param guild_name  公会名
     * @param guild_level 公会等级
     * @param money       公会贡献
     */
    void biGuildMoney(long guild_id, String guild_name, int guild_level, long money);

    /**
     * 前端 bi 数据
     * @param player
     * @param id
     */
    void biClientClick(Player player, long id);

    /**
     * 服务器操作
     * @param type 游戏服务器操作类型:1=开服(由备服开启) 2=维护开服 3=维护关服 101=进程启动 102=进程关闭 103 开服时间变化
     */
    void BiServer_op(int type);

    /**
     * 签到
     * @param player 玩家
     * @param type 类型
     * @param count 签到数
     * @param totalCount 全服总签到数
     */
    void biSign(Player player, int type, int rewardType, int count, int totalCount);

    /**
     * 諸界远征
     * @param expedition_id  远征轮次唯一id
     * @param city_id        城市id
     * @param city_type      城市类型 1: 2:
     * @param add_score      角色个人增加占领值
     * @param server_score   区服占领值
     * @param city_owner_id  城市所属服务器id
     */
    public void biWorldExpedition(
            Integer expedition_id,
            Integer city_id,
            Integer city_type,
            Integer add_score,
            Integer server_score,
            Integer city_owner_id) ;

}
