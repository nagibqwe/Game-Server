/**
 * Auto generated, do not edit it
 *
 * redPacket配置表
 */
package com.data.bean;

	
public class Cfg_RedPacket_Bean{
    /**
     * 红包ID
     */
    private final int id;
    /**
     * 红包ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 充值类型 1 首冲 2 累充 3 每日首冲 4 每日累充 5 自动发
     */
    private final int type;
    /**
     * 充值类型 1 首冲 2 累充 3 每日首冲 4 每日累充 5 自动发
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 红包名字
     */
    private final String name;
    /**
     * 红包名字
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 充值金额
     */
    private final int value;
    /**
     * 充值金额
     * @return
     */
    public final int getValue(){
        return value;
    }
    /**
     * 红包金额
     */
    private final int num;
    /**
     * 红包金额
     * @return
     */
    public final int getNum(){
        return num;
    }

    public Cfg_RedPacket_Bean(int id,int type,String name,int value,int num){
        this.id = id;
        this.type = type;
        this.name = name;
        this.value = value;
        this.num = num;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("type:").append(type).append(";");
        str.append("name:").append(name).append(";");
        str.append("value:").append(value).append(";");
        str.append("num:").append(num).append(";");
        return str.toString();
    }
}
