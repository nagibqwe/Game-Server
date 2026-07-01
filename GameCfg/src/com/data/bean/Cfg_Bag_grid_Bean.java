/**
 * Auto generated, do not edit it
 *
 * bag_grid配置表
 */
package com.data.bean;

	
public class Cfg_Bag_grid_Bean{
    /**
     * 格子索引
     */
    private final int bag_grid;
    /**
     * 格子索引
     * @return
     */
    public final int getBag_grid(){
        return bag_grid;
    }
    /**
     * 所属系统（1 背包 3 仓库）
     */
    private final int bag;
    /**
     * 所属系统（1 背包 3 仓库）
     * @return
     */
    public final int getBag(){
        return bag;
    }
    /**
     * 格子编号
     */
    private final int grid;
    /**
     * 格子编号
     * @return
     */
    public final int getGrid(){
        return grid;
    }
    /**
     * 开启时间/秒
     */
    private final long time;
    /**
     * 开启时间/秒
     * @return
     */
    public final long getTime(){
        return time;
    }
    /**
     * 元宝费用
     */
    private final long cost;
    /**
     * 元宝费用
     * @return
     */
    public final long getCost(){
        return cost;
    }

    public Cfg_Bag_grid_Bean(int bag_grid,int bag,int grid,long time,long cost){
        this.bag_grid = bag_grid;
        this.bag = bag;
        this.grid = grid;
        this.time = time;
        this.cost = cost;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("bag_grid:").append(bag_grid).append(";");
        str.append("bag:").append(bag).append(";");
        str.append("grid:").append(grid).append(";");
        str.append("time:").append(time).append(";");
        str.append("cost:").append(cost).append(";");
        return str.toString();
    }
}
