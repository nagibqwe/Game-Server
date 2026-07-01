/**
 * Auto generated, do not edit it
 *
 * monsterAi配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_MonsterAi_Bean{
    /**
     * aiid
     */
    private final int id;
    /**
     * aiid
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 触发条件(@;@_@)
     */
    private final ReadIntegerArrayEs reach;
    /**
     * 触发条件(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getReach(){
        return reach;
    }
    /**
     * 触发结果(@;@_@)
     */
    private final ReadIntegerArrayEs deal;
    /**
     * 触发结果(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getDeal(){
        return deal;
    }

    public Cfg_MonsterAi_Bean(int id,String reachStr,String dealStr){
        this.id = id;
        this.reach = new ReadIntegerArrayEs(reachStr,"}",",");
        this.deal = new ReadIntegerArrayEs(dealStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("reach:").append(reach).append(";");
        str.append("deal:").append(deal).append(";");
        return str.toString();
    }
}
