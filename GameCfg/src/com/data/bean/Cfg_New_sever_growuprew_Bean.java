/**
 * Auto generated, do not edit it
 *
 * new_sever_growuprew配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_New_sever_growuprew_Bean{
    /**
     * key用于标识
     */
    private final int id;
    /**
     * key用于标识
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     *  表示积分数量
     */
    private final int scroe;
    /**
     *  表示积分数量
     * @return
     */
    public final int getScroe(){
        return scroe;
    }
    /**
     * 奖励
item_num_bind_occ
bind:0未绑定，1绑定
occ：0男1女9通用
     */
    private final ReadIntegerArrayEs item;
    /**
     * 奖励
item_num_bind_occ
bind:0未绑定，1绑定
occ：0男1女9通用
     * @return
     */
    public final ReadIntegerArrayEs getItem(){
        return item;
    }

    public Cfg_New_sever_growuprew_Bean(int id,int scroe,String itemStr){
        this.id = id;
        this.scroe = scroe;
        this.item = new ReadIntegerArrayEs(itemStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("scroe:").append(scroe).append(";");
        str.append("item:").append(item).append(";");
        return str.toString();
    }
}
