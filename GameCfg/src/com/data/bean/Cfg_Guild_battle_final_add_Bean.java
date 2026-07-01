/**
 * Auto generated, do not edit it
 *
 * guild_battle_final_add配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Guild_battle_final_add_Bean{
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
     * 加成类型（0，连胜；1，连败）
     */
    private final int type;
    /**
     * 加成类型（0，连胜；1，连败）
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 连胜连败次数
     */
    private final int num;
    /**
     * 连胜连败次数
     * @return
     */
    public final int getNum(){
        return num;
    }
    /**
     * 外部显示的加成百分比
     */
    private final ReadIntegerArrayEs addons;
    /**
     * 外部显示的加成百分比
     * @return
     */
    public final ReadIntegerArrayEs getAddons(){
        return addons;
    }
    /**
     * 点开显示的加成内容
     */
    private final String des;
    /**
     * 点开显示的加成内容
     * @return
     */
    public final String getDes(){
        return des;
    }
    /**
     * 额外积分
     */
    private final int score;
    /**
     * 额外积分
     * @return
     */
    public final int getScore(){
        return score;
    }
    /**
     * 增加BUFFID
     */
    private final int buff;
    /**
     * 增加BUFFID
     * @return
     */
    public final int getBuff(){
        return buff;
    }
    /**
     * 调用的特殊连胜资源编号
     */
    private final int special_tex;
    /**
     * 调用的特殊连胜资源编号
     * @return
     */
    public final int getSpecial_tex(){
        return special_tex;
    }
    /**
     * 增加连胜次数调用
     */
    private final int special_add_num;
    /**
     * 增加连胜次数调用
     * @return
     */
    public final int getSpecial_add_num(){
        return special_add_num;
    }

    public Cfg_Guild_battle_final_add_Bean(int id,int type,int num,String addonsStr,String des,int score,int buff,int special_tex,int special_add_num){
        this.id = id;
        this.type = type;
        this.num = num;
        this.addons = new ReadIntegerArrayEs(addonsStr,"}",",");
        this.des = des;
        this.score = score;
        this.buff = buff;
        this.special_tex = special_tex;
        this.special_add_num = special_add_num;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("type:").append(type).append(";");
        str.append("num:").append(num).append(";");
        str.append("addons:").append(addons).append(";");
        str.append("des:").append(des).append(";");
        str.append("score:").append(score).append(";");
        str.append("buff:").append(buff).append(";");
        str.append("special_tex:").append(special_tex).append(";");
        str.append("special_add_num:").append(special_add_num).append(";");
        return str.toString();
    }
}
