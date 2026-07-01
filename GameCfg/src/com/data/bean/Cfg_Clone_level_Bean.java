/**
 * Auto generated, do not edit it
 *
 * clone_level配置表
 */
package com.data.bean;

import com.data.struct.ReadLongArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Clone_level_Bean{
    /**
     * 编号（副本ID*100+难度）
     */
    private final int id;
    /**
     * 编号（副本ID*100+难度）
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 副本ID
     */
    private final int clonemap_id;
    /**
     * 副本ID
     * @return
     */
    public final int getClonemap_id(){
        return clonemap_id;
    }
    /**
     * 副本的难度等级
     */
    private final int clone_level;
    /**
     * 副本的难度等级
     * @return
     */
    public final int getClone_level(){
        return clone_level;
    }
    /**
     * 副本的标题
     */
    private final String describe;
    /**
     * 副本的标题
     * @return
     */
    public final String getDescribe(){
        return describe;
    }
    /**
     * 进入所需最小等级
     */
    private final int min_lv;
    /**
     * 进入所需最小等级
     * @return
     */
    public final int getMin_lv(){
        return min_lv;
    }
    /**
     * 最高等级进入限制
     */
    private final int max_lv;
    /**
     * 最高等级进入限制
     * @return
     */
    public final int getMax_lv(){
        return max_lv;
    }
    /**
     * 副本成功奖励：物品ID_数量;物品ID_数量[@;@_@] （client ignore）
     */
    private final ReadLongArrayEs success_reward;
    /**
     * 副本成功奖励：物品ID_数量;物品ID_数量[@;@_@] （client ignore）
     * @return
     */
    public final ReadLongArrayEs getSuccess_reward(){
        return success_reward;
    }
    /**
     * 副本失败奖励：物品ID_数量;物品ID_数量[@;@_@]（client ignore）
     */
    private final ReadLongArrayEs fail_reward;
    /**
     * 副本失败奖励：物品ID_数量;物品ID_数量[@;@_@]（client ignore）
     * @return
     */
    public final ReadLongArrayEs getFail_reward(){
        return fail_reward;
    }
    /**
     * 副本特殊奖励【积分】：物品ID_数量_参数;物品ID_数量_参数[@;@_@]
     */
    private final ReadLongArrayEs extra_reward;
    /**
     * 副本特殊奖励【积分】：物品ID_数量_参数;物品ID_数量_参数[@;@_@]
     * @return
     */
    public final ReadLongArrayEs getExtra_reward(){
        return extra_reward;
    }
    /**
     * 刷怪索引（client ignore）
     */
    private final ReadIntegerArray clonemonster_id;
    /**
     * 刷怪索引（client ignore）
     * @return
     */
    public final ReadIntegerArray getClonemonster_id(){
        return clonemonster_id;
    }

    public Cfg_Clone_level_Bean(int id,int clonemap_id,int clone_level,String describe,int min_lv,int max_lv,String success_rewardStr,String fail_rewardStr,String extra_rewardStr,String clonemonster_idStr){
        this.id = id;
        this.clonemap_id = clonemap_id;
        this.clone_level = clone_level;
        this.describe = describe;
        this.min_lv = min_lv;
        this.max_lv = max_lv;
        this.success_reward = new ReadLongArrayEs(success_rewardStr,"}",",");
        this.fail_reward = new ReadLongArrayEs(fail_rewardStr,"}",",");
        this.extra_reward = new ReadLongArrayEs(extra_rewardStr,"}",",");
        this.clonemonster_id = new ReadIntegerArray(clonemonster_idStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("clonemap_id:").append(clonemap_id).append(";");
        str.append("clone_level:").append(clone_level).append(";");
        str.append("describe:").append(describe).append(";");
        str.append("min_lv:").append(min_lv).append(";");
        str.append("max_lv:").append(max_lv).append(";");
        str.append("success_reward:").append(success_reward).append(";");
        str.append("fail_reward:").append(fail_reward).append(";");
        str.append("extra_reward:").append(extra_reward).append(";");
        str.append("clonemonster_id:").append(clonemonster_id).append(";");
        return str.toString();
    }
}
