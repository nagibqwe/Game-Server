/**
 * Auto generated, do not edit it
 *
 * guild_war_contkill配置表
 */
package com.data.bean;

	
public class Cfg_Guild_war_contkill_Bean{
    /**
     * ID
     */
    private final int id;
    /**
     * ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 类型
1击杀
2终结（只能有一条数据）
     */
    private final int type;
    /**
     * 类型
1击杀
2终结（只能有一条数据）
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * type=1才有击杀人数，终结没有人数
type=2的时候，表示最低杀几个人才会有终结显示
     */
    private final int count;
    /**
     * type=1才有击杀人数，终结没有人数
type=2的时候，表示最低杀几个人才会有终结显示
     * @return
     */
    public final int getCount(){
        return count;
    }

    public Cfg_Guild_war_contkill_Bean(int id,int type,int count){
        this.id = id;
        this.type = type;
        this.count = count;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("type:").append(type).append(";");
        str.append("count:").append(count).append(";");
        return str.toString();
    }
}
