/**
 * Auto generated, do not edit it
 *
 * Leader_Preach配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Leader_Preach_Bean{
    /**
     * ID（等级；1，初级；2，中级；3，高级；4，特级）
     */
    private final int id;
    /**
     * ID（等级；1，初级；2，中级；3，高级；4，特级）
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 阵法名字
     */
    private final String name;
    /**
     * 阵法名字
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * Ui上的资源配置
     */
    private final String UI_res;
    /**
     * Ui上的资源配置
     * @return
     */
    public final String getUI_res(){
        return UI_res;
    }
    /**
     * 场景中的资源BUFF ID配置
     */
    private final int res;
    /**
     * 场景中的资源BUFF ID配置
     * @return
     */
    public final int getRes(){
        return res;
    }
    /**
     * 星级（分为1-10，每一个代表半颗星）
     */
    private final int star;
    /**
     * 星级（分为1-10，每一个代表半颗星）
     * @return
     */
    public final int getStar(){
        return star;
    }
    /**
     * 特殊掉落（X次获得经验时获得一次Y掉落，每天最多Z次；X_Y_Z)client ignore
     */
    private final ReadIntegerArray special_drop;
    /**
     * 特殊掉落（X次获得经验时获得一次Y掉落，每天最多Z次；X_Y_Z)client ignore
     * @return
     */
    public final ReadIntegerArray getSpecial_drop(){
        return special_drop;
    }

    public Cfg_Leader_Preach_Bean(int id,String name,String UI_res,int res,int star,String special_dropStr){
        this.id = id;
        this.name = name;
        this.UI_res = UI_res;
        this.res = res;
        this.star = star;
        this.special_drop = new ReadIntegerArray(special_dropStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("UI_res:").append(UI_res).append(";");
        str.append("res:").append(res).append(";");
        str.append("star:").append(star).append(";");
        str.append("special_drop:").append(special_drop).append(";");
        return str.toString();
    }
}
