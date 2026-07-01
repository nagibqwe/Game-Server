/**
 * Auto generated, do not edit it
 *
 * guild_war_closereward配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Guild_war_closereward_Bean{
    /**
     * 编号
     */
    private final int id;
    /**
     * 编号
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 对应的仙盟类型
对应guild_war_rank表的主键
1天仙仙盟
2神仙仙盟
3金仙仙盟
4鬼仙仙盟
     */
    private final int type;
    /**
     * 对应的仙盟类型
对应guild_war_rank表的主键
1天仙仙盟
2神仙仙盟
3金仙仙盟
4鬼仙仙盟
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 世界等级区间
     */
    private final ReadIntegerArray worldLevel;
    /**
     * 世界等级区间
     * @return
     */
    public final ReadIntegerArray getWorldLevel(){
        return worldLevel;
    }
    /**
     * 第一名仙盟掉落组奖励
     */
    private final ReadIntegerArray guildDropGoup1;
    /**
     * 第一名仙盟掉落组奖励
     * @return
     */
    public final ReadIntegerArray getGuildDropGoup1(){
        return guildDropGoup1;
    }
    /**
     * 第二名仙盟掉落组奖励
     */
    private final ReadIntegerArray guildDropGoup2;
    /**
     * 第二名仙盟掉落组奖励
     * @return
     */
    public final ReadIntegerArray getGuildDropGoup2(){
        return guildDropGoup2;
    }
    /**
     * 第三名仙盟掉落组奖励
     */
    private final ReadIntegerArray guildDropGoup3;
    /**
     * 第三名仙盟掉落组奖励
     * @return
     */
    public final ReadIntegerArray getGuildDropGoup3(){
        return guildDropGoup3;
    }

    public Cfg_Guild_war_closereward_Bean(int id,int type,String worldLevelStr,String guildDropGoup1Str,String guildDropGoup2Str,String guildDropGoup3Str){
        this.id = id;
        this.type = type;
        this.worldLevel = new ReadIntegerArray(worldLevelStr,",");
        this.guildDropGoup1 = new ReadIntegerArray(guildDropGoup1Str,",");
        this.guildDropGoup2 = new ReadIntegerArray(guildDropGoup2Str,",");
        this.guildDropGoup3 = new ReadIntegerArray(guildDropGoup3Str,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("type:").append(type).append(";");
        str.append("worldLevel:").append(worldLevel).append(";");
        str.append("guildDropGoup1:").append(guildDropGoup1).append(";");
        str.append("guildDropGoup2:").append(guildDropGoup2).append(";");
        str.append("guildDropGoup3:").append(guildDropGoup3).append(";");
        return str.toString();
    }
}
