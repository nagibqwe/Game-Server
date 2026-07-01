/**
 * Auto generated, do not edit it
 *
 * guild_title配置表
 */
package com.data.bean;

	
public class Cfg_Guild_title_Bean{
    /**
     * key=group*100+rankmix
     */
    private final int id;
    /**
     * key=group*100+rankmix
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 表示拥有领地的宗派排名。1表示1名，2表示2名，3表示3名。每日11点决定
     */
    private final int group;
    /**
     * 表示拥有领地的宗派排名。1表示1名，2表示2名，3表示3名。每日11点决定
     * @return
     */
    public final int getGroup(){
        return group;
    }
    /**
     * 领地的名字
     */
    private final String name;
    /**
     * 领地的名字
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 名次多少开始
     */
    private final int rankmix;
    /**
     * 名次多少开始
     * @return
     */
    public final int getRankmix(){
        return rankmix;
    }
    /**
     * 名次多少结束
     */
    private final int rankmax;
    /**
     * 名次多少结束
     * @return
     */
    public final int getRankmax(){
        return rankmax;
    }
    /**
     * 获得的显限时称号,对应title表中的ID
     */
    private final int title;
    /**
     * 获得的显限时称号,对应title表中的ID
     * @return
     */
    public final int getTitle(){
        return title;
    }
    /**
     * 获得的称号永久
     */
    private final int title_permanent;
    /**
     * 获得的称号永久
     * @return
     */
    public final int getTitle_permanent(){
        return title_permanent;
    }
    /**
     * 称号增加的战力，用于前端读取显示
     */
    private final int title_fighting;
    /**
     * 称号增加的战力，用于前端读取显示
     * @return
     */
    public final int getTitle_fighting(){
        return title_fighting;
    }

    public Cfg_Guild_title_Bean(int id,int group,String name,int rankmix,int rankmax,int title,int title_permanent,int title_fighting){
        this.id = id;
        this.group = group;
        this.name = name;
        this.rankmix = rankmix;
        this.rankmax = rankmax;
        this.title = title;
        this.title_permanent = title_permanent;
        this.title_fighting = title_fighting;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("group:").append(group).append(";");
        str.append("name:").append(name).append(";");
        str.append("rankmix:").append(rankmix).append(";");
        str.append("rankmax:").append(rankmax).append(";");
        str.append("title:").append(title).append(";");
        str.append("title_permanent:").append(title_permanent).append(";");
        str.append("title_fighting:").append(title_fighting).append(";");
        return str.toString();
    }
}
