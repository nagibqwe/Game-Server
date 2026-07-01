/**
 * Auto generated, do not edit it
 *
 * SoulBeastsEquipLevel配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_SoulBeastsEquipLevel_Bean{
    /**
     * ID
     */
    private final int id;
    /**
     * ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 装备部位(1头盔、2项圈、3铠甲、4利爪、5羽翼)
     */
    private final int part;
    /**
     * 装备部位(1头盔、2项圈、3铠甲、4利爪、5羽翼)
     * @return
     */
    public final int getPart(){
        return part;
    }
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
     * 强化本级属性(@;@_@)此处为装备本级属性，而不是成长属性，界面显示成长需要下一级减去当前级
     */
    private final ReadIntegerArrayEs att;
    /**
     * 强化本级属性(@;@_@)此处为装备本级属性，而不是成长属性，界面显示成长需要下一级减去当前级
     * @return
     */
    public final ReadIntegerArrayEs getAtt(){
        return att;
    }
    /**
     * 本级升级所需总经验
     */
    private final int needExp;
    /**
     * 本级升级所需总经验
     * @return
     */
    public final int getNeedExp(){
        return needExp;
    }

    public Cfg_SoulBeastsEquipLevel_Bean(int id,int part,int level,String attStr,int needExp){
        this.id = id;
        this.part = part;
        this.level = level;
        this.att = new ReadIntegerArrayEs(attStr,"}",",");
        this.needExp = needExp;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("part:").append(part).append(";");
        str.append("level:").append(level).append(";");
        str.append("att:").append(att).append(";");
        str.append("needExp:").append(needExp).append(";");
        return str.toString();
    }
}
