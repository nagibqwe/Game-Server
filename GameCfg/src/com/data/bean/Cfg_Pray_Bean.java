/**
 * Auto generated, do not edit it
 *
 * pray配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Pray_Bean{
    /**
     * 等级
     */
    private final int level;
    /**
     * 等级
     * @return
     */
    public final int getLevel(){
        return level;
    }
    /**
     * 银元宝祈祷
num_货币类型
     */
    private final ReadIntegerArray money;
    /**
     * 银元宝祈祷
num_货币类型
     * @return
     */
    public final ReadIntegerArray getMoney(){
        return money;
    }
    /**
     * 元宝祈祷经验
     */
    private final long exp;
    /**
     * 元宝祈祷经验
     * @return
     */
    public final long getExp(){
        return exp;
    }

    public Cfg_Pray_Bean(int level,String moneyStr,long exp){
        this.level = level;
        this.money = new ReadIntegerArray(moneyStr,",");
        this.exp = exp;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("level:").append(level).append(";");
        str.append("money:").append(money).append(";");
        str.append("exp:").append(exp).append(";");
        return str.toString();
    }
}
