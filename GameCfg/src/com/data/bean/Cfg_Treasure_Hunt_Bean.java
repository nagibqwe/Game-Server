/**
 * Auto generated, do not edit it
 *
 * Treasure_Hunt配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Treasure_Hunt_Bean{
    /**
     * 寻宝奖池id=rewardType*100+desc
     */
    private final int id;
    /**
     * 寻宝奖池id=rewardType*100+desc
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 轮数
只有仙甲寻宝在用，其他类型寻宝无用（用于时间开启下一轮，轮数配置在global表1778
     */
    private final int round;
    /**
     * 轮数
只有仙甲寻宝在用，其他类型寻宝无用（用于时间开启下一轮，轮数配置在global表1778
     * @return
     */
    public final int getRound(){
        return round;
    }
    /**
     * 奖池类型
1、机缘寻宝
2、仙魄奖池（灵魄抽奖）
3、造化寻宝
4、鸿蒙寻宝
5、上古寻宝
6，仙甲寻宝
对应Treasure_Pop表的主键
     */
    private final int rewardType;
    /**
     * 奖池类型
1、机缘寻宝
2、仙魄奖池（灵魄抽奖）
3、造化寻宝
4、鸿蒙寻宝
5、上古寻宝
6，仙甲寻宝
对应Treasure_Pop表的主键
     * @return
     */
    public final int getRewardType(){
        return rewardType;
    }
    /**
     * 是否在界面中显示给玩家看

-1 不显示（玩家可以抽到，但是不会展示在界面）
0 显示（只能显示10个，不能配置超过10个，仙甲寻宝12个，超过指定个程序则不会读取）
2：主奖励（显示在界面最中心）
1：副奖励1（显示在左边副奖励）
3：副奖励2（显示在右边副奖励）

     */
    private final int isShow;
    /**
     * 是否在界面中显示给玩家看

-1 不显示（玩家可以抽到，但是不会展示在界面）
0 显示（只能显示10个，不能配置超过10个，仙甲寻宝12个，超过指定个程序则不会读取）
2：主奖励（显示在界面最中心）
1：副奖励1（显示在左边副奖励）
3：副奖励2（显示在右边副奖励）

     * @return
     */
    public final int getIsShow(){
        return isShow;
    }
    /**
     * 道具类型
1：普通道具
2：极品道具
     */
    private final int type;
    /**
     * 道具类型
1：普通道具
2：极品道具
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * item_num_bind_occ
bind 0未绑定1绑定
occ 0男1女9通用
     */
    private final ReadIntegerArrayEs reward;
    /**
     * item_num_bind_occ
bind 0未绑定1绑定
occ 0男1女9通用
     * @return
     */
    public final ReadIntegerArrayEs getReward(){
        return reward;
    }
    /**
     * LevelMin_LevelMax
     */
    private final ReadIntegerArray worldLevel;
    /**
     * LevelMin_LevelMax
     * @return
     */
    public final ReadIntegerArray getWorldLevel(){
        return worldLevel;
    }
    /**
     * 掉落概率，总和为1
1=100000
     */
    private final int probability;
    /**
     * 掉落概率，总和为1
1=100000
     * @return
     */
    public final int getProbability(){
        return probability;
    }
    /**
     * 世界等级限制
     */
    private final ReadIntegerArray worldLevelLimit;
    /**
     * 世界等级限制
     * @return
     */
    public final ReadIntegerArray getWorldLevelLimit(){
        return worldLevelLimit;
    }
    /**
     * type=2中达到设定次数从中随机得概率1=100000
     */
    private final int probabilityFreq;
    /**
     * type=2中达到设定次数从中随机得概率1=100000
     * @return
     */
    public final int getProbabilityFreq(){
        return probabilityFreq;
    }
    /**
     * 控制该玩家每天获取上限
0为无限制
>0为具体次数
     */
    private final int dailyControl;
    /**
     * 控制该玩家每天获取上限
0为无限制
>0为具体次数
     * @return
     */
    public final int getDailyControl(){
        return dailyControl;
    }
    /**
     * 控制该服务器每天产出上限
0为无限制
>0为具体次数
     */
    private final int serverControl;
    /**
     * 控制该服务器每天产出上限
0为无限制
>0为具体次数
     * @return
     */
    public final int getServerControl(){
        return serverControl;
    }
    /**
     * 公告类型（10跑马灯）
     */
    private final int ifRadio;
    /**
     * 公告类型（10跑马灯）
     * @return
     */
    public final int getIfRadio(){
        return ifRadio;
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

    public Cfg_Treasure_Hunt_Bean(int id,int round,int rewardType,int isShow,int type,String rewardStr,String worldLevelStr,int probability,String worldLevelLimitStr,int probabilityFreq,int dailyControl,int serverControl,int ifRadio,String chatchannelStr){
        this.id = id;
        this.round = round;
        this.rewardType = rewardType;
        this.isShow = isShow;
        this.type = type;
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
        this.worldLevel = new ReadIntegerArray(worldLevelStr,",");
        this.probability = probability;
        this.worldLevelLimit = new ReadIntegerArray(worldLevelLimitStr,",");
        this.probabilityFreq = probabilityFreq;
        this.dailyControl = dailyControl;
        this.serverControl = serverControl;
        this.ifRadio = ifRadio;
        this.chatchannel = new ReadIntegerArray(chatchannelStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("round:").append(round).append(";");
        str.append("rewardType:").append(rewardType).append(";");
        str.append("isShow:").append(isShow).append(";");
        str.append("type:").append(type).append(";");
        str.append("reward:").append(reward).append(";");
        str.append("worldLevel:").append(worldLevel).append(";");
        str.append("probability:").append(probability).append(";");
        str.append("worldLevelLimit:").append(worldLevelLimit).append(";");
        str.append("probabilityFreq:").append(probabilityFreq).append(";");
        str.append("dailyControl:").append(dailyControl).append(";");
        str.append("serverControl:").append(serverControl).append(";");
        str.append("ifRadio:").append(ifRadio).append(";");
        str.append("chatchannel:").append(chatchannel).append(";");
        return str.toString();
    }
}
