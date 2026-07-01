/**
 * Auto generated, do not edit it
 *
 * Equip配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Equip_Bean{
    /**
     * 装备id,(id构成：部位，职业，品质，等级）

     */
    private final int Id;
    /**
     * 装备id,(id构成：部位，职业，品质，等级）

     * @return
     */
    public final int getId(){
        return Id;
    }
    /**
     * 装备名字
     */
    private final String name;
    /**
     * 装备名字
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 装备部位(0头盔.1武器.2胸甲.3项链.4腰带.5腿甲.6鞋子.7戒指.8手镯.9耳环.10灵章，11-21圣装，30仙甲武器，31仙甲战甲，32仙甲光环，33仙甲阵道，34仙甲左佩，35仙甲右佩，36仙甲头冠，37仙甲肩饰，38仙甲护腕，39仙甲手套，40仙甲内衬，41仙甲腰带，42仙甲裤子，43仙甲鞋履，44-57第二套仙甲，58-71第三套仙甲，72-85第四套仙甲。）；圣装【新】101-133；201 爪套，202 项圈，203 铃铛，204 福袋；211金之印,212木之印,213土之印,214水之印,215火之印,216雷之印,217太阴之印,218太阳之印,219太白之印,220混元之印；301 面纹，302 心纹，303 环纹，304 足纹，305-330魔魂装备（305-308阵营1，309-312阵营2，313-316阵营3，317-320阵营4，321-324阵营5，325-340为预留）401仙甲1阳，402仙甲1阴，403仙甲1八卦1，404仙甲1八卦2，405仙甲1八卦3，406仙甲1八卦4，407仙甲1八卦5，408仙甲1八卦6，409仙甲1八卦7，410仙甲1八卦8，411-440仙甲阴阳八卦,441幻装头盔，442幻装耳环，443幻装项链，444幻装衣服，445幻装裤子，446幻装武器，447幻装护腕，448幻装鞋子，449幻装戒指，450幻装手镯
     */
    private final int part;
    /**
     * 装备部位(0头盔.1武器.2胸甲.3项链.4腰带.5腿甲.6鞋子.7戒指.8手镯.9耳环.10灵章，11-21圣装，30仙甲武器，31仙甲战甲，32仙甲光环，33仙甲阵道，34仙甲左佩，35仙甲右佩，36仙甲头冠，37仙甲肩饰，38仙甲护腕，39仙甲手套，40仙甲内衬，41仙甲腰带，42仙甲裤子，43仙甲鞋履，44-57第二套仙甲，58-71第三套仙甲，72-85第四套仙甲。）；圣装【新】101-133；201 爪套，202 项圈，203 铃铛，204 福袋；211金之印,212木之印,213土之印,214水之印,215火之印,216雷之印,217太阴之印,218太阳之印,219太白之印,220混元之印；301 面纹，302 心纹，303 环纹，304 足纹，305-330魔魂装备（305-308阵营1，309-312阵营2，313-316阵营3，317-320阵营4，321-324阵营5，325-340为预留）401仙甲1阳，402仙甲1阴，403仙甲1八卦1，404仙甲1八卦2，405仙甲1八卦3，406仙甲1八卦4，407仙甲1八卦5，408仙甲1八卦6，409仙甲1八卦7，410仙甲1八卦8，411-440仙甲阴阳八卦,441幻装头盔，442幻装耳环，443幻装项链，444幻装衣服，445幻装裤子，446幻装武器，447幻装护腕，448幻装鞋子，449幻装戒指，450幻装手镯
     * @return
     */
    public final int getPart(){
        return part;
    }
    /**
     * 装备等级需求
     */
    private final int level;
    /**
     * 装备等级需求
     * @return
     */
    public final int getLevel(){
        return level;
    }
    /**
     * 职业限制
0-男剑
1-女枪
2-待定
3-待定
4-待定
5-待定
9-通用
     */
    private final ReadIntegerArray gender;
    /**
     * 职业限制
0-男剑
1-女枪
2-待定
3-待定
4-待定
5-待定
9-通用
     * @return
     */
    public final ReadIntegerArray getGender(){
        return gender;
    }
    /**
     * 1表示1个钻石，2表示2个钻石，0表示没有钻石，钻石显示为左上角
     */
    private final int diamond_Number;
    /**
     * 1表示1个钻石，2表示2个钻石，0表示没有钻石，钻石显示为左上角
     * @return
     */
    public final int getDiamond_Number(){
        return diamond_Number;
    }
    /**
     * 品阶：1表示1阶，2表示2阶
     */
    private final int grade;
    /**
     * 品阶：1表示1阶，2表示2阶
     * @return
     */
    public final int getGrade(){
        return grade;
    }
    /**
     * 填写转职id
     */
    private final int Classlevel;
    /**
     * 填写转职id
     * @return
     */
    public final int getClasslevel(){
        return Classlevel;
    }
    /**
     * 绑定类型(0：不绑定;1：获得时绑定;2：使用后绑定)
     */
    private final int bind;
    /**
     * 绑定类型(0：不绑定;1：获得时绑定;2：使用后绑定)
     * @return
     */
    public final int getBind(){
        return bind;
    }
    /**
     * 装备品质(：1.白 2.绿 3.蓝 4.紫 5.橙 6.金 7.红,8粉,9暗金.10幻彩)
     */
    private final int quality;
    /**
     * 装备品质(：1.白 2.绿 3.蓝 4.紫 5.橙 6.金 7.红,8粉,9暗金.10幻彩)
     * @return
     */
    public final int getQuality(){
        return quality;
    }
    /**
     * 装备评分
     */
    private final int score;
    /**
     * 装备评分
     * @return
     */
    public final int getScore(){
        return score;
    }
    /**
     * 初始属性：属性类型_数值，属性类型1_数值，(@;@_@)
     */
    private final ReadIntegerArrayEs attribute1;
    /**
     * 初始属性：属性类型_数值，属性类型1_数值，(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getAttribute1(){
        return attribute1;
    }
    /**
     * 特殊附加属性：属性类型_数值，属性类型1_数值，(@;@_@)
     */
    private final ReadIntegerArrayEs attribute2;
    /**
     * 特殊附加属性：属性类型_数值，属性类型1_数值，(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getAttribute2(){
        return attribute2;
    }
    /**
     * 特殊附加属性：属性类型_数值，属性类型1_数值，(@;@_@)
     */
    private final ReadIntegerArrayEs attribute3;
    /**
     * 特殊附加属性：属性类型_数值，属性类型1_数值，(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getAttribute3(){
        return attribute3;
    }
    /**
     * 回收价格（回收获得的金币数量）货币id_数量(@;@_@)
     */
    private final ReadIntegerArrayEs price;
    /**
     * 回收价格（回收获得的金币数量）货币id_数量(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getPrice(){
        return price;
    }
    /**
     * 回收成物品（回收获得的物品及数量）物品id_数量(@;@_@)
     */
    private final ReadIntegerArrayEs price1;
    /**
     * 回收成物品（回收获得的物品及数量）物品id_数量(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getPrice1(){
        return price1;
    }
    /**
     * 封印吞噬经验
     */
    private final int seal_exp;
    /**
     * 封印吞噬经验
     * @return
     */
    public final int getSeal_exp(){
        return seal_exp;
    }
    /**
     * 出售时是否弹出确认提示（0：不弹出;1：弹出）
     */
    private final int confirm;
    /**
     * 出售时是否弹出确认提示（0：不弹出;1：弹出）
     * @return
     */
    public final int getConfirm(){
        return confirm;
    }
    /**
     * 道具是否可进入仓库(1否，0是) client ignore
     */
    private final int save_warehouse;
    /**
     * 道具是否可进入仓库(1否，0是) client ignore
     * @return
     */
    public final int getSave_warehouse(){
        return save_warehouse;
    }
    /**
     * 是否记录产出日志（0：不记录;1：记录）client ignore
     */
    private final int log;
    /**
     * 是否记录产出日志（0：不记录;1：记录）client ignore
     * @return
     */
    public final int getLog(){
        return log;
    }
    /**
     * 掉落模型ID
     */
    private final int drop_model;
    /**
     * 掉落模型ID
     * @return
     */
    public final int getDrop_model(){
        return drop_model;
    }
    /**
     * 显示模型id
     */
    private final int model_id;
    /**
     * 显示模型id
     * @return
     */
    public final int getModel_id(){
        return model_id;
    }
    /**
     * 继承的装备ID client ignore
     */
    private final int inherit_equip_id;
    /**
     * 继承的装备ID client ignore
     * @return
     */
    public final int getInherit_equip_id(){
        return inherit_equip_id;
    }
    /**
     * 是否显示查看（1是;0否）(废弃)
     */
    private final int isCheck;
    /**
     * 是否显示查看（1是;0否）(废弃)
     * @return
     */
    public final int getIsCheck(){
        return isCheck;
    }
    /**
     * 仓库积分
     */
    private final int warehouse_integral;
    /**
     * 仓库积分
     * @return
     */
    public final int getWarehouse_integral(){
        return warehouse_integral;
    }
    /**
     * 装备的蓝钻价值 client ignore
     */
    private final int Equip_Price;
    /**
     * 装备的蓝钻价值 client ignore
     * @return
     */
    public final int getEquip_Price(){
        return Equip_Price;
    }
    /**
     * 是否可以被拆解（0为不可被拆解，1为可以被拆解）
     */
    private final int Equip_Dismantling;
    /**
     * 是否可以被拆解（0为不可被拆解，1为可以被拆解）
     * @return
     */
    public final int getEquip_Dismantling(){
        return Equip_Dismantling;
    }
    /**
     * 是否是普通装备
     */
    private final int if_common;
    /**
     * 是否是普通装备
     * @return
     */
    public final int getIf_common(){
        return if_common;
    }
    /**
     * 装备强化上限
     */
    private final int level_max;
    /**
     * 装备强化上限
     * @return
     */
    public final int getLevel_max(){
        return level_max;
    }
    /**
     * 洗练属性模板:属性类型_下限_上限;(@;@_@)client ignore
     */
    private final ReadIntegerArrayEs wash;
    /**
     * 洗练属性模板:属性类型_下限_上限;(@;@_@)client ignore
     * @return
     */
    public final ReadIntegerArrayEs getWash(){
        return wash;
    }
    /**
     * 洗练属性模板:属性类型_下限_上限;(@;@_@)
     */
    private final ReadIntegerArrayEs recommended_tips;
    /**
     * 洗练属性模板:属性类型_下限_上限;(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getRecommended_tips(){
        return recommended_tips;
    }
    /**
     * 填写境界的等级
     */
    private final int Statelevel;
    /**
     * 填写境界的等级
     * @return
     */
    public final int getStatelevel(){
        return Statelevel;
    }
    /**
     * 交易中默认的推荐价格，不填表示不能交易,4阶红3星以上可交易
     */
    private final int trade_recom;
    /**
     * 交易中默认的推荐价格，不填表示不能交易,4阶红3星以上可交易
     * @return
     */
    public final int getTrade_recom(){
        return trade_recom;
    }
    /**
     * 掉落出来是否公告,0不公告,1是公告
     */
    private final int drop_notice;
    /**
     * 掉落出来是否公告,0不公告,1是公告
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
     * 熔炉获得的灵晶数量
     */
    private final int seal_anima;
    /**
     * 熔炉获得的灵晶数量
     * @return
     */
    public final int getSeal_anima(){
        return seal_anima;
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
     * 此数值大于0才能上架,否则不能上架,填写0或空都为0,填写-1的时候不显示一口价,只可竞拍

     */
    private final int auction_max_price;
    /**
     * 此数值大于0才能上架,否则不能上架,填写0或空都为0,填写-1的时候不显示一口价,只可竞拍

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
    /**
     * 圣装专用（其他类型的装备不能使用）
1-2：对应圣装第一套的套装
3-4：对应圣装第二套的套装
5-6：对应圣装第三套的套装
（如需要扩展，需要程序代码支持）
     */
    private final int holySuitType;
    /**
     * 圣装专用（其他类型的装备不能使用）
1-2：对应圣装第一套的套装
3-4：对应圣装第二套的套装
5-6：对应圣装第三套的套装
（如需要扩展，需要程序代码支持）
     * @return
     */
    public final int getHolySuitType(){
        return holySuitType;
    }
    /**
     * 熔炼装备后对应加的聚宝盆银元宝奖励
     */
    private final int magic_bowl_solve;
    /**
     * 熔炼装备后对应加的聚宝盆银元宝奖励
     * @return
     */
    public final int getMagic_bowl_solve(){
        return magic_bowl_solve;
    }

    public Cfg_Equip_Bean(int Id,String name,int part,int level,String genderStr,int diamond_Number,int grade,int Classlevel,int bind,int quality,int score,String attribute1Str,String attribute2Str,String attribute3Str,String priceStr,String price1Str,int seal_exp,int confirm,int save_warehouse,int log,int drop_model,int model_id,int inherit_equip_id,int isCheck,int warehouse_integral,int Equip_Price,int Equip_Dismantling,int if_common,int level_max,String washStr,String recommended_tipsStr,int Statelevel,int trade_recom,int drop_notice,String chatchannelStr,int seal_anima,int auction_price_type,int auction_use_coin,int auction_min_price,int auction_max_price,int auction_single_type,int auction_single_price,int auction_countdown,int auction_all_time,int auction_guild_all_time,int dead_time,int holySuitType,int magic_bowl_solve){
        this.Id = Id;
        this.name = name;
        this.part = part;
        this.level = level;
        this.gender = new ReadIntegerArray(genderStr,",");
        this.diamond_Number = diamond_Number;
        this.grade = grade;
        this.Classlevel = Classlevel;
        this.bind = bind;
        this.quality = quality;
        this.score = score;
        this.attribute1 = new ReadIntegerArrayEs(attribute1Str,"}",",");
        this.attribute2 = new ReadIntegerArrayEs(attribute2Str,"}",",");
        this.attribute3 = new ReadIntegerArrayEs(attribute3Str,"}",",");
        this.price = new ReadIntegerArrayEs(priceStr,"}",",");
        this.price1 = new ReadIntegerArrayEs(price1Str,"}",",");
        this.seal_exp = seal_exp;
        this.confirm = confirm;
        this.save_warehouse = save_warehouse;
        this.log = log;
        this.drop_model = drop_model;
        this.model_id = model_id;
        this.inherit_equip_id = inherit_equip_id;
        this.isCheck = isCheck;
        this.warehouse_integral = warehouse_integral;
        this.Equip_Price = Equip_Price;
        this.Equip_Dismantling = Equip_Dismantling;
        this.if_common = if_common;
        this.level_max = level_max;
        this.wash = new ReadIntegerArrayEs(washStr,"}",",");
        this.recommended_tips = new ReadIntegerArrayEs(recommended_tipsStr,"}",",");
        this.Statelevel = Statelevel;
        this.trade_recom = trade_recom;
        this.drop_notice = drop_notice;
        this.chatchannel = new ReadIntegerArray(chatchannelStr,",");
        this.seal_anima = seal_anima;
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
        this.holySuitType = holySuitType;
        this.magic_bowl_solve = magic_bowl_solve;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("name:").append(name).append(";");
        str.append("part:").append(part).append(";");
        str.append("level:").append(level).append(";");
        str.append("gender:").append(gender).append(";");
        str.append("diamond_Number:").append(diamond_Number).append(";");
        str.append("grade:").append(grade).append(";");
        str.append("Classlevel:").append(Classlevel).append(";");
        str.append("bind:").append(bind).append(";");
        str.append("quality:").append(quality).append(";");
        str.append("score:").append(score).append(";");
        str.append("attribute1:").append(attribute1).append(";");
        str.append("attribute2:").append(attribute2).append(";");
        str.append("attribute3:").append(attribute3).append(";");
        str.append("price:").append(price).append(";");
        str.append("price1:").append(price1).append(";");
        str.append("seal_exp:").append(seal_exp).append(";");
        str.append("confirm:").append(confirm).append(";");
        str.append("save_warehouse:").append(save_warehouse).append(";");
        str.append("log:").append(log).append(";");
        str.append("drop_model:").append(drop_model).append(";");
        str.append("model_id:").append(model_id).append(";");
        str.append("inherit_equip_id:").append(inherit_equip_id).append(";");
        str.append("isCheck:").append(isCheck).append(";");
        str.append("warehouse_integral:").append(warehouse_integral).append(";");
        str.append("Equip_Price:").append(Equip_Price).append(";");
        str.append("Equip_Dismantling:").append(Equip_Dismantling).append(";");
        str.append("if_common:").append(if_common).append(";");
        str.append("level_max:").append(level_max).append(";");
        str.append("wash:").append(wash).append(";");
        str.append("recommended_tips:").append(recommended_tips).append(";");
        str.append("Statelevel:").append(Statelevel).append(";");
        str.append("trade_recom:").append(trade_recom).append(";");
        str.append("drop_notice:").append(drop_notice).append(";");
        str.append("chatchannel:").append(chatchannel).append(";");
        str.append("seal_anima:").append(seal_anima).append(";");
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
        str.append("holySuitType:").append(holySuitType).append(";");
        str.append("magic_bowl_solve:").append(magic_bowl_solve).append(";");
        return str.toString();
    }
}
