/**
 * Auto generated, do not edit it
 *
 * invest_Level配置表
 */
package com.data.bean;

	
public class Cfg_Invest_Level_Bean{
    /**
     * 成长基金投资档次
     */
    private final int investLevel;
    /**
     * 成长基金投资档次
     * @return
     */
    public final int getInvestLevel(){
        return investLevel;
    }
    /**
     * 货币类型1元宝、2绑元
     */
    private final int moneyType;
    /**
     * 货币类型1元宝、2绑元
     * @return
     */
    public final int getMoneyType(){
        return moneyType;
    }
    /**
     * 所需数量（程序会除以10显示）
     */
    private final int diamond;
    /**
     * 所需数量（程序会除以10显示）
     * @return
     */
    public final int getDiamond(){
        return diamond;
    }
    /**
     * 1开，0关（只能开一档）
     */
    private final int ifOpen;
    /**
     * 1开，0关（只能开一档）
     * @return
     */
    public final int getIfOpen(){
        return ifOpen;
    }
    /**
     * 描述（废弃）
     */
    private final String desc;
    /**
     * 描述（废弃）
     * @return
     */
    public final String getDesc(){
        return desc;
    }

    public Cfg_Invest_Level_Bean(int investLevel,int moneyType,int diamond,int ifOpen,String desc){
        this.investLevel = investLevel;
        this.moneyType = moneyType;
        this.diamond = diamond;
        this.ifOpen = ifOpen;
        this.desc = desc;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("investLevel:").append(investLevel).append(";");
        str.append("moneyType:").append(moneyType).append(";");
        str.append("diamond:").append(diamond).append(";");
        str.append("ifOpen:").append(ifOpen).append(";");
        str.append("desc:").append(desc).append(";");
        return str.toString();
    }
}
