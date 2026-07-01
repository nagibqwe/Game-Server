/**
 * Auto generated, do not edit it
 *
 * invest配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Invest_Bean{
    /**
     * ID=gear*10000+level
     */
    private final int ID;
    /**
     * ID=gear*10000+level
     * @return
     */
    public final int getID(){
        return ID;
    }
    /**
     * 27、第一档
28、第二档
29、第三档
     */
    private final int gear;
    /**
     * 27、第一档
28、第二档
29、第三档
     * @return
     */
    public final int getGear(){
        return gear;
    }
    /**
     * 领取等级
0为购买立返
     */
    private final int level;
    /**
     * 领取等级
0为购买立返
     * @return
     */
    public final int getLevel(){
        return level;
    }
    /**
     * 返回数量_货币类型
奖励为一列一列取
     */
    private final ReadIntegerArray money;
    /**
     * 返回数量_货币类型
奖励为一列一列取
     * @return
     */
    public final ReadIntegerArray getMoney(){
        return money;
    }

    public Cfg_Invest_Bean(int ID,int gear,int level,String moneyStr){
        this.ID = ID;
        this.gear = gear;
        this.level = level;
        this.money = new ReadIntegerArray(moneyStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("ID:").append(ID).append(";");
        str.append("gear:").append(gear).append(";");
        str.append("level:").append(level).append(";");
        str.append("money:").append(money).append(";");
        return str.toString();
    }
}
