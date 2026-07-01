/**
 * Auto generated, do not edit it
 *
 * item配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Item_Bean{
    /**
     * 物品ID
     */
    private final int id;
    /**
     * 物品ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 物品显示名字
     */
    private final String name;
    /**
     * 物品显示名字
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 掉落模型ID （client ignore）
     */
    private final int drop_model;
    /**
     * 掉落模型ID （client ignore）
     * @return
     */
    public final int getDrop_model(){
        return drop_model;
    }
    /**
     * 物品类型(1：货币；2：装备；3：效果道具； 4：材料；5：宝石；6：礼包；7：碎片；8：礼物；9：普通物品；10：特殊物品；11.称号；12，神兽装备；13神兽水晶；14，圣装；15，多选一宝箱 16 转职道具;17 转职洗髓普通道具18VIP经验19仙甲20婚姻礼炮21灵魄 22=仙甲 23=宠物装备 24=魂印 25=坐骑脉纹 26=魔魂装备 27=魔魂碎片 28=幻装 29=幻装碎片 30=家具物品)
     */
    private final int type;
    /**
     * 物品类型(1：货币；2：装备；3：效果道具； 4：材料；5：宝石；6：礼包；7：碎片；8：礼物；9：普通物品；10：特殊物品；11.称号；12，神兽装备；13神兽水晶；14，圣装；15，多选一宝箱 16 转职道具;17 转职洗髓普通道具18VIP经验19仙甲20婚姻礼炮21灵魄 22=仙甲 23=宠物装备 24=魂印 25=坐骑脉纹 26=魔魂装备 27=魔魂碎片 28=幻装 29=幻装碎片 30=家具物品)
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 用于交易行小类型的分类，对应AuctionMenu表中
     */
    private final int trade_type;
    /**
     * 用于交易行小类型的分类，对应AuctionMenu表中
     * @return
     */
    public final int getTrade_type(){
        return trade_type;
    }
    /**
     * 交易中默认的推荐价格，不填表示不能交易
     */
    private final int trade_recom;
    /**
     * 交易中默认的推荐价格，不填表示不能交易
     * @return
     */
    public final int getTrade_recom(){
        return trade_recom;
    }
    /**
     * 按钮类型（1，穿戴；2，使用；3，批量使用；4，获取途径；5，出售；6，摆摊；7，合成；8，赠送）(@_@) 
     */
    private final ReadIntegerArray button_type;
    /**
     * 按钮类型（1，穿戴；2，使用；3，批量使用；4，获取途径；5，出售；6，摆摊；7，合成；8，赠送）(@_@) 
     * @return
     */
    public final ReadIntegerArray getButton_type(){
        return button_type;
    }
    /**
     * 物品天生颜色（1.白 2.绿 3.蓝 4.紫 5.橙 6.金 7.红,8粉,9暗金.10幻彩）
     */
    private final int color;
    /**
     * 物品天生颜色（1.白 2.绿 3.蓝 4.紫 5.橙 6.金 7.红,8粉,9暗金.10幻彩）
     * @return
     */
    public final int getColor(){
        return color;
    }
    /**
     * 物品使用等级
     */
    private final int level;
    /**
     * 物品使用等级
     * @return
     */
    public final int getLevel(){
        return level;
    }
    /**
     * 物品最高使用等级
     */
    private final int max_level;
    /**
     * 物品最高使用等级
     * @return
     */
    public final int getMax_level(){
        return max_level;
    }
    /**
     * 玩家使用性别限制（0：女；1：男；2：通用）
     */
    private final int sex;
    /**
     * 玩家使用性别限制（0：女；1：男；2：通用）
     * @return
     */
    public final int getSex(){
        return sex;
    }
    /**
     * 玩家使用职业限制（0：齐王府；1：六扇门；2：待定职业1；3：待定职业2；9：通用）
     */
    private final ReadIntegerArray Occupation;
    /**
     * 玩家使用职业限制（0：齐王府；1：六扇门；2：待定职业1；3：待定职业2；9：通用）
     * @return
     */
    public final ReadIntegerArray getOccupation(){
        return Occupation;
    }
    /**
     * 绑定类型（0：默认；1：获得时绑定）
     */
    private final int bind;
    /**
     * 绑定类型（0：默认；1：获得时绑定）
     * @return
     */
    public final int getBind(){
        return bind;
    }
    /**
     * 最大叠加数（1：不可叠加，其他自然数：该物品最大叠加数量；除货币之外的最大值为9999）
     */
    private final long max;
    /**
     * 最大叠加数（1：不可叠加，其他自然数：该物品最大叠加数量；除货币之外的最大值为9999）
     * @return
     */
    public final long getMax(){
        return max;
    }
    /**
     * 使用需求条件(@;@_@) client ignore
     */
    private final ReadIntegerArrayEs use_need_condition;
    /**
     * 使用需求条件(@;@_@) client ignore
     * @return
     */
    public final ReadIntegerArrayEs getUse_need_condition(){
        return use_need_condition;
    }
    /**
     * 出售二次确定提示(0：不弹出；1：弹出)
     */
    private final int if_confirm;
    /**
     * 出售二次确定提示(0：不弹出；1：弹出)
     * @return
     */
    public final int getIf_confirm(){
        return if_confirm;
    }
    /**
     * 单日使用上限（0为无上限，其他自然数为上限值）client ignore
     */
    private final int daily_max_use;
    /**
     * 单日使用上限（0为无上限，其他自然数为上限值）client ignore
     * @return
     */
    public final int getDaily_max_use(){
        return daily_max_use;
    }
    /**
     * 永久使用上限（0为无上限，其他自然数为上限值）client ignore
     */
    private final int max_use;
    /**
     * 永久使用上限（0为无上限，其他自然数为上限值）client ignore
     * @return
     */
    public final int getMax_use(){
        return max_use;
    }
    /**
     * 效果配置[@;@_@]（1：增加属性【1_属性枚举_数值】；2：增加挂机时间【秒】；3：增加经验倍率【万分比_时间】；4_增加数值；5_增加称号和称号的时间；6_增加个人BOSS的刷新时间（秒）；8_挚友古道热肠类型；9_增加活跃度上限_数量；10_等级上限_万分比经验；11_dailyID_增加的次数；12vip经验增加点数；13货币增加"13_货币type_num"；14：激活对应的时装，对应时装表的ID）15:复活道具（1，世界BOSS，5年兽BOSS，7晶甲BOSS）16：召唤道具（1，世界BOSS，5年兽BOSS，7晶甲BOSS）17激活充值（对应rechargeitem的主键ID） 18 播放特效  19VIP宝珠特权(0限时宝珠_时间秒，1永久宝珠_货币类型_货币数量）20诸界远征积分宝箱,21获得家具_家具ID
     */
    private final ReadIntegerArrayEs effect_num;
    /**
     * 效果配置[@;@_@]（1：增加属性【1_属性枚举_数值】；2：增加挂机时间【秒】；3：增加经验倍率【万分比_时间】；4_增加数值；5_增加称号和称号的时间；6_增加个人BOSS的刷新时间（秒）；8_挚友古道热肠类型；9_增加活跃度上限_数量；10_等级上限_万分比经验；11_dailyID_增加的次数；12vip经验增加点数；13货币增加"13_货币type_num"；14：激活对应的时装，对应时装表的ID）15:复活道具（1，世界BOSS，5年兽BOSS，7晶甲BOSS）16：召唤道具（1，世界BOSS，5年兽BOSS，7晶甲BOSS）17激活充值（对应rechargeitem的主键ID） 18 播放特效  19VIP宝珠特权(0限时宝珠_时间秒，1永久宝珠_货币类型_货币数量）20诸界远征积分宝箱,21获得家具_家具ID
     * @return
     */
    public final ReadIntegerArrayEs getEffect_num(){
        return effect_num;
    }
    /**
     * 使用后触发脚本编号 client ignore
     */
    private final int script;
    /**
     * 使用后触发脚本编号 client ignore
     * @return
     */
    public final int getScript(){
        return script;
    }
    /**
     * 使用礼包调用的掉落
     */
    private final int ues_gift;
    /**
     * 使用礼包调用的掉落
     * @return
     */
    public final int getUes_gift(){
        return ues_gift;
    }
    /**
     * 宝箱的ID
     */
    private final int gift;
    /**
     * 宝箱的ID
     * @return
     */
    public final int getGift(){
        return gift;
    }
    /**
     * 配置在后台奖励的物品最大数量client ignore
     */
    private final int gm_item_max;
    /**
     * 配置在后台奖励的物品最大数量client ignore
     * @return
     */
    public final int getGm_item_max(){
        return gm_item_max;
    }
    /**
     * 获得时是否有获得提示（0，没有；1以上，堆叠达到的使用条件）
     */
    private final int if_use_info;
    /**
     * 获得时是否有获得提示（0，没有；1以上，堆叠达到的使用条件）
     * @return
     */
    public final int getIf_use_info(){
        return if_use_info;
    }
    /**
     * 物品预览类型（0，没有预览；1，有show动作的模型，填modelID；2.没有show动作的模型，填modelID；3.加人物模型；4.称号特效；5.称号图片）
     */
    private final int show_type;
    /**
     * 物品预览类型（0，没有预览；1，有show动作的模型，填modelID；2.没有show动作的模型，填modelID；3.加人物模型；4.称号特效；5.称号图片）
     * @return
     */
    public final int getShow_type(){
        return show_type;
    }
    /**
     * 展示的内容（时装，坐骑，宠物用模型ID，称号用名字）modelID_缩放大小_Y轴偏移_Y轴旋转_X轴旋转_Z轴旋转_x轴偏移_z轴偏移
     */
    private final String show_id;
    /**
     * 展示的内容（时装，坐骑，宠物用模型ID，称号用名字）modelID_缩放大小_Y轴偏移_Y轴旋转_X轴旋转_Z轴旋转_x轴偏移_z轴偏移
     * @return
     */
    public final String getShow_id(){
        return show_id;
    }
    /**
     * 模型缩放（百分比）
     */
    private final int size_scale;
    /**
     * 模型缩放（百分比）
     * @return
     */
    public final int getSize_scale(){
        return size_scale;
    }
    /**
     * 物品的蓝钻价值
     */
    private final int Item_Price;
    /**
     * 物品的蓝钻价值
     * @return
     */
    public final int getItem_Price(){
        return Item_Price;
    }
    /**
     * 是否任务检查（0否1是）
     */
    private final int needTaskCheck;
    /**
     * 是否任务检查（0否1是）
     * @return
     */
    public final int getNeedTaskCheck(){
        return needTaskCheck;
    }
    /**
     * 是否翅膀检查（0否1是）
     */
    private final int needwingCheck;
    /**
     * 是否翅膀检查（0否1是）
     * @return
     */
    public final int getNeedwingCheck(){
        return needwingCheck;
    }
    /**
     * 合成目标（目标物品ID_消耗自身数量）(@_@) 
     */
    private final ReadIntegerArray hechen_target;
    /**
     * 合成目标（目标物品ID_消耗自身数量）(@_@) 
     * @return
     */
    public final ReadIntegerArray getHechen_target(){
        return hechen_target;
    }
    /**
     * 合成额外消耗（物品ID_数量）(@_@) 
     */
    private final ReadIntegerArray hechen_money;
    /**
     * 合成额外消耗（物品ID_数量）(@_@) 
     * @return
     */
    public final ReadIntegerArray getHechen_money(){
        return hechen_money;
    }
    /**
     * 掉落物品后是否公告
     */
    private final int drop_notice;
    /**
     * 掉落物品后是否公告
     * @return
     */
    public final int getDrop_notice(){
        return drop_notice;
    }
    /**
     * 掉落物品后聊天发送(0世界4系统14传闻）
     */
    private final ReadIntegerArray chatchannel;
    /**
     * 掉落物品后聊天发送(0世界4系统14传闻）
     * @return
     */
    public final ReadIntegerArray getChatchannel(){
        return chatchannel;
    }
    /**
     * 拍卖价格类型，0配置价格，1自定义价格（最低价格2，最大价格使用auction_max_price字段）
     */
    private final int auction_price_type;
    /**
     * 拍卖价格类型，0配置价格，1自定义价格（最低价格2，最大价格使用auction_max_price字段）
     * @return
     */
    public final int getAuction_price_type(){
        return auction_price_type;
    }
    /**
     * 拍卖使用的货币类型
     */
    private final int auction_use_coin;
    /**
     * 拍卖使用的货币类型
     * @return
     */
    public final int getAuction_use_coin(){
        return auction_use_coin;
    }
    /**
     * 拍卖最小价格
     */
    private final int auction_min_price;
    /**
     * 拍卖最小价格
     * @return
     */
    public final int getAuction_min_price(){
        return auction_min_price;
    }
    /**
     * 拍卖最大价格
此数值大于0才能上架,否则不能上架,填写0或空都为0,-1为只能竞拍不能一口价
     */
    private final int auction_max_price;
    /**
     * 拍卖最大价格
此数值大于0才能上架,否则不能上架,填写0或空都为0,-1为只能竞拍不能一口价
     * @return
     */
    public final int getAuction_max_price(){
        return auction_max_price;
    }
    /**
     * 单次加价的类类型，0为数值价格，1为万分比数值
     */
    private final int auction_single_type;
    /**
     * 单次加价的类类型，0为数值价格，1为万分比数值
     * @return
     */
    public final int getAuction_single_type(){
        return auction_single_type;
    }
    /**
     * 拍卖单次增加，增加类型为0时表示具体价格，为1的时候表示当前价格万分比,没有增加价格表示只能一口价
     */
    private final int auction_single_price;
    /**
     * 拍卖单次增加，增加类型为0时表示具体价格，为1的时候表示当前价格万分比,没有增加价格表示只能一口价
     * @return
     */
    public final int getAuction_single_price(){
        return auction_single_price;
    }
    /**
     * 是否开拍倒计时(0不倒计时,1上架后倒计时)
     */
    private final int auction_countdown;
    /**
     * 是否开拍倒计时(0不倒计时,1上架后倒计时)
     * @return
     */
    public final int getAuction_countdown(){
        return auction_countdown;
    }
    /**
     * 世界拍卖总时间，单位秒
     */
    private final int auction_all_time;
    /**
     * 世界拍卖总时间，单位秒
     * @return
     */
    public final int getAuction_all_time(){
        return auction_all_time;
    }
    /**
     * 仙盟拍卖总时间，单位秒
     */
    private final int auction_guild_all_time;
    /**
     * 仙盟拍卖总时间，单位秒
     * @return
     */
    public final int getAuction_guild_all_time(){
        return auction_guild_all_time;
    }
    /**
     * 失效时间（从创造开始计时，到时就失效，单位：秒）
     */
    private final int dead_time;
    /**
     * 失效时间（从创造开始计时，到时就失效，单位：秒）
     * @return
     */
    public final int getDead_time(){
        return dead_time;
    }

    public Cfg_Item_Bean(int id,String name,int drop_model,int type,int trade_type,int trade_recom,String button_typeStr,int color,int level,int max_level,int sex,String OccupationStr,int bind,long max,String use_need_conditionStr,int if_confirm,int daily_max_use,int max_use,String effect_numStr,int script,int ues_gift,int gift,int gm_item_max,int if_use_info,int show_type,String show_id,int size_scale,int Item_Price,int needTaskCheck,int needwingCheck,String hechen_targetStr,String hechen_moneyStr,int drop_notice,String chatchannelStr,int auction_price_type,int auction_use_coin,int auction_min_price,int auction_max_price,int auction_single_type,int auction_single_price,int auction_countdown,int auction_all_time,int auction_guild_all_time,int dead_time){
        this.id = id;
        this.name = name;
        this.drop_model = drop_model;
        this.type = type;
        this.trade_type = trade_type;
        this.trade_recom = trade_recom;
        this.button_type = new ReadIntegerArray(button_typeStr,",");
        this.color = color;
        this.level = level;
        this.max_level = max_level;
        this.sex = sex;
        this.Occupation = new ReadIntegerArray(OccupationStr,",");
        this.bind = bind;
        this.max = max;
        this.use_need_condition = new ReadIntegerArrayEs(use_need_conditionStr,"}",",");
        this.if_confirm = if_confirm;
        this.daily_max_use = daily_max_use;
        this.max_use = max_use;
        this.effect_num = new ReadIntegerArrayEs(effect_numStr,"}",",");
        this.script = script;
        this.ues_gift = ues_gift;
        this.gift = gift;
        this.gm_item_max = gm_item_max;
        this.if_use_info = if_use_info;
        this.show_type = show_type;
        this.show_id = show_id;
        this.size_scale = size_scale;
        this.Item_Price = Item_Price;
        this.needTaskCheck = needTaskCheck;
        this.needwingCheck = needwingCheck;
        this.hechen_target = new ReadIntegerArray(hechen_targetStr,",");
        this.hechen_money = new ReadIntegerArray(hechen_moneyStr,",");
        this.drop_notice = drop_notice;
        this.chatchannel = new ReadIntegerArray(chatchannelStr,",");
        this.auction_price_type = auction_price_type;
        this.auction_use_coin = auction_use_coin;
        this.auction_min_price = auction_min_price;
        this.auction_max_price = auction_max_price;
        this.auction_single_type = auction_single_type;
        this.auction_single_price = auction_single_price;
        this.auction_countdown = auction_countdown;
        this.auction_all_time = auction_all_time;
        this.auction_guild_all_time = auction_guild_all_time;
        this.dead_time = dead_time;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("drop_model:").append(drop_model).append(";");
        str.append("type:").append(type).append(";");
        str.append("trade_type:").append(trade_type).append(";");
        str.append("trade_recom:").append(trade_recom).append(";");
        str.append("button_type:").append(button_type).append(";");
        str.append("color:").append(color).append(";");
        str.append("level:").append(level).append(";");
        str.append("max_level:").append(max_level).append(";");
        str.append("sex:").append(sex).append(";");
        str.append("Occupation:").append(Occupation).append(";");
        str.append("bind:").append(bind).append(";");
        str.append("max:").append(max).append(";");
        str.append("use_need_condition:").append(use_need_condition).append(";");
        str.append("if_confirm:").append(if_confirm).append(";");
        str.append("daily_max_use:").append(daily_max_use).append(";");
        str.append("max_use:").append(max_use).append(";");
        str.append("effect_num:").append(effect_num).append(";");
        str.append("script:").append(script).append(";");
        str.append("ues_gift:").append(ues_gift).append(";");
        str.append("gift:").append(gift).append(";");
        str.append("gm_item_max:").append(gm_item_max).append(";");
        str.append("if_use_info:").append(if_use_info).append(";");
        str.append("show_type:").append(show_type).append(";");
        str.append("show_id:").append(show_id).append(";");
        str.append("size_scale:").append(size_scale).append(";");
        str.append("Item_Price:").append(Item_Price).append(";");
        str.append("needTaskCheck:").append(needTaskCheck).append(";");
        str.append("needwingCheck:").append(needwingCheck).append(";");
        str.append("hechen_target:").append(hechen_target).append(";");
        str.append("hechen_money:").append(hechen_money).append(";");
        str.append("drop_notice:").append(drop_notice).append(";");
        str.append("chatchannel:").append(chatchannel).append(";");
        str.append("auction_price_type:").append(auction_price_type).append(";");
        str.append("auction_use_coin:").append(auction_use_coin).append(";");
        str.append("auction_min_price:").append(auction_min_price).append(";");
        str.append("auction_max_price:").append(auction_max_price).append(";");
        str.append("auction_single_type:").append(auction_single_type).append(";");
        str.append("auction_single_price:").append(auction_single_price).append(";");
        str.append("auction_countdown:").append(auction_countdown).append(";");
        str.append("auction_all_time:").append(auction_all_time).append(";");
        str.append("auction_guild_all_time:").append(auction_guild_all_time).append(";");
        str.append("dead_time:").append(dead_time).append(";");
        return str.toString();
    }
}
