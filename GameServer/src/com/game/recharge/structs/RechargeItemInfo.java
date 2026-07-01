package com.game.recharge.structs;

import com.data.struct.ReadIntegerArray;
import com.data.struct.ReadIntegerArrayEs;

import java.util.TreeMap;

/**
 * Created by cxl on 2021/3/12.
 * 充值配置表模型对象
 */
public class RechargeItemInfo {

    /**
     * 配置表ID
     */
    private int goods_id;

    /**
     * 游戏内部配置ID
     */
    private int goods_system_cfg_id;

    /**
     * 商品名字描述（主要用于BI后台数据）
     */
    private String goods_name;

    /**
     * 渠道名称(为空则表示游戏内正常充值)
     */
    private String goods_pay_channel;

    /**
     * 支付类型（第三方支付）
     */
    private int goods_pay_type;

    /**
     * 充值类型
     * 1：正常充值
     * 2：每日礼包充值
     * 3：畅游月卡
     * 4：尊享月卡
     * 5：终身卡
     * 6：成长基金
     * 7：神秘商店
     * 8：0元购
     * 9：直购礼包（超值折扣）
     * 10：狂欢周
     * 11：运营活动类（后台配置）
     */
    private int goods_type;

    /**
     * 只针对Type=1（正常充值）的情况使用，其他类型不能使用
     * 1=正常充值
     * 2=新手礼包（一生一次）
     * 3=周礼包（一周一刷新）
     * 4=日礼包（一日一刷新）
     */
    private int goods_subtype;

    /**
     * 充值次数（当前轮每个挡位对应充值的次数）
     * -1=无次数限制
     */
    private int goods_limit;

    /**
     * 显示的图标的ID（hide）
     */
    private int goods_icon;


    /**
     * 充值档位对应消耗的真实货币
     *  key 1 android：->CNY->VALUE  分
     *
     */
    private TreeMap<String, TreeMap<String,String>> goods_price = new TreeMap<>();

    /**
     * 充值计费点
     * 1：android：tzj_001
     * 2：ios: tzj_002
     * （不需要区分大小写）
     */
    private TreeMap<String, String> goods_price_point = new TreeMap<>();


    /**
     * 界面默认显示的货币 例如:THB
     */
    private String goods_show_price;

    /**
     * 对应奖励
     * 物品类型_数量_绑定_职业
     * 绑定 0未绑定 1绑定
     * 也只 0男剑 1女枪 9通用
     */
    private String goods_reward;

    /**
     * 充值倍数
     * 倍数_次数（3_2表示前2次充值都是3倍奖励）
     * -1代表无限次
     */
    private String goods_multiple;


    /**
     * 额外赠送
     * 物品类型_数量_绑定_职业
     * 绑定 0未绑定 1绑定
     * 也只 0男剑 1女枪 9通用
     */
    private String goods_extra_reward;


    /**
     * 额外奖励可领取次数
     * -1代表无限次
     */
    private int goods_extra_reward_limit;

    /**
     * 商品扩展字段
     */
    private String goods_ext;

    /**
     * 是否计入到游戏累充活动
     */
    private int isTotalRecharge;

    /**
     * 是否增加VIP经验
     */
    private int totalVipPower;

    /**
     * 物品的图片url
     * @return
     */
    private String goodsurl;


    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public int getGoods_system_cfg_id() {
        return goods_system_cfg_id;
    }

    public void setGoods_system_cfg_id(int goods_system_cfg_id) {
        this.goods_system_cfg_id = goods_system_cfg_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_pay_channel() {
        return goods_pay_channel;
    }

    public void setGoods_pay_channel(String goods_pay_channel) {
        this.goods_pay_channel = goods_pay_channel;
    }

    public int getGoods_pay_type() {
        return goods_pay_type;
    }

    public void setGoods_pay_type(int goods_pay_type) {
        this.goods_pay_type = goods_pay_type;
    }

    public int getGoods_type() {
        return goods_type;
    }

    public void setGoods_type(int goods_type) {
        this.goods_type = goods_type;
    }

    public int getGoods_subtype() {
        return goods_subtype;
    }

    public void setGoods_subtype(int goods_subtype) {
        this.goods_subtype = goods_subtype;
    }

    public int getGoods_limit() {
        return goods_limit;
    }

    public void setGoods_limit(int goods_limit) {
        this.goods_limit = goods_limit;
    }

    public int getGoods_icon() {
        return goods_icon;
    }

    public void setGoods_icon(int goods_icon) {
        this.goods_icon = goods_icon;
    }


    public TreeMap<String, TreeMap<String, String>> getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(TreeMap<String, TreeMap<String, String>> goods_price) {
        this.goods_price = goods_price;
    }

    public TreeMap<String, String> getGoods_price_point() {
        return goods_price_point;
    }

    public void setGoods_price_point(TreeMap<String, String> goods_price_point) {
        this.goods_price_point = goods_price_point;
    }

    public ReadIntegerArrayEs getGoods_reward() {
        return  getReadIntegerArrayEs(goods_reward);
    }

    public void setGoods_reward(String goods_reward) {
        this.goods_reward = goods_reward;
    }

    public ReadIntegerArray getGoods_multiple() {
        return  getReadIntegerArray(goods_multiple);
    }

    public void setGoods_multiple(String goods_multiple) {
        this.goods_multiple = goods_multiple;
    }

    public ReadIntegerArrayEs getGoods_extra_reward() {
        return getReadIntegerArrayEs(goods_extra_reward);
    }

    public void setGoods_extra_reward(String goods_extra_reward) {
        this.goods_extra_reward = goods_extra_reward;
    }

    public int getGoods_extra_reward_limit() {
        return goods_extra_reward_limit;
    }

    public void setGoods_extra_reward_limit(int goods_extra_reward_limit) {
        this.goods_extra_reward_limit = goods_extra_reward_limit;
    }

    public String getGoods_ext() {
        return goods_ext;
    }

    public void setGoods_ext(String goods_ext) {
        this.goods_ext = goods_ext;
    }

    public int getIsTotalRecharge() {
        return isTotalRecharge;
    }

    public void setIsTotalRecharge(int isTotalRecharge) {
        this.isTotalRecharge = isTotalRecharge;
    }

    public int getTotalVipPower() {
        return totalVipPower;
    }

    public void setTotalVipPower(int totalVipPower) {
        this.totalVipPower = totalVipPower;
    }

    private ReadIntegerArrayEs getReadIntegerArrayEs(String str){
        return new ReadIntegerArrayEs(str,";","_");
    }

    private ReadIntegerArray getReadIntegerArray(String str){
        return new ReadIntegerArray(str,"_");
    }

    public String getGoods_show_price() {
        return goods_show_price;
    }

    public void setGoods_show_price(String goods_show_price) {
        this.goods_show_price = goods_show_price;
    }

    public String getGoodsurl() {
        return goodsurl;
    }
    public void setGoodsurl(String goodsurl) {
        this.goodsurl = goodsurl;
    }
}
