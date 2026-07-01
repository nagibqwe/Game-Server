/**
 * Auto generated, do not edit it
 *
 * sdkplatform配置表
 */
package com.data.bean;

	
public class Cfg_Sdkplatform_Bean{
    /**
     * 研发自定义渠道ID
     */
    private final int id;
    /**
     * 研发自定义渠道ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 货币地区
     */
    private final String moneyCode;
    /**
     * 货币地区
     * @return
     */
    public final String getMoneyCode(){
        return moneyCode;
    }
    /**
     * 渠道
     */
    private final String chanel;
    /**
     * 渠道
     * @return
     */
    public final String getChanel(){
        return chanel;
    }
    /**
     * 使用的货币符号
     */
    private final String money_sign;
    /**
     * 使用的货币符号
     * @return
     */
    public final String getMoney_sign(){
        return money_sign;
    }
    /**
     * IP地区代码
     */
    private final int region;
    /**
     * IP地区代码
     * @return
     */
    public final int getRegion(){
        return region;
    }
    /**
     * 备注(client ignore)
     */
    private final String des;
    /**
     * 备注(client ignore)
     * @return
     */
    public final String getDes(){
        return des;
    }

    public Cfg_Sdkplatform_Bean(int id,String moneyCode,String chanel,String money_sign,int region,String des){
        this.id = id;
        this.moneyCode = moneyCode;
        this.chanel = chanel;
        this.money_sign = money_sign;
        this.region = region;
        this.des = des;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("moneyCode:").append(moneyCode).append(";");
        str.append("chanel:").append(chanel).append(";");
        str.append("money_sign:").append(money_sign).append(";");
        str.append("region:").append(region).append(";");
        str.append("des:").append(des).append(";");
        return str.toString();
    }
}
