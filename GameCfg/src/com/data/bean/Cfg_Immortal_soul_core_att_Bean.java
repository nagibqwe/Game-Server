/**
 * Auto generated, do not edit it
 *
 * immortal_soul_core_att配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Immortal_soul_core_att_Bean{
    /**
     * ID（核心ID*10000+核心等级）
     */
    private final int ID;
    /**
     * ID（核心ID*10000+核心等级）
     * @return
     */
    public final int getID(){
        return ID;
    }
    /**
     * 核心ID
     */
    private final int core_id;
    /**
     * 核心ID
     * @return
     */
    public final int getCore_id(){
        return core_id;
    }
    /**
     * 核心等级
     */
    private final int level;
    /**
     * 核心等级
     * @return
     */
    public final int getLevel(){
        return level;
    }
    /**
     * 增加的属性
     */
    private final ReadIntegerArrayEs add_att;
    /**
     * 增加的属性
     * @return
     */
    public final ReadIntegerArrayEs getAdd_att(){
        return add_att;
    }
    /**
     * 需要的当前灵魄的总等级
     */
    private final int all_level;
    /**
     * 需要的当前灵魄的总等级
     * @return
     */
    public final int getAll_level(){
        return all_level;
    }
    /**
     * 下一个等级所需得总等级
     */
    private final int next_level;
    /**
     * 下一个等级所需得总等级
     * @return
     */
    public final int getNext_level(){
        return next_level;
    }

    public Cfg_Immortal_soul_core_att_Bean(int ID,int core_id,int level,String add_attStr,int all_level,int next_level){
        this.ID = ID;
        this.core_id = core_id;
        this.level = level;
        this.add_att = new ReadIntegerArrayEs(add_attStr,"}",",");
        this.all_level = all_level;
        this.next_level = next_level;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("ID:").append(ID).append(";");
        str.append("core_id:").append(core_id).append(";");
        str.append("level:").append(level).append(";");
        str.append("add_att:").append(add_att).append(";");
        str.append("all_level:").append(all_level).append(";");
        str.append("next_level:").append(next_level).append(";");
        return str.toString();
    }
}
