/**
 * Auto generated, do not edit it
 *
 * bossHome配置表
 */
package com.data.bean;

	
public class Cfg_BossHome_Bean{
    /**
     * 编号ID，和monster表id一致
     */
    private final int ID;
    /**
     * 编号ID，和monster表id一致
     * @return
     */
    public final int getID(){
        return ID;
    }
    /**
     * 怪物名称 client ignore
     */
    private final String monster_name;
    /**
     * 怪物名称 client ignore
     * @return
     */
    public final String getMonster_name(){
        return monster_name;
    }
    /**
     * 是否在列表中显示(0否1是)
     */
    private final int canShow;
    /**
     * 是否在列表中显示(0否1是)
     * @return
     */
    public final int getCanShow(){
        return canShow;
    }
    /**
     * 怪物头像
     */
    private final int head_icon;
    /**
     * 怪物头像
     * @return
     */
    public final int getHead_icon(){
        return head_icon;
    }
    /**
     * boss等级
     */
    private final int bossLevel;
    /**
     * boss等级
     * @return
     */
    public final int getBossLevel(){
        return bossLevel;
    }
    /**
     * 推荐战力
     */
    private final int power;
    /**
     * 推荐战力
     * @return
     */
    public final int getPower(){
        return power;
    }
    /**
     * 掉落装备阶数
     */
    private final int dropEquipShow;
    /**
     * 掉落装备阶数
     * @return
     */
    public final int getDropEquipShow(){
        return dropEquipShow;
    }
    /**
     * 刷新副本地图
     */
    private final int clone_map;
    /**
     * 刷新副本地图
     * @return
     */
    public final int getClone_map(){
        return clone_map;
    }

    public Cfg_BossHome_Bean(int ID,String monster_name,int canShow,int head_icon,int bossLevel,int power,int dropEquipShow,int clone_map){
        this.ID = ID;
        this.monster_name = monster_name;
        this.canShow = canShow;
        this.head_icon = head_icon;
        this.bossLevel = bossLevel;
        this.power = power;
        this.dropEquipShow = dropEquipShow;
        this.clone_map = clone_map;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("ID:").append(ID).append(";");
        str.append("monster_name:").append(monster_name).append(";");
        str.append("canShow:").append(canShow).append(";");
        str.append("head_icon:").append(head_icon).append(";");
        str.append("bossLevel:").append(bossLevel).append(";");
        str.append("power:").append(power).append(";");
        str.append("dropEquipShow:").append(dropEquipShow).append(";");
        str.append("clone_map:").append(clone_map).append(";");
        return str.toString();
    }
}
