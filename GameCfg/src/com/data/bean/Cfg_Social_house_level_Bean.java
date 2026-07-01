/**
 * Auto generated, do not edit it
 *
 * social_house_level配置表
 */
package com.data.bean;

	
public class Cfg_Social_house_level_Bean{
    /**
     * ID等级
     */
    private final int id;
    /**
     * ID等级
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 升下一级需要的装饰度
     */
    private final int decorate_num;
    /**
     * 升下一级需要的装饰度
     * @return
     */
    public final int getDecorate_num(){
        return decorate_num;
    }
    /**
     * 每天能够领取的离线经验（对应等级的秒数）
     */
    private final int levelup_pay;
    /**
     * 每天能够领取的离线经验（对应等级的秒数）
     * @return
     */
    public final int getLevelup_pay(){
        return levelup_pay;
    }

    public Cfg_Social_house_level_Bean(int id,int decorate_num,int levelup_pay){
        this.id = id;
        this.decorate_num = decorate_num;
        this.levelup_pay = levelup_pay;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("decorate_num:").append(decorate_num).append(";");
        str.append("levelup_pay:").append(levelup_pay).append(";");
        return str.toString();
    }
}
