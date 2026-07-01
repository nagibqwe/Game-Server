/**
 * Auto generated, do not edit it
 *
 * vipPower配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_VipPower_Bean{
    /**
     * vip特权项，用于vip表直接调用ID（ID一定不能够修改，删除，因为大部分是程序写死处理的）
     */
    private final int vipPower;
    /**
     * vip特权项，用于vip表直接调用ID（ID一定不能够修改，删除，因为大部分是程序写死处理的）
     * @return
     */
    public final int getVipPower(){
        return vipPower;
    }
    /**
     * 可购买次数特权对应的价格
     */
    private final ReadIntegerArray vipPowerPrice;
    /**
     * 可购买次数特权对应的价格
     * @return
     */
    public final ReadIntegerArray getVipPowerPrice(){
        return vipPowerPrice;
    }

    public Cfg_VipPower_Bean(int vipPower,String vipPowerPriceStr){
        this.vipPower = vipPower;
        this.vipPowerPrice = new ReadIntegerArray(vipPowerPriceStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("vipPower:").append(vipPower).append(";");
        str.append("vipPowerPrice:").append(vipPowerPrice).append(";");
        return str.toString();
    }
}
