/**
 * Auto generated, do not edit it
 *
 * activity_yunying配置表
 */
package com.data.bean;

	
public class Cfg_Activity_yunying_Bean{
    /**
     * key值
     */
    private final int id;
    /**
     * key值
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 所属逻辑ID    
 1, -- 活跃兑换
 2, -- 每日充值
 3, -- 登陆有礼
 4, -- 限购礼包
 5, -- 天帝宝库
 6, -- 累计充值
 7, -- 限时消耗
 8, -- 集物兑换
 9, -- 团购
 10, -- 招财猫
 11,  --首领狂欢
 12, --庆典任务
 13,  --节日集字
 14,  --节日特惠(直购礼包)
 15,  --连续累充
 16,--限时商城
 17,  --限时礼包
 18,  --积分排名
 19,  --节日许愿
 20,  --FB分享
 21,--连续累充2（直接购买）
22,--新年祝福
23,--掷骰子
24.--外观展示
25.--上线图片提示
26, --聚宝盆
27, --幸运砸蛋
28, --绑玉招财猫
     */
    private final int logic_id;
    /**
     * 所属逻辑ID    
 1, -- 活跃兑换
 2, -- 每日充值
 3, -- 登陆有礼
 4, -- 限购礼包
 5, -- 天帝宝库
 6, -- 累计充值
 7, -- 限时消耗
 8, -- 集物兑换
 9, -- 团购
 10, -- 招财猫
 11,  --首领狂欢
 12, --庆典任务
 13,  --节日集字
 14,  --节日特惠(直购礼包)
 15,  --连续累充
 16,--限时商城
 17,  --限时礼包
 18,  --积分排名
 19,  --节日许愿
 20,  --FB分享
 21,--连续累充2（直接购买）
22,--新年祝福
23,--掷骰子
24.--外观展示
25.--上线图片提示
26, --聚宝盆
27, --幸运砸蛋
28, --绑玉招财猫
     * @return
     */
    public final int getLogic_id(){
        return logic_id;
    }
    /**
     * 0 普通活动
1 元旦
2 情人节
3 妇女节
4 愚人节
5 劳动节
6 儿童节
7 教师节
8 圣诞节
9 新年
10 元宵节
11 清明节
12 端午节
13 七夕
14 中秋节
15 重阳节
16 腊八节
17 除夕
18 泼水节
19 招财猫专用

     */
    private final int festival_id;
    /**
     * 0 普通活动
1 元旦
2 情人节
3 妇女节
4 愚人节
5 劳动节
6 儿童节
7 教师节
8 圣诞节
9 新年
10 元宵节
11 清明节
12 端午节
13 七夕
14 中秋节
15 重阳节
16 腊八节
17 除夕
18 泼水节
19 招财猫专用

     * @return
     */
    public final int getFestival_id(){
        return festival_id;
    }

    public Cfg_Activity_yunying_Bean(int id,int logic_id,int festival_id){
        this.id = id;
        this.logic_id = logic_id;
        this.festival_id = festival_id;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("logic_id:").append(logic_id).append(";");
        str.append("festival_id:").append(festival_id).append(";");
        return str.toString();
    }
}
