/**
 * Auto generated, do not edit it
 *
 * item_warning配置表
 */
package com.data.bean;

	
public class Cfg_Item_warning_Bean{
    /**
     * 物品Id(负数为属性枚举）
     */
    private final int itemId;
    /**
     * 物品Id(负数为属性枚举）
     * @return
     */
    public final int getItemId(){
        return itemId;
    }
    /**
     * 全服产出预警值
     */
    private final long serverLimit;
    /**
     * 全服产出预警值
     * @return
     */
    public final long getServerLimit(){
        return serverLimit;
    }
    /**
     * 单个玩家产出预警值
     */
    private final long playerLimit;
    /**
     * 单个玩家产出预警值
     * @return
     */
    public final long getPlayerLimit(){
        return playerLimit;
    }

    public Cfg_Item_warning_Bean(int itemId,long serverLimit,long playerLimit){
        this.itemId = itemId;
        this.serverLimit = serverLimit;
        this.playerLimit = playerLimit;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("itemId:").append(itemId).append(";");
        str.append("serverLimit:").append(serverLimit).append(";");
        str.append("playerLimit:").append(playerLimit).append(";");
        return str.toString();
    }
}
